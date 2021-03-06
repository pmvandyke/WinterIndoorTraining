import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.io.*;
import java.io.IOException;

 public class RadioButtonExample extends JPanel implements ActionListener{
	static final String delim =  " , ";
	static String inputFileName;	
	String buttonName;
	String tipText;
	
	public RadioButtonExample() {
	
		super (new BorderLayout());	
		
		//Create JPanel
		JPanel radioButtonPanel = new JPanel (new GridLayout (0,1));
		JLabel label = new JLabel("Click Button to Select a Workout Session: ");
		add(radioButtonPanel, BorderLayout.LINE_START);
		add(radioButtonPanel, BorderLayout.SOUTH);
		add(label, BorderLayout.NORTH);
		setBorder(BorderFactory.createEmptyBorder (20,20,20,20));
		
		//Build ButtonGroup from input file:inputFileName

		ButtonGroup group = new ButtonGroup ();
		try {
			BufferedReader in = new BufferedReader (new FileReader (new File (inputFileName)));	
			for (String inputString = in.readLine(); inputString != null; inputString = in.readLine()){
				String parts [] = inputString.split(delim);
				buttonName = parts [0];
				tipText = "No Details";
				if (parts.length > 1 ) tipText = parts [1];
				JRadioButton button = new JRadioButton (buttonName);
				button.setActionCommand(buttonName);
				button.setToolTipText(tipText);
				button.addActionListener(this);
				radioButtonPanel.add(button);
				group.add(button);
			}
		} catch (IOException e){
			System.out.println ("File I/O Error! " + e);
			System.exit(1);
		}
	}
	//Listen for Action Events 
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		String message = cmd + " was Selected";
		String title = "Confirmation Required:";
		int answer = JOptionPane.showConfirmDialog(null, message, 
		    title,JOptionPane.YES_NO_OPTION);
		System.out.println ("Confirmation: " + answer);
		if (answer == 0) System.exit(1);
	}
	public static void createAndShowGUI(){
        //Create and set up the frame.
		String frameTitle = "Winter Indoor Training Program";
		JFrame frame = new JFrame(frameTitle);
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setSize(350, 350);
		//frame.pack();
      	frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Create and Set Up content pane
		JComponent newContentPane = new RadioButtonExample();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
	}
		public static void main (String args[]){
			inputFileName = "TestMenu.txt";
		//Create the frame on the event dispatching thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				createAndShowGUI();
			}
		});
	}
}