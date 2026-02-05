package it.polimi.ingsw.Model.Cards.AbandonedShip;

import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.*;

public class AbandonedShipPreparation extends AbandonedShipState {
    /**
     * Represents the specific abandoned ship card associated with the current game state.
     * This card plays a central role in managing and transitioning between the various
     * phases of the abandoned ship scenario, and it encapsulates the penalties and
     * rewards related to the abandoned ship event.
     *
     * The `card` is immutable and is initialized by the class constructor.
     * It provides functionality for setting and transitioning the game state based
     * on the effects of the abandoned ship card.
     */
    private final Card_AbandonedShip card;
    /**
     * A list of players participating in the abandoned ship preparation phase.
     * This list is used to manage and track the players involved in the state
     * transitions and actions related to the abandoned ship scenario.
     */
    private ArrayList<Player> players;

    /**
     * Constructor for the AbandonedShipPreparation class, initializing the necessary card
     * and preparing the player list for the abandoned ship scenario.
     *
     * @param card the Card_AbandonedShip instance representing the abandoned ship card providing
     *             contextual information and behavior during this state.
     * @throws Exception if an error occurs during the initialization of the abandoned ship preparation state.
     */
    public AbandonedShipPreparation(Card_AbandonedShip card) throws Exception {
        this.card = card;
        players = new ArrayList<>();

        }

    /**
     * Activates the current state by initializing the players and setting the card state.
     *
     * @param flightBoard the flight board containing the player rank list required for activation
     * @throws Exception if an error occurs during the activation process
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        players.addAll(flightBoard.getPlayerRankList());
        card.setStateENUM(players,ABANDONED_SHIP_PREPARATION);


    }

    /**
     * Manages the process of selecting passengers to lose during the abandoned ship phase.
     * Depending on the player's actions or game state, this method validates the passenger selection,
     * transitions to the next game phase, or updates the player turn.
     *
     * @param p The player attempting to make a move during the abandoned ship phase.
     * @param yOn A boolean indicating whether the player intends to specify passengers to lose or opt out.
     * @param pass A 2D list of integers representing the IDs of the passengers the player intends to lose.
     * @throws Exception If an error occurs during the execution of the method.
     */
    public void choosePassengersToLose(Player p, boolean yOn, ArrayList<ArrayList<Integer>> pass) throws Exception {//da cambiare il nome
        if (p.equals(players.getFirst()) && !p.isBlocked()) {
            if (yOn) {
                if (p.getMyShip().CheckCorrectPeopleToLoose(pass, card.viewCrewPenalty())) {
                    card.nextPhase(p, pass);
                } else {
                    card.getGame().getEventBus().wrongInput(p);
                }
            } else {
                players.remove(p);
                if (players.isEmpty()) {
                    card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),ABANDONED_FINAL_STATE);
                    card.getGame().getGameState().startSurrenderTimer();
                } else
                    card.getGame().getEventBus().nextPlayerTurn();
            }
        } else {

            card.getGame().getEventBus().notYourTurn(p);
        }
    }

    /**
     * Transitions the current state to the next logical state in the card life cycle.
     *
     * @return the next state, which is an instance of {@code AbandonedShipTaken}.
     * @throws Exception if an error occurs during state transition.
     */
    public AbandonedShipState getNextState() throws Exception {
        return new AbandonedShipTaken(card);
    }


}

