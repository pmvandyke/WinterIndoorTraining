import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

 public class RadioButtonExample extends JPanel implements ActionListener{
	static String oneString = "Session 1 ";
	static String twoString = "Session 2 ";
	String cmd = null;
	
	public RadioButtonExample() {
		super (new BorderLayout());
		//Create the Radio ButtonGroup
		JRadioButton oneButton = new JRadioButton (oneString);
		oneButton.setMnemonic(KeyEvent.VK_O);
		oneButton.setActionCommand (oneString);
		oneButton.setSelected (true);
		
		JRadioButton twoButton = new JRadioButton (twoString);
		twoButton.setMnemonic(KeyEvent.VK_T);
		twoButton.setActionCommand (twoString);
		
		//Group the radio buttons
		ButtonGroup group = new ButtonGroup ();
		group.add(oneButton);
		group.add(twoButton);
		
		//Add action listeners for radio buttons
		oneButton.addActionListener(this);
		twoButton.addActionListener (this);
		//Put radio buttions in a colum in panel
		JPanel radioButtonPanel = new JPanel (new GridLayout (0,1));
		radioButtonPanel.add(oneButton);
		radioButtonPanel.add(twoButton);
		add(radioButtonPanel, BorderLayout.LINE_START);
		setBorder(BorderFactory.createEmptyBorder (20,20,20,20));
	}
	//Listen for Action Events
	public void actionPerformed(ActionEvent e){

		String cmd = e.getActionCommand();
		if (cmd == oneString)
				System.out.println ("Action 1 Performed: " + cmd);
		if (cmd == twoString) 
				System.out.println ("Action 2 Performed: " + cmd);
		JOptionPane.showMessageDialog(frame, "Command: " + cmd);
	}
	public static void createAndShowGUI(){
        //Create and set up the frame.
		String frameTitle = "Winter Indoor Training Program";
		String labelName = "Test of Radio Buttons";
        JFrame frame = new JFrame(frameTitle);
		frame.setSize (1900, 1900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create and Set Up content pane
		JComponent newContentPane = new RadioButtonExample();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		
		//Display the window
		frame.pack();
		frame.setVisible(true);
	}
	
		public static void main (String Args[]){
		//Create the frame on the event dispatching thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				createAndShowGUI();
			}
		});
	}
}