package it.polimi.ingsw.Model.Cards.AbandonedShip;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.ABANDONED_SHIP_PREPARATION;


public class Card_AbandonedShip extends Card {
    /**
     * Represents the penalty, expressed in flight days, incurred when interacting
     * with an abandoned ship in the game. This penalty negatively affects the player's
     * progress on the flight board by delaying their overall journey.
     *
     * The value of this variable is immutable and defined during the creation of a
     * {@code Card_AbandonedShip} instance. It is used in conjunction with other
     * penalties and rewards to execute the card's effects within specific game states.
     */
    private final int flightDaysPenalty;
    /**
     * Represents the penalty applied to the number of crew members as a consequence
     * of encountering the "Abandoned Ship" card. This penalty reflects the
     * reduction in the crew size due to the effects of the event represented
     * by the card.
     *
     * This variable is immutable and set during the initialization of the
     * {@code Card_AbandonedShip} instance. It is utilized by various game mechanics
     * to calculate the consequences of activating the card.
     */
    private final int crewNumberPenalty;
    /**
     * Represents the monetary reward earned upon successfully resolving actions
     * related to the abandoned ship card. This variable stores the amount
     * of cosmic credits gained when the conditions for rewards are met.
     *
     * The value is immutable and initialized when the card object is constructed.
     * It is utilized in game mechanics to calculate the player's earnings
     * upon activating the abandoned ship's associated effects or transitioning
     * its state to a relevant stage.
     */
    private final int moneyEarned;
    /**
     * The current state of the Abandoned Ship card.
     *
     * This variable represents the dynamic state associated with the abandoned ship card and is updated
     * as the game progresses through various phases of the card's lifecycle. The state determines
     * the behavior and associated actions of the card based on game-specific logic. It transitions
     * between different subclasses of {@code AbandonedShipState}, such as preparation, taken,
     * and final states, depending on player interactions and game events.
     *
     * It is initialized during the creation of the {@code Card_AbandonedShip} instance and can be
     * updated or accessed through the corresponding getter and setter methods.
     */
    private AbandonedShipState state;


    /**
     * Constructor for the Card_AbandonedShip class, representing an abandoned ship card in the game.
     * This card introduces penalties and rewards to the players during its activation phase, such as
     * flight days penalty, crew number penalty, and money earned. The initial state of the card is set
     * to the "AbandonedShipPreparation" phase.
     *
     * @param id the unique identifier of the card.
     * @param cardName the name of the card.
     * @param cardLevel the level of the card.
     * @param isTest a boolean indicating whether this instance is a test card.
     * @param flightDaysPenalty the penalty in terms of flight days associated with the card.
     * @param crewNumberPenalty the penalty in terms of crew members associated with the card.
     * @param moneyEarned the amount of money players can earn from the card.
     * @throws Exception if an error occurs during the card initialization process.
     */
    @JsonCreator
    public Card_AbandonedShip(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int cardLevel,
            @JsonProperty("isTest") boolean isTest,
            @JsonProperty("flightDaysPenalty") int flightDaysPenalty,
            @JsonProperty("crewNumberPenalty") int crewNumberPenalty,
            @JsonProperty("moneyEarned") int moneyEarned
    ) throws Exception {
        super(id, cardName, cardLevel, isTest);
        this.flightDaysPenalty = flightDaysPenalty;
        this.crewNumberPenalty = crewNumberPenalty;
        this.moneyEarned = moneyEarned;
        this.state = new AbandonedShipPreparation(this);
        this.State_enum = ABANDONED_SHIP_PREPARATION;
    }

    /**
     * Retrieves the monetary earnings associated with the abandoned ship card.
     *
     * @return the amount of money gained as an integer.
     */
    @Override
    public int viewMoneyEarning() {
        return moneyEarned;
    }

    /**
     * Retrieves the penalty in terms of flight days associated with the card.
     *
     * @return the number of flight days penalty as an integer.
     */
    @Override
    public int viewFlightPenalty() {
        return flightDaysPenalty;
    }

    /**
     * Returns the penalty applied to the crew members as defined by the card.
     *
     * @return the number of crew members lost or penalized, represented as an integer.
     */
    @Override
    public int viewCrewPenalty() {
        return crewNumberPenalty;
    }

    /**
     * Retrieves the number of crew members lost as a result of the abandoned ship scenario.
     * This method calculates and returns the number of crew members lost, typically
     * as a penalty defined by the card or game state logic.
     *
     * @return the number of crew members lost due to the abandoned ship event, represented as an integer
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Retrieves the amount of money gained as a result of the card's effects.
     *
     * @return the amount of money gained, represented as an integer.
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Executes the effect of the card by activating the current state with the given flight board.
     *
     * @param fb the flight board instance that the card's effect interacts with
     * @throws Exception if an error occurs during activation of the state
     */
    @Override
    public void effect(FlightBoard fb) throws Exception {
        state.activate(fb);
    }

    /**
     * Retrieves the current state of the Abandoned Ship card.
     *
     * @return the current state of type {@code AbandonedShipState}, representing the card's current phase
     *         in the game lifecycle.
     */
    public AbandonedShipState getState() {
        return state;
    }

    /**
     * Transitions the current state of the card to the next phase of the abandoned ship process.
     * The next state is determined by the current state's transition logic.
     * Additionally, this method activates the new state for the given player and position data.
     *
     * @param p the player who is interacting with the card during this state transition
     * @param posPers a nested list of integers representing position and personnel data related to the card's state
     * @throws Exception if issues arise during the state transition or activation process
     */
    public void nextPhase(Player p, ArrayList<ArrayList<Integer>> posPers) throws Exception {
        state = state.getNextState();
        state.Activate(p, posPers);
    }

    /**
     * Sets the current state of the card to the provided AbandonedShipState.
     * This method updates the internal state used to manage the card's lifecycle and behavior.
     *
     * @param state the new state to set for this card. Must be a valid instance of {@code AbandonedShipState}.
     * @throws RuntimeException if the provided state is null or an invalid transition occurs.
     */
    public void setState(AbandonedShipState state) throws RuntimeException {
        this.state = state;
    }


}