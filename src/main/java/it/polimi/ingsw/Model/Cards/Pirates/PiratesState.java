package it.polimi.ingsw.Model.Cards.Pirates;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;

public abstract class PiratesState extends Card_State {

    /**
     * Constructor for the PiratesState class.
     * Initializes the state with the provided Pirates card.
     *
     * @param pirates the Card instance representing a Pirates card. This parameter is used to set
     *                the state and associate it with a specific Pirates card.
     */
    public PiratesState(Card pirates) {
        this.card = pirates;
    }

    /**
     * Activates the current state, triggering an event to indicate the start of an effect.
     *
     * @param flightBoard The flight board instance on which the activation is performed.
     * @throws Exception If an error occurs during activation.
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        card.getGame().getEventBus().effectStarted();
    }

    /**
     * Determines and returns the next logical state in the card state sequence.
     *
     * @return the next Card_State transition in the state sequence
     */
    public abstract Card_State getNextState();
}

