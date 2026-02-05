package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.METEOR_CARD_EFFECT_STATE_FROM_COMBAT;

public class MeteorCardEffectStateFROMCOMBAT extends CombatZoneState {
    /**
     * Represents the current calculation state associated with processing the effects
     * of a meteor card within a combat zone. This variable tracks the state transition
     * and computations related to the meteor card during gameplay.
     *
     * The {@code meteorCalcState} is primarily responsible for handling the consequences
     * and actions triggered by the meteor card in the context of the combat zone, such as
     * resolving shots, managing dice rolls, and interacting with players and the game board.
     *
     * It serves as part of the broader game state management system, enabling smooth transitions
     * between different states and maintaining consistency in game logic execution.
     *
     * This field plays a key role in ensuring that the effects of the meteor card are computed
     * and applied correctly within the combat zone gameplay mechanics.
     */
    private MeteorCardCalcStateFROMCOMBAT meteorCalcState;
    /**
     * Represents the player that is affected by the specific meteor card effect.
     * This variable holds a reference to a {@code Player} object and is used within the
     * {@code MeteorCardEffectStateFROMCOMBAT} context to identify and manage the specific
     * player impacted by the current game's meteor effect state.
     */
    private final Player effectedPlayers;
    /**
     * A collection of {@link Shot} objects representing the shots fired or received during the
     * effect of a meteor card in the combat zone. Each shot is an instance of the {@link Shot} class,
     * containing details such as size, rotation, and type.
     *
     * This variable is utilized to store and manage the current state of shots that interact with
     * the game board or players during the meteor card effect sequence. The collection is immutable
     * after initialization to ensure consistency in the simulation of the meteor card's effects.
     */
    private final ArrayList<Shot> shots;
    /**
     * Represents the number of dice being used in the current meteor card effect state.
     * This variable is used to determine or calculate the outcomes related to the effect
     * of meteor cards during a combat zone state.
     */
    int dice;
    /**
     * Represents a specific combat zone card being used in the current state of the
     * Meteor Card Effect operation. This card contains a set of causes and consequences
     * that define its behavior and effects within the combat zone.
     *
     * The `card` variable is immutable and initialized during the creation of
     * a `MeteorCardEffectStateFROMCOMBAT` object. Its state and associated logic
     * influence the sequence of operations performed during meteor-related events
     * and their resolution.
     *
     * It is an instance of the {@link Card_Combat_zone} class that encapsulates
     * specific combat zone card mechanics, including possible triggering conditions
     * (causes) and outcomes (consequences).
     */
    private final Card_Combat_zone card;
    /**
     * Represents a list of consequences that are yet to be resolved or applied.
     * This collection includes the specific effects or penalties resulting from an event or action
     * within the combat zone, such as loss of resources, crew, or additional challenges like incoming shots.
     */
    private final ArrayList<Consequences> remaining; //lista delle conseguenze includa questa
    /**
     * Represents the previous state of a combat zone within the game, maintaining
     * an immutable reference to the prior {@link CombatZoneState}. This field is used for
     * tracking the state transitions and preserving the history of changes made to the
     * combat zone during the gameplay.
     *
     * The {@code prevState} variable allows the current state to access or revert to
     * the earlier combat zone state when needed, ensuring continuity in
     * game logic and behavior.
     *
     * In the context of advanced game mechanics, this field supports features
     * such as state comparison, debugging, or implementation of rollback functionality.
     */
    private final CombatZoneState prevState;
    /**
     * Represents the state of a specific combat zone related to Benin within the game.
     * It is an immutable field that holds the current status and behavior of the combat zone,
     * allowing for interaction and management of the game mechanics and events associated
     * with this specific area.
     *
     * The purpose of this variable is to tie the effects of the combat zone card to
     * the state and logic of the game, enabling smooth transitions and appropriate
     * handling of activates, consequences, and responses within the game's structure.
     *
     * This field is initialized when the corresponding MeteorCardEffectStateFROMCOMBAT
     * object is created and provides essential functionality for card operations.
     */
    private final CombatZoneState combatZoneBeninState;
    /**
     * Holds the current state and configuration of the flight board during the execution
     * of a meteor card effect in combat zone scenarios. Represents the context where
     * meteor-related computations and player actions are carried out.
     *
     * This field is used within the state to manage and interact with the flight board
     * as part of the game's state transitions and meteor-related effects.
     */
    private FlightBoard board;

