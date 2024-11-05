package Models;

import Constants.AppConstants;
import Services.PlayerService;
import java.io.Serializable;

/**
 * Represents a Diplomacy card used in a game.
 * <p>
 * The Diplomacy card allows players to initiate negotiations and diplomacy
 * with other players in the game. It handles the negotiation process
 * between the granting player and the marked player.
 * </p>
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
public class Diplomacy implements Card, Serializable {

    /**
     * The player granting the Diplomacy card.
     */
    Player d_grantingPlayer;

    /**
     * The player marked for diplomacy.
     */
    String d_markPlayer;

    /**
     * The order execution log for the Diplomacy card.
     */
    String d_orderLog;

    /**
     * Constructs a Diplomacy card with the specified marked player and granting
     * player.
     *
     * @param p_markPlayer     The player marked for diplomacy.
     * @param p_grantingPlayer The player granting the Diplomacy card.
     */
    public Diplomacy(String p_markPlayer, Player p_grantingPlayer) {
        this.d_markPlayer = p_markPlayer;
        this.d_grantingPlayer = p_grantingPlayer;
    }

    /**
     * Executes the Diplomacy card order by initiating negotiations with the marked
     * player.
     * It updates the order execution log and game state accordingly.
     *
     * @param p_gameState The current game state.
     */
    @Override
    public void execute(GameState p_gameState) {
        PlayerService l_playerHandler = new PlayerService();
        Player l_markPlayer = l_playerHandler.getPlayerByName(d_markPlayer, p_gameState);
        l_markPlayer.initiatePlayerNegotiation(d_grantingPlayer);
        d_grantingPlayer.initiatePlayerNegotiation(l_markPlayer);
        d_grantingPlayer.removeCard("negotiate");
        this.setD_orderExecutionLog("Negotiation with " + d_markPlayer + " approached by "
                + d_grantingPlayer.getD_playerName() + " successful!", AppConstants.LOG_MSG);
        p_gameState.updateLog(d_orderLog, AppConstants.ORDER_EFFECT);
    }

    /**
     * Checks the validity of the Diplomacy card order.
     * It ensures that the marked player exists in the game.
     *
     * @param p_gameState The current game state.
     * @return True if the order is valid, false otherwise.
     */

    @Override
    public boolean isValid(GameState p_gameState) {
        return true;
    }

    /**
     * Prints the Diplomacy order, showing the granting player and the marked
     * player.
     * This information is included in the order execution log.
     */
    public void printOrder() {
        this.d_orderLog = "---------- Player order to execute the diplomacy order is"
                + this.d_grantingPlayer.getD_playerName()
                + "----------" + System.lineSeparator() + "Request to " + " negotiate attacks from "
                + this.d_markPlayer;
        System.out.println(System.lineSeparator() + this.d_orderLog);
    }

    /**
     * Retrieves the order execution log for the Diplomacy card.
     *
     * @return The order execution log.
     */

    @Override
    public String orderExecutionLog() {
        return this.d_orderLog;
    }

    /**
     * Checks the validity of the Diplomacy card order by verifying if the marked
     * player
     * exists in the current game state.
     *
     * @param p_gameState The current game state.
     * @return True if the order is valid, false otherwise.
     */
    @Override
    public Boolean checkOrderValidity(GameState p_gameState) {
        PlayerService l_playerHandler = new PlayerService();
        Player l_markPlayer = l_playerHandler.getPlayerByName(d_markPlayer, p_gameState);
        if (!p_gameState.getD_playerList().contains(l_markPlayer)) {
            this.setD_orderExecutionLog("Player doesn't exist for the negotiation!", AppConstants.ERROR_LOG_MSG);
            p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
            return false;
        }
        return true;
    }

    /**
     * Sets the order execution log message for the Diplomacy card with the
     * specified log type.
     * Logs can be of type AppConstants.ERROR_LOG_MSG or "default."
     *
     * @param p_orderExecutionLog The message to set as the order execution log.
     * @param p_logType           The type of log, either AppConstants.ERROR_LOG_MSG
     *                            or "default."
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        this.d_orderLog = p_orderExecutionLog;
        // Determine the type of log message
        switch (p_logType) {
            case AppConstants.ERROR_LOG_MSG:
                // Log an error message
                System.err.println(p_orderExecutionLog);
                break;
            case AppConstants.LOG_MSG:
                // Log a standard message
                System.out.println(p_orderExecutionLog);
                break;
            default:
                // Log a message for an unknown log type
                System.out.println("Unknown log type: " + p_logType);
                break;
        }
    }

    /**
     * Retrieves a string representing the current Diplomacy card order, including
     * the command "negotiate" and
     * the marked player's name.
     *
     * @return The current Diplomacy order as a string.
     */
    private String currentOrder() {
        return "Diplomacy Order : " + "negotiate" + " " + this.d_markPlayer;
    }

    /**
     * Retrieves the name of the Diplomacy card order.
     *
     * @return The name of the Diplomacy card, which is "diplomacy."
     */
    @Override
    public String getOrderName() {
        return "diplomacy";
    }
}
