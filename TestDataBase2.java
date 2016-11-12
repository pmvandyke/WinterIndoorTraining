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
    private String framework = "embedded";
    private String protocol = "jdbc:derby:";

	public static void main(String[] args) {
        new TestDataBase().go(args);
        System.out.println("SimpleApp finished");
    }
	void go(String[] args)  {
        /* parse the arguments to determine which framework is desired*/
        //parseArguments(args);

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
			// connection properties  providing a user name and password is optional in the embedded database
            Properties props = new Properties(); 
            props.put("user", "user1");
            props.put("password", "user1");
			
			// the name of the database
            String dbName = "MyTestDB"; 
			
			// This connection specifies create=true in the connection URL to
            // cause the database to be created when connecting for the first time.
						
			conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);

            System.out.println("Connected to and created database " + dbName);
			
			// Manually control transactions manually. Autocommit is on by default in JDBC

            conn.setAutoCommit(false);
			
			// Create a statement object  for running variousSQL statements commands against the database.
            s = conn.createStatement();
            statements.add(s);
			
			// Create a table...
            s.execute("create table MainMenu (seq  int, workout varchar(15))");
				
            System.out.println("Created table MainMenu");
			
			//Insert Rows into Table  using PreparedStatement
			// parameter 1 is seq (int), parameter 2 is workout (varchar)
			
			 psInsert = conn.prepareStatement("insert into MainMenu values (?, ?)");
            statements.add(psInsert);
			
			//Row 1
			psInsert.setInt(1, 1);
            psInsert.setString(2, "Winter Workout.");
            psInsert.executeUpdate();
			
           //Row 2
            psInsert.setInt(1, 2);
            psInsert.setString(2, "Spring Workout.");
            psInsert.executeUpdate();
			
			//Row 3
            psInsert.setInt(1, 3);
            psInsert.setString(2, "Summer Workout.");
            psInsert.executeUpdate();
			
			//Row4
            psInsert.setInt(1, 4);
            psInsert.setString(2, "Fall Workout.");
            psInsert.executeUpdate();
			
            System.out.println("Inserted 4 Rows into Table MainMenu");
			
			 //Read Table MainMenu and verify content
             
            rs = s.executeQuery("SELECT seq, workout FROM MainMenu ORDER BY seq");
			while(rs.next()) {
				int sequence  = rs.getInt(1);
				String workout = rs.getString(2);
				System.out.println("Sequence: " + sequence + "Workout: " + workout);
            }
			 // drop the table
            s.execute("drop table MainMenu");
            System.out.println("Dropped table MainMenu");

            //Commit the transaction. Any changes will be persisted to  the database now
            conn.commit();
            System.out.println("Committed the transaction");
			
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
	

