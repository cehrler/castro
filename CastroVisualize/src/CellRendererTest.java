
 
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
 
import javax.swing.*;
 
 
public class CellRendererTest {
 
	public static void main(String[] args) {
		final CellRendererTest test = new CellRendererTest();
 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				test.createGui();
			}
		});
	}
 
	public void createGui() {
		JFrame frame = new JFrame("Testing Bogus ListCellRenderer");
		JList list = new JList(createBogusListItems());
		list.setCellRenderer(new MyListCellRenderer());
		frame.add(list);
		frame.setSize(200, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
 
 
	private MyListItem[] createBogusListItems() {
		List<MyListItem> items = new ArrayList<MyListItem>();
 
		items.add(new MyListItem("First item", "This is a nice short description."));
		items.add(new MyListItem("Another one", "This is a longer description which might take up a couple of lines."));
		items.add(new MyListItem("Next one", "This is a ridiculously long description which is total gibberish." +
				"Blah blah blabber jabber goo. Blither blather bonk. Oink boggle gaggle ker-plunk."));
		items.add(new MyListItem("No Desc", null));
		items.add(new MyListItem("Last one", "Boink!"));
 
		return items.toArray(new MyListItem[items.size()]);
	}
	
 
 
	static class MyListCellRenderer extends JPanel implements ListCellRenderer {
		private JLabel title;
		private JTextArea desc;
		
		// hack to see if this is even possible
		private static int[] rows = {1, 2, 3, 0, 1};
 
		public MyListCellRenderer() {
			setLayout(new BorderLayout());
			setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
			setOpaque(true);
 
			title = new JLabel();
			title.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 11));
			add(title, BorderLayout.NORTH);
 
			desc = new JTextArea(5,20);

			desc.setFont(new Font("Arial", Font.PLAIN, 9));
			desc.setOpaque(false);
			desc.setWrapStyleWord(true);
			desc.setLineWrap(true);
			add(desc, BorderLayout.CENTER);
		}
 
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
 
			MyListItem item = (MyListItem)value;
 
			title.setText(item.title);
			if (item.description != null && item.description.length() > 0) {
				desc.setText(item.description);
				desc.setVisible(true);
			}
			else {
				desc.setVisible(false);
			}
			
			// uncomment next line to to somewhat simulate the effect I want (hacked using the rows array)
			// desc.setRows(rows[index]);
 
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			return this;
		}
		
	}
 
	static class MyListItem {
		String title;
		String description;
 
		public MyListItem(String title, String description) {
			this.title = title;
			this.description = description;
		}
	}
}
