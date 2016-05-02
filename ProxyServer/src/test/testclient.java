package test;
import parsed.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.xml.sax.SAXException;

<<<<<<< HEAD:ProxyServer/src/test/testclient.java
=======
import parser.Doc;
>>>>>>> refs/remotes/origin/SelinaBranch:ProxyServer/test/testclient.java

public class testclient {
	static final String PROXYSERVER_ADDRESS = "localhost";
	public static final int PROXYPORT = 2000;

	public static void main(String[] args) throws IOException {
		// set up socket connection to server
		Socket clientSocket = null;
		PrintWriter out = null;
		DataInputStream in  = null;
		try {
			clientSocket = new Socket(PROXYSERVER_ADDRESS, PROXYPORT);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new DataInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String word = "apple";
		tryWord(word , out, in);
		word = "bear";
		tryWord(word , out, in);
	}
	public static void connectProxy() throws IOException {

	}
	public static void tryWord(String word, PrintWriter out,DataInputStream in) throws IOException {
		word = word.toLowerCase().trim();
		int wordPoints = 0;
		out.println(word);
		System.out.println("Client: " + word);
		
		if(in.readBoolean()) {// word exists in file
			System.out.println("Server: exists!");
			for (int i = 0; i < word.length(); i++){
			    char c = word.charAt(i);        
			    wordPoints += letterPoints(c);
			}
			System.out.println(word + ": " + wordPoints);   // CHANGE?: Points for the word
		
		}else{
			System.out.println("Server: NOT exists!");
		}
	}
	public static int letterPoints(char c) {
        int letterPoints = 0;

        switch (c) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'n':
            case 'r':
            case 't':
            case 'l':
            case 'u':
                letterPoints = 1;
                break;
            case 'd':
            case 'g':
            	letterPoints = 2;
                break;
            case 'b':
            case 'c':
            case 'm':
            case 'p':
            	letterPoints = 3;
                break;
            case 'f':
            case 'h':
            case 'v':
            case 'w':
            case 'y':
            	letterPoints = 4;
                break;
            case 'k':
            	letterPoints = 5;
                break;
            case 'j':
            case 'x':
            	letterPoints= 6;
                break;
            case 'q':
            case 'z':
            	letterPoints = 7;
                break;
           
            default: 
            	letterPoints = 0;
                break;
        }

        return letterPoints;
    }
}
