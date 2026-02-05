package it.polimi.ingsw.Model.Cards.CombatZone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.io.Serializable;

public class Consequences_Lost_days extends Consequences implements Serializable {

    /**
     * Represents the number of days associated with a specific consequence.
     * This value indicates a penalty or impact in terms of lost days.
     */
    int numDays;

    /**
     * Constructs a Consequences_Lost_days object with the specified number of days.
     *
     * @param days the number of days lost as a consequence
     * @throws RuntimeException if the provided input is invalid or an error occurs during instantiation
     */
    @JsonCreator
    public Consequences_Lost_days(@JsonProperty("numDays") int days) throws RuntimeException {
        this.numDays = days;
    }


    /**
     * Retrieves the number of days associated with the current object.
     *
     * @return the number of days as an integer.
     */
    public int getNumDays() {
        return numDays;
    }
}
