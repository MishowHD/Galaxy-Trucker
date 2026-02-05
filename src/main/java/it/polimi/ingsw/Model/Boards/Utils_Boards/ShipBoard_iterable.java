package it.polimi.ingsw.Model.Boards.Utils_Boards;

import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.io.Serializable;
import java.util.*;

/**
 * The type Ship board iterable.
 */
public class ShipBoard_iterable implements Iterable<SpaceShipTile>, Serializable {
    private final ShipBoard shipBoard;

    /**
     * Instantiates a new Ship board iterable.
     *
     * @param shipBoard the ship board
     */
    public ShipBoard_iterable(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    @Override
    public Iterator<SpaceShipTile> iterator() {
        return new ShipBoardIterator();
    }

    private class ShipBoardIterator implements Iterator<SpaceShipTile> {
        private int row = 0, col = 0;

        @Override
        public boolean hasNext() {
            return row < shipBoard.getShipMatrix().length && col < shipBoard.getShipMatrix()[row].length;
        }


        public SpaceShipTile next() {
            if (!hasNext()) {
                System.out.println("No element found");
                return null;
            }
            SpaceShipTile value = shipBoard.getShipMatrix()[row][col++];
            if (col >= shipBoard.getShipMatrix()[row].length) {
                row++;
                col = 0;
            }
            return value;
        }

    }

    /**
     * Contains boolean.
     *
     * @param target the target
     * @return the boolean
     */
    public boolean contains(SpaceShipTile target) {
        for (SpaceShipTile tile : this) {
            if (tile.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Alltiles array list.
     *
     * @return the array list
     */
    public ArrayList<SpaceShipTile> Alltiles() {
        ArrayList<SpaceShipTile> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (shipBoard.getShipMatrix()[i][j] != null && shipBoard.getShipMatrix()[i][j].getType() != SSTTypes.Tile_NonAccesiblePlace) {
                    result.add(shipBoard.getShipMatrix()[i][j]);

                }
            }
        }
        return result;
    }

    /**
     * Gets tiles of type.
     *
     * @param tipo the tipo
     * @return the tiles of type
     */
    public ArrayList<SpaceShipTile> getTilesOfType(SSTTypes tipo) {
        ArrayList<SpaceShipTile> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (shipBoard.getShipMatrix()[i][j] != null && shipBoard.getShipMatrix()[i][j].getType() == tipo) {
                    result.add(shipBoard.getShipMatrix()[i][j]);

                }
            }
        }
        return result;
    }

    /**
     * Gets tile position.
     *
     * @param tile the tile
     * @return the tile position
     * @throws RuntimeException the runtime exception
     */
    public static ArrayList<Integer> getTilePosition(SpaceShipTile tile) throws RuntimeException {//returns a 2 integers array of x and y pos of a tile


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (tile != null && tile.getAttachedShip().getShipMatrix()[i][j] != null) {
                    if (Objects.equals(tile.getAttachedShip().getShipMatrix()[i][j], tile)) {
                        ArrayList<Integer> xy = new ArrayList<>();
                        xy.add(i);
                        xy.add(j);
                        return xy;
                    }
                }
            }
        }
        throw new RuntimeException("exception");
    }

    /**
     * Gets tile eff capacity.
     *
     * @param tile the tile
     * @return the tile eff capacity
     */
    public int getTileEffCapacity(SpaceShipTile tile) {//returns an array of x,y arrays
        int EffCapacity = 0;
        switch (tile.getType()) {
            case SSTTypes.Tile_BatteryComponent -> EffCapacity += tile.getNumCharges();
            case Tile_CargoHold, Tile_SpecialCargoHold -> EffCapacity += tile.getEffectivePresentGoods().size();
            case Tile_Cabin -> {
                if (tile.getIsThereAlien()) EffCapacity += 1;
                else EffCapacity += tile.getNumPassengers();
            }
        }
        return EffCapacity;
    }
}
