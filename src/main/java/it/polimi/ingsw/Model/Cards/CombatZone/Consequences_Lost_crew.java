package it.polimi.ingsw.Model.Cards.CombatZone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Player.Player;

import java.io.Serializable;

public class Consequences_Lost_crew extends Consequences implements Serializable {
    /**
     * Represents the number of crew members affected by a consequence.
     * This variable is used to store the penalty value related to the loss of crew.
     */
    int numPass;


    /**
     * Constructs a Consequences_Lost_crew object with the specified number of passengers lost.
     *
     * @param numPass the number of passengers lost as a consequence
     */
    @JsonCreator
    public Consequences_Lost_crew(@JsonProperty("numPass") int numPass) {
        this.numPass = numPass;
    }


    /**
     * Retrieves the crew penalty associated with this instance.
     *
     * @return the penalty value for the crew as an integer
     */
    public int getCrewPen() {
        return numPass;
    }
}
