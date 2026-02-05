package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.COMBAT_ZONE_CHOOSE_GOODS;

public class CombatZoneChooseGoods extends CombatZoneState {
    /**
     * Represents a specific combat zone card associated with the current state of the game.
     * The card embodies the dynamics and metadata of a combat situation, including possible
     * causes and consequences that are triggered by player actions or game events.
     *
     * The `card` serves as an integral component of the combat zone state, maintaining
     * the relationship between the game logic and the associated card. It facilitates
     * updates, state changes, and interactions within the combat zone subsystem.
     *
     * This field is immutable and is initialized when the state is constructed.
     */
    private final Card_Combat_zone card;
    /**
     * A list of consequences that are yet to be processed or resolved within the
     * current game state. This variable plays a key role in managing the sequence
     * of outcomes that occur during the game's events or transitions. Each element
     * in the list represents a specific consequence that impacts the game state,
     * the player, or entities within the game, and it is modified as consequences
     * are dealt with.
     */
    private final ArrayList<Consequences> remaining; //lista delle conseguenze includa questa
    /**
     * Represents the previous state in the combat zone state machine.
     * This variable holds a reference to the CombatZoneState that precedes
     * the current state, enabling transitions back to the previous phase if needed.
     * It is immutable and is set during the initialization of the containing state.
     */
    private final CombatZoneState prevState;
    /**
     * Represents the player associated with this combat zone state.
     * This player is the one interacting with the game during the state where goods
     * are chosen or actions are performed.
     * It is used for validation and tracking player-specific decisions and game progress.
     */
    private final Player player;
    /**
     * Represents a list of goods associated with the initial selection or state in a combat zone scenario.
     * This variable holds the goods that might be processed or managed during the corresponding state lifecycle.
     */
    private ArrayList<Goods> goodsListDiPrima;

    /**
     * Constructs a new instance of the {@code CombatZoneChooseGoods} class, which
     * represents a state where the associated player chooses goods in the combat zone.
     * This state is initialized with the provided combat zone card, the list of remaining
     * consequences, the previous state, and the current player.
     *
     * @param cardC the combat zone card associated with this state
     * @param remaining an {@code ArrayList} of {@code Consequences}, representing the remaining
     *                  consequences to be processed in the combat zone
     * @param prevState the previous {@code CombatZoneState} from which this state transitions
     * @param player the {@code Player} involved in this state
     * @throws Exception if an error occurs during the initialization of the state
     */
    public CombatZoneChooseGoods(Card_Combat_zone cardC, ArrayList<Consequences> remaining, CombatZoneState prevState, Player player) throws Exception {
        super(cardC);
        this.remaining = remaining;
        this.prevState = prevState;
        this.player = player;
        card = cardC;
        ArrayList<Player> ps=new ArrayList<>();
        ps.add(player);
        card.setStateENUM(ps,COMBAT_ZONE_CHOOSE_GOODS);

    }

    /**
     * Activates the current state by initializing goods that were supposed to be managed previously.
     *
     * @param goodsList the list of goods to be activated and managed in the current state.
     */
    public void Activate(ArrayList<Goods> goodsList) {
        goodsListDiPrima = goodsList;
    }

    /**
     * Transitions the game state to the next state based on the current state and remaining consequences.
     * If there are no remaining consequences, transitions to the final combat zone state.
     * Otherwise, it updates the current state and activates it.
     *
     * @param p the player for whom the state transition is being handled
     * @throws Exception if an error occurs during the state transition process
     */
    public void goNextState(Player p) throws Exception {
        if (!remaining.isEmpty()) {
            remaining.removeFirst();//ho tolto la conseguenza
            if (remaining.isEmpty()) {
                card.setState(new CombatZoneFinal(card, p.getMyShip().getFlightBoard()));
            } else {
                //tells about the new state
                card.setState(prevState);
                //changes a middle state
                prevState.Activate(p.getMyShip().getFlightBoard());
            }
        } else {
            card.setState(new CombatZoneFinal(card, p.getMyShip().getFlightBoard()));
        }
    }

    /**
     * Handles the logic for choosing where to place goods on the player's ship.
     *
     * @param p           The player making the move.
     * @param posGoods    A list of positions on the ship where goods should be placed.
     *                    Each element corresponds to a specific set of goods.
     * @param goodsSets   A list of sets of goods being placed on the ship.
     *                    Each set corresponds to a position in posGoods.
     * @throws Exception  If an error occurs during the process of placing goods or transitioning states.
     */
    public void chooseWhereToPutGoods(Player p, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        if (p.equals(player) && !p.isBlocked()) {
            //calcolo che le persone siano giuste e nelle pos giuste
            if (p.getMyShip().checkAndCompareGoodsPosition(goodsListDiPrima, goodsSets, posGoods)) {
                //li posiziono
                p.getMyShip().addGoods(posGoods, goodsSets);
                card.getGame().getEventBus().updateAddGoods(p, posGoods, goodsSets);// update
                goNextState(p);
            } else//se non mi da input giusto allora aspetto
            {//qui update
                card.getGame().getEventBus().wrongInput(p);
            }
        } else {
            card.getGame().getEventBus().notYourTurn(p);
        }
    }

}
