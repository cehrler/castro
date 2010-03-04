package Visualizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


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
	
	public void drawGraph() {
		// System.out.println("The graph qt"+qt.toString()); // DEBUG
		
		Layout<Functionality.Node, Functionality.Edge> layout = new CircleLayout(this.qt);
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
		
		
		JFrame mainFrame = new JFrame("Castro Documents Visualizer");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().add(vv);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	
	
	
/*	private JMenu createMenuBar() {
			JMenuBar	menuBar = new JMenuBar();
			JMenu		menuMore = new JMenu("Information");
			JMenuItem	menuNodeDetails = new JMenuItem("Node Details") ;
			
			menuMore.add(menuNodeDetails);
			
			
	}*/
	
	
	public static void main(String[] args) {
		Functionality.Node n1 = new Functionality.Node(1);
		Functionality.Node n2 = new Functionality.Node(2);
		Functionality.Node n3 = new Functionality.Node(3);
		Functionality.Node n4 = new Functionality.Node(4);
		List ns = new ArrayList(4); //change to getVertices()
		ns.add(n1);
		ns.add(n2);
		ns.add(n3);
		ns.add(n4);
		
		List es = new ArrayList(5); //change to getVertices()
		es.add(new Functionality.Edge(n1,n2,1.0));
		es.add(new Functionality.Edge(n2,n3,2.0));
		es.add(new Functionality.Edge(n3,n4,0.5));
		es.add(new Functionality.Edge(n4,n1,8.0));
		es.add(new Functionality.Edge(n4,n2,0.5463));
		
		Functionality.Graph g =new Functionality.Graph(ns,es);
		Visualize visu = new Visualize(g);
		visu.drawGraph();
	}
}
