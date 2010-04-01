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