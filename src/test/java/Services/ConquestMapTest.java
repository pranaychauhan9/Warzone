package Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import Constants.AppConstants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.GameState;
import Models.Map;

/**
 * Test class for parsing conquest map file.
 *
 */
public class ConquestMapTest {

    /**
     * Map Service object.
     */
    MapService d_mapservice;

    /**
     * File content.
     */
    List<String> d_fileContent;

    /**
     * Map object.
     */
    Map d_map;

    /**
     * GameState object.
     */
    GameState d_state;

    /**
     * Conquest file reader to parse the map file.
     */
    ConquestMap d_conquestMap;

    /**
     * Setup method
     * 
     * @throws InvalidMap Invalid map exception
     */
    @Before
    public void setup() throws InvalidMap {
        d_conquestMap = new ConquestMap();
        d_mapservice = new MapService();
        d_map = new Map();
        d_state = new GameState();
        d_fileContent = d_mapservice.loadFile("testconquest");
    }

    /**
     * This test case is used to test the functionality of reading conquest map.
     *
     *
     * @throws IOException throws IOException
     * @throws InvalidMap  Invalid map exception
     */
    @Test
    public void testReadMap() throws IOException, InvalidMap {
        d_conquestMap.readMapContent(d_fileContent, d_map, d_state);

        assertNotNull(d_state.getD_map());
        assertEquals(d_state.getD_map().getD_continents().size(), 8);
        assertEquals(d_state.getD_map().getD_countries().size(), 99);
    }

    /**
     * tests add continents.
     * 
     * @throws IOException    Exceptions
     * @throws InvalidMap     Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testAddContinent() throws IOException, InvalidMap, InvalidCommand {
        d_conquestMap.readMapContent(d_fileContent, d_map, d_state);
        Map l_updatedContinents = d_mapservice.addRemoveContinents(d_state, d_state.getD_map(), "Add", "Asia 10");
        l_updatedContinents = d_mapservice.addRemoveContinents(d_state, d_state.getD_map(), "Add", "Europe 20");

        assertEquals(l_updatedContinents.getD_continents().size(), 10);
        assertEquals(l_updatedContinents.getD_continents().get(8).getD_continentName(), "Asia");
        assertEquals(l_updatedContinents.getD_continents().get(8).getD_continentValue().toString(), "10");
    }

    /**
     * tests remove continents.
     * 
     * @throws IOException    Exceptions
     * @throws InvalidMap     Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testRemoveContinent() throws IOException, InvalidMap, InvalidCommand {
        d_conquestMap.readMapContent(d_fileContent, d_map, d_state);
        Map l_updatedContinents = d_mapservice.addRemoveContinents(d_state, d_state.getD_map(), "Add", "Asia 10");
        assertEquals(l_updatedContinents.getD_continents().size(), 9);
        l_updatedContinents = d_mapservice.addRemoveContinents(d_state, d_state.getD_map(), "Remove", "Asia");
        assertEquals(l_updatedContinents.getD_continents().size(), 8);
    }

    /**
     * tests add country
     * 
     * @throws IOException    Exceptions
     * @throws InvalidMap     Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testAddCountry() throws IOException, InvalidMap, InvalidCommand {
        d_conquestMap.readMapContent(d_fileContent, d_map, d_state);
        d_mapservice.addRemoveContinents(d_state, d_state.getD_map(), "Add", "Europe 20");

        d_mapservice.editFunctions(d_state, "add", "Swiss Europe", AppConstants.COUNTRY);
        d_mapservice.editFunctions(d_state, "add", "Norway Europe", AppConstants.COUNTRY);
        assertEquals(d_state.getD_map().getD_countries().size(), 101);

    }

    /**
     * tests remove country.
     * 
     * @throws IOException    Exceptions
     * @throws InvalidMap     Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testRemoveCountry() throws IOException, InvalidMap, InvalidCommand {
        d_conquestMap.readMapContent(d_fileContent, d_map, d_state);
        d_mapservice.addRemoveContinents(d_state, d_state.getD_map(), "Add", "Europe 20");

        d_mapservice.editFunctions(d_state, "add", "Swiss Europe", AppConstants.COUNTRY);
        d_mapservice.editFunctions(d_state, "remove", "Swiss", AppConstants.COUNTRY);
        assertEquals(d_state.getD_map().getD_countries().size(), 99);
    }
}
