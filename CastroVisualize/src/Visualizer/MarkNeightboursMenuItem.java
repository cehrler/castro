// Name        : MarkNeighrboursMenuItem.java
// Author      : Yevgeni Berzak; Michal Richter
// Version     : 2.5.1
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
//Description  : This class implements the functionality of marking node's neighbours in the graph
//               
//===============================================================================================


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