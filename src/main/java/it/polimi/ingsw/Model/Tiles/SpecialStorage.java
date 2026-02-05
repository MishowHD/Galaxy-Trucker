package it.polimi.ingsw.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.List;

public class SpecialStorage extends Storage {

    /**
     * Creates an instance of SpecialStorage with specified parameters.
     *
     * @param id the unique identifier for the storage.
     * @param connectorList the list of available connectors of the storage.
     * @param capacity the maximum number of goods the storage can hold.
     * @param types the type of the storage represented as an SSTTypes value.
     */
    @JsonCreator
    public SpecialStorage(
            @JsonProperty("id") int id,
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("capacity") int capacity,
            @JsonProperty("types") SSTTypes types
    ) {
        super(id, connectorList, capacity, types);
    }

    /**
     * Adds a specified good to the storage if the capacity allows it.
     * If the storage is full, an error message is displayed.
     * This method does not impose any color restriction on the goods being added.
     *
     * @param good the goods to add to the storage
     */
    @Override
    public void addGoods(Goods good) {
        if (getEffectivePresentGoods().size() < getCapacity()) {
            // No color restriction in this special storage
            this.getAttachedShip().getFlightBoard().getBank().useGood(good);
            getEffectivePresentGoods().add(good);
        } else {
            System.out.println("Storage is at full capacity. Cannot add more goods.");
        }
    }
}