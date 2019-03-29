package security.crypto.examples;

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

import java.util.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Class responsible for the Example Cipher Text Test.
 * 
 * Description:
 * - A simple class and respectively program, to test and demonstrate,
 *   a basic and simple ciphering/de-ciphering process of a message based in
 *   Base-64 Algorithm Encryption/De-Encryption;
 * 
 * NOTE:
 * - Base-64 Algorithm Encryption/De-Encryption it's not
 *   a recommended Ciphering/De-Ciphering algorithm to be used;
 */
public class ExampleCipherTextTest {
    
	/**
	 * The main method.
	 * Given three arguments, the message to send, the cryptographic algorithm and
	 * the cipher modes specification and configuration, send the message,
	 * ciphered in the sender's end-point and de-ciphered in the another end-point. 
	 * 
	 * @param args three arguments,
	 *        <message-to-send>, <cryptographic-algorithm>, <cipher-modes-specification-configuration>
	 */
	public static void main(String args[]) {
        
    	try {
            if (args.length != 3) {
            	System.out.println("Usage: ExampleCipherTextTest "
                  				 + "<message-to-send> <cryptographic-algorithm> <cipher-modes-specification-configuration>");
            	System.out.println("Example: ExampleCipherTextTest TopSecret AES AES/CBC/PKCS5Padding");
                
            	System.exit(-1);
            }
            
            // First, it will be generate a key
            // - The way of getting the key can be got by many ways:
            //   a) A KeyStore already existent;
            //   b) Simply, generating a new one;
            //   c) Through a secure protocol of distributing keys;
            KeyGenerator keyGenerator = KeyGenerator.getInstance(args[1]);
            Cipher cipherText = Cipher.getInstance(args[2]);

            Key secretKey = keyGenerator.generateKey();

            // Initialising the cipher process
            System.out.println("Initialising the ciphering process...");	   
            
            cipherText.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte input[] = args[0].getBytes();
            byte encryptedData[] = cipherText.doFinal(input);
	  		
            byte initialisationVector[] = cipherText.getIV();
            
            // It will be used a Base-64 encryption
            byte[] encrypteDatadBase64 = Base64.getEncoder().encode(encryptedData);  

            // Print the ciphered text, in a Base-64 encryption
            System.out.println("Cipher Text (Encrypted by Base-64): " + new String(encrypteDatadBase64));
            
            System.out.println();
            System.out.println();
            
            // Initialising the de-cipher process
            System.out.println("Initialising the de-ciphering process...");	   

            IvParameterSpec initialisationVectorSpecifications = new IvParameterSpec(initialisationVector);
            
            cipherText.init(Cipher.DECRYPT_MODE, secretKey, initialisationVectorSpecifications);
            
            byte output[] = cipherText.doFinal(encryptedData);
            
            // Print the de-ciphered text
            System.out.println("Initial Plain Text: " + new String (output));
        }
    	catch (Exception e) {
            e.printStackTrace();
        }
    }
}