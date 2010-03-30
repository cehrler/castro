package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Functionality.DataModule;

public class SettingsWindow implements ActionListener {

	private JFrame frame;
	private static SettingsWindow settingsWindow;
	private JComboBox indexTypeCB;
	private JCheckBox smoothedIndexChB;
	private JCheckBox smoothedSimilarityMatrixChB;
	private JTextField personsInterpCoefTF;
	private JTextField locationsInterpCoefTF;
	private JTextField organizationsInterpCoefTF;
	private JTextField lexicalSimilarityInterpCoefTF;
	private JTextField maximumEdgeDensityTF;
	private JTextField relativeDottedEdgeRatioTF;
	private JTextField relativeThickEdgeRatioTF;
	private JTextField absoluteDottedEdgeRatioTF;
	private JTextField absoluteThickEdgeRatioTF;
	private JButton okBtn;
	private JButton cancelBtn;
	private JLabel waitLabel;

	public static Double dottedEdgeAbsoluteMultiplier = 0.7;
	public static Double thickEdgeAbsoluteMultiplier = 1.3;
	
	public static Double normalEdgeRelativeMultiplier = 1.0;
	public static Double thickEdgeRelativeMultiplier = 1.4;
	

	public static Double maxEdgeDensity = 3.0;
	public static Double maxEdgeThreshold = 1.0;

	public static String currIndex = "TF";
	public static boolean smoothedIndex = true;
	public static boolean smoothedSimMatrix = true;
	
	public static Double personsCoef = 0.3;
	public static Double locationsCoef = 0.3;
	public static Double organizationsCoef = 0.3;
	public static Double lexicalSimilarityCoef = 0.1;
	
	public SettingsWindow()
	{
		frame = new JFrame("Castro Settings");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel(new BorderLayout());
		
		Box leftBox = Box.createVerticalBox();
		leftBox.setBorder(BorderFactory.createLineBorder(Color.black));
		
		Box hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("Index Type:"));

		hbox.add(Box.createHorizontalStrut(5));		
		indexTypeCB = new JComboBox();
		indexTypeCB.addItem("TF");
		indexTypeCB.addItem("TFIDF");
		indexTypeCB.setSelectedItem(currIndex);
		hbox.add(indexTypeCB);

