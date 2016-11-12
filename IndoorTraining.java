import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class IndoorTraining{
	public static void main (String args[]) throws IOException{
	  System.out.println ("Ready to Start");
	  try {
		  BufferedReader in = new BufferedReader (new FileReader (new File ("MainMenu.txt")));
		  System.out.println (in.readLine());
			String delim = " , ";
			String aaa = "";
		  for (String x = in.readLine(); x != null; x = in.readLine()){
			  System.out.println (x);
			aaa = x;
			//String[] tokens = aaa.split(delim);
		//	for (int i = 0; i < tokens.length; i++)
		//		System.out.println(tokens[i]);
		  }

	  } catch (IOException e){
	  System.out.println ("File I/O Error!");
	  }
	  System.out.println ("File Close Successful");
	  System.out.println ("JRadioButtonMenuItem Example");
	}
}