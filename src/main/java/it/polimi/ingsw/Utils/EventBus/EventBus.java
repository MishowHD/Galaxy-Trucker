package it.polimi.ingsw.Utils.EventBus;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.stateEnum;

import java.util.*;

public class EventBus {
    /**
     * A list containing instances of {@link UpdateListener}.
     * This collection is used to store listeners that will be notified
     * of updates or changes in state.
     */
    private final List<UpdateListener> listeners = new ArrayList<>();
    /**
     * Represents a universally unique identifier (UUID) for this object.
     * This field serves as an immutable identifier to uniquely distinguish
     * instances of the class.
     */
    private final UUID uuid;

    /**
     * Constructs an EventBus instance with the specified unique identifier.
     *
     * @param uuid the unique identifier for this EventBus instance
     */
    public EventBus(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Registers an UpdateListener to receive updates. If the listener is already registered,
     * it will not be added again.
     *
     * @param listener the UpdateListener to be registered
     */
    public synchronized void register(UpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Ends the game session and notifies all registered listeners to disconnect.
     *
     * This method iterates through all registered {@code UpdateListener} instances
     * and invokes their {@code disconnectAll} method with the specified UUID.
     * It ensures thread safety by making the method synchronized.
     *
     * @throws Exception if any of the listeners encounter an error while disconnecting.
     */
    public synchronized void endGame() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.disconnectAll(uuid);
        }
    }

