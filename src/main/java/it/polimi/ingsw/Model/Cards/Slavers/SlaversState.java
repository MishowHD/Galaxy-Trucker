package it.polimi.ingsw.Model.Cards.Slavers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;

public abstract class SlaversState extends Card_State {

    /**
     * Constructs a new SlaversState, initializing it with the provided card.
     *
     * @param slavers the Card instance that this state is associated with
     */
    public SlaversState(Card slavers) {
        this.card = slavers;
    }

    /**
     * Activates the current card state and triggers the effect started event
     * on the game's event bus.
     *
     * @param flightBoard the flight board instance that maintains the game environment
     * @throws Exception if an error occurs during activation
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        card.getGame().getEventBus().effectStarted();
    }



    /**
     * Determines and returns the next state of the card.
     * The logic for transitioning to the next state is implemented
     * in subclasses of this method.
     *
     * @return the next state of the card as a {@code Card_State}.
     */
    public abstract Card_State getNextState();
}
