// Name        : NamedEntity.java
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
// Description : The class which represents the named entities
//               
//============================================================================

package Functionality;

public class NamedEntity implements Comparable<NamedEntity> {
	private boolean expanded;
	private double weight;
	private String text;
	private NamedEntityEnum neType;
	private int ID;
	
	public boolean getExpanded()
	{
		return expanded;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public String getText()
	{
		return text;
	}
	
	public NamedEntityEnum getNEType()
	{
		return neType;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public NamedEntity(int _ID, String _text, NamedEntityEnum _neType, double _weight, boolean _expanded)
	{
		text = _text;
		neType = _neType;
		weight = _weight;
		expanded = _expanded;
		ID = _ID;
	}

	public int compareTo(NamedEntity arg0) {
		if (this.ID == arg0.ID)
			return 0;
		else if (this.ID > arg0.ID)
			return -1;
		else
			return 1;
	}
}
