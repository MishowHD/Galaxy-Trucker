package it.polimi.ingsw.Network.Server.RMI;

import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Network.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Utils.stateEnum;

public interface VirtualViewRMI extends Remote, VirtualView {

    /**
     * This method is used to handle errors or perform error-related operations.
     * The specific implementation details of the error handling are determined
     * by the method's implementation.
     *
     * @throws Exception if an error occurs during the execution of this method
     */
    void error() throws Exception;

    /**
     * Sends a ping request to verify communication with a remote system or service.
     * This method is typically used to check if the remote system is reachable
     * and responsive.
     *
     * @throws RemoteException if there is an issue with the remote communication
     *                         or if the remote system is unavailable.
     */
    void ping() throws RemoteException;

    /**
     * Notifies the system or a service about an impending shutdown event.
     *
     * @param message A description or reason for the shutdown that provides additional context.
     * @throws RemoteException If a remote communication error occurs during the operation.
     */
    void notifyShutdown(String message) throws RemoteException;

    /**
     * Displays a list of old games.
     *
     * @param oldGames the list of old games to be displayed
     * @throws Exception if an error occurs during the display process
     */
    void showGames(ArrayList<OldGame> oldGames) throws Exception;

    /**
     * Retrieves the name associated with the current object.
     *
     * @return the name as a String
     * @throws RemoteException if a communication-related exception occurs
     */
    String getName() throws RemoteException;

    /**
     * Updates the ID with the provided UUID.
     *
     * @param id the new UUID to replace the existing ID
     * @throws Exception if an error occurs while changing the ID
     */
    void changeID(UUID id) throws Exception;

    /**
     * Sets the name with the specified value.
     *
     * @param name the name to set
     * @throws RemoteException if a remote communication error occurs
     */
    void setName(String name) throws RemoteException;

    /**
     * Updates the game state when the game is created with the provided parameters.
     *
     * @param level          The level of the game to be set.
     * @param showableDecks  A list of decks that should be visible or playable in the game.
     * @param players        A list of players who are part of the game.
     * @param hourglass      The hourglass value representing time-related constraints.
     * @param surrender      An integer to denote some mechanism or counter related to surrender.
     * @throws Exception     If an error occurs during the update process.
     */
    @Override
    void updateGameCreated(int level, List<Deck> showableDecks, ArrayList<Player> players, int hourglass, int surrender)
            throws Exception;

    /**
     * Completes the building phase of a process or workflow. This method is typically
     * intended to finalize any construction or initialization logic that occurs during
     * the building phase. Once this method is invoked, the building phase is considered
     * complete, and any further attempts to modify the state or properties being built
     * may result in errors or undefined behavior.
     *
     * @throws Exception if an error occurs while completing the building phase. The
     * exception may indicate issues such as invalid configuration, missing dependencies,
     * or unexpected runtime conditions that prevent successful completion.
     */
    @Override
    void completeBuildingPhase() throws Exception;

    /**
     * Deposits the item currently in the player's hand.
     *
     * @param playerNickname the nickname of the player attempting to deposit the item in hand
     * @throws Exception if the deposit operation fails
     */
    @Override
    void depositThingInHand(String playerNickname) throws Exception;

    /**
     * This method is responsible for handling the process of selecting a tile
     * in a game by a specific player. The tile selection is determined by the
     * given tile index.
     *
     * @param tileIndex the index of the tile to be chosen
     * @param playerNickname the nickname of the player selecting the tile
     * @throws Exception if there is an error during the tile selection process
     */
    @Override
    void pickTileLMR(int tileIndex, String playerNickname) throws Exception;

    /**
     * Inserts a tile into the game board at the specified location with the given rotation.
     * This method is used to place a tile on the board during the game and associates it
     * with the player who initiated the action.
     *
     * @param tileIndex The index of the tile being placed, typically corresponding to
     *                  its identifier in the tile set.
     * @param row The row position on the board where the tile will be inserted.
     * @param col The column position on the board where the tile will be inserted.
     * @param rotation The rotation of the tile, which determines its orientation.
     *                 The value is typically in degrees or a format recognized by the game logic.
     * @param playerNickname The nickname of the player performing the tile placement.
     * @throws Exception If the tile placement is invalid or an error occurs during insertion.
     */
    @Override
    void insertTileLMR(int tileIndex, int row, int col, int rotation, String playerNickname)
            throws Exception;

