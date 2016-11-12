	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;
	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.Calendar;
	import java.io.*;
	import java.io.IOException;
	import java.util.*;
	
	
 class RunWorkout extends JPanel {
	
	String currentSeconds;
	String timeRemaining;
	int taskTime;
	long currentTime;
	long newTime;
int j = 1;
	
	JTextField jtfDate;
	JTextField jtfExercize;
	JTextField jtfTask;
	JTextField jtfTotalTime;
	JTextField jtfTimeRemaining;
	
	Thread updateWODetails; //Second thread
	
	public RunWorkout() {
		System.out.println("Start Workout Display 5 ");	
		// Create a new JFrame container
		JFrame jfrm = new JFrame ("Run Workout");
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
		
		// Create the Runnable instance to become second thread
		// This method will run in a separate thread
		Runnable secondThread = new Runnable() {
			public void run(){
				for (int j=1; j<2; j++){
					jtfExercize.setText("Exercise: " + j);
					jtfTask.setText("Task: " + j);
					taskTime = 5;
					timeRemaining = Integer.toString(taskTime);
					jtfTotalTime.setText(displayTime(taskTime));
					try{
						for (int i=0; taskTime > 0;i++){
							//System.out.println("task Time: " + taskTime);
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
		};
		
		// Create new thread
		updateWODetails = new Thread (secondThread);
		
		// Start the second thread
		updateWODetails.start();
		
		//Display the Frame
		jfrm.setVisible(true);
	}	
	public void decrementTaskTime(){
			//String timeRemaining = Integer.toString(taskTime);
			System.out.println("Time Remaining: " + displayTime(taskTime));
			//System.out.println("Today's Date = " + todaysDate());	
			jtfTimeRemaining.setText(displayTime(taskTime));
			taskTime--;
			
	}

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
				System.out.println("Input Record: " + inputString);
				if (workoutTime == 0){
					jtfExercize.setText(workoutTask);
					jtfTask.setText("");
				}
				if (workoutTime != 0){
						System.out.println("Part 1 = " + workoutTime);
					jtfTask.setText(workoutTask);
					jtfTotalTime.setText(displayTime(workoutTime));
					System.out.println("displayTimeRemaining " );
					int totalTime = 0;
					String timeRemaining;
					totalTime = workoutTime;
					// loop and count down seconds until no time is left
					for (int i=0; totalTime != 0;i++){
						try{
							Thread.sleep(1000);
							totalTime--;
							System.out.println (totalTime);
		//					timeRemaining = displayTime (totalTime);
							timeRemaining = (" " + totalTime);
							jtfTimeRemaining.setText(timeRemaining);
						}catch (InterruptedException exc){
							System.out.println ("Call to sleep interrupted: ");
							System.exit(1);
						}
					}
					//displayTimeRemaining(workoutTime);
				}
			}
		} catch (IOException e){
			System.out.println ("File I/O Error! " + e);
			System.exit(1);
		}
	System.out.println("Today's Date = " + todaysDate());		
	}
	// This is important code, move to WorkoutDisplay
	public void displayTimeRemaining(int workoutTime){
	System.out.println("displayTimeRemaining " );
		int totalTime = 0;
		String timeRemaining;
		totalTime = workoutTime;
		// loop and count down seconds until no time is left
		for (int i=0; totalTime != 0;i++){
			try{
				Thread.sleep(1000);
				totalTime--;
	System.out.println (totalTime);
				timeRemaining = displayTime (totalTime);
				jtfTimeRemaining.setText(timeRemaining);
			}catch (InterruptedException exc){
				System.out.println ("Call to sleep interrupted: ");
				System.exit(1);
			}
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