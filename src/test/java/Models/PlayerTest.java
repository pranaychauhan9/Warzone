package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * This class is used to test functionality of player class.
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
public class PlayerTest {

    /**
     * Order List
     */
    List<Order> d_order_list = new ArrayList<Order>();

    /**
     * Player object
     */
    Player d_player = new Player();

    /**
     * Current State of the Game.
     */
    GameState d_gameState = new GameState();

    /**
     * Set up the test environment by adding sample players to the registered player
     * list.
     */
    @Before
    public void setup() {
        d_player = new Player("Sumit");
    }

    /**
     * Test the Player's next order processing logic.
     * It creates and adds multiple orders to a player's order list, then validates
     * the processing of the next order.
     */
    @Test
    public void testPlayerNextOrderOrder() {
        Order l_deployOrder1 = new Deploy(d_player, "Russia", 7);
        Order l_deployOrder2 = new Deploy(d_player, "Ukraine", 8);
        Order l_deployOrder3 = new Deploy(d_player, "India", 9);

        d_order_list.add(l_deployOrder1);
        d_order_list.add(l_deployOrder2);
        d_order_list.add(l_deployOrder3);

        d_player.setD_playerOrder(d_order_list);
        assertEquals(3, d_player.getD_playerOrder().size());

        Order l_order = d_player.next_order();
        assertEquals(l_deployOrder1, l_order);
        assertEquals(2, d_player.getD_playerOrder().size());
    }

    /**
     * Test case to ensure that deployment exceeds the reinforcement pool.
     */
    @Test
    public void testMaxDeploymentExceedsReinforcementPool() {
        d_player.setD_unallocatedArmyCount(10);
        String l_noOfArmiesT1 = "5";
        assertFalse(d_player.checkDeployArmyCount(d_player, l_noOfArmiesT1));

        String l_noOfArmiesT2 = "15";
        assertTrue(d_player.checkDeployArmyCount(d_player, l_noOfArmiesT2));
    }

    /**
     * Checks whether armies are updated after deploy order execution.
     */
    @Test
    public void testInitDeployOrder() {
        d_player.setD_unallocatedArmyCount(20);
        d_player.initDeployOrder("Deploy India 5");
        assertEquals(d_player.getD_unallocatedArmyCount().toString(), "15");
        assertEquals(d_player.getD_playerOrder().size(), 1);
    }

}
