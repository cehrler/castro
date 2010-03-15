//============================================================================
// Name        : SimMatrix.java
// Author      : Michal Richter, Michalisek
// Version     :
// Copyright   : This product is licensed under Fidel Castro restricted software license. 
//               Use of any kind is considered a breach of copyright law. 
//               You are not allowed to use this for any purpose; neither commercial 
//               nor non-commercial.
// Description : Abstract class for similarity matrix
//============================================================================


package Functionality;


public abstract class SimMatrix {
	
	protected Integer numSpeeches;
	
	public abstract Double getSimilarity(Node a, Node b);
	public abstract Double getSimilarity_byID(Integer a, Integer b);
	
	public Integer getNumDocs()
	{
		return numSpeeches;
	}
	
}
