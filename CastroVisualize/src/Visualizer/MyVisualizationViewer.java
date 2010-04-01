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
