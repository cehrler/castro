// Name        : ChineseWhisperClusteringNormalizing.java
// Author      : Michal Richter
// Version     : 2.5.1
// Copyright   : All rights regarding the source material are reserved by the authors: With the exception
//               of Caroline Sporleder and Martin Schreiber at Saarland University in Germany and for
//               research and teaching at Saarland University in general, explicit permission must be
//               obtained before do. Usage or reference to this work or any part thereof must feature
//               credit to all the authors. Without explicit permission from the authors beforehand, this
//               software, its source and documentation may not be distributed, incorporated into other
//               products or used to create derived works.
//               However, the authors hope that this project may be of interest and use to others,
//               and so are glad to grant permission to people wishing to incorporate this project into
//               others or to use it for other purposes, and are asked to contact the authors for these
//               permissions.
// Description : Our modification of CHWC with the working name "Give it to the poor"
//               
//============================================================================

package Functionality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import GUI.SettingsWindow;

public class ChineseWhisperClusteringNormalizing extends ChineseWhisperClustering {

	public ChineseWhisperClusteringNormalizing() {}
	
	private double clusterSizeDepMultiplier(int size)
	{
		double retVal = -1;
		
		if (size == 1) retVal = 1;
		retVal = (size - 1) * SettingsWindow.ChineseWhisperClusteingNormalizing_sizeAddConstant + 1;
		/*if (size == 2) retVal = 1.2;
		if (size == 3) retVal = 1.4;
		if (size == 4) retVal = 1.6;
		if (size == 5) retVal = 1.8;
		if (size == 6) retVal =  2;
		if (size == 7) retVal = 2.2;
		if (size == 8) retVal = 7.4;
		if (size > 8) retVal = (double)size;*/
		
		if (retVal >= 0)
		{
			//System.err.println("clusterSizeDepMultiplier: " + retVal);
			return retVal;
		}
		
		System.err.println("Invalid cluster size value: " + size);
		System.exit(1);
		
		return 0;
		
	}
	
	protected MyPair getBestClassForNode(Node n, Map<Node, Integer> nodesMap, Map<Integer, Integer> numNodesPerClass)
	{
		Map<Integer, Double> activationMap = new HashMap<Integer, Double>();
		
		Map<Node, Double> neighboursMap = n.getNeighborsMap();
		
		double maxActivation = -1000;
		int bestClass = -1;
		double currActivation;
		
		for (Iterator<Node> it = neighboursMap.keySet().iterator(); it.hasNext(); )
		{
			Node bleNode = it.next();
			
			int bleClassID = nodesMap.get(bleNode); 
			
			if (activationMap.containsKey(bleClassID))
			{
				activationMap.put(bleClassID, activationMap.get(bleClassID) + neighboursMap.get(bleNode));
			}
			else
			{
				activationMap.put(bleClassID, neighboursMap.get(bleNode));
			}
			
			currActivation = activationMap.get(bleClassID) / clusterSizeDepMultiplier(numNodesPerClass.get(bleClassID));
			if (currActivation  > maxActivation)
			{
				maxActivation = currActivation ;
				bestClass = bleClassID;
			}
			
		}
		
		MyPair pair = new MyPair();
		pair.classID = bestClass;
		pair.activationScore = maxActivation;
		return pair;
		
	}
	
	@Override
	public void Evaluate(Graph g, int maxNumClusters, int numIterations) {
		System.err.println("Chinese whisper normalizing!");
		
		double activationThresholdMultiplier = SettingsWindow.ChineseWhisperClusteringAdjusted_activationThresholdMultiplier;
		int minimalClusterSize = SettingsWindow.ChineseWhisperClustering_minimalSizeOfCluster;
		List<Node> ln = new ArrayList<Node>();
		
		List<Node> grNodes = g.getNodes();
		
		Map<Node, Integer> nodesMap = new HashMap<Node, Integer>();
		
		for (int i = 0; i < grNodes.size(); i++)
		{
			ln.add(grNodes.get(i));
			nodesMap.put(grNodes.get(i), i);
		}
		
		List<Edge> le = new ArrayList<Edge>();
		
		List<Edge> grEdges = g.getEdges();
		
		for (int i = 0; i < grEdges.size(); i++)
		{
			le.add(grEdges.get(i));
		}
		
		int numEdgesToCountMean = Math.min(le.size(), SettingsWindow.ChineseWhisperClusteringAdjusted_numberEdgesForMeanComputation);
		
		double sum = 0.0;
		for (int i = 0; i < numEdgesToCountMean; i++)
		{
			sum += le.get(i).getStrength();
		}
		
		double edgeStrengthMean = sum / numEdgesToCountMean;

		final Map<Integer, Integer> numNodesPerClass = new HashMap<Integer, Integer>();
		for (int i = 0; i < ln.size(); i++)
		{
			numNodesPerClass.put(i, 1);
		}
		
		for (int i = 0; i < numIterations; i++)
		{
			PermuteList(ln);
			
			for (int j = 0; j < ln.size(); j++)
			{
				Node n = ln.get(j);
				
				MyPair bestPair = getBestClassForNode(n, nodesMap, numNodesPerClass);
				
				if (bestPair.activationScore > edgeStrengthMean * activationThresholdMultiplier)
				{
					numNodesPerClass.put(bestPair.classID, numNodesPerClass.get(bestPair.classID) + 1);
					numNodesPerClass.put(nodesMap.get(n), numNodesPerClass.get(nodesMap.get(n)) - 1);
					nodesMap.put(n, bestPair.classID);
				}
			}
			
		}
		
		
		Node n;
		int classID;
		/*for (Iterator<Node> it = nodesMap.keySet().iterator(); it.hasNext(); )
		{
			n = it.next();
			classID = nodesMap.get(n);
			
			if (numNodesPerClass.containsKey(classID))
			{
				numNodesPerClass.put(classID, numNodesPerClass.get(classID) + 1);
			}
			else
			{
				numNodesPerClass.put(classID, 1);
			}
		}*/
		
		List<Integer> listClassID = new ArrayList<Integer>(numNodesPerClass.keySet());
		
		Collections.sort(listClassID, new Comparator<Integer>(){
			public int compare(Integer arg0, Integer arg1) {
				return numNodesPerClass.get(arg0).compareTo(numNodesPerClass.get(arg1));
			}
		});
		
		Collections.reverse(listClassID);
		
		Map<Integer, Integer> classIDtoClusterIDMapping = new HashMap<Integer, Integer>();
		for (int i = 0; i < Math.min(maxNumClusters, listClassID.size()); i++)
		{
			if (numNodesPerClass.get(listClassID.get(i)) >= minimalClusterSize)
				classIDtoClusterIDMapping.put(listClassID.get(i), i);
		}
		
		for (int i = 0; i < ln.size(); i++)
		{
			n = ln.get(i);
			classID = nodesMap.get(n);
			
			if (classIDtoClusterIDMapping.containsKey(classID))
			{
				n.SetCluster(classIDtoClusterIDMapping.get(classID));
			}
			else
			{
				n.SetCluster(-1);
			}
		}
	}

}
