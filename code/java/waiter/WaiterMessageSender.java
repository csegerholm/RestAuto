// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package waiter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import messageController.Message;

/**
 * Used to Send messages to the message controller to be forwarded to the correct employee.
 * @author cms549
 */
public class WaiterMessageSender extends Thread {

	/**
	 * Waiter's employee id
	 */
	private long empID;
	
	/**
	 * Socket that this waiter will connect to.
	 */
	private Socket sock;
	
	/**
	 * list of messages to send
	 */
	ConcurrentLinkedQueue<Message> pendingMessages;
	
	/**
	 * Constructor
	 * @param listener - socket to be used
	 * @param empID - waiter's employee id
	 */
	public WaiterMessageSender(Socket listener, long empID) {
		sock=listener;
		this.empID=empID;
		pendingMessages = new ConcurrentLinkedQueue<Message>();
	}

	/**
	 * Sends message out to Message controller
	 * Automatically will fill in senderInfo with waiter's position and id.
	 * @param m - message to send
	 */
	public void sendMessage(Message m){
		m.senderPosition='w';
		m.senderEmpID=empID;
		System.out.println("Waiter adding message:"+m);
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
			System.out.println("Failed to start up sender for waiter.");
			return;
		}
		
		
		while(true){
			Message m =pendingMessages.poll();
			if(m!=null){
				try {
					System.out.println("Waiter sending message:"+m);
					out.writeUTF(m.toString());
				} catch (IOException e) {
					System.out.println("Waiter Messsage Sender shutting down.");
					return;
				}
				
			}
		}
	}
}
