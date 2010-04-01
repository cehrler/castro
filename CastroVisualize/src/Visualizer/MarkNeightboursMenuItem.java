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

public class MarkNeightboursMenuItem extends JMenuItem implements VertexListener<Functionality.Node> {

	final static int N = 2; 
	private Functionality.Node vertex;
	private VisualizationViewer visComp;



	/** Creates a new instance of MarkNeightboursVertexedge StrokeMenuItem */
	public MarkNeightboursMenuItem() {
		super("Mark Strongest Neighbors");
		this.addActionListener(new ActionListener(){
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				VertexColorTransformer.vctInstance.setMarkedNodes(vertex.getNeighborsMap());
				visComp.repaint();
			}
		});
	}

	/**
	 * Implements the VertexMenuListener interface.
	 * @param v 
	 * @param visComp 
	 */
	public void setVertexAndView(Functionality.Node v, VisualizationViewer visComp) {
		this.vertex = v;
		this.visComp = visComp;
		this.setText("Mark Neightbours");
		System.err.println("Mark neightboursMenuItem!!!!!!");


	}

}