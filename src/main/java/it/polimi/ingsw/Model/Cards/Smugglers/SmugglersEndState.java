package it.polimi.ingsw.Model.Cards.Smugglers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SMUGGLERS_END_STATE;

public class SmugglersEndState extends SmugglersState {
    /**
     * Allows a player to choose and place batteries at specified positions on the shipboard.
     * This can be based on the game's logic, positions available, and player actions.
     *
     * @param p                         The {@code Player} object who is performing the action.
     * @param posBatAndNumBattXPos      A 2D {@code ArrayList} containing battery positions and
     *                                  the number of batteries to be placed on each position.
     * @throws RuntimeException         This method is not supported and throws a {@code RuntimeException}.
     */
    @Override
    public void chooseToPlaceBatteries(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the next state of the card.
     *
     * @return the next state of the card, represented as a Card_State.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }

    /**
     * Activates the current state by triggering associated game events such as ending an effect
     * and starting the surrender timer.
     *
     * @param flightBoard the flight board instance associated with the current game state
     * @throws Exception if an error occurs during activation
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        card.getGame().getEventBus().effectEnded();
        card.getGame().getGameState().startSurrenderTimer();
    }

    /**
     * Constructs a new SmugglersEndState object, initializing it with the given smugglers card
     * and setting the state of the card to SMUGGLERS_END_STATE based on the player rank list
     * retrieved from the game's flight board.
     *
     * @param smugglers the smugglers card used to initialize this state
     * @throws Exception if an error occurs while setting the card's state
     */
    public SmugglersEndState(Card smugglers) throws Exception {
        super(smugglers);
        card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),SMUGGLERS_END_STATE);


    }

}
