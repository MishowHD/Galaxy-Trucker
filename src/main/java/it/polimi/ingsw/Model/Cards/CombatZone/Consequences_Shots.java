package it.polimi.ingsw.Model.Cards.CombatZone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;

import java.io.Serializable;
import java.util.ArrayList;

public class Consequences_Shots extends Consequences implements Serializable {
    /**
     * Represents a collection of Shot objects associated with the consequence.
     * Each Shot in the list captures specific details such as its type, rotation,
     * and size, which are used to define the consequences in a combat zone.
     */
    ArrayList<Shot> Shots;

    /**
     * Constructs a Consequences_Shots object with the specified list of Shot objects.
     *
     * @param Shots the list of Shot objects representing the shots associated with this consequence.
     */
    @JsonCreator
    public Consequences_Shots(@JsonProperty("Shots") ArrayList<Shot> Shots) {
        this.Shots = Shots;
    }

    /**
     * Retrieves the list of shots associated with this instance.
     *
     * @return an ArrayList containing Shot objects that represent the shots associated
     *         with this instance.
     */
    public ArrayList<Shot> getShots() {
        return Shots;
    }

}
