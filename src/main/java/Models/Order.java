package Models;

/**
 * 
 * The Command pattern's implementation: This model class is responsible for
 * handling and overseeing the orders issued by players.
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
public interface Order {

	/**
	 * 
	 * The method that the Receiver will invoke to carry out the Order.
	 * 
	 * @param p_gameState current state of the game.
	 */
	public void execute(GameState p_gameState);

	/**
	 * 
	 * Checks the order for validity.
	 * 
	 * @return boolean true or false
	 * @param p_gameState GameState Instance
	 */
	public boolean isValid(GameState p_gameState);

	/**
	 * Print order information.
	 */
	public void printOrder();

	/**
	 * Returns the Log to GameState with Execution Log.
	 *
	 * @return String containing log message
	 */
	public String orderExecutionLog();

	/**
	 * Returns the log, including the execution log, to the GameState.
	 *
	 * @param p_orderExecutionLog String to be set as log
	 * @param p_logType           type of log : error, default
	 */
	public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType);

	/**
	 * Return the name of the order.
	 * 
	 * @return String
	 */
	public String getOrderName();
}
