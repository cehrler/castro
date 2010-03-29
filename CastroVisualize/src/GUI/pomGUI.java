package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class pomGUI implements ActionListener{

	private JFrame frame;
	private JButton addButton;
	private JButton b1;
	private JButton b2;
	private Box vbox;
	private Box vb1;
	private Box vb2;
	
	public void init()
	{
		frame = new JFrame("pomGUI");

		vbox = Box.createVerticalBox();
		
		addButton = new JButton("Add button");
		addButton.addActionListener(this);
		vbox.add(addButton);

		vb1 = Box.createVerticalBox();
		vb1.add(new JLabel("1st box"));
		vb1.add(new JLabel("1st VBox"));
		b1 = new JButton("b1");
		b1.addActionListener(this);
		vb1.add(b1);
		
		vb2 = Box.createVerticalBox();
		vb2.add(new JLabel("2nd box"));
		vb2.add(new JLabel("2nd VBox"));
		b2 = new JButton("b2");
		b2.addActionListener(this);
		vb2.add(b2);
		vb2.setVisible(false);
		
		vbox.add(vb1);
		vbox.add(vb2);
		frame.add(vbox);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setVisible(true);


	}
	
	public static void main(String[] args) {
		pomGUI pg = new pomGUI();
		pg.init();

	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == addButton)
		{
			if (vb1.isVisible())
			{
				vb1.setVisible(false);
				vb2.setVisible(true);
				//vbox.remove(vb1);
				//vbox.add(vb2);
				System.err.println("vb2");
			}
			else
			{
				vb2.setVisible(false);
				vb1.setVisible(true);
				//vbox.remove(vb2);
				//vbox.add(vb1);
				System.err.println("vb1");
			}
			vbox.validate();
			//frame.validate();
		}
		else
		{
			if (e.getSource() == b1)
			{
				System.err.println("b1");
			}
			else
			{
				System.err.println("b2");
			}
		}

	}

}
