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
