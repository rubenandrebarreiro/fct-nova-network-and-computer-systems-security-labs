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

import java.security.Security;

/**
 * Class responsible for the Simple Provider Test.
 * 
 * Description:
 * - A simple class and respectively program, that only verify/check if,
 *   some Security/Crypto Provider it's currently installed in
 *   the current computer/machine or not;
 *   
 * - Examples:
 *   a) SunJCE - Sun;
 *   b) BC;
 *   
 *   ... among others!
 */
public class SimpleProviderTest {
    public static void main(String[] args) {
        
    	System.out.println("--------------------------------------------------------------------------------------");   
    	System.out.println("VERYFING/CHECKING IF SOME SECURITY/CRYPTO PROVIDERS ARE INSTALLED OR NOT...");
    	System.out.println("--------------------------------------------------------------------------------------");   
    	
    	System.out.println();
    	System.out.println();
    	
    	System.out.println("----------------------------");
        
        // The list of Security/Crypto Providers, which it's pretended if are installed or not
    	// 
    	// Currently testing:
    	// a) SunJCE
    	// b) BC (Bouncy Castle)
        String[] providerNames = {"SunJCE", "BC"};
        
        // Verify/check if the SunJCE and BC (Bouncy Castle) Security/Crypto Providers
        // are installed or not (it's possible to be added more Security/Crytpo Providers)
        // 
        // NOTE:
        // - It's possible to add more Security/Crypto Providers,
        //   which it's pretended if are installed or not
        // - For that, it's only necessary to change the
        //   Array of Provider Names declared above, adding any others
        for(int i = 0; i < providerNames.length; i++) {
        	if(Security.getProvider(providerNames[i]) == null) {
                System.out.println(providerNames[i] + " it's not installed!!!");
            }
            else {
                System.out.println(providerNames[i] + " it's installed!!!");
            }
        
        	System.out.println("----------------------------");
        }
        
        System.out.println();
        System.out.println();
        
    	System.out.println("--------------------------------------------------------------------------------------");   
        System.out.println("ENDED THE VERYFING/CHECKING OF IF SOME SECURITY/CRYPTO PROVIDERS ARE INSTALLED OR NOT");
    	System.out.println("--------------------------------------------------------------------------------------");   
    }
}


