package DataModulePackage;

import java.util.ArrayList;
import java.util.List;


public class SimMatrixInterp extends SimMatrix {

	List<SimMatrix> lMatrix;
	List<Double> lWeight;
	
	@Override
	public Double getSimilarity(Integer a, Integer b) 
	{
		Double sum = 0.0;
		for (int i = 0; i < lMatrix.size(); i++)
		{
			sum += lWeight.get(i) * lMatrix.get(i).getSimilarity(a, b);
		}
		return sum;
	}
	
	public SimMatrixInterp(List<SimMatrix> _lMatrix, List<Double> _lWeight)
	{
		
		try {
			
			if (_lMatrix.size() != _lWeight.size())
			{
				throw new Exception("SimMatrixInterp: List sizes are not equal!");
			}
			
			lMatrix = _lMatrix;
			lWeight = _lWeight;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
