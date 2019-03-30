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


/**
 * Class responsible for Utils.
 * 
 * Description:
 * - A simple class for the conversion of formats of, usually,
 *   used data (like, hexadecimal, among others...);
 */
public class Utils {
	
	// Invariants/Constraints:
	
	/**
	 * The digits of the byte array of data
	 */
    private static String digits = "0123456789abcdef";
    
    /**
     * Returns the given byte array of data, in a hexadecimal format, given also, the original length.
     * 
     * @param data the byte array of data
     * @param length the length of the byte array of bytes
     * 
     * @return the given byte array of data, in a hexadecimal format, given also, the original length
     */
     public static String toHex(byte[] data, int length) {
        StringBuffer stringBuffer = new StringBuffer();
        
        for (int i = 0; i != length; i++) {
            int	vector = data[i] & 0xff;
            
            stringBuffer.append(digits.charAt(vector >> 4));
            stringBuffer.append(digits.charAt(vector & 0xf));
        }
        
        return stringBuffer.toString();
    }
   
   /**
    * Returns the given byte array of data, in a hexadecimal format.
    * 
    * @param data the byte array of data
    * 
    * @return the given byte array of data, in a hexadecimal format
    */
    public static String toHex(byte[] data) {
        return toHex(data, data.length);
    }
}