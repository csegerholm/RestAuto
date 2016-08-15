// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package waiter;

import dataBaseC.Dish;

/**
 * Used by the Waiter Interface to print a x2 for dishes that are ordered again.
 * @author cms549
 *
 */
public class DishForTicket {
	
	
	/**
	 * was this dish already sent to the chef yet
	 */
	boolean sent;
	
	/**
	 * The amount of this dish ordered
	 */
	int amount;
	
	/**
	 * Price of dish * amount of this dishes ordered on this ticket
	 */
	double price;
	
	/**
	 * Name of dish
	 */
	String name;
	
	/**
	 * Creates DishForTicket with amount =1, and sent = d.sent, price = d.price, and name = d.name
	 * @param d - dish to copy over
	 */
	public DishForTicket(Dish d){
		amount =1;
		sent = d.sent;
		price = d.price;
		name = d.name;
	}
	
}
