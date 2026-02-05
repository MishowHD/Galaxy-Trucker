package it.polimi.ingsw.Network.Client.RMI;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.VirtualServer;
import it.polimi.ingsw.Network.Server.RMI.VirtualViewRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

public interface VirtualServerRMI extends Remote, VirtualServer {
    /**
     * Establishes a connection between the server and a client.
     *
     * @param client   the client object that implements the VirtualViewRMI interface, representing the client side of the connection
     * @param uuid     the unique identifier associated with the client to identify the session
     * @param username the username of the client, used for session identification and interaction
     * @throws RemoteException if a remote communication error occurs
     */
    void connect(VirtualViewRMI client, UUID uuid, String username) throws RemoteException;

    /**
     * Creates a new game with the specified parameters.
     *
     * @param level the difficulty level of the game
     * @param id the unique identifier for the game
     * @param Numplayers the number of players in the game
     * @param Nickname the nickname of the initiating player
     * @throws Exception if game creation fails
     */
    void createGame(int level, UUID id, int Numplayers, String Nickname) throws Exception;

    /**
     * Allows a player to join an existing game on the virtual server.
     *
     * @param NickName the nickname of the player attempting to join the game
     * @param NumOfPlayers the number of players expected in the game
     * @param id the unique identifier of the game the player wants to join
     * @param old the unique identifier of the player's previous game, if applicable
     * @throws RemoteException if an error occurs during the remote method call
     */
    //1
    void JoinGame(String NickName, int NumOfPlayers, UUID id, UUID old) throws RemoteException;

    /**
     * Activates a timer for a specific player in the game session identified by the given UUID.
     *
     * @param player the username of the player for whom the timer is being activated
     * @param id the unique identifier of the game session
     * @throws RemoteException if a communication-related exception occurs during the execution of the remote method
     */
    // 2
    void activateTimer(String player, UUID id) throws RemoteException;

    /**
     * Sets the tile number for a specific player in a given position.
     *
     * @param playerid The ID of the player for whom the tile number is being set.
     * @param posTile  The position of the tile being set.
     * @param id       The unique identifier for the game session.
     * @throws RemoteException If a communication-related exception occurs during the remote method call.
     */
    //3
    void setNumTile(int playerid, int posTile, UUID id) throws RemoteException;

    /**
     * Allows a player to surrender in the game, performing relevant updates based on the player's ID
     * and the game instance identified by the UUID.
     *
     * @param playerID the unique identifier of the player who is surrendering
     * @param id the unique identifier of the game instance
     * @throws RemoteException if a remote method invocation error occurs
     */
    //4
    void Surrend(int playerID, UUID id) throws RemoteException;

    /**
     * Displays the final scores of the game associated with the given unique identifier.
     *
     * @param id the unique identifier of the game for which the final scores are to be visualized
     * @throws RemoteException if a remote communication error occurs
     */
    //5
    void visualizeFinalScores(UUID id) throws RemoteException;

    /**
     * Allows a player to pick a tile that has already been revealed or flipped over.
     *
     * @param index the position of the tile to be picked from the flipped tiles.
     * @param playerID the unique identifier of the player making the selection.
     * @param id the unique identifier of the game session.
     * @throws RemoteException if communication-related exceptions occur during the method execution.
     */
    //6
    void pickTileAlreadyFlipped(int index, int playerID, UUID id) throws RemoteException;

    /**
     * Allows a player to pick a tile whose type is unknown.
     *
     * @param playerID the unique identifier of the player performing the action
     * @param id the unique identifier of the game session
     * @throws RemoteException if a communication-related exception occurs during the remote method call
     */
    //7
    void pickTileUnknown(int playerID, UUID id) throws RemoteException;

