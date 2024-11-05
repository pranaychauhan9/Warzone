package Models;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the working of Benevolent Player Behavior.
 */
public class BenevolentPlayerTest {

	/**
	 * Player.
	 */
	Player d_player;

	/**
	 * Aggressive player instance.
	 */
	BenevolentPlayer d_benevolentPlayer = new BenevolentPlayer();

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
		d_player.setStrategy(new BenevolentPlayer());
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
	 * Check whether benevolent player deploy armies on weakest country.
	 */
	@Test
	public void testDeploy() {
		assertEquals("country3", d_benevolentPlayer.getWeakestCountry(d_player).getD_countryName());
	}

	/**
	 * Check whether benevolent player attacks to weakest neighbor.
	 */
	@Test
	public void testAttack() {
		assertEquals("country3", d_benevolentPlayer.getWeakestNeighbor(d_country1, d_gameState).getD_countryName());
	}

}
