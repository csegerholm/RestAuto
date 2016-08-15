// written by: Christina Segerholm and Athira Haridas
// tested by: Christina Segerholm and Athira Haridas
// debugged by: Christina Segerholm and Athira Haridas
package host;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import databaseB.Table;

/**
 * Panel that shows the two lists of tables.
 * One that is ready (can be seated). This is in green.
 * And one that is not ready. In this list, first the 
 * tables that are paid are listed (in yellow) these must be cleaned soon.
 * Then the seated tables are listed (in red).
 * @author cms549
 *
 */
public class HostTableScreen extends JPanel {

	public HostInterface hi;
	
	/**
	 * This is the current table selected by the host
	 */
	public Table tableSelected;
	
	/**
	 * This is the current waiter selected by the host
	 */
	public String waiterSelected;
	
	/**
	 * Constructor
	 * @param hI - host interface
	 */
	public HostTableScreen(HostInterface hI) {
		hi  = hI;
		//Set color to blue
		setBackground(new Color(51, 153, 255));
		//Array layout where you pick coordinates of each component
		setLayout(null);
		tableSelected=null;
		waiterSelected= null;
		updateScreen();
	}
	
	/** 
	 * Redraw the host screen
	 */
	public void updateScreen() {
		removeAll();
		makeNameText();
		makeTimeText();
		makeLogOutButton();
		makeCleanedButton();
		makeSeatButton();
		makeNotifyManagerButton();
		makeListOfWaiters();
		makeReadyTables();
		makeUnReadyTables();
		repaint();
		
	}
	
