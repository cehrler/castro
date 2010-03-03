package DataModulePackage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class DataModule {

	//sample use:
	//		DataModule.Init();
	//	    Graph g = DataModule.getGraph("1970-01-01", "1980-01-01", "NULL", "NULL", "interview");
	//		If you don't want to filter acc. to some parameter, just enter "NULL" value
	//      not java NULL but string "NULL"
	//
	//      So far - only Graph.nodes properly initialized
	//      Dates are stored as String, we avoid conversion errors,
	//		Dates in YYYY-MM-DD can be easily comared - so this representation fils our needs 
	
	private static Connection connection;
	
	private static String connHost = "localhost";
	private static String connDB = "castro_db";
	private static String connUser = "root";
	private static String connPasswd = "root";
	
	private static String DataDir = "../DataModuleData/";
	
	//In the future this will be extended, now I use only 1 distance matrix
	private static String distMatFile = DataDir + "PERSONS.tfidf.dist";
	
	private static List<List<Double> > distMat;
	
	private static Integer NumDocs = 0;
	
	private static SimMatrix simMatrix;
	private static String simMatFile = "../DataModuleData/PERSONS.sim.bin";
	
	public static void Init()
	{
		simMatrix = SimMatrixElem.LoadFromFile(simMatFile);
		
		connection = MySqlConnectionProvider.getNewConnection(connHost, connDB, connUser, connPasswd);
		
	}
	
	
	
	public static Graph getGraph(String SinceDate, String TillDate, String Place, String Author, String DocType, Double similarity_threshold)
	{
		if (SinceDate != "NULL") SinceDate = "\"" + SinceDate + "\"";
		if (TillDate != "NULL") TillDate = "\"" + TillDate + "\"";
		if (Place != "NULL") Place = "\"" + Place + "\"";
		if (Author != "NULL") Author = "\"" + Author + "\"";
		if (DocType != "NULL") DocType = "\"" + DocType + "\"";

		List<Node> ln = new ArrayList<Node>();
		//Map<Integer, Node> idToNode = new HashMap<Integer, Node>();
		List<Edge> le = new ArrayList<Edge>();
		try {
			java.sql.Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);	
			System.err.println("CALL getNodes(" + SinceDate + ", " + TillDate + ", " + Place + ", " + Author + ", " + DocType + ");");
			ResultSet srs = stmt.executeQuery("CALL getNodes(" + SinceDate + ", " + TillDate + ", " + Place + ", " + Author + ", " + DocType + ");");
			
			while (srs.next()) 
			{
		        	Node nod = new Node(srs.getInt("SPEECH_ID"), srs.getString("AUTHOR_NAME"), 
		        			srs.getString("HEADLINE"), srs.getString("REPORT_DATE"), 
		        			srs.getString("SOURCE_NAME"), srs.getString("PLACE_NAME"), srs.getString("DOCTYPE_NAME"),
		        			"", srs.getString("SPEECH_DATE"));
		        	
		        	ln.add(nod);
		        	//idToNode.put(nod.getSpeech_id(), nod);
			}
			
			
			System.err.println(ln.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Map<Integer,Integer> id_aux = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < ln.size(); i++)
		{
			id_aux.put(ln.get(i).getSpeech_id(), 1);
		}
		
		Double similarity;
		
		for (int i = 0; i < ln.size(); i++)
		{
			//Node n = ln.get(i);
			for (int j = 0; j < ln.size(); j++)
			{
				//if (i == j) continue;
				similarity = simMatrix.getSimilarity(i, j);
				
				if (similarity.compareTo(0.1) > 0) System.err.println(similarity);
				
				if (similarity.compareTo(similarity_threshold) > 0)
				{
					Edge e = new Edge(ln.get(i), ln.get(j), similarity);
					le.add(e);
					ln.get(i).addEdge(ln.get(j), similarity);
				}
			}
			
		}
		
		System.err.println("Number of edges: " + le.size());
		
		return new Graph(ln, le);
		
	}
	
	
}
