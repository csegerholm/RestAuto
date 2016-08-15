// written by: Christina Segerholm
// tested by: Christina Segerholm and Athira Haridas
// debugged by: Christina Segerholm
package host;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.google.gson.Gson;

import configuration.Configure;
import databaseB.Table;
import databaseB.TableList;
import loggingIn.LogInScreen;
import loggingIn.TabletApp;
import messageController.Message;

/**
 * Controls the jpanels being displayed and all the data for the host.
 * The host interface keeps track of all of the tables in the restaurant.
 * @author cms549
 *
 */
public class HostInterface {
	
	//Used to get Message Controller info
	private final static String MCdomainName = Configure.getDomainName("MessageController");
	private final static int MCportNumber = Configure.getPortNumber("MessageController");
	
	/**
	 * Used to convert java objects to string and vice versa.
	 */
	private Gson jsonConverter;
	/**
	 * Used to send messages to message controller
	 */
	public HostMessageSender sender;
	
	/**
	 * Employee ID - this will be used to ID the tablet for the Message Controller
	 */
	long empID;
	
	/**
	 * Employee Name- this will be displayed on the screen
	 */
	String name;
	
	
	/**
	 * J panel for the host that displays tables
	 */
	HostTableScreen tableScreen;
	
	/**
	 * Hash map links integer (table #) to its table object - holds all the tables in the restaurant
	 */
	public HashMap<Integer, Table> allTables; 
	
	/**
	 * list of all the ready tables in order they will be displayed
	 */
	ArrayList< Integer> readyTables; 
	/**
	 * list of tables that are seated 
	 */
	ArrayList< Integer> seatedTables;
	/**
	 * list of tables that need to be cleaned
	 */
	ArrayList< Integer> paidTables;
	
	/**
	 * HashMap that maps the name of the waiter to their ID.
	 */
	HashMap<String, Integer> listOfWaiters;
	
	/**
	 * HashMap that maps the name of the waiter to the number of tables he currently has.
	 */
	HashMap<String, Integer> waiterTotalTables;
	
	/***
	 * HashMap that maps table number to the waiter who has it
	 */
	HashMap<Integer,String> waiterOfTable;
	
	LogInScreen loginPanel;

	
    /**
     * Constructor
     * @param lp= login panel 
     */
	public HostInterface(LogInScreen lp){
		loginPanel=lp;
		name=lp.empName;
		empID = lp.currIDEntry;
		jsonConverter = new Gson();
		waiterTotalTables = new HashMap<String, Integer>();
		listOfWaiters = new HashMap<String,Integer>();
		waiterOfTable=new HashMap<Integer,String>();
		if(!loadWaiters()){
			logOut();
			return;
		}
		
		
		//if problem loading menu return right away
		if (!loadTables()){
			logOut();
			return;
		}
		readyTables = new ArrayList<Integer>();
		seatedTables = new ArrayList<Integer>();
		paidTables = new ArrayList<Integer>();
		
		Iterator<Integer> it = allTables.keySet().iterator();
		while( it.hasNext()){
			Integer key= it.next();
			Table table = allTables.get(key);
			table.changeStatus('r');
			readyTables.add(key);
		}
		setUpMessageController();
		
		tableScreen = new HostTableScreen(this);
		lp.frame.setContentPane(tableScreen);
		updateScreen();
	}

	/**
	 * Loads the list of tables from Data base B
	 * @return true on success, false on failure
	 */
	public boolean loadTables() {
		String DBhost = Configure.getDomainName("DatabaseBController");
		int DBPortNum = Configure.getPortNumber("DatabaseBController");
		Socket sock=null;
		try {
			sock = new Socket(DBhost, DBPortNum);
			DataInputStream in = new DataInputStream(sock.getInputStream());
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			String logInToMC = "T";
			out.writeUTF(logInToMC);
			String jtables = in.readUTF();
			TableList tab = jsonConverter.fromJson(jtables, TableList.class);
			
			allTables = tab.hm;
			sock.close();
			
		} catch (Exception e) {
			System.out.println("ERROR: can't load tables");
			e.printStackTrace();
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
			Thread t= new HostMessageListener(listener, this);
			t.start();
			sender = new HostMessageSender(listener,empID);
			sender.start();
			sender.sendMessage(new Message('L',-1, "Logging In"));
			
		} catch (Exception e) {
			System.out.println("Problem setting up Host MC.");
		}
		
	}
	
	/**
	 * Sends a notification to the manager
	 */
	public void notifyManager() {
		sender.sendMessage(new Message('m',-1, name+" needs help at host stand."));
		updateScreen();
	}
	
	/**
	 * notify waiter
	 */
	public void notifyWaiter(Message m){
		sender.sendMessage(m);
	}

