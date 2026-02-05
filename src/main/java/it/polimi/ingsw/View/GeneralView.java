package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.View.GUI.GUI;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;
import java.util.HashMap;

public interface GeneralView {
    /**
     * Retrieves the current instance of LittleModelRepresentation.
     *
     * @return the current LittleModelRepresentation instance
     */
    LittleModelRepresentation getLittleModelRepresentation();

    /**
     * Sets the client for the GeneralView.
     *
     * @param client the GenericClient instance to be associated with this GeneralView
     */
    void setClient(GenericClient client);

    /**
     * Sets the given LittleModelRepresentation for the GeneralView.
     *
     * @param littleModelRepresentation the LittleModelRepresentation object to be set
     */
    void setLittleModelRepresentation(LittleModelRepresentation littleModelRepresentation);

    /**
     * Updates the current state or data representation of the application using the provided JSON string.
     *
     * @param Json the JSON string containing the data update instructions or information.
     *             This must be a properly formatted JSON string that adheres to the application's data update schema.
     * @throws Exception if there is an error during the JSON parsing, validation,
     *                   or any issues encountered while applying the update.
     */
    void Update(String Json) throws Exception;

    /**
     * Initiates a process or action using the provided username.
     *
     * This method begins the execution of a specific operation associated
     * with the username, potentially throwing an exception if an error occurs.
     *
     * @param Username the username that identifies the user initiating the process.
     * @throws Exception if any error occurs during the execution of this method.
     */
    void Start(String Username) throws Exception;

    /**
     * Moves a player to a new position on the flightboard while updating their previous position.
     *
     * @param player the player to be moved on the flightboard
     * @param newPos the new position of the player on the flightboard
     * @param oldPos the previous position of the player on the flightboard
     * @throws Exception if an error occurs during updating the player's position
     */
    void movePlayerOnFlightboard(Player player, int newPos, int oldPos) throws Exception;

    /**
     * Adds a specific tile to the specified position on the ship board.
     *
     * @param tile the SpaceShipTile to be added
     * @param row the row index where the tile will be placed
     * @param col the column index where the tile will be placed
     * @param ship the ShipBoard where the tile will be added
     * @throws Exception if the operation fails or is invalid
     */
    void addTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception;

    /**
     * Removes a block of tiles from the specified ship board.
     *
     * @param s the ship board from which the block is to be removed
     * @param block the list of spaceship tiles that make up the block to be removed
     * @throws Exception if an error occurs during the removal process
     */
    void removeBLock(ShipBoard s, ArrayList<SpaceShipTile> block) throws Exception;// OK

