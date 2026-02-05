package it.polimi.ingsw.Model.Cards.Planets;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

public class PlanetState extends Card_State {

    /**
     * Constructs a new instance of the PlanetState class.
     *
     * This constructor initializes the state of a planet, typically as a representation
     * of the planet's current behavior or effect in the game's mechanics. The specific
     * attributes and behaviors of the state will vary depending on the context of the
     * subclass and its implementation.
     *
     * This class is intended to act as a base for more specialized planetary states,
     * providing a foundation for implementing game-specific logic.
     */
    public PlanetState() {
    }

    /**
     * Activates the current planetary card state and processes the provided list of goods.
     *
     * This method is intended to perform actions specific to the associated planetary state
     * using the provided list of goods. It processes the goods list as per the game logic.
     * The implementation for this method is currently not supported.
     *
     * @param goodsList the list of {@link Goods} objects to be processed during the activation
     * @throws Exception if an error occurs during activation
     */
    public void Activate(ArrayList<Goods> goodsList) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Activates a specific operation involving a list of players and a flight board.
     *
     * @param players a list of Player objects participating in the operation
     * @param flightBoard the FlightBoard instance associated with the current operation
     * @throws Exception if any error occurs during the activation process
     */
    public void Activate2(ArrayList<Player> players, FlightBoard flightBoard) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Updates the current state of the planet to the provided PlanetState instance.
     * This method allows for the modification of the planet's state, enabling
     * transitions between different states in the game logic. The new state is
     * represented by the PlanetState object passed as a parameter.
     *
     * @param planetState the new state to set for the planet. This parameter represents
     *                    the specific state to which the planet is being transitioned.
     * @throws RuntimeException if the operation is not supported or an error occurs
     *                          while setting the state.
     */
    public void setState(PlanetState planetState) throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Activates the specific behavior or state transition associated with this planetary state.
     * This method is designed to be executed when the planet's current state requires activation
     * of its predefined effects or logic within the game.
     *
     * The specific functionality of this method is expected to be implemented in the derived classes
     * or replaced with meaningful behavior in the context of the game logic. It serves as a protected
     * utility that can be overridden as necessary to define the activation behavior.
     *
     * @throws Exception If an error occurs during activation, such as invalid game state or logic issues.
     */
    protected void Activate() throws Exception {
    }
}
