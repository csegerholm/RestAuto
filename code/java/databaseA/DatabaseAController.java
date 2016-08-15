// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package databaseA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import configuration.Configure;

/**
 * Starts the DataBase A. This will handle requests for log in. DB A holds employee info.
 * @author cms549
 */
public class DatabaseAController extends Thread {
	
	/**
	 * Name of file that holds the list of employees
	 * Each Line is a new employee (first line is skipped b/c it is header)
	 * Format: EmployeeName,Position(character can be w,m,c,h)
	 * Exp: Christina Segerholm,w
	 */
	private static final String empFile = "src/configuration/employeeInfo.txt";
	
	/**
	 * Port number that Database A Controller will be listening for log in attempts on.
	 */
	private final static int portNumber = Configure.getPortNumber("DatabaseAController");
	
	/**
	 * Socket to one tablet that DB A will use to log people on.
	 * Each DBAController will have their own as there will be a new DBAController for each time someone tries to log on.
	 */
	private Socket currListener;
	
	/**
	 * This should hold the next ID number for a newly created employee.
	 * This ensures each employee ID is unique.
	 */
	private static long currentID;
	
	/**
	 * Array of Employees, Matches Employee ID (int) with employee
	 */
	private static ArrayList<Employee> employeeList;
	
	/**
	 * Constructor
	 * @param listener - socket to one tablet that database A will use to communicate with a tablet to log it on properly.
	 */
	public DatabaseAController(Socket listener) {
		currListener=listener;
	}

	/**
	 * Adds an employee to the database
	 * @param name - name of employee
	 * @param position - position of employee
	 * 		w = waiter, h = host, c = chef, m = manager, o = owner
	 * @return true on success, false if you did not put in a valid position or if name is null
	 */
	public static boolean addEmployee(String name, char position){
		char[] possiblePositions = {'w','h','c','m','o'};
		if(name==null){
			return false;
		}
		for(int i =0;i<possiblePositions.length;i++){
			if(position == possiblePositions[i]){
				employeeList.add(new Employee(name, currentID, position));
				employeeList.get((int) currentID).loggedIn=false;
				currentID++;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Starts a new thread for the DB A controller so it can communicate with one tablet on one thread.
	 * Reads the socket in and looks for a 'L' (which signifies logging in) or 'O' (logging out)
	 * Next it writes to the socket the position of the employee if they are logging in or '0' if the id is not valid
	 * 	it writes L if the employee is already logged in
	 * on a log out it changes the employee with that id to logged out
	 * This ensures that an employee doesn't log on to two devices.
	 * This is also used by the host when the host needs the list of employees and their ids.
	 * The socket will hang up after it logs a user in or out.
	 */
	public void run(){

		try(DataInputStream in = new DataInputStream(currListener.getInputStream());
				DataOutputStream out = new DataOutputStream(currListener.getOutputStream())) {
			
				String mess =in.readUTF();
				char first = mess.charAt(0);
				//logging in
				if(first=='L'){
					String num = mess.substring(2);
					int number = Integer.parseInt(num);
					//if employee id is invalid return 0
					if(number>=employeeList.size() || number<0){
						System.out.println("0");
						out.writeUTF("0");
					}
					else{
						Employee curE = employeeList.get(number);
						String ans = curE.position + curE.name;
						if(curE.loggedIn){
							System.out.println("L");
							out.writeUTF("L");//already logged in
						}
						else{
							curE.loggedIn=true;
							System.out.println(ans);
							out.writeUTF(ans);
						}
					}
				}
				//Logging out
				else if(first=='O'){ 
					String num = mess.substring(2);
					int number = Integer.parseInt(num);
					//if employee id is valid log them out
					if(number<employeeList.size() && number>=0){
						Employee curE = employeeList.get(number);
						curE.loggedIn=false;
					}
				}
				//Grab list of waiters 
				else if(first =='W'){
					out.writeUTF(sendWaiters());
				}
				currListener.close();
			}catch (Exception e) {
				e.printStackTrace();
			} 
	}
	
	/**
	 * Generates a string of all the waiters. ID,Name:ID,Name:ID,Name:
	 * @return
	 */
	private String sendWaiters() {
		String ans ="";
		for(int i=0; i< employeeList.size(); i++){
			Employee e = employeeList.get(i);
			if(e.position=='w'){
				ans = ans+i+","+e.name+":";
			}
		}
		return ans;
		
	}

	/**
	 * Starts the DataBase A. Creates a new thread for each log in request.
	 * @param args
	 */
	public static void main(String[] args){
		//later we will load this from a file
		currentID=0;
		employeeList = new ArrayList<Employee>();
		loadEmployees();
		
		if(args!=null && args.length==10){//for testing
			return;
		}
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
			System.out.println("Actual name="+serverSocket.getInetAddress());
			while (true) {
               new DatabaseAController(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("ERROR: DB A failed to start. Port " + portNumber+" is in use.");
            System.exit(-1);
        }
		
	}
	
	/**
	 * Loads employee list from file
	 */
	public static void loadEmployees(){
		//#EmployeeName,Position(character can be w,m,c,h)
		try (BufferedReader br = new BufferedReader(new FileReader(empFile))){

			String currLine;
			int i=0;
			while ((currLine = br.readLine()) != null) {
				if(i==0){//skip the header line
					i=1;
					continue;
				}
				String[] arr = currLine.split(",");
				addEmployee(arr[0],arr[1].charAt(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Used by DatabaseLadFromFileTest.java for testing 
	 * @return list of employees
	 */
	public static ArrayList<Employee> getEmployeeListForTesting(){
		return employeeList;
	}
	
	/**
	 * Returns current id of this object. Will always be 0 or greater.
	 * @return current id
	 */
	public long getCurrentID(){
		return currentID;
	}
	
	/**
	 * Returns employeeList of this object.
	 * @return employeeList
	 */
	public ArrayList<Employee> getEmployeeList(){
		return employeeList;
	}
	
	/**
	 * Returns currListener which is a socket of this object.
	 * @return currListener
	 */
	public Socket getCurrListener(){
		return currListener;
	}
	
	/**
	 * Returns path of file that holds employee info.
	 * @return empFile
	 */
	public String getEmpFile(){
		return empFile;
	}

	/**
	 * Getter for port number
	 * @return portNumber 
	 */
	public int getPortNumber(){
		return portNumber;
	}
}
