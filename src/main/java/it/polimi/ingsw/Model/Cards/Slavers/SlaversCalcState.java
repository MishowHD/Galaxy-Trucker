package it.polimi.ingsw.Model.Cards.Slavers;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SLAVERS_CALC_STATE;


public class SlaversCalcState extends SlaversState {
    /**
     * Tracks the current index of the player being processed in the `playerRank` list.
     *
     * This variable is used to determine which player's turn it is in the current game state.
     * It is incremented to move to the next player after processing the active player's actions.
     */
    private int currentIndex = 0;
    /**
     * A list representing the rank order of players participating in the current state of the game.
     * This list is used to determine the sequence in which players interact with game logic
     * or respond to game events.
     */
    private final ArrayList<Player> playerRank;

    /**
     * Initializes a new instance of the SlaversCalcState class.
     *
     * @param slavers The Card_Slavers object associated with this state.
     * @param playerRank The list of players ranked for the current game context.
     * @throws Exception If an error occurs during initialization.
     */
    public SlaversCalcState(Card_Slavers slavers, ArrayList<Player> playerRank) throws Exception {
        super(slavers);
        this.playerRank = playerRank;

    }


    /**
     * Activates the current state by initializing the game logic and transitioning to
     * the appropriate next state based on the current progress of the players.
     *
     * @param flightBoard the current game board representing the state of the game
     *                    during this phase of execution
     * @throws Exception if an error occurs during the state activation or transition
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        card.setStateENUM(playerRank,SLAVERS_CALC_STATE);
        // Logica iniziale se necessaria
        //card.getGame().getEventBus().effectStarted();
        if (currentIndex < playerRank.size()) {
            card.getGame().getEventBus().effectStarted();
        } else {
            card.setState(new SlaversEndState(card));
        }
    }

    /**
     * Retrieves the next state of the card associated with the current context.
     *
     * @return the next {@code Card_State} representing the current state of the card.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }

    /**
     * Handles the logic for a player to choose positions for cannons and batteries during the game.
     * It validates the player's turn, calculates firepower based on the chosen positions,
     * and transitions the game state depending on the result of the firepower comparison with the card's firepower.
     *
     * @param flightBoard the current flight board of the game, representing the gameplay area
     * @param player the player who is making the choice
     * @param cannonPos the positions of the cannons chosen by the player, represented as a list of coordinates
     * @param batteriesPos the positions of the batteries chosen by the player, represented as a list of coordinates
     * @throws Exception if an error occurs during the execution of the method
     */
    @Override
    public void chooseCannonBatteryPos(FlightBoard flightBoard, Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        if (playerRank.indexOf(player) != currentIndex) { //è il player sbagliato tra quelli che stanno rispondendo
            card.getGame().getEventBus().wrongPlayer(player);
            return;
        }
        if (currentIndex >= playerRank.size()) {
            card.setState(new SlaversEndState(card)); //non ci sono più giocatori
            card.goNextState(flightBoard);
        }
        float playerFirePower = player.getMyShip().getTotalFirePower(cannonPos, batteriesPos, player);
        if (playerFirePower == -1) {
            card.getGame().getEventBus().wrongInput(player);
            return;
        }
        card.getGame().getEventBus().slaversChooseCannonBatteryPos(player, cannonPos, batteriesPos);
        // Confronta con la fire power della carta
        if (playerFirePower > card.getFirePower()) {
            card.getGame().getEventBus().someoneWon(player.getUsername());
            card.setState(new SlaversWinningState(card, player));
            card.goNextState(flightBoard);
        } else if (playerFirePower == card.getFirePower()) {
            card.getGame().getEventBus().tie(player.getUsername());
            currentIndex++;
            //non ci sono più giocatori
        } else {
            card.getGame().getEventBus().lost(player.getUsername());
            currentIndex++;
            card.setState(new SlaversLosingState(card, player, this));
            card.goNextState(flightBoard);
        }
    }
}