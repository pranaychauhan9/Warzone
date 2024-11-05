package Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Constants.AppConstants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import CommonFunctions.CommonCode;

/**
 * The MapService class load, read, parse, edit, and save map file.
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
public class MapService implements Serializable {

	/**
	 * The 'loadmap' method handles the processing of map files.
	 * 
	 * @param p_gameState    current state of game.
	 * @param p_loadFileName map file name.
	 * @return Map object after processing map file.
	 */
	public Map loadMap(GameState p_gameState, String p_loadFileName) {
		Map l_map = new Map();
		l_map.setD_mapFile(p_loadFileName);
		List<String> l_linesOfFile = loadFile(p_loadFileName);
		if (isValidFileContent(l_linesOfFile) && l_linesOfFile.contains("[Territories]")) {
			MapAdapter l_mapAdapter = new MapAdapter(new ConquestMap());
			l_mapAdapter.readMapContent(l_linesOfFile, l_map, p_gameState);
		} else if (isValidFileContent(l_linesOfFile) && l_linesOfFile.contains("[countries]")) {
			DominationMap l_dominationMap = new DominationMap();
			l_dominationMap.readMapContent(l_linesOfFile, l_map, p_gameState);
		}
		return l_map;
	}

	/**
	 * Checks if the file content is valid.
	 *
	 * @param p_fileContent List of lines from the file
	 * @return true if the file content is valid, false otherwise
	 */
	private boolean isValidFileContent(List<String> p_fileContent) {
		return (null != p_fileContent && !p_fileContent.isEmpty());
	}

	/**
	 * The 'loadFile' method is used to handle the loading and reading of map files.
	 * 
	 * @param p_loadFileName map file name to load.
	 * @return List of lines from map file.
	 */
	public List<String> loadFile(String p_loadFileName) {

		String l_filePath = CommonCode.getMapFilePath(p_loadFileName);
		List<String> l_lineList = new ArrayList<>();

		BufferedReader l_reader;
		try {
			l_reader = new BufferedReader(new FileReader(l_filePath));
			l_lineList = l_reader.lines().collect(Collectors.toList());
			l_reader.close();
		} catch (IOException l_e1) {
			System.out.println("File not Found!");
		}
		return l_lineList;
	}

	/**
	 * Method is responsible for creating a new map if map to be edited does not
	 * exists, and if it exists it parses the map file to game state object.
	 * 
	 * @param p_gameState    GameState model class object
	 * @param p_editFilePath consists of base filepath
	 * @throws IOException triggered in case the file does not exist or the file
	 *                     name is invalid
	 */
	public void editMap(GameState p_gameState, String p_editFilePath) throws IOException {

		String l_filePath = CommonCode.getMapFilePath(p_editFilePath);
		File l_fileToBeEdited = new File(l_filePath);

		if (l_fileToBeEdited.createNewFile()) {
			System.out.println("File has been created.");
			Map l_map = new Map();
			l_map.setD_mapFile(p_editFilePath);
			p_gameState.setD_map(l_map);
			p_gameState.updateLogFile(p_editFilePath + " File has been created for user to edit",
					AppConstants.ORDER_EFFECT);
		} else {
			System.out.println("Editing " + p_editFilePath + " File.");
			this.loadMap(p_gameState, p_editFilePath);
			if (null == p_gameState.getD_map()) {
				p_gameState.setD_map(new Map());
			}
			p_gameState.getD_map().setD_mapFile(p_editFilePath);
			p_gameState.updateLogFile(p_editFilePath + " already exists and is loaded for editing",
					AppConstants.ORDER_EFFECT);
		}
	}

	/**
	 * Builds an updated list of continents based on the provided operations
	 * (Add/Remove) and their associated arguments.
	 * 
	 * @param p_gameState      Current GameState Object
	 * @param p_mapToBeUpdated Map Object to be Updated
	 * @param p_operation      Operation to perform on Continents
	 * @param p_argument       Arguments pertaining to the operations
	 * @return List of updated continents
	 * @throws InvalidMap invalidmap exception
	 */
	public Map addRemoveContinents(GameState p_gameState, Map p_mapToBeUpdated, String p_operation, String p_argument)
			throws InvalidMap {

		try {
			if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
				p_mapToBeUpdated.addContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
				this.setD_MapServiceLog("Continent " + p_argument.split(" ")[0] + " added successfully!", p_gameState);
			} else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 1) {
				p_mapToBeUpdated.removeContinent(p_argument.split(" ")[0]);
				this.setD_MapServiceLog("Continent " + p_argument.split(" ")[0] + " removed successfully!",
						p_gameState);
			} else {
				throw new InvalidMap("Continent " + p_argument.split(" ")[0]
						+ " couldn't be added/removed. Changes are not made due to Invalid Command Passed.");
			}
		} catch (InvalidMap | NumberFormatException l_e) {
			this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
		}

		return p_mapToBeUpdated;
	}

	/**
	 * Executes the add/remove operation on the countries within the map.
	 * 
	 * @param p_gameState      Current GameState Object
	 * @param p_mapToBeUpdated The Map to be updated
	 * @param p_operation      Operation to be performed
	 * @param p_argument       Arguments for the pertaining command operation
	 * @return Updated Map Object
	 * @throws InvalidMap invalidmap exception
	 */
	public Map addRemoveCountry(GameState p_gameState, Map p_mapToBeUpdated, String p_operation, String p_argument)
			throws InvalidMap {
		try {
			if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
				p_mapToBeUpdated.addCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
				this.setD_MapServiceLog("Country " + p_argument.split(" ")[0] + " added successfully!", p_gameState);
			} else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 1) {
				p_mapToBeUpdated.removeCountry(p_argument.split(" ")[0]);
				this.setD_MapServiceLog("Country " + p_argument.split(" ")[0] + " removed successfully!", p_gameState);
			} else {
				throw new InvalidMap("Country " + p_argument.split(" ")[0] + " could not be " + p_operation + "ed!");
			}
		} catch (InvalidMap l_e) {
			this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
		}
		return p_mapToBeUpdated;
	}

	/**
	 * Executes the add/remove operation on Map Object.
	 * 
	 * @param p_gameState      Current GameState Object
	 * @param p_mapToBeUpdated The Map to be updated
	 * @param p_operation      Add/Remove operation to be performed
	 * @param p_argument       Arguments for the pertaining command operation
	 * @return map to be updated
	 * @throws InvalidMap invalidmap exception
	 */
	public Map addRemoveNeighbour(GameState p_gameState, Map p_mapToBeUpdated, String p_operation, String p_argument)
			throws InvalidMap {

		try {
			if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
				p_mapToBeUpdated.addCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
				this.setD_MapServiceLog("Neighbour Pair " + p_argument.split(" ")[0] + " " + p_argument.split(" ")[1]
						+ " added successfully!", p_gameState);
			} else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 2) {
				p_mapToBeUpdated.removeCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
				this.setD_MapServiceLog("Neighbour Pair " + p_argument.split(" ")[0] + " " + p_argument.split(" ")[1]
						+ " removed successfully!", p_gameState);
			} else {
				throw new InvalidMap("Neighbour could not be " + p_operation + "ed!");
			}
		} catch (InvalidMap l_e) {
			this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
		}
		return p_mapToBeUpdated;
	}

	/**
	 * Controls the Flow of Edit Operations: editcontinent, editcountry,
	 * editneighbor.
	 *
	 * @param p_gameState       Current GameState Object.
	 * @param p_argument        Arguments for the pertaining command operation.
	 * @param p_operation       Add/Remove operation to be performed.
	 * @param p_switchParameter Type of Edit Operation to be performed.
	 * @throws IOException    Exception.
	 * @throws InvalidMap     invalidmap exception.
	 * @throws InvalidCommand invalid command exception
	 */
	public void editFunctions(GameState p_gameState, String p_operation, String p_argument, String p_switchParameter)
			throws IOException, InvalidMap, InvalidCommand {
		Map l_updatedMap;
		String l_mapFileName = p_gameState.getD_map().getD_mapFile();
		Map l_mapToBeUpdated = (CommonCode.isNull(p_gameState.getD_map().getD_continents())
				&& CommonCode.isNull(p_gameState.getD_map().getD_countries()))
						? this.loadMap(p_gameState, l_mapFileName)
						: p_gameState.getD_map();

		// Edit Control Logic for Continent, Country & Neighbor
		if (!CommonCode.isNull(l_mapToBeUpdated)) {
			switch (p_switchParameter.toLowerCase()) {
				case AppConstants.CONTINENT:
					l_updatedMap = addRemoveContinents(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
					break;
				case AppConstants.COUNTRY:
					l_updatedMap = addRemoveCountry(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
					break;
				case "neighbour":
					l_updatedMap = addRemoveNeighbour(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + p_switchParameter);
			}
			p_gameState.setD_map(l_updatedMap);
			p_gameState.getD_map().setD_mapFile(l_mapFileName);
		}
	}

	/**
	 * Executes the updated map to .map file and stores it at required location.
	 * 
	 * @param p_gameState Current GameState
	 * @param p_fileName  filename to save things in
	 * @return true/false based on successful save operation of map to file
	 * @throws InvalidMap InvalidMap exception
	 */
	public boolean saveMap(GameState p_gameState, String p_fileName) throws InvalidMap {
		try {
			// Verifies if the file linked to savemap and edited by user are same
			if (!p_fileName.equalsIgnoreCase(p_gameState.getD_map().getD_mapFile())) {
				p_gameState.setError("Kindly provide same file name to save which you have given for edit");
				return false;
			} else {
				if (null != p_gameState.getD_map()) {
					Models.Map l_currentMap = p_gameState.getD_map();

					// Proceeds to save the map if it passes the validation check
					this.setD_MapServiceLog("Validating Map......", p_gameState);
					boolean l_mapValidationStatus = l_currentMap.Validate();
					if (l_mapValidationStatus) {
						String l_format = null;
						Files.deleteIfExists(Paths.get(CommonCode.getMapFilePath(p_fileName)));
						FileWriter l_writer = new FileWriter(CommonCode.getMapFilePath(p_fileName));
						l_format = this.getMapFormat();
						writeChangesToMap(p_gameState, l_writer, l_format);
						l_writer.close();
					}
				} else {
					p_gameState.updateLogFile("Validation failed, Unable to Save the Map!", AppConstants.ORDER_EFFECT);
					p_gameState.setError("Validation Failed");
					return false;
				}
			}
			return true;
		} catch (IOException l_e) {
			l_e.printStackTrace();
			p_gameState.updateLogFile("Changes not saved", AppConstants.ORDER_EFFECT);
			p_gameState.setError("Error in saving map file");
			return false;
		}
	}

	/**
	 * Writes the Map changes to File.
	 *
	 * @param p_gameState current gamestate
	 * @param l_writer    file writer
	 * @param l_mapFormat Map Format
	 * @throws IOException Exception
	 */
	private void writeChangesToMap(GameState p_gameState, FileWriter l_writer, String l_mapFormat) throws IOException {
		if (l_mapFormat.equalsIgnoreCase(AppConstants.CONQUEST_MAP)) {
			MapAdapter l_mapAdapter = new MapAdapter(new ConquestMap());
			l_mapAdapter.writeChangesToMap(p_gameState, l_writer, l_mapFormat);
		} else {
			DominationMap l_dominationMap = new DominationMap();
			l_dominationMap.writeChangesToMap(p_gameState, l_writer, l_mapFormat);
		}
	}

	/**
	 * Restores the map in the Game State to its default state.
	 *
	 * @param p_gameState    object of GameState class
	 * @param p_name_of_file name of file to be loaded
	 */
	public void resetMap(GameState p_gameState, String p_name_of_file) {
		System.out.println("Map cannot be loaded, as it is invalid. Kindly provide valid map");
		p_gameState.updateLogFile(p_name_of_file + " map could not be loaded as it is invalid!",
				AppConstants.ORDER_EFFECT);
		p_gameState.setD_map(new Models.Map());
	}

	/**
	 * Set the log of map service methods.
	 *
	 * @param p_MapServiceLog Log Message for map service
	 * @param p_gameState     current gamestate
	 */
	public void setD_MapServiceLog(String p_MapServiceLog, GameState p_gameState) {
		System.out.println(p_MapServiceLog);
		p_gameState.updateLogFile(p_MapServiceLog, AppConstants.ORDER_EFFECT);
	}

	/**
	 * Checks which format user wants to save the map in.
	 *
	 * @return Map format to be saved
	 * @throws IOException reading exception
	 */
	public String getMapFormat() throws IOException {
		BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Press 1 to save in Conquest map format");
		System.out.println("Press 2 to save in Domination map format");
		String l_input = l_bufferedReader.readLine();
		if (l_input.equalsIgnoreCase("1")) {
			return AppConstants.CONQUEST_MAP;
		} else if (l_input.equalsIgnoreCase("2")) {
			return AppConstants.DOMINATION_MAP;
		} else {
			System.err.println("Invalid Input Passed.");
			return this.getMapFormat();
		}
	}
}
