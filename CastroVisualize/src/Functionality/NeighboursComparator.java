// Name        : NeighboursComparator.java
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
// Description : Simple comparator class that is useful when it is needed to sort the documents 
//               according to their similarity to the focused document.
//============================================================================

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
