package application.mychat;

/**
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
 */
public class MulticastChatClient extends JFrame implements MulticastChatEventListener {
	
	// Constants/Invariants:
	
	/**
	 * The serial version UID defined for this class.
	 */
	private static final long serialVersionUID = 1L;

	
	// Global Instance Variables:
	
	/**
	 * The Multicast Chat's class associated to this Multicast Chat's Client-Side Service.
	 */
	protected MulticastChat chat;

	/**
	 * The text-area where will be showed the messages of the conversation of
	 * this Multicast Chat's Client-Side Service, including the JOIN message,
	 * when someone joins to this Multicast Chat.
	 */
	protected JTextArea conversationMessagesTextArea;

	/**
	 * The text-field where the messages to be sent through the conversation of
	 * this Multicast Chat's Client-Side Service, can be written before be sent.
	 */
	protected JTextField messageToBeSentField;
	
	/**
	 * The text-field where can be placed the file to be able to download.
	 */
	protected JTextField fileToBeDownloadTextField;
	
	/**
	 * The list hosts/users (clients) currently "online" or active in
	 * this Multicast Chat's Client-Side Service.
	 */
	protected DefaultListModel<String> usersInChat;

	
	// Constructors:
	
	/**
	 * Constructor #1:
	 * 
	 * Construct a frame for this Multicast Chat's Client-Side Service
	 * (initialised in a state of Disconnected).
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
		
		// Add the Scroll Bar Pane to the center of the main layout of
		// this Multicast Chat's Client-Side Service
		getContentPane().add(textAreaScrollPane, BorderLayout.CENTER);
		
		// Create the Users' List currently "online" or active to
		// the main layout of this Multicast Chat's Client-Side Service
		this.usersInChat = new DefaultListModel<String>();
		JList<String> usersInChatList = new JList<String>(this.usersInChat);
		
		// Set the component of the Scroll Bars Pane of the
		// Users' List currently "online" or active for
		// this Multicast Chat's Client-Side Service
		JScrollPane usersListScrollPane = new JScrollPane(usersInChatList, 
														  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
														  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
				
				/**
				 * The default serial version UID.
				 */
				private static final long serialVersionUID = 1L;
				
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
		
		// Create the Users' List currently "online" or active to
		// the left side of the main layout of this
		// Multicast Chat's Client-Side Service
		getContentPane().add(usersListScrollPane, BorderLayout.WEST);

		Box mainLayoutBox = new Box(BoxLayout.Y_AXIS);
		mainLayoutBox.add(Box.createVerticalGlue());
		
		// Create the panel of where the messages will be
		// processed and all the related operations occur in
		// the main layout of the Multicast Chat's Client-Side Service
		JPanel messagePanel = new JPanel(new BorderLayout());
		
		// Add the label for the message panel for the text-field
		// for the messages to be sent can be written on the left side of
		// the main layout of this Multicast Chat's Client-Side Service
		messagePanel.add(new JLabel("Message:"), BorderLayout.WEST);
		
		// Create the text-field where can be written the message to
		// be sent to the centre side of the message panel in the
		// main layout of this Multicast Chat's Client-Side Service
		this.messageToBeSentField = new JTextField();
		
		// Add an Event Listener to the text-field where can be written the
		// message to be sent to the centre side of the message panel in the
		// main layout of this Multicast Chat's Client-Side Service
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
		// main layout of this Multicast Chat's Client-Side Service
		messagePanel.add(this.messageToBeSentField, BorderLayout.CENTER);
		
		// Create the label for the SEND button when it's pressed
		// for send the messages and added to the centre side of
		// the message panel in the main layout of this
		// Multicast Chat's Client-Side Service
		JButton sendButton = new JButton("SEND");
				
		// Add an Event Listener to the SEND button when it's pressed
		// for send the messages to be sent to the centre side of
		// the message panel in the main layout of this
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
		// main layout of this Multicast Chat's Client-Side Service
		messagePanel.add(sendButton, BorderLayout.EAST);
		
		// Add the panel of where the messages will be
		// processed and all the related operations occur in
		// the main layout of the Multicast Chat's Client-Side Service
		mainLayoutBox.add(messagePanel);
		mainLayoutBox.add(Box.createVerticalGlue());
		
