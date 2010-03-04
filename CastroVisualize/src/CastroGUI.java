import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;

import Visualizer.Visualize;

import edu.uci.ics.jung.graph.Graph;

import Functionality.DataModule;
import Functionality.IndexTypeEnum;
import Functionality.SimMatrixEnum;

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
	
	// similarity threshold slider
	static final int SIMILARITY_MIN = 0;
	static final int SIMILARITY_MAX = 100;
	static final int SIMILARITY_INIT = 50;
	
	// combo query boxes
	private JComboBox search_year_start;
	private JComboBox search_year_end;
	private JComboBox search_type;
	
	// form-based query search box
	private JCheckBox persons_button;
	private JTextField persons_form;
	private JCheckBox locations_button;
	private JTextField locations_form;
	private JCheckBox organizations_button;
	private JTextField organizations_form;
	private JButton search_button;
	
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
		Functionality.DataModule.Init(IndexTypeEnum.TF);
		menu = new JMenuBar();
		
		menu_file = new JMenu("File");
		menu_edit = new JMenu("Edit");
		menu_help = new JMenu("Help");
		
		menu.add(menu_file);
		menu.add(menu_edit);
		menu.add(menu_help);
		
		frame.setJMenuBar(menu);
		
		// main content of main window (i.e. not the menu or panel)
		content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(initSearchTable(), BorderLayout.NORTH);
		content.add(initSearchControls(), BorderLayout.WEST);
		//content.add(panel_search, BorderLayout.WEST);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Create and return search control panel including search fields and similarity threshold slider
	 * 
	 * @return VerticalBox
	 */
	private Box initSearchControls() {
		Box search_controls = Box.createVerticalBox();
		search_controls.add(initSearchBox());
		search_controls.add(initSimilarityThreshold());
		return search_controls;
	}
		
	private Box initSimilarityThreshold() {
		Box similarity_threshold = Box.createVerticalBox();
		JSlider threshold_slider = new JSlider(JSlider.HORIZONTAL, SIMILARITY_MIN, SIMILARITY_MAX, SIMILARITY_INIT);
		/**
		 * TODO write ChangeListener object
		 */
		//similarity_threshold.addChangeListener(this);

		//Turn on labels at major tick marks.
		threshold_slider.setMajorTickSpacing(25);
		threshold_slider.setMinorTickSpacing(5);
		threshold_slider.setPaintTicks(true);
		threshold_slider.setPaintLabels(true);
		similarity_threshold.add(new JLabel("Similarity threshold", JLabel.CENTER));
		similarity_threshold.add(threshold_slider);
		return similarity_threshold;
	}
	
	/**
	 * Create and return box comprising combo box menu selections and form query
	 * 
	 * @return HorizontalBox
	 */
	private Box initSearchBox() {
		Box search_box = Box.createHorizontalBox();
		search_box.add(initSearchPanel());
		search_box.add(initQueryForm());
		return search_box;
	}

	/**
	 *  Create and return box comprising combo box menu selection
	 *  
	 * @return VerticalBox
	 */
	private Box initSearchPanel() {
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
		
		search_type.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, search_type.getSelectedItem());
			}			
		});
		
		vertical_box.add(new JLabel("Select year from:"));
		vertical_box.add(search_year_start);
		vertical_box.add(new JLabel("Select year until:"));
		vertical_box.add(search_year_end);
		vertical_box.add(new JLabel("Type:"));
		vertical_box.add(search_type);
		
		return vertical_box;
	}
	
	/**
	 * Create and return box comprising combo box menu selection
	 * 
	 * @return HorizontalBox
	 */
	public Box initQueryForm() {
		persons_button = new JCheckBox();
		persons_form = new JTextField();
		locations_button = new JCheckBox();
		locations_form = new JTextField();
		organizations_button = new JCheckBox();
		organizations_form = new JTextField();
		
		Box checkbox_box = Box.createVerticalBox();
		checkbox_box.add(persons_button);
		checkbox_box.add(locations_button);
		checkbox_box.add(organizations_button);
		
		Box form_box = Box.createVerticalBox();
		form_box.add(new JLabel("Person(s)"));
		form_box.add(persons_form);
		form_box.add(new JLabel("Location(s)"));
		form_box.add(locations_form);
		form_box.add(new JLabel("Organization(s)"));
		form_box.add(organizations_form);

		search_button = new JButton("Search");
		search_button.setVerticalTextPosition(AbstractButton.BOTTOM);
		search_button.setHorizontalTextPosition(AbstractButton.CENTER);
		search_button.setMnemonic(KeyEvent.VK_S);
		search_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String SinceDate = (String)search_year_start.getSelectedItem() + "-01-01";
				String TillDate = (String)search_year_end.getSelectedItem() + "-12-31";
				String Author = "NULL";
				String DocType = (String)search_type.getSelectedItem();
				String Place = "NULL";
				List<String> queryTerms = new ArrayList<String>();
				List<Double> termWeights = new ArrayList<Double>();
				
				int maxNumNodes = 50;
				
				double similarity_threshold = 0.3;
				
				Functionality.Graph G = DataModule.getGraph(SinceDate, TillDate, Place, Author, DocType, queryTerms, termWeights, maxNumNodes, SimMatrixEnum.AllWeightedEqually, similarity_threshold);
				
				Visualize visu = new Visualize(G);
				JComponent graph = visu.drawGraph();
				content.add(graph, BorderLayout.CENTER);
			}
			
		});
		
		Box query_form = Box.createHorizontalBox();
		query_form.add(checkbox_box);
		query_form.add(form_box);
		query_form.add(search_button);
		
		return query_form;
		//search_box.add(query_form);
	}
	
	/**
	 * Create and return a table
	 * 
	 * @return	JTable
	 */
	private JTable initSearchTable() {
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
		return table_search;
	}
}
