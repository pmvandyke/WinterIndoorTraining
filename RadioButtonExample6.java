import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.io.*;
import java.io.IOException;

 class RadioButtonExample extends JPanel implements ActionListener{
	static final String delim =  " , ";
	String buttonName;
	String message;
	String tipText;
	String inputFileName = "TestMenu.txt";
	JLabel jlabChooseWorkout;
	JLabel jlabConfirmSession;
	JCheckBox jcbConfirm;
	JRadioButton jrbExercse1;
	JRadioButton jrbExercse2;
	
	public RadioButtonExample() {
	
		//super (new BorderLayout());
		System.out.println ("Getting Started " );
		
		
		//Create a new frame
		JFrame jfrm = new JFrame ("Workout Session Main Menu");
		// Specify a 1 col by 14 rows grid layout
		//****JPanel radioButtonPanel = new JPanel (new GridLayout(14,1));
		jfrm.getContentPane().setLayout(new GridLayout(14,1));
		//Give frame an initial size 
		jfrm.setSize (300,300);
		// Terminate when user closes
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create labels
		jlabChooseWorkout = new JLabel("Select a Workout Session");
		jlabConfirmSession = new JLabel("Confirm Selection");
		
		//Make the Check Box
		jcbConfirm = new JCheckBox("Confirm Selection");
		
		//Make the Radio Buttons
		//jrbExercse1 = new JRadioButton ("Exercize 1");
		//jrbExercse2 = new JRadioButton ("Exercize 2");
	
		//Add Label for Choose Workout
		jfrm.add (jlabChooseWorkout);
		
		
		//add the radio buttons to a group
				System.out.println ("Ready to Read Input File " );
		ButtonGroup group = new ButtonGroup ();
		try {
			BufferedReader in = new BufferedReader (new FileReader (new File (inputFileName)));	
			for (String inputString = in.readLine(); inputString != null; inputString = in.readLine()){
				System.out.println ("Input String: " + inputString );
				String parts [] = inputString.split(delim);
				buttonName = parts [0];
				tipText = "No Details";
				if (parts.length > 1 ) tipText = parts [1];
				JRadioButton button = new JRadioButton (buttonName);
				button.setActionCommand(buttonName);
				button.setToolTipText(tipText);
				button.addActionListener(this);
				jfrm.add(button);
				group.add(button);
			}
		} catch (IOException e){
			System.out.println ("File I/O Error! " + e);
			System.exit(1);
		}
		
		jfrm.setVisible(true);
		jfrm.add (jcbConfirm);
		
		// Action listener to jcbConfirm
		jcbConfirm.addItemListener (new ItemListener (){
			public void itemStateChanged(ItemEvent ie){
				if (jcbConfirm.isSelected()){
					System.exit(1);
					System.out.println("Selection Confirmed " );
				}
			}
				
		});
	}		
		
	//Listen for Action Events 
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		String message = "Confirm Selection: " + cmd ;
		jcbConfirm.setText(message);
	}
	
	
	public static void main (String args[]){
		//Create the frame on the event dispatching thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new RadioButtonExample();
			}
		});
	}
}