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

import Functionality.DataModule;

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
	public static int maxNumClusters = 4;
	
	private static int similarityMeasureType = 0;
	
	private static Double customPersonsCoef = 0.3;
	private static Double customLocationsCoef = 0.3;
	private static Double customOrganizationsCoef = 0.3;
	private static Double customLexicalSimilarityCoef = 0.1;
	
	public  static Boolean useDifferentColorsForClusters = true;
	
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
		matrixUpperPanel.setLayout(new GridLayout(2, 2));
		
		matrixUpperPanel.add(new JLabel("Use smoothed similarity measure:"));

		smoothedSimilarityMatrixChB = new JCheckBox("", smoothedSimMatrix);
		matrixUpperPanel.add(smoothedSimilarityMatrixChB);

		matrixUpperPanel.add(new JLabel("Similarity measure:"));

		similarityMeasureCB = new JComboBox(new String[] { "named entity", "lexical", "custom" } );
		similarityMeasureCB.setSelectedItem(similarityMeasureType);
		similarityMeasureCB.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (similarityMeasureCB.getSelectedIndex() == 2)
				{
					matrixLowerPanel.setVisible(true);
					matrixLowerBoxEmpty.setVisible(false);
				}
				else
				{
					matrixLowerPanel.setVisible(false);
					matrixLowerBoxEmpty.setVisible(true);
				}
				
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
		matrixLowerPanel.add(personsInterpCoefTF);

		matrixLowerPanel.add(new JLabel("Locations:"));

		locationsInterpCoefTF = new JTextField(customLocationsCoef.toString());
		matrixLowerPanel.add(locationsInterpCoefTF);

		matrixLowerPanel.add(new JLabel("Organizations:"));

		organizationsInterpCoefTF = new JTextField(customOrganizationsCoef.toString());
		matrixLowerPanel.add(organizationsInterpCoefTF);

		matrixLowerPanel.add(new JLabel("Lexical similarity:"));

		lexicalSimilarityInterpCoefTF = new JTextField(customLexicalSimilarityCoef.toString());
		matrixLowerPanel.add(lexicalSimilarityInterpCoefTF);
		matrixLowerPanel.setVisible(false);
		
		matrixBox.add(matrixLowerPanel);
		
		matrixLowerBoxEmpty = Box.createVerticalBox();
		matrixLowerBoxEmpty.add(Box.createVerticalStrut(matrixLowerPanel.getPreferredSize().height));
		matrixBox.add(matrixLowerBoxEmpty);
		
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

		waitLabel.setVisible(true);
		DataModule.Init(currIndex, smoothedIndex, smoothedSimMatrix, personsCoef, locationsCoef, organizationsCoef, lexicalSimilarityCoef);
		CastroGUI.gui.performSearch();
		waitLabel.setVisible(false);
		frame.dispose();
		
	}
}
