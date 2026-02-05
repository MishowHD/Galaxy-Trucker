package it.polimi.ingsw.Model.Cards.AbandonedStation;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;

public abstract class AbandonedStationState extends Card_State {
    /**
     * Represents the card instance associated with the current state of an
     * abandoned station gameplay entity. This field holds the specific
     * {@code Card_AbandonedStation} instance that the state is managing.
     * The card can transition through different states of the game lifecycle.
     */
    protected Card_AbandonedStation card;

    /**
     * Constructs a new instance of AbandonedStationState with the specified abandoned station card.
     * This constructor initializes the state by associating it with a specific Card_AbandonedStation instance.
     *
     * @param abandonedStation the Card_AbandonedStation instance to associate with this state
     */
    public AbandonedStationState(Card_AbandonedStation abandonedStation) {
        this.card = abandonedStation; // Inizializza il campo 'card'
    }

    /**
     * Activates the current state affecting the provided flight board.
     *
     * @param flightBoard the flight board instance to be influenced by the state activation
     * @throws Exception if there is an error during activation
     */
    public abstract void activate(FlightBoard flightBoard) throws Exception;

    /**
     * Retrieves the next state of the {@code AbandonedStationState}.
     * This can vary based on the specific implementation of the subclass.
     *
     * @return the next {@code AbandonedStationState} in the state transition process.
     * @throws Exception if an error occurs while determining the next state.
     */
    public abstract AbandonedStationState getNextState() throws Exception;

}