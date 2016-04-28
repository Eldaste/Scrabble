package test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class testclient {
	static final String SERVER_ADDRESS = "localhost";
	public static final int PROXYPORT = 2000;

	public static void main(String[] args) throws InterruptedException {
		// set up socket connection to server
		try {
			Socket clientSocket = new Socket(SERVER_ADDRESS, PROXYPORT);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			String word;
			word = "apple";
			word = word.toLowerCase().trim();
			int wordPoints = 0;
			out.println(word);
			System.out.println("Client: " + word);
			if(in.readBoolean()) {// word exists in file
				System.out.println("Server: exists!");
				
				for (int i = 0; i < word.length(); i++){
				    char c = word.charAt(i);        
				    wordPoints += getPoints(c);
				}
				System.out.println(wordPoints);
			
			}else{
				System.out.println("Server: NOT exists!");
			}
			word = "cowboyzzzz";
			out.println(word);
			System.out.println("Client: " + word);
			if(in.readBoolean()) {// word exists in file
				System.out.println("Server: exists!");
			//System.out.println("Server: " + response);
			}else{
				System.out.println("Server: NOT exists!");
			}
			word = "SHUTDOWNSHUTDOWNSHUTDOWNSHUTDOWN";
			System.out.println("Client: " + word);
			out.println(word);
			//System.out.println("Server: " + response);
			out.close(); // close stream

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static int getPoints(char c) {
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
