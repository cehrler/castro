package DataModulePackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class SimMatrixElem extends SimMatrix {
	
	private List<List<Double> > matrix;
	

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
	
	public static SimMatrixElem CountFromVMindex(VMindex index)
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
		
		for (int i = 0; i < simMat.numSpeeches; i++)
		{
			if (i % 20 == 0) System.out.println("i = " + i);

			Set<Integer> nonzeroI = index.GetNonzeroCells(i);
			for (int j = i + 1; j < simMat.numSpeeches; j++)
			{
				//Set<Integer> nonzeroJ = index.GetNonzeroCells(j);
				sum = 0.0;
				
				for (Iterator<Integer> it = nonzeroI.iterator(); it.hasNext();  )
				{
					pomInt = it.next();					
					sum += Math.min(index.GetValue(i, pomInt), index.GetValue(j, pomInt));
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
		
		return b / 256.0;
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