	/**
	 * Makes list of waiter names at bottom right of screen
	 */
	private void makeListOfWaiters() {
		int y = 570;
		Iterator<String> it = hi.listOfWaiters.keySet().iterator();
		HashMap<String,Integer> totalTables=hi.waiterTotalTables;
		while(it.hasNext()){
			String name = it.next();
			int numTables=totalTables.get(name);
			System.out.println(name+" "+numTables);
			JButton waiterButton = new JButton(name);
			JButton numTablesButton=new JButton(""+numTables);
			waiterButton.setForeground(Color.BLACK);
			numTablesButton.setForeground(Color.BLACK);
			waiterButton.setBackground(Color.WHITE);
			numTablesButton.setBackground(Color.WHITE);
			
			waiterButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					waiterSelected=name;
					
				}
			});
			waiterButton.setBounds(900, y, 200, 30);
			numTablesButton.setBounds(1100, y, 100, 30);
			add(waiterButton);
			add(numTablesButton);
			y=y-30;
		}
		
		JTextField header = new JTextField("Waiters:");
		header.setFont(new Font("Tahoma", Font.PLAIN, 14));
		header.setEditable(false);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		header.setBounds(900, y, 200, 30);
		header.setForeground(Color.BLACK);
		add(header);
		
		//add header for num current tables
		JTextField header2 = new JTextField("Total Tables:");
		header2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		header2.setEditable(false);
		header2.setHorizontalAlignment(SwingConstants.LEFT);
		header2.setBounds(1100, y, 100, 30);
		header2.setForeground(Color.BLACK);
		add(header2);
		
		
	}

	/**
	 * Draws the seat button used to seat tables.
	 */
	private void makeSeatButton() {
		JButton seatButton = new JButton("Seat");
		seatButton.setForeground(Color.BLACK);
		seatButton.setBackground(Color.GREEN);
		seatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateScreen();
				if(tableSelected ==null){
					drawWarningMessage("Please select a Table.");
				}
				else if(tableSelected.status=='r'){
					if(waiterSelected!=null){
						hi.seat(waiterSelected, tableSelected.tableNumber);
						waiterSelected=null;
						tableSelected=null;
					}
					else{
						drawWarningMessage("Please select a waiter.");
					}
				}
				else{
					drawWarningMessage("Table "+tableSelected.tableNumber+ " can't be sat.");
				}
				
			}
		});
		seatButton.setBounds(0, 570, 300, 30);
		add(seatButton);
		
	}
	

	/**
	 * Draws the cleaned button used specify that a table is cleaned.
	 */
	private void makeCleanedButton() {
		JButton cleanedButton = new JButton("Cleaned");
		cleanedButton.setForeground(Color.BLACK);
		cleanedButton.setBackground(Color.YELLOW);
		cleanedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateScreen();
				if(tableSelected ==null){
					drawWarningMessage("Please select a Table.");
				}
				else if(tableSelected.status=='p'){
						hi.cleaned(tableSelected.tableNumber);
						waiterSelected=null;
						tableSelected=null;
				}
				else{
					drawWarningMessage("Table "+tableSelected.tableNumber+ " can't be cleaned.");
				}
				
			}
		});
		cleanedButton.setBounds(300, 570, 300, 30);
		add(cleanedButton);
		
	}
	
	
	/**
	 * Draws a warning message on screen with the message message
	 * @param message - warning to show
	 */
	private void drawWarningMessage(String message){
		JTextField failedAttempt = new JTextField(message);
		failedAttempt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		failedAttempt.setEditable(false);
		failedAttempt.setHorizontalAlignment(SwingConstants.CENTER);
		failedAttempt.setBounds(450, 520, 300, 30);
		failedAttempt.setForeground(Color.RED);
		add(failedAttempt);
	}

	/**
	 * Makes list of not ready tables
	 */
	private void makeUnReadyTables() {
		JTextField header = new JTextField("Paid/Seated Tables:");
		header.setFont(new Font("Tahoma", Font.PLAIN, 14));
		header.setEditable(false);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		header.setBounds(100, 70, 400, 30);
		header.setForeground(Color.BLACK);
		add(header);
		
		
		
		int y =100;
		int cnt=0;
		for(int i=0; i<hi.paidTables.size() && cnt<10; i++){
			int tnum = hi.paidTables.get(i);
			char ttype=hi.allTables.get(tnum).type;
			String type="";
			if (ttype=='b'){
				type="Booth";
			}
			else type="Table";
			JButton tableButton = new JButton(type+" #"+tnum);
			tableButton.setForeground(Color.BLACK);
			tableButton.setBackground(Color.YELLOW);
			tableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tableSelected=hi.allTables.get(tnum);
					
				}
			});
			tableButton.setBounds(100, y, 400, 50);
			add(tableButton);
			y=y+40;
			cnt++;
		}
		if(cnt<10){
			for(int i=0; i<hi.seatedTables.size() && cnt<10; i++){
				int tnum = hi.seatedTables.get(i);
				char ttype=hi.allTables.get(tnum).type;
				String type="";
				if (ttype=='b'){
					type="Booth";
				}
				else type="Table";
				JButton tableButton = new JButton(type+" #"+tnum);
				tableButton.setForeground(Color.BLACK);
				tableButton.setBackground(Color.RED);
				tableButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tableSelected=hi.allTables.get(tnum);
						
					}
				});
				tableButton.setBounds(100, y, 400, 50);
				add(tableButton);
				y=y+40;
				cnt++;
			}
		}
		
		
	}

	/**
	 * Makes list of ready tables
	 */
	private void makeReadyTables() {
		JTextField header = new JTextField("Ready Tables:");
		header.setFont(new Font("Tahoma", Font.PLAIN, 14));
		header.setEditable(false);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		header.setBounds(700, 70, 400, 30);
		header.setForeground(Color.BLACK);
		add(header);
		int y =100;
		for(int i=0; i<hi.readyTables.size() && i<10; i++){
			int tnum = hi.readyTables.get(i);
			char ttype=hi.allTables.get(tnum).type;
			String type="";
			if (ttype=='b'){
				type="Booth";
			}
			else type="Table";
			JButton tableButton = new JButton(type+" #"+tnum);
			tableButton.setForeground(Color.BLACK);
			tableButton.setBackground(Color.GREEN);
			tableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tableSelected=hi.allTables.get(tnum);
					
				}
			});
			tableButton.setBounds(700, y, 400, 50);
			add(tableButton);
			y=y+40;
		}
		
	}

	/**
	 * writes the host's name at the top left
	 */
	private void makeNameText() {
		JTextField nameHeader;
		nameHeader = new JTextField("Logged In As: "+ hi.name);
		nameHeader.setEditable(false);
		nameHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
		nameHeader.setBounds(0, 0, 300, 30);
		add(nameHeader);
		
	}
/**
 * writes the time on screen
 */
	private void makeTimeText(){
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
	 * Creates an are you sure message box. Already prints "Are you sure "
	 * @param m - message to append to Are you sure 
	 * @param i - used to id what operation you are using this for
	 * 	1 is for log out, 2 is for notify manager
	 */
	private void makeAreYouSure(String m, int choice) {
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
				if(choice==1){//log out
					hi.logOut();
				}
				else if(choice==2){//notify manager
					hi.notifyManager();
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
