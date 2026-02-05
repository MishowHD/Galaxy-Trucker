package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.COMBAT_ZONE_BEGIN;

public class Card_Combat_zone extends Card {
    /**
     * A list of consequences associated with a card in the combat zone.
     * Each consequence defines a specific outcome or impact that can occur
     * as a result of an event or action in the combat zone.
     *
     * The consequences are represented as instances of subclasses of the
     * {@link Consequences} abstract class, allowing different types of consequences
     * to be modeled (e.g., lost crew, lost goods, etc.).
     *
     * This field is immutable and is initialized during the construction of the
     * {@code Card_Combat_zone} object.
     */
    private final ArrayList<Consequences> consequences;
    /**
     * Represents a collection of causes associated with a combat zone card.
     * Each cause defines a specific triggering condition or prerequisite
     * that impacts the state or behavior of the card within the combat zone.
     * The causes are represented as instances of the abstract class {@link Causes}
     * and may include various subclasses such as {@link Causes_Crew},
     * {@link Causes_Fire_power}, and {@link Causes_Motion_power}.
     * This list is initialized during the creation of the card and remains unmodifiable.
     */
    private final ArrayList<Causes> causes;


    /**
     * Retrieves the list of consequences associated with the current card or state.
     *
     * @return an ArrayList containing objects of type Consequences, representing the effects
     *         or penalties that may be applied when certain conditions are met.
     */
    @Override
    public ArrayList<Consequences> getConsequences() {
        return consequences;
    }

    /**
     * Retrieves the list of causes associated with the card.
     *
     * @return an ArrayList containing the causes related to this card.
     */
    @Override
    public ArrayList<Causes> getCauses() {
        return causes;
    }

    /**
     * Constructor for the Card_Combat_zone class.
     * This class represents a combat zone card, containing information about its
     * causes and consequences as well as its initial state.
     *
     * @param id The unique identifier for the card.
     * @param cardName The name of the card.
     * @param CardLevel The level of the card.
     * @param isTest A boolean flag indicating if the card is a test version.
     * @param consequences An ArrayList of Consequences representing the consequences
     *                     associated with the card.
     * @param causes An ArrayList of Causes representing the causes associated with the card.
     * @throws Exception If an error occurs during the creation of the card or its state.
     */
    @JsonCreator
    public Card_Combat_zone(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int CardLevel,
            @JsonProperty("isTest") boolean isTest,
            @JsonProperty("consequences") ArrayList<Consequences> consequences,
            @JsonProperty("causes") ArrayList<Causes> causes
    ) throws Exception {
        super(id, cardName, CardLevel, isTest);
        this.causes = causes;
        this.consequences = consequences;
        State_enum = COMBAT_ZONE_BEGIN;
        state = new CombatZoneBegin(this);

    }

    /**
     * Sets the current state of the combat zone.
     *
     * @param state the new state to set, represented as an instance of {@link CombatZoneState}.
     */
    public void setState(CombatZoneState state) {
        this.state = state;
        //state.activate(flightBoard);
    }

    /**
     * Retrieves the current state of the card.
     *
     * @return the current state of the card as a {@link Card_State} object.
     */
    public Card_State getState() {
        return state;
    }


    /**
     * Retrieves the number of crew members lost as a result of the combat.
     *
     * @return the number of crew members lost, currently always returns 0.
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Retrieves the amount of money gained.
     *
     * @return the total money gained as an integer
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Executes the effect associated with the current state of the combat zone card
     * by activating the relevant state logic on the provided flight board.
     *
     * @param board the flight board on which the card's effect will be applied
     * @throws Exception if an error occurs during the activation of the state
     */
    @Override
    public void effect(FlightBoard board) throws Exception {
        state.Activate(board);
    }

}