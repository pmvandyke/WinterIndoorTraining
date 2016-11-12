	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;
	import javax.swing.SpringLayout;
	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.Calendar;
	import java.io.*;
	import java.io.IOException;
	import java.util.*;
	
	
 class RunWorkout extends Frame {
	
	int taskTime;
	long currentTime;
	long newTime;
	String inputFileName = "TestWorkout.txt";
	String delim = "#";
	String workoutTask = "";
	int workoutTime = 0;
	
	JTextField jtfDate;
	JTextField jtfExercize;
	JTextField jtfTask;
	JTextField jtfTotalTime;
	JTextField jtfTimeRemaining;
	
	Thread updateWODetails; //Second thread
	
	public RunWorkout() {
		
		// Create a new JFrame container
		JFrame jfrm = new JFrame ("Run Workout");
		// Specify a GridLayout for the layout manager
		//jfrm.setLayout(new GridLayout(7,2));
		jfrm.setLayout(new FlowLayout());
 
		// Set initian Frame Size
		jfrm.setSize(500, 200);
		// Terminate the program when the user closes the program
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		//Create Labels
		JLabel jlabDate = new JLabel          ("   Today's Date:   ");
		jlabDate.setHorizontalAlignment(JLabel.RIGHT);
		JLabel jlabExercize = new JLabel      ("       Exercize:   ");
		jlabExercize.setHorizontalAlignment(JLabel.RIGHT);
		JLabel jlabTask = new JLabel          ("           Task:   ");
		jlabTask.setHorizontalAlignment(JLabel.RIGHT);
		JLabel jlabTotalTime = new JLabel     ("               Total Time:   ");
		jlabTotalTime.setHorizontalAlignment(JLabel.RIGHT);
		JLabel jlabTimeRemaining = new JLabel ("Time Remaining:   ");
		jlabTimeRemaining.setHorizontalAlignment(JLabel.RIGHT);
	
		// Create Text Fields
		jtfDate = new JTextField (30);
		jtfExercize = new JTextField (30);
		jtfTask = new JTextField (30);
		jtfTotalTime = new JTextField (30);
		jtfTimeRemaining = new JTextField (30);
	
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
		
		// Create the Runnable instance to become second thread
		// This method will run in a separate thread
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
						if (workoutTime == 0){
							jtfExercize.setText(workoutTask);
							jtfTask.setText("");
						}
						if (workoutTime != 0){
							//System.out.println("Part 1 = " + taskTime);
							jtfTask.setText(workoutTask);
							jtfTotalTime.setText(displayTime(workoutTime));
							jtfTimeRemaining.setText(displayTime(workoutTime));
							jtfTimeRemaining.setFont(new Font("Times New Roman", Font.PLAIN, 12));
							try{
								for (int i=0;taskTime !=0; i++){
									Thread.sleep(1000);
									SwingUtilities.invokeLater(new Runnable(){
										public void run() {
											decrementTaskTime ();
										}
									});
								}
							}catch (InterruptedException exc){
									System.out.println ("Call to sleep interrupted: ");
									System.exit(1);
							}
						}		
							
					}										
				}catch (IOException e){
					System.out.println ("File I/O Error! " + e);
					System.exit(1);
				}
			}
		};
		
		// Create new thread
		updateWODetails = new Thread (secondThread);
		
		// Start the second thread
		updateWODetails.start();
		
		//Display the Frame
		jfrm.setVisible(true);
	}	
	public void decrementTaskTime(){
		//Decrement task time by 1 
		taskTime--;
		if (taskTime  < 0) taskTime = 0;
		jtfTimeRemaining.setText(displayTime(taskTime));
		if (taskTime < 6) jtfTimeRemaining.setFont(new Font("Times New Roman", Font.BOLD, 16));
	}

	//Format todaysDate
	public String todaysDate(){
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm:ss");
		String todaysDate = formatter.format(date);
		System.out.println("Today's Date = " + todaysDate);
		return todaysDate;
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
					new RunWorkout();		
			}
		});
	}
 }