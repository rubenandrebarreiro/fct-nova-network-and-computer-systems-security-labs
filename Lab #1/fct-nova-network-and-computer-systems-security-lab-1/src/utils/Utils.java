package utils;

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


/**
 * Auxiliary Class - Static Methods:
 * - Class of Utils;
 * 
 * - Auxiliary methods/functions of displaying data composed by fields of strings;
 */
public class Utils {
	
	// Methods/Functions:
	
    /**
     * Returns a string composed of blank spaces, with a given length
     * 
     * @param length the length/size of the string composed of blank spaces
    
     * @return a string composed of blank spaces, with a given length
     */
    public static String makeBlankString(int length) {
        char[] buffer = new char[length];
        
        for (int i = 0; i != buffer.length; i++) {
        	buffer[i] = ' ';
        }
        
        return new String(buffer);
    }
}