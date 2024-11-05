package Models;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the working of Aggressive Player Behavior.
 */
public class AggressivePlayerTest {

	/**
	 * Player.
	 */
	Player d_player;

	/**
	 * Aggressive player instance.
	 */
	AggressivePlayer d_aggressivePlayer = new AggressivePlayer();

	/**
	 * Game State.
	 */
	GameState d_gameState = new GameState();

	/** Country 1 for testing */
	Country d_country1;

	/** Country 2 for testing */
	Country d_country2;

	/** Country 3 for testing */
	Country d_country3;

	/**
	 * Setup For testing Aggressive Behavior Strategy.
	 */
	@Before
	public void setup() {
		d_country1 = new Country(1, "country1", 1);
		d_country2 = new Country(2, "country2", 1);
		d_country3 = new Country(3, "country3", 1);

		d_country1.addNeighbour(3);
		d_country1.addNeighbour(2);

		d_country1.setD_armyCount(10);
		d_country2.setD_armyCount(3);
		d_country3.setD_armyCount(2);

		ArrayList<Country> l_list = new ArrayList<Country>();
		l_list.add(d_country1);
		l_list.add(d_country2);
		l_list.add(d_country3);

		d_player = new Player("Sumit");
		d_player.setD_playerCountries(l_list);
		d_player.setStrategy(new AggressivePlayer());
		d_player.setD_unallocatedArmyCount(8);

		List<Player> l_listOfPlayer = new ArrayList<Player>();
		l_listOfPlayer.add(d_player);

		Map l_map = new Map();
		l_map.setD_countries(l_list);
		l_map.setD_countries(l_list);
		d_gameState.setD_map(l_map);
		d_gameState.setD_players(l_listOfPlayer);

	}

	/**
	 * Checks whether Order creation is deploy initially.
	 *
	 * @throws IOException Exception
	 */
	@Test
	public void testInitialOrder() throws IOException {
		assertEquals("deploy", d_player.getPlayerOrder(d_gameState).split(" ")[0]);
	}

	/**
	 * Check whether aggressive player deploys armies on strongest country.
	 */
	@Test
	public void testDeployOnStrongestCountry() {
		assertEquals("country1", d_aggressivePlayer.getStrongestCountry(d_player, d_gameState).getD_countryName());
	}

}
