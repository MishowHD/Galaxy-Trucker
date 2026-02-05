package it.polimi.ingsw.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.io.Serializable;
import java.util.*;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "jsontype"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpaceShipTile.class, name = "SpaceShipTile"),
        @JsonSubTypes.Type(value = Engine.class, name = "Engine"),
        @JsonSubTypes.Type(value = Cannon.class, name = "Cannon"),
        @JsonSubTypes.Type(value = AlienLifeSupport.class, name = "AlienLifeSupport"),
        @JsonSubTypes.Type(value = SpecialStorage.class, name = "SpecialStorage"),
        @JsonSubTypes.Type(value = Storage.class, name = "Storage"),
        @JsonSubTypes.Type(value = Tile_DoublePowerCenter.class, name = "Tile_DoublePowerCenter"),
        @JsonSubTypes.Type(value = Tile_HousingUnit.class, name = "Tile_HousingUnit"),
        @JsonSubTypes.Type(value = Tile_TriplePowerCenter.class, name = "Tile_TriplePowerCenter"),})


public class SpaceShipTile implements Serializable {
    /**
     * Represents the unique identifier for a SpaceShipTile instance. This ID is used to
     * differentiate and identify individual tiles within a spaceship configuration or game.
     * Once set, the ID cannot be changed as it is final.
     */
    private final int id;
    /**
     * Represents a list of connectors associated with a spaceship tile.
     * Each connector in the list defines the type of connection available on specific sides of the tile.
     * The connectors can include single, double, universal, or smooth side types.
     * This list is immutable and initialized during the creation of the SpaceShipTile object.
     */
    private final List<Type_side_connector> connectorList;
    /**
     * Represents the current rotation state of the spaceship tile.
     * The value of this field determines how the tile is oriented.
     * Typically, the rotation is expressed in degrees (e.g., 0, 90, 180, 270).
     */
    private int rotation;
    /**
     * A boolean flag indicating whether the spaceship tile has been flipped.
     * This variable is used to track the flipped state of the tile, which can
     * affect its orientation and interactions within the game.
     */
    private boolean isFlipped;
    /**
     * Represents the specific type of the SpaceShipTile.
     * This is defined by an enumeration {@link SSTTypes}, which includes various tile types such as cabins, engines,
     * cannons, battery components, shield generators, cargo holds, structural modules, alien life support systems,
     * or non-accessible areas within the spaceship structure.
     *
     * The type information is immutable and influences the tile's properties, functionality, and interactions
     * within the spaceship configuration.
     */
    private final SSTTypes types;
    /**
     * Represents the ship currently attached to the SpaceShipTile.
     * This variable links a specific ShipBoard instance to the tile
     * and is used to manage gameplay mechanics involving the associated ship.
     */
    private ShipBoard attachedShip;


    /**
     * Constructor for the SpaceShipTile class.
     *
     * @param id             The unique identifier for the spaceship tile.
     * @param connectorList  A list of connectors associated with the tile, defined by the Type_side_connector enumeration.
     * @param types          The type of the spaceship tile, based on the SSTTypes enumeration.
     */
    @JsonCreator
    public SpaceShipTile(
            @JsonProperty("id") int id,
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("types") SSTTypes types
    ) {
        this.id = id;
        this.connectorList = connectorList;
        this.rotation = 0;
        this.isFlipped = false;
        this.attachedShip = null;
        this.types = types;

    }

    /**
     * Sets the flipped state of the SpaceShipTile.
     *
     * @param isFlipped the desired flipped state; true if the tile is flipped, false otherwise
     */
    public void setFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    /**
     * Retrieves the connector of the specified index from the connector list.
     *
     * @param connectorIndex the index of the connector to retrieve from the list
     * @return the Type_side_connector at the specified index in the connector list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Type_side_connector getConnector(int connectorIndex) {
        return connectorList.get(connectorIndex);
    }

    /**
     * Retrieves the current rotation value of the SpaceShipTile.
     *
     * @return the rotation value as an integer.
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Checks whether the tile is currently flipped.
     *
     * @return true if the tile is flipped, false otherwise
     */
    public boolean isFlipped() {
        return isFlipped;
    }

