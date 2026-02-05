package it.polimi.ingsw.Model.Cards.AbandonedStation;

import it.polimi.ingsw.Model.Boards.FlightBoard;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.ABANDONED_STATION_END_STATE;

public class AbandonedStationEndState extends AbandonedStationState {


    /**
     * Represents the end state of an abandoned station card.
     * Extends the generic AbandonedStationState and marks the
     * final phase within the lifecycle of the card's state.
     *
     * @param abandonedStation the specific instance of Card_AbandonedStation
     *                         that this state is associated with
     * @throws Exception if there is an issue initializing the state
     */
    public AbandonedStationEndState(Card_AbandonedStation abandonedStation) throws Exception {
        super(abandonedStation);

    }


    /**
     * Activates the abandoned station end state, setting the appropriate state
     * and initiating the surrender timer for the game.
     *
     * @param flightBoard the FlightBoard instance associated with the current game state
     * @throws Exception if an error occurs during state activation
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),ABANDONED_STATION_END_STATE);
        card.getGame().getGameState().startSurrenderTimer();
    }

    /**
     * Retrieves the next state for an {@code AbandonedStationState}.
     * In the context of the {@code AbandonedStationEndState}, this method returns itself
     * as this is the final state in the state transition.
     *
     * @return the current state, as this represents the final state with no subsequent state.
     * @throws RuntimeException if called in an unexpected scenario.
     */
    @Override
    public AbandonedStationState getNextState() throws RuntimeException {
        // This is the final state, no next state
        return this;
    }
}