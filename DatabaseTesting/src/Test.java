import java.sql.*;

public class Test {

	public static void main( String args[] )
	  {
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      /*String sql = "CREATE TABLE Users " +
	                   "(Username Text PRIMARY KEY," +
	                   " AuthToken      TEXT    NOT NULL, " + 
	                   " Score          INT)"; 
	      stmt.executeUpdate(sql);*/
	      
	      ResultSet rs = stmt.executeQuery( "SELECT Count(Username) FROM Users;" );
	      while ( rs.next() ) {
	         System.out.println( "SALARY = " + rs.getInt(1) );
	         System.out.println();
	      }
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Table created successfully");
	  }

}
