package player;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import gameState.GameState;


public class Player {

	/*
	 * Class Variables
	 * 
	 */
	private String myName;
	private int myScore;
	private int myNumOfTiles;
	private String myAuthToken;
	
	OutputStream out;
    InputStream in;
    
    final String authFile = "auth.txt";
	final int createAuth = 0x01;
	final int getTile = 0x03;
	
	public Player(String name) throws IOException, GenericUsernameError
	{
		//connect to server port 8080
		connect();
		
		//intialize
		myName = name;
		
		//write auth code to output stream
		out.write(createAuth);
		
		//convert name to int array
		int length = name.length();
		
		int [] tmpName = new int[length+4];
		
		for(int i = 0; i < length; i++)
		{
			tmpName[i] = (int)name.charAt(i);
		}

		//send int array
		sendMsg(tmpName,out);
		
		in.read();
	
		int[] authToken = recoverMsg(in);
		
		if(authToken[0] == 0xFF)
		{
			throw new GenericUsernameError();
		}
		
		byte [] byteAuthToken = new byte[authToken.length];
		
		for(int i = 0; i < authToken.length; i++)
		{
			byteAuthToken[i] = (byte)authToken[i];
		}

		//makes the init file
		File initalization = new  File(authFile);
		
		FileOutputStream fw = new FileOutputStream(initalization);
		
		fw.write(byteAuthToken);
		fw.close();
		
		myScore = 0;
		
		myNumOfTiles = 0;
		
	}

	public int getNumofTiles()
	{
		return 0;
	}
	
	public void setNumofTiles()
	{
		
	}
	
	public int getScore()
	{
		return 0;
	}

	public void setScore()
	{
		
	}

	
	public void connect()
	{
		try {
			Socket s = new Socket("127.0.0.1",8080);
		
			out = s.getOutputStream();
			in = s.getInputStream();

			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	 * From Milo's Worker Thread
	 */
	public int[] recoverMsg(InputStream i) throws IOException{
		int msglen=0;
		
		ll:while(true){
			int b=i.read();
			
			if(b==0){
				msglen+=255;
				continue ll;
			}
			
			msglen+=b;
			break ll;
		}
		
		int [] re=new  int[msglen];
		
		for(int ii=0;ii<msglen;ii++){
			re[ii]=i.read();
		}
		
		return re;
	}

	
	public void sendMsg(int[] outgoing,OutputStream o) throws IOException{
		int kk=outgoing.length;
		
		for(;kk>255;kk-=255){
			o.write(0x00);
		}
		
		o.write(kk);
		
		for(int ii=0;ii<outgoing.length;ii++){
			o.write(outgoing[ii]);
		}
	}
	
	public int[] readFileToInt() throws IOException
	{
		ArrayList<Character> authList = new ArrayList<Character>();
		FileReader fr = new FileReader(authFile);
		BufferedReader br = new BufferedReader(fr);
		String auth = br.readLine();
		
		//auth should be non-null
		char[] tmp = auth.toCharArray();
		int[] intAuth = new int[tmp.length];
		
		for(int i = 0; i < tmp.length; i++)
		{
			intAuth[i] = (int)tmp[i];
		}
		
		return intAuth;
	}
	
	
	public GameState makeNewGame(int numPlayers, String GN) throws IOException
	{
		int[] myAuthInt = readFileToInt();
		GameState = new GameState();
		
		return 0;
	}
	
	
}
