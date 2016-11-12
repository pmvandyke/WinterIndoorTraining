	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;
	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.Calendar;
	import java.io.*;
	import java.io.IOException;
	import java.util.*;
	
	
 class WorkoutDisplay extends JPanel {
	
	String currentSeconds;
	String newSeconds;
	long currentTime;
	long newTime;
	int totalTime;
	int workoutTime;
	String inputFileName = "TestWorkout.txt";
	boolean readFlag = true;
	String workoutTask;
	String delim = "#";
	
	JTextField jtfDate;
	JTextField jtfExercize;
	JTextField jtfTask;
	JTextField jtfTotalTime;
	JTextField jtfTimeRemaining;
	
	Thread updateWODetails; //Second thread
	
	public WorkoutDisplay() {
	
		// Create a new JFrame container
		JFrame jfrm = new JFrame ("Workout Display");
		// Specify a GridLayout for the layout manager
		jfrm.setLayout(new GridLayout(0,1));
		// Set initian Frame Size
		jfrm.setSize(240, 220);
		// Terminate the program when the user closes the program
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		//Create Labels
		JLabel jlabDate = new JLabel ("Today's Date: ");
		JLabel jlabExercize = new JLabel ("Exercize: ");
		JLabel jlabTask = new JLabel ("Task: ");
		JLabel jlabTotalTime = new JLabel ("Total Time: ");
		JLabel jlabTimeRemaining = new JLabel ("Time Remaining: ");
	
		// Create Text Fields
		jtfDate = new JTextField (20);
		jtfExercize = new JTextField (30);
		jtfTask = new JTextField (30);
		jtfTotalTime = new JTextField (6);
		jtfTimeRemaining = new JTextField (6);
	
		// Add componenets to the content pane
		jfrm.add(jlabDate);
		jfrm.add(jtfDate);
		jfrm.add(jlabExercize);
		jfrm.add(jtfExercize);
		jfrm.add(jlabTask);
		jfrm.add(jtfTask);
		jfrm.add(jlabTotalTime);
		jfrm.add(jtfTotalTime);
		jfrm.add(jlabTimeRemaining);
		jfrm.add(jtfTimeRemaining);
	
		// Set todaysDate from system time
		jtfDate.setText(todaysDate());
				
		// Read the workout file one record at a time until eofFlag - true
		int i = -1;
		System.out.println("Start to read: ");
		try {
			BufferedReader in = new BufferedReader (new FileReader (new File (inputFileName)));	
			for (String inputString = in.readLine(); inputString != null; inputString = in.readLine()){
			
			i++;
			if (i > 6) return;
				
				//String inputString = in.readline();
				String parts [] = inputString.split(delim);
				workoutTime = Integer.valueOf(parts[0]);
				workoutTask = parts[1];	
				if (workoutTime == 0){
					jtfExercize.setText(workoutTask);
					jtfTask.setText("");						
				}
				if (workoutTime != 0){
					jtfTask.setText(workoutTask);
					jtfTotalTime.setText(displayTime(workoutTime));
					jtfTimeRemaining.setText(displayTime(workoutTime));
					}
System.out.println("Input Record: " + i +" " + inputString);
							updateTimeRemaining(totalTime);
				decrementWorkoutTime(workoutTime);
				
			}
		}catch (IOException e) {
				System.out.println ("File I/O Error! " + e);
				System.exit(1);
		}
						
		// Create new thread
		updateWODetails = new Thread ();
		
		// Start the second thread
		updateWODetails.start();
		
		//Display the Frame
		jfrm.setVisible(true);
	}	
	
	public void updateTimeRemaining (int totalTime){
		//totalTime--;
		jtfTimeRemaining.setText(displayTime (workoutTime));
		System.out.println("Total Time = " + totalTime);
	}
	
		
	//Format todaysDate
	public String todaysDate(){
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM yyyy dd hh:mm:ss");
		String todaysDate = formatter.format(date);
		System.out.println("Today's Date = " + todaysDate);
		return todaysDate;
		}
		
	
	public void displayTimeRemaining(int workoutTime){
		int totalTime = 0;
		String timeRemaining;
		totalTime = workoutTime;
		// loop and count down seconds until no time is left
		for (int i=0; totalTime != 0;i++){
			try{
				Thread.sleep(1000);
				totalTime--;
				System.out.println (totalTime);
				jtfTimeRemaining.setText(displayTime (totalTime));
			}catch (InterruptedException exc){
				System.out.println ("Call to sleep interrupted: ");
				System.exit(1);
			}
		}
	}
	
	public String displayTime(int totalTime){;
	//Format displayTime based upon minutes remaining
		String timeDisplay;
		if (totalTime == 0){
			timeDisplay = "";
			return timeDisplay;
		}
		int minutesLeft = totalTime/60;
		int secondsLeft =  totalTime % 60;
		timeDisplay = ("" + secondsLeft);
		if (minutesLeft > 0) {
			timeDisplay =(minutesLeft + ":" + secondsLeft);
			if (secondsLeft < 10) timeDisplay =(minutesLeft + ":0" + secondsLeft);
		}
		if (minutesLeft == 0) {
			timeDisplay =("  " + secondsLeft);
			if (secondsLeft < 10) timeDisplay =("   " + secondsLeft);
		}
		//System.out.println("timeDisplay = " + timeDisplay);
		return timeDisplay;
	}
	//Run Main 
	public static void main (String args[]){
		//Create the frame on the event dispatching thread
			SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					new WorkoutDisplay();		
			}
		});
	}
 }