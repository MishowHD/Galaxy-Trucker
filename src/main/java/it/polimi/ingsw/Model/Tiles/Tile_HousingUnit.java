package it.polimi.ingsw.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.ArrayList;
import java.util.List;

public class Tile_HousingUnit extends SpaceShipTile {
    /**
     * Represents the number of passengers currently in the housing unit.
     * This field is used to track the total count of passengers residing within the tile.
     */
    private int numPassengers;
    /**
     * A boolean variable that indicates whether there is an alien present in the housing unit.
     * The alien is not considered a passenger and is treated distinctly from any passengers within the unit.
     */
    private boolean isThereAlien; //alien is not counted as passenger
    /**
     * Represents the color of the alien present in a housing unit on a spaceship tile.
     * It is used to distinguish between different alien types, such as VIOLET or BROWN.
     */
    private AlienColor alienColor;

    /**
     * Constructor for the Tile_HousingUnit class.
     *
     * @param id            The unique identifier for the housing unit tile.
     * @param connectorList A list of connectors associated with the tile, defined by the Type_side_connector enumeration.
     * @param types         The type of the housing unit tile, based on the SSTTypes enumeration.
     */
    @JsonCreator
    public Tile_HousingUnit(
            @JsonProperty("id") int id,
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("types") SSTTypes types
    ) {
        super(id, connectorList, types);
        this.numPassengers = 0;
        this.isThereAlien = false;
    }

    /**
     * Increments the number of passengers in this Tile_HousingUnit.
     * This method updates the internal counter for passengers,
     * reflecting an increase of one unit in the total number of passengers.
     */
    public void addPassengerTileHousing() {
        numPassengers++;
    }

    /**
     * Retrieves the number of passengers currently present in the housing unit.
     *
     * @return the number of passengers as an integer
     */
    @Override
    public int getNumPassengers() {
        return numPassengers;
    }

    /**
     * Determines whether there is an alien present in the housing unit.
     *
     * @return true if there is an alien present; false otherwise.
     */
    @Override
    public boolean getIsThereAlien() {
        return isThereAlien;
    }

    /**
     * Sets the presence of an alien in the housing unit.
     *
     * @param isThereAlien a boolean value indicating whether there is an alien
     *                     in the housing unit (true if an alien is present, false if not).
     */
    public void setIsThereAlien(boolean isThereAlien) {
        this.isThereAlien = isThereAlien;
    }

    /**
     * Sets the number of passengers for the housing unit.
     *
     * @param numPassengers the number of passengers to be set
     */
    public void setNumPassengers(int numPassengers) {
        this.numPassengers = numPassengers;
    }

    /**
     * Adds a passenger to the housing unit tile.
     * <p>
     * If the current number of passengers exceeds the allowed number (1),
     * prevents the addition of more passengers and triggers an event to
     * notify the player through the EventBus. Otherwise, increments the
     * passenger count.
     *
     * @throws Exception if an error occurs in notifying the player via the EventBus.
     */
    @Override
    public void addPassenger() throws Exception {
        if (numPassengers > 1) {
            System.out.println("You can't add More Passengers there!");
            getAttachedShip().getFlightBoard().getMygame().getEventBus().cannotInsert(getAttachedShip().getMyPlayer());
        } else {
            numPassengers++;
        }
    }

    /**
     * Removes a specified number of passengers from the housing unit.
     * This method prioritizes removing human passengers first, and if
     * necessary, it will remove an Alien if present.
     *
     * @param numPass the number of passengers to be removed.
     *                If the number exceeds the available human passengers,
     *                any remaining amount will attempt to remove an Alien
     *                if one is present.
     * @throws Exception if an error occurs during the removal process
     *                   or if the requested removal exceeds the possible amount.
     */
    @Override
    public void removePassenger(int numPass) throws Exception {
        int remaining = numPass;
        int toRemoveHumans = Math.min(remaining, numPassengers);
        numPassengers -= toRemoveHumans;
        remaining -= toRemoveHumans;
        if (remaining > 0 && isThereAlien) {
            removeAlien();
            remaining--;
        }
    }

    /**
     * Removes the alien from the current tile if one is present. Updates the state of the tile and notifies the game
     * system if the removal is attempted without an alien being present.
     *
     * @throws Exception if there is an error with the event notification or game system processing.
     *                   <p>
     *                   If no alien is present on the tile, a warning is logged, and the game system's event bus is notified of the invalid action.
     *                   If an alien is present, it is removed by setting the `isThereAlien` flag to false and resetting the `alienColor` to null.
     */
    @Override
    public void removeAlien() throws Exception {
        if (!isThereAlien) {
            System.out.println("You cannot remove Aliens there!");
            getAttachedShip().getFlightBoard().getMygame().getEventBus().wrongInput(getAttachedShip().getMyPlayer());
        } else {
            isThereAlien = false;
            alienColor = null;
        }
    }

    /**
     * Attempts to add an alien of the specified color to the given tile position on the spaceship.
     * The process will verify specific conditions such as the availability of a life-support module
     * of the matching color, and whether there are passengers or existing aliens of the same color
     * that conflict with the addition.
     *
     * @param wantAlien a boolean indicating whether the addition of the alien is desired
     * @param color     the color of the alien to be added (e.g., VIOLET, BROWN)
     * @param row       the row position on the spaceship tile matrix where the alien should be placed
     * @param column    the column position on the spaceship tile matrix where the alien should be placed
     * @throws Exception if the insertion process fails because conditions are not met or other unexpected errors occur
     */
    @Override
    public void addAlien(boolean wantAlien, AlienColor color, int row, int column) throws Exception {
        if (numPassengers > 0) {
            System.out.println("Cannot add an alien when there are passengers!");
            getAttachedShip().getFlightBoard().getMygame().getEventBus()
                    .cannotInsert(getAttachedShip().getMyPlayer());
            return;
        }
        if ((color == AlienColor.VIOLET && getAttachedShip().isVioletAlien()) ||
                (color == AlienColor.BROWN && getAttachedShip().isBrownAlien())) {
            System.out.println("Cannot add another " + color.name().toLowerCase() + " alien!");
            getAttachedShip().getFlightBoard().getMygame().getEventBus()
                    .cannotInsert(getAttachedShip().getMyPlayer());
            return;
        }

        SpaceShipTile[][] shipMatrix = getAttachedShip().getShipMatrix();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = column + dir[1];

            if (newRow >= 0 && newRow < shipMatrix.length &&
                    newCol >= 0 && newCol < shipMatrix[0].length) {

                SpaceShipTile adjacentTile = shipMatrix[newRow][newCol];

                if (adjacentTile.getType() == SSTTypes.Tile_AlienLifeSupport &&
                        adjacentTile.getColor() == color) {
                    isThereAlien = true;
                    alienColor = color;
                    getAttachedShip().addAlien(color);
                    this.setAlienColor(color);

                    return;
                }
            }
        }
        System.out.println("No matching life-support module found: cannot add alien.");
        getAttachedShip().getFlightBoard().getMygame().getEventBus()
                .cannotInsert(getAttachedShip().getMyPlayer());
    }


    /**
     * Retrieves the color of the alien associated with this Tile_HousingUnit.
     *
     * @return the AlienColor of the alien, which can be VIOLET or BROWN.
     */
    @Override
    public AlienColor getAlienColor() {
        return alienColor;
    }

    /**
     * Sets the color of the alien for this tile.
     *
     * @param alienColor the color to assign to the alien. It should be one of the predefined values in {@link AlienColor}.
     */
    @Override
    public void setAlienColor(AlienColor alienColor) {
        this.alienColor = alienColor;
    }
}