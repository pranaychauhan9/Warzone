package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;

/**
 * This class is used to test functionality of Airlift command.
 */
public class AirliftTest {

    /**
     * Player.
     */
    Player d_player;

    /**
     * Game State.
     */
    GameState d_gameState;

    /**
     * Setup before each test case.
     * 
     * @throws InvalidMap Invalid Map
     */
    @Before
    public void setup() throws InvalidMap {
        d_gameState = new GameState();
        d_player = new Player();
        d_player.setD_playerName("Sumit");

        List<Country> l_countries = new ArrayList<Country>();
        Country l_country = new Country(0, "Country0", 1);
        Country l_distantCountry = new Country(1, "Country1", 1);
        l_country.setD_armyCount(4);
        l_distantCountry.setD_armyCount(8);

        l_countries.add(l_country);
        l_countries.add(l_distantCountry);

        Map l_map = new Map();
        l_map.setD_countries(l_countries);
        d_gameState.setD_map(l_map);
        d_player.setD_playerCountries(l_countries);
    }

    /**
     * Test positive Airlift command execution.
     */
    @Test
    public void testAirliftExecution() {
        Airlift l_airliftCommand = new Airlift("Country0", "Country1", 3, d_player);
        l_airliftCommand.execute(d_gameState);
        Country l_country1 = d_gameState.getD_map().getCountryByName("Country1");
        assertEquals("11", l_country1.getD_armyCount().toString());
    }

    /**
     * Test negative validation of airlift order.
     */
    @Test
    public void testNegativeAirLiftCommand() {
        Airlift l_airliftCommand = new Airlift("Country0", "Country4", 1, d_player);
        assertFalse(l_airliftCommand.checkOrderValidity(d_gameState));
    }
}
