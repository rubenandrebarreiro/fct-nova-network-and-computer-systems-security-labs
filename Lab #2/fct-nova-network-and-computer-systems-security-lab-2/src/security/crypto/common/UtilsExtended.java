package security.crypto.common;

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

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Class responsible for the Utils Extended, that extends the Class Utils.
 * 
 * Description:
 * - A simple class for the conversion of formats of, usually,
 *   used data (like, hexadecimal, among others...);
 * - Introduced more features and format conversion, in comparison with Utils,
 *   like (String, bytes, etc.) 
 */
public class UtilsExtended extends Utils {
    
	// Methods/Functions:
	
	/**
	 * Returns a created Secret Key, using the AES (Advanced Encryption Standard - Rijndael)
     * Encryption/De-cryption Algorithm
	 * 
	 * @param keyBitSize the size/length pretended for the Secret Key
	 * @param secureRandom the source/seed for a secure random
	 * 
	 * @return a created Secret Key, using the AES (Advanced Encryption Standard - Rijndael)
     * 		   Encryption/De-cryption Algorithm
	 * 
	 * @throws NoSuchAlgorithmException a NoSuchAlgortihmException to be thrown, in the case of,
	 * 		   the Secure/Crypto Algorithm pretended to be used, don't exist or it's not installed
	 * @throws NoSuchProviderException a NoSuchProviderException to be thrown, in the case of,
	 * 		   the Secure/Crypto Provider pretended to be used, don't exist or it's not installed
	 */
    public static SecretKey createKeyForAES(int keyBitSize, SecureRandom secureRandom)
    			throws NoSuchAlgorithmException, NoSuchProviderException {
    	
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        
        // Initialise the Key Generator with a given bit size/length
        // NOTE:
        // - In this case, it will be used a Secret Key with 256-bits of size
        keyGenerator.init(256, secureRandom);
        //keyGenerator.init(keyBitSize, secureRandom);
        
        return keyGenerator.generateKey();
    }
    
    /**
     * Returns a created Initialising Vector and its Parameter Specifications
     * to use in a Cipher's Suite, which use AES (Advanced Encryption Standard - Rijndael)
     * Encryption/De-cryption Algorithm
     * 
     * NOTE:
     * - The Initialising Vector composed by 4 bytes (message number),
     *   4 random bytes and a counter of 8 bytes;
     * 
     * @param messageNumber the message number
     * @param secureRandom a source/seed for a secure random
     * 
     * @return a created Initialising Vector and its Parameter Specifications
     * 		   to use in a Cipher's Suite, which use AES (Advanced Encryption Standard - Rijndael)
     *         Encryption/De-cryption Algorithm
     */
    public static IvParameterSpec createCtrIvForAES(int messageNumber, SecureRandom secureRandom) {
    	byte[] initialisingVectorBytes = new byte[16];
        
        // Initially randomise   
        secureRandom.nextBytes(initialisingVectorBytes);
        
        // Set the message number bytes
        initialisingVectorBytes[0] = (byte) (messageNumber >> 24);
        initialisingVectorBytes[1] = (byte) (messageNumber >> 16);
        initialisingVectorBytes[2] = (byte) (messageNumber >> 8);
        initialisingVectorBytes[3] = (byte) (messageNumber >> 0);
        
        // Set the counter bytes to 1
        for (int i = 0; i != 7; i++) {
        	initialisingVectorBytes[(8 + i)] = 0;
        }
        
        initialisingVectorBytes[15] = 1;
        
        return new IvParameterSpec(initialisingVectorBytes);
    }
    
    /**
     * Returns and converts a String, from a byte array of characters of 8 bits,
     * but given a number of bytes to be converted, which can't be
     * the total length of the byte array.
     * 
     * @param bytes the byte array to be converted
     * @param length the size/length or number of bytes to be converted
     * 
     * @return and converts a String, from a byte array of characters of 8 bits,
     * 		   but given a number of bytes to be converted, which can't be
     *         the total length of the byte array
     */
    public static String toString(byte[] bytes, int length) {
        char[] chars = new char[length];
        
        for (int i = 0; i != chars.length; i++) {
            chars[i] = (char)(bytes[i] & 0xff);
        }
        
        return new String(chars);
    }
    
    /**
     * Returns and converts a String, from a byte array of characters of 8 bits.
     * 
     * @param bytes the byte array to be converted
     * 
     * @return and converts a String, from a byte array of characters of 8 bits
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, bytes.length);
    }
    
    /**
     * Returns and converts a byte array, from the 8 bits of a given String.
     * 
     * @param string a given String to be converted
     * 
     * @return and converts a byte array, from the 8 bits of a given String
     */
    public static byte[] toByteArray(String string) {
        byte[] bytes = new byte[string.length()];
        char[] chars = string.toCharArray();
        
        for (int i = 0; i != chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        
        return bytes;
    }
}