package block.ciphers.modes.ecb;

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
 * Class responsible for Simple ECB Mode Example.
 * 
 * Description:
 * - A simple and basic class and respectively, program, to show an example of
 *   an AES (Advanced Encryption Standard - Rijndael) Symmetric Encryption/De-cryption
 *   with ECB Mode, {some-padding} and Secret Key of 256-bit size;
 *   
 * Problem:
 * - If the Plain Text is equal, implies the same equal cipher too;
 */
public class SimpleECBModeExample {
	
	/**
	 * Main method. Performs an AES (Advanced Encryption Standard - Rijndael) Symmetric Encryption/De-cryption
	 * with ECB Mode, {some-padding} and Secret Key of 256-bit size.
	 * 
	 * @param args no arguments
	 * 
	 * @throws Exception an Exception to be thrown, in the case of, an anomaly occur,
	 * 	       during the AES (Advanced Encryption Standard - Rijndael)
	 * 		   Symmetric Encryption/De-cryption
	 */
	public static void main(String[] args) throws Exception {
		
		// NOTE:
		// Input lengths/block size available/allowed to the
		// AES (Advanced Encryption Standard - Rijhanel)
		// Symmetric Encryption/De-cryption Algorithm:
		// - 128, 192, 256 (Multiples of 16)
		
		
		// Global Instance Variables:
		
		// The byte stream of the data of the input
		byte[] inputDataBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
											 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 
											 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
											 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
		
		// The byte stream input to generate a Secret Key
		// ( 4 x 8 = 32 bytes = 32 x 8 = 256 bits ),
		// because 1 byte is equal to 8 bits
		byte[] secretKeyBytes = new byte[] { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, 
	            							 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, 
	            							(byte) 0xcd, (byte) 0xef, (byte) 0xfa, (byte) 0xc9 };
		
		// Set the Secret Key and its specifications,
		// using the AES (Advanced Encryption Standard - Rijndael) Symmetric Encryption/De-cryption
	    SecretKeySpec secretKeySpecifications = new SecretKeySpec(secretKeyBytes, "AES");
	    
	    // Set the Cipher to be used, configuring all the parameters
	    // 1) Encryption/De-cryption Algorithm:
	    // - AES Symmetric Encryption/De-cryption
	    //   (Advanced Encryption Standard/Rijndael Algorithm)
	    // 2) Mode of Encryption/De-cryption:
	    // - ECB (Electronic Code Book)
	    // 3) Padding of Encryption/De-cryption:
	    // - PKCS5 Padding (or no Padding)
	    // 4) Java Security Provider:
	    // - SunJCE
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
	    //Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "SunJCE");
	    //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");

	    // Print the Secret Key that will be used in the Cipher and the bytes of the data input
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
	    System.out.println("Secret Key:");
	    System.out.println("- " + Utils.toHex(secretKeyBytes));
        System.out.println("Input Data:");
        System.out.println("- " + Utils.toHex(inputDataBytes));
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
	    
        System.out.println();
        System.out.println();
        
        // The Encryption process and the ciphering of its respectively content
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpecifications);
        
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
      	cipher.init(Cipher.DECRYPT_MODE, secretKeySpecifications);
        
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