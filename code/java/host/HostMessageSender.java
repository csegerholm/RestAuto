// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package host;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import messageController.Message;

/**
 * Used to Send messages to the message controller to be forwarded to the correct employee.
 * @author cms549
 */
public class HostMessageSender extends Thread {

	/**
	 * Host's employee id
	 */
	private long empID;
	
	/**
	 * Socket that this Host will connect to.
	 */
	private Socket sock;
	
	/**
	 * list of messages to send
	 */
	ConcurrentLinkedQueue<Message> pendingMessages;
	
	/**
	 * Constructor
	 * @param listener - socket to be used
	 * @param empID - Host's employee id
	 */
	public HostMessageSender(Socket listener, long empID) {
		sock=listener;
		this.empID=empID;
		pendingMessages = new ConcurrentLinkedQueue<Message>();
	}

	/**
	 * Sends message out to Message controller
	 * Automatically will fill in senderInfo with host's position and id.
	 * @param m - message to send
	 */
	public void sendMessage(Message m){
		m.senderPosition='h';
		m.senderEmpID=empID;
		System.out.println("Host adding message:"+m);
		pendingMessages.offer(m);
	}
	
	/**
	 * Starts sending messages in pendingMessage.
	 */
	public void run(){
		DataOutputStream out;
		try {
			out = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e1) {
			System.out.println("Failed to start up sender for Host.");
			return;
		}
		
		
		while(true){
			Message m =pendingMessages.poll();
			if(m!=null){
				try {
					System.out.println("Host sending message:"+m);
					out.writeUTF(m.toString());
				} catch (IOException e) {
					System.out.println("Host Messsage Sender shutting down.");
					return;
				}
				
			}
		}
	}
}
