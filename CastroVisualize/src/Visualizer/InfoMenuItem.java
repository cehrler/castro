// Name        : InfoMenuItem.java
// Author      : Yevgeni Berzak
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
//Description  : The dialog window that displays the metadata for the selected document.
//               
//===============================================================================================


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