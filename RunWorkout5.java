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
	
//*  Breif description.
//*  RunWorkout creates a frame (jfrm) on a Runnable Thread that contains 
//*  the Date, Session, Exercise, Task and TotalTime labels.
//*  The values for the TextFiels associated with each, are read in form an external file (inputFileName)
//*  which contains the workout details. 
//*  The final Label and TextField is the time remaining which is a count down from the TotalTime.
//*  The countdown is accomplsihed using a second thread which waits (1000 millis).
//*  There is a CheckBox that allows the user to pause the countdown and then resume. 
//*  The Pause function is yet to be implemented,
	
 class RunWorkout  extends Frame {
	 
	int taskTime;
	long currentTime;
	long newTime;
	String inputFileName = " ";
	String delim = "#";
	String workoutTask = "";
	int workoutTime = 0;
	int totalWorkoutTime = 0;
	static String inputSession;
	
	JFrame jfrm = new JFrame ("Detail Training Exercise ");
	JTextField jtfDate;
	JTextField jtfSession;
	JTextField jtfExercize;
	JTextField jtfTask;
	JTextField jtfTotalTime;
	JTextField jtfTimeRemaining;
	JCheckBox jcbPause;
	
	JLabel jlabDate =    new JLabel       ("               Today's Date:  ");
	JLabel jlabExercize = new JLabel      ("                        Exercize: ");
	JLabel jlabSession = new JLabel        ("                       Session:   ");
	JLabel jlabTask = new JLabel          ("                             Task:  ");
	JLabel jlabTotalTime = new JLabel     ("                    Total Time: ");
	JLabel jlabTimeRemaining = new JLabel ("                    Remaining: ");
	JLabel jlabJobWellDone = new JLabel (" ");
	
	// Second Frame
	JFrame jfrm2 = new JFrame ("Exercise Timer ");
	JLabel jlabTotTime =    new JLabel       ("          Total Time:  ");
	JLabel jlabTimeLeft =    new JLabel       (" Time Remaining:  ");
	JTextField jtfTotTime;
	JTextField jtfTimeLeft;
	
	Thread updateWODetails; //Second thread
	
	boolean paused = false;
	
	public RunWorkout(String inputSession) {
		
		inputFileName = inputSession + ".txt";	
		createJFrameFields(inputSession);
		alignLabelsRight();
		createJTextFields();
		createCheckBox();
		setDateAndSession(inputSession);
		addComponentsToJFrame();
		makeFrameVisable();
		
		displayWorkout();
	}

	public void displayWorkout() {
	    // Create the Runnable instance to become second thread
		// This method will run in a separate thread
		// Create new thread
		
		Runnable secondThread = new Runnable() {
			public void run(){
				try {
					BufferedReader in = new BufferedReader (new FileReader (new File (inputFileName)));	
					for (String inputString = in.readLine(); inputString != null; inputString = in.readLine()){
						String parts [] = inputString.split(delim);
						workoutTask = parts [1];
						workoutTime = Integer.valueOf(parts [0]);
						taskTime= workoutTime;
						//System.out.println("Input Record: " + inputString);
						if (workoutTime > 3000){
							totalWorkoutTime = workoutTime;
							workoutTime = 0;
							jtfTotTime.setText(displayTime(totalWorkoutTime));
						}
						if (workoutTime == 0){
							jtfExercize.setText(workoutTask);
							jtfTask.setText("");
						}
						if (workoutTime != 0){
							jtfTask.setText(workoutTask);
							jtfTotalTime.setText(displayTime(workoutTime));
							jtfTimeRemaining.setText(displayTime(workoutTime));
							jtfTimeRemaining.setFont(new Font("Dialog", Font.PLAIN, 12));
							jtfTimeRemaining.setForeground(Color.black);
							try{
								for (int i=0;taskTime !=0; i++){
									Thread.sleep(1000);
									if(!paused) {
										SwingUtilities.invokeLater(new Runnable(){
											public void run() {
												decrementTotalTime ();
											}
										});
									}
								}
							}catch (InterruptedException exc){
									System.out.println ("Call to sleep interrupted: ");
									System.exit(1);
							}
						}			
					}
					jobWellDone();										
				}catch (IOException e){
					System.out.println ("File I/O Error! " + e);
					System.exit(1);
				}
			}
		};	
		
		// Action listener to jcbConfirm
		jcbPause.addItemListener (new ItemListener (){
			public void itemStateChanged(ItemEvent ie){
				if (jcbPause.isSelected()){
					paused = true;
				}
				else{
					paused = false;
				}
			}		
		}); 	
		// Create new thread	
		updateWODetails = new Thread (secondThread);
		updateWODetails.start();
	}
	
	
	public void makeFrameVisable(){
		//Display the Frame
		jfrm.setVisible(true);	
		jfrm.setLocation (500, 100);// in,down
		
		//Display Frame 2	
		jfrm2.setVisible(true);
		jfrm2.setLocation (1100, 100);// in,down
	}
	
	public void jobWellDone(){
		jlabJobWellDone.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jlabJobWellDone.setForeground(Color.blue);
		jlabJobWellDone.setText("Congratulations! Workout Completed. Click Close Icon to Exit");
	}
	public void createJFrameFields(String inputSession){
		// jfrm fields
		jfrm.setLayout(new FlowLayout());
		jfrm.setSize(510, 250);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//jfrm2 fields
		jfrm2.setLayout(new FlowLayout());
		jfrm2.setSize(250, 100); //wide, tall
		jfrm2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void alignLabelsRight() {
		//jfrm Fields
		jlabDate.setHorizontalAlignment(JLabel.RIGHT);
		jlabExercize.setHorizontalAlignment(JLabel.RIGHT);
		jlabSession.setHorizontalAlignment(JLabel.RIGHT);
		jlabTask.setHorizontalAlignment(JLabel.RIGHT);
		jlabTotalTime.setHorizontalAlignment(JLabel.RIGHT);
		jlabTimeRemaining.setHorizontalAlignment(JLabel.RIGHT);
		jlabJobWellDone.setHorizontalAlignment(JLabel.RIGHT);
		//jfrm2 fields
		jlabTotTime.setHorizontalAlignment(JLabel.RIGHT);
		jlabTimeLeft.setHorizontalAlignment(JLabel.RIGHT);
	}
	
	public void createJTextFields(){
		// jfrm fields
		jtfDate = new JTextField (30);
		jtfSession = new JTextField (30);
		jtfExercize = new JTextField (30);
		jtfTask = new JTextField (30);
		jtfTotalTime = new JTextField (30);
		jtfTimeRemaining = new JTextField (30);	
		//jfrm2 fields
		jtfTotTime = new JTextField (10);
		jtfTimeLeft = new JTextField (10);
	}
	
	public void createCheckBox(){
		//Create the Check Box and set font
		jcbPause = new JCheckBox("Pause the Workout");
		jcbPause.setFont(new Font("Times New Roman", Font.BOLD, 16));
	}
	
	public void addComponentsToJFrame(){
		// Add to jfrm
		jfrm.add(jlabDate);
		jfrm.add(jtfDate);
		jfrm.add(jlabSession);
		jfrm.add(jtfSession);
		jfrm.add(jlabExercize);
		jfrm.add(jtfExercize);
		jfrm.add(jlabTask);
		jfrm.add(jtfTask);
		jfrm.add(jlabTotalTime);
		jfrm.add(jtfTotalTime);
		jfrm.add(jlabTimeRemaining);
		jfrm.add(jtfTimeRemaining);
		jfrm.add(jcbPause);	
		jfrm.add(jlabJobWellDone);
		// Add to jfrm2
		jfrm2.add(jlabTotTime);
		jfrm2.add(jtfTotTime);
		jfrm2.add(jlabTimeLeft);
		jfrm2.add(jtfTimeLeft);
	}
	
	public void setDateAndSession(String inputSession){
		// Set todaysDate from system time
		jtfDate.setText(todaysDate());
		jtfSession.setText(inputSession);
	}
	
	public void decrementTotalTime(){
		//Decrement task time by 1 
		taskTime--;
		if (taskTime  < 0) taskTime = 0;
		jtfTimeRemaining.setText(displayTime(taskTime));
		if (taskTime < 6){
			jtfTimeRemaining.setFont(new Font("Dialog", Font.BOLD, 12));
			jtfTimeRemaining.setForeground(Color.red);
		} 
		//decrement totalWorkoutTime by 1
		totalWorkoutTime--;
		if (taskTime  < 0) totalWorkoutTime = 0;
		jtfTimeLeft.setText(displayTime(totalWorkoutTime));
	}

	//Format todaysDate
	public String todaysDate(){
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm:ss");
		String todaysDate = formatter.format(date);
		return todaysDate;
	}

	String displayTime(int totalTime){;
	//Format displayTime based upon minutes remaining
		String timeDisplay;
		int minutesLeft = totalTime/60;
		int secondsLeft =  totalTime % 60;
		timeDisplay = ("" + secondsLeft);
		if (minutesLeft > 0) {
			timeDisplay =(minutesLeft + ":" + secondsLeft);
			if (secondsLeft < 10) timeDisplay =(minutesLeft + ":0" + secondsLeft);
		}
		if (minutesLeft == 0) {
			timeDisplay =("  :" + secondsLeft);
			if (secondsLeft < 10) timeDisplay =("  :0" + secondsLeft);
		}
		//System.out.println("timeDisplay = " + timeDisplay);
		return timeDisplay;
	}
	//Run Main 
	public static void main (String args[]){
		//Create the frame on the event dispatching thread
			SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					new RunWorkout(inputSession);		
			}
		});
	}
	
 }