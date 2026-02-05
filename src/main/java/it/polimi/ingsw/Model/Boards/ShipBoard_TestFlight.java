package it.polimi.ingsw.Model.Boards;

import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.RemoveTile.RemoveTileStatePreChoose;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.ArrayList;

public class ShipBoard_TestFlight extends ShipBoard {

    /**
     * Constructs a new instance of ShipBoard_TestFlight.
     *
     * @param flightBoard the flight board associated with this ship board test flight
     * @param p the player associated with this ship board test flight
     */
    public ShipBoard_TestFlight(FlightBoard flightBoard, Player p) {
        super(flightBoard, p);
        setNonAccessibleTiles();
    }

    /**
     * Determines if the current entity is a violet alien.
     *
     * @return true if the entity is identified as a violet alien, false otherwise.
     */
    @Override
    public boolean isVioletAlien() {
        return false;
    }

    /**
     * Determines if the current alien is of the brown type.
     * This method is overridden to provide a specific implementation
     * for checking if an alien is classified as brown.
     *
     * @return true if the alien is classified as brown, false otherwise.
     */
    @Override
    public boolean isBrownAlien() {
        return false;
    }

    /**
     * Sets specific tiles on the game board as non-accessible.
     *
     * This method defines a configuration of tiles that are considered
     * non-accessible on the Spaceship board. It creates a {@code SpaceShipTile}
     * with specific properties (e.g., connectors set to {@code SMOOTH_SIDE}, and
     * tile type set to {@code Tile_NonAccesiblePlace}), and inserts these tiles
     * at predefined positions on the board. These positions correspond to a
     * specific layout and are intended to represent regions of the board that
     * players cannot interact with.
     *
     * @throws RuntimeException if an error occurs during the insertion of tiles.
     */
    @Override
    public void setNonAccessibleTiles() throws RuntimeException {

        ArrayList<Type_side_connector> c1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            c1.add(Type_side_connector.SMOOTH_SIDE);
        }
        SpaceShipTile TNotAccessible = new SpaceShipTile(-1, c1, SSTTypes.Tile_NonAccesiblePlace);
        int[][] positions = {
                {0, 0}, {0, 1}, {0, 2}, {0, 4}, {0, 5}, {0, 6},
                {1, 0}, {1, 1}, {1, 5}, {1, 6},
                {2, 0}, {2, 6},
                {3, 0}, {3, 6},
                {4, 0}, {4, 3}, {4, 6}
        };

