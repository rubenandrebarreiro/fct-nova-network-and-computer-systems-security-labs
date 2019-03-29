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

import java.security.Provider;
import java.security.Security;

import utils.Utils;

/**
 * Class responsible for the List of Security/Crypto Providers.
 * 
 * Description:
 * - A simple class and respectively program, that only display the list of,
 *   the Security/Crypto Providers that are currently installed in
 *   the current computer/machine or not;
 */
public class ListSecurityCryptoProviders {
    public static void main(String[] args) {
    	
        Provider[] providers = Security.getProviders();

        System.out.println("THERE ARE CURRENTLY INSTALLED " + providers.length +
        		           " SECURITY/CRYPTO PROVIDERS INSTALLED IN THIS MACHINE!!!");     
       
        System.out.println();
        System.out.println();
        
        System.out.println("------------------------------------------------------------------");    
        System.out.println("SECURITY/CRYPTO PROVIDERS INSTALLED IN THIS MACHINE:");     
        System.out.println("------------------------------------------------------------------");    
        
        System.out.println();
        System.out.println();
        
        System.out.println("1ST SECURITY/CRYPTO PROVIDER:");
        System.out.println("------------------------------------------");
        
        // List of all Crypto and Security Providers installed and,
        // their respectively descriptions
        for (int i = 0; i != providers.length; i++) {
            System.out.println("Provider's Name:\n- " + providers[i].getName() + 
            					Utils.makeBlankString(12 - providers[i].getName().length()) + 
            					" Version: " + providers[i].getVersion());
            
            System.out.println("------------------------------------------");     
            System.out.println("Provider's Description:\n- " + providers[i].getInfo());
            System.out.println("------------------------------------------");
            System.out.println();
            
            if((i + 2) == 2) {
            	System.out.println("2ND SECURITY/CRYPTO PROVIDER:");
                System.out.println("------------------------------------------");
            }
            else if ((i + 2) == 3) {
            	System.out.println("3RD SECURITY/CRYPTO PROVIDER:");
                System.out.println("------------------------------------------");
            }
            else {
            	if((i + 2) <= providers.length) {
            		System.out.println((i + 2) + "TH SECURITY/CRYPTO PROVIDER:");
                    System.out.println("------------------------------------------");
            	}
            }
        }
        
        System.out.println();
        System.out.println();
        System.out.println("------------------------------------------------------------------");    
        System.out.println("END OF ALL SECURITY/CRYPTO PROVIDERS INSTALLED IN THIS MACHINE");     
        System.out.println("------------------------------------------------------------------");    
    }
}


