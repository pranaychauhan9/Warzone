package Models;

import java.io.IOException;
import java.io.Serializable;

import Controllers.GameEngineCtx;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.MapService;
import Services.PlayerService;
import CommonFunctions.Command;
import Constants.AppConstants;

/**
 * This abstract class lists the methods required for all game phases.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public abstract class GamePlayPhase implements Serializable {

    /**
     * d_gameState stores the information about current GamePlay.
     */
    GameState d_gameState;

    /**
     * d_gameEngineCtx manage the state of the game engine and control gameplay.
     */
    GameEngineCtx d_gameEngineCtx;

    /**
     * d_mapService instance is used to perform map operations such as load, read,
     * parse, edit, and save map file.
     */
    MapService d_mapService = new MapService();

    /**
     * Player Service instance is used to modify player's list and give orders.
     */
    PlayerService d_playerService = new PlayerService();

    /**
     * It's a flag used to determine whether the map has been loaded.
     */
    boolean l_mapLoadSuccess;

    /**
     * Create a instance of tournament mode.
     */
    Tournament d_tournament = new Tournament();

    /**
     * A constructor for initializing the current game engine's value.
     *
     * @param p_gameEngineCtx Instance of the game engine used to update the
     *                        state.
     * @param p_gameState     The current game state associated with the game
     *                        engine instance.
     */
    public GamePlayPhase(GameEngineCtx p_gameEngineCtx, GameState p_gameState) {
        d_gameEngineCtx = p_gameEngineCtx;
        d_gameState = p_gameState;
    }

    /**
     * getD_gameState method is used as a getter to access the current game state.
     *
     * @return the current game state
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * setD_gameState method is utilized as a setter to update the current game
     * state.
     *
     * @param p_gameState game state instance to set for phase
     */
    public void setD_gameState(GameState p_gameState) {
        d_gameState = p_gameState;
    }

    /**
     * Process the entered commands.
     * 
     * @param p_userInputCommand command entered by the user in CLI
     * @param p_player           instance to Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    public void handleCommand(String p_userInputCommand, Player p_player)
            throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_userInputCommand, p_player);
    }

    /**
     * commandHandler processes user-entered commands and routes them to the
     * respective phase implementations.
     *
     * @param p_userInputCommand command entered by the user in CLI
     * @param p_player           instance to Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    public void commandHandler(String p_userInputCommand, Player p_player)
            throws InvalidMap, InvalidCommand, IOException {
        Command l_command = new Command(p_userInputCommand);
        String l_rootCommand = l_command.getRootCommand();
        l_mapLoadSuccess = d_gameState.getD_map() != null;

        d_gameState.updateLogFile(l_command.getD_command() + " 1", AppConstants.HANDLE_COMMAND);

        switch (l_rootCommand) {
            case "loadmap": {
                executeLoadMap(l_command, p_player);
                break;
            }
            case "editmap": {
                executeMapEdit(l_command, p_player);
                break;
            }
            case "editcontinent": {
                executeEditContinent(l_command, p_player);
                break;
            }
            case "editcountry": {
                executeEditCountry(l_command, p_player);
                break;
            }
            case "editneighbor": {
                executeEditNeighbour(l_command, p_player);
                break;
            }
            case "validatemap": {
                executeValidateMap(l_command, p_player);
                break;
            }
            case "savemap": {
                executeSaveMap(l_command, p_player);
                break;
            }
            case "gameplayer": {
                executeGameplayer(l_command, p_player);
                break;
            }
            case "assigncountries": {
                executeAssignCountries(l_command, p_player, false, d_gameState);
                break;
            }
            case "showmap": {
                executeShowMap(l_command, p_player);
                break;
            }
            case "savegame": {
                executeSaveGame(l_command, p_player);
                break;
            }
            case "loadgame": {
                executeLoadGame(l_command, p_player);
                break;
            }
            case "deploy": {
                executeDeploy(p_userInputCommand, p_player);
                break;
            }
            case "advance": {
                executeAdvance(p_userInputCommand, p_player);
                break;
            }
            case "airlift":
            case "blockade":
            case "negotiate":
            case "bomb": {
                executeCardAction(p_userInputCommand, p_player);
                break;
            }
            case "tournament": {
                executetournamentGamePlay(l_command);
                break;
            }
            case "exit": {
                System.exit(0);
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * This method is called when phase is being changed.
     * 
     * @param p_isTournamentMode if game is being played in tournament mode
     */
    public abstract void initPhase(boolean p_isTournamentMode);

    /**
     * Handles the deploy order command.
     *
     * @param p_command User command
     * @param p_player  Player instance
     * @throws IOException Failure in I/O operation
     */
    protected abstract void executeDeploy(String p_command, Player p_player) throws IOException;

    /**
     * Handles the tournament command.
     *
     * @param p_command User command
     * @throws InvalidCommand indicates command is invalid
     * @throws InvalidMap     InvalidMap
     */
    protected abstract void executetournamentGamePlay(Command p_command) throws InvalidCommand, InvalidMap;

    /**
     * Handle logging if a certain command is invalid in current phase.
     */
    public void LogInvalidCommandInCurrentPhase() {
        d_gameEngineCtx.setD_gameEngineCtxLog("Invalid Command in Current State",
                AppConstants.ORDER_EFFECT);
    }

    /**
     * Basic validation of <strong>loadmap</strong> command for checking required
     * arguments and
     * redirecting control to model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeLoadMap(Command p_command, Player p_player)
            throws InvalidCommand, InvalidMap, IOException;

    /**
     * Basic validation of <strong>editmap</strong> command for checking required
     * arguments and redirecting control to model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeMapEdit(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Basic validation of <strong>editcontinent</strong> command for checking
     * required arguments and redirecting control to model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeEditContinent(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Basic validation of <strong>editcountry</strong> command for checking
     * required arguments and redirecting control to model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeEditCountry(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Basic validation of <strong>editneighbor</strong> command for checking
     * required arguments and redirecting control to model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeEditNeighbour(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Basic validation of <strong>validatemap</strong> command for checking
     * required arguments and
     * redirecting control to model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeValidateMap(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Basic validation of <strong>savemap</strong> command for checking required
     * arguments and redirecting control to model for actual processing.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeSaveMap(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Controls the Game Loading Feature.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeLoadGame(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Controls the Game Save Feature.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeSaveGame(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Executes <strong>gameplayer</strong> command in order to add or remove
     * players.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeGameplayer(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Assigns countries and continents to players and starts the game loop.
     *
     * @param p_command          command entered by the user on CLI
     * @param p_player           instance of Player Object
     * @param p_isTournamentMode if game is being played in tournament mode
     * @param p_gameState        current state of the game
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeAssignCountries(Command p_command, Player p_player, boolean p_isTournamentMode,
            GameState p_gameState)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * This method is responsible for managing the 'show map' command.
     *
     * @param p_command command entered by the user on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    protected abstract void executeShowMap(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap;

    /**
     * Handles the execution of the advance command in game play phase.
     *
     * @param p_command command entered by user on the CLI
     * @param p_player  instance of player object
     * @throws IOException indicates failure in I/O operation
     */
    protected abstract void executeAdvance(String p_command, Player p_player) throws IOException;

    /**
     * Manages the Card Commands.
     *
     * @param p_enteredCommand command entered by the user on CLI
     * @param p_player         instance of Player Object
     * @throws IOException indicates failure in I/O operation
     */
    protected abstract void executeCardAction(String p_enteredCommand, Player p_player) throws IOException;

}