package it.polimi.ingsw.View.TUI;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Card_Combat_zone;
import it.polimi.ingsw.Model.Cards.Smugglers.Card_Smugglers;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.AbandonedShip.Card_AbandonedShip;
import it.polimi.ingsw.Model.Cards.AbandonedStation.Card_AbandonedStation;
import it.polimi.ingsw.Model.Cards.Epidemic.Card_Epidemic;
import it.polimi.ingsw.Model.Cards.MeteorSwarm.Card_MeteorSwarm;
import it.polimi.ingsw.Model.Cards.OpenSpace.Card_OpenSpace;
import it.polimi.ingsw.Model.Cards.Pirates.Card_Pirates;
import it.polimi.ingsw.Model.Cards.Planets.Card_PlanetCard;
import it.polimi.ingsw.Model.Cards.Slavers.Card_Slavers;
import it.polimi.ingsw.Model.Cards.Stardust.Card_Stardust;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.View.Utils_View.CommandType;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.View.GUI.GUI;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Utils.stateEnum;

import java.util.*;

public class TUI implements GeneralView {
    /**
     * Constructor for the TUI class.
     * This initializes a new instance of the TUI (Text User Interface).
     * The TUI serves as a text-based implementation of the user interface,
     * enabling interaction and visualization of the game or application in a command-line environment.
     */
    public TUI() {
    }

    /**
     * Represents an instance of the TUIHandler associated with the TUI class.
     * This field is responsible for managing and handling the textual user interface
     * operations and interactions within the system. It serves as the primary interface
     * to process commands, display information, and update the TUI state.
     */
    TUIHandler tuiHandler = new TUIHandler();
    /**
     * Represents a compact or simplified model used within the application.
     * This variable likely holds the state or view representation for the current application.
     * It may interact with or encapsulate information to facilitate user interface updates or logical operations.
     */
    LittleModelRepresentation littleModelRepresentation;
    /**
     * A collection of {@link CommandType} that is used to manage or represent various
     * commands in the TUI (Textual User Interface).
     *
     * This list serves as a repository for command types that may be invoked during
     * operations within the game or application. Commands in this list can include
     * actions such as creating or joining a game, managing tiles, activating effects,
     * and many more as defined in the {@link CommandType} enumeration.
     */
    ArrayList<CommandType> methodnumber = new ArrayList<>();


    /**
     * Sets the little model representation for this instance.
     *
     * @param littleModelRepresentation the LittleModelRepresentation object to set
     */
    public void setLittleModelRepresentation(LittleModelRepresentation littleModelRepresentation) {
        this.littleModelRepresentation = littleModelRepresentation;
    }

    /**
     * Retrieves the current instance of the LittleModelRepresentation.
     *
     * @return the LittleModelRepresentation object associated with this instance.
     */
    public LittleModelRepresentation getLittleModelRepresentation() {
        return littleModelRepresentation;
    }

    /**
     * Sets the client for the current instance.
     *
     * @param client the GenericClient instance to be set as the client
     */
    @Override
    public void setClient(GenericClient client) {

    }

    /**
     * Updates the state with the provided JSON representation.
     *
     * @param Json the JSON string containing the data or state to update
     */
    @Override
    public void Update(String Json) {

    }

    /**
     * Handles the occurrence of an error within the application by logging a formatted error message
     * and prompting the user for further input to resolve or address the issue.
     *
     * The method outputs a decorative error message to the console, designed to neutralize the negative
     * impact of the error occurrence in a user-friendly manner. It then requests the user to input a
     * method number along with its arguments for further processing by starting a separate input-handling
     * thread.
     *
     * @throws Exception if an error occurs during the error handling or input management process
     */
    @Override
    public void onError() throws Exception {
        System.out.println(
                """
                        \u001B[38;5;196m
                        
                        
                        ┌────────────────────────────────────────────────────────────────────────────┐
                        │ ❤️😊 Oops!Something went wrong, but your smile is the best remedy!😊❤️        │
                        └────────────────────────────────────────────────────────────────────────────┘
                        
                        \u001B[0m
                        """
        );

        System.out.print("Enter the method number and arguments: ");
        InputManager.startThreaded(methodnumber);
    }

