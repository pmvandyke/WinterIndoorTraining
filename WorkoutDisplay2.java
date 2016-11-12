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
	
		//Display the Frame
		jfrm.setVisible(true);
		
		jtfExercize.setText("Exercize 1");
		jtfTask.setText("Task 1");
		int workoutTime = 10;
		jtfTotalTime.setText(displayTime(workoutTime));
		jtfTimeRemaining.setText(displayTime(workoutTime));
		
	//	for (int i=0;workoutTime!=0;i++){
	//		try{
	//			Thread.sleep(1000);
	//		}catch (InterruptedException e){}
	//		workoutTime--;
	//		jtfTimeRemaining.setText(displayTime(workoutTime));
	//	}
		
	  //	for (int i=0;workoutTime!=0;i++){
	  //		currentTime = System.currentTimeMillis();
	  //		newTime = currentTime + 1000;
	  //		do{
	  //			currentTime = System.currentTimeMillis();
	  //		}while (currentTime < newTime);
	  //		workoutTime--;
	//		jtfTimeRemaining.setText(displayTime(workoutTime));
		//	System.out.println("set jtfTimeRemaining: ");
		//	jfrm.setVisible(true);
	  //	}
		

		displayWorkoutDetails();
	}
	// needs to be a method here to set all the text fields that change whith the tasks. ****************************************
	// all within Dispay workout method *******************************8
	
	//Format todaysDate
	public String todaysDate(){
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM yyyy dd hh:mm:ss");
		String todaysDate = formatter.format(date);
		System.out.println("Today's Date = " + todaysDate);
		return todaysDate;
		}
		
	// Set Exercize Name, Task Name, Total Time and Time Remaining
	public void displayWorkoutDetails() {
		System.out.println("displayWorkoutDetails: ");	
		String inputFileName = "TestWorkout.txt";
		String delim = "#";
		String workoutTask = "";
		int workoutTime = 0;
		
		try {
			BufferedReader in = new BufferedReader (new FileReader (new File (inputFileName)));	
			for (String inputString = in.readLine(); inputString != null; inputString = in.readLine()){
				String parts [] = inputString.split(delim);
				workoutTask = parts [1];
				workoutTime = Integer.valueOf(parts [0]);
				
				if (workoutTime == 0){
					System.out.println("Part 0 = " + workoutTime);
					jtfExercize.setText(workoutTask);
					jtfTask.setText("");
				}
				if (workoutTime != 0){
					jtfTask.setText(workoutTask);
					jtfTotalTime.setText(displayTime(workoutTime));
					displayTimeRemaining(workoutTime);
				}
			}
		} catch (IOException e){
			System.out.println ("File I/O Error! " + e);
			System.exit(1);
		}
		
	}
	//****************************************************************** Needs a return?????
	public void displayTimeRemaining(int workoutTime){
		long currentSeconds;
		long newSeconds;
		int totalTime = 0;
		String timeRemaining;
		
		totalTime = workoutTime;
		// loop and count down seconds until no time is left
		for (int i=0; totalTime != 0;i++){
			timeRemaining = displayTime (totalTime);
			currentSeconds = todaysSeconds();
			newSeconds = todaysSeconds();
			do {
				do {
				newSeconds = todaysSeconds();
				}while (currentSeconds == newSeconds);
			totalTime--;
			currentSeconds = newSeconds;
			timeRemaining = displayTime (totalTime);
			jtfTimeRemaining.setText(timeRemaining);
			
			}while(totalTime != 0);
		}
	}
	public long todaysSeconds (){
	//Calculate seconds from current time in Milliseconds
		long mSeconds = System.currentTimeMillis();
		long secondsTime = (mSeconds/1000) % 60;
		return secondsTime;
	}
	public String displayTime(int totalTime){;
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