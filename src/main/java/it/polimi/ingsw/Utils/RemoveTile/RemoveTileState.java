package it.polimi.ingsw.Utils.RemoveTile;

import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class RemoveTileState implements Serializable {
    /**
     * Represents the ship board associated with the current state.
     * This variable holds the main structure or layout of the spaceship tiles
     * and is manipulated by the state methods to update its configuration.
     * It is expected to be initialized when constructing the state and used
     * throughout the operations of various state transitions and modifications.
     */
    protected ShipBoard shipBoard;
    /**
     * Represents a collection of subsets of spaceship tiles, where each subset is a group of connected tiles
     * that forms a part of a ship on the game board.
     *
     * This variable is used to manage and manipulate the individual segments of ships that result from
     * splitting or removing tiles on the shipboard during the game.
     */
    protected ArrayList<ArrayList<SpaceShipTile>> subShips;

    /**
     * Initializes a new instance of the RemoveTileState class with the given ship board.
     *
     * @param shipBoard The ship board associated with this state.
     */
    public RemoveTileState(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    /**
     * Determines the next state in the tile removal state machine.
     *
     * @return The next state object of type RemoveTileState.
     */
    public abstract RemoveTileState GetNextState();

    /**
     * This method represents the main logic for a specific state in the game. It processes
     * sub-ships and ship board configurations, and manages state for a particular player.
     *
     * @param subShips An ArrayList of ArrayLists containing SpaceShipTile objects, representing
     *                 the sub-ships involved in the current state.
     * @param shipBoard The game board where the ship-related operations take place.
     * @param playerID The ID of the player whose turn or action is being processed.
     * @param indextopreserve An integer indicating the index of the sub-ship or component
     *                        that needs to be preserved during this state operation.
     * @param waste An integer representing the waste or loss associated with the operation
     *              in this state.
     * @throws Exception If an error occurs during state processing.
     */
    public abstract void StateMain(ArrayList<ArrayList<SpaceShipTile>> subShips, ShipBoard shipBoard, int playerID, int indextopreserve, int waste) throws Exception;

    /**
     * Executes the main logic for modifying the state of the ShipBoard at a specific location.
     *
     * @param shipBoard the ShipBoard instance on which the state logic will be applied
     * @param row the row index of the tile to be processed
     * @param col the column index of the tile to be processed
     * @param fromMistake a boolean indicating whether the action originates from a mistake
     * @throws Exception if an error occurs during execution of the state logic
     */
    public abstract void StateMain(ShipBoard shipBoard, int row, int col, boolean fromMistake) throws Exception;

    /**
     * Allows a player to choose one specific sub-ship from the sub-ships list after a ship has been split.
     * This operation is triggered when ship splitting occurs, and the player must decide which sub-ship to retain or handle.
     *
     * @param index the index of the sub-ship to be chosen from the list of generated sub-ships.
     * @param playerID the unique identifier of the player making the choice.
     * @throws Exception if the player ID is invalid, the index is out of bounds, or other issues arise during the operation.
     */
    public abstract void chooseOneSubShip(int index, int playerID) throws Exception;
}