package it.polimi.ingsw.Model.Cards.Planets;

import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PLANET_STATE_ADD_GOODS;

public class PlanetStateAddGoods extends PlanetState {
    /**
     * Represents the planet card associated with the current state in the game.
     * This variable holds an instance of {@link Card_PlanetCard}, which encapsulates
     * specific attributes and behaviors related to the card. The card's state,
     * penalties, and interactions with other game elements (e.g., players, goods, or
     * the game board) are managed through this reference.
     *
     * In the context of {@link PlanetStateAddGoods}, this card determines the rules
     * and actions for adding goods to a player's inventory or managing planetary resources.
     */
    private final Card_PlanetCard card;
    /**
     * Represents the player associated with the current state in the game.
     * This variable is a reference to the instance of the {@code Player} class
     * that is interacting with the planet state in the context of adding goods.
     *
     * The player stored in this variable is typically used for tracking and managing
     * their specific actions, validations, or turn-related interactions during
     * the process of adding goods to their ship or managing game state transitions.
     *
     * This variable is immutable and is initialized when the {@code PlanetStateAddGoods}
     * object is created.
     */
    private final Player P;
    /**
     * Represents the previous or base state of the planet to which the current state may transition.
     *
     * This variable holds a reference to a {@link PlanetState} object that defines the prior behavior
     * or setup of the planet. It is used during state transitions or to revert the planet back to its
     * previous state after specific operations are completed.
     *
     * The {@code status} variable is initialized during the construction of the enclosing class
     * and remains immutable to ensure consistency and thread safety.
     */
    private final PlanetState status;
    /**
     * Represents a list of {@link Goods} used to temporarily hold goods
     * within the state of the {@link PlanetStateAddGoods} class. This list
     * is primarily used to manage and process goods during the transition or
     * activation phases of the game logic.
     *
     * The variable is managed internally by the {@link PlanetStateAddGoods} methods
     * to store and track goods related to player or game actions, such as allocating
     * goods or determining their positions.
     */
    private ArrayList<Goods> goodsListDiPrima;

    /**
     * Constructs a new instance of the PlanetStateAddGoods class.
     *
     * This constructor initializes a new state for adding goods to the planet. It associates
     * the given planetary state, planet card, and player to configure the specific context
     * of this state. The state transition logic and interaction with the game entities
     * are handled accordingly.
     *
     * @param stato the current planetary state to be transitioned from
     * @param card the card representing the planet to which this state is applied
     * @param player the player who is interacting with the planet in this state
     */
    public PlanetStateAddGoods(PlanetState stato, Card_PlanetCard card, Player player) {
        super();
        this.card = card;
        this.P = player;
        this.status = stato;

    }

    /**
     * Activates the current state by assigning the provided goods list and updating the
     * state of the associated card and players.
     *
     * @param goodsList the list of goods to be processed during activation
     * @throws Exception if an error occurs during activation
     */
    public void Activate(ArrayList<Goods> goodsList) throws Exception {
        goodsListDiPrima = goodsList;
        System.out.println("Now players should gain their planets!");
        ArrayList<Player> players = new ArrayList<>();
        players.add(P);
        card.setStateENUM(players,PLANET_STATE_ADD_GOODS);

    }

    /**
     * Handles the process of deciding where to place goods for a player during their turn.
     * This method performs checks to validate the positioning of the goods
     * and updates the game state accordingly.
     *
     * @param p the player attempting to place goods
     * @param posGoods a list of lists representing the positions where the goods will be placed
     * @param goodsSets a list of lists representing the sets of goods to be placed at the specified positions
     * @throws Exception if an error occurs during the process, such as invalid input or game state issues
     */
    public void chooseWhereToPutGoods(Player p, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        if (P.equals(p) && !P.isBlocked()) {
            if (p.getMyShip().checkAndCompareGoodsPosition(goodsListDiPrima, goodsSets, posGoods)) {
                ArrayList<Goods> newGoods = new ArrayList<>();
                ArrayList<Goods> goodsListLeftOut = new ArrayList<>(goodsListDiPrima);
                if(goodsSets!=null) {
                    for (ArrayList<Goods> goods : goodsSets) {
                        newGoods.addAll(goods);
                    }
                    for (Goods g : newGoods) {
                        goodsListLeftOut.remove(g);
                    }
                    card.getGame().getBank().addGoodsFromList(goodsListLeftOut);

                }else{
                    card.getGame().getBank().addGoodsFromList(goodsListLeftOut);
                }
                System.out.println("Goods are ok so I can deposit them");
                p.getMyShip().addGoods(posGoods, goodsSets);
                card.getGame().getEventBus().updateAddGoods(p, posGoods, goodsSets);
                card.setState(status);
                card.getState().Activate();
            } else {
                card.getGame().getEventBus().wrongInput(p);
            }
        } else {
            card.getGame().getEventBus().notYourTurn(p);
        }
    }

}
