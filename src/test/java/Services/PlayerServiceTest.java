package Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Models.Player;

import Exceptions.InvalidCommand;

/**
 * This class contains test cases for the PlayerService class.
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
public class PlayerServiceTest {

    /**
     * Represents a player.
     */
    Player d_player;

    /**
     * Manages player-related operations.
     */
    PlayerService d_playerService;

    /**
     * Stores map information.
     */
    Map d_map;

    /**
     * Represents the game state.
     */
    GameState d_gameState;

    /**
     * Manages map-related operations.
     */
    MapService d_mapService;

    /**
     * Stores a list of players.
     */
    List<Player> d_playerList = new ArrayList<>();

    /**
     * Set up the initial state for testing.
     */
    @Before
    public void setup() {
        d_mapService = new MapService();
        d_map = new Map();
        d_player = new Player();
        d_playerService = new PlayerService();
        d_gameState = new GameState();
    }

    /**
     * Test method for adding new players to the game state.
     */
    @Test
    public void testAddNewPlayer() {
        // Set up the game state and add two players
        d_gameState.setD_map(new Map());
        d_playerService.updatePlayers(d_gameState, "add", "Sumit");
        d_playerService.updatePlayers(d_gameState, "add", "Darshan");

        // Verify that the player names were added correctly
        assertEquals("Sumit", d_gameState.getD_playerList().get(0).getD_playerName());
        assertEquals("Darshan", d_gameState.getD_playerList().get(1).getD_playerName());
    }

    /**
     * Test method for removing an existing player from the game state.
     */
    @Test
    public void testRemoveExistingPlayer() {
        // Set up the game state, add a player, and then remove the same player
        d_gameState.setD_map(new Map());
        d_playerService.updatePlayers(d_gameState, "add", "Sumit");
        d_playerService.updatePlayers(d_gameState, "remove", "Sumit");

        // Verify that the player was removed correctly, resulting in an empty player
        // list
        assertEquals(0, d_gameState.getD_playerList().size());
    }

    /**
     * Test method for verifying the allocation of countries to players.
     * 
     */
    @Test
    public void testCountryAllocation() {
        d_map = d_mapService.loadMap(d_gameState, "test-wholemap");
        d_gameState.setD_map(d_map);
        d_playerService.updatePlayers(d_gameState, "add", "Sumit");
        d_playerService.updatePlayers(d_gameState, "add", "Darshan");

        d_playerService.allocCountriesAndContinents(d_gameState);

        int l_playerCountries = 0;
        for (Player l_player : d_gameState.getD_playerList()) {
            l_playerCountries += l_player.getCountryList().size();
        }
        assertEquals(l_playerCountries, d_gameState.getD_map().getD_countries().size());
    }

    /**
     * Test case to verify the correct calculation of reinforcement armies for a
     * player.
     */
    @Test
    public void testCalculateReinforcementArmies() {
        // Given
        Player l_player = new Player();

        // Assuming the player owns 10 territories (for example)
        List<Country> l_ownedCountries = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            l_ownedCountries.add(new Country("Country" + i));
        }
        l_player.setD_playerCountries(l_ownedCountries);

        // Assuming the player owns two continents with values 5 and 3
        List<Continent> l_ownedContinents = new ArrayList<Continent>();
        l_ownedContinents.add(new Continent(1, "ContinentA", 5));
        l_ownedContinents.add(new Continent(2, "ContinentB", 3));

        l_player.setD_playerContinents(l_ownedContinents);

        // When
        int l_actualArmies = d_playerService.findArmyCount(l_player);

        // Then
        // For owning 10 territories, the base armies should be max(3, 10/3) = 4
        // Bonus for owning ContinentA (5) + Bonus for owning ContinentB (3)
        int l_expectedArmies = 4 + 5 + 3;

        assertEquals(l_expectedArmies, l_actualArmies);
    }

}
