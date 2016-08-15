// written by: Annie Antony and Nishtha Sharma
// tested by: Annie Antony and Nishtha Sharma
// debugged by: Annie Antony and Nishtha Sharma
package chef;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import chef.ChefInterface;
import dataBaseC.Ticket;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel that draws the screen for the chef's list of ticket screen.
 * This is shown when a chef first logs in.
 * @author cms549, aa1122, ns662
 *
 */
public class ChefPanel extends JPanel {
	/**
	 * used to access all of the methods in chef 
	 */
	public ChefInterface ci;

	/**
	 * Create the panel.
	 * @param chefInterface 
	 */
	public ChefPanel(ChefInterface chefInterface) {
		ci = chefInterface;
		setBackground(new Color(51, 153, 255));
		//Array layout where you pick coordinates of each component
		setLayout(null);
		
		updateScreen();
	}
	/**
	 * Redraws the screen using current data.
	 */
	public void updateScreen() {
		removeAll();
		makeLogOutButton();	
		makeNotifyManagerButton();
		makeNameText();
		displayTickets();
		repaint();
		
	}
	/**
	 * Displays the tickets based on status of preparation first and then chronological order 
	 */
	private void displayTickets() {
		int amtleft = 4;
		int size = ci.ticketQueueFinished.size();
		for(int i=0;i<size && amtleft>0;i++){
			Long tickNum = ci.ticketQueueFinished.get(i);
			Ticket t =ci.ticketLookup.get(tickNum);
			displayTicket(4-amtleft, t);
			amtleft--;
		}
		if(amtleft>0){
			size = ci.ticketQueueStarted.size();
			for(int i=0;i<size && amtleft>0;i++){
				Long tickNum = ci.ticketQueueStarted.get(i);
				Ticket t =ci.ticketLookup.get(tickNum);
				displayTicket(4-amtleft, t);
				amtleft--;
			}
		}
		if(amtleft>0){
			size = ci.ticketQueuesemiStarted.size();
			for(int i=0;i<size && amtleft>0;i++){
				Long tickNum = ci.ticketQueuesemiStarted.get(i);
				Ticket t =ci.ticketLookup.get(tickNum);
				displayTicket(4-amtleft, t);
				amtleft--;
			}
		}
		if(amtleft>0){
			size = ci.ticketQueueUnstarted.size();
			for(int i=0;i<size && amtleft>0;i++){
				Long tickNum = ci.ticketQueueUnstarted.get(i);
				Ticket t =ci.ticketLookup.get(tickNum);
				System.out.println("Chefpan 88: Ticket number = "+t.ticketNumber);
				displayTicket(4-amtleft, t);
				amtleft--;
			}
		}
		
	}

	/**
	 * Sets up the Log Out Button
	 */
	private void makeLogOutButton(){
		
		JButton logOutButton = new JButton("Log Out");
		logOutButton.setForeground(Color.WHITE);
		logOutButton.setBackground(Color.RED);
		logOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeAreYouSure("you want to Log Out", 1);
			}
		});
		logOutButton.setBounds(1000, 0, 200, 30);
		add(logOutButton);
		
		
	}
	
	
	/**
	 * displays one ticket on the screen 
	 * @param index - index based on how many tickets in each list 
	 * @param t - ticket
	 */
	private void displayTicket(int index, Ticket t) {
		int xbutton, xwords;
		if(index==0){
			xbutton = 50;
		
		}
		else if(index==1){
			xbutton = 350;
			
		}
		else if(index==2){
			xbutton = 650;
			
		}
		else{
			xbutton = 950;
			
		}
		
		//draw the rectangle as a button
		JButton tableBut = new JButton("");
		tableBut.setForeground(Color.WHITE);
		tableBut.setBackground(Color.RED);
		tableBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ci.openTicketScreen(t.ticketNumber); //open a ticket to see the status of each dish 
			}
		});
		tableBut.setBounds(xbutton, 160, 200, 280);
		add(tableBut);
		
		//write table number
		JTextField tableNumber;
		tableNumber = new JTextField();
		tableNumber.setEditable(false);
		tableNumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tableNumber.setHorizontalAlignment(SwingConstants.CENTER);
		tableNumber.setText("Table Number: "+ t.tableNumber);
		tableNumber.setBounds(xbutton, 120, 200, 30);
		add(tableNumber);
		tableNumber.setColumns(10);
		
		//status of ticket
		JTextField status;
		status = new JTextField();
		status.setEditable(false);
		status.setFont(new Font("Tahoma", Font.PLAIN, 14));
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setText("Status: "+ getStatus(t.status));
		status.setBounds(xbutton, 450, 200, 30);
		add(status);
		status.setColumns(10);
	}
