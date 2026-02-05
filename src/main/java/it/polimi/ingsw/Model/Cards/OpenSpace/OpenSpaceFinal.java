package it.polimi.ingsw.Model.Cards.OpenSpace;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.OPEN_SPACE_FINAL;

public class OpenSpaceFinal extends OpenSpaceState {


    /**
     * Constructs a new OpenSpaceFinal instance and initializes its state within the associated game.
     * This constructor updates the card's state to OPEN_SPACE_FINAL and starts the surrender timer
     * for the game.
     *
     * @param openSpace the card representing an open space that triggers this state.
     * @throws Exception if there is an issue setting the card state or starting the surrender timer.
     */
    public OpenSpaceFinal(Card openSpace) throws Exception {
        super(openSpace);
        this.card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),OPEN_SPACE_FINAL);
        card.getGame().getGameState().startSurrenderTimer();

    }

    /**
     * Activates the specified flight board with specific configurations or
     * actions defined by the OpenSpaceFinal state.
     *
     * @param border the flight board to be activated, representing the current
     *               game state or environment for modification.
     */
    public void activate(FlightBoard border) {

    }
}
