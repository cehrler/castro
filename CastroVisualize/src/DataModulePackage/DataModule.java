//============================================================================
// Name        : DataModule.java
// Author      : Michal Richter, Michalisek
// Version     :
// Copyright   : This product is licensed under Fidel Castro restricted software license. 
//               Use of any kind is considered a breach of copyright law. 
//               You are not allowed to use this for any purpose; neither commercial 
//               nor non-commercial.
// Description : Graph data object is constructed from user query here - see getGraph function
//               Need to be initialized first by Init();
//============================================================================


package DataModulePackage;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
		
	//In the future this will be extended, now I use only 1 distance matrix
	
	
	private static SimMatrix simMatrix;
	private static String simMatFile = "../DataModuleData/PERSONS.tfidf.sim";
	
	private static Map<String, Integer> personsMap;
	private static Map<String, Integer> locationsMap;
	private static Map<String, Integer> organizationsMap;
	
	private static VMindex personsIndex;
	private static VMindex locationsIndex;
	private static VMindex organizationsIndex;
	
	private static String personsIndexFile = "../DataModuleData/PERSONS.tfidf.bin";
	private static String locationsIndexFile = "../DataModuleData/LOCATIONS.tfidf.bin";
	private static String organizationsIndexFile = "../DataModuleData/ORGANIZATIONS.tfidf.bin";
	
	private DataModule() {}
	
	public static void Init()
	{
		simMatrix = SimMatrixElem.LoadFromFile(simMatFile);
		
		personsIndex = new VMindex(personsIndexFile);
		locationsIndex = new VMindex(locationsIndexFile);
		organizationsIndex = new VMindex(organizationsIndexFile);
		
		connection = MySqlConnectionProvider.getNewConnection(connHost, connDB, connUser, connPasswd);

		personsMap = new HashMap<String, Integer>();
		locationsMap = new HashMap<String, Integer>();
		organizationsMap = new HashMap<String, Integer>();
		
		try {
			java.sql.Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);	
			System.err.println("CALL getNE();");
			ResultSet srs = stmt.executeQuery("CALL getNE();");
			
			String pomS;
			
			while (srs.next()) 
			{
					pomS = srs.getString("NE_TYPE");
					
					if (pomS.equals("ORGANIZATIONS"))
					{
						organizationsMap.put(srs.getString("NE_NAME"), srs.getInt("NE_INDEX_ID"));
					}
					else if (pomS.equals("PERSONS"))
					{
						personsMap.put(srs.getString("NE_NAME"), srs.getInt("NE_INDEX_ID"));
					}
					else if (pomS.equals("LOCATIONS"))
					{
						locationsMap.put(srs.getString("NE_NAME"), srs.getInt("NE_INDEX_ID"));
					}
									
			}
						
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	private static void similarityUpdate(List<Node> nodes, VMindex currIndex, Integer termCol, Double weight)
	{
		for (int i = 0; i < nodes.size(); i++)
		{
			nodes.get(i).SetRelevance(nodes.get(i).GetRelevance() + currIndex.GetValue(nodes.get(i).getSpeech_id(), termCol) * weight);
		}
	}
	
	private static List<Node> sortNodes(List<Node> nodes, List<String> queryTerms , List<Double> termWeights, Integer maxNumNodes)
	{
		if (queryTerms == null)
		{
			return nodes;
		}
				
		
		for (int i = 0; i < nodes.size(); i++)
		{
			nodes.get(i).SetRelevance(0.0);
		}
		
		
		for (int i = 0; i < queryTerms.size(); i++)
		{
			if (personsMap.containsKey(queryTerms.get(i)))
			{
				similarityUpdate(nodes, personsIndex, personsMap.get(queryTerms.get(i)), termWeights.get(i));
			}
			
			if (locationsMap.containsKey(queryTerms.get(i)))
			{
				similarityUpdate(nodes, locationsIndex, locationsMap.get(queryTerms.get(i)), termWeights.get(i));
				
			}

			if (organizationsMap.containsKey(queryTerms.get(i)))
			{
				similarityUpdate(nodes, organizationsIndex, organizationsMap.get(queryTerms.get(i)), termWeights.get(i));
				
			}
			
		}
		
		Collections.sort(nodes);
		
		List<Node> sn = new ArrayList<Node>();
		
		if (maxNumNodes == null) maxNumNodes = nodes.size();
		
		for (int i = nodes.size() - 1; i >= nodes.size() - maxNumNodes; i--)
		{
			sn.add(nodes.get(i));
		}
		
		return sn;
	}
	
	public static Graph getGraph(String SinceDate, String TillDate, String Place, String Author, String DocType, Double similarity_threshold, List<String> queryTerms , List<Double> termWeights, Integer maxNumNodes)
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
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Node> sn = sortNodes(ln, queryTerms, termWeights, maxNumNodes);
		
		Double similarity;
		
		for (int i = 0; i < sn.size(); i++)
		{
			//Node n = ln.get(i);
			for (int j = i + 1; j < sn.size(); j++)
			{
				//if (i == j) continue;
				similarity = simMatrix.getSimilarity(sn.get(i), sn.get(j));
								
				if (similarity.compareTo(similarity_threshold) > 0)
				{					
					Edge e = new Edge(sn.get(i), sn.get(j), similarity);
					le.add(e);
					sn.get(i).addEdge(sn.get(j), similarity);
					sn.get(j).addEdge(sn.get(i), similarity);
				}
			}
			
		}

		System.err.println("Number of nodes: " + sn.size());
		System.err.println("Number of edges: " + le.size());
		
		return new Graph(sn, le);
		
	}
	
	
}
