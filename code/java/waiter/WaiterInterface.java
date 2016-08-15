// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package waiter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JFrame;

import com.google.gson.Gson;

import configuration.Configure;
import dataBaseC.Dish;
import dataBaseC.Menu;
import dataBaseC.Ticket;
import loggingIn.LogInScreen;
import loggingIn.TabletApp;
import messageController.Message;

/**
 * Controls what screen the waiter sees. 
 * Holds the waiters current tickets.
 * @author cms549
 *
 */
public class WaiterInterface {
	
	private final static String MCdomainName = Configure.getDomainName("MessageController");
	private final static int MCportNumber = Configure.getPortNumber("MessageController");
	
	private Gson jsonConverter;
	/**
	 * Sends messages to the Message controller
	 */
	public WaiterMessageSender sender;
	/**
	 * Sends messages to DB C - used for ticket auditing.
	 */
	public DataBaseCSender DBCSender;
	
	JFrame frame;
	
	/**
	 * Employee ID - this will be used to ID the tablet for the Message Controller
	 */
	long empID;
	
	/**
	 * Employee Name- this will be displayed on the screen
	 */
	String name;
	
	
	/**
	 * current ticket open, null if none is open
	 */
	public Ticket currTicket;
	
	/**
	 * List of the tickets this waiter is in charge of.
	 * Maps the table number to the appropriate tickets.
	 */
	HashMap<Integer, Ticket> listOfTickets;
	
	/**
	 * Menu of dishes.
	 */
	Menu menu;
	
	/**
	 * Holds list of manager's ids used to validate a coupon
	 */
	ArrayList<Integer> listOfManagerIds;
	
	WaiterTickListScreen ticketListScreen;
	public WaiterOneTicketScreen oneTickScreen;
	LogInScreen loginPanel;
	private KeyPadScreen keyPadScreen;
	
	/**
	 * Constructor
	 * @param lp - log in screen 
	 */
	public WaiterInterface(LogInScreen lp) {
		loginPanel=lp;
		name=lp.empName;
		empID = lp.currIDEntry;
		jsonConverter = new Gson();
		frame = loginPanel.frame;
		listOfTickets = new HashMap<Integer, Ticket>();
		
		//if problem loading menu return right away
		if (!loadMenu()){
			return;
		}
		
		//set up MC
		setUpMessageController();
		
		if(empID==0)
			generateTickets();
		
		loadManagers();
		
		
		
		//create waiter screen for list of tickets
		ticketListScreen = new WaiterTickListScreen(this);
		//set the screen to the waiter panel
		frame.setContentPane(ticketListScreen);
		frame.revalidate();
		
		oneTickScreen = new WaiterOneTicketScreen(this);
		keyPadScreen = new KeyPadScreen(this);
	}
	
	/**
	 * Fills in the list of managers
	 */
	private void loadManagers() {
		listOfManagerIds = new ArrayList<Integer>();
		listOfManagerIds.add(1);
		//later we will grab this from DB A
	}

	/**
	 * Returns upon logging out. 
	 * Sends a message to the Host and MC to notify that waiter is logging out.
	 */
	public void logOut(){
		//let the host know you are logging out
		try {
			DBCSender.sock.close();
		} catch (IOException e) {}
		sender.sendMessage(new Message('X',-1, "Log out"));
		TabletApp.logOut(loginPanel);
	}

	/**
	 * Loads menu from database C.
	 * @return true on success, false on failure
	 */
	public boolean loadMenu() {
		String DChost = Configure.getDomainName("DatabaseCController");
		int DCPortNum = Configure.getPortNumber("DatabaseCController");
		Socket sock=null;
		try {
			sock = new Socket(DChost, DCPortNum);
			DataInputStream in = new DataInputStream(sock.getInputStream());
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			String logInToMC = "M";
			out.writeUTF(logInToMC);
			String jmenu = in.readUTF();
			menu = jsonConverter.fromJson(jmenu, Menu.class);
			DBCSender = new DataBaseCSender(sock, out);
			DBCSender.start();
			new DataBaseCListener( in, this).start();

		} catch (Exception e) {
			System.out.println("ERROR: can't load menu");
			return false;
		}
		return true;
		
	}
		
