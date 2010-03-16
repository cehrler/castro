package Functionality;

import java.util.Comparator;

public class NeighboursComparator implements Comparator<Node> {

	private Node me;
	
	public NeighboursComparator(Node _me)
	{
		me = _me;
	}
	
	public int compare(Node neib1, Node neib2) {
		return me.getNeighborsMap().get(neib1).compareTo(me.getNeighborsMap().get(neib1));
	}

}