        for (int[] pos : positions) {
            this.insertTile(TNotAccessible, pos[0], pos[1]);
        }
    }

    /**
     * Adds an alien of the specified color to the ship board.
     *
     * @param alien_colour the color of the alien to be added, represented by the AlienColor enum.
     *                     It can be either VIOLET or BROWN.
     * @throws RuntimeException if the implementation is not yet defined or other runtime issues occur.
     */
    @Override
    public void addAlien(AlienColor alien_colour) throws RuntimeException {
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Removes the tiles that are in a waiting state for the end of building.
     * This method is expected to handle any logic related to clearing or managing
     * tiles that are marked as incomplete or pending during the construction phase.
     *
     * @throws RuntimeException if the method is not implemented or if it encounters
     * any errors during execution.
     */
    @Override
    public void removeWaitTilesForEndBuilding() throws RuntimeException {
        throw new RuntimeException("Not implemented yet");

    }

    /**
     * Removes a tile from the ship grid at the specified row and column.
     * It performs various operations based on the type of tile removed
     * and updates the game state accordingly.
     *
     * @param row the row index of the tile to remove
     * @param column the column index of the tile to remove
     * @param fromMistake a flag indicating whether the removal occurred
     *                    due to a player mistake or another game event
     * @throws Exception if an error occurs during the tile removal process
     */
    @Override
    public void removeTile(int row, int column, boolean fromMistake) throws Exception {
        this.state = new RemoveTileStatePreChoose(this);
        this.state.StateMain(this, row, column, fromMistake);
    }

    /**
     * Calculates the total firepower of the spaceship based on the positions of cannons and batteries,
     * and updates battery charges accordingly.
     *
     * @param cannonPos a list of positions of cannons on the spaceship, where each position is represented as
     *                  a list of integers [row, col].
     * @param batteriesPos a list of positions of batteries on the spaceship, where each position is represented
     *                     as a list of integers [row, col, charge required].
     * @param player the player who owns the spaceship, this parameter may influence the calculation in certain contexts.
     * @return the total firepower of the spaceship as a float value.
     * @throws RuntimeException if any error occurs during the calculation.
     */
    @Override
    public float getTotalFirePower(ArrayList<ArrayList<Integer>> cannonPos,
                                   ArrayList<ArrayList<Integer>> batteriesPos,
                                   Player player) throws RuntimeException {

        float totalFirePower = 0;
        SpaceShipTile[][] tiles = this.getShipMatrix();
        if (cannonPos == null || cannonPos.isEmpty()) {
            return this.getBasicFirePower();
        }
        for (ArrayList<Integer> cannon : cannonPos) {
            int row = cannon.get(0);
            int col = cannon.get(1);
            SpaceShipTile tile = tiles[row][col];

            totalFirePower += (tile.getRotation() == 0) ? 2 : 1;
        }
        int batteryNeeded = 0;
        for (ArrayList<Integer> b : batteriesPos) {
            batteryNeeded += b.get(2);
        }
        for (ArrayList<Integer> b : batteriesPos) {
            if (batteryNeeded == 0) break;

            int row = b.get(0);
            int col = b.get(1);
            int requested = b.get(2);

            SpaceShipTile tile = tiles[row][col];

            int toRemove = Math.min(requested, batteryNeeded);
            tile.removeCharge(toRemove);
            batteryNeeded -= toRemove;
        }
        for (SpaceShipTile tile : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Cannon)) {
            totalFirePower += (tile.getRotation() == 0) ? 1.0F : 0.5F;
        }

        return totalFirePower;
    }

    /**
     * Calculates the total motion power of the spaceship based on the positions of engine tiles
     * and the available charges in battery tiles. The method also adjusts the charges in battery tiles
     * as necessary and accounts for additional motion power from other specific tile types.
     *
     * @param enginePos a list of lists, where each inner list represents the coordinates of an engine tile
     * @param batteriesPos a list of lists, where each inner list represents the coordinates and charges of a battery tile
     * @param player the player associated with this calculation
     * @return the total motion power of the spaceship
     * @throws RuntimeException if the calculation encounters an unexpected error or invalid inputs
     */
    @Override
    public int getTotalMotionPower(ArrayList<ArrayList<Integer>> enginePos,
                                   ArrayList<ArrayList<Integer>> batteriesPos,
                                   Player player) throws RuntimeException {

        int totalMotionPower = 0;
        SpaceShipTile[][] tiles = this.getShipMatrix();
        if (enginePos == null || enginePos.isEmpty()) {
            return this.getBasicMotionPower();
        }
        for (ArrayList<Integer> _ : enginePos) {
            totalMotionPower += 2;
        }
        int batteryNeeded = 0;
        for (ArrayList<Integer> b : batteriesPos) {
            batteryNeeded += b.get(2);
        }
        for (ArrayList<Integer> b : batteriesPos) {
            if (batteryNeeded == 0) break;

            int row = b.get(0);
            int col = b.get(1);
            int requested = b.get(2);

            SpaceShipTile tile = tiles[row][col];

            int toRemove = Math.min(requested, batteryNeeded);
            tile.removeCharge(toRemove);
            batteryNeeded -= toRemove;
        }
        for (SpaceShipTile _ : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Engine)) {
            totalMotionPower++;
        }

        return totalMotionPower;
    }

}