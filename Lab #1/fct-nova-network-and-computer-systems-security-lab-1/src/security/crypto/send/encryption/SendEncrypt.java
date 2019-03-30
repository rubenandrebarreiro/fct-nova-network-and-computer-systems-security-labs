package security.crypto.send.encryption;

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
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import security.crypto.common.KeyRing;
import security.crypto.common.Utils;

/**
 * Class responsible for Send Encrypt.
 * 
 * Description:
 * - Encrypt a given data of a message and send it, to some host-name and port, using a TCP socket;
 */
public class SendEncrypt {

	
	public static void main(String args[]) throws Exception {
		
		// The default values for <host-name> and for the <port>
		// It's possible to define the <host-name> and the <port> 
		if(args.length != 2) {
			System.out.println("Usage: SendEncrypt <hostname> <port>");
			System.exit(-1);
		}
		
		
		// Global Instance Variables:
		
		// The values for the <host-name> and for the <port>:
		/**
		 * The destionation's host-name
		 */
		String destinationHostname = args[0];
		
		/**
		 * The destination's port
		 * 
		 * NOTE:
	     * - By default, will be used the port '5999' but, it's possible to be changed
		 */
		Integer destinationPort = Integer.parseInt(args[1]);

	   /**
		* The default value for the Cipher's Suite
		* It's possible to define the Cipher's Suite as parameter too
		*/
		String ciphersuite = "AES/CTR/PKCS5Padding";

		/**
		 * The bytes of the Initialising Vector to be used in the process of encryption, by the sender
		 * In this case, it will be used the same Initialising Vector, in both, sender and receiver
		 * 
		 * NOTE:
		 * - The best and correct way of, use Initialising Vectors,
		 *   it's to generate a new one, wind random content, for each Encryption/De-cryption process
		 */
		byte[] initialisingVectorBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
													  0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15 };
		
		// Set the specifications of the Initialising Vector used by the sender
		IvParameterSpec initialisingVectorSpecifications = new IvParameterSpec(initialisingVectorBytes);
		
		System.out.println();
		
		System.out.println("Destination [host-name: " + destinationHostname + "; port:" + destinationPort + "]");
		System.out.println("Cipher's Suite to be used: [" + ciphersuite + "]");

		String plaintext = "START";
		
		// Read the Secret Key, previously accorded between the two end-points
		SecretKey secretKey = KeyRing.readSecretKey();

		// Infinite loop, to handle the continuously prompt of the data of the messages to be sent,
		// and capture its content as 
		// Just stop the process
		for(;;) {
			plaintext = prompt("Plain Text of the Message: ");
			
			if(plaintext.equals("BYE")) {
				break;
			}
			
			byte[] plainTextBytes = plaintext.getBytes();

			System.out.println("--------------------------------------------");
			System.out.println("Plain Text pretended to be send in hexadecimal:");
			System.out.println("- " + Utils.toHex(plainTextBytes, plainTextBytes.length) + "\n");
			System.out.println("----------------------------------------------");

			// Set the Cipher accordingly to the Cipher's Suite, and also, all their respectively,
			// configurations and specifications to be used during the communication
			Cipher cipher = Cipher.getInstance(ciphersuite);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, initialisingVectorSpecifications);
			
			byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
			
			// The Cipher Text in hexadecimal,
			// containing the original message and another configurations required previously,
			// during all the process
			System.out.println("----------------------------------------------");
			System.out.println("Cipher Text to be, really, send in hexadecimal:");
			System.out.println(" - " + Utils.toHex(ciphertext, ciphertext.length)
							   + " [Size: " + ciphertext.length + "]\n");
			System.out.println("----------------------------------------------");

			// Send the cipher text through a TCP socket,
			// defined to the host-name and port of the destination
			Socket socket = new Socket(destinationHostname, destinationPort);
			
			try {
				// Create a Output Stream of Data, where the data of messages will be sent by to the receiver
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				
				dataOutputStream.writeInt(ciphertext.length);
				dataOutputStream.write(ciphertext);
				
				dataOutputStream.close();
			} 
			finally {
				try {
					socket.close();
				}
				catch (Exception e) {
					// Handle of Exceptions in the process of Encrypt and Send the data of the message,
					// as also, the 
					// the closing of the Output Stream, previously created
	    			System.err.print("Error on the process of Encrypt and Send the data of the message, " 
					                  + "as also, maybe in the TCP socket communication being used");
				}
			}
	  }
		
      System.exit(0);
	}

	/**
	 * Returns a prompt and capture the reply's data as a String format.
	 * 
	 * @param prompt to be returned, where the reply's data as a String format, will be captured
	 * 
	 * @return a prompt and capture the reply's data as a String format
	 * 
	 * @throws IOException an Input/Output Exception to be thrown, in the case of, an anomaly occur
	 */
	public static String prompt(String prompt) throws IOException {
		System.out.print(prompt);
		System.out.flush();
		
		BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		String response = inputBufferedReader.readLine();
		System.out.println();
    
		return response;
	} 
}