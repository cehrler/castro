package Visualizer;

//Name        : VertexMouseMenu.java
//Author      : Yevgeni Berzak (adapted from: see below)
//Version     : 2.5.1
//Copyright   : All rights regarding the source material are reserved by the authors: With the exception
//            of Caroline Sporleder and Martin Schreiber at Saarland University in Germany and for
//            research and teaching at Saarland University in general, explicit permission must be
//            obtained before do. Usage or reference to this work or any part thereof must feature
//            credit to all the authors. Without explicit permission from the authors beforehand, this
//            software, its source and documentation may not be distributed, incorporated into other
//            products or used to create derived works.
//            However, the authors hope that this project may be of interest and use to others,
//            and so are glad to grant permission to people wishing to incorporate this project into
//            others or to use it for other purposes, and are asked to contact the authors for these
//            permissions.
//Description  :Vertex mouse menu
//            
//===============================================================================================


/*
 * @Author: Yevgeni Barzak
 * Adapted from http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf
 * Original author: Dr. Greg M. Bernstein
 */
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JPopupMenu;



public class VertexMouseMenu<V, E> extends AbstractPopupGraphMousePlugin {

	private JPopupMenu edgePopup;
	private JPopupMenu vertexPopup;

	public VertexMouseMenu() {
		this(MouseEvent.BUTTON3_MASK);
	}

	/**
	 * Creates a new instance of PopupVertexEdgeMenuMousePlugin
	 * @param modifiers mouse event modifiers see the jung visualization Event class.
	 */
	public VertexMouseMenu(int modifiers) {
		super(modifiers);
	}

	/**
	 * Implementation of the VertexMouseMenu method. 
	 * @param e 
	 */
	protected void handlePopup(MouseEvent e) {
		final VisualizationViewer<V,E> vv =
			(VisualizationViewer<V,E>)e.getSource();
		Point2D p = e.getPoint();

		GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
		if(pickSupport != null) {
			final Functionality.Node v = (Functionality.Node) pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
			if(v != null) {
				// System.out.println("Vertex " + v + " was right clicked");
				updateVertexMenu(v, vv, p);
				vertexPopup.show(vv, e.getX(), e.getY());
			}
			
		}
	}

	private void updateVertexMenu(Functionality.Node v, VisualizationViewer vv, Point2D point) {
		if (vertexPopup == null) return;
		Component[] menuComps = vertexPopup.getComponents();
		for (Component comp: menuComps) {
			if (comp instanceof VertexListener) {
				((VertexListener) comp).setVertexAndView(v, vv);
			}
			if (comp instanceof MenuPointListener) {
				((MenuPointListener) comp).setPoint(point);
			}
		}

	}

	/**
	 * Getter for the edge popup.
	 * @return 
	 */
	public JPopupMenu getEdgePopup() {
		return edgePopup;
	}

	/**
	 * Setter for the Edge popup.
	 * @param edgePopup 
	 */
	public void setEdgePopup(JPopupMenu edgePopup) {
		this.edgePopup = edgePopup;
	}

	/**
	 * Getter for the vertex popup.
	 * @return 
	 */
	public JPopupMenu getVertexPopup() {
		return vertexPopup;
	}

	/**
	 * Setter for the vertex popup.
	 * @param vertexPopup 
	 */
	public void setVertexPopup(JPopupMenu vertexPopup) {
		this.vertexPopup = vertexPopup;
	}









}
