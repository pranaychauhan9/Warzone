package TestSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suit for running all test suites involved in this game project
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
@SuiteClasses({ ControllerTestSuite.class, ModelTestSuite.class, ServicesTestSuite.class })
public class RunTestSuite {
}