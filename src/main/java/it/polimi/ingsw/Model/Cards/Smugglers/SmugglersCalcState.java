package it.polimi.ingsw.Model.Cards.Smugglers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SMUGGLERS_CALC_STATE;

public class SmugglersCalcState extends SmugglersState {
    /**
     * Represents the current index used to track or reference the position of
     * an element within a list, array, or a sequence of items in the game's state logic.
     *
     * This variable is utilized during the calculations or state transitions
     * within the SmugglersCalcState, providing functionality related to
     * iterating or selecting specific elements.
     *
     * Initial value is set to 0 and can be updated based on the flow of the game.
     */
    private int currentIndex = 0;
    /**
     * Represents the current ranking of players within the game.
     * This list stores players in a specific order, typically representing their rank
     * based on game criteria such as score or progress.
     *
     * The list is immutable after instantiation, as it is declared final, ensuring
     * that the reference to the list cannot be reassigned. However, the contents of
     * the list itself may still be modifiable unless explicitly handled outside of this class.
     */
    private final ArrayList<Player> playerRank;

    /**
     * Constructor for the SmugglersCalcState class, initializing the state with the given smugglers card
     * and the player ranking list. Inherits functionality from the SmugglersState superclass.
     *
     * @param smugglers The smugglers card associated with this state.
     * @param playerRank An ArrayList representing the ranking of players to be considered in this state.
     * @throws Exception If there is an issue initializing the state.
     */
    public SmugglersCalcState(Card smugglers, ArrayList<Player> playerRank) throws Exception {
        super(smugglers);
        this.playerRank = playerRank;

    }

    /**
     * Activates the smugglers calculation state, updating the card's state based on the player rankings
     * and transitioning to the next state if all players have been processed.
     *
     * @param flightBoard the current game flight board containing game state and player information
     * @throws Exception if an error occurs during state activation or state transition
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        this.card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),SMUGGLERS_CALC_STATE);
        if (currentIndex >= playerRank.size()) {
            card.setState(new SmugglersEndState(card));
            card.goNextState(flightBoard);
        }

    }

    /**
     * Evaluates and handles the player's choice of cannon and battery positions during a smuggling-related game state.
     * Based on the player's decisions, triggers corresponding updates in game states, event communication, and validations.
     *
     * @param flightBoard the current state of the flight board used during gameplay.
     * @param player the player making the choice of cannon and battery positions.
     * @param cannonPos a list of lists representing the positions of the chosen cannons.
     * @param batteriesPos a list of lists representing the positions of the chosen batteries.
     * @throws Exception if an error occurs during the game state update or input validation.
     */
    @Override
    public void chooseCannonBatteryPos(FlightBoard flightBoard,
                                       Player player,
                                       ArrayList<ArrayList<Integer>> cannonPos,
                                       ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        // 1) Controlli iniziali
        if (playerRank.indexOf(player) != currentIndex) {
            card.getGame().getEventBus().wrongPlayer(player);
            return;
        }
        if (currentIndex >= playerRank.size()) {
            card.setState(new SmugglersEndState(card));
            card.goNextState(flightBoard);
            return;
        }
        if (!player.getMyShip().checkCorrectCannonBatteries(cannonPos, batteriesPos, player)
                || player.isBlocked()) {
            card.getGame().getEventBus().wrongInput(player);
            return;
        }

        // 2) Calcolo fire power
        float playerFirePower = player.getMyShip().getTotalFirePower(cannonPos, batteriesPos, player);
        if (playerFirePower < 0) {
            card.getGame().getEventBus().wrongInput(player);
            return;
        }
        card.getGame().getEventBus().updateSmugglersCalc(player, cannonPos, batteriesPos);

        // 3) Confronto con la carta
        float cardFP = card.getFirePower();
        if (playerFirePower > cardFP) {
            currentIndex++;
            card.getGame().getEventBus().someoneWon(player.getUsername());
            card.setState(new SmugglersWinningState(card, player));
            card.goNextState(flightBoard);


        } else if (playerFirePower == cardFP) {
            card.getGame().getEventBus().tie(player.getUsername());
            currentIndex++;
            if (currentIndex >= playerRank.size()) {
                card.setState(new SmugglersEndState(card));
                card.goNextState(flightBoard);
            }

        } else {
            card.getGame().getEventBus().lost(player.getUsername());
            int remainingAfterPenalty =
                    player.getMyShip().getMySortedGoods().size() - card.getGoodsPenalty();

            if (remainingAfterPenalty > 0) {
                // Rimuovo i goods e preparo lo stato di posizionamento
                // Crea una copia difensiva dei beni rimasti per evitare modifiche esterne
                ArrayList<Goods> goodsToPlace = new ArrayList<>(player.getMyShip().looseOrAddGoods(null, card.getGoodsPenalty()));
                currentIndex++;
                SmugglersLostGoodsState lostState =
                        new SmugglersLostGoodsState(card, this, player, goodsToPlace);
                card.setState(lostState);
                // Crea una copia della lista prima di passarla all'EventBus
                card.getGame().getEventBus().updateGoodsRemaining(player, new ArrayList<>(goodsToPlace));

                card.goNextState(flightBoard);

            } else if (remainingAfterPenalty == 0) {
                player.getMyShip().looseAllGoods();
                currentIndex++;
                activate(flightBoard);

            } else {
                // tolgo tutti i goods e poi batterie
                player.getMyShip().looseAllGoods();
                int battNum = player.getMyShip().getBatteriesNumber();
                int toPlace = battNum + remainingAfterPenalty; // remainingAfterPenalty è negativo

                player.getMyShip().looseAllBatteris();
                if (toPlace > 0) {
                    currentIndex++;
                    SmugglersLostBatteriesState batState =
                            new SmugglersLostBatteriesState(card, this, player, toPlace);

                    card.setState(batState);
                    card.goNextState(flightBoard);

                }else{
                    currentIndex++;
                    activate(flightBoard);
                }

            }

            // Passo al prossimo giocatore (o termine)
           // currentIndex++;

        }
    }

    /**
     * Returns the next state of the card that this state represents.
     *
     * @return the next {@code Card_State} associated with the card.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}
