package GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ClusteringSettings {

	private static ClusteringSettings clusteringSettings;
	
	private JDialog frame;
	private Container content;
	private JComboBox clusteringAlgorithmCB;
	private JTextField maxNumClustersTF;
	private JButton okBtn;
	private JButton cancelBtn;
	private JCheckBox differentColorsChB;
	
	public ClusteringSettings()
	{
		frame = new JDialog(CastroGUI.frame, "Clustering settings", Dialog.ModalityType.APPLICATION_MODAL);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		content = frame.getContentPane();
		
		Box vbox = Box.createVerticalBox();
		vbox.add(Box.createVerticalStrut(10));
		JLabel bleLabel = new JLabel("Clustering algorithm:");
		bleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		vbox.add(bleLabel);

		vbox.add(Box.createVerticalStrut(6));

		
		clusteringAlgorithmCB = new JComboBox(new String[] { "Chinese Whisper clustering", "K-means clustering" });
		clusteringAlgorithmCB.setAlignmentX(Component.LEFT_ALIGNMENT);
		vbox.add(clusteringAlgorithmCB);

		vbox.add(Box.createVerticalStrut(10));
		
		bleLabel = new JLabel("Maximal number of clusters:");
		bleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		vbox.add(bleLabel);

		vbox.add(Box.createVerticalStrut(6));
		
		maxNumClustersTF = new JTextField(new Integer(SettingsWindow.maxNumClusters).toString());
		maxNumClustersTF.setAlignmentX(Component.LEFT_ALIGNMENT);
		vbox.add(maxNumClustersTF);

		vbox.add(Box.createVerticalStrut(6));
		
		differentColorsChB = new JCheckBox("Denote clusters by different color", SettingsWindow.useDifferentColorsForClusters);
		vbox.add(differentColorsChB);
		
		vbox.add(Box.createVerticalStrut(10));
		
		Box buttonBox = Box.createHorizontalBox();
		
		okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				SettingsWindow.maxNumClusters = Integer.parseInt(maxNumClustersTF.getText());
				SettingsWindow.useDifferentColorsForClusters = differentColorsChB.isSelected();
				CastroGUI.gui.performSearch();
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
