package GUI;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import javax.media.j3d.Node;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import Visualizer.CastroTableModel;
import Visualizer.SpeechDetailPanel;
import Visualizer.Visualize;

import edu.uci.ics.jung.graph.Graph;
import Functionality.DataModule;
import Functionality.EdgeTypeEnum;
import Functionality.IndexTypeEnum;
import Functionality.SimMatrixEnum;
import Functionality.VertexDisplayPredicateMode;
import Visualizer.CastroTableModel;

public class CastroGUI implements ActionListener, ChangeListener {
	
	private static JFrame frame;
	private Container content;
	
	private JComboBox search_year_start;
	private JComboBox search_year_end;
	private JComboBox search_type;
	
	private JButton search_button;
	
	private JTextField NE_textField;
	private JTable table_search;
	
	private JComponent graph_component;
	
	private static Functionality.Graph bigGraph;

	private JComboBox simMatCB;
	
	private Visualize visu;
	
	private JTextField maxDocsTB;
	private JComboBox indexCB;
	private JComboBox filterCB;
	private Box vbFilterDistance;
	private Box vbFilterNone;
	private Box vbFilterActivation;
	private Box vbFilter;
	private JPanel graphPanel;
	private JComboBox distanceFilterTypeCB;
	private JSlider distanceSlider;
	private JComboBox coreEdgeTypeCB;
	private SpeechDetailPanel speechDetailPanel;
	
	private JSlider dottedEdgeSlider;
	private JSlider normalEdgeSlider;
	private JSlider thickEdgeSlider;
	
	private JCheckBox dottedEdgeChB;
	private JCheckBox normalEdgeChB;
	private JCheckBox thickEdgeChB;
	
	private Integer maxNumNodes = 25;
	
	private static int edgeSliderNumberOfValues = 100;
	
	private static double dottedEdgeDensity = 1;
	private static double normalEdgeDensity = 0.7;
	private static double thickEdgeDensity = 0.7;
	
	private static int frame_width = 1200;
	private static int frame_height = 700;
	
	private static double maxEdgeDensity = 3;
	
	private static String currIndex = "TF";
	private static SelectionListener listener;
	private static CastroGUI gui;
	public CastroGUI() {
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		String str1 = "ble";
		String str2 = "bleble";
		
		str1 += "ble";
		
		/*if (str1.equals(str2))
		{
			System.err.println("Same!");
		}*/
		
		Set<String> s1 = new HashSet<String>();
		Set<String> s2 = new HashSet<String>();
		s1.add(str1);
		s2.add(str2);
		
		if (s1.contains(str2) && s2.contains(str1))
		{
			System.err.println("s1 contains int2");
		}
		
		gui = new CastroGUI();
		gui.init();
	}
	
	private void tableSearchSetColumnWidth()
	{
		//String[] names =  {"ID", "Author", "Title", "Type", "Location", "Report Date", "Source", "Speech Date"};
		TableColumnModel tcm = table_search.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(20);
		tcm.getColumn(1).setPreferredWidth(50);
		tcm.getColumn(2).setPreferredWidth(150);
		tcm.getColumn(3).setPreferredWidth(50);
		tcm.getColumn(4).setPreferredWidth(70);
		tcm.getColumn(5).setPreferredWidth(50);
		tcm.getColumn(6).setPreferredWidth(120);
		tcm.getColumn(7).setPreferredWidth(50);
	}
	
	public static void updateTableSelection(Set<Functionality.Node> sn)
	{
		gui.table_search.getSelectionModel().removeListSelectionListener(listener);
		
		Functionality.Node node;
		for (int i = 0; i < gui.table_search.getRowCount(); i++)
		{
			node = DataModule.displayedGraph.getNodeById((Integer)((CastroTableModel)gui.table_search.getModel()).getSpeechIDofSelectedRow(i));
			if (sn.contains(node)) 
			{
				gui.table_search.addRowSelectionInterval(i, i);
			}
			else
			{
				gui.table_search.removeRowSelectionInterval(i, i);
			}
		}
	
		gui.table_search.getSelectionModel().addListSelectionListener(listener);
		listener.valueChanged(null);
	}
	
