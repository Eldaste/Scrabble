package valid;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/* This class makes a word checker that is connected to the Proxy Server, which checks the word against a word file. */
public class WordChecker {
	/* for connecting to proxy server */
	static final String PROXYSERVER_ADDRESS = "localhost";
	public static final int PROXYPORT = 2000;
	
	/* Class Variables */
	String word;
	Socket clientSocket;
	PrintWriter out;
	DataInputStream in;
	
	WordChecker() throws IOException { /* default ctor */
		word = null;
		try {
			clientSocket = new Socket(PROXYSERVER_ADDRESS, PROXYPORT);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new DataInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	/* sets word and checks */
	public boolean isValid(String w) throws IOException {
		word = w.toLowerCase().trim();
		int wordPoints = 0;
		out.println(word);	// send word to proxy to check
		if (word.equals("shutdownshutdownshutdownshutdown")) { // stops proxy
			return;	
		}
		if(in.readBoolean()) {// proxy sends back bool
			return true;
		}else{
			return false;
		}
	}
}
