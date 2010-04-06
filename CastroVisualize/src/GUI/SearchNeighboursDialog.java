//============================================================================
// Name        : SearchNeighboursDialog.java
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
// Description : Dialog window that is used for searching similar documents to the focused one
//               
//============================================================================

package GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Functionality.Node;
import Functionality.SearchQuerySimilarNodes;

public class SearchNeighboursDialog {

	private static SearchNeighboursDialog searchNeighboursDialog;
	
	private static Functionality.Node node = null;

	private JDialog dialog;
	private JTextField maxNumDocsTF;
	private JComboBox yearFromCB;
	private JComboBox yearUntilCB;
	private JComboBox speechTypeCB;
	private JButton okBtn;
	private JButton cancelBtn;
	
	private static Integer maxNumDocs = 25;
	private static String yearFrom = "1959";
	private static String yearUntil = "1994";
	private static String speechType = "All";
	
	
	private SearchNeighboursDialog()
	{
		dialog = new JDialog(CastroGUI.frame, "Retrieve similar documents", Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container content = dialog.getContentPane();

		Box vbox = Box.createVerticalBox();
		vbox.add(Box.createVerticalStrut(10));
		
		JPanel mainPanel = new JPanel(new GridLayout(4, 2));
		
		JLabel bleLabel = new JLabel("Number of results:");
		bleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		mainPanel.add(bleLabel);
		
		maxNumDocsTF = new JTextField(maxNumDocs.toString());
		mainPanel.add(maxNumDocsTF);

		
		
		//-------------------------------------------------
		
		
		
		String[] years = new String[37];
		for (int i = 0; i < years.length; ++i) {
			years[i] = new Integer(1959 + i).toString();
		}

		yearFromCB = new JComboBox(years);
		yearUntilCB = new JComboBox(years);
		
		yearFromCB.setSelectedItem(yearFrom);
		yearUntilCB.setSelectedItem(yearUntil);
		//---------------------------------------------------------------
		
		bleLabel = new JLabel("From:");
		bleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		mainPanel.add(bleLabel);
		mainPanel.add(yearFromCB);
		yearFromCB.setEnabled(false);
		
		bleLabel = new JLabel("To:");
		bleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		mainPanel.add(bleLabel);
		mainPanel.add(yearUntilCB);
		yearUntilCB.setEnabled(false);

		speechTypeCB = new JComboBox();
		speechTypeCB.addItem("All");
		speechTypeCB.addItem("SPEECH");
		speechTypeCB.addItem("INTERVIEW");
		speechTypeCB.addItem("MESSAGE");
		speechTypeCB.addItem("APPEARANCE");
		speechTypeCB.addItem("MEETING");
		speechTypeCB.addItem("REPORT");
		speechTypeCB.setSelectedItem(speechType);
		speechTypeCB.setEnabled(false);
		
		bleLabel = new JLabel("Type:");
		bleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		mainPanel.add(bleLabel);
		
		mainPanel.add(speechTypeCB);
		
		vbox.add(mainPanel);
		vbox.add(Box.createVerticalStrut(10));
		
		Box buttonBox = Box.createHorizontalBox();
		
		okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				maxNumDocs = Integer.parseInt(maxNumDocsTF.getText());
				yearFrom = (String)(yearFromCB.getSelectedItem());
				yearUntil = (String)(yearUntilCB.getSelectedItem());
				speechType = (String)(speechTypeCB.getSelectedItem());
				
				SearchQuerySimilarNodes sq = new SearchQuerySimilarNodes(node.getSpeech_id(), maxNumDocs, node.getHeadline(), yearFrom, yearUntil, speechType);
				CastroGUI.gui.performSearch(sq, true);
				dialog.dispose();
			}
		});
		
		buttonBox.add(okBtn);
		buttonBox.add(Box.createHorizontalStrut(5));
		
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		
		buttonBox.add(cancelBtn);
		
		vbox.add(buttonBox);
		
		vbox.add(Box.createVerticalStrut(5));
		
		content.add(vbox);
		
		dialog.setLocationRelativeTo(CastroGUI.frame);
		
		dialog.setLocation(CastroGUI.frame.getWidth() / 2 - dialog.getPreferredSize().width / 2, CastroGUI.frame.getHeight() / 2 - dialog.getPreferredSize().height / 2);

		
		dialog.pack();
		dialog.setResizable(false);
		dialog.setVisible(true);

		
	}
	
	public static void SetNode(Functionality.Node n)
	{
		node = n;
	}
	
	public static void Show()
	{
		searchNeighboursDialog = new SearchNeighboursDialog();
	}
}
