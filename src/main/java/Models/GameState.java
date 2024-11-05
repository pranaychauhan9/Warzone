package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Models.LogModel.LogEntryBuffer;

/**
 * This class is used to test functionality of GameState class functions.
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
public class GameState implements Serializable {

	/**
	 * map object.
	 */
	Map d_map;

	/**
	 * error message.
	 */
	String d_error;

	/**
	 * Confirms whether the 'load' command has been executed by the user.
	 */
	Boolean d_loadmapCommand = false;

	/**
	 * Holds list of player.
	 */
	List<Player> d_playerList;

	/**
	 * Log entry buffer for current game state.
	 */
	LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();

	/**
	 * Number of turns in tournament.
	 */
	int d_maxnumberofturns = 0;

	/**
	 * Number of remaining turns in tournament.
	 */
	int d_numberOfTurnsLeft = 0;

	/**
	 * Maintains list of players lost in the game.
	 */
	List<Player> d_playersFailed = new ArrayList<Player>();

	/**
	 * Winner Player.
	 */
	Player d_winner;

	/**
	 * establish a getter method to get the map.
	 * 
	 * @return map object
	 */
	public Map getD_map() {
		return d_map;
	}

	/**
	 * establish a setter method to set the map.
	 * 
	 * @param p_map map object
	 */
	public void setD_map(Map p_map) {
		this.d_map = p_map;
	}

	/**
	 * establish a getter method to get the error message.
	 * 
	 * @return error message
	 */
	public String getError() {
		return d_error;
	}

	/**
	 * establish a setter method to set the error message.
	 * 
	 * @param p_error error message
	 */
	public void setError(String p_error) {
		this.d_error = p_error;
	}

	/**
	 * Add log message to log buffer.
	 *
	 * @param p_logMsg  Message to be added in log
	 * @param p_logType Type of Message to be Added
	 */
	public void updateLogFile(String p_logMsg, String p_logType) {
		d_logEntryBuffer.setLogMsg(p_logMsg, p_logType);
	}

	/**
	 * Get the Log in current GameState.
	 *
	 * @return Log Message
	 */
	public String getLog() {
		return d_logEntryBuffer.getD_logMessage();
	}

	/**
	 * Get the list of players.
	 * 
	 * @return list of players
	 */
	public List<Player> getD_playerList() {
		return d_playerList;
	}

	/**
	 * Set the players list.
	 * 
	 * @param p_playerList players list
	 */
	public void setD_players(List<Player> p_playerList) {
		this.d_playerList = p_playerList;
	}

	/**
	 * This assigns a boolean variable named 'loadMap'.
	 */
	public void setD_loadMapCommand() {
		this.d_loadmapCommand = true;
	}

	/**
	 * Determines whether the 'load' command has been used.
	 *
	 * @return bool value if map is loaded
	 */
	public boolean getD_loadMapCommand() {
		return this.d_loadmapCommand;
	}

	/**
	 * 
	 * The message that should be included in the log.
	 *
	 * @param p_logMessage Log Message to be set in the Object
	 * @param p_logType    Type of Log Message to be Added
	 */
	public void updateLog(String p_logMessage, String p_logType) {
		d_logEntryBuffer.setLogMsg(p_logMessage, p_logType);
	}

	/**
	 * Fetches the most recent Log in current GameState.
	 *
	 * @return recent Log Message
	 */
	public String getRecentLog() {
		return d_logEntryBuffer.getD_logMessage();
	}

	/**
	 * Returns max number of turns allowed in tournament.
	 * 
	 * @return int number of turns
	 */
	public int getD_maxnumberofturns() {
		return d_maxnumberofturns;
	}

	/**
	 * Sets max number of turns allowed in tournament.
	 * 
	 * @param d_maxnumberofturns number of turns
	 */
	public void setD_maxnumberofturns(int d_maxnumberofturns) {
		this.d_maxnumberofturns = d_maxnumberofturns;
	}

	/**
	 * Gets number of turns left at any stage of tournament.
	 * 
	 * @return int number of remaining turns
	 */
	public int getD_numberOfTurnsLeft() {
		return d_numberOfTurnsLeft;
	}

	/**
	 * Sets number of turns left at any stage of tournament.
	 * 
	 * @param d_numberOfTurnsLeft number of remaining turns
	 */
	public void setD_numberOfTurnsLeft(int d_numberOfTurnsLeft) {
		this.d_numberOfTurnsLeft = d_numberOfTurnsLeft;
	}

	/**
	 * Adds the Failed Player in GameState.
	 *
	 * @param p_player player instance to remove
	 */
	public void removePlayer(Player p_player) {
		d_playersFailed.add(p_player);
	}

	/**
	 * Retrieves the list of failed players.
	 *
	 * @return List of Players that lost game.
	 */
	public List<Player> getD_playersFailed() {
		return d_playersFailed;
	}

	/**
	 * Sets the winner player object.
	 *
	 * @param p_player winner player object
	 */
	public void setD_winner(Player p_player) {
		d_winner = p_player;
	}

	/**
	 * Returns the winner player object.
	 *
	 * @return returns winning player
	 */
	public Player getD_winner() {
		return d_winner;
	}
}