		// Create the panel where the file can be download in the
		// main layout of this Multicast Chat's Client-Side Service
		JPanel fileToBeDownloadPanel = new JPanel(new BorderLayout());
		
		// Add the label in the right side to the panel where the file can be
		// download in the main layout of this Multicast Chat's Client-Side Service
		fileToBeDownloadPanel.add(new JLabel("Not used"), BorderLayout.WEST);
		
		// Create a text-field related to the title of the file to be download,
		// through the Multicast Chat's Client-Side Service
		this.fileToBeDownloadTextField = new JTextField();
		
		// Add an Event Listener to the text-field related to the
		// title of the file to be download in through the
		// Multicast Chat's Client-Side Service
		this.fileToBeDownloadTextField.addActionListener(new ActionListener() {
			
			/**
			 * Action to be performed related to the text-field
			 * where the title of the file to be download can be viewed.
			 */
			public void actionPerformed(ActionEvent dummFileToBeDownloadedFieldEvent) {
				// The process of send the message through this
				// Multicast Chat's Client-Side Service 
				downloadFile();
			}
		});
		
		// Add the text-field related to the title of the file to be download,
		// to the centre side of the panel of the file to be download in the
		// main layout of this Multicast Chat's Client-Side Service
		fileToBeDownloadPanel.add(this.fileToBeDownloadTextField, BorderLayout.CENTER);

		// Create the button that can be pressed when it's pretended
		// to download a file in this Multicast Chat's Client-Side Service
		JButton downloadButton = new JButton("Not Implemented");
		
		downloadButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downloadFile();
			}
		});
		
		fileToBeDownloadPanel.add(downloadButton, BorderLayout.EAST);
		mainLayoutBox.add(fileToBeDownloadPanel);
		
		mainLayoutBox.add(Box.createVerticalGlue());
		

		getContentPane().add(mainLayoutBox, BorderLayout.SOUTH);

		// detect window closing and terminate multicast chat session
		// detectar o fecho da janela no termino de uma sessao de chat    // 
		addWindowListener(new WindowAdapter() {
		
			// Invocado na primeira vez que a janela e tornada visivel.
			public void windowOpened(WindowEvent e) {
				messageToBeSentField.requestFocus();
			} 
			
			// terminar o char a quando do fecho da janela
			public void windowClosing(WindowEvent e) {
				onQuit();
				dispose();
			}
			
			/**
			 * Close this Multicast Chat's 
			 */
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			} 
		});
	}
	
	/**
	 * Adiciona utilizador no interface do utilizador
	 */
	protected void uiAddUser(String username) {
		this.usersInChat.addElement(username);
	}
	
	/**
	 * Remove utilizador no interface do utilizador.
	 * @return Devolve true se utilizador foi removido.
	 */
	protected boolean uiRemUser(String username) {
		return this.usersInChat.removeElement(username);
	}
	
	/**
	 * Inicializa lista de utilizadores a partir de um iterador -- pode ser usado
	 * obtendo iterador de qualquer estrutura de dados de java
	 */
	protected void uiInitUsers(Iterator<String> usersIterator) {
		this.usersInChat.clear();
		
		// There's currently "online" or active users using the Multicast Chat's Client-Side Service
		if(usersIterator != null) {
			while(usersIterator.hasNext())
				this.usersInChat.addElement(usersIterator.next());
		}
	}
	
	/**
	 * Devolve um Enumeration com o nome dos utilizadores que aparecem no UI.
	 */
	protected Enumeration<String> uiListUsers() {
		return this.usersInChat.elements();
	}
	
	// Configuracao do grupo multicast da sessao de chat na interface do cliente
	/**
	 * Configure the 
	 * 
	 * @param username
	 * @param group
	 * @param port
	 * @param timeToLive
	 * 
	 * @throws IOException
	 */
	public void join(String username, InetAddress group, int port, 
					 int timeToLive) throws IOException {
		
		setTitle("CHAT Multicast IP " + username + "@" + group.getHostAddress() 
				 + ":" + port + " [TTL=" + timeToLive + "]");

		// Criar sessao de chat multicast
		chat = new MulticastChat(username, group, port, timeToLive, this);
	} 
	
	/**
	 * 
	 * 
	 * @param message
	 */
	protected void log(final String message) {
		Date date = new Date();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				conversationMessagesTextArea.append(message + "\n");
			} 
		});
	} 

	/**
	 * Envia mensagem. Chamado quando se carrega no botao de SEND ou se faz ENTER 
	 * na linha da mensagem. 
	 * Executa operacoes relacionadas com interface -- nao modificar
	 */
	protected void sendMessage() {
		String messageToBeSent = messageToBeSentField.getText();
		messageToBeSentField.setText("");
		
		// Send the message through the Multicast Chat's Client-Side Service,
		// previously written in the 
		doSendMessage(messageToBeSent);
		messageToBeSentField.requestFocus();
	}

	/**
	 * Executa operacoes relativas ao envio de mensagens
	 */
	protected void doSendMessage( String message) {
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
	 * Imprime mensagem de erro
	 */
	protected void displayMsg( final String str, final boolean error) {
		final JFrame f = this;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if( error)
					JOptionPane.showMessageDialog(f, str, "Chat Error", JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(f, str, "Chat Information", JOptionPane.INFORMATION_MESSAGE);
			} 
			});
	}

	/**
	 * Pede downlaod dum ficheiro. Chamado quando se carrega no botao de SEND ou se faz ENTER 
	 * na linha de download. 
	 * Executa operacoes relacionadas com interface -- nao modificar
	 */
	
	/**
	 * Ask for the download of a file.
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
	 * Executa operacoes relativas ao envio de mensagens.
	 * 
	 * NOTA: Qualquer informacao ao utilizador deve ser efectuada usando 
	 * o metodo "displayMsg".
	 */
	protected void doDownloadFile( String file) {
		// TODO: a completar
		System.err.println( "Pedido download do ficheiro " + file);
	}

	/**
	 * Chamado quando o utilizador fechou a janela do chat
	 */
	
	/**
	 * 
	 */
	protected void onQuit() {
		try {
			if(chat != null)
				chat.terminateService();
		}
		catch (Throwable ex) {
			JOptionPane.showMessageDialog(this, "Erro no termino do chat:  "
										  + ex.getMessage(), "ERRO no Chat", 
										 JOptionPane.ERROR_MESSAGE);
		} 
	} 

	// Invocado quando s erecebe uma mensagem  // 
	
	/**
	 * 
	 */
	public void chatMessageReceived(String username, InetAddress address, 
									int port, String message) {
		log("MSG:[" + username+"@"+address.getHostName() + "] disse: " + message);
	} 


	// Invocado quando um novo utilizador se juntou ao chat  // 
	public void chatParticipantJoined(String username, InetAddress address, 
									  int port) {
		log("+++ NOVO PARTICIPANTE: " + username + " juntou-se ao grupo do chat a partir de " + address.getHostName()
			+ ":" + port);
	} 

	// Invocado quando um utilizador sai do chat  // 
	public void chatParticipantLeft(String username, InetAddress address, 
									int port) {
		log("--- ABANDONO: " + username + " ababdonou o grupo de chat, a partir de " + address.getHostName() + ":" 
			+ port);
	} 

	// Command-line invocation expecting three arguments
	public static void main(String[] args) {
		
		if((args.length != 3) && (args.length != 4)) {
			System.err.println("Utilizar: MChatCliente " 
							   + "<nickusername> <grupo IPMulticast> <porto> { <ttl> }");
			System.err.println("       - TTL default = 1");
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
		} catch (NumberFormatException e) {
			System.err.println("Invalid Port: " + args[2]);
			System.exit(1);
		} 

		if (args.length >= 4) {
			try {
				timeToLive = Integer.parseInt(args[3]);
			}
			catch (NumberFormatException e) {
				System.err.println("Invalid TTL: " + args[3]);
				System.exit(1); 
			} 
		} 

		try {
			MulticastChatClient frame = new MulticastChatClient();
			frame.setSize(800, 300);
			frame.setVisible( true);

			frame.join(username, group, port, timeToLive);
		}
		catch (Throwable errorException) {
			System.err.println("Error occured while the frame was initialising: " + errorException.getClass().getName() 
							   + ": " + errorException.getMessage());
			
			System.exit(1);
		} 
	} 
}