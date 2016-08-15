// written by: Christina Segerholm Annie Antony and Nishtha Sharma
// tested by: Annie Antony and Nishtha Sharma
// debugged by: Annie Antony and Nishtha Sharma
package chef;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import configuration.Configure;
import dataBaseC.Dish;
import dataBaseC.Ticket;
import loggingIn.LogInScreen;
import loggingIn.TabletApp;
import messageController.Message;

/**
 * Controls what screen is shown to chef. Holds all data that is shown to Chef.
 * @author cms549
 *
 */
public class ChefInterface {
	private final static String MCdomainName = Configure.getDomainName("MessageController");
	private final static int MCportNumber = Configure.getPortNumber("MessageController");
	
	public static Gson jsonConverter = new Gson();
	/**
	 * Employee ID - this will be used to ID the tablet for the Message Controller
	 */
	long empID;
	/**
	 * Employee Name- this will be displayed on the screen
	 */
	String name;
	/**
	 * Ticket Number to give next incoming ticket
	 */
	static long currTicketNumber =0;
	/**
	 * HashMap that maps the ticket number to the ticket 
	 */
	HashMap<Long, Ticket>ticketLookup;
	/**
	 * Sends messages to the Message controller
	 */
	public ChefMessageSender sender;
	
	/**
	 * Ticket Queues that the chef can view - separated based on status of preparation
	 */
	public ArrayList<Long> ticketQueueUnstarted;
	public ArrayList<Long> ticketQueuesemiStarted;
	public ArrayList<Long> ticketQueueStarted;
	public ArrayList<Long> ticketQueueFinished;
	/**
	 * The ticket list screen that the chef views
	 */
    ChefPanel chefPanel;
    /**
     * Once the chef opens a ticket, the screen that shows the dishes of the ticket
     */
	public ChefOneTickScreen oneTickScreen;
    LogInScreen loginPanel;
    /**
	 * Constructor
	 * @param lp - log in screen 
	 */
	public ChefInterface(LogInScreen lp){
		loginPanel=lp;
		name= lp.empName;
		empID = lp.currIDEntry;
		ticketQueueUnstarted = new ArrayList<Long>();
		ticketQueuesemiStarted = new ArrayList<Long>();
		ticketQueueStarted = new ArrayList<Long>();
		ticketQueueFinished = new ArrayList<Long>();
		ticketLookup = new HashMap<Long, Ticket>();
		
		setUpMessageController();
		
		generateTickets();
		chefPanel = new ChefPanel(this);
		lp.frame.setContentPane(chefPanel);
		lp.frame.revalidate();
		oneTickScreen = new ChefOneTickScreen(this);
		
		
	}

	
	/**
	 * Function used to read the incoming tickets from the waiters and places them in the queue that the chef can view.
	 * @param ticket - ticket the waiter sends to the chef
	 */
	public void chefTicketListener(Ticket ticket){
		if(ticket!=null){
			//set up the index for this ticket
			ticket.ticketNumber = currTicketNumber;
			System.out.println("Adding ticket "+currTicketNumber);
			//add the ticket to the end of the unstarted list since it is unstarted
			ticketQueueUnstarted.add(currTicketNumber);
			ticketLookup.put(currTicketNumber, ticket);
			currTicketNumber++;
			if(chefPanel!=null)
				chefPanel.updateScreen();
		}
	}
	    

	/**
	 * Decrements the amount of each item in the inventory by the proper amount of ingredients for this dish.
	 * @param dish - dish that chef makes 
	 */
	public void decrementInventoryForDish(Dish dish) {
			//Send message to DB C to decrement this dish
			//message should start with a D and end with the name of the dish (and thats it)
		}
	

