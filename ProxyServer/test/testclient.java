package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class testclient {
	static final String SERVER_ADDRESS = "localhost";
	public static final int PORT = 2000;

	public static void main(String[] args) throws InterruptedException {
		// set up socket connection to server
		try {
			Socket clientSocket = new Socket(SERVER_ADDRESS, PORT);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String request;
			String response;
			request = "zebra";
			System.out.println("Client: " + request);
			out.println(request);
			response = in.readLine(); // read the text from client
			System.out.println("Server: " + response);
			request = "cowboy";
			System.out.println("Client: " + request);
			out.println(request);
			response = in.readLine(); // read the text from client
			System.out.println("Server: " + response);
			request = "SHUTDOWNSHUTDOWNSHUTDOWNSHUTDOWN";
			System.out.println("Client: " + request);
			out.println(request);
			response = in.readLine(); // read the text from client
			System.out.println("Server: " + response);
			out.close(); // close stream

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
