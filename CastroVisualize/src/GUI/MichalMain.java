//============================================================================
// Name        : MichalMain.java
// Author      : Michal Richter, Michalisek
// Version     :
// Copyright   : This product is licensed under Fidel Castro restricted software license. 
//               Use of any kind is considered a breach of copyright law. 
//               You are not allowed to use this for any purpose; neither commercial 
//               nor non-commercial.
// Description : My main class for data experiments
//============================================================================
package GUI;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Functionality.*;
import Functionality.SimMatrixElem.SimilarityMeasure;

public class MichalMain {

	public static void main(String[] args) {
		
		
		Functionality.DataModule.InitConfiguration();
		Functionality.DataModule.Init(SettingsWindow.currIndex,
				SettingsWindow.smoothedIndex, SettingsWindow.smoothedSimMatrix,
				SettingsWindow.personsCoef, SettingsWindow.locationsCoef,
				SettingsWindow.organizationsCoef,
				SettingsWindow.lexicalSimilarityCoef);

		SettingsWindow.maxNumClusters = 10;
		SettingsWindow.ChineseWhisperClustering_minimalSizeOfCluster = 3;
		SettingsWindow.ChineseWhisperClustering_tempGraphDensity = 4;
		SettingsWindow.ChineseWhisperClusteringAdjusted_activationThresholdMultiplier = 1.5;
		SettingsWindow.ChineseWhisperClusteringAdjusted_numMasterEdges = 2;
		SettingsWindow.ChineseWhisperClusteringAdjusted_activationThresholdMultiplierIncrement = 0.5;
		SettingsWindow.ChineseWhisperClustering_numberOfIterations = 5;
		
		Graph g = DataModule.getGraphThreshold("1940-01-01", "2000-12-31", "NULL", "NULL", "NULL", new ArrayList<String>(), new ArrayList<Double>(), 2000, 0.5, 0.8, 1.2);
		
		System.err.println("number of clusters: " + g.GetNumberOfClusters());
		
		Map<Integer, Integer> clusterIdToNumNodes = new HashMap<Integer, Integer>();
		Map<Integer, String> clusterToInfo = new HashMap<Integer, String>();
		int clusterID;
		List<Node> ln = g.getNodes();
		
		for (int i = 0; i < ln.size(); i++)
		{
			clusterID = ln.get(i).GetCluster();
			
			if (clusterIdToNumNodes.containsKey(clusterID))
			{
				clusterIdToNumNodes.put(clusterID, clusterIdToNumNodes.get(clusterID) + 1);
			}
			else
			{
				clusterIdToNumNodes.put(clusterID, 1);
			}
			
			if (clusterID >= 0)
			{
				String nodeInfo = ln.get(i).toString();
				
				if (clusterToInfo.containsKey(clusterID))
				{
					clusterToInfo.put(clusterID, clusterToInfo.get(clusterID) + nodeInfo);
				}
				else
				{
					clusterToInfo.put(clusterID, nodeInfo);
				}
			}
		}
		
		
		try
		{
			//FileOutputStream fos = new FileOutputStream("/home/michalisek/clustering.out");
			//OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter br = new BufferedWriter(new FileWriter("/home/michalisek/clustering.out"));
	
			
			
			for (Iterator<Integer> it = clusterIdToNumNodes.keySet().iterator(); it.hasNext(); )
			{
				clusterID = it.next();
				System.err.println("clusterID: " + clusterID + ", numDocs: " + clusterIdToNumNodes.get(clusterID));
				
				if (clusterID >= 0)
				{
					br.write("\n---------------------\n\n");
					br.write("ClusterID: " + clusterID + "\n");
					br.write(clusterToInfo.get(clusterID));
				}
			}
			
			br.flush();
			br.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		//makeBinIndexes();
		//makeSimMatrices(SimMatrixElem.SimilarityMeasure.euclidean);
		
		//RunTest();
		
		//DataModule.Init(IndexTypeEnum.TF);
		
		/*
		Abraham Lincoln,  Alfonso Barrantes
		Salvador, Santa Cruz
		Ministry of Development
		*/
		
		/*List<String> queryTerms = new ArrayList<String>();
		List<Double> termWeights = new ArrayList<Double>();
		
		queryTerms.add("PRENSA LATINA"); termWeights.add(1.0);
		queryTerms.add("Conrado Benitez"); termWeights.add(1.0);
		queryTerms.add("Salvador"); termWeights.add(1.0);
		queryTerms.add("Chile"); termWeights.add(1.0);
		queryTerms.add("Manual Ascunce"); termWeights.add(0.0);
		
		
		//Graph G = DataModule.getGraph("NULL", "NULL", "NULL", "NULL", "NULL", queryTerms, termWeights, 50, SimMatrixEnum.LocationsOnly, 0.4);
		Graph G = null;
		List<Node> ln = G.getNodes();
		
		for (int i = 0; i < Math.min(10, ln.size()); i++)
		{
			Node n = ln.get(i);
			System.err.println(n.getSpeech_id() + ", " + n.getSpeech_date() + ", " + n.getHeadline() + " --> " + n.GetRelevance());
		}
		
		System.err.println("OK!");*/
		
	}

	private static void makeBinIndexes()
	{
		VMindex.ConvertTextToBin("../work/PERSONS.tf", "../DataModuleData/PERSONS.tf.bin");
		VMindex.ConvertTextToBin("../work/LOCATIONS.tf", "../DataModuleData/LOCATIONS.tf.bin");
		VMindex.ConvertTextToBin("../work/ORGANIZATIONS.tf", "../DataModuleData/ORGANIZATIONS.tf.bin");
		VMindex.ConvertTextToBin("../work/GENERAL.tf", "../DataModuleData/GENERAL.tf.bin");

		VMindex.ConvertTextToBin("../work/PERSONS.tfidf", "../DataModuleData/PERSONS.tfidf.bin");
		VMindex.ConvertTextToBin("../work/LOCATIONS.tfidf", "../DataModuleData/LOCATIONS.tfidf.bin");
		VMindex.ConvertTextToBin("../work/ORGANIZATIONS.tfidf", "../DataModuleData/ORGANIZATIONS.tfidf.bin");
		VMindex.ConvertTextToBin("../work/GENERAL.tfidf", "../DataModuleData/GENERAL.tfidf.bin");

		VMindex.ConvertTextToBin("../work/PERSONS-smooth.tf", "../DataModuleData/PERSONS-smooth.tf.bin");
		VMindex.ConvertTextToBin("../work/LOCATIONS-smooth.tf", "../DataModuleData/LOCATIONS-smooth.tf.bin");
		VMindex.ConvertTextToBin("../work/ORGANIZATIONS-smooth.tf", "../DataModuleData/ORGANIZATIONS-smooth.tf.bin");

		VMindex.ConvertTextToBin("../work/PERSONS-smooth.tfidf", "../DataModuleData/PERSONS-smooth.tfidf.bin");
		VMindex.ConvertTextToBin("../work/LOCATIONS-smooth.tfidf", "../DataModuleData/LOCATIONS-smooth.tfidf.bin");
		VMindex.ConvertTextToBin("../work/ORGANIZATIONS-smooth.tfidf", "../DataModuleData/ORGANIZATIONS-smooth.tfidf.bin");

	}

	private static void RunTest()
	{
		VMindex.ConvertTextToBin("../work/test.tf", "../work/test.tf.bin");
		
		VMindex pomI = new VMindex("../work/test.tf.bin");
		
		for (int i = 0; i < pomI.NumDocs(); i++)
			for (int j = 0; j < pomI.NumTerms(); j++)
			{
				System.err.println("(" + i + ", " + j + ") = " + pomI.GetValue(i, j));
			}
		
		SimMatrixElem sim = SimMatrixElem.CountFromVMindex(pomI, SimMatrixElem.SimilarityMeasure.cosine);
		
		System.err.println("");
		System.err.println("Similarities:");
			
		for (int i = 0; i < sim.getNumDocs(); i++)
			for (int j = 0; j < sim.getNumDocs(); j++)
			{
				System.err.println("(" + i + ", " + j + ") = " + sim.getSimilarity_byID(i, j));				
			}
		
		sim.SaveToFile("../work/test.tf.sim");
		SimMatrixElem sim2 = SimMatrixElem.LoadFromFile("../work/test.tf.sim");
		
		System.err.println("");
		System.err.println("Similarities after save/load:");
			
		for (int i = 0; i < sim.getNumDocs(); i++)
			for (int j = 0; j < sim.getNumDocs(); j++)
			{
				System.err.println("(" + i + ", " + j + ") = " + sim.getSimilarity_byID(i, j) + " -> " + sim2.getSimilarity_byID(i,j));				
			}
	
		/*VMindex persI = new VMindex("../DataModuleData/PERSONS.tfidf.bin");
		
		for (int i = 0; i < 100; i++)
		{
			int a =  (int)Math.round(Math.floor(Math.random() * persI.NumDocs()));
			
			Set<Integer> si = persI.GetNonzeroCells(a);
			
			
			int b = (int)Math.round(Math.floor(Math.random() * si.size()));
			
			int ind;
			
			Iterator<Integer> it = si.iterator();
			
			for (int g = 0; g < b; g++)
			{
				it.next();
			}
			ind = it.next();
			
			System.err.println("(doc: " + a + ", term: " + b + ") = " + persI.GetValue(a, ind));
		}
		
		SimMatrixElem simPers = SimMatrixElem.CountFromVMindex(persI);
		
		
		simPers.SaveToFile("../DataModuleData/PERSONS.tfidf.sim");
		
		SimMatrixElem simPersLoaded = SimMatrixElem.LoadFromFile("../DataModuleData/PERSONS.tfidf.sim");

		for (int i = 0; i < 100; i++)
		{
			int a = (int)Math.round(Math.floor(Math.random() * simPers.getNumDocs()));
			int b = (int)Math.round(Math.floor(Math.random() * simPers.getNumDocs()));
			
			System.err.println("sim(" + a + ", " + b + ") = " + simPers.getSimilarity_byID(a, b) + " -> " + simPersLoaded.getSimilarity_byID(a, b));
		}

		*/
		return;				
	}
	
	private static void makeSimMatrices(SimMatrixElem.SimilarityMeasure simMeas)
	{
		String simString = simMeas.toString();
		VMindex ind = new VMindex("../DataModuleData/PERSONS.tf.bin");
		SimMatrixElem sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/PERSONS." + simString + ".tf.sim");

		ind = new VMindex("../DataModuleData/PERSONS.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/PERSONS." + simString + ".tfidf.sim");
		
		ind = new VMindex("../DataModuleData/LOCATIONS.tf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/LOCATIONS." + simString + ".tf.sim");

		ind = new VMindex("../DataModuleData/LOCATIONS.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/LOCATIONS." + simString + ".tfidf.sim");

		ind = new VMindex("../DataModuleData/ORGANIZATIONS.tf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/ORGANIZATIONS." + simString + ".tf.sim");

		ind = new VMindex("../DataModuleData/ORGANIZATIONS.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/ORGANIZATIONS." + simString + ".tfidf.sim");

		ind = new VMindex("../DataModuleData/PERSONS-smooth.tf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/PERSONS-smooth." + simString + ".tf.sim");

		ind = new VMindex("../DataModuleData/PERSONS-smooth.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/PERSONS-smooth." + simString + ".tfidf.sim");
		
		ind = new VMindex("../DataModuleData/LOCATIONS-smooth.tf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/LOCATIONS-smooth." + simString + ".tf.sim");

		ind = new VMindex("../DataModuleData/LOCATIONS-smooth.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/LOCATIONS-smooth." + simString + ".tfidf.sim");

		ind = new VMindex("../DataModuleData/ORGANIZATIONS-smooth.tf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/ORGANIZATIONS-smooth." + simString + ".tf.sim");

		ind = new VMindex("../DataModuleData/ORGANIZATIONS-smooth.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/ORGANIZATIONS-smooth." + simString + ".tfidf.sim");

		ind = new VMindex("../DataModuleData/GENERAL.tf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/GENERAL." + simString + ".tf.sim");

		ind = new VMindex("../DataModuleData/GENERAL.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind, simMeas);
		sim.SaveToFile("../DataModuleData/GENERAL." + simString + ".tfidf.sim");
	}
	
}