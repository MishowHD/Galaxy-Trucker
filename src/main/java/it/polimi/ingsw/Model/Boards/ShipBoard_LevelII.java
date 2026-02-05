package it.polimi.ingsw.Model.Boards;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.RemoveTile.RemoveTileStatePreChoose;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.ArrayList;

public class ShipBoard_LevelII extends ShipBoard {
    /**
     * Represents a specific tile on the spaceship board used as a waiting area in the level II configuration.
     * This tile is typically utilized to manage the state or position of game elements
     * during gameplay in the level II stage of the spaceship board.
     */
    SpaceShipTile waitTile1;
    /**
     * Represents the second waiting tile on the ShipBoard_LevelII.
     * This tile is used as part of the board setup for managing game mechanics
     * such as the placement of spaceship components or other gameplay elements.
     */
    SpaceShipTile waitTile2;

    /**
     * Retrieves the first waiting tile associated with the ship board.
     *
     * @return the first waiting tile (waitTile1) of type SpaceShipTile
     */
    public SpaceShipTile getWaitTile1() {
        return waitTile1;
    }

    /**
     * Sets the first waiting tile of the shipboard.
     *
     * @param waitTile1 the {@code SpaceShipTile} to be assigned as the first waiting tile
     */
    public void setWaitTile1(SpaceShipTile waitTile1) {
        this.waitTile1 = waitTile1;
    }

    /**
     * Retrieves the second wait tile on the current shipboard level.
     *
     * @return the second wait tile (waitTile2) as a SpaceShipTile instance.
     */
    public SpaceShipTile getWaitTile2() {
        return waitTile2;
    }

    /**
     * Sets the specified SpaceShipTile as the second waiting tile on the ship board.
     *
     * @param waitTile2 the SpaceShipTile to be set as the second waiting tile
     */
    public void setWaitTile2(SpaceShipTile waitTile2) {
        this.waitTile2 = waitTile2;
    }

    /**
     * Constructs a ShipBoard_LevelII object with the specified flight board and player.
     * This constructor initializes the ship board level by setting up the required non-accessible tiles.
     *
     * @param flightBoard the FlightBoard used to initialize this ShipBoard_LevelII
     * @param p the Player associated with this ShipBoard_LevelII
     */
    public ShipBoard_LevelII(FlightBoard flightBoard, Player p) {
        super(flightBoard, p);
        setNonAccessibleTiles();
    }

    /**
     * Determines if the alien present on the board is of the Violet type.
     *
     * @return {@code true} if the alien is Violet, {@code false} otherwise.
     */
    @Override
    public boolean isVioletAlien() {
        return VioletAlien;
    }

    /**
     * Determines if the alien on the ship board is a brown alien.
     *
     * @return true if the alien is a brown alien, false otherwise.
     */
    @Override
    public boolean isBrownAlien() {
        return BrownAlien;
    }

