// Name        : Graph.java
// Author      : Carsten Ehrler, Michal Richter
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
// Description : The similarity graph representation
//               
// Version     : 2.5.1
//============================================================================

package Functionality;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import GUI.SettingsWindow;

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
	
	public int GetNumberOfClusters()
	{
		int maxID = -1;
		
		for (int i = 0; i < nodes.size(); i++)
		{
			if (nodes.get(i).GetCluster() > maxID)
			{
				maxID = nodes.get(i).GetCluster();
			}
		}
		
		return maxID + 1;
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
			//System.err.println("edge " + i + ": " + e.getStrength());
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
	
	public static Graph createGraphThreshold(List<Node> ln, SimMatrix _simMatrix, double _normalEdgeThreshold, double _dottedEdgeAbsoluteMultiplier, double _thickEdgeAbsoluteMultiplier, int maxClusters)
	{
		Graph gr = new Graph();
		gr.nodes = ln;
				
		gr.nodeMap = new HashMap<Integer,Node>();
		for (int i = 0; i < gr.nodes.size(); i++)
		{
			gr.nodeMap.put(gr.nodes.get(i).getSpeech_id(), gr.nodes.get(i));
		}
		
		gr.dottedEdgeThreshold = _normalEdgeThreshold * _dottedEdgeAbsoluteMultiplier;
		gr.normalEdgeThreshold = _normalEdgeThreshold;
		gr.thickEdgeThreshold = _normalEdgeThreshold * _thickEdgeAbsoluteMultiplier;
		
		gr.simMatrix = _simMatrix;

		if (maxClusters > 0)
		{
			gr.createEdgesDensity((int)(Math.round(ln.size() * SettingsWindow.ChineseWhisperClustering_tempGraphDensity)));
			ChineseWhisperClustering.GetImplementation().Evaluate(gr, SettingsWindow.maxNumClusters, SettingsWindow.ChineseWhisperClustering_numberOfIterations);
		}
		else
		{
			for (int i = 0; i < ln.size(); i++)
			{
				ln.get(i).SetCluster(-1);
			}
		}
		
		
		gr.createEdgesThreshold();
		
		return gr;
		
	}
	
	public void ChangeEdgeThresholds(double _normalEdgeThreshold, double _dottedEdgeAbsoluteMultiplier, double _thickEdgeAbsoluteMultiplier)
	{
		dottedEdgeThreshold = _normalEdgeThreshold * _dottedEdgeAbsoluteMultiplier;
		normalEdgeThreshold = _normalEdgeThreshold;
		thickEdgeThreshold = _normalEdgeThreshold * _thickEdgeAbsoluteMultiplier;
		
		createEdgesThreshold();
		
	}
	
	public void ChangeEdgeDensities(double _edgeDensity, double _normalEdgeRelativeMultiplier, double _thickEdgeRelativeMultiplier)
	{
		//System.err.println("edge density: " + _edgeDensity);
		int numEdges = (int)Math.round( nodes.size() * _edgeDensity );
			
		createEdgesDensity(numEdges);
		
		double sumStrength = 0;
		for (int i = 0; i < edges.size(); i++)
		{
			sumStrength += edges.get(i).getStrength();
		}
		
		double avgStrength = sumStrength / edges.size();
		
		if (numEdges > 0)
		{
			dottedEdgeThreshold = edges.get(0).getStrength();
			normalEdgeThreshold = avgStrength * _normalEdgeRelativeMultiplier;
			thickEdgeThreshold = avgStrength * _thickEdgeRelativeMultiplier;
		}
		else
		{
			dottedEdgeThreshold = 0.4;
			normalEdgeThreshold = 0.5;
			thickEdgeThreshold = 0.6;			
		}
		
		//System.err.println("---dottedEdge: " + dottedEdgeThreshold + ", normalEdge: " + normalEdgeThreshold + ", thickEdge: " + thickEdgeThreshold);
		
	}
	
	public static Graph createGraphDensity(List<Node> ln, SimMatrix _simMatrix, double _edgeDensity, double _normalEdgeRelativeMultiplier, double _thickEdgeRelativeMultiplier, int maxClusters) {
		
		Graph gr = new Graph();
		gr.nodes = ln;
		
		gr.nodeMap = new HashMap<Integer,Node>();
		for (int i = 0; i < gr.nodes.size(); i++)
		{
			gr.nodeMap.put(gr.nodes.get(i).getSpeech_id(), gr.nodes.get(i));
		}

		gr.simMatrix = _simMatrix;
		
		if (maxClusters > 0)
		{
			gr.createEdgesDensity((int)(Math.round(ln.size() * SettingsWindow.ChineseWhisperClustering_tempGraphDensity)));
			ChineseWhisperClustering.GetImplementation().Evaluate(gr, SettingsWindow.maxNumClusters, SettingsWindow.ChineseWhisperClustering_numberOfIterations);
		}
		else
		{
			for (int i = 0; i < ln.size(); i++)
			{
				ln.get(i).SetCluster(-1);
			}
		}
		
		gr.ChangeEdgeDensities(_edgeDensity, _normalEdgeRelativeMultiplier, _thickEdgeRelativeMultiplier);
		
		//System.err.println("dottedEdgeThreshold: " + gr.dottedEdgeThreshold);
		//System.err.println("normalEdgeThreshold: " + gr.normalEdgeThreshold);
		//System.err.println("thickEdgeThreshold: " + gr.thickEdgeThreshold);
		
		
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
