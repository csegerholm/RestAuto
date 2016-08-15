// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package testing;

import java.util.ArrayList;
import java.util.HashMap;

import dataBaseC.DatabaseCController;
import dataBaseC.Dish;
import databaseA.DatabaseAController;
import databaseA.Employee;
import databaseB.DatabaseBController;
import databaseB.Table;

/**
 * Integration test to test reading the files in Configuration Folder into DataBase Controllers
 * @author cms549
 */
public class DatabaseLoadFromFileTesting {
	
	
	
	/**
	 * Tests files that are read from Configuration Folder:
	 * 	employeeInfo.txt --> Loaded into DataBaseA
	 * 	tableNumbers.txt --> Loaded into DataBaseB
	 * 	menu.txt         --> loaded into DataBaseC
	 * @param args
	 */
	public static void main(String args[]){
			
		if(testEmployee() && testTables() && testMenu() ){
			System.out.println("Database Loading Integration Test: Passed.");
		}
		else{
			System.out.println("Database Loading Integration Test: Failed.");
		}

	}

	/**
	 * Tests DB A
	 */
	private static boolean testEmployee() {
		if(DatabaseAController.getEmployeeListForTesting()==null){
			DatabaseAController.main(new String[10]);//loads the employees
			ArrayList<Employee> list = DatabaseAController.getEmployeeListForTesting();
			//checks all employee's were loaded and the second one is athira
			if(list.size() == 5 && list.get(1).name.equals("Athira Haridas")){
				System.out.println("Employee Unit Test: Passed.");
				return true;
			}
			else{
				System.out.println("Employee Unit Test: Failed.");
				return false;
			}
		}
		else{
			System.out.println("Employee Unit Test: Could not perform, emplist is already initialized.");
			return false;
		}
		
	}
	
	/**
	 * Tests DB B
	 */
	private static boolean testTables() {
		if(DatabaseBController.listOfTables==null){
			DatabaseBController.main(new String[10]);//loads the tables
			HashMap<Integer, Table> list = DatabaseBController.listOfTables.hm;
			//checks all tables were loaded and table 2's type is type table
			if(list.size() == 7 && list.get(2).type=='t'){
				System.out.println("Table Unit Test: Passed.");
				return true;
			}
			else{
				System.out.println("Table Unit Test: Failed.");
				return false;
			}
		}
		else{
			System.out.println("Table Unit Test: Could not perform, tablelist is already initialized.");
			return false;
		}
		
	}
	
	/**
	 * Tests DB C
	 */
	private static boolean testMenu() {
		if(DatabaseCController.menu==null){
			DatabaseCController.main(new String[10]);//loads the tables
			HashMap<String, HashMap<String, Dish>> men = DatabaseCController.menu.menu;
			//checks all types of dishes were added
			if(men.size() == 4 && men.get("entree").get("Stuffed Shells").price==12.99){
				System.out.println("Menu Unit Test: Passed.");
				return true;
			}
			else{
				System.out.println("Menu Unit Test: Failed.");
				return false;
			}
		}
		else{
			System.out.println("Menu Unit Test: Could not perform, menu is already initialized.");
			return false;
		}
		
	}
	
	
}

