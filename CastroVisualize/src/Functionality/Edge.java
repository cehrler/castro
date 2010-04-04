// Name        : Edge.java
// Author      : Carsten Ehrler; Michal Richter
// Version     :
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
// Description : Class for representing of the graph edge
//               
// Version     : 2.5.1
//============================================================================
package Functionality;

public class Edge implements Comparable<Object> {
	private Node node1;
	private Node node2;
	private Double strength;
	
	public Edge(Node u, Node v, Double strength) {
		this.node1 = u;
		this.node2 = v;
		this.strength = strength;
	}
	
	public Node getNode1() {
		return node1;
	}
	
	public Node getNode2() {
		return node2;
	}
	
	public Double getStrength() {
		return this.strength;
	}

	public int compareTo(Object obj) {
		if(obj == null) {
			return -1;
		}
		if(obj instanceof Edge) {
			Edge that = (Edge)obj;
			return that.strength.compareTo(this.strength);
		}
		return -1;
	}

	@Override
	public String toString() {
		return "";// + this.strength;
	}
}
