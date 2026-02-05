package it.polimi.ingsw.Network;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Utils.stateEnum;

import java.util.*;

public interface VirtualView {

    /**
     * Handles an error scenario within the system, triggering any necessary error-related processes or communications.
     *
     * @throws Exception if an error occurs during the handling process
     */
    void error() throws Exception;

    /**
     * Updates the unique identifier (UUID) for the current view.
     *
     * @param id the new UUID to associate with the view
     * @throws Exception if the update process fails
     */
    void changeID(UUID id) throws Exception;

    /**
     * Updates the game creation state with the specified parameters.
     *
     * @param level The level of the game being created.
     * @param showableDecks A list of decks that can be shown during the game setup.
     * @param players A list of players participating in the game.
     * @param hourglass The number of hourglass timers available.
     * @param surrender The maximum allowable number of surrenders before resolution.
     * @throws Exception If an error occurs during the update of the game creation state.
     */
    void updateGameCreated(int level, List<Deck> showableDecks, ArrayList<Player> players, int hourglass, int surrender) throws Exception;

    /**
     * Handles the action of depositing a specific item or object currently in the possession
     * of the specified player into the appropriate location or storage.
     *
     * @param playerNickname The unique identifier or name of the player who is performing
     *                       the deposit action.
     * @throws Exception If the deposit action cannot be completed due to invalid input,
     *                   player state, or other unexpected issues.
     */
    void depositThingInHand(String playerNickname) throws Exception;

    /**
     * Allows a player to pick a specific tile by specifying its index. The operation is performed
     * by the player identified by their nickname.
     *
     * @param tileIndex the index of the tile to be picked
     * @param playerNickname the nickname of the player who is picking the tile
     * @throws Exception if the operation cannot be completed
     */
    void pickTileLMR(int tileIndex, String playerNickname) throws Exception;

    /**
     * Inserts a tile into a specific position within the game board with a defined rotation,
     * while associating the action with a specific player.
     *
     * @param tileIndex the index of the tile to be inserted
     * @param row the row position where the tile will be placed
     * @param col the column position where the tile will be placed
     * @param rotation the rotation to be applied to the tile during placement
     * @param playerNickname the nickname of the player performing this action
     * @throws Exception if the tile cannot be inserted due to game rules or other constraints
     */
    void insertTileLMR(int tileIndex, int row, int col, int rotation, String playerNickname) throws Exception;

    /**
     * Allows a player to pick a small deck by specifying its index. This action
     * is associated with a specific player identified by their nickname.
     *
     * @param deckIndex       the index of the small deck to be picked
     * @param playerNickname  the nickname of the player performing the action
     * @throws Exception      if the action cannot be completed due to game-specific issues
     */
    void pickLittleDeckLMR(int deckIndex, String playerNickname) throws Exception;

    /**
     * Adds an alien or human to the specified position in the game for the given player.
     *
     * @param playerNickname the nickname of the player performing the action
     * @param wantAlien a boolean indicating whether the entity to add is an alien (true) or a human (false)
     * @param alienColor the color of the alien to add, if wantAlien is true
     * @param row the row position where the entity will be added
     * @param column the column position where the entity will be added
     * @throws Exception if there is an issue adding the entity or the inputs are invalid
     */
    void addAlienOrHumansLMR(String playerNickname, boolean wantAlien, AlienColor alienColor, int row, int column) throws Exception;

    /**
     * Sets the position of a player on the flight board.
     *
     * @param playerNickname the nickname of the player whose position is to be set
     * @param pos the new position of the player on the flight board
     * @throws Exception if an error occurs while setting the player's position
     */
    void setPlayerPosInFlightBoard(String playerNickname, int pos) throws Exception;

    /**
     * Ends the building phase for the specified player and moves them to the
     * given position on the game board or related structure.
     *
     * @param playerNickname the nickname of the player whose building phase is being ended
     * @param positionWhereToGo the position on the game board or structure where the player is to be moved
     * @throws Exception if an error occurs while ending the building phase or updating the player's position
     */
    void endbuilding(String playerNickname, int positionWhereToGo) throws Exception;

