// Name        : KMeansCluster.java
// Author      : Michal Richter
// Version     :
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
// Description : Our implementation of the k-means clustering algorithm
//               
// Version     : 2.5.1
//============================================================================
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
		clVec = new ArrayList<ClusterVector>();
		
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


