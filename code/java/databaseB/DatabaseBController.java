// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package databaseB;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;

import configuration.Configure;

/**
 * Starts the DB B.
 * Host will use this to grab the list of tables 
 * @author cms549
 *
 */
public class DatabaseBController extends Thread {
	
	/**
	 * Name of file that holds the list of tables for restuarant
	 * Each Line is a new table (first line is skipped b/c it is header)
	 * Format: TableNumber,NumberOfSeats,Booth(b)/Table(t)
	 * Exp: 1,4,b ==> Table Number = 1, Max Occup = 4, Table is a booth.
	 */
	private static final String tabNumFile = "src/configuration/tableNumbers.txt";
	
	/**
	 * port number DB B will be listening on
	 */
	private final static int portNumber = Configure.getPortNumber("DatabaseBController");
	
	/**
	 * used to convert java objects to JSON format and vice versa.
	 */
	public static Gson jsonConverter = new Gson();
	
	/**
	 * Listens to one tablet.
	 * Each DBCController thread gets one.
	 */
	private Socket currListener;
	
	/**
	 * Holds the list of tables. 
	 * See TableList.java
	 */
	public static TableList listOfTables;

	/**
	 * Constructor
	 * @param listener
	 */
	public DatabaseBController(Socket listener) {
		currListener=listener;
	}

	/**
	 * Adds table to the list of tables
	 * @param tabNum - table number you wish to add
	 * @param maxOccupancy - max amount of people that can fit at the table
	 * @return true on success, false on failure
	 */
	public static boolean addTable(int tabNum, int maxOccupancy, char type){
		if(listOfTables.hm.containsKey(tabNum)){
			return false;
		}
		listOfTables.hm.put(tabNum, new Table(tabNum, maxOccupancy,type));
		return true;
	}
	
	
	/**
	 * Each request will get its own thread. This will be used to send the host the list of tables.
	 * Socket hangs up after sending the table list.
	 */
	public void run(){
		String mess = "";
		try(DataInputStream in = new DataInputStream(currListener.getInputStream());
				DataOutputStream out = new DataOutputStream(currListener.getOutputStream());) {
			mess=in.readUTF();
			char first = mess.charAt(0);
			if(first=='T'){ //send the host a list of tables
				System.out.println(jsonConverter.toJson(listOfTables));
				out.writeUTF(jsonConverter.toJson(listOfTables));
			}
			in.close();
			out.close();
			currListener.close();
		} catch (Exception e) {
			System.out.println("Before Error: Read in: "+ mess);
			e.printStackTrace();
		} 
	}
	
	/**
	 * Starts base thread for DB B
	 * @param args
	 */
	public static void main(String[] args){
		listOfTables = new TableList();
		loadTablesFromFile();
		if(args!=null && args.length==10){//for testing
			return;
		}
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (true) {
               new DatabaseBController(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("ERROR: DB B failed to start. Port " + portNumber+" is in use.");
            System.exit(-1);
        }	
	}
	
	
	/**
	 * Load the tables from the text file
	 */
	public static void loadTablesFromFile(){
		
		try (BufferedReader br = new BufferedReader(new FileReader(tabNumFile))){

			String currLine;
			int i=0;
			while ((currLine = br.readLine()) != null) {
				if(i==0){//skip the header line
					i=1;
					continue;
				}
				String[] arr = currLine.split(",");
				addTable(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]),arr[2].charAt(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Getter for tabNumFile - String that holds path to text file that holds all of the table numbers
	 * @return tabNumFile
	 */
	public String getTabNumFile(){
		return tabNumFile;
	}
	/**
	 * Getter for listOfTables - type = TableList
	 * @return listOfTables
	 */
	public TableList getListOfTables(){
		return listOfTables;
	}

	/**
	 * Getter for port number
	 * @return portNumber 
	 */
	public int getPortNumber(){
		return portNumber;
	}
}
