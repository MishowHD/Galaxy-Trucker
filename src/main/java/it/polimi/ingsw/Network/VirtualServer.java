package it.polimi.ingsw.Network;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.Goods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public interface VirtualServer {
    /**
     * Creates a new game with the specified level, ID, number of players, and nickname.
     *
     * @param level the level of the game
     * @param id the unique identifier for the game
     * @param numplayrs the number of players participating in the game
     * @param Nickname the nickname assigned to the game or host player
     * @throws Exception if an error occurs during game creation
     */
    //il primo parametro è un intero del metodo. poi rispettiamo la signature e chiamiamo
    void createGame(int level, UUID id, int numplayrs, String Nickname) throws Exception;

    /**
     * Allows a player to join an existing game on the server using their nickname and identifiers.
     *
     * @param NickName     The nickname of the player joining the game.
     * @param NumOfPlayers The number of players already in the game.
     * @param id           The unique identifier of the game the player is joining.
     * @param oldid        The unique identifier representing the most recent game state or player's previous connection.
     * @throws IOException If an input or output error occurs.
     */
    //1
    void JoinGame(String NickName, int NumOfPlayers, UUID id, UUID oldid) throws IOException;

    /**
     * Activates a timer for a specific player in the game identified by the given UUID.
     *
     * @param player The name of the player for whom the timer will be activated.
     * @param id The unique identifier of the game in which the timer is being activated.
     * @throws IOException If an I/O error occurs during the timer activation process.
     */
    // 2
    void activateTimer(String player, UUID id) throws IOException;

    /**
     * Sets the number tile on the player’s board in the specified position.
     *
     * @param playerid The ID of the player whose board is being modified.
     * @param posTile  The position on the board where the tile is to be placed.
     * @param id       The unique identifier of the game session.
     * @throws IOException If an input or output error occurs during the operation.
     */
    //3
    void setNumTile(int playerid, int posTile, UUID id) throws IOException;

    /**
     * Allows a player to surrender in the current game session.
     *
     * @param playerID the unique identifier of the player surrendering
     * @param id the unique game session identifier
     * @throws IOException if an I/O error occurs during the surrender process
     */
    //4
    void Surrend(int playerID, UUID id) throws IOException;

    /**
     * Visualizes the final scores of the game identified by the unique ID.
     *
     * @param id The unique identifier (UUID) of the game for which the final scores are to be visualized.
     * @throws IOException If an input or output exception occurs during the process.
     */
    //5
    void visualizeFinalScores(UUID id) throws IOException;

    /**
     * Allows a player to pick a tile that has already been flipped during the game.
     *
     * @param index The index of the tile to be picked.
     * @param playerID The unique identifier of the player attempting to pick the tile.
     * @param id The unique game session identifier.
     * @throws IOException If an I/O error occurs during the tile-picking action.
     */
    //6
    void pickTileAlreadyFlipped(int index, int playerID, UUID id) throws IOException;

    /**
     * Allows a player to pick an unknown tile associated with the specified game session.
     *
     * @param playerID The unique identifier of the player attempting to pick the tile.
     * @param id The unique identifier of the game session.
     * @throws IOException If an input or output exception occurs during the operation.
     */
    //7
    void pickTileUnknown(int playerID, UUID id) throws IOException;

    /**
     * Allows a player to pick a "little deck" during the game.
     * The little deck is associated with a specific index and player ID
     * within an ongoing game identified by the UUID.
     *
     * @param index the index of the little deck to be picked
     * @param playerID the unique identifier of the player performing the action
     * @param id the unique identifier of the game session
     * @throws IOException if an I/O error occurs while performing the action
     */
    //8
    void pickLittleDeck(int index, int playerID, UUID id) throws IOException;

    /**
     * Allows the player to deposit their little deck in the game by specifying their unique player ID and game session ID.
     *
     * @param playerID the unique identifier of the player performing the action
     * @param id the unique identifier of the game session in which the action is performed
     * @throws IOException if an I/O error occurs during the process
     */
    //9
    void depositLittleDeck(int playerID, UUID id) throws IOException;

    /**
     * Allows a player to deposit a tile during the game.
     *
     * @param playerID the unique identifier of the player performing the action
     * @param id the unique identifier of the game
     * @throws IOException if an input or output error occurs during the operation
     */
    //10
    void depositTile(int playerID, UUID id) throws IOException;

    /**
     * Ends the building phase for a player and transitions to the specified position.
     *
     * @param PlayerID The unique identifier for the player ending the building phase.
     * @param positionwheretogo The position or phase to transition to after ending the building phase.
     * @param id The unique game session identifier.
     * @throws IOException If an input or output exception occurs.
     */
    //11
    void endbuilding(int PlayerID, int positionwheretogo, UUID id) throws IOException;

    /**
     * Inserts a tile into the specified position on the game board with the given rotation.
     *
     * @param row      the row index where the tile should be inserted
     * @param col      the column index where the tile should be inserted
     * @param playerID the ID of the player inserting the tile
     * @param rotation the rotation to be applied to the tile before insertion
     * @param id       the unique identifier for the current game session
     * @throws IOException if there is an issue during the insertion process
     */
    //12
    void insertTile(int row, int col, int playerID, int rotation, UUID id) throws IOException;

    /**
     * Activates a specific effect within the virtual server using the provided unique identifier.
     *
     * @param id the unique identifier (UUID) of the effect to be activated
     * @throws IOException if an I/O error occurs during the effect activation process
     */
    //13
    void EffectActivation(UUID id) throws IOException;

    /**
     * Retrieves the card currently in use for a given game instance identified by the provided UUID.
     *
     * @param id The unique identifier (UUID) associated with the game instance for which the current card in use needs to be retrieved.
     * @throws IOException If an input or output exception occurs during the operation.
     */
    //14
    void getCardinuse(UUID id) throws IOException;

    /**
     * Allows a player to choose a specific sub-ship in the game.
     *
     * @param index    the index of the sub-ship being selected
     * @param playerID the unique identifier for the player making the selection
     * @param id       the unique identifier for the game session
     * @throws IOException if an I/O error occurs during the operation
     */
    //15
    void chooseOneSubShip(int index, int playerID, UUID id) throws IOException;

    /**
     * Allows a player to decide whether to accept or reject the opportunity to land on a planet.
     *
     * @param p        The player's identifier or name involved in the decision to land on the planet.
     * @param yOn      A boolean flag indicating the player's choice. True if the player accepts the landing, false otherwise.
     * @param NumPlanet The number representing the specific planet on which the player has the option to land.
     * @param id       A unique identifier (UUID) for the game session or player context.
     * @throws IOException If an input or output exception occurs during the process.
     */
    //16
    void acceptToLandOnAPlanet(String p, boolean yOn, int NumPlanet, UUID id) throws IOException;

    /**
     * Allows the player to interact with an abandoned station, specifying their preferences
     * for handling the scenario.
     *
     * @param player the name or identifier of the player interacting with the abandoned station
     * @param yOn a boolean indicating whether the player agrees to proceed with the interaction
     * @param storageTiles a list of storage tiles represented as a list of lists of integers,
     *                     where each sublist corresponds to a specific storage configuration
     * @param newGoods a list of goods sets represented as a list of lists, where each sublist contains Goods objects
     * @param id the unique identifier of the game or session
     * @throws IOException if an error occurs during the interaction process
     */
    //17
    void chooseAbandonedStation(String player, boolean yOn, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods, UUID id) throws IOException;

    /**
     * Allows a player to select the positions for cannon batteries during the game.
     *
     * @param player the name or identifier of the player who is making the selection
     * @param cannonPos an ArrayList of ArrayLists containing the positions of cannons on the player's ship
     * @param batteriesPos an ArrayList of ArrayLists containing the positions of batteries on the ship
     * @param id the unique identifier for the game session
     * @throws IOException if an input or output operation is failed or interrupted
     */
    //18
    void chooseCannonBatteryPos(String player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos, UUID id) throws IOException;

    /**
     * Determines how the player chooses to defend against meteors in a game.
     *
     * @param player the nickname of the player making the decision
     * @param howToDefenceFromShots a list of integers representing the player's strategy or actions to defend against meteor threats
     * @param id the unique identifier of the game instance
     * @throws IOException if an input or output exception occurs
     */
    //19
    void chooseHowToFaceMeteors(String player, ArrayList<Integer> howToDefenceFromShots, UUID id) throws IOException;

    /**
     * Handles the process of choosing passengers to lose in a particular scenario determined
     * by the game's logic and state.
     *
     * @param p       The player's identifier or name involved in the action.
     * @param yOn     A boolean flag indicating the current state or condition (e.g., a validation check or action toggle).
     * @param pass    A list of lists representing groups of passengers and their respective data,
     *                where each inner list might contain specific passenger-related information.
     * @param id      A unique identifier for the game instance, used for correlating the action
     *                within the context of the ongoing game.
     * @throws IOException If an I/O-related error occurs during the execution of this method.
     */
    //20
    void choosePassengersToLose(String p, boolean yOn, ArrayList<ArrayList<Integer>> pass, UUID id) throws IOException;

    /**
     * Allows a player to claim a reward in the game, with the option to confirm or deny the action.
     *
     * @param yOn      A boolean value indicating whether the player chooses to claim the reward.
     * @param player   The nickname or identifier of the player performing the action.
     * @param id       The unique identifier for the game session or instance where the action takes place.
     * @throws IOException If an I/O error occurs during the process of claiming the reward.
     */
    //21
    void chooseToClaimReward(boolean yOn, String player, UUID id) throws IOException;

    /**
     * Allows the player to choose whether to claim a reward. This involves processing
     * the player's decision alongside their storage tiles and new goods.
     *
     * @param yOn a boolean value indicating the player's decision to claim the reward.
     * @param player a String representing the player's name making the decision.
     * @param storageTiles an ArrayList of ArrayLists containing integers that represent
     *                     the current state of the player's storage tiles.
     * @param newGoods an ArrayList of ArrayLists containing Goods objects, representing
     *                 the new goods available for claiming.
     * @param id a UUID that uniquely identifies the game session.
     * @throws IOException if an input or output exception occurs during the operation.
     */
    //22
    void chooseToClaimReward(boolean yOn, String player, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods, UUID id) throws IOException;

    /**
     * Allows the player to choose and place batteries at specified positions during the game.
     *
     * @param p the player identifier or name making the selection
     * @param posBatAndNumBattXPos a 2D list where each sublist contains the positions and the corresponding number of batteries to place
     * @param id the unique identifier associated with the current game session
     * @throws IOException if an I/O error occurs during the process
     */
    //23
    void chooseToPlaceBatteries(String p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, UUID id) throws IOException;

    /**
     * Initiates firepower in the game based on the specified parameters.
     *
     * @param p The player's identifier engaged in firepower activation.
     * @param DoubFireTriplets A list of triplets specifying the coordinates and parameters for double-fire actions.
     * @param BatteriesToAct A list of positions indicating which batteries should participate in the action.
     * @param id The unique identifier of the game session.
     * @throws IOException If an input-output operation fails during execution.
     */
    //24
    void chooseToStartFirePower(String p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct, UUID id) throws IOException;

    /**
     * Allows the player to initiate the process of starting a motor, specifying
     * the positions of engines and batteries to be used.
     *
     * @param player        the name of the player requesting to start the motor
     * @param enginesPos    a 2D list containing the positions of the engines to be activated
     * @param batteriesPos  a 2D list containing the positions of the batteries to power the engines
     * @param id            the unique identifier for the current game session
     * @throws IOException  if an I/O error occurs during the operation
     */
    //25
    void chooseToStartMotor(String player, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos, UUID id) throws IOException;

    /**
     * Allows a player to choose positions to store goods in the game. The method interacts
     * with goods and their corresponding positions to allocate them appropriately in the game context.
     *
     * @param player the name of the player making the selection
     * @param posGoods a nested list of integers representing possible positions for the goods
     * @param goodsSets a nested list of Goods objects to be allocated across the specified positions
     * @param id a universally unique identifier (UUID) associated with the current game session
     * @throws IOException if an I/O error occurs during the operation
     */
    //26
    void chooseWhereToPutGoods(String player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, UUID id) throws IOException;

    /**
     * Adds a "wait tile" for the specified player in the game identified by the given UUID.
     *
     * @param playerID the ID of the player who is adding the wait tile
     * @param id the unique identifier of the game
     * @throws IOException if an I/O error occurs during the operation
     */
    //27
    void addWaitTile(int playerID, UUID id) throws IOException;

    /**
     * Inserts a wait tile into the specified position and orientation on the board for a given player.
     *
     * @param playerID The ID of the player who is inserting the wait tile.
     * @param index The index of the wait tile to be inserted.
     * @param row The row on the board where the wait tile should be placed.
     * @param col The column on the board where the wait tile should be placed.
     * @param rotation The rotation of the wait tile to be applied before placement.
     * @param id The unique identifier of the game in which the tile is being inserted.
     * @throws IOException If an I/O error occurs while performing the operation.
     */
    //28
    void insertWaitTile(int playerID, int index, int row, int col, int rotation, UUID id) throws IOException;

    /**
     * Fills a tile on the board for a specific player based on the given parameters.
     * The tile can optionally be populated with an alien of a specified color.
     *
     * @param playerid The unique identifier of the player making the command.
     * @param wantalien A boolean indicating if the tile should contain an alien.
     * @param color The color of the alien to be placed, if applicable.
     * @param row The row index where the tile is to be placed.
     * @param col The column index where the tile is to be placed.
     * @param id The unique game session identifier.
     * @throws IOException If an I/O error occurs during the tile placement.
     */
    //29
    void CommandFillTile(int playerid, boolean wantalien, AlienColor color, int row, int col, UUID id) throws IOException;

    /**
     * Activates all ongoing games associated with the specified UUID.
     *
     * @param uuid the unique identifier of the game server or session for which active games need to be processed
     * @throws IOException if an input-output exception occurs during the activation of games
     */
    //30
    void activeGames(UUID uuid) throws IOException;

    /**
     * Demonstrates a game associated with the given unique identifier (UUID).
     *
     * @param uuid The unique identifier of the game to be demonstrated.
     * @throws IOException If an I/O error occurs during the demonstration process.
     */
    //31
    void demoGame(UUID uuid) throws IOException;
}
