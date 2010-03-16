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


package Functionality;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

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
	
	
	private static SimMatrix smPersons;
	private static SimMatrix smLocations;
	private static SimMatrix smOrganizations;
	private static SimMatrix smAllWeightedEqually;
	private static SimMatrix smAllPersonsWeightedDouble;
	private static SimMatrix smAllLocationsWeightedDouble;
	private static SimMatrix smAllOrganizationsWeightedDouble;
	
	
	private static String smPersonsFile;
	private static String smLocationsFile;
	private static String smOrganizationsFile;
	
	
	private static Map<String, Integer> personsMap;
	private static Map<String, Integer> locationsMap;
	private static Map<String, Integer> organizationsMap;
	
	private static VMindex personsIndex;
	private static VMindex locationsIndex;
	private static VMindex organizationsIndex;
	
	private static String personsIndexFile;
	private static String locationsIndexFile;
	private static String organizationsIndexFile;
	
	private static double simEpsilon = 0.000001;
	
	public static Graph displayedGraph;
	
	private DataModule() {}
	
	
	public static void Init(IndexTypeEnum indexType)
	{
		switch (indexType)
		{
			case TF: personsIndexFile = "../DataModuleData/PERSONS.tf.bin";
					 locationsIndexFile = "../DataModuleData/LOCATIONS.tf.bin";
					 organizationsIndexFile = "../DataModuleData/ORGANIZATIONS.tf.bin";
					 smPersonsFile = "../DataModuleData/PERSONS.tf.sim";
					 smLocationsFile = "../DataModuleData/LOCATIONS.tf.sim";
					 smOrganizationsFile = "../DataModuleData/ORGANIZATIONS.tf.sim";
					 break;
			
			case TFIDF: personsIndexFile = "../DataModuleData/PERSONS.tfidf.bin";
			 			locationsIndexFile = "../DataModuleData/LOCATIONS.tfidf.bin";
			 			organizationsIndexFile = "../DataModuleData/ORGANIZATIONS.tfidf.bin";
			 			smPersonsFile = "../DataModuleData/PERSONS.tfidf.sim";
			 			smLocationsFile = "../DataModuleData/LOCATIONS.tfidf.sim";
			 			smOrganizationsFile = "../DataModuleData/ORGANIZATIONS.tfidf.sim";
			 			break;
			
			case NoNormalization: personsIndexFile = "../DataModuleData/PERSONS.nonorm.bin";
 								  locationsIndexFile = "../DataModuleData/LOCATIONS.nonorm.bin";
 								  organizationsIndexFile = "../DataModuleData/ORGANIZATIONS.nonorm.bin";
 								  smPersonsFile = "../DataModuleData/PERSONS.tf.sim";
 								  smLocationsFile = "../DataModuleData/LOCATIONS.tf.sim";
 								  smOrganizationsFile = "../DataModuleData/ORGANIZATIONS.tf.sim";
			 break;

		}
		
		smPersons = SimMatrixElem.LoadFromFile(smPersonsFile);
		smLocations = SimMatrixElem.LoadFromFile(smLocationsFile);
		smOrganizations = SimMatrixElem.LoadFromFile(smOrganizationsFile);
		
		List<SimMatrix> lsm = new ArrayList<SimMatrix>();
		lsm.add(smPersons);
		lsm.add(smLocations);
		lsm.add(smOrganizations);
		
		List<Double> lw1 = new ArrayList<Double>();
		lw1.add(1.0 / 3.0);
		lw1.add(1.0 / 3.0);
		lw1.add(1.0 / 3.0);
		
		smAllWeightedEqually = new SimMatrixInterp(lsm, lw1);

		List<Double> lw2 = new ArrayList<Double>();
		lw2.add(2.0 / 4.0);
		lw2.add(1.0 / 4.0);
		lw2.add(1.0 / 4.0);
		
		smAllPersonsWeightedDouble = new SimMatrixInterp(lsm, lw2);

		List<Double> lw3 = new ArrayList<Double>();
		lw3.add(1.0 / 4.0);
		lw3.add(2.0 / 4.0);
		lw3.add(1.0 / 4.0);
		
		smAllLocationsWeightedDouble = new SimMatrixInterp(lsm, lw2);

		List<Double> lw4 = new ArrayList<Double>();
		lw4.add(1.0 / 4.0);
		lw4.add(1.0 / 4.0);
		lw4.add(2.0 / 4.0);
		
		smAllOrganizationsWeightedDouble = new SimMatrixInterp(lsm, lw4);
	
		personsIndex = new VMindex(personsIndexFile);
		locationsIndex = new VMindex(locationsIndexFile);
		organizationsIndex = new VMindex(organizationsIndexFile);
		
		connection = MySqlConnectionProvider.getNewConnection(connHost, connDB, connUser, connPasswd);

		if (personsMap == null || locationsMap == null || organizationsMap == null)
		{
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
		Collections.reverse(nodes);
		List<Node> sn = new ArrayList<Node>();
		
		if (maxNumNodes == null) maxNumNodes = nodes.size();
		
		for (int i = 0; i < Math.min(nodes.size(), maxNumNodes); i++)
		{
			if (nodes.get(i).GetRelevance() <= simEpsilon) break;
			sn.add(nodes.get(i));
		}
		
		return sn;
	}
		
	public static Graph getGraph(String SinceDate, String TillDate, String Place, String Author, String DocType, List<String> queryTerms , List<Double> termWeights, Integer maxNumNodes, SimMatrixEnum sme, double dottedEdgeThreshold, double normalEdgeThreshold, double thickEdgeThreshold)
	{
		SimMatrix simMatrix;
		
		switch (sme)
		{
			case PersonsOnly: simMatrix = smPersons; break;
			case LocationsOnly: simMatrix = smLocations; break;
			case OrganizationsOnly: simMatrix = smOrganizations; break;
			case AllWeightedEqually: simMatrix = smAllWeightedEqually; break;
			case AllPersonsWeightedDouble: simMatrix = smAllPersonsWeightedDouble; break;
			case AllLocationsWeightedDouble: simMatrix = smAllLocationsWeightedDouble; break;
			case AllOrganizationsWeightedDouble: simMatrix = smAllOrganizationsWeightedDouble; break;
			default: simMatrix = smAllWeightedEqually; break;
		}
		
		if (SinceDate != "NULL") SinceDate = "\"" + SinceDate + "\"";
		if (TillDate != "NULL") TillDate = "\"" + TillDate + "\"";
		if (Place != "NULL") Place = "\"" + Place + "\"";
		if (Author != "NULL") Author = "\"" + Author + "\"";
		if (DocType != "NULL") DocType = "\"" + DocType + "\"";

		List<Node> ln = new ArrayList<Node>();

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
		        			/*srs.getString("SPEECH_TEXT").replace("<br>", "\n")*/"", srs.getString("SPEECH_DATE"));
		        	
		        	ln.add(nod);
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Node> sn = sortNodes(ln, queryTerms, termWeights, maxNumNodes);
		
		displayedGraph = new Graph(sn, simMatrix, dottedEdgeThreshold, normalEdgeThreshold, thickEdgeThreshold); 
		return displayedGraph;
		
	}
	
	
}