	/**
	 *Changes the location of the ticket based on the status of it. ie: takes ticket off of unstarted and adds it to semi started
	 * @param oldstatus - status before modification
	 * @param t - the ticket with the status modified 
	 */
	public void changeTicketLocation(char oldstatus, Ticket t) {
		if(oldstatus == 'u'){
			ticketQueueUnstarted.remove(ticketQueueUnstarted.indexOf(t.ticketNumber));
		}
		else if(oldstatus=='s'){
			ticketQueuesemiStarted.remove(ticketQueuesemiStarted.indexOf(t.ticketNumber));
		}
		else if(oldstatus =='S'){
			ticketQueueStarted.remove(ticketQueueStarted.indexOf(t.ticketNumber));
		}
		else{//finished
			ticketQueueFinished.remove(ticketQueueFinished.indexOf(t.ticketNumber));
		}
		
		char newStatus = t.status;
		if(newStatus == 'u'){
			ticketQueueUnstarted.add(t.ticketNumber);
		}
		else if(newStatus=='s'){
			ticketQueuesemiStarted.add(t.ticketNumber);
		}
		else if(newStatus =='S'){
			ticketQueueStarted.add(t.ticketNumber);
		}
		else{//finished
			ticketQueueFinished.add(t.ticketNumber);
		}
	}
	/**
	 * Sets up the Message Controller and alerts it that chef is logged in.
	 */
	private void setUpMessageController() {
		Socket listener=null;
		try {
			listener = new Socket(MCdomainName, MCportNumber);
			Thread t= new ChefMessageListener(listener, this);
			t.start();
			sender = new ChefMessageSender(listener,empID);
			sender.start();
			sender.sendMessage(new Message('L',-1, "Logging In"));
			
		} catch (Exception e) {
			System.out.println("Chef: Disconnected from MC.");
			try {
				listener.close();
			} catch (IOException e1) {}
		}
		
		
	}
	/**
	 * Switches from list of tickets screen to one open ticket screen
	 * @param ticketNumber - the ticket number of ticket you wish to open
	 */
	public void openTicketScreen(long ticketNumber) { //opens one Ticket on the screen 
		Ticket currTicket = ticketLookup.get(ticketNumber);
		//System.out.println("Ticket openning = "+currTicket.toStringForChef());
		oneTickScreen.setTicket(currTicket);
		loginPanel.frame.setContentPane(oneTickScreen);
		loginPanel.frame.revalidate();
		
	}
	/**
	 * Switches the screen from an open ticket screen to the list of tickets screen.
	 */
	public void backToMainScreen(){
		loginPanel.frame.setContentPane(chefPanel);
		chefPanel.updateScreen();
		loginPanel.frame.revalidate();
	}

	/**
	 * Used for testing
	 */
	public void generateTickets(){
        Dish newdish =  new Dish("Chicken Marsala",8.99, "entree");
        Dish newdish1 =  new Dish("Cheesecake",12.99, "dessert");
        Dish newdish2 =  new Dish("Steak",18.99, "entree");
        Dish newdish3 =  new Dish("Buffalo Chicken",6.99, "appetizer");
        ArrayList<Dish> dishlist = new ArrayList<Dish>();
        dishlist.add(newdish);
        dishlist.add(newdish1);
        dishlist.add(newdish2);
        dishlist.add(newdish3);
        Ticket ticket = new Ticket("Emma Ruossos",14,0);
        ticket.listOfDishes = dishlist;
        chefTicketListener(ticket);
        
    }

	/**
	 * Returns upon logging out. 
	 * Sends a message to the Host and MC to notify that chef is logging out.
	 */

	public void logOut() {
		if(sender!=null){
			sender.sendMessage(new Message('X',-1, "Log out"));
			//System.out.println("Sender is not null: Sent message");
		}
		TabletApp.logOut(loginPanel);
	}

	/**
	 * Sends a notification to the manager
	 */
	public void notifyManager() {
		sender.sendMessage(new Message('m',-1, name+" needs help in the kitchen."));
		updateScreen();
	}

	/**
	 * Adds this notification to the chef screen
	 * @param message - notification
	 */
	public void addNotification(String message){
		chefPanel.makeNotification(message);
	}

	/**
	 * updates the screen to the main chef screen
	 */
	public void updateScreen() {
		chefPanel.updateScreen();
		
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
	

	/**
	 * Getter for current ticket number
	 * @return currTicketNumber 
	 */
	public long getCurrTicketNumber(){
		return currTicketNumber;
	}
	
	/**
	 * Getter for ticket lookup
	 * @return ticketLookup 
	 */
	public HashMap<Long, Ticket> getTicketLookup(){
		return ticketLookup;
	}
	
}
