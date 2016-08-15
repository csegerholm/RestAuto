// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package databaseB;

/**
 * Table is a data structure that is used to represent a table.
 * Holds the information for the table, including the maximum occupancy, 
 * whether the table is a booth, the status of the table, and the name of the waiter 
 * who is serving it.
 * @author cms549
 *
 */
public class Table {
	
	/**
	 * Table number
	 */
	public int tableNumber;
	/**
	 * Status of table: r=ready, p=paid, s=seated
	 */
	public char status;
	/**
	 * Waiter who has this table
	 */
	public String waiter;
	/**
	 * How many people can sit at this table
	 */
	public int maxOccupancy;
	/**
	 * The type of table 'b' for booth, 't' for table
	 */
	public char type;
	
	
	
	/**
	 * Constructor - automatically initializes the status to ready
	 * @param tableNumber = table number of table to be created
	 * @param maxOccupancy = the amount of guests taht can sit at this table at once
	 */
	public Table(int tableNumber, int maxOccupancy, char type){
		this.tableNumber=tableNumber;
		status='r';
		this.maxOccupancy=maxOccupancy;
		this.type=type;
	}
	
	/**
	 * Seats the table with the waiter specified.
	 * @param waiter - name of the waiter who will get this table
	 * @return true on success, false if the table is not ready to be sat
	 */
	public boolean seat(String waiter){
		if(status!='r'){
			return false;
		}
		this.waiter=waiter;
		this.status='s';
		return true;
	}

	/**
	 * Changes the tables status to the specified status
	 * @param status
	 * @return true on success, false if invalid status entered
	 */
	public boolean changeStatus(char status){
		if(status=='r'||status=='p'||status=='s'){
			this.status=status;
			return true;
		}
		return false;
	}
	

}
