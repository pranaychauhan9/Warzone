/**
 * The `HumanPlayer` class represents a player with human behavior in a game.
 * It extends the `PlayerBehaviorStrategy` class and implements the necessary methods
 * for generating orders based on user input.
 *
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The `HumanPlayer` class represents a player with human behavior in a game.
 * It extends the `PlayerBehaviorStrategy` class and implements the necessary
 * methods
 * for generating orders based on user input.
 */
public class HumanPlayer extends PlayerBehaviorStrategy {

    /**
     * Returns the behavior type of the player.
     *
     * @return The string "Human" representing the player's behavior.
     */
    @Override
    public String getPlayerBehavior() {
        return "Human";
    }

    /**
     * Generates a command for the player based on user input.
     *
     * @param p_player    The player for whom the command is generated.
     * @param p_gameState The current state of the game.
     * @return A string representing the command entered by the user.
     * @throws IOException If an I/O error occurs while reading the command.
     */
    @Override
    public String generateOrder(Player p_player, GameState p_gameState) throws IOException {
        BufferedReader l_readerCmd = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nPlease enter a command to issue an order for player "
                + p_player.getD_playerName() + " or use the 'showmap' command to view the current state of the game.");

        String l_cmdEntered = l_readerCmd.readLine();
        return l_cmdEntered;
    }

    /**
     * Generates a deployment order for the player.
     *
     * @param p_player    The player for whom the deployment order is generated.
     * @param p_gameState The current state of the game.
     * @return Always returns null as human players don't generate deployment
     *         orders.
     */
    @Override
    public String generateDeployOrder(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * Generates an advance order for the player.
     *
     * @param p_player    The player for whom the advance order is generated.
     * @param p_gameState The current state of the game.
     * @return Always returns null as human players don't generate advance orders.
     */
    @Override
    public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * Generates a card-based order for the player.
     *
     * @param p_player    The player for whom the card-based order is generated.
     * @param p_gameState The current state of the game.
     * @param p_cardName  The name of the card associated with the order.
     * @return Always returns null as human players don't generate card-based
     *         orders.
     */
    @Override
    public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
        return null;
    }
}
