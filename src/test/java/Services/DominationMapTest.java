package Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;
import Models.GameState;
import Models.Map;

/**
 * Test class for parsing map file to map of game state.
 *
 */
public class DominationMapTest {

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
     * Domination Map instance to read or write map.
     */
    DominationMap d_dominationMap;

    /**
     * Setup method
     * 
     * @throws InvalidMap Invalid map exception
     */
    @Before
    public void setup() throws InvalidMap {
        d_dominationMap = new DominationMap();
        d_mapservice = new MapService();
        d_map = new Map();
        d_state = new GameState();
        d_fileContent = d_mapservice.loadFile("canada");
    }

    /**
     * This test case is used to read domination map.
     *
     *
     * @throws IOException throws IOException
     * @throws InvalidMap  Invalid map exception
     */
    @Test
    public void testReadMapFile() throws IOException, InvalidMap {
        d_dominationMap.readMapContent(d_fileContent, d_map, d_state);

        assertNotNull(d_state.getD_map());
        assertEquals(d_state.getD_map().getD_continents().size(), 6);
        assertEquals(d_state.getD_map().getD_countries().size(), 31);
    }

}