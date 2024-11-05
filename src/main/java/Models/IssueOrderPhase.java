package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import Controllers.GameEngineCtx;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.GameService;
import CommonFunctions.ExceptionLogHandler;
import CommonFunctions.Command;
import Constants.AppConstants;
import Views.ShowMap;

/**
 * Implementation of the Issue Order Phase for GamePlay using the State Pattern.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class IssueOrderPhase extends GamePlayPhase {

    /**
     * This is a constructor responsible for initializing the GameEngine context
     * within the Phase class.
     *
     * @param p_gameEngineController Instance of the game engine used to update the
     *                               state.
     * @param p_gameState            The current game state associated with the game
     *                               engine instance.
     */
    public IssueOrderPhase(GameEngineCtx p_gameEngineController, GameState p_gameState) {
        super(p_gameEngineController, p_gameState);
    }

    @Override
    protected void executeCardAction(String p_enteredCommand, Player p_player) throws IOException {
        if (p_player.getD_playerOwnedCards().contains(p_enteredCommand.split(" ")[0])) {
            p_player.handleCardActions(p_enteredCommand, d_gameState);
            d_gameEngineCtx.setD_gameEngineCtxLog(p_player.d_playerLogMessage, AppConstants.ORDER_EFFECT);
        }
    }

    @Override
    protected void executeShowMap(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
        ShowMap l_showMap = new ShowMap(d_gameState);
        l_showMap.showMap();

        askForOrder(p_player);
    }

    @Override
    protected void executeAdvance(String p_command, Player p_player) throws IOException {
        p_player.initAdvanceOrder(p_command, d_gameState);
        d_gameState.updateLog(p_player.getD_playerLogMessage(), AppConstants.ORDER_EFFECT);
    }

    @Override
    protected void executeDeploy(String p_command, Player p_player) throws IOException {
        p_player.initDeployOrder(p_command);
        d_gameState.updateLogFile(p_player.getD_playerLogMessage(), AppConstants.ORDER_EFFECT);
    }

    @Override
    public void initPhase(boolean p_isTournamentMode) {
        while (d_gameEngineCtx.getD_CurrentPhase() instanceof IssueOrderPhase) {
            issueOrders(p_isTournamentMode);
        }
    }

    /**
     * Accepts orders from players.
     * 
     * @param p_isTournamentMode if game is being played in tournament mode
     */
    protected void issueOrders(boolean p_isTournamentMode) {
        do {
            for (Player l_player : d_gameState.getD_playerList()) {
                if (l_player.getD_playerCountries().size() == 0) {
                    l_player.setD_additionalOrders(false);
                }
                if (l_player.getD_additionalOrders() && !l_player.getD_playerName().equals("Neutral")) {
                    try {
                        l_player.issue_order(this);
                        l_player.checkAdditionalOrders(p_isTournamentMode);
                    } catch (InvalidCommand | IOException | InvalidMap l_exception) {
                        d_gameEngineCtx.setD_gameEngineCtxLog(l_exception.getMessage(),
                                AppConstants.ORDER_EFFECT);
                    }
                }
            }
        } while (d_playerService.checkAdditionalOrders(d_gameState.getD_playerList()));

        d_gameEngineCtx.setGamePlayPhase(AppConstants.ORDER_EXECUTION_PHASE, false);
    }

    /**
     * Asks for order commands from user.
     * 
     * @param p_player player for which commands are to be issued
     * @throws InvalidCommand exception if command is invalid
     * @throws IOException    indicates failure in I/O operation
     * @throws InvalidMap     indicates failure in using the invalid map
     */
    public void askForOrder(Player p_player) throws InvalidCommand, IOException, InvalidMap {
        String l_commandEntered = p_player.getPlayerOrder(d_gameState);

        if (l_commandEntered == null)
            return;

        d_gameState.updateLogFile("(Player: " + p_player.getD_playerName() + ") " + l_commandEntered,
                AppConstants.ISSUE_ORDERS);
        handleCommand(l_commandEntered, p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeAssignCountries(Command p_command, Player p_player, boolean isTournamentMode,
            GameState p_gameState)
            throws InvalidCommand, IOException, InvalidMap {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeGameplayer(Command p_command, Player p_player)
            throws InvalidCommand, IOException, InvalidMap {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeEditNeighbour(Command p_command, Player p_player)
            throws InvalidCommand, InvalidMap, IOException {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeValidateMap(Command p_command, Player p_player)
            throws InvalidMap, InvalidCommand, IOException {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeEditContinent(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeMapEdit(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeEditCountry(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeLoadGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        LogInvalidCommandInCurrentPhase();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeSaveGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        List<java.util.Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(AppConstants.INVALID_SAVEGAME_CMD_ERROR);
        }

        for (Map<String, String> l_map : l_operations_list) {
            if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)) {
                String l_filename = l_map.get(AppConstants.ARGUMENTS);
                GameService.saveGame(this, l_filename);
                d_gameEngineCtx.setD_gameEngineCtxLog("Game Saved Successfully to " + l_filename,
                        AppConstants.ORDER_EFFECT);
            } else {
                throw new InvalidCommand(AppConstants.INVALID_SAVEGAME_CMD_ERROR);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executetournamentGamePlay(Command p_enteredCommand) throws InvalidCommand, InvalidMap {

    }

}