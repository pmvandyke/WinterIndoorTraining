import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
//import java.io.*;
//import java.io.IOException;

// import java files for sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;

//* This application creates a Frame which desplays the Training Menu which has been received.
//* The menu is created from items read from a DataBasee contained in databaseName.
//* The menu options are displayed as Radio Buttons.
//* An ActionListener receives the selection and passes the button name to a Confirm Check Box.
//* The Confirm Check Box allows the user to verify the selection.
//* And then transfer control to an application which displays the selected Workout.
//* Application passes the Radio Buttion Selection

 class DisplayWorkoutMenu extends JFrame implements ActionListener{
	 
	/* the default framework is embedded */
    static String framework = "embedded";
    static String  protocol = "jdbc:derby:";
	
	//dbName is the name of the database
	static String dbName = "MyTestDB"; 
	static String tableName "TestWorkout";

	static Statement s;
	static ResultSet rs = null;
	static Connection conn = null;
	PreparedStatement psInsert;
    PreparedStatement psUpdate;
	static int sequence;
	static String workout;
	String buttonName;
	String message = "";
	String tipText;
	String actionCommand;
	static String currentWorkout;
	static String inputWorkout;
	//String workoutFileName;
		
	JFrame jfrm = new JFrame (" ");
	JLabel jlabConfirmSession;
	JLabel jlabChooseWorkout = new JLabel("Select a Workout Session");	
	JCheckBox jcbConfirm = new JCheckBox("Confirm Selection");
	
	public static void main (String args[]){ 
		//Create the frame on the event dispatching thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new RadioButtonExample(inputWorkout);
			}
		});
	}
	public DisplayWorkoutMenu(String inputWorkout) {
		//System.out.println("inputWorkout: " + inputWorkout);
		createFrame(inputWorkout);
		//remove middle space
		String Str = new String (inputWorkout );
		tableName = Str.replace(" ","");
		//System.out.println("1 Table Name: " + tableName); 
		openDataBase();
		//createTable();
		//insertRows();
		readButtonTable();
		//updateRows();
		//dropTable ();
		//commitUpdates();
		closeAll();
	}	
	public void  readButtonTable(){
		//add the radio buttons to a group from input file
		ButtonGroup group = new ButtonGroup ();
		try {
			Statement s = conn.createStatement();
			String sql = "SELECT seq, workout FROM " + tableName +  " ORDER BY seq" ;
			ResultSet rs = s.executeQuery(sql);	
			while(rs.next()) {
				sequence  = rs.getInt(1);
				workout = rs.getString(2);
				//System.out.println("Sequence: " + sequence + " Workout: " + workout);
				buttonName = workout;
				tipText = "No Details";
				JRadioButton button = new JRadioButton (buttonName);
				button.setActionCommand(buttonName);
				button.setToolTipText(tipText);
				button.addActionListener(this);
				jfrm.add(button);
				group.add(button);
			}
			System.out.println("Read Table " + tableName);
		}catch (SQLException sqle){
			System.out.println("Read Button Table: " + tableName);
			System.out.println(sqle);
		}  
		// Action listener to jcbConfirm
		jcbConfirm.addItemListener (new ItemListener (){
			public void itemStateChanged(ItemEvent ie){
				if (jcbConfirm.isSelected()){
					if (message == ""){
						jcbConfirm.setSelected(false);
						return;
					}
					displayNextWorkout();
					//System.exit(0);	
				}
			}		
		});	
			createCheckBox();	
	}
		
	// display the next workout routine
	public void displayNextWorkout(){
		System.out.println("Next Workout: " + actionCommand );
		// Added this to launch the workout Frame
		SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					//new RunWorkout(actionCommand.toString()+".txt");		
					new RunWorkout(actionCommand);
			}
		});
	}
	//Listen for Action Events 
	public void actionPerformed(ActionEvent ae){
		String cmd = ae.getActionCommand();
		actionCommand = cmd;
		message = "Confirm Selection: " + cmd ;
		jcbConfirm.setText(message);
	}
	
	public void createFrame (String inputWorkout) {
		jfrm.setTitle(inputWorkout + " Main Menu");
		// Specify a 1 col by 14 rows grid layout
		jfrm.setLayout(new GridLayout(14,1));
		//Give frame an initial size: width and height
		jfrm.setSize (400,300);
		// Terminate prrogram when user closes the application (window)
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jlabChooseWorkout.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jfrm.add (jlabChooseWorkout);
		jfrm.setVisible(true);
		jfrm.setLocation (50, 350); //in, down
	}
	public void createCheckBox(){
		jcbConfirm.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jfrm.add (jcbConfirm);
	}
	public static void openDataBase(){
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
	
	public static void createTable(){
		try {
			// Create a statement object  for running variousSQL statements commands against the database.
			// Create a table using tableName	
			Statement s;					
			s = conn.createStatement();
			String sql = "create table " + tableName +" (seq int, workout varchar(40))";
			s.execute(sql);
			System.out.println("Created Table " + tableName);
			
		}catch (SQLException sqle){
			// check for table already exists
			if (!sqle.getSQLState().equals("X0Y32")) {
				System.out.println("Create Table - Table Name: " + tableName);
				System.out.println(sqle);
			} 	
		}  
	}// End of create Table
		
	public static void insertRows(){
		try {
			//Insert Rows into Table  using PreparedStatements
			// parameter 1 is seq (int), parameter 2 is workout (varchar)

			String psI = "insert into " + tableName +"  values (?, ?)";
			PreparedStatement psInsert = conn.prepareStatement(psI);	
			
			//Row 1
			psInsert.setInt(1, 1);
			psInsert.setString(2, "Winter Workout");
			psInsert.executeUpdate();

			//Row 2
			psInsert.setInt(1, 2);
			psInsert.setString(2, "Spring Workout");
			psInsert.executeUpdate();

			//Row 3
			psInsert.setInt(1, 3);
			psInsert.setString(2, "Summer Workout");
			psInsert.executeUpdate();

			//Row4
			psInsert.setInt(1, 4);
			psInsert.setString(2, "Fall Workout");
			psInsert.executeUpdate();
			
			psInsert.close();
			System.out.println("Inserted 4 Rows into Table: " + tableName);
			
		}catch (SQLException sqle){
			System.out.println("Updated Table: " + tableName);
			System.out.println(sqle);
		}  
	}	// End of updateTable
	
	public static void readTable(){
		//Read Table  and verify content
		try {
			Statement s = conn.createStatement();
			String sql = "SELECT seq, workout FROM " + tableName + " ORDER BY seq";
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				int sequence  = rs.getInt(1);
				String workout = rs.getString(2);
				System.out.println("Sequence: " + sequence + " Workout: " + workout);
			}
			System.out.println("Read Table " + tableName);
		}catch (SQLException sqle){
			System.out.println("readTable: " + tableName);
			System.out.println(sqle);
		}  
	}// End of Read Table	
	
	public static void updateRows(){
		try {
			String sql = "update " +tableName + " set seq=?, workout=? where seq=?";
			PreparedStatement psUpdate = conn.prepareStatement(sql);
			psUpdate.setInt(1, 1);
			psUpdate.setString(2, "Winter Workout.");
						
			psUpdate.close();
			System.out.println("Update Table: " + tableName);
		}catch (SQLException sqle){
			System.out.println("Update Table: " + tableName);
			System.out.println(sqle);
		}  
	}//End of updateRow
		
	public static void dropTable(){
		try {
			 // drop the table	
			String sql = "drop table " + tableName;
			Statement s = conn.createStatement();
			s.execute(sql);
			
			System.out.println("Dropped Table " + tableName);
		}catch (SQLException sqle){
			System.out.println("dropTable Table Name: " + tableName);
			System.out.println(sqle);
		}  
	}// End of dropTable	
	
	public static  void commitUpdates(){
		try {
			// commit the updates previously made,Any changes will be persisted to  the database now.	
			conn.commit();
			
			System.out.println("Commit updates in Table " + tableName);
		}catch (SQLException sqle){
			System.out.println("Commit updates in Table " + tableName);;
			System.out.println(sqle);
		}  
	}// End of commitUpdatese	
		

	public static void closeAll(){
		//shut down Derb with the shutdown=true attribute 
		try{
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		}
		catch (SQLException se)
		{
			if (( (se.getErrorCode() == 50000)
					&& ("XJ015".equals(se.getSQLState()) ))) {
				// we got the expected exception
				System.out.println("Derby shut down normally");
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
}