    /**
     * Sets the attached ship associated with this SpaceShipTile.
     *
     * @param attachedShip the ShipBoard to be attached to this SpaceShipTile
     */
    public void setAttachedShip(ShipBoard attachedShip) {
        this.attachedShip = attachedShip;
    }

    /**
     * Retrieves the ShipBoard instance currently attached to this SpaceShipTile.
     *
     * @return the attached ShipBoard, or null if no ShipBoard is attached
     */
    public ShipBoard getAttachedShip() {
        return attachedShip;
    }

    /**
     * Rotates the SpaceShipTile 90 degrees to the right.
     *
     * If the tile is flipped (represented by the isFlipped field being true),
     * the rotation field is updated to reflect the new rotation by adding 90
     * degrees to its current value and using modulo 360 to ensure it stays
     * within valid bounds (0-359 degrees).
     *
     * Independently of the isFlipped state, the method rotates the order of
     * elements in the connectorList field, shifting them one position forward.
     * This corresponds to the physical rotation of connectors around the tile.
     */
    public void Rotate90right() {
        if (isFlipped) {
            rotation = (rotation + 90) % 360;
        }
        Collections.rotate(connectorList, 1);
    }

    /**
     * Toggles the flip state of the SpaceShipTile object.
     * If the tile is currently flipped, this method will unflip it, and vice versa.
     */
    public void Flip() {
        isFlipped = !isFlipped;
    }

