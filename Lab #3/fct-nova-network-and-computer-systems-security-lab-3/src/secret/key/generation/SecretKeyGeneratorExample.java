package secret.key.generation;

/**
*
* Network and Computer Systems Security
* 
* Practical Lab #3.
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

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import security.crypto.common.Utils;

/**
 * Class responsible for Secret Key Generator Example.
 * 
 * Description:
 * - A simple and basic class and respectively, program, to show an example of
 *   how to use a Secret Key Generator, using AES (Advanced Encryption Standard - Rijndael)
 *   Symmetric Encryption/De-cryption with CBC Mode, {some-padding} and Secret Key of 192-bit size;
 */
public class SecretKeyGeneratorExample {
	
	/**
	 * Main method. Performs a generation of Secret Key using AES (Advanced Encryption Standard - Rijndael)
     * Symmetric Encryption/De-cryption with CBC Mode, {some-padding} and Secret Key of 192-bit size.
	 * 
	 * @param args no arguments
	 * 
	 * @throws Exception an Exception to be thrown, in the case of, an anomaly occur,
	 * 	       during the generation process of a Secret Key
	 */
	public static void main(String[] args) throws Exception {
		
		
		// Global Instance Variables:
		
		// The byte stream of the data of the input
		byte[] inputDataBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
                							 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                							 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };

		// The Initialising Vector bytes to be used
		byte[] initialisingVectorBytes = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x04, 0x05, 0x06, 0x07,
                									  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };

		// NOTE:
		// - In this example, it will be used the AES (Advanced Encryption Standard - Rijndael) Algorithm,
		//   with CBC mode and without Padding, accordingly with the Security Provider indicated
		
		// Set the Cipher to be used, configuring all the parameters
	    // 1) Encryption/De-cryption Algorithm:
	    // - AES Symmetric Encryption/De-cryption
	    //   (Advanced Encryption Standard/Rijndael Algorithm)
	    // 2) Mode of Encryption/De-cryption:
	    // - CBC (Cipher Block Chaining)
	    // 3) Padding of Encryption/De-cryption:
	    // - PKCS5 Padding (or no Padding)
	    // 4) Java Security Provider:
	    // - SunJCE
		//Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        //Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
		// The Key Generator to be used, accordingly to the instance of
		// AES (Advanced Encryption Standard - Rijndael) Algorithm
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

		// Initialise the Key Generator, to be able to,
		// generate Secret Keys with 192-bits size
		keyGenerator.init(192);
		
		// Generate Secret Key for the Encryption process
		Key encryptionKey = keyGenerator.generateKey();
		
		// Print the information related to the Secret Key generated previously
		System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Generated Secret Key:");
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Crypto's Algorithm: " + encryptionKey.getAlgorithm());
        System.out.println("Format: " + encryptionKey.getFormat());
	    System.out.println("Bytes Encoded: " + Utils.toHex(encryptionKey.getEncoded()));
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Input Data: " + Utils.toHex(inputDataBytes));
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        
        System.out.println();
        System.out.println();
        
        // The Encryption process and the ciphering of its respectively content
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new IvParameterSpec(initialisingVectorBytes));
        
        byte[] cipherText = new byte[cipher.getOutputSize(inputDataBytes.length)];
        
        int cipherTextLength = cipher.update(inputDataBytes, 0, inputDataBytes.length, cipherText, 0);
        cipherTextLength += cipher.doFinal(cipherText, cipherTextLength);
        
        // Print the Cipher Text passing through the communication channel
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Cipher Text Passing Through the Communication Channel:");
        System.out.println("- " + Utils.toHex(cipherText, cipherTextLength));
        System.out.println("- [Size in Bytes: " + cipherTextLength + "]");
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
      	
      	System.out.println();
      	System.out.println();
        
        // The De-cryption process and the de-ciphering of its respectively content
      	Key	decryptionKey = new SecretKeySpec(encryptionKey.getEncoded(), encryptionKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, decryptionKey, new IvParameterSpec(initialisingVectorBytes));
        
        // The Plain Text of the bytes of the data input received through the communication channel
      	byte[] plainText = new byte[cipher.getOutputSize(cipherTextLength)];
        
      	int plainTextLength = cipher.update(cipherText, 0, cipherTextLength, plainText, 0);
      	plainTextLength += cipher.doFinal(plainText, plainTextLength);
        
        // Print the Plain Text passing through the communication channel
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Plain Text Received Through the Communication Channel:");
        System.out.println("- " + Utils.toHex(plainText, plainTextLength));
        System.out.println("- [Size in Bytes: " + plainTextLength + "]");
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
	}
}
