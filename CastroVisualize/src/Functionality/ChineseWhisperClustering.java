//============================================================================
// Name        : ChineseWhisperClustering.java
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
// Description : Abstract parent of all CHWC related algorithms
//               
//============================================================================

package Functionality;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class ChineseWhisperClustering {
	
	private static ChineseWhisperClustering implementation;
	
	public abstract void Evaluate(Graph g, int maxNumClusters, int numIterations);
	
	public static ChineseWhisperClustering GetImplementation()
	{
		return implementation;
	}
	
	public static void SetImplementation(ChineseWhisperTypesEnum en)
	{
		if (en == ChineseWhisperTypesEnum.standard)
		{
			implementation = new ChineseWhisperClusteringBasic();			
		}
		else if (en == ChineseWhisperTypesEnum.modified)
		{
			implementation = new ChineseWhisperClusteringAdjusted();
		}
		else if (en == ChineseWhisperTypesEnum.modifiedNew)
		{
			System.err.println("Forgot to push, have to wait for the functionality!");
			System.exit(1);
			//implementation = new ChineseWhisperClusteringAdjustedNew();
		}
		else if (en == ChineseWhisperTypesEnum.normalizing)
		{
			implementation = new ChineseWhisperClusteringNormalizing();
		}
	}
	
	public enum ChineseWhisperTypesEnum
	{
		standard, modified, modifiedNew, normalizing
	}
	
	protected class MyPair
	{
		public int classID;
		public double activationScore;
	}


	protected static void PermuteList(List<Node> ln)
	{			
			for (int k = ln.size() - 1; k > 0; k--) {
			    int w = (int)Math.floor(Math.random() * (k+1));
			    Node temp = ln.get(w);
			    ln.set(w, ln.get(k));
			    ln.set(k, temp);
			}
	}

	protected MyPair getBestClassForNode(Node n, Map<Node, Integer> nodesMap)
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

}
