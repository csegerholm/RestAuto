// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package testing;

import messageController.Message;

/**
 * Tests the Message fromString method in Message class.
 * @author cms549
 */
public class MessageTesting {
	
	
	
	
	public static void main(String args[]){
		String m="h3*w0*1";
		Message mess = Message.fromString(m);
		if(mess.senderPosition=='h' && mess.senderEmpID==3 && mess.receiverEmpID==0 && mess.content.equals("1")){
			System.out.println("Message From String Unit Test: PASSED");
			return;
		}
		System.out.println("Sender Position: Expected: h    Got:"+mess.senderPosition);
		System.out.println("Sender Employee ID: Expected: 3    Got:"+mess.senderEmpID);
		System.out.println("Receiver Position: Expected: w  Got:"+mess.receiverPosition);
		System.out.println("Receiver Employee ID: Expected: 0    Got:"+mess.receiverEmpID);
		System.out.println("Content: Expected:1    Got:"+mess.content);
		
		System.out.println("Message From String Unit Test: FAILED");
	}
}

