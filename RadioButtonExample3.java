import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.io.*;
import java.io.IOException;

 public class RadioButtonExample extends JPanel implements ActionListener{
	
	String [] buttonName = new String [3];
	static String inputFileName;	
	
	public RadioButtonExample() {
	
		super (new BorderLayout());	
		System.out.println (inputFileName);
		//Build array of Buttons from input file 
		try {
			BufferedReader in = new BufferedReader (new FileReader (new File (inputFileName)));
			int i = 0;	
			for (String x = in.readLine(); x != null; x = in.readLine()){
				buttonName [i] = x;
				System.out.println (buttonName [i]);
				i++;
			}
		} catch (IOException e){
			System.out.println ("File I/O Error! " + e);
			System.exit(1);
		}

		//Create the Radio ButtonGroup,Add action listeners,Put radio buttions in a colum in panel
		JPanel radioButtonPanel = new JPanel (new GridLayout (0,1));
		add(radioButtonPanel, BorderLayout.LINE_START);
		setBorder(BorderFactory.createEmptyBorder (20,20,20,20));
		
		ButtonGroup group = new ButtonGroup ();
		
		for (int i = 0; i < buttonName.length; i++){
			JRadioButton button = new JRadioButton (buttonName[i]);
			button.setActionCommand(buttonName[i]);
			button.setToolTipText("Click button to select");
			button.addActionListener(this);
			radioButtonPanel.add(button);
			group.add(button);
		}		
	}
	
	//Listen for Action Events 
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		
		for (int i = 0; i < buttonName.length; i++){
			if (cmd == buttonName[i])
			System.out.println ("Action " + i + " Performed: " );
		}		
		
		String message = cmd + " Selected";
		String title = "Confirmation Required:";
		//---------------------------------------------------------------------
			int answer = JOptionPane.showConfirmDialog(null, message, 
		      title,JOptionPane.YES_NO_OPTION);
		//---------------------------------------------------------------------
		System.out.println ("Confirmation: " + answer);
		if (answer == 0) System.exit(1);
	}
	public static void createAndShowGUI(){
        //Create and set up the frame.
		String frameTitle = "Winter Indoor Training Program";
		JFrame frame = new JFrame(frameTitle);
		//JLabel labelName = new JLabel("Test of Radio Buttons");
		//frame.add(labelName, JLabel.BOTTOM);
		frame.setSize(350, 250);
		JFrame.setDefaultLookAndFeelDecorated(true);
      	frame.setVisible(true);
		
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create and Set Up content pane
		JComponent newContentPane = new RadioButtonExample();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		//frame.pack();
	
	}
		public static void main (String args[]){
			inputFileName = args[0];
			System.out.println (inputFileName);
		//Create the frame on the event dispatching thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				createAndShowGUI();
			}
		});
	}
}