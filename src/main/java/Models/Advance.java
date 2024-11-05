package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import CommonFunctions.CommonCode;
import Constants.AppConstants;
import Services.PlayerService;

/**
 * Represents a specific command following the Command pattern.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class Advance implements Order, Serializable {

	/**
	 * Name of the country receiving the new armies.
	 */
	String d_targetCountry;

	/**
	 * Name of the country from which armies are to be transferred.
	 */
	String d_sourceCountry;

	/**
	 * number of armies to be allocated.
	 */
	Integer d_armiesToAllocate;

	/**
	 * Initiating player for the order.
	 */
	Player d_playerInitiator;

	/**
	 * Log containing information about the orders.
	 */
	String d_orderExecutionLog;

	/**
	 * Constructor receiving all necessary parameters for executing the order.
	 *
	 * @param p_playerInitiator       Player initiating the order
	 * @param p_sourceCountryName     Country from which armies are transferred
	 * @param p_targetCountry         Country receiving the new armies
	 * @param p_numberOfArmiesToPlace Number of armies to be added
	 */
	public Advance(Player p_playerInitiator, String p_sourceCountryName, String p_targetCountry,
			Integer p_numberOfArmiesToPlace) {
		this.d_targetCountry = p_targetCountry;
		this.d_sourceCountry = p_sourceCountryName;
		this.d_playerInitiator = p_playerInitiator;
		this.d_armiesToAllocate = p_numberOfArmiesToPlace;
	}

	/**
	 * Executes the order and makes necessary changes to the game state.
	 *
	 * @param p_gameState Current state of the game
	 */
	@Override
	public void execute(GameState p_gameState) {
		if (isValid(p_gameState)) {
			Player l_targetCountryPlayer = fetchTargetCountryPlayer(p_gameState);
			if (l_targetCountryPlayer != null) {
				if (l_targetCountryPlayer.d_negotiatedWith.contains(this.d_playerInitiator)) {
					this.setD_orderExecutionLog(
							"Advance Command cannot be executed as Player:" + this.d_playerInitiator.getD_playerName()
									+ " negotiated terms with Player:" + l_targetCountryPlayer.getD_playerName(),
							AppConstants.LOG_MSG);
					p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
				} else {
					Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountry);
					Country l_sourceCountry = p_gameState.getD_map().getCountryByName(d_sourceCountry);
					Integer l_sourceArmyCountUpdate = l_sourceCountry.getD_armyCount() - this.d_armiesToAllocate;
					l_sourceCountry.setD_armyCount(l_sourceArmyCountUpdate);
					processOrder(p_gameState, l_targetCountryPlayer, l_targetCountry, l_sourceCountry);
				}
			}
		} else {
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
		}
	}

	/**
	 * Process the specific order based on conditions determined in the 'execute'
	 * method.
	 * This method handles the logic to execute different actions according to the
	 * target country, player,
	 * and current game state, directing the flow of the order's execution.
	 *
	 * @param p_gameState           Current state of the game
	 * @param p_targetCountryPlayer Player of the target country
	 * @param p_targetCountry       Target country specified in the order
	 * @param p_sourceCountry       Source country specified in the order
	 */
	private void processOrder(GameState p_gameState, Player p_targetCountryPlayer, Country p_targetCountry,
			Country p_sourceCountry) {
		if (p_targetCountryPlayer.getD_playerName().equalsIgnoreCase(this.d_playerInitiator.getD_playerName())) {
			deployArmiesToTarget(p_targetCountry);
		} else if (p_targetCountry.getD_armyCount() == null) {
			conquerTargetCountry(p_gameState, p_targetCountryPlayer, p_targetCountry);
			this.d_playerInitiator.allocateCard();
		} else {
			processOrderResult(p_gameState, p_targetCountryPlayer, p_targetCountry, p_sourceCountry);
		}
	}

	/**
	 * Process and return the result of the advance order after battle.
	 *
	 * @param p_gameState           Current state of the game
	 * @param p_targetCountryPlayer Player owning the target country
	 * @param p_targetCountry       Target country specified in the order
	 * @param p_sourceCountry       Source country specified in the order
	 */
	private void processOrderResult(GameState p_gameState, Player p_targetCountryPlayer, Country p_targetCountry,
			Country p_sourceCountry) {
		int l_armiesInAttack = Math.min(this.d_armiesToAllocate, p_targetCountry.getD_armyCount());

		List<Integer> l_attackerArmies = createArmyUnits(l_armiesInAttack, "attacker");
		List<Integer> l_defenderArmies = createArmyUnits(l_armiesInAttack, "defender");
		this.processBattleResult(p_sourceCountry, p_targetCountry, l_attackerArmies, l_defenderArmies,
				p_targetCountryPlayer);

		p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
		this.updateContinents(this.d_playerInitiator, p_targetCountryPlayer, p_gameState);
	}

	/**
	 * Conquers the target country when it has no defending armies.
	 *
	 * @param p_gameState           Current state of the game
	 * @param p_targetCountryPlayer Player owning the target country
	 * @param p_targetCountry       Target country involved in the battle
	 */
	private void conquerTargetCountry(GameState p_gameState, Player p_targetCountryPlayer, Country p_targetCountry) {
		p_targetCountry.setD_armyCount(d_armiesToAllocate);
		p_targetCountryPlayer.getD_playerCountries().remove(p_targetCountry);
		this.d_playerInitiator.getD_playerCountries().add(p_targetCountry);
		this.setD_orderExecutionLog(
				"Player : " + this.d_playerInitiator.getD_playerName() + " is assigned with Country : "
						+ p_targetCountry.getD_countryName() + " and armies : " + p_targetCountry.getD_armyCount(),
				AppConstants.LOG_MSG);
		p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
		this.updateContinents(this.d_playerInitiator, p_targetCountryPlayer, p_gameState);
	}

	/**
	 * Fetches the player owning the target country.
	 * 
	 * @param p_gameState current state of the game
	 * @return target country player
	 */
	private Player fetchTargetCountryPlayer(GameState p_gameState) {
		Player l_playerOfTargetCountry = null;
		for (Player l_player : p_gameState.getD_playerList()) {
			String l_cont = l_player.getCountryList().stream()
					.filter(l_country -> l_country.equalsIgnoreCase(this.d_targetCountry)).findFirst().orElse(null);
			if (!CommonCode.isEmpty(l_cont)) {
				l_playerOfTargetCountry = l_player;
			}
		}
		return l_playerOfTargetCountry;
	}

	/**
	 * Deploys the allocated armies to the target country.
	 *
	 * @param p_targetCountry The country to which armies are to be deployed
	 */
	public void deployArmiesToTarget(Country p_targetCountry) {
		Integer l_updatedTargetContArmies = p_targetCountry.getD_armyCount() == null ? this.d_armiesToAllocate
				: p_targetCountry.getD_armyCount() + this.d_armiesToAllocate;
		p_targetCountry.setD_armyCount(l_updatedTargetContArmies);
	}

	/**
	 * Processes the battle result by determining surviving armies and handling the
	 * aftermath.
	 *
	 * @param p_sourceCountry       The source country of the battle
	 * @param p_targetCountry       The target country of the battle
	 * @param p_attackerArmies      List of integers representing the attacker's
	 *                              armies
	 * @param p_defenderArmies      List of integers representing the defender's
	 *                              armies
	 * @param p_targetCountryPlayer Player who owns the target country
	 */
	private void processBattleResult(Country p_sourceCountry, Country p_targetCountry, List<Integer> p_attackerArmies,
			List<Integer> p_defenderArmies, Player p_targetCountryPlayer) {
		int l_attackerArmiesLeft = determineAttackerArmiesLeft(p_targetCountry.getD_armyCount());
		int l_defenderArmiesLeft = determineDefenderArmiesLeft(p_targetCountry.getD_armyCount());

		determineSurvivingArmies(p_attackerArmies, p_defenderArmies, l_attackerArmiesLeft, l_defenderArmiesLeft);

		handleSurvivingArmies(l_attackerArmiesLeft, l_defenderArmiesLeft, p_sourceCountry, p_targetCountry,
				p_targetCountryPlayer);
	}

	/**
	 * Determines the number of attacker armies left after the battle.
	 *
	 * @param p_targetCountryArmyCount Number of armies in the target country
	 * @return Number of attacker armies left
	 */
	private int determineAttackerArmiesLeft(int p_targetCountryArmyCount) {
		return this.d_armiesToAllocate > p_targetCountryArmyCount ? this.d_armiesToAllocate - p_targetCountryArmyCount
				: 0;
	}

	/**
	 * Determines the number of defender armies left after the battle.
	 *
	 * @param p_targetCountryArmyCount Number of armies in the target country
	 * @return Number of defender armies left
	 */
	private int determineDefenderArmiesLeft(int p_targetCountryArmyCount) {
		return this.d_armiesToAllocate < p_targetCountryArmyCount ? p_targetCountryArmyCount - this.d_armiesToAllocate
				: 0;
	}

	/**
	 * Determines the surviving armies during the battle.
	 *
	 * @param p_attackerArmies     List of attacker armies
	 * @param p_defenderArmies     List of defender armies
	 * @param p_attackerArmiesLeft Remaining attacker armies
	 * @param p_defenderArmiesLeft Remaining defender armies
	 */
	private void determineSurvivingArmies(List<Integer> p_attackerArmies, List<Integer> p_defenderArmies,
			int p_attackerArmiesLeft, int p_defenderArmiesLeft) {
		for (int i = 0; i < p_attackerArmies.size(); i++) {
			if (p_attackerArmies.get(i) > p_defenderArmies.get(i)) {
				p_attackerArmiesLeft++;
			} else {
				p_defenderArmiesLeft++;
			}
		}
	}

	/**
	 * Handles the surviving armies after a battle and updates country ownership and
	 * armies accordingly.
	 *
	 * @param p_attackerArmiesLeft  Remaining attacker armies after the battle
	 * @param p_defenderArmiesLeft  Remaining defender armies after the battle
	 * @param p_sourceCountry       Source country of the battle
	 * @param p_targetCountry       Target country of the battle
	 * @param p_targetCountryPlayer Player owning the target country
	 */
	public void handleSurvivingArmies(int p_attackerArmiesLeft, int p_defenderArmiesLeft,
			Country p_sourceCountry, Country p_targetCountry, Player p_targetCountryPlayer) {
		if (p_defenderArmiesLeft == 0) {
			transferOwnership(p_attackerArmiesLeft, p_targetCountry, p_targetCountryPlayer);
		} else {
			updateArmies(p_sourceCountry, p_targetCountry, p_attackerArmiesLeft, p_defenderArmiesLeft,
					p_targetCountryPlayer);
		}
	}

	/**
	 * Transfers ownership when defender armies are eliminated.
	 *
	 * @param p_attackerArmiesLeft  Remaining attacker armies after the battle
	 * @param p_targetCountry       Target country of the battle
	 * @param p_targetCountryPlayer Player owning the target country
	 */
	private void transferOwnership(int p_attackerArmiesLeft, Country p_targetCountry, Player p_targetCountryPlayer) {
		p_targetCountryPlayer.getD_playerCountries().remove(p_targetCountry);
		p_targetCountry.setD_armyCount(p_attackerArmiesLeft);
		this.d_playerInitiator.getD_playerCountries().add(p_targetCountry);
		updateLogAfterTransfer(p_targetCountryPlayer, p_targetCountry);
		this.d_playerInitiator.allocateCard();
	}

	/**
	 * Updates armies and logs when defender armies survive.
	 *
	 * @param p_sourceCountry       Source country of the battle
	 * @param p_targetCountry       Target country of the battle
	 * @param p_attackerArmiesLeft  Remaining attacker armies after the battle
	 * @param p_defenderArmiesLeft  Remaining defender armies after the battle
	 * @param p_targetCountryPlayer Player owning the target country
	 */
	private void updateArmies(Country p_sourceCountry, Country p_targetCountry, int p_attackerArmiesLeft,
			int p_defenderArmiesLeft, Player p_targetCountryPlayer) {
		p_targetCountry.setD_armyCount(p_defenderArmiesLeft);
		int l_sourceArmiesToUpdate = p_sourceCountry.getD_armyCount() + p_attackerArmiesLeft;
		p_sourceCountry.setD_armyCount(l_sourceArmiesToUpdate);
		updateLogAfterBattle(p_targetCountry, p_sourceCountry, p_targetCountryPlayer);
	}

	/**
	 * Updates log after ownership transfer.
	 *
	 * @param p_targetCountryPlayer Player owning the target country
	 * @param p_targetCountry       Target country of the battle
	 */
	private void updateLogAfterTransfer(Player p_targetCountryPlayer, Country p_targetCountry) {
		String l_logEntry = "Player: " + this.d_playerInitiator.getD_playerName() + " is assigned with Country: "
				+ p_targetCountry.getD_countryName() + " and armies: " + p_targetCountry.getD_armyCount();
		setD_orderExecutionLog(l_logEntry, AppConstants.LOG_MSG);
	}

	/**
	 * Updates log after battle when defender armies survive.
	 *
	 * @param p_targetCountry       Target country of the battle
	 * @param p_sourceCountry       Source country of the battle
	 * @param p_targetCountryPlayer Player owning the target country
	 */
	private void updateLogAfterBattle(Country p_targetCountry, Country p_sourceCountry,
			Player p_targetCountryPlayer) {
		String l_logEntry1 = "Country: " + p_targetCountry.getD_countryName() + " is left with "
				+ p_targetCountry.getD_armyCount() + " armies and is still owned by player: "
				+ p_targetCountryPlayer.getD_playerName();
		String l_logEntry2 = "Country: " + p_sourceCountry.getD_countryName() + " is left with "
				+ p_sourceCountry.getD_armyCount() + " armies and is still owned by player: "
				+ this.d_playerInitiator.getD_playerName();
		setD_orderExecutionLog(l_logEntry1 + System.lineSeparator() + l_logEntry2, AppConstants.LOG_MSG);
	}

	/**
	 * Validates whether country given for deploy belongs to players countries or
	 * not.
	 * 
	 * @return boolean if given advance command is valid or not.
	 */

	@Override
	public boolean isValid(GameState p_gameState) {
		Country l_country = d_playerInitiator.getD_playerCountries().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_sourceCountry.toString()))
				.findFirst().orElse(null);
		if (l_country == null) {
			this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Source country : "
					+ this.d_sourceCountry + " given in advance command does not belongs to the player : "
					+ d_playerInitiator.getD_playerName(), AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}
		if (this.d_armiesToAllocate > l_country.getD_armyCount()) {
			this.setD_orderExecutionLog(this.currentOrder()
					+ " is not executed as armies given in advance order exceeds armies of source country : "
					+ this.d_sourceCountry, AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}
		if (this.d_armiesToAllocate == l_country.getD_armyCount()) {
			this.setD_orderExecutionLog(this.currentOrder() + " is not executed as source country : "
					+ this.d_sourceCountry + " has " + l_country.getD_armyCount()
					+ " army units and all of those cannot be given advance order, atleast one army unit has to retain the territory.",
					AppConstants.ERROR_LOG_MSG);
			p_gameState.updateLog(orderExecutionLog(), AppConstants.ORDER_EFFECT);
			return false;
		}
		return true;
	}

	/**
	 * Gives current advance order which is being executed.
	 * 
	 * @return advance order command
	 */
	private String currentOrder() {
		return "Advance Order : " + "advance" + " " + this.d_sourceCountry + " " + this.d_targetCountry + " "
				+ this.d_armiesToAllocate;
	}

	/**
	 * Prints information about order.
	 */
	@Override
	public void printOrder() {
		this.d_orderExecutionLog = "\n---------- Advance order issued by player "
				+ this.d_playerInitiator.getD_playerName()
				+ " ----------\n" + System.lineSeparator() + "Move " + this.d_armiesToAllocate + " armies from "
				+ this.d_sourceCountry + " to " + this.d_targetCountry;
		System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
	}

	/**
	 * Gets updated execution log.
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
		if (p_logType.equals(AppConstants.ERROR_LOG_MSG)) {
			System.err.println(p_orderExecutionLog);
		} else {
			System.out.println(p_orderExecutionLog);
		}
	}

	/**
	 * Creates army units based on attacker and defender's winning
	 * chances.
	 * 
	 * @param p_size number of random armies to be generated
	 * @param p_role armies to be generated is for defender or for attacker
	 * @return List random army units based on probability
	 */
	private List<Integer> createArmyUnits(int p_size, String p_role) {
		List<Integer> l_armyList = new ArrayList<>();
		Double l_probability = "attacker".equalsIgnoreCase(p_role) ? 0.6 : 0.7;
		for (int l_i = 0; l_i < p_size; l_i++) {
			int l_randomNumber = getRandomInteger(10, 1);
			Integer l_armyUnit = (int) Math.round(l_randomNumber * l_probability);
			l_armyList.add(l_armyUnit);
		}
		return l_armyList;
	}

	/**
	 * returns random integer between minimum and maximum range.
	 * 
	 * @param p_maximum upper limit
	 * @param p_minimum lower limit
	 * @return int random number
	 */
	private static int getRandomInteger(int p_maximum, int p_minimum) {
		return ((int) (Math.random() * (p_maximum - p_minimum))) + p_minimum;
	}

	/**
	 * Updates continents of players based on battle results.
	 * 
	 * @param p_playerOfSourceCountry player owning source country
	 * @param p_targetCountryPlayer   player owning target country
	 * @param p_gameState             current state of the game
	 */
	private void updateContinents(Player p_playerOfSourceCountry, Player p_targetCountryPlayer,
			GameState p_gameState) {
		System.out.println("Updating continents of players involved in battle...");
		List<Player> l_playesList = new ArrayList<>();
		p_playerOfSourceCountry.setD_playerContinents(new ArrayList<>());
		p_targetCountryPlayer.setD_playerContinents(new ArrayList<>());
		l_playesList.add(p_playerOfSourceCountry);
		l_playesList.add(p_targetCountryPlayer);

		PlayerService l_playerService = new PlayerService();
		l_playerService.allocRandomContinents(l_playesList, p_gameState.getD_map().getD_continents());
	}

	/**
	 * Gets order name.
	 */
	@Override
	public String getOrderName() {
		return "advance";
	}
}
