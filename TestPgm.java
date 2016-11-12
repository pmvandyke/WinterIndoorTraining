	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.Calendar;
	import java.io.*;
	import java.io.IOException;
	
public class TestPgm {

	String currentSeconds;
	String newSeconds;
	
	public static String displayTime(int totalTime){;
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
		return timeDisplay;
	}
	public static void todaysDate (){
	//Format todaysDate
		Date date = Calendar.getInstance().getTime();
    	SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM yyyy dd hh:mm:ss");
		String todaysDate = formatter.format(date);
		System.out.println("Today's Date = " + todaysDate);
	}	
	public static long todaysSeconds (){
	//Calculate seconds from current time in Milliseconds
		long mSeconds = System.currentTimeMillis();
		long secondsTime = (mSeconds/1000) % 60;
		return secondsTime;
	}
	public static void displayTasks(String workoutTask, int workoutTime) {
	//Display Summary Task or Task with Time Remaining
		
		long currentSeconds;
		long newSeconds;
		int totalTime = 0;
		String timeRemaining;
		
		System.out.println (" ");
		if (workoutTime == 0) System.out.println (workoutTask);
		if (workoutTime != 0) System.out.println (workoutTask + " " + workoutTime + " Seconds");
		totalTime = workoutTime;
		// loop and count down seconds until no time is left
		for (int i=0; totalTime != 0;i++){
			System.out.println(" " );
			timeRemaining = displayTime (totalTime);
			System.out.println("Time Remaining: " + timeRemaining);
			currentSeconds = todaysSeconds();
			newSeconds = todaysSeconds();
			do {
				do {
				newSeconds = todaysSeconds();
				}while (currentSeconds == newSeconds);
			totalTime--;
			currentSeconds = newSeconds;
			timeRemaining = displayTime (totalTime);
			System.out.println("Time Remaining: " + timeRemaining);
			}while(totalTime != 0);
		}
	}
	public static void main(String args[]) {
	//Read in workout file, determine Task and Duration 
		String inputFileName = "TestWorkout.txt";
		String delim = " , ";
		String workoutTask = "";
		int workoutTime = 0;
		
		todaysDate();
	
		try {
			BufferedReader in = new BufferedReader (new FileReader (new File (inputFileName)));	
			for (String inputString = in.readLine(); inputString != null; inputString = in.readLine()){
				String parts [] = inputString.split(delim);
				workoutTask = parts [0];
				workoutTime = Integer.valueOf(parts [1]);;
				Return(workoutTask,workoutTime);
			}
		} catch (IOException e){
			System.out.println ("File I/O Error! " + e);
			System.exit(1);
		}
		
	}	
}