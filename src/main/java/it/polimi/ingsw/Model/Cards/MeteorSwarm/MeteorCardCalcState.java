package it.polimi.ingsw.Model.Cards.MeteorSwarm;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Dice.DiceRoller;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.METEOR_CARD_CALC_STATE;

public class MeteorCardCalcState extends MeteorCardState {
    /**
     * Represents the index of the currently processed shot in a sequence of shots associated
     * with the Meteor Swarm card. This variable is used to track progress through the series
     * of shots during the card's effect execution.
     *
     * Each invocation of the `activate` method in the MeteorCardCalcState class processes
     * the shot at the current index, after which this variable is incremented.
     * When the value of this variable reaches the total number of shots, the card transitions
     * to the next state.
     *
     * Initial value is set to 0, indicating that no shots have been processed.
     */
    private int currentShot = 0;
    /**
     * Represents a list of players who are affected by the effects of a meteor card in the game.
     * This includes all players currently involved in the ongoing meteor swarm event.
     * The affected players are initialized when a meteor card calculation state is created.
     * They are utilized to determine which players are impacted by specific meteor-related actions.
     */
    private final ArrayList<Player> effectedPlayers;
    /**
     * A collection of {@link Shot} objects representing the shots utilized during the Meteor Swarm card's effect.
     *
     * This list contains the specific configuration and properties of the shots (e.g., size, rotation, and type)
     * that determine the behavior and impact of the card in the game. The shots are processed sequentially
     * during the card's activation, affecting the game state based on their individual attributes.
     */
    private final ArrayList<Shot> shots;
    /**
     * Represents the DiceRoller used to handle dice rolling mechanics within the context of the game state.
     *
     * This is a final instance responsible for providing random dice values through its roll method
     * and resetting its state when required. The diceRoller plays a crucial role in determining
     * specific outcomes within the game's meteor swarm mechanics, such as the calculation and
     * execution of shots during the MeteorCardCalcState.
     */
    private final DiceRoller diceRoller;

    /**
     * Constructs a new instance of the {@code MeteorCardCalcState} class,
     * representing a state responsible for calculating the outcomes of a
     * {@code Card_MeteorSwarm} effect. This state processes the interactions
     * between the meteor swarm and the affected players, initializing the
     * relevant data structures and rolling dice for determining effects.
     *
     * @param meteorSwarm       The {@link Card_MeteorSwarm} instance that this state
     *                          is associated with, containing information about the
     *                          meteor shots and card behavior.
     * @param effectedPlayers   An {@link ArrayList} of {@link Player} objects representing
     *                          the players affected by the meteor swarm card in the game.
     * @throws Exception        If any unexpected event occurs during the initialization
     *                          of this state, such as issues with related objects or data.
     */
    public MeteorCardCalcState(Card_MeteorSwarm meteorSwarm, ArrayList<Player> effectedPlayers) throws Exception {
        super(meteorSwarm);
        this.effectedPlayers = effectedPlayers; // All players in the game
        this.shots = meteorSwarm.getShots();

        this.diceRoller = card.getGame().getDice();
        diceRoller.reset();

    }

    /**
     * Activates the current state of the meteor card, handling the sequence of actions
     * such as rolling dice, transitioning states, and managing effects on the players.
     *
     * @param flightBoard the current flight board on which the card actions are executed
     * @throws Exception if an error occurs while transitioning to the next state or during the activation process
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        this.card.setStateENUM(effectedPlayers,METEOR_CARD_CALC_STATE);
        if (currentShot == 0) {
            card.getGame().getEventBus().effectStarted();
        }
        if (currentShot >= shots.size()) {
            meteorSwarm.setState(new MeteorCardEndState(meteorSwarm));
            try {
                meteorSwarm.goNextState(flightBoard);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            int dice = diceRoller.roll();
            System.out.println(dice);
            meteorSwarm.setState(new MeteorCardEffectState(meteorSwarm, this, effectedPlayers, shots, dice, currentShot));
            currentShot++;
            meteorSwarm.goNextState(flightBoard);

        }
    }

    /**
     * Retrieves the next state of the card.
     *
     * This method returns the current state object of the associated `meteorSwarm`.
     * It provides the logic for transitioning or determining the next operational
     * state of the meteor swarm within the context of its lifecycle.
     *
     * @return the next {@code Card_State} representing the current or updated state of the card.
     */
    @Override
    public Card_State getNextState() {
        return meteorSwarm.getState();
    }

}