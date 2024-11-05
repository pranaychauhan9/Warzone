package Models;

import java.io.Serializable;

import CommonFunctions.CommonCode;
import Constants.AppConstants;

/**
 * Using the bomb card results in a 50% reduction in the army units of the
 * target country.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class Bomb implements Card, Serializable {

	/**
	 * Bomb card will be occupied by this player.
	 */
	Player d_playerCommencer;

	/**
	 * name of the aim country.
	 */
	String d_targetCountryID;

	/**
	 * Sets the Log containing Information about orders.
	 */
	String d_orderHistory;

	/**
	 * The constructor receives all the parameters necessary to implement the order.
	 * 
	 * @param p_playerCommencer Player
	 * @param p_targetCountryID target country ID
	 */
	public Bomb(Player p_playerCommencer, String p_targetCountryID) {
		this.d_playerCommencer = p_playerCommencer;
		this.d_targetCountryID = p_targetCountryID;
	}

	/**
	 * Executes the Bomb order.
	 * 
	 * @param p_gameState current state of the game.
	 */
	@Override
	public void execute(GameState p_gameState) {
		if (isValid(p_gameState)) {
			Player l_targetCountryPlayer = fetchTargetCountryPlayer(p_gameState, d_targetCountryID);
			if (l_targetCountryPlayer != null) {
				if (l_targetCountryPlayer.d_negotiatedWith.contains(this.d_playerCommencer)) {
					this.setD_orderExecutionLog(
							"Bomb Command cannot be executed as Player:" + this.d_playerCommencer.getD_playerName()
									+ " negotiated terms with Player:" + l_targetCountryPlayer.getD_playerName(),
							AppConstants.LOG_MSG);
				} else {
					Country l_targetCountryID = p_gameState.getD_map().getCountryByName(d_targetCountryID);
					Integer l_noOfArmiesOnTargetCountry = l_targetCountryID.getD_armyCount() == 0 ? 1
							: l_targetCountryID.getD_armyCount();
					Integer l_newArmies = (int) Math.floor(l_noOfArmiesOnTargetCountry / 2);
					l_targetCountryID.setD_armyCount(l_newArmies);
					d_playerCommencer.removeCard("bomb");
					this.setD_orderExecutionLog(
							"\nPlayer : " + this.d_playerCommencer.getD_playerName()
									+ " is executing Bomb card on country :  "
									+ l_targetCountryID.getD_countryName() + " with armies :  "
									+ l_noOfArmiesOnTargetCountry
									+ ". New armies: " + l_targetCountryID.getD_armyCount(),
							AppConstants.LOG_MSG);
				}
			}
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
		}
	}

	/**
	 * Fetches the player owning the target country.
	 * 
	 * @param p_gameState     current state of the game
	 * @param p_targetCountry name of the target country
	 * @return target country player
	 */
	private Player fetchTargetCountryPlayer(GameState p_gameState, String p_targetCountry) {
		Player l_playerOfTargetCountry = null;
		for (Player l_player : p_gameState.getD_playerList()) {
			String l_cont = l_player.getCountryList().stream()
					.filter(l_country -> l_country.equalsIgnoreCase(p_targetCountry)).findFirst().orElse(null);
			if (!CommonCode.isEmpty(l_cont)) {
				l_playerOfTargetCountry = l_player;
			}
		}
		return l_playerOfTargetCountry;
	}

	/**
	 * Gives current bomb order which is being executed.
	 * 
	 * @return advance order command
	 */
	private String currentOrder() {
		return "Bomb card order : " + "bomb" + " " + this.d_targetCountryID;
	}

	/**
	 * Validates whether target country belongs to the Player who executed the order
	 * or not.
	 * 
	 * @return boolean if given advance command is valid or not.
	 */
	@Override
	public boolean isValid(GameState p_gameState) {
		Country l_country = d_playerCommencer.getD_playerCountries().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryID)).findFirst()
				.orElse(null);

		// Player cannot bomb own territory
		if (!CommonCode.isNull(l_country)) {
			this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country : "
					+ this.d_targetCountryID + " given in bomb command is owned by the player : "
					+ d_playerCommencer.getD_playerName()
					+ " VALIDATES:- You are not allowed to use the bomb on your own territory!",
					AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}
		return true;
	}

	/**
	 * Printing the Bomb order.
	 */
	@Override
	public void printOrder() {
		this.d_orderHistory = "----------Bomb card order initiated by player "
				+ this.d_playerCommencer.getD_playerName() + "----------" + System.lineSeparator()
				+ "Creating a bomb order = " + "on country ID. " + this.d_targetCountryID;
		System.out.println(System.lineSeparator() + this.d_orderHistory);

	}

	/**
	 * Execution log.
	 * 
	 * @return String return execution log
	 */
	public String orderExecutionLog() {
		return this.d_orderHistory;
	}

	/**
	 * Prints and Sets the order execution log.
	 *
	 * @param p_orderExecutionLog String to be set as log
	 * @param p_logType           type of log : error, default
	 */
	public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
		this.d_orderHistory = p_orderExecutionLog;
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
	 * Pre-validation of card type order.
	 * 
	 * @param p_gameState object of GameState
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
	 * Return order name.
	 * 
	 * @return String order name
	 */
	@Override
	public String getOrderName() {
		return "bomb";
	}
}
