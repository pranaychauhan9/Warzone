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
public class DiplomacyTest {

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

    /** Country 3 for testing */
    Country d_country3;

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
        d_country3 = new Country("Country3");

        d_country1.setD_armyCount(8);
        d_country3.setD_armyCount(5);
        d_playerOneCountries = new ArrayList<>();
        d_playerOneCountries.add(d_country1);
        d_playerOneCountries.add(d_country3);
        d_sourcePlayer.setD_playerCountries(d_playerOneCountries);

        d_country2.setD_armyCount(3);
        d_playerTwoCountries = new ArrayList<>();
        d_playerTwoCountries.add(d_country2);
        d_targetPlayer.setD_playerCountries(d_playerTwoCountries);

        // update a map with countries
        d_mapCountries = new ArrayList<>();
        d_mapCountries.add(d_country1);
        d_mapCountries.add(d_country2);
        d_mapCountries.add(d_country3);
        d_map = new Map();
        d_map.setD_countries(d_mapCountries);
        d_gameState.setD_map(d_map);
        d_gameState.setD_players(Arrays.asList(d_sourcePlayer, d_targetPlayer));
    }

    /**
     * Tests to check functionality of diplomacy.
     */
    @Test
    public void testDiplomacyExecution() {
        Diplomacy l_diplomacyOrder = new Diplomacy(d_targetPlayer.getD_playerName(), d_sourcePlayer);
        l_diplomacyOrder.execute(d_gameState);
        assertEquals(d_targetPlayer, d_sourcePlayer.d_negotiatedWith.get(0));
    }

    /**
     * Tests whether advance attack orders work after negotiation.
     */
    @Test
    public void testAdvancePostNegotiation() {
        Diplomacy l_diplomacyOrder = new Diplomacy(d_targetPlayer.getD_playerName(), d_sourcePlayer);
        Advance l_advance = new Advance(d_targetPlayer, "Country2", "Country1", 2);
        l_diplomacyOrder.execute(d_gameState);
        l_advance.execute(d_gameState);
        assertEquals(d_gameState.getLog().trim(),
                "Log: Advance Command cannot be executed as Player:Darshan negotiated terms with Player:Sumit");
    }

    /**
     * Tests whether bomb attack orders work after negotiation.
     */
    @Test
    public void testBombPostNegotiation() {
        Diplomacy l_diplomacyOrder = new Diplomacy(d_targetPlayer.getD_playerName(), d_sourcePlayer);
        Bomb l_bombOrder = new Bomb(d_targetPlayer, "Country1");
        l_diplomacyOrder.execute(d_gameState);
        l_bombOrder.execute(d_gameState);
        assertEquals(d_gameState.getLog().trim(),
                "Log: Bomb Command cannot be executed as Player:Darshan negotiated terms with Player:Sumit");
    }
}