    /**
     * Displays the final scoreboard in a formatted ranking table. The method
     * accepts a list of player names and their corresponding scores, sorts the
     * players by their scores in descending order, and prints them in a visually
     * formatted way, including a ranking system and relevant emojis for top
     * positions.
     *
     * @param finalScores A HashMap representing the final scores of players,
     *                    where the key is the player's name (String) and the
     *                    value is their score (Float). The map is used to determine
     *                    the ranking and display the formatted leaderboard.
     */
    @Override
    public void showFinalScore(HashMap<String, Float> finalScores) {
        List<Map.Entry<String, Float>> sortedScores = new ArrayList<>(finalScores.entrySet());
        sortedScores.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        System.out.print("\n");
        if (sortedScores.isEmpty()) {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║    🏆 CLASSIFICA FINALE VUOTA 🏆       ║");
            System.out.println("╚════════════════════════════════════════╝");
            return;
        }
        int maxNameLength = 0;
        for (Map.Entry<String, Float> entry : sortedScores) {
            maxNameLength = Math.max(maxNameLength, entry.getKey().length());
        }
        int frameWidth = Math.max(30, maxNameLength + 25);
        String topBorder = "╔" + "═".repeat(frameWidth) +
                "╗";

        String bottomBorder = "╚" + "═".repeat(frameWidth) +
                "╝";

        System.out.println(topBorder);

        String title = "🏆 CLASSIFICA FINALE 🏆";
        int padding = (frameWidth - title.length()) / 2;
        String titleLine = "║" + " ".repeat(padding) +
                title +
                " ".repeat(Math.max(0, frameWidth - padding - title.length())) +
                "║";
        System.out.println(titleLine);

        String divider = "║" + "─".repeat(frameWidth) +
                "║";
        System.out.println(divider);

        int currentPosition = 1;
        Float previousScore = null;

        for (int i = 0; i < sortedScores.size(); i++) {
            Map.Entry<String, Float> entry = sortedScores.get(i);
            String name = entry.getKey();
            Float score = entry.getValue();

            if (previousScore == null || !previousScore.equals(score)) {
                currentPosition = i + 1;
            }
            String positionEmoji;
            if (currentPosition == 1) {
                positionEmoji = " 🥇 ";
            } else if (currentPosition == 2) {
                positionEmoji = " 🥈 ";
            } else if (currentPosition == 3) {
                positionEmoji = " 🥉 ";
            } else if (currentPosition <= 10) {
                positionEmoji = " 🎮 ";
            } else {
                positionEmoji = " ⭐ ";
            }

            String formattedScore = String.format("%.2f", score);
            StringBuilder playerLine = new StringBuilder(String.format("║%s %d° classificato: %-" + maxNameLength + "s - %s punti",
                    positionEmoji, currentPosition, name, formattedScore));

            while (playerLine.length() < frameWidth + 1) {
                playerLine.append(" ");
            }
            playerLine.append("║");

            System.out.println(playerLine);

            previousScore = score;
        }

        System.out.println(bottomBorder);
    }

    /**
     * Handles the event where all goods on the given shipboard are lost.
     *
     * @param s the ShipBoard instance representing the ship whose goods are to be removed
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void looseAllGoods(ShipBoard s) throws Exception {
        toShow();
    }

    /**
     * Handles the scenario where all batteries are lost from the given shipboard.
     * This method performs any required operations to handle the updated state of the game.
     *
     * @param s the shipboard instance from which all batteries are lost
     * @throws Exception if an unexpected error occurs during the process
     */
    @Override
    public void looseAllbatteries(ShipBoard s) throws Exception {
        toShow();
    }

    /**
     * Executes actions or updates related to the middle phase of a game or workflow.
     * This method is called as part of the overall game flow and triggers the
     * {@code toShow} method to perform specific display-related operations.
     *
     * @throws Exception if an error occurs during execution.
     */
    @Override
    public void middleEffect() throws Exception {
        toShow();
    }

    /**
     * This method is called to finalize an effect or operation.
     * It represents the end of a specific action or series of actions in the flow.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    @Override
    public void endedEffect() throws Exception {
        toShow();
    }

    /**
     * Updates the current card representation within the TUI (Textual User Interface).
     * This method is called to refresh or display the card-related information as required.
     *
     * @throws Exception if an error occurs during the update process.
     */
    @Override
    public void updateCard() throws Exception {
        toShow();
    }

    /**
     * Updates the cosmic credits value for a specified player.
     *
     * @param p The player whose cosmic credits need to be updated.
     * @param i The new value of cosmic credits to be set for the player.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateCosmicCredits(Player p, int i) throws Exception {
        toShow();
    }

    /**
     * Updates the current state of the players in a game session.
     * This method invokes the `toShow` function, which may be responsible
     * for performing synchronization or updating the representation of players.
     *
     * @throws Exception if an error occurs during the update process.
     */
    @Override
    public void updatePlayersInGame() throws Exception {
        toShow();
    }

    /**
     * Updates the message content and triggers the display logic.
     *
     * @param message the message to be updated and displayed
     * @throws Exception if an error occurs during the update or display process
     */
    @Override
    public void updateMessageOnly(String message) throws Exception {
        toShow();
    }

    /**
     * Updates the current time or timing-related aspect of the class.
     * This method invokes the {@code toShow} method to handle the relevant logic for updating the time.
     *
     * @throws Exception if an error occurs during the time update process
     */
    @Override
    public void updateTime() throws Exception {
        toShow();
    }

