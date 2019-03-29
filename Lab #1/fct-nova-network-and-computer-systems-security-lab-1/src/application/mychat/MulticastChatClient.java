package application.mychat;

/**
 * 
 * Network and Computer Systems Security
 * 
 * Practical Lab #1.
 * 
 * Integrated Master of Computer Science and Engineering
 * Faculty of Science and Technology of New University of Lisbon
 * 
 * Authors (Professors):
 * @author Henrique Joao Domingos - hj@fct.unl.pt
 * 
 * Adapted by:
 * @author Ruben Andre Barreiro - r.barreiro@campus.fct.unl.pt
 * @author Vicente Alves Almeida - vo.almeida@campus.fct.unl.pt
 *
 */

import java.io.IOException;
import java.net.InetAddress;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * The class responsible for the Multicast Chat's Client-Side Service,
 * extending the JFrame class for the layout of the Chat's Service and
 * implementing the Multicast Chat's Event Listener interface.
 * 
 * This class implements an interface for a Chat's Session SWING-based
 * and can be improved by the students to fulfil better the various pretended
 * features and improvements of the project.
 * 
 * Description:
 * - The Graphic Interface for the Multicast Chat's Client-Side Service and
 *   for all the final users using it, where they can communicate between
 *   with each others.
 */
public class MulticastChatClient extends JFrame implements MulticastChatEventListener {
	
	// Constants/Invariants:
	
	/**
	 * The serial version UID defined for this class
	 */
	private static final long serialVersionUID = 1L;

	
	// Global Instance Variables:
	
	/**
	 * The Multicast Chat's class associated to this Multicast Chat's Client-Side Service
	 */
	protected MulticastChat chat;

	/**
	 * The text-area where will be showed the messages of the conversation of
	 * this Multicast Chat's Client-Side Service, including the JOIN message,
	 * when someone joins to this Multicast Chat
	 */
	protected JTextArea conversationMessagesTextArea;

	/**
	 * The text-field where the messages to be sent through the conversation of
	 * this Multicast Chat's Client-Side Service, can be written before be sent
	 */
	protected JTextField messageToBeSentField;
	
	/**
	 * The text-field where can be placed the file to be able to download
	 */
	protected JTextField fileToBeDownloadTextField;
	
	/**
	 * The list hosts/users (Clients) currently "Connected"/"Online" in
	 * this Multicast Chat's Client-Side Service
	 */
	protected DefaultListModel<String> usersInChat;

	
	// Constructors:
	
