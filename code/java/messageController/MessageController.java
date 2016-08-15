// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package messageController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import configuration.Configure;

/**
 * Starts the Message Controller - will handle ticket sends and message sends between tablets
 *  First you create the listener and the listener creates the sender.
 *  When the listener receives the log out then it will close the socket and remove itself from the list.
 * Message Controller will be the one to close the socket.
 * @author cms549
 */
public class MessageController {
	
	private final static int portNumber = Configure.getPortNumber("MessageController");
	
	/**
	 * List of output streams(senders) of waiters logged in with employee id as key
	 */
	static HashMap<Long,MessageControllerSender> waiterOut; 
	
	/**
	 * List of output streams(senders) of hosts logged in with employee id as key
	 */
	static HashMap<Long,MessageControllerSender> hostOut;
	
	/**
	 * List of output streams(senders) of chefs logged in with employee id as key
	 */
	static HashMap<Long,MessageControllerSender> chefOut;
	
	/**
	 * List of output streams(senders) of managers logged in with employee id as key
	 */
	static HashMap<Long,MessageControllerSender> managerOut;

	/**
	 * Adds a waiter to the list of output stream
	 * @param id - employee id 
	 * @param listener - input stream for this employee
	 */
	public static void addWaiterSender(long id, MessageControllerSender sender){
		waiterOut.put(id,sender);
	}
	/**
	 * Adds a host to the list of output stream
	 * @param id - employee id 
	 * @param listener - input stream for this employee
	 */
	public static void addHostSender(long id,MessageControllerSender sender){
		hostOut.put(id,sender);
	}
	/**
	 * Adds a chef to the list of output stream
	 * @param id - employee id 
	 * @param listener - input stream for this employee
	 */
	public static void addChefSender(long id,MessageControllerSender sender){
		chefOut.put(id,sender);
	}
	
	/**
	 * Adds a manager to the list of output stream
	 * @param id - employee id 
	 * @param listener - input stream for this employee
	 */
	public static void addManagerSender(long id,MessageControllerSender sender){
		managerOut.put(id,sender);
	}
	
	/**
	 * Removes a waiter from the list of output streams
	 * @param id - employee id 
	 */
	public static void removeWaiterSocket(long id){
		waiterOut.remove(id);
	}
	/**
	 * Removes a host from the list of output stream
	 * @param id - employee id 
	 */
	public static void removeHostSocket(long id){
		hostOut.remove(id);
	}
	/**
	 * Removes a chef from the list of output stream
	 * @param id - employee id 
	 */
	public static void removeChefSocket(long id){
		chefOut.remove(id);
	}
	
	/**
	 * Removes a manager from the list of output stream
	 * @param id - employee id 
	 */
	public static void removeManagerSocket(long id){
		managerOut.remove(id);
	}
	
	/**
	 * Each tablet that connects with the server should have a listener and sender associated with it.
	 * @param args
	 */
	public static void main(String[] args){
		
		waiterOut= new HashMap<Long,MessageControllerSender>();
		hostOut= new HashMap<Long,MessageControllerSender>();
		chefOut= new HashMap<Long,MessageControllerSender>();
		managerOut= new HashMap<Long,MessageControllerSender>();
		
		
		
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (true) {
            	Socket oneTablet = serverSocket.accept();
				new MessageControllerListener(oneTablet).start();
               
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }	
	}
	
	/**
	 * Getter for port number
	 * @return portNumber 
	 */
	public int getPortNumber(){
		return portNumber;
	}
	
	
}
