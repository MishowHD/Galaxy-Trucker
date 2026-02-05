package it.polimi.ingsw.Model.Cards.Pirates;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Dice.DiceRoller;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PIRATES_LOSING_STATE;

public class PiratesLosingState extends PiratesState {
    /**
     * Tracks the index of the current shot being executed in the game state.
     * This variable is incremented as each shot is processed in the game logic.
     */
    private int currentShot = 0;
    /**
     * A list of {@link Shot} objects representing the shots associated with a specific state
     * of the Pirates game. Each shot contains details such as its size, rotation, and type.
     * This collection is initialized based on the state of the associated card in the game.
     * It is used to determine the sequence and properties of actions during gameplay.
     */
    private final ArrayList<Shot> shots;
    /**
     * Represents a list of players who have been defeated or are in a losing state
     * in the context of the current game or game phase. This list is used to track
     * and manage actions or state transitions involving players who are no longer
     * participating actively or have failed specific conditions.
     *
     * The list may serve operational purposes in determining game flow, applying
     * game effects, or transitioning the game state appropriately based on the
     * status of the players within it.
     */
    private final ArrayList<Player> losers;
    /**
     * A final instance of the DiceRoller interface used to manage dice-related actions
     * such as rolling the dice and resetting its state within the context of the
     * PiratesLosingState. This diceRoller is tightly coupled with the game's logic
     * and is required to determine outcomes during the execution of game states.
     */
    private final DiceRoller diceRoller;

    /**
     * Constructs the PiratesLosingState, setting up the state for handling pirate losers and shots,
     * initializing the dice roller, and resetting it.
     *
     * @param pirates the card representing the pirates
     * @param losers the list of players who are marked as losers in this state
     * @throws Exception if an error occurs during state initialization
     */
    public PiratesLosingState(Card pirates, ArrayList<Player> losers) throws Exception {
        super(pirates);
        this.losers = losers;
        this.shots = pirates.getShots();
        card.setStateENUM(losers,PIRATES_LOSING_STATE);
        this.diceRoller = card.getGame().getDice();
        diceRoller.reset();
    }

    /**
     * Activates the current state by performing necessary state transitions based on the current status
     * of the game, the shot count, and the losers list.
     * If no losers are present or all shots have been processed, the state transitions to PiratesEndState.
     * Otherwise, rolls a dice, updates the necessary game components, and sets the next state.
     *
     * @param flightBoard the current flight board on which actions and state transitions are executed
     * @throws Exception if an error occurs during state activation or transition
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        if (losers == null || losers.isEmpty()) {
            card.setState(new PiratesEndState(card));
            card.goNextState(flightBoard);
        } else {
            if (currentShot >= shots.size()) {
                card.setState(new PiratesEndState(card));
                card.goNextState(flightBoard);
            } else {
                int dice = diceRoller.roll();
                card.getGame().getEventBus().updateDice(dice);
                card.setState(new PiratesEffectState(card, this, losers, shots, dice, currentShot));
                currentShot++;
                card.goNextState(flightBoard);
            }
        }
    }

    /**
     * Retrieves the next state of the card in the game.
     *
     * @return the next state of the card represented as a {@code Card_State}.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }

}
