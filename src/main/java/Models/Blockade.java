package Models;

import java.io.Serializable;

import Constants.AppConstants;
import CommonFunctions.CommonCode;

/**
 * Execution of the Blockade order involves converting one of your territories
 * into a neutral one while tripling the number of armies on that territory.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class Blockade implements Card, Serializable {

	/**
	 * Player in possession of the Blockade card.
	 */
	Player d_initiatorPlayer;

	/**
	 * The unique identifier of the designated target country.
	 */
	String d_targetCountryID;

	/**
	 * Establishes the log that contains order-related information.
	 */
	String d_orderExecutionLog;

	/**
	 * The constructor accepts all the required parameters for order implementation.
	 * 
	 * @param p_playerInitiator Player
	 * @param p_targetCountry   target country ID
	 */
	public Blockade(Player p_playerInitiator, String p_targetCountry) {
		this.d_initiatorPlayer = p_playerInitiator;
		this.d_targetCountryID = p_targetCountry;
	}

	/**
	 * Carries out the Blockade command.
	 * 
	 * @param p_gameState current state of the game.
	 */
	@Override
	public void execute(GameState p_gameState) {
		if (isValid(p_gameState)) {
			Country l_targetCountryID = p_gameState.getD_map().getCountryByName(d_targetCountryID);
			Integer l_noOfArmiesOnTargetCountry = l_targetCountryID.getD_armyCount() == 0 ? 1
					: l_targetCountryID.getD_armyCount();
			l_targetCountryID.setD_armyCount(l_noOfArmiesOnTargetCountry * 3);

			// change the territory to a neutral territory
			d_initiatorPlayer.getD_playerCountries().remove(l_targetCountryID);

			Player l_player = p_gameState.getD_playerList().stream()
					.filter(l_pl -> l_pl.getD_playerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);

			// assign neutral territory to existing neutral player.
			if (!CommonCode.isNull(l_player)) {
				l_player.getD_playerCountries().add(l_targetCountryID);
				System.out.println("Neutral territory: " + l_targetCountryID.getD_countryName()
						+ "assigned to the Neutral Player.");
			}

			d_initiatorPlayer.removeCard("blockade");
			this.setD_orderExecutionLog("\nPlayer : " + this.d_initiatorPlayer.getD_playerName()
					+ " is executing defensive blockade on Country :  " + l_targetCountryID.getD_countryName()
					+ " with armies :  " + l_targetCountryID.getD_armyCount(), "default");
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
		}
	}

	/**
	 * Checks if the target country is owned by the executing
	 * player and ensures that any attacks, airlifts, or
	 * other actions occur before the country becomes neutral.
	 * 
	 * @return boolean if given advance command is valid or not.
	 */
	@Override
	public boolean isValid(GameState p_gameState) {

		// Validates whether target country belongs to the Player who executed the order
		// or not
		Country l_country = d_initiatorPlayer.getD_playerCountries().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryID)).findFirst()
				.orElse(null);

		if (CommonCode.isNull(l_country)) {
			this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country : "
					+ this.d_targetCountryID + " given in blockade command does not owned to the player : "
					+ d_initiatorPlayer.getD_playerName()
					+ " The card will have no affect and you don't get the card back.", AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}
		return true;
	}

	/**
	 * Print Blockade order.
	 */
	@Override
	public void printOrder() {
		this.d_orderExecutionLog = "----------Blockade card order issued by player "
				+ this.d_initiatorPlayer.getD_playerName() + "----------" + System.lineSeparator()
				+ "Creating a defensive blockade with armies = " + "on country ID: " + this.d_targetCountryID;
		System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
	}

	/**
	 * Displays and records the order execution log.
	 *
	 * @param p_orderExecutionLog String that is to be set as a log
	 * @param p_logType           type of log : error, default
	 */
	public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
		this.d_orderExecutionLog = p_orderExecutionLog;
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
	 * Card order type pre-validation.
	 * 
	 * @param p_gameState Gamestate
	 * @return true or false
	 */
	@Override
	public Boolean checkOrderValidity(GameState p_gameState) {
		Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryID);
		if (l_targetCountry == null) {
			this.setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!",
					AppConstants.ERROR_LOG_MSG);
			return false;
		}
		return true;
	}

	/**
	 * Retrieve the name of the order.
	 * 
	 * @return String
	 */
	@Override
	public String getOrderName() {
		return "blockade";
	}

	/**
	 * Provides the active Blockade order currently in progress.
	 * 
	 * @return advance order command
	 */
	private String currentOrder() {
		return "Blockade card order : " + "blockade" + " " + this.d_targetCountryID;
	}

	/**
	 * Execution log.
	 * 
	 * @return String return execution log
	 */
	public String orderExecutionLog() {
		return this.d_orderExecutionLog;
	}
}