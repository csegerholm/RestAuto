// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package testing;

import dataBaseC.Dish;
import dataBaseC.Ticket;

/**
 * Tests Ticket class's addDishToTicket(), toStringForChef(), and fromString() methods
 * @author cms549
 */
public class TicketToMessageConversion {
	
	
	
	/**
	 * Make a new ticket and send it into string then get it back from string
	 * Make sure the names, comments, waiter names, priority, table number, waiter ids all match
	 * @param args
	 */
	public static void main(String args[]){
		//to do
		Ticket t = new Ticket("Bob", 13, 4);
		t.priority = true;
		t.addDishToTicket(new Dish("Chicken", 10, null));
		t.listOfDishes.get(0).comments.add("To Go");
		String st = t.toStringForChef();
		Ticket t2 = Ticket.fromString(st);
		if(st.equals(t2.toStringForChef()) ){
			System.out.println("TicketToMessage Test: Passed");
		}
		else{
			System.out.println("TicketToMessage Test: Failed");
			System.out.println(st);
			System.out.println(t2.toStringForChef());
		}

	}
}