    /**
     * Displays information about incorrect tiles to the user and requests them to select a tile to remove.
     * The behavior of this method varies depending on whether the user's nickname matches the effective nickname.
     *
     * @param tiles         A list of integers representing the coordinates of the tiles to be processed.
     *                      The expected format is a size of 4 where [0,1] correspond to the first tile's
     *                      row and column, and [2,3] correspond to the second tile's row and column.
     * @param nickname      The nickname of the player interacting with the method.
     * @param nickEff       The effective nickname that determines whether the user can interact with the tiles
     *                      or is waiting for other players' actions.
     */
    @Override
    public void showWrongTiles(ArrayList<Integer> tiles, String nickname, String nickEff) {
        if (!Objects.equals(nickname, nickEff)) {
            System.out.println("Waiting for other players' choice");
        } else {
            for (ShipBoard s : littleModelRepresentation.getShipBoards()) {
                if (Objects.equals(s.getMyPlayer().getUsername(), nickname)) {
                    System.out.println("\nYou have to choose a tile to REMOVE from the following, use method number 3: ");
                    int row1 = tiles.get(0) + 5;
                    int col1 = tiles.get(1) + 4;
                    int row2 = tiles.get(2) + 5;
                    int col2 = tiles.get(3) + 4;
                    System.out.println("Insert 0 : " + s.getShipMatrix()[tiles.get(0)][tiles.get(1)].getType() + " row: " + row1 + " column: " + col1);
                    System.out.println("Insert 1 : " + s.getShipMatrix()[tiles.get(2)][tiles.get(3)].getType() + " row: " + row2 + " column: " + col2);
                    System.out.println("To choose the first tile, insert < 3 0 >");
                    break;
                }
            }
        }
        //toShow();
    }

    /**
     * Updates the view to reflect that a shot has been received by a player, including potential defensive actions
     * and the result of a dice roll.
     *
     * @param player the player who has received the shot
     * @param shot the shot object detailing the shot received
     * @param howToDefenceFromShots a list of integers describing the defensive options or status available
     * @param dice the value of the dice roll that may influence the outcome of the shot
     * @throws Exception if an error occurs during update or processing
     */
    @Override
    public void updateShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {
        toShow();
    }

    /**
     * Allows a player to select a specific subship from a collection of available options.
     *
     * @param playerNickname the nickname of the player making the selection
     * @param subShips a list of subships, where each subship is represented as a list of SpaceShipTile objects
     * @param indexToPreserve the index of the subship to preserve
     * @param waste an integer representing a parameter related to the selection process
     * @throws Exception if an error occurs during the selection process
     */
    public void ChooseSubShip(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) throws Exception {
        toShow();
    }

