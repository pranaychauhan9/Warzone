package Models;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;
import Services.MapService;

/**
 * This class is used to test functionality of Map class.
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
public class MapTest {

    /**
     * Stores map information.
     */
    Map d_mapInfo;

    /**
     * Manages map-related operations.
     */
    MapService d_mapService;

    /**
     * Represents the game state.
     */
    GameState d_gameState;

    /**
     * Set up the initial state for testing.
     */
    @Before
    public void setup() {
        d_mapInfo = new Map();
        d_mapService = new MapService();
        d_gameState = new GameState();
    }

    /**
     * This method tests the validation of a correct map using the
     * {@code assertEquals}
     * method
     *
     * @throws InvalidMap If the map validation fails, this exception is expected to
     *                    be thrown.
     */
    @Test
    public void testCorrectMapValidate() throws InvalidMap {
        // test positive scenario
        d_mapInfo = d_mapService.loadMap(d_gameState, "canada");
        assertEquals(true, d_mapInfo.Validate());
    }

    /**
     * This method tests the validation of a map using the {@code assertEquals}
     * method
     * and expects it to throw an {@code InvalidMap} exception.
     *
     * @throws InvalidMap If the map validation fails, this exception is expected to
     *                    be thrown.
     */
    @Test(expected = InvalidMap.class)
    public void testReadInvalidMap() throws InvalidMap {
        // test negative scenario
        d_mapInfo = d_mapService.loadMap(d_gameState, "invalidMapTest");
        assertEquals(true, d_mapInfo.Validate());
    }

    /**
     * Checks wether countries in a continent are connected or not.
     *
     * @throws InvalidMapException if the map is invalid
     */
    @Test(expected = InvalidMap.class)
    public void testContinentConnectivity() throws InvalidMap {
        d_mapInfo = d_mapService.loadMap(d_gameState, "test-continent-subgraph");
        d_mapInfo.Validate();
    }

    /**
     * Tests the connectivity of countries in the whole map.
     *
     * @throws InvalidMapException if there are disconnected countries.
     */
    @Test(expected = InvalidMap.class)
    public void testCountryConnectivity() throws InvalidMap {
        d_mapInfo = d_mapService.loadMap(d_gameState, "test-wholemap");
        d_mapInfo.checkCountryConnectivity();
    }

}
