//============================================================================
//Name        : SimMatrixElem.java
//Author      : Michal Richter, Michalisek
//Version     :
//Copyright   : This product is licensed under Fidel Castro restricted software license. 
//              Use of any kind is considered a breach of copyright law. 
//              You are not allowed to use this for any purpose; neither commercial 
//              nor non-commercial.
//Description : Similarity matrix implementation - initialized by factory
//              functions - LoadFromFile, CountFromVMIndex
//============================================================================

package Functionality;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class SimMatrixElem extends SimMatrix {
	
	public enum SimilarityMeasure
	{
		manhattan, euclidean, cosine
	}
	
	private List<List<Double> > matrix;
	
	private static List<Double> convertArray = null;

	private SimMatrixElem() 
	{
		if (convertArray == null)
		{
			convertArray = new ArrayList<Double>();
			
			for (int i = 0; i < 257; i++)
			{
				convertArray.add((double)i / 256.0);
			}
		}
	}
	
	private void setSim(Integer a, Integer b, Double val)
	{
		matrix.get(a).set(b, val);
	}
	
	@Override
	public Double getSimilarity_byID(Integer a, Integer b)
	{
		try 
		{
			if (a.compareTo(numSpeeches) >= 0 || b.compareTo(numSpeeches) >= 0)
			{
				throw new Exception("Error: getSimilarity: greater indices than numSpeeches: numSpeeches = " + numSpeeches + ", a = " + a + ", b = " + b);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return matrix.get(a).get(b);
		
	}
	
	@Override
	public Double getSimilarity(Node a, Node b)
	{
		Integer aID = a.getSpeech_id();
		Integer bID = b.getSpeech_id();
		return getSimilarity_byID(aID, bID);
	}
	

	
	public static SimMatrixElem LoadFromFile(String filename)
	{
		Double pomDouble;
		SimMatrixElem simMat = new SimMatrixElem();
		
		System.err.println("SimMatrixElem: LoadFromFile(" + filename + ")");
		
		try {
	        DataInputStream is = new DataInputStream(new FileInputStream(filename));
			
	        simMat.numSpeeches = is.readInt();
	        
	        if (simMat.numSpeeches.compareTo(0) < 0 || simMat.numSpeeches.compareTo(100000) > 0)
	        {
	        	throw new Exception("LoadFromFile: bad input format or very big matrix beeing loaded");
	        }
			simMat.matrix = new ArrayList<List<Double> >();
	        
			for (int i = 0; i < simMat.numSpeeches; i++)
			{
				simMat.matrix.add(new ArrayList<Double>());
				for (int j = 0; j < simMat.numSpeeches; j++)
				{
					simMat.matrix.get(i).add(0.0);
				}
			}	        
	        
	        for (int i = 0; i < simMat.numSpeeches; i++)
	        	for (int j = i; j < simMat.numSpeeches; j++)
	        	{
	        		pomDouble = ByteToDouble(is.readByte());
	        		simMat.matrix.get(i).set(j, pomDouble);
	        		simMat.matrix.get(j).set(i, pomDouble);
	        	}
	        
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return simMat;
	}
	
	public static SimMatrixElem CountFromVMindex(VMindex index, SimilarityMeasure simMeasure)
	{
		SimMatrixElem simMat = new SimMatrixElem();
		
		System.out.println("CountFromVMindex");
		
		simMat.numSpeeches = index.NumDocs();
		
		Double sum;
		
		simMat.matrix = new ArrayList<List<Double> >();
		
		for (int i = 0; i < simMat.numSpeeches; i++)
		{
			simMat.matrix.add(new ArrayList<Double>());
			for (int j = 0; j < simMat.numSpeeches; j++)
			{
				simMat.matrix.get(i).add(0.0);
			}
		}
		
		Integer pomInt;
		
		double pomBle;
		for (int i = 0; i < simMat.numSpeeches; i++)
		{
			//if (i % 20 == 0) System.out.println("i = " + i);

			Set<Integer> nonzeroI = index.GetNonzeroCells(i);
			for (int j = i + 1; j < simMat.numSpeeches; j++)
			{
				//Set<Integer> nonzeroJ = index.GetNonzeroCells(j);
				sum = 0.0;
				
				for (Iterator<Integer> it = nonzeroI.iterator(); it.hasNext();  )
				{
					pomInt = it.next();		
					
					if (simMeasure == SimilarityMeasure.manhattan)
					{
						sum += Math.min(index.GetValue(i, pomInt), index.GetValue(j, pomInt));
					}
					else if (simMeasure == SimilarityMeasure.cosine)
					{
						sum += index.GetValue(i, pomInt) * index.GetValue(j, pomInt);
					}
					else if (simMeasure == SimilarityMeasure.euclidean)
					{
						pomBle = index.GetValue(i, pomInt) - index.GetValue(j, pomInt);
						sum += pomBle * pomBle;
					}
				}
				
				if (simMeasure == SimilarityMeasure.euclidean)
				{
					Set<Integer> nonzeroJ = index.GetNonzeroCells(j);
					for (Iterator<Integer> it = nonzeroJ.iterator(); it.hasNext();  )
					{
						pomInt = it.next();
						if (index.GetNonzeroCells(i).contains(pomInt))
							continue;
						
						pomBle = index.GetValue(i, pomInt) - index.GetValue(j, pomInt);
						sum += pomBle * pomBle;
					}
					
				}
				
				
				if (simMeasure == SimilarityMeasure.cosine)
				{
					sum = sum / (index.GetDocLength(i) * index.GetDocLength(j));
				}
				
				if (simMeasure == SimilarityMeasure.euclidean)
				{
					sum = 1 - Math.sqrt(sum);
				}
				
				simMat.setSim(i, j, sum);
				simMat.setSim(j, i, sum);
			}
			
			simMat.setSim(i, i, 0.99999999);
		}
		return simMat;
	}
	
	
	private static byte DoubleToByte(Double d) throws Exception
	{
		
		int pomI = (int)Math.round(Math.floor(d * 256));
		if (pomI < 0 || pomI > 256)
		{
			throw new Exception("DoubleToByte doesn't work or nonstandard similarity matrix");
		}
		
		if (pomI == 256) pomI = 255;
		
		byte b = (byte)pomI;
		
		return b;
	}
	
	private static Double ByteToDouble(int b)
	{
		if (b < 0) b += 256;
		
		return convertArray.get(b);
	}
	
	public void SaveToFile(String filename)
	{
		
		try {
	        DataOutputStream os = new DataOutputStream(new FileOutputStream(filename));
			
	        os.writeInt(numSpeeches);
	        
	        for (int i = 0; i < numSpeeches; i++)
	        	for (int j = i; j < numSpeeches; j++)
	        	{
	        		os.writeByte(DoubleToByte(matrix.get(i).get(j) ));
	        	}
	        
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}
