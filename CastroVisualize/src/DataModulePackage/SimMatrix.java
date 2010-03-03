package DataModulePackage;

import java.util.Map;


public abstract class SimMatrix {
	
	protected Integer numSpeeches;
	
	public abstract Double getSimilarity(Integer a, Integer b);
	
	public Integer getNumDocs()
	{
		return numSpeeches;
	}
	
}
