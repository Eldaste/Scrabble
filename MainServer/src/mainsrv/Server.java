// (c) Milo Wimmer 2016, All Rights Reserved

package mainsrv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import Threads.Worker;

public class Server {
	public static final int PORT=8080;
	public static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static final int DEFAULTAUTHLENGTH=20;
	public static final String GAMEDB="Srs.db";
	public static final boolean AUTOINCPOINTS=true;
	
	static SecureRandom rnd = new SecureRandom();
	
	Connection c = null;
    Statement stmt = null;
    ServerSocket s=null;

    public static int o=0;
    
	public static void main(String[] args) {
		try {
			new Server();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public Server() throws ClassNotFoundException, SQLException, IOException{
			
		    Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:"+GAMEDB);
		    c.setAutoCommit(false);
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
		String comm="SELECT COUNT(Username) FROM Users WHERE Username='";
		int i=0;
		StringBuffer sb=new StringBuffer();
		
		while(test[i]!=0){
			sb.append((char)test[i]);
			i++;
		}
		
		i++;
		
		comm+=sb.toString()+"' AND AuthToken='";
		
		sb=new StringBuffer();
		
		for(;i<test.length;i++){
			if(test[i]==0)
				break;
			sb.append((char)test[i]);
		}
		
		comm+=sb.toString()+"';";
		
		ResultSet rs=stmt.executeQuery(comm);
		boolean res=(0==rs.getInt(1));
		
		rs.close();
		
		return (!res);
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

	public boolean userExists(int[] test) throws SQLException {
		String comm="SELECT COUNT(Username) FROM Users WHERE Username='"+extractUsername(test)+"';";
		
		ResultSet rs=stmt.executeQuery(comm);
		boolean res=(0==rs.getInt(1));
		
		rs.close();
		
		return (!res);
	}

	public int[] createUser(int[] msg) throws SQLException {
		int[] auth=new int[DEFAULTAUTHLENGTH];
		String token=randomString(DEFAULTAUTHLENGTH);
		
		for(int i=0; i<DEFAULTAUTHLENGTH;i++){
			auth[i]=token.charAt(i);
		}
		
		System.out.println("Making User: "+extractUsername(msg));

		String comm="INSERT INTO Users (Username,AuthToken) VALUES('"+extractUsername(msg)+"','"+token+"');";
		
		stmt.executeUpdate(comm);
		c.commit();
		
		return auth;
	}
	
	public String randomString( int len ){
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
		}
	
	public int[] alterPass(int[] msg) throws SQLException {
		int[] trimmsg=trimUA(msg);
		
		if(trimmsg.length==0)
			return new int[]{0xFF};
		
		StringBuffer sb=new StringBuffer();
		
		int i=0;
		for(;i<msg.length&&msg[i]!=0;i++);
		i++;
		for(;i<msg.length;i++){
			if(msg[i]==0)
				break;
			sb.append((char)msg[i]);
		}
		
		String comm="UPDATE Users SET AuthToken='"+sb.toString()+"' WHERE Username='"+extractUsername(msg)+"';";
		
		stmt.executeUpdate(comm);
		c.commit();
		
		return new int[]{0x00};
	}
	
	public int[] trimUA(int[] msg) {
		int i=0;
		for(;i<msg.length&&msg[i]!=0;i++);
		i++;
		for(;i<msg.length&&msg[i]!=0;i++);//set i=2nd instance of 0 in the arr
		i++;
		
		int[] fin;
		if(i>=msg.length)
			fin=new int[0];
		else
			fin=new int[msg.length-i];
		
		for(int p=0;i<msg.length;i++,p++){
			fin[p]=msg[i];
		}
		
		return fin;
	}
	
	public int[] trimSingle(int[] msg) {
		int i=0;
		for(;i<msg.length&&msg[i]!=0;i++);
		i++;
		
		int[] fin;
		if(i>=msg.length)
			fin=new int[0];
		else
			fin=new int[msg.length-i];
		
		for(int p=0;i<msg.length;i++,p++){
			fin[p]=msg[i];
		}
		
		return fin;
	}

	public String extractUsername(int[] test){
		StringBuffer sb=new StringBuffer();
		
		for(int i=0;i<test.length;i++){
			if(test[i]==0)
				break;
			sb.append((char)test[i]);
		}
		
		return sb.toString();
	}

	public int[] gameList(int[] msg) throws SQLException {
		String comm="SELECT g.Name,g.GID FROM Game g,InGame i WHERE i.GID=g.GID AND i.Username='"
				+extractUsername(msg)+"';";
		ResultSet rs = stmt.executeQuery( comm );
		int totallen=0;
		ArrayList<int[]> taco=new ArrayList<int[]>();
		
	    while ( rs.next() ) {
	    	String st=rs.getString("Name");
	    	String nu=Integer.toString(rs.getInt("GID"));

	    	int[] curr=new int[st.length()+nu.length()+1];
	    	totallen+=curr.length+1;
	    	
	    	int i=0;
	    	for(;i<st.length();i++){
	    		curr[i]=st.charAt(i);
	    	}
	    	
	    	curr[i]=0;
	    	i++;
	    	
	    	for(int j=0;j<nu.length();j++,i++){
	    		curr[i]=nu.charAt(j);
	    	}
	    	
	    	taco.add(curr);
	    }
	    rs.close();
	    
	    int[] fin=new int[totallen+1];
	    fin[0]=taco.size();
	    int tt=1;
	    
	    for(int[] flarb:taco){
	    	fin[tt]=flarb.length;
	    	tt++;
	    	
	    	for(int i=0;i<flarb.length;i++,tt++){
	    		fin[tt]=flarb[i];
	    	}
	    }
		
		return fin;
	}

	public int[] gameSeekList(int[] msg, boolean allGames) throws SQLException {
		String comm="SELECT g.Name,g.GSID FROM GameSeek g";
		if(!allGames)
				comm+=",Seeking i WHERE i.GSID=g.GSID AND i.Username='"+extractUsername(msg)+"';";
		ResultSet rs = stmt.executeQuery( comm );
		int totallen=0;
		ArrayList<int[]> taco=new ArrayList<int[]>();
		
	    while ( rs.next() ) {
	    	String st=rs.getString("Name");
	    	String nu=Integer.toString(rs.getInt("GSID"));

	    	int[] curr=new int[st.length()+nu.length()+1];
	    	totallen+=curr.length+1;
	    	
	    	int i=0;
	    	for(;i<st.length();i++){
	    		curr[i]=st.charAt(i);
	    	}
	    	
	    	curr[i]=0;
	    	i++;
	    	
	    	for(int j=0;j<nu.length();j++,i++){
	    		curr[i]=nu.charAt(j);
	    	}
	    	
	    	taco.add(curr);
	    }
	    rs.close();
	    
	    int[] fin=new int[totallen+1];
	    fin[0]=taco.size();
	    int tt=1;
	    
	    for(int[] flarb:taco){
	    	fin[tt]=flarb.length;
	    	tt++;
	    	
	    	for(int i=0;i<flarb.length;i++,tt++){
	    		fin[tt]=flarb[i];
	    	}
	    }
		
		return fin;
	}

	public int[] getBoard(int[] msg) throws SQLException {
		String comm="SELECT t.Boardstate FROM Turn t WHERE t.GID="+new String(extractUsername(trimUA(msg)))
				+" AND NOT EXISTS(SELECT * FROM Turn b WHERE b.TurnNum>a.TurnNum);";
		
		ResultSet rs = stmt.executeQuery( comm );
		
		String bs=rs.getString("Boardstate");
		
		rs.close();
		
		return byteToInt(bs.getBytes());
	}

	public int[] byteToInt(byte[] bytes) {
		int[] res=new int[bytes.length];
		
		for(int i=0;i<res.length;i++){
			res[i]=bytes[i];
		}
		
		return res;
	}

	public int[] getPlayers(int[] msg) throws SQLException {
		String comm="SELECT i.Username FROM InGame i WHERE i.GID="+extractUsername(trimUA(msg))
				+" ORDER BY TurnNum;";
		
		ResultSet rs = stmt.executeQuery( comm );
		int totallen=0;
		ArrayList<int[]> taco=new ArrayList<int[]>();
		
	    while ( rs.next() ) {
	    	String tem=rs.getString("Username");
	    	totallen+=tem.length()+1;
	    	taco.add(byteToInt(tem.getBytes()));
	    }
	    
	    rs.close();
	    
	    int[] fin=new int[totallen+1];
	    fin[0]=taco.size();
	    int tt=1;
	    
	    for(int[] flarb:taco){
	    	for(int i=0;i<flarb.length;i++,tt++){
	    		fin[tt]=flarb[i];
	    	}
	    	fin[tt]=0;
	    	tt++;
	    }
		
		return fin;
	}

	public int[] newTile(int[] msg) throws SQLException {
		int[] heldTiles=getTiles(msg);
		String game=extractUsername(trimUA(msg));
		String newTil=extractUsername(heldTiles);
		
		String comm="SELECT i.AvalibleTiles FROM Game i WHERE GID="+game+";";		
		ResultSet rs = stmt.executeQuery( comm );		
		int[] res=byteToInt((rs.getString("AvalibleTiles").getBytes()));
		rs.close();
		
		int t=rnd.nextInt(res.length);
		newTil+=res[t];
		
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<res.length;i++){
			if(i==t)
				continue;
			sb.append(res[i]);
		}
		
		comm="UPDATE Game SET AvalibleTiles='"+sb.toString()+"' WHERE GID="+game+";";
		stmt.executeUpdate(comm);
		
		comm="UPDATE InGame SET Tiles='"+newTil+"' WHERE GID="+game+
				"AND Username='"+extractUsername(msg)+"';";
		stmt.executeUpdate(comm);
		
		c.commit();
				
		return new int[]{res[t]};
	}

	public int[] getTiles(int[] msg) throws SQLException {
		String user=extractUsername(msg);
		String game=extractUsername(trimUA(msg));
		String comm="SELECT i.Tiles FROM InGame i WHERE Username='"+user+"' AND GID="+game+";";
		
		ResultSet rs = stmt.executeQuery( comm );
		
		String res=rs.getString("Tiles");
		
		rs.close();
		
		return byteToInt(res.getBytes());
	}

	public int[] returnTile(int[] msg) throws SQLException {
		int[] heldTiles=getTiles(msg);
		int[] toRem=trimSingle(trimUA(msg));
		int[] newTiles=new int[heldTiles.length-toRem.length];
		
		loopi:for(int i=0;toRem.length>i;i++){
			for(int k=0;k< heldTiles.length;k++){
				if(toRem[i]==heldTiles[k]){
					toRem[i]=0;
					heldTiles[k]=0;
					continue loopi;
				}
			}
			
			return new int[]{0xFF};
		}
		
		for(int i=0,k=0;i<heldTiles.length;i++){
			if(heldTiles[i]!=0){
				newTiles[k]=heldTiles[i];
				k++;
			}
		}
		
		String user=extractUsername(msg);
		String game=extractUsername(trimUA(msg));
		
		String comm="UPDATE InGame SET Tiles='"+extractUsername(newTiles)+
				"' WHERE Username='"+user+"' AND GID="+game+";";
		
		stmt.executeUpdate(comm);
		c.commit();
		
		return new int[]{0x00};
	}

	
	public int[] makeNewGame(int[] msg) throws SQLException {
		int [] msgc=trimUA(msg);
		String numplay=extractUsername(msgc);
		String gname=extractUsername(trimSingle(msgc));
		
		String comm="INSERT INTO GameSeek SELECT a.GSID+1, "+numplay+", '"+gname+"' FROM GameSeek a "
				+ "WHERE NOT EXISTS(SELECT * FROM GameSeek b WHERE b.GSID>a.GSID);";
		
		stmt.executeUpdate(comm);
		
		comm="INSERT INTO Seeking SELECT a.GSID, '"+extractUsername(msg)+"' FROM GameSeek a "
				+ "WHERE NOT EXISTS(SELECT * FROM GameSeek b WHERE b.GSID>a.GSID);";
		
		stmt.executeUpdate(comm);
		
		c.commit();
		
		comm="SELECT a.GSID FROM GameSeek a WHERE NOT EXISTS(SELECT * FROM GameSeek b WHERE b.GSID>a.GSID);";
		
		ResultSet rs = stmt.executeQuery( comm );
		
		String res=Integer.toString(rs.getInt("GSID"));
		
		rs.close();

		return byteToInt(res.getBytes());
	}

	public int[] joinGame(int[] msg) throws SQLException {
		String comm="INSERT INTO Seeking VALUES ("+extractUsername(trimUA(msg))+", '"+extractUsername(msg)+"');";
		
		stmt.executeUpdate(comm);
		c.commit();
		
		return new int[]{0};
	}

	public int[] getPlayersInSeek(int[] msg) throws SQLException {
		String comm="SELECT COUNT(a.Username) AS numPlayers, b.Players FROM GameSeek b,Seeking a"
				+ " WHERE b.GSID="+extractUsername(trimUA(msg))+" AND b.GSID=a.GSID";
		
		ResultSet rs = stmt.executeQuery( comm );
		
		String res=Integer.toString(rs.getInt("numPlayers"));
		String res2=Integer.toString(rs.getInt("Players"));
		
		rs.close();
		
		int[] fin=new int[res.length()+res2.length()+1];
		int i=0;
		
		for(int j=0;j<res.length();j++,i++){
			fin[i]=res.charAt(j);
		}
		
		fin[i]=0;
		i++;
		
		for(int j=0;j<res.length();j++,i++){
			fin[i]=res2.charAt(j);
		}
		
		return fin;
	}

	
	public int[] makeMove(int[] msg,boolean autoRemoveTiles) throws SQLException {
		int[] tmp=trimUA(msg);
		String gNum=extractUsername(tmp);
		tmp=trimSingle(tmp);
		String Pts=extractUsername(tmp);
		tmp=trimSingle(tmp);
		String tilesUsd=null;
		if(autoRemoveTiles){
			tilesUsd=extractUsername(tmp);
			tmp=trimSingle(tmp);
		}
		String word=extractUsername(tmp);
		tmp=trimSingle(tmp);
		String state=extractUsername(tmp);
		
		if(autoRemoveTiles){
			String uname=extractUsername(msg);
			int[] kk=new int[tilesUsd.length()+2+uname.length()];
			
			int i=0;			
			for(int j=0;j<uname.length();j++,i++)
				kk[i]=uname.charAt(j);
			
			kk[i]=0;
			i++;
			kk[i]=0;
			i++;
			
			for(int j=0;j<tilesUsd.length();j++,i++)
				kk[i]=tilesUsd.charAt(j);
			
			returnTile(kk);
		}
		
		String comm;
		
		if(AUTOINCPOINTS){
			comm="SELECT Score FROM InGame WHERE Username='"+extractUsername(msg)+"' AND GID="+ gNum +";";
			ResultSet rs = stmt.executeQuery( comm );
			
			int res=rs.getInt("Score");
			
			rs.close();
			
			res+=Integer.parseInt(Pts);
			
			comm="UPDATE InGame SET Score="+Integer.toString(res)+" "
					+ "WHERE Username='"+extractUsername(msg)+"' AND GID="+ gNum +";";
			
			stmt.executeUpdate(comm);
		}
		
		comm="INSERT INTO Turn SELECT "+gNum+", a.TurnNum+1 , "+Pts+", '"+word+"', '"+state+"',"
				+ " Player='"+extractUsername(msg)+"'"
				+ " FROM Turn a WHERE NOT EXISTS(SELECT * FROM Turn b WHERE b.TurnNum >a.TurnNum );";
		
		stmt.executeUpdate(comm);
		c.commit();

		return new int[]{0};
	}

	public int[] getCurrPlayer(int[] msg) throws SQLException {
		String gNum=extractUsername(trimUA(msg));
		
		String comm="SELECT a.TOrder FROM InGame a, Turn c "
				+ "WHERE NOT EXISTS(SELECT * FROM Turn d WHERE d.TurnNum >c.TurnNum ) AND c.Username=a.Username "
				+ "AND c.GID="+gNum+" AND c.GID=a.GID;";
		
		int ans,nump=0;
		
		ResultSet rs;
		
		try {
			rs = stmt.executeQuery( comm );
			
			ans=rs.getInt("TOrder");
			
			rs.close();
		} catch (SQLException e) {
			//If no players have taken a turn			
			ans=0;
		}			
		
		comm="SELECT i.Username FROM InGame i WHERE i.GID="+gNum+" ORDER BY TurnNum;";
		
		rs = stmt.executeQuery( comm );

		ArrayList<String> taco=new ArrayList<String>();
		
	    while ( rs.next() ) {
	    	taco.add(rs.getString("Username"));
	    	nump++;
	    }
	    
	    rs.close();
	    
	    if(nump==0)
	    	return new int[]{0xFF};
	    
		ans+=1;
		ans%=nump;
		if(ans!=0)
			ans--;	
		
		return byteToInt(taco.get(ans).getBytes());
	}

	public int[] getPoints(int[] msg) throws SQLException {
		int[] tmp=trimUA(msg);
		String gNum=extractUsername(tmp);
		tmp=trimSingle(tmp);
		String user=extractUsername(tmp);
		
		String comm="SELECT Score FROM InGame WHERE GID="+gNum+" AND Username='"+user+"';";
		
		ResultSet rs = stmt.executeQuery( comm );
		
		String ans=Integer.toString(rs.getInt("Score"));
		
		rs.close();
		
		return byteToInt(ans.getBytes());
	}

}


