package utils.verify.jce.crypto.algorithms;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Class responsible for the List Security Algorithms, Features and Tools. 
 * 
 * Description:
 * - Lists of all the sets of Security/Crypto Algorithms, Features and Tools,
 *   installed in this computer/machine, as also, their respectively organisation by category;
 * 
 * Sets/Categories available:
 * a) Cipher Algorithms (Cryptographic Algorithms);
 * b) Key Agreements;
 * c) MACs;
 * d) Message Digests (Secure Hash-Functions);
 * e) Signatures/Digital Signatures with Public Keys Methods);
 */
public class ListSecurityAlgorithmsFeaturesAndTools {
	
	/**
	 * Print the set of entries of a given Crypto/Security Algorithm,
	 * to have their respectively Features and Tools organised:
	 * - The name of the set/category will be displayed with no indentation;
	 * - The entries will be displayed with indentation of a line;
	 * 
	 * @param setName the name of the set/category to print
	 * @param crytpoAlgorithms the Crypo/Security Algorithm,
	 *        to have their respectively Features and Tools organised
	 */
    public static void printSet(String setName, Set<String> crytpoAlgorithms) {
    	System.out.println("----------------------------------------------------------------");
        System.out.println(setName);
        System.out.println("----------------------------------------------------------------");
        
        if(crytpoAlgorithms.isEmpty()) {
        	System.out.println("----------------------------------------------------------------");
            System.out.println("NONE AVAILABLE...");
            System.out.println("----------------------------------------------------------------");
        }
        else {
            Iterator<?>	crypotAlgortihmsIterator = crytpoAlgorithms.iterator();
            
            while(crypotAlgortihmsIterator.hasNext()) {
                String name = (String) crypotAlgortihmsIterator.next();
                
                System.out.println("-  " + name);
            }
        }
    }
    
    /**
     * Main method. Perform all the listing, selection and categorisation process described previously.
     * 
     * @param args no arguments
     */
    public static void main(String[] args) {
        
    	// The list of all the sets of Security/Crypto Providers, installed in this computer/machine
    	Provider[] providers = Security.getProviders();
        
        // The lists of all the sets of Security/Crypto Algorithms, Features and Tools,
    	// installed in this computer/machine
        Set<String> ciphers = new HashSet<String>();
        Set<String> keyAgreements = new HashSet<String>();
        Set<String> macs = new HashSet<String>();
        Set<String> messageDigests = new HashSet<String>();
        Set<String> signatures = new HashSet<String>();
        
        // For all Security/Crypto Providers found in this computer/machine, 
        // search and select the information found, to do the categorisation of
        // all the Algorithm, Features and Tools, related to that
        // Security/Crypto Provider described previously
        for(int i = 0; i != providers.length; i++) {
            Iterator<?> providerEntryIterator = providers[i].keySet().iterator();
            
            while(providerEntryIterator.hasNext()) {
                String entry = (String) providerEntryIterator.next();
                
                // Find all the Aliases of Security/Crypto Algorithms,
                // installed in this computer/machine
                if(entry.startsWith("Alg.Alias.")) {
                    entry = entry.substring("Alg.Alias.".length());
                }
                
                // Add and organise by categories, all the Security/Crypto Algorithms, Features and Tools,
                // installed in this computer/machine, to the respectively sets
                if(entry.startsWith("Cipher.")) {
                    ciphers.add(entry.substring("Cipher.".length()));
                }
                else if(entry.startsWith("KeyAgreement.")) {
                    keyAgreements.add(entry.substring("KeyAgreement.".length()));
                }
                else if(entry.startsWith("Mac.")) {
                    macs.add(entry.substring("Mac.".length()));
                }
                else if(entry.startsWith("MessageDigest.")) {
                    messageDigests.add(entry.substring("MessageDigest.".length()));
                }
                else if(entry.startsWith("Signature.")) {
                    signatures.add(entry.substring("Signature.".length()));
                }
            }
        }
        
        // The number of all the Security/Crypto Algorithms, Features and Tools,
        // installed in this computer/machine
        int numSecurityAlgorithmsFeaturesAndTools =
        		 ( ciphers.size() + keyAgreements.size() + macs.size() + messageDigests.size() + signatures.size());
        
        System.out.println("THERE ARE INSTALLED " + numSecurityAlgorithmsFeaturesAndTools 
        		           + " SECURITY/CRYPTO ALGORITHMS, FEATURES AND TOOLS, IN THIS COMPUTER/MACHINE!!!");
        
        System.out.println();
        System.out.println();
        
        // Print all the sets of Security/Crypto Algorithms, Features and Tools,
        // currently installed in this computer/machine and all organised by categories:
        // a) Cipher Algorithms (Cryptographic Algorithms);
        // b) Key Agreements;
        // c) MACs;
        // d) Message Digests (Secure Hash-Functions);
        // e) Signatures/Digital Signatures with Public Keys Methods);
        printSet("Ciphers (Cryptographic Algorithms): ", ciphers);
        
        System.out.println();
        System.out.println();
        
        printSet("Key Agreements: ", keyAgreements);
        
        System.out.println();
        System.out.println();
        
        printSet("MACs: ", macs);
        
        System.out.println();
        System.out.println();
        
        printSet("Message Digests (Secure Hash-Functions): ", messageDigests);
        
        System.out.println();
        System.out.println();
        
        printSet("Signatures (Digital Signatures with Public Keys Methods):", signatures);
    }
}