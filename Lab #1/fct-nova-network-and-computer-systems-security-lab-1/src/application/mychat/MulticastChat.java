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

import java.io.*;
import java.net.*;

/**
 * Class responsible for the Multicast Chat in the Client-Side Application,
 * extending the Java's Thread.
 */
public class MulticastChat extends Thread {
	// Constants/Invariants:

	/**
	 * The number of the JOIN operation to the Multicast Chat's Service.
	 */
	public static final int JOIN = 1;

	/**
	 * The number of the LEAVE operation to the Multicast Chat's Service.
	 */
	public static final int LEAVE = 2;

	/**
	 * The number of a simple/basic processing MESSAGE operation to the Multicast Chat's Service.
	 */
	public static final int MESSAGE = 3;
	
	/**
	 * The magic number that works like an unique ID of the Multicast Chat's Service.
	 */
	public static final long CHAT_MAGIC_NUMBER = 4969756929653643804L;
	
	/**
	 * The time (in milliseconds) to test the pooling of termination of the Socket used in the Multicast Chat's Service.
	 * NOTE: Currently using a time of 5 seconds (5000 milliseconds)
	 */
	public static final int DEFAULT_SOCKET_TIMEOUT_MILLIS = 5000;
	
	
	// Global Instance Variables:
	
	/**
	 * The Multicast's Socket used to send and receive Multicast's Protocol PDUs.
	 * This Multicast's Socket is used to send and receive messages in the scope of
	 * the related operations that can occur in the Chat's Service.
	 */
	protected MulticastSocket multicastSocket;
	
	/**
	 * The Username used by the client/host of the Multicast Chat's Service.
	 */
	protected String username;
	
	/**
	 * The IP Group used by this Multicast Chat's Service.
	 */
	protected InetAddress multicastIPGroup;
	
	/**
	 * The Event Listener to catch events sent through the Multicast Chat's Service.
	 */
	protected MulticastChatEventListener eventListener;
	
	/**
	 * The boolean value too keep information about if the Multicast Chat's Service
	 * it's currently active or not.
	 */
	protected boolean isActive;

	
	// Constructors:

	/**
	 * 
	 * 
	 * @param username
	 * @param multicastIPGroup
	 * @param port
	 * @param timeToLive
	 * @param eventListener
	 * @throws IOException
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

		// start receive thread and send multicast join message
		start();
		this.sendJoin();
	}
	
	/**
	 * Request an asynchronous termination of the execution's thread of the Multicast Chat's Service.
	 * 
	 * @throws IOException an Input/Output Exception to be thrown
	 */
	public void terminate() throws IOException {
		this.isActive = false;
		sendLeave();
	} 

	/**
	 * Issue an error message, in a case of an anomaly occurs.
	 * 
	 * @param errorMessage the error message to be shown
	 */
	protected void error(String errorMessage) {
		System.err.println(new java.util.Date() + ": Multicast Chat's Service: " + errorMessage);
	} 

	/**
	 * Send a JOIN message through the Multicast Chat's Service.
	 * 
	 * @throws IOException an Input/Output Exception to be thrown
	 */
	protected void sendJoin() throws IOException {
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
	 * Process a JOIN message received through the Multicast Chat's Service, with a notification.
	 * 
	 * @param dataInputStream
	 * @param address
	 * @param port
	 * 
	 * @throws IOException
	 */
	protected void processJoin(DataInputStream dataInputStream, InetAddress address, int port) throws IOException {
		String name = dataInputStream.readUTF();

		try {
			this.eventListener.chatParticipantJoined(name, address, port);
		}
		catch (Throwable exception) {}
	} 
	
	/**
	 * Send a LEAVE message through the Multicast Chat's Service.
	 * 
	 * @throws IOException an Input/Output Exception to be thrown
	 */
	protected void sendLeave() throws IOException {

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

  // Processes a multicast chat LEAVE PDU and notifies listeners
  // Processamento de mensagem de LEAVE  // 
  
  
  protected void processLeave(DataInputStream istream, InetAddress address, 
                              int port) throws IOException {
	  String username = istream.readUTF();

	  try {
		  this.eventListener.chatParticipantLeft(username, address, port);
	  }
	  catch (Throwable e) {}
  } 

  // Envio de uma mensagem normal
  // 
  public void sendMessage(String message) throws IOException {

    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    DataOutputStream dataStream = new DataOutputStream(byteStream);

    dataStream.writeLong(CHAT_MAGIC_NUMBER);
    dataStream.writeInt(MESSAGE);
    dataStream.writeUTF(username);
    dataStream.writeUTF(message);
    dataStream.close();

    byte[] data = byteStream.toByteArray();
    DatagramPacket packet = new DatagramPacket(data, data.length, this.multicastIPGroup, 
                                               this.multicastSocket.getLocalPort());
    this.multicastSocket.send(packet);
  } 

  /**
   * Processing of a normal message received through the Multicast Chat's Service.
   * 
   * @param dataInputStream the Data Input Stream where the message it's received
   * @param address the address used to receive 
   * @param port
   * 
   * @throws IOException an Input/Output Exception to be thrown
   */
  	protected void processMessage(DataInputStream dataInputStream, InetAddress address, int port) throws IOException {
  		String username = dataInputStream.readUTF();
  		String message = dataInputStream.readUTF();

  		try {
  			this.eventListener.chatMessageReceived(username, address, port, message);
  		}
  		catch (Throwable inputOrOutputException) {}
  	} 

  	// Loops - recepcao e desmultiplexagem de datagramas de acordo com
  	// as operacoes e mensagens
  	//
  	
  	/**
  	 * 
  	 */
  	public void run() {
  		byte[] buffer = new byte[65508];
  		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

  		while (this.isActive) {
  			try {

  				// Comprimento do DatagramPacket RESET antes do request
  				packet.setLength(buffer.length);
  				this.multicastSocket.receive(packet);

  				DataInputStream dataInputStream = 
  						new DataInputStream(new ByteArrayInputStream(packet.getData(), 
					  						packet.getOffset(), packet.getLength()));

  				long magicNumber = dataInputStream.readLong();

  				// Only continues all the entire process if the Magic Number it's different
  				if(magicNumber != CHAT_MAGIC_NUMBER)
  					continue;
    		
  				int operationCode = dataInputStream.readInt();
        
  				switch (operationCode) {
			  		case JOIN:
			  			processJoin(dataInputStream, packet.getAddress(), packet.getPort());
			  			break;
			  		case LEAVE:
			  			processLeave(dataInputStream, packet.getAddress(), packet.getPort());
			  			break;
			  		case MESSAGE:
			  			processMessage(dataInputStream, packet.getAddress(), packet.getPort());
			  			break;
			  		default:
			  			error("Unknown operation code " + operationCode + " sent by " 
			  				   + packet.getAddress() + ":" + packet.getPort());
  				}
  			}
  			catch (InterruptedIOException interruptedException) {
  				/**
  				 * O timeout e usado apenas para forcar um loopback e testar unknown
  				 * o valor isActive 
  				 */	 
  			}
  			catch (Throwable errorMessage) {
  				error("Processing error: " + errorMessage.getClass().getName() + ": " 
  					   + errorMessage.getMessage());
  			} 
  		} 
  		try {
  			// Close the connection through the socket of the Multicast Chat's Service
  			this.multicastSocket.close();
  		}
  		catch (Throwable e) {}
  	} 
}