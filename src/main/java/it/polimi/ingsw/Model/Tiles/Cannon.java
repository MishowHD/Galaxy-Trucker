package it.polimi.ingsw.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.ArrayList;
import java.util.List;

public class Cannon extends SpaceShipTile {
    /**
     * Constructor for the Cannon class, defining the properties of the cannon tile
     * on the spaceship. Inherits from the SpaceShipTile class.
     *
     * @param connectorList a list of Type_side_connector enums representing the types of connectors on the sides of the tile.
     * @param types an SSTTypes enumeration specifying the type of the tile, e.g., Tile_Cannon, Tile_Double_Cannon, etc.
     * @param id an integer identifying the unique ID of the tile component.
     */
    @JsonCreator
    public Cannon(
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("types") SSTTypes types,
            @JsonProperty("id") int id

    ) {
        super(id, connectorList, types);
    }

    /**
     * This method verifies the correct positioning of a tile in a specific row and column within a ship's matrix.
     * It checks whether the current tile's position aligns properly with surrounding tiles based on the ship's rotation
     * and established tile connection rules. If a conflict is identified, the method may prompt the user for further
     * actions or modify the state of the ship and its tiles.
     *
     * @param row the row index of the tile to be positioned.
     * @param column the column index of the tile to be positioned.
     * @return an integer indicating the result of the positioning check.
     *         Returns -1 if positioning is incorrect and user action is prompted.
     *         Returns 1 if a correction was made and the state was successfully updated.
     * @throws Exception if an unexpected condition occurs during the positioning check.
     */
    @Override
    public int CheckCorrectPositioning(int row, int column) throws Exception {
        int numTile = getAttachedShip().getNumTile();
        int wrongrow, wrongcolumn;
        SpaceShipTile tile, tile1, WrongTile = null;
        if(getAttachedShip().getchoosesubship()){
            return -1;
        }
        // boolean isBuilding = getAttachedShip().isBuilding();


        if ((row - 1) >= 0 && !this.getAttachedShip().getShipMatrix()[row - 1][column].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = this.getAttachedShip().getShipMatrix()[row - 1][column];
            tile1 = this.getAttachedShip().getShipMatrix()[row][column];
            if (this.getRotation() == 0) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row - 1);
                    wrongTiles.add(column);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    getAttachedShip().getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, this.getAttachedShip().getMyPlayer());
                    System.out.println("\nWhat Tile do you want to remove " + this.getAttachedShip().getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row - 1) + " column: " + (column) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));
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
                //serve un eventbus per refreshare
                return 1;
                //qui era return 1

            } else if (this.getAttachedShip().checkConnectorsPosition(tile1.getConnector(0), tile.getConnector(2))) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row - 1);
                    wrongTiles.add(column);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    getAttachedShip().getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, this.getAttachedShip().getMyPlayer());
                    System.out.println("\nWhat Tile do you want to remove " + this.getAttachedShip().getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row - 1) + " column: " + (column) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));
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
            if (this.getRotation() == 270) {
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

            } else if (this.getAttachedShip().checkConnectorsPosition(tile1.getConnector(3), tile.getConnector(1))) {
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
            if (this.getRotation() == 90) {
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

            } else if (this.getAttachedShip().checkConnectorsPosition(tile1.getConnector(1), tile.getConnector(3))) {
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
            if (this.getRotation() == 180) {
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

            } else if (this.getAttachedShip().checkConnectorsPosition(tile1.getConnector(2), tile.getConnector(0))) {
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

