package TestSuites;

import Models.DeployTest;
import Models.DiplomacyTest;
import Models.MapTest;
import Models.PlayerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Models.AdvanceTest;
import Models.AggressivePlayer;
import Models.AggressivePlayerTest;
import Models.AirliftTest;
import Models.BenevolentPlayerTest;
import Models.BlockadeTest;

/**
 * Test suite for testing issue and execution of order functionality and
 * various player services of adding players, assigning armies and countries
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
@RunWith(Suite.class)
@SuiteClasses({ AdvanceTest.class, DeployTest.class,
        MapTest.class, PlayerTest.class, AirliftTest.class, BlockadeTest.class, DiplomacyTest.class,
        AggressivePlayerTest.class, BenevolentPlayerTest.class })
public class ModelTestSuite {
}