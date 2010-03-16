package TextWindow;

import java.awt.*;  
import java.awt.event.*;  
import java.util.EventObject;  
import javax.swing.*;  
import javax.swing.tree.*;  
   
public class SplitNodeTest {  
    private JScrollPane getContent() {  
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");  
        root.add(new DefaultMutableTreeNode(  
                     new SplitNode("Node 1", false)));  
        root.add(new DefaultMutableTreeNode(  
                     new SplitNode("Node 2", true)));  
        JTree tree = new JTree(new DefaultTreeModel(root));  
        tree.setEditable(true);  
        tree.setCellRenderer(new SplitNodeRenderer());  
        tree.setCellEditor(new SplitNodeEditor());  
        return new JScrollPane(tree);  
    }  
   
    public static void main(String[] args) {  
        JFrame f = new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.getContentPane().add(new SplitNodeTest().getContent());  
        f.setSize(360,300);  
        f.setLocation(200,200);  
        f.setVisible(true);  
    }  
}  
   
class SplitNode {  
    String name;  
    boolean value;  
   
    public SplitNode(String s, boolean b) {  
        name = s;  
        value = b;  
    }  
   
    public String toString() {  
        return "SplitNode[value: " + value + ", name:" + name + "]";  
    }  
}  
   
class SplitNodeRenderer implements TreeCellRenderer {  
    JLabel label;  
    JCheckBox checkBox;  
    JTextField textField;  
    JPanel panel;  
    JTextPane jta;
    public SplitNodeRenderer() {  
        label = new JLabel();  
        checkBox = new JCheckBox();  
        checkBox.setBackground(UIManager.getColor("Tree.background"));  
        checkBox.setBorder(null);  
        textField = new JTextField();  
        textField.setEditable(false);  
        textField.setBackground(UIManager.getColor("Tree.background"));  
        textField.setBorder(null);  
        panel = new JPanel();  
        panel.setOpaque(false);  
        panel.add(checkBox);  
        panel.add(textField); 
		jta = new JTextPane();
		jta.setEditable(true);
		jta.setText("aagdfsg sfg sdg dfg sdfg lkdfsgkl sjfdglsdfjg<br> lksdgj slkdfgj slkdfgj<br> sldkfgj lsdkfg sldkfg jlkdsf g");
		JScrollPane jsp = new JScrollPane(jta);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setMaximumSize(new Dimension(150, 1000));
		jsp.setPreferredSize(new Dimension(150, 60));
		jsp.setAlignmentX(0);
		panel.add(jsp);
    }  
   
    public Component getTreeCellRendererComponent(JTree tree,  
                                                  Object value,  
                                                  boolean selected,  
                                                  boolean expanded,  
                                                  boolean leaf,  
                                                  int row,  
                                                  boolean hasFocus) {  
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;  
        if(node.getUserObject() instanceof SplitNode) {  
            SplitNode splitNode = (SplitNode)node.getUserObject();  
            checkBox.setSelected(splitNode.value);  
            textField.setText(splitNode.name);  
            return panel;  
        } else {  
            label.setText(node.toString());  
            return label;  
        }  
    }  
}  
   
class SplitNodeEditor extends AbstractCellEditor  
                      implements TreeCellEditor, ActionListener {  
    JLabel label;  
    JCheckBox checkBox;  
    JTextField textField;  
    SplitNode splitNode;  
    JComponent editedComponent;  
    JPanel panel; 
    JTextPane jta;
   
    public SplitNodeEditor() {  
        label = new JLabel();  
        checkBox = new JCheckBox();  
        checkBox.addActionListener(this);  
        checkBox.setBackground(UIManager.getColor("Tree.background"));  
        checkBox.setBorder(null);  
        checkBox.addMouseListener(new MouseAdapter() {  
            public void mousePressed(MouseEvent e) {  
                checkBox.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));  
            }  
            public void mouseReleased(MouseEvent e) {  
                checkBox.setBorder(null);  
            }  
        });  
        textField = new JTextField();  
        textField.addActionListener(this);  
        textField.setBackground(UIManager.getColor("Tree.background"));  
        textField.setBorder(null);  
        panel = new JPanel();  
        panel.setOpaque(false);  
        panel.add(textField);  
        panel.add(checkBox);
		jta = new JTextPane();
		jta.setEditable(true);
		jta.setText("aagdfsg sfg sdg dfg sdfg lkdfsgkl sjfdglsdfjg<br> lksdgj slkdfgj slkdfgj<br> sldkfgj lsdkfg sldkfg jlkdsf g");
		JScrollPane jsp = new JScrollPane(jta);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setMaximumSize(new Dimension(150, 1000));
		jsp.setPreferredSize(new Dimension(150, 60));
		jsp.setAlignmentX(0);
		panel.add(jsp);
 
        
    }  
   
    public Component getTreeCellEditorComponent(JTree tree,  
                                                Object value,  
                                                boolean isSelected,  
                                                boolean expanded,  
                                                boolean leaf,  
                                                int row) {  
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;  
        if(node.getUserObject() instanceof SplitNode) {  
            splitNode = (SplitNode)node.getUserObject();  
            checkBox.setSelected(splitNode.value);  
            textField.setText(splitNode.name);  
            return panel;  
        } else {  
            label.setText(node.toString());  
            return label;  
        }  
    }  
   
    public Object getCellEditorValue() {  
        if(editedComponent == textField)  
            splitNode.name = textField.getText();  
        else  
            splitNode.value = checkBox.isSelected();  
        return splitNode;  
    }  
   
    public boolean isCellEditable(EventObject anEvent) {  
        if (anEvent instanceof MouseEvent) {  
            Point p = ((MouseEvent)anEvent).getPoint();  
            JTree tree = (JTree)anEvent.getSource();  
            TreePath path = tree.getPathForLocation(p.x, p.y);  
            DefaultMutableTreeNode node =  
                (DefaultMutableTreeNode)path.getLastPathComponent();  
            int clickCountToStart =  
                    (node.getUserObject() instanceof SplitNode) ? 1 : 2;  
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;  
        }  
          return true;  
    }  
   
    public void actionPerformed(ActionEvent e) {  
        editedComponent = (JComponent)e.getSource();  
        //super.stopCellEditing();  
    }  
}  
