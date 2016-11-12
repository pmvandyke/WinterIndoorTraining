	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.Calendar;
	
public class TestPgm1 {

	String currentSeconds;
	String newSeconds;
	
	public static void todaysDate (){
		Date date = Calendar.getInstance().getTime();
    	SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM yyyy dd hh:mm:sssss");
		String todaysDate = formatter.format(date);
		System.out.println("Today's Date = " + todaysDate);
	}
	
	public static String todaysSeconds (){
		long startTime = System.currentTimeMillis();
		System.out.println("Today's milliSecond = " + startTime/1000);
		Date date = Calendar.getInstance().getTime();
    	SimpleDateFormat formatter = new SimpleDateFormat("ss");
		String todaysSeconds = formatter.format(date);
		//System.out.println("Today's Second = " + todaysSeconds);
		return todaysSeconds;
	}
	public static void main(String args[]) {
		String currentSeconds;
		String newSeconds;
		
		currentSeconds = todaysSeconds();
		System.out.println("Current Seconds = " + currentSeconds);
		
		newSeconds = todaysSeconds();
		System.out.println("new Seconds = " + newSeconds);
		int totalTime = 15;
		System.out.println("totalTime = " + totalTime);
		todaysDate();
		do {
			do {
			//System.out.println("totalTime = " + totalTime);
			newSeconds = todaysSeconds();
			}while (currentSeconds == newSeconds);
			
			totalTime--;
			currentSeconds = newSeconds;
		}while(totalTime != 0);
		todaysDate();
	}	
}