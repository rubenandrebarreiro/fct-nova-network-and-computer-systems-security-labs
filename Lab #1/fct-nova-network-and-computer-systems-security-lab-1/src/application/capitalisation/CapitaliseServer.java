package application.capitalisation;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class responsible for the Capitalise Server-Side.
 * 
 * A server program which accepts requests from clients to
 * capitalise strings. When clients connect, a new thread is
 * started to handle an interactive dialog in which the client
 * sends in a string and the server thread sends back the
 * capitalised version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform
 * dependent. If you ran it from a console window with the "java"
 * interpreter, "Ctrl+C" generally will shut it down.
 */
public class CapitaliseServer {
    
	/**
	 * Application method to run the server runs in an infinite loop
     * listening on port 9898. When a connection is requested, it
     * spawns a new thread to do the servicing and immediately returns
     * to listening. The server keeps a unique client number for each
     * client that connects just to show interesting logging
     * messages. It's certainly not necessary to do this.
	 * 
	 * @param args some arguments formated as strings
	 * @throws Exception some exception if the connection fail for some reason
	 */
	public static void main(String[] args) throws Exception {
        System.out.println("The Capitalisation Server-Side is running.");
        
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        
        try {
            while (true)
            	// Accepting connections from the Client-Side Application
                new Capitalizer(listener.accept(), clientNumber++).start();
        }
        finally {
            listener.close();
        }
    }

    /**
     * A private thread to handle capitalisation requests on a particular socket.
     * The Client-Side Application terminates the dialogue by sending a single line
     * containing only a period (".").
     */
    private static class Capitalizer extends Thread {
       
    	private Socket socket;
        private int clientNumber;

        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            
            this.printLog("New connection with client #" + clientNumber + " at " + socket);
        }

        /**
         * Offer services to this thread's Client by first sending to the
         * Client-Side Application, a welcome message then repeatedly reading
         * strings and sending back the capitalised version of the string.
         */
        public void run() {
            try {

                // Decorate the both streams so we can send characters
                // and not just bytes. Ensure output is flushed
                // after every newline.
                BufferedReader in =
                		new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                PrintWriter out =
                		new PrintWriter(this.socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println("Enter a line with only a period ('.') to quit!\n");

                // Get messages from the Client-Side Application, line by line
                // Return them capitalised
                while (true) {
                    String input = in.readLine();
                    
                    // Stop receiving the messages from Client-Side Application
                    if (input == null || input.equals("."))
                        break;
                   
                    out.println(input.toUpperCase());
                }
            }
            catch (IOException ioException) {
            	this.printLog("Error handling client #" + clientNumber + ": " + ioException);
            }
            finally {
            	try {
                    this.socket.close();
                }
                catch (IOException e) {
                	this.printLog("Couldn't close a socket, what's going on?!");
                }
                
            	this.printLog("Connection with client #" + clientNumber + " closed!");
            }
        }

        /**
         * Log a simple message. In this case, we just write the
         * message to the Server-Side Applications' standard output.
         */
        private void printLog(String message) {
            System.out.println(message);
        }
    }
}