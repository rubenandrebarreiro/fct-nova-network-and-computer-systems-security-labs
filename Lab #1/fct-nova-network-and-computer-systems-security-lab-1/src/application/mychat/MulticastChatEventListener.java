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
import java.net.InetAddress;
import java.util.EventListener;

/**
 * Interface responsible for the Multicast Chat in the Client-Side Application's Event Listener,
 * extending the Java's Event Listener.
 */
public interface MulticastChatEventListener extends EventListener {

	
  // Methods/Functions:

  /**
   * Invoked when a Multicast chat message in Client-Side Application has been received.
   * 	
   * @param username the username of the host/user (client)
   * @param host the address of the host/user (client)
   * @param port the port used by host/the user (client) to communicate, send/receive messages,
   * 		join and/or left
   * @param message the message received by the user (client)
   */
  void chatMessageReceived(String username, InetAddress host, int port, String message);

  /**
   * Invoked when a Multicast participant in Client-Side Application has joined.
   * 
   * @param username the username of the host/user (client)
   * @param host the address of the host/user (client)
   * @param port the port used by the host/user (client) to communicate, send/receive messages,
   * 		join and/or left
   */
  void chatParticipantJoined(String username, InetAddress host, int port);

  /**
   * Invoked when a Multicast participant in Client-Side Application has left.
   * 
   * @param username the username of the host/user (client)
   * @param host the address of the host/user (client)
   * @param port the port used by the host/user (client) to communicate, send/receive messages,
   * 		join and/or left the chat
   */
  void chatParticipantLeft(String username, InetAddress host, int port);
}