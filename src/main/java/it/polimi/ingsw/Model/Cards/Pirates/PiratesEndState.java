
package it.polimi.ingsw.Model.Cards.Pirates;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PIRATES_END_STATE;


public class PiratesEndState extends PiratesState {
    /**
     * Initializes the PiratesEndState for the specified card and transitions the card to the pirates end state.
     * This includes setting the appropriate state, updating player rankings, and starting the surrender timer.
     *
     * @param pirates the card associated with the PiratesEndState.
     * @throws Exception if an error occurs while setting the state or starting the surrender timer.
     */
    public PiratesEndState(Card pirates) throws Exception {
        super(pirates);
        card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),PIRATES_END_STATE);
        //card.getGame().getEventBus().updateStatus(stateEnum.CARD_ENDED_EFFECT);
        card.getGame().getGameState().startSurrenderTimer();

    }

    /**
     * Retrieves the next state of the card.
     *
     * @return the next state of the card as a Card_State object.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}
