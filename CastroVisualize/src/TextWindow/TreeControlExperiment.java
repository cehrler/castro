package TextWindow;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.TextArea;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

public class TreeControlExperiment {

	/**
	 * @param args
	 */
	
	private JTree tree;
	
	public void init()
	{
		DefaultMutableTreeNode top = new DefaultMutableTreeNode();
		createNodes(top);
		tree = new JTree(top);
		tree.setRootVisible(false);
		
		tree.setCellRenderer(new MyCellRenderer());
		tree.putClientProperty("JTree.lineStyle", "None");
		tree.setShowsRootHandles(true);
		//tree.setLayout(null);
		tree.setEditable(true);
		tree.setSelectionModel(new DefaultTreeSelectionModel());
		tree.setCellEditor(null);

		
		JScrollPane jsp = new JScrollPane(tree);
		JFrame frame = new JFrame("Custom tree");
		frame.setSize(200, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container pane = frame.getContentPane();
		Box vbox = Box.createVerticalBox();
		//vbox.setLayout(null);
		JTextPane ta = new JTextPane();
		ta.setText("sdfgsd sdfg sdfg sd gdf sdfg dfgdsf gdsfg dsfg ");
		
		//ta.setColumns(20);
		vbox.add(ta);
		vbox.add(jsp);
		pane.add(vbox);
		frame.setVisible(true);
		frame.pack();
			}
	
	public static void main(String[] args) {
		TreeControlExperiment te = new TreeControlExperiment();
		te.init();
	}

	private void createNodes(DefaultMutableTreeNode top) {
	    DefaultMutableTreeNode category = null;
	    DefaultMutableTreeNode book = null;
	    
	    category = new DefaultMutableTreeNode(456);
	    top.add(category);
	    
	    //original Tutorial
	    book = new DefaultMutableTreeNode("The Java Tutorial: A Short Course on the Basics");
	    
	    category.add(book);
	    
	    //Tutorial Continued
	    book = new DefaultMutableTreeNode("The Java Tutorial Continued: The Rest of the JDK");
	    category.add(book);
	    
	    //JFC Swing Tutorial
	    book = new DefaultMutableTreeNode("The JFC Swing Tutorial: A Guide to Constructing GUIs");
	    category.add(book);

	    //...add more books for programmers...

	    category = new DefaultMutableTreeNode("Books for Java Implementers");
	    top.add(category);

	    //VM
	    book = new DefaultMutableTreeNode("The Java Virtual Machine Specification");
	    category.add(book);

	    //Language Spec
	    book = new DefaultMutableTreeNode("The Java Language Specification");
	    category.add(book);
	}

}
