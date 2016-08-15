// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package waiter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Number key pad Panel used by waiter when:
 * 		- gift card
 * 		- manager validation
 * 		- coupon amount
 * 		- notify other waiter --- takes in table number
 * @author cms549
 *
 */
public class KeyPadScreen extends JPanel{
	
	/**
	 * Pointer back to waiter interface
	 */
	public WaiterInterface wi;
	
	/**
	 * Type of question you need the screen for - 'g' = giftCard Amount, 'm'= manager id, 'c'= coupon amount, 'n' = table id
	 */
	public char type;
	
	//JTextFields to draw out the screen
	private JTextField answer;
	private JTextField question;
	
	/**
	 * Creates the log in screen panel
	 * @param frame 
	 * @param lock
	 */
	public KeyPadScreen(WaiterInterface wi){
		this.wi=wi;
		type='0';
		//Set color to black
		setBackground(Color.BLACK);
		//Array layout where you pick coordinates of each component
		setLayout(null);
		answer = new JTextField();
		answer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		answer.setEditable(false);
		answer.setHorizontalAlignment(SwingConstants.CENTER);
		answer.setBounds(550, 80, 100, 25);
		answer.setBackground(Color.LIGHT_GRAY);
		updateScreen();
	}
	
	/**
	 * Redraws the screen using current data.
	 */
	public void updateScreen() {
		removeAll();
		makeTime();
		drawBackButton();
		makeHeaderText();
		makeKeypad();
		makeIDTextField();
		
	}
	
	
	
	/**
	 * Sets up the employee ID Text.
	 *  Display the current input number as a JTextField.
	 */
	private void makeIDTextField(){
		add(answer);
	}
	
	/**
	 * Draws the number keyboard on the screen
	 */
	private void makeKeypad(){
		
		JButton done = new JButton("DONE");
		done.setForeground(Color.WHITE);
		done.setBackground(new Color(0, 128, 0));
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean done = handleDone();
				answer.setText("");
				if(done){
					returnToRightScreen();
				}
			}
		});
		done.setBounds(725, 150, 90, 30);
		add(done);
		
		JButton deleteButton = new JButton("Clear");
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setBackground(new Color(255, 0, 0));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answer.setText("");
			}
		});
		deleteButton.setBounds(725, 270, 90, 30);
		add(deleteButton);
		
		JButton[] keypad = new JButton[10];
		int xRow1 = 500;
		int row=0;
		int cnt =0;
		for(int i = 1; i<10; i++){
			if(cnt>2){
				cnt=0;
				row++;
			}
			int d=i;
			keypad[i] = new JButton(""+i);
			keypad[i].setBounds(xRow1+ 75*cnt, 150+row*60, 50, 30);
			keypad[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					answer.setText(answer.getText()+d);
				}
			});
			add(keypad[i]);
			cnt++;
		}
		
		
		keypad[0] = new JButton("0");
		keypad[0].setBounds(575, 150+180, 50, 30);
		keypad[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answer.setText(answer.getText()+"0");
			}
		});
		add(keypad[0]);
		
		if(type=='c' || type=='g'){
			JButton per = new JButton(".");
			per.setBounds(500, 150+180, 50, 30);
			per.setVisible(true);
			per.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					answer.setText(answer.getText()+".");
				}
			});
			add(per);
		}
		
		
	}
	
	/**
	 * Returns false if it shows an error message and must stay on the screen
	 * @return
	 */
	private boolean handleDone() {
		if(type=='n'){//notify that waiter
			try{
				int table = Integer.parseInt(answer.getText());
				wi.notifyWaiter(table);
				return true;
			}catch(Exception e){
				drawErrorMessage("Table Number "+answer.getText()+" is Invalid.");
				return false;
			}
		}
		else if(type=='c'){//add coupon to ticket
			try{
				double amt = Double.parseDouble(answer.getText());
				wi.addGCorCoupon(amt*-1.0);
				return true;
			}catch(Exception e){
				drawErrorMessage("Amount Invalid.");
				return false;
			}
			
		}else if(type=='g'){
			try{
				double amt = Double.parseDouble(answer.getText());
				wi.addGCorCoupon(amt*1.0);
				return true;
			}catch(Exception e){
				drawErrorMessage("Amount Invalid.");
				return false;
			}
			
			
		}
		else{//if type = m we handle it in return to right screen
				try{
					int id = Integer.parseInt(answer.getText());
					//check if manager id is the right one
					if(wi.listOfManagerIds.contains(id)){
						//change to coupon amount screen
						type = 'c';
						updateScreen();
						return false;
					}else{
						drawErrorMessage(answer.getText()+"is not a Manager's ID.");
						answer.setText("");
						return false;
					}
					
				}catch(Exception e){
					drawErrorMessage(answer.getText()+"is not a Manager's ID.");
					answer.setText("");
					return false;
				}
		}
	}

	private void returnToRightScreen() {
		char old = type;
		if(old=='n'){//notify that waiter
			type='0';
			wi.backToMainScreen();
		}
		else{
			type='0';
			wi.backToOpenTicketScreen();
		}
		
		
	}

	/**
	 * Draws error message on screen
	 * @param message
	 */
	private void drawErrorMessage(String message) {
		JTextField failedAttempt = new JTextField(message);
		failedAttempt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		failedAttempt.setEditable(false);
		failedAttempt.setHorizontalAlignment(SwingConstants.CENTER);
		failedAttempt.setBounds(450, 520, 300, 30);
		failedAttempt.setForeground(Color.RED);
		add(failedAttempt);
		
	}

	/**
	 * Sets up the question As a JTextField that is non editable
	 */
	private void makeHeaderText(){
		question = new JTextField(getQuestion());
		question.setEditable(false);
		question.setFont(new Font("Tahoma", Font.PLAIN, 14));
		question.setHorizontalAlignment(SwingConstants.CENTER);
		question.setBounds(300, 20, 600, 30);
		add(question);

	}


	/**
	 * Grabs the question by using the type
	 * @return
	 */
	private String getQuestion() {
		if(type=='m'){
			return "COUPON: Please Enter Manager ID:";
		}
		else if(type =='n'){
			return "NOTIFY WAITER: Please Enter Table Number:";
		}
		else if(type =='c'){
			return "COUPON: Please Enter Amount to Take Off the Ticket:";
		}
		else{
			return "GIFT CARD: Please Enter Amount to Put On the Gift Card:";
		}
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
			timeHeader.setBounds(0, 0, 300, 30);
			add(timeHeader);
		}
	
	/**
	 * Draws back button
	 */
	private void drawBackButton(){
		JButton back = new JButton("BACK");
		back.setBounds(900, 0, 300, 30);
		back.setBackground(Color.RED);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answer.setText("");
				char old=type;
				type='0';
				if(old=='n'){
					wi.backToMainScreen();
				}
				else{
					wi.backToOpenTicketScreen();
				}
			}
		});
		add(back);
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