	/**
	 * Sets up the Message Controller and alerts it that waiter is logged in.
	 */
	private void setUpMessageController() {
		Socket listener=null;
		try {
			listener = new Socket(MCdomainName, MCportNumber);
			Thread t= new WaiterMessageListener(listener, this);
			t.start();
			sender = new WaiterMessageSender(listener,empID);
			sender.start();
			sender.sendMessage(new Message('L',-1, "Logging In"));
			
		} catch (Exception e) {
			System.out.println("Problem setting up Waiter MC.");
		}
		
	}

	/**
	 * Will add a GiftCard or Coupon to the ticket and price
	 * @param price - price of gift card or amt to take off with coupon
	 */
	public void addGCorCoupon(double price){
		if(price==0){
			return;
		}
		else if(price>0){
			//GIFT CARD
			Dish gc = new Dish("Gift Card", price, null);
			gc.changeStatus('c');//c means coupon or giftcard
			currTicket.listOfDishes.add(gc);
			currTicket.price=currTicket.price + gc.price;
		}
		else{
			//COUPON
			Dish comp = new Dish("Coupon", price, null);
			comp.changeStatus('c');
			currTicket.listOfDishes.add(comp);
			currTicket.price=currTicket.price + comp.price;
		}
		
	}
	
	/**
	 * Adds a the given dish to the ticket that is currently selected.
	 * Caller should make sure currTicket field is not null.
	 * @param dish - Dish of dish you wish to add
	 */
	public boolean addDishToTicket(Dish dish) {
		currTicket.addDishToTicket(dish.makeCopyOfDish());
	     updateScreen();
		return true;
		
	}

	/**
	 * Removes a the dish at the given index on the ticket that is currently selected.
	 * Caller should make sure currTicket field is not null and index is valid.
	 * @param indexInTicket = index of the dish in the current ticket
	 */
	public boolean removeDishFromTicket(int indexInTicket) {
		currTicket.removeDishFromTicket(indexInTicket);
		return true;
	}

	/**
	 * Adds comment com to the dish at index ind on the curr ticket
	 * @param ind = index of the dish in the current ticket
	 */
	public void addComment(int ind, String com) {
		Dish d = currTicket.listOfDishes.get(ind);
		if(d.getStatus()!='c')//can't add a comment to a gc or coupon
			d.comments.add(com);
	}
	
	
	/**
	 * Switches from list of tickets screen to one open ticket screen
	 * @param tableNumber - the table number of ticket you wish to open
	 */
	public void openTicketScreens(int tableNumber) {
		currTicket = listOfTickets.get(tableNumber);
		oneTickScreen.setTicket(currTicket);
		frame.setContentPane(oneTickScreen);
		frame.revalidate();
		
	}
	
	/**
	 * Switches the screen from an open ticket screen to the list of tickets screen.
	 */
	public void backToMainScreen(){
		currTicket = null;
		frame.setContentPane(ticketListScreen);
		ticketListScreen.updateScreen();
		frame.revalidate();
	}
	
	/**
	 * Sends a ticket to printer
	 * @param t- Ticket to print
	 */
	public void printTicket(Ticket t){
		//connect to printer and send the ticket as a string to print
		//Printer.print(t.toStringForDBC());
	}
	
	/**
	 * Brings you back to the ticket screen you were on before opening the keypad
	 */
	public void backToOpenTicketScreen(){
		frame.setContentPane(oneTickScreen);
		oneTickScreen.updateScreen();
		frame.revalidate();
	}
	
