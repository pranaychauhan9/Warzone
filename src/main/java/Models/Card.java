package Models;

/**
 * This model class oversees the player's card ownership and management.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public interface Card extends Order {

	/**
	 * 
	 * Pre-checking the order of card types.
	 * 
	 * @param p_gameState Gamestate
	 * @return true or false
	 */
	public Boolean checkOrderValidity(GameState p_gameState);

}