// Name        : CastroTableModel.java
// Author      : Carsten Ehrler
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
//Description  : Data model for the table which is displayed in the CastroGUI main frame
//               
//===============================================================================================

package Visualizer;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import Functionality.Graph;
import Functionality.Node;


public class CastroTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = {"ID", "Author", "Title", "Type", "Location", "Report Date", "Source", "Speech Date"};
	
	private List<Node> nodes;
	
	public CastroTableModel(Graph G) {
		this.nodes = G.getNodes();
	}
	
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return nodes.size();
	}
	
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public Object getValueAt(int arg0, int arg1) {
		
		if (arg1 == 0)
		{
			return nodes.get(arg0).getSpeech_id();
		}
		else if(arg1 == 1) {
			return nodes.get(arg0).getAuthor();
		}
		else if(arg1 == 2) {
			return nodes.get(arg0).getHeadline();
		}
		else if(arg1 == 3) {
			return nodes.get(arg0).getDocument_type();
			} 
		else if(arg1 == 4) {
			return nodes.get(arg0).getPlace();
		} 
		else if(arg1 == 5) {
			return nodes.get(arg0).getReport_date();
		} 
		else if(arg1 == 6) {
			return nodes.get(arg0).getSource();
		}
		else if(arg1 == 7) {
			return nodes.get(arg0).getSpeech_date();
		}
		else {
			return "";
		}
	}
	
	public Object getSpeechIDofSelectedRow(int row) {
		return nodes.get(row).getSpeech_id();
	}
}
