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

import java.io.*;
import java.net.*;

/**
 * Class responsible for the Multicast Chat in the Client-Side Application,
 * extending the Java's Thread.
 * 
 * Description:
 * - A Multicast Chat program which accepts the entry of new Clients,
 *   which use the JOIN operation and may accepts, also,
 *   the leave of the same Clients which use the LEAVE operation.
 *   This Multicast Chat program can also, accept the exchange of,
 *   simple and normal messages between the Clients,   
 *   by using the MESSAGE operations;
 * 
 */
public class MulticastChat extends Thread {
	
	// Constants/Invariants:

	/**
	 * The number of the JOIN operation to the Multicast Chat's Service
	 */
	public static final int JOIN = 1;

	/**
	 * The number of the LEAVE operation to the Multicast Chat's Service
	 */
	public static final int LEAVE = 2;

	/**
	 * The number of a simple/basic processing MESSAGE operation to 
	 * the Multicast Chat's Service
	 */
	public static final int MESSAGE = 3;
	
	/**
	 * The magic number that works like an unique ID of the Multicast Chat's Service
	 */
	public static final long CHAT_MAGIC_NUMBER = 4969756929653643804L;
	
	/**
	 * The time (in milliseconds) to test the pooling of termination of
	 * the Socket used in the Multicast Chat's Service
	 * 
	 * NOTE: Currently using a time of 5 seconds (5000 milliseconds)
	 */
	public static final int DEFAULT_SOCKET_TIMEOUT_MILLIS = 5000;
	
	/**
	 * The length of the packet of the messages that is allowed to be received
	 */
	public static final int MESSAGE_PACKET_LENGTH = 65508;
	
	
	// Global Instance Variables:
	
	/**
	 * The Multicast's Socket used to send and receive Multicast's Protocol PDUs
	 * 
	 * This Multicast's Socket is used to send and receive messages in the scope of
	 * the related operations that can occur in the Chat's Service
	 */
	protected MulticastSocket multicastSocket;
	
	/**
	 * The Username used by the host/user (Client) of the Multicast Chat's Service
	 */
	protected String username;
	
	/**
	 * The IP Group used by this Multicast Chat's Service,
	 * that the host/user (Client) pretends to JOIN
	 */
	protected InetAddress multicastIPGroup;
	
	/**
	 * The Event Listener to catch events sent through the Multicast Chat's Service
	 */
	protected MulticastChatEventListener eventListener;
	
	/**
	 * The boolean value too keep information about if the Multicast Chat's Service
	 * it's currently active or not
	 */
	protected boolean isActive;

	
	// Constructors:

	/**
	 * Constructor #1:
	 * 
	 * Constructor the Multicast Chat in the Client-Side Application.
	 * 
	 * @param username the Username used by the host/user (Client) of the Multicast Chat's Service
	 * @param multicastIPGroup the IP Group used by this Multicast Chat's Service,
	 * 		  that the host/user (Client) pretends to join
	 * @param port the port to communicate in this Multicast Chat's Service
	 * @param timeToLive the Time To Live (TTL) associated to this Multicast Chat's Service
	 * @param eventListener the Event Listener to catch events sent through the Multicast Chat's Service
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
	public MulticastChat(String username, InetAddress multicastIPGroup, int port, 
                       int timeToLive, MulticastChatEventListener eventListener) throws IOException {

		this.username = username;
		this.multicastIPGroup = multicastIPGroup;
		this.eventListener = eventListener;
		this.isActive = true;

		// Create and Configure the Multicast Socket to be used by the Multicast Chat's Service
		this.multicastSocket = new MulticastSocket(port);
		this.multicastSocket.setSoTimeout(DEFAULT_SOCKET_TIMEOUT_MILLIS);
		this.multicastSocket.setTimeToLive(timeToLive);
		this.multicastSocket.joinGroup(this.multicastIPGroup);

		// Start the process of Receiving Messages in this extended Thread and send also,
		// a JOIN message by a Multicast communication
		start();
		this.sendJoinMessage();
	}
	
	
	// Methods/Functions:
	
	// 1) Some basic methods:
	
	/**
	 * Request an asynchronous termination of the execution's thread of the Multicast Chat's Service.
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
	public void terminateService() throws IOException {
		this.isActive = false;
		
		// Send a LEAVE message by the Multicast communication in use
		sendLeaveMessage();
	} 

	/**
	 * Issue and print an error message, in a case of an anomaly occurs.
	 * 
	 * @param errorMessage the error message to be shown
	 */
	protected void printErrorMessage(String errorMessage) {
		System.err.println(new java.util.Date() + ": Multicast Chat's Service: " + errorMessage);
	} 

	
	// 2) Communications/Messages methods:
	
