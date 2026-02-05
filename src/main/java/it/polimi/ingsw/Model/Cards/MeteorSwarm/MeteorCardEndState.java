package it.polimi.ingsw.Model.Cards.MeteorSwarm;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.METEOR_CARD_END_STATE;

public class MeteorCardEndState extends MeteorCardState {
    /**
     * Initializes a new instance of the MeteorCardEndState class, representing the final state of the Meteor Swarm card.
     * This constructor updates the state of the card and initiates the surrender timer on the game's state.
     *
     * @param meteorSwarm the Card_MeteorSwarm object associated with this state, representing the meteor swarm card
     *                    whose state is being transitioned to the end state.
     * @throws Exception if there are issues while setting the state or starting the surrender timer.
     */
    public MeteorCardEndState(Card_MeteorSwarm meteorSwarm) throws Exception {
        super(meteorSwarm);
        this.card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),METEOR_CARD_END_STATE);
        //here I change the card stare
        card.getGame().getGameState().startSurrenderTimer();

    }

    /**
     * Determines and returns the subsequent game state for a card.
     *
     * This method is intended to transition the card from its current state
     * to the next logical state in the game sequence. The returned state
     * represents the next phase of the card's lifecycle or behavior in the game.
     *
     * @return the next state of the card as a Card_State object, or null if
     *         no further state is defined or applicable.
     */
    @Override
    public Card_State getNextState() {
        return null;
    }

    /**
     * Activates the current state by triggering necessary game events and transitions.
     *
     * @param flightBoard the current state of the game board used during the activation process
     * @throws Exception if an error occurs during the activation
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        card.getGame().getEventBus().effectEnded();
    }
}