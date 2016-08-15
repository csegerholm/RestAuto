// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package messageController;

/**
 * Data structure used to represent a message. Used for communication between tablets.
 * Consists of sender info which lets the MC know who is sending the message and
 * Receiver info which lets the MC know who to send the message to.
 * @author cms549
 */
public class Message {
	
	/**
	 * Sender info:
	 * position of employee
	 */
	public char senderPosition;
	
	/**
	 * Sender info:
	 * employee's unique id
	 */
	public long senderEmpID;
	
	/**
	 * Receiver info:
	 * position of employee
	 * 	w=waiter, m=manager, h=host, c=chef, X = log out, L=log in
	 */
	public char receiverPosition;
	/**
	 * Receiver info:
	 * employee's unique id
	 * 	-1 will mean send it to all of the employees of this pos logged in
	 */
	public long receiverEmpID;
	
	/**
	 * Actual message to be forwarded
	 */
	public String content;
	
	/**
	 *  Creates a new message 
	 * @param recPos - receiver's position
	 * @param recID - receiver's employee id
	 * @param mess
	 */
	public Message(char recPos, long recID, String mess){
		senderPosition = '1';
		senderEmpID = -1;
		
		//Load in receiver
		receiverPosition =recPos;
		
		//Load in receiver id
		receiverEmpID = recID;
		
		//Load in actual message
		content = mess;
	}
	
	/**
	 * Empty constructor
	 */
	public Message(){}

	
	/**
	 * Makes the message into a string so it can be sent over a socket.
	 */
	public String toString(){
		String ans = senderPosition +""+senderEmpID+"*"+receiverPosition+receiverEmpID+"*"+content;
		return ans;
	}
	
	public static Message fromString(String string){
		if(string==null || string.length()<4){
			return null;
		}
		Message ans = new Message();
		ans.senderPosition = string.charAt(0);
		string = string.substring(1);
		int indexStar = string.indexOf('*');
		String id = string.substring(0, indexStar);
		int idN = Integer.parseInt(id);
		ans.senderEmpID = idN;
		string = string.substring(indexStar+1);
		
		ans.receiverPosition = string.charAt(0);
		string = string.substring(1);
		indexStar = string.indexOf('*');
		id = string.substring(0, indexStar);
		idN = Integer.parseInt(id);
		ans.receiverEmpID = idN;
		string = string.substring(indexStar+1);
		ans.content = string;
		return ans;
		
		
	}
}
