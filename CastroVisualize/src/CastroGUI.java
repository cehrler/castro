import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
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
	private JComboBox search_type;
	
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
		String[] years = new String[37];
		for(int i = 0; i < years.length; ++i) {
			years[i] = new Integer(1959 + i).toString();
		}
		
		search_year_start = new JComboBox();
		search_year_end = new JComboBox();
		search_type = new JComboBox();
		
		for(String s : years) {
			search_year_start.addItem(s);
			search_year_end.addItem(s);
		}
		Box vertical_box = Box.createVerticalBox();
		search_type.addItem("All");
		search_type.addItem("Speech");
		search_type.addItem("Interview");
		search_type.addItem("Report");
		
		vertical_box.add(new JLabel("Select year from:"));
		vertical_box.add(search_year_start);
		vertical_box.add(new JLabel("Select year until:"));
		vertical_box.add(search_year_end);
		vertical_box.add(new JLabel("Type:"));
		vertical_box.add(search_type);
		
		panel_search.add(vertical_box, BorderLayout.WEST);
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