    /**
     * Executes the action of picking a small deck for a specific player in the virtual game session.
     *
     * @param index the index indicating which small deck to pick.
     * @param playerID the unique identifier of the player performing the action.
     * @param id the unique identifier of the game session.
     * @throws RemoteException if a remote communication error occurs.
     */
    //8
    void pickLittleDeck(int index, int playerID, UUID id) throws RemoteException;

    /**
     * Deposits the player's current "little deck" to the server, associated with the specified game session.
     *
     * @param playerID The unique identifier of the player performing the action.
     * @param id The universal unique identifier (UUID) of the game session.
     * @throws RemoteException If a communication-related exception occurs during the remote method call.
     */
    //9
    void depositLittleDeck(int playerID, UUID id) throws RemoteException;

    /**
     * Handles the action of depositing a tile for a specific player within a game session identified by the given UUID.
     *
     * @param playerID the unique identifier of the player performing the action
     * @param id the unique identifier of the game session
     * @throws RemoteException if a remote communication error occurs
     */
    //10
    void depositTile(int playerID, UUID id) throws RemoteException;

    /**
     * Ends the building phase for a specific player in a game and transitions to another position or phase.
     *
     * @param PlayerID           the ID of the player ending the building phase
     * @param positionwheretogo  the position or phase to transition to after ending the building phase
     * @param id                 the UUID of the game or session
     * @throws RemoteException   if a communication-related error occurs during the remote call
     */
    //11
    void endbuilding(int PlayerID, int positionwheretogo, UUID id) throws RemoteException;

    /**
     * Inserts a tile into the game board at the specified row and column with the given rotation.
     * This method associates the inserted tile with a specific player and game identified by the given UUID.
     *
     * @param row       The row index where the tile should be placed on the game board.
     * @param col       The column index where the tile should be placed on the game board.
     * @param playerID  The ID of the player performing the tile insertion.
     * @param rotation  The rotation of the tile, represented as an integer.
     * @param id        The unique identifier for the game session in which the action is taking place.
     *
     * @throws RemoteException If a communication-related error occurs during the execution of the remote method.
     */
    //12
    void insertTile(int row, int col, int playerID, int rotation, UUID id) throws RemoteException;

    /**
     * Activates the effect associated with the provided game session identifier.
     *
     * @param id The unique identifier representing the game session for which the effect is to be activated.
     * @throws RemoteException If a communication-related exception occurs during the remote method call.
     */
    //13
    void EffectActivation(UUID id) throws RemoteException;

    /**
     * Retrieves the card currently in use for the specified game session.
     *
     * @param id the unique identifier (UUID) of the game session
     * @throws RemoteException if a remote communication error occurs
     */
    //14
    void getCardinuse(UUID id) throws RemoteException;

    /**
     * Allows a player to choose a specific sub-ship during gameplay.
     * This method is implemented on the server side and called by the client
     * to interact with the game involving sub-ships selection.
     *
     * @param index The index of the sub-ship to be chosen.
     * @param playerID The ID of the player making the selection.
     * @param id The unique identifier of the game session.
     * @throws RemoteException If a remote communication issue occurs.
     */
    //15
    void chooseOneSubShip(int index, int playerID, UUID id) throws RemoteException;

    /**
     * Allows a player to accept or decline the option to land on a planet within the game.
     *
     * @param p         The identifier of the player making the decision.
     * @param yOn       A boolean indicating whether the player accepts (true) or declines (false) the option to land on the planet.
     * @param NumPlanet The identifier of the planet on which the player may land.
     * @param id        The unique game session identifier associated with the current action.
     * @throws RemoteException If a communication-related error occurs during the remote invocation.
     */
    //16
    void acceptToLandOnAPlanet(String p, boolean yOn, int NumPlanet, UUID id) throws RemoteException;

