package TextWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.TextArea;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class MyCellRenderer implements TreeCellRenderer
{
	private JPanel renderer;
	private JTextPane jta;
	
	public MyCellRenderer()
	{
		renderer = new JPanel(new BorderLayout());
		Box vbox = Box.createVerticalBox();
											
		vbox.add(Box.createVerticalStrut(5));
		jta = new JTextPane();
		jta.setEditable(true);
		jta.setContentType("text/html");
		jta.setText("aagdfsg <span style='color:green;font-weight:bold'>sfg sdg</span> dfg sdfg lkdfsgkl sjfdglsdfjg lksdgj slkdfgj slkdfgj sldkfgj lsdkfg sldkfg jlkdsf g");
		JScrollPane jsp = new JScrollPane(jta);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		jsp.setMaximumSize(new Dimension(150, 1000));
		//jsp.setPreferredSize(new Dimension(150, 120));
		
		jsp.setAlignmentX(0);
		//jta.setColumns(30);
		//jta.setRows(5);
		
		vbox.add(jsp);
		vbox.add(Box.createVerticalStrut(5));
		vbox.add(new JCheckBox("asf"));
		renderer.add(vbox);
		
	}
	public Component getTreeCellRendererComponent(JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus)
	{
		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode)value;
		
		if (dmt.getDepth() == 0)
		{

			return renderer;
		}
		else
		{
			return new JLabel("label");
		}
	}
}
