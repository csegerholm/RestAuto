// written by: Christina Segerholm and Athira Haridas
// tested by: Christina Segerholm and Athira Haridas
// debugged by: Christina Segerholm and Athira Haridas
package waiter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dataBaseC.Dish;
import dataBaseC.Ticket;

/**
 * Panel that will be used when one ticket is selected from the list of ticket screen.
 * Displays the current ticket as well as the menu.
 * This is where the waiter will place the order.
 * @author cms549
 */
public class WaiterOneTicketScreen extends JPanel {

	public WaiterInterface wi;
	/**
	 * current ticket open, null if none is open
	 */
	public Ticket currTicket;
	
	/**
	 * Can be Apps, Entrees, Drinks, Desserts
	 */
	public String currTabOpen;
	
	/**
	 * Remembers the last dish selected on the ticket so it knows what to remove
	 */
	private int lastDishSelected;
	
	public WaiterOneTicketScreen(WaiterInterface waiterInterface) {
		currTabOpen=null;
		lastDishSelected =-1;
		wi = waiterInterface;
		//Set color to blue
		setBackground(new Color(51, 153, 255));
		//Array layout where you pick coordinates of each component
		setLayout(null);
	}

	/**
	 * Draws the menu tabs near the top of the screen
	 */
	private void makeMenuChoices() {
		//make the categories
		int i =0; 
		Iterator<String> it = wi.menu.menu.keySet().iterator();
		while(it.hasNext() && i<4){
			String cat =it.next();
			JButton oneCategoryButton = new JButton(cat);
			oneCategoryButton.setForeground(Color.BLACK);
			oneCategoryButton.setBackground(Color.WHITE);
			oneCategoryButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currTabOpen = cat;
					updateScreen();
				}
			});
			oneCategoryButton.setBounds(350+i*150, 10, 150, 20);
			add(oneCategoryButton);
			i++;
		}
		//list items in openTab
		if (currTabOpen!=null){
			HashMap<String, Dish> menu = wi.menu.menu.get(currTabOpen);
			int j =0; 
			Iterator<String> it1 =menu.keySet().iterator();
			while(it1.hasNext() && j<80){
				String dish =it1.next();
				JButton oneDishButton = new JButton(dish);
				oneDishButton.setForeground(Color.BLACK);
				oneDishButton.setBackground(Color.WHITE);
				oneDishButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						wi.addDishToTicket(menu.get(dish));
						updateScreen();
					}
				});
				oneDishButton.setBounds(350+i%4*200, 50+(i/4)*25, 200, 25);
				add(oneDishButton);
				i++;
			}
		}
	}

	/**
	 * Draws the ticket on the left of the screen.
	 */
	private void makeTicketOnLeft() {
		JTextField whiteBox;
		whiteBox = new JTextField();
		whiteBox.setEditable(false);
		whiteBox.setBounds(0, 30, 300, 500);
		add(whiteBox);
		
		//Write Table Number
		JTextField tableNum;
		tableNum = new JTextField("Table #: "+ currTicket.tableNumber);
		tableNum.setEditable(false);
		tableNum.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tableNum.setHorizontalAlignment(SwingConstants.CENTER);
		tableNum.setBounds(0, 0, 300, 30);
		whiteBox.add(tableNum);
		
		if(currTicket.priority){
			JTextField prior;
			prior = new JTextField("PRIORITY");
			prior.setEditable(false);
			prior.setFont(new Font("Tahoma", Font.PLAIN, 14));
			prior.setHorizontalAlignment(SwingConstants.CENTER);
			prior.setBackground(Color.ORANGE);
			prior.setBounds(0, 30, 300, 30);
			whiteBox.add(prior);
		}
		
		//make a row for each item on the ticket (or up to 16)
		ArrayList<Dish> listOfDishes = currTicket.listOfDishes;
		int row = 3, i =0, y=60;
		while(row<19 && i<listOfDishes.size()){
			int index =i;
			Dish dish = listOfDishes.get(i);
			String name = dish.name;
			double price = dish.price;
			//write the x1 name then price
			String button = name + "\t$"+price;	
			JButton oneDishButton = new JButton(button);
			oneDishButton.setForeground(Color.BLACK);
			if(dish.sent){
				oneDishButton.setBackground(Color.CYAN);
			}
			else{
				oneDishButton.setBackground(Color.WHITE);
			}
			oneDishButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					lastDishSelected = index;
				}
			});
			oneDishButton.setBounds(0, y, 300, 30);
			y= y+30;
			whiteBox.add(oneDishButton);
			i++;
			row++;
			for(int k=0; k<dish.comments.size(); k++){
				String oneComment = " -"+dish.comments.get(k);
				JTextField oneC = new JTextField(oneComment);
				oneC.setEditable(false);
				oneC.setFont(new Font("Tahoma", Font.PLAIN, 12));
				oneC.setHorizontalAlignment(SwingConstants.LEFT);
				oneC.setForeground(Color.BLACK);
				if(dish.sent){
					oneC.setBackground(Color.CYAN);
				}
				else{
					oneC.setBackground(Color.WHITE);
				}
				oneC.setBounds(0, y, 300, 15);
				whiteBox.add(oneC);
				y=y+15;
			}
		}
		
		
		//last row is total
		JTextField total;
		String price = ""+currTicket.price;
		if(currTicket.price!=0){
			if(price.indexOf('.')==-1){
				price=price+".00";
			}
			else if(price.indexOf('.')+3 <=price.length()){
				price=price.substring(0,price.indexOf('.')+3);
			}
			else if(price.indexOf('.')+2 <=price.length()){
				price=price.substring(0,price.indexOf('.')+2)+"0";
			}
			else if(price.indexOf('.')+1<=price.length()){
				price=price+"00";
			}
		}
		total = new JTextField("Total: $"+ price);
		total.setEditable(false);
		total.setFont(new Font("Tahoma", Font.PLAIN, 14));
		total.setHorizontalAlignment(SwingConstants.CENTER);
		total.setBounds(0, 470, 300, 30);
		whiteBox.add(total);
		
	}

	/**
	 * Draws the waiter's name at the top left
	 */
	private void makeNameText() {
		JTextField nameHeader;
		nameHeader = new JTextField("Logged In As: "+ wi.name);
		nameHeader.setEditable(false);
		nameHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
		nameHeader.setBounds(0, 0, 300, 30);
		add(nameHeader, getComponentCount());
		
	}
	
	/**
	 * Sets up the Back Button which is used to jump back to the list of tickets screen.
	 */
	private void makeBackButton(){
		
		JButton logOutButton = new JButton("Back");
		logOutButton.setForeground(Color.WHITE);
		logOutButton.setBackground(Color.RED);
		logOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wi.backToMainScreen();
			}
		});
		logOutButton.setBounds(1000, 0, 200, 30);
		add(logOutButton, getComponentCount());
		
	}
	
	/**
	 * Sets up the Priority Button which is used to switch a ticket as a priority or non priority ticket.
	 */
	private void makePriorityButton(){
		
		JButton prior = new JButton("Priority Ticket");
		prior.setFont(new Font("Tahoma", Font.PLAIN, 20));
		prior.setHorizontalAlignment(SwingConstants.CENTER);
		prior.setVerticalAlignment(SwingConstants.CENTER);
		prior.setForeground(Color.ORANGE);
		prior.setBackground(Color.BLACK);
		prior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTicket.priority = !currTicket.priority;
				updateScreen();
			}
		});
		prior.setBounds(200,530, 200, 80);
		add(prior, getComponentCount());
	}
	
	
	/**
	 * Sets up the notifyManager Button used to notify the manager about a unhappy table.
	 */
	private void makeNotifyManagerButton(){
		
		JButton notifyManager = new JButton("Notify Manager");
		notifyManager.setFont(new Font("Tahoma", Font.PLAIN, 20));
		notifyManager.setHorizontalAlignment(SwingConstants.CENTER);
		notifyManager.setVerticalAlignment(SwingConstants.CENTER);
		notifyManager.setForeground(Color.BLACK);
		notifyManager.setBackground(Color.ORANGE);
		notifyManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeNotifyManagerOptions();
				
			}
		});
		notifyManager.setBounds(800,530, 200, 80);
		add(notifyManager, getComponentCount());
		
	}
	
	public String message="";
	
	/**
	 * Draws the options for notifying a manager on the screen
	 */
	private void makeNotifyManagerOptions(){
		updateScreen();
		JTextField whiteBox;
		whiteBox = new JTextField();
		whiteBox.setEditable(false);
		whiteBox.setVisible(true);
		whiteBox.setBounds(450, 240, 475, 300);
		add(whiteBox);
		JButton sat = new JButton("Satisfied Customer");
		sat.setForeground(Color.BLACK);
		sat.setBackground(Color.WHITE);
		sat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message="Satisfied Customer!";
				updateScreen();
				wi.notifyManager(currTicket,message);
				}
			
		});
		sat.setBounds(160,60, 150, 30);
		whiteBox.add(sat);
		
		JButton unsat = new JButton("Angry Customer");
		unsat.setForeground(Color.BLACK);
		unsat.setBackground(Color.WHITE);
		unsat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message="Angry Customer";
				updateScreen();
				wi.notifyManager(currTicket,message);
				}
			
		});
		unsat.setBounds(160,100, 150, 30);
		whiteBox.add(unsat);
		
		JButton speed = new JButton("Coupon");
		speed.setForeground(Color.BLACK);
		speed.setBackground(Color.WHITE);
		speed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message="Coupon";
				updateScreen();
				wi.notifyManager(currTicket,message);
				}
			
		});
		speed.setBounds(160,140, 150, 30);
		whiteBox.add(speed);
		
		JButton meal = new JButton("Meal Issue");
		meal.setForeground(Color.BLACK);
		meal.setBackground(Color.WHITE);
		meal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message="Meal Issue";
				updateScreen();
				wi.notifyManager(currTicket,message);
				}
			
		});
		meal.setBounds(160,180, 150, 30);
		whiteBox.add(meal);
		
		JButton waiter = new JButton("Waiter Issue");
		waiter.setForeground(Color.BLACK);
		waiter.setBackground(Color.WHITE);
		waiter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message="Waiter Issue";
				updateScreen();
				wi.notifyManager(currTicket,message);
				}
			
		});
		waiter.setBounds(160,220, 150, 30);
		whiteBox.add(waiter);
		
		JButton back = new JButton("BACK");
		back.setForeground(Color.BLACK);
		back.setBackground(Color.RED);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateScreen();
				}
			
		});
		back.setBounds(0,0, 100, 30);
		whiteBox.add(back);
		
	}
	
	/**
	 * Sets up the sendTicket Button used to send the chef the ticket
	 */
	private void makeSendTicketButton(){
		
		JButton sendButton = new JButton("Send to Chef");
		sendButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		sendButton.setHorizontalAlignment(SwingConstants.CENTER);
		sendButton.setVerticalAlignment(SwingConstants.CENTER);
		sendButton.setForeground(Color.WHITE);
		sendButton.setBackground(Color.GREEN);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wi.sendTicket(currTicket);
				updateScreen();
			}
		});
		sendButton.setBounds(0,530, 200, 80);
		add(sendButton, getComponentCount());
		
	}
	
	
	/**
	 * Sets up the Paid Button used to notify the host that this table has paid and take it off of this waiter's
	 * list of tickets as they have paid so the ticket is no longer needed.
	 */
	private void makePaidButton(){
		
		JButton paidButton = new JButton("Paid");
		paidButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		paidButton.setHorizontalAlignment(SwingConstants.CENTER);
		paidButton.setVerticalAlignment(SwingConstants.CENTER);
		paidButton.setForeground(Color.WHITE);
		paidButton.setBackground(Color.RED);
		paidButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//are you sure?
				makeAreYouSure("table "+ currTicket.tableNumber +" paid?",0);
			}
		});
		paidButton.setBounds(1000,530, 200, 80);
		add(paidButton, getComponentCount());
		
	}
	
	/**
	 * Creates an are you sure message box. Already prints "Are you sure "
	 * @param m - message to append to Are you sure 
	 * @param i - used to id what operation you are using this for
	 * 	0 is for paid
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
		setComponentZOrder(areYouSure, 2);
		
		
		//Make yes button
		JButton yes = new JButton("YES");
		yes.setForeground(Color.BLACK);
		yes.setBackground(Color.GREEN);
		yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (i ==0){
					wi.paid(currTicket.tableNumber);
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
	
	/**
	 * Sets up the Modify Dish Button used to modify a dish from the open ticket.
	 */
	private void makeModifyButton(){
		
		JButton modify = new JButton("Modify");
		modify.setFont(new Font("Tahoma", Font.PLAIN, 20));
		modify.setHorizontalAlignment(SwingConstants.CENTER);
		modify.setVerticalAlignment(SwingConstants.CENTER);
		modify.setForeground(Color.WHITE);
		modify.setBackground(Color.ORANGE);
		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(lastDishSelected != -1){
					makeModifyOptions();
				}
			}
		});
		modify.setBounds(400,530, 200, 80);
		add(modify, getComponentCount());
		
	}
	
	/**
	 * Creates the print button
	 */
	private void makePrintButton(){
		
		JButton printButton = new JButton("Print");
		printButton.setForeground(Color.WHITE);
		printButton.setBackground(Color.GREEN);
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//are you sure?
				//makeAreYouSure("table "+ currTicket.tableNumber +" paid?",0);
			}
		});
		printButton.setBounds(1000,480, 200, 40);
		add(printButton, getComponentCount());
		
	}
	
	/**
	 * Draws allergy options, to go, and birthday options
	 */
	private void makeModifyOptions(){
		//make a box
		updateScreen();
		JTextField whiteBox= new JTextField();
		whiteBox.setEditable(false);
		whiteBox.setVisible(true);
		whiteBox.setBounds(400, 240, 475, 300);
		add(whiteBox);
		
		
		//put all options in box
		String [] options={"Gluten Free", "To Go", "Nut Allergy", "Vegan", "Lactose Intolerant", "Vegetarian", "Birthday","Soy Allergy"};
		JButton [] optButtons=new JButton[8];
		int xbound=160; int ybound=60; int length=100; int width=30;
		for (int i=0;i<optButtons.length;i++){
			int d=i;
			optButtons[i]=new JButton(options[i]);
			optButtons[i].setForeground(Color.BLACK);
			optButtons[i].setBackground(Color.WHITE);
			optButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						wi.addComment(lastDishSelected,options[d]);					
						updateScreen();	
						lastDishSelected=-1;
				}
			});
			optButtons[i].setBounds(xbound,ybound,length,width);
			whiteBox.add(optButtons[i]);
			if(i%2==0){
				xbound+=120;
			}
			if (i%2!=0){
				ybound+=40;
				xbound-=120;
			}
		}
		//create a back button
		JButton back=new JButton("BACK");
		back.setForeground(Color.BLACK);
		back.setBackground(Color.RED);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateScreen();
			}
		});
		back.setBounds(0,0, 150, 30);
		whiteBox.add(back);

	}
	
	/**
	 * Sets up the Remove Dish Button used to remove a dish from the open ticket.
	 */
	private void makeRemoveButton(){
		JButton removeButton = new JButton("Remove");
		removeButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		removeButton.setHorizontalAlignment(SwingConstants.CENTER);
		removeButton.setVerticalAlignment(SwingConstants.CENTER);
		removeButton.setForeground(Color.BLACK);
		removeButton.setBackground(Color.RED);
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(lastDishSelected != -1){
					wi.removeDishFromTicket(lastDishSelected);
					lastDishSelected = -1;
					updateScreen();
				}
			}
		});
		removeButton.setBounds(600,530, 200, 80);
		add(removeButton, getComponentCount());
		
	}
	
	/**
	 * This is called when ever you switch to this screen, it sets up the ticket 
	 * to be displayed on the screen.
	 * @param t - ticket to be displayed
	 */
	public void setTicket(Ticket t){
		currTicket = t;
		currTicket.recentlySat=false;
		currTicket.hotFood=false;
		updateScreen();
		
	}

	/**
	 * Draws button for gift cards or coupons - this includes gift card or coupons/comps
	 */
	private void makeSpecialsButton(){
		JButton spec = new JButton("Coupon");
		spec.setForeground(Color.BLACK);
		spec.setBackground(Color.WHITE);
		spec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wi.toKeyPadScreen('m');
			}
		});
		spec.setBounds(1000,360, 200, 40);
		add(spec);
		
		JButton spec2 = new JButton("Gift Card");
		spec2.setForeground(Color.BLACK);
		spec2.setBackground(Color.WHITE);
		spec2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wi.toKeyPadScreen('g');
			}
		});
		spec2.setBounds(1000,400, 200, 40);
		add(spec2);
	}
	
	/**
	 * Refreshes the screen
	 */
	public void updateScreen() {
		removeAll();
		makeNameText();
		makeTicketOnLeft();
		makeBackButton();
		makeNotifyManagerButton();
		makeSendTicketButton();
		makePaidButton();
		makeRemoveButton();
		makeModifyButton();
		makeSpecialsButton();
		makeMenuChoices();
		makePriorityButton();
		makePrintButton();
		repaint();
		validate();
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
