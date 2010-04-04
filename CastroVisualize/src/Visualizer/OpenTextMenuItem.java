// Name        : OpenTextMenuItem.java
// Author      : Yevgeni Berzak, Michal Richter
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
//Description  : This class implements the functionality of showing the full text of the document
//               with the highlighted NEs and search terms to the user
//===============================================================================================


package Visualizer;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.Color;
import java.awt.Container;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.commons.collections15.Transformer;

import GUI.CastroGUI;


public class OpenTextMenuItem<V> extends JMenuItem implements VertexListener<V> {
    private V vertex;
    private VisualizationViewer visComp;
    
    private int width = 400;
    private int height = 600;
    
    /** Creates a new instance of OpenTextMenuItem */
    public OpenTextMenuItem() {
        super("Open Text");
        this.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                visComp.getPickedVertexState().pick(vertex, false);

        		JFrame frame = new JFrame("Node "+ ((Functionality.Node) vertex).getSpeech_id() + ": " + ((Functionality.Node) vertex).getHeadline());
        		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        		JEditorPane ta = new JEditorPane();
        		ta.setContentType("text/html");
        		
        		String text = ((Functionality.Node) vertex).getSpeech_text(); 
        		
        		text = text.replaceAll("<PERSONS>", "<span style=\"color:" + neTypeColors.getPersonsString() + "\"><b>");
        		text = text.replaceAll("</PERSONS>", "</span></b>");
        		
        		text = text.replaceAll("<LOCATIONS>", "<span style=\"color:" + neTypeColors.getLocationsString() + "\"><b>");
        		text = text.replaceAll("</LOCATIONS>", "</span></b>");
        		
        		text = text.replaceAll("<ORGANIZATIONS>", "<span style=\"color:" + neTypeColors.getOrganizationsString() + "\"><b>");
        		text = text.replaceAll("</ORGANIZATIONS>", "</span></b>");
        		
        		List<String> queryTerms = CastroGUI.gui.getQueryTerms();
        		
        		for (int i = 0; i < queryTerms.size(); i++)
        		{
        			text = text.replaceAll(queryTerms.get(i), "<b><u>" + queryTerms.get(i) + "</u></b>");
        		}
        		
        		System.err.println(text);
        		
        		ta.setText(text);
        		JScrollPane sp = new JScrollPane(ta);
        		
        		Container content = frame.getContentPane();
        		content.setLayout(null);
        		
        		content.add(sp);
        		sp.setBounds(10, 10, width - 20, height - 35);
        		
        		//frame.pack();
        		frame.setSize(width, height);
            	frame.setResizable(false);
        		frame.setFocusable(true);
           		frame.setVisible(true);
                           
                
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
        this.setText("Open text..");
    }
    
}
