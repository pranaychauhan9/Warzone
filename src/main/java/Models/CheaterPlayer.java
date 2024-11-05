/**
 * The `CheaterPlayer` class represents a player with a cheating strategy in a game.
 * It extends the `PlayerBehaviorStrategy` class and implements methods for generating
 * cheat-related orders during the game.
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

import Services.PlayerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Constants.AppConstants;

/**
 * The `CheaterPlayer` class represents a player with a cheating strategy in a
 * game.
 * It extends the `PlayerBehaviorStrategy` class and implements methods for
 * generating
 * cheat-related orders during the game.
 */
public class CheaterPlayer extends PlayerBehaviorStrategy {

	/**
	 * Generates cheat-related orders for the player.
	 *
	 * @param p_player    The player for whom cheat-related orders are generated.
	 * @param p_gameState The current state of the game.
	 * @return Always returns null.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public String generateOrder(Player p_player, GameState p_gameState) throws IOException {

		if (p_player.getD_unallocatedArmyCount() != 0) {
			while (p_player.getD_unallocatedArmyCount() > 0) {
				Random l_rnd = new Random();
				Country l_rndCountry = getRandomCountry(p_player.getD_playerCountries());
				int l_armiesToDeploy = l_rnd.nextInt(p_player.getD_unallocatedArmyCount()) + 1;

				l_rndCountry.setD_armyCount(l_armiesToDeploy);
				p_player.setD_unallocatedArmyCount(p_player.getD_unallocatedArmyCount() - l_armiesToDeploy);

				String l_logMsg = String.format("Cheater Player: %s assigned %d armies to %s",
						p_player.getD_playerName(), l_armiesToDeploy, l_rndCountry.getD_countryName());

				p_gameState.updateLog(l_logMsg, AppConstants.ORDER_EFFECT);
			}
		}

		overcomeNearbyEnemies(p_player, p_gameState);
		boostEnemyNeighborsCounties(p_player, p_gameState);

		p_player.checkAdditionalOrders(true); // In "player.java" the checkAdditionalOrders method need change
		return null;
	}

	/**
	 * Retrieves a random country from the given list of countries.
	 *
	 * @param p_listOfCountries The list of countries from which to select a random
	 *                          country.
	 * @return A randomly selected country from the provided list.
	 */
	private Country getRandomCountry(List<Country> p_listOfCountries) {
		Random l_random = new Random();
		return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
	}

	/**
	 * Boosts the armies of enemy neighbors of the player's owned countries.
	 *
	 * @param p_player    The player executing the cheat.
	 * @param p_gameState The current state of the game.
	 */
	private void boostEnemyNeighborsCounties(Player p_player, GameState p_gameState) {
		List<Country> l_ownedCountries = p_player.getD_playerCountries();

		for (Country l_playerOwnedCountry : l_ownedCountries) {
			ArrayList<Integer> l_enemyCountries = getEnemies(p_player, l_playerOwnedCountry);

			if (l_enemyCountries.size() == 0)
				continue;

			Integer l_territoryArmies = l_playerOwnedCountry.getD_armyCount();

			if (l_territoryArmies != null) {
				l_playerOwnedCountry.setD_armyCount(l_territoryArmies * 2);
				String l_logMsg = String.format("Cheater Player: %s doubled the armies (Now: %d) in %s",
						p_player.getD_playerName(), l_territoryArmies * 2, l_playerOwnedCountry.getD_countryName());

				p_gameState.updateLog(l_logMsg, AppConstants.ORDER_EFFECT);
			}

		}
	}

