//============================================================================
//Name        : VMindex.java
//Author      : Michal Richter, Michalisek
//Version     :
//Copyright   : This product is licensed under Fidel Castro restricted software license. 
//              Use of any kind is considered a breach of copyright law. 
//              You are not allowed to use this for any purpose; neither commercial 
//              nor non-commercial.
//Description : Vector model index class - used for sorting of retrieved speeches(Nodes)
//              and eventually for computing similarity matrix
//============================================================================


package Functionality;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
	
	public static void ConvertTextToBin(String inputfile, String outputfile)
	{
		System.err.println("Converting " + inputfile + " to " + outputfile);

		try {
			FileInputStream ios =  new FileInputStream(inputfile);
		    DataInputStream in = new DataInputStream(ios);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));

	        DataOutputStream os = new DataOutputStream(new FileOutputStream(outputfile));
	        
	        
	        String s;
	        Integer numSpeeches;
	        Integer numNEs;
	        
	        s = br.readLine();
	        
	        if (s.contains("speeches:"))
	        {
	        	numSpeeches = Integer.parseInt(s.substring(9));
	        	os.writeInt(numSpeeches);
	        	//System.err.println("Integer: " + numSpeeches);
	        }
	        else
	        {
	        	throw new Exception("invalid first row!");
	        }
	        
	        s = br.readLine();
	        
	        if (s.contains("NEs:"))
	        {
	        	numNEs = Integer.parseInt(s.substring(4));
	        	os.writeInt(numNEs);
	        	//System.err.println("Integer: " + numNEs);
	        }
	        else
	        {
	        	throw new Exception("invalid second row!");
	        }
	        
	        int pom1, pom2;
	        int docID, termID;
	        double pomVal;
	        
	        List<Map<Integer, Double>> pomMat = new ArrayList<Map<Integer, Double>>();
	        
	        for (int i = 0; i < numSpeeches; i++)
	        {
	        	pomMat.add(new HashMap<Integer, Double>());
	        }
	        
	        while (br.ready())
	        {
	        	s = br.readLine();
	        	
	        	pom1 = s.indexOf(',', 0);
	        	docID = Integer.parseInt(s.substring(1, pom1));
	        	
	        	pom2 = s.indexOf('=', 0);
	        	
	        	termID = Integer.parseInt(s.substring(pom1 + 1, pom2 - 1));
	        	pomVal = Double.parseDouble(s.substring(pom2 + 1));
	        	
	        	pomMat.get(docID).put(termID, pomVal);
	        	
	        	//System.err.println(s.substring(1, pom1) + " " + s.substring(pom1 + 1, pom2 - 1) + " " + s.substring(pom2 + 1));
	        	//System.exit(1);
	        }
	        
	        for (int i = 0; i < numSpeeches; i++)
	        {
	            Map<Integer, Double> hm = pomMat.get(i);
	        	Integer count = 0;

		        for (Iterator<Integer> it = hm.keySet().iterator(); it.hasNext(); )
		        {
		        	count++;
		        	it.next();
		        }
		        os.writeInt(count);
		        
		        for (Iterator<Integer> it = hm.keySet().iterator(); it.hasNext(); )
		        {		        	
		        	Integer pomInt = it.next();
		        	Double val2 = hm.get(pomInt);
		        	os.writeInt(pomInt);
		        	os.writeDouble(val2);
		        	//System.err.println("Double: " + val2);
		        }
		        
		        
		        //throw new Exception("That's enough");
		    }
	        
	        if (br.ready())
	        {
	        	throw new Exception("eof expected, but there is something left!");
	        }
	        
	        os.close();
	        
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	
	}

}
