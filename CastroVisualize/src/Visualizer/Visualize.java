package Visualizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout3d.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import Functionality.*; 

public class Visualize {
	
	Graph<Functionality.Node, Functionality.Edge> qt;
	
	@SuppressWarnings("unchecked")
	public Visualize(Functionality.Graph g) {
		qt = new SparseMultigraph(); 
		Iterator<Functionality.Node> nodeIterator = g.getNodes().iterator();
		while(nodeIterator.hasNext()) {
			qt.addVertex(nodeIterator.next());
		}
		
		Iterator<Functionality.Edge> edgeIterator = g.getEdges().iterator();
		while(edgeIterator.hasNext()) {
			Functionality.Edge currEdge = edgeIterator.next();
			qt.addEdge(currEdge, currEdge.getNode1(), currEdge.getNode2());
		}
		
		
	}
	
	public JComponent drawGraph() {
		// System.out.println("The graph qt"+qt.toString()); // DEBUG
		
		Layout<Functionality.Node, Functionality.Edge> layout = new SpringLayout2(this.qt);
		//Layout<Node, Edge> layout = new StaticLayout(this.qt);
		layout.setSize(new Dimension(400,400));
		VisualizationViewer<Functionality.Node,Functionality.Edge> vv = new VisualizationViewer<Functionality.Node,Functionality.Edge>(layout);
		
		vv.setPreferredSize(new Dimension(450,450));
		// Show vertex and edge labels
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		
		// Create a graph mouse and add it to the visualization component
		//DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		
		VertexMouseMenu vMouseMenu = new VertexMouseMenu();
		JPopupMenu vertexMenu = new MyVertexMenu();
		vMouseMenu.setVertexPopup(vertexMenu);
		gm.add(vMouseMenu);
		
		vv.setGraphMouse(gm);
		
		
		return vv;
	}
	
	
	
	
/*	private JMenu createMenuBar() {
			JMenuBar	menuBar = new JMenuBar();
			JMenu		menuMore = new JMenu("Information");
			JMenuItem	menuNodeDetails = new JMenuItem("Node Details") ;
			
			menuMore.add(menuNodeDetails);
			
			
	}*/
	
	
	public static void main(String[] args) {
		/*Functionality.Node n1 = new Functionality.Node(1);
		Functionality.Node n2 = new Functionality.Node(2);
		Functionality.Node n3 = new Functionality.Node(3);
		Functionality.Node n4 = new Functionality.Node(4);
		Functionality.Node n5 = new Functionality.Node(5);
		Functionality.Node n6 = new Functionality.Node(6);

		List ns = new ArrayList(4); //change to getVertices()
		ns.add(n1);
		ns.add(n2);
		ns.add(n3);
		ns.add(n4);
		ns.add(n5);
		ns.add(n6);
		
		
		List es = new ArrayList(5); //change to getVertices()
		es.add(new Functionality.Edge(n1,n2,1.0));
		es.add(new Functionality.Edge(n2,n3,2.0));
		es.add(new Functionality.Edge(n3,n4,0.5));
		es.add(new Functionality.Edge(n4,n1,8.0));
		es.add(new Functionality.Edge(n4,n2,0.5463));
		es.add(new Functionality.Edge(n5,n6,0.7));
		
		Functionality.Graph g =new Functionality.Graph(ns,es);*/
		
		Functionality.DataModule.Init(IndexTypeEnum.TF);

		List<String> queryTerms = new ArrayList<String>();
		List<Double> termWeights = new ArrayList<Double>();
		
		
		
		queryTerms.add("PRENSA LATINA"); termWeights.add(1.0);
		queryTerms.add("Conrado Benitez"); termWeights.add(1.0);
		queryTerms.add("Salvador"); termWeights.add(1.0);
		queryTerms.add("Chile"); termWeights.add(1.0);
		queryTerms.add("Manual Ascunce"); termWeights.add(0.0);
		
		
		Functionality.Graph G = DataModule.getGraph("NULL", "NULL", "NULL", "NULL", "NULL", queryTerms, termWeights, 20, SimMatrixEnum.AllWeightedEqually, 0.3);

		
		Visualize visu = new Visualize(G);
		visu.drawGraph();
	}
}
