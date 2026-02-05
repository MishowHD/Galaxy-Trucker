package it.polimi.ingsw.Utils.RemoveTile;

import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Boards.ShipBoard_TestFlight;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.ArrayList;

public class RemoveTileStatePreChoose extends RemoveTileState {

    /**
     * Represents an integer variable used to track the activation state or status
     * within the RemoveTileStatePreChoose class.
     *
     * This variable may be used to control or determine the flow of operations
     * related to the removal of tiles, such as activating specific functions,
     * steps, or transitions within the class methods.
     */
    private int activate;
    /**
     * Represents the player currently interacting with the state.
     * This variable is a reference to the active player whose actions or choices
     * are being processed or managed within the current state of the game.
     * It is final as the reference to the player should not change during the
     * lifecycle of the state object.
     */
    private final Player player;

    /**
     * Constructor for the RemoveTileStatePreChoose class.
     * Initializes the state with the given ship board and sets up default values for activation and player.
     *
     * @param shipBoard The ShipBoard instance associated with the current game state.
     */
    public RemoveTileStatePreChoose(ShipBoard shipBoard) {
        super(shipBoard);
        activate = 0;
        this.player = shipBoard.getMyPlayer();
    }

    /**
     * Determines the next state in the tile removal state machine.
     *
     * @return The next state object, which is an instance of RemoveTileStatePostChoose,
     * initialized with the current shipBoard.
     */
    @Override
    public RemoveTileState GetNextState() {
        return new RemoveTileStatePostChoose(shipBoard);
    }

    /**
     * Executes the main actions associated with the current state of the game, modifying the game state based on the input parameters.
     *
     * @param subShips a 2D list of SpaceShipTile objects representing the current state and configuration of the sub-ships.
     * @param shipBoard the ShipBoard object representing the current state of the game board.
     * @param playerID the identifier of the player performing the action.
     * @param indextopreserve the index of an element that should be preserved during the operation.
     * @param waste an additional integer parameter that can be used to determine further state behavior or tracking.
     * @throws Exception if an error occurs during the execution of the state logic.
     */
    @Override
    public void StateMain(ArrayList<ArrayList<SpaceShipTile>> subShips, ShipBoard shipBoard, int playerID, int indextopreserve, int waste) throws Exception {

    }

    /**
     * Handles the primary logic for removing a tile from the spaceship board, including
     * processing any effects or updates caused by the removal, such as modifying nearby
     * tiles, updating the game state, and interacting with the event bus.
     *
     * @param shipBoard The current state of the spaceship board used for accessing and modifying tiles.
     * @param row The row index of the tile to be removed.
     * @param col The column index of the tile to be removed.
     * @param fromMistake Indicates whether the tile removal was triggered due to a mistake.
     * @throws Exception Thrown if any errors occur during the execution of the method or if the operation fails.
     */
    @Override
    public void StateMain(ShipBoard shipBoard, int row, int col, boolean fromMistake) throws Exception {
        Player player = shipBoard.getMyPlayer();
        SpaceShipTile tileToRemove = shipBoard.getShipMatrix()[row][col];
        if (tileToRemove == null || tileToRemove.getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            System.out.println("Tile already removed or non-accessible.");
            shipBoard.getFlightBoard().getMygame().getEventBus().wrongInput(shipBoard.getMyPlayer());
            return;
        }
        switch (tileToRemove.getType()) {
            case SSTTypes.Tile_BatteryComponent ->
                    shipBoard.getFlightBoard().getBank().addBattery(tileToRemove.getNumCharges());
            case SSTTypes.Tile_CargoHold ->
                    shipBoard.getFlightBoard().getBank().addGoodsFromList(tileToRemove.getEffectivePresentGoods());//do good alla banca
            case SSTTypes.Tile_AlienLifeSupport -> {
                int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

                for (int[] d : dirs) {
                    int r = row + d[0];
                    int c = col + d[1];
                    if (r < 0 || r >= shipBoard.getShipMatrix().length ||
                            c < 0 || c >= shipBoard.getShipMatrix()[0].length) {
                        continue;
                    }
                    SpaceShipTile neigh = shipBoard.getShipMatrix()[r][c];
                    if (neigh == null) continue;
                    if (neigh.getType().equals(SSTTypes.Tile_Cabin) && neigh.getIsThereAlien()) {
                        neigh.removeAlien();
                        shipBoard.getFlightBoard().getMygame()
                                .getEventBus()
                                .removeAlien(player.getUsername(), r, c);
                        break;
                    }
                }
            }
        }
        shipBoard.getFlightBoard().getMygame().getEventBus().removeSingleTile(shipBoard.getMyPlayer().getUsername(), row, col, fromMistake, 1);
        shipBoard.getShipMatrix()[row][col] = null;
        ArrayList<Type_side_connector> emptyConnector = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            emptyConnector.add(Type_side_connector.SMOOTH_SIDE);
        }
        SpaceShipTile nonAccessibleTile = new SpaceShipTile(0, emptyConnector, SSTTypes.Tile_NonAccesiblePlace);
        shipBoard.insertTile(nonAccessibleTile, row, col);
        shipBoard.getShipMatrix()[row][col].setAttachedShip(shipBoard);
        if (shipBoard.getClass() == ShipBoard_TestFlight.class) {
            if (!fromMistake) {
                shipBoard.addLostPieces(1);
            }
        } else {
            shipBoard.addLostPieces(1);
        }
        subShips = shipBoard.findConnectedBlocks();
        if (subShips.size() > 1) {
            activate = 1;
            System.out.println("sono qui e devo rimuovere un blocco");
            System.out.println("scegli il blocco da rimuovere " + player.getUsername());
            System.out.println(subShips.size());
            for (int i = 0; i < subShips.size(); i++) {
                System.out.println(subShips.get(i).toString() + " " + i);
            }
            shipBoard.getFlightBoard().getMygame().getEventBus().messageToChooseSubship(player, subShips);
            shipBoard.setchoosesubship(true);
        } else {
            shipBoard.getFlightBoard().getMygame().getEventBus().ChooseSubship(player.getUsername(), subShips, 0, 1);
            shipBoard.Gonextstate(subShips, 0, player.getPlayerId(), 1);
            activate = 0;
        }
    }

    /**
     * Allows a player to select one subship from the available subships. It validates the player's
     * input and ensures the action is performed by the correct player. If the input is invalid or
     * the player is incorrect, appropriate events are triggered.
     *
     * @param index   the index of the subship to be chosen from the list of subships
     * @param playerID the unique identifier of the player attempting to make the selection
     * @throws Exception if an error occurs during the selection process or notification of events
     */
    public void chooseOneSubShip(int index, int playerID) throws Exception {
        if (activate == 1) {
            if (player.getPlayerId() == playerID) {
                if (index < 0 || index >= subShips.size()) {
                    shipBoard.getFlightBoard().getMygame().getEventBus().wrongInput(shipBoard.getFlightBoard().getMygame().getPlayer(playerID));
                } else {
                    shipBoard.getFlightBoard().getMygame().getEventBus().ChooseSubship(player.getUsername(), subShips, index, 1);
                    shipBoard.Gonextstate(subShips, index, playerID, 1);
                    activate = 0;
                }
            } else {
                System.out.println("wrong player");
                shipBoard.getFlightBoard().getMygame().getEventBus().wrongPlayer(shipBoard.getFlightBoard().getMygame().getPlayer(playerID));
            }
        }
    }
}
