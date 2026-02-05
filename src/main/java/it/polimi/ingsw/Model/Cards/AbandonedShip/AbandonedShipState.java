package it.polimi.ingsw.Model.Cards.AbandonedShip;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

public class AbandonedShipState extends Card_State {

    /**
     * Activates the effect of the Abandoned Ship card for the specified player.
     * This method is expected to perform a series of actions involving the player
     * and their ship, based on the provided positional information and game logic.
     *
     * @param player the player for whom the card effect is activated
     * @param posPers a nested list containing positional information regarding affected passengers/actions
     * @throws Exception if the activation process fails or encounters an issue
     */
    public void Activate(Player player, ArrayList<ArrayList<Integer>> posPers) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }


    /**
     * Transitions the current state to the next logical state in the card lifecycle.
     *
     * @return the next state, which is an instance of {@code AbandonedShipState}.
     * @throws Exception if an error occurs during state transition.
     */
    public AbandonedShipState getNextState() throws Exception {
        throw new RuntimeException("Not supported yet.");
    }
}
