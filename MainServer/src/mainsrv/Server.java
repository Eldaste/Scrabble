package mainsrv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

import Threads.Worker;

public class Server {
	public final int PORT=8080;
	
	Connection c = null;
    Statement stmt = null;
    ServerSocket s=null;

    public static int o=0;
    
	public static void main(String[] args) {
		try {
			new Server();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Server() throws ClassNotFoundException, SQLException, IOException{
			
		    Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:Srs.db");
		    System.out.println("Opened database successfully");
		    
		    s=new ServerSocket(PORT);
		    System.out.println("Opened server successfully");
		    
		    stmt = c.createStatement();
		    
		    while(true){
		    	o=1;
		    	Socket newcon=s.accept();
			
		    	System.out.println("New connection from: "+newcon.getInetAddress().getHostAddress());
		    	Worker w=new Worker(newcon,this);
		    	w.start();
			}
		    
		    //String sql = ""; 
		    //stmt.executeUpdate(sql);
		  }
	
	public boolean validate(int[] test) throws SQLException{
		String comm="SELECT COUNT(Username) FROM Users WHERE Username=";
		int i=0;
		StringBuffer sb=new StringBuffer();
		
		while(test[i]!=0){
			sb.append((char)test[i]);
			i++;
		}
		
		i++;
		
		comm+=sb.toString()+" AND AuthToken=";
		
		sb=new StringBuffer();
		
		for(;i<test.length;i++){
			if(test[i]==0)
				break;
			sb.append((char)test[i]);
		}
		
		comm+=sb.toString()+";";
		
		ResultSet rs=stmt.executeQuery(comm);
		boolean res=0==rs.getInt(1);
		
		rs.close();
		
		return (res);
	}

	public void terminate(){
		
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    System.out.println("Toodles");
	    System.exit(0);
	}
}