    /**
     * Sets the flipped state of the SpaceShipTile.
     *
     * @param isFlipped a boolean indicating whether the tile should be flipped (true if flipped, false otherwise)
     */
    public void SetFlip(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    /**
     * Retrieves the type of the spaceship tile.
     *
     * @return the type of the spaceship tile represented by an SSTTypes enum value
     */
    public SSTTypes getType() {
        return types;
    }

    /**
     * Retrieves the battery capacity of the tile.
     *
     * @return the battery capacity of the tile as an integer
     * @throws RuntimeException if battery capacity is not available
     */
    public int getTileBattCapacity() throws RuntimeException {
        throw new RuntimeException("There are no battery capacity");
    }

    /**
     * Checks the positioning of a tile at the specified row and column within the ship's matrix.
     * Determines if the tile placement respects all constraints and connectivity rules.
     * If the placement is invalid, appropriate corrective actions might be suggested or taken.
     *
     * @param row the row index of the tile being checked
     * @param column the column index of the tile being checked
     * @return an integer status code indicating the result of the check:
     *       1 - Positioning is correct and valid.
     *      -1 - Positioning is incorrect, requires corrective action or selection of tiles to remove.
     * @throws Exception if there is an error during the checking or processing of the tile's positioning
     */
    public int CheckCorrectPositioning(int row, int column) throws Exception {
        if (attachedShip.getShipMatrix()[row][column].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            return 1;
        }
        if(getAttachedShip().getchoosesubship()){
            return -1;
        }
        int numTile = attachedShip.getNumTile();
        //boolean isBuilding = attachedShip.isBuilding();

        int wrongrow, wrongcolumn;
        SpaceShipTile tile, tile1, WrongTile = null;
        if ((row - 1) >= 0 && !attachedShip.getShipMatrix()[row - 1][column].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = attachedShip.getShipMatrix()[row - 1][column];
            tile1 = attachedShip.getShipMatrix()[row][column];
            if (attachedShip.checkConnectorsPosition(tile1.getConnector(0), tile.getConnector(2))) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row - 1);
                    wrongTiles.add(column);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    attachedShip.getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, attachedShip.getMyPlayer());
                    System.out.println("\nWhat Tile do you want to remove " + attachedShip.getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row - 1) + " column: " + (column) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));
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
                attachedShip.removeTile(wrongrow, wrongcolumn, true);
                attachedShip.setModified(true);
                attachedShip.setNumTile(-1);
                return 1;
                //qui era return 1
            }
        }
        if ((column - 1) >= 0 && !attachedShip.getShipMatrix()[row][column - 1].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = attachedShip.getShipMatrix()[row][column - 1];
            tile1 = attachedShip.getShipMatrix()[row][column];
            if (attachedShip.checkConnectorsPosition(tile1.getConnector(3), tile.getConnector(1))) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row);
                    wrongTiles.add(column - 1);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    attachedShip.getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, attachedShip.getMyPlayer());
                    System.out.println("\nWhat Tile do you want to remove " + attachedShip.getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row) + " column: " + (column - 1) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));
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
                attachedShip.removeTile(wrongrow, wrongcolumn, true);
                attachedShip.setModified(true);
                attachedShip.setNumTile(-1);
                return 1;
                //qui era return 1
            }
        }
        if ((column + 1) < 7 && !attachedShip.getShipMatrix()[row][column + 1].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = attachedShip.getShipMatrix()[row][column + 1];
            tile1 = attachedShip.getShipMatrix()[row][column];
            if (attachedShip.checkConnectorsPosition(tile1.getConnector(1), tile.getConnector(3))) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row);
                    wrongTiles.add(column + 1);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    attachedShip.getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, attachedShip.getMyPlayer());
                    System.out.println("\nWhat Tile do you want to remove " + attachedShip.getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row) + " column: " + (column + 1) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));

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
                attachedShip.removeTile(wrongrow, wrongcolumn, true);
                attachedShip.setModified(true);
                attachedShip.setNumTile(-1);
                return 1;
                //qui era return 1
            }
        }
        if ((row + 1) < 5 && !attachedShip.getShipMatrix()[row + 1][column].getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            tile = attachedShip.getShipMatrix()[row + 1][column];
            tile1 = attachedShip.getShipMatrix()[row][column];
            if (attachedShip.checkConnectorsPosition(tile1.getConnector(2), tile.getConnector(0))) {
                if (numTile == -1) {
                    ArrayList<Integer> wrongTiles = new ArrayList<>();
                    wrongTiles.add(row + 1);
                    wrongTiles.add(column);
                    wrongTiles.add(row);
                    wrongTiles.add(column);
                    attachedShip.getFlightBoard().getMygame().getEventBus().wrongTiles(wrongTiles, attachedShip.getMyPlayer());
                    System.out.println("\nWhat Tile do you want to remove " + attachedShip.getMyPlayer().getUsername() + "? Choose one of the following tiles: \n 0 : " + tile.getType() + " row: " + (row + 1) + " column: " + (column) + " \n 1 : " + tile1.getType() + " row: " + (row) + " column: " + (column));

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
                attachedShip.removeTile(wrongrow, wrongcolumn, true);
                attachedShip.setModified(true);
                attachedShip.setNumTile(-1);
                return 1;
                //qui era return 1
            }
        }
//        if(!isBuilding){
//            return 1;
//        }

        if (((row - 1 < 0 ||
                getAttachedShip().getShipMatrix()[row - 1][column] == null ||
                getAttachedShip().getShipMatrix()[row - 1][column].getConnector(2).equals(Type_side_connector.SMOOTH_SIDE)))) {

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

    /**
     * Retrieves the number of passengers currently associated with the spaceship tile.
     *
     * @return the number of passengers present in the tile
     * @throws RuntimeException if the method is not supported or implemented
     */
    public int getNumPassengers() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Determines whether there is an alien present on the current tile.
     *
     * @return true if an alien is present on the tile, false otherwise
     * @throws RuntimeException if the operation is not supported
     */
    public boolean getIsThereAlien() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Adds a passenger to the SpaceShipTile.
     *
     * This method attempts to increment the passenger count for the SpaceShipTile
     * instance. If the operation cannot be completed, an exception is thrown.
     *
     * @throws Exception if adding a passenger is not supported or cannot be performed.
     */
    public void addPassenger() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Removes a passenger from the spaceship tile.
     *
     * @param removable the number of passengers to be removed
     * @throws Exception if the operation is not supported or if an error occurs during removal
     */
    public void removePassenger(int removable) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Adds an alien to the designated position on the spaceship tile.
     *
     * @param wantalien Determines if an alien should be added.
     *                  True to add an alien, false to skip the operation.
     * @param color Specifies the color of the alien to be added.
     *              Possible values are defined in the AlienColor enum.
     * @param row The row index where the alien should be added.
     *            Must be a valid row within the spaceship tile.
     * @param column The column index where the alien should be added.
     *               Must be a valid column within the spaceship tile.
     * @throws Exception If the operation fails due to constraints or invalid arguments.
     */
    public void addAlien(boolean wantalien, AlienColor color, int row, int column) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Removes an alien from the spaceship tile. The implementation of this method is not supported yet.
     *
     * @throws Exception if the operation cannot be performed or is unsupported.
     */
    public void removeAlien() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the number of charges currently stored in the spaceship tile.
     *
     * @return the number of charges as an integer
     * @throws RuntimeException if the operation is not supported or cannot be performed
     */
    public int getNumCharges() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Adds a charge to the associated spaceship tile. This method is intended to
     * increment the charge count or energy level of a spaceship tile.
     *
     * @throws RuntimeException if the operation is not supported or if the
     *                          implementation logic has not been provided.
     */
    public void addCharge() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Removes a specified number of charges from the current object.
     *
     * @param charges the number of charges to remove
     * @throws RuntimeException if the operation is not supported or cannot be performed
     */
    public void removeCharge(int charges) throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the list of effective and present goods associated with this instance.
     * This method identifies goods that are currently available and valid for consideration.
     *
     * @return a list of {@code Goods} objects representing the effective and present goods
     * @throws RuntimeException if the operation is not supported or cannot be executed
     */
    public List<Goods> getEffectivePresentGoods() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the capacity of the SpaceShipTile.
     *
     * @return the capacity as an integer.
     * @throws RuntimeException if the method is not supported or another runtime error occurs.
     */
    public int getCapacity() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Adds a goods item to the spaceship tile.
     *
     * @param good the goods item to be added
     * @throws RuntimeException if the operation is not supported
     */
    public void addGoods(Goods good) throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the color of the alien present on the tile.
     *
     * @return the color of the alien as an {@link AlienColor} enumeration.
     * @throws RuntimeException if the operation is not supported or if the color cannot be retrieved.
     */
    public AlienColor getColor() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Recharges all chargeable components of the spaceship tile to their full capacity.
     *
     * This method is intended to ensure that all charges for the spaceship tile
     * or its attached components are fully replenished. It does not currently
     * contain a specific implementation and will throw an exception when invoked.
     *
     * @throws RuntimeException if the method is invoked, as it is not supported yet.
     */
    public void RechargeAll() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Sets the charge value for the SpaceShipTile.
     *
     * @param i the charge value to be set
     * @throws RuntimeException if the method is not supported
     */
    public void setCharge(int i) throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the unique identifier associated with this SpaceShipTile instance.
     *
     * @return the unique identifier of the SpaceShipTile as an integer.
     */
    public int getID() {
        return id;
    }

    /**
     * Retrieves the color of the alien present on the spaceship tile.
     *
     * @return the {@link AlienColor} of the alien on this tile
     * @throws RuntimeException if the operation is not supported
     */
    public AlienColor getAlienColor() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Sets the color of the alien associated with this tile.
     *
     * @param alienColor the new color of the alien; must be a valid value from the AlienColor enumeration
     * @throws RuntimeException if the operation is not supported or cannot be completed
     */
    public void setAlienColor(AlienColor alienColor) throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Sets the presence of an alien on this tile.
     *
     * @param b a boolean value where {@code true} indicates the presence of an alien
     *          and {@code false} indicates the absence of an alien.
     */
    public void setIsThereAlien(boolean b) {
        throw new RuntimeException("Not supported yet.");
    }
}
