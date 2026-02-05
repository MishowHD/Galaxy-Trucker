package it.polimi.ingsw.Model.Cards.CombatZone;


import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Dice.DiceRoller;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.METEOR_CARD_CALC_STATE_FROM_COMBAT;

public class MeteorCardCalcStateFROMCOMBAT extends CombatZoneState {
    /**
     * Represents the current index or position of the shot being processed.
     * This variable is used to track and incrementally update the shot index
     * during the activation of specific combat zone and meteor card states.
     *
     * The value of this variable starts at 0 and increments each time a shot
     * is processed, ensuring correct tracking and sequencing of the shots
     * within the gameplay logic.
     */
    private int currentShot = 0;
    /**
     * Represents the player affected during specific game actions or events
     * in the context of the MeteorCardCalcStateFROMCOMBAT system.
     * This variable is immutable and refers to the designated player
     * interacting with or impacted by the state or its associated logic.
     */
    private final Player effectedPlayers;
    /**
     * A collection of shots that represent attacks or impacts within the context of the combat zone.
     * Each shot in the collection is characterized by specific properties such as type, size, and rotation.
     * This collection is used to manage and track the sequence of shots during gameplay, allowing
     * for processing of their effects and resolutions.
     */
    private final ArrayList<Shot> shots;
    /**
     * Represents the specific combat zone card that is the focus of the current
     * state or calculation. This card contains the causes and consequences
     * associated with a combat zone card, as well as the functionalities to manage
     * its states and effects within the game.
     *
     * The card is immutable within the context of the state and serves as a
     * reference to apply state transitions, calculate effects, and evaluate consequences
     * or causes during the game's progression.
     *
     * This variable is finalized to ensure it cannot be reassigned once initialized,
     * providing consistency and a clear link between the state and the associated combat
     * zone card throughout the lifecycle of the state object.
     */
    private final Card_Combat_zone card;
    /**
     * Represents a collection of consequences that need to be processed within the context of the current combat state.
     * Each consequence in the list may define specific effects or penalties such as loss of crew, goods, or other game-related impacts.
     * The list is processed sequentially to handle game events triggered by combat card interactions.
     */
    private final ArrayList<Consequences> remaining; //lista delle conseguenze includa questa
    /**
     * Stores the previous state of the combat zone. It is used for restoring the
     * state of the combat zone in scenarios where a transition between states
     * needs to temporarily branch or return to a prior operational condition.
     *
     * This field plays a critical role in state management for the combat zone,
     * enabling transitions back to an earlier state when required, such as after
     * handling specific game logic or events.
     */
    private CombatZoneState prevState;
    /**
     * Represents a utility for handling dice rolling functionality within the current game state.
     * The DiceRoller is responsible for generating random dice rolls and resetting its state when needed.
     * It is used during the management and activation of specific combat states.
     */
    private DiceRoller diceRoller;


    /**
     * Constructor for the {@code MeteorCardCalcStateFROMCOMBAT} class, which initializes
     * the state for the combat zone where a meteor card's effects are being calculated.
     * It manages transitions between states based on dice rolls and remaining consequences.
     *
     * @param cardc The {@code Card_Combat_zone} object representing the card currently
     *              involved in the combat zone.
     * @param remainingC The {@code ArrayList} of {@code Consequences} that are yet to be processed.
     * @param prevState The previous state of the combat zone, represented as a {@code CombatZoneState}.
     * @param player The {@code Player} object affected by the card's consequences.
     * @param shots The {@code ArrayList} of {@code Shot} objects representing the shooting actions
     *              that need to be resolved.
     * @throws Exception If an error occurs during the initialization or state transition processes.
     */
    public MeteorCardCalcStateFROMCOMBAT(Card_Combat_zone cardc, ArrayList<Consequences> remainingC, CombatZoneState prevState, Player player, ArrayList<Shot> shots) throws Exception {
        super(cardc);
        card = cardc;
        remaining = remainingC;
        this.effectedPlayers = player; //tutti i giocatori del gioco
        this.shots = shots;
        this.prevState = prevState;
        ArrayList<Player> ps=new ArrayList<>();
        ps.add(player);
        card.setStateENUM(ps,METEOR_CARD_CALC_STATE_FROM_COMBAT);
        this.diceRoller = card.getGame().getDice();
        diceRoller.reset();
    }

    /**
     * Activates the current state of the combat zone, determining the effects of the meteor card
     * and transitioning to the appropriate state based on the current game logic.
     *
     * @param flightBoard the flight board representing the current state of the game environment.
     * @throws Exception if an unexpected condition occurs during the activation process.
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        if (shots.isEmpty()) { //se ho gestito tutti i colpi
            if (!remaining.isEmpty()) {
                remaining.removeFirst();//ho tolto la conseguenza
                if (remaining.isEmpty()) {
                    card.setState(new CombatZoneFinal(card, flightBoard));
                } else {
                    //tells about the new state
                    card.setState(prevState);
                    prevState.Activate(flightBoard);
                }
            } else {
                card.setState(new CombatZoneFinal(card, flightBoard));
            }
        } else {
            int dice = diceRoller.roll();
            card.setState(new MeteorCardEffectStateFROMCOMBAT(card, remaining, this, prevState, effectedPlayers, shots, dice, currentShot));
            currentShot++;
            card.getState().activate(flightBoard);


        }
    }


}