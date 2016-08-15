// written by: Annie Antony and Nishtha Sharma
// tested by: Annie Antony and Nishtha Sharma
// debugged by: Annie Antony and Nishtha Sharma
package chef;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import dataBaseC.Dish;
import dataBaseC.Ticket;

/**
 * Panel that will be used when one ticket is selected from the list of tickets screen.
 * Displays the current ticket - the dishes in it.
 * This is where the chef would modify the status of each dish
 * @author aa1122 and ns662
 *
 */
public class ChefOneTickScreen extends JPanel{
	/**
	 * current ticket opened
	 */
	public Ticket currTicket;
	/**
	 * way to access all the methods in the chef interface
	 */
	public ChefInterface ci;
	/**
	 * Constructor
	 * @param chefInterface
	 */
	public ChefOneTickScreen(ChefInterface chefInterface) {
		ci = chefInterface;
		/**
		 * Set color to blue
		 */
		setBackground(new Color(51, 153, 255));
		/**
		 * Array layout where you pick coordinates of each component
		 */
		setLayout(null);
	}

	/**
	 * This is called when ever you switch to this screen, it sets up the ticket 
	 * to be displayed on the screen.
	 * @param t - ticket to be displayed
	 */
	public void setTicket(Ticket t){
		currTicket = t;
		updateScreen();
		
	}
	/**
	 * Refreshes the screen
	 */
	public void updateScreen() {
		makeBackButton();
		makeTicketScreen();
		displayDishes();
		repaint();
		validate();
	}
	
	/**
	 * Shows the inside of the ticket
	 */
	private void makeTicketScreen() {
		// TODO Auto-generated method stub
		JTextField tablenumberHeader;
		tablenumberHeader = new JTextField();
		tablenumberHeader.setEditable(false);
		tablenumberHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tablenumberHeader.setHorizontalAlignment(SwingConstants.CENTER);
		tablenumberHeader.setText("Table Number: "+ currTicket.tableNumber);
		tablenumberHeader.setBounds(450, 0, 200, 30);
		add(tablenumberHeader);
		tablenumberHeader.setColumns(10);
		
		JTextField AppetizerHeader;
		AppetizerHeader = new JTextField();
		AppetizerHeader.setEditable(false);
		AppetizerHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		AppetizerHeader.setHorizontalAlignment(SwingConstants.CENTER);
		AppetizerHeader.setText("Appetizer");
		AppetizerHeader.setBounds(0,100, 200, 30);
		add(AppetizerHeader);
		AppetizerHeader.setColumns(10);
		
		JTextField EntreeHeader;
		EntreeHeader = new JTextField();
		EntreeHeader.setEditable(false);
		EntreeHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		EntreeHeader.setHorizontalAlignment(SwingConstants.CENTER);
		EntreeHeader.setText("Entree");
		EntreeHeader.setBounds(300, 100, 200, 30);
		add(EntreeHeader);
		EntreeHeader.setColumns(10);
		
		JTextField DrinksHeader;
		DrinksHeader = new JTextField();
		DrinksHeader.setEditable(false);
		DrinksHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		DrinksHeader.setHorizontalAlignment(SwingConstants.CENTER);
		DrinksHeader.setText("Drinks");
		DrinksHeader.setBounds(600, 100, 200, 30);
		add(DrinksHeader);
		DrinksHeader.setColumns(10);
		
		JTextField DessertHeader;
		DessertHeader = new JTextField();
		DessertHeader.setEditable(false);
		DessertHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		DessertHeader.setHorizontalAlignment(SwingConstants.CENTER);
		DessertHeader.setText("Dessert");
		DessertHeader.setBounds(900, 100, 200, 30);
		add(DessertHeader);
		DessertHeader.setColumns(10);
		
		}
	/**
	 * Displays the list of dishes 
	 */
	private void displayDishes(){
		ArrayList<Dish> listOfDishes = currTicket.listOfDishes;
		int row = 3;
		int i = 0;
		int apy = 130;
		int eny = 130;
		int desy = 130;
		int drink = 130;
		Dish temp;
		while(row<14 && i<listOfDishes.size()){
			temp = listOfDishes.get(i);
			if(temp.typeOfDish == null){
				displayDish(315,eny,temp);
				eny = eny + 30;
				row++;
				i++;
				continue;	
			}
			if(temp.typeOfDish.equals("appetizer")){
				displayDish(15,apy,temp);
				apy = apy + 30;
				row++;
				i++;
				continue;
			}else if(temp.typeOfDish.equals("entree")){
				displayDish(315,eny,temp);
				eny = eny + 30;
				row++;
				i++;
				continue;
			}else if(temp.typeOfDish.equals("dessert")){
				displayDish(615,desy,temp);
				desy = desy + 30;
				row++;
				i++;
				continue;
			}else{
				displayDish(915,drink,temp);
				drink = drink + 30;
				row++;
				i++;
				continue;
			}
				
			}
	}
	
	/**
	 * Displays an individual dish
	 * @param index
	 * @param dish
	 */
	private void displayDish(int xindex,int yindex, Dish dish){
		JButton dishBut = new JButton(dish.name);
		dishBut.setForeground(Color.BLACK);
		dishBut.setBackground(Color.WHITE);
		dishBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//do something 
			}
		});
		
		dishBut.setBounds(xindex,yindex ,170 ,30);
		add(dishBut);
		
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
				ci.backToMainScreen();
			}
		});
		logOutButton.setBounds(1000, 0, 200, 30);
		add(logOutButton, getComponentCount());
		
	}
}
