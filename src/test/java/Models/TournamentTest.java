package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import Controllers.GameEngineCtx;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;

public class TournamentTest {

        /**
         * First Player.
         */
        Player d_firstPlayer;

        /**
         * Second Player.
         */
        Player d_secondPlayer;

        /**
         * Game State.
         */
        GameState d_gameState;

        /**
         * Game engine.
         */
        GameEngineCtx d_gameEngineCtx;

        /**
         * Setup.
         * 
         * @throws InvalidMap Invalid Map
         */
        @Before
        public void setup() throws InvalidMap {
                d_gameEngineCtx = new GameEngineCtx();
                d_gameState = new GameState();
                d_firstPlayer = new Player("a");
                d_secondPlayer = new Player("b");

                // Set strategy
                d_firstPlayer.setStrategy(new AggressivePlayer());
                d_secondPlayer.setStrategy(new RandomPlayer());

                d_gameState.setD_players(Arrays.asList(d_firstPlayer, d_secondPlayer));
        }

        /**
         * Checks valid tournament commands.
         * 
         * @throws InvalidCommand invalid command passed
         * @throws InvalidMap     invalid map name passed
         */
        @Test
        public void testTournamentMode() throws InvalidMap, InvalidCommand {
                Tournament l_tournament = new Tournament();
                GameEngineCtx l_gameEngine = new GameEngineCtx();
                l_tournament.parseTournamentCommand(d_gameState, "M",
                                "canada", l_gameEngine);
                l_tournament.parseTournamentCommand(d_gameState, "P",
                                "Aggressive Random", l_gameEngine);
                l_tournament.parseTournamentCommand(d_gameState, "G",
                                "3", l_gameEngine);
                l_tournament.parseTournamentCommand(d_gameState, "D",
                                "11", l_gameEngine);

                assertEquals(l_tournament.getD_gameStateList().size(), 3);
                assertEquals(l_tournament.getD_gameStateList().get(0).getD_map().getD_mapFile(), "canada");

                assertEquals(l_tournament.getD_gameStateList().get(0).getD_playerList().size(), 2);
                assertEquals(l_tournament.getD_gameStateList().get(0).getD_maxnumberofturns(), 11);

        }

        /**
         * Tests handling of tournament invalid player strategies arguments.
         * 
         * @throws InvalidCommand invalid command passed
         * @throws InvalidMap     invalid map name passed
         */
        @Test
        public void testInvalidPlayerStrategies() throws InvalidMap, InvalidCommand {
                Tournament l_tournament = new Tournament();
                assertFalse(l_tournament.parseTournamentCommand(d_gameState, "P",
                                "Random Human", new GameEngineCtx()));
        }

        /**
         * Tests tournament for invalid game arguments.
         * 
         * @throws InvalidCommand invalid command passed
         * @throws InvalidMap     invalid map name passed
         */
        @Test
        public void testInvalidNumberOfGames() throws InvalidMap, InvalidCommand {
                Tournament l_tournament = new Tournament();
                assertFalse(l_tournament.parseTournamentCommand(d_gameState, "G",
                                "8", new GameEngineCtx()));
        }

        /**
         * Tests tournament for invalid turns.
         * 
         * @throws InvalidCommand invalid command passed
         * @throws InvalidMap     invalid map name passed
         */
        @Test
        public void testInvalidNumberOfTurns() throws InvalidMap, InvalidCommand {
                Tournament l_tournament = new Tournament();
                assertFalse(l_tournament.parseTournamentCommand(d_gameState, "D",
                                "52", new GameEngineCtx()));
        }

        /**
         * Tests tournament for invalid map arguments.
         * 
         * @throws InvalidCommand invalid command passed
         * @throws InvalidMap     invalid map name passed
         */
        @Test
        public void testInvalidMap() throws InvalidMap, InvalidCommand {
                Tournament l_tournament = new Tournament();
                assertFalse(l_tournament.parseTournamentCommand(d_gameState, "M",
                                "canada europe test-continent-subgraph test-map test-wholemap testconquest",
                                new GameEngineCtx()));
        }

}
