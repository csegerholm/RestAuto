// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package databaseB;

import java.util.HashMap;

/**
 * Data structure to hold the list of tables for the restaurant
 * @author cms549
 *
 */
public class TableList {
	/**
	 * Links Table Number to Table's info
	 */
	public HashMap<Integer, Table> hm;
	
	/**
	 * Constructor to make empty list of tables
	 */
	public TableList(){
		hm = new HashMap<Integer,Table>();
	}
}
