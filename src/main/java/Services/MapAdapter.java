package Services;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import Models.GameState;
import Models.Map;

/**
 * Map Adapter implementation to handle different formats of map.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class MapAdapter extends DominationMap {

    /**
     * ConquestMap Object.
     */
    private ConquestMap d_conquestMap;

    /**
     * Adapter constructor for setting conquest map .
     * 
     * @param p_conquestMap conquest map
     */
    public MapAdapter(ConquestMap p_conquestMap) {
        this.d_conquestMap = p_conquestMap;
    }

    /**
     * Adapter for reading different type of map file through adaptee.
     * 
     * @param p_fileContent lines of loaded file
     * @param p_map         map to be set
     * @param p_gameState   current state of the game
     */
    public void readMapContent(List<String> p_fileContent, Map p_map, GameState p_gameState) {
        d_conquestMap.readMapContent(p_fileContent, p_map, p_gameState);
    }

    /**
     * Adapter for writing to different type of map file through adaptee.
     * 
     * @param p_gameState current state of the game
     * @param p_writer    file writer
     * @param p_mapFormat format in which map file has to be saved
     * @throws IOException Io exception
     */
    public void writeChangesToMap(GameState p_gameState, FileWriter p_writer, String p_mapFormat) throws IOException {
        d_conquestMap.writeChangesToMap(p_gameState, p_writer, p_mapFormat);
    }
}
