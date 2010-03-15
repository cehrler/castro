package Functionality;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Graph {
	
	private List<Node> nodes;
	private List<Edge> edges;
	
	private Map<Node, Map<Node, Edge> > edgeMap;
	
	private double dottedEdgeThreshold = 0.5;
	private double normalEdgeThreshold = 0.65;
	private double thickEdgeThreshold = 0.75;
	private SimMatrix simMatrix;
	
	public double getDottedEdgeThreshold()
	{
		return dottedEdgeThreshold;
	}
	
	public double getNormalEdgeThreshold()
	{
		return normalEdgeThreshold;
	}
	
	public double getThickEdgeThreshold()
	{
		return thickEdgeThreshold;
	}
	
	public void SetEdgeParams(SimMatrix _simMatrix, double _dottedEdgeThreshold, double _normalEdgeThreshold, double _thickEdgeThreshold)
	{
		dottedEdgeThreshold = _dottedEdgeThreshold;
		normalEdgeThreshold = _normalEdgeThreshold;
		thickEdgeThreshold = _thickEdgeThreshold;
		simMatrix = _simMatrix;
		createEdges();
	}
	
	public void SetNormalEdgeThreshold(double _normalEdgeThreshold)
	{
		normalEdgeThreshold = _normalEdgeThreshold;
	}
	
	public void SetDottedEdgeThreshold(double _dottedEdgeThreshold)
	{
		dottedEdgeThreshold = _dottedEdgeThreshold;
		createEdges();
	}
	
	public void SetThickEdgeThreshold(double _thickEdgeThreshold)
	{
		thickEdgeThreshold = _thickEdgeThreshold;
	}
	
	private Map<Integer,Node> nodeMap;
	
	public Graph() {
		nodes = new ArrayList<Node>();
		nodeMap = new HashMap<Integer,Node>();
	}

	private void createEdges()
	{
		Double similarity;
		edges = new ArrayList<Edge>();
		edgeMap = new HashMap<Node, Map<Node, Edge> >();
		
		for (int i = 0; i < nodes.size(); i++)
		{
			edgeMap.put(nodes.get(i), new HashMap<Node, Edge>());
		}
		
		for (int i = 0; i < nodes.size(); i++)
		{
			nodes.get(i).clearNeighborsSet();
		}
		
		for (int i = 0; i < nodes.size(); i++)
		{
			//Node n = ln.get(i);
			for (int j = i + 1; j < nodes.size(); j++)
			{
				//if (i == j) continue;
				similarity = simMatrix.getSimilarity(nodes.get(i), nodes.get(j));
								
				if (similarity.compareTo(dottedEdgeThreshold) > 0)
				{					
					Edge e = new Edge(nodes.get(i), nodes.get(j), similarity);
					edges.add(e);
					nodes.get(i).addEdge(nodes.get(j), similarity);
					nodes.get(j).addEdge(nodes.get(i), similarity);
					edgeMap.get(nodes.get(i)).put(nodes.get(j), e);
					edgeMap.get(nodes.get(j)).put(nodes.get(i), e);
				}
			}
			
		}

	}
	
	public Graph(List<Node> ln, SimMatrix _simMatrix, double _dottedEdgeThreshold, double _normalEdgeThreshold, double _thickEdgeThreshold) {
		
		nodes = ln;
		
		nodeMap = new HashMap<Integer,Node>();
		for (int i = 0; i < nodes.size(); i++)
		{
			nodeMap.put(nodes.get(i).getSpeech_id(), nodes.get(i));
		}
		
		dottedEdgeThreshold = _dottedEdgeThreshold;
		normalEdgeThreshold = _normalEdgeThreshold;
		thickEdgeThreshold = _thickEdgeThreshold;
		simMatrix = _simMatrix;
		createEdges();
		
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	public Set<Node> getNodesSet() {
		Set<Node> sn = new HashSet<Node>();
		for (int i = 0; i < nodes.size(); i++)
		{
			sn.add(nodes.get(i));
		}
		
		return sn;
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public Node getNodeById(Integer nodeId)
	{
		return nodeMap.get(nodeId);
	}

	public int getListId(Node n)
	{
		for (int i = 0; i < nodes.size(); i++)
		{
			if (nodes.get(i).equals(n)) return i;
		}
		return 0;
	}

	public void setCenter(Node n)
	{
		n.depth = 0;

		for (int i = 0; i < nodes.size(); i++)
		{
			nodes.get(i).visited = 0;
		}
		
		int nodePointer = 0;
		Node currNode;
		List<Node> proceedNodes = new ArrayList<Node>();
		proceedNodes.add(n);
		
		while (nodePointer < proceedNodes.size())
		{
			currNode = proceedNodes.get(nodePointer);
			for (Iterator<Node> it = currNode.getNeighbors().iterator(); it.hasNext(); )
			{
				Node neib = it.next();
				
				if (neib.visited == 0)
				{
					neib.visited = 1;
					neib.depth = currNode.depth + 1;
					proceedNodes.add(neib);
				}
			}
			currNode.visited = 2;
			nodePointer++;
		}
		
		for (int i = 0; i < nodes.size(); i++)
		{
			if (nodes.get(i).visited == 0)
			{
				nodes.get(i).depth = 1000;
			}
		}
	}
	
	private Edge getEdge(Node n1, Node n2)
	{
		if (edgeMap.containsKey(n1) && edgeMap.get(n1).containsKey(n2))
		{
			return edgeMap.get(n1).get(n2);
		}
		else
		{
			return null;
		}
	}
	
	public boolean edgeIsDotted(Node n1, Node n2)
	{
		Edge e = getEdge(n1, n2);
		if (e == null) return false;
		if (e.getStrength() < normalEdgeThreshold)
			return true;
		else
			return false;
	}
	
	public boolean edgeIsNormal(Node n1, Node n2)
	{
		Edge e = getEdge(n1, n2);
		if (e == null) return false;
		if (e.getStrength() >= normalEdgeThreshold && e.getStrength() < thickEdgeThreshold)
			return true;
		else
			return false;
	}
	
	public boolean edgeIsDouble(Node n1, Node n2)
	{
		Edge e = getEdge(n1, n2);
		if (e == null) return false;
		if (e.getStrength() >= thickEdgeThreshold)
			return true;
		else
			return false;
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
