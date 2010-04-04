//============================================================================
// Name        : SettingsWindow.java
// Author      : Michal Richter
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
// Description : Implementation of the menu where the index type and similarity matrix type can be set
//               
//============================================================================
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Functionality.ChineseWhisperClustering;
import Functionality.DataModule;
import Functionality.SimMatrixElem;
import Functionality.SimMatrixEnum;
import Functionality.ChineseWhisperClustering.ChineseWhisperTypesEnum;

public class SettingsWindow implements ActionListener {

	private JDialog frame;
	private static SettingsWindow settingsWindow;
	private JComboBox indexTypeCB;
	private JCheckBox smoothedIndexChB;
	private JCheckBox smoothedSimilarityMatrixChB;
	private JTextField personsInterpCoefTF;
	private JTextField locationsInterpCoefTF;
	private JTextField organizationsInterpCoefTF;
	private JTextField lexicalSimilarityInterpCoefTF;
	private JComboBox similarityMeasureCB;
	private JButton okBtn;
	private JButton cancelBtn;
	private JLabel waitLabel;
	private JPanel matrixLowerPanel;
	private Box matrixLowerBoxEmpty;
	private JComboBox similarityMeasure_algorithmCB;

	public static Double dottedEdgeAbsoluteMultiplier = 0.7;
	public static Double thickEdgeAbsoluteMultiplier = 1.3;
	
	public static Double normalEdgeRelativeMultiplier = 1.0;
	public static Double thickEdgeRelativeMultiplier = 1.4;
	

	public static Double maxEdgeDensity = 3.0;
	public static Double maxEdgeThreshold = 1.0;

	public static String currIndex = "TFIDF";
	public static boolean smoothedIndex = false;
	public static boolean smoothedSimMatrix = false;
	
	public static Double personsCoef = 0.33;
	public static Double locationsCoef = 0.33;
	public static Double organizationsCoef = 0.34;
	public static Double lexicalSimilarityCoef = 0.0;
	
	private static int similarityMeasureType = 0;
	
	private static Double customPersonsCoef = 0.3;
	private static Double customLocationsCoef = 0.3;
	private static Double customOrganizationsCoef = 0.3;
	private static Double customLexicalSimilarityCoef = 0.1;
	
	public static Boolean useDifferentColorsForClusters = true; //ok
	public static int maxNumClusters = 0; //ok
	public static int ChineseWhisperClusteringAdjusted_numMasterEdges = 8;
	public static double ChineseWhisperClusteringAdjusted_activationThresholdMultiplier = 3.0;
	public static double ChineseWhisperClustering_tempGraphDensity = 3.0;
	public static int ChineseWhisperClusteringAdjusted_numberEdgesForMeanComputation = 10000000; //Means that all of them are used!
	public static int ChineseWhisperClustering_minimalSizeOfCluster = 3;
	public static ChineseWhisperClustering.ChineseWhisperTypesEnum ChineseWhisperClustering_type = ChineseWhisperTypesEnum.modified;
	public static int ChineseWhisperClustering_numberOfIterations = 15;
	public static double ChineseWhisperClusteringAdjusted_activationThresholdMultiplierIncrement = 0.5;
	public static double ChineseWhisperClusteingNormalizing_sizeAddConstant = 0.3;
	
	public static SimMatrixElem.SimilarityMeasure similarityMeasure = SimMatrixElem.SimilarityMeasure.cosine;
	
