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
		clusteringAlgorithmCB = new JComboBox(new String[] { "Chinese Whisper clustering", "Modified Chinese Whisper clustering" });
		
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
		
		modifiedPanel.setVisible(false);
		
		dummyComponent = Box.createVerticalStrut(modifiedPanel.getPreferredSize().height);
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
					ChineseWhisperClustering.SetImplementationToBasic();
				}
				else
				{
					ChineseWhisperClustering.SetImplementationToAdjusted();
				}
				
				CastroGUI.gui.performSearch(CastroGUI.GetCurrentSearchQuery(), true);
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
