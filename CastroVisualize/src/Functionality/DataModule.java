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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
	private static SimMatrix smDictionary;
	private static SimMatrix smAllWeightedEqually;
	private static SimMatrix smAllPersonsWeightedDouble;
	private static SimMatrix smAllLocationsWeightedDouble;
	private static SimMatrix smAllOrganizationsWeightedDouble;
	private static SimMatrix smHalfNEsHalfDictionary;
	
	private static String smPersonsFile;
	private static String smLocationsFile;
	private static String smOrganizationsFile;
	private static String smDictionaryFile;
	
	
	private static Map<String, Integer> personsMap;
	private static Map<String, Integer> locationsMap;
	private static Map<String, Integer> organizationsMap;
	private static Map<String, Integer> dictionaryMap;
	
	private static List<String> personsList;
	private static List<String> organizationsList;
	private static List<String> locationsList;
	private static List<String> dictionaryList;
	
	private static VMindex personsIndex;
	private static VMindex locationsIndex;
	private static VMindex organizationsIndex;
	private static VMindex dictionaryIndex;
	
	private static VMindex personsIndexSmooth;
	private static VMindex locationsIndexSmooth;
	private static VMindex organizationsIndexSmooth;
	
	private static String personsIndexFile;
	private static String locationsIndexFile;
	private static String organizationsIndexFile;
	private static String dictionaryIndexFile;
	
	private static String personsIndexSmoothFile;
	private static String locationsIndexSmoothFile;
	private static String organizationsIndexSmoothFile;

	private static double edgeDensity = 2;
	
	private static double dottedEdgeAbsoluteMultiplier = 0.7;
	private static double thickEdgeAbsoluteMultiplier = 1.3;
	private static double normalEdgeThreshold = 0.5;
	
	private static double normalEdgeRelativeMultiplier = 0.7;
	private static double thickEdgeRelativeMultiplier = 1.3;
	
	
	private static double simEpsilon = 0.000001;
	
	public static Graph displayedGraph;
	
	private DataModule() 
	{
	}
	
	public String getPersonString(int index)
	{
		return personsList.get(index);
	}
	
	public String getLocationString(int index)
	{
		return locationsList.get(index);
	}
	
	public String getOrganizationString(int index)
	{
		return organizationsList.get(index);
	}
	
	public static void InitConfiguration()
	{
		try
		{
			FileInputStream fio = new FileInputStream("castro.conf");
			InputStreamReader isr = new InputStreamReader(fio, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String s;
			System.err.println("file content:");
			while ((s = br.readLine()) != null)
			{
				System.err.println(s);
				
				if (s.substring(0, 4).equals("host"))
				{
					connHost = s.substring(5);
					System.err.println("-->host=" + connHost);
				}
				
				if (s.substring(0, 8).equals("database"))
				{
					connDB = s.substring(9);
					System.err.println("-->database=" + connDB);
				}
				
				if (s.substring(0, 4).equals("user"))
				{
					connUser = s.substring(5);
					System.err.println("-->user=" + connUser);
					
				}

				if (s.substring(0, 8).equals("password"))
				{
					connPasswd = s.substring(9);
					System.err.println("-->password=" + connPasswd);
					
				}

			}
			System.err.println("-------");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void Init(IndexTypeEnum indexType)
	{
		switch (indexType)
		{
			case TF: personsIndexFile = "../DataModuleData/PERSONS.tf.bin";
					 locationsIndexFile = "../DataModuleData/LOCATIONS.tf.bin";
					 organizationsIndexFile = "../DataModuleData/ORGANIZATIONS.tf.bin";
					 dictionaryIndexFile = "../DataModuleData/GENERAL.tf.bin";
					 personsIndexSmoothFile = "../DataModuleData/PERSONS-smooth.tf.bin";
					 locationsIndexSmoothFile = "../DataModuleData/LOCATIONS-smooth.tf.bin";
					 organizationsIndexSmoothFile = "../DataModuleData/ORGANIZATIONS-smooth.tf.bin";
					 smPersonsFile = "../DataModuleData/PERSONS-smooth.tf.sim";
					 smLocationsFile = "../DataModuleData/LOCATIONS-smooth.tf.sim";
					 smOrganizationsFile = "../DataModuleData/ORGANIZATIONS-smooth.tf.sim";
					 smDictionaryFile = "../DataModuleData/GENERAL.tf.sim";
					 break;
			
			case TFIDF: personsIndexFile = "../DataModuleData/PERSONS.tfidf.bin";
			 			locationsIndexFile = "../DataModuleData/LOCATIONS.tfidf.bin";
			 			organizationsIndexFile = "../DataModuleData/ORGANIZATIONS.tfidf.bin";
			 			dictionaryIndexFile = "../DataModuleData/GENERAL.tf.bin";
			 			personsIndexSmoothFile = "../DataModuleData/PERSONS-smooth.tfidf.bin";
			 			locationsIndexSmoothFile = "../DataModuleData/LOCATIONS-smooth.tfidf.bin";
			 			organizationsIndexSmoothFile = "../DataModuleData/ORGANIZATIONS-smooth.tfidf.bin";
			 			smPersonsFile = "../DataModuleData/PERSONS-smooth.tfidf.sim";
			 			smLocationsFile = "../DataModuleData/LOCATIONS-smooth.tfidf.sim";
			 			smOrganizationsFile = "../DataModuleData/ORGANIZATIONS-smooth.tfidf.sim";
			 			smDictionaryFile = "../DataModuleData/GENERAL.tfidf.sim";
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
		smDictionary = SimMatrixElem.LoadFromFile(smDictionaryFile);
		
		
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

		List<SimMatrix> lsmBle = new ArrayList<SimMatrix>();
		lsmBle.add(smAllWeightedEqually);
		lsmBle.add(smDictionary);
		
		List<Double> lwBle = new ArrayList<Double>();
		lwBle.add(0.5);
		lwBle.add(0.5);
		
		smHalfNEsHalfDictionary = new SimMatrixInterp(lsmBle, lwBle);

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
		dictionaryIndex = new VMindex(dictionaryIndexFile);
		
		personsIndexSmooth = new VMindex(personsIndexSmoothFile);
		locationsIndexSmooth = new VMindex(locationsIndexSmoothFile);
		organizationsIndexSmooth = new VMindex(organizationsIndexSmoothFile);
		
		connection = MySqlConnectionProvider.getNewConnection(connHost, connDB, connUser, connPasswd);

		if (personsMap == null || locationsMap == null || organizationsMap == null || dictionaryMap == null)
		{
			personsMap = new HashMap<String, Integer>();
			locationsMap = new HashMap<String, Integer>();
			organizationsMap = new HashMap<String, Integer>();
			dictionaryMap = new HashMap<String, Integer>();
			
			personsList = new ArrayList<String>();
			locationsList = new ArrayList<String>();
			organizationsList = new ArrayList<String>();
			dictionaryList = new ArrayList<String>();
			
			System.err.println("Loading database...");
			
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
							organizationsList.add("");
						}
						else if (pomS.equals("PERSONS"))
						{
							personsMap.put(srs.getString("NE_NAME"), srs.getInt("NE_INDEX_ID"));
							personsList.add("");
						}
						else if (pomS.equals("LOCATIONS"))
						{
							locationsMap.put(srs.getString("NE_NAME"), srs.getInt("NE_INDEX_ID"));
							locationsList.add("");
						}
										
				}
				
				String key;
				for (Iterator<String> it = organizationsMap.keySet().iterator(); it.hasNext(); )
				{
					key = it.next();
					organizationsList.set(organizationsMap.get(key), key);
				}

				for (Iterator<String> it = locationsMap.keySet().iterator(); it.hasNext(); )
				{
					key = it.next();
					locationsList.set(locationsMap.get(key), key);
				}

				for (Iterator<String> it = personsMap.keySet().iterator(); it.hasNext(); )
				{
					key = it.next();
					personsList.set(personsMap.get(key), key);
				}

				stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	                    ResultSet.CONCUR_READ_ONLY);	
				System.err.println("CALL getDictionary();");
				srs = stmt.executeQuery("CALL getDictionary();");
				
				while (srs.next())
				{
					dictionaryMap.put(srs.getString("WORDFORM"), srs.getInt("WORD_ID"));
					dictionaryList.add("");
				}
				
				
				for (Iterator<String> it = dictionaryMap.keySet().iterator(); it.hasNext(); )
				{
					key = it.next();
					
					if (dictionaryMap.get(key) == dictionaryList.size())
					{
						dictionaryList.add(key);
					}
					else
					{
						dictionaryList.set(dictionaryMap.get(key), key);
					}
				}
				
				System.err.println("dictionaryMap.get(sugar) = " + dictionaryMap.get("sugar"));

				
			} catch (SQLException e) {
				e.printStackTrace();
			}

			System.err.println("...done");
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
			System.err.println("sortNodes: queryTerms[" + i + "] = " + queryTerms.get(i));
			
			if (personsMap.containsKey(queryTerms.get(i)))
			{
				similarityUpdate(nodes, personsIndexSmooth, personsMap.get(queryTerms.get(i)), termWeights.get(i));
			}
			
			if (locationsMap.containsKey(queryTerms.get(i)))
			{
				similarityUpdate(nodes, locationsIndexSmooth, locationsMap.get(queryTerms.get(i)), termWeights.get(i));
				
			}

			if (organizationsMap.containsKey(queryTerms.get(i)))
			{
				similarityUpdate(nodes, organizationsIndexSmooth, organizationsMap.get(queryTerms.get(i)), termWeights.get(i));
				
			}
			
			if (dictionaryMap.containsKey(queryTerms.get(i)))
			{
				System.err.println("sortNodes: dictionaryMap contains: " + queryTerms.get(i) + ", term column: " + dictionaryMap.get(queryTerms.get(i)));
				similarityUpdate(nodes, dictionaryIndex, dictionaryMap.get(queryTerms.get(i)), termWeights.get(i));
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
		
		System.err.println("sortNodes: numNodes = " + sn.size());
		
		return sn;
	}
	
	public static String getSpeechText(int SpeechID)
	{
		try 
		{
			java.sql.Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);	
			ResultSet srs = stmt.executeQuery("CALL getSpeechText(" + SpeechID + ");");
			
			if (srs.next())
			{
				return srs.getString("SPEECH_TEXT");
			}
			else
			{
				return "";
			}
		}
		catch (Exception e)
		{
			e.getStackTrace();
			System.exit(1);
		}
		
		return "";
	}

	public static Graph getGraphThreshold(String SinceDate, String TillDate, String Place, String Author, String DocType, List<String> queryTerms , List<Double> termWeights, Integer maxNumNodes, SimMatrixEnum sme, double _normalEdgeThreshold, double _dottedEdgeAbsoluteMultiplier, double _thickEdgeAbsoluteMultiplier)
	{
		SimMatrix simMatrix;
	
		normalEdgeThreshold = _normalEdgeThreshold;
		dottedEdgeAbsoluteMultiplier = _dottedEdgeAbsoluteMultiplier;
		thickEdgeAbsoluteMultiplier = _thickEdgeAbsoluteMultiplier;
		
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
		        			srs.getString("SPEECH_DATE"));
		        	
		        	ln.add(nod);
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Node> sn = sortNodes(ln, queryTerms, termWeights, maxNumNodes);
		
		displayedGraph = Graph.createGraphThreshold(sn, simMatrix, normalEdgeThreshold, dottedEdgeAbsoluteMultiplier, thickEdgeAbsoluteMultiplier); 
		return displayedGraph;
		
	}

	
	public static Graph getGraphDensity(String SinceDate, String TillDate, String Place, String Author, String DocType, List<String> queryTerms , List<Double> termWeights, Integer maxNumNodes, SimMatrixEnum sme, double _edgeDensity, double _normalEdgeRelativeMultiplier, double _thickEdgeRelativeMultiplier)
	{
		SimMatrix simMatrix;
	
		edgeDensity = _edgeDensity;
		normalEdgeRelativeMultiplier = _normalEdgeRelativeMultiplier;
		thickEdgeRelativeMultiplier = _thickEdgeRelativeMultiplier;
		
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
		        			srs.getString("SPEECH_DATE"));
		        	
		        	ln.add(nod);
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Node> sn = sortNodes(ln, queryTerms, termWeights, maxNumNodes);
		
		displayedGraph = Graph.createGraphDensity(sn, simMatrix, edgeDensity, normalEdgeRelativeMultiplier, thickEdgeRelativeMultiplier); 
		return displayedGraph;
		
	}
	
	public static Set<NamedEntity> getPersonsInDocument(Node n)
	{
		Set<NamedEntity> ret = new HashSet<NamedEntity>();
		Integer speechID = n.getSpeech_id();
		Set<Integer> neCells = personsIndexSmooth.GetNonzeroCells(speechID);
		
		Integer col;
		for (Iterator<Integer> it = neCells.iterator(); it.hasNext(); )
		{
			col = it.next();
			
			boolean expanded; 
			if (personsIndex.GetValue(speechID, col) >= 0.0001)
			{
				expanded = false;
			}
			else
			{
				expanded = true;
			}
			
			ret.add(new NamedEntity(col, personsList.get(col), NamedEntityEnum.persons, personsIndexSmooth.GetValue(speechID, col), expanded));
		}
		
		return ret;
	}
	
	public static Set<NamedEntity> getLocationsInDocument(Node n)
	{
		Set<NamedEntity> ret = new HashSet<NamedEntity>();
		Integer speechID = n.getSpeech_id();
		Set<Integer> neCells = locationsIndexSmooth.GetNonzeroCells(speechID);
		
		Integer col;
		for (Iterator<Integer> it = neCells.iterator(); it.hasNext(); )
		{
			col = it.next();
			boolean expanded;
			
			if (locationsIndex.GetValue(speechID, col) > 0.001)
			{
				expanded = false;
			}
			else
			{
				expanded = true;
			}
			
			ret.add(new NamedEntity(col, locationsList.get(col), NamedEntityEnum.locations, locationsIndexSmooth.GetValue(speechID, col), expanded));
		}
		
		return ret;
	}
	
	public static Set<NamedEntity> getOrganizationsInDocument(Node n)
	{
		Set<NamedEntity> ret = new HashSet<NamedEntity>();
		Integer speechID = n.getSpeech_id();
		Set<Integer> neCells = organizationsIndexSmooth.GetNonzeroCells(speechID);
		
		Integer col;
		for (Iterator<Integer> it = neCells.iterator(); it.hasNext(); )
		{
			col = it.next();
			boolean expanded;
			
			if (organizationsIndex.GetValue(speechID, col) > 0.001)
			{
				expanded = false;
			}
			else
			{
				expanded = true;
			}
			
			ret.add(new NamedEntity(col, organizationsList.get(col), NamedEntityEnum.organizations, organizationsIndexSmooth.GetValue(speechID, col), expanded));
		}
		
		return ret;
	}

	
}
