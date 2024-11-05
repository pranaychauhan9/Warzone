package Constants;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * This class defines a set of constants used throughout the application.
 * These constants include error messages, command formats, file extensions,
 * and color codes for console output.
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
public class AppConstants implements Serializable {
	/**
	 * Error message for an invalid 'Savegame' command format.
	 */
	public static final String INVALID_SAVEGAME_CMD_ERROR = "The CMD provided is invalid. Please use the following format for the 'savegame' CMD: 'savegame filename.'";

	/**
	 * Error message for an invalid 'loadgame' command format.
	 */
	public static final String INVALID_LOADGAME_CMD_ERROR = "The CMD provided is invalid. Please use the following format for the 'loadgame' CMD: 'loadgame filename.'";

	/**
	 * Error message for an invalid 'editmap' command.
	 */
	public static final String INVALID_EDITMAP_CMD_ERROR = "Invalid CMD. Kindly provide CMD in Format of : editmap filename";

	/**
	 * Error message for when a map is empty.
	 */
	public static final String CMD_MAP_ERROR_EMPTY = "Map not found. To proceed, please load a valid map.";

	/**
	 * Error message for an invalid 'loadmap' command format.
	 */
	public static final String INVALID_LOADMAP_CMD_ERROR = "The CMD provided is invalid. Please use the following format for the 'loadmap' CMD: 'loadmap filename.'";

	/**
	 * Error message for an invalid 'tournament' command format.
	 */
	public static final String INVALID_TOURNAMENT_CMD_ERROR = "Invalid Command. Kindly provide command in format of : tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns";
	/**
	 * Key for specifying arguments in a map.
	 */
	public static final String ARGUMENTS = "arguments";

	/**
	 * Key for specifying operations in a map.
	 */
	public static final String OPERATION = "operation";

	/**
	 * File extension for map files.
	 */
	public static final String EXTENSION_FOR_MAP_FILES = ".map";

	/**
	 * Section header for continents in map files.
	 */
	public static final String CONTINENTS = "[continents]";

	/**
	 * Section header for countries in map files.
	 */
	public static final String COUNTRIES = "[countries]";

	/**
	 * Section header for borders in map files.
	 */
	public static final String BORDERS = "[borders]";

	/**
	 * Label for armies.
	 */
	public static final String ARMIES = "Armies";

	/**
	 * Label for control value.
	 */
	public static final String CONTROL_VALUE = "Control Value";

	/**
	 * Label for connections.
	 */
	public static final String CONNECTIVITY = "Connections";

	/**
	 * Default directory path for map files.
	 */
	public static final String WARZONE_SRC_MAIN_RESOURCES = "src/main/resources";

	/**
	 * Error message for an incorrect 'deploy' command format.
	 */
	public static final String DEPLOY_ERROR_MSG = "Incorrect command. To deploy, follow this format: deploy countryID <CountryName> <num>";

	/**
	 * Error message for an invalid 'assigncountries' command.
	 */
	public static final String ASSIGNCOUNTRIES_ERROR_MSG = "Invalid command. To assign countries, follow this format: assigncountries";

	/**
	 * Error message for an incorrect 'gameplayer' command format.
	 */
	public static final String GAMEPLAYER_ERROR_MSG = "Incorrect command. To add or remove a player, follow this format: gameplayer -add playername -remove playername";

	/**
	 * Error message for an invalid 'editcontinent' command.
	 */
	public static final String INVALID_EDIT_CONTINENT_CMD_ERROR = "Invalid CMD. Kindly provide CMD in Format of : editcontinent -add continentID continentvalue -remove continentID";

	/**
	 * Error message for an invalid 'editcountry' command.
	 */
	public static final String INVALID_EDITCOUNTRY_CMD_ERROR = "Invalid CMD. Kindly provide CMD in Format of : editcountry -add countrytID continentID -remove countryID";

	/**
	 * Error message for an invalid 'editneighbor' command.
	 */
	public static final String INVALID_EDITNEIGHBOUR_CMD_ERROR = "Invalid command. Kindly provide command in Format of : editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID";

	/**
	 * Error message for an invalid 'savemap' command.
	 */
	public static final String INVALID_SAVEMAP_CMD_ERROR = "Invalid CMD. Kindly provide CMD in Format of : savemap filename";

	/**
	 * Error message for an invalid 'validatemap' command.
	 */
	public static final String INVALID_VALIDATEMAP_CMD_ERROR = "Invalid CMD! validatemap is not supposed to have any arguments";

