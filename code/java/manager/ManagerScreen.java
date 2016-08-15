// written by: Christina Segerholm and Athira Haridas
// tested by: Christina Segerholm and Athira Haridas
// debugged by: Christina Segerholm and Athira Haridas
package manager;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import messageController.Message;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel that is displayed when manager logs on.
 * Shows all of the messages sent by any employees to the manger.
 * Also allows for manager to broadcast a message to all employees.
 * @author cms549
 *
 */
public class ManagerScreen extends JPanel {

	public ManagerInterface mi;
	
	/**
	 * Keeps track of the last message clicked on. This will be used in the delete message method.
	 */
	private int lastMessageSelected;
	
	/**
	 * Create the panel.
	 * @param waiterInterface 
	 */
	public ManagerScreen(ManagerInterface MI) {
		lastMessageSelected=-1;
		mi = MI;
		//Set color to blue
		setBackground(new Color(51, 153, 255));
		//Array layout where you pick coordinates of each component
		setLayout(null);
		
		updateScreen();
	}

	private void makeNameText() {
		JTextField nameHeader;
		nameHeader = new JTextField();
		nameHeader.setEditable(false);
		nameHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
		nameHeader.setText("Logged In As: "+ mi.name);
		nameHeader.setBounds(0, 0, 200, 30);
		add(nameHeader);
		nameHeader.setColumns(10);
		
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
		add(logOutButton);
		
	}