    /**
     * Ends the building phase for all active players in the game.
     * This method is responsible for transitioning the game state
     * from the building phase to the subsequent phase, ensuring that
     * all required actions related to the building phase are finalized.
     *
     * @throws Exception if an error occurs during the operation.
     */
    void endBuildingPhaseForAll() throws Exception;

    /**
     * Sends a penalty notification or applies a specific penalty based on the given parameters.
     *
     * @param penalty the value of the penalty to be applied; should typically represent the magnitude or severity.
     * @param type the type or category of the penalty to define its nature or context.
     * @throws Exception if there is an issue while sending or processing the penalty.
     */
    void sendPenalty(int penalty, String type) throws Exception;

    /**
     * Activates the "abandoned ship taken" event for the specified player and processes
     * the related positions of personnel or other elements.
     *
     * @param player  The player for whom the "abandoned ship taken" event is activated.
     * @param posPers A list of positions represented as a 2D array of integers,
     *                where each inner list indicates specific personnel positions or game-related data.
     * @throws Exception If an error occurs during the activation process.
     */
    void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers) throws Exception;

    /**
     * Updates the state of the specified card to reflect its current usage in the game.
     * This method performs any necessary updates to the card and ensures the game state is consistent with the card's effects or usage.
     *
     * @param card the card whose state is to be updated. Must not be null and must be a valid card instance in the current game context.
     * @throws Exception if the update process encounters an error or if the provided card is invalid.
     */
    void updateCardUse(Card card) throws Exception;

    /**
     * Updates the state of a card in the game with the specified c_State object.
     *
     * @param Stato the state object representing the updated state of the card
     * @throws Exception if an error occurs during the update process
     */
    void updateCardUseSTATE(c_State Stato) throws Exception;

    /**
     * Updates the current state of the game.
     *
     * @param status the new state to be set, represented as a value from the {@code stateEnum} enumeration
     * @throws Exception if an error occurs while updating the state
     */
    void updateStatus(stateEnum status) throws Exception;

    /**
     * Updates the final scores for players in the game.
     *
     * @param finalScores a HashMap where the keys are player nicknames (as Strings)
     *                    and the values are their respective final scores (as Floats).
     * @throws Exception if an error occurs while updating the scores.
     */
    void updateFinalScores(HashMap<String, Float> finalScores) throws Exception;

    /**
     * Activates the process of handling an abandoned station for the specified player.
     * The method determines the outcome based on the player's decision to accept or reject
     * the activation, updates the flight board, and manages the storage tiles and goods affected.
     *
     * @param player        The player involved in the abandoned station activation.
     * @param flightBoard   The flight board representing the current state of the game.
     * @param accept        A boolean indicating whether the player accepted (true) or rejected (false)
     *                      the activation of the abandoned station.
     * @param storageTiles  A nested list of integers representing the positions of the storage tiles
     *                      linked to the abandoned station.
     * @param newGoods      A nested list of goods objects representing the goods associated with
     *                      the abandoned station.
     * @throws Exception    If there is an issue processing the activation.
     */
    void chooseAbandonedStationActivate(Player player,
                                        FlightBoard flightBoard,
                                        boolean accept,
                                        ArrayList<ArrayList<Integer>> storageTiles,
                                        ArrayList<ArrayList<Goods>> newGoods) throws Exception;

    /**
     * Updates the positions of the active batteries for the specified player.
     *
     * @param player    the player whose batteries' positions are being updated
     * @param positions a nested list of integers representing the positions of the batteries
     * @throws Exception if an error occurs while updating the battery positions
     */
    void updateAssertBatteriesPos(Player player, ArrayList<ArrayList<Integer>> positions) throws Exception;

    /**
     * Updates the player's spaceship to add specified goods at specified positions.
     *
     * @param player The player whose goods need to be updated.
     * @param posGoods A list of lists containing coordinates where the goods should be placed on the spaceship.
     * @param goodsSets A list of lists containing the goods sets to be added at the specified positions.
     * @throws Exception If an error occurs during the update process.
     */
    void updateAddGoods(Player player,
                        ArrayList<ArrayList<Integer>> posGoods,
                        ArrayList<ArrayList<Goods>> goodsSets) throws Exception;

    /**
     * Updates the state of the game by processing the choice of which passengers
     * to lose based on the given player, consequences, and passenger data.
     *
     * @param player the player making the decision about which passengers to lose.
     * @param consequences the consequences associated with losing passengers.
     * @param passengers a 2D list representing the positions of passengers affected.
     * @throws Exception if an error occurs during processing.
     */
    void updateChoosePassengersToLose(Player player,
                                      Consequences consequences,
                                      ArrayList<ArrayList<Integer>> passengers) throws Exception;

    /**
     * Activates the planetary final statistics phase with the provided players and flight board.
     *
     * @param players      the list of players participating in the phase
     * @param flightBoard  the flight board associated with the current game process
     * @throws Exception   if an error occurs during the activation of the phase
     */
    void planetFinStatActivate(ArrayList<Player> players, FlightBoard flightBoard) throws Exception;

    /**
     * Updates the information related to the number of lost days due to a consequence in the game.
     * This might involve processing game mechanics regarding penalties or consequences
     * based on the player's and flight board's state.
     *
     * @param player The player who is affected by the consequence.
     * @param flightBoard The flight board on which the player's actions or penalties are applied.
     * @param numDays The number of days lost as a result of the consequence.
     * @param lostAll Indicates whether the player has lost all days (true for all days, false otherwise).
     * @throws Exception If an error occurs during the update process.
     */
    void updateConsequenceLostDays(Player player,
                                   FlightBoard flightBoard,
                                   int numDays,
                                   Boolean lostAll) throws Exception;

    /**
     * Updates the status of a player when they lose all goods in a particular scenario.
     *
     * @param player the player whose goods are being updated
     * @param finished indicates whether the process or scenario has concluded
     * @param batteriesLost specifies if batteries were lost during the process
     * @param allBatteriesLost specifies if all the batteries were lost
     * @throws Exception if an error occurs during the update
     */
    void updateLooseAllGoods(Player player, Boolean finished, Boolean batteriesLost, Boolean allBatteriesLost) throws Exception;


    /**
     * Updates the calculation of smugglers' effects on the game.
     *
     * @param player    the player for whom the smugglers' calculation is being updated
     * @param cannonPos a list of positions representing the cannon slots to consider
     * @param batteryPos a list of positions representing the battery slots to consider
     * @throws Exception if an error occurs during the update process
     */
    void updateSmugglersCalc(Player player,
                             ArrayList<ArrayList<Integer>> cannonPos,
                             ArrayList<ArrayList<Integer>> batteryPos) throws Exception;

    /**
     * Updates the state of lost batteries for smuggling scenarios.
     * This method is used to handle the logic related to the loss of batteries during smuggling activities.
     *
     * @param player the player whose batteries are being updated
     * @param batteryData a 2D list containing the details about the batteries, including their positions
     * @param numBatteries the total number of batteries that have been lost
     * @throws Exception if an error occurs while processing the update
     */
    void updateLostBatteriesSmug(Player player,
                                 ArrayList<ArrayList<Integer>> batteryData,
                                 int numBatteries) throws Exception;

    /**
     * Completes the current building phase in the game. This method is invoked to finalize
     * the building actions and progress to the next phase or state. It ensures that all
     * necessary operations related to the building phase are executed.
     *
     * @throws Exception if an error occurs while processing the building phase.
     */
    void completeBuildingPhase() throws Exception;

    /**
     * Updates the state of the game when a player loses goods during a smuggling event.
     *
     * @param player the player who lost goods during the smuggling event
     * @param posGoods a 2D list containing the positions of the lost goods
     * @param goodsSets a 2D list containing sets of goods affected by the smuggling event
     * @param initialGoods the initial list of goods prior to the smuggling event
     * @throws Exception if an error occurs during the update process
     */
    void updateLostGoodsSmug(Player player,
                             ArrayList<ArrayList<Integer>> posGoods,
                             ArrayList<ArrayList<Goods>> goodsSets,
                             ArrayList<Goods> initialGoods) throws Exception;

    /**
     * Handles the player's choice to claim or refuse a reward from smugglers and updates the game state accordingly.
     *
     * @param accept A boolean indicating whether the player chooses to accept the reward (true) or refuse it (false).
     * @param player The player making the decision regarding the reward.
     * @param storageTiles A nested list representing the storage tiles' positions to be updated based on the decision.
     * @param newGoods A nested list of goods associated with the reward that may be added to the player's inventory.
     * @throws Exception if an error occurs during the reward handling process or if the operation cannot be completed.
     */
    void chooseToClaimRewardSmug(boolean accept, Player player,
                                 ArrayList<ArrayList<Integer>> storageTiles,
                                 ArrayList<ArrayList<Goods>> newGoods) throws Exception;

    /**
     * Allows the slavers to choose positions for cannons and batteries during the game.
     *
     * @param player     the player who is making the selection
     * @param cannonPos  a list of positions representing cannon placements
     * @param batteryPos a list of positions representing battery placements
     * @throws Exception if the selection process encounters an error
     */
    void slaversChooseCannonBatteryPos(Player player,
                                       ArrayList<ArrayList<Integer>> cannonPos,
                                       ArrayList<ArrayList<Integer>> batteryPos) throws Exception;

    /**
     * Handles the logic for slavers to choose which passengers to lose. This decision
     * may depend on the given player, the accept flag, and the tiles specified.
     *
     * @param player The player involved in the current slavers' decision-making process.
     * @param accept A boolean indicating whether the player agrees with the slavers' decision.
     * @param tiles A 2D list of integers representing the positions of passengers or related game elements.
     * @throws Exception If any error occurs during the process.
     */
    void slaversChoosePassengersToLose(Player player,
                                       boolean accept,
                                       ArrayList<ArrayList<Integer>> tiles) throws Exception;

    /**
     * Allows pirates to choose specific positions for cannon and battery placements during the game.
     *
     * @param player the player representing the pirates making the decision
     * @param cannonPos the list of potential positions for placing cannons, represented as a nested list of integers
     * @param batteryPos the list of potential positions for placing batteries, represented as a nested list of integers
     * @throws Exception if an error occurs while processing the positions
     */
    void piratesChooseCannonBatteryPos(Player player,
                                       ArrayList<ArrayList<Integer>> cannonPos,
                                       ArrayList<ArrayList<Integer>> batteryPos) throws Exception;

    /**
     * Allows the pirate player to decide how to face incoming meteors by using the available defenses.
     *
     * @param player The player who must decide the response to the meteors.
     * @param defenses A list of the player's available defenses.
     * @param shot The shot object representing the meteor threat.
     * @param dice The value of the dice roll that may influence the outcome.
     * @throws Exception If there is an error during the decision-making process.
     */
    void piratesChooseHowToFaceMeteors(Player player,
                                       ArrayList<Integer> defenses,
                                       Shot shot,
                                       int dice) throws Exception;

    /**
     * Triggers the process where pirates decide whether to claim a reward.
     *
     * @param accept A boolean indicating whether the player accepts the reward.
     * @param player The player making the decision.
     * @throws Exception If an error occurs during the process.
     */
    void piratesChooseToClaimReward(boolean accept, Player player) throws Exception;

    /**
     * Moves the player to a specified position on the flight board.
     *
     * @param playerNickname the nickname of the player to be moved
     * @param pos the position to move the player to on the flight board
     * @throws Exception if the specified move cannot be completed
     */
    void movePlayerInFlightBoard(String playerNickname, int pos) throws Exception;

    /**
     * Allows the player to start the motors in open space based on selected engine and battery positions.
     *
     * @param player The player selecting the motor activation.
     * @param flightBoard The flight board where the action is taking place.
     * @param enginePos A list of positions specifying which engines to activate for the motor.
     * @param batteryPos A list of positions specifying which batteries to use for the motor activation.
     * @throws Exception If an error occurs during the motor activation process.
     */
    void openSpaceChooseToStartMotor(Player player,
                                     FlightBoard flightBoard,
                                     ArrayList<ArrayList<Integer>> enginePos,
                                     ArrayList<ArrayList<Integer>> batteryPos) throws Exception;

    /**
     * Handles the process of choosing how to face an incoming meteor event using defenses and shots
     * based on the provided player, dice roll, and shot sequence.
     * Determines the method of defense or impact for each meteor in the sequence.
     *
     * @param player       The player who is facing the meteors and must make decisions.
     * @param defenses     A list of integers representing the player's available defenses.
     * @param shots        A list of shots detailing the sequence in which meteors are approaching.
     * @param dice         The dice roll result influencing the meteor's trajectory or effectiveness of defenses.
     * @param currentShot  The index of the current shot in the sequence that is being processed.
     * @throws Exception   If an error occurs during the meteor handling process.
     */
    void meteorCardChooseHowToFaceMeteors(Player player,
                                          ArrayList<Integer> defenses,
                                          ArrayList<Shot> shots,
                                          int dice,
                                          int currentShot) throws Exception;

    /**
     * Activates the base state for the epidemic, processing the specified set of visited spaceship tiles.
     *
     * @param visitedTiles A set of {@code SpaceShipTile} objects representing the tiles visited during the epidemic state.
     * @throws Exception If an error occurs during the activation process.
     */
    void epidemicStateBaseActivate(Set<SpaceShipTile> visitedTiles) throws Exception;

    /**
     * Applies the Stardust effect within the game simulation.
     * This method triggers and handles the consequences or effects
     * associated with stardust interactions on the game state or players involved.
     *
     * @throws Exception if an error occurs during the execution of the stardust effect.
     */
    void stardustEffect() throws Exception;

    /**
     * Updates the remaining goods for a specific player.
     *
     * @param player          The player whose goods are being updated.
     * @param remainingGoods  The list of goods that remain available for the player.
     * @throws Exception      If an error occurs during the update process.
     */
    void updateGoodsRemaining(Player player, ArrayList<Goods> remainingGoods) throws Exception;

    /**
     * Notifies a player that it is not their turn.
     *
     * This method is intended to communicate relevant game state information
     * to a player who may attempt to take an action when it is not currently
     * their turn. It can throw an exception if an error occurs during the
     * notification process.
     *
     * @throws Exception if the notification cannot be delivered.
     */
    // Player-specific notifications
    void notYourTurn() throws Exception;

    /**
     * Indicates that the timer has not been started. This method primarily serves as a
     * notification or state update to reflect that the timer is in an inactive or uninitialized state.
     *
     * @throws Exception if an error occurs during the handling of this state.
     */
    void timerNotStarted() throws Exception;

    /**
     * Notifies the relevant system components or players that a specific tile
     * has not been flipped during the game or interaction process.
     *
     * This method could be used in scenarios where mandatory tile flipping
     * interaction does not occur, requiring appropriate handling or notifying
     * other game mechanics about the state of the tile.
     *
     * @throws Exception if an error occurs while processing the notification or during related operations
     */
    void tileNotFlipped() throws Exception;

    /**
     * Represents an action to signal that surrender is not an option or event within the game.
     * Typically used to notify the system or players when a surrender attempt is invalid or impermissible.
     *
     * @throws Exception if an error occurs while processing the no-surrender action.
     */
    void noSurrender() throws Exception;

    /**
     * Advances the game to the next player's turn.
     * This method is responsible for properly updating the game state
     * and notifying the necessary components or players about the change
     * in active player turn.
     *
     * @throws Exception if an error occurs while processing the transition
     *                   to the next player's turn.
     */
    void nextPlayerTurn() throws Exception;

    /**
     * Indicates that an invalid input was provided by a player or user.
     * This method is used to handle scenarios where input does not meet the expected
     * requirements or is otherwise incorrect. Once triggered, it aims to notify of
     * any errors regarding input validation and prompt necessary actions.
     *
     * @throws Exception if an error occurs during the handling of invalid input.
     */
    void wrongInput() throws Exception;

    /**
     * Notifies the system when a user's input is correct during a specific action or game phase.
     * This method is typically used to confirm the validation and processing of the provided data
     * and ensures the flow proceeds without issue.
     *
     * @throws Exception if an unexpected error occurs during processing of the correct input notification.
     */
    void correctInput() throws Exception;

    /**
     * Notifies the system or the relevant components when the action is triggered
     * by the wrong player. This method typically ensures that only the correct
     * player is allowed to perform specific actions based on the current game state.
     *
     * @throws Exception if there is an issue during the notification process or
     *                   if the system encounters an unexpected error while
     *                   handling the situation.
     */
    void wrongPlayer() throws Exception;

    /**
     * Indicates that the effect of a specific event or action has started
     * during the game lifecycle. This method serves as a notification or
     * trigger point for any logic to be executed when an effect begins.
     *
     * @throws Exception if the initiation of the effect encounters an issue.
     */
    void effectStarted() throws Exception;

    /**
     * Notifies that a player has won the game.
     *
     * @param playerNickname the nickname of the player who won
     * @throws Exception if an error occurs while processing the notification
     */
    void someoneWon(String playerNickname) throws Exception;

    /**
     * Notifies that the game has resulted in a tie and specifies the player involved.
     *
     * @param playerNickname the nickname of the player being notified about the tie.
     * @throws Exception if an error occurs while processing the tie notification.
     */
    void tie(String playerNickname) throws Exception;

    /**
     * Notifies that a specific player has lost.
     *
     * @param playerNickname the nickname of the player who lost
     * @throws Exception if there is an issue processing the loss for the specified player
     */
    void lost(String playerNickname) throws Exception;

    /**
     * Notifies that the player has refused a reward.
     *
     * @param playerNickname the nickname of the player who has refused the reward
     * @throws Exception if an error occurs during the process
     */
    void refusedReward(String playerNickname) throws Exception;

    /**
     * Notifies that an effect in the game has ended. This may be used as a signal
     * to update the state of the game, inform relevant players, or trigger other
     * game mechanics that depend on the completion of the effect.
     *
     * @throws Exception if there is any error during the notification process
     *                   or subsequent handling operations.
     */
    void effectEnded() throws Exception;

    /**
     * Handles the scenario where goods are considered lost within the context
     * of the virtual view. This method may be triggered in relevant game states
     * to indicate the loss of specific goods or resources.
     *
     * @throws Exception if an error occurs during the handling of lost goods
     */
    void lostGoods() throws Exception;

    /**
     * Handles the notification or execution of the consequences for the player.
     * This method generally informs the player or processes the penalties resulting
     * from a specific in-game action, event, or decision that impacts gameplay.
     *
     * @throws Exception if an error occurs while handling the consequences.
     */
    void youPayConsequences() throws Exception;

    /**
     * Updates the dice information in the system with the provided value.
     *
     * @param dice The value of the dice to update.
     * @throws Exception if the update operation encounters an issue.
     */
    void updateDice(int dice) throws Exception;

    /**
     * Notifies that a player cannot perform a deposit action in the current game state.
     * This method is intended to handle scenarios where the deposit action is restricted or invalid,
     * possibly due to game rules or specific conditions affecting the player or game environment.
     *
     * @throws Exception if an issue occurs when triggering the notification.
     */
    void cannotDeposit() throws Exception;

    /**
     * Indicates an error when an insertion operation cannot be performed.
     * This method is used to notify or handle cases where an insertion
     * is prohibited or unsuccessful in the context of the application logic.
     *
     * @throws Exception if an error occurs while handling the inability to insert.
     */
    void cannotInsert() throws Exception;

    /**
     * Notifies that a player or action is unable to pick a specified item or object.
     *
     * This method is generally called to indicate a constraint or limitation that
     * prevents the picking of an item during a specific game state or as a result
     * of certain game rules.
     *
     * @throws Exception if the notification fails or if there is an underlying issue
     *                   during the execution of this method.
     */
    void cannotPick() throws Exception;

    /**
     * Notifies that an entity cannot fill a required action or state.
     *
     * This method is intended to signal errors, limitations, or restrictions
     * that prevent a specific "fill" operation or requirement from being completed.
     *
     * @throws Exception if any issue occurs during the invocation of the notification process.
     */
    void cannotFill() throws Exception;

    /**
     * Indicates that a particular operation or action has been blocked.
     * This method may be used to notify or handle scenarios where a blocking condition occurs.
     *
     * @throws Exception if an error occurs during the execution of the blocking operation.
     */
    void blocked() throws Exception;

    /**
     * Terminates or halts the ongoing building phase or process within the application.
     * The specific behavior and consequences of stopping the building process may vary
     * depending on the current state of the system.
     *
     * @throws Exception Indicates that an error occurred while attempting to stop the building process.
     */
    void stopBuilding() throws Exception;

    /**
     * Signals that a timer has been successfully started.
     * This method is typically called when a timer-related event or action begins,
     * notifying the system or related components about the timer's activation state.
     *
     * @throws Exception if an error occurs while starting the timer or during the notification process.
     */
    void timerStarted() throws Exception;

    /**
     * Notifies that the timer has already been started and cannot be restarted.
     *
     * @throws Exception if there is an issue with notifying about the timer state.
     */
    void timerAlreadyStarted() throws Exception;

    /**
     * Removes a block of spaceship tiles associated with a particular player.
     *
     * @param block An ArrayList of SpaceShipTile objects representing the block to be removed.
     * @param playerNickname The nickname of the player whose block is being removed.
     * @throws Exception If an error occurs during the removal process.
     */
    void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname) throws Exception;

    /**
     * Notifies that the timer has ended.
     *
     * @param b a Boolean value indicating whether the timer ended successfully (true)
     *          or if there was an error or interruption (false).
     * @throws Exception if an error occurs during the processing of the timer ending.
     */
    void timerEnded(Boolean b) throws Exception;

    /**
     * Notifies about incorrect tiles based on their positions, player nickname, and the nickname
     * associated with the effect causing the error.
     *
     * @param pos      a list of positions representing the tiles that are incorrect.
     * @param nickname the nickname of the player associated with the incorrect tiles.
     * @param nickEff  the nickname of the effect associated with the error.
     * @throws Exception if an unexpected error occurs while processing the notification.
     */
    void wrongTiles(ArrayList<Integer> pos, String nickname, String nickEff) throws Exception;

    /**
     * Updates the management state for a received shot in the game.
     *
     * @param player The player who is the target of the shot.
     * @param shot The specific shot being received.
     * @param howToDefenceFromShots A list of integers representing the defense strategies to be considered.
     * @param dice The dice value rolled for the defense or impact resolution.
     * @throws Exception If any error occurs during the shot management update.
     */
    void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception;

    /**
     * Sends a message to the specified player to choose a subset of sub-ships.
     *
     * @param player The player who needs to make the selection of a sub-ship.
     * @param subShips A list of sub-ships represented by an array of SpaceShipTile objects that the player can choose from.
     * @throws Exception If an error occurs while processing the request.
     */
    void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips) throws Exception;

    /**
     * Allows a player to choose a subship from a list of available subships.
     *
     * @param playerNickname the nickname of the player making the choice
     * @param subShips a list of possible subships available for selection, represented as nested lists of SpaceShipTile objects
     * @param choice the index indicating which subship the player has chosen
     * @param waste an additional parameter for waste management during the choice process
     * @throws Exception if an error occurs while processing the subship selection
     */
    void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste) throws Exception;

    /**
     * Removes a single tile located at the specified coordinates on the spaceship grid and updates the game state accordingly.
     *
     * @param playerNickname the nickname of the player requesting the removal
     * @param row the row index of the tile to be removed
     * @param column the column index of the tile to be removed
     * @param fromMistake indicates whether the removal is due to a mistake action
     * @param waste the amount of waste generated by this removal
     * @throws Exception if the removal operation cannot be performed
     */
    void removeSingleTile(String playerNickname, int row, int column, boolean fromMistake, int waste) throws Exception;

    /**
     * Adds a tile to the waiting list for the player specified by their nickname.
     *
     * @param playerNickname the nickname of the player to whom the tile should be assigned
     * @param TileIndex the index of the tile to be added to the waiting list
     * @throws Exception if there is an issue adding the tile to the waiting list
     */
    void addTileToWaitList(String playerNickname, int TileIndex) throws Exception;

    /**
     * Inserts a waiting tile into the game for the specified player.
     *
     * @param playerNickname the nickname of the player for whom the waiting tile is being inserted
     * @param TileIndex the index of the tile to be inserted
     * @throws Exception if an error occurs during the insertion process
     */
    void insertWaitTileLMR(String playerNickname, int TileIndex) throws Exception;

    /**
     * Indicates that certain actions are required to fill empty tiles in the game.
     * This method is likely called to handle scenarios where empty tiles must be dealt with,
     * typically to ensure game continuity or enforce game rules related to tile management.
     *
     * @throws Exception if an error occurs while performing the actions to fill empty tiles.
     */
    void haveToFillEmptyTiles() throws Exception;

    /**
     * Removes a player from the flightboard based on their nickname.
     *
     * @param playerNickname The nickname of the player to be removed from the flightboard.
     * @throws Exception If an error occurs during the removal process.
     */
    void removePlayerFromFlightboard(String playerNickname) throws Exception;

    /**
     * Handles the decision made by a player regarding whether or not to claim the reward
     * in a situation involving slavers. The method processes the player's choice and
     * applies the associated consequences based on their decision.
     *
     * @param player the player involved in the decision-making process
     * @param accept a boolean indicating the player's choice; true if the player
     *               chooses to claim the reward, false if they refuse
     * @throws Exception if an error occurs during the processing of the player's decision
     */
    void slaversChooseToClaimReward(Player player, boolean accept) throws Exception;

    /**
     * Displays the list of old games to the user.
     *
     * @param oldGames the list of old games to be displayed
     * @throws Exception if an error occurs during the operation
     */
    void showGames(ArrayList<OldGame> oldGames) throws Exception;
    /**
     * Notifies the user or system that there are not enough goods available
     * to proceed with the intended operation.
     *
     * @throws Exception if an error occurs during the notification process.
     */
    void notEnoughGoods() throws Exception;

    /**
     * Updates the remaining batteries for a player.
     *
     * @param p the player whose battery count needs to be updated
     * @param batt the new battery count to be set for the player
     * @throws Exception if any error occurs during the update
     */
    void updateBatteriesRemaining(Player p, int batt)throws Exception;

    /**
     * Removes an alien entity from a specified position on the flight board.
     *
     * @param username the username of the player initiating the action
     * @param r the row coordinate from which the alien should be removed
     * @param c the column coordinate from which the alien should be removed
     * @throws Exception if the alien cannot be removed or if an error occurs
     */
    void removeAlien(String username, int r, int c) throws Exception;

    /**
     * Processes the loss or deactivation of batteries for a specific player.
     *
     * @param p The player for whom the batteries will be affected.
     * @param batteriesToAct A 2D list representing the positions or identifiers of batteries to be deactivated or lost.
     *                       Each internal list corresponds to a specific collection of affected batteries.
     * @throws Exception If an error occurs during the operation.
     */
    void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct) throws Exception;
}