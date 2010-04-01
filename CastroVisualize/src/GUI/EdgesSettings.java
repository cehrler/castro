package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EdgesSettings {

	private static EdgesSettings edgesSettings;
	private JDialog frame;
	
	private JTextField maximumEdgeDensityTF;
	private JTextField relativeDottedEdgeRatioTF;
	private JTextField relativeThickEdgeRatioTF;
	private JTextField absoluteDottedEdgeRatioTF;
	private JTextField absoluteThickEdgeRatioTF;
	private JButton okBtn;
	private JButton cancelBtn;

	private EdgesSettings()
	{
		frame = new JDialog(CastroGUI.frame, "Link Settings", Dialog.ModalityType.APPLICATION_MODAL);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		

		Container content = frame.getContentPane();
		
		Box vbox = Box.createVerticalBox();
		vbox.add(Box.createVerticalStrut(10));
		
		JPanel relativePanel = new JPanel();
		relativePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Edges - relative mode:"));
		relativePanel.setLayout(new GridLayout(3, 2));
		
		relativePanel.add(new JLabel("Maximum edge density:"));
		maximumEdgeDensityTF = new JTextField(SettingsWindow.maxEdgeDensity.toString());
		relativePanel.add(maximumEdgeDensityTF);
		
		relativePanel.add(new JLabel("dotted edge const:"));
		relativeDottedEdgeRatioTF = new JTextField(SettingsWindow.normalEdgeRelativeMultiplier.toString());
		relativePanel.add(relativeDottedEdgeRatioTF);
		
		relativePanel.add(new JLabel("thick edge const:"));
		relativeThickEdgeRatioTF = new JTextField(SettingsWindow.thickEdgeRelativeMultiplier.toString());
		relativePanel.add(relativeThickEdgeRatioTF);
		
		vbox.add(relativePanel);
		
		vbox.add(Box.createVerticalStrut(10));
		 
		JPanel absolutePanel = new JPanel();
		absolutePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Edges - absolute mode:"));
		absolutePanel.setLayout(new GridLayout(2, 2));
		
		absolutePanel.add(new JLabel("dotted edge const:"));
		absoluteDottedEdgeRatioTF = new JTextField(SettingsWindow.dottedEdgeAbsoluteMultiplier.toString());
		absolutePanel.add(absoluteDottedEdgeRatioTF);

		absolutePanel.add(new JLabel("thick edge const:"));
		absoluteThickEdgeRatioTF = new JTextField(SettingsWindow.thickEdgeAbsoluteMultiplier.toString());
		absolutePanel.add(absoluteThickEdgeRatioTF);
		
		vbox.add(absolutePanel);

		vbox.add(Box.createVerticalStrut(10));

		Box hboxButtons = Box.createHorizontalBox();
		
		okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				SettingsWindow.dottedEdgeAbsoluteMultiplier = Double.parseDouble(absoluteDottedEdgeRatioTF.getText());
				
				
				SettingsWindow.thickEdgeAbsoluteMultiplier = Double.parseDouble(absoluteThickEdgeRatioTF.getText());
				
				SettingsWindow.normalEdgeRelativeMultiplier = Double.parseDouble(relativeDottedEdgeRatioTF.getText());
				SettingsWindow.thickEdgeRelativeMultiplier = Double.parseDouble(relativeThickEdgeRatioTF.getText());
				

				SettingsWindow.maxEdgeDensity = Double.parseDouble(maximumEdgeDensityTF.getText());
				
				CastroGUI.gui.performSearch();

				
				frame.dispose();
				
			}
		});
		
		hboxButtons.add(okBtn);
		hboxButtons.add(Box.createHorizontalStrut(5));
		
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		hboxButtons.add(cancelBtn);
		vbox.add(hboxButtons);
		vbox.add(Box.createVerticalStrut(5));
		
		content.add(vbox);

		frame.setLocationRelativeTo(CastroGUI.frame);
		
		frame.setLocation(CastroGUI.frame.getWidth() / 2 - frame.getWidth() / 2, CastroGUI.frame.getHeight() / 2 - frame.getHeight() / 2);

		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);


		

	}
	
	public static void Show()
	{
		edgesSettings = new EdgesSettings();
	}
}
