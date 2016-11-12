	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.Calendar;
	
public class TestPgm {

	String currentSeconds;
	String newSeconds;
	
	public static String displayTime(int totalTime){;
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
		Date date = Calendar.getInstance().getTime();
    	SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM yyyy dd hh:mm:ss");
		String todaysDate = formatter.format(date);
		System.out.println("Today's Date = " + todaysDate);
	}	
	public static long todaysSeconds (){
		long mSeconds = System.currentTimeMillis();
		long secondsTime = (mSeconds/1000) % 60;
		return secondsTime;
	}
	public static void main(String args[]) {
		long currentSeconds;
		long newSeconds;
		String timeRemaining;
		//todaysDate();			
		String workoutTask [] = {"1. Zone 2 - Right Leg Only","1. Zone 2 - Right Leg Only"};
		int workoutDuration []= {15,10};
		
		for (int i=0; i<workoutDuration.length;i++){
			int totalTime = workoutDuration[i];
			System.out.println(" " );
			System.out.println (workoutTask[i] + " " + totalTime + " Seconds");
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
			//todaysDate();
	}	
}