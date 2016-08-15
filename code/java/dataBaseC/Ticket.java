// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package dataBaseC;

import java.util.ArrayList;

/**
 * Data structure to hold the ticket and the data associated with it.
 * Used by waiter and chef.
 * @author cms549
 */
public class Ticket {
	/**
	 * table number this order belongs to
	 */
	public int tableNumber;
	/**
	 * name of waiter this ticket is under
	 */
	public String waiterName;
	/**
	 * id of waiter this ticket belongs to
	 */
	public long waiterID;
	/**
	 * status of ticket: u=unstarted, s=semi started, S=started, f=finished
	 */
	public char status;
	/**
	 * List of dishes on the ticket
	 */
	public ArrayList<Dish> listOfDishes;
	
	/**
	 * unique id of ticket
	 */
	public long ticketNumber;
	
	/**
	 * total price of ticket
	 */
	public double price;
	
	//Counters used to figure out the status of the ticket
	public int amountOfDishesUnstarted;
	public int amountOfDishesStarted;
	public int amountOfDishesFinished;
	public int amountOfDishes;
	
	/**
	 * Used to keep track of recently sat tables for waiters
	 */
	public boolean recentlySat;
	
	/**
	 * Used to keep track of hot food tables for waiters
	 */
	public boolean hotFood;
	
	/**
	 * Used to mark the ticket as a priority ticket to the chef
	 */
	public  boolean priority;
	
	/**
	 * Creates a new empty ticket with the following
	 * @param waiterName - name of waiter for this ticket
	 * @param tableNum - table number the ticket is under
	 * @param waiterID - id of the waiter
	 */
	public Ticket(String waiterName,int tableNum, long waiterID){
		this.waiterName = waiterName;
		this.tableNumber=tableNum;
		this.waiterID= waiterID;
		this.status='u';
		this.listOfDishes= new ArrayList<Dish>();
		amountOfDishes = 0;
		amountOfDishesUnstarted = 0;
		this.price=0;
		amountOfDishesStarted = 0;
		amountOfDishesFinished = 0;
	}
	
	/**
	 * Creates empty ticket
	 */
	public Ticket() {
		this.status='u';
		this.listOfDishes= new ArrayList<Dish>();
		amountOfDishes = 0;
		amountOfDishesUnstarted = 0;
		amountOfDishesStarted = 0;
		amountOfDishesFinished = 0;
	}

	/**
	 * Adds dish to ticket and also updates price and status of ticket
	 * @param d
	 */
	public void addDishToTicket(Dish d){
		listOfDishes.add(d);
		amountOfDishesUnstarted= amountOfDishesUnstarted+1;
		amountOfDishes=amountOfDishes+1;
		this.price=this.price + d.price;
		updateStatusOfTicket();
		
	}
	
	/**
	 * Removes the dish at index i from the ticket and decrements the price
	 * @param indexOfDishInTickList
	 * @return
	 */
	public boolean removeDishFromTicket(int indexOfDishInTickList){
		if(indexOfDishInTickList<0 || indexOfDishInTickList>=listOfDishes.size()){
			return false;
		}
		
		Dish del =listOfDishes.get(indexOfDishInTickList);
		if(del==null || del.sent){
			return false;
		}
		this.price=this.price - del.price;
		if(del.getStatus() =='c'){
			listOfDishes.remove(indexOfDishInTickList);
			return true;
		}
		amountOfDishes= amountOfDishes-1;
		char s = del.getStatus();
		if(s=='u'){
			amountOfDishesUnstarted= amountOfDishesUnstarted-1;
		}
		else if(s=='s'){
			amountOfDishesStarted= 	amountOfDishesStarted-1;
		}
		else{
			amountOfDishesFinished=amountOfDishesFinished-1;
		}
		listOfDishes.remove(indexOfDishInTickList);
		updateStatusOfTicket();
		return true;
	}
	
