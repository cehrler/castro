// Name        : SimMatrix.java
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
//Description  : Interpolation of similarity matrices
//               
//===============================================================================================


package Functionality;

import java.util.List;


public class SimMatrixInterp extends SimMatrix {

	List<SimMatrix> lMatrix;
	List<Double> lWeight;
	
	@Override
	public Double getSimilarity_byID(Integer a, Integer b) 
	{
		Double sum = 0.0;
		for (int i = 0; i < lMatrix.size(); i++)
		{
			sum += lWeight.get(i) * lMatrix.get(i).getSimilarity_byID(a, b);
		}
		return sum;
	}
	
	@Override
	public Double getSimilarity(Node a, Node b) 
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
		
		numSpeeches = _lMatrix.get(0).getNumDocs();
	}

}
