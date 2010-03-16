package Visualizer;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InfoMenuItem<V> extends JMenuItem implements VertexListener<V> {
    private V vertex;
    private VisualizationViewer visComp;
    
    /** Creates a new instance of DeleteVertexMenuItem */
    public InfoMenuItem() {
        super("More Info");
        this.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                visComp.getPickedVertexState().pick(vertex, false);

        		JFrame frame = new JFrame("Node "+ ((Functionality.Node) vertex).getSpeech_id()+": "+
        				((Functionality.Node) vertex).getHeadline());
        		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        		JPanel panel = new  JPanel();
        		JTextArea ta = new JTextArea(
        				"Document Type: "+((Functionality.Node) vertex).getDocument_type()+"\n"+
        				"ID: "+((Functionality.Node) vertex).getSpeech_id()+"\n"+
        				"Headline: "+ ((Functionality.Node) vertex).getHeadline()+"\n"+
        				"Date: "+((Functionality.Node) vertex).getSpeech_date()+"\n"+
        				"Place: "+((Functionality.Node) vertex).getPlace(), 
        				20, 20);
        		
        		panel.add(ta);
        		frame.getContentPane().add(panel);
        		
        		frame.pack();
        		frame.setVisible(true);
        		frame.setFocusable(true);
                
                
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
        this.setText("More details..");
    }
    
}