    /**
     * Selects a smaller deck from the list of available decks using the given index,
     * associating it with the specified player's nickname.
     *
     * @param deckIndex       the index of the desired deck to pick
     * @param playerNickname  the nickname of the player selecting the deck
     * @throws Exception      if the deckIndex is invalid or the operation fails
     */
    @Override
    void pickLittleDeckLMR(int deckIndex, String playerNickname) throws Exception;

    /**
     * Adds either an alien or a human to the specified location on the game board.
     *
     * @param playerNickname the nickname of the player performing the action
     * @param wantAlien a boolean indicating whether the action involves an alien (true) or a human (false)
     * @param alienColor the color of the alien if an alien is being added
     * @param row the row position on the game board where the entity is to be added
     * @param column the column position on the game board where the entity is to be added
     * @throws Exception if there is an error during the addition of the entity
     */
    @Override
    void addAlienOrHumansLMR(String playerNickname, boolean wantAlien,
                             AlienColor alienColor, int row, int column)
            throws Exception;

    /**
     * Updates the position of a player on the flight board.
     *
     * @param playerNickname the nickname of the player whose position is being updated
     * @param pos the new position to set for the player on the flight board
     * @throws Exception if the position update fails or encountered an invalid state
     */
    @Override
    void setPlayerPosInFlightBoard(String playerNickname, int pos) throws Exception;

    /**
     * Executes the end building process for a player in the game.
     *
     * @param playerNickname the nickname of the player who is ending the building process
     * @param positionWhereToGo the target position where the player should proceed after ending the building
     * @throws Exception if an error occurs during the end building process
     */
    @Override
    void endbuilding(String playerNickname, int positionWhereToGo) throws Exception;

    /**
     * Marks the end of the building phase for all entities or components in the context
     * managed by this method. This method is typically responsible for performing finalization
     * tasks or cleanup actions once the building phase has concluded.
     *
     * @throws Exception if an error occurs during the process of ending the building phase.
     */
    @Override
    void endBuildingPhaseForAll() throws Exception;

    /**
     * Sends a penalty with the specified amount and type.
     *
     * @param penalty the penalty amount to be sent
     * @param type the type of the penalty
     * @throws Exception if an error occurs while sending the penalty
     */
    @Override
    void sendPenalty(int penalty, String type) throws Exception;

