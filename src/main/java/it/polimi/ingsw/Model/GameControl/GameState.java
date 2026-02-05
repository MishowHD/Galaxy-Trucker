package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameState implements Serializable {
    /**
     * Represents an instance of the game associated with the current game state.
     * This variable is used to manage and interact with the primary game logic and elements
     * throughout the lifecycle of the game state.
     */
    protected Game game;

    /**
     * Creates a new instance of GameState associated with the specified game.
     *
     * @param game the game instance that this GameState will manage
     */
    public GameState(Game game) {
        this.game = game;
    }

    /**
     * Allows a player to join an ongoing game session.
     *
     * @param NickName The nickname of the player joining the game.
     * @param NumOfPlayers The number of players currently in the game.
     * @throws Exception If the game cannot accommodate the new player or any other error occurs during the join process.
     */
    public void JoinGame(String NickName, int NumOfPlayers) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Represents the main state execution of the game.
     * This method is placeholder and currently not supported.
     *
     * @throws Exception always thrown as the method is not implemented.
     */
    public void StateMain() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Extracts the deck of cards or components associated with the game state.
     * This method is intended to handle operations related to deck retrieval.
     *
     * @throws RuntimeException if the operation is not supported or an error occurs during execution
     */
    public void extractDeck() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Activates the timer for the specified player.
     *
     * @param player the player for whom the timer is to be activated
     * @throws Exception if there is an issue activating the timer
     */
    public void activateTimer(Player player) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Verifies and corrects the building configuration for all players in the game.
     * Iterates through each player's ship matrix and checks the positioning of each tile.
     * If corrections are required, it modifies the ship configuration accordingly.
     * The method continues corrections until the ship configuration becomes stable.
     *
     * @return 1 if the building configuration is correct for all players, -1 if an
     *         invalid configuration is found during the verification process.
     * @throws Exception if an error occurs during the validation or correction process.
     */
    public int CheckandCorrectBuilding() throws Exception {
        for (Player p : game.getPlayers()) {
            boolean corrections = true;
            while (corrections) {
                corrections = false;
                ShipBoard shipBoard = p.getMyShip();
                SpaceShipTile[][] shipMatrix = shipBoard.getShipMatrix();
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 7; j++) {
                        int num;
                        if (shipMatrix[i][j].getType() == SSTTypes.Tile_NonAccesiblePlace) {
                            num = 1;
                        } else {
                            num = shipMatrix[i][j].CheckCorrectPositioning(i, j);
                        }
                        if (p.getMyShip().isModified()) {
                            corrections = true;
                            p.getMyShip().setModified(false);
                        }
                        if (num == -1) {
                            return -1;
                        }
                    }
                }
            }
        }
        return 1;
    }

    /**
     * Sets the number of tiles for a specific player's shipboard.
     *
     * @param playerid the unique identifier of the player whose shipboard tile count is to be set
     * @param posTile the number of tiles to set on the player's shipboard
     * @throws Exception if an error occurs while setting the tile count
     */
    public void setNumTile(int playerid, int posTile) throws Exception {
        game.getPlayer(playerid).getMyShip().setNumTile(posTile);

    }

    /**
     * Fills a specified tile on the game board for the given player.
     *
     * @param playerid   The ID of the player performing the action.
     * @param wantalien  Indicates if the player wants to use an alien.
     * @param color      The color of the alien to be used if applicable.
     * @param row        The row index of the tile to be filled.
     * @param column     The column index of the tile to be filled.
     * @throws Exception If any error occurs during the tile-filling operation.
     */
    public void CommandFillTile(int playerid, boolean wantalien, AlienColor color, int row, int column) throws Exception {

    }

    /**
     * Completes the current building phase in the game process.
     * This method typically signals the conclusion of the building phase for a player
     * or all players, depending on the game's context and state.
     *
     * @throws Exception if an error occurs during the completion of the building phase.
     */
    public void completeBuildingPhase() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to surrender and leave the game.
     *
     * @param playerID The unique identifier of the player who wishes to surrender.
     * @throws Exception If an error occurs during the surrender process.
     */
    public void Surrend(int playerID) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Automatically fills tiles for a player based on game logic.
     *
     * This method is intended to handle automatic filling of tiles during the game,
     * typically used in scenarios where a player may not manually choose tiles or
     * when automated game progression is required.
     *
     * @throws Exception if the automatic tile filling process encounters an error
     *                    or cannot be completed due to unforeseen circumstances.
     */
    public void AutomaticFillTile() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the next state of the game.
     * This method is expected to handle the transition between game states.
     *
     * @return the next GameState object representing the upcoming state in the game.
     * @throws Exception if the transition to the next state cannot be performed.
     */
    public GameState getNextState() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Visualizes the final scores of the game for all players.
     * This method generates a summary of final scores by providing the player names
     * and their respective scores in a key-value format.
     *
     * @return a HashMap where the keys represent player names (String),
     *         and the values represent their final scores (Float).
     * @throws RuntimeException if the method is not supported or cannot complete the operation.
     */
    public HashMap<String, Float> visualizeFinalScores() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Handles the action of picking a tile that has already been revealed.
     *
     * @param index The index of the tile to be picked, representing its position in a collection or array.
     * @param playerID The unique identifier of the player attempting to pick the tile.
     * @throws Exception If the action cannot be completed or is not supported.
     */
    public void pickTileAlreadyFlipped(int index, int playerID) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to pick a tile of unknown properties. This may involve
     * specific game logic to determine which tile is picked and its effects.
     *
     * @param playerID The unique identifier of the player performing the action.
     * @throws Exception If the action cannot be completed due to game rules or constraints.
     */
    public void pickTileUnknown(int playerID) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to pick a specific tile during the game.
     *
     * @param index The index of the tile to be picked.
     * @param playerID The ID of the player picking the tile.
     * @throws Exception If the action is not supported or encounters an error.
     */
    public void pickTile(int index, int playerID) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Demonstrates a playable game session.
     * This method is typically used to showcase the gameplay mechanics
     * or simulate a session in the context of the current game state.
     *
     * @throws Exception If any error or exception occurs during the game demonstration.
     */
    public void demoGame() throws Exception {
    }

    /**
     * Allows a player to pick a smaller deck from a set of decks within the game.
     * This method is intended to facilitate gameplay interaction related to deck management.
     *
     * @param index the index of the small deck to be picked
     * @param playerID the unique identifier of the player performing the action
     * @throws Exception if the operation cannot be completed or is unsupported
     */
    public void pickLittleDeck(int index, int playerID) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Deposits the little deck for the specified player.
     *
     * @param playerID the unique identifier of the player performing the deck deposit.
     * @throws Exception if an error occurs during the operation.
     */
    public void depositLittleDeck(int playerID) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Handles the action for depositing a tile associated with a specific player
     * in the game state. The logic of this method is dependent on the current
     * implementation of the game rules.
     *
     * @param playerID the unique identifier of the player who is depositing the tile
     * @throws Exception if an error occurs during the tile deposit process
     */
    public void depositTile(int playerID) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Ends the building phase for a specific player and transitions to another position or state.
     *
     * @param PlayerID the ID of the player ending their building phase
     * @param positionwheretogo the position or state to transition to after ending the building phase
     * @throws Exception if an error occurs during the transition
     */
    public void endbuilding(int PlayerID, int positionwheretogo) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Ends the building phase for all players in the game.
     * This method is used to conclude the current building stage and transition
     * to the subsequent phase for all players simultaneously.
     *
     * @throws Exception if an error occurs while finalizing the building phase.
     */
    public void endBuildingPhaseForAll() throws Exception {
    }

    /**
     * Inserts a tile into the game board at the specified position and updates the game state accordingly.
     *
     * @param row      The row index where the tile will be inserted.
     * @param col      The column index where the tile will be inserted.
     * @param playerID The ID of the player performing the action.
     * @param rotation The rotation of the tile to be inserted.
     * @throws Exception If the insertion fails due to invalid input, invalid game state, or other errors.
     */
    public void insertTile(int row, int col, int playerID, int rotation) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Activates effects within the game state. This method is intended to handle
     * the logic for triggering specific gameplay effects, which may include
     * interacting with game objects, modifying state, or resolving actions.
     *
     * Throws:
     *   Exception - if an error occurs during the effect activation process.
     */
    public void EffectActivation() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the card currently in use in the game state.
     *
     * @return the card that is currently in use
     * @throws RuntimeException if the operation is not supported
     */
    public Card getCardinuse() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to choose a sub-ship based on the provided index.
     *
     * @param index the index of the sub-ship to select
     * @param playerID the unique identifier of the player making the selection
     * @throws Exception if the selection process encounters an error
     */
    public void chooseOneSubShip(int index, int playerID) throws Exception {

    }

    /**
     * Allows a player to accept or decline landing on a specific planet during the game.
     *
     * @param p        The player who is deciding whether to land on the planet.
     * @param yOn      A boolean indicating the player's choice; true if the player agrees to land, false otherwise.
     * @param NumPlanet The number of the planet the player wants to land on.
     * @throws Exception If the action cannot be completed due to game state or other constraints.
     */
    public void acceptToLandOnAPlanet(Player p, boolean yOn, int NumPlanet) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Handles the process where a player chooses an abandoned station during the game.
     *
     * @param player The player making the choice.
     * @param flightBoard The flight board associated with the game state.
     * @param yOn A boolean value indicating specific game state or condition.
     * @param storageTiles A 2D list of integers representing storage tiles.
     * @param newGoods A 2D list of goods to be placed in the abandoned station.
     * @throws Exception If an error occurs during the operation.
     */
    public void chooseAbandonedStation(Player player, FlightBoard flightBoard, boolean yOn, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows the player to choose positions for cannon batteries on the FlightBoard.
     *
     * @param fb The current FlightBoard instance where the cannon batteries will be placed.
     * @param player The Player making the selection of positions.
     * @param cannonPos A list of positions for the cannons specified as a nested ArrayList structure.
     * @param batteriesPos A list of positions for the batteries specified as a nested ArrayList structure.
     * @throws Exception if an error occurs during the operation or invalid positions are selected.
     */
    public void chooseCannonBatteryPos(FlightBoard fb, Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Determines how the player will face incoming meteors and manage their defense accordingly.
     *
     * @param player The player who is deciding how to defend against the meteors.
     * @param howToDefenceFromShots A list of integers representing the player's decisions or strategies for defending against meteor shots.
     * @param flightBoard The flight board representing the game state during the meteor encounter.
     * @throws Exception If an error occurs during the decision-making process.
     */
    public void chooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, FlightBoard flightBoard) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }


    /**
     * Allows a player to choose which passengers to lose, based on the provided parameters.
     *
     * @param p The player who is making the decision about losing passengers.
     * @param yOn A boolean flag indicating whether a specific condition is activated during this decision-making process.
     * @param pass A 2D list containing details about the passengers, structured as a list of lists of integers.
     * @param flightBoard The current state of the flight board associated with the game.
     * @throws Exception If the process of determining which passengers to lose cannot be completed or encounters an error.
     */
    public void choosePassengersToLose(Player p, boolean yOn, ArrayList<ArrayList<Integer>> pass, FlightBoard flightBoard) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to decide whether to claim a reward or not during the game.
     *
     * @param yOn a boolean indicating the player's decision to claim the reward (true to claim, false otherwise)
     * @param player the player making the decision to claim the reward
     * @throws Exception if any error occurs during the execution of the method
     */
    public void chooseToClaimReward(boolean yOn, Player player) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to choose whether to claim a reward in the context of the game,
     * involving the provided flight board, storage tiles, and goods.
     *
     * @param fb           the flight board object representing the game state that may be relevant to the reward.
     * @param yOn          a boolean indicating additional context or conditions for claiming the reward.
     * @param player       the player object for whom the reward decision is being made.
     * @param storageTiles a two-dimensional ArrayList of integers representing the tiles in the player's storage.
     * @param newGoods     a two-dimensional ArrayList of Goods representing the new goods that might be involved in the reward.
     * @throws Exception if an error occurs during the reward claiming decision process.
     */
    public void chooseToClaimReward(FlightBoard fb, boolean yOn, Player player, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to choose where to place the batteries within their configuration.
     *
     * @param p the player making the choice for battery placement
     * @param posBatAndNumBattXPos a two-dimensional list representing the possible positions
     *                             for batteries and the corresponding number of batteries at each position
     * @throws Exception if any error occurs during the execution of the method
     */
    public void chooseToPlaceBatteries(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Determines and initiates the sequence for activating double firepower and specified cannon batteries
     * for a player during the game.
     *
     * @param p The player who is choosing to start firepower.
     * @param DoubFireTriplets A list of triplets, where each triplet denotes a double cannon activation.
     * @param BatteriesToAct A list of cannon battery positions to act upon during the firepower phase.
     * @throws Exception If an error occurs during the process.
     */
    public void chooseToStartFirePower(Player p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to attempt starting the motor of their ship using specified engines and batteries.
     *
     * @param player The player attempting to start the motor.
     * @param flightBoard The current state of the flight board.
     * @param enginesPos A list of engine positions that the player wants to activate.
     * @param batteriesPos A list of battery positions to be used for powering the engines.
     * @throws Exception If an error occurs during the process of starting the motor.
     */
    public void chooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows the player to determine where to place goods during the game.
     *
     * @param player the player making the decision about goods placement
     * @param posGoods a nested list of integers representing possible positions to place the goods
     * @param goodsSets a nested list of goods objects representing the sets of goods available for placement
     * @throws Exception if an error occurs during the decision-making process
     */
    public void chooseWhereToPutGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Starts a timer that monitors the surrender process for players or game actions.
     * This method initiates the countdown until the surrender is handled or processed.
     *
     * @throws Exception if the timer initialization or surrender processing fails.
     */
    public void startSurrenderTimer() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Callback method invoked when a timer completes its countdown.
     *
     * @param isLastActivation indicates whether this is the final activation of the timer.
     * @throws Exception if an error occurs during timer completion handling.
     */
    public void onTimerComplete(boolean isLastActivation) throws Exception {

    }

    /**
     * Inserts a wait tile for a specific player at a designated position with a specified rotation.
     *
     * @param playerID The unique identifier for the player.
     * @param index    The index of the wait tile being inserted.
     * @param row      The row position where the tile is to be placed.
     * @param col      The column position where the tile is to be placed.
     * @param rotation The rotation value of the tile, typically in degrees.
     * @throws Exception If there is an issue with inserting the tile.
     */
    public void insertWaitTile(int playerID, int index, int row, int col, int rotation) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Adds a wait tile for the player identified by the given player ID.
     *
     * @param playerID the unique identifier of the player for whom the wait tile is being added
     * @throws Exception if an error occurs while adding the wait tile
     */
    public void addWaitTile(int playerID) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

}
