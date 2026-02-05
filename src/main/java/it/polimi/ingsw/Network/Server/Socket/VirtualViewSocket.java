package it.polimi.ingsw.Network.Server.Socket;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.VirtualView;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Utils.stateEnum;


import java.io.IOException;
import java.util.*;

public interface VirtualViewSocket extends VirtualView {
    // public void update(String g) throws Exception;

    /**
     * Handles an error within the VirtualViewSocket context. This method may be invoked
     * when an unexpected or critical issue occurs, requiring appropriate error handling
     * or reporting mechanisms.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    void error() throws Exception;

    /**
     * Sends a ping operation to verify connectivity or response time.
     *
     * This method is used to check the liveness of the connection or
     * responsiveness of a specific endpoint in the virtual view socket.
     *
     * @throws IOException if there are any issues while performing the ping operation.
     */
    void ping() throws IOException;

    /**
     * Updates the game state to reflect a new game that has been created. This includes initializing
     * game parameters such as the level, showable decks, players, hourglass count, and surrender count.
     *
     * @param level The level of the new game being created.
     * @param ShowableDecks A list of decks available and visible to players.
     * @param players The list of players participating in the game.
     * @param hourglass The amount of hourglass resources available in the game.
     * @param surrender The number of surrender opportunities remaining in the game.
     * @throws Exception If an error occurs during the update process.
     */
    void updateGameCreated(int level, List<Deck> ShowableDecks, ArrayList<Player> players, int hourglass, int surrender) throws Exception;

    /**
     * Allows the specified player to deposit the item they are currently holding.
     *
     * @param playerNickname the nickname of the player who is depositing the item in hand
     * @throws Exception if the operation cannot be completed
     */
    void depositThingInHand(String playerNickname) throws Exception;

    /**
     * Allows a player to select a tile given its index.
     * This method updates the game state to reflect the tile selection.
     *
     * @param TileIndex        the index of the tile to be selected
     * @param playerNickname   the nickname of the player making the selection
     * @throws Exception       if an error occurs during the tile selection process
     */
    void pickTileLMR(int TileIndex, String playerNickname) throws Exception;

    /**
     * Inserts a tile into the game board at a specified position and orientation for a given player.
     * It represents a game action where the tile details are provided, and the player executing the action is identified.
     *
     * @param TileIndex the index of the tile being inserted
     * @param r the row position on the board where the tile should be inserted
     * @param c the column position on the board where the tile should be inserted
     * @param rotation the rotation of the tile (in degrees or game-specific rotation unit)
     * @param playerNickname the nickname of the player performing the action
     * @throws Exception if the action cannot be completed due to invalid input or game rules
     */
    void insertTileLMR(int TileIndex, int r, int c, int rotation, String playerNickname) throws Exception;

    /**
     * This method allows a player to pick a Little Deck in the game. The deck is identified by its index, and the action is associated with a specific player.
     *
     * @param deckIndex the index of the deck to be picked
     * @param playerNickname the nickname of the player performing the action
     * @throws Exception if an error occurs during the operation
     */
    void pickLittleDeckLMR(int deckIndex, String playerNickname) throws Exception;


    /**
     * Adds an alien or human to the game board at the specified position.
     *
     * @param playerNickname the nickname of the player performing the action
     * @param wantAlien a boolean indicating whether an alien should be added (true) or not (false)
     * @param alienColor the color of the alien to be added if wantAlien is true, represented by an instance of AlienColor
     * @param row the row on the game board where the entity should be added
     * @param column the column on the game board where the entity should be added
     * @throws Exception if an error occurs during the operation
     */
    void addAlienOrHumansLMR(String playerNickname, boolean wantAlien, AlienColor alienColor, int row, int column) throws Exception;

    /**
     * Updates the position of a player on the flight board.
     *
     * @param playerNickname the nickname of the player whose position is to be set
     * @param pos the new position of the player on the flight board
     * @throws Exception if an error occurs while updating the player's position
     */
    void setPlayerPosInFlightBoard(String playerNickname, int pos) throws Exception;

