package DataModulePackage;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class VMindex {
	
	private Integer numSpeeches;
	private Integer numNEs;
	
	private Map<Integer, Double> sparseMat;
	private List<Set<Integer> > neCells;
	//just because there is no standard Pair class in java
	private Integer dummyMap(int a, int b)
	{
		return a * numNEs + b;
	}
	
	public Integer NumDocs()
	{
		return numSpeeches;
	}
	
	public Integer NumTerms()
	{
		return numNEs;
	}
	
	public Double GetValue(Integer docID, Integer termID)
	{
		Integer ind = dummyMap(docID, termID);
		
		if (!sparseMat.containsKey(ind))
		{
			return 0.0;
		}
		else
		{
			return sparseMat.get(ind);
		}
	}
	
	public Set<Integer> GetNonzeroCells(Integer docID)
	{
		return neCells.get(docID);
	}
	
	
	public VMindex(String filename)
	{
		sparseMat = new HashMap<Integer, Double>();
		Integer pomInt;
		Double pomDouble;
		
		try {
	        DataInputStream os = new DataInputStream(new FileInputStream(filename));
			
	        numSpeeches = os.readInt();
	        System.err.println("numSpeeches: " + numSpeeches);
	        if (numSpeeches > 10000 || numSpeeches < 0)
	        {
	        	throw new Exception("Wrong format or our database inflated!");
	        }
	        
	        neCells = new ArrayList<Set<Integer> >();
	        
	        numNEs = os.readInt();
	        System.err.println("numNEs: " + numNEs);
	  	  
	        if (numNEs > 10000000 || numNEs < 0)
	        {
	        	throw new Exception("Wrong format or really big number of entities!");
	        }
	        
	        for (int i = 0; i < numSpeeches; i++)
	        {
	        	neCells.add(new HashSet<Integer>());
	        	
	        	int numNEcurrSpeech = os.readInt();
	        	
	        	if (numNEcurrSpeech < 0 || numNEs.compareTo(numNEcurrSpeech) < 0)
	        	{
	        		throw new Exception("Wrong input format: numNEcurrSpeech = " + numNEcurrSpeech);
	        	}
	        		        	
	        	
	        	for (int j = 0; j < numNEcurrSpeech; j++)
	        	{
	        		//System.err.println(j + "\n");
	        		pomInt = os.readInt();
	        		pomDouble = os.readDouble();
	        		
	        		if (pomDouble.compareTo(0.0) < 0 || pomDouble.compareTo(1.1) > 1)
	        		{
	        			throw new Exception("Wrong input format: double value out of range: " + pomDouble.toString());
	        		}
	        		
	        		if (pomInt.compareTo(0) < 0 || pomInt.compareTo(numNEs) >= 0)
	        		{
	        			throw new Exception("Wrong input format: entity id value out of range: " + pomInt.toString());
	        		}
	        		sparseMat.put(dummyMap(i, pomInt), pomDouble);
	        		neCells.get(i).add(pomInt);
	        	}
	        }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
