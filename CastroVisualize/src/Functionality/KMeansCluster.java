package Functionality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class KMeansCluster {

	private VMindex persI;
	private VMindex locsI;
	private VMindex orgsI;
	private Map<Node, Integer> nodesMap;
	private int numClusters;
	private int numNodes;
	private List<ClusterVector> clVec;
	public Random r;
	
	public KMeansCluster(VMindex _persI, VMindex _locsI, VMindex _orgsI, List<Node> _nodes, int _numClusters)
	{
		persI = _persI;
		locsI = _locsI;
		orgsI = _orgsI;
		
		nodesMap = new HashMap<Node, Integer>();
		
		for (int i = 0; i < _nodes.size(); i++)
		{
			nodesMap.put(_nodes.get(i), 0);
		}
		
		numNodes = _nodes.size();
		
		numClusters = _numClusters;
		r = new Random(System.currentTimeMillis());
		
		for (int i = 0; i < numClusters; i++)
		{
			clVec.add(ClusterVector.createRandomInstance(this, persI.NumTerms(), locsI.NumTerms(), orgsI.NumTerms()));
		}
		
	}
	
	public void NextStep()
	{
		double currentMin;
		double curDist;
		Node n;
		
		for (Iterator<Node> it = nodesMap.keySet().iterator(); it.hasNext(); )
		{
			currentMin = 1000;
			n = it.next();
			
			for (int j = 0; j < numClusters; j++)
			{
				curDist = getDistance(clVec.get(j), n);
				if (curDist < currentMin)
				{
					currentMin = curDist;
					nodesMap.put(n, j);
				}
			}
		}
		
		List<ClusterVector> sum = new ArrayList<ClusterVector>();
		
		for (int i = 0; i < numClusters; i++)
		{
			sum.add(ClusterVector.createZeroInstance(persI.NumTerms(), locsI.NumTerms(), orgsI.NumTerms()));
		}
		
		int clusterIndex;
		for (Iterator<Node> it = nodesMap.keySet().iterator(); it.hasNext(); )
		{
			n = it.next();
			clusterIndex = nodesMap.get(n);
			sumUpNodeAndClusterVector(sum.get(clusterIndex), n);
		}
		
		for (int i = 0; i < numClusters; i++)
		{
			makeMean(sum.get(i));
		}
	}
	
	public void Evaluate()
	{
		Node n;
		for (Iterator<Node> it = nodesMap.keySet().iterator(); it.hasNext(); )
		{
			n = it.next();
			n.SetCluster(nodesMap.get(n));
		}
	}
	
	private static double getPartialSimilarity(VMindex index, Node n, List<Double> ld)
	{
		double sum = 0;
		int speechID = n.getSpeech_id();
		Set<Integer> NZcells = index.GetNonzeroCells(speechID);
		
		Integer i;
		for (Iterator<Integer> it = NZcells.iterator(); it.hasNext(); )
		{
			i = it.next();
			sum += Math.min(index.GetValue(speechID, i), ld.get(i));
		}
		
		return sum;
	}
	
	private double getDistance(ClusterVector clv, Node n)
	{
		double sim = 0;
		
		sim += getPartialSimilarity(persI, n, clv.persons);
		sim += getPartialSimilarity(locsI, n, clv.locations);
		sim += getPartialSimilarity(orgsI, n, clv.organizations);
		
		return 1 - sim;
	}
	
	private static void partialSummation(VMindex index, Node n, List<Double> ld)
	{
		double sum = 0;
		int speechID = n.getSpeech_id();
		Set<Integer> NZcells = index.GetNonzeroCells(speechID);
		
		Integer i;
		for (Iterator<Integer> it = NZcells.iterator(); it.hasNext(); )
		{
			i = it.next();
			ld.set(i, ld.get(i) + index.GetValue(speechID, i));
		}
		
	}
	
	private void sumUpNodeAndClusterVector(ClusterVector clv, Node n)
	{
		partialSummation(persI, n, clv.persons);
		partialSummation(locsI, n, clv.locations);
		partialSummation(orgsI, n, clv.organizations);
	}
	
	private void makeMean(ClusterVector clv)
	{
		for (int i = 0; i < clv.persons.size(); i++)
		{
			clv.persons.set(i, clv.persons.get(i) / numNodes);
		}

		for (int i = 0; i < clv.locations.size(); i++)
		{
			clv.locations.set(i, clv.locations.get(i) / numNodes);
		}

		for (int i = 0; i < clv.organizations.size(); i++)
		{
			clv.organizations.set(i, clv.organizations.get(i) / numNodes);
		}

	}
	

}


