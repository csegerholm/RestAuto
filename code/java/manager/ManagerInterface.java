// written by: Christina Segerholm
// tested by: Christina Segerholm and Athira Haridas
// debugged by: Christina Segerholm
package manager;

import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JFrame;

import configuration.Configure;
import loggingIn.LogInScreen;
import loggingIn.TabletApp;
import messageController.Message;

/**
 * Holds the list of messages that all of the employees sent the manager.
 * Controls this list of messages.
 * @author cms549
 *
 */
public class ManagerInterface {
	
	private final static String MCdomainName = Configure.getDomainName("MessageController");
	private final static int MCportNumber = Configure.getPortNumber("MessageController");
	
	/**
	 * List of messages that manager has recieved
	 * End of list is most recent
	 */
	public LinkedList<Message> listOfMessages;
	
	/**
	 * This will be used to send messages to the MC
	 */
	public ManagerMessageSender sender;
	
	private JFrame frame;
	
	/**
	 * Employee ID - this will be used to ID the tablet for the Message Controller
	 */
	long empID;
	
	/**
	 * Employee Name- this will be displayed on the screen
	 */
	String name;
	
	
	ManagerScreen manScreen;
	
	LogInScreen loginPanel;

	
    /**
     * Constructor
     * @param lp= login panel 
     */
	public ManagerInterface(LogInScreen lp){
		loginPanel=lp;
		name=lp.empName;
		empID = lp.currIDEntry;
		listOfMessages = new LinkedList<Message>();
		this.frame=lp.frame;
		
		//set up MC
		setUpMessageController();
		
		
		//create waiter screen for list of tickets
		manScreen = new ManagerScreen(this);
		//set the screen to the waiter panel
		this.frame.setContentPane(manScreen);
		frame.revalidate();
		//generateMessages();
	}
	
	/**
	 * Returns when manager logs out.
	 * Sends a message to the MC to alert it that the manager is logging out.
	 */
	public void logOut(){
		if(sender!=null)
		sender.sendMessage(new Message('X',-1, "Logging out"));
		TabletApp.logOut(loginPanel);
	}

	/**
	* Updates the current panel - makes them redraw all the buttons
	 */
	public void updateScreen() {
		manScreen.updateScreen();
		frame.revalidate();
	}
	
	/**
	 * Sets up the socket to the MC
	 */
	private void setUpMessageController() {
		Socket listener;
		try {
			listener = new Socket(MCdomainName, MCportNumber);
			Thread t= new ManagerMessageListener(listener, this);
			t.start();
			sender = new ManagerMessageSender(listener,empID);
			sender.start();
			sender.sendMessage(new Message('L',-1, "Logging in"));
			
		} catch (Exception e) {
			System.out.println("Problem setting up Manager MC.");
		}
		
	}
	
	/**
	 * Delete's the message at the given index in the message list
	 * Caller should be sure to check the index is valid.
	 * @param index - index of message to be deleted in the list of messages
	 */
	public void deleteMessage(int index){
		listOfMessages.remove(index);
		updateScreen();
	}
	
	/**
	 * Sends a mass notification to all servers, hosts, and chefs
	 * @param content
	 */
	public void sendMassNotification(String content){
		sender.sendMessage(new Message('h',-1, content));
		sender.sendMessage(new Message('c',-1, content));
		sender.sendMessage(new Message('w',-1, content));
	}

	/**
	 * Adds this message to the list of messages
	 * @param m - message to be added
	 */
	public void addMessageToList(Message m) {
		if(m==null){
			return;
		}
		listOfMessages.add(m);
		updateScreen();
		
	}
	
	/**
	 * For Testing
	 */
	public void generateMessages(){
		
		Message m =new Message();
		m.content= "Waiter needs help at Table 6.";
		addMessageToList(m);
		Message m1 =new Message();
		m1.content= "Host needs help at host stand.";
		addMessageToList(m1);
	}
	
	/**
	 * Getter for port number
	 * @return portNumber 
	 */
	public int getMCPortNumber(){
		return MCportNumber;
	}
	

	/**
	 * Getter for empID
	 * @return empID 
	 */
	public long getEmpID(){
		return empID;
	}
	
}
