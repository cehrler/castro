// Name        : VMindex.java
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
//Description  : Vector model index class - used for sorting of retrieved speeches(Nodes)
//               and eventually for computing similarity matrix
//===============================================================================================

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
	private List<Double> docLengths;
	
	private static boolean newVersion = false;
	
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
	
	public Double GetDocLength(int docID)
	{
		return docLengths.get(docID);
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
			if (newVersion)
			{
				return sparseMat.get(ind) / docLengths.get(docID);
			}
			else
			{
				return sparseMat.get(ind);
			}
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
	        System.err.println("Loading index file: " + filename);
	        if (numSpeeches > 10000 || numSpeeches < 0)
	        {
	        	throw new Exception("Wrong format or our database inflated!");
	        }
	        
	        neCells = new ArrayList<Set<Integer> >();
	        
	        numNEs = os.readInt();
	  	  
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
		
		int index;
		double pomSum;
		docLengths = new ArrayList<Double>();
		for (int i = 0; i < numSpeeches; i++)
		{
			docLengths.add(0.0);
			pomSum = 0;
			for (Iterator<Integer> it = neCells.get(i).iterator(); it.hasNext(); )
			{
				index = it.next();
				pomSum += (this.GetValue(i, index)) * (this.GetValue(i, index)); 
			}
			
			docLengths.set(i, Math.sqrt(pomSum));
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
