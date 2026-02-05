package it.polimi.ingsw.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.ArrayList;
import java.util.List;

public class Engine extends SpaceShipTile {
    /**
     * Constructs an Engine object with the specified parameters.
     *
     * @param id the unique identifier for the engine
     * @param connectorList a list of connectors that define the engine's connections with other components
     * @param types the specific type of the spaceship tile for this engine
     */
    @JsonCreator
    public Engine(
            @JsonProperty("id") int id,
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("types") SSTTypes types
    ) {
        super(id, connectorList, types);
    }

    /**
     * Checks the correct positioning of a tile in a ship's structure at the specified row and column.
     * It validates if the positioning is valid and handles scenarios where correction is needed,
     * potentially removing invalid tiles.
     *
     * @param row the row of the tile to check
     * @param column the column of the tile to check
     * @return an integer representing the result of the check:
     *         -1 if there are conflicting tile positions to resolve,
     *          1 if a correction was made,
     *          otherwise, it may depend on other internal conditions being evaluated within the method.
     * @throws Exception if any error occurs during the checking or adjustment of tile positioning
     */
    @Override
    public int CheckCorrectPositioning(int row, int column) throws Exception {
        int numTile = this.getAttachedShip().getNumTile();
        int wrongrow, wrongcolumn;
        SpaceShipTile tile, tile1, WrongTile = null;
        // boolean isBuilding = getAttachedShip().isBuilding();
        if (getAttachedShip().getchoosesubship()) {
            return -1;
        }
        if (this.getRotation() != 0) {
            this.getAttachedShip().removeTile(row, column, true);
            this.getAttachedShip().setModified(true);
            return 1;
        }

        if ((row - 1) >= 0 && !this.getAttachedShip().getShipMatrix()[row - 1][column].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = this.getAttachedShip().getShipMatrix()[row - 1][column];
            tile1 = this.getAttachedShip().getShipMatrix()[row][column];
            if (this.getAttachedShip().checkConnectorsPosition(tile1.getConnector(0), tile.getConnector(2))) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row - 1);
                    wrongTiles.add(column);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    System.out.println("\nWhat Tile do you want to remove " + this.getAttachedShip().getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row - 1) + " column: " + (column) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));
                    getAttachedShip().getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, this.getAttachedShip().getMyPlayer());
                    return -1;
                }
                if (numTile == 0) {
                    WrongTile = tile;
                } else if (numTile == 1) {
                    WrongTile = tile1;
                }
                if (WrongTile == tile) {
                    wrongrow = row - 1;
                } else {
                    wrongrow = row;
                }
                wrongcolumn = column;
                this.getAttachedShip().removeTile(wrongrow, wrongcolumn, true);
                this.getAttachedShip().setModified(true);
                this.getAttachedShip().setNumTile(-1);
                return 1;
                //qui era return 1
            }
        }
        if ((column - 1) >= 0 && !this.getAttachedShip().getShipMatrix()[row][column - 1].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = this.getAttachedShip().getShipMatrix()[row][column - 1];
            tile1 = this.getAttachedShip().getShipMatrix()[row][column];
            if (this.getAttachedShip().checkConnectorsPosition(tile1.getConnector(3), tile.getConnector(1))) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row);
                    wrongTiles.add(column - 1);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    getAttachedShip().getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, this.getAttachedShip().getMyPlayer());
                    System.out.println("\nWhat Tile do you want to remove " + this.getAttachedShip().getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row) + " column: " + (column - 1) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));
                    return -1;
                }
                if (numTile == 0) {
                    WrongTile = tile;
                } else if (numTile == 1) {
                    WrongTile = tile1;
                }
                wrongrow = row;
                if (WrongTile == tile) {
                    wrongcolumn = column - 1;
                } else {
                    wrongcolumn = column;
                }
                this.getAttachedShip().removeTile(wrongrow, wrongcolumn, true);
                this.getAttachedShip().setModified(true);
                this.getAttachedShip().setNumTile(-1);
                return 1;
                //qui era return 1
            }
        }
        if ((column + 1) < 7 && !this.getAttachedShip().getShipMatrix()[row][column + 1].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = this.getAttachedShip().getShipMatrix()[row][column + 1];
            tile1 = this.getAttachedShip().getShipMatrix()[row][column];
            if (this.getAttachedShip().checkConnectorsPosition(tile1.getConnector(1), tile.getConnector(3))) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row);
                    wrongTiles.add(column + 1);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    getAttachedShip().getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, this.getAttachedShip().getMyPlayer());
                    System.out.println("\nWhat Tile do you want to remove " + this.getAttachedShip().getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row) + " column: " + (column + 1) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));

                    return -1;
                }
                if (numTile == 0) {
                    WrongTile = tile;
                } else if (numTile == 1) {
                    WrongTile = tile1;
                }
                wrongrow = row;
                if (WrongTile == tile) {
                    wrongcolumn = column + 1;
                } else {
                    wrongcolumn = column;
                }
                this.getAttachedShip().removeTile(wrongrow, wrongcolumn, true);
                this.getAttachedShip().setModified(true);
                this.getAttachedShip().setNumTile(-1);
                return 1;
                //qui era return 1
            }
        }
        if ((row + 1) < 5 && !this.getAttachedShip().getShipMatrix()[row + 1][column].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = this.getAttachedShip().getShipMatrix()[row + 1][column];
            tile1 = this.getAttachedShip().getShipMatrix()[row][column];
            if (numTile == -1) {
                ArrayList<Integer> wrongTiles = new ArrayList<>();
                wrongTiles.add(row + 1);
                wrongTiles.add(column);
                wrongTiles.add(row);
                wrongTiles.add(column);
                getAttachedShip().getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, this.getAttachedShip().getMyPlayer());
                System.out.println("\nWhat Tile do you want to remove " + this.getAttachedShip().getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row + 1) + " column: " + (column) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));

                return -1;
            }
            if (numTile == 0) {
                WrongTile = tile;
            } else if (numTile == 1) {
                WrongTile = tile1;
            }
            if (WrongTile == tile) {
                wrongrow = row + 1;
            } else {
                wrongrow = row;
            }
            wrongcolumn = column;
            this.getAttachedShip().removeTile(wrongrow, wrongcolumn, true);
            this.getAttachedShip().setModified(true);
            this.getAttachedShip().setNumTile(-1);
            return 1;
            //qui era return 1

        }
