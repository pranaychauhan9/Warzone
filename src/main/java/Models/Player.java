package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Constants.AppConstants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import CommonFunctions.CommonCode;

/**
 * This model class manages all the players in the game.
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
public class Player implements Serializable {

    /**
     * Player color.
     */
    private String d_playerColor;

    /**
     * player name.
     */
    private String d_playerName;

    /**
     * player orders.
     */
    List<Order> d_playerOrder;

    /*
     * player owned countries.
     */
    List<Country> d_playerCountries;

    /*
     * player owned continents.
     */
    List<Continent> d_playerContinents;

    /*
     * Count of armies.
     */
    Integer d_unallocatedArmyCount;

    /**
     * Additional orders to be accepted for player.
     */
    boolean d_additionalOrders;

    /**
     * Keeps log messages of individual players.
     */
    String d_playerLogMessage;

    /**
     * If the card per turn has already been assigned, it is currently set to false.
     */
    boolean d_allowOnlyOneCardPerTurn = false;

    /**
     * Name of the card Player owns.
     */
    List<String> d_playerOwnedCardList = new ArrayList<String>();

    /**
     * List of players to not attack if negotiated with.
     */
    List<Player> d_negotiatedWith = new ArrayList<Player>();

    /**
     * Object of Player Behavior Strategy class.
     */
    PlayerBehaviorStrategy d_playerBehaviorStrategy;

    /**
     * Contructor to generate player and give him a name and 0 armies.
     * 
     * @param p_playerName A string representing the player's name to set.
     */
    public Player(String p_playerName) {
        this.d_playerName = p_playerName;
        this.d_unallocatedArmyCount = 0;
        this.d_playerCountries = new ArrayList<Country>();
        this.d_playerOrder = new ArrayList<>();
        this.d_additionalOrders = true;
    }

    /**
     * Empty Constructor.
     */
    public Player() {

    }

    /**
     * Get player color.
     * 
     * @return player color
     */
    public String getD_playerColor() {
        return d_playerColor;
    }

    /**
     * Set player color.
     * 
     * @param p_playerColor A string representing the player color to set.
     */
    public void setD_playerColor(String p_playerColor) {
        this.d_playerColor = p_playerColor;
    }

    /**
     * Get player name.
     * 
     * @return player name
     */
    public String getD_playerName() {
        return d_playerName;
    }

    /**
     * Set player name.
     * 
     * @param p_playerName A string representing the player's name to set.
     */
    public void setD_playerName(String p_playerName) {
        this.d_playerName = p_playerName;
    }

    /**
     * 
     * Get player order.
     * 
     * @return player orders
     */
    public List<Order> getD_playerOrder() {
        return d_playerOrder;
    }

    /**
     * Set player order.
     * 
     * @param d_playerOrder A List of OrderImpl objects representing the player's
     *                      orders to set.
     */
    public void setD_playerOrder(List<Order> d_playerOrder) {
        this.d_playerOrder = d_playerOrder;
    }

    /**
     * Get list of countries owned by player.
     * 
     * @return countries owned by individual player
     */
    public List<Country> getD_playerCountries() {
        return d_playerCountries;
    }

    /**
     * Set list of countries owned by player.
     * 
     * @param p_playerCountries A List of Country objects representing the countries
     *                          controlled by the player.
     */
    public void setD_playerCountries(List<Country> p_playerCountries) {
        this.d_playerCountries = p_playerCountries;
    }

    /**
     * Get list of continents owned by player.
     * 
     * @return continents owned by individual player
     */
    public List<Continent> getD_playerContinents() {
        return d_playerContinents;
    }

    /**
     * Set for list of continents owned by player.
     * 
     * @param p_playerContinents A List of Continent objects representing the
     *                           continents controlled by the player.
     */
    public void setD_playerContinents(List<Continent> p_playerContinents) {
        this.d_playerContinents = p_playerContinents;
    }

    /**
     * Get count of unallocated armies for a player.
     * 
     * @return count of unallocated armies
     */
    public Integer getD_unallocatedArmyCount() {
        return d_unallocatedArmyCount;
    }

    /**
     * Set count of unallocated armies for a player.
     * 
     * @param p_unallocatedArmyCount An Integer representing the number of
     *                               unallocated armies for the player.
     */
    public void setD_unallocatedArmyCount(Integer p_unallocatedArmyCount) {
        this.d_unallocatedArmyCount = p_unallocatedArmyCount;
    }

    /**
     * 
     * Provides a list of cards in the possession of the player.
     *
     * @return List of Strings with cards
     */
    public List<String> getD_playerOwnedCards() {
        return this.d_playerOwnedCardList;
    }

    /**
     * 
     * Establishes the boolean flag for the allocation of the Per Turn Card.
     *
     * @param p_value Bool to Set.
     */
    public void setD_allowOnlyOneCardPerTurn(Boolean p_value) {
        this.d_allowOnlyOneCardPerTurn = p_value;
    }

    /**
     * Sets the player log message and performs an action based on the log type.
     *
     * @param p_playerLogMessage The message to be set as the player log message.
     * @param p_logType          The type of log message (AppConstants.ERROR_LOG_MSG
     *                           or "log").
     */
    public void setD_playerLog(String p_playerLogMessage, String p_logType) {
        this.d_playerLogMessage = p_playerLogMessage;

        switch (p_logType) {
            case AppConstants.ERROR_LOG_MSG:
                System.err.println(p_playerLogMessage);
                break;
            case AppConstants.LOG_MSG:
                System.out.println(p_playerLogMessage);
                break;
            default:
                System.out.println("Unknown log type: " + p_logType);
                break;
        }
    }

    /**
     * Retrieves the player log message.
     *
     * @return The player log message as a string.
     */
    public String getD_playerLogMessage() {
        return this.d_playerLogMessage;
    }

    /**
     * Extracts the list of IDs of countries owned by the player.
     *
     * @return list of country Ids
     */
    public List<Integer> getCountryIDs() {
        List<Integer> l_countryIDs = new ArrayList<Integer>();
        for (Country c : d_playerCountries) {
            l_countryIDs.add(c.getD_countryId());
        }
        return l_countryIDs;
    }

    /**
     * It returns a string list of player owned countries.
     * 
     * @return list of player owned countries
     */
    public List<String> getCountryList() {
        List<String> l_countryNameList = new ArrayList<String>();

        try {
            if (d_playerCountries != null) {
                for (Country l_country : d_playerCountries) {
                    String l_countryName = l_country.getD_countryName();
                    if (l_countryName != null) {
                        l_countryNameList.add(l_countryName);
                    } else {
                        System.out.println("Error: Country name is null.");
                    }
                }
            } else {
                System.out.println("Error: Player doesn't own any countries.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return l_countryNameList;
    }

    /**
     * It returns a string list of player owned continents.
     * 
     * @return list of player owned continents.
     */
    public List<String> getContinentList() {
        List<String> l_continentNameList = new ArrayList<String>();

        try {
            if (d_playerContinents != null) {
                for (Continent c : d_playerContinents) {
                    String continentName = c.getD_continentName();
                    if (continentName != null) {
                        l_continentNameList.add(continentName);
                    } else {
                        System.out.println("Error: Continent name is null.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return l_continentNameList;
    }

    /**
     * Retrieves and removes the next order from the player's order queue.
     *
     * @return The next order in the player's order queue, or {@code null} if the
     *         queue is empty.
     */
    public Order next_order() {
        if (CommonCode.isCollectionEmpty(this.d_playerOrder)) {
            return null;
        }
        Order l_order = this.d_playerOrder.get(0);
        this.d_playerOrder.remove(l_order);
        return l_order;
    }

    /**
     * Obtain the player's order based on its strategy.
     *
     * @param p_gameState Current GameState Object
     * @return String representing Order
     * @throws IOException Exception
     */
    public String getPlayerOrder(GameState p_gameState) throws IOException {
        String l_stringOrder = this.d_playerBehaviorStrategy.generateOrder(this, p_gameState);
        return l_stringOrder;
    }

    /**
     * Returns player strategy object.
     *
     * @return player strategy
     */
    public PlayerBehaviorStrategy getD_playerBehaviorStrategy() {
        return d_playerBehaviorStrategy;
    }

    /**
     * Sets the strategy of the Player Behavior.
     * 
     * @param p_playerBehaviorStrategy object of PlayerBehaviorStrategy class
     */
    public void setStrategy(PlayerBehaviorStrategy p_playerBehaviorStrategy) {
        d_playerBehaviorStrategy = p_playerBehaviorStrategy;
    }

    /**
     * Determines if additional orders from the player should be accepted.
     *
     * @return boolean true if player wants to give more order or else false
     */
    public boolean getD_additionalOrders() {
        return d_additionalOrders;
    }

    /**
     * Sets flag on the basis of whether additional orders from the player be
     * accepted.
     *
     * @param p_additionalOrders additional orders required
     */
    public void setD_additionalOrders(boolean p_additionalOrders) {
        this.d_additionalOrders = p_additionalOrders;
    }

    /**
     * Issues an order on behalf of the player.
     *
     * @param p_issueOrderPhase current phase of the game
     * @throws InvalidMap     indicates map is invalid
     * @throws InvalidCommand indicates command is invalid
     * @throws IOException    indicates failure in I/O operation
     */
    public void issue_order(IssueOrderPhase p_issueOrderPhase) throws InvalidCommand, IOException, InvalidMap {
        p_issueOrderPhase.askForOrder(this);
    }

    /**
     * Checks if there are any additional orders for player in next turn.
     * 
     * @param p_isTournament Flag to indicate whether game is being run in
     *                       tournament mode
     *
     * @throws IOException exception in reading inputs from user
     */
    void checkAdditionalOrders(boolean p_isTournament) throws IOException {
        if (p_isTournament || !this.getD_playerBehaviorStrategy().getPlayerBehavior().equalsIgnoreCase("Human")) {
            Random l_random = new Random();
            System.out.println("Trying to execute next boolean logic");
            boolean l_moreOrders = l_random.nextBoolean();
            this.setD_additionalOrders(l_moreOrders);
        } else {
            BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nCommand for " + this.getD_playerName()
                    + ": Enter Y for additional order or N for No orders for the next turn : ");
            String l_orderInput = l_reader.readLine();
            if (l_orderInput.equalsIgnoreCase("Y")) {
                this.setD_additionalOrders(true);
            } else if (l_orderInput.equalsIgnoreCase("N")) {
                this.setD_additionalOrders(false);
            } else {
                System.err.println("Invalid Input Passed.");
                this.checkAdditionalOrders(p_isTournament);
            }
        }
    }

    /**
     * Checks if a player has enough unallocated armies to perform a deployment
     * action.
     *
     * @param p_player     The player whose army count is being checked.
     * @param p_noOfArmies The number of armies to be deployed (as a String).
     * @return true if the player has enough unallocated armies for deployment,
     *         false otherwise.
     * @throws NumberFormatException If the provided p_noOfArmies parameter cannot
     *                               be parsed as an integer.
     */
    public boolean checkDeployArmyCount(Player p_player, String p_noOfArmies) {
        if (p_player.getD_unallocatedArmyCount() < Integer.parseInt(p_noOfArmies)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Initializes a deployment order for a player, given a deploy command.
     *
     * @param p_command The deploy command entered by the player.
     */
    public void initDeployOrder(String p_command) {

        try {
            // Extract the country name and number of armies from the command
            String l_country = p_command.split(" ")[1];
            String l_armyCount = p_command.split(" ")[2];

            // Check if the player has enough unallocated armies for the deployment
            if (checkDeployArmyCount(this, l_armyCount)) {
                // Print an error message if the deployment order can't be executed due to
                // insufficient armies
                this.setD_playerLog(
                        "The deploy order exceeds the player's available unallocated armies and cannot be executed",
                        AppConstants.ERROR_LOG_MSG);
            } else {
                this.d_playerOrder.add(new Deploy(this, l_country, Integer.parseInt(l_armyCount)));
                // Update the player's unallocated armies count
                Integer l_unallocatedArmies = this.getD_unallocatedArmyCount() - Integer.parseInt(l_armyCount);
                this.setD_unallocatedArmyCount(l_unallocatedArmies);
                d_playerOrder.get(d_playerOrder.size() - 1).printOrder();
                // Print a message indicating that the order has been added to the execution
                // queue
                this.setD_playerLog("Order Queued for player: " + this.d_playerName, AppConstants.LOG_MSG);

            }
        } catch (Exception l_exception) {
            this.setD_playerLog("Invalid command for deploy", AppConstants.ERROR_LOG_MSG);
        }
    }

    /**
     * Handles the initialization and processing of the advance command entered by
     * the player.
     *
     * @param p_commandEntered command entered by the user
     * @param p_gameState      current state of the game
     */
    public void initAdvanceOrder(String p_commandEntered, GameState p_gameState) {
        try {
            if (p_commandEntered.split(" ").length == 4) {
                String l_sourceCountry = p_commandEntered.split(" ")[1];
                String l_targetCountry = p_commandEntered.split(" ")[2];
                String l_armyCount = p_commandEntered.split(" ")[3];
                if (this.isCountryPresentInMap(l_sourceCountry, p_gameState)
                        && this.isCountryPresentInMap(l_targetCountry, p_gameState)
                        && !isArmyCountZero(l_armyCount)
                        && areCountriesAdjacent(p_gameState, l_sourceCountry, l_targetCountry)) {
                    this.d_playerOrder
                            .add(new Advance(this, l_sourceCountry, l_targetCountry, Integer.parseInt(l_armyCount)));
                    this.setD_playerLog(
                            "Advance order added to queue for execution for player: " + this.d_playerName,
                            AppConstants.LOG_MSG);
                }
            } else {
                this.setD_playerLog("Invalid Arguments Passed For Advance Order", AppConstants.ERROR_LOG_MSG);
            }

        } catch (Exception l_e) {
            this.setD_playerLog("Invalid Advance Order Given", AppConstants.ERROR_LOG_MSG);
        }
    }

    /**
     * Checks whether a country given in advance command is present in the
     * map or not.
     *
     * @param p_country   country name which needs to be checked in map
     * @param p_gameState current state of the map
     * @return true if country exists in map or else false
     */
    private Boolean isCountryPresentInMap(String p_country, GameState p_gameState) {
        boolean l_isCountryPresent = p_gameState.getD_map().getCountryByName(p_country) != null;
        if (!l_isCountryPresent) {
            this.setD_playerLog("Country : " + p_country + " does not exist in the map. Hence, order is ignored.",
                    AppConstants.ERROR_LOG_MSG);
        }
        return l_isCountryPresent;
    }

    /**
     * Checks whether the user command consists of 0 armies.
     *
     * @param p_armyCount number of armies given in order
     * @return true if given order has zero armies or else false
     */
    private Boolean isArmyCountZero(String p_armyCount) {
        boolean l_isArmyCountZero = Integer.parseInt(p_armyCount) == 0;
        if (l_isArmyCountZero) {
            this.setD_playerLog(
                    "Advance order should be given with more than zero armies. Hence, order cant be executed.",
                    AppConstants.ERROR_LOG_MSG);
        }
        return l_isArmyCountZero;
    }

    /**
     * Checks whether two countries are adjacent or not.
     *
     * @param p_gameState     current state of the game
     * @param p_sourceCountry source country name
     * @param p_targetCountry target country name
     * @return boolean true if countries are adjacent or else false
     */
    @SuppressWarnings("unlikely-arg-type")
    public boolean areCountriesAdjacent(GameState p_gameState, String p_sourceCountry, String p_targetCountry) {
        Country l_sourceCountry = p_gameState.getD_map().getCountryByName(p_sourceCountry);
        Country l_targetCountry = p_gameState.getD_map().getCountryByName(p_targetCountry);
        Integer l_targetCountryId = l_sourceCountry.getD_adjacentCountryIds().stream()
                .filter(l_adjCountry -> l_adjCountry == l_targetCountry.getD_countryId()).findFirst().orElse(null);
        boolean l_isAdjacent = l_targetCountryId != null;
        if (!l_isAdjacent) {
            this.setD_playerLog("Advance order cannot be executed since target country : " + p_targetCountry
                    + " is not adjacent to source country : " + p_sourceCountry, AppConstants.ERROR_LOG_MSG);
        }
        return l_isAdjacent;
    }

    /**
     * This method will allocate a random card from the available set to the player
     * when they successfully conquer a territory.
     *
     */
    public void allocateCard() {
        if (!d_allowOnlyOneCardPerTurn) {
            Random l_random = new Random();
            this.d_playerOwnedCardList
                    .add(AppConstants.AVAILABLE_CARDS.get(l_random.nextInt(AppConstants.NUM_AVAILABLE_CARDS)));
            // this.d_playerOwnedCardList.add("negotiate");
            this.setD_playerLog(
                    "Player: " + this.d_playerName + " has earned card as reward for the successful conquest- "
                            + this.d_playerOwnedCardList.get(this.d_playerOwnedCardList.size() - 1),
                    AppConstants.LOG_MSG);
            this.setD_allowOnlyOneCardPerTurn(true);
        } else {
            this.setD_playerLog(
                    "Player: " + this.d_playerName + " has already earned maximum cards that can be allotted in a turn",
                    AppConstants.ERROR_LOG_MSG);
        }
    }

    /**
     * 
     * Eliminate the card that has been utilized.
     *
     * @param p_cardLabel name of the card to remove.
     */
    public void removeCard(String p_cardLabel) {
        this.d_playerOwnedCardList.remove(p_cardLabel);
    }

    /**
     * Initiates a player negotiation with another player.
     * <p>
     * This method adds the specified player to the list of players with whom
     * negotiations have been initiated.
     * </p>
     *
     * @param p_playerToNegotiate The player with whom negotiation is being
     *                            initiated.
     */

    public void initiatePlayerNegotiation(Player p_playerToNegotiate) {
        this.d_negotiatedWith.add(p_playerToNegotiate);
    }

    /**
     * Clears all negotiation from the previous turn.
     */
    public void resetNegotiation() {
        d_negotiatedWith.clear();
    }

    /**
     * Checks the validity of the card parameters.
     *
     * @param p_receivedCommand command of card
     * @return bool if valid
     */
    public boolean checkCardCommands(String p_receivedCommand) {
        if (p_receivedCommand.split(" ")[0].equalsIgnoreCase("airlift")) {
            return p_receivedCommand.split(" ").length == 4;
        } else if (p_receivedCommand.split(" ")[0].equalsIgnoreCase("blockade")
                || p_receivedCommand.split(" ")[0].equalsIgnoreCase("bomb")
                || p_receivedCommand.split(" ")[0].equalsIgnoreCase("negotiate")) {
            return p_receivedCommand.split(" ").length == 2;
        } else {
            return false;
        }
    }

    /**
     * Manages the Card Commands: organizes and includes them in the list.
     *
     * @param p_receivedCommand command entered
     * @param p_gameState       gamestate instance
     */
    public void handleCardActions(String p_receivedCommand, GameState p_gameState) {
        if (checkCardCommands(p_receivedCommand)) {
            switch (p_receivedCommand.split(" ")[0]) {
                case "airlift":
                    Card l_newOrder = new Airlift(p_receivedCommand.split(" ")[1],
                            p_receivedCommand.split(" ")[2],
                            Integer.parseInt(p_receivedCommand.split(" ")[3]), this);
                    if (l_newOrder.checkOrderValidity(p_gameState)) {
                        this.d_playerOrder.add(l_newOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!",
                                AppConstants.LOG_MSG);
                        p_gameState.updateLog(getD_playerLogMessage(), AppConstants.ORDER_EFFECT);
                    }
                    break;
                case "blockade":
                    Card l_blockadeOrder = new Blockade(this, p_receivedCommand.split(" ")[1]);
                    if (l_blockadeOrder.checkOrderValidity(p_gameState)) {
                        this.d_playerOrder.add(l_blockadeOrder);
                        this.setD_playerLog("Card Command added to Queue for Execution Successfully!",
                                AppConstants.LOG_MSG);
                        p_gameState.updateLog(getD_playerLogMessage(), AppConstants.ORDER_EFFECT);
                    }
                    break;
                case "bomb":
                    Card l_bombOrder = new Bomb(this, p_receivedCommand.split(" ")[1]);
                    if (l_bombOrder.checkOrderValidity(p_gameState)) {
                        this.d_playerOrder.add(l_bombOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!",
                                AppConstants.LOG_MSG);
                        p_gameState.updateLog(getD_playerLogMessage(), AppConstants.ORDER_EFFECT);
                    }
                    break;
                case "negotiate":
                    Card l_negotiateOrder = new Diplomacy(p_receivedCommand.split(" ")[1], this);
                    if (l_negotiateOrder.checkOrderValidity(p_gameState)) {
                        this.d_playerOrder.add(l_negotiateOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!",
                                AppConstants.LOG_MSG);
                        p_gameState.updateLog(getD_playerLogMessage(), AppConstants.ORDER_EFFECT);
                    }
                    break;
                default:
                    this.setD_playerLog("Invalid Command!", AppConstants.ERROR_LOG_MSG);
                    p_gameState.updateLog(getD_playerLogMessage(), AppConstants.ORDER_EFFECT);
                    break;
            }
        } else {
            this.setD_playerLog("Invalid Card Command Passed! Check Arguments!", AppConstants.ERROR_LOG_MSG);
        }
    }
}