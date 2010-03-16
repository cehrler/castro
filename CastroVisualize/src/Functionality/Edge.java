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
