package jceks.keystores;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Base64;

public class SecretSymmetricKeysManagerForKeyStores {
	
	/**
	 * Main methods.
	 * 
	 * @param args no arguments
	 */
	public static void main(String[] args) {

		try {
			
			// The Key Store where Symmetric Secret Keys are saved and stored
			// NOTE:
			// - Symmetric Secret Keys (of JCEKS type)
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
    
			// The File Input Stream to read and process all the entries of Symmetric Secret Keys
			// saved and stored in the Key Store
			FileInputStream fileInputStream = new FileInputStream("src/jceks/keystores/MyKeyStoreFile.jceks");
			
			// Load the Key Store from the File Input Stream related to the Key Store's file,
			// using a password defined previously
			// NOTE:
			// - The password of this Key Store, in this case is: "password";
			// - To access this Key Store, will be always asked for this password;
			keyStore.load(fileInputStream, "password".toCharArray());
			
			// In this example, the Key Store will keep the Symmetric Secret Keys
			// as entries, in the form of: mykey1, mykey2, (...)
			// Also, was used the password of this Key Store to protect each entry
			Key secretKey1 = keyStore.getKey("mykey1", "password".toCharArray());
			Key secretKey2 = keyStore.getKey("mykey2", "password".toCharArray());
            
			System.out.println("Secret Key #1 and Secret Key #2 extracted from the Key Store...");
            
			System.out.println();
			System.out.println();
			
			
			// The Encoded format of the Secret Key #1
			byte[] secretKey1Bytes = secretKey1.getEncoded();
			//String secretKey1String = new String(secretKey1Bytes);
			
			// The String representation of the Encoded Bytes of the Secret Key #1
			String secretKey1String = Base64.getEncoder().encodeToString(secretKey1Bytes);
			
			// Print the information related to the Secret Key #1
            System.out.println("----------------------------------------------------------------------------------------------------------");
            System.out.println("My Secret Key #1:");
            System.out.println("----------------------------------------------------------------------------------------------------------");
            
            System.out.println("Crypto's Algorithm: " + secretKey1.getAlgorithm());
            System.out.println("Size in Bytes: " + secretKey1Bytes.length);
    	    System.out.println("Format: " + secretKey1.getFormat());
    	    System.out.println("Bytes Encoded (in Base64): " + secretKey1String);
    	    
            System.out.println("----------------------------------------------------------------------------------------------------------");
            
            
            System.out.println();
            System.out.println();
            
            
            // The Encoded format of the Secret Key #2
			byte[] secretKey2Bytes = secretKey2.getEncoded();
			//String secretKey2String = new String(secretKey2Bytes);
			
			// The String representation of the Encoded Bytes of the Secret Key #2
			String secretKey2String = Base64.getEncoder().encodeToString(secretKey2Bytes);
			
			// Print the information related to the Secret Key #2
            System.out.println("----------------------------------------------------------------------------------------------------------");
	        System.out.println("My Secret Key #2:");
            System.out.println("----------------------------------------------------------------------------------------------------------");
	         
	        System.out.println("Crypto's Algorithm: " + secretKey2.getAlgorithm());
	        System.out.println("Size in Bytes: " + secretKey2Bytes.length);
	 	    System.out.println("Format: " + secretKey2.getFormat());
	 	    System.out.println("Bytes Encoded (in Base64): " + secretKey2String);
	 	    
            System.out.println("----------------------------------------------------------------------------------------------------------");
            
            System.out.println();
            System.out.println();
		}
		catch (Exception exception) {
			System.err.println("Error in the generation of Symmetric Secret Keys!!!");			
			exception.printStackTrace();
		}
	}
}
