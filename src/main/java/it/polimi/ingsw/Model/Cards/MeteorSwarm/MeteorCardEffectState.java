package it.polimi.ingsw.Model.Cards.MeteorSwarm;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.METEOR_CARD_EFFECT_STATE;

public class MeteorCardEffectState extends MeteorCardState {
    /**
     * Represents the calculation state of the Meteor Swarm card effect within the context of the game.
     *
     * This variable holds a reference to a {@link MeteorCardCalcState} instance which is responsible
     * for processing the computation and transition-related mechanics of the Meteor Swarm card. It
     * encapsulates data and methods required to manage the current sequence of shots, dice rolls,
     * affected players, and other game mechanics specific to this state.
     *
     * The {@code meteorCalcState} is a key component in controlling the flow of the Meteor Swarm card's
     * gameplay impact, ensuring that each shot and its associated effect are executed systematically
     * before transitioning to subsequent states in the card's lifecycle.
     *
     * Being declared as {@code final}, it is immutable once initialized and serves as a consistent
     * reference throughout the lifecycle of the {@link MeteorCardEffectState}.
     */
    private final MeteorCardCalcState meteorCalcState;
    /**
     * Represents the index of the shot currently being processed or executed during the
     * Meteor Swarm card effect. This variable is used to track progress through the
     * sequence of shots associated with the card.
     *
     * The value starts from 0 and increments as the shots are iterated. It ensures
     * that the shots are applied in a specific order and provides information on
     * which shot is currently in effect within the game's logic.
     *
     * This variable is immutable, meaning that its value is assigned during the
     * instantiation of the containing object and remains constant thereafter.
     */
    private final int currentShot;
    /**
     * A collection storing the players who are still actively participating in the current game state.
     *
     * This list is used to manage the remaining players affected by the meteor card effect,
     * allowing the class to track which players are still in the game after specific interactions
     * or processing steps have occurred.
     */
    private final ArrayList<Player> remainingPlayers = new ArrayList<>();
    /**
     * A collection of {@link Shot} objects representing the shots currently active in the context of the game state.
     *
     * This list is used to track and process the specific properties and effects of each shot
     * such as their type (e.g., meteor, cannon), size, and rotation. It plays a crucial role
     * in determining the interactions and outcomes related to the MeteorCardEffectState.
     *
     * The contents of this list are essential for managing the game's logic involving
     * meteor swarm effects and any associated player actions or defenses.
     */
    private final ArrayList<Shot> shots;
    /**
     * Represents the number of dice used during the current state of a meteor card effect.
     *
     * This variable defines the quantity of dice available or required for performing
     * specific operations within the meteor card state, such as resolving effects or
     * calculating outcomes based on the meteor swarm interactions.
     */
    int dice;

    /**
     * Constructs a new instance of MeteorCardEffectState, representing the effect state of the Meteor Swarm card.
     * This state handles the interactions, dice updates, and associated events related to the card's effect.
     *
     * @param meteorSwarm       The {@link Card_MeteorSwarm} object associated with this effect state.
     * @param meteorCalcState   The {@link MeteorCardCalcState} representing the calculation state preceding this effect state.
     * @param effectedPlayers   A list of {@link Player} objects who are affected by the card effect.
     * @param shots             A list of {@link Shot} objects representing the meteor shots associated with the card.
     * @param dice              The number of dice to be used for this effect state.
     * @param currentShot       An integer indicating the current shot index in the sequence of meteor shots.
     * @throws Exception        If there is an issue initializing the effect state.
     */
    public MeteorCardEffectState(Card_MeteorSwarm meteorSwarm, MeteorCardCalcState meteorCalcState, ArrayList<Player> effectedPlayers, ArrayList<Shot> shots, int dice, int currentShot) throws Exception {
        super(meteorSwarm);
        this.meteorCalcState = meteorCalcState;
        this.shots = shots;
        this.dice = dice;
        this.currentShot = currentShot;
        this.remainingPlayers.addAll(effectedPlayers);
        this.card.setStateENUM(effectedPlayers,METEOR_CARD_EFFECT_STATE);
        card.getGame().getEventBus().effectStarted();
        card.getGame().getEventBus().updateDice(dice);
    }

    /**
     * Activates the MeteorCardEffectState for the provided flight board.
     * Handles the initiation of meteor card effects, such as triggering
     * the associated events or updating game elements.
     *
     * @param flightBoard the flight board representing the current state of the game
     *                    on which the meteor card effect is activated
     * @throws Exception if an error occurs while activating the meteor card effect
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
//        card.getGame().getEventBus().effectStarted();
//        card.getGame().getEventBus().updateDice(dice);
    }

    /**
     * Continues the meteor card effect state by updating the state of the card and activating
     * the meteor calculation state if there are no remaining players.
     *
     * @param flightBoard the current flight board context in which the effect is processed
     * @throws Exception if an error occurs during the state transition or activation
     */
    public void Continue(FlightBoard flightBoard) throws Exception {
        this.card.setStateENUM(remainingPlayers,METEOR_CARD_EFFECT_STATE);
        if (remainingPlayers.isEmpty()) {

            meteorSwarm.setState(meteorCalcState);
            meteorCalcState.activate(flightBoard);
        }
    }

    /**
     * Handles the logic to determine how the specified player chooses to confront meteors during a meteor event.
     * This includes verifying the defense method, managing shots received, and triggering subsequent game state changes.
     *
     * @param player The player taking the action of choosing a way to face the meteors.
     * @param howToDefenceFromShots A list of integers representing the player's selected defenses against the incoming meteors.
     * @param flightBoard The flight board where the current game state and actions occur.
     * @throws Exception If an unexpected event occurs during the execution of this method.
     */
    @Override
    public void chooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, FlightBoard flightBoard) throws Exception {
        if (remainingPlayers.contains(player)) {
            if (player.getMyShip().checkCorrectDefence(howToDefenceFromShots)) {

                //card.getGame().getEventBus().meteorCardChooseHowToFaceMeteors(player, howToDefenceFromShots, shots, dice, currentShot);
                if(howToDefenceFromShots!=null) {
                    ArrayList<ArrayList<Integer>> BatteriesToActivate = new ArrayList<>();
                    ArrayList<Integer> pos=new ArrayList<>();
                    pos.add(howToDefenceFromShots.get(2));
                    pos.add(howToDefenceFromShots.get(3));
                    pos.add(1);
                    BatteriesToActivate.add(pos);
                    card.getGame().getEventBus().lostBatteries(player, BatteriesToActivate);
                }
                player.getMyShip().manageShotReceived(shots.get(currentShot), howToDefenceFromShots, dice);
                remainingPlayers.remove(player);
                if (player.getMyShip().findConnectedBlocks().size() > 1) {
                    card.setState(new meteor_fixing_state(card, this, player, flightBoard, player.getMyShip().findConnectedBlocks()));
                } else {
                    Continue(flightBoard);
                }

            } else {
                card.getGame().getEventBus().wrongInput(player);
            }

        }else{
            card.getGame().getEventBus().wrongPlayer(player);
        }


    }

    /**
     * Returns the next state of the card effect.
     *
     * @return the next {@code Card_State} that corresponds to the meteor calculation state.
     */
    @Override
    public Card_State getNextState() {
        return meteorCalcState;
    }


}