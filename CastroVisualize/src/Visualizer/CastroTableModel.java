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