package proxy;

import java.io.*;
import java.net.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ProxyServer {
	public static final int PORT = 2000;
	ServerSocket serverSocket;
	Thread mainThread;
	Set<PlayerThread> players = // keep track of players; internal locks on all
								// methods of set
	Collections.synchronizedSet(new HashSet<PlayerThread>());
	private static final Logger Log = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	public ProxyServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.mainThread = Thread.currentThread();
	}

	@SuppressWarnings("resource")
	public void acceptLoop() {
		Socket socket = null;
		try {
			while (true) {
				socket = serverSocket.accept();
				Log.info("Accepted server socket");
				PlayerThread thread = new PlayerThread(this, socket);
				thread.start();
				this.players.add(thread);
			}
		} catch(IOException e) {
			if(serverSocket.isClosed()) {
				Log.info("Server socket is closed, acceptLoop was exited");
			} else {
				Log.severe("Some error in acceptLoop");
				try {
					serverSocket.close();
				} catch(IOException ignored) {}
			}
		}

	}
	public static void main(String[] args) throws IOException {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		ProxyServer ps = new ProxyServer(PORT);
		ps.acceptLoop();
	}

}
