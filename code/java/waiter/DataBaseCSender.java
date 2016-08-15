// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package waiter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import dataBaseC.Ticket;

/**
 * Used to Send ticket auditing messages to DB C to be recorded.
 * @author cms549
 */
public class DataBaseCSender extends Thread {
	
	/**
	 * DB Socket that this waiter will connect to.
	 */
	public Socket sock;
	
	/**
	 * Output stream connected to DB C
	 */
	private DataOutputStream out;
	
	/**
	 * list of messages to send to DBC
	 */
	ConcurrentLinkedQueue<String> pendingMessages;
	
	/**
	 * Constructor
	 * @param listener - socket to be used
	 * @param out - output stream
	 */
	public DataBaseCSender(Socket listener, DataOutputStream out) {
		sock=listener;
		this.out=out;
		pendingMessages = new ConcurrentLinkedQueue<String>();
	}

	/**
	 * Sends ticket out to DB C to be recorded makes the ticket into 
	 * @param m - message to send
	 */
	public void sendTicket(Ticket t){
		String m = "T"+t.toStringForDBC();
		pendingMessages.offer(m);
	}
	
	/**
	 * Starts sending messages in pendingMessage.
	 */
	public void run(){

		while(true){
			String m =pendingMessages.poll();
			if(m!=null){
				try {
					out.writeUTF(m);
				} catch (IOException e) {
					System.out.println("Waiter DB C Sender shutting down.");
					return;
				}
				
			}
		}
	}
}
