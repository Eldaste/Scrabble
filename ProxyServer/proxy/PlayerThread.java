package proxy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
	private static final Logger Log = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public PlayerThread(ProxyServer playerServer, Socket proxySocket) {
		this.server = playerServer;
		this.socket = proxySocket;
	}

	public void run() {
		Log.info("Running new thread for client.");
		DataOutputStream out = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Log.severe("Some issue with making IO Objects");
			return;
		}
		String line;
		try {
			Scanner scanner = new Scanner("allwords.txt");
			while (true) {
				try {
					line = br.readLine();
					if ((line == null) || line.equalsIgnoreCase("QUIT")) {
						Log.info("Closing socket and thread for client player.");
						socket.close();
						return;
					} else { // read word & check if legit
						line = line.trim(); // remove all leading and trailing
											// white space
						Log.info("PlayerThread, run() - reading socket: " + line);

						if (exists(line)) {
							out.writeBytes(line + "\n\r");
						} else {

						}
						out.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			while ((line = br.readLine()) != null) {

				if (line.startsWith("SHUTDOWN")) { // stop account stuff
					Log.info("Closing socket to stop new clients from connecting");
					this.server.serverSocket.close();
					// remove current thread from list before entering loop
					this.server.workers.remove(Thread.currentThread());
					for (WorkerThread wt : this.server.workers) {
						// wait until all clients disconnect
						wt.join();
					}

					// calculate sum
					int sum = 0;
					Collection<Account> accounts = this.server.bananaBank.getAllAccounts();
					for (Account acc : accounts) {
						sum += acc.getBalance();
					}
					print.println(String.valueOf(sum) + "\n");
					print.flush();
					// save new account statuses
					this.server.bananaBank.save("output_accounts.txt");

				} else { // perform account operations
					String[] tokens = line.split(" ");

					if (tokens.length != 3) {
						print.println("Failure: Invalid operation\n");
						throw new IllegalArgumentException();
					}

					amount = Integer.parseInt(tokens[0]);
					src = Integer.parseInt(tokens[1]);
					dst = Integer.parseInt(tokens[2]);
					Account srcAcc = this.server.bananaBank.getAccount(src);
					Account dstAcc = this.server.bananaBank.getAccount(dst);

					if (srcAcc == null) {
						print.println("Invalid source account");
					} else if (dstAcc == null) {
						print.println("Invalid destination account");
					} else {
						// prevent deadlocks by acquiring locks in fixed
						// sequence
						Account lock1, lock2;
						if (srcAcc.getAccountNumber() == dstAcc.getAccountNumber()) {
							print.println("Failure: Invalid Operation: " + "account cannot transfer to itself");
							throw new IllegalArgumentException();
						} else if (srcAcc.getAccountNumber() < dstAcc.getAccountNumber()) {
							lock1 = srcAcc;
							lock2 = dstAcc;
						} else {
							lock1 = dstAcc;
							lock2 = srcAcc;
						}
						synchronized (lock1) {
							synchronized (lock2) {
								if (lock1.getBalance() >= amount) {
									srcAcc.transferTo(amount, dstAcc);
								} else {
									print.println("Failure: Invalid Operation: "
											+ "account does not have enough to make transfer");
									throw new IllegalArgumentException();
								}
							}
						}
						print.println(
								tokens[0] + " was transferred from account " + tokens[1] + " to account " + tokens[2]);
					}
					print.flush();
				}
			}
		} catch (

		InterruptedException e)

		{
			e.printStackTrace();
		} catch (

		IOException e)

		{
			e.printStackTrace();
		}
	}
	public boolean exists(String line) {
		return 
	}

}
