package it.polimi.ingsw.Utils.EventBus;

import java.util.*;
import java.util.UUID;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.stateEnum;

public interface UpdateListener {

    /**
     * Handles an error condition for the specified match.
     *
     * @param matchId the unique identifier of the match associated with the error
     * @throws Exception if an error occurs during error handling
     */
    void onError(UUID matchId) throws Exception;

    /**
     * Updates the state of a game when it is created with the provided parameters.
     *
     * @param level         The current level of the game.
     * @param showableDecks A list of decks that can be shown during the game.
     * @param players       A list of players participating in the game.
     * @param hourglass     The number of hourglasses available in the game.
     * @param surrender     The surrender state of the game.
     * @param matchId       The unique identifier for the game match.
     * @throws Exception    If an error occurs during the update process.
     */
    void updateGameCreated(int level, List<Deck> showableDecks, ArrayList<Player> players, int hourglass, int surrender, UUID matchId) throws Exception;

    /**
     * Deposits the item currently held by the player into the designated storage or container
     * associated with the specific match.
     *
     * @param playerNickname the nickname of the player performing the deposit action
     * @param matchId the unique identifier of the match in which the action is being performed
     * @throws Exception if the deposit operation fails due to invalid inputs, lack of held item, or
     *                   internal system errors
     */
    void depositThingInHand(String playerNickname, UUID matchId) throws Exception;

    /**
     * Completes the building phase for a specific match.
     *
     * This method finalizes or progresses the designated building phase
     * of a match identified by its unique match ID. It may throw an exception
     * in case of errors or invalid conditions during the process.
     *
     * @param matchId The unique identifier of the match whose building phase
     *                is to be completed.
     * @throws Exception If the building phase cannot be completed successfully
     *                   due to unforeseen circumstances or invalid input.
     */
    void completeBuildingPhase(UUID matchId) throws Exception;

    /**
     * Allows a player to pick a tile in a game using the Left-Middle-Right (LMR) tile selection system.
     *
     * @param tileIndex the index of the tile to be selected
     * @param playerNickname the nickname of the player making the selection
     * @param matchId the unique identifier of the match where the tile is being selected
     * @throws Exception if the tile selection fails due to invalid parameters or game constraints
     */
    void pickTileLMR(int tileIndex, String playerNickname, UUID matchId) throws Exception;

    /**
     * Inserts a tile into the game with specified parameters. This method places the
     * tile with a given rotation at the specified row and column on the game board.
     * It also associates the move with a player's nickname and the match identifier.
     *
     * @param tileIndex       the index of the tile being inserted, representing its type or ID
     * @param r               the row on the board where the tile is to be placed
     * @param c               the column on the board where the tile is to be placed
     * @param rotation        the rotation of the tile, typically specified in degrees or steps
     * @param playerNickname  the nickname of the player making the move
     * @param matchId         the unique identifier for the match during which the move is made
     * @throws Exception      if the insertion violates game rules or conditions, or for other errors
     */
    void insertTileLMR(int tileIndex, int r, int c, int rotation, String playerNickname, UUID matchId) throws Exception;

    /**
     * Allows a player to pick a specific little deck based on the provided index.
     * This method links the player's choice of deck to a specific match.
     *
     * @param deckIndex The index of the little deck that the player wants to pick.
     * @param playerNickname The nickname of the player picking the deck.
     * @param matchId The unique identifier of the match in which the deck is being picked.
     * @throws Exception If the operation fails, such as due to invalid parameters or game state issues.
     */
    void pickLittleDeckLMR(int deckIndex, String playerNickname, UUID matchId) throws Exception;

    /**
     * Adds an alien or human character to the game based on the specified parameters.
     *
     * @param playerNickname the nickname of the player who is adding the character
     * @param wantAlien true if an alien is being added, false if a human is being added
     * @param alienColor the color of the alien to be added, ignored if adding a human
     * @param row the row position where the character should be added
     * @param column the column position where the character should be added
     * @param matchId the unique identifier of the match in which the character is to be added
     * @throws Exception if the addition fails due to invalid parameters or game constraints
     */
    void addAlienOrHumansLMR(String playerNickname, boolean wantAlien, AlienColor alienColor, int row, int column, UUID matchId) throws Exception;