    /**
     * Updates the goods on the shipboard based on the specified storage tiles and new goods.
     *
     * @param s the shipboard where the goods will be updated
     * @param storagetiles a 2D list of integers representing the storage tiles that will be updated
     * @param newgoods a 2D list of goods representing the new goods to be placed on the shipboard
     * @throws Exception if the operation cannot be completed due to an error
     */
    void updateGoods(ShipBoard s, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception;

    /**
     * Updates the state of batteries on the specified ship board based on the provided positions.
     *
     * @param BattxPos A 2D list representing the positions of batteries to be updated.
     *                 Each inner list contains two integers denoting the row and column indices respectively.
     * @param shipBoard The ship board where the batteries need to be updated.
     * @param toLoose A boolean indicating whether the batteries are to be removed or maintained.
     * @throws Exception If there is an issue while updating the batteries.
     */
    void updateBatteries(ArrayList<ArrayList<Integer>> BattxPos, ShipBoard shipBoard, boolean toLoose) throws Exception;

    /**
     * Removes a specified number of passengers from the given tiles of a ship board.
     *
     * @param s         the ship board from which passengers are to be removed
     * @param tiles     a list of tiles, represented as a 2D list of integers, from which passengers will be removed
     * @param numPass   the number of passengers to remove
     * @throws Exception if an error occurs during passenger removal
     */
    void removePassengers(ShipBoard s, ArrayList<ArrayList<Integer>> tiles, int numPass) throws Exception;

    /**
     * Removes a specified number of passengers from a given tile on the shipboard.
     *
     * @param s the instance of the ShipBoard from which passengers should be removed
     * @param tile the specific SpaceShipTile from which passengers should be removed
     * @param numPass the number of passengers to be removed from the specified tile
     * @throws Exception if an error occurs during the removal process
     */
    void removePassengersFromTile(ShipBoard s, SpaceShipTile tile, int numPass) throws Exception;

    /**
     * Removes an alien from the specified tile on the provided ship board.
     *
     * @param s    The ship board from which the alien will be removed.
     * @param tile The tile on the ship board from which the alien will be removed.
     * @throws Exception If the operation cannot be completed.
     */
    void removeAlienFromTile(ShipBoard s, SpaceShipTile tile) throws Exception;

    /**
     * Updates the "thing" currently held by the specified player.
     *
     * @param p The player whose item in hand will be updated.
     * @param thing The new item to set in the player's hand.
     * @throws Exception If an error occurs during the update operation.
     */
    void updateThingInHand(Player p, Object thing) throws Exception;

    /**
     * Adds a flipped tile to the game using its index.
     *
     * This method handles the logic related to adding a tile that has been flipped
     * during the game, based on the specified index.
     *
     * @param indextile the index of the tile to be added as flipped
     * @throws Exception if an error occurs while adding the flipped tile
     */
    void addTileFlipped(int indextile) throws Exception;

    /**
     * Handles the event where a player deposits a little deck in the game.
     *
     * @param d the deck that is being deposited
     * @param player the player who is depositing the deck
     * @throws Exception if an error occurs during the process
     */
    void someoneDepositedLittleDeck(Deck d, Player player) throws Exception;

    /**
     * Executes the action of a player picking a little deck.
     *
     * @param d the deck that is being picked
     * @param player the player who picked the deck
     * @throws Exception if the operation fails or an error occurs during execution
     */
    void someonePickedLittleDeck(Deck d, Player player) throws Exception;

    /**
     * Removes a flipped tile identified by the provided index.
     *
     * This method performs the operation of removing a tile that has been flipped,
     * ensuring the game state is updated accordingly.
     *
     * @param t the index of the flipped tile to be removed
     * @throws Exception if an error occurs during the removal process
     */
    void removeTileFlipped(int t) throws Exception;

    /**
     * Adds a passenger to the specified position on the ship board.
     *
     * @param s the ship board where the passenger is to be added
     * @param row the row index on the ship board where the passenger should be added
     * @param column the column index on the ship board where the passenger should be added
     * @throws Exception if the operation fails for any reason
     */
    void addPassenger(ShipBoard s, int row, int column) throws Exception;

    /**
     * Triggers the necessary actions or updates required when a player must face
     * consequences in the game.
     *
     * The method serves as a central point for handling the repercussions or penalty logic
     * associated with certain game events or player actions. It likely interacts with other
     * methods or components to apply specific consequences to the involved player(s),
     * such as deducting resources, updating statuses, or modifying the game state.
     *
     * This method does not specify the exact type of consequences or the conditions under
     * which it is invoked. Implementation details are determined by the class logic and game rules.
     */
    void youPayConsequences();

    /**
     * Sends a penalty to the system.
     *
     * @param penalty the penalty value to be applied, represented as an integer.
     * @param type the type or category of the penalty, represented as a string.
     */
    void sendPenalty(int penalty, String type);

    /**
     * Adds an alien of the specified color at the given row and column on the ship board.
     *
     * @param s The ship board where the alien will be added.
     * @param row The row position where the alien will be placed.
     * @param column The column position where the alien will be placed.
     * @param alienColor The color of the alien to be added.
     * @throws Exception If the operation fails due to invalid parameters or other issues.
     */
    void addAlien(ShipBoard s, int row, int column, AlienColor alienColor) throws Exception;

    /**
     * Displays the initial state or configuration of the system or interface.
     *
     * This method is intended to present the first visual representation or
     * state to the user, often used to initialize or prepare the system's
     * first interaction or overview. The specifics of this visualization
     * depend on the context and implementation of the method within the
     * system but generally relate to setting up or providing an initial
     * perspective to the user.
     *
     * @throws Exception if there is an error during the execution of the method.
     */
    void showFirstSight() throws Exception;

    /**
     * Updates the status of the current system or game state.
     *
     * This method is responsible for synchronizing or refreshing
     * various aspects of the environment, such as player states,
     * game board conditions, or other relevant components.
     * The exact implementation details are context-specific and
     * depend on the application's requirements.
     *
     * @throws Exception if an error occurs during the status update process.
     */
    void updateStatus() throws Exception;

    /**
     * Handles error events within the application.
     *
     * This method is triggered when an error occurs during the execution
     * of a process in the context where it is implemented. The specific behavior
     * and handling logic of this method need to be defined by the implementing class.
     *
     * @throws Exception if an error occurs during the handling process. The specific
     *                   nature of the exception depends on the implementation.
     */
    void onError() throws Exception;

    /**
     * Displays the final scores of the game.
     *
     * @param finalScores a HashMap where the key is the player's name (String)
     *                    and the value is their final score (Float)
     * @throws Exception if an error occurs while displaying the final score
     */
    void showFinalScore(HashMap<String, Float> finalScores) throws Exception;

    /**
     * Removes all goods from the provided ship board.
     *
     * @param s the ShipBoard object from which all goods are to be removed
     * @throws Exception if an error occurs during the process
     */
    void looseAllGoods(ShipBoard s) throws Exception;

    /**
     * Removes all batteries from the provided ShipBoard instance.
     *
     * @param s the ShipBoard instance from which all batteries will be removed
     * @throws Exception if an error occurs while removing batteries
     */
    void looseAllbatteries(ShipBoard s) throws Exception;

    /**
     * Executes the middle phase effect in the gameplay or application process.
     *
     * This method is intended to handle operations or actions that occur
     * during the intermediate stage of a sequence, event, or game workflow.
     * The specific implementation details depend on the context provided
     * by the containing class or application logic.
     *
     * @throws Exception if an error occurs during the execution of the middle phase effect.
     */
    void middleEffect() throws Exception;

    /**
     * Executes the final effect in the context of a game or application.
     *
     * This method is intended to process or trigger operations related to
     * the concluding part of an ongoing effect, event, or action sequence.
     * It ensures that all necessary actions tied to the "ended effect"
     * stage are properly executed.
     *
     * @throws Exception if an error occurs during the execution of the final effect.
     */
    void endedEffect() throws Exception;

    /**
     * Updates the current status or display of the card information within the system.
     *
     * This method is responsible for handling any necessary changes or updates related to the card.
     * It might involve refreshing or modifying the card data or its visual representation
     * within the user interface or application logic.
     *
     * @throws Exception if an error occurs during the card update process.
     */
    void updateCard() throws Exception;

    /**
     * Updates the cosmic credits of a specified player by a specified amount.
     *
     * @param p the player whose cosmic credits need to be updated
     * @param i the amount to update the cosmic credits by; can be positive or negative
     * @throws Exception if updating the cosmic credits fails
     */
    void updateCosmicCredits(Player p, int i) throws Exception;

    /**
     * Updates the player states or statuses in the current game.
     *
     * This method handles the necessary changes or updates for players
     * who are actively participating in the game. It ensures that all player-related
     * details or data are synchronized and in line with the current game state.
     *
     * @throws Exception if there is an error during the update process.
     */
    void updatePlayersInGame() throws Exception;

    /**
     * Updates the message displayed in the user interface or within the system.
     * This method is used to modify or set a new message without any additional behavior.
     *
     * @param message The message to be displayed or updated. It should be a non-null string.
     * @throws Exception If an error occurs during the message update process.
     */
    void updateMessageOnly(String message) throws Exception;

    /**
     * Updates the time-related state or information within the system.
     *
     * This method is responsible for synchronizing or managing time-based aspects
     * in the application. It may interact with other components to ensure time-related
     * data is accurate and consistent across the system.
     *
     * @throws Exception if an error occurs during the update of time-related information.
     */
    void updateTime() throws Exception;

    /**
     * Displays the tiles that were identified as incorrect based on the given input.
     *
     * @param tiles an ArrayList of integers representing the identifiers of the tiles that are incorrect
     * @param nickname a String specifying the nickname of the player associated with the incorrect tiles
     * @param nickEff a String indicating the effective nickname or alias being utilized in this context
     * @throws Exception if an error occurs while attempting to display the incorrect tiles
     */
    void showWrongTiles(ArrayList<Integer> tiles, String nickname, String nickEff) throws Exception;

    /**
     * Updates the state when a shot is received by a player.
     *
     * @param player The player who is the target of the shot.
     * @param shot The shot being received by the player.
     * @param howToDefenceFromShots A list of integers representing the defensive measures or actions the player can take to mitigate the effect of the shot.
     * @param dice The dice roll value associated with the shot, which may influence the outcome.
     * @throws Exception If there is an error during the update process.
     */
    void updateShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception;

    /**
     * Sends a message related to a player's selected subgroup of spaceship tiles.
     *
     * @param subShip The list of lists representing the subgroup of spaceship tiles to be associated with the message.
     * @param playerNickname The nickname of the player involved in the messaging process.
     * @throws Exception If an error occurs while processing the message.
     */
    void messageSubShips(ArrayList<ArrayList<SpaceShipTile>> subShip, String playerNickname) throws Exception;

    /**
     * Allows a player to choose a sub-ship from the provided list of sub-ships.
     * Depending on the parameters, the chosen sub-ship is preserved while the others are discarded.
     *
     * @param playerNickname the nickname of the player selecting the sub-ship
     * @param subShips a list of sub-ships represented by 2D arrays of SpaceShipTile objects
     * @param indexToPreserve the index of the sub-ship to be preserved
     * @param waste an integer representing the waste or unused elements that may occur during this operation
     * @throws Exception if an error occurs during the selection process
     */
    void ChooseSubShip(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) throws Exception;

    /**
     * Removes a single tile from a specified position on the game board for a specific player.
     *
     * This method allows you to remove a single tile based on the provided coordinates, with additional
     * options to handle mistakes or track wastage. Exceptions may be thrown in the event of invalid operations.
     *
     * @param playerNickname the nickname of the player whose tile is being removed
     * @param row the row index of the tile to be removed
     * @param col the column index of the tile to be removed
     * @param fromMistake a boolean indicating whether the removal is due to a mistake
     * @param waste the amount of waste generated by removing the tile
     * @throws Exception if the removal operation fails
     */
    void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) throws Exception;