	/**
	 * Message indicating that the loaded map is valid.
	 */
	public static final String VALID_MAP = "The loaded map is valid!";

	/**
	 * Width of the console output.
	 */
	public static final int CONSOLE_WIDTH = 80;

	/**
	 * Default directory path for resources in the 'src' directory.
	 */
	public static final String SRC_MAIN_RESOURCES = "src/main/resources";

	/**
	 * ANSI color code for red text in console output.
	 */
	public static final String RED = "\033[0;31m";

	/**
	 * ANSI color code for green text in console output.
	 */
	public static final String GREEN = "\033[0;32m";

	/**
	 * ANSI color code for yellow text in console output.
	 */
	public static final String YELLOW = "\033[0;33m";

	/**
	 * ANSI color code for blue text in console output.
	 */
	public static final String BLUE = "\033[0;34m";

	/**
	 * ANSI color code for purple text in console output.
	 */
	public static final String PURPLE = "\033[0;35m";

	/**
	 * ANSI color code for cyan text in console output.
	 */
	public static final String CYAN = "\033[0;36m";

	/**
	 * List of ANSI color codes for console text.
	 */
	public static final List<String> COLORS = Arrays.asList(RED, GREEN, YELLOW, BLUE, PURPLE, CYAN);

	/**
	 * Log message - Handle command.
	 */
	public static final String HANDLE_COMMAND = "HANDLE_COMMAND";

	/**
	 * Log message - Give orders.
	 */
	public static final String ISSUE_ORDERS = "GIVE_ORDERS";

	/**
	 * Log message - Gameplay phase.
	 */
	public static final String GAMEPLAY_PHASE = "GAMEPLAY_PHASE";

	/**
	 * Log message - Effect of the order.
	 */
	public static final String ORDER_EFFECT = "ORDER_EFFECT";

	/**
	 * Log message - Start game state.
	 */
	public static final String START_GAME = "START_GAME";

	/**
	 * Log message - End game state.
	 */
	public static final String END_GAME = "END_GAME";

	/**
	 * Denotes game startup phase.
	 */
	public static final String START_UP_PHASE = "STARTUP";

	/**
	 * Denotes issue order phase.
	 */
	public static final String ISSUE_ORDER_PHASE = "ISSUE";

	/**
	 * Denotes order execution phase.
	 */
	public static final String ORDER_EXECUTION_PHASE = "EXECUTION";

	/**
	 * Error message to be logged.
	 */
	public static final String ERROR_LOG_MSG = "error";

	/**
	 * Successfull instructions or commands message to be logged.
	 */
	public static final String LOG_MSG = "log";

	/**
	 * List of available card types.
	 */
	public static final List<String> AVAILABLE_CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");

	/**
	 * List of actions that can be used for blockade validation.
	 */
	public static final List<String> BLOCKADE_VALIDATION = Arrays.asList("bomb", "advance", "airlift", "negotiate");

	/**
	 * Number of available card types.
	 */
	public static final int NUM_AVAILABLE_CARDS = AVAILABLE_CARDS.size();

	/**
	 * Represents the constant string for continent.
	 */
	public static final String CONTINENT = "continent";

	/**
	 * Represents the constant string for country.
	 */
	public static final String COUNTRY = "country";

	/**
	 * Represents the constant string for border.
	 */
	public static final String BORDER = "border";

	/**
	 * List of available Player Behaviors.
	 */
	// public static final List<String> PLAYER_BEHAVIORS = Arrays.asList("Human","Benevolent","Aggressive", "Random",
	// 		"Cheater");
	public static final List<String> PLAYER_BEHAVIORS =
	Arrays.asList("Aggressive","Random","Cheater");

	/**
	 * List of available Player Behaviors in Tournament.
	 */
	public static final List<String> TOURNAMENT_PLAYER_BEHAVIORS = Arrays.asList("Aggressive", "Random","Benevolent",
			"Cheater");
	/**
	 * Represents the constant string for conquest continents.
	 */
	public static final String CONQUEST_CONTINENT = "[Continents]";

	/**
	 * Represents the constant string for conquest territories.
	 */
	public static final String CONQUEST_TERRITORIES = "[Territories]";

	/**
	 * Represents the constant string for domination map.
	 */
	public static final String DOMINATION_MAP = "domination";

	/**
	 * Represents the constant string for conquest map.
	 */
	public static final String CONQUEST_MAP = "conquest";

}