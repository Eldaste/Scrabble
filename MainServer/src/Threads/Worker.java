package Threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

import mainsrv.Server;

public class Worker extends Thread {
	
	private Socket inte;
	private Server serv;

	public Worker(Socket newcon, Server jj) {
		inte=newcon;
		serv=jj;
	}
	
	public void run(){
		try {
			InputStream i=inte.getInputStream();
			OutputStream o=inte.getOutputStream();
			
			loop:while(true){
				if(inte.isClosed())
					break loop;
				
				int c=i.read();
				int[] msg=recoverMsg(i);
				
				int[] outgoing={0xFF};
				
				switch(c){
					case 0x00:	if(serv.validate(msg))
									outgoing[0]=0x00;
								break; 
					default:	continue loop;
				}
				
				o.write(c);
				int kk=outgoing.length;
				
				for(;kk>255;kk-=255){
					o.write(0x00);
				}
				
				o.write(kk);
				
				for(int ii=0;ii<outgoing.length;ii++){
					o.write(outgoing[ii]);
				}
			}
		} catch (IOException e) {/*e.printStackTrace();*/} 
		catch (SQLException e) {e.printStackTrace();}
		finally{
			System.out.println("Client Diconnected ");
			try {
				inte.close();
			} 
			catch (IOException e) {}
		}
	}
	
	public int[] recoverMsg(InputStream i) throws IOException{
		int msglen=0;
		
		ll:while(true){
			int b=i.read();
			
			if(b==0){
				msglen+=255;
				continue ll;
			}
			
			msglen+=b;
			break ll;
		}
		
		int [] re=new  int[msglen];
		
		for(int ii=0;ii<msglen;ii++){
			re[ii]=i.read();
		}
		
		return re;
	}
}
