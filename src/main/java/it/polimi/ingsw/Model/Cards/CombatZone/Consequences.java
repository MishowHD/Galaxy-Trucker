package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.ArrayList;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "consequenceType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Consequences_Lost_crew.class, name = "Consequences_Lost_crew"),
        @JsonSubTypes.Type(value = Consequences_Lost_days.class, name = "Consequences_Lost_days"),
        @JsonSubTypes.Type(value = Consequences_Lost_goods.class, name = "Consequences_Lost_goods"),
        @JsonSubTypes.Type(value = Consequences_Shots.class, name = "Consequences_Shots"),
})

public abstract class Consequences implements Serializable {
    /**
     * Retrieves the penalty related to the crew.
     *
     * @return the penalty value as an integer
     * @throws RuntimeException if the operation is not supported
     */
    public int getCrewPen() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the number of goods.
     *
     * @return the quantity of goods.
     * @throws RuntimeException if the operation is not supported.
     */
    public int getNumGoods() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }


    /**
     * Retrieves the list of shots associated with the current object.
     *
     * @return an ArrayList containing Shot objects.
     * @throws RuntimeException if the method is not supported.
     */
    public ArrayList<Shot> getShots() {
        throw new RuntimeException("Not supported yet.");
    }
}
