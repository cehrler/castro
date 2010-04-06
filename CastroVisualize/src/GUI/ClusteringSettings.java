// Name        : ClusteringSettings.java
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
//Description  : Implementation of the dialog window that is used for settings the clustering parameters
//               
//===============================================================================================


package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
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
import Functionality.ChineseWhisperClustering.ChineseWhisperTypesEnum;

public class ClusteringSettings {

	private static ClusteringSettings clusteringSettings;
	
	private JDialog frame;
	private Container content;
	private JComboBox clusteringAlgorithmCB;
	private JTextField maxNumClustersTF;
	private JTextField minimumClusterSizeTF;
	private JTextField temporaryGraphDensityTF;
	private JTextField activationThresholdMultiplierTF;
	private JTextField numMasterEdgesTF;
	private JPanel modifiedPanel;
	private JButton okBtn;
	private Component dummyComponent;
	private JButton cancelBtn;
	private JCheckBox differentColorsChB;
	
	public ClusteringSettings()
	{
		frame = new JDialog(CastroGUI.frame, "Clustering settings", Dialog.ModalityType.APPLICATION_MODAL);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		content = frame.getContentPane();
		
		Box vbox = Box.createVerticalBox();

		vbox.add(Box.createVerticalStrut(6));

		JPanel mainPanel = new JPanel(new GridLayout(5,2));
		
		mainPanel.add(new JLabel("Clustering algorithm:"));
		clusteringAlgorithmCB = new JComboBox(new String[] { "Chinese Whisper clustering", "Modified Chinese Whisper clustering", "Give it to the poor CHWS" });
		if (SettingsWindow.ChineseWhisperClustering_type == ChineseWhisperTypesEnum.standard)
		{
			clusteringAlgorithmCB.setSelectedIndex(0);
		}
		else if (SettingsWindow.ChineseWhisperClustering_type == ChineseWhisperTypesEnum.modified)
		{
			clusteringAlgorithmCB.setSelectedIndex(1);
		}
		else
		{
			clusteringAlgorithmCB.setSelectedIndex(2);
		}
		
		clusteringAlgorithmCB.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if (clusteringAlgorithmCB.getSelectedIndex() == 0)
				{
					modifiedPanel.setVisible(false);
					frame.pack();
				}
				else
				{
					modifiedPanel.setVisible(true);
					frame.pack();
				}
				
			}
		});
		clusteringAlgorithmCB.setAlignmentX(Component.LEFT_ALIGNMENT);

		mainPanel.add(clusteringAlgorithmCB);
		
		
		mainPanel.add(new JLabel("Maximal number of clusters:"));
		
		maxNumClustersTF = new JTextField(new Integer(SettingsWindow.maxNumClusters).toString());
		maxNumClustersTF.setAlignmentX(Component.LEFT_ALIGNMENT);
		maxNumClustersTF.setHorizontalAlignment(SwingConstants.TRAILING);
		mainPanel.add(maxNumClustersTF);

		mainPanel.add(new JLabel("Use colors:"));
		
		differentColorsChB = new JCheckBox("", SettingsWindow.useDifferentColorsForClusters);
		mainPanel.add(differentColorsChB);
		
		mainPanel.add(new JLabel("Minimal cluster size:"));
		minimumClusterSizeTF = new JTextField(new Integer(SettingsWindow.ChineseWhisperClustering_minimalSizeOfCluster).toString());
		minimumClusterSizeTF.setHorizontalAlignment(SwingConstants.TRAILING);
		mainPanel.add(minimumClusterSizeTF);
		
		mainPanel.add(new JLabel("Temporary graph density:"));
		temporaryGraphDensityTF = new JTextField(new Double(SettingsWindow.ChineseWhisperClustering_tempGraphDensity).toString());
		temporaryGraphDensityTF.setHorizontalAlignment(SwingConstants.TRAILING);
		mainPanel.add(temporaryGraphDensityTF);
		
		vbox.add(mainPanel);
		vbox.add(Box.createVerticalStrut(10));
		
		modifiedPanel = new JPanel(new GridLayout(2, 2));
		modifiedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Modified algorithm parameters:"));
		
		modifiedPanel.add(new JLabel("Init iterations:"));
		
		numMasterEdgesTF = new JTextField(new Integer(SettingsWindow.ChineseWhisperClusteringAdjusted_numMasterEdges).toString());
		numMasterEdgesTF.setHorizontalAlignment(SwingConstants.TRAILING);
		modifiedPanel.add(numMasterEdgesTF);
		
		modifiedPanel.add(new JLabel("Activation threshold multiplier:"));
		
		activationThresholdMultiplierTF = new JTextField(new Double(SettingsWindow.ChineseWhisperClusteringAdjusted_activationThresholdMultiplier).toString());
		activationThresholdMultiplierTF.setHorizontalAlignment(SwingConstants.TRAILING);
		modifiedPanel.add(activationThresholdMultiplierTF);

		//dummyComponent = Box.createVerticalStrut(modifiedPanel.getPreferredSize().height);

		if (clusteringAlgorithmCB.getSelectedIndex() > 0 )
		{
			modifiedPanel.setVisible(true);
			//dummyComponent.setVisible(false);
		}
		else
		{
			modifiedPanel.setVisible(false);
			
		}
		
		vbox.add(modifiedPanel);
		
		Box buttonBox = Box.createHorizontalBox();
		
		okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				SettingsWindow.maxNumClusters = Integer.parseInt(maxNumClustersTF.getText());
				SettingsWindow.useDifferentColorsForClusters = differentColorsChB.isSelected();
				SettingsWindow.ChineseWhisperClustering_minimalSizeOfCluster = Integer.parseInt(minimumClusterSizeTF.getText());
				SettingsWindow.ChineseWhisperClustering_tempGraphDensity = Double.parseDouble(temporaryGraphDensityTF.getText());
				SettingsWindow.ChineseWhisperClusteringAdjusted_activationThresholdMultiplier = Double.parseDouble(activationThresholdMultiplierTF.getText());
				SettingsWindow.ChineseWhisperClusteringAdjusted_numMasterEdges = Integer.parseInt(numMasterEdgesTF.getText());
				
				
				if (clusteringAlgorithmCB.getSelectedIndex() == 0)
				{
					ChineseWhisperClustering.SetImplementation(ChineseWhisperTypesEnum.standard);
					SettingsWindow.ChineseWhisperClustering_type = ChineseWhisperTypesEnum.standard;
				}
				else if (clusteringAlgorithmCB.getSelectedIndex() == 1)
				{
					ChineseWhisperClustering.SetImplementation(ChineseWhisperTypesEnum.modified);
					SettingsWindow.ChineseWhisperClustering_type = ChineseWhisperTypesEnum.modified;
				}
				else
				{
					ChineseWhisperClustering.SetImplementation(ChineseWhisperTypesEnum.normalizing);
					SettingsWindow.ChineseWhisperClustering_type = ChineseWhisperTypesEnum.normalizing;
				}
				
				if (CastroGUI.GetCurrentSearchQuery() != null)
				{
					CastroGUI.gui.performSearch(CastroGUI.GetCurrentSearchQuery(), true);
				}
				frame.dispose();
				
			}
		});
		buttonBox.add(okBtn);
		
		buttonBox.add(Box.createHorizontalStrut(5));
		
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				
			}
		});
		buttonBox.add(cancelBtn);

		buttonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		vbox.add(buttonBox);
		vbox.add(Box.createVerticalStrut(6));
		
		
		
		content.add(vbox);

		frame.setLocationRelativeTo(CastroGUI.frame);
		
		frame.setLocation(CastroGUI.frame.getWidth() / 2 - frame.getPreferredSize().width / 2, CastroGUI.frame.getHeight() / 2 - frame.getPreferredSize().height / 2);

		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		frame.setLocationRelativeTo(CastroGUI.frame);
		
		frame.setLocation(CastroGUI.frame.getWidth() / 2 - frame.getWidth() / 2, CastroGUI.frame.getHeight() / 2 - frame.getHeight() / 2);


	}
	
	public static void Show()
	{
		clusteringSettings = new ClusteringSettings();
	}

}
