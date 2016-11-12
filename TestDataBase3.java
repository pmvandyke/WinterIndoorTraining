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
	static String dbName = "MyTestDB"; 
	static String tableName = "mainMenu";
	
	public static void main(String[] args) {
			TestDataBase();		
        System.out.println("TestDataBase  finished");
    }
	public static void TestDataBase()  {
      

        System.out.println("TestDataBase starting in " + framework + " mode");
		
		/* We will be using Statement and PreparedStatement objects for executing SQL. 
        * These objects, as well as Connections and ResultSets, are resources that should
        *  be released explicitly after use, hence the try-catch-finally pattern used below.
        * We are storing the Statement and Prepared statement object references
        * in an array list for convenience.    
		*/
		Connection conn = null;
        ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
        PreparedStatement psInsert;
        PreparedStatement psUpdate;
        Statement s;
        ResultSet rs = null;
		
		try {
		// connect to DataBase, provide a user name and password is optional in the embedded database
		// create=true in the connection URL to cause the database to be created when connecting for the first time.
		// Manually control transactions manually. Autocommit is on by default in JDBC
		
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
            s.execute("create table " + tableName +" (seq  int, workout varchar(15))");
			//s.execute("create table MainMenu (seq  int, workout varchar(15))");
			
System.out.println("Created table: " +tableName);
			
			//Insert Rows into Table  using PreparedStatement
			// parameter 1 is seq (int), parameter 2 is workout (varchar)
			
			 psInsert = conn.prepareStatement("insert into " + tableName +"  values (?, ?)");
            statements.add(psInsert);
			
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
			
System.out.println("Inserted 4 Rows into Table: " + tableName);
			
			 //Read Table  and verify content
             
            rs = s.executeQuery("SELECT seq, workout FROM " + tableName + " ORDER BY seq");
			while(rs.next()) {
				int sequence  = rs.getInt(1);
				String workout = rs.getString(2);
				System.out.println("Sequence: " + sequence + " Workout: " + workout);
            }
			 // drop the table
            s.execute("drop table " + tableName);
            System.out.println("Dropped table MainMenu");

            //Commit the transaction. Any changes will be persisted to  the database now
            conn.commit();
            System.out.println("Committed the transactions");
			
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
        }
		catch (SQLException sqle)
        {
            System.out.println(sqle);
        } finally {
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
	
}