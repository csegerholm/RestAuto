// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package dataBaseC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;

/**
 * Listens to messages from one chef or waiter.
 * Waiter will send it Tickets to record. This message must start with a T followed by the ticket info.
 * Chef will send it started dishes. This message must start with D followed by the dish name.
 * @author cms549
 */
public class Listener  extends Thread{

	static int currID=0;
	/**
	 * Socket's id - used in the DB C list of Waiters, -1 means its a chef
	 */
	private int socketid;
	
	/**
	 * Socket this listener will listen on
	 */
	private Socket currListener;
	
	/**
	 * Used to convert java objects to JSON format and vice versa.
	 */
	public Gson jsonConverter;
	
	
	/**
	 * Constructor 
	 * @param listener - socket to listen to
	 * @param sender - MessageControllerSender - this will add messages to this to send it
	 */
	public Listener(Socket listener) {
		currListener=listener;
		jsonConverter = new Gson();
	}
	

	/**
	 * Starts a new thread for the DB C controller so it can communicate with one tablet on one thread.
	 * Reads the socket in to determine the request and responds accordingly.
	 * Socket will hang up on waiters after sending them menu and after reading a ticket
	 * Socket won't hang up on chef since chef will be sending information about each dish that was started.
	 */
	public void run(){
		//MUST FIRST ADD WAITER OR CHEF SENDER TO THE LIST
		//then start listening 
		
		String mess = "";
		try(DataInputStream in = new DataInputStream(currListener.getInputStream()); 
				DataOutputStream out = new DataOutputStream(currListener.getOutputStream())) {
			mess =in.readUTF();
			char first = mess.charAt(0);
			if(first=='M'){//Waiter needs menu when logging in
				currID++;
				socketid = currID;
				DatabaseCController.waiterSenders.put(currID,new Sender(out, 'w'));
				String jmenu = jsonConverter.toJson(DatabaseCController.menu);
				out.writeUTF(jmenu);
				while(true){
					mess =in.readUTF();
					if(mess.length()<3){
						mess = mess+ in.readUTF();
					}
					first = mess.charAt(0);
					if(first == 'T'){//waiter is sending you a paid ticket so you can save it - hang up after you read it
						String tick = mess.substring(1);
						//add the ticket to the file that holds all tickets for today
						DatabaseCController.recordTicket(tick);
					}
				}
			}
			else{
				//set up chef interface
				DatabaseCController.chefSender = new Sender(out, 'c');
				socketid = -1;
				while(true){
					mess =in.readUTF();
					if(mess.length()<3){
						mess = mess+ in.readUTF();
					}
					first = mess.charAt(0);
					
					if(first=='A'){ //add ingredinet
						//add the ingredient with the info into the inventory
					}
					else if(first=='R'){//Remove this ingredinet from inventory
						
					}
					else if(first =='D'){//decrement the ingredients for this dish
						DatabaseCController.decrementDish(mess.substring(1));
					}
				}
				
				
			}
			
			
		} catch (Exception e) {
			try {
				//NEED A WAY TO REMOVE HIM FROM THE LIST!!!!!!!!!!!!
				if(socketid==-1){
					DatabaseCController.chefSender=null;
				}
				else{
					DatabaseCController.waiterSenders.remove(currID);
				}
				currListener.close();
			} catch (IOException e1) {			}
			System.out.println("DBC Listener Closing: Before closing Read in: "+ mess);
			e.printStackTrace();
		} 
	}
	
	
}
