package it.polimi.ingsw.Model.Cards.CombatZone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Consequences_Lost_goods extends Consequences implements Serializable {
    /**
     * Represents the quantity of goods affected as a result of a consequence.
     * This variable holds the number of lost goods in the context of a specific consequence.
     */
    int numGoods;

    /**
     * Constructs a Consequences_Lost_goods instance with the specified number of lost goods.
     *
     * @param numGoods the number of goods lost as a consequence
     */
    @JsonCreator
    public Consequences_Lost_goods(@JsonProperty("numGoods") int numGoods) {
        this.numGoods = numGoods;
    }

    /**
     * Retrieves the number of goods associated with this consequence.
     *
     * @return the number of goods as an integer.
     */
    public int getNumGoods() {
        return numGoods;
    }
}
