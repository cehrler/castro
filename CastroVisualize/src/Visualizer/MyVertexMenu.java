package Visualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MyVertexMenu extends JPopupMenu {
	public MyVertexMenu() {
		super("Vertex Menu");
		this.add(new InfoMenuItem<Functionality.Node>());
		this.add(new OpenTextMenuItem<Functionality.Node>());
		this.add(new MarkNeightboursMenuItem());
		//this.add(new SearchVertexMenuItem());
		
		
		this.addSeparator();
	}
}