    /**
     * Removes a single tile from the game board for a specific player.
     *
     * @param playerNickname The nickname of the player whose tile needs to be removed.
     * @param row The row index of the tile to be removed.
     * @param col The column index of the tile to be removed.
     * @param fromMistake Indicates whether the tile is being removed due to a mistake.
     * @param waste The amount of waste generated by removing the tile.
     * @throws Exception If an error occurs during the removal process.
     */
    @Override
    public void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) throws Exception {
        toShow();
    }

    /**
     * Adds a wait tile to the specified position on the ship board. This method is used
     * to place a {@code SpaceShipTile} into a designated row and column on a given
     * {@code ShipBoard}.
     *
     * @param tile the {@code SpaceShipTile} to be added
     * @param row the row index on the {@code ShipBoard} where the tile will be placed
     * @param col the column index on the {@code ShipBoard} where the tile will be placed
     * @param ship the {@code ShipBoard} on which the tile will be added
     * @throws Exception if an error occurs during the addition of the tile
     */
    @Override
    public void addWaitTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        toShow();
    }

    /**
     * Ends the building phase for a specified player.
     *
     * @param playerNick the nickname of the player whose building phase is ending
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void endBuilding(String playerNick) throws Exception {
        toShow();
    }

    /**
     * Indicates that the current player or entity needs to fill spaceship tiles
     * as part of the game's process. This method is called to notify about the requirement
     * and triggers relevant actions to handle the tile-filling process.
     *
     * @throws Exception if an error occurs while performing the required actions
     */
    @Override
    public void haveToFillTiles() throws Exception {
        toShow();
    }

    /**
     * Updates the list of remaining goods for the specified player.
     *
     * @param p the player whose remaining goods are to be updated
     * @param goodFInali the updated list of goods remaining for the player
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception {
        toShow();
    }

    /**
     * Removes a player from the flight board based on their current position.
     *
     * @param player the nickname of the player to be removed from the flight board
     * @param OldPos the current position of the player on the flight board
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removePlayerFromFlightboard(String player, int OldPos) throws Exception {
        toShow();
    }

    /**
     * Displays a message to the player about available subships and provides information for selection.
     *
     * @param matrix a 2D list of SpaceShipTile objects representing the available subships.
     *               Each sublist corresponds to a specific subship, and individual tiles
     *               represent components of that subship.
     * @param playerNickname the nickname of the player to whom the message is addressed.
     *                       It identifies the player receiving the instructions.
     */
    @Override
    public void messageSubShips(ArrayList<ArrayList<SpaceShipTile>> matrix, String playerNickname) {
        if (matrix == null) {
            System.out.println("No subships to choose from");
            return;
        }
        System.out.println("Choose a subship to KEEP from the following (use method number 15):");
        for (int x = 0; x < matrix.size(); x++) {
            System.out.print("Insert " + x + "for selecting this subship:");
            List<SpaceShipTile> row = matrix.get(x);
            if (row == null) continue;
            for (int y = 0; y < row.size(); y++) {
                SpaceShipTile tile = row.get(y);
                if (tile != null) {
                    System.out.print("(" + x + "," + y + "->" + tile.getType().name() + ") ");
                }
            }
            System.out.println();
        }
    }


    /**
     * Starts the GalaxyTrucker game interface, providing options for creating new games,
     * testing levels, and viewing active games. Configures command types for the game and
     * initializes inputs for further processing.
     *
     * @param name The nickname of the player to associate with the session.
     * @throws Exception If an error occurs during the execution of the method.
     */
    @Override
    public void Start(String name) throws Exception {
        System.out.println("Welcome to GalaxyTrucker! \n Type < 0 [level] [NumPlayer] > if you want to create a new game. \n If you want test level, insert 0, if you want level 2, insert 1. \n\n Or insert 30 if you want to see active games, to join one");
        Objects.requireNonNull(methodnumber);
        methodnumber.clear();
        methodnumber.add(CommandType.CREATE_GAME);
        methodnumber.add(CommandType.ACTIVE_GAMES);
        System.out.print("Enter the method number and arguments: ");
        littleModelRepresentation.setMyNickname(name);
        InputManager.startThreaded(methodnumber);
    }

    /**
     * Moves a player on the flightboard from an old position to a new position.
     *
     * @param player The player to be moved.
     * @param newPos The new position on the flightboard.
     * @param oldPos The current position of the player before moving.
     * @throws Exception If an error occurs while moving the player.
     */
    @Override
    public void movePlayerOnFlightboard(Player player, int newPos, int oldPos) throws Exception {
        toShow();
    }

    /**
     * Adds a specific tile to a spaceship board at the given position.
     *
     * @param tile the {@code SpaceShipTile} to be added to the ship
     * @param row the row index where the tile should be placed
     * @param col the column index where the tile should be placed
     * @param ship the {@code ShipBoard} to which the tile is being added
     * @throws Exception if the operation fails for any reason
     */
    @Override
    public void addTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        toShow();
    }

    /**
     * Removes a block of tiles from the specified ship board.
     *
     * @param s the ship board from which the block will be removed
     * @param block the list of spaceship tiles to be removed from the ship board
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeBLock(ShipBoard s, ArrayList<SpaceShipTile> block) throws Exception {
        toShow();
    }

    /**
     * Updates the goods on the given ship board based on the provided storage tiles and new goods list.
     *
     * @param s the ship board to update
     * @param storagetiles a 2D ArrayList representing the coordinates of tiles to update in storage
     * @param newgoods a 2D ArrayList containing the new goods to add to storage tiles
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateGoods(ShipBoard s, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        toShow();
    }


    /**
     * Updates the battery positions on the provided ShipBoard and applies
     * the specified action based on the 'toLoose' parameter.
     *
     * @param BattxPos a two-dimensional ArrayList representing the positions
     *                 of the batteries on the ship.
     * @param shipBoard the ShipBoard instance representing the current state
     *                  of the player's ship.
     * @param toLoose a boolean parameter indicating whether the operation
     *                will result in losing some functionality or items.
     * @throws Exception if any error occurs during the update process.
     */
    @Override
    public void updateBatteries(ArrayList<ArrayList<Integer>> BattxPos, ShipBoard shipBoard, boolean toLoose) throws Exception {
        toShow();
    }

    /**
     * Removes a specified number of passengers from the ship and updates the tiles accordingly.
     *
     * @param s the ShipBoard instance representing the state of the spaceship.
     * @param tiles a nested ArrayList representing the tiles affected by passenger removal.
     * @param numPass the number of passengers to be removed.
     * @throws Exception if an error occurs during execution.
     */
    @Override
    public void removePassengers(ShipBoard s, ArrayList<ArrayList<Integer>> tiles, int numPass) throws Exception {
        toShow();
    }

    /**
     * Removes a specified number of passengers from a given tile on the ship board.
     *
     * @param s the ship board from which passengers will be removed
     * @param tile the specific tile to remove passengers from
     * @param numPass the number of passengers to remove from the tile
     * @throws Exception if an error occurs while removing passengers
     */
    @Override
    public void removePassengersFromTile(ShipBoard s, SpaceShipTile tile, int numPass) throws Exception {
        toShow();
    }

    /**
     * Removes the alien from the specified tile of the ship board.
     *
     * @param s the ShipBoard from which the alien is to be removed
     * @param tile the specific SpaceShipTile from which the alien is to be removed
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeAlienFromTile(ShipBoard s, SpaceShipTile tile) throws Exception {
        toShow();
    }

    /**
     * Updates the item being held or managed by a specific player.
     *
     * @param p     the player whose item in hand is to be updated
     * @param thing the new object to update as the item in hand for the player
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateThingInHand(Player p, Object thing) throws Exception {
        toShow();
    }

    /**
     * Adds a flipped tile to the view by its index.
     *
     * @param indextile the index of the tile to be added as flipped
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void addTileFlipped(int indextile) throws Exception {
        toShow();
    }

    /**
     * Handles the event when a player deposits a little deck.
     *
     * @param d       the little deck being deposited
     * @param player  the player who is depositing the little deck
     * @throws Exception if an error occurs while processing the deposit
     */
    @Override
    public void someoneDepositedLittleDeck(Deck d, Player player) throws Exception {
        toShow();
    }

    /**
     * Handles the event when a player picks a little deck.
     *
     * @param d      the deck that has been picked by the player
     * @param player the player who picked the deck
     * @throws Exception if an error occurs during the execution
     */
    @Override
    public void someonePickedLittleDeck(Deck d, Player player) throws Exception {
        toShow();
    }

    /**
     * Removes a tile from the flipped state.
     *
     * @param t the index of the tile to be removed from the flipped state
     * @throws Exception if an exceptional condition occurs during the operation
     */
    @Override
    public void removeTileFlipped(int t) throws Exception {
        toShow();
    }

    /**
     * Adds a passenger to the specified position (row and column) on the given ship board.
     *
     * @param s the ship board where the passenger will be added
     * @param row the row index where the passenger will be placed
     * @param column the column index where the passenger will be placed
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void addPassenger(ShipBoard s, int row, int column) throws Exception {
        toShow();
    }

    /**
     * Executes the logic designed to handle and enforce the consequences of specific actions
     * or events within the system. This method operates as a key component for managing
     * in-game penalties or repercussions, ensuring players or systems face appropriate
     * outcomes tied to their behavior or game events.
     *
     * This method is invoked during scenarios where actions or events trigger a need for
     * the application of rules or consequences, making it essential for maintaining balance
     * and integrity within the system's mechanics. The actual implementation details and
     * operations are defined based on the specific requirements of the game logic.
     */
    @Override
    public void youPayConsequences() {

    }

    /**
     * Sends a penalty of a specified type to the relevant component within the system.
     *
     * @param penalty the amount of the penalty to be applied
     * @param type the type or category of penalty to be sent
     */
    @Override
    public void sendPenalty(int penalty, String type) {

    }

    /**
     * Adds an alien to the specified position on the shipboard.
     *
     * @param s the shipboard to which the alien should be added
     * @param row the row position on the shipboard where the alien should be placed
     * @param column the column position on the shipboard where the alien should be placed
     * @param alienColor the color of the alien to be added
     * @throws Exception if an error occurs while adding the alien
     */
    @Override
    public void addAlien(ShipBoard s, int row, int column, AlienColor alienColor) throws Exception {
        toShow();
    }

    /**
     * Displays the first sight or initial view to the user.
     * This method is typically invoked to present the opening state or initial setup details.
     * It utilizes the internal {@code toShow()} method, which handles the actual rendering
     * of the relevant visuals or information.
     *
     * @throws Exception if an error occurs during the process of displaying the first sight.
     */
    @Override
    public void showFirstSight() throws Exception {
        toShow();
    }

    /**
     * Updates the current status by invoking the `toShow` method.
     * This method is intended to provide an updated view or status representation.
     *
     * @throws Exception if there is an error during the update process
     */
    @Override
    public void updateStatus() throws Exception {
        toShow();
    }

    /**
     * Executes a synchronized method to display the current game state and handles
     * associated processing based on the state of the game. Depending on the game state,
     * various outputs are displayed and user inputs are managed accordingly.
     *
     * The method performs specific actions for the following game states:
     * - INITIAL: Prints available commands, prompts the user for input, and displays a message.
     * - BUILDING: Displays the flight board, visible tiles, ship boards, and the user's current hand,
     *   and manages user input for commands. Also prints the remaining time.
     * - POSITIONING: Displays the flight board, ship boards, messages, and prompts for input to end the building phase.
     * - SET_NUM_TILE: Displays the flight board, ship boards, messages, and manages input for setting number tiles
     *   or choosing a subset of the ship.
     * - SET_ALIEN: Displays the flight board, ship boards, messages, and prompts for choosing an alien.
     * - FLIGHT: Displays the flight board, activated cards, associated commands, ship boards, messages, and manages
     *   user interaction for executing commands related to the current game card.
     * - SCORING: Ends the game, clears method commands, and stops input handling, displaying a game over message.
     *
     * @throws Exception if an error occurs during the method execution.
     */
    public synchronized void toShow() throws Exception {
        stateEnum gameState1 = littleModelRepresentation.getGameState();
        if (gameState1.equals(stateEnum.INITIAL)) {
            ArrayList<CommandType> commands = new ArrayList<>();
            tuiHandler.printCommand(commands);
            System.out.print("Enter the method number and arguments: ");
            InputManager.startThreaded(commands);
            System.out.println(littleModelRepresentation.getMessage());
        } else if (gameState1.equals(stateEnum.BUILDING)) {
            tuiHandler.printFlightBoard(littleModelRepresentation.getFlightBoard());
            tuiHandler.printTilesArray(littleModelRepresentation.getVisibleTiles());
            for (ShipBoard s : littleModelRepresentation.getShipBoards()) {
                tuiHandler.printShipBoard(s);
                if (s.getMyPlayer().getUsername().equals(littleModelRepresentation.getMyNickname())) {
                    tuiHandler.printThingInHand(s.getMyPlayer().getThingInHand(), s.getMyPlayer().getUsername(), littleModelRepresentation);
                }
            }
            System.out.println(littleModelRepresentation.getMessage());
            System.out.println(littleModelRepresentation.getTimeRemaining());

            ArrayList<CommandType> commands = getIntegers();
            tuiHandler.printCommand(commands);
            System.out.print("Enter the method number and arguments: ");
            InputManager.startThreaded(commands);

        } else if (gameState1.equals(stateEnum.POSITIONING)) {
            tuiHandler.printFlightBoard(littleModelRepresentation.getFlightBoard());
            for (ShipBoard s : littleModelRepresentation.getShipBoards()) {
                tuiHandler.printShipBoard(s);
            }
            System.out.println(littleModelRepresentation.getMessage());
            ArrayList<CommandType> commands = new ArrayList<>();
            commands.add(CommandType.END_BUILDING);
            tuiHandler.printCommand(commands);
            System.out.print("Enter the method number and arguments: ");
            InputManager.startThreaded(commands);
        } else if (gameState1.equals(stateEnum.SET_NUM_TILE)) {
            tuiHandler.printFlightBoard(littleModelRepresentation.getFlightBoard());
            for (ShipBoard s : littleModelRepresentation.getShipBoards()) {
                tuiHandler.printShipBoard(s);
            }
            System.out.println(littleModelRepresentation.getMessage());
            ArrayList<CommandType> commands = new ArrayList<>();
            commands.add(CommandType.SET_NUM_TILE);
            commands.add(CommandType.CHOOSE_ONE_SUB_SHIP);
            tuiHandler.printCommand(commands);
            System.out.print("Set num tile: ");
            InputManager.startThreaded(commands);
        } else if (gameState1.equals(stateEnum.SET_ALIEN)) {
            tuiHandler.printFlightBoard(littleModelRepresentation.getFlightBoard());
            for (ShipBoard s : littleModelRepresentation.getShipBoards()) {
                tuiHandler.printShipBoard(s);
            }
            System.out.println(littleModelRepresentation.getMessage());
            ArrayList<CommandType> commands = new ArrayList<>();
            commands.add(CommandType.COMMAND_FILL_TILE);
            tuiHandler.printCommand(commands);
            System.out.print("Choose alien: ");
            InputManager.startThreaded(commands);
        } else if (gameState1.equals(stateEnum.FLIGHT)) {
            ArrayList<CommandType> comandi;

            tuiHandler.printFlightBoard(littleModelRepresentation.getFlightBoard());
            tuiHandler.printCard(littleModelRepresentation.getActivatedcard(), littleModelRepresentation);
            for (ShipBoard s : littleModelRepresentation.getShipBoards()) {
                tuiHandler.printShipBoard(s);
            }
            System.out.println(littleModelRepresentation.getMessage());
            comandi = commandsPerCard(littleModelRepresentation.getActivatedcard());
            methodnumber.clear(); // Resetta la lista esistente
            methodnumber.addAll(comandi); // Aggiorna con i nuovi comandi
            if (!comandi.isEmpty()) tuiHandler.printCommand(comandi);
            System.out.print("Enter the method number and arguments: ");
            InputManager.startThreaded(comandi);

        } else if (gameState1.equals(stateEnum.SCORING)) {
//            tuiHandler.printFlightBoard(littleModelRepresentation.getFlightBoard());
//            for (ShipBoard s : littleModelRepresentation.getShipBoards()) {
//                tuiHandler.printShipBoard(s);
//            }
//            System.out.println(littleModelRepresentation.getMessage());
            ArrayList<CommandType> commands = new ArrayList<>();
            methodnumber.clear();
            methodnumber.addAll(commands);

            System.out.print("\n THE GAME ENDED \n");
            InputManager.stop();
            System.out.println("Press enter to close...");


        }
    }

    /**
     * Retrieves a list of command types based on the level of the LittleModelRepresentation.
     *
     * Depending on the level returned by littleModelRepresentation.getLev(), this method
     * populates an ArrayList with a corresponding set of CommandType values:
     * - Level 0 includes commands related to basic tile selections and actions.
     * - Level 1 includes additional commands such as activating timers and
     *   managing little decks, along with those supported at level 0.
     *
     * @return an ArrayList of CommandType enums representing the available commands
     *         for the current level in the LittleModelRepresentation.
     */
    private ArrayList<CommandType> getIntegers() {
        ArrayList<CommandType> commands = new ArrayList<>();
        if (littleModelRepresentation.getLev() == 0) {
            commands.add(CommandType.PICK_TILE_ALREADY_FLIPPED);
            commands.add(CommandType.PICK_TILE_UNKNOWN);
            commands.add(CommandType.DEPOSIT_TILE);
            commands.add(CommandType.END_BUILDING);
            commands.add(CommandType.INSERT_TILE);

        } else if (littleModelRepresentation.getLev() == 1) {
            commands.add(CommandType.ACTIVATE_TIMER);
            commands.add(CommandType.PICK_TILE_ALREADY_FLIPPED);
            commands.add(CommandType.PICK_TILE_UNKNOWN);
            commands.add(CommandType.PICK_LITTLE_DECK);
            commands.add(CommandType.DEPOSIT_LITTLE_DECK);
            commands.add(CommandType.DEPOSIT_TILE);
            commands.add(CommandType.END_BUILDING);
            commands.add(CommandType.INSERT_TILE);
            commands.add(CommandType.ADD_WAIT_TILE);
            commands.add(CommandType.INSERT_WAIT_TILE);
        }
        return commands;
    }

    /**
     * Determines the list of commands available for a specific card based on its state.
     *
     * @param card The card for which commands need to be determined. The card has a specific type
     *             and state that influences the available commands.
     * @return An ArrayList of CommandType objects representing the commands available
     *         for the given card in its current state. If no commands are available,
     *         an empty list is returned.
     */
    public ArrayList<CommandType> commandsPerCard(Card card) {
        c_State state = card.getStateEnum();
        ArrayList<CommandType> commands = new ArrayList<>();
        switch (card) {
            case Card_OpenSpace _ -> {
                if (state == c_State.OPEN_SPACE_PREPARATION) {
                    return new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_START_MOTOR));
                } else if (state == c_State.OPEN_SPACE_FINAL) {
                    System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                    return new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                }
            }
            case Card_Pirates _ -> {
                return switch (state) {
                    case PIRATES_CALC_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_CANNON_BATTERY_POS));
                    case PIRATES_EFFECT_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_HOW_TO_FACE_METEORS));
                    case PIRATES_WINNING_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_CLAIM_REWARD));
                    case CHOOSE_DISC_BLOCK -> {
                        System.out.println("Before you continue you have to choose one sub ship");
                        yield new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_ONE_SUB_SHIP));
                    }
                    case PIRATES_END_STATE -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    default -> commands;
                };
            }
            case Card_Smugglers _ -> {
                return switch (state) {
                    case SMUGGLERS_CALC_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_CANNON_BATTERY_POS));
                    case SMUGGLERS_LOST_BATTERIES_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_PLACE_BATTERIES));
                    case SMUGGLERS_LOST_GOODS_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_WHERE_TO_PUT_GOODS));
                    case SMUGGLERS_WINNING_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_CLAIM_REWARD_WITH_GOODS));
                    case SMUGGLERS_END_STATE -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    default -> commands;
                };
            }
            case Card_Slavers _ -> {
                return switch (state) {
                    case SLAVERS_CALC_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_CANNON_BATTERY_POS));
                    case SLAVERS_LOSING_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_PASSENGERS_TO_LOSE));
                    case SLAVERS_WINNING_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_CLAIM_REWARD));
                    case SLAVERS_END_STATE -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    default -> commands;
                };
            }
            case Card_MeteorSwarm _ -> {
                return switch (state) {
                    case METEOR_CARD_EFFECT_STATE, METEOR_CARD_CALC_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_HOW_TO_FACE_METEORS));
                    case METEOR_CARD_END_STATE -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    case CHOOSE_DISC_BLOCK -> {
                        System.out.println("Before you continue you have to choose one sub ship");
                        yield new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_ONE_SUB_SHIP));
                    }
                    default -> commands;
                };
            }
            case Card_AbandonedShip _ -> {
                return switch (state) {
                    case ABANDONED_SHIP_PREPARATION ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_PASSENGERS_TO_LOSE));
                    case ABANDONED_SHIP_TAKEN, ABANDONED_FINAL_STATE -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    default -> commands;
                };
            }
            case Card_AbandonedStation _ -> {
                return switch (state) {
                    case ABANDONED_STATION_START_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_ABANDONED_STATION));
                    case ABANDONED_STATION_END_STATE -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    default -> commands;
                };
            }
            case Card_Combat_zone _ -> {
                return switch (state) {
                    case COMBAT_ZONE_BEGIN -> commands;
                    case COMBAT_ZONE_CHOOSING_PASS ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_PASSENGERS_TO_LOSE));
                    case COMBAT_ZONE_CHOOSE_GOODS ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_WHERE_TO_PUT_GOODS));
                    case COMBAT_ZONE_CHOOSE_BATTERIES ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_PLACE_BATTERIES));
                    case METEOR_CARD_EFFECT_STATE_FROM_COMBAT ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_HOW_TO_FACE_METEORS));
                    case COMBAT_ZONE_FINAL -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    case COMBAT_MOTION_POWER ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_START_MOTOR));
                    case COMBAT_FIRE_POWER ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_START_FIRE_POWER));
                    case CHOOSE_DISC_BLOCK -> {
                        System.out.println("Before you continue you have to choose one sub ship");
                        yield new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_ONE_SUB_SHIP));
                    }
                    default -> commands;
                };
            }
            case Card_Epidemic _ -> {
                return switch (state) {
                    case EPIDEMIC_STATE_FINAL -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    default -> commands;
                };
            }
            case Card_PlanetCard _ -> {
                return switch (state) {
                    case PLANET_STATE_INIT ->
                            new ArrayList<>(Collections.singletonList(CommandType.ACCEPT_TO_LAND_ON_A_PLANET));
                    case PLANET_STATE_ADD_GOODS ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_WHERE_TO_PUT_GOODS));
                    case PLANET_FIN_STAT -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    default -> commands;
                };
            }
            case Card_Stardust cardStardust -> {
                return switch (state) {
                    case STARDUST_FINAL -> {
                        System.out.print("You have 10 seconds to surrend, if you want to, insert 4: ");
                        yield new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    }
                    default -> commands;
                };
            }
            default -> {
            }
        }

        return commands;
    }

    /**
     * Displays a list of old games and provides options for interacting with them.
     * If the list is empty, indicates that there are no games to interact with.
     *
     * @param oldGames the list of OldGame objects to be displayed
     * @throws Exception if any error occurs while displaying the old games
     */
    @Override
    public void showOldGames(ArrayList<OldGame> oldGames) throws Exception {
        if (oldGames.isEmpty()) {
            System.out.println("There are no active Games :(");
            System.out.println("Enter the method number and arguments: ");
        } else {
            for (OldGame oldGame : oldGames) {
                System.out.println("\n" + "Game " + oldGame.getN() + " joined players: " + oldGame.getJoined() + " max players: " + oldGame.getMax() + " and uuid: " + oldGame.getUuid() + "\nNow type 1 [GameNumber] to enter the game or 0 [level] [NumPlayer] to create another one");
            }
        }
    }

    /**
     * Sets the view for the GUI component.
     *
     * @param view the GUI object that represents the view to be set
     */
    @Override
    public void setView(GUI view) {

    }

    /**
     * Displays an error dialog with the given title and message.
     *
     * @param title   the title of the error dialog
     * @param message the message to be displayed in the error dialog
     * @throws Exception if an error occurs while displaying the dialog
     */
    @Override
    public void showErrorDialog(String title, String message) throws Exception {
        toShow();
    }

    /**
     * Displays an informational dialog to the user with the specified title and message.
     *
     * @param title   the title of the dialog
     * @param message the message to display in the dialog
     * @throws Exception if an error occurs while displaying the dialog
     */
    @Override
    public void showInfoDialog(String title, String message) throws Exception {
        toShow();
    }

    /**
     * Ends the application by displaying the provided shutdown reason
     * and calling System.exit with a success status.
     *
     * @param reason the reason for shutting down the application
     */
    @Override
    public void showShutdown(String reason) {
        System.out.println(reason);
        System.exit(0);
    }

    /**
     * Handles the action of inserting a wait tile for the given player's nickname into
     * the little model representation in the context of the game logic. This method
     * performs an update operation based on the player's identifier.
     *
     * @param playerNickname the nickname of the player for whom the wait tile is being inserted
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void insertwaittileLMR1(String playerNickname) throws Exception {
        toShow();
    }

    /**
     * Handles the insertion logic for a wait tile and invokes the toShow method.
     *
     * @param playerNickname the nickname of the player for whom the wait tile insertion logic is being executed
     * @throws Exception if an error occurs during the execution of this method
     */
    @Override
    public void insertwaittileLMR2(String playerNickname) throws Exception {
        toShow();
    }

    /**
     * Processes and validates the provided input message.
     *
     * @param message the input message to be processed and validated
     * @throws Exception if an error occurs during processing or validation
     */
    @Override
    public void correctinput(String message) throws Exception {
        toShow();
    }

    /**
     * Determines and proceeds to the next player's turn in the game based on the current state.
     *
     * @param myNickname the nickname of the current player whose turn is ending
     * @throws Exception if an error occurs while determining or transitioning to the next player's turn
     */
    @Override
    public void nextPlayerTurn(String myNickname) throws Exception {
        toShow();
    }

    /**
     * Displays timed information to the user for a specific duration.
     *
     * @param text The information text to be displayed.
     * @param seconds The duration in seconds for which the information is visible.
     * @throws Exception If an error occurs during the display process.
     */
    @Override
    public void showTimedInfo(String text, int seconds) throws Exception {
        toShow();
    }

    /**
     * Updates the number of batteries remaining for the specified player.
     *
     * @param p The player whose remaining batteries are to be updated.
     * @param batt The new number of batteries remaining for the player.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateBatteriesRemaining(Player p, int batt) throws Exception {
        toShow();
    }


}
