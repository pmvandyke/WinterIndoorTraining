
public class TestPgm2{
	
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
			timeDisplay =(" :" + secondsLeft);
			if (secondsLeft < 10) timeDisplay =("   " + secondsLeft);
		}
		return timeDisplay;
		}
		
	public static void main(String args[]) {
		String timeDisplay;
		int timeArray[] = {605, 100, 60, 30, 5, 0};
		int i;
		
		for (i=0; i < timeArray.length; i++){
			timeDisplay = displayTime(timeArray[i]);
			System.out.println(timeArray[i]);
		}
	}
	
}