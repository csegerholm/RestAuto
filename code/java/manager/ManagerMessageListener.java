// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package manager;

import java.io.DataInputStream;
import java.net.Socket;
import messageController.Message;

/**
 * Used to listen from messages from the message controller and decode them.
 * @author cms549
 */
public class ManagerMessageListener extends Thread {
	
	/**
	 * Socket that this manager will connect to.
	 */
	private Socket sock;
	
	/**
	 * Pointer back to its manager interface
	 */
	private ManagerInterface mi;

	/**
	 * Constructor
	 * @param listener - socket to listen to
	 * @param mI - manager interface
	 */
	public ManagerMessageListener(Socket listener, ManagerInterface wI) {
		sock=listener;
		mi=wI;
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
				Message m = Message.fromString(mes);
				mi.addMessageToList(m);
			}
			
			
		}catch (Exception e) {
			System.out.println("Manager Message Listener disconnected from MC.");
		} 
		
	}

	
}
