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
	 * The text-area where will be showed the messages of the conversation of this Multicast Chat's Client-Side Service,
	 * including the JOIN message, when someone joins to this Multicast Chat.
	 */
	protected JTextArea conversationMessagesTextArea;

	/**
	 * The text-field where the messages to be sent through the conversation of this Multicast Chat's Client-Side Service,
	 * can be written before be sent.
	 */
	protected JTextField messageToBeSentField;
	
	// Campo de texto onde se dara a entrada do ficheiro a fazer download
	/**
	 * The text-field TODO
	 */
	protected JTextField fileField;
	
	/**
	 * The list hosts/users (clients) currently "online" or active in this Multicast Chat's Client-Side Service.
	 */
	protected DefaultListModel<String> usersInChat;

	// Construtor para uma frame com do chat Multicast (initialised in a state of disconnected)
	
	
	/**
	 * 
	 */
	public MulticastChatClient() {
		
		super("Multicast Chat (Mode: disconnected)");

		// Construct and initialise the components of the Graphics User Interface (Session's Start/Initialising)
		conversationMessagesTextArea = new JTextArea();
		conversationMessagesTextArea.setEditable(false);
		conversationMessagesTextArea.setLineWrap( true);
		conversationMessagesTextArea.setBorder(BorderFactory.createLoweredBevelBorder());

		// Set the Scroll Bars for this Multicast Chat's Client-Side Service
		JScrollPane textAreaScrollPane = new JScrollPane(conversationMessagesTextArea, 
														 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
														 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// TODO
		getContentPane().add(textAreaScrollPane, BorderLayout.CENTER);
		
		this.usersInChat = new DefaultListModel<String>();
		JList<String> usersInChatList = new JList<String>(this.usersInChat);
		
		JScrollPane usersListScrollPane = new JScrollPane(usersInChatList, 
														 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
														 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
				public Dimension getMinimumSize() {
					Dimension d = super.getMinimumSize();
					d.width = 100;
					return d;
				}
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					d.width = 100;
					return d;
				}
			};
		getContentPane().add(usersListScrollPane, BorderLayout.WEST);

		Box box = new Box( BoxLayout.Y_AXIS);
		box.add( Box.createVerticalGlue());
		JPanel messagePanel = new JPanel(new BorderLayout());

		messagePanel.add(new JLabel("Message:"), BorderLayout.WEST);

		messageToBeSentField = new JTextField();
		messageToBeSentField.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			sendMessage();
			}
			});
		messagePanel.add(messageToBeSentField, BorderLayout.CENTER);

		JButton sendButton = new JButton("SEND");
		sendButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			sendMessage();
			}
			});
		messagePanel.add(sendButton, BorderLayout.EAST);
		box.add( messagePanel);

		box.add( Box.createVerticalGlue());
		
		
		JPanel filePanel = new JPanel(new BorderLayout());

		filePanel.add(new JLabel("Not used"), BorderLayout.WEST);
		fileField = new JTextField();
		fileField.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			downloadFile();
			}
			});
		filePanel.add(fileField, BorderLayout.CENTER);

		JButton downloadButton = new JButton("Not Implemented");
		downloadButton.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		downloadFile();
		}
		});
		filePanel.add(downloadButton, BorderLayout.EAST);
		box.add( filePanel);
		
		box.add( Box.createVerticalGlue());
		

		getContentPane().add(box, BorderLayout.SOUTH);

		// detect window closing and terminate multicast chat session
		// detectar o fecho da janela no termino de uma sessao de chat    // 
		addWindowListener( new WindowAdapter() {
			// Invocado na primeira vez que a janela e tornada visivel.
			public void windowOpened(WindowEvent e) {
				messageToBeSentField.requestFocus();
			} 
			// terminar o char a quando do fecho da janela
			public void windowClosing(WindowEvent e) {
				onQuit();
				dispose();
			} 
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
	public void join(String username, InetAddress group, int port, 
					 int ttl) throws IOException {
		setTitle("CHAT MulticastIP " + username + "@" + group.getHostAddress() 
				 + ":" + port + " [TTL=" + ttl + "]");


		
		// Criar sessao de chat multicast
		chat = new MulticastChat(username, group, port, ttl, this);
	} 

	protected void log(final String message) {
		java.util.Date date = new java.util.Date();

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
		String message = messageToBeSentField.getText();
		messageToBeSentField.setText("");
		doSendMessage( message);
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
	protected void downloadFile() {
		final String file = fileField.getText();
		fileField.setText("");
		new Thread( new Runnable() {
			public void run() {
				doDownloadFile( file);
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
	protected void onQuit() {
		try {
			if (chat != null) {
				chat.terminateService();
			} 
		} catch (Throwable ex) {
			JOptionPane.showMessageDialog(this, "Erro no termino do chat:  "
										  + ex.getMessage(), "ERRO no Chat", 
										 JOptionPane.ERROR_MESSAGE);
		} 
	} 


	// Invocado quando s erecebe uma mensagem  // 
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