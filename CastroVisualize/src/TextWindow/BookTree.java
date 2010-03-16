package TextWindow;


/*
Definitive Guide to Swing for Java 2, Second Edition
By John Zukowski     
ISBN: 1-893115-78-X
Publisher: APress
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class BookTree {
  public static void main(String args[]) {
    JFrame frame = new JFrame("Book Tree");
    Book javaBooks[] = {
        new Book("Core Java 2 Fundamentals", "Cornell/Horstmann",
            42.99f),
        new Book("Taming Java Threads", "Holub", 34.95f),
        new Book("JavaServer  Pages", "Pekowsky", 39.95f) };
    Book htmlBooks[] = { new Book("Dynamic HTML", "Goodman", 39.95f),
        new Book("HTML 4 Bible", "Pfaffenberger/Gutzman", 49.99f) };
    Vector javaVector = new NamedVector("Java Books", javaBooks);
    Vector htmlVector = new NamedVector("HTML Books", htmlBooks);
    Object rootNodes[] = { javaVector, htmlVector };
    Vector rootVector = new NamedVector("Root", rootNodes);
    JTree tree = new JTree(rootVector);
    TreeCellRenderer renderer = new BookCellRenderer();
    tree.setCellRenderer(renderer);
    JScrollPane scrollPane = new JScrollPane(tree);
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setSize(300, 300);
    frame.setVisible(true);
  }
}

class Book {
  String title;

  String authors;

  float price;

  public Book(String title, String authors, float price) {
    this.title = title;
    this.authors = authors;
    this.price = price;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthors() {
    return authors;
  }

  public float getPrice() {
    return price;
  }
}

class BookCellRenderer implements TreeCellRenderer {
  JLabel titleLabel;

  JLabel authorsLabel;

  JLabel priceLabel;

  JPanel renderer;
  
  JTextPane jta;

  DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

  Color backgroundSelectionColor;

  Color backgroundNonSelectionColor;

  public BookCellRenderer() {
    renderer = new JPanel(new GridLayout(0, 1));
    titleLabel = new JLabel(" ");
    titleLabel.setForeground(Color.blue);
    renderer.add(titleLabel);
    authorsLabel = new JLabel(" ");
    authorsLabel.setForeground(Color.blue);
    renderer.add(authorsLabel);
    priceLabel = new JLabel(" ");
    priceLabel.setHorizontalAlignment(JLabel.RIGHT);
    priceLabel.setForeground(Color.red);
    renderer.add(priceLabel);
	jta = new JTextPane();
	jta.setEditable(true);
	jta.setText("aagdfsg sfg sdg dfg sdfg lkdfsgkl sjfdglsdfjg<br> lksdgj dfgdfg <sdfg> dsfgdfg sdg d d dfjk dfkjh k slkdfgj slkdfgj<br> sldkfgj lsdkfg sldkfg jlkdsf g");
		JScrollPane jsp = new JScrollPane(jta);
	jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	jsp.setMaximumSize(new Dimension(150, 1000));
	jsp.setPreferredSize(new Dimension(150, 40));
	jsp.setAlignmentX(0);
	renderer.add(jsp);
	
	renderer.setBorder(BorderFactory.createLineBorder(Color.black));
    backgroundSelectionColor = defaultRenderer
        .getBackgroundSelectionColor();
    backgroundNonSelectionColor = defaultRenderer
        .getBackgroundNonSelectionColor();
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {
    Component returnValue = null;
    if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
      Object userObject = ((DefaultMutableTreeNode) value)
          .getUserObject();
      if (userObject instanceof Book) {
        Book book = (Book) userObject;
        titleLabel.setText(book.getTitle());
        authorsLabel.setText(book.getAuthors());
        priceLabel.setText("" + book.getPrice());
        if (selected) {
          renderer.setBackground(backgroundSelectionColor);
        } else {
          renderer.setBackground(backgroundNonSelectionColor);
        }
        renderer.setEnabled(tree.isEnabled());
        returnValue = renderer;
      }
    }
    if (returnValue == null) {
      returnValue = defaultRenderer.getTreeCellRendererComponent(tree,
          value, selected, expanded, leaf, row, hasFocus);
    }
    return returnValue;
  }
}

class NamedVector extends Vector {
  String name;

  public NamedVector(String name) {
    this.name = name;
  }

  public NamedVector(String name, Object elements[]) {
    this.name = name;
    for (int i = 0, n = elements.length; i < n; i++) {
      add(elements[i]);
    }
  }

  public String toString() {
    return "[" + name + "]";
  }
}