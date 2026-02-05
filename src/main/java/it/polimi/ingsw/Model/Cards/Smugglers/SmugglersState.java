package it.polimi.ingsw.Model.Cards.Smugglers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;

public abstract class SmugglersState extends Card_State {

    /**
     * Constructs a new SmugglersState with the specified Smugglers card.
     *
     * @param smugglers the Smugglers card associated with this state
     */
    public SmugglersState(Card smugglers) {
        this.card = smugglers;
    }

    /**
     * Determines and returns the next state in the card state cycle.
     * This method should be implemented by subclasses to define
     * the transition logic to the next state.
     *
     * @return the next state as an instance of Card_State
     */
    public abstract Card_State getNextState();


}
