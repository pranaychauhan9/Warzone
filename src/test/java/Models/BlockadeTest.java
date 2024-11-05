package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Controllers.GameEngineCtx;

/**
 * This class is used to test functionality of Advance command execution.
 */
public class BlockadeTest {

    /**
     * Holds current game state info
     */
    GameState d_gameState = new GameState();

    // Member Variables

    /** The source player */
    Player d_sourcePlayer;

    /** The target player */
    Player d_targetPlayer;

    /** Country 1 for testing */
    Country d_country1;

    /** Country 2 for testing */
    Country d_country2;

    /** List of countries for player one */
    List<Country> d_playerOneCountries;

    /** List of countries for player two */
    List<Country> d_playerTwoCountries;

    /** List of countries in the map */
    List<Country> d_mapCountries;

    /** The map for testing */
    Map d_map;

    /**
     * Setup before each test case.
     */
    @Before
    public void setup() {
        d_sourcePlayer = new Player("Sumit");
        d_targetPlayer = new Player("Darshan");

        d_country1 = new Country("Country1");
        d_country2 = new Country("Country2");

        d_country1.setD_armyCount(5);
        d_playerOneCountries = new ArrayList<>();
        d_playerOneCountries.add(d_country1);
        d_sourcePlayer.setD_playerCountries(d_playerOneCountries);

        d_country2.setD_armyCount(4);
        d_playerTwoCountries = new ArrayList<>();
        d_playerTwoCountries.add(d_country2);
        d_targetPlayer.setD_playerCountries(d_playerTwoCountries);

        // update a map with countries
        d_mapCountries = new ArrayList<>();
        d_mapCountries.add(d_country1);
        d_mapCountries.add(d_country2);
        d_map = new Map();
        d_map.setD_countries(d_mapCountries);
        d_gameState.setD_map(d_map);
        d_gameState.setD_players(Arrays.asList(d_sourcePlayer, d_targetPlayer));
    }

    /**
     * Test Blockade command execution.
     */
    @Test
    public void testBlockadeExecution() {
        Blockade l_blockadeCommand = new Blockade(d_sourcePlayer, "Country1");
        l_blockadeCommand.execute(d_gameState);
        assertEquals("15", d_gameState.getD_map().getCountryByName("Country1").getD_armyCount().toString());
    }

    /**
     * Test invalid Bloackade command.
     */
    @Test
    public void testinvalidBlockade() {
        // Country2 does not belong to d_sourcePlayer
        Blockade l_blockadeCommand = new Blockade(d_sourcePlayer, "Country2");
        assertFalse(l_blockadeCommand.isValid(d_gameState));
    }

}
