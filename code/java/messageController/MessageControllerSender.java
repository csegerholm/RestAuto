// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package messageController;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Sends messages to a particular tablet connected to the message controller.
 * @author cms549
 */
public class MessageControllerSender extends Thread {
	
	/**
	 * Socket this controller will listen to
	 */
	private Socket currSender;
	
	/**
	 * list of messages to send
	 */
	ConcurrentLinkedQueue<Message> pendingMessages;
	
	/**
	 * Unique Employee ID of employee communicating with this socket
	 */
	private long empId;
	/**
	 * Employee position of employee communicating with this socket
	 */
	private char empPos;
	
	
	/**
	 * Constructor
	 * @param oneTablet - socket sender will be sending messages on
	 * @param empId - unique id of employee this socket sends messages to
	 * @param empPos - position of employee this socket sends messages to
	 */
	public MessageControllerSender(Socket oneTablet, char empPos, long empId){
		pendingMessages = new ConcurrentLinkedQueue<Message>();
		currSender = oneTablet;
		this.empPos = empPos;
		this.empId = empId;
	}
	
	/**
	 * Starts sending messages in pendingMessage.
	 */
	public void run(){
		DataOutputStream out;
		try {
			out = new DataOutputStream(currSender.getOutputStream());
		} catch (IOException e1) {
			System.out.println("Failed to start up sender for pos = "+empPos);
			return;
		}
		if(empPos=='w'){
			MessageController.addWaiterSender(empId, this);
		}
		else if(empPos=='c'){
			MessageController.addChefSender(empId, this);
		}
		else if(empPos=='h'){
			MessageController.addHostSender(empId, this);
		}
		else if(empPos=='m'){
			MessageController.addManagerSender(empId, this);
		}
		
		
		while(true){
			Message m =pendingMessages.poll();
			if(m!=null){
				try {
					System.out.println("Sending: "+ m +" - to "+empPos +empId);
					out.writeUTF(m.toString());
				} catch (IOException e) {
					System.out.println("Messsage Controller for Pos = "+ empPos+" shutting down.");
					return;
				}
				
			}
		}
	}
}
