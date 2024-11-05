package Models;

import java.io.IOException;
import java.io.Serializable;

/**
 * This is Player Behavior's abstract strategy class.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public abstract class PlayerBehaviorStrategy implements Serializable {

	/**
	 * object of player class.
	 */
	Player d_player;

	/**
	 * object of GameState class.
	 */
	GameState d_gameState;

	/**
	 * A new order is created for Random, Aggressive, Cheater, and Benevolent
	 * Players using this method.
	 * 
	 * @param p_player    object of Player class
	 * @param p_gameState object of GameState class
	 * 
	 * @return Order object of order class
	 * @throws IOException Exception
	 */
	public abstract String generateOrder(Player p_player, GameState p_gameState) throws IOException;

	/**
	 * Deploy Orders based on strategy definition.
	 *
	 * @param p_player    player to give deploy orders
	 * @param p_gameState current Gamestate
	 * @return String representing Order
	 */
	public abstract String generateDeployOrder(Player p_player, GameState p_gameState);

	/**
	 * Advance Orders based on strategy definition.
	 *
	 * @param p_player    player to give advance orders
	 * @param p_gameState GameState representing current Game
	 * @return String representing Order.
	 */
	public abstract String generateAdvanceOrder(Player p_player, GameState p_gameState);

	/**
	 * Card Orders based on strategy definition.
	 *
	 * @param p_player    player to give Card Orders.
	 * @param p_gameState GameState representing Current Game
	 * @param p_cardName  Card Name to create Order for
	 * @return String representing order
	 */
	public abstract String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName);

	/**
	 * This method returns the player behavior.
	 * 
	 * @return String player behavior
	 */
	public abstract String getPlayerBehavior();

}
