// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package waiter;

import java.io.DataInputStream;

/**
 * Used to listen from messages from the message controller and decode them.
 * @author cms549
 */
public class DataBaseCListener extends Thread {
	
	/**
	 * Pointer back to its waiter interface
	 */
	private WaiterInterface wi;
	/**
	 * Input stream connected to DBC
	 */
	private DataInputStream in;

	/**
	 * Constructor
	 * @param listener - socket to listen to
	 * @param in 
	 * @param empID - waiter's employee id
	 * @param wI - waiter interface
	 */
	public DataBaseCListener(DataInputStream in, WaiterInterface wI) {
		wi=wI;
		this.in = in;
	}
	
	/**
	 * Listens for messages sent from the DB C - should just be low inventory
	 */
	public void run(){
		try{	
		//just keep listening
			while(true){
				String mes = in.readUTF();
				if(mes.length()==2){
					String second = in.readUTF();
					mes = mes +second;
				}
				decodeMessage(mes);
			}
			
			
		}catch (Exception e) {
			System.out.println("Waiter DBC Listener disconnected.");
		} 
		
	}

	/**
	 * Update the menu in case of low inventory.
	 * The message should have form LDISH1,DISH2
	 * 	so it should start with the letter L (for low inventory)
	 * 	then have the name of dish L then a comma then the name of dish 2
	 * @param m
	 */
	private void decodeMessage(String m) {
		//check if low inventory message
		if(m.charAt(0)=='L'){
			m = m.substring(1);
			String[] menuItems = m.split(",");
			wi.removeLowInventoryDishes(menuItems);
		}
	}
	
}
