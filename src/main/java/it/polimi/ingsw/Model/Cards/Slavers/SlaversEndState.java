package it.polimi.ingsw.Model.Cards.Slavers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SLAVERS_END_STATE;

public class SlaversEndState extends SlaversState {
    /**
     * Constructs a new SlaversEndState and initializes the state of the provided card.
     * This method transitions the card into the SLAVERS_END_STATE and starts the surrender timer.
     *
     * @param slavers the card representing slavers to be transitioned to the SLAVERS_END_STATE
     * @throws Exception if an error occurs during state initialization or timer start
     */
    public SlaversEndState(Card slavers) throws Exception {
        super(slavers);
        card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),SLAVERS_END_STATE);
        card.getGame().getGameState().startSurrenderTimer();

    }

    /**
     * Retrieves the next state of the card within the current game context.
     *
     * @return the next state of the card as a Card_State object.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}
