import java.util.List;
import java.util.ArrayList;

public class Graph {
	
	private List<Node> nodes;
	
	private List<Edge> edges;
	
	public Graph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	
	public Graph(List<Node> ln, List<Edge> le) {
		nodes = ln;
		edges = le;
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	/*public Node addNode(Integer id) {
		Node node = new Node(id);
		nodes.add(node);
		return node;
	}
	
	public Edge addEdge(Node u, Node v, Double strength) {
		assert nodes.contains(u);
		assert nodes.contains(v);
		
		Edge edge = new Edge(u, v, strength);
		edges.add(edge);
		
		u.addEdge(v, strength);
		v.addEdge(u, strength);
		
		return edge;
	}*/
}