	/**
	 * Overcomes nearby enemies by conquering their countries.
	 *
	 * @param p_player    The player executing the cheat.
	 * @param p_gameState The current state of the game.
	 */
	private void overcomeNearbyEnemies(Player p_player, GameState p_gameState) {
		List<Country> l_ownedCountries = p_player.getD_playerCountries();

		for (Country l_playerOwnedCountry : l_ownedCountries) {
			ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_playerOwnedCountry);

			for (Integer l_enemyId : l_countryEnemies) {
				Map l_loadedMap = p_gameState.getD_map();
				Player l_enemyCountryOwner = this.getCountryOwner(p_gameState, l_enemyId);
				Country l_enemyCountry = l_loadedMap.getCountryByID(l_enemyId);
				this.conquerTargetCountry(p_gameState, l_enemyCountryOwner, p_player, l_enemyCountry);

				String l_logMsg = String.format("Cheater Player: %s Now owns %s",
						p_player.getD_playerName(), l_enemyCountry.getD_countryName());

				p_gameState.updateLog(l_logMsg, AppConstants.ORDER_EFFECT);
			}

		}
	}

	/**
	 * Retrieves the owner of a country based on its ID.
	 *
	 * @param p_gameState The current state of the game.
	 * @param p_countryId The ID of the country to check.
	 * @return The owner player of the specified country.
	 */
	private Player getCountryOwner(GameState p_gameState, Integer p_countryId) {
		List<Player> l_players = p_gameState.getD_playerList();
		Player l_owner = null;

		for (Player l_player : l_players) {
			List<Integer> l_playerOwnedCountry = l_player.getCountryIDs();
			if (l_playerOwnedCountry.contains(p_countryId)) {
				l_owner = l_player;
				break;
			}
		}

		return l_owner;
	}

	/**
	 * Conquers the target country from the opponent and updates player continents.
	 *
	 * @param p_gameState            The current state of the game.
	 * @param p_opponentTargetPlayer The opponent player owning the target country.
	 * @param p_cheaterPlayer        The cheater player conquering the target
	 *                               country.
	 * @param p_targetCountry        The target country to conquer.
	 */
	private void conquerTargetCountry(GameState p_gameState, Player p_opponentTargetPlayer, Player p_cheaterPlayer,
			Country p_targetCountry) {
		if (p_opponentTargetPlayer != null) {
			p_opponentTargetPlayer.getD_playerCountries().remove(p_targetCountry);
			p_opponentTargetPlayer.getD_playerCountries().add(p_targetCountry);
			// Add Log Here
			this.updateContinents(p_cheaterPlayer, p_opponentTargetPlayer, p_gameState);
		}
	}

	/**
	 * Updates the continents for both cheater player and opponent player.
	 *
	 * @param p_cheaterPlayer        The cheater player.
	 * @param p_opponentTargetPlayer The opponent player.
	 * @param p_gameState            The current state of the game.
	 */
	private void updateContinents(Player p_cheaterPlayer, Player p_opponentTargetPlayer,
			GameState p_gameState) {
		List<Player> l_playesList = new ArrayList<>();
		p_cheaterPlayer.setD_playerContinents(new ArrayList<>());
		p_opponentTargetPlayer.setD_playerContinents(new ArrayList<>());
		l_playesList.add(p_cheaterPlayer);
		l_playesList.add(p_opponentTargetPlayer);

		PlayerService l_playerService = new PlayerService();
		l_playerService.allocRandomContinents(l_playesList, p_gameState.getD_map().getD_continents());
	}

	/**
	 * Gets a list of enemy neighbors for a given country owned by the player.
	 *
	 * @param p_player  The player owning the country.
	 * @param p_country The country for which to find enemy neighbors.
	 * @return A list of enemy neighbors' IDs.
	 */
	private ArrayList<Integer> getEnemies(Player p_player, Country p_country) {
		ArrayList<Integer> l_enemyNeighbors = new ArrayList<Integer>();

		for (Integer l_countryID : p_country.getD_adjacentCountryIds()) {
			if (!p_player.getCountryIDs().contains(l_countryID))
				l_enemyNeighbors.add(l_countryID);
		}
		return l_enemyNeighbors;
	}

	/**
	 * Generates deploy order (not used for CheaterPlayer, always returns null).
	 *
	 * @param p_player    The player for whom the deploy order is generated.
	 * @param p_gameState The current state of the game.
	 * @return Always returns null.
	 */
	@Override
	public String generateDeployOrder(Player p_player, GameState p_gameState) {
		return null;
	}

	/**
	 * Generates advance order (not used for CheaterPlayer, always returns null).
	 *
	 * @param p_player    The player for whom the advance order is generated.
	 * @param p_gameState The current state of the game.
	 * @return Always returns null.
	 */
	@Override
	public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
		return null;
	}

	/**
	 * Generates card order (not used for CheaterPlayer, always returns null).
	 *
	 * @param p_player    The player for whom the card order is generated.
	 * @param p_gameState The current state of the game.
	 * @param p_cardName  The name of the card.
	 * @return Always returns null.
	 */
	@Override
	public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
		return null;
	}

	/**
	 * Gets the player behavior as a String.
	 *
	 * @return The player behavior, which is "Cheater" for this class.
	 */
	@Override
	public String getPlayerBehavior() {
		return "Cheater";
	}
}