    /**
     * Adds a wait tile to the specified position on the ship board.
     *
     * @param tile The SpaceShipTile to be added.
     * @param row The row position on the ship board where the tile will be added.
     * @param col The column position on the ship board where the tile will be added.
     * @param ship The ShipBoard to which the tile will be added.
     * @throws Exception If an error occurs while adding the wait tile.
     */
    void addWaitTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception;

    /**
     * Completes the building phase for the specified player.
     *
     * This method finalizes actions or updates necessary when a player finishes their building process.
     *
     * @param playerNick the nickname of the player who has completed their building phase
     * @throws Exception if an error occurs while ending the building phase
     */
    void endBuilding(String playerNick) throws Exception;

    /**
     * Handles the process where tiles need to be filled during the game.
     *
     * This method is invoked at a specific point in the game flow when the game logic
     * determines that tiles must be populated or completed on a game board. It may interact
     * with the associated game model or user interface to ensure that the tiles are properly
     * filled as required.
     *
     * @throws Exception if an error occurs during the tile-filling process. This can include
     * issues related to the game model, user input, or system constraints.
     */
    void haveToFillTiles() throws Exception;

    /**
     * Updates the remaining goods for a given player with the provided list of goods.
     *
     * @param p the player whose goods information needs to be updated
     * @param goodFInali the list of goods to update for the player
     * @throws Exception if an error occurs while updating the goods
     */
    void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception;

