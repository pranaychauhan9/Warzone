package CommonFunctions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.junit.Test;

/**
 * This class is used to test functionality of Command module.
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
public class CommandTests {

    /**
     * Test whether root command is fetched successfully
     */
    @Test
    public void testFetchRootCommand() {
        Command l_command = new Command("gameplayer -add sumit");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("gameplayer", l_rootCommand);
    }

    /**
     * Test method for verifying the extraction of operations and arguments from a
     * Command object.
     * This test creates a Command object with a specific command string,
     * "gameplayer -add sumit",
     * and then calls the getOperationsAndArguments method to retrieve the
     * operations and arguments.
     * It compares the actual result to the expected result to ensure the correct
     * extraction of values.
     */
    @Test
    public void testGetOperationsAndArguments() {
        Command l_command = new Command("gameplayer -add sumit");
        List<Map<String, String>> l_actualCommandList = l_command.getOperationsAndArguments();
        List<Map<String, String>> l_expectedCommandList = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandVal = new HashMap<String, String>() {
            {
                put("arguments", "sumit");
                put("operation", "add");
            }
        };
        l_expectedCommandList.add(l_expectedCommandVal);
        assertEquals(l_expectedCommandList, l_actualCommandList);
    }

}
