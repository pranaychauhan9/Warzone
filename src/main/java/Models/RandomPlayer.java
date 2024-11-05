/**
 *
 */
package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This describes the Random Player class, which randomly
 * deploys armies, attacks neighboring countries at random, and moves
 * armies within their own territories randomly.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class RandomPlayer extends PlayerBehaviorStrategy {

	/**
	 * List containing deploy order countries.
	 */
	ArrayList<Country> d_deployCountries = new ArrayList<Country>();

	/**
	 * This function initiates the formation of a new order.
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
				int l_index = (int) (Math.random() * 3) + 1;
				switch (l_index) {
					case 1:
						l_command = generateDeployOrder(p_player, p_gameState);
						break;
					case 2:
						l_command = generateAdvanceOrder(p_player, p_gameState);
						break;
					case 3:
						if (p_player.getD_playerOwnedCards().size() == 1) {
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
					l_command = generateDeployOrder(p_player, p_gameState);
				} else {
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
			Random l_random = new Random();
			System.out.println(p_player.getD_playerCountries().size());
			Country l_randomCountry = getRandomCountry(p_player.getD_playerCountries());
			d_deployCountries.add(l_randomCountry);
			int l_armiesToDeploy = l_random.nextInt(p_player.getD_unallocatedArmyCount()) + 1;

			return String.format("deploy %s %d", l_randomCountry.getD_countryName(), l_armiesToDeploy);
		} else {
			return generateAdvanceOrder(p_player, p_gameState);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
		int l_armiesToSend;
		Random l_random = new Random();
		Country l_randomOwnCountry = getRandomCountry(d_deployCountries);
		int l_randomIndex = l_random.nextInt(l_randomOwnCountry.getD_adjacentCountryIds().size());
		Country l_randomNeighbor;
		if (l_randomOwnCountry.getD_adjacentCountryIds().size() > 1) {
			l_randomNeighbor = p_gameState.getD_map()
					.getCountry(l_randomOwnCountry.getD_adjacentCountryIds().get(l_randomIndex));
		} else {
			l_randomNeighbor = p_gameState.getD_map().getCountry(l_randomOwnCountry.getD_adjacentCountryIds().get(0));
		}

		if (l_randomOwnCountry.getD_armyCount() != null && l_randomOwnCountry.getD_armyCount() > 1) {
			l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getD_armyCount() - 1) + 1;
		} else {
			l_armiesToSend = 1;
		}
		return "advance " + l_randomOwnCountry.getD_countryName() + " " + l_randomNeighbor.getD_countryName() + " "
				+ l_armiesToSend;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
		int l_armiesToSend;
		Random l_random = new Random();
		Country l_randomOwnCountry = getRandomCountry(p_player.getD_playerCountries());

		Country l_randomNeighbour = p_gameState.getD_map().getCountry(l_randomOwnCountry.getD_adjacentCountryIds()
				.get(l_random.nextInt(l_randomOwnCountry.getD_adjacentCountryIds().size())));
		Player l_randomPlayer = getRandomPlayer(p_player, p_gameState);

		if (l_randomOwnCountry.getD_armyCount() != null && l_randomOwnCountry.getD_armyCount() > 1) {
			l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getD_armyCount() - 1) + 1;
		} else {
			l_armiesToSend = 1;
		}
		switch (p_cardName) {
			case "bomb":
				return "bomb " + l_randomNeighbour.getD_countryName();
			case "blockade":
				return "blockade " + l_randomOwnCountry.getD_countryName();
			case "airlift":
				return "airlift " + l_randomOwnCountry.getD_countryName() + " "
						+ getRandomCountry(p_player.getD_playerCountries()).getD_countryName() + " " + l_armiesToSend;
			case "negotiate":
				return "negotiate" + " " + l_randomPlayer.getD_playerName();
		}
		return null;
	}

	/**
	 * This function yields the player's behavior.
	 * 
	 * @return String player behavior
	 */
	@Override
	public String getPlayerBehavior() {
		return "Random";
	}

	/**
	 *
	 * Provides a randomly selected country owned by the player.
	 *
	 * @param p_listOfCountries list of countries owned by player
	 * @return a random country from list
	 */
	private Country getRandomCountry(List<Country> p_listOfCountries) {
		Random l_random = new Random();
		return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
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

	/**
	 * Selects a player at random for negotiation.
	 *
	 * @param p_player    player object
	 * @param p_gameState current gamestate.
	 * @return player object
	 */
	private Player getRandomPlayer(Player p_player, GameState p_gameState) {
		ArrayList<Player> l_playerList = new ArrayList<Player>();
		Random l_random = new Random();

		for (Player l_player : p_gameState.getD_playerList()) {
			if (!l_player.equals(p_player))
				l_playerList.add(p_player);
		}
		return l_playerList.get(l_random.nextInt(l_playerList.size()));
	}
}
