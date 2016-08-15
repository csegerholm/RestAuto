// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package configuration;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class will be used to get the port numbers and domain names of the different databases and message controller.
 * @author cms549
 * It gets the information from domainNames.txt and portNumbers.txt.
 */
public class Configure {

	private static final String portNumFile = "src/configuration/portNumbers.txt";
	private static final String domainNameFile = "src/configuration/domainNames.txt";
	
	/**
	 * Reads the port number for the given server in the configuration file
	 * @param serverName = name of server (databaseacontroller) this is not case sensitive
	 * @returns the port number or -1 on error
	 */
	public static int getPortNumber(String serverName){
		
		try (BufferedReader br = new BufferedReader(new FileReader(portNumFile))){

			String currLine;

			while ((currLine = br.readLine()) != null) {
				
				String[] arr = currLine.split("=");
				if(arr[0].equals(serverName.toLowerCase())){
					return Integer.parseInt(arr[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return -1;
	}
		
	/**
	 * Reads the domain name for the given server in the configuration file
	 * @param serverName = name of server (databaseacontroller) this is not case sensitive
	 * @returns the domain name or null on error
	 */
	public static String getDomainName(String serverName){
		
		try (BufferedReader br = new BufferedReader(new FileReader(domainNameFile))){

			String currLine;

			while ((currLine = br.readLine()) != null) {
				
				String[] arr = currLine.split("=");
				if(arr[0].equals(serverName.toLowerCase())){
					return arr[1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

}
