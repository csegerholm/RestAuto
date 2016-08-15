// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package waiter;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dataBaseC.Ticket;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Panel that draws the screen for the waiter's list of ticket screen.
 * This is shown when a waiter first logs in.
 * @author cms549
 *
 */
public class WaiterTickListScreen extends JPanel {

	public WaiterInterface wi;
	
	/**
	 * Create the panel.
	 * @param waiterInterface 
	 */
	public WaiterTickListScreen(WaiterInterface waiterInterface) {
		wi = waiterInterface;
		//Set color to blue
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
		makeNameText();
		makeLogOutButton();	
		makeTicketButtons();
		makeNotifyWaiter();
		makeTime();
		repaint();
	}

	/**
	 * Writes the waiter's name at the top left
	 */
	private void makeNameText() {
		JTextField nameHeader;
		nameHeader = new JTextField("Logged In As: "+ wi.name);
		nameHeader.setEditable(false);
		nameHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
		nameHeader.setBounds(0, 0, 300, 30);
		add(nameHeader);
		
	}

	/**
	 * Looks through the ticket list and generates buttons for this
	 */
	private void makeTicketButtons() {
		int index =0;
		Iterator<Integer> keyset = wi.listOfTickets.keySet().iterator();
		while(keyset.hasNext() && index<4){
			int key = keyset.next();
			makeTicketButton(wi.listOfTickets.get(key),index);
			index++;
		}
	}

	/**
	 * Draws one ticket button on the screen
	 * @param t - ticket to be drawn
	 * @param index - spot on the screen to draw the ticket (can be 0-3)
	 */
	private void makeTicketButton(Ticket t, int index) {
		int xbut=0, ybut=0;
		if(index ==0){
			xbut = 100;
			ybut = 100;
		}
		else if(index ==1){
			xbut = 100;
			ybut = 350;
		}
		else if(index ==2){
			xbut = 700;
			ybut = 100;
		}
		else if(index ==3){
			xbut = 700;
			ybut = 350;
		}
		
		//draw the button
		JButton ticketButton = new JButton("#"+t.tableNumber);
		ticketButton.setFont(new Font("Tahoma", Font.PLAIN, 36));
		ticketButton.setForeground(Color.BLACK);
		ticketButton.setBackground(Color.WHITE);
		ticketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open that ticket
				wi.openTicketScreens(t.tableNumber);
			}
		});
		ticketButton.setBounds(xbut, ybut, 400, 150);
		add(ticketButton);
		
		if(t.hotFood){
			JTextField nameHeader;
			nameHeader = new JTextField("Hot Food");
			nameHeader.setEditable(false);
			nameHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
			nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
			nameHeader.setBounds(xbut+110, ybut+100, 200, 30);
			add(nameHeader);	
		}
		else if (t.recentlySat){
			JTextField nameHeader;
			nameHeader = new JTextField("Recently Sat");
			nameHeader.setEditable(false);
			nameHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
			nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
			nameHeader.setBounds(xbut+110, ybut+100, 200, 30);
			add(nameHeader);
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
				makeAreYouSure();
			}
		});
		logOutButton.setBounds(1000, 0, 200, 30);
		add(logOutButton,0);
		
	}	
	
	/**
	 * Creates an are you sure message box
	 */
	private void makeAreYouSure() {
		//Make a orange box with "Are you sure"
		JTextField areYouSure;
		areYouSure = new JTextField("Are you sure you want to log out?");
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
				wi.logOut();
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
	

	/**
	 * Sets up the Notify Waiter Button
	 */
	private void makeNotifyWaiter(){
		
		JButton notify = new JButton("Notify Waiter");
		notify.setForeground(Color.BLACK);
		notify.setBackground(Color.ORANGE);
		notify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wi.toKeyPadScreen('n');
			}
		});
		notify.setBounds(975, 570, 200, 30);
		add(notify);
		
	}
	
	
	/**
	 * writes the time on screen
	 */
		private void makeTime(){
			Calendar cal=Calendar.getInstance();
			JTextField timeHeader;
			String tmp=""+cal.getTime();
			tmp=tmp.substring(0, tmp.length()-12);
			timeHeader=new JTextField(tmp);
			timeHeader.setEditable(false);
			timeHeader.setFont(new Font("Tahoma",Font.PLAIN,14));
			timeHeader.setHorizontalAlignment(SwingConstants.CENTER);
			timeHeader.setBounds(450, 0, 300, 30);
			add(timeHeader);
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