//        if(!isBuilding){
//            return 1;
//        }

        if ((row - 1 < 0 ||
                getAttachedShip().getShipMatrix()[row - 1][column] == null ||
                getAttachedShip().getShipMatrix()[row - 1][column].getConnector(2).equals(Type_side_connector.SMOOTH_SIDE))) {

            if ((column - 1 < 0 ||
                    getAttachedShip().getShipMatrix()[row][column - 1] == null ||
                    getAttachedShip().getShipMatrix()[row][column - 1].getConnector(1).equals(Type_side_connector.SMOOTH_SIDE))) {

                if ((row + 1 >= 5 ||
                        getAttachedShip().getShipMatrix()[row + 1][column] == null ||
                        getAttachedShip().getShipMatrix()[row + 1][column].getConnector(0).equals(Type_side_connector.SMOOTH_SIDE))) {

                    if ((column + 1 >= 7 ||
                            getAttachedShip().getShipMatrix()[row][column + 1] == null ||
                            getAttachedShip().getShipMatrix()[row][column + 1].getConnector(3).equals(Type_side_connector.SMOOTH_SIDE))) {
                        if (getAttachedShip().getShipBoardIterable().getTilesOfType(SSTTypes.Tile_NonAccesiblePlace).size() == 34) {
                            return 1;
                        }
                        getAttachedShip().removeTile(row, column, true);
                        getAttachedShip().setModified(true);

                        return 1;
                    }
                }
            }
        }
        return 1;
    }


}