    /**
     * Allows a player to choose an abandoned station during the game.
     * This method handles the selection process for abandoned stations, which may
     * involve updating the storage tiles and adding new goods for the player.
     *
     * @param player the name of the player making the selection.
     * @param yOn a boolean flag for additional criteria or conditions for the selection.
     * @param storageTiles an ArrayList of ArrayLists representing the storage tiles for the game.
     * @param newGoods an ArrayList of ArrayLists containing the new goods associated with the selection.
     * @param id the unique identifier of the game session.
     * @throws RemoteException if a remote communication error occurs during method invocation.
     */
    //17
    void chooseAbandonedStation(String player, boolean yOn, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods, UUID id) throws RemoteException;

    /**
     * Allows the player to choose positions for cannon batteries during the gameplay.
     *
     * @param player        The username or identifier of the player making the selection.
     * @param cannonPos     The list of possible positions for cannons, represented as a 2D array of integers.
     * @param batteriesPos  The list of possible positions for batteries, represented as a 2D array of integers.
     * @param id            The unique identifier of the game session.
     * @throws RemoteException If there is an issue during the remote method invocation.
     */
    //18
    void chooseCannonBatteryPos(String player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos, UUID id) throws RemoteException;

    /**
     * Allows the specified player to choose how to defend against incoming meteors during the game.
     * The choice is represented by a list of integers, where each integer refers to a specific defensive action or option.
     *
     * @param player the username of the player making the decision
     * @param howToDefenceFromShots a list of integers representing the player's chosen defensive actions or methods
     * @param id the unique identifier for the game session
     * @throws RemoteException if there is a communication error during the remote method invocation
     */
    //19
    void chooseHowToFaceMeteors(String player, ArrayList<Integer> howToDefenceFromShots, UUID id) throws RemoteException;

    /**
     * Handles the selection of passengers to be lost based on the provided parameters.
     *
     * @param p         The identifier of the player performing the action.
     * @param yOn       A boolean indicating whether specific game conditions are active.
     * @param pass      A list of lists representing the passengers and their attributes.
     * @param id        The unique identifier of the game session.
     * @throws RemoteException If a remote communication error occurs.
     */
    //20
    void choosePassengersToLose(String p, boolean yOn, ArrayList<ArrayList<Integer>> pass, UUID id) throws RemoteException;

    /**
     * Allows a player to choose whether to claim a reward during the game.
     *
     * @param yOn     A boolean indicating the player's decision, where true means the reward is claimed and false means it is not.
     * @param player  The name or identifier of the player making the decision.
     * @param id      The unique identifier of the game session.
     * @throws RemoteException If a remote communication error occurs during the invocation of this method.
     */
    //21
    void chooseToClaimReward(boolean yOn, String player, UUID id) throws RemoteException;

    /**
     * Allows a player to choose to claim a reward during the game. This action involves providing
     * storage tile positions and new goods that will be associated with the reward. The game server
     * processes this request based on the provided parameters.
     *
     * @param yOn a boolean indicating the player's decision regarding the reward; true if they choose to claim.
     * @param player a String representing the player's identifier or name.
     * @param storageTiles an ArrayList of ArrayLists of Integers representing the storage tile positions
     *                     to be used or associated with the reward.
     * @param newGoods an ArrayList of ArrayLists of Goods objects representing the new goods to be
     *                 added when claiming the reward.
     * @param id a UUID uniquely identifying the game session.
     * @throws RemoteException if there is an error during the remote method call.
     */
    //22
    void chooseToClaimReward(boolean yOn, String player, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods, UUID id) throws RemoteException;

    /**
     * Allows the player to choose and place batteries in specific positions during the game.
     *
     * @param p                        the identifier of the player making the choice
     * @param posBatAndNumBattXPos     a list of lists, where each inner list specifies positions and the number of batteries to place at each position
     * @param id                       the unique identifier of the game session
     * @throws RemoteException         if a remote communication error occurs
     */
    //23
    void chooseToPlaceBatteries(String p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, UUID id) throws RemoteException;

