// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package loggingIn;

import java.awt.Dimension;
import javax.swing.JFrame;

import chef.ChefInterface;
import host.HostInterface;
import manager.ManagerInterface;
import waiter.WaiterInterface;

/**
 * This will be the base process running on all tablets. 
 * It handles logging in and switching to the appropriate screen once logged in.
 * @author cms549
 */
public class TabletApp {
	
	
	public static void logOut(LogInScreen logInPanel){
		logInPanel.logOut(logInPanel.currIDEntry);
		//show log in screen again
		logInPanel.loggedIn='0';
		logInPanel.currIDEntry=-1;
		logInPanel.frame.setContentPane(logInPanel);
		logInPanel.frame.revalidate();
	}
	
	
	public static void logIn(LogInScreen logInPanel){
		//calls the interface to set up the screen
		//constructors won't return until the screen closes or they log out
		if(logInPanel.loggedIn=='h'){
			new HostInterface(logInPanel);
		}
		else if(logInPanel.loggedIn=='c'){
			new ChefInterface(logInPanel);
		}
		else if(logInPanel.loggedIn=='m'){
			new ManagerInterface(logInPanel);
		}
		else if(logInPanel.loggedIn=='w'){
			new WaiterInterface(logInPanel);
		}
	
	}
	
	/**
	 * Starts the log in GUI 
	 * @param args
	 */
	public static void main(String[] args) {
		//GUI stuff
		JFrame frame= new JFrame("SWE");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LogInScreen logInPanel= new LogInScreen(frame);
		frame.setContentPane(logInPanel);
		frame.pack();
		frame.setSize(new Dimension(1200,650));
	}
}
