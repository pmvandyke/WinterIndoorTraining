	import java.awt.*;
	import java.awt.Container;
	import java.awt.event.*;
	import javax.swing.*;
	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.Calendar;
	import java.io.*;
	import java.io.IOException;
	import java.util.*;
	
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;

	import java.util.ArrayList;
	import java.util.Properties;
	
//*  Breif description.
//*  RunWorkout creates a frame (jfrm) on a Runnable Thread that contains 
//*  the Date, Session, Exercise, Task and TotalTime labels.
//*  The values for the TextFields associated with each, are read in form an external file (inputFileName)
//*  which contains the workout details. 
//*  The final Label and TextField is the time remaining which is a count down from the TotalTime.
//*  The countdown is accomplsihed using a second thread which waits (1000 millis).
//*  There is a CheckBox that allows the user to pause the countdown and then resume. 
//*  The Pause function is yet to be implemented,
	
 class RunWorkout  extends Frame{
	 
	// database variables the default framework is embedded, dbName is the name of the database 
	static String framework = "embedded";
	static String  protocol = "jdbc:derby:";
	static String dbName = "MyTestDB"; 
	static Statement s;
	static ResultSet rs = null;
	static Connection conn = null;
	String tableName;
	
	int taskTime;
	long currentTime;
	long newTime;
	String inputFileName = " ";
	String delim = "#";
	String workoutTask = "";
	int workoutTime = 0;
	int totalWorkoutTime = 0;
	static String inputSession;
	String summaryName;
	String inputWorkout="Test Workout";
	JFrame jfrm = new JFrame ("Detail Training Exercise ");
	JTextField jtfDate;
	JTextField jtfSession;
	JTextField jtfExercize;
	JTextField jtfTask;
	JTextField jtfTotalTime;
	JTextField jtfTimeRemaining;
	JCheckBox jcbPause;
	
	JLabel jlabDate =    new JLabel       ("               Today's Date:  ");
	JLabel jlabExercize = new JLabel      ("                        Exercize: ");
	JLabel jlabSession = new JLabel        ("                       Session:   ");
	JLabel jlabTask = new JLabel          ("                             Task:  ");
	JLabel jlabTotalTime = new JLabel     ("                    Total Time: ");
	JLabel jlabTimeRemaining = new JLabel ("                    Remaining: ");
	JLabel jlabJobWellDone = new JLabel (" ");
	
	// Second Frame
	JFrame jfrm2 = new JFrame ("Exercise Timer ");
	JLabel jlabTotTime =    new JLabel       ("          Total Time:  ");
	JLabel jlabTimeLeft =    new JLabel       ("Time Remaining:  ");
	JTextField jtfTotTime;
	JTextField jtfTimeLeft;
	
	Thread updateWODetails; //Second thread
	boolean paused = false;
	
	public RunWorkout(String inputSession) {
		
	///DB Variables

		tableName = inputWorkout.substring (0,12);
		tableName = tableName.replace(".","");
		tableName = tableName.replace (" ","");
		System.out.println("Table Name: " + tableName);
		Connection conn = null;
		ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
		PreparedStatement psInsert;
		PreparedStatement psUpdate;
		Statement s;
		ResultSet rs = null;
		
		inputFileName = inputSession + ".txt";	
		createJFrameFields(inputSession);
		alignLabelsRight();
		createJTextFields();
		createCheckBox();
		setDateAndSession(inputSession);
		addComponentsToJFrame();
		makeFrameVisable();
		
	
	
		openDataBase (dbName);
		//	createTable(tableName);
		displayWorkout();
		closeAll();
		
	}
		
	public void displayWorkout()  {
		 // Create the Runnable instance to become second thread
		// This method will run in a separate thread
		// Create new thread
	
		//Runnable secondThread = new Runnable() {
			//public void run(){
				System.out.println("Read from table: " + tableName);
				try{      //Read Data Base for WorkoutDetails
					Statement s = conn.createStatement();
							 
					rs = s.executeQuery("SELECT  seq, summary, duration, description FROM " +tableName + " ORDER BY  seq");
					while(rs.next()) {
						int sequence  = rs.getInt(1);
						summaryName = rs.getString(2);
						workoutTime = rs.getInt(3);
						workoutTask = rs.getString(4);
				System.out.println(" 1:" + sequence +  " 2:" + summaryName + " 3:" + workoutTime +" 4:" + workoutTask);
							taskTime = workoutTime;

				String snTest = summaryName.substring(0,1);				
						if (sequence == 0) {
							jtfSession.setText(workoutTask);
							//set session timer
						}	
						
						if (summaryName.length()>2) 	{
								jtfExercize.setText(summaryName);
								jtfTask.setText("");
								workoutTime=0;
								//set summary timer
						}	else{
							jtfTask.setText(summaryName);
							// set task timer
						}
						if (workoutTime > 3000){
							totalWorkoutTime = workoutTime;
							workoutTime = 0;
							jtfTotTime.setText(displayTime(totalWorkoutTime));
						}
						if (workoutTime == 0){
							jtfExercize.setText(workoutTask);
							jtfTask.setText("");
						}
						if (workoutTime != 0){
							jtfTask.setText(workoutTask);
							jtfTotalTime.setText(displayTime(workoutTime));
							jtfTimeRemaining.setText(displayTime(workoutTime));
							jtfTimeRemaining.setFont(new Font("Dialog", Font.PLAIN, 12));
							jtfTimeRemaining.setForeground(Color.black);
							
							for (int i=0;taskTime !=0; i++){
								
								if(!paused) {
								//	SwingUtilities.invokeLater(new Runnable(){
								//		public void run() {
											System.out.println(" TaskTime: "+ taskTime); 
											//System.out.println(" workoutTimer: " +taskTime); 
											decrementTotalTime ();
									//	}
								//	});
								}
							}
						}
					}// End of DB Table Read			
					jobWellDone();	
				}  catch (SQLException sqle){
					System.out.println ("Read Data Base Error! " +sqle);
					System.exit(1);
				}		
		//	}
	//	};
		
	  // Create new thread	
	//updateWODetails = new Thread (secondThread);
	//updateWODetails.start(); 	
	
		// Action listener to jcbConfirm
		jcbPause.addItemListener (new ItemListener (){
			public void itemStateChanged(ItemEvent ie){
				if (jcbPause.isSelected()){
					paused = true;
				}
				else{
					paused = false;
				}
			}		
		}); 	
	
	}
	
	public void makeFrameVisable(){
		//Display the Frame
		jfrm.setVisible(true);	
		jfrm.setLocation (500, 100);// in,down
		
		//Display Frame 2	
		jfrm2.setVisible(true);
		jfrm2.setLocation (1100, 100);// in,down
		System.out.println(" Frames made visable " ); 
	}
	
	public void jobWellDone(){
		jlabJobWellDone.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jlabJobWellDone.setForeground(Color.blue);
		jlabJobWellDone.setText("Congratulations! Workout Completed. Click Close Icon to Exit");
	}
	
	public void createJFrameFields(String inputSession){
		// jfrm fields
		jfrm.setLayout(new FlowLayout());
		jfrm.setSize(510, 250);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//jfrm2 fields
		jfrm2.setLayout(new FlowLayout());
		jfrm2.setSize(250, 100); //wide, tall
		jfrm2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void alignLabelsRight() {
		//jfrm Fields
		jlabDate.setHorizontalAlignment(JLabel.RIGHT);
		jlabExercize.setHorizontalAlignment(JLabel.RIGHT);
		jlabSession.setHorizontalAlignment(JLabel.RIGHT);
		jlabTask.setHorizontalAlignment(JLabel.RIGHT);
		jlabTotalTime.setHorizontalAlignment(JLabel.RIGHT);
		jlabTimeRemaining.setHorizontalAlignment(JLabel.RIGHT);
		jlabJobWellDone.setHorizontalAlignment(JLabel.RIGHT);
		//jfrm2 fields
		jlabTotTime.setHorizontalAlignment(JLabel.RIGHT);
		jlabTimeLeft.setHorizontalAlignment(JLabel.RIGHT);
	}
	
	public void createJTextFields(){
		// jfrm fields
		jtfDate = new JTextField (30);
		jtfSession = new JTextField (30);
		jtfExercize = new JTextField (30);
		jtfTask = new JTextField (30);
		jtfTotalTime = new JTextField (30);
		jtfTimeRemaining = new JTextField (30);	
		//jfrm2 fields
		jtfTotTime = new JTextField (10);
		jtfTimeLeft = new JTextField (10);
	}
	
	public void createCheckBox(){
		//Create the Check Box and set font
		jcbPause = new JCheckBox("Pause the Workout");
		jcbPause.setFont(new Font("Times New Roman", Font.BOLD, 16));
	}
	
	public void addComponentsToJFrame(){
		// Add to jfrm
		jfrm.add(jlabDate);
		jfrm.add(jtfDate);
		jfrm.add(jlabSession);
		jfrm.add(jtfSession);
		jfrm.add(jlabExercize);
		jfrm.add(jtfExercize);
		jfrm.add(jlabTask);
		jfrm.add(jtfTask);
		jfrm.add(jlabTotalTime);
		jfrm.add(jtfTotalTime);
		jfrm.add(jlabTimeRemaining);
		jfrm.add(jtfTimeRemaining);
		jfrm.add(jcbPause);	
		jfrm.add(jlabJobWellDone);
		// Add to jfrm2
		jfrm2.add(jlabTotTime);
		jfrm2.add(jtfTotTime);
		jfrm2.add(jlabTimeLeft);
		jfrm2.add(jtfTimeLeft);
	}
	
	public void setDateAndSession(String inputSession){
		// Set todaysDate from system time
		jtfDate.setText(todaysDate());
		jtfSession.setText(inputSession);
	}
	
	public void decrementTotalTime(){	
		
				System.out.println(" workoutTimer: " +taskTime); 
				try{//Decrement task time by 1 
					System.out.println("Thread sleep 1 second");
					Thread.sleep(500);
					taskTime--;
					if (taskTime  < 0) taskTime = 0;
					jtfTimeRemaining.setText(displayTime(taskTime));
					if (taskTime < 6){
						jtfTimeRemaining.setFont(new Font("Dialog", Font.BOLD, 12));
						jtfTimeRemaining.setForeground(Color.red);
					} 
					//decrement totalWorkoutTime by 1
					totalWorkoutTime--;
					if (taskTime  < 0) totalWorkoutTime = 0;
					jtfTimeLeft.setText(displayTime(totalWorkoutTime));
				}catch (InterruptedException exc){
					System.out.println ("Call to sleep interrupted: " + exc);
					System.exit(1);
				}			
				
	}
	//Format todaysDate
	public String todaysDate(){
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm:ss");
		String todaysDate = formatter.format(date);
		return todaysDate;
	}

	String displayTime(int totalTime){
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
	
	public static void openDataBase(String dbName){
		try {
			Properties props = new Properties(); 
			props.put("user", "user1");
			props.put("password", "user1");	
			
			conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);
			conn.setAutoCommit(false);
			System.out.println("Connected to and created database " + dbName);
			
		}catch (SQLException sqle){
			System.out.println("DataBase Name: " + dbName);
			System.out.println(sqle);
		}  
	}// End of DB Open
	
	public static void createTable(String tableName){
		try {
			// Create a statement object  for running variousSQL statements commands against the database.
			// Create a table using tableName	
			Statement s;					
			s = conn.createStatement();
			s.execute("create table " + tableName +" (seq  int,  summary varchar(25) , duration int, description varchar(60))");
			System.out.println("Created Table " + tableName);
			
		}catch (SQLException sqle){
			// check for table already exists
			if (!sqle.getSQLState().equals("X0Y32")) {
				System.out.println("Create Table - Table Name: " + tableName);
				System.out.println(sqle);
				}	
					else {
					System.out.println("Create Table Name Error:  " + tableName);
				System.out.println(sqle);
			}
		}  
	}// End of create Table
	
	public static void closeAll (){
		//shut down Derb with the shutdown=true attribute 
		try{
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		}
		catch (SQLException se)
		{
			if (( (se.getErrorCode() == 50000)
					&& ("XJ015".equals(se.getSQLState()) ))) {
				// we got the expected exception
				System.out.println("CloseAll: Derby shut down normally");
				} else {
				System.out.println("Derby did not shut down normally");
				 System.out.println(se);
			}
		}finally {
            // release all open resources to avoid unnecessary memory usage
            // ResultSet, Statement, Connection
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
				if (s != null) {
                        s.close();
                        s = null;
                    }
				if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException sqle) {
				System.out.println("Closing of Resources: ");
               System.out.println(sqle);
			}
		} 
	} //end  of closeAll 
	
	//Run Main 
	public static void main (String args[]){
		//Create the frame on the event dispatching thread
			SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					new RunWorkout(inputSession);		
			}
		});
	}
}