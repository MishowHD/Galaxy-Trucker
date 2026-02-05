package it.polimi.ingsw.Model.Cards.Slavers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.*;

public class Card_Slavers extends Card {

    /**
     * Represents the penalty in terms of the quantity of goods that will be lost
     * if the player fails to overcome the challenge presented by this card.
     * This variable plays a critical role in determining the consequences of a failed action
     * while interacting with the card's effects.
     */
    private final int goodsPenalty;  // The penalty in terms of goods to be lost if the player loses
    /**
     * Represents the collection of goods that the player gains upon winning a specific scenario or challenge.
     * This field is immutable and is initialized at the creation of the object containing it.
     */
    private final ArrayList<Goods> goodsGained;  // The goods gained if the player wins
    /**
     * Represents the penalty in terms of days a player will lose if they win a specific encounter,
     * such as in the context of a game card effect or event resolution.
     * The value of this variable dictates the duration of the penalty in days,
     * impacting the player's progress or status within the game.
     */
    private final int daysPenalty;  // The penalty in terms of days to be lost if the player wins
    /**
     * Represents the attack strength of the Slavers in the game.
     * This value is used to compare against the player's firepower during specific encounters.
     * A higher firePower indicates a stronger attack capability of the Slavers.
     * This variable is final, meaning its value is immutable after initialization.
     */
    private final int firePower;  // Slaver's attack strength
    /**
     * Represents the number of crew members lost during a specific event or interaction
     * involving the Slavers card in the game.
     *
     * This value determines the penalty during certain game states or transitions, where
     * players might be required to lose the specified number of crew members from their ship
     * as part of the event's effect.
     */
    private final int crewLost;
    /**
     * Represents the monetary reward or profit that the player acquires
     * as a consequence of interacting with the "Slavers" card.
     *
     * This variable records the amount of money earned, playing a
     * pivotal role in determining the outcomes or benefits gained
     * during the gameplay events governed by this card.
     *
     * It is initialized through the constructor of the Card_Slavers class
     * and remains immutable throughout the lifecycle of the object.
     */
    private final int moneyGained;


    /**
     * Constructs a Card_Slavers object with specific penalties, rewards, and attributes.
     *
     * @param id The unique identifier of the card.
     * @param cardName The name of the card.
     * @param CardLevel The level of the card.
     * @param isTest A boolean indicating if the card is used for testing purposes.
     * @param goodsPenalty The penalty in terms of goods associated with this card.
     * @param goodsGained A list of goods gained when interacting with this card.
     * @param daysPenalty The penalty in terms of days associated with this card.
     * @param firePower The firepower required or represented by this card.
     * @param crewLost The number of crew members lost associated with this card.
     * @param moneyGained The amount of money gained when interacting with this card.
     */
    // Constructor
    @JsonCreator
    public Card_Slavers(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int CardLevel,
            @JsonProperty("isTest") boolean isTest,
            @JsonProperty("goodsPenalty") int goodsPenalty,
            @JsonProperty("goodsGained") ArrayList<Goods> goodsGained,
            @JsonProperty("daysPenalty") int daysPenalty,
            @JsonProperty("firePower") int firePower,
            @JsonProperty("crewLost") int crewLost,
            @JsonProperty("moneyGained") int moneyGained
    ) {
        super(id, cardName, CardLevel, isTest);

        this.goodsPenalty = goodsPenalty;
        this.goodsGained = goodsGained;
        this.daysPenalty = daysPenalty;
        this.firePower = firePower;
        this.crewLost = crewLost;
        this.moneyGained = moneyGained;
        State_enum = SLAVERS_CALC_STATE;
    }

    /**
     * Retrieves the firepower value associated with the card.
     *
     * @return the firepower value of the card as an integer.
     */
    @Override
    public int getFirePower() {
        return firePower;
    }

    /**
     * Retrieves the penalty in days associated with the card.
     *
     * @return the number of days as a penalty for an action.
     */
    @Override
    public int getDaysPenalty() {
        return daysPenalty;
    }

    /**
     * Retrieves the number of crew members lost during the encounter with the slavers.
     *
     * @return the number of crew members lost.
     */
    // Getter methods for Slavers class
    @Override
    public int getCrewLost() {
        return crewLost;
    }

    /**
     * Retrieves the amount of money gained associated with this card.
     *
     * @return the amount of money gained as an integer
     */
    @Override
    public int getMoneyGained() {
        return moneyGained;
    }

    /**
     * Activates the effect of the "Slavers" card by updating the game state
     * and performing the associated actions on the provided flight board.
     *
     * @param flightBoard the current game flight board containing player-related
     *                    information and game state to be modified by the card's effect
     * @throws Exception if an error occurs while activating the card's effect or changing the game state
     */
    @Override
    public void effect(FlightBoard flightBoard) throws Exception {
        state = new SlaversCalcState(this, flightBoard.getPlayerRankList());
        state.activate(flightBoard);

    }

    /**
     * Changes the state of the card to a new state and updates related systems accordingly.
     * Depending on the specific type of the new state, it transitions to the appropriate state enumeration
     * in the game system, notifying relevant game components and players.
     *
     * @param newState the new state to set for the card. It must be an instance of a subclass of Card_State.
     *                 Supported states include SlaversCalcState, SlaversLosingState, SlaversWinningState, and SlaversEndState.
     * @throws Exception if an error occurs during the state transition or if the state update fails.
     */
    // Metodo per cambiare lo stato
    public void setState(Card_State newState) throws Exception {
        this.state = newState;
        if (newState instanceof SlaversCalcState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SLAVERS_CALC_STATE);
        } else if (newState instanceof SlaversLosingState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SLAVERS_LOSING_STATE);
        } else if (newState instanceof SlaversWinningState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SLAVERS_WINNING_STATE);
        } else if (newState instanceof SlaversEndState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),SLAVERS_END_STATE);
        }
    }

    /**
     * Transitions the current card to its next state by activating the provided flightBoard instance.
     *
     * @param flightBoard the context on which the transition to the next state should be performed
     * @throws Exception if an error occurs during the state activation
     */
    @Override
    public void goNextState(FlightBoard flightBoard) throws Exception {
        state.activate(flightBoard);
    }

}