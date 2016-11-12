	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.Calendar;
	
public class HelloWorld {
	
	public static void main(String args[]) {
		
		Date date = Calendar.getInstance().getTime();
    	SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM yyyy dd hh:mm:ss");
		String todaysDate = formatter.format(date);
		System.out.println("Today's Date = " + todaysDate);
		
		int x = 0;
		while (x < 2) {
		System.out.println("Hello to: "+ x + " "+ args[0]);
		x++;
		}
	}
}