	/**
	 * Constructor #1:
	 * 
	 * Construct a frame for this Multicast Chat's Client-Side Service
	 * (initialised in a state of "Disconnected"/"Offline").
	 */
	public MulticastChatClient() {
		
		super("Multicast Chat (Mode: Disconnected)");

		// Construct and initialise the components of the Graphics User Interface
		// (Session's Start/Initialising)
		this.conversationMessagesTextArea = new JTextArea();
		this.conversationMessagesTextArea.setEditable(false);
		this.conversationMessagesTextArea.setLineWrap(true);
		this.conversationMessagesTextArea.setBorder(BorderFactory.createLoweredBevelBorder());

		// Set the Scroll Bar Pane for this Multicast Chat's Client-Side Service
		JScrollPane textAreaScrollPane = new JScrollPane(this.conversationMessagesTextArea, 
														 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
														 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Add the Scroll Bar Pane to the centre of the main layout of
		// this Multicast Chat's Client-Side Service
		getContentPane().add(textAreaScrollPane, BorderLayout.CENTER);
		
		// Create the Users' List currently "Connected"/"Online" to
		// the main layout of this Multicast Chat's Client-Side Service
		this.usersInChat = new DefaultListModel<String>();
		JList<String> usersInChatList = new JList<String>(this.usersInChat);
		
		// Set the component of the Scroll Bars Pane of the
		// Users' List currently "Connected"/"Online" for
		// this Multicast Chat's Client-Side Service
		JScrollPane usersListScrollPane = new JScrollPane(usersInChatList, 
														  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
														  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
				
				// Global Instance Variables:
			
				/**
				 * The default serial version UID
				 */
				private static final long serialVersionUID = 1L;
				
				
				// Methods/Functions:
				
				// 1) Some basic methods:
				
				/**
				 * Returns the minimum size of the Scroll Bars Pane.
				 * 
				 * @return the minimum size of the Scroll Bars Pane 
				 */
				public Dimension getMinimumSize() {
					Dimension minimumDimension = super.getMinimumSize();
					
					// Set the width pretended on the minimum size of
					// the Scroll Bars Pane
					minimumDimension.width = 100;
					
					return minimumDimension;
				}
				
				/**
				 * Returns the preferred size of the Scroll Bar Pane.
				 * 
				 * @return the preferred size of the Scroll Bar Pane 
				 */
				public Dimension getPreferredSize() {
					Dimension preferredDimension = super.getPreferredSize();
					
					// Set the width pretended on the preferred size of
					// the Scroll Bars Pane
					preferredDimension.width = 100;
					
					return preferredDimension;
				}
		};
		
		// Create the Users' List currently "Connected"/"Online" to
		// the left side of the main layout box of this
		// Multicast Chat's Client-Side Service
		getContentPane().add(usersListScrollPane, BorderLayout.WEST);

		Box mainLayoutBox = new Box(BoxLayout.Y_AXIS);
		mainLayoutBox.add(Box.createVerticalGlue());
		
		// Create the panel of where the messages will be
		// processed and all the related operations occur in
		// the main layout box of the Multicast Chat's Client-Side Service
		JPanel messagePanel = new JPanel(new BorderLayout());
		
		// Add the label for the message panel for the text-field
		// for the messages to be sent can be written on the left side of
		// the main layout box of this Multicast Chat's Client-Side Service
		messagePanel.add(new JLabel("Message:"), BorderLayout.WEST);
		
		// Create the text-field where can be written the message to
		// be sent to the centre side of the message panel in the
		// main layout box of this Multicast Chat's Client-Side Service
		this.messageToBeSentField = new JTextField();
		
		// Add an Event Listener to the text-field where can be written the
		// message to be sent to the centre side of the message panel in the
		// main layout box of this Multicast Chat's Client-Side Service
		this.messageToBeSentField.addActionListener(new ActionListener() {
			
			/**
			 * Action to be performed related to the text-field
			 * where the message to be send can be written
			 */
			public void actionPerformed(ActionEvent dummyMessageToBeSentFieldEvent) {
				// The process of send the message through this
				// Multicast Chat's Client-Side Service 
				sendMessage();
			}
		});
		
		// Add the text-field where can be written the message to
		// be sent to the centre side of the message panel in the
		// main layout box of this Multicast Chat's Client-Side Service
		messagePanel.add(this.messageToBeSentField, BorderLayout.CENTER);
		
		// Create the label for the SEND button when it's pressed
		// for send the messages and added to the centre side of
		// the message panel in the main layout box of this
		// Multicast Chat's Client-Side Service
		JButton sendButton = new JButton("SEND");
				
		// Add an Event Listener to the SEND button when it's pressed
		// for send the messages to be sent to the centre side of
		// the message panel in the main layout box of this
		// Multicast Chat's Client-Side Service
		sendButton.addActionListener(new ActionListener() {
			
			/**
			 * Action to be performed related to the button
			 * where the message to be send can be written.
			 */
			public void actionPerformed(ActionEvent sendButtonPressedEvent) {
				// The process of send the message through this
				// Multicast Chat's Client-Side Service 
				sendMessage();
			}
		});
		
		// Add the text-field where can be written the message to
		// be sent to the right side of the message panel in the
		// main layout box of this Multicast Chat's Client-Side Service
		messagePanel.add(sendButton, BorderLayout.EAST);
		
		// Add the panel of where the messages will be
		// processed and all the related operations occur in
		// the main layout box of the Multicast Chat's Client-Side Service
		mainLayoutBox.add(messagePanel);
		mainLayoutBox.add(Box.createVerticalGlue());
		
		// Create the panel where the file can be download in the
		// main layout box of this Multicast Chat's Client-Side Service
		JPanel fileToBeDownloadPanel = new JPanel(new BorderLayout());
		
		// Add the label in the right side to the panel where the file can be
		// download in the main layout box of this Multicast Chat's Client-Side Service
		fileToBeDownloadPanel.add(new JLabel("Not used"), BorderLayout.WEST);
		
		// Create a text-field related to the title of the file to be download,
		// through the Multicast Chat's Client-Side Service
		this.fileToBeDownloadTextField = new JTextField();
		
		// Add an Event Listener to the text-field related to the
		// title of the file to be download in through the
		// Multicast Chat's Client-Side Service
		this.fileToBeDownloadTextField.addActionListener(new ActionListener() {
			
			// Action to be performed related to the text-field
			// where the title of the file to be download can be viewed
			public void actionPerformed(ActionEvent dummFileToBeDownloadedFieldEvent) {
				// The process of send the message through this
				// Multicast Chat's Client-Side Service 
				downloadFile();
			}
		});
		
		// Add the text-field related to the title of the file to be download,
		// to the centre side of the panel of the file to be download in the
		// main layout box of this Multicast Chat's Client-Side Service
		fileToBeDownloadPanel.add(this.fileToBeDownloadTextField, BorderLayout.CENTER);

		// Create the button that can be pressed when it's pretended
		// to download a file in this Multicast Chat's Client-Side Service
		// (Not implemented)
		JButton downloadButton = new JButton("Not Implemented");
		
		downloadButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downloadFile();
			}
		});
		
		// Add the button related to the download feature of the file to be download,
		// to the right side of the panel of the file to be download in the
		// main layout box of this Multicast Chat's Client-Side Service
		// (Not implemented)
		fileToBeDownloadPanel.add(downloadButton, BorderLayout.EAST);
		
		// Add the panel of the file to be download to the
		// main layout box of this Multicast Chat's Client-Side Service
		mainLayoutBox.add(fileToBeDownloadPanel);
				
		// Add a vertical glue component to the
		// main layout of this Multicast Chat's Client-Side Service
		mainLayoutBox.add(Box.createVerticalGlue());
		
		// Add the main layout box to the bottom of the Content's Pane
		getContentPane().add(mainLayoutBox, BorderLayout.SOUTH);

		/**
		 * Set the window's event listeners for both, opening and closing processes,
		 * in the start and end of a chat's session
		 */
		addWindowListener(new WindowAdapter() {
			
			/**
			 * 1st step on opening the Multicast Chat's Client-Side Service:
			 * - Open the window of this Multicast Chat's Client-Side Service,
			 *   request a focus on it and make it visible, until be closed
			 */
			public void windowOpened(WindowEvent e) {
				messageToBeSentField.requestFocus();
			}
			
			/**
			 * 1st step on closing the Multicast Chat's Client-Side Service:
			 * - Close the window of this Multicast Chat's Client-Side Service
			 */
			public void windowClosing(WindowEvent e) {
				onQuit();
				dispose();
			}
			
			/**
			 * 2nd step on closing the Multicast Chat's Client-Side Service:
			 * - Close this Multicast Chat's Client-Side Service completely
			 */
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			} 
		});
	}
	
	/**
	 * Adds a new user to the User's Interface of the currently "Online"/"Connected"
	 * presented in this Multicast Chat's Client-Side Service.
	 * 
	 * @param username the username of the user to be added to
	 *        the User's Interface of the currently "Online"/"Connected"
	 * 		  presented in this Multicast Chat's Client-Side Service
	 */
	protected void usersInterfaceAddUser(String username) {
		this.usersInChat.addElement(username);
	}
	
	/**
	 * Removes a user from the User's Interface of the currently "Online"/"Connected"
	 * presented in this Multicast Chat's Client-Side Service.
	 * 
	 * @param username the username of the user to be removed from
	 *        the User's Interface of the currently "Online"/"Connected"
	 * 		  presented in this Multicast Chat's Client-Side Service
	 * 
	 * @return the user removed from the User's Interface of the currently "Online"/"Connected"
	 * 		   presented in this Multicast Chat's Client-Side Service
	 */
	protected boolean usersInterfaceRemoveUser(String username) {
		return this.usersInChat.removeElement(username);
	}
	
	/**
	 * Initialise the list of, currently "Online"/"Connected" users using
     * the Multicast Chat's Client-Side Service from an Iterator,
     * containing all the usernames of the "Online"/"Connected" users.
     * 
     * Can be used, obtaining any Java's data structure.
	 * 
	 * @param usersIterator an Iterator, containing all the usernames of
	 *        the "Online"/"Connected" users
	 */
	protected void usersInterfaceUsersIteratorList(Iterator<String> usersIterator) {
		this.usersInChat.clear();
		
		// There's currently "Online"/"Connected" users using
		// the Multicast Chat's Client-Side Service
		if(usersIterator != null) {
			while(usersIterator.hasNext())
				this.usersInChat.addElement(usersIterator.next());
		}
	}
	
	/**
	 * Returns an enumeration with the names of the users appearing
	 * as "Online"/"Connected", in the this Multicast Chat's Client-Side Service
	 * and respectively, Graphics User Interface.
	 * 
	 * @return an enumeration with the names of the users appearing
	 * 		   as "Online"/"Connected", in the this Multicast Chat's Client-Side Service
	 * 		   and respectively, Graphics User Interface
	 */
	protected Enumeration<String> usersInterfaceListOfUsersEnumeration() {
		return this.usersInChat.elements();
	}
	
	/**
	 * Configure the group of the Multicast Chat's Client-Side Service's session
	 * in the Graphics User Interface.
	 * 
	 * @param username the username of the user that pretend to JOIN to the Multicast Chat's Client-Side Service's session
	 * 		  in the Graphics User Interface
	 * @param group the group of the Multicast Chat's Client-Side Service's session, in the Graphics User Interface
	 * @param port the port of the Multicast Chat's Client-Side Service's session, in the Graphics User Interface
	 * @param timeToLive the Time To Live (TTL) associated to the Multicast Chat's Client-Side Service's session,
	 *        in the Graphics User Interface
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
	public void join(String username, InetAddress group, int port, 
					 int timeToLive) throws IOException {
		
		setTitle("CHAT Multicast IP " + username + "@" + group.getHostAddress() 
				 + ":" + port + " [TTL=" + timeToLive + "]");

		// Create a Multicast Chat's Client-Side Service's session
		chat = new MulticastChat(username, group, port, timeToLive, this);
	} 
	
	/**
	 * Log a message which passed through the communication channel of
	 * the Multicast Chat's Client-Side Service's session.
	 * 
	 * @param message the message which passed through the communication channel of
	 *        the Multicast Chat's Client-Side Service's session
	 */
	protected void log(final String message) {
		Date date = new Date();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				conversationMessagesTextArea.append(message + " at " + date + "\n");
			} 
		});
	} 

	/**
	 * Send a normal message through the Multicast Chat's Client-Side Service's session.
	 * It's called when a click on SEND button in the Graphic Interface for the
	 * Multicast Chat's Client-Side Service occurs or, when the user press the ENTER key on
	 * the keyboard in the messages' line
	 */
	protected void sendMessage() {
		String messageToBeSent = messageToBeSentField.getText();
		messageToBeSentField.setText("");
		
		// Send the message through the Multicast Chat's Client-Side Service,
		// previously written in the 
		doSendMessage(messageToBeSent);
		messageToBeSentField.requestFocus();
	}

	/*
	 * Execute the operations related to the process of sending messages,
	 * through Multicast Chat's Client-Side Service's session
	 */
	protected void doSendMessage(String message) {
		try {
			chat.sendNormalMessage(message);
		}
		catch (Throwable ex) {
			JOptionPane.showMessageDialog(this,
										  "Error occurred while a message will about to be sent: " 
										  + ex.getMessage(), "Chat Error", 
															 JOptionPane.ERROR_MESSAGE);
		} 
	}
	
	/**
	 * Display/Print a message related to the Graphic Interface for
	 * the Multicast Chat's Client-Side Service and, which it's being passed through
	 * the communication of the Multicast Chat's Client-Side Service's session
	 */
	protected void displayMessage(final String string, final boolean error) {
		final JFrame jFrame = this;

		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				if(error) {
					JOptionPane.showMessageDialog(jFrame, string, "Chat Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(jFrame, string, "Chat Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}

	/**
	 * Ask for the download of a file
	 * 
	 * This method/function it's called when the
	 * SEND button or the 'ENTER' key in the user's keyboard is pressed.
	 */
	protected void downloadFile() {
		final String file = fileToBeDownloadTextField.getText();
		fileToBeDownloadTextField.setText("");
		
		// Initialise the Thread responsible for the download and
		// all the respectively transfer process
		new Thread(new Runnable() {
			
			// Start the Thread previously explained
			public void run() {
				doDownloadFile(file);
			}
		}).start();
		
		messageToBeSentField.requestFocus();
	}

	/**
	 * Execute operations related with the download of the file
	 * 
	 * NOTE: Any information to be reported to the user should made using the method "displayMessage"
	 */
	protected void doDownloadFile(String file) {
		// TODO: to complete
		System.err.println("Request the download of the file: " + file);
	}

	/**
	 * Method called when an user close the window of Graphic Interface of
	 * the Multicast Chat's Client-Side Service's session
	 */
	protected void onQuit() {
		try {
			if(chat != null)
				chat.terminateService();
		}
		catch (Throwable ex) {
			JOptionPane.showMessageDialog(this, "ERROR in the termination of the Chat's Service: "
										  + ex.getMessage(), " - ERROR occurred in the Chat's Service", 
										 JOptionPane.ERROR_MESSAGE);
		} 
	} 

	/**
	 * Method called when an user receive a message through the window of
	 * Graphic Interface of the Multicast Chat's Client-Side Service's session
	 */
	public void chatMessageReceived(String username, InetAddress address, int port, String message) {
		this.log("MESSAGE [from " + username + " @ " + address.getHostName() + "]:\n- " + message);
	} 

	/**
	 * Method called when an user joined to the the Multicast Chat's Client-Side Service's session
	 */
	public void chatParticipantJoined(String username, InetAddress address, int port) {
		this.log("A NEW PARTICIPANT JOINED [" + username + " joined to the group of this Chat's Service from: "
				 + address.getHostName() + ":" + port + "]");
	} 

	/**
	 * Method called when an user left the the Multicast Chat's Client-Side Service's session
	 */
	public void chatParticipantLeft(String username, InetAddress address, 
									int port) {
		log("A PARTICIPANT LEFT [" + username + " left the group of this Chat's Service from: " 
		    + address.getHostName() + ":" + port + "]");
	} 

	/**
	 * Command-line invocation of Multicast Chat's Client-Side Service's session,
	 * expecting at least, thre arguments, at least, with one optional more
	 * 
	 * @param args three arguments, at least, with one optional more
	 *             [ <username> <group address of IP Multicast> <port> { optional: <ttl (time to live)> } ]
	 */
	public static void main(String[] args) {
		
		if((args.length != 3) && (args.length != 4)) {
			System.err.println("Usage: MChatCliente " 
							   + "<username> <group address of IP Multicast> <port> { optional: <ttl (time to live)> }");
			System.err.println("       - TTL (TIME TO LIVE) default value = 1");
			System.exit(1);
		} 

		String username = args[0];
		InetAddress group = null;
		
		int port = -1;
		int timeToLive = 1;

		try {
			group = InetAddress.getByName(args[1]);
		}
		catch (Throwable invalidIPGroupAddressMulticast) {
			System.err.println("Invalid IP Group Address of Multicast: " 
							   + invalidIPGroupAddressMulticast.getMessage());
			System.exit(1);
		} 

		if (!group.isMulticastAddress()) {
			System.err.println("The IP Group Address '" + args[1] 
							   + "' it's not a Multicast IP Address");
			System.exit(1);
		} 

		try {
			port = Integer.parseInt(args[2]);
		}
		catch (NumberFormatException e) {
			System.err.println("Invalid Port: '" + args[2] + "'");
			System.exit(1);
		} 

		if (args.length >= 4) {
			try {
				timeToLive = Integer.parseInt(args[3]);
			}
			catch (NumberFormatException e) {
				System.err.println("Invalid TTL: '" + args[3] + "'");
				System.exit(1); 
			} 
		} 

		try {
			MulticastChatClient multicastChatClientSessionFrame = new MulticastChatClient();
			multicastChatClientSessionFrame.setSize(800, 300);
			multicastChatClientSessionFrame.setVisible(true);

			multicastChatClientSessionFrame.join(username, group, port, timeToLive);
		}
		catch (Throwable errorException) {
			System.err.println("Error occured while the frame of the Multicast Chat's Client-Side Service's session was initialising [" +
								errorException.getClass().getName() + ": " + errorException.getMessage() + "]");
			
			System.exit(1);
		} 
	} 
}