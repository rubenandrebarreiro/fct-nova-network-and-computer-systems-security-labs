package secure.macs.tampering.attack.simple;

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


import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import security.crypto.common.UtilsExtended;

/**
 * Class responsible for Simple Tampering Attack Example 2.
 * 
 * Description:
 * - A simple and basic class and respectively, program, to show an example of
 *   a Tampering Active Attack;
 */
public class SimpleTamperingAttackExample2 {
	/**
     * Main method. Simulates a Tampering Active Attack made by Mallory to a money transaction to
	 * a Bank Account made by Alice to Bob.
     * 
     * @param args no arguments
     * 
     * @throws Exception an Exception to be thrown, in the case of, an anomaly occur, during the simulation of
	 *         the Tampering Active Attack to a Encryption/De-cryption processes
     */
	public static void main(String[] args) throws Exception {
		
    	// The source/seed of a secure random
		SecureRandom secureRandom = new SecureRandom();
        
    	// The Initialising Vector and its parameter specifications
		IvParameterSpec initialVectorParameterSpecifications = UtilsExtended.createCtrIvForAES(1, secureRandom);
		
		// The Secret Key specifications generated to be
    	// used in this Encryption/De-cryption processes
    	// using the AES (Advanced Encryption Standard - Rijndael) Algorithm,
		// with CTR mode byte oriented (byte counter oriented), no Padding and
		// a Secret Key of 256-bit length/size, from the Bouncy Castle Provider API
		Key secretKey = UtilsExtended.createKeyForAES(256, secureRandom);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        
        // The input data of a Bank Account's transaction
    	String inputTransactionData = "Transfer 0,000,100 Euros to Bank Account no. 1234-5678";
		
    	// Print the input data of a Bank Account's transaction
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
    	System.out.println("Input Data of the Back Account's Transaction made by Alice:");
    	System.out.println("- " + inputTransactionData);
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
    	
    	System.out.println();
    	System.out.println();
    	
    	// The (correct) Encryption process made by Alice and the ciphering of its respectively content
    	// Cipher =  Message|Hash(Message)
    	cipher.init(Cipher.ENCRYPT_MODE, secretKey, initialVectorParameterSpecifications);
   
    	byte[] cipherText = cipher.doFinal(UtilsExtended.toByteArray(inputTransactionData));
    	
    	// Print the Cipher Text passing through the communication channel
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Cipher Text Passing Through the Communication Channel:");
        System.out.println("- " + UtilsExtended.toHex(cipherText));
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
      	
      	System.out.println();
      	System.out.println();
      	
      	
      	// THE SIMULATION OF THE TAMPERING ATTACK
        
        /************************************************************/
        // The Tampering Attack made by Mallory (MiM) to a Back Account's transaction 
        // Changing the byte no. 9 from the value 0 to the value 9:
        // - Changing the amount of money of the Back Account's transaction,
        //   from 0,000,100 Euros to 9,000,100 Euros;
        // Changing the byte no. 47 from the value 3 to the value 0:
        // - Changing the Bank Account's no.,
        //   from 1234-5678 to 1204-5678;
        //   (Something happened!!!)
        
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
      	System.out.println("MiM Attack in Communication Channel:");
        System.out.println("- Message Interception and Message Tampered!!!");

        System.out.println();
        System.out.println("Performing the changes of the Tampering Attack (Hacking)...");
      	System.out.println();
        
      	// Performing the changes of the Tampering Attack (Hacking)
        cipherText[9] ^= '0' ^ '9';
        cipherText[47] ^= '3' ^ '0';
        
        System.out.println("Cipher Text Tampered:");
        System.out.println("- " + UtilsExtended.toHex(cipherText));
    
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");

        /************************************************************/

    	
    	System.out.println();
      	System.out.println();
      	
      	// The (correct) De-cryption process made by Bob and the de-ciphering of its respectively content
        cipher.init(Cipher.DECRYPT_MODE, secretKey, initialVectorParameterSpecifications);
        
        // The Plain Text of the data received by Bob related to Bank Account's transaction,
        // supposed made by Alice
        byte[] plainText = cipher.doFinal(cipherText);
        
        // Print the Plain Text of the data received by Bob related to Bank Account's transaction,
        // supposed made by Alice (verifying it the value of the Hash Functions of both messages are equal)
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Plain Text of the Received Data of the Back Account Transaction by Bob:");
    	System.out.println("- " + UtilsExtended.toString(plainText));
    	System.out.println("----------------------------------------------------------------------------------------------------------------------");    
	}
}