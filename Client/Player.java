import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
    
	final int createAuth = 0x01;
	final int getTile = 0x03;
	
	public Player(String name, int score, int numOfTiles) throws IOException, GenericUsernameError
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
		File initalization = new  File("init.txt");
		
		FileOutputStream fw = new FileOutputStream(initalization);
		
		fw.write(byteAuthToken);
		
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

	public int setScore()
	{
		return 0;
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
	public static int[] recoverMsg(InputStream i) throws IOException{
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

	
	public static void sendMsg(int[] outgoing,OutputStream o) throws IOException{
		int kk=outgoing.length;
		
		for(;kk>255;kk-=255){
			o.write(0x00);
		}
		
		o.write(kk);
		
		for(int ii=0;ii<outgoing.length;ii++){
			o.write(outgoing[ii]);
		}
	}
}
