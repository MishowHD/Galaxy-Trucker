package it.polimi.ingsw.Model.Tiles;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.Utils.Goods;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;


public class Storage extends SpaceShipTile {
    /**
     * Represents the maximum number of goods that the storage can hold.
     * This value is immutable and defines the storage's capacity constraint.
     */
    private final int capacity;
    /**
     * A list that represents the currently stored goods within the storage.
     * This list stores objects of type {@link Goods}, which are added or managed
     * based on certain conditions such as storage capacity and goods properties.
     * The effectivePresentGoods variable ensures that only permitted goods, based
     * on their attributes, are stored in the inventory.
     *
     * It is initialized as an empty {@link ArrayList} and grows dynamically as goods
     * are added, up to the storage's defined capacity.
     */
    private final ArrayList<Goods> effectivePresentGoods;

    /**
     * Constructor for the Storage class, a specialized type of SpaceShipTile that holds goods.
     *
     * @param id           The unique identifier for the storage tile.
     * @param connectorList A list of connectors associated with the storage tile, defined by the Type_side_connector enumeration.
     * @param capacity     The maximum capacity of goods that can be stored.
     * @param types        The type of the storage tile, based on the SSTTypes enumeration.
     */
    @JsonCreator
    public Storage(
            @JsonProperty("id") int id,
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("capacity") int capacity,
            @JsonProperty("types") SSTTypes types
    ) {
        super(id, connectorList, types);
        this.capacity = capacity;
        this.effectivePresentGoods = new ArrayList<>(); // Inizializziamo la lista vuota
    }

    /**
     * Retrieves the list of goods currently stored in the storage that are considered effective and present.
     * The returned goods must abide by any capacity or value constraints defined in the storage.
     *
     * @return a list of goods that are effectively present in this storage. The list is mutable and reflects the current state of stored goods.
     */
    @Override
    public List<Goods> getEffectivePresentGoods() {
        return effectivePresentGoods;
    }

    /**
     * Retrieves the maximum capacity of goods that can be stored.
     *
     * @return the storage capacity as an integer.
     */
    @Override
    public int getCapacity() {
        return capacity;
    }

    /**
     * Adds a specified {@code Goods} object to the storage if conditions for storage capacity
     * and goods value are met. If the storage is full or the goods are not eligible for storage,
     * appropriate messages are displayed.
     *
     * @param good the {@code Goods} object to be added to the storage
     *             - Goods with a value of 3 or greater cannot be added.
     *             - If the storage has no available capacity, the goods will not be added.
     */
    @Override
    public void addGoods(Goods good) {
        if (effectivePresentGoods.size() < capacity) {
            if (good.getValue() < 3) {
                this.getAttachedShip().getFlightBoard().getBank().useGood(good);
                effectivePresentGoods.add(good);
            } else {
                System.out.println("Red goods cannot be stored in this storage.");
            }
        } else {
            System.out.println("Storage is at full capacity. Cannot add more goods.");
        }
    }
}