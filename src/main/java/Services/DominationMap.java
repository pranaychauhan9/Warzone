package Services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import Constants.AppConstants;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;

/**
 * This class is responsible to read map in domination format.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class DominationMap implements Serializable {

    /**
     * Processes the map content extracted from the file and updates the map and
     * game state.
     *
     * @param p_fileContent List of lines from the file
     * @param p_map         Map object to update with processed data
     * @param p_gameState   GameState to update with the processed map
     */
    public void readMapContent(List<String> p_fileContent, Map p_map, GameState p_gameState) {
        List<String> l_continentData = retrieveMetaData(p_fileContent, AppConstants.CONTINENT);
        List<String> l_countryData = retrieveMetaData(p_fileContent, AppConstants.COUNTRY);
        List<String> l_bordersMetaData = retrieveMetaData(p_fileContent, AppConstants.BORDER);

        List<Continent> l_continentObjects = analyzeContinentsMetaData(l_continentData);
        List<Country> l_countryObjects = analyzeCountriesMetaData(l_countryData);
        l_countryObjects = analyzeBorderMetaData(l_countryObjects, l_bordersMetaData);
        l_continentObjects = linkCountryContinents(l_countryObjects, l_continentObjects);

        p_map.setD_continents(l_continentObjects);
        p_map.setD_countries(l_countryObjects);
        p_gameState.setD_map(p_map);
    }

    /**
     * Provides the essential lines obtained from the map file.
     *
     * @param p_fileLines       All Lines in the map document
     * @param p_switchParameter Type of lines needed : country, continent, borders
     * @return List required set of lines
     */
    public List<String> retrieveMetaData(List<String> p_fileLines, String p_switchParameter) {
        switch (p_switchParameter) {
            case AppConstants.CONTINENT:
                List<String> l_continentLines = p_fileLines.subList(
                        p_fileLines.indexOf(AppConstants.CONTINENTS) + 1,
                        p_fileLines.indexOf(AppConstants.COUNTRIES) - 1);
                return l_continentLines;
            case AppConstants.COUNTRY:
                List<String> l_countryLines = p_fileLines.subList(p_fileLines.indexOf(AppConstants.COUNTRIES) + 1,
                        p_fileLines.indexOf(AppConstants.BORDERS) - 1);
                return l_countryLines;
            case AppConstants.BORDER:
                List<String> l_bordersLines = p_fileLines.subList(p_fileLines.indexOf(AppConstants.BORDERS) + 1,
                        p_fileLines.size());
                return l_bordersLines;
            default:
                return null;
        }
    }

    /**
     * In the 'analyzeContinentsMetaData' method, the extracted continent data from
     * the map file is parsed.
     * 
     * 
     * @param p_continentList includes continent data in list from map file
     * @return List of processed continent meta data
     */
    public List<Continent> analyzeContinentsMetaData(List<String> p_continentList) {
        int l_continentId = 1;
        List<Continent> l_continents = new ArrayList<Continent>();

        for (String cont : p_continentList) {
            String[] l_metaData = cont.split(" ");
            l_continents.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
            l_continentId++;
        }
        return l_continents;
    }

    /**
     * In the 'analyzeCountriesMetaData' method, the extracted country and border
     * data from the map file are analyzed.
     * 
     * @param p_countriesList includes country data in list from map file.
     * @return List of processed country meta data.
     */
    public List<Country> analyzeCountriesMetaData(List<String> p_countriesList) {
        List<Country> l_countriesList = new ArrayList<Country>();
        for (String country : p_countriesList) {
            String[] l_metaDataCountries = country.split(" ");
            l_countriesList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1],
                    Integer.parseInt(l_metaDataCountries[2])));

        }

        return l_countriesList;
    }

    /**
     * Establishes connections between the Country Objects and their corresponding
     * neighbors.
     *
     * @param p_countriesList Total Country Objects Initialized
     * @param p_bordersList   Border Data Lines
     * @return List Updated Country Objects
     */
    public List<Country> analyzeBorderMetaData(List<Country> p_countriesList, List<String> p_bordersList) {
        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

        for (String l_border : p_bordersList) {
            if (null != l_border && !l_border.isEmpty()) {
                ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
                String[] l_splitString = l_border.split(" ");
                for (int i = 1; i <= l_splitString.length - 1; i++) {
                    l_neighbours.add(Integer.parseInt(l_splitString[i]));

                }
                l_countryNeighbors.put(Integer.parseInt(l_splitString[0]), l_neighbours);
            }
        }
        for (Country c : p_countriesList) {
            List<Integer> l_adjacentCountries = l_countryNeighbors.get(c.getD_countryId());
            c.setD_adjacentCountryIds(l_adjacentCountries);
        }
        return p_countriesList;
    }

    /**
     * Associates countries with their respective continents and assigns them within
     * the continent object.
     * 
     * @param p_countries  Total Country Objects
     * @param p_continents Total Continent Objects
     * @return List of updated continents
     */
    public List<Continent> linkCountryContinents(List<Country> p_countries, List<Continent> p_continents) {
        for (Country c : p_countries) {
            for (Continent cont : p_continents) {
                if (cont.getD_continentID().equals(c.getD_continentId())) {
                    cont.addCountry(c);
                }
            }
        }
        return p_continents;
    }

    /**
     * 
     * Retrieves country and border data from the game state and further processes
     * it for writing to a file using a file writer.
     * 
     * @param p_gameState Current GameState Object
     * @param p_writer    Writer object for file
     * @throws IOException handles I/0
     */
    public void writeCountryAndBoarderMetaData(GameState p_gameState, FileWriter p_writer) throws IOException {
        String l_countryMetaData = new String();
        String l_bordersMetaData = new String();
        List<String> l_bordersList = new ArrayList<>();

        // Writes Country Objects to File And Organizes Border Data for each of them
        p_writer.write(System.lineSeparator() + AppConstants.COUNTRIES + System.lineSeparator());
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            l_countryMetaData = new String();
            l_countryMetaData = l_country.getD_countryId().toString().concat(" ").concat(l_country.getD_countryName())
                    .concat(" ").concat(l_country.getD_continentId().toString());
            p_writer.write(l_countryMetaData + System.lineSeparator());

            if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
                l_bordersMetaData = new String();
                l_bordersMetaData = l_country.getD_countryId().toString();
                for (Integer l_adjCountry : l_country.getD_adjacentCountryIds()) {
                    l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_adjCountry.toString());
                }
                l_bordersList.add(l_bordersMetaData);
            }
        }

        // Writes Border data to the File
        if (null != l_bordersList && !l_bordersList.isEmpty()) {
            p_writer.write(System.lineSeparator() + AppConstants.BORDERS + System.lineSeparator());
            for (String l_borderStr : l_bordersList) {
                p_writer.write(l_borderStr + System.lineSeparator());
            }
        }
    }

    /**
     * Obtains continent data from the game state and proceeds to write it to a
     * file.
     * 
     * @param p_gameState Current GameState
     * @param p_writer    Writer Object for file
     * @throws IOException handles I/O
     */
    public void writeContinentMetadata(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + AppConstants.CONTINENTS + System.lineSeparator());
        for (Continent l_continent : p_gameState.getD_map().getD_continents()) {
            p_writer.write(
                    l_continent.getD_continentName().concat(" ").concat(l_continent.getD_continentValue().toString())
                            + System.lineSeparator());
        }
    }

    /**
     * Writes the domination Map changes to File.
     *
     * @param p_gameState current gamestate
     * @param l_writer    file writer
     * @param l_mapFormat Map Format
     * @throws IOException Exception
     */
    public void writeChangesToMap(GameState p_gameState, FileWriter l_writer, String l_mapFormat) throws IOException {
        if (null != p_gameState.getD_map().getD_continents()
                && !p_gameState.getD_map().getD_continents().isEmpty()) {
            writeContinentMetadata(p_gameState, l_writer);
        }
        if (null != p_gameState.getD_map().getD_countries()
                && !p_gameState.getD_map().getD_countries().isEmpty()) {
            writeCountryAndBoarderMetaData(p_gameState, l_writer);
        }
    }
}