    /**
     * Removes a player from the flightboard at the specified position.
     *
     * @param player the name of the player to be removed from the flightboard.
     * @param oldPos the position on the flightboard where the player is currently located.
     * @throws Exception if an error occurs during the removal process.
     */
    void removePlayerFromFlightboard(String player, int oldPos) throws Exception;

    /**
     * Displays a list of previously played games.
     *
     * @param oldGames an ArrayList of OldGame objects representing the old games to be displayed
     * @throws Exception if an error occurs while displaying the old games
     */
    void showOldGames(ArrayList<OldGame> oldGames) throws Exception;

    /**
     * Sets the current GUI view to the specified view object.
     *
     * @param view the GUI instance to be set as the current view
     */
    void setView(GUI view);

    /**
     * Displays an error dialog with the provided title and message.
     *
     * @param title the title of the error dialog
     * @param message the message to be displayed in the error dialog
     * @throws Exception if an error occurs while displaying the dialog
     */
    void showErrorDialog(String title, String message) throws Exception;

    /**
     * Displays an informational dialog box with the specified title and message.
     *
     * @param title   the title of the information dialog
     * @param message the message content to be displayed in the dialog
     * @throws Exception if there is an error displaying the dialog
     */
    void showInfoDialog(String title, String message) throws Exception;

