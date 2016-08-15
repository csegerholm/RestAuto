// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package chef;

import java.io.DataInputStream;
import java.net.Socket;

import dataBaseC.Ticket;
import messageController.Message;

/**
 * Used to listen from messages from the message controller and decode them.
 * @author cms549
 */
public class ChefMessageListener extends Thread {
	
	/**
	 * Socket that this waiter will connect to.
	 */
	private Socket sock;
	
	/**
	 * Pointer back to its waiter interface
	 */
	private ChefInterface ci;

	/**
	 * Constructor
	 * @param listener - socket to listen to
	 * @param cI - chef interface
	 */
	public ChefMessageListener(Socket listener, ChefInterface wI) {
		sock=listener;
		ci=wI;
	}
	
	/**
	 * Listens for messages sent from the MC
	 */
	public void run(){
		DataInputStream in = null;
		try {
			in = new DataInputStream(sock.getInputStream());
			//just keep listening
			while(true){
				String mes = in.readUTF();
				if(mes.length()==2){
					String second = in.readUTF();
					mes = mes +second;
				}
				System.out.println("Message = "+ mes);
				Message m = Message.fromString(mes);
				decodeMessage(m);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Chef Message Listener disconnected from MC.");
		} 
		
	}

	/**
	 * If manager sent message it must be a notification
	 * @param m
	 */
	private void decodeMessage(Message m) {
		char senderPos = m.senderPosition;
		if(senderPos=='m'){
			//NOTIFY from manager
			ci.addNotification(m.content);
		}
		else if(senderPos=='w'){
			//add new ticket
			ci.chefTicketListener(Ticket.fromString(m.content));
			//THE STRING IS IN THE FORM ticket.toString from the Ticket class
		}
	}
	
}