    /**
     * Notifies all registered {@code UpdateListener} instances about an error event.
     * This method iterates over the list of listeners and invokes the {@code onError}
     * method on each listener, passing the unique identifier {@code uuid}.
     *
     * @throws Exception if an error occurs during the notification process.
     */
    public synchronized void notifyError() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.onError(uuid);
        }
    }

    /**
     * Notifies all registered listeners that a game has been created with the specified parameters.
     *
     * @param level the difficulty level of the created game
     * @param ShowableDecks the list of decks that can be shown during the game
     * @param players the list of players participating in the game
     * @param hourglassResting the resting duration for the hourglass in the game
     * @param surrenderTime the allotted time for surrendering in the game
     * @throws Exception if any error occurs during the listener update
     */
    public synchronized void updateGameCreated(int level, List<Deck> ShowableDecks, ArrayList<Player> players, int hourglassResting, int surrenderTime) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateGameCreated(level, ShowableDecks, players, hourglassResting, surrenderTime, uuid);
        }
    }

    /**
     * Facilitates the action of depositing the item currently held by a player into the system.
     * This method notifies all registered update listeners about the deposit action.
     *
     * @param playerNickname the nickname of the player performing the deposit action
     * @throws Exception if any issues occur during the deposit process or notification of listeners
     */
    public synchronized void depositThingInHand(String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.depositThingInHand(playerNickname, uuid);
        }
    }

    /**
     * Handles the selection of a tile in a game by a player, notifying all registered
     * update listeners about the action.
     *
     * @param TileIndex The index of the tile being selected by the player.
     * @param playerNickname The nickname of the player selecting the tile.
     * @throws Exception If an error occurs while notifying update listeners.
     */
    public synchronized void pickTileLMR(int TileIndex, String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.pickTileLMR(TileIndex, playerNickname, uuid);
        }
    }

    /**
     * Inserts a tile into the game at the specified location and orientation, associated with a player's action.
     *
     * @param TileIndex        The index of the tile to be inserted.
     * @param r                The row where the tile is to be placed.
     * @param c                The column where the tile is to be placed.
     * @param rotation         The rotation of the tile to be applied (e.g., 0, 90, 180, 270 degrees).
     * @param playerNickname   The nickname of the player performing the action.
     * @throws Exception       If an error occurs during the insertion process.
     */
    public synchronized void insertTileLMR(int TileIndex, int r, int c, int rotation, String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.insertTileLMR(TileIndex, r, c, rotation, playerNickname, uuid);
        }
    }

    /**
     * Communicates the action of picking a little deck by a specified player to all update listeners.
     *
     * @param deckIndex      The index of the deck being picked.
     * @param playerNickname The nickname of the player picking the deck.
     * @throws Exception If an error occurs during the operation.
     */
    public synchronized void pickLittleDeckLMR(int deckIndex, String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.pickLittleDeckLMR(deckIndex, playerNickname, uuid);
        }
    }

    /**
     * Adds a request to place an alien or human on the game board at the specified location.
     * This method is synchronized to ensure thread safety during updates.
     *
     * @param playerNickname the nickname of the player making the request
     * @param wantAlien a boolean indicating if the player wants to place an alien (true) or a human (false)
     * @param alienColor the color of the alien to be placed; ignored if placing a human
     * @param row the row index where the alien or human is to be placed
     * @param column the column index where the alien or human is to be placed
     * @throws Exception if there is an error processing the request
     */
    public synchronized void addAlienOrHumansLMR(String playerNickname, boolean wantAlien, AlienColor alienColor, int row, int column) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.addAlienOrHumansLMR(playerNickname, wantAlien, alienColor, row, column, uuid);
        }
    }

    /**
     * Updates the position of a player in the flight board and notifies all registered listeners with the player's updated position.
     *
     * @param playerNickname the nickname of the player whose position is to be updated
     * @param pos the new position of the player on the flight board
     * @throws Exception if an error occurs during the update process
     */
    public synchronized void setPlayerPosInFlightBoard(String playerNickname, int pos) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.setPlayerPosInFlightBoard(playerNickname, pos, uuid);
        }
    }

    /**
     * Ends the building process for a specified player and directs them to a specified position.
     *
     * @param playerNickname the nickname of the player ending the building process
     * @param positionwheretogo the position where the player should be directed after ending the building
     * @throws Exception if there is an issue ending the building process
     */
    public synchronized void endbuilding(String playerNickname, int positionwheretogo) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.endbuilding(playerNickname, positionwheretogo, uuid);
        }
    }

    /**
     * Ends the building phase for all registered listeners.
     * This method iterates over the collection of listeners and invokes their respective
     * endBuildingPhaseForAll method, passing the unique identifier (UUID) as a parameter.
     *
     * This method is synchronized to ensure thread safety during the iteration and invocation
     * of listener methods.
     *
     * @throws Exception if any of the listener methods throw an exception during execution
     */
    public synchronized void endBuildingPhaseForAll() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.endBuildingPhaseForAll(uuid);
        }
    }

    /**
     * Sends a penalty to all registered listeners with the specified parameters.
     * This method is synchronized to ensure thread safety during execution.
     *
     * @param penalty the penalty amount or code to be sent to the listeners
     * @param type the type or category of the penalty to be sent
     * @throws Exception if an error occurs while sending the penalty to a listener
     */
    public synchronized void sendPenalty(int penalty, String type) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.sendPenalty(penalty, type, uuid);
        }
    }

    /**
     * Activates the abandoned ship taken event and notifies all registered listeners.
     *
     * @param player The player involved in the event.
     * @param posPers A 2D list representing positions and other related data for the event.
     * @throws Exception If an error occurs during the activation process.
     */
    public synchronized void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.abandonedShipTakenActivate(player, posPers, uuid);
        }
    }

    /**
     * Updates the use of the specified card by notifying all registered update listeners.
     *
     * @param card the card object to be updated
     * @throws Exception if an error occurs while updating the card
     */
    public synchronized void updateCardUse(Card card) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateCardUse(card, uuid);
        }
    }

    /**
     * Updates the card use state by notifying all registered listeners.
     * The method is synchronized to prevent concurrent modifications when multiple threads update the state.
     *
     * @param ps the list of players involved in the update
     * @param STATO the current state to be applied
     * @throws Exception if an error occurs during the update process
     */
    public synchronized void updateCardUseSTATE(ArrayList<Player> ps, c_State STATO) throws Exception {

        for (UpdateListener listener : listeners) {
            listener.updateCardUseSTATE(ps, STATO, uuid);
        }
    }

    /**
     * Updates the status by notifying all registered listeners with the provided state and UUID.
     * The method ensures thread safety by synchronizing the operation.
     *
     * @param stato the new state to be set and communicated to the listeners
     * @throws Exception if an error occurs during the operation of any registered listener
     */
    public synchronized void updateStatus(stateEnum stato) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateStatus(stato, uuid);
        }
    }

    /**
     * Updates the final scores by notifying all registered listeners with the provided scores.
     * This method is synchronized to ensure thread safety.
     *
     * @param finalScores a HashMap containing the final scores where the key is a String representing
     *                    an identifier and the value is a Float representing the score.
     * @throws Exception if an error occurs during the notification of listeners.
     */
    public synchronized void updateFinalScores(HashMap<String, Float> finalScores) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateFinalScores(finalScores, uuid);
        }
    }

    /**
     * Activates the abandoned station for the specified player and updates the necessary game components.
     *
     * @param player the player attempting to activate the abandoned station
     * @param flightBoard the flight board representing the current state of the game
     * @param yOn a boolean flag indicating specific activation conditions
     * @param storagetiles a 2D list containing the storage tiles related to the game
     * @param newgoods a 2D list containing updated goods to be incorporated during activation
     * @throws Exception if any error occurs during the activation process
     */
    public synchronized void chooseAbandonedStationActivate(Player player, FlightBoard flightBoard, boolean yOn,
                                                            ArrayList<ArrayList<Integer>> storagetiles,
                                                            ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.chooseAbandonedStationActivate(player, flightBoard, yOn, storagetiles, newgoods, uuid);
        }
    }

    /**
     * Updates the positions of batteries for a specified player and notifies all listeners
     * about this update with the provided data.
     *
     * @param p The player for whom the battery positions are being updated.
     * @param posBatAndNumBattXPos A nested list containing battery position data and the number of batteries
     *                             for a specific position.
     * @throws Exception If an error occurs during the update process.
     */
    public synchronized void updateAssertBatteriesPos(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateAssertBatteriesPos(p, posBatAndNumBattXPos, uuid);
        }
    }

    /**
     * Updates and notifies listeners about the addition of goods to a player's inventory.
     * This method is synchronized to ensure thread safety and consistency during updates.
     *
     * @param player    The player whose inventory is being updated.
     * @param posGoods  A list of position goods, where each sublist contains details
     *                  about the positions and related integers for the goods.
     * @param goodsSets A list of goods sets, where each sublist contains the goods
     *                  to be added or updated in the player's inventory.
     * @throws Exception If an error occurs during the update process, such as issues
     *                   with notifying listeners.
     */
    public synchronized void updateAddGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateAddGoods(player, posGoods, goodsSets, uuid);
        }
    }

    /**
     * Notifies all registered update listeners to handle the process of choosing passengers to lose.
     *
     * @param player the player object representing the entity making the decision.
     * @param c the consequences object that contains the outcomes related to the decision.
     * @param pass a nested list of integers representing the passengers affected by the decision.
     * @throws Exception if an error occurs while notifying the update listeners.
     */
    public synchronized void updateChoosePassengersToLose(Player player, Consequences c, ArrayList<ArrayList<Integer>> pass) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateChoosePassengersToLose(player, c, pass, uuid);
        }
    }

    /**
     * Activates the planet financial status and notifies all registered listeners.
     *
     * @param playersss     the list of players involved in the operation
     * @param flightBoard   the flight board instance associated with the operation
     * @throws Exception    if an error occurs during the activation process
     */
    public synchronized void planetFinStatActivate(ArrayList<Player> playersss, FlightBoard flightBoard) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.planetFinStatActivate(playersss, flightBoard, uuid);
        }
    }

    /**
     * Updates the consequence of lost days associated with a player and a flight board.
     * This method notifies all registered update listeners about the change.
     *
     * @param player       The player for whom the consequence of lost days needs to be updated.
     * @param flightBoard  The flight board associated with the specified player.
     * @param numDays      The number of days to be updated as lost.
     * @param t            A flag indicating the specific state or condition of the update.
     * @throws Exception   If an error occurs while notifying update listeners.
     */
    public synchronized void updateConsequenceLostDays(Player player, FlightBoard flightBoard, int numDays, Boolean t) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateConsequenceLostDays(player, flightBoard, numDays, t, uuid);
        }
    }

    /**
     * Updates the loose state of all goods for the given player.
     * This method notifies all registered listeners about the update.
     *
     * @param player the player whose goods' state needs to be updated
     * @param finished indicates if the process triggering this update is finished
     * @param batttoloose indicates if the battle is lost and goods are to be affected
     * @param allbatlost indicates if all battles are lost for the given context
     * @throws Exception if an error occurs while updating or notifying listeners
     */
    public synchronized void updateLooseAllGoods(Player player, Boolean finished, Boolean batttoloose, Boolean allbatlost) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateLooseAllGoods(player, finished, batttoloose, allbatlost, uuid);
        }
    }

    /**
     * Updates the state of the management system when a shot is received.
     * This method notifies all registered listeners with the provided details of the shot.
     *
     * @param player the player who is involved in the shot event
     * @param shot the shot object containing details about the current shot
     * @param howToDefenceFromShots a list of integers representing strategies or methods
     *                              to defend against the shot
     * @param dice an integer value representing the dice roll or random factor
     *             associated with the event
     * @throws Exception if an error occurs during the update process
     */
    public synchronized void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateManageShotReceived(player, shot, howToDefenceFromShots, dice, uuid);
        }
    }

    /**
     * Updates the smugglers calculation based on the player's state, cannon positions,
     * and battery positions. This method notifies all registered listeners about the update.
     *
     * @param player the player object containing the player's state and relevant attributes
     * @param cannonPos a list of lists representing the positions of the cannons
     * @param batteriesPos a list of lists representing the positions of the batteries
     * @throws Exception if an error occurs while updating the smugglers calculation
     */
    public synchronized void updateSmugglersCalc(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateSmugglersCalc(player, cannonPos, batteriesPos, uuid);
        }
    }

    /**
     * Updates the lost batteries information for the given player. This method
     * notifies all registered listeners about the update.
     *
     * @param p the player whose lost battery information is being updated
     * @param posBatAndNumBattXPos a nested list containing the positions of the batteries and corresponding counts
     * @param numbatt the number of batteries to update
     * @throws Exception if updating listeners encounters an issue
     */
    public synchronized void updateLostBatteriesSmug(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, int numbatt) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateLostBatteriesSmug(p, posBatAndNumBattXPos, numbatt, uuid);
        }
    }

    /**
     * Updates the lost goods and notifies all the registered listeners about the changes.
     * This method is synchronized to ensure thread safety when modifying shared resources.
     *
     * @param p the player associated with the lost goods update
     * @param posGoods a list containing the positions of the goods that are impacted
     * @param goodsSets a list of goods sets that are associated with the update
     * @param goodsListDiPrima a list of goods that are marked specifically for this update
     * @throws Exception if an error occurs during the update process or notification of listeners
     */
    public synchronized void updateLostGoodsSmug(Player p, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, ArrayList<Goods> goodsListDiPrima) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateLostGoodsSmug(p, posGoods, goodsSets, goodsListDiPrima, uuid);
        }
    }

    /**
     * Allows a player to claim a reward based on specified conditions and updates the relevant data structures.
     * The method is synchronized to ensure thread safety as it may involve shared resources.
     *
     * @param yOn a boolean indicating whether a specific condition is met to claim the reward.
     * @param player the Player object representing the player who is attempting to claim the reward.
     * @param storagetiles a two-dimensional ArrayList containing integers that represent the storage tiles status.
     * @param newgoods a two-dimensional ArrayList containing Goods objects that represent the new goods available to the player.
     * @throws Exception if an error occurs during the reward claiming operation.
     */
    public synchronized void chooseToClaimRewardSmug(boolean yOn, Player player, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.chooseToClaimRewardSmug(yOn, player, storagetiles, newgoods, uuid);
        }
    }

    /**
     * Allows slaver players to choose the positions of cannons and batteries during gameplay.
     * This method informs all registered listeners about the selected positions.
     *
     * @param player the player choosing the cannon and battery positions.
     * @param cannonPos a list of positions for the cannons, represented as a 2D list of integers.
     *                  Each inner list represents a single cannon's coordinates.
     * @param batteriesPos a list of positions for the batteries, represented as a 2D list of integers.
     *                     Each inner list represents a single battery's coordinates.
     * @throws Exception if any error occurs while processing the cannon and battery selection.
     */
    public synchronized void slaversChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.slaversChooseCannonBatteryPos(player, cannonPos, batteriesPos, uuid);
        }
    }

    /**
     * Notifies all registered listeners when a player chooses to claim a reward.
     *
     * @param player The player who is choosing to claim the reward.
     * @param yOn A boolean flag indicating a specific condition related to the reward claim.
     * @throws Exception If an error occurs during the notification process.
     */
    public synchronized void slaversChooseToClaimReward(Player player, boolean yOn) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.slaversChooseToClaimReward(player, yOn, uuid);
        }
    }

    /**
     * Notifies all registered update listeners to handle the action of slavers choosing passengers to lose.
     *
     * @param player the player involved in the action
     * @param yOn a boolean flag indicating additional state information
     * @param tiles a two-dimensional list representing tile data involved in the action
     * @throws Exception if an error occurs during the process
     */
    public synchronized void slaversChoosePassengersToLose(Player player, boolean yOn, ArrayList<ArrayList<Integer>> tiles) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.slaversChoosePassengersToLose(player, yOn, tiles, uuid);
        }
    }

    /**
     * Notifies all registered listeners that a player has chosen a cannon battery position.
     *
     * @param player the player selecting the cannon battery position
     * @param cannonPos a list of lists containing the positions of available cannons
     * @param batteriesPos a list of lists containing the positions of available batteries
     * @throws Exception if an error occurs during the notification process
     */
    public synchronized void piratesChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.piratesChooseCannonBatteryPos(player, cannonPos, batteriesPos, uuid);
        }
    }

    /**
     * This method is used by pirates to make a decision on how to face incoming meteor shots.
     * It notifies all registered listeners about the selected strategy based on the provided arguments.
     *
     * @param player The player making the decision on how to handle the meteors.
     * @param howToDefenceFromShots A list of integer values representing potential defense strategies.
     * @param shot The incoming shot that needs to be addressed by the pirates.
     * @param dice The dice roll value that may influence the decision-making process.
     * @throws Exception If any error occurs during the operation or listener notification.
     */
    public synchronized void piratesChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, Shot shot, int dice) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.piratesChooseHowToFaceMeteors(player, howToDefenceFromShots, shot, dice, uuid);
        }
    }

    /**
     * Notifies all registered update listeners that pirates have chosen to claim their reward.
     * This method is synchronized to ensure thread safety during listener notification.
     *
     * @param yOn a boolean value indicating a specific condition related to the reward claim.
     * @param player the Player object representing the player who is claiming the reward.
     * @throws Exception if any error occurs during the notification process to listeners.
     */
    public synchronized void piratesChooseToClaimReward(boolean yOn, Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.piratesChooseToClaimReward(yOn, player, uuid);
        }
    }

    /**
     * Moves a player on the flight board to the specified position.
     *
     * @param playerNickname the nickname of the player to move
     * @param pos the position to move the player to
     * @throws Exception if an error occurs during the movement process
     */
    public synchronized void movePlayerInFlightBoard(String playerNickname, int pos) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.movePlayerInFlightBoard(playerNickname, pos, uuid);
        }
    }

    /**
     * Allows a player to choose to start the motor in a game scenario.
     * This involves notifying registered listeners about the decision
     * while managing the progression of the player's turn.
     *
     * @param player The player making the decision to start the motor.
     * @param flightBoard The current state of the flight board.
     * @param enginesPos The positions of the engines as a two-dimensional list of integers.
     * @param batteriesPos The positions of the batteries as a two-dimensional list of integers.
     * @throws Exception If an error occurs during the operation.
     */
    public synchronized void openSpaceChooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        nextPlayerTurn();
        for (UpdateListener listener : listeners) {
            listener.openSpaceChooseToStartMotor(player, flightBoard, enginesPos, batteriesPos, uuid);
        }
    }

    /**
     * Executes the logic for choosing how to face meteors in the game based on the meteor card.
     * This involves notifying all registered listeners about the action.
     *
     * @param player The player making the choice on how to defend against the meteors.
     * @param howToDefenceFromShots A list representing the defense methods chosen by the player for each shot.
     * @param shots A list of shots/missiles that the player must defend against.
     * @param dice The dice roll result influencing the defense mechanics.
     * @param currentShot The index of the current shot being processed.
     * @throws Exception If an error occurs during the execution of the action.
     */
    public synchronized void meteorCardChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, ArrayList<Shot> shots, int dice, int currentShot) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.meteorCardChooseHowToFaceMeteors(player, howToDefenceFromShots, shots, dice, currentShot, uuid);
        }
    }

    /**
     * Activates the epidemic state base by notifying all the registered update listeners.
     * This method is synchronized to ensure thread-safe operations when accessing shared resources.
     *
     * @param AlreadyVisited a set of SpaceShipTile objects that have already been visited.
     *                       These tiles provide context for the base activation process.
     * @throws Exception if any exception occurs during the notification of update listeners.
     */
    public synchronized void epidemicStateBaseActivate(Set<SpaceShipTile> AlreadyVisited) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.epidemicStateBaseActivate(AlreadyVisited, uuid);
        }
    }

    /**
     * Triggers the stardust effect for all registered listeners.
     *
     * This method iterates over the list of UpdateListener objects and invokes
     * the stardustEffect method on each listener using the provided UUID.
     * It is synchronized to ensure thread safety during execution.
     *
     * @throws Exception if any listener operation fails during the stardust effect process.
     */
    public synchronized void stardustEffect() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.stardustEffect(uuid);
        }
    }

    /**
     * Updates the remaining goods for a given player and notifies all registered listeners
     * about the changes.
     *
     * @param p the player whose goods are being updated
     * @param goodFInali the list of goods that represent the updated state
     * @throws Exception if an error occurs during the update process
     */
    public synchronized void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateGoodsRemaining(p, goodFInali, uuid);
        }
    }

    /**
     * Updates the remaining batteries for a given player and notifies all registered listeners.
     *
     * @param p   the player whose battery count is to be updated
     * @param bat the updated number of batteries remaining
     * @throws Exception if an error occurs during the update process
     */
    public synchronized void updateBatteriesRemaining(Player p, int bat) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateBatteriesRemaining(p, bat, uuid);
        }
    }

    /**
     * Notifies all registered update listeners that it's not the specified player's turn.
     * This method is thread-safe.
     *
     * @param player the player for whom it is not their turn
     * @throws Exception if an error occurs while notifying listeners
     */
    public synchronized void notYourTurn(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.notYourTurn(player, uuid);
        }
    }

    /**
     * Notifies all registered listeners that the timer has not started.
     *
     * @param player the player associated with the timer that did not start
     * @throws Exception if an error occurs while notifying listeners
     */
    public synchronized void timerNotStarted(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.timerNotStarted(player, uuid);
        }
    }

    /**
     * Notifies all registered listeners that a tile has not been flipped by the specified player.
     *
     * @param player the player who did not flip the tile
     * @throws Exception if an error occurs while notifying the listeners
     */
    public synchronized void tileNotFlipped(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.tileNotFlipped(player, uuid);
        }
    }

    /**
     * Notifies all update listeners that the noSurrender action has occurred for a specific player.
     *
     * @param player the player who has initiated the noSurrender action
     * @throws Exception if an error occurs during the notification process
     */
    public synchronized void noSurrender(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.noSurrender(player, uuid);
        }
    }

    /**
     * Notifies all registered listeners that it is the next player's turn.
     *
     * This method iterates through the list of UpdateListener objects and invokes
     * their nextPlayerTurn method, passing the UUID of the current player as a parameter.
     *
     * It is synchronized to ensure thread safety during the iteration and notification process.
     *
     * @throws Exception if any listener encounters an issue while processing the
     *                   nextPlayerTurn notification.
     */
    public synchronized void nextPlayerTurn() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.nextPlayerTurn(uuid);
        }
    }

    /**
     * Notifies all registered UpdateListeners about an incorrect player input event.
     *
     * @param player the Player object associated with the wrong input event
     * @throws Exception if any listener invocation results in an exception
     */
    public synchronized void wrongInput(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.wrongInput(player, uuid);
        }
    }

    /**
     * Processes the correct input for the given player and notifies all update listeners.
     *
     * @param player the player whose input is being processed
     * @throws Exception if an error occurs during input correction
     */
    public synchronized void correctInput(Player player) throws Exception {
        nextPlayerTurn();
        for (UpdateListener listener : listeners) {
            listener.correctInput(player, uuid);
        }
    }

    /**
     * Notifies all registered listeners that the specified player is incorrect or invalid.
     *
     * @param player the player that is identified as incorrect or invalid
     * @throws Exception if an error occurs during the notification process
     */
    public synchronized void wrongPlayer(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.wrongPlayer(player, uuid);
        }
    }

    /**
     * Notifies all registered listeners that an effect has started.
     * This method iterates through the collection of listeners and invokes the
     * effectStarted method on each listener, passing the unique identifier (UUID)
     * that represents the effect.
     *
     * This method is synchronized to ensure thread safety during the notification
     * process, preventing concurrent modifications to the listener collection or
     * simultaneous calls causing unexpected behavior.
     *
     * @throws Exception if an exception occurs during the notification of any listener.
     */
    public synchronized void effectStarted() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.effectStarted(uuid);
        }
    }

    /**
     * Notifies all registered update listeners that a player has won the game.
     *
     * @param playerNickname the nickname of the player who won the game
     * @throws Exception if an error occurs during notification
     */
    public synchronized void someoneWon(String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.someoneWon(playerNickname, uuid);
        }
    }

    /**
     * Notifies all registered listeners about a tie event involving a player.
     *
     * @param playerNickname the nickname of the player involved in the tie
     * @throws Exception if an error occurs while notifying the listeners
     */
    public synchronized void tie(String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.tie(playerNickname, uuid);
        }
    }

    /**
     * Notifies all registered update listeners that the specified player has lost.
     *
     * @param playerNickname The nickname of the player who lost.
     * @throws Exception If an error occurs while notifying listeners.
     */
    public synchronized void lost(String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.lost(playerNickname, uuid);
        }
    }

    /**
     * Notifies all registered listeners that a reward has been refused by a player.
     * This method is synchronized to ensure thread safety.
     *
     * @param playerNickname the nickname of the player who refused the reward
     * @throws Exception if an error occurs while notifying the listeners
     */
    public synchronized void refusedReward(String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.refusedReward(playerNickname, uuid);
        }
    }

    /**
     * Notifies all registered listeners that the effect associated with the given UUID
     * has ended. This method is synchronized to ensure thread-safety when accessing
     * the list of listeners.
     *
     * @throws Exception if any listener operation throws an exception during notification
     */
    public synchronized void effectEnded() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.effectEnded(uuid);
        }
    }

    /**
     * Notifies all registered {@code UpdateListener} instances about a lost goods event
     * associated with a specific {@code Player}.
     *
     * @param player the {@code Player} who has lost goods
     * @throws Exception if any of the listeners encounters an issue while processing the event
     */
    public synchronized void lostGoods(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.lostGoods(player, uuid);
        }
    }

    /**
     * Executes the consequences for the specified player by notifying all registered update listeners.
     *
     * @param player The player who will face the consequences.
     * @throws Exception If an error occurs during the execution of the consequences.
     */
    public synchronized void youPayConsequences(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.youPayConsequences(player, uuid);
        }
    }

    /**
     * Updates the dice value by notifying all registered update listeners.
     *
     * @param dice the new dice value to be updated
     * @throws Exception if an error occurs during the update process
     */
    public synchronized void updateDice(int dice) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.updateDice(dice, uuid);
        }
    }

    /**
     * Notifies all registered update listeners that the specified player cannot deposit.
     *
     * @param player the player who is attempting to deposit but is not allowed
     * @throws Exception if an error occurs while notifying the listeners
     */
    public synchronized void cannotDeposit(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.cannotDeposit(player, uuid);
        }
    }

    /**
     * Notifies registered update listeners that the specified player cannot be inserted.
     * This method is synchronized to ensure thread safety when notifying listeners.
     *
     * @param player the player object that cannot be inserted
     * @throws Exception if an error occurs during the notification process
     */
    public synchronized void cannotInsert(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.cannotInsert(player, uuid);
        }
    }

    /**
     * Notifies all registered update listeners that the specified player cannot pick an item.
     *
     * @param player the player who is unable to pick an item
     * @throws Exception if an error occurs while notifying listeners
     */
    public synchronized void cannotPick(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.cannotPick(player, uuid);
        }
    }

    /**
     * Notifies all registered update listeners that the specified player cannot fill.
     *
     * @param player the player who cannot perform the fill operation
     * @throws Exception if an error occurs while notifying the listeners
     */
    public synchronized void cannotFill(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.cannotFill(player, uuid);
        }
    }

    /**
     * Invokes the blocked event for all registered listeners, notifying them that the specified player is blocked.
     * This method is synchronized to ensure thread safety while iterating through the listeners.
     *
     * @param player the player that is being blocked
     * @throws Exception if an error occurs during the notification process
     */
    public synchronized void blocked(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.blocked(player, uuid);
        }
    }

    /**
     * Stops the building process by notifying all registered listeners to halt their activities.
     * This method iterates through all the listeners in the collection and invokes
     * the stopBuilding method on each listener, using the provided UUID.
     *
     * This method is synchronized to ensure thread-safe execution, avoiding potential
     * issues caused by concurrent access to the listener collection.
     *
     * @throws Exception if any of the listeners encounter an error while stopping the building process.
     */
    public synchronized void stopBuilding() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.stopBuilding(uuid);
        }
    }

    /**
     * Notifies all registered listeners that the timer has started.
     * This method synchronizes access to ensure thread safety.
     *
     * @throws Exception if an error occurs during notification of listeners.
     */
    public synchronized void timerStarted() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.timerStarted(uuid);
        }
    }

    /**
     * Notifies all registered listeners that a timer has already been started for the specified player.
     *
     * @param player the player for whom the timer has already started
     * @throws Exception if an error occurs while notifying listeners
     */
    public synchronized void timerAlreadyStarted(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.timerAlreadyStarted(uuid, player);
        }
    }

    /**
     * Removes a block associated with a specific player identified by their nickname.
     * This method notifies all registered update listeners about the block removal.
     *
     * @param block the list of SpaceShipTile objects representing the block to be removed
     * @param playerNickname the nickname of the player whose block is being removed
     * @throws Exception if an error occurs during the removal process
     */
    public synchronized void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.removeBlock(block, playerNickname, uuid);
        }
    }

    /**
     * Notifies all registered listeners that the timer has ended.
     *
     * @param isLastActivation a boolean indicating whether this is the last activation of the timer.
     * @throws Exception if an error occurs while notifying the listeners.
     */
    public synchronized void timerEnded(boolean isLastActivation) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.timerEnded(isLastActivation, uuid);
        }
    }

    /**
     * Notifies the update listeners about incorrect tile positions for a specific player.
     *
     * @param pos The list of tile positions that are marked as incorrect.
     * @param player The player associated with the incorrect tile positions.
     * @throws Exception If an error occurs while notifying the listeners.
     */
    public synchronized void wrongTiles(ArrayList<Integer> pos, Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.wrongTiles(pos, player, uuid);
        }
    }

    /**
     * Notifies all registered {@code UpdateListener} instances that the building
     * phase has been completed. Each listener's {@code completeBuildingPhase}
     * method is called, providing the associated {@code uuid}.
     *
     * This method is synchronized to ensure thread safety when notifying listeners
     * and handling shared resources.
     *
     * @throws Exception if any error occurs during the notification process.
     */
    public synchronized void completeBuildingPhase() throws Exception {
        for (UpdateListener listener : listeners) {
            listener.completeBuildingPhase(uuid);
        }
    }

    /**
     * Sends a message to choose a subship for the given player and provides a list of subships to choose from.
     * This method notifies all registered update listeners about the subship selection event.
     *
     * @param player   The player who is required to choose a subship.
     * @param subShips A list of lists representing the available subships. Each inner list contains SpaceShipTile objects that constitute a subship.
     * @throws Exception If an error occurs during the notification to the listeners.
     */
    public synchronized void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.messageToChooseSubship(player, subShips, uuid);
        }
    }

    /**
     * Allows a player to choose a subship from a list of available subships.
     * Notifies all registered update listeners about the choice.
     *
     * @param playerNickname the nickname of the player making the choice
     * @param subShips a list of subship options available for selection
     * @param choice the index of the chosen subship within the provided list
     * @param waste a value representing the resources or actions wasted during the process
     * @throws Exception if an error occurs during the execution of the method
     */
    public synchronized void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.ChooseSubship(playerNickname, subShips, choice, waste, uuid);
        }
    }

    /**
     * Removes a single tile from the specified position on the board.
     * Notifies all registered listeners about the removal operation.
     *
     * @param playerNickname the nickname of the player performing the action
     * @param row the row index of the tile to be removed
     * @param column the column index of the tile to be removed
     * @param fromMistake a flag indicating if the removal is due to a mistake
     * @param waste the amount of waste resulting from this operation
     * @throws Exception if an error occurs during the removal process
     */
    public synchronized void removeSingleTile(String playerNickname, int row, int column, boolean fromMistake, int waste) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.removeSingleTile(playerNickname, row, column, fromMistake, waste, uuid);
        }
    }

    /**
     * Adds a tile to the waitlist for a player. This method notifies all registered update listeners
     * about the addition of the tile to the waitlist by calling their respective addTileToWaitList method.
     *
     * @param playerNickname the nickname of the player for whom the tile is being added to the waitlist
     * @param TileIndex the index of the tile being added to the waitlist
     * @throws Exception if an error occurs while notifying the listeners
     */
    public synchronized void addTileToWaitList(String playerNickname, int TileIndex) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.addTileToWaitList(playerNickname, TileIndex, uuid);
        }
    }

    /**
     * Notifies all update listeners to insert a waiting tile for a player
     * with the specified nickname at the given index.
     *
     * @param playerNickname the nickname of the player for whom the waiting tile is to be inserted
     * @param index the position at which the waiting tile is to be inserted
     * @throws Exception if an error occurs during the operation
     */
    public synchronized void insertWaitTileLMR(String playerNickname, int index) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.insertWaitTileLMR(playerNickname, index, uuid);
        }
    }

    /**
     * Notifies all registered update listeners that empty tiles need to be filled.
     *
     * @param player the player instance that triggers the need to fill empty tiles
     * @throws Exception if any update listener throws an exception during the notification process
     */
    public synchronized void haveToFillEmptyTiles(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.haveToFillEmptyTiles(player, uuid);
        }
    }

    /**
     * Removes a player from the flightboard by their nickname.
     * This method is synchronized to ensure thread-safety when modifying the flightboard.
     *
     * @param playerNickname the nickname of the player to be removed from the flightboard
     * @throws Exception if an error occurs during the removal process
     */
    public synchronized void removePlayerFromFlightboard(String playerNickname) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.removePlayerFromFlightboard(playerNickname, uuid);
        }
    }

    /**
     * Notifies all registered listeners that there are not enough goods for the specified player.
     * This method is synchronized to ensure thread safety when notifying multiple listeners.
     *
     * @param player the player object for which the notification about insufficient goods is sent
     * @throws Exception if an error occurs while notifying the listeners
     */
    public synchronized void notEnoughGoods(Player player) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.notEnoughGoods(player, uuid);
        }
    }

    /**
     * Removes an alien identified by its position from the game board and notifies all registered listeners.
     *
     * @param username the username of the player performing the removal
     * @param r the row index of the alien's position
     * @param c the column index of the alien's position
     * @throws Exception if an error occurs during the removal process
     */
    public void removeAlien(String username, int r, int c) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.removeAlien(username, r, c, uuid);
        }
    }

    /**
     * Notifies listeners about the lost batteries for a specific player.
     *
     * @param p the player who lost the batteries
     * @param batteriesToAct a nested list of integers representing the specific details about the batteries that are affected
     * @throws Exception if an error occurs during the notification process
     */
    public void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct) throws Exception {
        for (UpdateListener listener : listeners) {
            listener.lostBatteries(p, batteriesToAct, uuid);
        }
    }
}