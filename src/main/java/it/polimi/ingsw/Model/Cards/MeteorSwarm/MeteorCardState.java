package it.polimi.ingsw.Model.Cards.MeteorSwarm;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;

public abstract class MeteorCardState extends Card_State {
    /**
     * A reference to an instance of {@link Card_MeteorSwarm} representing the specific card
     * with meteor swarm capabilities.
     *
     * This variable is used to maintain and manipulate the state and behavior
     * of the Meteor Swarm card in the context of a game's mechanics. It serves as the
     * starting point for accessing the card's properties, associated shots, and state transitions.
     */
    protected Card_MeteorSwarm meteorSwarm;

    /**
     * Constructs a new instance of the MeteorCardState class, initializing it with the
     * specified Card_MeteorSwarm object. This constructor associates the state with a specific
     * Meteor Swarm card instance, which determines the behavior and attributes of the state.
     *
     * @param meteorSwarm The Card_MeteorSwarm instance that this state is associated with.
     *                    This parameter provides the details and functionalities of the
     *                    Meteor Swarm card, including its shots and effects.
     */
    public MeteorCardState(Card_MeteorSwarm meteorSwarm) {
        this.meteorSwarm = meteorSwarm;
        this.card = meteorSwarm;
    }

    /**
     * Retrieves the subsequent state for the card's execution or progression
     * in the context of game logic. The implementation of this method is
     * specific to the concrete subclass that defines the behavior of state
     * transitions for specific card types or functionalities.
     *
     * @return the next state as a {@code Card_State} object, representing the
     *         logical progression of the card's state.
     */
    public abstract Card_State getNextState();
    /**
     * Activates the current state of the meteor card on the provided flight board.
     *
     * @param flightBoard the flight board on which the current state of the meteor card is activated
     * @throws Exception if an error occurs during the activation process
     */
    public abstract void activate(FlightBoard flightBoard)throws Exception;
}