    /**
     * Activates the abandoned ship state with the specified player and position parameters.
     *
     * @param player The player object triggering the abandoned ship activation process.
     * @param posPers A 2D ArrayList containing integer values representing specific positional
     *                data used during the activation.
     * @throws Exception If any error occurs during the activation process.
     */
    @Override
    void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers)
            throws Exception;

    /**
     * Updates the usage information of the given card.
     *
     * @param card The card object containing the details to be updated.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    void updateCardUse(Card card) throws Exception;

    /**
     *
     */
    @Override
    void updateStatus(stateEnum status) throws Exception;

    /**
     * Updates the final scores for the given map of student or item identifiers and their scores.
     *
     * @param finalScores a HashMap where the key represents the identifier (e.g., student ID)
     *                    and the value represents the associated score to be updated.
     * @throws Exception if an error occurs during the update process.
     */
    @Override
    void updateFinalScores(HashMap<String, Float> finalScores) throws Exception;

    /**
     * Method that activates the selection of an abandoned station during the gameplay.
     *
     * @param player        The player who is making the selection.
     * @param flightBoard   The current state of the flight board in the game.
     * @param accept        A boolean indicating whether the player accepts the action or not.
     * @param storageTiles  A two-dimensional list representing the tiles in storage related to the station.
     * @param newGoods      A two-dimensional list of goods that are being introduced as part of the action.
     * @throws Exception    Throws an Exception if an error occurs during the process.
     */
    @Override
    void chooseAbandonedStationActivate(Player player,
                                        FlightBoard flightBoard,
                                        boolean accept,
                                        ArrayList<ArrayList<Integer>> storageTiles,
                                        ArrayList<ArrayList<Goods>> newGoods)
            throws Exception;

    /**
     * Updates the positions of the batteries associated with the given player.
     * This method ensures that the batteries are updated with the specified positions
     * and performs any necessary validations or assertions for consistency.
     *
     * @param player The player object whose batteries' positions need to be updated.
     * @param positions A list of positions represented as an ArrayList of ArrayLists of integers,
     *                  where each inner list contains the position coordinates.
     * @throws Exception If there is an error while updating the positions or if the assertions fail.
     */
    @Override
    void updateAssertBatteriesPos(Player player, ArrayList<ArrayList<Integer>> positions)
            throws Exception;

    /**
     * Updates and adds goods to the player's inventory based on the position and goods sets provided.
     *
     * @param player The player whose goods inventory is to be updated.
     * @param posGoods A two-dimensional ArrayList containing the positions of goods to be added or updated.
     * @param goodsSets A two-dimensional ArrayList containing the sets of goods corresponding to the positions in posGoods.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    void updateAddGoods(Player player,
                        ArrayList<ArrayList<Integer>> posGoods,
                        ArrayList<ArrayList<Goods>> goodsSets)
            throws Exception;

    /**
     * Updates the selection of passengers to be lost based on the provided player, consequences,
     * and current passenger list. This method is invoked to handle logic for passenger selection
     * during game state changes.
     *
     * @param player The player involved in the current context or action.
     * @param consequences The consequences object which tracks the outcomes or impacts resulting
     *                     from the selection of passengers.
     * @param passengers The nested list representing groups of passengers and their identifiers
     *                   that are subject to selection or removal.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    void updateChoosePassengersToLose(Player player,
                                      Consequences consequences,
                                      ArrayList<ArrayList<Integer>> passengers)
            throws Exception;

    /**
     * Activates the financial status of the planet based on the provided players and flight board data.
     * This method may throw an exception if the operation fails.
     *
     * @param players The list of players involved in the planetary financial status activation.
     * @param flightBoard The flight board data required for processing the activation.
     * @throws Exception If the activation process encounters an error.
     */
    @Override
    void planetFinStatActivate(ArrayList<Player> players, FlightBoard flightBoard)
            throws Exception;

    /**
     * Updates the consequence of lost days for a player on the flight board.
     *
     * @param player The player for whom the lost days consequence is being updated.
     * @param flightBoard The flight board associated with the player's game session.
     * @param numDays The number of days lost that need to be updated.
     * @param lostAll Indicates if the player has lost all days or only a specific number.
     * @throws Exception If an error occurs while updating the lost days information.
     */
    @Override
    void updateConsequenceLostDays(Player player,
                                   FlightBoard flightBoard,
                                   int numDays,
                                   Boolean lostAll)
            throws Exception;

    /**
     * Updates the state of loose goods for a player in the game.
     *
     * @param player             The player for whom the loose goods are being updated.
     * @param finished           A flag indicating if the process has been completed.
     * @param batteriesLost      A flag indicating whether any batteries have been lost.
     * @param allBatteriesLost   A flag indicating whether all batteries have been lost.
     * @throws Exception         If an error occurs during the update process.
     */
    @Override
    void updateLooseAllGoods(Player player, Boolean finished, Boolean batteriesLost,
                             Boolean allBatteriesLost) throws Exception;

    /**
     * Updates the smuggler calculation based on player information, cannon positions,
     * and battery positions.
     *
     * @param player the player object that contains relevant player data
     * @param cannonPos a nested list representing the positions of cannons
     * @param batteryPos a nested list representing the positions of batteries
     * @throws Exception if an error occurs during the calculation update process
     */
    @Override
    void updateSmugglersCalc(Player player,
                             ArrayList<ArrayList<Integer>> cannonPos,
                             ArrayList<ArrayList<Integer>> batteryPos)
            throws Exception;

    /**
     * Updates the lost batteries in the player's inventory or records.
     * This method processes the provided battery data and modifies the player's state accordingly,
     * based on the number of batteries specified.
     *
     * @param player         the Player object representing the current player whose data needs to be updated
     * @param batteryData    a 2D ArrayList containing information about the batteries, such as their identifiers or details
     * @param numBatteries   the number of batteries that need to be updated or marked as lost
     * @throws Exception     if an error occurs during the update process or if invalid arguments are provided
     */
    @Override
    void updateLostBatteriesSmug(Player player,
                                 ArrayList<ArrayList<Integer>> batteryData,
                                 int numBatteries) throws Exception;

    /**
     * Updates the records for lost goods and smugglers during game events.
     *
     * @param player       the player instance interacting with the game data
     * @param posGoods     a list of positions representing the quantities of goods
     * @param goodsSets    a list of sets describing groups of goods based on criteria
     * @param initialGoods a list of goods initially available to the player
     * @throws Exception if an error occurs during the update process
     */
    @Override
    void updateLostGoodsSmug(Player player,
                             ArrayList<ArrayList<Integer>> posGoods,
                             ArrayList<ArrayList<Goods>> goodsSets,
                             ArrayList<Goods> initialGoods)
            throws Exception;

    /**
     * Allows the player to choose whether to claim a reward smugly. This method processes the
     * player's decision and updates the storage tiles and goods accordingly.
     *
     * @param accept Indicates whether the player has chosen to accept the reward. True if the reward
     *               should be claimed, false otherwise.
     * @param player The player object representing the current player making the decision.
     * @param storageTiles The storage tiles containing the current state of stored items, represented
     *                     as an ArrayList of ArrayLists of integers.
     * @param newGoods The new goods to be added or processed based on the player's decision, represented
     *                 as an ArrayList of ArrayLists of Goods objects.
     * @throws Exception Thrown if an error occurs during the process of claiming the reward.
     */
    @Override
    void chooseToClaimRewardSmug(boolean accept, Player player,
                                 ArrayList<ArrayList<Integer>> storageTiles,
                                 ArrayList<ArrayList<Goods>> newGoods)
            throws Exception;

    /**
     * Method to allow slavers to choose positions for cannon and battery.
     *
     * @param player the player object representing the current player.
     * @param cannonPos a 2D list of integers representing potential positions for cannons.
     * @param batteryPos a 2D list of integers representing potential positions for batteries.
     * @throws Exception if an error occurs while determining positions.
     */
    @Override
    void slaversChooseCannonBatteryPos(Player player,
                                       ArrayList<ArrayList<Integer>> cannonPos,
                                       ArrayList<ArrayList<Integer>> batteryPos)
            throws Exception;

    /**
     * Handles the action of slavers deciding which passengers to lose based on the
     * game state, player decision, and provided tile data.
     *
     * @param player The player involved in the decision process.
     * @param accept A boolean indicating whether the player accepts or rejects
     *               the slavers' proposal.
     * @param tiles  A nested list of integers representing the tiles and their
     *               associated data relevant to the decision-making process.
     * @throws Exception If an error occurs during the execution of the method.
     */
    @Override
    void slaversChoosePassengersToLose(Player player,
                                       boolean accept,
                                       ArrayList<ArrayList<Integer>> tiles)
            throws Exception;

    /**
     * Allows pirates to choose positions for cannons and batteries based on the given inputs.
     *
     * @param player the player for whom the positions are chosen
     * @param cannonPos a list of cannon positions where each sublist represents coordinates
     * @param batteryPos a list of battery positions where each sublist represents coordinates
     * @throws Exception if an error occurs while determining the positions
     */
    @Override
    void piratesChooseCannonBatteryPos(Player player,
                                       ArrayList<ArrayList<Integer>> cannonPos,
                                       ArrayList<ArrayList<Integer>> batteryPos)
            throws Exception;

    /**
     * Determines the strategy pirates will use to face incoming meteors
     * based on the provided parameters.
     *
     * @param player   the player controlling the pirates who will decide the strategy
     * @param defenses a list of integers representing the defensive options available
     * @param shot     an object representing the shot or attack mechanism of the pirates
     * @param dice     an integer representing the dice roll outcome influencing decision
     * @throws Exception if the decision-making process encounters an unexpected issue
     */
    @Override
    void piratesChooseHowToFaceMeteors(Player player,
                                       ArrayList<Integer> defenses,
                                       Shot shot,
                                       int dice)
            throws Exception;

    /**
     * Allows pirates to make a choice on whether to claim a reward or not.
     *
     * @param accept Indicates whether the reward is accepted (true) or declined (false).
     * @param player The player object representing the pirate making the decision.
     * @throws Exception If there is an error during the process of claiming the reward.
     */
    @Override
    void piratesChooseToClaimReward(boolean accept, Player player)
            throws Exception;

    /**
     * Moves the specified player to a new position on the flight board.
     * This method updates the position of a player during the gameplay.
     *
     * @param playerNickname the nickname of the player to be moved
     * @param pos the new position index to move the player to
     * @throws Exception if the move cannot be completed or is invalid
     */
    @Override
    void movePlayerInFlightBoard(String playerNickname, int pos)
            throws Exception;

    /**
     * Initiates the process to start the motor in an open space scenario.
     * This method enables the player to choose a starting configuration for the motor
     * based on the positions of engines and batteries provided.
     *
     * @param player The player interacting with the flight board and making the selection.
     * @param flightBoard The flight board being used where the motor positions are configured.
     * @param enginePos A list of lists containing the positions of engines for configuration.
     * @param batteryPos A list of lists containing the positions of batteries for configuration.
     * @throws Exception If any error occurs during the motor start process.
     */
    @Override
    void openSpaceChooseToStartMotor(Player player,
                                     FlightBoard flightBoard,
                                     ArrayList<ArrayList<Integer>> enginePos,
                                     ArrayList<ArrayList<Integer>> batteryPos)
            throws Exception;

    /**
     * Determines how the player chooses to face meteors by evaluating defenses, shots, and dice values.
     *
     * @param player the player who is making the decision on how to face the meteors.
     * @param defenses a list of integers representing the available defense options for the player.
     * @param shots a list of shots that are being directed towards the player.
     * @param dice the value of the dice rolled, which might influence the decision-making process.
     * @param currentShot the index of the current shot that the player needs to address.
     * @throws Exception if an error occurs during the process of evaluating how to face the meteors.
     */
    @Override
    void meteorCardChooseHowToFaceMeteors(Player player,
                                          ArrayList<Integer> defenses,
                                          ArrayList<Shot> shots,
                                          int dice,
                                          int currentShot)
            throws Exception;

    /**
     * Activates the base state of the epidemic within the spaceship, processing the passed set
     * of tiles that have been visited. This method determines and executes actions based on
     * the epidemic state and the tiles provided.
     *
     * @param visitedTiles a set of SpaceShipTile objects that represent the tiles visited
     *                     during the epidemic control process
     * @throws Exception if an error occurs during the activation process
     */
    @Override
    void epidemicStateBaseActivate(Set<SpaceShipTile> visitedTiles)
            throws Exception;

    /**
     * Triggers the stardust effect, applying visual or computational changes
     * associated with a "stardust" theme.
     *
     * This method is designed to be overridden to implement specific behavior
     * or transformations required for a stardust effect in a subclass.
     *
     * @throws Exception if there is an error during the execution of the stardust effect.
     */
    @Override
    void stardustEffect() throws Exception;


    /**
     * Updates the remaining goods associated with a player.
     *
     * @param player the player whose goods information is being updated
     * @param remainingGoods the list of goods that remain for the player
     * @throws Exception if an error occurs during the update
     */
    @Override
    void updateGoodsRemaining(Player player, ArrayList<Goods> remainingGoods)
            throws Exception;

    /**
     * Updates the number of batteries remaining for a given player.
     *
     * @param player the player whose batteries count is to be updated
     * @param batt the new number of batteries to update for the player
     * @throws Exception if an error occurs during the update process
     */
    @Override
    void updateBatteriesRemaining(Player player, int batt)
            throws Exception;
    /**
     * This method is invoked in scenarios where an action or behavior is attempted
     * but it is not the invoking entity's turn to act. The specific implementation
     * will dictate what occurs when this method is called, typically enforcing turn-based
     * rules or restrictions in certain systems or applications.
     *
     * @throws Exception if an error occurs during execution or an invalid state is encountered.
     */
    @Override
    void notYourTurn() throws Exception;

    /**
     * This method is invoked when a timer has not been started.
     * It provides a mechanism to handle the scenario where an expected timer operation
     * has not been initiated.
     *
     * @throws Exception if an error occurs during the handling process for the unstarted timer.
     */
    @Override
    void timerNotStarted() throws Exception;

    /**
     * Handles the scenario where a tile is not flipped.
     * This method is invoked when a tile flipping action is skipped or not performed.
     *
     * @throws Exception if an error occurs while processing the tile not flipped scenario.
     */
    @Override
    void tileNotFlipped() throws Exception;

    /**
     * Overrides the noSurrender method to provide specific functionality.
     * This method is executed under certain conditions where the operation
     * requires behavior that cannot be overridden by an external force.
     *
     * @throws Exception if an error occurs during execution
     */
    @Override
    void noSurrender() throws Exception;

    /**
     * Advances the game to the next player's turn.
     * This method is responsible for handling all necessary operations
     * required to transition from the current player's turn to the next.
     *
     * @throws Exception if an error occurs during the transition to the next player's turn.
     */
    @Override
    void nextPlayerTurn() throws Exception;

    /**
     * This method is called when there is an incorrect or invalid input encountered.
     * It handles the scenarios where the input does not conform to the expected format or criteria.
     *
     * @throws Exception if an error occurs during the handling of the wrong input
     */
    @Override
    void wrongInput() throws Exception;

    /**
     * Validates and corrects the given input to ensure it meets predefined constraints or requirements.
     * This method may modify the input to align with the expected format or values.
     *
     * @throws Exception if an error occurs during validation or correction, or if the input
     *         cannot be corrected to meet the required standards.
     */
    @Override
    void correctInput() throws Exception;

    /**
     * Handles scenarios where the wrong player is detected during gameplay or game-related operations.
     * This method is invoked to execute logic that responds to invalid player actions
     * or incorrect player turn situations.
     *
     * @throws Exception if an error occurs during the handling of the wrong player logic.
     */
    @Override
    void wrongPlayer() throws Exception;

    /**
     * Called when an effect is initiated. This method is invoked to signal
     * the start of an effect's operation or lifecycle. Implementations
     * can utilize this method to perform any necessary setup or initialization
     * required when the effect begins.
     *
     * @throws Exception if an error occurs during the start of the effect.
     */
    @Override
    void effectStarted() throws Exception;

    /**
     * Executes the operations needed when a player has won the game.
     *
     * @param playerNickname the nickname of the player who won the game
     * @throws Exception if any errors occur while processing the win
     */
    @Override
    void someoneWon(String playerNickname) throws Exception;

    /**
     * Handles a tie scenario in the game by involving the specified player's nickname.
     *
     * @param playerNickname the nickname of the player involved in the tie
     * @throws Exception if an error occurs during the tie-handling process
     */
    @Override
    void tie(String playerNickname) throws Exception;

    /**
     * This method is called when a player loses the game.
     * Executes the necessary operations to handle the loss event for the specified player.
     *
     * @param playerNickname The nickname of the player who lost the game.
     * @throws Exception If an error occurs while processing the player's loss.
     */
    @Override
    void lost(String playerNickname) throws Exception;

    /**
     * This method is invoked when a player refuses a reward.
     *
     * @param playerNickname the nickname of the player who has refused the reward
     * @throws Exception if an error occurs while processing the refusal
     */
    @Override
    void refusedReward(String playerNickname) throws Exception;

    /**
     * This method is invoked when an effect or action has completed or reached its termination point.
     * It is useful for handling cleanup operations, releasing resources, or performing any logic
     * necessary after the conclusion of the effect.
     *
     * @throws Exception if an error occurs while handling the end of the effect.
     */
    @Override
    void effectEnded() throws Exception;

    /**
     * Performs operations related to lost goods.
     * This method is invoked when goods have been marked as lost or missing.
     *
     * @throws Exception if an error occurs during the process.
     */
    @Override
    void lostGoods() throws Exception;

    /**
     * Executes the "youPayConsequences" operation, which performs a specific action or process
     * that may have significant consequences or outcomes. The exact details of the operation
     * are determined by the implemented class.
     *
     * This method overrides a parent class or interface method, providing a concrete implementation.
     *
     * @throws Exception if an error occurs during the execution of the operation.
     */
    @Override
    void youPayConsequences() throws Exception;

    /**
     * Updates the value of the dice.
     *
     * @param dice the new value of the dice to be updated
     * @throws Exception if the update operation fails
     */
    @Override
    void updateDice(int dice) throws Exception;

    /**
     * This method is triggered when a deposit action cannot be completed.
     *
     * It represents a specific condition where deposit operations are restricted or have failed.
     * Implementers should invoke this method to handle such conditions appropriately and ensure
     * that any necessary logic or notifications related to the failure are executed.
     *
     * @throws Exception if an error occurs during execution of the method
     */
    @Override
    void cannotDeposit() throws Exception;

    /**
     * Indicates that an insertion operation cannot be performed.
     *
     * This method represents a scenario where an insertion is prohibited or
     * not allowed for any reason. The implementation of this method should
     * provide the specific logic or conditions under which the insertion
     * operation is restricted.
     *
     * @throws Exception if an error occurs while handling the inability to insert.
     */
    @Override
    void cannotInsert() throws Exception;

    /**
     * This method is invoked to indicate that an item cannot be picked,
     * or the operation of picking is restricted or disallowed.
     *
     * It may be used in scenarios where a picking action is attempted,
     * but certain conditions prevent the successful execution of the action.
     *
     * @throws Exception if an error occurs while handling the inability to pick.
     */
    @Override
    void cannotPick() throws Exception;

    /**
     * Indicates an operation or process that cannot be completed or fulfilled.
     * This method should be overridden to define specific behavior or logic
     * when a "cannot fill" scenario occurs.
     *
     * @throws Exception if the operation encounters an issue that prevents it from executing.
     */
    @Override
    void cannotFill() throws Exception;

    /**
     * This method is invoked when the associated operation is blocked.
     * Implementation of this method should handle the scenario where the
     * execution cannot proceed due to a blocking condition.
     *
     * @throws Exception if an error occurs during the handling of the blocked state
     */
    @Override
    void blocked() throws Exception;

    /**
     * Stops the current building process of the system or operation.
     *
     * This method is intended to halt any ongoing building tasks immediately.
     * It ensures that resources utilized during the building process are released properly
     * and the operation is safely terminated.
     *
     * @throws Exception if an error occurs while attempting to stop the building process
     */
    @Override
    void stopBuilding() throws Exception;

    /**
     * Invoked to handle the event when a timer is started.
     * This method is designed to be overridden and implemented by a subclass
     * to define specific behavior for the timer start action.
     *
     * @throws Exception if an error occurs during the execution of this method.
     */
    @Override
    void timerStarted() throws Exception;

    /**
     * This method is called when there is an attempt to start a timer that is already running.
     * It is executed to handle scenarios where a timer cannot be started again
     * because it has already been initiated and is currently active.
     *
     * @throws Exception if any issue occurs while handling the already started timer
     */
    @Override
    void timerAlreadyStarted() throws Exception;

    /**
     * Removes a block of SpaceShipTile objects associated with a specific player.
     *
     * @param block           an ArrayList of SpaceShipTile objects representing
     *                        the block to be removed.
     * @param playerNickname  the nickname of the player associated with the block
     *                        to be removed.
     * @throws Exception      if the removal operation fails or an error occurs.
     */
    @Override
    void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname)
            throws Exception;

    /**
     * This method is called when a timer has ended and performs an action based on the provided parameter.
     *
     * @param b a Boolean parameter that indicates whether the timer ended successfully or encountered an issue.
     * @throws Exception if an error occurs during the execution of the method.
     */
    void timerEnded(Boolean b) throws Exception;

    /**
     * Processes a list of tile positions and handles errors based on the provided nickname and its associated effect.
     *
     * @param pos the list of integers representing tile positions
     * @param nickname the name associated with the processing of tile positions
     * @param nickEff the effect linked to the nickname that might influence the processing
     * @throws Exception if an error occurs during the processing of tile positions
     */
    void wrongTiles(ArrayList<Integer> pos, String nickname, String nickEff) throws Exception;

    /**
     * Updates the state of the game when a shot is received, managing how the shot
     * is defended and applying any relevant effects based on the outcome of the defense.
     *
     * @param player                The player who is managing the received shot.
     * @param shot                  The shot object that the player has received.
     * @param howToDefenceFromShots A list of integers that provides the method(s)
     *                              or strategy to defend against the shot.
     * @param dice                  The dice value rolled to determine the outcome of the defense.
     * @throws Exception            Throws an exception if any error occurs during the shot management process.
     */
    void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception;

    /**
     * Sends a message to the player to choose a subship from the provided list of subships.
     *
     * @param player the player who will be prompted to choose a subship
     * @param subShips a list of subship configurations represented as a 2D list of SpaceShipTile objects
     * @throws Exception if an error occurs while processing the request
     */
    void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips) throws Exception;

    /**
     * Allows a player to choose a subset of spaceship tiles as their subship based on the provided choice.
     *
     * @param playerNickname the nickname of the player making the choice
     * @param subShips a nested list of SpaceShipTile objects representing the available subships
     * @param choice the index representing the selected subship option
     * @param waste a parameter indicating the number of tiles to discard or waste during the selection process
     * @throws Exception if an invalid choice is made or the operation cannot be completed
     */
    void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste) throws Exception;

    /**
     * Removes a single tile from a board or grid as per the provided specifications.
     *
     * @param playerNickname the nickname of the player attempting the tile removal
     * @param row the row index of the tile to be removed
     * @param column the column index of the tile to be removed
     * @param fromMistake indicates if the removal is due to a mistake
     * @param waste the amount of waste generated during the tile removal
     * @throws Exception if the tile removal fails due to invalid parameters or other conditions
     */
    void removeSingleTile(String playerNickname, int row, int column, boolean fromMistake, int waste) throws Exception;

    /**
     * Adds a tile to the waitlist for a specific player.
     *
     * @param playerNickname the nickname of the player who is adding the tile to the waitlist
     * @param TileIndex the index of the tile to be added to the waitlist
     * @throws Exception if an error occurs while adding the tile to the waitlist
     */
    void addTileToWaitList(String playerNickname, int TileIndex) throws Exception;

    /**
     * Inserts a wait tile into the specified player's game instance at the given tile index.
     * This operation may throw an exception in case of invalid inputs or if the operation
     * violates game constraints.
     *
     * @param playerNickname the nickname of the player for whom the wait tile is to be inserted
     * @param TileIndex the index at which the wait tile should be inserted
     * @throws Exception if the player nickname is invalid, the tile index is out of valid range,
     *                   or the operation cannot be completed due to game rules
     */
    void insertWaitTileLMR(String playerNickname, int TileIndex) throws Exception;

    /**
     * Ensures that any empty tiles in the data structure or user interface are
     * filled according to the required application logic. The method may perform
     * validations, calculations, or modifications to ensure the proper handling
     * of these empty tiles.
     *
     * @throws Exception if an error occurs during the process of filling empty tiles.
     */
    void haveToFillEmptyTiles() throws Exception;

    /**
     * Removes a player from the flightboard using their nickname.
     *
     * @param playerNickname the nickname of the player to be removed from the flightboard
     * @throws Exception if an error occurs during the removal process
     */
    void removePlayerFromFlightboard(String playerNickname) throws Exception;

    /**
     * Handles the action of slavers choosing whether to claim a reward.
     *
     * @param player the player object associated with the slaver's action
     * @param accept a boolean indicating if the reward is accepted (true) or declined (false)
     * @throws Exception if any issue occurs while processing the reward claiming action
     */
    void slaversChooseToClaimReward(Player player, boolean accept) throws Exception;

    /**
     * Updates the card state using the provided state.
     *
     * @param stato The state object representing the new state to update the card to.
     * @throws Exception If an error occurs during the state update process.
     */
    void updateCardUseSTATE(c_State stato) throws Exception;

    /**
     * Indicates that there are not enough goods available to complete a specific operation
     * or fulfill a requested quantity.
     *
     * @throws Exception if there is an inability to proceed due to insufficient goods.
     */
    void notEnoughGoods() throws Exception;

    /**
     * Processes the batteries that a player loses during the game.
     *
     * @param p The player object representing the current player losing batteries.
     * @param batteriesToAct A 2D list of integers representing the batteries to be acted upon.
     *                        Each sub-list contains the details necessary to handle battery loss.
     * @throws Exception If an error occurs while processing the battery loss.
     */
    void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct) throws Exception;
}
