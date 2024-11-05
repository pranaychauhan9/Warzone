package Models;

import java.io.Serializable;

import Constants.AppConstants;

/**
 * This class handles the execution and validation of Airlift Validate.
 *
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class Airlift implements Card, Serializable {

	/**
	 * The card belonging to the player.
	 */
	Player d_player;

	/**
	 * Country from which to withdraw troops.
	 */
	String d_sourceCountry;

	/**
	 * Country for deploying troops.
	 */
	String d_targetCountry;

	/**
	 * Quantity of troops for the Airlift.
	 */
	Integer d_armyCount;

	/**
	 * Logs the execution history.
	 */
	String d_orderExecutionLog;

	/**
	 * Constructor that sets the card parameters during initialization.
	 *
	 * @param p_sourceCountry source country to airlift from.
	 * @param p_targetCountry target country to drop off armies to.
	 * @param p_armyCount     No of armies to airlift
	 * @param p_player        player owning the card
	 */
	public Airlift(String p_sourceCountry, String p_targetCountry, Integer p_armyCount, Player p_player) {
		this.d_armyCount = p_armyCount;
		this.d_targetCountry = p_targetCountry;
		this.d_sourceCountry = p_sourceCountry;
		this.d_player = p_player;
	}

	/**
	 * Carries out the command.
	 */
	@Override
	public void execute(GameState p_gameState) {
		if (isValid(p_gameState)) {
			Country l_sourceCountry = p_gameState.getD_map().getCountryByName(d_sourceCountry);
			Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountry);
			Integer l_updatedTargetArmies = (l_targetCountry.getD_armyCount() == null) ? this.d_armyCount
					: l_targetCountry.getD_armyCount() + this.d_armyCount;
			Integer l_updatedSourceArmies = l_sourceCountry.getD_armyCount() - this.d_armyCount;
			l_targetCountry.setD_armyCount(l_updatedTargetArmies);
			l_sourceCountry.setD_armyCount(l_updatedSourceArmies);
			d_player.removeCard("airlift");
			this.setD_orderExecutionLog(
					"Airlift from " + d_sourceCountry + " to " + d_targetCountry + " is a success!",
					AppConstants.LOG_MSG);
			p_gameState.updateLog(d_orderExecutionLog, AppConstants.ORDER_EFFECT);
		} else {
			this.setD_orderExecutionLog("Execution of Airlift failed!",
					AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(d_orderExecutionLog, AppConstants.ORDER_EFFECT);
		}
	}

	private Country findCountryByName(String countryName) {
		for (Country country : d_player.getD_playerCountries()) {
			if (country.getD_countryName().equalsIgnoreCase(countryName)) {
				return country;
			}
		}
		return null;
	}

	/**
	 * Checks the validation before executing orders.
	 */
	@Override
	public boolean isValid(GameState p_gameState) {
		Country l_sourceCountry = findCountryByName(this.d_sourceCountry.toString());

		if (l_sourceCountry == null) {
			String l_logMsg = this.currentOrder() + " not executed as Source country : " + this.d_sourceCountry
					+ " given in card order does not belong to the player : " + d_player.getD_playerName();
			this.setD_orderExecutionLog(l_logMsg, AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}

		Country l_targetCountry = findCountryByName(this.d_targetCountry.toString());

		if (l_targetCountry == null) {
			String l_logMsg = this.currentOrder() + " not executed since Target country : " + this.d_sourceCountry
					+ " given in card order does not belong to the player : " + d_player.getD_playerName();
			this.setD_orderExecutionLog(l_logMsg, AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}

		if (this.d_armyCount > l_sourceCountry.getD_armyCount()) {
			String l_logMsg = this.currentOrder()
					+ " is not executed as armies given in card order exceed armies of source country : "
					+ this.d_sourceCountry;
			this.setD_orderExecutionLog(l_logMsg, AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}

		return true;
	}

	/**
	 * Prints the Order.
	 */
	@Override
	public void printOrder() {
		this.d_orderExecutionLog = "----------Airlift order issued by player " + this.d_player.getD_playerName()
				+ "----------" + System.lineSeparator() + "Move " + this.d_armyCount + " armies from "
				+ this.d_sourceCountry + " to " + this.d_targetCountry;
		System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
	}

	/**
	 * Record the execution log for the order.
	 *
	 * @return String of Log.
	 */
	@Override
	public String orderExecutionLog() {
		return this.d_orderExecutionLog;
	}

	/**
	 * Prints and Sets the order execution log.
	 *
	 * @param p_orderExecutionLog String to be set as log
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
	 * Provides the active advance order currently in progress.
	 *
	 * @return advance order command
	 */
	private String currentOrder() {
		return "Airlift Order : " + "airlift" + " " + this.d_sourceCountry + " " + this.d_targetCountry + " "
				+ this.d_armyCount;
	}

	/**
	 * Verifies preconditions when issuing commands.
	 *
	 * @param p_GameState current GameState Instance
	 * @return Bool to check if the order is valid
	 */
	@Override
	public Boolean checkOrderValidity(GameState p_GameState) {
		Country l_sourceCountry = p_GameState.getD_map().getCountryByName(d_sourceCountry);
		Country l_targetCountry = p_GameState.getD_map().getCountryByName(d_targetCountry);
		if (l_sourceCountry == null) {
			this.setD_orderExecutionLog("Invalid Source Country! Not on the map!",
					AppConstants.ERROR_LOG_MSG);
			p_GameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}
		if (l_targetCountry == null) {
			this.setD_orderExecutionLog("Invalid Target Country! Not on the map!",
					AppConstants.ERROR_LOG_MSG);
			p_GameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
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
		return "airlift";
	}
}