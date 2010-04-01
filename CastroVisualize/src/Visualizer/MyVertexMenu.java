package Visualizer;

import javax.swing.JPopupMenu;

public class MyVertexMenu extends JPopupMenu {
	public MyVertexMenu() {
		super("Vertex Menu");
		this.add(new InfoMenuItem<Functionality.Node>());
		this.add(new OpenTextMenuItem<Functionality.Node>());
		this.add(new MarkNeightboursMenuItem());
		this.addSeparator();
	}
}

