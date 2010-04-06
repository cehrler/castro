// Name        : QueryHistoryWindow.java
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
//Description  : Class implementing the dialog that allows reusing of queries that were used in
//               tha past
//===============================================================================================


package GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import Functionality.SearchQuery;
import Functionality.SearchQueryStandard;

public class QueryHistoryWindow {

	private static QueryHistoryWindow queryHistoryWindow;
	
	private static List<SearchQuery> queryList = new ArrayList<SearchQuery>();
	
	private JDialog dialog;
	private JList jlist;
	private JButton okBtn;
	private JButton cancelBtn;
	
	public static void SetQueryList(List<SearchQuery> _queryList)
	{
		queryList = _queryList;
	}
	
	public static void AddQueryToList(SearchQuery sq)
	{
		if (queryList.size() > 0)
		{
			if (queryList.get(queryList.size() - 1).toString().equals(sq.toString()))
			{
				return;
			}
		}
		queryList.add(sq);
	}
	
	private QueryHistoryWindow()
	{
		/*for (int i = 0; i < 100; i++)
		{
			List<String> ls = new ArrayList<String>();
			ls.add("ble"); ls.add("tfuj");
			queryList.add(new SearchQueryStandard(ls, "1950", "1980", "Promluva", 25));
		}*/
		
		dialog = new JDialog(CastroGUI.frame, "Searching history", Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container content = dialog.getContentPane();
		
		Box vbox = Box.createVerticalBox();
		
		vbox.add(Box.createVerticalStrut(10));
		
		jlist = new JList(queryList.toArray());
		
		JScrollPane scrollPane = new JScrollPane(jlist);
		scrollPane.setPreferredSize(new Dimension(790, 400));
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		vbox.add(scrollPane);
		
		vbox.add(Box.createVerticalBox());
		
		Box buttonBox = Box.createHorizontalBox();
		okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				
				if (jlist.getSelectedValue() != null)
				{
					CastroGUI.gui.performSearch((SearchQuery)(jlist.getSelectedValue()), true);
				}
			}
		});
		
		buttonBox.add(okBtn);
		buttonBox.add(Box.createHorizontalStrut(5));
		
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
		buttonBox.add(cancelBtn);
		buttonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		vbox.add(buttonBox);
		vbox.add(Box.createVerticalStrut(5));
		
		content.add(vbox);
		
		dialog.setLocationRelativeTo(CastroGUI.frame);		
		dialog.setLocation(CastroGUI.frame.getWidth() / 2 - dialog.getPreferredSize().width / 2, CastroGUI.frame.getHeight() / 2 - dialog.getPreferredSize().height / 2);
		
		dialog.pack();
		dialog.setResizable(false);
		dialog.setVisible(true);

	}
	
	public static void Show()
	{
		queryHistoryWindow = new QueryHistoryWindow();
	}
}
