package Models;

import Constants.AppConstants;
import java.io.Serializable;

/**
 * Concrete Command of Command pattern.
 *
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class Deploy implements Order, Serializable {
	/**
	 * Denotes name of the target country.
	 */
	String d_targetCountry;

	/**
	 * Number of armies to be allocated.
	 */
	Integer d_armiesToAllocate;

	/**
	 * Player Initiator.
	 */
	Player d_playerInitiator;

	/**
	 * Sets the Log containing Information about orders.
	 */
	String d_orderExecutionLog;

	/**
	 * parameterized constructor.
	 * 
	 * @param p_playerInitiator  player that created the order
	 * @param p_targetCountry    country that will receive the new armies
	 * @param p_armiesToAllocate number of armies to be added
	 */
	public Deploy(Player p_playerInitiator, String p_targetCountry, Integer p_armiesToAllocate) {
		this.d_targetCountry = p_targetCountry;
		this.d_playerInitiator = p_playerInitiator;
		this.d_armiesToAllocate = p_armiesToAllocate;
	}

	/**
	 * Executes the deploy order.
	 * 
	 * @param p_gameState current state of the game.
	 */
	@Override
	public void execute(GameState p_gameState) {
		if (isValid(p_gameState)) {
			for (Country l_country : p_gameState.getD_map().getD_countries()) {
				if (l_country.getD_countryName().equalsIgnoreCase(this.d_targetCountry)) {
					Integer l_armiesToUpdate = l_country.getD_armyCount() == null ? this.d_armiesToAllocate
							: l_country.getD_armyCount() + this.d_armiesToAllocate;
					l_country.setD_armyCount(l_armiesToUpdate);
					this.setD_orderExecutionLog(l_armiesToUpdate
							+ " armies have been deployed successfully on country : " + l_country.getD_countryName(),
							AppConstants.LOG_MSG);
				}
			}
		} else {
			this.setD_orderExecutionLog("Deploy Order = " + "deploy" + " " + this.d_targetCountry + " "
					+ this.d_armiesToAllocate + " is not executed since Target country: "
					+ this.d_targetCountry + " given in deploy command does not belongs to the player : "
					+ d_playerInitiator.getD_playerName(), AppConstants.ERROR_LOG_MSG);
			d_playerInitiator.setD_unallocatedArmyCount(
					d_playerInitiator.getD_unallocatedArmyCount() + this.d_armiesToAllocate);
		}
		p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
	}

	/**
	 * Validates whether country given for deploy belongs to players countries or
	 * not.
	 */
	@Override
	public boolean isValid(GameState p_gameState) {
		Country l_country = d_playerInitiator.getD_playerCountries().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountry.toString()))
				.findFirst().orElse(null);
		return l_country != null;
	}

	/**
	 * Prints deploy Order.
	 */
	@Override
	public void printOrder() {
		this.d_orderExecutionLog = "\n---------- Deploy order issued by player "
				+ this.d_playerInitiator.getD_playerName() + " ----------\n" + System.lineSeparator() + "Deploy "
				+ this.d_armiesToAllocate + " armies to " + this.d_targetCountry;
		System.out.println(this.d_orderExecutionLog);
	}

	/**
	 * Gets order execution log.
	 */
	@Override
	public String orderExecutionLog() {
		return d_orderExecutionLog;
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
	 * Return order name.
	 * 
	 * @return String
	 */
	@Override
	public String getOrderName() {
		return "deploy";
	}
}
