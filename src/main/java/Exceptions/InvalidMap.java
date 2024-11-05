package Exceptions;

import java.io.Serializable;

/**
 * Defined Map Exceptions are thrown by this class.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 * 
 */
public class InvalidMap extends Exception implements Serializable {

	/**
	 * InvalidMap constructor is used to print message when exception is caught in
	 * case map is invalid.
	 *
	 * @param p_message message to print when map is invalid.
	 */
	public InvalidMap(String p_message) {
		super(p_message);
	}
}