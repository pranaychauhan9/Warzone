package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * This class is used to test functionality of OrderImpl class.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 * 
 */
public class DeployTest {

    /**
     * Player
     */
    Player d_player;

    /**
     * Deploy Order given to the player.
     */
    Deploy d_deployOrder;

    /**
     * Game State.
     */
    GameState d_gameState = new GameState();

    /**
     * Set up the initial state for testing.
     */
    @Before
    public void setup() {
        d_player = new Player();
        d_player.setD_playerName("Sumit");
        List<Country> l_countries = new ArrayList<Country>();
        l_countries.add(new Country("Ukraine"));
        l_countries.add(new Country("Russia"));
        l_countries.add(new Country("India"));
        d_player.setD_playerCountries(l_countries);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, "Ukraine", 1);
        Country l_country2 = new Country(2, "Russia", 2);
        Country l_country3 = new Country(2, "India", 3);
        l_country2.setD_armyCount(7);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);
        l_mapCountries.add(l_country3);

        Map l_map = new Map();
        l_map.setD_countries(l_mapCountries);
        d_gameState.setD_map(l_map);

        d_deployOrder = new Deploy(d_player, "Russia", 5);
    }

    /**
     * Test to check if a player owns a target country where it's deploying order.
     */
    @Test
    public void testCheckCountryOwnership() {
        boolean l_actualValue = d_deployOrder.isValid(d_gameState);
        assertTrue(l_actualValue);
    }

    /**
     * Test the executeOrder method of OrderImpl with a deploy order.
     * It deploys armies to a specified country and checks if the army count is
     * updated correctly.
     */
    @Test
    public void testExecuteOrder() {
        d_deployOrder.execute(d_gameState);
        Country l_russia = d_gameState.getD_map().getCountryByName("Russia");
        assertEquals("12", l_russia.getD_armyCount().toString());
    }
}
