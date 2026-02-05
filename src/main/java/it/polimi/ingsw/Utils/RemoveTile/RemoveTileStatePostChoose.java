package it.polimi.ingsw.Utils.RemoveTile;

import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.ArrayList;

public class RemoveTileStatePostChoose extends RemoveTileState {
    /**
     * Constructor for the RemoveTileStatePostChoose state.
     *
     * @param shipBoard The current ShipBoard instance representing the player's board,
     *                  which manages the state and tiles being manipulated.
     */
    public RemoveTileStatePostChoose(ShipBoard shipBoard) {
        super(shipBoard);
    }

    /**
     * Determines and returns the next state in the tile removal process.
     *
     * This method transitions from the current state to a predefined next state
     * in the state machine governing the tile removal process for the game. It
     * creates a new instance of the RemoveTileStatePreChoose class, which is
     * associated with a specific phase of the tile removal logic.
     *
     * @return A new instance of RemoveTileStatePreChoose representing the next state in the process.
     */
    @Override
    public RemoveTileState GetNextState() {
        return new RemoveTileStatePreChoose(shipBoard);
    }

    /**
     * Executes the main logic for the state. This method processes the removal of blocks
     * from sub-ships, updates the ship matrix, and handles certain state-specific operations.
     *
     * @param subShips         A list of sub-ships, represented as a 2D ArrayList of SpaceShipTile objects.
     * @param shipBoard        The game board where the ship is located and the operation is performed.
     * @param playerID         The ID of the player initiating this state operation.
     * @param indexToPreserve  The index of the sub-ship that must be preserved during the operation.
     * @param waste            An unused parameter; may represent additional logic or resources in the future.
     * @throws Exception       If an error occurs during the processing of the state.
     */
    @Override
    public void StateMain(ArrayList<ArrayList<SpaceShipTile>> subShips, ShipBoard shipBoard, int playerID, int indexToPreserve, int waste) throws Exception {
        for (int i = 0; i < subShips.size(); i++) {
            if (i != indexToPreserve) {
                System.out.println("ciao, sono qui e devo rimuovere un blocco");
                shipBoard.removeBlock(subShips.get(i));
                shipBoard.getFlightBoard().getMygame().getEventBus().removeBlock(subShips.get(i), shipBoard.getMyPlayer().getUsername());
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (shipBoard.getShipMatrix()[i][j] == null || shipBoard.getShipMatrix()[i][j].getID() == -1) {
                    ArrayList<Type_side_connector> emptyConnector = new ArrayList<>();
                    for (int k = 0; k < 4; k++) {
                        emptyConnector.add(Type_side_connector.SMOOTH_SIDE);
                    }
                    shipBoard.getShipMatrix()[i][j] = new SpaceShipTile(0, emptyConnector, SSTTypes.Tile_NonAccesiblePlace);
                    shipBoard.getShipMatrix()[i][j].setAttachedShip(shipBoard);
                }
                if (shipBoard.getShipMatrix()[i][j].getAttachedShip() == null) {
                    shipBoard.getShipMatrix()[i][j].setAttachedShip(shipBoard);
                }
            }
        }

        //shipBoard.checkSurrender();
        shipBoard.setchoosesubship(false);
    }

    /**
     * Executes the main logic for the current state on the provided ShipBoard.
     *
     * @param shipBoard The ShipBoard instance representing the current state of the game.
     * @param row The row index of the operation to be performed on the ShipBoard.
     * @param col The column index of the operation to be performed on the ShipBoard.
     * @param fromMistake A boolean indicating whether the method is invoked as a result of a mistake.
     * @throws Exception if an error occurs during the execution of the state logic.
     */
    @Override
    public void StateMain(ShipBoard shipBoard, int row, int col, boolean fromMistake) throws Exception {

    }

    /**
     * Allows a player to choose one specific sub-ship from the list of available sub-ships
     * after a split operation on the ship has occurred. This method updates the game
     * state to reflect the player's choice.
     *
     * @param index the index of the sub-ship to be selected from the list of sub-ships.
     * @param playerID the unique identifier of the player making the selection.
     * @throws Exception if the provided player ID is invalid, the index is out of bounds,
     *                   or if an error occurs during the selection process.
     */
    @Override
    public void chooseOneSubShip(int index, int playerID) throws Exception {

    }
}
