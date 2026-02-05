package it.polimi.ingsw.Model.Cards.AbandonedShip;


import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;

public class AbShFinal extends AbandonedShipState {
    /**
     * Constructs an AbShFinal object by modifying the state of the provided card
     * and executing necessary game state updates. Specifically, it transitions
     * the card to the "ABANDONED_FINAL_STATE" and initiates the surrender timer
     * in the game state.
     *
     * @param carta the Card object whose states and associated game properties
     *              are to be updated. The card's game structure is modified to
     *              reflect the abandoned final state.
     * @throws Exception if there is any issue during the state transition
     *                   or surrender timer initiation.
     */
    public AbShFinal(Card carta) throws Exception {
        //here I change the card stare
        carta.getGame().getEventBus().updateCardUseSTATE(carta.getGame().getGameFlightBoard().getPlayerRankList(),c_State.ABANDONED_FINAL_STATE);
        carta.getGame().getGameState().startSurrenderTimer();
    }
}