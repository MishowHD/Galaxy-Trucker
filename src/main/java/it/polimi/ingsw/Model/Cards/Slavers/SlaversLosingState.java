package it.polimi.ingsw.Model.Cards.Slavers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SLAVERS_LOSING_STATE;

public class SlaversLosingState extends SlaversState {
    /**
     * Represents the player who is identified as the loser in the current state of the Slavers game.
     * This player is subjected to penalties or specific actions as dictated by the state logic.
     */
    private final Player loser;
    /**
     * Represents the state to transition to after completing the current losing state logic
     * for the Slavers game scenario. This variable holds a reference to an instance of
     * {@code SlaversCalcState} that determines the subsequent game flow.
     *
     * It is used to manage the state progression by invoking appropriate methods and logic
     * defined within the {@code SlaversCalcState} class when the current state concludes.
     */
    private final SlaversCalcState slaversCalcState;
    /**
     * Represents the current state of the FlightBoard in the context of the
     * SlaversLosingState. This variable is primarily used to reference the
     * FlightBoard during gameplay state transitions and event handling processes.
     * It is set during the activation of this state and is utilized in subsequent
     * operations that require access to the FlightBoard instance.
     */
    private FlightBoard fb;

    /**
     * Constructs a new SlaversLosingState instance, representing the state where the specified player loses
     * due to slavers, and initializes the necessary components.
     *
     * @param slavers the card representing the slavers event
     * @param loser the player who is impacted by the slavers and is considered the loser in this state
     * @param slaversCalcState the next state of the slavers card for further calculations and operations
     * @throws Exception if there are issues initializing the object or setting the state
     */
    public SlaversLosingState(Card slavers, Player loser, SlaversCalcState slaversCalcState) throws Exception {
        super(slavers);
        this.loser = loser;
        this.slaversCalcState = slaversCalcState;
        ArrayList<Player> players = new ArrayList<>();
        players.add(loser);
        card.setStateENUM(players,SLAVERS_LOSING_STATE);
    }

    /**
     * Activates the current state by processing the losing player's passengers and
     * transitioning to the next calculation state if necessary.
     *
     * @param flightBoard the flight board object associated with the current game state
     *                     and gameplay session.
     * @throws Exception if an issue occurs during state activation or passenger processing.
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        fb = flightBoard;
        int crewLost = card.getCrewLost();
        // numero di passeggeri attualmente a bordo
        int currentPassengers = loser.getMyShip().getTotalPassengers();
        if (currentPassengers <= crewLost) {
            ArrayList<ArrayList<Integer>> allTiles = loser.getMyShip().getAllPassengerTiles();
            card.getGame().getEventBus().slaversChoosePassengersToLose(loser, true, allTiles);
            // rimozione effettiva
            loser.getMyShip().removePassengers(allTiles, crewLost);
            // passo allo stato di calcolo successivo
            card.setState(slaversCalcState);
            card.getState().activate(flightBoard);
        }
    }


    /**
     * Handles the process of selecting passengers to lose during the slavers' losing state.
     * Validates the input, ensures the correct player is making the call, and manages the transition
     * to the next game state after passengers are removed.
     *
     * @param player the player attempting to choose passengers to lose, must be the designated loser.
     * @param confirmY a boolean indicating whether the action is confirmed by the player.
     * @param tiles a list of tile coordinates specifying the passengers to be removed.
     * @throws Exception if any errors occur during state transitions or input validation.
     */
    public void choosePassengersToLose(Player player, boolean confirmY, ArrayList<ArrayList<Integer>> tiles) throws Exception {
        // 1) Only the loser may call this
        if (!player.equals(loser)) {
            card.getGame().getEventBus().wrongPlayer(player);
            return;
        }
        // 2) Validate EXACTLY tot removals from valid, non-duplicate cabins
        boolean valid = player.getMyShip().CheckCorrectPeopleToLoose(tiles, card.getCrewLost());
        if (!valid) {
            card.getGame().getEventBus().wrongInput(player);
            return;
        }
        // 3) All good: notify, remove, advance state
        card.getGame().getEventBus().slaversChoosePassengersToLose(player, confirmY, tiles);
        player.getMyShip().removePassengers(tiles, card.getCrewLost());
        card.setState(slaversCalcState);
        card.getState().activate(fb);
    }

    /**
     * Retrieves the next state of the card in the game.
     *
     * @return the next {@code Card_State} of the card.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}