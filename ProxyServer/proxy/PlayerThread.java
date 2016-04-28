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

	public void run() {
		Log.info("Running new thread for client.");
		DataOutputStream out = null;
		BufferedReader br, fr = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			fr = new BufferedReader(new FileReader(WORDS_FILENAME));
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Log.severe("Some issue with making IO Objects");
			return;
		}
		String line, word;
		while (true) {
			try {
				line = br.readLine();
				if ((line == null) || line.equalsIgnoreCase("QUIT")) {
					Log.info("Closing socket and thread for client player.");
					this.server.players.remove(Thread.currentThread());
					socket.close();
					return;
				} else { // read word & check if legit
					line = line.trim(); // remove white space
					Log.info("PlayerThread, run() - reading socket: " + line);
					while ((word = fr.readLine()) != null) {
						word.trim();
						if (word.equals(line)) {
							out.writeBoolean(true); // word exists
						} else {
							out.writeBoolean(false);
						}
					}
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
