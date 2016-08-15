// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package messageController;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

/**
 * Listens to messages from one tablet.
 * @author cms549
 */
public class MessageControllerListener  extends Thread{

	/**
	 * Socket this controller will listen to
	 */
	private Socket currListener;
	
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
	 * @param listener - socket to listen to
	 * @param sender - MessageControllerSender - this will add messages to this to send it
	 */
	public MessageControllerListener(Socket listener) {
		currListener=listener;
	}
	
	/**
	 * Starts listening to messages. Closes the socket once employee sends log out message (pos ='X')
	 */
	public void run(){
		try {
			DataInputStream in = new DataInputStream(currListener.getInputStream());
			String message =in.readUTF();
			if(message.length()==2){
				String second = in.readUTF();
				message = message+second;
			}
			System.out.println("Log in message : "+message);
			Message m = Message.fromString(message);
			char pos = m.receiverPosition;
			if(pos=='L'){//logging in
				empPos = m.senderPosition;
				empId = m.senderEmpID;
				if(empPos=='w'|| empPos=='c' || empPos=='h'|| empPos=='m'){
					new MessageControllerSender(currListener, empPos, empId).start();
				}
				else{
					System.out.println("Position "+empPos+" is not recognized.");
					currListener.close();
					return;
				}
			}else{//issue everyone needs to first log in before using MC
				pos = m.senderPosition;
				System.out.println("Position "+pos+" tried to use MC w/o log in");
				currListener.close();
				return;
			}
			
			//listen to messages until a log out
			while(true){
				message =in.readUTF();
				if(message.length()==2){
					String second = in.readUTF();
					message = message+second;
				}
				System.out.println("Received : "+message);
				m = Message.fromString(message);
				pos = m.receiverPosition;
				if(pos=='X'){//logging out
					if(empPos=='w'){
						MessageController.removeWaiterSocket(empId);
					}
					else if(empPos=='c'){
						MessageController.removeChefSocket(empId);
					}
					else if(empPos=='h'){
						MessageController.removeHostSocket(empId);
					}
					else if(empPos=='m'){
						MessageController.removeManagerSocket(empId);
					}
					currListener.close();
					return;
				}
				else{
					MessageControllerSender sender;
					if(pos=='w'){
						if(MessageController.waiterOut.isEmpty()){ //if no waiter logged in
							continue;
						}
						if(m.receiverEmpID==-1){
							//send to all waiters
							Iterator<Long> it = MessageController.waiterOut.keySet().iterator();
							//grab each employees id of waiter and 
							while(it.hasNext()){
								sender = MessageController.waiterOut.get(it.next());
								//Add the forwarded message to message controller
								System.out.println("Adding message:"+m+" -To "+pos);
								sender.pendingMessages.offer(m);
							}
							
							
							continue;
						}
						sender = MessageController.waiterOut.get(m.receiverEmpID);
					}
					else if(pos=='c'){
						if(MessageController.chefOut.isEmpty()){ //if no chef logged in
							continue;
						}
						if(m.receiverEmpID==-1){
							m.receiverEmpID = MessageController.chefOut.keySet().iterator().next();
						}
						sender = MessageController.chefOut.get(m.receiverEmpID);
					}
					else if(pos=='h'){
						if(MessageController.hostOut.isEmpty()){ //if no host logged in
							continue;
						}
						if(m.receiverEmpID==-1){
							m.receiverEmpID = MessageController.hostOut.keySet().iterator().next();
						}
						sender = MessageController.hostOut.get(m.receiverEmpID);
					}
					else if(pos=='m'){
						if(MessageController.managerOut.isEmpty()){ //if no manager logged in
							continue;
						}
						if(m.receiverEmpID==-1){
							m.receiverEmpID = MessageController.managerOut.keySet().iterator().next();
						}
						sender = MessageController.managerOut.get(m.receiverEmpID);
					}
					else{//error with this message position
						System.out.println("Error message did not have valid receiver.");
						continue;
					}
					//Add the forwarded message to message controller
					System.out.println("Adding message:"+m+" -To "+pos);
					sender.pendingMessages.offer(m);
				}
			}	
		}catch (Exception e) {
			System.out.println("Removing "+empPos+empId+" from MC");
			if(empPos=='w'){
				MessageController.removeWaiterSocket(empId);
			}
			else if(empPos=='c'){
				MessageController.removeChefSocket(empId);
			}
			else if(empPos=='h'){
				MessageController.removeHostSocket(empId);
			}
			else if(empPos=='m'){
				MessageController.removeManagerSocket(empId);
			}
			try {
				currListener.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} 
		
	}
}
