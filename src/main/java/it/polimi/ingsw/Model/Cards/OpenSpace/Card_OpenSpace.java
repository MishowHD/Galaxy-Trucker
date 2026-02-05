package it.polimi.ingsw.Model.Cards.OpenSpace;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.OPEN_SPACE_PREPARATION;

public class Card_OpenSpace extends Card {
    /**
     * Constructs a new instance of {@code Card_OpenSpace}.
     * This card represents an open space state and initializes its state to {@code OPEN_SPACE_PREPARATION}.
     *
     * @param id         the unique identifier of the card
     * @param cardName   the name of the card
     * @param cardLevel  the level of the card
     * @param isTest     a boolean indicating whether the card is in a test mode
     */
    @JsonCreator
    public Card_OpenSpace(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int cardLevel,
            @JsonProperty("isTest") boolean isTest
    ) {
        super(id, cardName, cardLevel, isTest);
        State_enum = OPEN_SPACE_PREPARATION;
    }

    /**
     * Gets the number of crew members lost in this card's specific context.
     *
     * @return the number of crew members lost, which is always 0 for this implementation.
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Method to retrieve the amount of money gained.
     *
     * @return the integer value representing the money gained.
     *         By default, this returns 0 unless overridden by specific implementations.
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Transitions to the next state by activating the current state on the given flight board.
     *
     * @param flightBoard the flight board on which the current state will be activated
     * @throws Exception if any error occurs during state activation
     */
    public void goNextState(FlightBoard flightBoard) throws Exception {
        state.activate(flightBoard);
    }

    /**
     * Updates the state of the card to a new specified state.
     *
     * @param newState the new state to be assigned to the card. Must be a valid instance of Card_State.
     */
    // Metodo per cambiare lo stato
    public void setState(Card_State newState) {
        this.state = newState;
    }

    /**
     * Applies the effect of the card on the given flight board by transitioning to a new state
     * and activating the associated behavior of that state.
     *
     * @param flightBoard the flight board on which the effect is to be applied
     * @throws Exception if there is an issue during the effect application or state activation
     */
    @Override
    public void effect(FlightBoard flightBoard) throws Exception {
        state = new OpenSpacePreparation(this);

        state.activate(flightBoard);
    }


}