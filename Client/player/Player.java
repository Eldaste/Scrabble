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
import java.util.Arrays;

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
    final int nullSpace = 0x00;
	final int createAuth = 0x01;
	final int getTile = 0x03;
	final int getGameSeek = 0x13;
	final int makeGame = 0x20;
	final int failResponse = 0xFF;
	
	public Player(String name) throws IOException, GenericUsernameError
	{
		//connect to server port 8080
		connect();
		
		//initialize
		myName = name;
		
		System.out.println(myName);
		
		out.write(createAuth);
		
		byte [] nameBytes = myName.getBytes();
		System.out.println(Arrays.toString(nameBytes));
		
		int[] tmp = byteToInt(nameBytes);
		

		System.out.println(Arrays.toString(tmp));
		
		//send int array
		sendMsg(tmp,out);
		
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
	
	public String getMyName() {
		return myName;
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
	
	public int[] byteToInt(byte[] bytes) {
		int[] res=new int[bytes.length];
		
		for(int i=0;i<res.length;i++){
			res[i]=bytes[i];
		}
		
		return res;
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
	
	
	public GameState makeNewGame(int numP, String gName) throws IOException
	{
		int[] myAuthInt = readFileToInt();
		
		GameState myGameState = new GameState(gName,numP);
		int[] name = myGameState.char2IntArray();
		
		int[] nameInt = byteToInt(myName.getBytes());
		
		String numPl=Integer.toString(numP);
		
		//add the arguments 
		int [] makeGameMsg = new int[nameInt.length+myAuthInt.length+name.length+3+numPl.length()];
		int i = 0;
		
		for (; i < nameInt.length; i++)
		{
			makeGameMsg[i]= nameInt[i];
		}
		
		makeGameMsg[i]=0;
		i++;
		
		for (int j=0; j < myAuthInt.length; i++,j++)
		{
			makeGameMsg[i]= myAuthInt[j];
		}
		
		makeGameMsg[i]=nullSpace;
		i++;
		
		for (int j=0; j < numPl.length(); i++,j++)
		{
			makeGameMsg[i]= numPl.charAt(j);
		}
		
		makeGameMsg[i]=nullSpace;
		i++;
		
		for (int j=0; j <name.length; j++,i++)
		{
			makeGameMsg[i] = name[j];
		}
		
		out.write(makeGame);
		sendMsg(makeGameMsg,out);
		
		in.read();
		recoverMsg(in);
		//Done with creation of game at this point
		
		int[]checkGameMsg = composeUAMsg(nameInt, myAuthInt);
		
		
		for(int h = 1; h < myAuthInt.length+1;h++)
		{
			checkGameMsg[h] = myAuthInt[h];
		}
		
		sendMsg(checkGameMsg,out);
		in.read();
		
		int[] response = recoverMsg(in);
		
		while(response[nullSpace] == 0x00)
		{
			sendMsg(checkGameMsg,out);
			in.read();
			
			response = recoverMsg(in);
		}

		
		//it should only equal 1
		if(response[nullSpace] == 1)
		{
			int num = response[1];
			
			int start = 0;
			for(;start< num;start++)
			{
				if(response[start] == 0x00)
				{
					break;
				}
			}
			
			char[] tmp = new char[num-start+1];
			for(; start < num;start++)
			{
				tmp[start] = (char)response[start];
			}
			
			//tmp is now the game num in chars
			String tmpString = tmp.toString();
			
			int GN = Integer.decode(tmpString);
			
			myGameState.setGameNum(GN);
		}

		return myGameState;
		
	}
	
	public void joinNewGame() throws IOException, joinFailureError
	{
		//get list of all matches
		int[] tmp = composeUAMsg(getMyName(),readFileToInt());
		
		out.write(getGameSeek);
		
		sendMsg(tmp, out);
		
		in.read();
		
		int[] GS = recoverMsg(in);
		
		if(GS[nullSpace] == failResponse)
		{
			throw new joinFailureError();
		}else
		{
			for(int i = 0; i< GS[nullSpace];i++)
			{
				
			}
		}
		
		
		
	}
	
	public static int[] concatWZero(int[] o,int[] t){
		int [] makeGameMsg = new int[o.length+t.length+1];
		  int i = 0;
		  
		  for (; i < nameInt.length; i++)
		  {
		   makeGameMsg[i]= o[i];
		  }
		  
		  makeGameMsg[i]=0;
		  i++;
		  
		  for (int j=0; j < myAuthInt.length; i++,j++)
		  {
		   makeGameMsg[i]= t[j];
		  }

		 return makeGameMsg;
		}
	
	public int[] composeUAMsg(String name,int[] myAuthInt){
		int [] makeGameMsg = new int[name.length()+myAuthInt.length+2];
		  int i = 0;
		  
		  for (; i < name.length(); i++)
		  {
		   makeGameMsg[i]= name.charAt(i);
		  }
		  
		  makeGameMsg[i]=nullSpace;
		  i++;
		  
		  for (int j=0; j < myAuthInt.length; i++,j++)
		  {
		   makeGameMsg[i]= myAuthInt[j];
		  }
		  
		  makeGameMsg[i]=nullSpace;

		  return makeGameMsg;
	}
	
	public int[] composeMsg(String nameInt,int[] myAuthInt,int[] msg){
		  int [] makeGameMsg = new int[nameInt.length()+myAuthInt.length+msg.length+2];
		  int i = 0;
		  
		  for (; i < nameInt.length(); i++)
		  {
		   makeGameMsg[i]= nameInt.charAt(i);
		  }
		  
		  makeGameMsg[i]=nullSpace;
		  i++;
		  
		  for (int j=0; j < myAuthInt.length; i++,j++)
		  {
		   makeGameMsg[i]= myAuthInt[j];
		  }
		  
		  makeGameMsg[i]=nullSpace;
		  i++;
		  
		  for (int j=0; j < msg.length; i++,j++)
		  {
		   makeGameMsg[i]= msg[j];
		  }

		  return makeGameMsg;
		}
}