    /**
     * Handles the player's decision to start fire power during the game.
     *
     * @param p                The player making the decision, represented as a string.
     * @param DoubFireTriplets A list of coordinates or identifiers for selected targets that will be attacked
     *                         with double firepower, presented as a nested list of integers.
     * @param BatteriesToAct   A list of coordinates or identifiers for the batteries to be activated
     *                         to enable the firepower, presented as a nested list of integers.
     * @param id               The unique identifier for the specific game instance where this action occurs.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    //24
    void chooseToStartFirePower(String p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct, UUID id) throws RemoteException;

    /**
     * Allows a player to activate certain motors in the game, specifying their positions and the
     * corresponding batteries needed to power them.
     *
     * @param player the name or identifier of the player making the move
     * @param enginesPos a list of lists containing the positions of the engines chosen by the player
     * @param batteriesPos a list of lists containing the positions of the batteries to power the engines
     * @param id the unique identifier of the game session
     * @throws RemoteException if there is an issue with the remote method invocation
     */
    //25
    void chooseToStartMotor(String player, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos, UUID id) throws RemoteException;

    /**
     * Allows a player to choose where to place specific goods within their storage system.
     *
     * @param player the identifier of the player making the choice.
     * @param posGoods a list of positions representing where the goods can potentially be placed.
     * @param goodsSets a list of goods sets available to the player for placement.
     * @param id the unique identifier for the current game instance.
     * @throws RemoteException if a remote invocation error occurs.
     */
    //26
    void chooseWhereToPutGoods(String player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, UUID id) throws RemoteException;

    /**
     * Adds a wait tile for the specified player in the game identified by the given UUID.
     *
     * @param playerID the ID of the player who will have the wait tile added
     * @param id the unique identifier of the game
     * @throws RemoteException if a remote invocation error occurs
     */
    //27
    void addWaitTile(int playerID, UUID id) throws RemoteException;

    /**
     * Inserts a wait tile for a specific player at the given position and orientation.
     *
     * @param playerID the ID of the player for whom the wait tile is being inserted
     * @param index the index of the tile to insert
     * @param row the row position where the tile is to be placed
     * @param col the column position where the tile is to be placed
     * @param rotation the rotation value of the tile
     * @param id the unique identifier of the game instance
     * @throws RemoteException if a remote communication error occurs
     */
    //28
    void insertWaitTile(int playerID, int index, int row, int col, int rotation, UUID id) throws RemoteException;

    /**
     * Executes the command to fill a tile on the game board, possibly with an alien presence, at the specified coordinates.
     *
     * @param playerid  the ID of the player executing the command
     * @param wantalien a flag indicating whether to place an alien in the specified tile
     * @param color     the color of the alien to be placed, if applicable
     * @param row       the row index of the tile to be filled
     * @param col       the column index of the tile to be filled
     * @param id        the unique identifier for the game session
     * @throws RemoteException if a communication-related exception occurs during execution
     */
    //29
    void CommandFillTile(int playerid, boolean wantalien, AlienColor color, int row, int col, UUID id) throws RemoteException;

    /**
     * Provides a way to retrieve a list of active games associated with a specific UUID.
     * The UUID is used to identify the server or client context for the games.
     *
     * @param uuid the unique identifier associated with the server or client context
     * @throws RemoteException if a communication-related error occurs during the RMI call
     */
    //30
    void activeGames(UUID uuid) throws RemoteException;

    /**
     * Demonstrates a game scenario for the specified game instance.
     *
     * @param uuid the unique identifier of the game instance to be demonstrated
     * @throws RemoteException if an error occurs during the remote method call
     */
    //31
    void demoGame(UUID uuid) throws RemoteException;

    /**
     * Sends a simple ping request to check the connection between the client and the server.
     * This method is used to ensure that the remote server is reachable and responsive.
     *
     * @throws RemoteException if a communication-related exception occurs during the execution
     *                         of the remote method.
     */
    void ping(UUID uuid, String name) throws RemoteException;
}
