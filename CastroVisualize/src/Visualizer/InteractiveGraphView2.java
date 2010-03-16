/*
 * SimpleGraphView2.java
 *
 * Created on March 20, 2007, 7:49 PM
 *
 * Copyright March 20, 2007 Grotto Networking
 */

package Visualizer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Demonstrates how to use the DefaultModalGraph mouse's key listener to
 * change modes.  This is one additional line of code from
 * InterativeGraphView1.  Usage: type a "t" while the mouse is over the graph
 * for transforming mode and a "p" for picking mode.
 * @author Dr. Greg M. Bernstein
 */
public class InteractiveGraphView2 {
    Graph<Integer, String> g;
    /** Creates a new instance of SimpleGraphView */
    public InteractiveGraphView2() {
        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
        g = new SparseMultigraph<Integer, String>();
        // Add some vertices and edges
        g.addVertex((Integer)1);
        g.addVertex((Integer)2);
        g.addVertex((Integer)3); 
        g.addEdge("Edge-A", 1, 2); 
        g.addEdge("Edge-B", 2, 3);  
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InteractiveGraphView2 sgv = new InteractiveGraphView2(); // Creates the graph...
        // Layout<V, E>, VisualizationViewer<V,E>
        Layout<Integer, String> layout = new CircleLayout(sgv.g);
        layout.setSize(new Dimension(300,300));
        VisualizationViewer<Integer,String> vv = new VisualizationViewer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(350,350));
        
        // Show vertex and edge labels
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        
        // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm); 
        
        // Add the mouses mode key listener to work it needs to be added to the visualization component
        vv.addKeyListener(gm.getModeKeyListener());
        JFrame frame = new JFrame("Interactive Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);       
    }
    
}
