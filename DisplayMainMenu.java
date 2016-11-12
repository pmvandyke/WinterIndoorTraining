import java.awt.*;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;
import java.io.IOException;
import java.util.*;

 class DisplayMainMenu  extends JFrame implements ActionListener{
	 
	String message = "";
	String actionCommand = "";
	 
	JFrame jfrm ;
	JLabel jlabConfirmSession;
	JLabel jlabChooseWorkout ;
	JLabel jlabSessionDelayed;
	JCheckBox jcbConfirm ;
	JRadioButton jrbWinterWorkout;
	JRadioButton jrbSpringWorkout;
	JRadioButton jrbSummerWorkout;
	JRadioButton jrbFallWorkout;
	
	 
	public DisplayMainMenu (){
			
			
		createFrameDetails();
		createLabels();
		createCheckBox();
		createRadioButtons();
		addButtonsToGroup();
		//setActionCommand ();
		addActionListener ();
		addToJFrame();
		setVisible ();
	
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
		
	}
	void createFrameDetails(){
		// Create a new JFrame container
		jfrm = new JFrame ("Indoor Workout Main Menu");
		jfrm.setFont(new Font("Times New Roman", Font.BOLD, 16));
		// Specify a Grid Layout 7 x 1 for the layout manager
		jfrm.setLayout(new GridLayout(7,1));
		// Set initian Frame Size
		jfrm.setSize(350, 175); //wide, tall
		// Terminate the program when the user closes the program
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	void createLabels(){
		jlabChooseWorkout = new JLabel("Select a Workout: ");
		jlabSessionDelayed = new JLabel(" ");
	}
	
	void createCheckBox(){
		
		jcbConfirm = new JCheckBox("Confirm Workout");
	}
	
	void createRadioButtons(){
		jrbWinterWorkout = new JRadioButton ("Winter Workout");
		jrbSpringWorkout = new JRadioButton ("Spring Workout");
		jrbSummerWorkout = new JRadioButton ("Summer Workout");
		jrbFallWorkout = new JRadioButton ("Fall Workout");
	}
	
	void addButtonsToGroup(){
		ButtonGroup bg = new ButtonGroup();
		bg.add (jrbWinterWorkout);
		bg.add (jrbSpringWorkout);
		bg.add (jrbSummerWorkout);
		bg.add (jrbFallWorkout);
	}
		
	void addActionListener (){
		jrbWinterWorkout.addActionListener(this);
		jrbSpringWorkout.addActionListener(this);
		jrbSummerWorkout.addActionListener(this);
		jrbFallWorkout.addActionListener(this);
	}
	
	void addToJFrame(){		
		jfrm.add (jlabChooseWorkout);
		
		jfrm.add (jrbWinterWorkout);
		jfrm.add (jrbSpringWorkout);
		jfrm.add (jrbSummerWorkout);
		jfrm.add (jrbFallWorkout);
		jcbConfirm.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jfrm.add (jcbConfirm);
		jfrm.add (jlabSessionDelayed);
	}

	void setVisible(){
		jfrm.setVisible(true);
		jfrm.setLocation (50, 100);
	}
	
	// display the next workout routine
	public void displayNextWorkout(){
		
		String nextWorkout = actionCommand;
		//System.out.println("Next Workout: " + nextWorkout );
		// Added to launch DisplayWorkoutMenu with the selected nextWorkout
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				jfrm.setVisible(false);		
				new DisplayWorkoutMenu(nextWorkout);
			}
		});
	}
	//Listen for Action Events 
	public void actionPerformed(ActionEvent ae){
		String cmd = ae.getActionCommand();
		actionCommand = cmd;
		jlabSessionDelayed.setText (" ");
		message = "Confirm Selection: " + cmd ;
		jcbConfirm.setText(message);
	}
	//Run Main 
	public static void main (String args[]){
		//Create the frame on the event dispatching thread
			SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					new DisplayMainMenu();		
			}
		});
	}
 }
 