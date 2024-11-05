package Controllers;

import Models.GameState;
import Models.IssueOrderPhase;
import Models.OrderExecutionPhase;
import Models.GamePlayPhase;
import Models.StartUpPhase;

import java.io.Serializable;

import Constants.AppConstants;

/**
 * This is the entry point of the Game and keeps the track of current Game
 * State.
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
public class GameEngineCtx implements Serializable {

	/**
	 * d_gameState stores the information about current GamePlay.
	 */
	GameState d_gameState = new GameState();

	/**
	 * Gets current state of the game.
	 * 
	 * @return state of the game
	 */
	public GameState getD_gameState() {
		return d_gameState;
	}

	/**
	 * Sets state of the game.
	 * 
	 * @param p_gameState state of the game
	 */
	public void setD_gameState(GameState p_gameState) {
		this.d_gameState = p_gameState;
	}

	/**
	 * This represents the current game play phase according to the state pattern.
	 */
	GamePlayPhase d_currentPhase = new StartUpPhase(this, d_gameState);

	/**
	 * Tournament mode or single game mode.
	 */
	static boolean d_isTournamentMode = false;

	/**
	 * Tournament mode information.
	 * 
	 * @return true if tournament is being played or else false
	 */
	public boolean isD_isTournamentMode() {
		return d_isTournamentMode;
	}

	/**
	 * Sets tournament mode information.
	 * 
	 * @param p_isTournamentMode true if tournament is being played or else false
	 */
	public void setD_isTournamentMode(boolean p_isTournamentMode) {
		GameEngineCtx.d_isTournamentMode = p_isTournamentMode;
	}

	/**
	 * This method serves as a getter for the current phase of the Game Context.
	 *
	 * @return current Phase of Game Context
	 */
	public GamePlayPhase getD_CurrentPhase() {
		return d_currentPhase;
	}

	/**
	 * It is utilized for updating the context.
	 *
	 * @param p_phase new Phase to set in Game context
	 */
	public void setD_CurrentPhase(GamePlayPhase p_phase) {
		d_currentPhase = p_phase;
	}

	/**
	 * Handle load game feature by setting phase from Object stream.
	 *
	 * @param p_phase new Phase to set in Game context
	 */
	public void loadPhase(GamePlayPhase p_phase) {
		d_currentPhase = p_phase;
		d_gameState = p_phase.getD_gameState();
		getD_CurrentPhase().initPhase(d_isTournamentMode);
	}

	/**
	 * Handles logging for game engine context class.
	 *
	 * @param p_gameEngineCtxLog Log message.
	 * @param p_logType          Type of Log.
	 */
	public void setD_gameEngineCtxLog(String p_gameEngineCtxLog, String p_logType) {
		d_currentPhase.getD_gameState().updateLogFile(p_gameEngineCtxLog, p_logType);
		String l_consoleLogger = p_logType.equals(AppConstants.GAMEPLAY_PHASE)
				? "\n************ " + p_gameEngineCtxLog + " ************\n"
				: p_gameEngineCtxLog;
		System.out.println(l_consoleLogger);
	}

	/**
	 * This method transitions the current phase to the given phase in
	 * accordance with the State Pattern.
	 * 
	 * @param p_gamePlayPhase    new Phase to set in Game context
	 * @param p_isTournamentMode tornament mode flag.
	 */
	public void setGamePlayPhase(String p_gamePlayPhase, boolean p_isTournamentMode) {
		switch (p_gamePlayPhase) {
			case AppConstants.START_UP_PHASE:
				this.setD_gameEngineCtxLog("Startup Phase", AppConstants.GAMEPLAY_PHASE);
				setD_CurrentPhase(new StartUpPhase(this, d_gameState));
				getD_CurrentPhase().initPhase(d_isTournamentMode);
				break;
			case AppConstants.ISSUE_ORDER_PHASE:
				this.setD_gameEngineCtxLog("Issue Order Phase", AppConstants.GAMEPLAY_PHASE);
				setD_CurrentPhase(new IssueOrderPhase(this, d_gameState));
				getD_CurrentPhase().initPhase(p_isTournamentMode);
				break;
			case AppConstants.ORDER_EXECUTION_PHASE:
				this.setD_gameEngineCtxLog("Order Execution Phase", AppConstants.GAMEPLAY_PHASE);
				setD_CurrentPhase(new OrderExecutionPhase(this, d_gameState));
				getD_CurrentPhase().initPhase(d_isTournamentMode);
				break;
			default:
				break;
		}

	}

	/**
	 * The primary method responsible for receiving user commands and directing them
	 * to the appropriate logical processes.The primary method responsible for
	 * receiving user commands and directing them to the appropriate logical
	 * processes.
	 *
	 * @param p_args the program doesn't use default command line arguments
	 */
	public static void main(String[] p_args) {
		GameEngineCtx l_gameEngineCtx = new GameEngineCtx();
		l_gameEngineCtx.getD_CurrentPhase().getD_gameState()
				.updateLogFile("STARTING WARZONE.........." + System.lineSeparator(), AppConstants.START_GAME);
		l_gameEngineCtx.setGamePlayPhase(AppConstants.START_UP_PHASE, false);
		l_gameEngineCtx.getD_CurrentPhase().initPhase(d_isTournamentMode);
	}

}