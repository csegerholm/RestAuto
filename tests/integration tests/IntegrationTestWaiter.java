// written by: Annie Antony
// tested by: Annie Antony
// debugged by: Annie Antony
package testing;

import javax.swing.JFrame;
import dataBaseC.Dish;
import dataBaseC.Ticket;
import loggingIn.LogInScreen;
import waiter.WaiterInterface;
/**
 * 
 * @author Annie Antony
 * 
 * Before running these tests, please run the following:
 * 1.DatabaseAController.java
 * 2.DatabaseCController.java
 * 3.MessageController.java
 *
 */

public class IntegrationTestWaiter {
	
	public static boolean testLoadMenu(WaiterInterface w){
		boolean loadMenuTest=w.loadMenu();
		System.out.println("Testing loadMenu");
		System.out.println("expected value: TRUE, return value is "+loadMenuTest);	
		if (loadMenuTest==true){
			System.out.println("TEST PASSED");
			return true;
		}
		else {
			System.out.println("TEST FAILED");
			return false;
		}
	}
	
	public static boolean testaddDishtoTicket(WaiterInterface w){
		Dish tempdish = new Dish("Pancake",3.99,"Appetizer");
		boolean tester = w.addDishToTicket(tempdish);
		if(tester){
			System.out.println("The dish is added to the ticket!");
			return true;
		}else{
			System.out.println("Not added :(");
			return false;
		}
		
	}

	public static void main(String[] args) {
		JFrame frame=new JFrame();
		LogInScreen lp=new LogInScreen(frame);
		WaiterInterface w=new WaiterInterface(lp);
		//Integration Test 2 of functions to add dish to the Ticket
				//Step 1: Load all the menu items
				boolean func1=testLoadMenu(w);
				w.currTicket = new Ticket("Annie",3, 4);
				w.oneTickScreen.currTicket = w.currTicket;
				//Step 2: Add the dish to the Ticket
				boolean func2=testaddDishtoTicket(w);
			
				if (func1 && func2){
					System.out.println("Integration Test 2 PASSED");
				}
				else{
					System.out.println("Integration Test 2 FAILED");
				}

	}

}