    /**
     * Calculates the total firepower of the spaceship based on the cannon positions,
     * batteries positions, and any additional conditions specific to the player or alien type.
     *
     * @param cannonPos a list of lists containing the positions of cannons represented as
     *                  row and column coordinates.
     * @param batteriesPos a list of lists containing the positions of batteries represented
     *                     as row, column, and available charge respectively.
     * @param player the player whose spaceship firepower needs to be calculated.
     * @return the total firepower calculated as a floating-point value.
     * @throws RuntimeException if any error occurs during the calculation of the firepower.
     */
    @Override
    public float getTotalFirePower(ArrayList<ArrayList<Integer>> cannonPos,
                                   ArrayList<ArrayList<Integer>> batteriesPos,
                                   Player player) throws RuntimeException {
        float totalFirePower = this.getBasicFirePower();
        SpaceShipTile[][] tiles = this.getShipMatrix();
        if (!(cannonPos == null || cannonPos.isEmpty())) {
            for (ArrayList<Integer> cannonPair : cannonPos) {
                int row = cannonPair.get(0);
                int col = cannonPair.get(1);
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
        }
        if (totalFirePower > 0 && this.isVioletAlien()) {
            totalFirePower += 2;
        }
        return totalFirePower;
    }


    /**
     * Calculates the total motion power of the ship based on the provided engine positions, battery positions,
     * and the player's attributes. The motion power is affected by the number of engines and battery charges,
     * and may be modified depending on specific conditions such as alien presence.
     *
     * @param enginePos A list of positions (row and column) for the engines on the ship. Each position represents
     *                  an active engine contributing to the motion power.
     * @param batteriesPos A list of positions (row and column) for the batteries on the ship along with their charges.
     *                     Used to fulfill the energy demands for the motion power if necessary.
     * @param player The player operating the ship. Attributes of the player may influence the motion power calculation.
     * @return The total motion power of the ship as an integer. This value is the sum of basic motion power
     *         and contributions from engines, batteries, and other applicable conditions.
     * @throws RuntimeException If any error occurs during calculation, such as invalid data or unexpected conditions.
     */
    @Override
    public int getTotalMotionPower(ArrayList<ArrayList<Integer>> enginePos,
                                   ArrayList<ArrayList<Integer>> batteriesPos,
                                   Player player) throws RuntimeException {

        int totalMotionPower = this.getBasicMotionPower();
        SpaceShipTile[][] tiles = this.getShipMatrix();
        if (!(enginePos == null || enginePos.isEmpty())) {
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
        }
        if (totalMotionPower > 0 && this.isBrownAlien()) {
            totalMotionPower += 2;
        }
        return totalMotionPower;
    }


    /**
     * Sets the non-accessible tiles on the shipboard.
     * This method creates a special tile marked as non-accessible and places it
     * in predefined positions on the shipboard to represent areas that players cannot access.
     *
     * The non-accessible tile is defined with a unique identifier, specific connectors,
     * and a non-accessible type designation. The tile is then inserted at specified
     * row-column coordinates on the board.
     *
     * @throws RuntimeException if there is an issue while inserting the non-accessible tile.
     */
    @Override
    public void setNonAccessibleTiles() throws RuntimeException {
        ArrayList<Type_side_connector> connectors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            connectors.add(Type_side_connector.SMOOTH_SIDE);
        }

        SpaceShipTile TNotAccessible = new SpaceShipTile(-1, connectors, SSTTypes.Tile_NonAccesiblePlace);
        int[][] positions = {
                {0, 0}, {0, 1}, {0, 3}, {0, 5}, {0, 6},
                {1, 0}, {1, 6},
                {4, 3}
        };

        for (int[] pos : positions) {
            this.insertTile(TNotAccessible, pos[0], pos[1]);
            TNotAccessible.setAttachedShip(this);
        }
    }

    /**
     * Removes a specific tile located at the given row and column on the shipboard.
     * This method initiates a state transition to handle the tile removal process,
     * updates the state of the shipboard, and performs necessary updates depending on
     * the type and status of the tile. Additionally, it ensures proper handling of
     * connected blocks and potential mistakes during the tile removal.
     *
     * @param row the row coordinate of the tile to be removed
     * @param column the column coordinate of the tile to be removed
     * @param fromMistake a flag indicating whether the removal is due to a mistake
     * @throws Exception if an error occurs during the tile removal process
     */
    @Override
    public void removeTile(int row, int column, boolean fromMistake) throws Exception {
        this.state = new RemoveTileStatePreChoose(this);
        this.state.StateMain(this, row, column, fromMistake);
    }

    /**
     * Adds an alien to the ShipBoard_LevelII based on its color.
     * This method updates the state of the board to reflect the presence
     * of the specified alien color.
     *
     * @param alien_colour the color of the alien to be added. Valid values are:
     *                     {@code AlienColor.VIOLET} and {@code AlienColor.BROWN}.
     *                     If {@code AlienColor.VIOLET} is passed, the violet alien state is set to true.
     *                     If {@code AlienColor.BROWN} is passed, the brown alien state is set to true.
     * @throws RuntimeException if the operation fails.
     */
    @Override
    public void addAlien(AlienColor alien_colour) throws RuntimeException {
        if (alien_colour == AlienColor.VIOLET) {
            VioletAlien = true;
        } else if (alien_colour == AlienColor.BROWN) {
            BrownAlien = true;
        }
    }

    /**
     * Removes the waiting tiles associated with the ship at the end of the building process.
     * If the waiting tiles are present, it detaches the associated ship and updates the count
     * of lost pieces on the ship board.
     *
     * Postconditions:
     * - If `waitTile1` is not null, it is set to null, its associated ship is detached,
     *   and the count of lost pieces is incremented by 1.
     * - If `waitTile2` is not null, it is set to null, its associated ship is detached,
     *   and the count of lost pieces is incremented by 1.
     */
    public void removeWaitTilesForEndBuilding() {
        if (waitTile1 != null) {
            waitTile1.setAttachedShip(null);
            waitTile1 = null;
            addLostPieces(1);
        }
        if (waitTile2 != null) {
            waitTile2.setAttachedShip(null);
            waitTile2 = null;
            addLostPieces(1);
        }
    }
}
