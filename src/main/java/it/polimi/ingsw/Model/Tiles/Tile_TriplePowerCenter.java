package it.polimi.ingsw.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.List;

public class Tile_TriplePowerCenter extends Tile_DoublePowerCenter {
    /**
     * Constructs a Tile_TriplePowerCenter object, representing a specialized spaceship tile
     * capable of supporting triple power center functionality with a battery capacity of three units.
     *
     * @param id            The unique identifier for this tile.
     * @param connectorList A list defining the connection capabilities of the tile,
     *                      represented as a list of {@link Type_side_connector} enumerations.
     * @param types         The specific type of this tile, represented by the {@link SSTTypes} enumeration.
     */
    @JsonCreator
    public Tile_TriplePowerCenter(
            @JsonProperty("id") int id,
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("types") SSTTypes types
    ) {
        super(id, connectorList, types);
        this.numBatteries = getTileBattCapacity(); // initialize to full capacity
    }

    /**
     * Retrieves the battery capacity of the tile.
     *
     * @return the maximum battery capacity for the tile, which is 3.
     */
    @Override
    public int getTileBattCapacity() {
        return 3;
    }
}