    /**
     * Constructor for the MeteorCardEffectStateFROMCOMBAT class.
     * Initializes the state with the provided parameters, setting up the necessary
     * values and transitioning the card to the appropriate state.
     *
     * @param cardc The combat zone card associated with this state.
     * @param rem An ArrayList of consequences remaining to be addressed.
     * @param meteorCalcState The previous state of the meteor card calculation.
     * @param combatZoneBeginState The initial state of the combat zone.
     * @param effectedPlayer The player affected by this state.
     * @param shots An ArrayList of shots to consider during this state.
     * @param dice The dice value rolled in the current context.
     * @param currentShot The index of the current shot being processed.
     * @throws Exception If an error occurs during the initialization of this state.
     */
    public MeteorCardEffectStateFROMCOMBAT(Card_Combat_zone cardc, ArrayList<Consequences> rem, MeteorCardCalcStateFROMCOMBAT meteorCalcState, CombatZoneState combatZoneBeginState, Player effectedPlayer, ArrayList<Shot> shots, int dice, int currentShot) throws Exception {
        super(cardc);
        card = cardc;
        prevState = meteorCalcState;
        this.effectedPlayers = effectedPlayer;
        this.shots = shots;
        this.dice = dice;
        remaining = rem;
        this.meteorCalcState = meteorCalcState;
        this.combatZoneBeninState = combatZoneBeginState;
        ArrayList<Player> ps=new ArrayList<>();
        ps.add(effectedPlayer);
        card.setStateENUM(ps,METEOR_CARD_EFFECT_STATE_FROM_COMBAT);
        //faccio vedere cose nuove

    }


    /**
     * Activates the current state by updating the dice value on the event bus.
     *
     * @param flightBoard the current flight board where the state is being activated
     * @throws Exception if an error occurs during activation
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        //System.out.println("losing state activated");
        card.getGame().getEventBus().updateDice(dice);
    }

    /**
     * Manages how the player chooses to defend against incoming meteors
     * by utilizing cannons, shields, and batteries based on player input.
     *
     * @param player the player who is currently facing the meteors.
     * @param howToDefenceFromShots a list of integers representing the player's selected
     *        defenses (e.g., positions or types of defenses such as cannons or shields).
     * @param flightBoard the current state of the flight board associated with the game.
     * @throws Exception if there is an error in processing the player's defense choices
     *         (e.g., invalid input, unexpected game state, or other issues).
     */
    //sono 4 xyzw per cannone o scudo e batteria
    public void chooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, FlightBoard flightBoard) throws Exception {
        board = flightBoard;
        if (effectedPlayers.equals(player)) {
            if (player.getMyShip().checkCorrectDefence(howToDefenceFromShots)) {
                //System.out.println("Dice effective: "+dice);
                player.getMyShip().manageShotReceived(shots.getFirst(), howToDefenceFromShots, dice);
                //update
                card.getGame().getEventBus().updateManageShotReceived(player, shots.getFirst(), howToDefenceFromShots, dice);

                if(howToDefenceFromShots!=null) {
                    ArrayList<ArrayList<Integer>> BatteriesToActivate = new ArrayList<>();
                    ArrayList<Integer> pos=new ArrayList<>();
                    pos.add(howToDefenceFromShots.get(2));
                    pos.add(howToDefenceFromShots.get(3));
                    pos.add(1);
                    BatteriesToActivate.add(pos);
                    card.getGame().getEventBus().lostBatteries(player, BatteriesToActivate);
                }
                if (player.getMyShip().findConnectedBlocks().size() > 1) {
                    card.setState(new combat_fixing_state(card, this, player, flightBoard, player.getMyShip().findConnectedBlocks()));
                } else goNextState(player);


            } else {
                card.getGame().getEventBus().wrongInput(player);
            }

        } else {

            card.getGame().getEventBus().notYourTurn(player);
        }
    }


    /**
     * Advances the game to the next state based on the player's actions and the current system conditions.
     * It determines whether to progress to a new phase of the Combat Zone, reset to a previous state,
     * or finalize the current sequence of effects.
     *
     * @param p the {@link Player} who is involved in the current game state transition.
     *          The player's actions and state are critical in determining the next state.
     * @throws Exception if an error occurs during the state transition. This includes scenarios
     *                   where invalid conditions or unexpected game states are encountered.
     */
    public void goNextState(Player p) throws Exception {
        if (shots.isEmpty()) {//ho finito di sparare
            if (!remaining.isEmpty()) {
                remaining.removeFirst();//ho tolto la conseguenza
                if (remaining.isEmpty()) {
                    card.setState(new CombatZoneFinal(card, p.getMyShip().getFlightBoard()));
                } else {
                    //check if there are more than 1 connected blocks
                    //if so it means player should choose one subship
                    //tells about the new state
                    card.setState(combatZoneBeninState);
                    //changes a middle state
                    //card.getGame().getEventBus().updateStatus(stateEnum.MIDDLE_EFFECT_ENDED);
                    combatZoneBeninState.Activate(p.getMyShip().getFlightBoard());
                }
            } else {
                card.setState(new CombatZoneFinal(card, p.getMyShip().getFlightBoard()));
            }
        } else {
            shots.removeFirst();
            card.setState(prevState);
            card.getState().activate(board);

        }
    }
}