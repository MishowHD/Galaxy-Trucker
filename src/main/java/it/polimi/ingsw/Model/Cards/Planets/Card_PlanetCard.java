package it.polimi.ingsw.Model.Cards.Planets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PLANET_STATE_INIT;

public class Card_PlanetCard extends Card {
    /**
     * Represents the penalty in days associated with the flight when interacting with a PlanetCard.
     * This value indicates the delay or setback in terms of flight days that a player incurs due to
     * the card's effect. It is a static penalty set at the card creation and remains constant throughout
     * the card's lifecycle.
     */
    private final int FlightDaysPenalty;
    /**
     * Represents a collection of planets associated with a Card_PlanetCard.
     * This variable stores an ArrayList of Planet objects that are used as part of the game's card effects
     * and state management. It is initialized during the creation of a Card_PlanetCard instance and
     * accessed through getter methods or directly within the class for various operations.
     *
     * The PlanetVector's functionality relates to managing and manipulating the planets that interact
     * with card mechanics, such as state transitions or specific game effects.
     */
    private final ArrayList<Planet> PlanetVector;
    /**
     * Represents the current state of the planet card.
     * This variable holds an instance of {@link PlanetState} which defines the behavior
     * and transitions for the particular state the card is in during gameplay.
     *
     * The state determines actions that can be triggered on the card and may affect
     * other game elements, such as the players or the flight board. The {@link PlanetState}
     * instance assigned to this variable can be dynamically updated to reflect changes
     * in the card's state.
     */
    private PlanetState state;

    /**
     * Constructor for the Card_PlanetCard class. This class represents a planet card in the game,
     * which has specific attributes such as a flight days penalty and a vector of planets associated with it.
     *
     * @param id                  An integer representing the unique identifier for the card.
     * @param cardName            A string specifying the name of the card.
     * @param CardLevel           An integer indicating the level of the card.
     * @param isTest              A boolean flag indicating whether the card is used in a test scenario.
     * @param flightDaysPenalty   An integer denoting the penalty in flight days associated with this card.
     * @param PlanetVector        An ArrayList of Planet objects representing the planets associated with this card.
     */
    @JsonCreator
    public Card_PlanetCard(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int CardLevel,
            @JsonProperty("isTest") boolean isTest,
            @JsonProperty("FlightDaysPenalty") int flightDaysPenalty,
            @JsonProperty("PlanetVector") ArrayList<Planet> PlanetVector
    ) {
        super(id, cardName, CardLevel, isTest);
        this.FlightDaysPenalty = flightDaysPenalty;
        this.PlanetVector = PlanetVector;
        State_enum = PLANET_STATE_INIT;

    }

    /**
     * Retrieves the vector of Planet instances associated with the card.
     *
     * @return an ArrayList containing the Planet objects linked to this card.
     */
    @Override
    public ArrayList<Planet> getPlanetVector() {
        return PlanetVector;
    }

    /**
     * Updates the current state of the planet card.
     *
     * @param state the new state to be set for the planet card
     */
    public void setState(PlanetState state) {
        this.state = state;
    }

    /**
     * Retrieves the current state of the planet card.
     *
     * @return the current PlanetState associated with the planet card.
     */
    public PlanetState getState() {
        return state;
    }

    /**
     * Retrieves the penalty in days associated with the flight.
     *
     * @return the penalty in days as an integer.
     */
    public int getDaysPenalty() {
        return FlightDaysPenalty;
    }

    /**
     * Retrieves the number of crew members lost as a result of the card's effects.
     *
     * @return the number of crew members lost, which is always 0 for this implementation.
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Calculates and returns the amount of money gained from the card.
     *
     * @return the amount of money gained, represented as an integer
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Initializes the state of the planet card and activates it using the provided flight board and planet vector.
     *
     * @param fb the FlightBoard object that provides contextual information required to activate the state
     * @throws Exception if an error occurs during the activation of the state
     */
    public void effect(FlightBoard fb) throws Exception {
        state = new PlanetStateInit(this, fb, PlanetVector);

        state.Activate();
    }

}