		leftBox.add(hbox);
		
		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("Use smoothed index:"));

		hbox.add(Box.createHorizontalStrut(5));		
		smoothedIndexChB = new JCheckBox("", smoothedIndex);
		hbox.add(smoothedIndexChB);

		leftBox.add(hbox);

		Box simVBox = Box.createVerticalBox();
		simVBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Similarity matrix:"));
		
		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("Use smoothed similarity matrix:"));

		hbox.add(Box.createHorizontalStrut(5));		
		smoothedSimilarityMatrixChB = new JCheckBox("", smoothedSimMatrix);
		hbox.add(smoothedSimilarityMatrixChB);
		simVBox.add(hbox);
		simVBox.add(Box.createVerticalStrut(10));
		simVBox.add(new JLabel("Interpolations coefficients:"));
		
		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("Persons:"));

		hbox.add(Box.createHorizontalStrut(5));		
		personsInterpCoefTF = new JTextField(personsCoef.toString());
		hbox.add(personsInterpCoefTF);

		simVBox.add(hbox);

		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("Locations:"));

		hbox.add(Box.createHorizontalStrut(5));		
		locationsInterpCoefTF = new JTextField(locationsCoef.toString());
		hbox.add(locationsInterpCoefTF);

		simVBox.add(hbox);

		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("Organizations:"));

		hbox.add(Box.createHorizontalStrut(5));		
		organizationsInterpCoefTF = new JTextField(organizationsCoef.toString());
		hbox.add(organizationsInterpCoefTF);

		simVBox.add(hbox);

		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("Lexical similarity:"));

		hbox.add(Box.createHorizontalStrut(5));		
		lexicalSimilarityInterpCoefTF = new JTextField(lexicalSimilarityCoef.toString());
		hbox.add(lexicalSimilarityInterpCoefTF);

		simVBox.add(hbox);
		
		leftBox.add(simVBox);
		
		northPanel.add(leftBox, BorderLayout.WEST);
		
		Box rightBox = Box.createVerticalBox();

		Box edgeRelativeBox = Box.createVerticalBox();
		edgeRelativeBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Edges - relative mode:"));
		
		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("Maximum edge density:"));

		hbox.add(Box.createHorizontalStrut(5));		
		maximumEdgeDensityTF = new JTextField(maxEdgeDensity.toString());
		hbox.add(maximumEdgeDensityTF);
		edgeRelativeBox.add(hbox);
		edgeRelativeBox.add(Box.createVerticalStrut(10));

		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("dotted edge const:"));

		hbox.add(Box.createHorizontalStrut(5));		
		relativeDottedEdgeRatioTF = new JTextField(normalEdgeRelativeMultiplier.toString());
		hbox.add(relativeDottedEdgeRatioTF);
		edgeRelativeBox.add(hbox);
		edgeRelativeBox.add(Box.createVerticalStrut(10));

		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("thick edge const:"));

		hbox.add(Box.createHorizontalStrut(5));		
		relativeThickEdgeRatioTF = new JTextField(thickEdgeRelativeMultiplier.toString());
		hbox.add(relativeThickEdgeRatioTF);
		edgeRelativeBox.add(hbox);

		rightBox.add(edgeRelativeBox);
		
		Box edgeAbsoluteBox = Box.createVerticalBox();
		edgeAbsoluteBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Edges - absolute mode:"));
		
		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("dotted edge const:"));

		hbox.add(Box.createHorizontalStrut(5));		
		absoluteDottedEdgeRatioTF = new JTextField(dottedEdgeAbsoluteMultiplier.toString());
		hbox.add(absoluteDottedEdgeRatioTF);
		edgeAbsoluteBox.add(hbox);
		edgeAbsoluteBox.add(Box.createVerticalStrut(10));

		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(new JLabel("thick edge const:"));

		hbox.add(Box.createHorizontalStrut(5));		
		absoluteThickEdgeRatioTF = new JTextField(thickEdgeAbsoluteMultiplier.toString());
		hbox.add(absoluteThickEdgeRatioTF);
		edgeAbsoluteBox.add(hbox);
		
		rightBox.add(edgeAbsoluteBox);
		
		northPanel.add(rightBox, BorderLayout.EAST);
		
		content.add(northPanel, BorderLayout.NORTH);
		
		Box southBox = Box.createHorizontalBox();
		
		okBtn = new JButton("OK");

		okBtn.addActionListener(this);
		southBox.add(okBtn);
		southBox.add(Box.createHorizontalStrut(5));
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				frame.dispose();
			}
			
		});
		southBox.add(cancelBtn);
		waitLabel = new JLabel("Loading data structures...");
		southBox.add(Box.createHorizontalStrut(15));
		waitLabel.setVisible(false);
		southBox.add(waitLabel);
		
		content.add(southBox, BorderLayout.SOUTH);
		
		//frame.setSize(new Dimension(500, 500));
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		
	}
	
	public static void Show()
	{
		settingsWindow = new SettingsWindow();
	}
	
	public void actionPerformed(ActionEvent e) {
		dottedEdgeAbsoluteMultiplier = Double.parseDouble(absoluteDottedEdgeRatioTF.getText());
		
		
		thickEdgeAbsoluteMultiplier = Double.parseDouble(absoluteThickEdgeRatioTF.getText());
		
		normalEdgeRelativeMultiplier = Double.parseDouble(relativeDottedEdgeRatioTF.getText());
		thickEdgeRelativeMultiplier = Double.parseDouble(relativeThickEdgeRatioTF.getText());
		

		maxEdgeDensity = Double.parseDouble(maximumEdgeDensityTF.getText());

		currIndex = (String)(indexTypeCB.getSelectedItem());
		smoothedIndex = smoothedIndexChB.isSelected();
		smoothedSimMatrix = smoothedSimilarityMatrixChB.isSelected();
		
		personsCoef = Double.parseDouble(personsInterpCoefTF.getText());
		locationsCoef = Double.parseDouble(locationsInterpCoefTF.getText());
		organizationsCoef = Double.parseDouble(organizationsInterpCoefTF.getText());
		lexicalSimilarityCoef = Double.parseDouble(lexicalSimilarityInterpCoefTF.getText());

		waitLabel.setVisible(true);
		DataModule.Init(currIndex, smoothedIndex, smoothedSimMatrix, personsCoef, locationsCoef, organizationsCoef, lexicalSimilarityCoef);
		CastroGUI.gui.performSearch();
		waitLabel.setVisible(false);
		frame.dispose();
		
	}
}
