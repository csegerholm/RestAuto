// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package dataBaseC;

import java.util.HashMap;

/**
 * Data structure that holds the menu of dishes.
 * @author cms549
 */
public class Menu {
	/**
	 * First String maps category (Drink, App, Entree, Dessert) to a map
	 * Inner map maps dish name to dish
	 */
	public HashMap<String, HashMap<String,Dish>> menu;
	
	/**
	 * Constructor for empty menu.
	 */
	public Menu(){
		menu = new HashMap<String, HashMap<String,Dish>>();
	}
}
