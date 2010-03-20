package Functionality;
import java.util.Collections;
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
		createEdgesThreshold();
	}
	
	public void SetNormalEdgeThreshold(double _normalEdgeThreshold)
	{
		normalEdgeThreshold = _normalEdgeThreshold;
	}
	
	public void SetDottedEdgeThreshold(double _dottedEdgeThreshold)
	{
		dottedEdgeThreshold = _dottedEdgeThreshold;
		createEdgesThreshold();
	}
	
	public void SetThickEdgeThreshold(double _thickEdgeThreshold)
	{
		thickEdgeThreshold = _thickEdgeThreshold;
	}
	
	private Map<Integer,Node> nodeMap;
	
	private Graph() {
		nodes = new ArrayList<Node>();
		nodeMap = new HashMap<Integer,Node>();
	}

	private void createEdgesDensity(int numEdges)
	{
		Double similarity;
		List<Edge> edgesBle = new ArrayList<Edge>();
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
													
					Edge e = new Edge(nodes.get(i), nodes.get(j), similarity);
					edgesBle.add(e);
					//nodes.get(i).addEdge(nodes.get(j), similarity);
					//nodes.get(j).addEdge(nodes.get(i), similarity);
					//edgeMap.get(nodes.get(i)).put(nodes.get(j), e);
					//edgeMap.get(nodes.get(j)).put(nodes.get(i), e);
			}
			
		}
		
		Collections.sort(edgesBle);
		
		for (int i = 0; i < Math.min(edgesBle.size(), numEdges); i++)
		{
			Edge e = edgesBle.get(i);
			System.err.println("edge " + i + ": " + e.getStrength());
			edges.add(e);
			e.getNode1().addEdge(e.getNode2(), e.getStrength());
			e.getNode2().addEdge(e.getNode1(), e.getStrength());
			edgeMap.get(e.getNode1()).put(e.getNode2(), e);
			edgeMap.get(e.getNode2()).put(e.getNode1(), e);
		}
		
		Collections.reverse(edges);
		
		
	}
	
	private void createEdgesThreshold()
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
	
	public static Graph createGraphThreshold(List<Node> ln, SimMatrix _simMatrix, double _dottedEdgeThreshold, double _normalEdgeThreshold, double _thickEdgeThreshold)
	{
		Graph gr = new Graph();
		gr.nodes = ln;
				
		gr.nodeMap = new HashMap<Integer,Node>();
		for (int i = 0; i < gr.nodes.size(); i++)
		{
			gr.nodeMap.put(gr.nodes.get(i).getSpeech_id(), gr.nodes.get(i));
		}
		
		gr.dottedEdgeThreshold = _dottedEdgeThreshold;
		gr.normalEdgeThreshold = _normalEdgeThreshold;
		gr.thickEdgeThreshold = _thickEdgeThreshold;
		
		gr.simMatrix = _simMatrix;
		gr.createEdgesThreshold();
		
		return gr;
		
	}
	
	public void ChangeEdgeDensities(double _dottedEdgeDensity, double _normalEdgeDensity, double _thickEdgeDensity)
	{
		System.err.println("dotted: " + _dottedEdgeDensity + ", normal: " + _normalEdgeDensity + ", thick: " + _thickEdgeDensity);
		int numEdges = (int)Math.round( nodes.size() * (_dottedEdgeDensity + _normalEdgeDensity + _thickEdgeDensity) );
		
		int normalEdgeIndex = (int)Math.round( nodes.size() * _dottedEdgeDensity);
		int thickEdgeIndex = (int)Math.round( nodes.size() * (_normalEdgeDensity + _dottedEdgeDensity));
		
		createEdgesDensity(numEdges);
		
		if (numEdges == 0)
		{
			dottedEdgeThreshold = 0.4;
			normalEdgeThreshold = 0.5;
			thickEdgeThreshold = 0.6;
		}
		else
		{
			dottedEdgeThreshold = edges.get(0).getStrength();
			if (normalEdgeIndex < numEdges)
			{
				if (normalEdgeIndex > 0)
				{
					normalEdgeThreshold = (edges.get(normalEdgeIndex - 1).getStrength() + edges.get(normalEdgeIndex).getStrength()) / 2;
				}
				else
				{
					normalEdgeThreshold = edges.get(normalEdgeIndex).getStrength();
				}
				
				if (thickEdgeIndex < numEdges)
				{
					if (thickEdgeIndex > 0)
					{
						thickEdgeThreshold = (edges.get(thickEdgeIndex - 1).getStrength() + edges.get(thickEdgeIndex).getStrength()) / 2;
					}
					else
					{
						thickEdgeThreshold = edges.get(thickEdgeIndex).getStrength();
					}
				}
				else
				{
					thickEdgeThreshold = 1;
				}
			}
			else
			{
				normalEdgeThreshold = 1;
			}
		}
		
	}
	
	public static Graph createGraphDensity(List<Node> ln, SimMatrix _simMatrix, double _dottedEdgeDensity, double _normalEdgeDensity, double _thickEdgeDensity) {
		
		Graph gr = new Graph();
		gr.nodes = ln;
		
		gr.nodeMap = new HashMap<Integer,Node>();
		for (int i = 0; i < gr.nodes.size(); i++)
		{
			gr.nodeMap.put(gr.nodes.get(i).getSpeech_id(), gr.nodes.get(i));
		}

		gr.simMatrix = _simMatrix;
		gr.ChangeEdgeDensities(_dottedEdgeDensity, _normalEdgeDensity, _thickEdgeDensity);
		
		System.err.println("dottedEdgeThreshold: " + gr.dottedEdgeThreshold);
		System.err.println("normalEdgeThreshold: " + gr.normalEdgeThreshold);
		System.err.println("thickEdgeThreshold: " + gr.thickEdgeThreshold);
		
		
		return gr;
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
