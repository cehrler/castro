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
