package Visualizer;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;

import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JMenuItem;

import org.apache.commons.collections15.Transformer;

import Functionality.Edge;

public class MarkNeightboursMenuItem<V> extends JMenuItem implements VertexListener<V> {

	final static int N = 2; 
	private V vertex;
	private VisualizationViewer visComp;



	/** Creates a new instance of MarkNeightboursVertexedge StrokeMenuItem */
	public MarkNeightboursMenuItem() {
		super("Mark Strongest Neighbors");
		this.addActionListener(new ActionListener(){
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {

				//Initiation step: mark all back to false
				Collection<Functionality.Node> allNodes = visComp.getGraphLayout().getGraph().getVertices();
				Iterator<Functionality.Node> it = allNodes.iterator();
				while (it.hasNext()){
					it.next().setMarked(false);
				}

				//visComp.getPickedVertexState().pick(vertex, false);
				Collection<Functionality.Edge> edg = visComp.getGraphLayout().getGraph().getOutEdges(vertex);

				// sort edges by strength
				ArrayList arr = new ArrayList();
				arr.addAll(edg);
				Collections.sort(arr);
				Iterator<Functionality.Edge> itr = arr.iterator();
				int i=0;

				//collect strongest neighbors
				ArrayList strongestNeighbors = new ArrayList(N);
				while (itr.hasNext() && i< N){
					Functionality.Edge curr = itr.next();
					Functionality.Node n1 = curr.getNode1();
					Functionality.Node n2 = curr.getNode2();
					if (n1.getSpeech_id() == ((Functionality.Node) vertex).getSpeech_id()) {
						strongestNeighbors.add(n2);
					} else {
						strongestNeighbors.add(n1);
					}
					i++;

				}
				//mark strongest neighbors and obtain the holy grail
				Iterator<Functionality.Node> snIt = strongestNeighbors.iterator();
				while (snIt.hasNext()){
					snIt.next().setMarked(true);
				}

				Transformer<Functionality.Node, Paint> vertexPaint = new Transformer<Functionality.Node, Paint>() {
					public Paint transform(Functionality.Node i) {
						if (i.getMarked()){

							return Color.YELLOW;
						}
						//if (i.getSpeech_id() == ((Functionality.Node) vertex).getSpeech_id()){
						//	return Color.GREEN;
						//} 
						else  {
							return Color.RED;
						}
					}
				};

				visComp.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
				visComp.repaint();



			}
		});
	}

	/**
	 * Implements the VertexMenuListener interface.
	 * @param v 
	 * @param visComp 
	 */
	public void setVertexAndView(V v, VisualizationViewer visComp) {
		this.vertex = v;
		this.visComp = visComp;
		this.setText("Mark Neightbours");
	}

}