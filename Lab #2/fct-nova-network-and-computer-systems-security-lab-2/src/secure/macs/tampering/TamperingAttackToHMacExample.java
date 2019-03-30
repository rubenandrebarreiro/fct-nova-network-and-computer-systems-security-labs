package secure.macs.tampering;

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
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import security.crypto.common.UtilsExtended;

/**
 * Class responsible for Tampering Attack To Mac Example.
 * 
 * Description:
 * - A simple and basic class and respectively, program, to show an example of a Tampering Attack to
 *   a HMacSHA256 Algorithm - Message Digest Encryption/De-cryption processes;
 *   
 * NOTE:
 * - This type of Encryption/De-cryption (HMacSHA256 Algorithm - Message Digest)
 *   it's more used for communications of exchanging of messages or transactions, per example;
 * - Message tampering - Cipher with synthesis, AES (Advanced Encryption Standard - Rijndael)
 *   and CTR mode byte oriented (byte counter oriented);
 * - Encryption of most of transactions type NEVER should be done byte oriented (byte counter oriented),
 *   but block oriented, because it's always more safe;
 * - Encryption byte oriented (byte counter oriented) should be used in Chat's service in
 *   real time and similar type of applications;
 */
public class TamperingAttackToHMacExample {
	
	/**
	 * Main method. Simulates a Tampering Attack made by Mallory to a money transaction to
	 * a Bank Account made by of Alice.
	 * 
	 * @param args no arguments
	 * 
	 * @throws Exception to be thrown, in the case of, an anomaly occur, during the simulation of
	 *         the Tampering Attack to a HMacSHA256 Algorithm - Message Digest Encryption/De-cryption processes
	 */
    public static void main(String[] args) throws Exception {
    	
    	// The source/seed of a secure random
    	SecureRandom secureRandom = new SecureRandom();
    	
    	// The Initialising Vector and its parameters specifications
    	IvParameterSpec initialisationVectorSpecifications = UtilsExtended.createCtrIvForAES(1, secureRandom);
    	
    	// The 1st Secret Key specifications generated to be
    	// used in this Message Digest Encryption/De-cryption processes
    	// using CTR mode byte oriented (byte counter oriented);
    	Key secretKey = UtilsExtended.createKeyForAES(256, secureRandom);
    	Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    	
    	// The input data of a Bank Account's transaction
    	String inputTransactionData = "Transfer 0,000,100 Euros to Bank Account no. 1234-5678";
        	
    	// The HMacSHA256 Algorithm's instance
    	Mac hMac = Mac.getInstance("HMacSHA256");
      
    	// The 2nd Secret Key specifications generated to be
    	// used in this Message Digest Encryption/De-cryption processes
    	Key hMacSecretKey = new SecretKeySpec(secretKey.getEncoded(), "HMacSHA256");
        
    	// Print the input data of a Bank Account's transaction
    	System.out.println("-----------------------------------------------------------------------------");
    	System.out.println("Input Data of the Back Account's Transaction made by Alice:");
    	System.out.println("- " + inputTransactionData);
    	System.out.println("-----------------------------------------------------------------------------");
    	
    	System.out.println();
    	System.out.println();
        
        // The (correct) Encryption process made by Alice and the ciphering of its respectively content
    	// Cipher = Message|Hash(Message) TODO
    	cipher.init(Cipher.ENCRYPT_MODE, secretKey, initialisationVectorSpecifications);
        
        byte[] cipherText = new byte[cipher.getOutputSize(inputTransactionData.length() + hMac.getMacLength())];

        int cipherTextLength = cipher.update(UtilsExtended.toByteArray(inputTransactionData), 0, inputTransactionData.length(), cipherText, 0);
       
        // The initialising of the HMacSHA256 Algorithm's instance,
        // accordingly with the Secret Key generated previously
        hMac.init(hMacSecretKey);
        hMac.update(UtilsExtended.toByteArray(inputTransactionData));
      
        // The length of bytes resulting of the Cipher Text
        cipherTextLength += cipher.doFinal(hMac.doFinal(), 0, hMac.getMacLength(), cipherText, cipherTextLength);

        
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
        
        // Performing the changes of the Tampering Attack (Hacking)
        cipherText[9] ^= '0' ^ '9';
        cipherText[47] ^= '3' ^ '0';
        
        // NOTE:
        // - Replace the synthesis (But how?!)
        /************************************************************/
        	
        
        // The (correct) De-cryption process made by Bob and the de-ciphering of its respectively content
        cipher.init(Cipher.DECRYPT_MODE, secretKey, initialisationVectorSpecifications);
        
        // The Plain Text of the data received by Bob related to Bank Account's transaction,
        // supposed made by Alice
        byte[] plainText = cipher.doFinal(cipherText, 0, cipherTextLength);
        int messageLength = plainText.length - hMac.getMacLength();
        
        // The initialising of the HMacSHA256 Algorithm's instance, with the received Secret Key of
        // the HMacSHA256 Algorithm's instance
        hMac.init(hMacSecretKey);
        hMac.update(plainText, 0, messageLength);
        
        // The Hash of the message calculated by a Hash-Function of the HMacSHA256 Algorithm's instance,
        // created previously
        byte[] messageHashBytes = new byte[hMac.getMacLength()];
        System.arraycopy(plainText, messageLength, messageHashBytes, 0, messageHashBytes.length);
        
        // Print the Plain Text of the data received by Bob related to Bank Account's transaction,
        // supposed made by Alice (verifying it the value of the Hash Functions of both messages are equal)
    	System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Plain Text of the Received Data of the Back Account Transaction by Bob:");
    	System.out.println("- " + UtilsExtended.toString(plainText, messageLength));
        System.out.println("Verified with Message-integrity and Message-Authentication:");
        System.out.println("- " + MessageDigest.isEqual(hMac.doFinal(), messageHashBytes));
    	System.out.println("-----------------------------------------------------------------------------");
    }
}