	/**
	 * Creates an are you sure message box for logging out
	 */
	private void makeAreYouSure() {
		//Make a White box with "Are you sure"
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
				mi.logOut();
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
	* Sets up the Delete Message Button
	 */
	private void makeDeleteButton(){
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setBackground(Color.RED);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(lastMessageSelected!=-1){
					mi.deleteMessage(lastMessageSelected);
				}
				lastMessageSelected=-1;
			}
		});
		deleteButton.setBounds(1000, 570, 200, 30);
		add(deleteButton);
		
	}

	/** 
	* Sets up the Create New Message Button
	 */
	private void makeNewMessageButton(){
		
		JButton newMess = new JButton("Create New Message");
		newMess.setForeground(Color.BLACK);
		newMess.setBackground(Color.WHITE);
		newMess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: bring up new screen for compose message
				newMessageScreen();
			}
		});
		newMess.setBounds(0, 570, 200, 30);
		add(newMess);
		
	}

	private void newMessageScreen() {
		// Draw the box
		JTextField whiteBox;
		whiteBox = new JTextField();
		whiteBox.setEditable(false);
		whiteBox.setBounds(100, 100, 1000, 400);
		add(whiteBox);
		
		//box that shows message
				JTextField messageBox=new JTextField();
				messageBox.setEditable(false);
				messageBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
				messageBox.setHorizontalAlignment(SwingConstants.LEFT);
				messageBox.setBounds(50,50,900,30);
				whiteBox.add(messageBox);
				
		
		//Make send button
		JButton yes = new JButton("SEND");
		yes.setForeground(Color.BLACK);
		yes.setBackground(Color.GREEN);
		yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message=messageBox.getText();
				messageBox.setText("");
				mi.sendMassNotification(message);
				updateScreen();
			}
		});
		yes.setBounds(800,370, 200, 30);
		whiteBox.add(yes);
				
		//Make close button
		JButton no = new JButton("CLOSE");
		no.setForeground(Color.BLACK);
		no.setBackground(Color.RED);
		no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateScreen();
			}
		});
		no.setBounds(0,370, 200, 30);
		whiteBox.add(no);
		
		
		JButton[] keys=new JButton[27];//26 letters
		int[] asciiVal=new int[27];
		JButton[] nums=new JButton[10];
		JButton backspace=new JButton("BACKSPACE");
		
		backspace.setForeground(Color.BLACK);
		backspace.setBackground(Color.WHITE);
		backspace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//updateScreen();
				messageBox.setText(messageBox.getText().substring(0, messageBox.getText().length()-1));
			}
		});
		backspace.setBounds(800,100, 200, 30);
		whiteBox.add(backspace);
		
		JButton spacebar=new JButton("SPACEBAR");
		spacebar.setForeground(Color.BLACK);
		spacebar.setBackground(Color.WHITE);
		spacebar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//updateScreen();
				messageBox.setText(messageBox.getText()+" ");
			}
		});
		spacebar.setBounds(250,280,500,30);
		whiteBox.add(spacebar);
		
		
		int xBound=250; int yBound=100; int length=50; int width=30;
		
		for (int i=1;i<nums.length;i++){
			nums[i]=new JButton(""+i);
			int d=i;
			nums[i].setForeground(Color.BLACK);
			nums[i].setBackground(Color.WHITE);
			nums[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//updateScreen();
						messageBox.setText(messageBox.getText()+d);
				}
			});
			nums[i].setBounds(xBound,yBound,length,width);
			whiteBox.add(nums[i]);
			xBound+=50;
		}
		nums[0]=new JButton("0");
		nums[0].setForeground(Color.BLACK);
		nums[0].setBackground(Color.WHITE);
		nums[0].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				messageBox.setText(messageBox.getText()+"0");
			}
		});
		nums[0].setBounds(xBound,yBound,length,width);
		whiteBox.add(nums[0]);
		
		//ROW 1
		keys[1]=new JButton("Q");asciiVal[1]=81;
		keys[2]=new JButton("W");asciiVal[2]=87;
		keys[3]=new JButton("E");asciiVal[3]=69;
		keys[4]=new JButton("R");asciiVal[4]=82;
		keys[5]=new JButton("T");asciiVal[5]=84;
		keys[6]=new JButton("Y");asciiVal[6]=89;
		keys[7]=new JButton("U");asciiVal[7]=85;
		keys[8]=new JButton("I");asciiVal[8]=73;
		keys[9]=new JButton("O");asciiVal[9]=79;
		keys[10]=new JButton("P");asciiVal[10]=80;
		 xBound=250;  yBound=140;  length=50;  width=30;
		for (int i=1;i<=10;i++){
			int d=i;
			keys[i].setForeground(Color.BLACK);
			keys[i].setBackground(Color.WHITE);
			keys[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//updateScreen();
						messageBox.setText(messageBox.getText()+Character.toString((char)asciiVal[d]));
				}
			});
			keys[i].setBounds(xBound,yBound,length,width);
			whiteBox.add(keys[i]);
			xBound+=50;
			
			
			
		}
		//ROW 2
		keys[11]=new JButton("A");asciiVal[11]=65;
		keys[12]=new JButton("S");asciiVal[12]=83;
		keys[13]=new JButton("D");asciiVal[13]=68;
		keys[14]=new JButton("F");asciiVal[14]=70;
		keys[15]=new JButton("G");asciiVal[15]=71;
		keys[16]=new JButton("H");asciiVal[16]=72;
		keys[17]=new JButton("J");asciiVal[17]=74;
		keys[18]=new JButton("K");asciiVal[18]=75;
		keys[19]=new JButton("L");asciiVal[19]=76;
		
		xBound=275; yBound=180; length=50; width=30;
		for (int i=11;i<=19;i++){
			int d=i;
			keys[i].setForeground(Color.BLACK);
			keys[i].setBackground(Color.WHITE);
			keys[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//updateScreen();
					messageBox.setText(messageBox.getText()+Character.toString((char)asciiVal[d]));
				}
			});
			keys[i].setBounds(xBound,yBound,length,width);
			whiteBox.add(keys[i]);
			xBound+=50;
			
			
			
		}
		
		//ROW 3
		keys[20]=new JButton("Z");asciiVal[20]=90;
		keys[21]=new JButton("X");asciiVal[21]=88;
		keys[22]=new JButton("C");asciiVal[22]=67;
		keys[23]=new JButton("V");asciiVal[23]=86;
		keys[24]=new JButton("B");asciiVal[24]=66;
		keys[25]=new JButton("N");asciiVal[25]=78;
		keys[26]=new JButton("M");asciiVal[26]=77;
		
		xBound=325; yBound=220; length=50; width=30;
		for (int i=20;i<=26;i++){
			int d=i;
			keys[i].setForeground(Color.BLACK);
			keys[i].setBackground(Color.WHITE);
			keys[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//updateScreen();
					messageBox.setText(messageBox.getText()+Character.toString((char)asciiVal[d]));
				}
			});
			keys[i].setBounds(xBound,yBound,length,width);
			whiteBox.add(keys[i]);
			xBound+=50;
			
			
			
		}
		
		repaint();
		
		
	}

	public void updateScreen() {
		removeAll();
		makeNameText();
		makeDeleteButton();
		makeNewMessageButton();
		makeLogOutButton();	
		makeListOfMessages();
		repaint();
		
	}

	/**
	 * prints the list of messages
	 */
	private void makeListOfMessages() {
		int size = mi.listOfMessages.size();
		for(int i =0 ; i< size; i++){
			int index = size-1 -i ;
			if(i>20){
				break;
			}
			Message m = mi.listOfMessages.get(index);
			//make a button for each message
			JButton newMess = new JButton(m.content);
			newMess.setForeground(Color.BLACK);
			newMess.setBackground(Color.WHITE);
			newMess.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					lastMessageSelected=index;
				}
			});
			newMess.setBounds(100, 50+i*50, 1000, 50);
			add(newMess);
			
		}
		
	}
	

}
