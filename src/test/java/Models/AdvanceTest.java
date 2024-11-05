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
public class AdvanceTest {

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
     * Test to validate whether advance command is valid or not.
     */
    @Test
    public void testValidateAdvanceOrder() {
        // test to check advance fails when armies exceed their army count.
        assertFalse(new Advance(d_sourcePlayer, "Country1", "Country2", 10).isValid(d_gameState));

        // test to check advance fails when all armies are ordered to advance to target
        // country.
        assertFalse(new Advance(d_sourcePlayer, "Country2", "Country4", 3).isValid(d_gameState));

        // test to check advance fails when source country does not belong to player.
        assertFalse(new Advance(d_sourcePlayer, "Country2", "Country5", 5).isValid(d_gameState));

        // test for successfull advance command
        assertTrue(new Advance(d_sourcePlayer, "Country1", "Country2", 4).isValid(d_gameState));
    }

    /**
     * Verifies whether the Attacker has emerged victorious in the battle and
     * ensures the proper update of countries and armies.
     */
    @Test
    public void testAttackersWin() {

        Advance l_advance = new Advance(d_sourcePlayer, "Country1", "Country2", 5);
        l_advance.handleSurvivingArmies(5, 0, d_country1, d_country2, d_targetPlayer);

        // the target player has lost all the countries
        assertEquals(d_targetPlayer.getD_playerCountries().size(), 0);

        // the player which initiated the command has conqueres the country of target
        // player
        assertEquals(d_sourcePlayer.getD_playerCountries().size(), 3);

        // As a result, the army count gets updated
        assertEquals(d_sourcePlayer.getD_playerCountries().get(1).getD_armyCount().toString(), "5");

        OrderExecutionPhase l_orderExec = new OrderExecutionPhase(new GameEngineCtx(), d_gameState);
        assertTrue(l_orderExec.checkGameHasEnded(d_gameState));
    }

    /**
     * Verifies whether the player has conquered the target country and updates
     * armies accordingly.
     */
    @Test
    public void testCountryConquering() {
        Advance l_advance = new Advance(d_sourcePlayer, "Country1", "Country2", 5);
        l_advance.handleSurvivingArmies(5, 0, d_country1, d_country2, d_targetPlayer);

        // the target player has lost all the countries
        assertEquals(0, d_targetPlayer.getD_playerCountries().size());

        // the player which initiated the command has conquered the country of the
        // target player
        assertEquals(3, d_sourcePlayer.getD_playerCountries().size());
    }

    /**
     * Verifies whether the army count is updated correctly in the conquered
     * country.
     */
    @Test
    public void testArmyCountUpdate() {
        Advance l_advance = new Advance(d_sourcePlayer, "Country1", "Country2", 5);
        l_advance.handleSurvivingArmies(5, 0, d_country1, d_country2, d_targetPlayer);

        // Check the army count update in the conquered country
        assertEquals("5", d_sourcePlayer.getD_playerCountries().get(1).getD_armyCount().toString());
    }

    /**
     * Verifies the game end condition after an advance command.
     */
    @Test
    public void testGameEndCondition() {
        Advance l_advance = new Advance(d_sourcePlayer, "Country1", "Country2", 7);
        l_advance.handleSurvivingArmies(5, 0, d_country1, d_country2, d_targetPlayer);

        OrderExecutionPhase l_orderExec = new OrderExecutionPhase(new GameEngineCtx(), d_gameState);
        assertTrue(l_orderExec.checkGameHasEnded(d_gameState));
    }

    /**
     * Checks if Defender has won battle, countries and armies are updated correctly
     * or not.
     */
    @Test
    public void testDefendersWin() {
        Advance l_advance = new Advance(d_sourcePlayer, "Country1", "Country2", 2);
        l_advance.handleSurvivingArmies(1, 4, d_country1, d_country2, d_targetPlayer);

        assertEquals(d_targetPlayer.getD_playerCountries().size(), 1);
        assertEquals(d_sourcePlayer.getD_playerCountries().size(), 2);
        assertEquals(d_sourcePlayer.getD_playerCountries().get(0).getD_armyCount().toString(), "9");
        assertEquals(d_targetPlayer.getD_playerCountries().get(0).getD_armyCount().toString(), "4");
    }

    /**
     * Checks if armies are deployed to target or not.
     */
    @Test
    public void testDeployToTarget() {
        Player d_sourcePlayer = new Player("Sumit");
        List<Country> l_countries = new ArrayList<>();

        Country d_country1 = new Country("Country1");
        Country d_country2 = new Country("Country2");
        d_country1.setD_armyCount(5);
        l_countries.add(d_country1);
        d_country2.setD_armyCount(3);
        l_countries.add(d_country2);
        d_sourcePlayer.setD_playerCountries(l_countries);

        Advance l_advance = new Advance(d_sourcePlayer, "Country1", "Country2", 2);
        l_advance.deployArmiesToTarget(d_country2);
        assertEquals(d_country2.getD_armyCount().toString(), "5");
    }
}