	/**
	 * Seat the table number with this server
	 * @param waiterName
	 * @param tableNumber
	 * @return true on success and false on fail.
	 */
	public boolean seat(String waiterName, int tableNumber){
		Table t = allTables.get(tableNumber);
		if (!(t.status=='r')){ //if table is not ready
			return false;
		}
		t.status = 's';//change status to seated
		for(int i =0; i<readyTables.size(); i++){
			if(readyTables.get(i) == tableNumber){
				readyTables.remove(i);//take table off the ready list
				seatedTables.add(tableNumber);//add it to seated list
				break;
			}
		}
		int currTables=waiterTotalTables.get(waiterName);
		currTables++;
		waiterTotalTables.put(waiterName, currTables);
		waiterOfTable.put(tableNumber, waiterName);
		sendSeated(listOfWaiters.get(waiterName), tableNumber);
		updateScreen();
		return true;
	}
	
	/**
	 * Sends a message to the waiter whos table you just sat.
	 * @param waiterId - id of waiter you wish to send message to
	 * @param tableNumber - table you just sat 
	 */
	public void sendSeated(long waiterId, int tableNumber){
		sender.sendMessage(new Message('w',waiterId, "R"+tableNumber ));
	}
	
	/**
	 * Adds a notification on current screen by calling another method in panel
	 * @param content
	 */
	public void addNotification(String content) {
		tableScreen.makeNotification(content);
	}
	
	/**
	 * Keeps tablet in host interface screen until it logs out.
	 * Then sends log out message to MC.
	 */
	public void logOut(){
		if(sender!=null)
		sender.sendMessage(new Message('X',-1, "Log out"));
		TabletApp.logOut(loginPanel);
	}

	/**
	 * Updates the current panel - makes them redraw all the buttons
	 */
	public void updateScreen() {
		tableScreen.updateScreen();
		loginPanel.frame.revalidate();
	}

	/**
	 * Moves seated table to paid
	 * @param tableNumber - table number that paid
	 * @return true on success and false on fail.
	 */
	public boolean paid(int tableNumber) {
		Table t = allTables.get(tableNumber);
		if(t.status!='s'){
			return false;
		}
		t.status = 'p';
		for(int i=0; i<seatedTables.size();i++){
			int curr = seatedTables.get(i);
			if(curr == tableNumber){
				seatedTables.remove(i);
				paidTables.add(tableNumber);
				String waiterName=waiterOfTable.get(tableNumber);
				int currTables=waiterTotalTables.get(waiterName);
				currTables--;
				waiterTotalTables.put(waiterName,currTables);
				 break;
			}
		}
		updateScreen();
		return true;
		
	}

	/**
	 * Move a table that was just cleaned from paid into ready list
	 * @param tableNumber
	 * @return true on success and false on fail.
	 */
	public boolean cleaned(int tableNumber) {
		Table t = allTables.get(tableNumber);
		if(t.status!='p'){
			return false;
		}
		t.status = 'r';
		for(int i=0; i<paidTables.size();i++){
			int curr = paidTables.get(i);
			if(curr == tableNumber){
				 paidTables.remove(i); //remove table from paid list
	             readyTables.add(tableNumber) ;//add it to the paid list
	             break;
			}
		}
		
		updateScreen();
		return true;
		
	}

	/**
	 * Loads waiters and ids from database A.
	 * @return true on success, false on failure
	 */
	public boolean loadWaiters() {
		String DAhost = Configure.getDomainName("DatabaseAController");
		int DAPortNum = Configure.getPortNumber("DatabaseAController");
		Socket sock=null;
		try {
			sock = new Socket(DAhost, DAPortNum);
			DataInputStream in = new DataInputStream(sock.getInputStream());
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			String requestWaiters = "W";
			out.writeUTF(requestWaiters);
			String waiterAsString = in.readUTF();
			String[] waiter = waiterAsString.split(":");
			for(int i=0; i<waiter.length;i++){
				String idString = waiter[i].substring(0, waiter[i].indexOf(","));
				int id = Integer.parseInt(idString);
				String name = waiter[i].substring(waiter[i].indexOf(",")+1);
				listOfWaiters.put(name,id);
				waiterTotalTables.put(name, 0);
				
			}
			
			
			sock.close();

		} catch (Exception e) {
			System.out.println("ERROR: can't get waiter list");
			return false;
		}
		return true;
		
	}
	 /** 
	 * @param tableNumber
	 * @return the ID of the waiter who has this table
	 * returns -1 if waiter not found
	 */
	long getWaiterIdForTable(int tableNumber){
		String waiterName=waiterOfTable.get(tableNumber);
		long waiterID=-1;
		if (waiterName!=null){
			waiterID=listOfWaiters.get(waiterName);
		}
		return waiterID;
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
