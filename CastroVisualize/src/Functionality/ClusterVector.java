// Name        : ClusterVector.java
// Author      : Michal Richter, Michalisek
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
// Description : Arbitrary data type used in k-means algorithm
//               
//============================================================================

package Functionality;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClusterVector {
	public List<Double> persons;
 	public List<Double> locations;
	public List<Double> organizations;
	
	private ClusterVector(List<Double> _persons, List<Double> _locations, List<Double> _organizations)
	{
		persons = _persons;
		locations = _locations;
		organizations = _organizations;
	}
	
	public static ClusterVector createRandomInstance(KMeansCluster kmeans, int numPers, int numLocs, int numOrgs)
	{
		List<Double> pers = new ArrayList<Double>();
		List<Double> locs = new ArrayList<Double>();
		List<Double> orgs = new ArrayList<Double>();
		Random r = kmeans.r;
		
		for (int i = 0; i < numPers; i++)
		{
			pers.add(r.nextDouble());
		}
		
		for (int i = 0; i < numLocs; i++)
		{
			locs.add(r.nextDouble());
		}
		
		for (int i = 0; i < numOrgs; i++)
		{
			orgs.add(r.nextDouble());
		}
		return new ClusterVector(pers, locs, orgs);
	}
	
	public static ClusterVector createZeroInstance(int numPers, int numLocs, int numOrgs)
	{
		List<Double> pers = new ArrayList<Double>();
		List<Double> locs = new ArrayList<Double>();
		List<Double> orgs = new ArrayList<Double>();
		
		for (int i = 0; i < numPers; i++)
		{
			pers.add(0.0);
		}
		
		for (int i = 0; i < numLocs; i++)
		{
			locs.add(0.0);
		}
		
		for (int i = 0; i < numOrgs; i++)
		{
			orgs.add(0.0);
		}
		return new ClusterVector(pers, locs, orgs);
		
	}
	
}