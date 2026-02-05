package it.polimi.ingsw.Model.Cards.Pirates;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PIRATES_EFFECT_STATE;

public class PiratesEffectState extends PiratesState {
    /**
     * An immutable reference to the current state where pirates are in a losing condition.
     * This variable is part of the PiratesEffectState and represents the PiratesLosingState,
     * which dictates the behavior or effects to be processed during this state.
     * It holds essential information about the current progression related to the players
     * identified as losing during a specific interaction or phase involving pirates.
     */
    private final PiratesLosingState piratesLosingState;
    /**
     * Represents the index of the current shot being evaluated or processed in the context of
     * the game state. This variable tracks the progression through a sequence of shots during
     * game mechanics such as pirate attacks or defenses.
     *
     * The value of this variable is initialized when the {@code PiratesEffectState} instance
     * is created and remains constant during its lifecycle due to the {@code final} modifier.
     */
    private final int currentShot;
    /**
     * A list containing the players that are still active in the game.
     * This collection is used to track players who have not surrendered
     * or been otherwise eliminated, and are continuing to participate in the current game state.
     */
    private final ArrayList<Player> remainingPlayers;
    /**
     * Represents the list of shots that are part of the current state in the game.
     * Each shot is encapsulated as an instance of the {@link Shot} class, which contains details
     * such as its size, rotation, and type (e.g., Meteor or Cannon).
     * This list may be used to track, update, or process the shots associated with the PiratesEffectState.
     */
    private final ArrayList<Shot> shots;
    /**
     * Represents the number of dice available for rolling in the current state
     * of the PiratesEffectState. This variable determines the number of attempts
     * or potential outcomes that can be used by players to resolve the effects
     * or challenges encountered during this state.
     *
     * The dice value is specifically related to the game logic for pirates and
     * may influence decision-making, outcomes, and state transitions within the
     * gameplay.
     */
    int dice;

    /**
     * Constructor for the PiratesEffectState class.
     *
     * @param pirates The Card object representing the pirate card that triggers this state.
     * @param piratesLosingState The PiratesLosingState object representing the state when players have to defend against shots.
     * @param effectedPlayers The list of players affected by the pirates' attack.
     * @param shots The list of shots fired by the pirates.
     * @param dice The result of the dice roll determining the impact of the shots.
     * @param currentShot The index of the current shot being processed.
     * @throws Exception If any error occurs during the initialization of the state.
     */
    public PiratesEffectState(Card pirates, PiratesLosingState piratesLosingState, ArrayList<Player> effectedPlayers, ArrayList<Shot> shots, int dice, int currentShot) throws Exception {
        super(pirates);
        this.piratesLosingState = piratesLosingState;
        this.shots = shots;
        this.dice = dice;
        this.remainingPlayers = new ArrayList<>(effectedPlayers);
        this.currentShot = currentShot;
        card.setStateENUM(effectedPlayers,PIRATES_EFFECT_STATE);
    }

    /**
     * Executes the continuation logic for the current Pirate effect state.
     *
     * This method updates the state of the card and transitions to the next
     * state if all players have responded.
     *
     * @param flightBoard the current flight board on which the game state is managed
     * @throws Exception if an error occurs during state transition or card state update
     */
    @Override
    public void Continue(FlightBoard flightBoard) throws Exception {
        card.setStateENUM(remainingPlayers,PIRATES_EFFECT_STATE);
        if (remainingPlayers.isEmpty()) { //se hanno risposto tutti vado via
            card.setState(piratesLosingState);
            card.goNextState(flightBoard);
        }
    }

    /**
     * Activates the pirates effect state on the given flight board.
     *
     * @param flightBoard the flight board on which the pirates effect state is activated
     */
    @Override
    public void activate(FlightBoard flightBoard) {
    }

    /**
     * Determines how a specific player should respond to meteors by analyzing their defense strategy
     * and their ship's current state. Updates the game state accordingly, including handling improper
     * inputs or invalid actions.
     *
     * @param player the player who is deciding how to face the meteors
     * @param howToDefenceFromShots a list of integers detailing the player's defense strategy against the shots
     * @param fb the flight board associated with the game and the player's ship
     * @throws Exception if an error occurs during game state transitions or if inputs are invalid
     */
    public void chooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, FlightBoard fb) throws Exception {
        if (remainingPlayers.contains(player)) {
            if (player.getMyShip().checkCorrectDefence(howToDefenceFromShots)) {
                //card.getGame().getEventBus().piratesChooseHowToFaceMeteors(player, howToDefenceFromShots, shots.get(currentShot), dice);
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
                    card.setState(new pirates_fixing_state(card, this, player, player.getMyShip().getFlightBoard(), player.getMyShip().findConnectedBlocks()));
                } else {
                    Continue(fb);
                }
            } else {
                card.getGame().getEventBus().wrongInput(player);
            }
        }else{
            card.getGame().getEventBus().wrongPlayer(player);
        }
    }

    /**
     * Retrieves the next state of the card.
     *
     * @return the next Card_State of the card, based on its current state.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}