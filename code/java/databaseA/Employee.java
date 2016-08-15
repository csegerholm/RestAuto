// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package databaseA;

/**
 * Data structure to represent an Employee. Used by Database A to keep track of Employees.
 * @author cms549
 *
 */
public class Employee {
	
	/**
	 * Name of employee
	 */
	public String name;
	/**
	 * Unique ID of employee
	 */
	public Long id;
	/**
	 * Char to describe employees position: w=waiter, h=host, c=chef, m=manager, o=owner
	 */
	public char position;
	
	/**
	 * True if the employee is currently logged in. False if not.
	 */
	public boolean loggedIn;
	
	/**
	 * Create a new employee
	 * @param name - name of employee
	 * @param id - employee id
	 * @param position - position of employee (w=waiter, h=host, c=chef, m=manager, o=owner)
	 */
	public Employee(String name, long id, char position){
		this.name= name;
		this.id=id;
		this.position=position;
	}

}
