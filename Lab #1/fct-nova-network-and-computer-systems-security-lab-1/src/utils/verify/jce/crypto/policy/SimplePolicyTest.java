package utils.verify.jce.crypto.policy;

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

import javax.crypto.*;
import javax.crypto.spec.*;


/**
 * Class responsible for the Simple Policy Test.
 * 
 * Description:
 * - A simple class and respectively program, that only test the policy of JDK/JRE;
 * - This it's important to know which different sizes/lengths of the Secret Keys
 *   should be used in the cryptographic algorithms, since it's possible to
 *   occur some problems and constraints in their installation of environments of the JDK/JRE,
 *   due to compliance's policies;
 * 
 * NOTE:
 * - It's possible, to change the constraints of that compliance's policies, but always,
 *   under your own responsibility (in this case, this should be important in this Labs);
 */
public class SimplePolicyTest {
    public static void main(String[] args) throws Exception {
    	
        System.out.println("-----------------------------------------------------------------------"); 
        System.out.println("Policy and Imposed Restrictions Tests");    
        System.out.println("-----------------------------------------------------------------------"); 
        
        // Some data, composed of random bytes in hexadecimal
        // Example:
        // - { 0 1 2 3 4 5 6 7 }
        byte[] data = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        
        
        // SOME EXAMPLES OF USAGE OF SECRET KEYS WITH DIFFERENT SIZES/LENGTHS
        
        // A) 1st Example - Using a 64-bit Secret Key
        System.out.println("1ST EXAMPLE - USING A 64-BIT SECRET KEY");
        
        // Create a 64-bit Secret Key from RAW bytes
        SecretKey secretKey64 = 
        		new SecretKeySpec(new byte[] { 0x00, 0x01,0x02, 0x03, 0x04, 0x05, 0x06, 0x07 }, 
        				          "Blowfish");

        // Try to encrypt with this key (using BLOWFISH Algorithm)
        Cipher cipherText = Cipher.getInstance("Blowfish/ECB/NoPadding");

        // Initialise the cipher text,
        // specifying the instance's parameters and the 64-bit Secret Key
        cipherText.init(Cipher.ENCRYPT_MODE, secretKey64);
		cipherText.doFinal(data);
		
		System.out.println("Using a 64-bit Secret Key: OK!");       
        
        System.out.println();
        System.out.println();
       
		
		// B) 2nd Example - Using a 128-bit Secret Key
        System.out.println("2ND EXAMPLE - USING A 128-BIT SECRET KEY");
		
        // Now, create a 128-bit Secret Key from RAW bytes
        SecretKey secretKey128 = 
        		new SecretKeySpec(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
        									   0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E , 0x0F}, 
        						  "Blowfish");

        // Initialise the cipher text again,
        // specifying the new instance's parameters and the 128-bit Secret Key
        cipherText.init(Cipher.ENCRYPT_MODE, secretKey128);
        cipherText.doFinal(data);
        
        System.out.println("Using a 128-bit Secret Key: OK!");       
        
        System.out.println();
        System.out.println();
        
        
		// C) 3rd Example - Using a 192-bit Secret Key
        System.out.println("3RD EXAMPLE - USING A 192-BIT SECRET KEY");

        // Now, create a 192-bit Secret Key from RAW bytes
        SecretKey secretKey192 = new SecretKeySpec(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
        												  		0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 
        												  		0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 }, 
        									 	   "Blowfish");

        // Initialise the cipher text again,
        // specifying the new instance's parameters and the 192-bit Secret Key
        cipherText.init(Cipher.ENCRYPT_MODE, secretKey192);
        cipherText.doFinal(data);
        
        System.out.println("Using a 192-bit Secret Key: OK!");       
        
        System.out.println();
        System.out.println();
       
        
        // D) 4th Example - Using a 256-bit Secret Key
        System.out.println("4TH EXAMPLE - USING A 256-BIT SECRET KEY");

        // Now, create a 256-bit Secret Key from RAW bytes
        SecretKey secretKey256 = new SecretKeySpec(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
        												  		0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 
        												  		0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 
        												  		0x18, 0x19, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25 }, 
        									 	   "Blowfish");

        // Initialise the cipher text again,
        // specifying the new instance's parameters and the 256-bit Secret Key
        cipherText.init(Cipher.ENCRYPT_MODE, secretKey256);
        cipherText.doFinal(data);
        
        System.out.println("Using a 256-bit Secret Key: OK!");        
        
        System.out.println();
        System.out.println();    
       
        
        // E) 5th Example - Using a 448-bit Secret Key
        System.out.println("5TH EXAMPLE - USING A 448-BIT SECRET KEY");

        // Now, create a 448-bit Secret Key from RAW bytes
        SecretKey secretKey448 = new SecretKeySpec(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
													            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 
													            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 
													            0x18, 0x19, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 
													            0x26, 0x27, 0x28, 0x29, 0x30, 0x31, 0x32, 0x33, 
													            0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 
													            0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49 }, 
												   "Blowfish");

        // Initialise the cipher text again,
        // specifying the new instance's parameters and the 448-bit Secret Key
		cipherText.init(Cipher.ENCRYPT_MODE, secretKey448);
		cipherText.doFinal(data);
		
		System.out.println("Using a 448-bit Secret Key: OK!");         

		System.out.println();
		System.out.println();
		
        System.out.println("-----------------------------------------------------------------------"); 
		System.out.println("If all the tests passed with success: Unrestricted Policy!");
        System.out.println("-----------------------------------------------------------------------"); 
    }
}