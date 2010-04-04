// Name        : MyVertexMenu.java
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
//Description  : Implementation of the context menu that is displayed when the user right-click
//               at the node
//===============================================================================================


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
		this.add(new SearchVertexMenuItem());
		
		
		this.addSeparator();
	}
}

