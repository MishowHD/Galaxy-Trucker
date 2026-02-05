package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

public abstract class Building_Phase_State extends GameState {

    /**
     * Constructs a new Building_Phase_State object and associates it with the given game instance.
     *
     * @param game the Game instance associated with this state
     */
    public Building_Phase_State(Game game) {
        super(game);

    }

    /**
     * Handles the action of depositing a tile for the player with the given ID.
     * This method checks if the player has a tile in hand and is not blocked.
     * If the conditions are not met, the action is denied; otherwise, the tile
     * is deposited successfully.
     *
     * @param playerID the unique identifier of the player attempting to deposit the tile
     * @throws Exception if an error occurs during the deposit process or interaction with event handling
     */
    @Override
    public void depositTile(int playerID) throws Exception {
        game.getPlayer(playerID);
        if (!game.getPlayer(playerID).hasSomethingInHand() || game.getPlayer(playerID).isBlocked()) {
            game.getEventBus().cannotDeposit(game.getPlayer(playerID));
        } else {
            game.getEventBus().depositThingInHand(game.getPlayer(playerID).getUsername());
            game.getGameSSTIH().remove(game.getPlayer(playerID).getTileInHand());
            game.getPlayer(playerID).depositTilePlayer(game.getPlayer(playerID).getTileInHand());
        }
    }

    /**
     * Executes the main logic for the current game state within the building phase.
     * This method initializes and sets up the game environment for this phase, ensuring that:
     * - All spaceship tiles are set to their initial state.
     * - All players are unblocked.
     * Depending on the current game level, it updates the game's event bus with relevant information.
     *
     * @throws Exception if an error occurs during the execution of the main state logic.
     */
    @Override
    public void StateMain() throws Exception {
        for (SpaceShipTile t : game.getGameTiles()) {
            t.SetFlip(false);
        }//forse da mettere in Iniziali
        for (Player p : game.getPlayers()) {
            p.setBlocked(false);
        }
        //QUi abbiamo già i deck, le tile, la flightboard vuota, i player creati
        if (game.getLevel() == 1)
            game.getEventBus().updateGameCreated(1, game.getGameFlightBoard().getShowableDecks(), game.getPlayers(), game.getHourglassTime(), game.getSurrenderTimer());
        else
            game.getEventBus().updateGameCreated(0, null, game.getPlayers(), game.getHourglassTime(), game.getSurrenderTimer());
    }

    /**
     * Attempts to insert a tile into the game board at a specified position with a specific rotation.
     * The method validates inputs such as tile placement, rotation, and whether the player has
     * the required items to execute the action. Events are dispatched based on success or failure.
     *
     * @param row      The row index where the tile should be inserted. Must be between 0 and 4 inclusive.
     * @param col      The column index where the tile should be inserted. Must be between 0 and 6 inclusive.
     * @param playerID The unique identifier of the player attempting the insertion.
     * @param rotation The rotation in degrees for the tile. Must be a multiple of 90 (e.g., 0, 90, 180, 270).
     *                 Invalid values result in no action and an error event for the player.
     * @throws Exception If any issues arise during tile insertion or internal game state processing.
     */
    @Override
    public void insertTile(int row, int col, int playerID, int rotation) throws Exception { //rotation is 0, 90, 180,270 ecc..
        Player player = game.getPlayer(playerID);
        if (rotation % 90 != 0) {
            game.getEventBus().wrongInput(player);
            return;
        }
        if (((row < 0 || row > 4) || (col < 0) || col > 6)) {
            game.getEventBus().wrongInput(player);
            return;
        }
        if (!player.hasSomethingInHand() || player.isBlocked()) {
            game.getEventBus().cannotInsert(player);
        } else {
            SpaceShipTile tile = player.getTileInHand();
            if (player.InsertTile(tile, row, col)) {
                game.getGameSSTIH().remove(tile);
                game.getGameTiles().remove(tile);
                int rot = (rotation % 360) / 90; //i have either 0, 1, 2 or 3
                for (int i = 0; i < rot; i++) {
                    tile.Rotate90right();
                }
                game.getEventBus().insertTileLMR(tile.getID(), row, col, rot, player.getUsername());
                tile.setAttachedShip(player.getMyShip());
            }
        }
    }

    /**
     * Allows a player to choose a sub-ship from their connected ship components by specifying an index.
     * This method is intended to be called during the building phase, assuming the player has completed
     * any required prior actions and there are multiple sub-ships to choose from.
     *
     * @param index    The index of the sub-ship to choose.
     * @param playerID The unique ID of the player making the selection.
     * @throws Exception If the input is invalid or any unexpected error occurs during execution.
     */
    public void chooseOneSubShip(int index, int playerID) throws Exception {
        //IM shure to have players on fb cause the had to end building to call chooose sub ship
        Player player = game.getGameFlightBoard().getPlayerfromID(playerID);
        //if there are subshipt you can call it
        if (player.getMyShip().findConnectedBlocks().size() > 1) {
            game.getPlayer(playerID).getMyShip().getRemoveTileState().chooseOneSubShip(index, playerID);
            game.getGameState().completeBuildingPhase();
        } else game.getEventBus().wrongInput(player);
    }

    /**
     * Checks if a specific tile is in the hand of another player in the game.
     *
     * @param tile the SpaceShipTile to be checked
     * @param playerID the ID of the player to be excluded from the check
     * @return {@code true} if the tile is found in the hand of a player other than the specified playerID, {@code false} otherwise
     * @throws RuntimeException if an error occurs during the operation
     */
    boolean isTileintheHandofOther(SpaceShipTile tile, int playerID) throws RuntimeException {
        for (Player p : game.getPlayers()) {
            if (p.getPlayerId() != playerID && p.getTileInHand() == tile) {
                return true;
            }
        }
        return false;
    }
}
