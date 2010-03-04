package Visualizer;
/**
 * 
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
