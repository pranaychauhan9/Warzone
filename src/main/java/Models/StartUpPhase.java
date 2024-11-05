package Models;

import Constants.AppConstants;
import Controllers.GameEngineCtx;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import CommonFunctions.Command;
import CommonFunctions.CommonCode;
import Views.ShowMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import Services.GameService;
import CommonFunctions.ExceptionLogHandler;
import Views.TournamentView;

/**
 * Implementation of the concrete state Start-Up Phase for GamePlay.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class StartUpPhase extends GamePlayPhase {

	/**
	 * This is a constructor responsible for initializing the GameEngine context
	 * within the Phase class.
	 *
	 * @param p_gameEngineController Instance of the game engine used to update the
	 *                               state.
	 * @param p_gameState            The current game state associated with the game
	 *                               engine instance.
	 */
	public StartUpPhase(GameEngineCtx p_gameEngineController, GameState p_gameState) {
		super(p_gameEngineController, p_gameState);
	}

	@Override
	protected void executeCardAction(String p_enteredCommand, Player p_player) throws IOException {
		LogInvalidCommandInCurrentPhase();
	}

	/**
	 * {@inheritDoc}
	 */
	public void initPhase(boolean isTournamentMode) {
		BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));

		while (d_gameEngineCtx.getD_CurrentPhase() instanceof StartUpPhase) {
			try {
				System.out.println("Enter Game Commands or type 'exit' for quitting");
				String l_commandEntered = l_reader.readLine();

				handleCommand(l_commandEntered, null);
			} catch (InvalidCommand | InvalidMap | IOException l_exception) {
				d_gameEngineCtx.setD_gameEngineCtxLog(l_exception.getMessage(),
						AppConstants.ORDER_EFFECT);
			}
		}
	}

	@Override
	protected void executeShowMap(Command p_command, Player p_player) {
		ShowMap l_showMap = new ShowMap(d_gameState);
		l_showMap.showMap();
	}

	@Override
	protected void executeAdvance(String p_command, Player p_player) {
		LogInvalidCommandInCurrentPhase();
	}

	@Override
	protected void executeDeploy(String p_command, Player p_player) {
		LogInvalidCommandInCurrentPhase();
	}

	/**
	 * {@inheritDoc}
	 */
	public void executeLoadMap(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
		List<Map<String, String>> l_operationArgList = p_command.getOperationsAndArguments();
		if (null == l_operationArgList || l_operationArgList.isEmpty()) {
			throw new InvalidCommand(AppConstants.INVALID_LOADMAP_CMD_ERROR);
		} else {
			for (Map<String, String> l_map : l_operationArgList) {
				if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)) {
					try {

						// Loads the map if it is valid or resets the game state
						Models.Map l_chosenMap = d_mapService.loadMap(d_gameState,
								l_map.get(AppConstants.ARGUMENTS));

						if (l_chosenMap.Validate()) {
							d_gameState.setD_loadMapCommand();
							System.out.println("Map has been loaded successfully. \n");
						} else {
							d_mapService.resetMap(d_gameState, l_map.get(AppConstants.ARGUMENTS));
						}
					} catch (InvalidMap l_e) {
						d_mapService.resetMap(d_gameState, l_map.get(AppConstants.ARGUMENTS));
					}
				} else {
					throw new InvalidCommand(AppConstants.INVALID_LOADMAP_CMD_ERROR);
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void executeMapEdit(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		if (l_operations_list == null || l_operations_list.isEmpty()) {
			throw new InvalidCommand(AppConstants.INVALID_EDITMAP_CMD_ERROR);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)) {
					d_mapService.editMap(d_gameState, l_map.get(AppConstants.ARGUMENTS));
				} else {
					throw new InvalidCommand(AppConstants.INVALID_EDITMAP_CMD_ERROR);
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void executeEditContinent(Command p_command, Player p_player)
			throws IOException, InvalidCommand, InvalidMap {
		if (!l_mapLoadSuccess) {
			d_gameEngineCtx.setD_gameEngineCtxLog("Edit Continent command failed, initiate with editmap",
					AppConstants.ORDER_EFFECT);
			return;
		}

		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		if (l_operations_list == null || l_operations_list.isEmpty()) {
			throw new InvalidCommand(AppConstants.INVALID_EDIT_CONTINENT_CMD_ERROR);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)
						&& p_command.checkRequiredKeysPresent(AppConstants.OPERATION, l_map)) {
					d_mapService.editFunctions(d_gameState, l_map.get(AppConstants.OPERATION),
							l_map.get(AppConstants.ARGUMENTS), "Continent");
				} else {
					throw new InvalidCommand(AppConstants.INVALID_EDIT_CONTINENT_CMD_ERROR);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void executeEditCountry(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {

		if (!l_mapLoadSuccess) {
			d_gameEngineCtx.setD_gameEngineCtxLog("Edit Country command failed, initiate with editmap",
					AppConstants.ORDER_EFFECT);
			return;
		}

		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
		if (null == l_operations_list || l_operations_list.isEmpty()) {
			throw new InvalidCommand(AppConstants.INVALID_EDITCOUNTRY_CMD_ERROR);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)
						&& p_command.checkRequiredKeysPresent(AppConstants.OPERATION, l_map)) {
					d_mapService.editFunctions(d_gameState, l_map.get(AppConstants.OPERATION),
							l_map.get(AppConstants.ARGUMENTS), "country");
				} else {
					throw new InvalidCommand(AppConstants.INVALID_EDITCOUNTRY_CMD_ERROR);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void executeEditNeighbour(Command p_command, Player p_player)
			throws IOException, InvalidCommand, InvalidMap {
		if (!l_mapLoadSuccess) {
			d_gameEngineCtx.setD_gameEngineCtxLog("Edit Neighbour command failed, initiate with editmap",
					AppConstants.ORDER_EFFECT);
			return;
		}
		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
		if (null == l_operations_list || l_operations_list.isEmpty()) {
			throw new InvalidCommand(AppConstants.INVALID_EDITNEIGHBOUR_CMD_ERROR);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)
						&& p_command.checkRequiredKeysPresent(AppConstants.OPERATION, l_map)) {
					d_mapService.editFunctions(d_gameState, l_map.get(AppConstants.OPERATION),
							l_map.get(AppConstants.ARGUMENTS), "neighbour");
				} else {
					throw new InvalidCommand(AppConstants.INVALID_EDITNEIGHBOUR_CMD_ERROR);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void executeValidateMap(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
		if (!l_mapLoadSuccess) {
			d_gameEngineCtx.setD_gameEngineCtxLog("validatemap failed as no map found, Initiate loadmap & editmap",
					AppConstants.ORDER_EFFECT);
			return;
		}
		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
		if (null == l_operations_list || l_operations_list.isEmpty()) {
			Models.Map l_currentMap = d_gameState.getD_map();
			if (l_currentMap == null) {
				throw new InvalidMap(AppConstants.CMD_MAP_ERROR_EMPTY);
			} else {
				if (l_currentMap.Validate()) {
					System.out.println(AppConstants.VALID_MAP);
				} else {
					System.out.println("Failed to Validate map!");
				}
			}
		} else {
			throw new InvalidCommand(AppConstants.INVALID_VALIDATEMAP_CMD_ERROR);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void executeSaveMap(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
		if (!l_mapLoadSuccess) {
			d_gameEngineCtx.setD_gameEngineCtxLog("savemap failed as no map found, Initiate editmap",
					AppConstants.ORDER_EFFECT);
			return;
		}
		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		if (null == l_operations_list || l_operations_list.isEmpty()) {
			throw new InvalidCommand(AppConstants.INVALID_SAVEMAP_CMD_ERROR);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)) {
					boolean l_fileUpdateStatus = d_mapService.saveMap(d_gameState,
							l_map.get(AppConstants.ARGUMENTS));
					if (l_fileUpdateStatus)
						System.out.println("Required changes has been done in map file");
					else
						System.out.println(d_gameState.getError());
				} else {
					throw new InvalidCommand(AppConstants.INVALID_SAVEMAP_CMD_ERROR);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void executeGameplayer(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
		if (!l_mapLoadSuccess) {
			d_gameEngineCtx.setD_gameEngineCtxLog("No map found, Please loadmap before adding game players",
					AppConstants.ORDER_EFFECT);
			return;
		}
		List<Map<String, String>> l_operationArgList = p_command.getOperationsAndArguments();

		if (CommonCode.isCollectionEmpty(l_operationArgList)) {
			throw new InvalidCommand(AppConstants.GAMEPLAYER_ERROR_MSG);
		} else {
			if (d_gameState.getD_loadMapCommand()) {
				for (Map<String, String> l_map : l_operationArgList) {
					if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)
							&& p_command.checkRequiredKeysPresent(AppConstants.OPERATION, l_map)) {
						d_playerService.updatePlayers(d_gameState, l_map.get(AppConstants.OPERATION),
								l_map.get(AppConstants.ARGUMENTS));
					} else {
						throw new InvalidCommand(AppConstants.GAMEPLAYER_ERROR_MSG);
					}
				}
			} else {
				d_gameEngineCtx.setD_gameEngineCtxLog("Please load a valid map first via loadmap command!",
						AppConstants.ORDER_EFFECT);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void executeAssignCountries(Command p_command, Player p_player, boolean p_istournamentmode,
			GameState p_gameState) throws InvalidCommand {
		// List<Map<String, String>> l_operationArgList =
		// p_command.getOperationsAndArguments();
		// if (!CommonCode.isCollectionEmpty(l_operationArgList)) {
		// throw new InvalidCommand(AppConstants.ASSIGNCOUNTRIES_ERROR_MSG);
		// }

		// // Initialize countries and continents allocation
		// d_playerService.allocCountriesAndContinents(d_gameState);
		// d_playerService.allocColors(d_gameState);
		// d_playerService.allocArmies(d_gameState);
		// d_gameEngineCtx.setGamePlayPhase(AppConstants.ISSUE_ORDER_PHASE);
		if (p_gameState.getD_loadMapCommand()) {
			List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
			Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
			if (CommonCode.isCollectionEmpty(l_operations_list) || p_istournamentmode) {
				d_gameEngineCtx.setD_gameState(p_gameState);
				d_gameEngineCtx.setD_isTournamentMode(p_istournamentmode);
				d_playerService.allocCountriesAndContinents(p_gameState);
				d_playerService.allocColors(p_gameState);
				d_playerService.allocArmies(p_gameState);
				d_gameEngineCtx.setGamePlayPhase(AppConstants.ISSUE_ORDER_PHASE, p_istournamentmode);
			} else {
				throw new InvalidCommand(AppConstants.ASSIGNCOUNTRIES_ERROR_MSG);
			}
		} else {
			d_gameEngineCtx.setD_gameEngineCtxLog("Please load a valid map first via loadmap command!",
					AppConstants.ORDER_EFFECT);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void executeLoadGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
		List<java.util.Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		if (l_operations_list == null || l_operations_list.isEmpty()) {
			throw new InvalidCommand(AppConstants.INVALID_LOADGAME_CMD_ERROR);
		}

		for (Map<String, String> l_map : l_operations_list) {
			if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)) {
				String l_filename = l_map.get(AppConstants.ARGUMENTS);

				try {
					GamePlayPhase l_phase = GameService.loadGame(l_filename);

					this.d_gameEngineCtx.loadPhase(l_phase);
				} catch (ClassNotFoundException l_e) {
					l_e.printStackTrace();
				}
			}
		}
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
	protected void executetournamentGamePlay(Command p_command) throws InvalidCommand, InvalidMap {

		if (d_gameState.getD_playerList() != null && d_gameState.getD_playerList().size() > 1) {
			List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
			boolean l_parsingSuccessful = false;
			Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

			if (CommonCode.isCollectionEmpty(l_operations_list)
					&& !d_tournament.mandatoryTournamentArgPresent(l_operations_list, p_command)) {
				throw new InvalidCommand(AppConstants.INVALID_TOURNAMENT_CMD_ERROR);
			} else {
				for (Map<String, String> l_map : l_operations_list) {
					if (p_command.checkRequiredKeysPresent(AppConstants.ARGUMENTS, l_map)
							&& p_command.checkRequiredKeysPresent(AppConstants.OPERATION, l_map)) {
						l_parsingSuccessful = d_tournament.parseTournamentCommand(d_gameState,
								l_map.get(AppConstants.OPERATION), l_map.get(AppConstants.ARGUMENTS),
								d_gameEngineCtx);
						if (!l_parsingSuccessful)
							break;

					} else {
						throw new InvalidCommand(AppConstants.INVALID_TOURNAMENT_CMD_ERROR);
					}
				}
			}
			if (l_parsingSuccessful) {
				System.out.println("Parsing Succesful");
				for (GameState l_gameState : d_tournament.getD_gameStateList()) {
					d_gameEngineCtx.setD_gameEngineCtxLog(
							"\nStarting New Game on map : " + l_gameState.getD_map().getD_mapFile() + " .........\n",
							AppConstants.ORDER_EFFECT);
					executeAssignCountries(new Command("assigncountries"), null, true, l_gameState);

					d_gameEngineCtx.setD_gameEngineCtxLog(
							"\nGame Completed on map : " + l_gameState.getD_map().getD_mapFile() + " .........\n",
							AppConstants.ORDER_EFFECT);
				}
				d_gameEngineCtx.setD_gameEngineCtxLog("************ Tournament Completed ************",
						AppConstants.ORDER_EFFECT);
				TournamentView l_tournamentView = new TournamentView(d_tournament);
				l_tournamentView.viewTournament();
			}
		} else {
			d_gameEngineCtx.setD_gameEngineCtxLog("Please add 2 or more players first in the game.",
					AppConstants.ORDER_EFFECT);
		}
	}

}