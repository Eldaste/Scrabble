package proxy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Scanner;
import java.util.logging.Logger;

import proxy.ProxyServer;

public class PlayerThread extends Thread {
	private ProxyServer server;
	private Socket socket;
	final static String WORDS_FILENAME = "allwords.txt";
	private static final Logger Log = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public PlayerThread(ProxyServer playerServer, Socket proxySocket) {
		this.server = playerServer;
		this.socket = proxySocket;
	}

	@SuppressWarnings("resource")
	public void run() {
		Log.info("Running new thread for client.");
		DataOutputStream out = null;
		BufferedReader br, fr = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			fr = new BufferedReader(new FileReader(WORDS_FILENAME));
			out = new DataOutputStream(socket.getOutputStream());
			PrintStream print = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			Log.severe("Some issue with making IO Objects");
			return;
		}
		Log.info("Successful creation of IO");
		String line, word;
		while (true) {
			try {
				line = br.readLine();
				if ((line == null) || 
						line.equalsIgnoreCase("SHUTDOWNSHUTDOWNSHUTDOWNSHUTDOWN")) {
					Log.info("Closing socket and thread for client player.");
					this.server.players.remove(Thread.currentThread());
					socket.close();
					return;
				} else { // read word & check if legit
					boolean found = false;
					line = line.trim(); // remove white space
					Log.info("PlayerThread, run() - reading socket: " + line);
					while ((word = fr.readLine()) != null && !found) {
						Log.info("Searching");
						if (word.equals(line)) {
							Log.info("Word exists!");
							
							found = true;
							out.flush();
						}
					}
					if (!found) {
						out.writeBoolean(false); // word exists
					} else {
						out.writeBoolean(true); // word exists
					}
					out.flush();
				}
				Log.info("Out of try");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
