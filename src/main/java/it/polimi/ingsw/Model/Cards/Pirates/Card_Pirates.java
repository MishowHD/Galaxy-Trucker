package it.polimi.ingsw.Model.Cards.Pirates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.util.ArrayList;
import java.util.Vector;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.*;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        //@JsonSubTypes.Type(value = Card_Pirates.class, name = "Card_Pirates"),
        //@JsonSubTypes.Type(value = Card_Slavers.class, name = "Card_Slavers"),
        // @JsonSubTypes.Type(value = Card_Smugglers.class, name = "Card_Smugglers")
})

public class Card_Pirates extends Card {
    /**
     * Represents the penalty in terms of days lost if a player successfully defeats the pirates.
     * This value determines the number of days that will be deducted from the player's timeline
     * as a consequence of winning against the card's challenges.
     */
    // Fields
    private final int daysPenalty;  // The penalty in terms of days to be lost if the player wins
    /**
     * Represents the attack strength of the smuggler in the context of a pirate card.
     * This variable determines the threshold of power required by players to defeat the pirates.
     * It is a fixed value used during gameplay to compare against the fire power of players.
     */
    private final int firePower;  // Smuggler's attack strength
    /**
     * Represents the amount of money gained by the player or entity
     * upon interaction with the pirate card. This value reflects
     * the reward collected as a result of the card's effect or outcome.
     */
    private final int moneyGained;
    /**
     * A collection of shots associated with the {@code Card_Pirates} class.
     * This variable stores the details of the shots that can be utilized within the game,
     * such as the shot type (e.g., Meteor, Cannon), rotation, and size.
     * The shots are used to determine various gameplay effects, such as firepower calculations
     * or pirate attack logic during specific game states.
     *
     * This list is immutable once initialized due to its final modifier, ensuring the integrity
     * of the shots data throughout the lifecycle of the card.
     */
    private final ArrayList<Shot> shots;  // Vector to store shots

    /**
     * Constructor for the Card_Pirates class, which initializes the attributes of a pirate card.
     *
     * @param id The unique identifier of the card.
     * @param cardName The name of the card.
     * @param CardLevel The level of the card.
     * @param isTest Indicates whether the card is in test mode.
     * @param daysPenalty The number of penalty days associated with this card.
     * @param firePower The firepower rating of the card.
     * @param moneyGained The amount of money gained from this card.
     * @param shots A collection of Shot objects associated with this card.
     */
    // Constructor
    @JsonCreator
    public Card_Pirates(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int CardLevel,
            @JsonProperty("isTest") boolean isTest,
            @JsonProperty("daysPenalty") int daysPenalty,
            @JsonProperty("firePower") int firePower,
            @JsonProperty("moneyGained") int moneyGained,
            @JsonProperty("shots") Vector<Shot> shots
    ) {
        super(id, cardName, CardLevel, isTest);
        this.daysPenalty = daysPenalty;
        this.firePower = firePower;
        this.moneyGained = moneyGained;
        this.shots = new ArrayList<>(shots);
        State_enum = PIRATES_CALC_STATE;
    }

    /**
     * Retrieves the firepower of the card.
     *
     * @return the firepower value as an integer.
     */
    // Implementing abstract methods from Enemies class
    @Override
    public int getFirePower() {
        return firePower;
    }

    /**
     * Retrieves the number of days of penalty associated with the card.
     *
     * @return the integer value representing the days of penalty.
     */
    @Override
    public int getDaysPenalty() {
        return daysPenalty;
    }


    /**
     * Retrieves the number of crew members lost during the encounter.
     *
     * @return the number of crew members lost as an integer
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Retrieves the amount of money gained.
     *
     * @return the money gained as an integer value.
     */
    public int getMoneyGained() {
        return moneyGained;
    }

    /**
     * Retrieves the list of shots associated with this card.
     *
     * @return An ArrayList containing Shot objects representing the shots.
     */
    // Getter method for shots vector
    public ArrayList<Shot> getShots() {
        return shots;
    }

    /**
     * Activates the effect of the Pirates card on the given flight board.
     * This involves calculating the impact based on the player rank list and
     * updating the state accordingly.
     *
     * @param flightBoard The flight board containing the current state of the game,
     *                    including the player rank list, on which the card's effect is applied.
     * @throws Exception If an error occurs while activating or transitioning states.
     */
    @Override
    public void effect(FlightBoard flightBoard) throws Exception {
        state = new PiratesCalcState(this, flightBoard.getPlayerRankList());

        state.activate(flightBoard);
    }

    /**
     * Changes the current state of the card to the specified state and updates the game's state accordingly.
     * This method differentiates between different state types and handles them appropriately.
     *
     * @param newState The new state to be set for the card. Must be an instance of a class extending {@code Card_State}.
     * @throws Exception If an error occurs while transitioning to the new state.
     */
    // Metodo per cambiare lo stato
    public void setState(Card_State newState) throws Exception {
        this.state = newState;
        if (newState instanceof PiratesCalcState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),PIRATES_CALC_STATE);
        } else if (newState instanceof PiratesWinningState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),PIRATES_WINNING_STATE);
        } else if (newState instanceof PiratesEffectState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),PIRATES_EFFECT_STATE);
        } else if (newState instanceof PiratesEndState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),PIRATES_END_STATE);
        } else if (newState instanceof PiratesLosingState) {
            this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),PIRATES_LOSING_STATE);
        }
    }

    /**
     * Advances the card to the next state by activating the associated state object.
     *
     * @param flightBoard the flight board on which the state transition and activation are applied
     * @throws Exception if an error occurs during state activation
     */
    public void goNextState(FlightBoard flightBoard) throws Exception {
        state.activate(flightBoard);
    }
}