/**
 * Gets the status of the ticket - Unstarted, Semi-Started, Started, Finished
 * @param status - status of the ticket
 * @return
 */
	private String getStatus(char status) {
		if(status =='u'){
			return "Unstarted";
		}
		else if(status =='s'){
			return "Semi-Started";
		}
		else if(status =='S'){
			return "Started";
		}
		else if(status =='f'){
			return "Finished";
		}
		return null;
	}

	
	/**
	 * Makes the name header of the chef at the top left 
	 */
	private void makeNameText() {
		JTextField nameHeader;
		nameHeader = new JTextField();
		nameHeader.setEditable(false);
		nameHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
		nameHeader.setText("Logged In As: "+ ci.name);
		nameHeader.setBounds(0, 0, 200, 30);
		add(nameHeader);
		nameHeader.setColumns(10);
		
	}
	
	/**
	 * Sets up the notifyManager Button
	 */
	private void makeNotifyManagerButton(){
		
		JButton notifyManager = new JButton("Notify Manager");
		notifyManager.setForeground(Color.BLACK);
		notifyManager.setBackground(Color.ORANGE);
		notifyManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeAreYouSure("you want to notify the manager?",2);
			}
		});
		notifyManager.setBounds(600,570, 300, 30);
		add(notifyManager, getComponentCount());
		
	}
	
	
	
	/**
	 * Creates an are you sure message box for either logout or notifyManager
	 */
	private void makeAreYouSure(String m, int i) {
		//Make a White box with "Are you sure"
        JTextField areYouSure;
		areYouSure = new JTextField("Are you sure "+m);
		areYouSure.setEditable(false);
		areYouSure.setFont(new Font("Tahoma", Font.PLAIN, 16));
		areYouSure.setHorizontalAlignment(SwingConstants.CENTER);
		areYouSure.setBackground(Color.ORANGE);
		areYouSure.setBounds(250, 150, 700, 300);
		add(areYouSure);
		setComponentZOrder(areYouSure, 0);
		
		
		//Make yes button
		JButton yes = new JButton("YES");
		yes.setForeground(Color.BLACK);
		yes.setBackground(Color.GREEN);
		yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(i==1){//log out
					ci.logOut();
				}
				else if(i==2){//notify manager
					ci.notifyManager();
				}
				
			}
		});
		yes.setBounds(50,200, 200, 30);
		areYouSure.add(yes);
		
		//Make no button
		JButton no = new JButton("NO");
		no.setForeground(Color.BLACK);
		no.setBackground(Color.RED);
		no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateScreen();
			}
		});
		no.setBounds(450,200, 200, 30);
		areYouSure.add(no);
		repaint();
	}
	
	/** Draws a notification button on top of screen like banner
	 * once it is clicked it closes it
	 * @param content - message to be put in the notification
	 */
	public void makeNotification(String content) {
		JButton notificationButton = new JButton(content);
		notificationButton.setForeground(Color.BLACK);
		notificationButton.setBackground(Color.YELLOW);
		notificationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(notificationButton);
				repaint();
			}
		});
		notificationButton.setBounds(0, 0, 1200, 50);
		add(notificationButton,0);
		repaint();
		
	}
	
	
	
}
