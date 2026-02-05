package it.polimi.ingsw.Model.Cards.Epidemic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.EPIDEMIC_STATE_BASE;

public class Card_Epidemic extends Card {

    /**
     * Constructs a Card_Epidemic object by initializing its properties and setting its state
     * to the base epidemic state.
     *
     * @param id the unique identifier of the card
     * @param cardName the name of the card
     * @param cardLevel the level or rank of the card
     * @param isTest a flag indicating whether this card is for testing purposes or not
     * @throws Exception if there are issues during initialization
     */
    @JsonCreator
    public Card_Epidemic(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int cardLevel,
            @JsonProperty("isTest") boolean isTest
    ) throws Exception {
        super(id, cardName, cardLevel, isTest);
        State_enum = EPIDEMIC_STATE_BASE;
        state = new EpidemicStateBase(this);

    }

    /**
     * Retrieves the number of crew members lost as a result of this card's effect.
     *
     * @return the number of crew members lost, represented as an integer
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Retrieves the amount of money gained by this card.
     *
     * @return the amount of money gained, always 0 for this card implementation.
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Activates the current state of the epidemic card on the provided flight board.
     *
     * @param flightBoard the flight board on which the epidemic effect is applied
     * @throws Exception if an error occurs during the activation process
     */
    public void effect(FlightBoard flightBoard) throws Exception {

        state.Activate(flightBoard);
    }


    /**
     * Sets a new state for the epidemic card.
     *
     * @param state the new {@code EpidemicState} to be set for this card
     */
    public void setState(EpidemicState state) {
        this.state = state;
    }


}
