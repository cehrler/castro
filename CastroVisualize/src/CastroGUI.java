import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTable;

public class CastroGUI {
	
	// the gui components
	private static JFrame frame;
	
	// the menubar
	
	private JMenuBar menu;
	
	private JMenu menu_file;
	private JMenu menu_edit;
	private JMenu menu_help;
	
	// the content
	private Container content;
	private JPanel panel_search;
	private JComboBox search_year_start;
	private JComboBox search_year_end;
	
	private JTable table_search;
	
	
	public CastroGUI() {
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CastroGUI gui = new CastroGUI();
		gui.init();
	}

	private void init() {
		
		frame = new JFrame("CastroGUI");
		
		menu = new JMenuBar();
		
		menu_file = new JMenu("File");
		menu_edit = new JMenu("Edit");
		menu_help = new JMenu("Help");
		
		menu.add(menu_file);
		menu.add(menu_edit);
		menu.add(menu_help);
		
		frame.setJMenuBar(menu);
		
		initSearchTable();
		initSearchPanel();
		
		content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(table_search, BorderLayout.NORTH);
		content.add(panel_search, BorderLayout.WEST);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void initSearchPanel() {
		panel_search = new JPanel();
		panel_search.setLayout(new GridLayout(0,1));
		
		String[] years = new String[37];
		for(int i = 0; i < years.length; ++i) {
			years[i] = new Integer(1959 + i).toString();
		}
		
		search_year_start = new JComboBox();
		search_year_end = new JComboBox();
		
		for(String s : years) {
			search_year_start.addItem(s);
			search_year_end.addItem(s);
		}
		
		panel_search.add(new JLabel("Select year from:"));
		panel_search.add(search_year_start);
		panel_search.add(new JLabel("Select year until:"));
		panel_search.add(search_year_end);
	}
	
	private void initSearchTable() {
		// this is a preliminary draft version
		String[][] data = {
				{"Fidel Castro", "Speech", "Cuba", "Viva la Revolution", "01/01/1959"},
				{"Fidel Castro", "Speech", "Cuba", "Viva la Revolution", "01/01/1959"},
				{"Fidel Castro", "Speech", "Cuba", "Viva la Revolution", "01/01/1959"},
				{"Fidel Castro", "Speech", "Cuba", "Viva la Revolution", "01/01/1959"},
				{"Fidel Castro", "Speech", "Cuba", "Viva la Revolution", "01/01/1959"},
				{"Fidel Castro", "Speech", "Cuba", "Viva la Revolution", "01/01/1959"},
				{"Fidel Castro", "Speech", "Cuba", "Viva la Revolution", "01/01/1959"}
		};
		
		String[] names = {"Author", "Type", "Location", "Title", "Date"}; 
		
		table_search = new JTable(data, names);
	}
}
