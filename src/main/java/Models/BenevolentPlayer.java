package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 * This denotes the category of a Benevolent Player who
 * prioritizes the defense of their own nations
 * and refrains from initiating any attacks.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class BenevolentPlayer extends PlayerBehaviorStrategy {

	/**
	 * Sequence of countries based on deployment order.
	 */
	ArrayList<Country> d_deployCountries = new ArrayList<Country>();

	/**
	 * This function generates a fresh order.
	 * 
	 * @param p_player    object of Player class
	 * @param p_gameState object of GameState class
	 * 
	 * @return Order object of order class
	 */
	@Override
	public String generateOrder(Player p_player, GameState p_gameState) {
		String l_command;
		if (!checkIfArmiesDepoyed(p_player)) {
			if (p_player.getD_unallocatedArmyCount() > 0) {
				l_command = generateDeployOrder(p_player, p_gameState);
			} else {
				l_command = generateAdvanceOrder(p_player, p_gameState);
			}
		} else {
			if (p_player.getD_playerOwnedCards().size() > 0) {
				System.out.println("Enters Card Logic");
				int l_index = (int) (Math.random() * 3) + 1;
				switch (l_index) {
					case 1:
						System.out.println("Deploy!");
						l_command = generateDeployOrder(p_player, p_gameState);
						break;
					case 2:
						System.out.println("Advance!");
						l_command = generateAdvanceOrder(p_player, p_gameState);
						break;
					case 3:
						if (p_player.getD_playerOwnedCards().size() == 1) {
							System.out.println("Cards!");
							l_command = generateCardOrder(p_player, p_gameState,
									p_player.getD_playerOwnedCards().get(0));
							break;
						} else {
							Random l_random = new Random();
							int l_randomIndex = l_random.nextInt(p_player.getD_playerOwnedCards().size());
							l_command = generateCardOrder(p_player, p_gameState,
									p_player.getD_playerOwnedCards().get(l_randomIndex));
							break;
						}
					default:
						l_command = generateAdvanceOrder(p_player, p_gameState);
						break;
				}
			} else {
				Random l_random = new Random();
				Boolean l_randomBoolean = l_random.nextBoolean();
				if (l_randomBoolean) {
					System.out.println("Without Card Deploy Logic");
					l_command = generateDeployOrder(p_player, p_gameState);
				} else {
					System.out.println("Without Card Advance Logic");
					l_command = generateAdvanceOrder(p_player, p_gameState);
				}
			}
		}
		return l_command;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateDeployOrder(Player p_player, GameState p_gameState) {
		if (p_player.getD_unallocatedArmyCount() > 0) {
			Country l_weakestCountry = getWeakestCountry(p_player);
			d_deployCountries.add(l_weakestCountry);

			Random l_random = new Random();
			int l_armiesToDeploy = l_random.nextInt(p_player.getD_unallocatedArmyCount()) + 1;

			System.out.println("deploy " + l_weakestCountry.getD_countryName() + " " + l_armiesToDeploy);
			return String.format("deploy %s %d", l_weakestCountry.getD_countryName(), l_armiesToDeploy);
		} else {
			return generateAdvanceOrder(p_player, p_gameState);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
		// advance on weakest country
		int l_armiesToSend;
		Random l_random = new Random();

		Country l_randomSourceCountry = getRandomCountry(d_deployCountries);
		System.out.println("Source country" + l_randomSourceCountry.getD_countryName());
		Country l_weakestTargetCountry = getWeakestNeighbor(l_randomSourceCountry, p_gameState);
		System.out.println("Target Country" + l_weakestTargetCountry.getD_countryName());
		if (l_randomSourceCountry.getD_armyCount() > 1) {
			l_armiesToSend = l_random.nextInt(l_randomSourceCountry.getD_armyCount() - 1) + 1;
		} else {
			l_armiesToSend = 1;
		}

		System.out.println("advance " + l_randomSourceCountry.getD_countryName() + " "
				+ l_weakestTargetCountry.getD_countryName() + " " + l_armiesToSend);
		return "advance " + l_randomSourceCountry.getD_countryName() + " " + l_weakestTargetCountry.getD_countryName()
				+ " " + l_armiesToSend;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
		int l_armiesToSend;
		Random l_random = new Random();
		Country l_randomOwnCountry = getRandomCountry(p_player.getD_playerCountries());
		Country l_randomEnemyNeighbor = p_gameState.getD_map()
				.getCountry(randomEnemyNeighbor(p_player, l_randomOwnCountry)
						.get(l_random.nextInt(randomEnemyNeighbor(p_player, l_randomOwnCountry).size())));

		if (l_randomOwnCountry.getD_armyCount() > 1) {
			l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getD_armyCount() - 1) + 1;
		} else {
			l_armiesToSend = 1;
		}

		switch (p_cardName) {
			case "bomb":
				System.err.println("I am benevolent player, I don't hurt anyone.");
				return "bomb" + " " + "false";
			case "blockade":
				return "blockade " + l_randomOwnCountry.getD_countryName();
			case "airlift":
				return "airlift " + l_randomOwnCountry.getD_countryName() + " "
						+ getRandomCountry(p_player.getD_playerCountries()).getD_countryName() + " " + l_armiesToSend;
			case "negotiate":
				return "negotiate " + p_player.getD_playerName();
		}
		return null;
	}

	/**
	 * This method returns the player behavior.
	 * 
	 * @return String player behavior
	 */
	@Override
	public String getPlayerBehavior() {
		return "Benevolent";
	}

	/**
	 * This function yields the player's behavior.
	 * 
	 * @param p_listOfCountries list of countries
	 * @return return country
	 */
	private Country getRandomCountry(List<Country> p_listOfCountries) {
		Random l_random = new Random();
		return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
	}

	/**
	 * This function returns the weakest country where a benevolent player can
	 * deploy armies.
	 * 
	 * @param p_player Player
	 * @return weakest country
	 */
	public Country getWeakestCountry(Player p_player) {
		List<Country> l_countriesOwnedByPlayer = p_player.getD_playerCountries();
		Country l_Country = calculateWeakestCountry(l_countriesOwnedByPlayer);
		return l_Country;
	}

	/**
	 * This function returns the weakest neighbor where
	 * the source country can advance armies.
	 * 
	 * @param l_randomSourceCountry Source country
	 * @param p_gameState           GameState
	 * @return weakest neighbor
	 */
	public Country getWeakestNeighbor(Country l_randomSourceCountry, GameState p_gameState) {
		List<Integer> l_adjacentCountryIds = l_randomSourceCountry.getD_adjacentCountryIds();
		List<Country> l_listOfNeighbors = new ArrayList<Country>();
		for (int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++) {
			Country l_country = p_gameState.getD_map()
					.getCountry(l_randomSourceCountry.getD_adjacentCountryIds().get(l_index));
			l_listOfNeighbors.add(l_country);
		}
		Country l_Country = calculateWeakestCountry(l_listOfNeighbors);

		return l_Country;
	}

	/**
	 * This function computes the weakest country.
	 * 
	 * @param l_listOfCountries list of countries
	 * @return weakest country
	 */
	public Country calculateWeakestCountry(List<Country> l_listOfCountries) {
		LinkedHashMap<Country, Integer> l_CountryWithArmies = new LinkedHashMap<Country, Integer>();

		int l_smallestNoOfArmies;
		Country l_Country = null;

		// Returns the weakest country among those owned by the player.
		for (Country l_country : l_listOfCountries) {
			l_CountryWithArmies.put(l_country, l_country.getD_armyCount() == null ? 0 : l_country.getD_armyCount());
		}
		l_smallestNoOfArmies = Collections.min(l_CountryWithArmies.values());
		for (Entry<Country, Integer> entry : l_CountryWithArmies.entrySet()) {
			if (entry.getValue().equals(l_smallestNoOfArmies)) {
				return entry.getKey();
			}
		}
		return l_Country;

	}

	/**
	 * This function provides a list of country IDs for randomly selected enemy
	 * neighbors.
	 * 
	 * @param p_player  Player
	 * @param p_country Country
	 * @return List of Ids.
	 */
	private ArrayList<Integer> randomEnemyNeighbor(Player p_player, Country p_country) {
		ArrayList<Integer> l_enemyNeighbors = new ArrayList<Integer>();

		for (Integer l_countryID : p_country.getD_adjacentCountryIds()) {
			if (!p_player.getCountryIDs().contains(l_countryID))
				l_enemyNeighbors.add(l_countryID);
		}
		return l_enemyNeighbors;
	}

	/**
	 * Verify whether it is the initial turn.
	 *
	 * @param p_player player instance
	 * @return boolean
	 */
	private Boolean checkIfArmiesDepoyed(Player p_player) {
		if (p_player.getD_playerCountries().stream()
				.anyMatch(l_country -> {
					Integer armyCount = l_country.getD_armyCount();
					return armyCount != null && armyCount > 0;
				})) {
			return true;
		}
		return false;
	}
}
