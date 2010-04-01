package Functionality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import GUI.SettingsWindow;

public class ChineseWhisperClusteringAdjusted extends ChineseWhisperClustering {
	
	public ChineseWhisperClusteringAdjusted() {}
	
	private class MyPair
	{
		public int classID;
		public double activationScore;
	}
	
	private static void PermuteList(List<Node> ln)
	{			
			for (int k = ln.size() - 1; k > 0; k--) {
			    int w = (int)Math.floor(Math.random() * (k+1));
			    Node temp = ln.get(w);
			    ln.set(w, ln.get(k));
			    ln.set(k, temp);
			}
	}
	
	
	private MyPair getBestClassForNode(Node n, Map<Node, Integer> nodesMap)
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
			
			currActivation = activationMap.get(bleClassID);
			if (currActivation > maxActivation)
			{
				maxActivation = currActivation;
				bestClass = bleClassID;
			}
			
		}
		
		MyPair pair = new MyPair();
		pair.classID = bestClass;
		pair.activationScore = maxActivation;
		return pair;
		
	}
	
	public void Evaluate(Graph g, int maxNumClusters, int numIterations)
	{
		System.err.println("Chinese whisper adjusted!");
		
		int numMaster = SettingsWindow.ChineseWhisperClusteringAdjusted_numMasterEdges;
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
		
		Collections.sort(le, new Comparator<Edge>() {
			public int compare(Edge arg0, Edge arg1) {
				return (arg0.getStrength().compareTo(arg1.getStrength()));
			}
		});
		Collections.reverse(le);
		
		Edge e;
		for (int i = 0; i < Math.min(le.size(), numMaster); i++)
		{
			e = le.get(i);
			MyPair firstNodePair = getBestClassForNode(e.getNode1(), nodesMap);
			MyPair secondNodePair = getBestClassForNode(e.getNode2(), nodesMap);
			
			if (firstNodePair.activationScore > secondNodePair.activationScore)
			{
				nodesMap.put(e.getNode1(), firstNodePair.classID);
			}
			else
			{
				nodesMap.put(e.getNode2(), secondNodePair.classID);
			}
		}
		
		for (int i = 0; i < numIterations; i++)
		{
			PermuteList(ln);
			
			for (int j = 0; j < ln.size(); j++)
			{
				Node n = ln.get(j);
				
				MyPair bestPair = getBestClassForNode(n, nodesMap);
				
				if (bestPair.activationScore > edgeStrengthMean * activationThresholdMultiplier)
				{
					nodesMap.put(n, bestPair.classID);
				}
			}
			
		}
		
		final Map<Integer, Integer> numNodesPerClass = new HashMap<Integer, Integer>();
		
		Node n;
		int classID;
		for (Iterator<Node> it = nodesMap.keySet().iterator(); it.hasNext(); )
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
		}
		
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
