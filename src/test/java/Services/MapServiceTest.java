package Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import CommonFunctions.CommonCode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;

/**
 * This class contains test cases for the MapService class, which is responsible
 * for managing game maps and related operations.
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
public class MapServiceTest {

    /**
     * This field represents an instance of the MapService class, which is
     * responsible
     * for loading and managing maps in the game.
     */
    private MapService d_mapService;

    /**
     * This field represents an instance of the Map class, which holds information
     * about
     * the current game map.
     */
    private Map d_map;

    /**
     * This field represents an instance of the GameState class, which manages the
     * state
     * of the game.
     */
    private GameState d_gameState;

    /**
     * This method is called before each test case to set up the initial state.
     */
    @Before
    public void setup() {
        d_mapService = new MapService();
        d_map = new Map();
        d_gameState = new GameState();
        d_map = d_mapService.loadMap(d_gameState, "canada");
    }

    /**
     * Test case for editing a map.
     *
     * @throws IOException If an I/O error occurs during map editing.
     */
    @Test
    public void testEditMap() throws IOException {
        d_mapService.editMap(d_gameState, "test-wholemap");
        File l_file = new File(CommonCode.getMapFilePath("test-wholemap"));

        assertTrue(l_file.exists());
    }

    /**
     * Test case for adding a continent to the map.
     *
     * @throws IOException If an I/O error occurs during continent addition.
     * @throws InvalidMap  If the map is invalid.
     */
    @Test
    public void testAddContinent() throws IOException, InvalidMap, InvalidCommand {
        d_mapService.loadMap(d_gameState, "test-map");
        d_mapService.editFunctions(d_gameState, "add", "Continent1 2", "continent");

        assertEquals(1, d_gameState.getD_map().getD_continents().size());
        assertEquals("Continent1", d_gameState.getD_map().getD_continents().get(0).getD_continentName());
    }

    /**
     * Test case for removing a continent from the map.
     *
     * @throws IOException If an I/O error occurs during continent removal.
     * @throws InvalidMap  If the map is invalid.
     */
    @Test
    public void testRemoveContinent() throws IOException, InvalidMap, InvalidCommand {
        List<Continent> l_continents = new ArrayList<>();
        Continent l_continent1 = new Continent();

        l_continent1.setD_continentID(1);
        l_continent1.setD_continentName("Continent1");
        l_continent1.setD_continentValue(10);

        l_continents.add(l_continent1);

        Map l_map = new Map();
        l_map.setD_continents(l_continents);
        d_gameState.setD_map(l_map);
        d_mapService.editFunctions(d_gameState, "remove", "Continent1", "continent");
        assertEquals(0, l_map.getD_continents().size());
    }

    /**
     * Test case for adding a country to the map.
     *
     * @throws IOException If an I/O error occurs during country addition.
     * @throws InvalidMap  If the map is invalid.
     */
    @Test
    public void testAddCountry() throws IOException, InvalidMap, InvalidCommand {
        d_mapService.loadMap(d_gameState, "test-wholemap");
        int l_initialCount = d_gameState.getD_map().getD_countries().size();

        d_mapService.editFunctions(d_gameState, "add", "Country7 ContinentA", "country");
        l_initialCount++;

        d_mapService.editFunctions(d_gameState, "add", "Country8 ContinentA", "country");
        l_initialCount++;

        assertEquals(l_initialCount, d_gameState.getD_map().getD_countries().size());
        assertEquals("Country7", d_gameState.getD_map().getCountryByName("Country7").getD_countryName());
        assertEquals("Country8", d_gameState.getD_map().getCountryByName("Country8").getD_countryName());
    }

    /**
     * Test case for removing a country from the map.
     *
     * @throws IOException If an I/O error occurs during country addition.
     * @throws InvalidMap  If the map is invalid.
     */
    @Test
    public void testRemoveCountry() throws IOException, InvalidMap, InvalidCommand {
        d_mapService.loadMap(d_gameState, "test-wholemap");
        int l_initialCount = d_gameState.getD_map().getD_countries().size();

        d_mapService.editFunctions(d_gameState, "remove", "Country4", "country");
        l_initialCount--;

        d_mapService.editFunctions(d_gameState, "remove", "Country5", "country");
        l_initialCount--;

        assertEquals(l_initialCount, d_gameState.getD_map().getD_countries().size());
    }

    /**
     * Test case for adding neighbour country to another country.
     *
     * @throws IOException
     * @throws InvalidMap
     */
    @Test
    public void testAddNeighbour() throws InvalidMap, IOException, InvalidCommand {
        d_mapService.loadMap(d_gameState, "test-wholemap");

        d_mapService.editFunctions(d_gameState, "add", "testContinent 5", "continent");

        d_mapService.editFunctions(d_gameState, "add", "testCountry1 testContinent", "country");
        d_mapService.editFunctions(d_gameState, "add", "testCountry2 testContinent", "country");
        d_mapService.editFunctions(d_gameState, "add", "testCountry3 testContinent", "country");
        d_mapService.editFunctions(d_gameState, "add", "testCountry4 testContinent", "country");

        d_mapService.editFunctions(d_gameState, "add", "testCountry1 testCountry2", "neighbour");
        d_mapService.editFunctions(d_gameState, "add", "testCountry3 testCountry4", "neighbour");

        assertEquals(d_gameState.getD_map().getCountryByName("testCountry2").getD_countryId(),
                d_gameState.getD_map().getCountryByName("testCountry1").getD_adjacentCountryIds().get(0));
        assertEquals(d_gameState.getD_map().getCountryByName("testCountry4").getD_countryId(),
                d_gameState.getD_map().getCountryByName("testCountry3").getD_adjacentCountryIds().get(0));
    }

    /**
     * Test case for removing neighbour country.
     *
     * @throws IOException
     * @throws InvalidMap
     */
    @Test
    public void testRemoveNeighbour() throws InvalidMap, IOException, InvalidCommand {
        d_mapService.loadMap(d_gameState, "test-wholemap");

        d_mapService.editFunctions(d_gameState, "add", "testContinentD 5", "continent");

        d_mapService.editFunctions(d_gameState, "add", "testCountry11 testContinentD", "country");
        d_mapService.editFunctions(d_gameState, "add", "testCountry12 testContinentD", "country");

        d_mapService.editFunctions(d_gameState, "add", "testCountry11 testCountry12", "neighbour");

        assertEquals(d_gameState.getD_map().getCountryByName("testCountry12").getD_countryId(),
                d_gameState.getD_map().getCountryByName("testCountry11").getD_adjacentCountryIds().get(0));

        d_mapService.editFunctions(d_gameState, "remove", "testCountry11 testCountry12", "neighbour");
        assertEquals(0, d_gameState.getD_map().getCountryByName("testCountry11").getD_adjacentCountryIds().size());

    }
}
