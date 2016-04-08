import java.io.*;
import java.net.*;
import java.util.logging.Logger;

public class ProxyServer {
	private static String host;
	private static int remoteport;
	private static int localport;
	private static final Logger Log = 
			Logger.getLogger(Thread.currentThread().getStackTrace()
					[0].getClassName());

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			System.out.println("Starting proxy server for " + host
					+ ":" + remoteport + " on port " + localport);
			ServerSocket serverSocket = new ServerSocket(localport);
			
			final byte[] request = new byte[1024];
			byte[] reply = new byte[4096];
			
			while(true) {
				Socket client = null, server = null;
				client = serverSocket.accept();
				Log.info("Accepted server socket for client on local port.");
				// make input streams
				final InputStream fromClient = client.getInputStream();
				final OutputStream toClient = client.getOutputStream();
				
				try {
					server = new Socket(host, remoteport);
					Log.info("Successfully connected to server.");
				} catch (IOException e) {
					PrintWriter out = new PrintWriter(toClient);
			        out.print("Proxy server cannot connect to " + host + ":"
			              + remoteport + ":\n" + e + "\n");
			        out.flush();
			        throw new RuntimeException(e);
				}
				
				// make server streams
				final InputStream fromServer = server.getInputStream();
				final OutputStream toServer = server.getOutputStream();
				
			}
			
		} catch (Exception e) {
			Log.severe("Some error in starting up proxy.");
			System.err.println(e);
		}

	}

}
