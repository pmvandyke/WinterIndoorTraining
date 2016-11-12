// import java files for sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;

public class TestDataBase {
	
	/* the default framework is embedded */
    static String framework = "embedded";
    static String  protocol = "jdbc:derby:";
	
	//dbName is the name of the database
	
	static String tableName = "mainMenu";
	static Statement s;
	static ResultSet rs = null;
	static Connection conn = null;
	
	// sql strings used in derby methods:
		static String dbName = "MyTestDB"; 
		static String sqlCreate = "create table " + tableName +" (seq  int, workout varchar(15))";
		static String sqlInsert = "insert into " + tableName +"  values (?, ?)";
		static String sqlRead = "SELECT seq, workout FROM " + tableName + " ORDER BY seq";
		static String sqlUpdate = "update " +tableName + " set seq=?, workout=? where seq=?";
		static String sqlDrop = "drop table " + tableName;
		
	public static  void main(String[] args) {
		TestDataBase();		
        System.out.println("TestDataBase  finished");
    }
	public static  void TestDataBase()  {
      

        System.out.println("TestDataBase starting in " + framework + " mode");
		
		/* We will be using Statement and PreparedStatement objects for executing SQL. 
        * These objects, as well as Connections and ResultSets, are resources that should
        *  be released explicitly after use, hence the try-catch-finally pattern used below.   
		*/
		
        PreparedStatement psInsert;
        PreparedStatement psUpdate;
        		
		// connect to DataBase, provide a user name and password is optional in the embedded database
		// create=true in the connection URL to cause the database to be created when connecting for the first time.
		// Manually control transactions manually. Autocommit is on by default in JDBC
	
		
		openDataBase(dbName);
		createTable(sqlCreate);
		insertRows(sqlInsert);
		readTable(sqlRead);
		updateRows(sqlUpdate);
		dropTable (sqlDrop);
		commitUpdates();
		closeAll();
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
	
	public static void createTable(String sqlCreate){
		try {
			// Create a statement object  for running variousSQL statements commands against the database.
			// Create a table using tableName	
			Statement s;					
			s = conn.createStatement();
			s.execute(sqlCreate);
			System.out.println("Created Table " + tableName);
			
		}catch (SQLException sqle){
			System.out.println("Table Name: " + tableName);
			System.out.println(sqle);
		}  
	}// End of create Table
		
	public static void insertRows(String sqlInsert){
		try {
			//Insert Rows into Table  using PreparedStatements
			// parameter 1 is seq (int), parameter 2 is workout (varchar)
			PreparedStatement psInsert = conn.prepareStatement(sqlInsert);	
			
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
			System.out.println("Inserted 4 Rows into Table: " + tableName);;
			
		}catch (SQLException sqle){
			System.out.println("Updated Table: " + tableName);
			System.out.println(sqle);
		}  
	}	// End of updateTable
	
	public static void readTable(String sqlRead){
		//Read Table  and verify content
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sqlRead);
			while(rs.next()) {
				int sequence  = rs.getInt(1);
				String workout = rs.getString(2);
				System.out.println("Sequence: " + sequence + " Workout: " + workout);
			}
			System.out.println("Read Table " + tableName);
		}catch (SQLException sqle){
			System.out.println("Read Table: " + tableName);
			System.out.println(sqle);
		}  
	}// End of Read Table	
	
	public static void updateRows(String sqlUpdate){
		try {
			PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
			psUpdate.setInt(1, 1);
			psUpdate.setString(2, "Winter Workout.");
			System.out.println("Update Table: " + tableName);
		}catch (SQLException sqle){
			System.out.println("Update Table: " + tableName);
			System.out.println(sqle);
		}  
	}//End of updateRow
		
	public static void dropTable(String sqlDrop){
		try {
			 // drop the table	

			Statement s = conn.createStatement();
			s.execute(sqlDrop);
			
			System.out.println("Dropped Table " + tableName);
		}catch (SQLException sqle){
			System.out.println("Table Name: " + tableName);
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