	public SettingsWindow()
	{
		frame = new JDialog(CastroGUI.frame, "Index and Similarity matrix", Dialog.ModalityType.APPLICATION_MODAL);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		
		Box vbox = Box.createVerticalBox();
		vbox.add(Box.createVerticalStrut(10));
		
		JPanel indexPanel = new JPanel();
		indexPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Index:"));
		indexPanel.setLayout(new GridLayout(2, 2));
				
		indexPanel.add(new JLabel("Index Type:"));

		indexTypeCB = new JComboBox();
		indexTypeCB.addItem("TF");
		indexTypeCB.addItem("TFIDF");
		indexTypeCB.setSelectedItem(currIndex);
		indexPanel.add(indexTypeCB);


		indexPanel.add(new JLabel("Use smoothed index:"));
		smoothedIndexChB = new JCheckBox("", smoothedIndex);
		indexPanel.add(smoothedIndexChB);
		
		vbox.add(indexPanel);
		
		vbox.add(Box.createVerticalStrut(10));
		
		Box matrixBox = Box.createVerticalBox();
		matrixBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Similarity matrix:"));
		
		JPanel matrixUpperPanel = new JPanel();
		matrixUpperPanel.setLayout(new GridLayout(3, 2));

		matrixUpperPanel.add(new JLabel("Similarity measure:"));
		
		similarityMeasure_algorithmCB = new JComboBox(new SimMatrixElem.SimilarityMeasure[] { SimMatrixElem.SimilarityMeasure.manhattan, SimMatrixElem.SimilarityMeasure.cosine });
		similarityMeasure_algorithmCB.setSelectedItem(similarityMeasure);
		matrixUpperPanel.add(similarityMeasure_algorithmCB);
		
		matrixUpperPanel.add(new JLabel("Use smoothed similarity measure:"));

		smoothedSimilarityMatrixChB = new JCheckBox("", smoothedSimMatrix);
		matrixUpperPanel.add(smoothedSimilarityMatrixChB);

		matrixUpperPanel.add(new JLabel("Similarity matrix:"));

		similarityMeasureCB = new JComboBox(new String[] { "named entity", "lexical", "custom" } );
		similarityMeasureCB.setSelectedIndex(similarityMeasureType);
		similarityMeasureCB.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (similarityMeasureCB.getSelectedIndex() == 2)
				{
					matrixLowerPanel.setVisible(true);
					//matrixLowerBoxEmpty.setVisible(false);
				}
				else
				{
					matrixLowerPanel.setVisible(false);
					//matrixLowerBoxEmpty.setVisible(true);
				}
				frame.pack();
			}
		});
		
		matrixUpperPanel.add(similarityMeasureCB);
		
		
		matrixBox.add(matrixUpperPanel);
		matrixBox.add(Box.createVerticalStrut(10));
		
		matrixLowerPanel = new JPanel();
		matrixLowerPanel.setLayout(new GridLayout(4, 2));
		matrixLowerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Interpolation coefficients:"));
		
		matrixLowerPanel.add(new JLabel("Persons:"));
		
		personsInterpCoefTF = new JTextField(customPersonsCoef.toString());
		personsInterpCoefTF.setHorizontalAlignment(SwingConstants.TRAILING);
		matrixLowerPanel.add(personsInterpCoefTF);

		matrixLowerPanel.add(new JLabel("Locations:"));

		locationsInterpCoefTF = new JTextField(customLocationsCoef.toString());
		locationsInterpCoefTF.setHorizontalAlignment(SwingConstants.TRAILING);
		matrixLowerPanel.add(locationsInterpCoefTF);

		matrixLowerPanel.add(new JLabel("Organizations:"));

		organizationsInterpCoefTF = new JTextField(customOrganizationsCoef.toString());
		organizationsInterpCoefTF.setHorizontalAlignment(SwingConstants.TRAILING);
		matrixLowerPanel.add(organizationsInterpCoefTF);

		matrixLowerPanel.add(new JLabel("Lexical similarity:"));

		lexicalSimilarityInterpCoefTF = new JTextField(customLexicalSimilarityCoef.toString());
		lexicalSimilarityInterpCoefTF.setHorizontalAlignment(SwingConstants.TRAILING);
		matrixLowerPanel.add(lexicalSimilarityInterpCoefTF);
		matrixLowerPanel.setVisible(false);
		
		matrixBox.add(matrixLowerPanel);
		
		/*matrixLowerBoxEmpty = Box.createVerticalBox();
		matrixLowerBoxEmpty.add(Box.createVerticalStrut(matrixLowerPanel.getPreferredSize().height));
		matrixBox.add(matrixLowerBoxEmpty);*/
		
		matrixBox.add(Box.createVerticalStrut(5));
		
		vbox.add(matrixBox);
		
		
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
		
		vbox.add(southBox);

		
		content.add(vbox, BorderLayout.SOUTH);
		
		//frame.setSize(new Dimension(500, 500));
	
		frame.setLocationRelativeTo(CastroGUI.frame);
		
		frame.setLocation(CastroGUI.frame.getWidth() / 2 - frame.getPreferredSize().width / 2, CastroGUI.frame.getHeight() / 2 - frame.getPreferredSize().height / 2);

		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);


	}
	
	public static void Show()
	{
		settingsWindow = new SettingsWindow();
	}
	
	public void actionPerformed(ActionEvent e) {

		currIndex = (String)(indexTypeCB.getSelectedItem());
		smoothedIndex = smoothedIndexChB.isSelected();
		smoothedSimMatrix = smoothedSimilarityMatrixChB.isSelected();

		customPersonsCoef = Double.parseDouble(personsInterpCoefTF.getText());
		customLocationsCoef = Double.parseDouble(locationsInterpCoefTF.getText());
		customOrganizationsCoef = Double.parseDouble(organizationsInterpCoefTF.getText());
		customLexicalSimilarityCoef = Double.parseDouble(lexicalSimilarityInterpCoefTF.getText());

		similarityMeasure = (SimMatrixElem.SimilarityMeasure)(similarityMeasure_algorithmCB.getSelectedItem());
		similarityMeasureType = similarityMeasureCB.getSelectedIndex();
		
		if (similarityMeasureType == 2)
		{
			personsCoef = customPersonsCoef;
			locationsCoef = customLocationsCoef;
			organizationsCoef = customOrganizationsCoef;
			lexicalSimilarityCoef = customLexicalSimilarityCoef; 
		}
		else if (similarityMeasureType == 1)
		{
			personsCoef = 0.0;
			locationsCoef = 0.0;
			organizationsCoef = 0.0;
			lexicalSimilarityCoef = 1.0;
		}
		else
		{
			personsCoef = 0.33;
			locationsCoef = 0.33;
			organizationsCoef = 0.34;
			lexicalSimilarityCoef = 0.0;
		}
		
		System.err.println("personsCoef = " + personsCoef + ", locationsCoef = " + locationsCoef + ", organizationsCoef = " + organizationsCoef + ", lexicalSimilarityCoef = " + lexicalSimilarityCoef);

		DataModule.Init(currIndex, smoothedIndex, smoothedSimMatrix, personsCoef, locationsCoef, organizationsCoef, lexicalSimilarityCoef);
		if (CastroGUI.GetCurrentSearchQuery() != null)
		{
			CastroGUI.gui.performSearch(CastroGUI.GetCurrentSearchQuery(), true);
		}
		frame.dispose();
		
	}
}
