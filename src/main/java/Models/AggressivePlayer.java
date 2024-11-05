package Models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Defines an Aggressive Player strategy. This player accumulates all his
 * armies,
 * launches attacks from his most powerful territory, and strategically places
 * armies
 * to strengthen his position in a specific country.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class AggressivePlayer extends PlayerBehaviorStrategy {

	/**
	 * An ArrayList to hold countries for deployment sequence.
	 */
	ArrayList<Country> d_deployCountries = new ArrayList<Country>();

	/**
	 * Generates a new order for the player.
	 * 
	 * @param p_player    Reference to a Player object
	 * @param p_gameState Reference to a GameState object
	 * 
	 * @return A string representation of the generated order
	 */
	@Override
	public String generateOrder(Player p_player, GameState p_gameState) {
		System.out.println("Creating order for : " + p_player.getD_playerName());
		String l_command;

		if (p_player.getD_unallocatedArmyCount() > 0) {
			l_command = generateDeployOrder(p_player, p_gameState);
		} else {
			if (p_player.getD_playerOwnedCards().size() > 0) {
				Random l_random = new Random();
				int l_randomIndex = l_random.nextInt(p_player.getD_playerOwnedCards().size() + 1);
				if (l_randomIndex == p_player.getD_playerOwnedCards().size()) {
					l_command = generateAdvanceOrder(p_player, p_gameState);
				} else {
					l_command = generateCardOrder(p_player, p_gameState,
							p_player.getD_playerOwnedCards().get(l_randomIndex));
				}
			} else {
				l_command = generateAdvanceOrder(p_player, p_gameState);
			}
		}
		return l_command;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateDeployOrder(Player p_player, GameState p_gameState) {
		Random l_random = new Random();
		// get strongest country then deploy
		Country l_strongestCountry = getStrongestCountry(p_player, p_gameState);
		d_deployCountries.add(l_strongestCountry);
		int l_armiesToDeploy = 1;
		if (p_player.getD_unallocatedArmyCount() > 1) {
			l_armiesToDeploy = l_random.nextInt(p_player.getD_unallocatedArmyCount() - 1) + 1;
		}
		return String.format("deploy %s %d", l_strongestCountry.getD_countryName(), l_armiesToDeploy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
		// move armies from its neighbors to maximize armies on source country
		Country l_randomSourceCountry = getRandomCountry(d_deployCountries);
		moveArmiesFromItsNeighbors(p_player, l_randomSourceCountry, p_gameState);

		Random l_random = new Random();
		Country l_randomTargetCountry = p_gameState.getD_map()
				.getCountry(l_randomSourceCountry.getD_adjacentCountryIds()
						.get(l_random.nextInt(l_randomSourceCountry.getD_adjacentCountryIds().size())));

		int l_armiesToSend = l_randomSourceCountry.getD_armyCount() != null
				&& l_randomSourceCountry.getD_armyCount() > 1 ? l_randomSourceCountry.getD_armyCount() : 1;

		// attacks with strongest country
		return "advance " + l_randomSourceCountry.getD_countryName() + " " + l_randomTargetCountry.getD_countryName()
				+ " " + l_armiesToSend;

	}

	/**
	 * Consolidates armies by moving them from neighboring countries.
	 * 
	 * @param p_player              The player performing the action
	 * @param p_randomSourceCountry The source country for the movement
	 * @param p_gameState           The current state of the game
	 */
	public void moveArmiesFromItsNeighbors(Player p_player, Country p_randomSourceCountry, GameState p_gameState) {
		List<Integer> l_adjacentCountryIds = p_randomSourceCountry.getD_adjacentCountryIds();
		List<Country> l_listOfNeighbors = new ArrayList<Country>();
		for (int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++) {
			Country l_country = p_gameState.getD_map()
					.getCountry(p_randomSourceCountry.getD_adjacentCountryIds().get(l_index));
			if (p_player.getD_playerCountries().contains(l_country)) {
				l_listOfNeighbors.add(l_country);
			}
		}

		int l_ArmiesToMove = 0;
		for (Country l_con : l_listOfNeighbors) {
			l_ArmiesToMove += p_randomSourceCountry.getD_armyCount() != null
					&& p_randomSourceCountry.getD_armyCount() > 0
							? p_randomSourceCountry.getD_armyCount() + (l_con.getD_armyCount())
							: (l_con.getD_armyCount());

		}
		p_randomSourceCountry.setD_armyCount(l_ArmiesToMove);
	}

	/**
	 * Chooses a country at random from a given list.
	 * 
	 * @param p_listOfCountries A list of country objects
	 * @return A randomly selected country
	 */
	private Country getRandomCountry(List<Country> p_listOfCountries) {
		Random l_random = new Random();
		return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
		Random l_random = new Random();
		Country l_StrongestSourceCountry = getStrongestCountry(p_player, d_gameState);

		Country l_randomTargetCountry = p_gameState.getD_map()
				.getCountry(l_StrongestSourceCountry.getD_adjacentCountryIds()
						.get(l_random.nextInt(l_StrongestSourceCountry.getD_adjacentCountryIds().size())));

		int l_armiesToSend = l_StrongestSourceCountry.getD_armyCount() != null
				&& l_StrongestSourceCountry.getD_armyCount() > 1 ? l_StrongestSourceCountry.getD_armyCount()
						: 1;

		switch (p_cardName) {
			case "bomb":
				return "bomb " + l_randomTargetCountry.getD_countryName();
			case "blockade":
				return "blockade " + l_StrongestSourceCountry.getD_countryName();
			case "airlift":
				return "airlift " + l_StrongestSourceCountry.getD_countryName() + " "
						+ getRandomCountry(p_player.getD_playerCountries()).getD_countryName() + " " + l_armiesToSend;
			case "negotiate":
				return "negotiate" + " " + getRandomEnemyPlayer(p_player, p_gameState).getD_playerName();
		}
		return null;
	}

	/**
	 * Selects a random opposing player.
	 * 
	 * @param p_player    The player making the selection
	 * @param p_gameState The current state of the game
	 * @return A player chosen at random from the enemy players
	 */
	private Player getRandomEnemyPlayer(Player p_player, GameState p_gameState) {
		ArrayList<Player> l_playerList = new ArrayList<Player>();
		Random l_random = new Random();

		for (Player l_player : p_gameState.getD_playerList()) {
			if (!l_player.equals(p_player))
				l_playerList.add(p_player);
		}
		return l_playerList.get(l_random.nextInt(l_playerList.size()));
	}

	/**
	 * This method returns the player behavior.
	 * 
	 * @return String player behavior
	 */
	@Override
	public String getPlayerBehavior() {
		return "Aggressive";
	}

	/**
	 * Get strongest country.
	 * 
	 * @param p_player    Player
	 * @param p_gameState Game state
	 * @return Strongest country
	 */
	public Country getStrongestCountry(Player p_player, GameState p_gameState) {
		List<Country> l_countriesOwnedByPlayer = p_player.getD_playerCountries();
		Country l_Country = calculateStrongestCountry(l_countriesOwnedByPlayer);
		return l_Country;
	}

	/**
	 * This method calculates strongest country.
	 * 
	 * @param l_listOfCountries List of countries
	 * @return strongest country
	 */
	public Country calculateStrongestCountry(List<Country> l_listOfCountries) {
		LinkedHashMap<Country, Integer> l_CountryWithArmies = new LinkedHashMap<Country, Integer>();

		int l_largestNoOfArmies;
		Country l_Country = null;
		// return strongest country from owned countries of player.
		for (Country l_country : l_listOfCountries) {
			l_CountryWithArmies.put(l_country, l_country.getD_armyCount() == null ? 0 : l_country.getD_armyCount());
		}
		l_largestNoOfArmies = Collections.max(l_CountryWithArmies.values());
		for (Entry<Country, Integer> entry : l_CountryWithArmies.entrySet()) {
			if (entry.getValue().equals(l_largestNoOfArmies)) {
				return entry.getKey();
			}
		}
		return l_Country;

	}

	/**
	 * Check if it is first turn.
	 *
	 * @param p_player player instance
	 * @return boolean
	 */
	private Boolean checkIfArmiesDepoyed(Player p_player) {
		if (p_player.getD_playerCountries().stream().anyMatch(l_country -> l_country.getD_armyCount() > 0)) {
			return true;
		}
		return false;
	}

}
