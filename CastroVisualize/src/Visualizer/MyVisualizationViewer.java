// Name        : MyVisualizationViewer.java
// Author      : Michal Richter
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
//Description  : The extension of standard jung VisualizationViewer class. We implemented this class
//               in order to stop unnecesary redrawing of the graph several times each second.
//				 This implementation is probably not very safe. It simply ignores some dispatched events.
//				 But it works so far.
//===============================================================================================


package Visualizer;

import java.awt.Dimension;

import javax.swing.event.ChangeEvent;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class MyVisualizationViewer<V, E> extends VisualizationViewer<V, E> {

	private int redrawingCounter = 0;
	private int redrawingActivePeriod = 5;
	private int redrawingPassivePeriod = 20;
	
	private boolean doRedrawing = false;
	
	public void setRedrawing(boolean _redrawing)
	{
		doRedrawing = _redrawing;
	}
	
	public boolean getRedrawing()
	{
		return doRedrawing;
	}
	
	public MyVisualizationViewer(Layout<V, E> layout, Dimension preferredSize) {
		super(layout, preferredSize);
		// TODO Auto-generated constructor stub
	}
	
	public MyVisualizationViewer(Layout<V, E> layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}
	
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() instanceof GraphZoomScrollPane)
		{
			System.err.println("GraphZoomScrollPane!!!");
		}
		fireStateChanged();
		//super.stateChanged(e);
			redrawingCounter++;
			if ((redrawingCounter >= redrawingActivePeriod && doRedrawing) /*|| redrawingCounter >= redrawingPassivePeriod*/)
			{
				redrawingCounter = 0;
				repaint();
				
			}
	    //System.err.println("State changed, but I give a shit about that!");
	}

	public void REDRAW()
	{
		repaint();
	}
}
