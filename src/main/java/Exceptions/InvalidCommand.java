package Exceptions;

import java.io.Serializable;

/**
 * Command Exceptions are thrown by this class.
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
public class InvalidCommand extends Exception implements Serializable {

	/**
	 * InvalidCommand constructor is used to print message when exception is caught
	 * in case command is invalid.
	 *
	 * @param p_message message to print when command is invalid.
	 */
	public InvalidCommand(String p_message) {
		super(p_message);
	}
}
