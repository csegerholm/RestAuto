// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package waiter;

import java.io.DataInputStream;
import java.net.Socket;

import dataBaseC.Ticket;
import messageController.Message;

/**
 * Used to listen from messages from the message controller and decode them.
 * @author cms549
 */
public class WaiterMessageListener extends Thread {
	
	/**
	 * Socket that this waiter will connect to.
	 */
	private Socket sock;
	
	/**
	 * Pointer back to its waiter interface
	 */
	private WaiterInterface wi;

	/**
	 * Constructor
	 * @param listener - socket to listen to
	 * @param wI - waiter interface
	 */
	public WaiterMessageListener(Socket listener, WaiterInterface wI) {
		sock=listener;
		wi=wI;
	}
	
	/**
	 * Listens for messages sent from the MC
	 */
	public void run(){
		DataInputStream in = null;
		try {
			in = new DataInputStream(sock.getInputStream());
			System.out.println("WML will start listening now.");
			//just keep listening
			while(true){
				String mes = in.readUTF();
				if(mes.length()==2){
					String second = in.readUTF();
					mes = mes +second;
				}
				System.out.println("Waiter reading message:"+mes);
				Message m = Message.fromString(mes);
				decodeMessage(m);
			}
			
			
		}catch (Exception e) {
			System.out.println("Waiter Message Listener disconnected from MC.");
		} 
		
	}

	/**
	 * If manager sent message it must be a notification
	 * if host sent message it must be a recently sat -> the content is the table number
	 * if chef sent message it must be hot food -> the content is the table number
	 * @param m
	 */
	private void decodeMessage(Message m) {
		char senderPos = m.senderPosition;
		if(senderPos=='m'){
			//NOTIFY
			wi.addNotification(m.content);
		}
		else if(senderPos=='h'){
			char type = m.content.charAt(0);
			String tNumStr = m.content.substring(1);
			if(type=='R'){
				//RECENTLY SAT
				int tableNumber = Integer.parseInt(tNumStr);
				Ticket t = new Ticket(wi.name, tableNumber, wi.empID);
				wi.listOfTickets.put(tableNumber, t);
				t.recentlySat=true;
			}
			else if(type == 'N'){
				//notification that table needs you
				wi.addNotification("Table "+ tNumStr+ " needs your assistance.");
			}
			wi.updateScreen();
		}
		else if(senderPos=='c'){
			//HOT FOOD
			String tNumStr = m.content;
			int tableNumber = Integer.parseInt(tNumStr);
			Ticket t =wi.listOfTickets.get(tableNumber);
			t.hotFood=true;
			wi.updateScreen();
		}
	}
	
}
