package application.capitalisation;

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Class responsible for the Capitalise Client-Side Application.
 * 
 * Description:
 * - A simple Swing-Based Client for the
 *   Capitalisation Server Application. It has a main frame window with
 *   a text field for entering strings and a text-area to see the
 *   results of capitalising them;
 */
public class CapitaliseClient {

	// Global Instance Variables:
	
	/**
	 * The Buffered Reader to be used in Input Stream of the Socket
	 */
    private BufferedReader in;
    
    /**
     * The Print Writer to be used in Output Stream of the Socket
     */
    private PrintWriter out;
    
    /**
     * The frame of the window of the Graphics User Interface (GUI)
     */
    private JFrame frame = new JFrame("Capitalise Client");
    
    /**
     * The text-field related to the data field to be
     * used in the layout of the Graphics User Interface (GUI)
     */
    private JTextField dataField = new JTextField(40);

    /**
     * The text-area related to the message area to be 
     * used in the layout of the Graphics User Interface (GUI)
     */
    private JTextArea messageArea = new JTextArea(8, 60);

    
    // Constructors:
    
    /**
     * Constructor #1:
     * 
     * Construct the Client by laying out the Graphics User Interface (GUI) and
     * registering a listener with the text-field so that pressing Enter in the
     * listener sends the text-field contents to the Server;
     */
    public CapitaliseClient() {

        // Set the layout of the Graphics User Interface (GUI)
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");

        /**
         * Add the listeners to the Graphics User Interface (GUI) layout,
         * most precisely to the text-field related to the data field
         */
        dataField.addActionListener(new ActionListener() {
		
	        /**
			 * Respond to pressing the enter key in the text-field
			 * by sending the contents of the text field to the
			 * Server and displaying the response from the Server
			 * in the text area. If the response is "." we exit
			 * the whole application, which closes all sockets,
			 * Streams and windows
			 */
			public void actionPerformed(ActionEvent e) {
			    out.println(dataField.getText());
			    String response;

			    try {
			    	response = in.readLine();
			    	if (response == null || response.equals(""))
			    		System.exit(0);
			    }
			    catch (IOException ex) {
			    	response = "Error: " + ex;
			    }
			    
			    messageArea.append(response + "\n");
			    dataField.selectAll();
			}
	    });
    }
    
    
    // Methods/Functions:
	
 	// 1) Some basic methods:
    
    /**
     * Implement the connection logic by prompting the end user for
     * the Server's IP address, connecting, setting up Streams, and
     * consuming the welcome messages from the Server. The Capitaliser
     * protocol says that the Server sends three lines of text to the
     * Client immediately after establishing a connection
     * 
     * @throws IOException an Input/Output exception to be thrown, in the case of,
     *         occurred in the processing of Input/Output Streams
     */
    public void connectToServer() throws IOException {

        // Get the Server-Side Address from a dialog box
        String serverAddress = JOptionPane.showInputDialog(
							   frame,
							   "Enter the IP Address of the Server:",
							   "Welcome to the Capitalisation Program",
							   JOptionPane.QUESTION_MESSAGE);

        // Make connection and initialise Streams
        @SuppressWarnings("resource")
		Socket socket = new Socket(serverAddress, 9898);
        
        // Initialise and start to use the both Input/Output Streams
        // to be used during the connection
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Consume the initial welcoming messages from the Server-Side Application
        for (int i = 0; i < 3; i++)
            messageArea.append(in.readLine() + "\n");
    }

    /**
     * Main method. Run the Client-Side Application.
     * 
     * @param args the arguments for this main method
     * 
     * @throws Exception the exception to be thrown, in the case of,
     *         occurring an anomaly in the Client-Side Application
     */
    public static void main(String[] args) throws Exception {
        CapitaliseClient client = new CapitaliseClient();
        
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        
        // Establish the connection to the Server-Side Application
        client.connectToServer();
    }
}