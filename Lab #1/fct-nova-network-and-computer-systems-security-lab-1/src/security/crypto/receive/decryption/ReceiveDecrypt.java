package security.crypto.receive.decryption;

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
 * Class responsible for Receive De-crypt.
 * 
 * Description:
 * - Receive the data of a message at a certain port and de-crypt its content, using a TCP socket;
 */
public class ReceiveDecrypt {
	
	/**
	 * Main method. Set a Sever's Socket, to be continuously waiting, for incoming data of messages.
	 * For each incoming data of message, process the de-cryption of message and, respectively,
	 * de-ciphering its content.
	 * 
	 * @param args some arguments that can be passed, to define some parameters and settings of the receptor
	 * 
	 * @throws Exception an Exception to be thrown, in the case of, some anomaly occur
	 */
	public static void main(String args[]) throws Exception {

		// Global Instance Variables:
		
		/**
		 * The port used to be expecting the reception of the data of messages received
		 * 
		 * NOTE:
		 * - By default, will be used the port '5999' but, it's possible to be changed
		 */
		Integer port = 5999;

		/**
		 * The default value for the Cipher's Suite
		*  It's possible to define the Cipher's Suite as parameter too
		 * 
		 * NOTE:
		 * - By default, will be used the port '5999' but, it's possible to be changed
		 */
		String cipherSuite = "AES/CTR/PKCS5Padding";

		System.out.println("Possible Usage: ReceiveDecrypt <port> <cipher-suite>");
		System.out.println();
		
		// Read some parameters, passed as arguments
		// The argument read about the port used in the receiver
		if(args.length == 1) {
			port = Integer.parseInt(args[0]);
		}

		// The arguments read about the port and Cipher's Suite used
		if(args.length == 2) {
			port = Integer.parseInt(args[0]);
			cipherSuite = args[1];
		}

		/**
		 * The bytes of the Initialising Vector to be used in the process of de-cryption, by the receiver
		 * In this case, it will be used the same Initialising Vector, in both, sender and receiver
		 * 
		 * NOTE:
		 * - The best and correct way of, use Initialising Vectors,
		 *   it's to generate a new one, wind random content, for each Encryption/De-cryption process
		 */
		byte[] initialisingVectorBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
									                  0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15 };
		
		// Set the specifications of the Initialising Vector used by the receiver
		IvParameterSpec initialisingVectorSpecifications = new IvParameterSpec(initialisingVectorBytes);

		System.out.println();
		
		System.out.println("Waiting data of messages with a cypher text format "
				            + "[@ port '" + port + "' and with the Cipher's Suite of '" + cipherSuite + "']...");
		
		System.out.println();

		// Read the Secret Key, previously accorded between the two end-points
		SecretKey secretKey = KeyRing.readSecretKey();
		byte[] ciphertext = null;

		// Infinite loop, to handle the continuously reception of the data of messages,
		// probably in a cipher text format
		for(;;) {
			
			// Create a new Server's Socket to establish the connection for the communication channel,
			// where the data of messages will be received from the sender
			ServerSocket serverSocket = new ServerSocket(port);

			// Receive a cipher text, probably, related to the data of a message
			try {
				Socket socket = serverSocket.accept();
			
				try {
					// Create a Input Stream of Data, where the data of messages will be received from the sender
					DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        
					ciphertext = new byte[dataInputStream.readInt()];
					dataInputStream.read(ciphertext);

					System.out.println("----------------------------------------------");
					System.out.println("Cipher Text received in hexadecimal format:\n");
					System.out.println("- " + Utils.toHex(ciphertext, ciphertext.length) 
									   + " [Size: " +ciphertext.length + "]\n");
					System.out.println("----------------------------------------------");
				} 
				finally {
					try {
						socket.close();
					}
					catch (Exception e) {
						// Handle of Exceptions in the process of Receive and De-crypt the data of the message,
						// as also, the closing of the TCP Socket, previously created
		    			System.err.print("Error on the process of Receive and Send the data of the message, " 
						                  + "as also, maybe in the TCP Socket communication being used");
					} 
				} 
			} 
			finally {
				try {
					serverSocket.close();
				}
				catch (Exception e) {
					// Handle of Exceptions in the process of Receive and De-crypt the data of the message,
					// as also, the closing of the TCP Server's Socket, previously created
	    			System.err.print("Error on the process of Receive and Send the data of the message, " 
					                  + "as also, maybe in the TCP Server Socket communication being used");
				} 
			}

			System.out.println();
			System.out.println();
			
			// De-cipher the received message
			System.out.println("De-ciphering the received message...");
			
			System.out.println();

			// Initialising the Cipher to be used to do the De-cryption of the message received
			Cipher cipher = Cipher.getInstance(cipherSuite);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, initialisingVectorSpecifications);
  
			// The Plain Text obtained from the Cipher of the data of the message received
			byte[] plainText = new byte[cipher.getOutputSize(ciphertext.length)];
			int plainTextLength = cipher.update(ciphertext, 0, ciphertext.length, plainText, 0);
			
			plainTextLength += cipher.doFinal(plainText, plainTextLength);
			
			// The Plain Text in hexadecimal,
			// containing the original message and another configurations required previously,
			// during all the process
			System.out.println("----------------------------------------------");    
			System.out.println("Plain Text in hexadecimal format:\n");
			System.out.println("- " + Utils.toHex(plainText, plainTextLength)
							   + " [Size: " + plainTextLength + "]");
			System.out.println("----------------------------------------------");    
			
			System.out.println();
			System.out.println();
			
			String originalMessage = new String(plainText);

			// Print the content of the data of the message received,
			// after its de-cryption process and the respectively, de-ciphering
			System.out.println("----------------------------------------------");      
			System.out.println("Original Plain Text of the Message:\n");
			System.out.println("- " + originalMessage);
			System.out.println("----------------------------------------------");      
		}
	} 
}