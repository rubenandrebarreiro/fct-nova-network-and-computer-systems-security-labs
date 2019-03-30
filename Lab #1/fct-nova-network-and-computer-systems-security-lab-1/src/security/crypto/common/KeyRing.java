package security.crypto.common;

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
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class responsible for the Key Ring.
 * 
 * Description:
 * - A simple class for the initialisations of a key used by a Symmetric Algorithms, as also,
 *   their respectively configurations and specifications;
 */
public class KeyRing {
	
	/**
	 * The Security/Crypto's Algorithm, currently being used by the Key Generator that
	 * be placed in the Key Storage
	 */
	// AES Algorithm (Advanced Encryption Standard - Rijndael)
    public static final String ALGORITHM = "AES";
    
    // DESede Algorithm (3DES [Triple Data Encryption Standard] - Encryption-De-encryption-Encryption)
    // public static final String ALGORITHM = "DESede";
    
    // Blowfish Algorithm
    // public static final String ALGORITHM = "Blowfish";
    
    /**
     * The File of the Key Storage, where all the Secret Keys (generated by the Symmethric Algorithm),
     * are placed and where can be got
     */
    public static final String KEYRING = "keyring";
    
    
    // Methods/Functions:
    
    // 1) Basic Methods/Functions:
    
    /**
     * Returns the Secret Key from the Key Storage (generated by the Symmetric Algorithm).
     * 
     * @return the Secret Key from the Key Storage (generated by the Symmetric Algorithm)
     * 
     * @throws Exception an Exception to be thrown, in the case of, an anomaly occurs
     */
    public static SecretKey readSecretKey() throws Exception {

    	// Read the Secret Key from the Key Storage
    	System.out.println("Reading the Secret Key from the Key Storage...");
    	
    	File fileKeyStorage = new File(KEYRING);
    	long fileKeyStorageLength = fileKeyStorage.length();
    	
    	byte[] keyBuffer = new byte[(int) fileKeyStorageLength];

    	InputStream inputStream = new FileInputStream(KEYRING);
    	
    	try {
    		inputStream.read(keyBuffer);
    	} 
    	finally {
    		try {
    			inputStream.close();
    		}
    		catch (Exception e) {
    			// Handle of Exceptions in the process of Read Secret Key
    			System.err.print("Error on the process of Read Secret Key");
    		} 
    	} 

    	// NOTE:
    	// - The Secret Key stay represented in its internal format,
    	//   which it's an object of type SecretKey
    	
    	return new SecretKeySpec(keyBuffer, ALGORITHM);
    }	 
}