package Services;

import CommonFunctions.*;
import Constants.AppConstants;
import Models.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * This class is responsible for handling player operations.
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
public class PlayerService implements Serializable {

    /**
     * Log messages for player service operations.
     */
    String d_playerServiceLog;

    /**
     * This method check whether the player name exists.
     * 
     * @param playersList list of already exisitng players.
     * @param playerName  player name we want to add.
     * 
     * @return returns true if the player name already exists, otherwise it returns
     *         false.
     */
    public boolean isPlayerNameTaken(List<Player> playersList, String playerName) {
        if (CommonCode.isCollectionEmpty(playersList)) {
            return false;
        }

        for (Player player : playersList) {
            if (player.getD_playerName().equalsIgnoreCase(playerName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method is called by the controller to add or remove players and update
     * the game state.
     *
     * @param p_gameState  The game state to update with player information.
     * @param p_command    The command denoting ("add" or "remove").
     * @param p_playerName The name of the player to add or remove.
     */
    public void updatePlayers(GameState p_gameState, String p_command, String p_playerName) {
        Optional.ofNullable(p_gameState.getD_map())
                .ifPresentOrElse(map -> {
                    String l_playerName;

                    // Get the list of players from the game state
                    List<Player> l_playerList = p_gameState.getD_playerList();

                    // Copy the current player list to avoid modifying it directly.
                    List<Player> l_modifiedPlayerList = new ArrayList<>();
                    if (l_playerList != null) {
                        l_modifiedPlayerList.addAll(l_playerList);
                    }

                    if (p_playerName != null) {
                        l_playerName = p_playerName.split(" ")[0];
                    } else {
                        return;
                    }

                    boolean l_isPlayerNamePresent = isPlayerNameTaken(l_playerList, l_playerName);

                    // Perform the specified operation on the player list
                    switch (p_command.toLowerCase()) {
                        case "add":
                            addPlayer(l_modifiedPlayerList, l_playerName,
                                    l_isPlayerNamePresent);
                            break;
                        case "remove":
                            removePlayer(l_playerList, l_modifiedPlayerList,
                                    l_playerName, l_isPlayerNamePresent);
                            break;
                        default:
                            setD_playerLog("Invalid Operation on Players list");
                    }

                    // Update the game state with the updated player list
                    p_gameState.setD_players(l_modifiedPlayerList);
                    p_gameState.updateLogFile(this.d_playerServiceLog, AppConstants.ORDER_EFFECT);
                },
                        () -> {
                            // This block is executed if p_gameState.getD_map() is null
                            this.setD_playerLog(
                                    "[ERROR]The map is null. Load the map to add player: " + p_playerName);
                            p_gameState.updateLogFile(this.d_playerServiceLog, AppConstants.ORDER_EFFECT);
                        });
    }

    /**
     * Allocate countries and continents to players.
     *
     * This method allocates countries to players based on the number of available
     * players and countries.
     * It also allocates continents to players based on the countries they own.
     *
     * @param p_gameStateInfo The game state information containing players,
     *                        countries, and continents.
     */
    public void allocCountriesAndContinents(GameState p_gameStateInfo) {
        if (!arePlayersLoaded(p_gameStateInfo)) {
            p_gameStateInfo.updateLogFile("Add players before allocating countries", AppConstants.ORDER_EFFECT);
            return;
        }

        List<Country> l_countryList = p_gameStateInfo.getD_map().getD_countries();

        // Calculate the average number of countries to assign per player
        int l_averageCountriesPerPlayer = Math.floorDiv(l_countryList.size(), p_gameStateInfo.getD_playerList().size());

        // alloc random countries to players
        this.allocRandomCountries(l_averageCountriesPerPlayer, l_countryList, p_gameStateInfo.getD_playerList());

        // alloc continents to players based on their owned countries
        this.allocRandomContinents(p_gameStateInfo.getD_playerList(), p_gameStateInfo.getD_map().getD_continents());

        p_gameStateInfo.updateLogFile("Allocation of Country/Continent:", AppConstants.ORDER_EFFECT);
        System.out.println("Countries allocated successfully");
    }

    /**
     * Allocates random countries to players.
     *
     * This method distributes a specified number of random countries to each player
     * from a list of available countries.
     * It continues this process until all countries have been allocated to players
     * or redistributed if any are left unallocated.
     *
     * @param p_averageCountriesPerPlayer The average number of countries to be
     *                                    allocated to each player.
     * @param p_countryList               The list of available countries.
     * @param p_playerList                The list of players to whom countries
     *                                    should be allocated.
     */
    private void allocRandomCountries(int p_averageCountriesPerPlayer, List<Country> p_countryList,
            List<Player> p_playerList) {
        List<Country> l_unallocatedCountries = new ArrayList<>(p_countryList);

        // Iterate through each player and assign countries
        for (Player l_player : p_playerList) {
            if (l_unallocatedCountries.isEmpty())
                break;

            // Randomly assign countries to the player based on the specified average count
            for (int i = 0; i < p_averageCountriesPerPlayer; i++) {
                Random l_randomVal = new Random();
                int l_randomInd = l_randomVal.nextInt(l_unallocatedCountries.size());
                Country l_randomCountryVal = l_unallocatedCountries.get(l_randomInd);

                if (l_player.getD_playerCountries() == null)
                    l_player.setD_playerCountries(new ArrayList<>());

                // Add the random country to the player's list
                l_player.getD_playerCountries().add(l_randomCountryVal);

                // Print a message indicating the country assignment to the player
                System.out.println("Country " + l_randomCountryVal.getD_countryName()
                        + " has been allocated to player: " + l_player.getD_playerName());

                // Remove the allocated country from the unallocated list
                l_unallocatedCountries.remove(l_randomCountryVal);
            }
        }

        // If any countries are still left unassigned, redistribute them among players
        if (!l_unallocatedCountries.isEmpty()) {
            allocRandomCountries(1, l_unallocatedCountries, p_playerList);
        }
    }

    /**
     * Allocates continents to players based on their owned countries.
     *
     * This method iterates through the list of players and checks if a player owns
     * all the countries of a continent.
     * If a player owns all the countries of a continent, that continent is
     * allocated to the player.
     *
     * @param p_playerList    The list of players in the game.
     * @param p_continentList The list of continents in the game.
     */
    public void allocRandomContinents(List<Player> p_playerList, List<Continent> p_continentList) {
        for (Player l_player : p_playerList) {
            List<String> l_playerCountriesOwned = new ArrayList<>();

            // Create a list of country names owned by the player
            if (!CommonCode.isCollectionEmpty(l_player.getD_playerCountries())) {
                l_player.getD_playerCountries()
                        .forEach(l_countryVal -> l_playerCountriesOwned.add(l_countryVal.getD_countryName()));

                for (Continent l_cont : p_continentList) {
                    List<String> l_countriesOfContinent = new ArrayList<>();

                    // Create a list of country names in the continent
                    l_cont.getD_countries().forEach(l_count -> l_countriesOfContinent.add(l_count.getD_countryName()));

                    // Check if the player owns all the countries of the continent
                    if (l_playerCountriesOwned.containsAll(l_countriesOfContinent)) {
                        if (l_player.getD_playerContinents() == null)
                            l_player.setD_playerContinents(new ArrayList<>());

                        l_player.getD_playerContinents().add(l_cont);

                        // Print a message indicating the continent is allocated to the player
                        System.out.println("Continent " + l_cont.getD_continentName()
                                + " has been allocated to player: " + l_player.getD_playerName());
                    }
                }
            }
        }
    }

    /**
     * Checks if players have been added to the game.
     *
     * @param p_gameStateInfo Current game state information to be checked.
     * @return True if players loaded, false otherwise.
     */
    public boolean arePlayersLoaded(GameState p_gameStateInfo) {
        // Check if the player list in the game state is null or empty
        if (p_gameStateInfo.getD_playerList() == null || p_gameStateInfo.getD_playerList().isEmpty()) {
            System.err.println("Add players before assigning countries");
            return false;
        }
        return true;
    }

    /**
     * Checks if there are unallocated armies among a list of players.
     *
     * This method iterates through a list of players and calculates the total
     * number
     * of unallocated armies among them. If the total number of unallocated armies
     * is
     * not equal to zero, it indicates that there are unallocated armies in the
     * game.
     *
     * @param p_playersList The list of players to check for unallocated armies.
     * @return {@code true} if there are unallocated armies among the players,
     *         {@code false} otherwise.
     */
    public boolean isArmyUnallocated(List<Player> p_playersList) {
        int l_unallocatedArmies = 0;
        for (Player l_player : p_playersList) {
            l_unallocatedArmies += l_player.getD_unallocatedArmyCount();
        }
        return l_unallocatedArmies != 0;
    }

    /**
     * Checks if there are pending orders among a list of players.
     *
     * This method iterates through a list of players and calculates the total
     * number
     * of pending orders among them. If the total number of pending orders is not
     * equal
     * to zero, it indicates that there are pending orders in the game.
     *
     * @param p_playersList The list of players to check for pending orders.
     * @return {@code true} if there are pending orders among the players,
     *         {@code false} otherwise.
     */
    public boolean pendingOrdersExist(List<Player> p_playersList) {
        int l_totalPendingOrders = 0;
        for (Player l_player : p_playersList) {
            l_totalPendingOrders += l_player.getD_playerOrder().size();
        }
        return l_totalPendingOrders != 0;
    }

    /**
     * Allocate different Colors to the players randomly to distinguish them.
     *
     * @param p_gameStateInfo Current Game State
     */
    public void allocColors(GameState p_gameStateInfo) {
        if (arePlayersLoaded(p_gameStateInfo)) {
            List<Player> l_playerList = p_gameStateInfo.getD_playerList();
            int l_color = 0;
            for (Player l_player : l_playerList) {
                l_player.setD_playerColor(AppConstants.COLORS.get(l_color++));
            }
        } else {
            return;
        }
    }

    /**
     * Calculates the number of armies to assign to a player based on their owned
     * countries and continents.
     *
     * @param p_player The player for whom to calculate armies.
     * @return The calculated number of armies to assign to the player.
     */
    public int findArmyCount(Player p_player) {
        int l_countOfArmies = 0;
        if (!CommonCode.isCollectionEmpty(p_player.getD_playerCountries())) {
            int l_countryCount = p_player.getD_playerCountries().size();
            int l_baseArmies = Math.max(3, Math.round(l_countryCount / 3));
            l_countOfArmies = l_baseArmies;
        }
        if (!CommonCode.isCollectionEmpty(p_player.getD_playerContinents())) {
            int l_continentControlVal = 0;
            // Calculate the continent control value
            for (Continent l_continent : p_player.getD_playerContinents()) {
                l_continentControlVal += l_continent.getD_continentValue();
            }
            l_countOfArmies += l_continentControlVal;
        }
        return l_countOfArmies;
    }

    /**
     * Allocates armies to players in the game based on their respective territories
     * and continents.
     *
     * @param p_gameStateInfo The game state information containing player data and
     *                        game status.
     */
    public void allocArmies(GameState p_gameStateInfo) {
        for (Player l_player : p_gameStateInfo.getD_playerList()) {
            Integer l_countOfArmies = this.findArmyCount(l_player);
            this.setD_playerLog("Allocated " + l_countOfArmies + " armies to " + l_player.getD_playerName() + ".");
            p_gameStateInfo.updateLogFile(this.d_playerServiceLog, AppConstants.ORDER_EFFECT);
            l_player.setD_unallocatedArmyCount(l_countOfArmies);
        }
    }

    /**
     * Adds new player to the game.
     * 
     * @param p_modifiedPlayersList Modified player list
     * @param p_playerName          Player name to be added
     * @param p_isPlayerNamePresent TRUE if player already present in the list;false
     *                              otherwise
     */
    private void addPlayer(List<Player> p_modifiedPlayersList, String p_playerName,
            boolean p_isPlayerNamePresent) {
        if (p_isPlayerNamePresent) {
            setD_playerLog("Player: " + p_playerName + " already present. No modifications made.");
        } else {
            // assignrandomstrategy(p_playerName);
            // p_modifiedPlayersList.add(new Player(p_playerName));
            // setD_playerLog("Player: " + p_playerName + " successfully added.");
            Player l_addNewPlayer = new Player(p_playerName);
            // String l_playerStrategy = "Benevolent";
            String l_playerStrategy = AppConstants.PLAYER_BEHAVIORS
                    .get(new Random().nextInt(AppConstants.PLAYER_BEHAVIORS.size()));

            switch (l_playerStrategy) {
                case "Human":
                    l_addNewPlayer.setStrategy(new HumanPlayer());
                    break;
                case "Aggressive":
                    l_addNewPlayer.setStrategy(new AggressivePlayer());
                    break;
                case "Random":
                    l_addNewPlayer.setStrategy(new RandomPlayer());
                    break;
                case "Benevolent":
                    l_addNewPlayer.setStrategy(new BenevolentPlayer());
                    break;
                case "Cheater":
                    l_addNewPlayer.setStrategy(new CheaterPlayer());
                    break;
                default:
                    setD_playerLog("Invalid Player Behavior");
                    break;
            }
            p_modifiedPlayersList.add(l_addNewPlayer);
            setD_playerLog("Player with name : " + p_playerName + " and strategy: " + l_playerStrategy
                    + " has been added successfully.");
        }
    }

    /**
     * Assign Random Starategy to the player.
     *
     * @param p_enteredPlayerName Given player Name
     */
    public void assignrandomstrategy(String p_enteredPlayerName) {
        Player newPlayer = new Player(p_enteredPlayerName);
        String playerStrategy = AppConstants.PLAYER_BEHAVIORS
                .get(new Random().nextInt(AppConstants.PLAYER_BEHAVIORS.size()));

        switch (playerStrategy) {
            case "Human":
                newPlayer.setStrategy(new HumanPlayer());
                break;
            case "Aggressive":
                newPlayer.setStrategy(new AggressivePlayer());
                break;
            case "Random":
                newPlayer.setStrategy(new RandomPlayer());
                break;
            case "Benevolent":
                newPlayer.setStrategy(new BenevolentPlayer());
                break;
            case "Cheater":
                newPlayer.setStrategy(new CheaterPlayer());
                break;
            default:
                setD_playerLog("Invalid Player Behavior");
                break;
        }
        setD_playerLog("Player with name : " + p_enteredPlayerName + " and strategy: " + playerStrategy
                + " has been added successfully.");
    }

    /**
     * Removes a player from the game if the player exists in the list.
     *
     * @param p_playerList          The existing player list present in the game.
     * @param p_modifiedPlayerList  The updated player list where removal is to
     *                              be performed.
     * @param p_playerName          The player name to be removed.
     * @param p_isPlayerNamePresent true if the player already present;
     *                              false otherwise.
     */
    private void removePlayer(List<Player> p_playerList, List<Player> p_modifiedPlayerList,
            String p_playerName, boolean p_isPlayerNamePresent) {
        if (p_isPlayerNamePresent) {
            for (Player l_player : p_playerList) {
                if (l_player.getD_playerName().equalsIgnoreCase(p_playerName)) {
                    p_modifiedPlayerList.remove(l_player);
                    setD_playerLog("Player: " + p_playerName + " has been removed successfully.");
                    return; // Exit the loop after the player is removed.
                }
            }
            // If the loop completes without finding the player.
            setD_playerLog(p_playerName + " not found.");
        } else {
            setD_playerLog("Player: " + p_playerName + " is not present. No changes made.");
        }
    }

    /**
     * Checks if any players in the game wish to issue additional orders.
     *
     * @param p_playersList players involved in game
     * @return boolean whether there are more orders to give or not
     */
    public boolean checkAdditionalOrders(List<Player> p_playersList) {
        for (Player l_player : p_playersList) {
            if (l_player.getD_additionalOrders())
                return true;
        }
        return false;
    }

    /**
     * Resets each players information for accepting further orders.
     *
     * @param p_playersList players involved in game
     */
    public void resetPlayersFlag(List<Player> p_playersList) {
        for (Player l_player : p_playersList) {
            if (!l_player.getD_playerName().equalsIgnoreCase("Neutral"))
                l_player.setD_additionalOrders(true);
            l_player.setD_allowOnlyOneCardPerTurn(false);
            l_player.resetNegotiation();
        }
    }

    /**
     * Sets the player service log message and prints it to the standard output
     * stream.
     *
     * @param p_playerServiceLog The message to be set as the player service log
     *                           message.
     */
    public void setD_playerLog(String p_playerServiceLog) {
        this.d_playerServiceLog = p_playerServiceLog;
        System.out.println(p_playerServiceLog);
    }

    /**
     * Retrieves a player from the game state by their player name.
     * <p>
     * Searches for a player with the specified player name in the list of players
     * within the given game state.
     * </p>
     *
     * @param p_playerName The name of the player to retrieve.
     * @param p_gameState  The current game state containing the player list.
     * @return The player with the matching player name if found; otherwise, null.
     */
    public Player getPlayerByName(String p_playerName, GameState p_gameState) {
        return p_gameState.getD_playerList().stream()
                .filter(l_player -> l_player.getD_playerName().equals(p_playerName)).findFirst().orElse(null);
    }

}
