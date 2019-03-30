package stream.ciphers;

/**
*
* Network and Computer Systems Security
* 
* Practical Lab #2.
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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import security.crypto.common.Utils;

/**
 * Class responsible for Simple Stream Example.
 * 
 * Description:
 * - A simple and basic class and respectively, program, to show an example of a simple stream
 *   Encryption/De-cryption processes and, the ciphering and de-ciphering of the content involved;
 *   
 * NOTE:
 * - This type of Encryption/De-cryption (RC4 - byte oriented)
 *   it's more used for communications of radio, per example;
 */
public class SimpleStreamExample {
	
	/**
	 * Main method. Show an example of a simple stream Encryption/De-cryption processes and,
	 * the ciphering and de-ciphering of the content involved
	 * 
	 * @param args no arguments
	 * 
	 * @throws Exception an Exception to be thrown, in the case of, some anomaly occur,
	 *         during the processes of Encryption/De-cryption 
	 */
	public static void main(String[] args) throws Exception {
		
		// The bytes of the input of data to be sent
		byte[] inputDataBytes = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 
								   (byte) 0x88, (byte) 0x99, (byte) 0xaa, (byte) 0xbb,
								   (byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff };
        
		// The bytes of the Secret Key used for the Encrpytion/De-cryption processes 
        byte[] secretKeyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                							 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        
        // Set the specifications of the Secret Key to be used for the Encrpytion/De-cryption processes 
        SecretKeySpec secretKeySpecifications = new SecretKeySpec(secretKeyBytes, "RC4");
        Cipher cipher = Cipher.getInstance("RC4");
        
        // Print the Plain Text of the bytes of the input data, in the sender-side
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Plain Text of the Bytes of the Input of Data in hexadecimal:\n");
        System.out.println("- " + Utils.toHex(inputDataBytes) + "\n");
        System.out.println("-----------------------------------------------------------------");
        
        System.out.println();
        System.out.println();
        
        // Initialising the Encryption process and the ciphering of the content of the Plain Text
        byte[] cipherText = new byte[inputDataBytes.length];
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpecifications);
    
        int cipherTextLength = cipher.update(inputDataBytes, 0, inputDataBytes.length, cipherText, 0);
        
        cipherTextLength += cipher.doFinal(cipherText, cipherTextLength);
    
        // Print the Cipher Text of the bytes of the input data, in the sender-side
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Cipher Text of the Bytes of the Input Data in hexadecimal:\n");
        System.out.println("- " + Utils.toHex(cipherText) + " [Bytes: " + cipherTextLength + "]\n");
        System.out.println("-----------------------------------------------------------------");
        
        System.out.println();
        System.out.println();
        
        // Initialising the De-cryption process and the de-ciphering of the content of the Cipher Text
        byte[] plainText = new byte[cipherTextLength];
        
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecifications);
        
        int plainTextLength = cipher.update(cipherText, 0, cipherTextLength, plainText, 0);
        
        plainTextLength += cipher.doFinal(plainText, plainTextLength);
        
        // Print the Plain Text of the bytes of the data received, in the receiver-side
        System.out.println("----------------------------------------------");    
        System.out.println("Plain Text of the Bytes of the Data Received in hexadecimal:\n");
        System.out.println("- " + Utils.toHex(plainText) + " [Bytes: " + plainTextLength + "]\n");
        System.out.println("----------------------------------------------");
    }
}