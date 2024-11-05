package Controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.GamePlayPhase;
import Models.GameState;
import Services.GameService;
import Models.StartUpPhase;

/**
 * This class is used to test functionality of game engine context class.
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
public class GameEngineCtxTest {

    /**
     * object of GameEngineCtx class.
     */
    GameEngineCtx d_gameEngineCtx;

    /**
     * setup before each test case.
     */
    @Before
    public void setup() {
        d_gameEngineCtx = new GameEngineCtx();
    }

    /**
     * Test to validate correct startup phase.
     */
    @Test
    public void testValidateStartupPhase() {
        assertTrue(d_gameEngineCtx.getD_CurrentPhase() instanceof StartUpPhase);
    }

    /**
     * Tests valid savegame command.
     *
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     * @throws IOException    Exception
     */
    @Test
    public void testValidSaveGame() throws InvalidCommand, InvalidMap, IOException {
        GamePlayPhase l_phase = d_gameEngineCtx.getD_CurrentPhase();
        l_phase.handleCommand("savegame hello.txt", null);
        GameState l_state = l_phase.getD_gameState();

        assertEquals("Log: Game Saved Successfully to hello.txt" + System.lineSeparator(),
                l_state.getRecentLog());

    }

    /**
     * Tests invalid savegame command.
     *
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     * @throws IOException    Exception
     */
    @Test
    public void testInvalidSaveGame() throws InvalidCommand, InvalidMap, IOException {
        GamePlayPhase l_phase = d_gameEngineCtx.getD_CurrentPhase();
        l_phase.handleCommand("save game hello.txt", null);
        GameState l_state = l_phase.getD_gameState();

        assertEquals(System.lineSeparator() + "Command Entered: save game hello.txt 1" + System.lineSeparator(),
                l_state.getRecentLog());

    }

    /**
     * Tests valid loadgame command.
     *
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     * @throws IOException    Exception
     */
    @Test
    public void testValidLoadGame() throws InvalidCommand, InvalidMap, IOException, ClassNotFoundException {
        GamePlayPhase l_phase = Services.GameService.loadGame("hello.txt");
        assertTrue(l_phase instanceof StartUpPhase);

    }

    /**
     * Tests invalid loadgame command.
     *
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     * @throws IOException    Exception
     */
    @Test(expected = FileNotFoundException.class)
    public void testinvalidLoadGame()
            throws InvalidCommand, InvalidMap, IOException, ClassNotFoundException {
        GamePlayPhase l_phase = Services.GameService.loadGame("12333");
    }

}