	public class SelectionListener implements ListSelectionListener {
		JTable table;
		
		SelectionListener(JTable table) {
			this.table = table;
		}

		
		public void valueChanged(ListSelectionEvent e) {
			int[] index = table.getSelectedRows();		
			
			if (index.length <= 0)
			{
				return;
			}
			
			Set<Functionality.Node> sn = new HashSet<Functionality.Node>();

			Integer id = new Integer(0);
			for (int i = 0; i < index.length; i++)
			{
			    id = (Integer)((CastroTableModel)table_search.getModel()).getSpeechIDofSelectedRow(index[i]);
			   
				sn.add(DataModule.displayedGraph.getNodeById(id));
				
			}
			
			
			//bigGraph.setCenter(bigGraph.getNodeById(id));
			visu.FocusNodes(sn);
		}
	}

	private void init() {
		
		frame = new JFrame("CastroGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Functionality.DataModule.InitConfiguration();
		Functionality.DataModule.Init(IndexTypeEnum.TF);
				
		content = frame.getContentPane();
		content.setLayout(null);
		
		String[][] data = {};
		
		String[] names =  {"ID", "Author", "Title", "Type", "Location", "Report Date", "Source", "Speech Date"};
		
		Insets insets = content.getInsets();		
		
		Integer table_height = 100;
		Integer search_top = 65 + insets.top + table_height;
		table_search = new JTable(data, names);
		table_search.setFillsViewportHeight(true);
		table_search.setColumnSelectionAllowed(false); 
		table_search.setRowSelectionAllowed(true); 
		table_search.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listener = new SelectionListener(table_search);
		table_search.getSelectionModel().addListSelectionListener(listener);	
				
		JScrollPane scroll_panel = new JScrollPane(table_search);
		content.add(scroll_panel);		
		scroll_panel.setBounds(insets.left + 10, insets.top + 10, frame_width - insets.left - insets.right - 20, 150);
		
		tableSearchSetColumnWidth();
		
		search_year_start = new JComboBox();
		search_year_end = new JComboBox();
		search_type = new JComboBox();
		
		String[] years = new String[37];
		for(int i = 0; i < years.length; ++i) {
			years[i] = new Integer(1959 + i).toString();
		}
		
		for(String s : years) {
			search_year_start.addItem(s);
			search_year_end.addItem(s);
		}
		
		search_year_end.setSelectedIndex(search_year_end.getItemCount() - 1);
		
		Box horizontal_box = Box.createHorizontalBox();
		
		search_type.addItem("All");
		search_type.addItem("SPEECH");
		search_type.addItem("INTERVIEW");
		search_type.addItem("MESSAGE");
		search_type.addItem("APPEARANCE");
		search_type.addItem("MEETING");
		search_type.addItem("REPORT");
		NE_textField = new JTextField();
		NE_textField.setText("\"Conrado Benitez\" \"Santiago Chile\" \"PRENSA LATINA Havana\"");
		
		Box smallVB1 = Box.createVerticalBox();
		JLabel bleLabel = new JLabel("Search terms:");
		bleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		smallVB1.add(bleLabel);
		smallVB1.add(Box.createVerticalStrut(5));
		smallVB1.add(NE_textField);
		
		Box smallVB1p1 = Box.createVerticalBox();
		
		bleLabel = new JLabel("Max. num. docs");
		bleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		smallVB1p1.add(bleLabel);
		maxDocsTB = new JTextField();
		maxDocsTB.setText(maxNumNodes.toString());
		maxDocsTB.setHorizontalAlignment(JTextField.RIGHT);
		smallVB1p1.add(Box.createVerticalStrut(5));
		smallVB1p1.setMaximumSize(new Dimension(80, 1000));
		smallVB1p1.add(maxDocsTB);

		
		Box smallVB2 = Box.createVerticalBox();
		bleLabel = new JLabel("Select year from:");
		bleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		smallVB2.add(bleLabel);
		smallVB2.add(Box.createVerticalStrut(5));
		smallVB2.add(search_year_start);
		
		Box smallVB3 = Box.createVerticalBox();
		bleLabel = new JLabel("Select year until:");
		bleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		smallVB3.add(bleLabel);
		smallVB3.add(Box.createVerticalStrut(5));
		smallVB3.add(search_year_end);
		
		Box smallVB4 = Box.createVerticalBox();
		bleLabel = new JLabel("Type:");
		bleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		smallVB4.add(bleLabel);
		smallVB4.add(Box.createVerticalStrut(5));
		smallVB4.add(search_type);
				
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
				List<String> queryTerms = processQueryString(NE_textField.getText());
				List<Double> termWeights = new ArrayList<Double>();
				
				if (DocType == "All") { DocType = "NULL"; }
				
				if (!((String)indexCB.getSelectedItem()).equals(currIndex))
				{
					currIndex = (String)indexCB.getSelectedItem();
					if (currIndex.equals("TF"))
					{
						DataModule.Init(IndexTypeEnum.TF);
					}
					else
					{
						DataModule.Init(IndexTypeEnum.TFIDF);
					}
				}
				
				for (int i = 0; i < queryTerms.size(); i++)
				{
					termWeights.add(1.0);
				}
				
				SimMatrixEnum sm = SimMatrixEnum.AllWeightedEqually;				
				if ((String)simMatCB.getSelectedItem() == "PersonsOnly")
				{
					sm = SimMatrixEnum.PersonsOnly;
				}
				else if ((String)simMatCB.getSelectedItem() == "LocationsOnly")
				{
					sm = SimMatrixEnum.LocationsOnly;
				}
				else if ((String)simMatCB.getSelectedItem() == "OrganizationsOnly")
				{
					sm = SimMatrixEnum.OrganizationsOnly;
				}
				else if ((String)simMatCB.getSelectedItem() == "AllWeightedEqually")
				{
					sm = SimMatrixEnum.AllWeightedEqually;
				}
				else if ((String)simMatCB.getSelectedItem() == "AllPersonsWeightedDouble")
				{
					sm = SimMatrixEnum.AllPersonsWeightedDouble;
				}
				else if ((String)simMatCB.getSelectedItem() == "AllLocationsWeightedDouble")
				{
					sm = SimMatrixEnum.AllPersonsWeightedDouble;
				}
				else if ((String)simMatCB.getSelectedItem() == "AllOrganizationsWeightedDouble")
				{
					sm = SimMatrixEnum.AllOrganizationsWeightedDouble;
				}
				
				Integer maxNumNodes = Integer.parseInt(maxDocsTB.getText());
								
				System.err.println("dottedEdgeDensity: " + dottedEdgeDensity);
				System.err.println("normalEdgeDensity: " + normalEdgeDensity);
				System.err.println("thickEdgeDensity: " + thickEdgeDensity);
				
				bigGraph = DataModule.getGraph(SinceDate, TillDate, Place, Author, DocType, queryTerms, termWeights, maxNumNodes, sm, dottedEdgeDensity, normalEdgeDensity, thickEdgeDensity);
				table_search.setModel(new CastroTableModel(bigGraph));
				tableSearchSetColumnWidth();
				visualizeGraph();
				
				//setEdgeSliderValue(dottedEdgeSlider, bigGraph.getDottedEdgeThreshold());
				System.err.println("dottedEdgeThreshold: " + bigGraph.getDottedEdgeThreshold());
				//setEdgeSliderValue(normalEdgeSlider, bigGraph.getNormalEdgeThreshold());
				System.err.println("normalEdgeThreshold: " + bigGraph.getNormalEdgeThreshold());
				//setEdgeSliderValue(thickEdgeSlider, bigGraph.getThickEdgeThreshold());
				System.err.println("thickEdgeThreshold: " + bigGraph.getThickEdgeThreshold());
			}
		});

		Box smallVB5 = Box.createVerticalBox();
		bleLabel = new JLabel("Action:");
		bleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		smallVB5.add(bleLabel);
		smallVB5.add(Box.createVerticalStrut(5));
		search_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		smallVB5.add(search_button);
		
		horizontal_box.add(smallVB1);
		Component strut1 = Box.createHorizontalStrut(10);
		horizontal_box.add(strut1);
		
		horizontal_box.add(smallVB1p1);
		horizontal_box.add(Box.createHorizontalStrut(10));
		
		horizontal_box.add(smallVB2);
		Component strut2 = Box.createHorizontalStrut(10);
		horizontal_box.add(strut2);
		horizontal_box.add(smallVB3);
		Component strut3 = Box.createHorizontalStrut(10);
		horizontal_box.add(strut3);
		horizontal_box.add(smallVB4);
		Component strut4 = Box.createHorizontalStrut(10);
		horizontal_box.add(strut4);
		horizontal_box.add(smallVB5);

		content.add(horizontal_box);
		horizontal_box.setBounds(10 + insets.left, search_top, frame_width - insets.left - insets.right - 20, 45);

		Box featuresVB = Box.createVerticalBox();

		
		bleLabel = new JLabel("Similarity matrix:");
		bleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//featuresVB.add(bleLabel);
		simMatCB = new JComboBox();
		simMatCB.addItem("AllWeightedEqually");
		simMatCB.addItem("PersonsOnly");
		simMatCB.addItem("LocationsOnly");
		simMatCB.addItem("OrganizationsOnly");
		simMatCB.addItem("AllPersonsWeightedDouble");
		simMatCB.addItem("AllLocationsWeightedDouble");
		simMatCB.addItem("AllOrganizationsWeightedDouble");
		//featuresVB.add(simMatCB);
		
		//featuresVB.add(Box.createVerticalStrut(10));
		
		bleLabel = new JLabel("Index type:");
		bleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//featuresVB.add(bleLabel);
				
		indexCB = new JComboBox();
		indexCB.addItem("TF");
		indexCB.addItem("TFIDF");		
		//featuresVB.add(indexCB);
		
		//featuresVB.add(Box.createVerticalStrut(10));
		
		
		Box hbFilter = Box.createHorizontalBox();
		hbFilter.add(Box.createHorizontalStrut(10));
		hbFilter.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Vertex Filter:"));
		
		vbFilter = Box.createVerticalBox();

		
		filterCB = new JComboBox(new String[] {"NoFilter", "DistanceFilter", "ActivationFilter"} );
		filterCB.addActionListener(this);
		
		vbFilter.add(filterCB);
		vbFilter.add(Box.createVerticalStrut(10));
		
		vbFilterDistance = Box.createVerticalBox();

		distanceSlider = new JSlider(0, 3);
		distanceSlider.setMajorTickSpacing(1);
		distanceSlider.setPaintLabels(true);
		distanceSlider.setPaintTicks(true);
		distanceSlider.setSnapToTicks(true);
		distanceSlider.addChangeListener(this);
		
		vbFilterDistance.add(distanceSlider);
		vbFilterDistance.add(Box.createVerticalStrut(10));
		
		distanceFilterTypeCB = new JComboBox(new String[] { "Conjunction", "Disjunction" });
		distanceFilterTypeCB.addActionListener(this);
		vbFilterDistance.add(distanceFilterTypeCB);
		
		vbFilterDistance.add(Box.createVerticalStrut(10));
		
		coreEdgeTypeCB = new JComboBox(new String[] { "AllEdges", "NormalAndThick", "ThickOnly" });
		coreEdgeTypeCB.addActionListener(this);
		vbFilterDistance.add(coreEdgeTypeCB);
		
		vbFilterDistance.setVisible(false);
		
		vbFilterNone = Box.createVerticalBox();
		vbFilterNone.add(Box.createVerticalStrut(vbFilterDistance.getPreferredSize().height));
		
		vbFilter.add(vbFilterNone);
		vbFilter.add(vbFilterDistance);
		vbFilter.add(Box.createVerticalStrut(10));
		hbFilter.add(vbFilter);
		hbFilter.add(Box.createHorizontalStrut(10));
		
		featuresVB.add(hbFilter);	
		
		Box hbEdges = Box.createHorizontalBox();
		hbEdges.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Edges:"));
		hbEdges.add(Box.createHorizontalStrut(10));
		
		Box vbEdges = Box.createVerticalBox();
		//vbEdges.add(Box.createVerticalStrut(5));
		
		Dictionary<Integer, JComponent> edgeSliderDictionary = new Hashtable<Integer, JComponent>();
		edgeSliderDictionary.put(0, new JLabel("0"));
		edgeSliderDictionary.put(10, new JLabel("0.5"));
		edgeSliderDictionary.put(20, new JLabel("1"));

		Box hbDottedEdges = Box.createHorizontalBox();
		hbDottedEdges.add(new JLabel("Dotted:"));
		hbDottedEdges.add(Box.createHorizontalStrut(3));
		
		dottedEdgeSlider = new JSlider(0, edgeSliderNumberOfValues);
		dottedEdgeSlider.setPaintTicks(true);
		//dottedEdgeSlider.setPaintLabels(true);
		dottedEdgeSlider.setMajorTickSpacing(edgeSliderNumberOfValues / 2);
		dottedEdgeSlider.setMinorTickSpacing(edgeSliderNumberOfValues / 20);
		dottedEdgeSlider.setSnapToTicks(true);
		dottedEdgeSlider.addChangeListener(this);
		setEdgeSliderValue(dottedEdgeSlider, dottedEdgeDensity);
		//dottedEdgeSlider.setLabelTable(edgeSliderDictionary);
		hbDottedEdges.add(dottedEdgeSlider);
		hbDottedEdges.add(Box.createHorizontalStrut(3));
		
		dottedEdgeChB = new JCheckBox();
		dottedEdgeChB.setSelected(true);
		dottedEdgeChB.addChangeListener(this);
		hbDottedEdges.add(dottedEdgeChB);
		
		vbEdges.add(hbDottedEdges);
		vbEdges.add(Box.createVerticalStrut(5));
		
		Box hbNormalEdges = Box.createHorizontalBox();
		JLabel normalEdgeLabel = new JLabel("Normal:");
		hbNormalEdges.add(normalEdgeLabel);
		hbNormalEdges.add(Box.createHorizontalStrut(3));

		normalEdgeSlider = new JSlider(0, edgeSliderNumberOfValues);
		normalEdgeSlider.setPaintTicks(true);
		//normalEdgeSlider.setPaintLabels(true);
		normalEdgeSlider.setMajorTickSpacing(edgeSliderNumberOfValues / 2);
		normalEdgeSlider.setMinorTickSpacing(edgeSliderNumberOfValues / 20);
		normalEdgeSlider.setSnapToTicks(true);
		normalEdgeSlider.addChangeListener(this);
		setEdgeSliderValue(normalEdgeSlider, normalEdgeDensity);
		//normalEdgeSlider.setLabelTable(edgeSliderDictionary);
		hbNormalEdges.add(normalEdgeSlider);
		
		hbNormalEdges.add(Box.createHorizontalStrut(3));
		
		normalEdgeChB = new JCheckBox();
		normalEdgeChB.setSelected(true);
		normalEdgeChB.addChangeListener(this);
		hbNormalEdges.add(normalEdgeChB);
		
		vbEdges.add(hbNormalEdges);
		vbEdges.add(Box.createVerticalStrut(5));
		
		Box hbThickEdges = Box.createHorizontalBox();
		bleLabel = new JLabel("Thick:    ");
		bleLabel.setMinimumSize(normalEdgeLabel.getPreferredSize());
		hbThickEdges.add(bleLabel);
		hbThickEdges.add(Box.createHorizontalStrut(3));

		thickEdgeSlider = new JSlider(0, edgeSliderNumberOfValues);
		thickEdgeSlider.setPaintTicks(true);
		//thickEdgeSlider.setPaintLabels(true);
		thickEdgeSlider.setMajorTickSpacing(edgeSliderNumberOfValues / 2);
		thickEdgeSlider.setMinorTickSpacing(edgeSliderNumberOfValues / 20);
		thickEdgeSlider.setSnapToTicks(true);
		thickEdgeSlider.addChangeListener(this);
		setEdgeSliderValue(thickEdgeSlider, thickEdgeDensity);
		//thickEdgeSlider.setLabelTable(edgeSliderDictionary);
		hbThickEdges.add(thickEdgeSlider);
		
		hbThickEdges.add(Box.createHorizontalStrut(3));
		thickEdgeChB = new JCheckBox();
		thickEdgeChB.setSelected(true);
		thickEdgeChB.addChangeListener(this);
		hbThickEdges.add(thickEdgeChB);
		
		vbEdges.add(hbThickEdges);
		vbEdges.add(Box.createVerticalStrut(5));

		
		hbEdges.add(vbEdges);
		hbEdges.createHorizontalStrut(10);
		
		featuresVB.add(hbEdges);
		featuresVB.add(Box.createVerticalStrut(10));
		
		/*bleLabel = new JLabel("Edge threshold:");
		bleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		featuresVB.add(bleLabel);
		edgeThresholdTB = new JTextField();
		edgeThresholdTB.setText(edgeThresholdDefault.toString());
		edgeThresholdTB.setHorizontalAlignment(JTextField.RIGHT);
		featuresVB.add(edgeThresholdTB);*/

		featuresVB.add(Box.createVerticalStrut(10));
		
		content.add(featuresVB);
		featuresVB.setBounds(10 + insets.left, search_top + 50, 170, frame_height - search_top - 85);
		
		graphPanel = new JPanel();
		
		graphPanel.setBorder(new LineBorder(Color.BLACK, 1));
		graphPanel.setBackground(Color.WHITE);
		content.add(graphPanel);
	
		int graphLeft = 10 + insets.left + 170;
		int graphTop = search_top + 60;
		int graphWidth = frame_width - (10 + insets.left + 185 + 200);
		int graphHeight = frame_height - 260;
		graphPanel.setBounds(graphLeft, graphTop, graphWidth,  graphHeight);
		
		
		JEditorPane jep = new JEditorPane();
		jep.setEditable(true);
		
		JScrollPane jepScroll = new JScrollPane(jep);
		content.add(jepScroll);
		jepScroll.setBounds(graphLeft + graphWidth + 10, graphTop, 180, graphHeight);
		speechDetailPanel = new SpeechDetailPanel(jep);
		
		frame.setSize(new Dimension(frame_width, frame_height));
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	private void setDistanceFilter()
	{
		EdgeTypeEnum ete;
		
		if (coreEdgeTypeCB.getSelectedItem().equals("AllEdges"))
		{
			ete = EdgeTypeEnum.dotted;
		}
		else if (coreEdgeTypeCB.getSelectedItem().equals("NormalAndThick"))
		{
			ete = EdgeTypeEnum.normal;
		}
		else if (coreEdgeTypeCB.getSelectedItem().equals("ThickOnly"))
		{
			ete = EdgeTypeEnum.thick;
		}
		else
		{
			ete = EdgeTypeEnum.dotted;
			System.err.println("Invalid core edge type");
		}
		
		if (distanceFilterTypeCB.getSelectedItem().equals("Conjunction"))
		{
			
			visu.setDistanceFilter(distanceSlider.getValue(), VertexDisplayPredicateMode.conjunction, ete);
		}
		else
		{
			visu.setDistanceFilter(distanceSlider.getValue(), VertexDisplayPredicateMode.disjunction, ete);
		}

	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(filterCB))
		{
			System.err.println(e.getActionCommand());
			if (filterCB.getSelectedItem().equals("DistanceFilter"))
			{
				vbFilterNone.setVisible(false);
				vbFilterDistance.setVisible(true);
				if (visu != null) 
				{
					System.err.println(distanceFilterTypeCB.getSelectedItem());
					setDistanceFilter();
				}
			}
			else if (filterCB.getSelectedItem().equals("NoFilter"))
			{
				vbFilterNone.setVisible(true);
				vbFilterDistance.setVisible(false);
				if (visu != null) visu.setNoneFilter();
			}
		} else if (e.getSource().equals(distanceFilterTypeCB))
		{
			setDistanceFilter();
		}
		else if (e.getSource().equals(coreEdgeTypeCB))
		{
			setDistanceFilter();
		}
		
		/*
		if (e.getSource().equals(filterBtn) || e.getSource().equals(filterCB))
		{
			visu.updateFilter(filterChB.isSelected(), Integer.parseInt(depthTB.getText()));
		}*/
	}

	private static void setEdgeSliderValue(JSlider slider, double value)
	{
		System.err.println("sedEdgeSliderValue: " + value);
		slider.setValue((int)Math.round(value / (maxEdgeDensity / (double)edgeSliderNumberOfValues)));
	}
	
	private static double getEdgeSliderValue(JSlider slider)
	{
		System.err.println("getEdgeSliderValue: " + slider.getValue() * (maxEdgeDensity / (double)edgeSliderNumberOfValues));
		return (slider.getValue() - 1) * (maxEdgeDensity / (double)edgeSliderNumberOfValues);
	}
		
	
	private void visualizeGraph()
	{
		//-198, -270
		visu = new Visualize(bigGraph, frame_width - 208, frame_height - 280);
		
		if (graph_component != null) 
		{
			graphPanel.remove(graph_component);
		}
		
		//visu.thick_edge_theshold = Double.parseDouble(edgeThresholdTB.getText()) * 3.0 / 2.0;
		//visu.normal_edge_threshold = Double.parseDouble(edgeThresholdTB.getText());
		graph_component = visu.drawGraph();
		graphPanel.add(graph_component, BorderLayout.CENTER);
		graphPanel.validate();
		System.err.println("added to content");
	
	}
	
	/**
	 * Create and return a table
	 * 
	 * @return	JTable
	 */

	
	private static List<String> processQueryString(String str)
	{
		int b = 0;
		int parStartIndex = -1;
		int normStartIndex = 0;
		
		List<String> terms = new ArrayList<String>();
		
		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) == '"')
			{
				if (b == 0)
				{
					parStartIndex = i + 1;
					b = 1;
				}
				else
				{
					terms.add(str.substring(parStartIndex, i));
					parStartIndex = -1;
					b = 0;
					normStartIndex = i + 1;
				}
			}
			else
			{
				if (str.charAt(i) == ' ' && b == 0)
				{
					if (normStartIndex < i)
					{
						terms.add(str.substring(normStartIndex, i));
						
					}
					
					normStartIndex = i + 1;
				}
			}
		}
		
		return terms;
	}

	
	public void stateChanged(ChangeEvent arg0) {
		
		if (arg0.getSource() == distanceSlider)
		{
			System.err.println(distanceFilterTypeCB.getSelectedItem());
			setDistanceFilter();
		}
		else if (arg0.getSource() == dottedEdgeSlider)
		{
			//double newVal = getEdgeSliderValue(dottedEdgeSlider);
			
				dottedEdgeDensity = getEdgeSliderValue(dottedEdgeSlider);
				
				if (DataModule.displayedGraph != null)
				{
					System.err.println("bleble!!!");
					DataModule.displayedGraph.ChangeEdgeDensities(dottedEdgeDensity, normalEdgeDensity, thickEdgeDensity);
					
										
					if (graph_component != null) 
					{
						graphPanel.remove(graph_component);
					}
					
					//visu.thick_edge_theshold = Double.parseDouble(edgeThresholdTB.getText()) * 3.0 / 2.0;
					//visu.normal_edge_threshold = Double.parseDouble(edgeThresholdTB.getText());
					graph_component = visu.actualizeGraph();
					visu.setEdgeWeightStrokeFunction();
					graphPanel.add(graph_component, BorderLayout.CENTER);
					graphPanel.validate();

					if (filterCB.getSelectedItem().equals("DistanceFilter"))
					{
						System.err.println("!!!setDistanceFilter");
						setDistanceFilter();
					}

					//visualizeGraph();
				}
		}
		else if (arg0.getSource() == normalEdgeSlider)
		{
			normalEdgeDensity = getEdgeSliderValue(normalEdgeSlider);
			
			if (DataModule.displayedGraph != null)
			{
				System.err.println("bleble!!!");
				DataModule.displayedGraph.ChangeEdgeDensities(dottedEdgeDensity, normalEdgeDensity, thickEdgeDensity);
				
									
				if (graph_component != null) 
				{
					graphPanel.remove(graph_component);
				}
				
				//visu.thick_edge_theshold = Double.parseDouble(edgeThresholdTB.getText()) * 3.0 / 2.0;
				//visu.normal_edge_threshold = Double.parseDouble(edgeThresholdTB.getText());
				graph_component = visu.actualizeGraph();
				visu.setEdgeWeightStrokeFunction();
				graphPanel.add(graph_component, BorderLayout.CENTER);
				graphPanel.validate();

				if (filterCB.getSelectedItem().equals("DistanceFilter"))
				{
					System.err.println("!!!setDistanceFilter");
					setDistanceFilter();
				}

				//visualizeGraph();
			}
		}
		else if (arg0.getSource() == thickEdgeSlider)
		{
			thickEdgeDensity = getEdgeSliderValue(thickEdgeSlider);
			
			if (DataModule.displayedGraph != null)
			{
				System.err.println("bleble!!!");
				DataModule.displayedGraph.ChangeEdgeDensities(dottedEdgeDensity, normalEdgeDensity, thickEdgeDensity);
				
									
				if (graph_component != null) 
				{
					graphPanel.remove(graph_component);
				}
				
				//visu.thick_edge_theshold = Double.parseDouble(edgeThresholdTB.getText()) * 3.0 / 2.0;
				//visu.normal_edge_threshold = Double.parseDouble(edgeThresholdTB.getText());
				graph_component = visu.actualizeGraph();
				visu.setEdgeWeightStrokeFunction();
				graphPanel.add(graph_component, BorderLayout.CENTER);
				graphPanel.validate();

				if (filterCB.getSelectedItem().equals("DistanceFilter"))
				{
					System.err.println("!!!setDistanceFilter");
					setDistanceFilter();
				}

				//visualizeGraph();
			}
		}
		else if (arg0.getSource() == dottedEdgeChB || arg0.getSource() == normalEdgeChB || arg0.getSource() == thickEdgeChB)
		{
			visu.setEdgeFilter(dottedEdgeChB.isSelected(), normalEdgeChB.isSelected(), thickEdgeChB.isSelected());
		}
		
	}
	
	public static void setSelectedNodesDetail(List<Functionality.Node> nodes)
	{
		gui.speechDetailPanel.setText(nodes);
	}
}
