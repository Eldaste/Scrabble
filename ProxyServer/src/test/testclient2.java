package test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class testclient2 {
	static final String SERVER_ADDRESS = "localhost";
	public static final int PROXYPORT = 2000;

	public static void main(String[] args) throws InterruptedException {
		// set up socket connection to server
		try {
			Socket clientSocket = new Socket(SERVER_ADDRESS, PROXYPORT);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			String request;
			request = "apple";
			out.println(request);
			System.out.println("Client: " + request);
			if(in.readBoolean()) // read the text from client
				System.out.println("Server: exists!");
			else{
				System.out.println("Server: NOT exists!");
			}
			request = "cowboyzzzz";
			out.println(request);
			System.out.println("Client: " + request);
			if(in.read() != 0) // read the text from client
				System.out.println("Server: exists!");
			//System.out.println("Server: " + response);
			request = "SHUTDOWNSHUTDOWNSHUTDOWNSHUTDOWN";
			System.out.println("Client: " + request);
			out.println(request);
			//System.out.println("Server: " + response);
			out.close(); // close stream

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