    /**
     * Sends a message to the player prompting them to choose a submarine ship from the provided options.
     *
     * @param player the player to whom the message is sent
     * @param subShips a list of lists containing SpaceShipTile objects representing the available submarine ship options
     * @param uuid the unique identifier associated with the submarine ship selection
     * @throws Exception if an error occurs during the process
     */
    void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips, UUID uuid) throws Exception;

    /**
     * Allows a player to choose a subship from a given list of subships based on their selection.
     *
     * @param playerNickname the nickname of the player making the choice
     * @param subShips a list of lists containing the subships available for selection
     * @param choice the index of the subship chosen by the player
     * @param waste the value representing unwanted or unused parts during the process
     * @param uuid the unique identifier associated with the process or session
     * @throws Exception if the selection is invalid or an error occurs during the process
     */
    void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste, UUID uuid) throws Exception;

    /**
     * Removes a single tile from the game board based on the specified parameters.
     *
     * @param playerNickname the nickname of the player performing the operation
     * @param row the row index of the tile to be removed
     * @param column the column index of the tile to be removed
     * @param fromMistake indicates whether the removal is due to a mistake
     * @param waste the waste value associated with the removal process
     * @param uuid the unique identifier for the tile removal instance
     * @throws Exception if the tile removal fails due to invalid inputs or other errors
     */
    void removeSingleTile(String playerNickname, int row, int column, boolean fromMistake, int waste, UUID uuid) throws Exception;

    /**
     * Adds a tile to the waitlist for a specified player.
     *
     * @param playerNickname the nickname of the player to whom the tile will be added
     * @param TileIndex the index of the tile to be added
     * @param uuid the unique identifier associated with the tile
     * @throws Exception if an error occurs while adding the tile to the waitlist
     */
    void addTileToWaitList(String playerNickname, int TileIndex, UUID uuid) throws Exception;

    /**
     * Updates the player's position on the flight board within a specific match.
     *
     * @param playerNickname the nickname of the player whose position is to be updated
     * @param pos the new position of the player on the flight board
     * @param matchId the unique identifier of the match in which the player's position is being updated
     * @throws Exception if an error occurs while updating the player's position
     */
    void setPlayerPosInFlightBoard(String playerNickname, int pos, UUID matchId) throws Exception;

    /**
     * Ends the current building phase for a player in a match and transitions to the specified position.
     *
     * @param playerNickname the nickname of the player ending the building phase
     * @param positionWhereToGo the position to which the player will transition after ending the building phase
     * @param matchId the unique identifier of the match where this action is taking place
     * @throws Exception if an error occurs during the operation
     */
    void endbuilding(String playerNickname, int positionWhereToGo, UUID matchId) throws Exception;

    /**
     * Ends the building phase for all participants in the match identified by the given match ID.
     *
     * @param matchId The unique identifier of the match for which the building phase needs to be ended.
     * @throws Exception If an error occurs while ending the building phase.
     */
    void endBuildingPhaseForAll(UUID matchId) throws Exception;

    /**
     * Sends a penalty for a specific match and type.
     *
     * @param penalty the value of the penalty to be sent
     * @param type the type of the penalty (e.g., foul, misconduct)
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs while sending the penalty
     */
    void sendPenalty(int penalty, String type, UUID matchId) throws Exception;


    /**
     * Activates the abandoned ship logic when a specific condition is met.
     *
     * @param player The player triggering the abandoned ship activation.
     * @param posPers A 2D list representing position and persistence data required for the activation process.
     * @param matchId The unique identifier of the ongoing match.
     * @throws Exception If an error occurs during the activation process.
     */
    void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers, UUID matchId) throws Exception;

    /**
     * Updates the usage information of a card in the context of a specific match.
     *
     * @param card    the card object that needs its usage details updated
     * @param matchId the unique identifier of the match in which the card usage is being updated
     * @throws Exception if the update operation fails
     */
    void updateCardUse(Card card, UUID matchId) throws Exception;

    /**
     * Updates the state of a card for the given players within a specific match.
     *
     * @param ps       the list of players whose card state needs to be updated
     * @param stato    the state to be applied to the card
     * @param matchId  the unique identifier of the match where the operation is performed
     * @throws Exception if an error occurs during the state update process
     */
    void updateCardUseSTATE(ArrayList<Player> ps, c_State stato, UUID matchId) throws Exception;

    /**
     * Updates the status of a specific match identified by its UUID.
     *
     * @param stato the new status to be set for the match, represented by a stateEnum value
     * @param matchId the unique identifier of the match whose status is to be updated
     * @throws Exception if an error occurs during the status update
     */
    void updateStatus(stateEnum stato, UUID matchId) throws Exception;

    /**
     * Updates the final scores of a match with the given scores.
     *
     * @param finalScores A HashMap where the key represents the participant's identifier
     *                    and the value represents their final score.
     * @param matchId The unique identifier of the match for which the scores are being updated.
     * @throws Exception If there is an error during the update process.
     */
    void updateFinalScores(HashMap<String, Float> finalScores, UUID matchId) throws Exception;

    /**
     * Activates an abandoned station by processing the relevant game mechanics
     * including updates to the player's status, flight board configuration,
     * storage tiles, and goods allocation based on the match context.
     *
     * @param player The player for whom the abandoned station is being activated.
     * @param flightBoard The current state of the flight board in the game.
     * @param yOn A boolean indicating whether a specific condition (denoted by 'yOn') is active.
     * @param storageTiles A 2D list representing the storage tiles affected by the activation.
     * @param newGoods A 2D list containing new goods to be allocated as a result of the activation.
     * @param matchId The unique identifier for the current match.
     * @throws Exception If an error occurs during the activation process.
     */
    void chooseAbandonedStationActivate(Player player, FlightBoard flightBoard, boolean yOn,
                                        ArrayList<ArrayList<Integer>> storageTiles,
                                        ArrayList<ArrayList<Goods>> newGoods,
                                        UUID matchId) throws Exception;

    /**
     * Updates and asserts the positions of batteries for a given player in a match.
     *
     * @param player the player whose battery positions are to be updated and verified
     * @param posBatAndNumBattXPos a nested list containing battery positions and the corresponding count for each position
     * @param matchId the unique identifier for the match in which the updates are to occur
     * @throws Exception if an error occurs during the update or assertion of battery positions
     */
    void updateAssertBatteriesPos(Player player, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, UUID matchId) throws Exception;

    /**
     * Updates the goods associated with a particular player and match. This method
     * processes the goods at specified positions and updates the player's current
     * goods list with the new goods provided.
     *
     * @param player The player whose goods are being updated.
     * @param posGoods A nested list containing the positions of goods that
     *                 correspond to the specified match.
     * @param goodsSets A nested list of goods that are to be added or updated
     *                  for the player.
     * @param matchId The unique identifier for the match which the goods update
     *                pertains to.
     * @throws Exception Throws an exception if there is an issue during the
     *                   processing or updating of goods.
     */
    void updateAddGoods(Player player, ArrayList<ArrayList<Integer>> posGoods,
                        ArrayList<ArrayList<Goods>> goodsSets,
                        UUID matchId) throws Exception;

    /**
     * Updates the state of the game by choosing the passengers to be lost based on the
     * provided parameters. This method modifies the game's logic for a specific player,
     * consequences, and a given match.
     *
     * @param player the player for whom the passengers are being determined
     * @param c the consequences that need to be considered for the loss of passengers
     * @param pass an ArrayList of ArrayLists representing the passengers to choose from
     * @param matchId the unique identifier for the match being updated
     * @throws Exception if there is an error during the update process
     */
    void updateChoosePassengersToLose(Player player, Consequences c, ArrayList<ArrayList<Integer>> pass, UUID matchId) throws Exception;

    /**
     * Activates the financial status update procedure for a planet based on the
     * provided players, flight board data, and match identifier.
     *
     * @param players an ArrayList of Player objects representing the current players in the match
     * @param flightBoard the FlightBoard object containing current flight information
     * @param matchId the UUID identifying the current match
     * @throws Exception if an error occurs during the activation process
     */
    void planetFinStatActivate(ArrayList<Player> players, FlightBoard flightBoard, UUID matchId) throws Exception;

    /**
     * Updates the number of lost days as a consequence for a player's action in a specific game match.
     *
     * @param player      The player whose lost days need to be updated.
     * @param flightBoard The flight board associated with the game state.
     * @param numDays     The number of days to be added or removed from the player's lost days.
     * @param t           A flag indicating the type of update to perform.
     * @param matchId     The unique identifier for the match in which the update is to be made.
     * @throws Exception  If there is an error while updating the lost days.
     */
    void updateConsequenceLostDays(Player player, FlightBoard flightBoard, int numDays, Boolean t, UUID matchId) throws Exception;

    /**
     * Updates the status of all loose goods in the context of a game or match.
     *
     * @param player      the player for whom the update is being performed
     * @param finished    indicates if the current game or process has finished
     * @param batToLoose  specifies if the bat is to be considered loose
     * @param allBatLost  indicates if all bats have been lost
     * @param matchId     the unique identifier for the match
     * @throws Exception  if any error occurs during the execution of the update
     */
    void updateLooseAllGoods(Player player, Boolean finished, Boolean batToLoose, Boolean allBatLost, UUID matchId) throws Exception;

    /**
     * Updates the state of the game when a shot is received by a player during a match.
     *
     * @param player The player who is the target of the received shot.
     * @param shot The details of the shot that was received.
     * @param howToDefenceFromShots A list of integers representing the defense strategies or methods available for the player to counter the shot.
     * @param dice The value obtained from a simulated dice roll, which may impact the outcome of the defense strategy.
     * @param matchId The unique identifier of the match in which the shot was received.
     * @throws Exception If an error occurs during the update process or if invalid input parameters are provided.
     */
    void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice, UUID matchId) throws Exception;

    /**
     * Updates the smugglers' calculations based on the current game state.
     *
     * @param player       The player object initiating the update.
     * @param cannonPos    A nested ArrayList containing positions of cannons in the game.
     * @param batteriesPos A nested ArrayList containing positions of battery defenses in the game.
     * @param matchId      The unique identifier for the current match.
     * @throws Exception   If an error occurs during the update process.
     */
    void updateSmugglersCalc(Player player, ArrayList<ArrayList<Integer>> cannonPos,
                             ArrayList<ArrayList<Integer>> batteriesPos,
                             UUID matchId) throws Exception;

    /**
     * Updates the status of lost batteries in the player's game state.
     *
     * @param player              The player whose batteries are being updated.
     * @param posBatAndNumBattXPos A nested list containing battery positions and the number of batteries at each position.
     * @param numBatt             The number of batteries lost to be updated.
     * @param matchId             The unique identifier for the match in which the update is taking place.
     * @throws Exception          If an error occurs during the update process.
     */
    void updateLostBatteriesSmug(Player player,
                                 ArrayList<ArrayList<Integer>> posBatAndNumBattXPos,
                                 int numBatt,
                                 UUID matchId) throws Exception;

    /**
     * Updates the lost goods for a smuggling event in a game.
     *
     * @param player The player involved in the smuggling event.
     * @param posGoods A list of positions for goods represented as an ArrayList of ArrayLists of integers.
     * @param goodsSets A list of goods sets represented as an ArrayList of ArrayLists of Goods objects.
     * @param goodsListDiPrima A list of goods related to the primary set of items.
     * @param matchId The unique identifier for the game match.
     * @throws Exception If an error occurs during the update process.
     */
    void updateLostGoodsSmug(Player player,
                             ArrayList<ArrayList<Integer>> posGoods,
                             ArrayList<ArrayList<Goods>> goodsSets,
                             ArrayList<Goods> goodsListDiPrima,
                             UUID matchId) throws Exception;

    /**
     * Allows a player to claim a reward smugly based on game conditions.
     *
     * @param yOn           A boolean representing whether the player has enabled the smug reward claim option.
     * @param player        The Player object representing the current player attempting to claim the reward.
     * @param storageTiles  A 2D list of integers representing the storage tiles in the game related to the claiming process.
     * @param newGoods      A 2D list of goods representing new goods to be considered during the reward claim process.
     * @param matchId       A unique identifier for the match in which the reward claim is taking place.
     * @throws Exception    If an error occurs during the claim process due to invalid game state or rules violations.
     */
    void chooseToClaimRewardSmug(boolean yOn, Player player,
                                 ArrayList<ArrayList<Integer>> storageTiles,
                                 ArrayList<ArrayList<Goods>> newGoods,
                                 UUID matchId) throws Exception;

    /**
     * Determines the cannon battery positions for the slavers during the game.
     *
     * @param player       The player representing the slaver choosing the cannon battery positions.
     * @param cannonPos    The list of positions for the cannon units.
     * @param batteriesPos The list of positions for the battery units.
     * @param matchId      The unique identifier for the current match.
     * @throws Exception   If an error occurs during the process of choosing positions.
     */
    void slaversChooseCannonBatteryPos(Player player,
                                       ArrayList<ArrayList<Integer>> cannonPos,
                                       ArrayList<ArrayList<Integer>> batteriesPos,
                                       UUID matchId) throws Exception;

    /**
     * Handles the logic for slavers choosing passengers to lose in the game.
     *
     * @param player The player object representing the current player interacting with the game.
     * @param yOn A boolean flag indicating whether a certain game condition or state is active.
     * @param tiles A 2D ArrayList of integers representing the game board or tile configuration.
     * @param matchId The unique identifier for the current game match.
     * @throws Exception If any error or invalid operation occurs during execution.
     */
    void slaversChoosePassengersToLose(Player player, boolean yOn,
                                       ArrayList<ArrayList<Integer>> tiles,
                                       UUID matchId) throws Exception;

    /**
     * Processes the action where slavers choose to claim their reward in the game.
     *
     * @param player  The player object representing the slavers who are claiming the reward.
     * @param yOn     A boolean flag indicating whether the reward claim option is enabled.
     * @param matchId The unique identifier for the match in which the reward is to be claimed.
     * @throws Exception If any error occurs during the reward claim process.
     */
    void slaversChooseToClaimReward(Player player, boolean yOn, UUID matchId) throws Exception;

    /**
     * Allows the pirate team to choose the positions for cannon batteries during the match.
     *
     * @param player The player representing the pirate team making the selection.
     * @param cannonPos A list of potential cannon positions represented as 2D integer coordinates.
     * @param batteriesPos A list of positions where cannon batteries will be placed, represented as 2D integer coordinates.
     * @param matchId The unique identifier of the match in which the cannon battery positions are being chosen.
     * @throws Exception Throws an exception if the operation fails or an error occurs during processing.
     */
    void piratesChooseCannonBatteryPos(Player player,
                                       ArrayList<ArrayList<Integer>> cannonPos,
                                       ArrayList<ArrayList<Integer>> batteriesPos,
                                       UUID matchId) throws Exception;

    /**
     * Determines how pirates choose to face meteors in the game context based on the given parameters.
     *
     * @param player the player deciding how to face the meteors
     * @param howToDefenceFromShots the list representing possible defensive strategies or actions related to shots
     * @param shot the shot object representing the current attack or projectile
     * @param dice the rolled dice value influencing the decision-making process
     * @param matchId the unique identifier of the match in which the action is taking place
     * @throws Exception if any error occurs during the process of determining the decision
     */
    void piratesChooseHowToFaceMeteors(Player player,
                                       ArrayList<Integer> howToDefenceFromShots,
                                       Shot shot, int dice,
                                       UUID matchId) throws Exception;

    /**
     * Handles the logic for pirates to choose whether to claim a reward in a game.
     *
     * @param yOn        a boolean flag indicating whether a specific condition is active
     * @param player     the player object representing the pirate making the choice
     * @param matchId    the unique identifier for the current game match
     * @throws Exception if an error occurs during the process
     */
    void piratesChooseToClaimReward(boolean yOn, Player player, UUID matchId) throws Exception;

    /**
     * Moves the player to a specified position on the flight board in the context of a specific match.
     *
     * @param playerNickname the nickname of the player to be moved
     * @param pos the target position to move the player to on the flight board
     * @param matchId the unique identifier of the match
     * @throws Exception if any error occurs during the operation
     */
    void movePlayerInFlightBoard(String playerNickname, int pos, UUID matchId) throws Exception;

    /**
     * Initiates the motor starting process in the given open space on the flight board.
     *
     * @param player The player initiating the motor start.
     * @param flightBoard The flight board where the action is taking place.
     * @param enginesPos Positions of all engines that can interact with the motor start process.
     * @param batteriesPos Positions of all batteries available for the motor start process.
     * @param matchId Unique identifier of the match in which the action is performed.
     * @throws Exception If an error occurs during the motor start process.
     */
    void openSpaceChooseToStartMotor(Player player, FlightBoard flightBoard,
                                     ArrayList<ArrayList<Integer>> enginesPos,
                                     ArrayList<ArrayList<Integer>> batteriesPos,
                                     UUID matchId) throws Exception;

    /**
     * Determines how a player chooses to face meteors during the game based on the provided parameters.
     *
     * @param player The player who is choosing how to face meteors.
     * @param howToDefenceFromShots A list of integers representing the defense strategy or methods the player can use to counter shots.
     * @param shots A list of Shot objects representing the shots or meteors directed at the player.
     * @param dice The dice number rolled, influencing the player's options or decisions.
     * @param currentShot The index of the current shot being considered in the list of shots.
     * @param matchId The unique identifier for the match in which this action is being performed.
     * @throws Exception If an error occurs while processing how the player responds to meteor shots.
     */
    void meteorCardChooseHowToFaceMeteors(Player player,
                                          ArrayList<Integer> howToDefenceFromShots,
                                          ArrayList<Shot> shots, int dice, int currentShot,
                                          UUID matchId) throws Exception;

    /**
     * Activates the epidemic state base for a given set of tiles and match identifier.
     *
     * @param alreadyVisited a set of SpaceShipTile objects representing tiles that have already been visited
     * @param matchId the unique identifier for the match in which the epidemic state is being activated
     * @throws Exception if there is an issue during the activation of the epidemic state base
     */
    void epidemicStateBaseActivate(Set<SpaceShipTile> alreadyVisited, UUID matchId) throws Exception;

    /**
     * Triggers the stardust effect for a specific match identified by its unique ID.
     *
     * @param matchId the unique identifier of the match for which the stardust effect is triggered
     * @throws Exception if an error occurs while triggering the effect
     */
    void stardustEffect(UUID matchId) throws Exception;

    /**
     * Updates the remaining goods for a specific player in a match.
     *
     * @param player      The player whose goods need to be updated.
     * @param goodsFinal  The list of final goods to be updated.
     * @param matchId     The unique identifier for the match where the update is applied.
     * @throws Exception  If there's an issue during the update process.
     */
    void updateGoodsRemaining(Player player, ArrayList<Goods> goodsFinal, UUID matchId) throws Exception;

    /**
     * Updates the remaining number of batteries for a given player in a specific match.
     *
     * @param player       The player whose battery count is being updated.
     * @param batteriesRem The new number of batteries remaining for the player.
     * @param matchId      The unique identifier of the match where the update takes place.
     * @throws Exception   If an error occurs during the update process.
     */
    void updateBatteriesRemaining(Player player, int batteriesRem, UUID matchId) throws Exception;

    /**
     * Handles the scenario where it is not the specified player's turn in the game.
     *
     * @param player the player attempting the action
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs while processing the turn handling
     */
    void notYourTurn(Player player, UUID matchId) throws Exception;

    /**
     * Executes actions or logic when a timer has not started for a specific match.
     *
     * @param player  the Player object representing the participant associated with the match
     * @param matchId the unique identifier of the match for which the timer has not started
     * @throws Exception if any error occurs during the execution of this method
     */
    void timerNotStarted(Player player, UUID matchId) throws Exception;

    /**
     * Handles the logic when a tile is not flipped during a game.
     *
     * @param player  the player involved in the current match
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the operation
     */
    void tileNotFlipped(Player player, UUID matchId) throws Exception;

    /**
     * Prevents a player from surrendering during a match identified by the given match ID.
     *
     * @param player the player instance attempting to surrender
     * @param matchId the unique identifier of the match in which the surrender is being attempted
     * @throws Exception if the operation fails or surrender prevention cannot be enforced
     */
    void noSurrender(Player player, UUID matchId) throws Exception;

    /**
     * Advances the game to the next player's turn in the specified match.
     *
     * @param matchId the unique identifier of the match whose turn is to be updated
     * @throws Exception if an error occurs while processing the player's turn
     */
    void nextPlayerTurn(UUID matchId) throws Exception;

    /**
     * Handles scenarios where incorrect input is provided by a player during a match.
     *
     * @param player the player who provided the incorrect input
     * @param matchId the unique identifier of the match in which the incorrect input occurred
     * @throws Exception if an unexpected error occurs while processing the incorrect input
     */
    void wrongInput(Player player, UUID matchId) throws Exception;

    /**
     * Processes and validates the input provided by the player for the specified match.
     * If the input is deemed incorrect, the method will perform the necessary steps to correct it.
     *
     * @param player  the player whose input is being checked and corrected
     * @param matchId the unique identifier of the match for which the input is being validated
     * @throws Exception if input correction cannot be completed or if an error occurs during the process
     */
    void correctInput(Player player, UUID matchId) throws Exception;

    /**
     * Indicates an action related to an incorrect or unintended player in a match.
     *
     * @param player  the Player object representing the player involved in the action
     * @param matchId the UUID of the match where the action occurred
     * @throws Exception if an error occurs during the processing of the action
     */
    void wrongPlayer(Player player, UUID matchId) throws Exception;

    /**
     * Notifies that an effect has started for the specified match.
     *
     * @param matchId the unique identifier of the match for which the effect has started
     * @throws Exception if an error occurs when processing the start of the effect
     */
    void effectStarted(UUID matchId) throws Exception;

    /**
     * Notifies that a player has won in a specific match.
     * This method processes the winner's information and the match details
     * based on the provided parameters.
     *
     * @param playerNickname the nickname of the player who won
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the notification process
     */
    void someoneWon(String playerNickname, UUID matchId) throws Exception;

    /**
     * Records a tie for a match, involving a player identified by their nickname and the match identifier.
     *
     * @param playerNickname the nickname of the player involved in the tie
     * @param matchId the unique identifier of the match that ended in a tie
     * @throws Exception if an error occurs during the operation
     */
    void tie(String playerNickname, UUID matchId) throws Exception;

    /**
     * Records the information of a player who has lost a match.
     *
     * @param playerNickname the nickname of the player who lost the match
     * @param matchId the unique identifier of the match the player participated in
     * @throws Exception if an error occurs while processing the lost status
     */
    void lost(String playerNickname, UUID matchId) throws Exception;

    /**
     * Processes the refusal of a reward by a player in a specific match.
     * This method should handle any logic related to the rejection of a reward
     * by the player and may trigger necessary follow-up actions or cleanup.
     *
     * @param playerNickname the nickname of the player refusing the reward
     * @param matchId the unique identifier of the match associated with the reward
     * @throws Exception if an error occurs while processing the refusal
     */
    void refusedReward(String playerNickname, UUID matchId) throws Exception;

    /**
     * Handles the termination of an effect associated with a specific match.
     *
     * @param matchId the unique identifier of the match for which the effect has ended
     * @throws Exception if an issue occurs while processing the effect end
     */
    void effectEnded(UUID matchId) throws Exception;

    /**
     * Handles the scenario where a player has lost goods during a match.
     * This method may process penalties, update match records, or notify the system
     * about the lost goods based on the provided player and match details.
     *
     * @param player the player who has lost goods in the match
     * @param matchId the unique identifier of the match in which goods were lost
     * @throws Exception if there is an error during the process
     */
    void lostGoods(Player player, UUID matchId) throws Exception;

    /**
     * Executes the necessary actions or consequences for a player during a match.
     *
     * @param player the Player instance who will face the consequences.
     * @param matchId the unique identifier of the match to which the consequences apply.
     * @throws Exception if an error occurs while applying the consequences.
     */
    void youPayConsequences(Player player, UUID matchId) throws Exception;

    /**
     * Updates the state of a dice for a given match.
     *
     * @param dice the integer value representing the dice to be updated
     * @param matchId the unique identifier of the match for which the dice state is being updated
     * @throws Exception if an error occurs during the update process
     */
    void updateDice(int dice, UUID matchId) throws Exception;

    /**
     * Determines whether a player is restricted from depositing in a specific match.
     *
     * @param player The player attempting the deposit.
     * @param matchId The unique identifier of the match.
     * @throws Exception If an error occurs during the operation.
     */
    void cannotDeposit(Player player, UUID matchId) throws Exception;

    /**
     * Evaluates whether an insertion operation can be performed for the given player in the specified match.
     * If the operation is invalid, an exception will be thrown.
     *
     * @param player the player for whom the insertion operation is being checked
     * @param matchId the unique identifier of the match
     * @throws Exception if the insertion cannot be performed
     */
    void cannotInsert(Player player, UUID matchId) throws Exception;

    /**
     * Prevents the specified player from participating in a match with the given match identifier.
     *
     * @param player The player who will be restricted from joining the match.
     * @param matchId The unique identifier of the match from which the player will be restricted.
     * @throws Exception If an error occurs while restricting the player.
     */
    void cannotPick(Player player, UUID matchId) throws Exception;

    /**
     * Determines whether a specific player cannot fill a position or role in a match.
     *
     * @param player  the player whose eligibility is being determined
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the operation
     */
    void cannotFill(Player player, UUID matchId) throws Exception;

    /**
     * Handles the event when a player is blocked during a match.
     *
     * @param player the player who is blocked
     * @param matchId the unique identifier of the match where the block occurred
     * @throws Exception if an error occurs during the handling of the block event
     */
    void blocked(Player player, UUID matchId) throws Exception;

    /**
     * Stops the building process for a particular match.
     *
     * @param matchId the unique identifier of the match whose building process is to be stopped
     * @throws Exception if an error occurs while stopping the building process
     */
    void stopBuilding(UUID matchId) throws Exception;

    /**
     * Invoked to indicate that a timer has started for a specific match.
     *
     * @param matchId the unique identifier of the match for which the timer has started
     * @throws Exception if an error occurs during the timer start process
     */
    void timerStarted(UUID matchId) throws Exception;

    /**
     * Checks and handles the scenario where a timer has already been started for a specific match.
     *
     * @param matchId the unique identifier of the match for which the timer is being checked
     * @param player the player associated with the match where the timer is checked
     * @throws Exception if an error occurs during the process
     */
    void timerAlreadyStarted(UUID matchId, Player player) throws Exception;

    /**
     * Handles the event when a timer has completed its countdown or duration.
     *
     * @param isLastActivation a Boolean indicating whether this is the last activation of the timer.
     *                         If true, it signifies that no further timer reactivations are expected.
     * @param matchId          the UUID representing the unique identifier of the match associated with the timer.
     * @throws Exception if there is an error during the handling of the timer ending event.
     */
    void timerEnded(Boolean isLastActivation, UUID matchId) throws Exception;

    /**
     * Removes a block of SpaceShipTile objects associated with the given player and match.
     *
     * @param block the list of SpaceShipTile objects to be removed
     * @param playerNickname the nickname of the player whose block is removed
     * @param matchId the unique identifier of the match
     * @throws Exception if the operation fails or an error occurs during removal
     */
    void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname, UUID matchId) throws Exception;

    /**
     * Identifies and processes incorrect tile placements in a game.
     *
     * @param pos       A list of integers representing the positions of tiles to be checked.
     * @param player    The player object representing the current player involved in the operation.
     * @param matchId   The unique identifier for the game match.
     * @throws Exception If an error occurs during the processing of tile positions.
     */
    void wrongTiles(ArrayList<Integer> pos, Player player, UUID matchId) throws Exception;

    /**
     * Inserts a wait tile for a specified player in the match at the given index.
     *
     * @param playerNickname the nickname of the player associated with the wait tile
     * @param index the index position where the wait tile should be inserted
     * @param matchId the unique identifier of the match where the wait tile will be inserted
     * @throws Exception if an error occurs during the operation
     */
    void insertWaitTileLMR(String playerNickname, int index, UUID matchId) throws Exception;

    /**
     * Fills the empty tiles in a match based on the given player's actions.
     *
     * @param player the Player object representing the player involved in the match
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs while filling the empty tiles
     */
    void haveToFillEmptyTiles(Player player, UUID matchId) throws Exception;

    /**
     * Removes a player from the flightboard in the specified match.
     *
     * @param playerNickname the nickname of the player to be removed from the flightboard
     * @param matchId the unique identifier of the match from which the player is to be removed
     * @throws Exception if an error occurs during the removal process
     */
    void removePlayerFromFlightboard(String playerNickname, UUID matchId) throws Exception;

    /**
     * Disconnects all participants associated with the provided match ID.
     *
     * @param matchId the unique identifier of the match whose participants are to be disconnected
     * @throws Exception if an error occurs during the disconnection process
     */
    void disconnectAll(UUID matchId) throws Exception;

    /**
     * Handles the scenario where there are not enough goods for the specified player.
     *
     * @param player the player involved in the operation
     * @param uuid the unique identifier associated with the operation
     * @throws Exception if an error occurs during the handling of goods
     */
    void notEnoughGoods(Player player, UUID uuid) throws Exception;

    /**
     * Removes an alien entity identified by its username and UUID from a specified position on a grid.
     *
     * @param username the username associated with the alien entity to be removed
     * @param r the row index in the grid where the alien is located
     * @param c the column index in the grid where the alien is located
     * @param uuid the unique identifier for the alien entity
     * @throws Exception if the removal operation fails
     */
    void removeAlien(String username, int r, int c, UUID uuid) throws Exception;

    /**
     * Handles the process of removing batteries associated with a specific
     * player or based on certain conditions provided in the batteriesToAct.
     * This method may also utilize the unique identifier (UUID) as necessary.
     *
     * @param p The player for whom batteries are being processed.
     * @param batteriesToAct A list of lists containing integers representing
     *                       the specific batteries to act upon.
     * @param uuid The unique identifier that may be used to identify the
     *             context or entity related to this operation.
     * @throws Exception If an error occurs during the process.
     */
    void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct, UUID uuid) throws Exception;
}