	/**
	 * Switches the screen to the keypad screen
	 * @param type = Type of question you need the screen for - 'g' = giftCard Amount, 'm'= manager id, 'c'= coupon amount, 'n' = notify waiter
	 */
	public void toKeyPadScreen(char type){
		//set up keyPadScreen
		keyPadScreen.type=type;
		frame.setContentPane(keyPadScreen);
		keyPadScreen.updateScreen();
		frame.revalidate();
	}

	/**
	 * Sends message to manager
	 * @param currTicket2 - ticket object that holds the table number
	 * @param message - message to send to manager
	 */
	public void notifyManager(Ticket currTicket2, String message) {
		sender.sendMessage(new Message('m',-1, currTicket2.waiterName+"@ Table: "+currTicket2.tableNumber+": "+message));
		updateScreen();
	}
	
	/**
	 * Sends ticket to Chef and marks the sent items
	 * @param t - ticket to send
	 */
	public void sendTicket(Ticket t){
		sender.sendMessage(new Message('c',-1,t.toStringForChef() ));
	}
	
	/**
	 * Used for testing
	 */
	public void generateTickets(){	
		Ticket T1=new Ticket(name,1,empID);//table 1, waiter id=1
		Ticket T2=new Ticket( name ,14,empID);//table 14, waiter id=1
		T2.hotFood=true;
		
		listOfTickets.put(1,T1);
		listOfTickets.put(14,T2);
	}

	
	/**
	 * Adds a notification on current screen by calling another method in panel
	 * @param content - content to post on the screen
	 */
	public void addNotification(String content) {
		if(keyPadScreen.type !='0'){
			keyPadScreen.makeNotification(content);
		}
		else if(currTicket==null){
			ticketListScreen.makeNotification(content);
		}
		else{
			oneTickScreen.makeNotification(content);
		}
	}


	/**
	 * Updates the current panel - makes them redraw all the buttons
	 */
	public void updateScreen() {
		if(keyPadScreen.type!='0'){
			keyPadScreen.updateScreen();
		}
		else if(currTicket==null){
			ticketListScreen.updateScreen();
		}
		else{
			oneTickScreen.updateScreen();
		}
		frame.revalidate();
	}

	/**
	 * This is called when a table has paid and server clicks the paid button.
	 * It alerts host and take this ticket off the list.
	 * @param tableNumber - table number of table that just paid
	 */
	public void paid(int tableNumber) {
		sender.sendMessage(new Message('h',-1, "P"+tableNumber));
		if(listOfTickets.get(tableNumber).listOfDishes.size()!=0)
			DBCSender.sendTicket(listOfTickets.get(tableNumber));
		listOfTickets.remove(tableNumber);
		backToMainScreen();
	}
	
	/**
	 * This is called when a table requests their waiter.
	 * The host interface will notify the right waiter.
	 * @param tableNumber - table number of table that asked for their waiter
	 */
	public void notifyWaiter(int tableNumber) {
		sender.sendMessage(new Message('h',-1, "N"+tableNumber));
	}
	

/**
 * This is called when low inventory is met and chef can no longer make these dishes.
 * Removes the array of dish names from the menu.
 * @param dishes - array of dish names to be removed from menu.
 */
	public void removeLowInventoryDishes(String[] dishes) {
		//for each dish that we need to mark as low inventory
		for(int i =0; i<dishes.length;i++){
			String dishName = dishes[i];
			Iterator<String> it = menu.menu.keySet().iterator();
			//check each sub category for that dish
			while(it.hasNext()){
				String category = it.next();
				HashMap<String, Dish> hm = menu.menu.get(category);
				if(hm.containsKey(dishName)){
					hm.remove(dishName);
					break;
				}
			}
		}
		updateScreen();
		
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
	 * Getter for menu
	 * @return menu 
	 */
	public Menu getMenu(){
		return menu;
	}
	/**
	 * Getter for listOfTickets
	 * @return listOfTickets 
	 */
	public HashMap<Integer, Ticket> getListOfTickets(){
		return listOfTickets;
	}
	
}
