package password.based.encryption;

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
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import security.crypto.common.Utils;

/**
 * Class responsible for Password Based Encryption Without Parameters Example.
 * 
 * Description:
 * - A simple and basic class and respectively, program, to show an example of
 *   how to use a process of Password Based Encryption without parameters,
 *   using SHA and 3 Key Triple DES (Triple Data Encryption Standard);
 */
public class PasswordBasedEncryptionWithoutParametersExample {

	/**
	 * Main method. Performs a Password Based Encryption without parameters,
	 * using SHA and 3 Key Triple DES (Triple Data Encryption Standard).
	 * 
	 * @param args no arguments
	 * 
	 * @throws Exception an Exception to be thrown, in the case of, an anomaly occur,
	 *         during the Password Based Encryption process
	 */
	public static void main(String[] args) throws Exception {
		
		
		// Global Instance Variables:
		
		// The byte stream of the data of the input
		byte[] inputDataBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
                							 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                							 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };

		// The byte stream input to generate a Secret Key
		byte[] secretKeyBytes = new byte[] { 0x73, 0x2f, 0x2d, 0x33, (byte) 0xc8, 0x01, 0x73, 
											 0x2b, 0x72, 0x06, 0x75, 0x6c, (byte) 0xbd, 0x44, 
											(byte) 0xf9, (byte) 0xc1, (byte) 0xc1, 0x03, (byte) 0xdd, 
											(byte) 0xd9, 0x7c, 0x7c, (byte) 0xbe, (byte) 0x8e };

		// The Initialising Vector bytes to be used
		byte[] initialisingVectorBytes = new byte[] { (byte) 0xb0, 0x7b, (byte) 0xf5, 0x22,
													  (byte) 0xc8, (byte) 0xd6, 0x08, (byte) 0xb8 };
		
		// The Password to use in the Encryption
        char[] passwordEncryption = "password".toCharArray();
        
        // The Salt to use in the Encryption
        byte[] saltEncryption = new byte[] { 0x7d, 0x60, 0x43, 0x5f, 0x02, (byte)0xe9, (byte)0xe0, (byte)0xae };
        
        // The Iteration Count
        int iterationCount = 2048;
		
        // Print the byte stream of the data information used as Input
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Input Data:");
        System.out.println("- " + Utils.toHex(inputDataBytes));
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        System.out.println();
        System.out.println();
        
        // The Password Based Encryption's Secret Key Specifications
        PBEKeySpec passwordBasedEncryptionKeySpecifications = new PBEKeySpec(passwordEncryption, saltEncryption, iterationCount);
        
        // The Secret Key's Factory to generate the Secret Keys
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAnd3KeyTripleDES");
        
        
        // Set the Cipher to be used in the Encryption process, configuring all the parameters
	    // 1) Encryption/De-cryption Algorithm:
	    // - Triple DES (Triple Data Encryption Standard)
	    // 2) Mode of Encryption/De-cryption:
	    // - CBC (Cipher Block Chaining)
	    // 3) Padding of Encryption/De-cryption:
	    // - PKCS7 Padding (or no Padding)
	    // 4) Java Security Provider:
	    // - BC 
		Cipher cipherEncryption = Cipher.getInstance("DESede/CBC/PKCS7Padding", "BC");
		
		// The Secret Key generated
        Key secretKey = keyFactory.generateSecret(passwordBasedEncryptionKeySpecifications);
		
		// The Encryption process and the ciphering of its respectively content
        cipherEncryption.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKeyBytes, "DESede"), new IvParameterSpec(initialisingVectorBytes));

        // The output of the byte stream resulted of the Cipher's Encryption
        byte[] cipheredOutputBytes = cipherEncryption.doFinal(inputDataBytes);
        
        // Print the Plain Text of the de-ciphered data passing through the communication channel
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Cipher Text of the Ciphered Data to be Send Through the Communication Channel:");
        System.out.println("- " + Utils.toHex(cipheredOutputBytes));
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("- Generated Secret Key: " + Utils.toHex(secretKey.getEncoded()));
        System.out.println("- Generated Initialising Vector: " + Utils.toHex(cipherEncryption.getIV()));
        System.out.println("- Secret Key's Format: " + secretKey.getFormat());
        System.out.println("- Secret Key's Algorithm: " + secretKey.getAlgorithm());
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        System.out.println();
        System.out.println();

        // Set the Cipher to be used in the Encryption process, configuring all the parameters
	    // 1) Encryption/De-cryption Algorithm:
	    // - Triple DES (Triple Data Encryption Standard)
	    // 2) Mode of Encryption/De-cryption:
	    // - CBC (Cipher Block Chaining)
	    // 3) Padding of Encryption/De-cryption:
	    // - PKCS7 Padding (or no Padding)
	    // 4) Java Security Provider:
	    // - BC 
        Cipher cipherDecryption = Cipher.getInstance("PBEWithSHAAnd3KeyTripleDES", "BC");
     
        // The De-cryption process and the de-ciphering of its respectively content
        cipherDecryption.init(Cipher.DECRYPT_MODE, secretKey);
        
        // Print the Plain Text of the de-ciphered data passing through the communication channel
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Plain Text of the De-ciphered Data Received Through the Communication Channel:");
        System.out.println("- " + Utils.toHex(cipherDecryption.doFinal(cipheredOutputBytes)));
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("- Generated Secret Key: " + Utils.toHex(secretKey.getEncoded()));
        System.out.println("- Generated Initialising Vector: " + Utils.toHex(cipherDecryption.getIV()));
        System.out.println("- Secret Key's Format: " + secretKey.getFormat());
        System.out.println("- Secret Key's Algorithm: " + secretKey.getAlgorithm());
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
	
        System.out.println();
        System.out.println();
        
        // Print information about the confirmation of the input data,
        // before and after, the Password Based Encryption process
        System.out.println("NOTE:");
        System.out.println("- The Input Data used in Cipher Text and the Plain Text of the De-ciphered Data Received Through the Communication Channel must be equal!!!");
	}
}