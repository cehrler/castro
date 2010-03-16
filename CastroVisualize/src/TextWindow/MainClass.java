package TextWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class MainClass extends JFrame {

  static String sometext = "Text Text Text Text Text Text Text Text Text Text Text Text ";

  public MainClass() {
    super("Simple SplitPane Frame");
    setSize(450, 200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JTextArea jt1 = new JTextArea(sometext);
    JTextArea jt2 = new JTextArea(sometext);

    jt1.setLineWrap(true);
    jt2.setLineWrap(true);
    jt1.setMinimumSize(new Dimension(150, 150));
    jt2.setMinimumSize(new Dimension(150, 150));
    jt1.setPreferredSize(new Dimension(250, 200));
    JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jt1, jt2);
    getContentPane().add(sp, BorderLayout.CENTER);
  }

  public static void main(String args[]) {
    MainClass ssb = new MainClass();
    ssb.setVisible(true);
  }
}


