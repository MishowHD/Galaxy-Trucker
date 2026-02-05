package it.polimi.ingsw.Model.Cards.Epidemic;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.EPIDEMIC_STATE_FINAL;

public class EpidemicStateFinal extends EpidemicState {
    /**
     * Constructs an instance of the final stage of the epidemic state for the given epidemic card.
     * This constructor sets the card's state to {@code EPIDEMIC_STATE_FINAL}, updates the relevant
     * player rankings, and starts the surrender timer for the game.
     *
     * @param card the epidemic card instance undergoing a state change to its final stage
     * @throws Exception if starting the surrender timer or setting the state involves a failure
     */
    public EpidemicStateFinal(Card_Epidemic card) throws Exception {
        super();
        card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),EPIDEMIC_STATE_FINAL);
        //here I change the card stare
        //EventBus.getInstance().updateStatus(stateEnum.CARD_ENDED_EFFECT);
        card.getGame().getGameState().startSurrenderTimer();
    }
}