    /**
     * Signals the end of the building phase for a specific player and indicates the position
     * where the player should go next.
     *
     * @param playerNickname the nickname of the player completing their building phase
     * @param positionwheretogo the position where the player is directed following the end
     *        of their building phase
     * @throws Exception if an error occurs during the operation
     */
    void endbuilding(String playerNickname, int positionwheretogo) throws Exception;


    /**
     * Ends the building phase for all players in the game session.
     * This method is responsible for signaling the termination of the building phase,
     * applying necessary logic and transitioning the game state as needed.
     *
     * @throws Exception if any error occurs while ending the building phase.
     */
    void endBuildingPhaseForAll() throws Exception;

    /**
     * Sends a penalty to a participant or a system component based on the specified type.
     *
     * @param penalty the value of the penalty to be sent
     * @param type the type or category of the penalty
     * @throws Exception if there is an issue with processing or sending the penalty
     */
    void sendPenalty(int penalty, String type) throws Exception;

    /**
     * Activates the abandoned ship effect for the specified player.
     *
     * @param player The player who has taken the abandoned ship.
     * @param posPers A 2D list containing the positions of personal effects or resources affected.
     * @throws Exception If an error occurs during the activation process.
     */
    void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers) throws Exception;

    /**
     * Notifies the system that a specific card has been used and updates the necessary components accordingly.
     *
     * @param card the card that has been used, which contains relevant information to process the card's effects
     * @throws Exception if an error occurs while handling the card update process
     */
    void updateCardUse(Card card) throws Exception;

    /**
     * Updates the state of a card in the game using the provided card state object.
     *
     * @param card the state of the card to update, represented as a c_State object
     * @throws Exception if an error occurs during the update process
     */
    void updateCardUseSTATE(c_State card) throws Exception;

    /**
     * Updates the current status of the system to the specified state.
     *
     * @param stato the new state to be set, represented as an enumeration value of {@code stateEnum}
     * @throws Exception if an error occurs while updating the status
     */
    void updateStatus(stateEnum stato) throws Exception;

    /**
     * Updates the final scores of players in the game.
     *
     * @param finalScores A map where the keys are player nicknames and the values are their final scores.
     * @throws Exception If an error occurs during the update process.
     */
    void updateFinalScores(HashMap<String, Float> finalScores) throws Exception;

    /**
     * Activates the selection logic for an abandoned station in the game.
     *
     * @param player         The player who is activating the abandoned station.
     * @param flightBoard    The flight board where the action occurs.
     * @param yOn            A boolean flag indicating a specific condition during activation.
     * @param storagetiles   A 2D ArrayList representing the storage tiles related to the station.
     * @param newgoods       A 2D ArrayList representing the new goods obtained or affected by the station activation.
     * @throws Exception     If an error occurs during the execution of the method.
     */
    void chooseAbandonedStationActivate(Player player, FlightBoard flightBoard, boolean yOn,
                                        ArrayList<ArrayList<Integer>> storagetiles,
                                        ArrayList<ArrayList<Goods>> newgoods) throws Exception;

    /**
     * Updates the positions and the number of batteries for a specific player in the game.
     *
     * @param p The player for whom the battery positions and counts are being updated.
     * @param posBatAndNumBattXPos A nested list representing the positions of the batteries and their respective counts.
     * @throws Exception If there is an error during the update process.
     */
    void updateAssertBatteriesPos(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception;

    /**
     * Completes the current building phase for an ongoing game session.
     * This method finalizes all actions associated with the building phase, ensuring
     * all necessary validations and updates are performed before transitioning to the next phase.
     *
     * @throws Exception if an error occurs during the completion of the building phase.
     */
    void completeBuildingPhase() throws Exception;

    /**
     * Updates the game state by adding goods to a player's collection in specific positions.
     *
     * @param player   The player to whom the goods will be added.
     * @param posGoods A list of positions specifying where the goods should be added.
     *                  Each inner list represents a set of positions in the player's storage or board.
     * @param goodsSets A list of sets of goods to be added. Each inner list contains goods corresponding
     *                  to positions specified in the `posGoods` parameter.
     * @throws Exception if the update cannot be completed or encounters an error.
     */
    void updateAddGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception;

    /**
     * Updates the selection of passengers a player chooses to lose based on certain consequences.
     *
     * @param player The player who is selecting passengers to lose.
     * @param c The consequences that affect the selection of passengers.
     * @param pass A nested list of integers that represents the positions of passengers to be lost.
     * @throws Exception If an error occurs during the process.
     */
    void updateChoosePassengersToLose(Player player, Consequences c, ArrayList<ArrayList<Integer>> pass) throws Exception;

    /**
     * Activates the final planet status for the players and the flight board.
     *
     * @param playersss   the list of players involved in the game
     * @param flightBoard the flight board representing the current game state
     * @throws Exception if an error occurs during the activation process
     */
    void planetFinStatActivate(ArrayList<Player> playersss, FlightBoard flightBoard) throws Exception;

    /**
     * Updates the number of lost days due to a consequence in the game for the specified player on the flight board.
     *
     * @param player The player who is affected by the consequence and the lost days.
     * @param flightBoard The flight board where the game is being played.
     * @param numDays The number of days to be marked as lost for the player.
     * @param t A flag to indicate specific circumstances of the lost days (e.g., true for certain conditions).
     * @throws Exception If an error occurs during the update process.
     */
    void updateConsequenceLostDays(Player player, FlightBoard flightBoard, int numDays, Boolean t) throws Exception;

    /**
     * Updates the state of the game by indicating that the player has lost all goods, handles the transition after
     * this event, and updates related statuses accordingly.
     *
     * @param player       The player who has lost all the goods.
     * @param finished     A boolean indicating if the process of losing goods is finished.
     * @param batttoloose  A boolean indicating if a battle caused the player to lose goods.
     * @param allbatlost   A boolean indicating if all battles have been lost.
     * @throws Exception   If an error occurs while updating the state.
     */
    void updateLooseAllGoods(Player player, Boolean finished, Boolean batttoloose, Boolean allbatlost) throws Exception;

    /**
     * Updates the calculation of smugglers based on the positions of cannons and batteries for a specific player.
     *
     * @param player The player for whom the smugglers' calculation is being updated.
     * @param cannonPos A nested list containing the positions of cannons.
     * @param batteriesPos A nested list containing the positions of batteries.
     * @throws Exception If an error occurs during the update process.
     */
    void updateSmugglersCalc(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception;

    /**
     * Updates the status of lost batteries for the player in relation to their
     * position and the number of batteries affected during an encounter with smugglers.
     *
     * @param p the player whose lost batteries status is being updated
     * @param posBatAndNumBattXPos a nested list representing the positions of batteries
     *                             and the number of batteries at each position
     * @param numbatt the total number of batteries lost
     * @throws Exception if an error occurs during the update process
     */
    void updateLostBatteriesSmug(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, int numbatt) throws Exception;

    /**
     * Updates the lost goods as a result of smuggler's activity in the game.
     *
     * @param p the player associated with the goods that were lost.
     * @param posGoods a list of positions indicating where the lost goods were located.
     * @param goodsSets a list of sets containing groupings of goods associated with the lost positions.
     * @param goodsListDiPrima a list of goods representing the initial state of the goods before the loss.
     * @throws Exception if an error occurs while processing the update.
     */
    void updateLostGoodsSmug(Player p, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, ArrayList<Goods> goodsListDiPrima) throws Exception;

    /**
     * Allows a player to choose whether to claim a reward in the context of interactions with smugglers.
     *
     * @param yOn a boolean indicating if the player agrees or declines to claim the reward.
     * @param player the Player instance representing the player making the choice.
     * @param storagetiles an ArrayList of ArrayLists of integers representing the storage tiles related to the player.
     * @param newgoods an ArrayList of ArrayLists of Goods representing new goods that may be associated with the reward.
     * @throws Exception if an error occurs while handling the player's decision or updating the game state.
     */
    void chooseToClaimRewardSmug(boolean yOn, Player player, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception;

    /**
     * Allows slavers to choose positions of cannons and batteries on the flight board.
     *
     * @param player the player who is making the selections
     * @param cannonPos a list of positions for the cannons
     * @param batteriesPos a list of positions for the batteries
     * @throws Exception if an error occurs during the selection process
     */
    void slaversChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception;

    /**
     * Allows slavers to choose passengers to lose from a player's spaceship based on the game state and provided tiles.
     *
     * @param player the player whose passengers are being chosen by the slavers
     * @param yOn a boolean flag indicating specific game conditions or choices while selecting passengers
     * @param tiles a 2D list representing the arrangement and positions of tiles where passengers can be selected or removed
     * @throws Exception if an error occurs during the process
     */
    void slaversChoosePassengersToLose(Player player, boolean yOn, ArrayList<ArrayList<Integer>> tiles) throws Exception;

    /**
     * Handles the selection of positions for cannon batteries by pirates during the game.
     * The player interacting with the method selects positions for their cannon batteries
     * based on the provided lists of available positions and other conditions within the game.
     *
     * @param player      the player who is choosing the cannon battery positions
     * @param cannonPos   the current positions of the cannons in the game
     * @param batteriesPos the positions where batteries can be placed
     * @throws Exception  if an error occurs during the selection process
     */
    void piratesChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception;

    /**
     * The method allows pirates to decide how to face meteors during the game.
     *
     * @param player                 The current player making the decision.
     * @param howToDefenceFromShots  An ArrayList of integers representing the player's chosen defense options against the meteors.
     * @param shot                   The Shot object representing the meteor or attack to be faced.
     * @param dice                   The result of a dice roll influencing the outcome of the player's decision.
     * @throws Exception             In case of any error during the execution of the method.
     */
    void piratesChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, Shot shot, int dice) throws Exception;

    /**
     * Allows pirates to choose whether to claim a reward or not.
     *
     * @param yOn a boolean indicating the choice of the pirates to claim the reward (true for claiming, false for declining).
     * @param player the player object associated with the action.
     * @throws Exception if there is an error during the execution of the operation.
     */
    void piratesChooseToClaimReward(boolean yOn, Player player) throws Exception;

    /**
     * Moves a player to a specific position on the flight board.
     *
     * @param playerNickname the nickname of the player to be moved
     * @param pos the target position on the flight board where the player will be moved
     * @throws Exception if the move cannot be performed due to any error
     */
    void movePlayerInFlightBoard(String playerNickname, int pos) throws Exception;

    /**
     * Handles the player's choice to start the motor in open space scenarios.
     *
     * @param player The player making the decision.
     * @param flightBoard The current state of the flight board in the game.
     * @param enginesPos The positions of the engines in a 2D grid structure.
     * @param batteriesPos The positions of the batteries in a 2D grid structure.
     * @throws Exception If an error occurs while processing the choice.
     */
    void openSpaceChooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception;

    /**
     * Handles the process where a player chooses how to face meteors during the meteor card phase of the game.
     *
     * @param player The player making the decision on how to face the meteors.
     * @param howToDefenceFromShots A list of integers representing the chosen defense mechanisms for each meteor shot.
     * @param shots A list of all the meteor shots relevant to the current phase.
     * @param dice An integer representing the dice roll result associated with the current meteor encounter.
     * @param currentShot An integer representing the index of the current meteor shot being resolved.
     * @throws Exception If an error occurs during the execution of this method.
     */
    void meteorCardChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, ArrayList<Shot> shots, int dice, int currentShot) throws Exception;

    /**
     * Activates the epidemic state based on the provided set of spaceship tiles.
     *
     * @param AlreadyVisited a set of {@code SpaceShipTile} objects that have already been visited.
     * @throws Exception if an error occurs during the activation process.
     */
    void epidemicStateBaseActivate(Set<SpaceShipTile> AlreadyVisited) throws Exception;

    /**
     * Triggers the "stardust effect" mechanism within the system.
     * The implementation and purpose of this effect depend on the context and the game logic.
     * This method is designed to be invoked during specific scenarios that require such activation.
     *
     * @throws Exception if an error occurs during the execution of the stardust effect.
     */
    void stardustEffect() throws Exception;

    /**
     * Updates the list of goods remaining for a specific player.
     * This method processes and reflects changes in the remaining goods
     * of the player's inventory or state. It can throw an exception
     * in case of an error during the update process.
     *
     * @param p the player whose goods are being updated
     * @param goodFInali the list of goods remaining for the player
     * @throws Exception if an error occurs while updating the goods
     */
    void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception;

    /**
     * Notifies a specific player that it is not their turn to play.
     *
     * This method should only be invoked for the player who is not currently
     * allowed to take an action and is designated by the game's turn order logic.
     *
     * @throws Exception if there is an issue notifying the player or handling
     *         the request internally.
     */
    //in questi metodi, tutti quelli con un player devono essere iniviati solo al player specifico indicato in player, non a tutti i player
    void notYourTurn() throws Exception;

    /**
     * Notifies that the timer has not been started yet.
     * This method may be called to indicate a state where an action requiring a timer cannot proceed
     * because the timer is inactive or not initialized.
     *
     * @throws Exception if an error occurs during the notification process
     */
    void timerNotStarted() throws Exception;

    /**
     * Notifies the relevant components that a tile was not flipped.
     * This method is typically invoked when there is a condition or
     * state where an expected tile flipping action does not occur,
     * signaling any necessary adjustments or responses in the system.
     *
     * @throws Exception if an error occurs while processing the notification.
     */
    void tileNotFlipped() throws Exception;

    /**
     * Informs the connected client that surrendering is not an option in the
     * current game state or context. This method ensures that no surrender
     * action is taken or considered.
     *
     * @throws Exception if there is an issue with notifying the client.
     */
    void noSurrender() throws Exception;

    /**
     * Advances the game to the next player's turn.
     *
     * This method handles the logic of switching from the current player's turn
     * to the next player in the turn order. It ensures that the game state
     * transitions correctly to allow for the next player to begin their turn.
     *
     * @throws Exception if an error occurs while transitioning to the next player's turn.
     */
    void nextPlayerTurn() throws Exception;

    /**
     * Notifies that an incorrect or invalid input has been received.
     *
     * @throws Exception if any processing error occurs while handling the invalid input.
     */
    void wrongInput() throws Exception;

    /**
     * Handles the situation where an input is identified as correct within the context of the system or game.
     * This method processes and acknowledges valid inputs appropriately to ensure the system's workflow
     * continues without disruption.
     *
     * @throws Exception if any issue occurs during the processing of the input.
     */
    void correctInput() throws Exception;

    /**
     * Indicates that the action or operation being attempted was performed by
     * an incorrect player or a player who is not allowed to perform that action.
     * This method is typically used to notify about rule or turn violations
     * specific to the current game state.
     *
     * @throws Exception if there is an issue processing the notification or a related error occurs.
     */
    void wrongPlayer() throws Exception;

    /**
     * Indicates that an effect has started within the system.
     * This method is used to notify or manage the state when an effect is initiated.
     *
     * @throws Exception if an error occurs during the start of the effect process.
     */
    void effectStarted() throws Exception;

    /**
     * Notifies that a player has won the game.
     *
     * @param playerNickname the nickname of the player who won the game
     * @throws Exception if an error occurs during the notification process
     */
    void someoneWon(String playerNickname) throws Exception;

    /**
     * Notifies the system that the specified player has reached a tie in the game.
     *
     * @param playerNickname the nickname of the player who has tied
     * @throws Exception if an error occurs during the operation
     */
    void tie(String playerNickname) throws Exception;

    /**
     * Notifies that the specified player has lost the game.
     *
     * @param playerNickname the nickname of the player who has lost
     * @throws Exception if an error occurs during the process
     */
    void lost(String playerNickname) throws Exception;

    /**
     * Notifies that the specified player has refused a reward.
     *
     * @param playerNickname The nickname of the player who refused the reward.
     * @throws Exception If an error occurs during the notification process.
     */
    void refusedReward(String playerNickname) throws Exception;

    /**
     * Signals the end of an effect in the game.
     * This method is invoked when the effect's duration or impact has concluded.
     *
     * @throws Exception if there is an issue during the transition or finalization of the effect.
     */
    void effectEnded() throws Exception;

    /**
     * Notifies the system or participants that goods have been lost.
     *
     * This method is used as part of the game's internal communication to indicate
     * that a loss of goods has occurred. It serves as a notification mechanism
     * to handle game states or interactions related to this event.
     *
     * @throws Exception if the notification fails or if any unexpected error occurs.
     */
    void lostGoods() throws Exception;

    /**
     * Triggers the handling of consequences associated with a specific event or
     * action in the game. This method is expected to manage the application of
     * penalties, adjustments, or any necessary updates resulting from the
     * occurring consequences.
     *
     * @throws Exception if an error occurs during the execution or processing
     *         of the consequences. The specific nature of the exception may vary
     *         depending on the context of the invoking scenario and the
     *         implementation details.
     */
    void youPayConsequences() throws Exception;

    /**
     * Updates the value of the dice in the game's state.
     *
     * @param dice an integer representing the new value of the dice to update.
     * @throws Exception if the update could not be performed due to an error.
     */
    void updateDice(int dice) throws Exception;

    /**
     * Indicates that a deposit action cannot be performed.
     * This method is invoked to notify the relevant entity of
     * an unsuccessful or blocked attempt to make a deposit.
     *
     * @throws Exception if an error occurs during the notification process.
     */
    void cannotDeposit() throws Exception;

    /**
     * Notifies that an insert action cannot be performed.
     *
     * This method is intended to indicate to the user or system that an insertion operation has failed
     * or is invalid due to specific conditions or constraints. The exact reasons for the failure
     * are context-dependent and should be handled accordingly by the calling class or method.
     *
     * @throws Exception if an error occurs during the notification process
     */
    void cannotInsert() throws Exception;

    /**
     * Notifies the client that the requested pick operation cannot be performed.
     *
     * @throws Exception if an error occurs while sending the notification to the client.
     */
    void cannotPick() throws Exception;

    /**
     * Indicates that a specific action or task related to filling an operation cannot be completed within the system.
     * The precise context or condition under which this method is invoked depends on the broader application logic.
     *
     * @throws Exception if an error related to the inability to fill occurs or other underlying issues are encountered.
     */
    void cannotFill() throws Exception;

    /**
     * Handles the scenario where a specific action or operation is blocked and cannot proceed.
     * This method is used to notify or manage the blocked state within the system's workflow.
     *
     * @throws Exception if an error occurs while processing the blocked state.
     */
    void blocked() throws Exception;

    /**
     * Terminates the building process for all players currently engaged in the building phase.
     * This method may trigger other processes or state updates to ensure all players
     * transition properly out of the building phase and into the next stage of the game.
     *
     * @throws Exception if an error occurs during the termination of the building process.
     */
    void stopBuilding() throws Exception;


    /**
     * Notifies that the timer has been started successfully.
     *
     * @throws Exception if an error occurs during the notification process.
     */
    void timerStarted() throws Exception;

    /**
     * Indicates that a timer has already been started.
     * This method is invoked to inform the system or notify a relevant component
     * when an attempt is made to start a timer that is already active.
     *
     * @throws Exception if an error occurs during the notification or processing of this state.
     */
    void timerAlreadyStarted() throws Exception;


    /**
     * Removes a set of spaceship tiles from the current game state for a specific player.
     *
     * @param block The list of spaceship tiles to be removed.
     * @param playerNickname The nickname of the player associated with the tiles to be removed.
     * @throws Exception If an error occurs during the removal process.
     */
    void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname) throws Exception;

    /**
     * Notifies that the specified tiles are invalid or incorrectly placed during the game.
     *
     * @param pos       The positions of the tiles that are incorrect or invalid.
     * @param nickname  The nickname of the player responsible for the placement of the tiles.
     * @param nickEff   The nickname of the player affected by the incorrect tiles, if applicable.
     * @throws Exception If an error occurs during the notification process.
     */
    void wrongTiles(ArrayList<Integer> pos, String nickname, String nickEff) throws Exception;

    /**
     * Updates the management of a received shot for a specific player, defines the defense mechanisms
     * to be used against the shot, and incorporates the effect of a dice roll.
     *
     * @param player               The player who received the shot.
     * @param shot                 The shot received that needs to be managed.
     * @param howToDefenceFromShots A list of integers determining how to defend from the shot.
     * @param dice                 The dice roll value affecting the shot and its defense.
     * @throws Exception           If an error occurs during the handling of the shot.
     */
    void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception;

    /**
     * Sends a message to the specified player, prompting them to choose a subship from the given list of subships.
     *
     * @param player The player who is required to choose a subship.
     * @param subShips A list of possible subship configurations presented to the player.
     * @throws Exception If an issue occurs while sending the message or processing the request.
     */
    void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips) throws Exception;

    /**
     * Allows a player to choose a subship from a list of available subships.
     *
     * @param playerNickname The nickname of the player making the choice.
     * @param subShips A list of available subships represented as lists of SpaceShipTile objects.
     * @param choice The index of the selected subship within the list.
     * @param waste A parameter indicating a specific value related to the selection process.
     * @throws Exception If an error occurs during the execution of the subship choice.
     */
    void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste) throws Exception;

    /**
     * Removes a single tile from the player's spaceship at the specified position.
     * This operation affects gameplay elements such as tiles, mistakes, and resource management.
     *
     * @param playerNickname the nickname of the player who is performing the removal
     * @param row the row index of the tile to be removed
     * @param column the column index of the tile to be removed
     * @param fromMistake a flag indicating if the removal is due to a mistake
     * @param waste an integer representing associated waste incurred from the removal
     * @throws Exception if an error occurs during the tile removal process
     */
    void removeSingleTile(String playerNickname, int row, int column, boolean fromMistake, int waste) throws Exception;

    /**
     * Adds a tile to the waiting list associated with a specific player.
     *
     * @param playerNickname the nickname of the player who is adding a tile to the waiting list
     * @param TileIndex the index of the tile to be added to the waiting list
     * @throws Exception if an error occurs while adding the tile to the waiting list
     */
    void addTileToWaitList(String playerNickname, int TileIndex) throws Exception;

    /**
     * Inserts a tile identified by its index from the waiting list into the game for the specified player.
     *
     * @param playerNickname the nickname of the player for whom the tile is to be inserted.
     * @param TileIndex the index of the tile to be inserted from the waiting list.
     * @throws Exception if an error occurs during the insertion process.
     */
    void insertWaitTileLMR(String playerNickname, int TileIndex) throws Exception;

    /**
     * Notifies that empty tiles in the game board need to be filled.
     *
     * This method is called to signal the requirement to fill spaces
     * that are currently vacant, ensuring that the game board remains
     * consistent and ready for further actions or gameplay.
     *
     * @throws Exception if an error occurs while processing the request.
     */
    void haveToFillEmptyTiles() throws Exception;

    /**
     * Removes a player from the flight board identified by their nickname.
     *
     * @param playerNickname the nickname of the player to be removed from the flight board
     * @throws Exception if the operation fails
     */
    void removePlayerFromFlightboard(String playerNickname) throws Exception;

    /**
     * Handles the decision of a player when encountering slavers to either claim or refuse a reward.
     *
     * @param player the player involved in the interaction with the slavers.
     * @param accept true if the player chooses to claim the reward, false if the player refuses.
     * @throws Exception if an error occurs during the process.
     */
    void slaversChooseToClaimReward(Player player, boolean accept) throws Exception;

    /**
     * Changes the identifier of the object.
     *
     * @param id the new unique identifier to set
     * @throws Exception if the operation cannot be completed
     */
    void changeID(UUID id) throws Exception;

    /**
     * Handles the event when a timer has ended.
     *
     * @param b a Boolean indicating whether the timer ended successfully (true) or encountered an error (false).
     * @throws Exception if an issue occurs during the processing of the timer ending event.
     */
    void timerEnded(Boolean b) throws Exception;

    /**
     * Displays a list of previously played games.
     *
     * @param oldGames the list of games to be displayed, represented as an ArrayList of OldGame objects
     * @throws Exception if an error occurs during the operation
     */
    void showGames(ArrayList<OldGame> oldGames) throws Exception;
}