    /**
     * Displays the shutdown procedure with a specific reason provided.
     *
     * @param reason the reason or message indicating why the shutdown process is taking place
     */
    void showShutdown(String reason);

    /**
     * Displays the current view or representation.
     *
     * This method is intended to make the associated view (UI or component) visible
     * to the user. The implementation of this method may be empty (no operation)
     * if the visibility of the view does not require any specific action.
     */
    default void show() { /* no-op */ }

    /**
     * Hides the current view or element. This method is intended to be a no-operation
     * (no-op) default implementation, allowing subclasses or implementing classes
     * to provide specific behavior for hiding functionality if needed.
     */
    default void hide() { /* no-op */ }

    /**
     * Inserts a waiting tile in the Little Model Representation (LMR) for the specified player.
     * The operation may throw an exception if it encounters issues during the process.
     *
     * @param playerNickname the nickname of the player for whom the waiting tile is to be inserted
     * @throws Exception if an error occurs during the insertion of the waiting tile
     */
    void insertwaittileLMR1(String playerNickname) throws Exception;

    /**
     * Inserts a wait tile into the Little Model Representation (LMR) for the specified player.
     * This method is used to manage the representation of the game state, particularly
     * for the player's wait tile in the LMR.
     *
     * @param playerNickname the nickname of the player for whom the wait tile is being inserted
     * @throws Exception if an error occurs during the insertion process
     */
    void insertwaittileLMR2(String playerNickname) throws Exception;

    /**
     * Processes the provided input message to ensure it meets the expected criteria.
     * Typically invoked when user input or communication messages need to be validated
     * or corrected as part of the system's operations.
     *
     * @param message the input message to be processed and corrected if necessary
     * @throws Exception if any error occurs during the correction process
     */
    void correctinput(String message) throws Exception;

    /**
     * Advances the game to the next player's turn, based on the current state.
     *
     * @param myNickname the nickname of the current player whose turn is ending.
     * @throws Exception if the operation cannot be completed due to an error.
     */
    void nextPlayerTurn(String myNickname) throws Exception;

    /**
     * Displays the provided text for a specific duration in seconds.
     *
     * @param text The text message to be displayed.
     * @param seconds The duration, in seconds, for which the text will be displayed.
     * @throws Exception If an error occurs during the display process.
     */
    void showTimedInfo(String text, int seconds) throws Exception;

    /**
     * Updates the number of batteries remaining for a specified player.
     *
     * @param p  the player whose battery count is to be updated
     * @param batt  the new number of batteries to set for the player
     * @throws Exception if an error occurs during the update process
     */
    void updateBatteriesRemaining(Player p, int batt) throws Exception;
}
