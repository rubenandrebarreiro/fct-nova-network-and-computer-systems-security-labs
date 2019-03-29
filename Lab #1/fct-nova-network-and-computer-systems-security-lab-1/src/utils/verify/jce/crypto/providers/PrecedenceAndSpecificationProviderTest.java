package utils.verify.jce.crypto.providers;


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

import javax.crypto.Cipher;

/**
 * Class responsible for the Precedence and Specification Provider Test.
 * 
 * Description:
 * - A simple class and respectively program, to do a simple and basic demonstration of,
 *   what difference can make specifying the Security/Crypto Provider to be used or not;
 *   
 * - Examples:
 *   a) Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
 *                           
 *                                 OR
 *                           
 *   b) Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding", "BC");
 */
public class PrecedenceAndSpecificationProviderTest {
	
    public static void main(String[] args) throws Exception {
    	
    	// 1st Example - NOT specifying the pretended Security/Crypto Provider
    	// NOTE:
    	// - It's going to be used the Security/Crypto Provider defined as default
    	System.out.println("1ST EXAMPLE (NOT SPECIFYING THE SECURITY/CRYPTO PROVIDER)");
        System.out.println("--------------------------------------------------------------------------------------------");
        
        // In this case, will not be specified what Security/Crypto Provider will be used
        // So, it will be used the Security/Crypto Provider by default
        // (Probably, the SunJCE Security/Crypto Provider)
        Cipher cipherText = Cipher.getInstance("Blowfish/ECB/NoPadding");
        System.out.println("First Security/Crypto Provider by default and, using this cipher: " 
        				   + cipherText.getProvider());
        
        System.out.println("--------------------------------------------------------------------------------------------");
        
        System.out.println();
        System.out.println();
        
        // 2nd Example - Specifying the pretended Security/Crypto Provider
    	System.out.println("2ND EXAMPLE (SPECIFYING A DETERMINED SECURITY/CRYPTO PROVIDER)");
        System.out.println("--------------------------------------------------------------------------------------------");
                
        // In this another cipher's setup, will be specified what Security/Crypto Provider will be used
        // To do that, it's only necessary to add the Security/Crypto Provider as
        // the following argument of the initialisation of the cipher
        // (example: BC, for the Bouncy Castle Security/Crypto Provider's API) 
        cipherText = Cipher.getInstance("Blowfish/ECB/NoPadding", "BC");
        System.out.println("The pretended and defined Security/Crypto Provider, using this cipher: "
                           + cipherText.getProvider());
        
        System.out.println("--------------------------------------------------------------------------------------------");
    }
}

