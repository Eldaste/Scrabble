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

