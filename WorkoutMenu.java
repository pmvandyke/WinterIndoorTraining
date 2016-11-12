import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.io.*;
import java.io.IOException;

//* This application creates a Frame which desplays the Training Menu which has been received.
//* The menu is created from items read from a input file contained in inputFileName.
//* The menu options are displayed as Radio Buttons.
//* An ActionListener receives the selection and passes the button name to a Confirm Check Box.
//* The Confirm Check Box allows the user to verify the selection.
//* And then transfer control to an application which displays the selected Workout.
//* Application passes the Radio Buttion Selection

 class WorkoutMenu extends JFrame implements ActionListener{
	static final String delim =  "#";
	String buttonName;
	String message = "";
	String tipText;
	String actionCommand;
	String inputFileName;
	String inputWorkout;
	//String workoutFileName;
		
	JFrame jfrm = new JFrame (" ");
	JLabel jlabConfirmSession;
	JLabel jlabChooseWorkout = new JLabel("Select a Workout Session");
	JCheckBox jcbConfirm = new JCheckBox("Confirm Selection");
	
	public WorkoutMenu(String inputWorkout) {
		
		createFrame(inputWorkout);
		inputFileName = inputWorkout + ".txt";	
		//add the radio buttons to a group from input file
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
				jfrm.add(button);
				group.add(button);
			}
		} catch (IOException e){
			System.out.println ("File I/O Error! " + e);
			System.exit(1);
		}
		// Action listener to jcbConfirm
		jcbConfirm.addItemListener (new ItemListener (){
			public void itemStateChanged(ItemEvent ie){
				if (jcbConfirm.isSelected()){
					if (message == ""){
						jcbConfirm.setSelected(false);
						return;
					}
					displayNextWorkout();
					//System.exit(0);	
				}
			}		
		});	
			createCheckBox();	
	}	
	// display the next workout routine
	public void displayNextWorkout(){
		//System.out.println("Next Workout: " + actionCommand );
		// Added this to launch the workout Frame
		SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					//new RunWorkout(actionCommand.toString()+".txt");		
					new RunWorkout(actionCommand);
			}
		});
	}
	//Listen for Action Events 
	public void actionPerformed(ActionEvent ae){
		String cmd = ae.getActionCommand();
		actionCommand = cmd;
		message = "Confirm Selection: " + cmd ;
		jcbConfirm.setText(message);
	}
	public void main (String args[]){
		//Create the frame on the event dispatching thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new RadioButtonExample(inputWorkout);
			}
		});
	}
	public void createFrame (String inputWorkout) {
		jfrm.setTitle(inputWorkout + " Main Menu");
		// Specify a 1 col by 14 rows grid layout
		jfrm.setLayout(new GridLayout(14,1));
		//Give frame an initial size: width and height
		jfrm.setSize (400,300);
		// Terminate prrogram when user closes the application (window)
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jlabChooseWorkout.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jfrm.add (jlabChooseWorkout);
		jfrm.setVisible(true);
		jfrm.setLocation (50, 350); //in, down
	}
	public void createCheckBox(){
		jcbConfirm.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jfrm.add (jcbConfirm);
	}
}