package it.polimi.ingsw.Model.Cards.Smugglers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SMUGGLERS_LOST_GOODS_STATE;

public class SmugglersLostGoodsState extends SmugglersState {
    /**
     * Represents the previous state in the Smugglers game flow.
     *
     * This variable holds a reference to the calculated state (`SmugglersCalcState`)
     * that precedes the current `SmugglersLostGoodsState`. It is used to maintain a link
     * between the states, enabling transitions back to or referencing logic
     * executed during the earlier stage.
     *
     * The variable is declared as `final`, ensuring the reference to the previous state
     * remains immutable once assigned. It provides contextual information or logic
     * necessary for managing the transition and interaction between the states
     * in the game's lifecycle.
     */
    private final SmugglersCalcState prevState;
    /**
     * Represents the player currently involved in the state of the game.
     * This attribute is immutable and provides a reference to the specific player
     * for whom the game state is being manipulated or managed.
     *
     * It is utilized in various state-specific actions and operations, ensuring
     * that the state actions are properly associated with the correct player.
     */
    private final Player player;
    /**
     * A list of goods associated with the current state of smuggler activities.
     * This list represents the set of goods that are currently being processed,
     * handled, or referenced within the SmugglersLostGoodsState context.
     * Each item in the list is an instance of the {@link Goods} class, encapsulating
     * specific attributes such as its value and whether it is radioactive.
     *
     * The list is marked as final, ensuring that its reference cannot be changed
     * once initialized. However, the contents of the list can be modified if needed.
     *
     * This variable is primarily used within this state to manage, process, and determine
     * the placement or handling of the goods during gameplay actions.
     */
    private final ArrayList<Goods> goodsListDiPrima;

    /**
     * Constructor for the SmugglersLostGoodsState class, which initializes the state
     * with the specified parameters, updates the card state, and triggers the lost goods event.
     *
     * @param card the card associated with the SmugglersLostGoodsState
     * @param prevState the previous state before transitioning to SmugglersLostGoodsState
     * @param player the player affected by this state
     * @param goodsToPlace the list of goods to be placed during this state
     * @throws Exception if an error occurs during the state initialization
     */
    public SmugglersLostGoodsState(Card card,
                                   SmugglersCalcState prevState,
                                   Player player,
                                   ArrayList<Goods> goodsToPlace) throws Exception {
        super(card);
        this.prevState = prevState;
        this.player = player;
        this.goodsListDiPrima = new ArrayList<>(goodsToPlace);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        card.setStateENUM(players,SMUGGLERS_LOST_GOODS_STATE);
        card.getGame().getEventBus().lostGoods(player);
    }

    /**
     * Activates the current game state by interacting with the specified flight board.
     *
     * @param flightBoard the flight board to be used during the activation of the state
     * @throws Exception if an error occurs while processing the activation
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
    }


    /**
     * Manages the placement of goods on the player's ship during the "Smugglers Lost Goods" phase,
     * ensuring valid input and compliance with game rules. If the movement of goods is valid or no
     * movement is specified, the goods are processed and the game state transitions to the next phase.
     *
     * @param p the player attempting to place goods on their ship.
     * @param posGoods a list of positions indicating where the goods should be placed on the ship, or null for no movement.
     * @param goodsSets a list of sets of goods corresponding to the positions given in posGoods, or null for no movement.
     * @throws Exception if an error occurs during the process.
     */
    // Fix for SmugglersLostGoodsState.chooseWhereToPutGoods
    public void chooseWhereToPutGoods(Player p,
                                      ArrayList<ArrayList<Integer>> posGoods,
                                      ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        boolean noMovement = posGoods == null && goodsSets == null;
        if (!p.equals(player) || p.isBlocked()) {
            card.getGame().getEventBus().wrongPlayer(player);
            return;
        }
        if (!noMovement && (posGoods == null || goodsSets == null)) {
            card.getGame().getEventBus().wrongInput(player);
            return;
        }

        // Create defensive copies to avoid modification during iteration
        ArrayList<Goods> goodsListCopy = new ArrayList<>(goodsListDiPrima);

        if (noMovement || p.getMyShip().checkAndCompareGoodsPosition(
                goodsListCopy, goodsSets, posGoods)) {
            if (!noMovement) {
                ArrayList<Goods> newGoods = new ArrayList<>();
                ArrayList<Goods> goodsListLeftOut = new ArrayList<>(goodsListDiPrima);
                for (ArrayList<Goods> goods : goodsSets) {
                    newGoods.addAll(goods);
                }
                for (Goods g : newGoods) {
                    goodsListLeftOut.remove(g);
                }
                card.getGame().getBank().addGoodsFromList(goodsListLeftOut);

                // Add goods first
                p.getMyShip().addGoods(posGoods, goodsSets);

                // Then notify - using copies to prevent concurrent modification
                ArrayList<ArrayList<Integer>> posGoodsCopy = new ArrayList<>(posGoods);
                ArrayList<ArrayList<Goods>> goodsSetsCopy = new ArrayList<>(goodsSets);

                card.getGame().getEventBus().updateLostGoodsSmug(
                        p, posGoodsCopy, goodsSetsCopy, goodsListCopy);
            }
            // Return to previous state
            card.setState(prevState);
            card.goNextState(p.getMyShip().getFlightBoard());
        } else {
            card.getGame().getEventBus().wrongInput(player);
        }
    }

    /**
     * Retrieves the next state of the card associated with this instance.
     *
     * @return the next state of the card encapsulated within the Card_State object
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}
