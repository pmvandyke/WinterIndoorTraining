import java.io.*;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;

class UpdateWorkoutDetails{
	
// database variables the default framework is embedded, dbName is the name of the database 
	static String framework = "embedded";
	static String  protocol = "jdbc:derby:";
	static String dbName = "MyTestDB"; 
			
	public static void main (String args[]){
		UpdateWorkoutMenu();
		System.out.println ("Workout Detail Complete:  ");	
	}
	
	public static void UpdateWorkDetails(){
		// file variables:	
		String delim =  "#";		
		String inputWorkout="Session 1.0 Fundementals";
		String inputFileName;
		String workoutName;
		String seasonName;
		String newSeasonName;
		String tipText;
		inputFileName = inputWorkout + ".txt";
		
		///DB Variables
	
		String tableName = inputWorkout.substring (0,11);
		tableName = tableName.replace(".","");
		tableName = tablename.replace (" ","")'
		System.out.println("Table Name: " + tableName);
		Connection conn = null;
		ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
		PreparedStatement psInsert;
		PreparedStatement psUpdate;
		Statement s;
		ResultSet rs = null;
		
		// connect to DataBase, provide a user name and password is optional in the embedded database
		// create=true in the connection URL to cause the database to be created when connecting for the first time.
		// Manually control transactions manually. Autocommit is on by default in JDBC
		try {
				
            Properties props = new Properties(); 
            props.put("user", "user1");
            props.put("password", "user1");						
			conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);
			conn.setAutoCommit(false);
			 
			System.out.println("Connected to and created database " + dbName);

			// Create a statement object  for running variousSQL statements commands against the database.
			// Create a table using tableName			
            s = conn.createStatement();
            statements.add(s);		
            s.execute("create table " + tableName +" (seq  int, workout varchar(40))");
			
			System.out.println("Created table: " +tableName);		
		}
		catch (SQLException sqle){
			System.out.println("Table Name: " + tableName);
            System.out.println(sqle);
		}  // End of DB set up
		String seqString;
		//Read the input file
		int seqNbr =0;
		newSeasonName = null;
		try {
			BufferedReader in = new BufferedReader (new FileReader (new File (inputFileName)));	
			for (String inputString = in.readLine(); inputString != null; inputString = in.readLine()){
				//System.out.println ("Input : " + inputString);
				String parts [] = inputString.split(delim);
				workoutName = parts [0];
				tipText = "No Details";
				if (parts.length > 1 ) tipText = parts [1];
				//System.out.println ( " SeqNbr: " + seqNbr + " Workout Name:  " +workoutName);
				//Insert DataBase Row 
				try{
					// set up statement to add recods
					psInsert = conn.prepareStatement("insert into " + tableName +"  values (?, ?)");
					statements.add(psInsert);
					psInsert.setInt(1, seqNbr);				
					psInsert.setString(2, workoutName);
					psInsert.executeUpdate();
				}catch (SQLException sqle){
					System.out.println(sqle);
				}
				seqNbr++;
			}
		} catch (IOException e){
			System.out.println ("File I/O Error! " + e);
			System.exit(1);
		}
		System.out.println ("Ready to read table entries");
		//Read Table  and verify content
        try{
			 s = conn.createStatement();
            statements.add(s);
			rs = s.executeQuery("SELECT  seq, workout FROM " +tableName + " ORDER BY  seq");
			while(rs.next()) {
				int sequence  = rs.getInt(1);
				String workout = rs.getString(2);
				System.out.println(" Sequence: " + sequence + " Workout: " + workout);
            }
			//******************************************************************************************
		 //Commit the transaction. Any changes will be persisted to  the database now
				String sqlDrop = "drop table " + tableName;
				//Statement s =conn.createStatement();
			//s.execute(sqlDrop);
            conn.commit();
            System.out.println("Committed the transactions");
			
		}  catch (SQLException sqle){
            System.out.println(sqle);
		}  // End of DB Table Read
            
		// shut down Derb with the shutdown=true attribute 
		if (framework.equals("embedded")) {
			try
			{
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			}
			catch (SQLException se)
			{
				if (( (se.getErrorCode() == 50000)
						&& ("XJ015".equals(se.getSQLState()) ))) {
					// we got the expected exception
					System.out.println("Derby shut down normally");
					} else {
					System.err.println("Derby did not shut down normally");
					 System.out.println(se);
				}
			}
		}
  		
	   // close ResultSet
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}

		// close Statements and PreparedStatements
		int i = 0;
		while (!statements.isEmpty()) {
			// PreparedStatement extend Statement
			Statement st = (Statement)statements.remove(i);
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (SQLException sqle) {
				System.out.println(sqle);
			}
		}

		//close Connection
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException sqle) {
		   System.out.println(sqle);
		}
	}	
}