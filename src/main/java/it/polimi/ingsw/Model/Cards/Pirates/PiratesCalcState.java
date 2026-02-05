package it.polimi.ingsw.Model.Cards.Pirates;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PIRATES_CALC_STATE;

public class PiratesCalcState extends PiratesState {
    /**
     * Represents the index of the current player or state in the context of the
     * game. This variable is used to track and manage the progression through
     * players or states during the gameplay.
     *
     * It is initialized to 0 and can change dynamically as the game progresses,
     * depending on its state or interactions.
     */
    private int currentIndex = 0;
    /**
     * A list of Player objects representing the players who have been defeated
     * during the current game state. This list is used to keep track of
     * the eliminated players for game progression and logic implementation.
     */
    private final ArrayList<Player> defeatedPlayers;
    /**
     * A list representing the ranking of players during the gameplay.
     * It stores Player objects in the order of their current standings.
     * This data structure is immutable and ensures that the player ranking
     * remains consistent throughout the game state.
     */
    private final ArrayList<Player> playerRank;
    /**
     * Indicates whether any player has won during the current state or phase of the game.
     * This variable is used to track if a victory condition has been achieved and can
     * influence the progression of the game states or the end of the game.
     *
     * Default value is {@code false}, reflecting that no player has won by default
     * until the condition is explicitly updated during gameplay.
     */
    boolean someoneWon = false;

    /**
     * Constructs a new PiratesCalcState object, initializing it with the provided Pirates card
     * and list of players' rankings. This state is responsible for managing the calculation
     * phase of the Pirates card's effect in the game.
     *
     * @param pirates The instance of the {@code Card_Pirates} associated with this state.
     *                This card determines the context and attributes for the current state.
     * @param playerRank An {@code ArrayList} of {@code Player} objects representing the
     *                   rankings of the players in the current game. This list determines
     *                   the order of evaluation during the Pirates calculation phase.
     * @throws Exception If an error occurs during the creation of the state, such as issues
     *                   in the super implementation or state initialization logic.
     */
    public PiratesCalcState(Card_Pirates pirates, ArrayList<Player> playerRank) throws Exception {
        super(pirates);
        defeatedPlayers = new ArrayList<>();
        this.playerRank = playerRank;

    }


    /**
     * Retrieves the next state of the card.
     *
     * @return the next state of the card as a {@code Card_State} object.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }

    /**
     * Activates the current state logic for the PiratesCalcState.
     *
     * @param flightBoard the game board on which the state is activated, containing relevant game data
     * @throws Exception if an error occurs during the activation of the state
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        card.setStateENUM(playerRank,PIRATES_CALC_STATE);
        // Logica iniziale se necessaria

        if (someoneWon || currentIndex >= playerRank.size()) {//non ci sono più giocatori

            card.setState(new PiratesLosingState(card, defeatedPlayers));
            card.goNextState(flightBoard);
        }
    }

    /**
     * Allows a player to choose the positions of cannons and batteries for their ship during gameplay.
     * The method verifies the current player's turn, computes their firepower based on the chosen positions,
     * and evaluates the result against the card's firepower. Depending on the outcome, the player may win, lose,
     * or trigger a tie event.
     *
     * @param flightBoard the flight board representing the current state of the game board
     * @param player the player currently taking their turn
     * @param cannonPos a list of positions chosen for cannons
     * @param batteriesPos a list of positions chosen for batteries
     * @throws Exception if an unexpected error occurs during the method execution
     */
    public void chooseCannonBatteryPos(FlightBoard flightBoard, Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        // Verifica che il giocatore corrente sia quello giusto
        if (someoneWon || currentIndex >= playerRank.size()) {//non ci sono più giocatori

            card.setState(new PiratesLosingState(card, defeatedPlayers));
            card.goNextState(player.getMyShip().getFlightBoard());
        }
        if (playerRank.indexOf(player) != currentIndex) {
            card.getGame().getEventBus().wrongPlayer(player);
            return;
        }
        // Ottiene la fire power del giocatore, che è già quello giusto
        float playerFirePower = player.getMyShip().getTotalFirePower(cannonPos, batteriesPos, player);
        if (playerFirePower == -1) {
            card.getGame().getEventBus().wrongInput(player);
            return;
        }
        card.getGame().getEventBus().piratesChooseCannonBatteryPos(player, cannonPos, batteriesPos);
        // Confronta con la fire power della carta
        if (playerFirePower > card.getFirePower()) {
            card.getGame().getEventBus().someoneWon(player.getUsername());
            someoneWon = true;
            card.setState(new PiratesWinningState(card, player, this));
            card.goNextState(player.getMyShip().getFlightBoard());
        } else if (playerFirePower == card.getFirePower()) {
            card.getGame().getEventBus().tie(player.getUsername());
            currentIndex++;
            //check for state
            activate(flightBoard);
        } else {
            card.getGame().getEventBus().lost(player.getUsername());
            defeatedPlayers.add(player); //teoricamente aggiunge in coda
            currentIndex++;
        }
        if (currentIndex >= playerRank.size()) {

            card.setState(new PiratesLosingState(card, defeatedPlayers));
            card.goNextState(player.getMyShip().getFlightBoard());
        }
    }
}