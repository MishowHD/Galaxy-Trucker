package it.polimi.ingsw.Model.Cards.Smugglers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.*;

public class Card_Smugglers extends Card {

    /**
     * Represents the penalty in the number of goods that a player must surrender or lose
     * when interacting with the card under specific game conditions.
     * This value is fixed for the card instance and determines the amount of goods
     * penalized in particular states or events of the game logic.
     */
    private final int goodsPenalty;
    /**
     * Represents the list of goods gained by the card. Each entry in the list corresponds
     * to a specific type of goods with its associated value and properties such as
     * radioactivity. These goods may influence the game's mechanics when the card
     * is activated or utilized.
     */
    private final ArrayList<Goods> goodsGained;
    /**
     * Represents the penalty in days incurred when specific conditions are met
     * during the Smugglers card gameplay.
     *
     * This variable is immutable and indicates the number of days that a player
     * or the game might be penalized as part of the card's effects. It is used
     * in conjunction with the card's states and game flow to determine penalties
     * that apply to players during the gameplay.
     */
    private final int daysPenalty;
    /**
     * Represents the firepower value of the Smuggler card.
     * This value is used to compare against players' firepower during
     * specific game state calculations to determine outcomes such as
     * winning, losing, or tying against the card's challenges.
     *
     * Firepower is compared in contexts such as the SmugglersCalcState
     * to determine the player's success in overcoming the Smuggler card's
     * conditions.
     *
     * Once set, the firepower value cannot be modified as it is declared
     * final.
     */
    private final int firePower;

    /**
     * Constructor for the Card_Smugglers class.
     *
     * @param id the unique identifier of the card
     * @param cardName the name of the card
     * @param CardLevel the level of the card
     * @param isTest a flag indicating whether the card is in test mode
     * @param goodsPenalty the penalty in goods applied by the card
     * @param goodsGained the list of goods gained by the card
     * @param daysPenalty the penalty in days applied by the card
     * @param firePower the firepower value associated with the card
     */
    // Constructor
    @JsonCreator
    public Card_Smugglers(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int CardLevel,
            @JsonProperty("isTest") boolean isTest,
            @JsonProperty("goodsPenalty") int goodsPenalty,
            @JsonProperty("goodsGained") ArrayList<Goods> goodsGained,
            @JsonProperty("daysPenalty") int daysPenalty,
            @JsonProperty("firePower") int firePower
    ) {
        super(id, cardName, CardLevel, isTest);
        this.goodsPenalty = goodsPenalty;
        this.goodsGained = goodsGained;
        this.daysPenalty = daysPenalty;
        this.firePower = firePower;
        State_enum = SMUGGLERS_CALC_STATE;
        // Inizializza direttamente con lo stato di calcolo
    }

    /**
     * Retrieves the firepower value of the card.
     *
     * @return the firepower value as an integer
     */
    @Override
    public int getFirePower() {
        return firePower;
    }

    /**
     * Retrieves the number of penalty days associated with the card.
     *
     * @return the penalty days as an integer.
     */
    @Override
    public int getDaysPenalty() {
        return daysPenalty;
    }

    /**
     * Retrieves the penalty associated with goods for the current smuggler card.
     *
     * @return the penalty value for goods.
     */
    @Override
    public int getGoodsPenalty() {
        return goodsPenalty;
    }

    /**
     * Retrieves the list of goods obtained by the Smugglers card.
     *
     * @return an ArrayList containing the Goods gained by the Smugglers card.
     */
    @Override
    public ArrayList<Goods> getGoods() {
        return goodsGained;
    }

    /**
     * Retrieves the number of crew members lost as a result of the current card's effect.
     *
     * @return the number of crew members lost; always returns 0 in this implementation.
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Retrieves the amount of money gained from this card.
     *
     * @return the amount of money gained as an integer. Returns 0 if no money is gained by default.
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Applies the effect of the Smugglers card on the given flight board.
     * This method initializes the card's state and activates it to influence the game logic.
     *
     * @param flightBoard the flight board representing the current game state and players.
     * @throws Exception if an error occurs during the activation of the card's state.
     */
    @Override
    public void effect(FlightBoard flightBoard) throws Exception {
        state = new SmugglersCalcState(this, flightBoard.getPlayerRankList());
        state.activate(flightBoard);

    }


    /**
     * Updates the state of the card with the specified new state and performs specific actions
     * based on the type of the new state.
     *
     * @param newState The new state of the card. Must be an instance of a specific subclass
     *                 of {@code Card_State}: {@code SmugglersCalcState}, {@code SmugglersWinningState},
     *                 {@code SmugglersLostGoodsState}, {@code SmugglersLostBatteriesState},
     *                 or {@code SmugglersEndState}.
     * @throws Exception If an error occurs during the state transition.
     */
    // Metodo per cambiare lo stato
    public void setState(Card_State newState) throws Exception {
        this.state = newState;
        if (newState instanceof SmugglersCalcState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SMUGGLERS_CALC_STATE);
        } else if (newState instanceof SmugglersWinningState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SMUGGLERS_WINNING_STATE);
        } else if (newState instanceof SmugglersLostGoodsState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SMUGGLERS_LOST_GOODS_STATE);
        } else if (newState instanceof SmugglersLostBatteriesState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SMUGGLERS_LOST_BATTERIES_STATE);
        } else if (newState instanceof SmugglersEndState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SMUGGLERS_END_STATE);
        }
    }


    /**
     * Executes the transition to the next state of the flight board.
     * The current state is activated, triggering its specific behavior on the provided flight board.
     *
     * @param flightBoard the flight board on which the state transition is performed
     * @throws Exception if an error occurs during the state activation
     */
    @Override
    public void goNextState(FlightBoard flightBoard) throws Exception {
        state.activate(flightBoard);
    }
}