	/**
	 * Looks through the dishes of the ticket and updates the status of the ticket accordingly
	 * @return old status of ticket
	 */
	public char updateStatusOfTicket(){
		//update the status of the ticket using the statuses of all the dishes
		char oldstatus = status;
		//change status here
		if(amountOfDishesUnstarted == amountOfDishes){
			status='u';//unstarted
		}
		else if(amountOfDishesUnstarted>0){
			status='s';//semi started
		}
		else if(amountOfDishesFinished==amountOfDishes){
			status='f';//finished
		}
		else{
			status='S';//started
		}
		return oldstatus;
	}

	/**
	 * Creates a string representation of this ticket that DB C will use to record expenses.
	 * Includes the waiter name and id, the table number, and the price of the ticket.
	 * @return the string format of the ticket
	 * Format:
	 * 	Waiter Name:John Waiter ID:123 Table Number:14 Price:$5.00
	 */
	public String toStringForDBC(){
		String stprice = ""+price;
		stprice = stprice.substring(0,stprice.indexOf('.')+3);
		String ans = "Waiter Name:"+waiterName+" Waiter ID:"+waiterID+" Table Number:"+tableNumber+" Price:$"+stprice;
		return ans;
	}
	
	/**
	 * Creates a string representation of this ticket that Chef will use to see dishes.
	 * Includes the priority waiter's name, id, the table number, and the list of dishes
	 * MARKS DISHES THAT ARE GETTING SENT AS SENT
	 * @return the string format of the ticket
	 * Format is as follows:
	 * 	P:WAITERNAME:WAITERID:TABLENUMBER:;DISHNAME1-,COMMENT11,COMMENT12;DISHNAME2-,COMMENT21;DISHNAME3-,COMMENT31,COMMENT32,COMMENT33
	 * 	N:WAITERNAME:WAITERID:TABLENUMBER:;DISHNAME1-,COMMENT11,COMMENT12;DISHNAME2-,COMMENT21;DISHNAME3-,COMMENT31,COMMENT32,COMMENT33
	 */
	public String toStringForChef(){
		String ans;
		if(priority){
			ans = "P:"+waiterName+":"+waiterID+":"+tableNumber+":";
		}
		else{
			ans = "N:"+waiterName+":"+waiterID+":"+tableNumber+":";
		}
		for(int i=0; i<listOfDishes.size();i++){
			Dish d = listOfDishes.get(i);
			//only send dishes that have not been sent
			if(!d.sent){
				ans = ans+";"+ d.name+"-";
				d.sent=true;
				if(d.comments!=null && d.comments.size()>0)
					for(int j =0; j< d.comments.size();j++){
						ans = ans+","+d.comments.get(j);
					}
				
				
			}
		}
		return ans;
	}

	/**
	 * Takes the string representation of this ticket and make a new ticket object for it
	 * @return a new Ticket
	 * Format is as follows:
	 * 	P:WAITERNAME:WAITERID:TABLENUMBER:;DISHNAME1-,COMMENT11,COMMENT12;DISHNAME2-,COMMENT21;DISHNAME3-,COMMENT31,COMMENT32,COMMENT33
	 * 	N:WAITERNAME:WAITERID:TABLENUMBER:;DISHNAME1-,COMMENT11,COMMENT12;DISHNAME2-,COMMENT21;DISHNAME3-,COMMENT31,COMMENT32,COMMENT33
	 */
	public static Ticket fromString(String tick){
		Ticket ans = new Ticket();
		String[] parts= tick.split(":");
		if(parts[0].charAt(0)=='P'){
			ans.priority = true;
		}
		ans.waiterName = parts[1];
		ans.waiterID = Integer.parseInt(parts[2]);
		ans.tableNumber = Integer.parseInt(parts[3]);
		String[] dishANDcomment = parts[4].split(";");
		//go through each dish
		for(int j=1; j<dishANDcomment.length; j++){
			String[] namelist = dishANDcomment[j].split("-");
			Dish d = new Dish(namelist[0],0,null);
			//add the name
			ans.listOfDishes.add(d);
			ans.amountOfDishes++;
			ans.amountOfDishesUnstarted++;
			if(namelist.length!=1){
				String[] eachComm = namelist[1].split(",");
				for(int c =1; c<eachComm.length; c++){
					d.comments.add(eachComm[c]);
				}
			}
			
		}
		
		return ans;
	}


}
