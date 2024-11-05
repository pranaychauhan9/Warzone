package Views;

import java.util.List;

import org.davidmoten.text.utils.WordWrap;

import Constants.AppConstants;
import Models.GameState;
import Models.Tournament;

/**
 * Tournament view class Implementation.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class TournamentView {

    /**
     * Tournament Object to be displayed.
     */
    Tournament d_tournament;

    /**
     * List of GameState Objects from tournament.
     */
    List<GameState> d_gameStateObjects;

    /**
     * Reset Color ANSI Code.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constructor setting for tournament object.
     *
     * @param p_tournament tournament object
     */
    public TournamentView(Tournament p_tournament) {
        d_tournament = p_tournament;
        d_gameStateObjects = d_tournament.getD_gameStateList();
    }

    /**
     * Returns the Colored String.
     *
     * @param p_color Color to be changed to.
     * @param p_s     String to be changed color of.
     * @return colored string.
     */
    private String getColorizedString(String p_color, String p_s) {
        if (p_color == null)
            return p_s;

        return p_color + p_s + ANSI_RESET;
    }

    /**
     * Renders the Center String for Heading.
     *
     * @param p_width Defined width in formatting.
     * @param p_s     String to be rendered.
     */
    private void displayCenteredText(int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width + "s",
                String.format("%" + (p_s.length() + (p_width - p_s.length()) / 2) + "s", p_s));

        System.out.format(l_centeredString + "\n");
    }

    /**
     * Renders the Separator for heading.
     *
     */
    private void displaySeparator() {
        StringBuilder l_separator = new StringBuilder();

        for (int i = 0; i < AppConstants.CONSOLE_WIDTH - 2; i++) {
            l_separator.append("-");
        }
        System.out.format("+%s+%n", l_separator.toString());
    }

    /**
     * Renders the name of Map File and Game Number.
     *
     * @param p_gameIndex game Index
     * @param p_mapName   map name
     */
    private void displayMapName(Integer p_gameIndex, String p_mapName) {
        String l_formattedString = String.format("%s %s %d %s", p_mapName, " (Game Number: ", p_gameIndex, " )");
        displaySeparator();
        displayCenteredText(AppConstants.CONSOLE_WIDTH, l_formattedString);
        displaySeparator();
    }

    /**
     * Renders info of each game.
     *
     * @param p_gameState gamestate object.
     */
    private void displayGames(GameState p_gameState) {
        String l_winner;
        String l_summary;
        if (p_gameState.getD_winner() == null) {
            l_winner = " ";
            l_summary = "Draw!";
        } else {
            System.out.println("Entered Here");
            l_winner = p_gameState.getD_winner().getD_playerName();
            l_summary = "Winning Player Strategy: " + p_gameState.getD_winner().getD_playerBehaviorStrategy();
        }
        String l_winnerString = String.format("%s %s", "Winner -> ", l_winner);
        StringBuilder l_commaSeparatedPlayers = new StringBuilder();

        for (int i = 0; i < p_gameState.getD_playersFailed().size(); i++) {
            l_commaSeparatedPlayers.append(p_gameState.getD_playersFailed().get(i).getD_playerName());
            if (i < p_gameState.getD_playersFailed().size() - 1)
                l_commaSeparatedPlayers.append(", ");
        }
        String l_losingPlayers = "Losing Players -> "
                + WordWrap.from(l_commaSeparatedPlayers.toString()).maxWidth(AppConstants.CONSOLE_WIDTH).wrap();
        String l_summaryString = String.format("%s %s", "Conclusion of Game -> ", l_summary);
        System.out.println(l_winnerString);
        System.out.println(l_losingPlayers);
        System.out.println(l_summaryString);
    }

    /**
     * Renders the View of tournament results.
     */
    public void viewTournament() {
        int l_counter = 0;
        System.out.println();
        if (d_tournament != null && d_gameStateObjects != null) {
            for (GameState l_gameState : d_tournament.getD_gameStateList()) {
                l_counter++;
                displayMapName(l_counter, l_gameState.getD_map().getD_mapFile());
                displayGames(l_gameState);
            }
        }
    }

}
