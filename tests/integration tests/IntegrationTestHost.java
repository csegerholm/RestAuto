// written by: Athira Haridas
// tested by: Athira Haridas
// debugged by: Athira Haridas
package testing;

import javax.swing.JFrame;

import loggingIn.LogInScreen;
import databaseB.Table;
import host.HostInterface;

/**
 * 
 * @author Athira
 * 
 * Before running these tests, please run the following:
 * 1.DatabaseAController.java
 * 2.DatabaseBController.java
 * 3.MessageController.java
 *
 */
public class IntegrationTestHost {
	
	public static boolean testLoadTables(HostInterface h){
		boolean loadTablesTest=h.loadTables();
		System.out.println("Testing loadTables()");
		System.out.println("expected value: TRUE, return value is "+loadTablesTest);	
		if (loadTablesTest==true){
			System.out.println("TEST PASSED");
			return true;
		}
		else {
			System.out.println("TEST FAILED");
			return false;
		}
		
	}

	
	public static boolean testSeat(HostInterface h){
		String nameofWaiter="Emma Roussos";
		int tableNum=1;
		Table t=h.allTables.get(tableNum);
		h.allTables.put(10, t);
		System.out.println("Testing seat():");
		char initStatus=t.status;
		System.out.println("Initial Status of table 1 Expected value 'r' observed value: "+initStatus);
		boolean retval=h.seat(nameofWaiter,tableNum);
		char finalStatus=t.status;
		if (!retval){
			System.out.println("seat() TEST FAILED: initial status is not 'r'");
		}
		else{
			System.out.println("seat() final status of Table 10 is: "+finalStatus);
		}
		if (finalStatus=='s'){
			System.out.println("TEST PASSED");
			return true;
		}
		else{
			System.out.println("TEST FAILED");
			return false;
		}
		
		//System.out.println("Final Status of table 1: Expected value 's' observed value: "+finalStatus);
	}
	
	
	public static void main(String args[]){
		JFrame frame=new JFrame();
		LogInScreen lp=new LogInScreen(frame);
		HostInterface h=new HostInterface(lp);
		
		
		
		//Integration Test of functions to change the table status from READY to SEATED
		//Step 1: Load all tables
		boolean func1=testLoadTables(h);
		//Step 2: Change status to seated
		boolean func2=testSeat(h);
		if (func1 && func2){
			System.out.println("Integration Test Host PASSED");
		}
		else{
			System.out.println("Integration Test Host FAILED");
		}
		
	}
	

}

