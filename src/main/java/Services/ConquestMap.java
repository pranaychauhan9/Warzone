package Services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Constants.AppConstants;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;

/**
 * Handles read, write operatons on ConquestMap.
 *
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class ConquestMap implements Serializable {

	/**
	 * Processes the map content extracted from the file and updates the map and
	 * game state.
	 *
	 * @param p_fileContent List of lines from the file
	 * @param p_map         Map object to update with processed data
	 * @param p_gameState   GameState to update with the processed map
	 */
	public void readMapContent(List<String> p_fileContent, Map p_map, GameState p_gameState) {
		// Parses the file and stores information in objects
		List<String> l_continentData = retrieveMetaData(p_fileContent, AppConstants.CONTINENT);
		List<String> l_countryData = retrieveMetaData(p_fileContent, AppConstants.COUNTRY);
		List<Continent> l_continentObjects = analyzeContinentsMetaData(l_continentData);
		List<Country> l_countryObjects = analyzeCountriesMetaData(l_countryData, l_continentObjects);
		List<Country> l_updatedCountries = analyzeBorderMetaData(l_countryObjects, l_countryData);

		l_continentObjects = linkCountryContinents(l_updatedCountries, l_continentObjects);
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
						p_fileLines.indexOf(AppConstants.CONQUEST_CONTINENT) + 1,
						p_fileLines.indexOf(AppConstants.CONQUEST_TERRITORIES) - 1);
				return l_continentLines;
			case AppConstants.COUNTRY:
				List<String> l_countryLines = p_fileLines
						.subList(p_fileLines.indexOf(AppConstants.CONQUEST_TERRITORIES) + 1, p_fileLines.size());
				return l_countryLines;
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
			String[] l_metaData = cont.split("=");
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
	 * @param p_continentList includes Continents data in list from map file.
	 * @return List of processed country meta data.
	 */
	public List<Country> analyzeCountriesMetaData(List<String> p_countriesList, List<Continent> p_continentList) {
		List<Country> l_countriesList = new ArrayList<Country>();
		int l_country_id = 1;
		for (String country : p_countriesList) {
			String[] l_metaDataCountries = country.split(",");
			Continent l_continent = this.getContinentByName(p_continentList, l_metaDataCountries[3]);
			Country l_countryObj = new Country(l_country_id, l_metaDataCountries[0],
					l_continent.getD_continentID());
			l_countriesList.add(l_countryObj);
			l_country_id++;
		}
		return l_countriesList;
	}

	/**
	 * Establishes connections between the Country Objects and their corresponding
	 * neighbors.
	 *
	 * @param p_countriesList Total Country Objects Initialized
	 * @param p_countryLines  Country Lines
	 * @return List Updated Country Objects
	 */
	public List<Country> analyzeBorderMetaData(List<Country> p_countriesList, List<String> p_countryLines) {
		List<Country> l_updatedCountryList = new ArrayList<>(p_countriesList);
		String l_matchedCountry = null;
		for (Country l_cont : l_updatedCountryList) {
			for (String l_contStr : p_countryLines) {
				if ((l_contStr.split(",")[0]).equalsIgnoreCase(l_cont.getD_countryName())) {
					l_matchedCountry = l_contStr;
					break;
				}
			}
			if (l_matchedCountry.split(",").length > 4) {
				for (int i = 4; i < l_matchedCountry.split(",").length; i++) {
					Country l_country = this.getCountryByName(p_countriesList, l_matchedCountry.split(",")[i]);
					l_cont.getD_adjacentCountryIds().add(l_country.getD_countryId());
				}
			}
		}
		return l_updatedCountryList;
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
	 * Filters continent based on continent name.
	 * 
	 * @param p_continentList list of continents from which filtering has to be done
	 * @param p_continentName name of the continent which has to be matched
	 * @return filtered continent based on name
	 */
	public Continent getContinentByName(List<Continent> p_continentList, String p_continentName) {
		Continent l_continent = p_continentList.stream()
				.filter(l_cont -> l_cont.getD_continentName().equalsIgnoreCase(p_continentName))
				.findFirst().orElse(null);
		return l_continent;
	}

	/**
	 * Filters country based on country name.
	 * 
	 * @param p_countrytList list of countries from which filtering has to be done
	 * @param p_countryName  name of the country which has to be matched
	 * @return filtered country based on name
	 */
	public Country getCountryByName(List<Country> p_countrytList, String p_countryName) {
		Country l_country = p_countrytList.stream()
				.filter(l_cont -> l_cont.getD_countryName().equalsIgnoreCase(p_countryName))
				.findFirst().orElse(null);
		return l_country;
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

		// Writes Country Objects to File And Organizes Border Data for each of them
		p_writer.write(
				System.lineSeparator() + AppConstants.CONQUEST_TERRITORIES + System.lineSeparator());
		for (Country l_country : p_gameState.getD_map().getD_countries()) {
			l_countryMetaData = new String();
			l_countryMetaData = l_country.getD_countryName().concat(",dummy1,dummy2,")
					.concat(p_gameState.getD_map().getContinentByID(l_country.getD_continentId()).getD_continentName());

			if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
				for (Integer l_adjCountry : l_country.getD_adjacentCountryIds()) {
					l_countryMetaData = l_countryMetaData.concat(",")
							.concat(p_gameState.getD_map().getCountryByID(l_adjCountry).getD_countryName());
				}
			}
			p_writer.write(l_countryMetaData + System.lineSeparator());
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
		p_writer.write(System.lineSeparator() + AppConstants.CONQUEST_CONTINENT + System.lineSeparator());
		for (Continent l_continent : p_gameState.getD_map().getD_continents()) {
			p_writer.write(
					l_continent.getD_continentName().concat("=").concat(l_continent.getD_continentValue().toString())
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