	/**
	 * Send a JOIN message through the Multicast Chat's Service, to the other Event Listeners.
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
	protected void sendJoinMessage() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);

		dataStream.writeLong(CHAT_MAGIC_NUMBER);
		dataStream.writeInt(JOIN);
		dataStream.writeUTF(username);
		dataStream.close();

		byte[] data = byteStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, this.multicastIPGroup,
												   		 this.multicastSocket.getLocalPort());
		
		// Send the Datagram Packet related to the JOIN message to be sent through the Multicast Chat's Service
		this.multicastSocket.send(packet);
	} 

	/**
	 * Process a JOIN message received through the Multicast Chat's Service, with a notification to the other Event Listeners.
	 * 
	 * @param dataInputStream the Data Input Stream which through the 
	 * @param address the address to where the JOIN message will be sent
	 * @param port the port used for do the exchange of messages in this communication
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
	protected void processJoinMessage(DataInputStream dataInputStream, InetAddress address, int port) throws IOException {
		String name = dataInputStream.readUTF();

		try {
			this.eventListener.chatParticipantJoined(name, address, port);
		}
		catch (Throwable exception) {}
	} 
	
	/**
	 * Send a LEAVE message through the Multicast Chat's Service, to the other Event Listeners.
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
	protected void sendLeaveMessage() throws IOException {

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);

		dataStream.writeLong(CHAT_MAGIC_NUMBER);
		dataStream.writeInt(LEAVE);
		dataStream.writeUTF(username);
		dataStream.close();

		byte[] data = byteStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, this.multicastIPGroup, this.multicastSocket.getLocalPort());
		
		this.multicastSocket.send(packet);
	}
	
	/**
	 * Process a LEAVE message received through the Multicast Chat's Service, with a notification to the others Event Listeners.
	 * 
	 * @param dataInputStream the Data Input Stream which through the 
	 * @param address the address to where the JOIN message will be sent
	 * @param port the port used for do the exchange of messages in this communication
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
	protected void processLeaveMessage(DataInputStream istream, InetAddress address, int port) throws IOException {
		String username = istream.readUTF();

		try {
			this.eventListener.chatParticipantLeft(username, address, port);
		}
		catch (Throwable e) {}
	} 

	/**
	 * Send a normal message through the Multicast Chat's Service, to the other Event Listeners.
	 * 
	 * @param message the message pretended to be sent through the Multicast Chat's Service
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
	public void sendNormalMessage(String message) throws IOException {

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteOutputStream);

		dataOutputStream.writeLong(CHAT_MAGIC_NUMBER);
		dataOutputStream.writeInt(MESSAGE);
		dataOutputStream.writeUTF(username);
		dataOutputStream.writeUTF(message);
    
		dataOutputStream.close();

		byte[] data = byteOutputStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, this.multicastIPGroup, 
                                               this.multicastSocket.getLocalPort());
		
		this.multicastSocket.send(packet);
	}

	/**
	 * Processing of a normal message received through the Multicast Chat's Service.
	 * 
	 * @param dataInputStream the Data Input Stream which through the 
	 * @param address the address to where the JOIN message will be sent
	 * @param port the port used for do the exchange of messages in this communication
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occurs
	 */
  	protected void processNormalMessage(DataInputStream dataInputStream, InetAddress address, int port) throws IOException {
  		
  		String username = dataInputStream.readUTF();
  		String message = dataInputStream.readUTF();

  		try {
  			this.eventListener.chatMessageReceived(username, address, port, message);
  		}
  		catch (Throwable inputOrOutputException) {}
  	}
  	
  	/**
  	 * The main process of this Multicast Chat's Service.
  	 * This service, since the moment that it's started,
  	 * starts waiting the receive of messages using the Multicast communication
  	 * associated to this Service, accordingly to the IP Group used by
  	 * this Multicast Chat's Service.
  	 * 
  	 * Every time that a message is received, this process will de-multiplex the
  	 * Datagram associated to the message receive, accordingly to the
  	 * operations and messages defined and allowed by this Service.
  	 */
  	public void run() {
  		
  		byte[] buffer = new byte[MESSAGE_PACKET_LENGTH];
  		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

  		while (this.isActive) {
  			try {

  				// Set the length of the packet that is allowed to be received
  				// For each iteration, this length is reseted
  				packet.setLength(buffer.length);
  				this.multicastSocket.receive(packet);

  				DataInputStream dataInputStream = 
  						new DataInputStream(new ByteArrayInputStream(packet.getData(), 
					  						packet.getOffset(), packet.getLength()));

  				long magicNumber = dataInputStream.readLong();

  				// Only continues all the entire process if the Magic Numbers are different
  				if(magicNumber != CHAT_MAGIC_NUMBER)
  					continue;
    		
  				int operationCode = dataInputStream.readInt();
  				
  				// The operations and messages defined and allowed by this Service
  				switch (operationCode) {
			  		case JOIN:
			  			this.processJoinMessage(dataInputStream, packet.getAddress(), packet.getPort());
			  			break;
			  		case LEAVE:
			  			this.processLeaveMessage(dataInputStream, packet.getAddress(), packet.getPort());
			  			break;
			  		case MESSAGE:
			  			this.processNormalMessage(dataInputStream, packet.getAddress(), packet.getPort());
			  			break;
			  		default:
			  			this.printErrorMessage("Unknown operation code " + operationCode + " sent by " 
			  				   + packet.getAddress() + ":" + packet.getPort());
  				}
  			}
  			catch (InterruptedIOException interruptedIOException) {
  				// The timeout used it's just to force a loop-back and test unknown operations
  			}
  			catch (Throwable errorMessage) {
  				this.printErrorMessage("Processing error: " + errorMessage.getClass().getName() + ": " 
  					   + errorMessage.getMessage());
  			} 
  		} 
  		try {
  			// Close the connection through the socket of the Multicast Chat's Service
  			this.multicastSocket.close();
  		}
  		catch (Throwable e) {
  			// Empty process
  		}
  	} 
}