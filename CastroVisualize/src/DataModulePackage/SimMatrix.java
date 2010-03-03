package DataModulePackage;

import java.util.Map;


public abstract class SimMatrix {
	
	protected Integer numSpeeches;
	
	public abstract Double getSimilarity(Node a, Node b);
	public abstract Double getSimilarity_byID(Integer a, Integer b);
	
	public Integer getNumDocs()
	{
		return numSpeeches;
	}
	
}
