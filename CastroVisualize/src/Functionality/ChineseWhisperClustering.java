package Functionality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChineseWhisperClustering {
	private ChineseWhisperClustering() {}
	
	private static void PermuteList(List<Node> ln)
	{			
			for (int k = ln.size() - 1; k > 0; k--) {
			    int w = (int)Math.floor(Math.random() * (k+1));
			    Node temp = ln.get(w);
			    ln.set(w, ln.get(k));
			    ln.set(k, temp);
			}
	}
	
	public static void Evaluate(Graph g, int maxNumClusters, int numIterations)
	{
		List<Node> ln = new ArrayList<Node>();
		
		List<Node> grNodes = g.getNodes();
		
		Map<Node, Integer> nodesMap = new HashMap<Node, Integer>();
		
		for (int i = 0; i < grNodes.size(); i++)
		{
			ln.add(grNodes.get(i));
			nodesMap.put(grNodes.get(i), i);
		}
		
		for (int i = 0; i < numIterations; i++)
		{
			PermuteList(ln);
			
			for (int j = 0; j < ln.size(); j++)
			{
				Node n = ln.get(j);
				
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
				
				nodesMap.put(n, bestClass);
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
