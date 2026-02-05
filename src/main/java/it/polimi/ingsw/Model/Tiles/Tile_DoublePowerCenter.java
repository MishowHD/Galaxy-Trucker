package it.polimi.ingsw.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.List;

public class Tile_DoublePowerCenter extends SpaceShipTile {
    /**
     * Represents the number of batteries that are currently stored in the tile.
     * This variable tracks the current charge capacity of the tile.
     * The value is constrained to not exceed the tile's maximum battery capacity.
     */
    protected int numBatteries;

    /**
     * Returns the battery capacity of the tile.
     *
     * @return the maximum number of batteries that the tile can hold
     */
    public int getTileBattCapacity() {
        return 2;
    }

    /**
     * Constructs a Tile_DoublePowerCenter object, which represents a specific type of spaceship tile
     * capable of supporting a double power center functionality.
     *
     * @param id            The unique identifier for this tile.
     * @param connectorList A list of connectors that define the tile's connection capabilities,
     *                      represented by the Type_side_connector enumeration.
     * @param types         The specific type of the tile, represented by the SSTTypes enumeration.
     */
    @JsonCreator
    public Tile_DoublePowerCenter(
            @JsonProperty("id") int id,
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("types") SSTTypes types
    ) {
        super(id, connectorList, types);
        this.numBatteries = getTileBattCapacity();
    }

    /**
     * Sets the number of batteries to the given value,
     * ensuring it remains within the range of 0 and the maximum battery capacity.
     *
     * @param i the desired number of batteries to set. The value is constrained
     *          between 0 and the maximum capacity returned by getTileBattCapacity().
     */
    public void setCharge(int i) {
        numBatteries = Math.max(0, Math.min(i, getTileBattCapacity()));
    }

    /**
     * Retrieves the current number of charges (batteries) stored in the tile.
     *
     * @return the number of batteries currently available in the tile.
     */
    @Override
    public int getNumCharges() {
        return numBatteries;
    }

    /**
     * Adds a charge (battery) to this tile's battery count, if the current number
     * of batteries is less than the tile's maximum capacity. If the tile's battery
     * count is less than the maximum allowed, one battery is consumed
     * from the associated bank. If the tile is already at maximum capacity,
     * a message will be printed indicating that no additional batteries can be added.
     * <p>
     * Behavior:
     * - Checks if `numBatteries` is less than the value returned by `getTileBattCapacity`.
     * - If there is space for additional batteries:
     * - Calls `useBattery` on the bank associated with the ShipBoard to decrement one battery.
     * - Increments the `numBatteries` count by one.
     * - Otherwise, prints a message indicating there is no more space for additional batteries.
     */
    @Override
    public void addCharge() {
        if (numBatteries < getTileBattCapacity()) {
            this.getAttachedShip().getFlightBoard().getBank().useBattery(1);
            numBatteries++;
        } else {
            System.out.println("No more space to add batteries");
        }
    }

    /**
     * Removes a specified number of charges (batteries) from the tile's current capacity.
     * If the number of charges to be removed exceeds the existing capacity, all batteries
     * are removed and a message is printed. If the specified number is non-positive, the
     * method returns without action.
     *
     * @param numCharges the number of charges to remove from the tile's battery capacity
     */
    @Override
    public void removeCharge(int numCharges) {
        if (numCharges <= 0) return;
        if (numBatteries - numCharges >= 0) {
            this.getAttachedShip().getFlightBoard().getBank().addBattery(numCharges);
            numBatteries -= numCharges;
        } else {
            System.out.println("No more batteries to remove");
            numBatteries = 0;
        }
    }

    /**
     * Recharges the power center to its full battery capacity if it is currently depleted.
     * <p>
     * If the current battery count (`numBatteries`) is zero, this method restores the battery
     * count to the tile's maximum capacity by consuming the required number of batteries
     * from the associated ship's bank.
     * <p>
     * The battery consumption is managed through the `useBattery` method of the ship bank,
     * ensuring that the ship has sufficient battery resources available. If insufficient
     * resources are available in the bank, the operation may fail.
     */
    @Override
    public void RechargeAll() {
        if (numBatteries == 0) {
            numBatteries = getTileBattCapacity();
            this.getAttachedShip().getFlightBoard().getBank().useBattery(numBatteries);
        }
    }
}