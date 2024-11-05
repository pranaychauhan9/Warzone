package TestSuites;

import Services.ConquestMapTest;
import Services.DominationMapTest;
import Services.MapServiceTest;
import Services.PlayerServiceTest;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class represents a test suite that includes multiple test classes
 * related to map-related functionalities.
 * Specifically, it includes tests from the MapTest and MapServiceTest classes.
 * The test suite allows for the execution of multiple related tests together.
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
@SuiteClasses({ MapServiceTest.class, PlayerServiceTest.class, DominationMapTest.class, ConquestMapTest.class })
public class ServicesTestSuite {

}
