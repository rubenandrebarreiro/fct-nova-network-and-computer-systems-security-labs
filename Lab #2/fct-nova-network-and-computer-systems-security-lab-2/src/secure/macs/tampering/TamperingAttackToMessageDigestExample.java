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
import javax.crypto.spec.IvParameterSpec;

import security.crypto.common.UtilsExtended;

/**
 * Class responsible for Tampering Attack To Message Digest Example.
 * 
 * Description:
 * TODO
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
public class TamperingAttackToMessageDigestExample {
	 public static void main(String[] args) throws Exception {
		    
	    	// The source/seed of a secure random
	    	SecureRandom secureRandom = new SecureRandom();
	    	
	    	// The Initialising Vector and its parameters specifications
	        IvParameterSpec intialisingVectorParameterSpecifications = UtilsExtended.createCtrIvForAES(1, secureRandom);
	        
	        // The 1st Secret Key specifications generated to be
	    	// used in this Message Digest Encryption/De-cryption processes
	    	// using CTR mode byte oriented (byte counter oriented);
	        Key secretKey = UtilsExtended.createKeyForAES(256, secureRandom);
	        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
	        
	        // The input data of a Bank Account's transaction
	        String inputTransactionData = "Transfer 0,000,100 Euros to Bank Account no. 1234-5678";
	        
	        // The calculated Hash value for the Message Digest, using SHA1 Algorithm
	        MessageDigest hashMessageDigest = MessageDigest.getInstance("SHA1");
	        
	        // Print the input data of a Bank Account's transaction
	    	System.out.println("-----------------------------------------------------------------------------");
	    	System.out.println("Input Data of the Back Account's Transaction made by Alice:");
	    	System.out.println("- " + inputTransactionData);
	    	System.out.println("-----------------------------------------------------------------------------");
	    	
	    	System.out.println();
	    	System.out.println();
	    	
	    	// The (correct) Encryption process made by Alice and the ciphering of its respectively content
	    	// Cipher =  Message|Hash(Message)
	    	cipher.init(Cipher.ENCRYPT_MODE, secretKey, intialisingVectorParameterSpecifications);
	    	
	    	byte[] cipherText = new byte[cipher
	    	                             .getOutputSize( (inputTransactionData.length() 
	    	                            		        + hashMessageDigest.getDigestLength())) ];

	        int cipherTextLength = cipher.update(UtilsExtended
	        							 .toByteArray(inputTransactionData), 0,
	        							             inputTransactionData.length(), 
	        							             cipherText, 0);
	        
	        // Update the Hash value with the Input Text based in SHA1 encryption algorithm
	        // Cipher' = Encryption w/ secret key ( Cipher )
	        // NOTE:
	        // - Cipher = Message|Hash(Message)
	        hashMessageDigest.update(UtilsExtended.toByteArray(inputTransactionData));
	        
	        // Update the size of the Cipher Text, accordingly with the new Cipher
	        cipherTextLength += cipher.doFinal(hashMessageDigest.digest(), 0, 
	        		                           hashMessageDigest.getDigestLength(),
	        		                           cipherText, cipherTextLength);
	        
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
	        
	        /************************************************************/
	        
	        
	        // The (correct) De-cryption process made by Bob and the de-ciphering of its respectively content
	        cipher.init(Cipher.DECRYPT_MODE, secretKey,
	        			intialisingVectorParameterSpecifications);
	        
	        byte[] plainText = cipher.doFinal(cipherText, 0, cipherTextLength);
	        int messageLength = ( plainText.length - hashMessageDigest.getDigestLength() );
	        
	        // The calculated Hash value of the Message Digest of the data of
	        // the message received
	        hashMessageDigest.update(plainText, 0, messageLength);

	        // The Hash of the message calculated by a Hash-Function of
	        // the SHA1 Algorithm's instance, created previously
	        byte[] messageHashBytes = new byte[hashMessageDigest.getDigestLength()];
	        System.arraycopy(plainText,
	        		         messageLength, messageHashBytes,
	        		         0, messageHashBytes.length);
	        
	        // Print the Plain Text of the data received by Bob related to Bank Account's transaction,
	        // supposed made by Alice (verifying it the value of the Hash Functions of both messages are equal)
	    	System.out.println("-----------------------------------------------------------------------------");
	        System.out.println("Plain Text of the Received Data of the Back Account Transaction by Bob:");
	    	System.out.println("- " + UtilsExtended.toString(plainText, messageLength));
	        System.out.println("Verified with Message-integrity and Message-Authentication:");
	        System.out.println("- " + MessageDigest.isEqual(hashMessageDigest.digest(), messageHashBytes));
	    	System.out.println("-----------------------------------------------------------------------------");
	 }	
}