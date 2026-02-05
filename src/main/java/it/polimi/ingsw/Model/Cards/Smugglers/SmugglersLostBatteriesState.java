package it.polimi.ingsw.Model.Cards.Smugglers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SMUGGLERS_LOST_BATTERIES_STATE;

public class SmugglersLostBatteriesState extends SmugglersState {
    /**
     * Represents the previous state of the Smugglers calculation in the game's state machine.
     * This variable is used to keep a reference to the state from which the current
     * {@code SmugglersLostBatteriesState} was transitioned.
     *
     * The {@code prevState} is expected to provide context or functionality that might
     * aid in executing or resolving specific logic within the current state. It is
     * immutable and set during the initialization of the {@code SmugglersLostBatteriesState}.
     */
    private final SmugglersCalcState prevState;
    /**
     * Represents the player associated with the current state of the game.
     * The player interacts with the game through various actions and is
     * directly affected by the rules determined in this state.
     * This field is immutable once set, ensuring a consistent reference
     * to the player throughout the state.
     */
    private final Player player;
    /**
     * Represents the number of batteries a player must surrender or lose
     * during a specific game state associated with Smugglers mechanics.
     * This value is fixed and immutable, determining the quantity of batteries
     * involved in the penalty when transitioning through certain states.
     */
    private final int numbatt;

    /**
     * Constructor for creating a new state representing a scenario where the Smugglers card
     * causes a loss of batteries for a player in the game. It initializes the state and
     * updates the game and card to reflect the new state.
     *
     * @param card the card associated with this state
     * @param prevState the previous state of the smugglers card before this transition
     * @param player the player affected by the loss of batteries
     * @param numbattoloose the number of batteries to be reduced from the player's total
     * @throws Exception if there is an error during state initialization or interaction with game mechanisms
     */
    public SmugglersLostBatteriesState(Card card, SmugglersCalcState prevState, Player player, int numbattoloose) throws Exception {
        super(card);
        this.prevState = prevState;
        this.player = player;
        this.numbatt = numbattoloose;
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        card.setStateENUM(players,SMUGGLERS_LOST_BATTERIES_STATE);
        card.getGame().getEventBus().updateBatteriesRemaining(player, numbatt);


    }

    /**
     * Activates the current state, performing actions related to the loss of batteries
     * and updating the game state accordingly.
     *
     * @param flightBoard the current flight board instance that manages game data
     *                    and coordinates game state changes.
     * @throws Exception if any error occurs during the activation process.
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        //faccio vedere cose nuove
        //card.getGame().getEventBus().sendPenalty(numbatt, "batteries");
    }

    /**
     * Manages the placement of batteries for a player during the game. This method determines
     * if the requested battery placements are valid and updates the game state accordingly.
     *
     * @param p the player attempting to place batteries
     * @param posBatAndNumBattXPos a list of multiple arrays, where each array contains the x-coordinate,
     *                             y-coordinate, and the quantity of batteries to place at that position
     * @throws Exception if the input validation fails or an error occurs during state transitions
     */
    @Override
    public void chooseToPlaceBatteries(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception {
        System.out.println("[DEBUG] chooseToPlaceBatteries invoked for player: " + p);
        if (posBatAndNumBattXPos != null) {
            System.out.println("[DEBUG] Received positions list size = " + posBatAndNumBattXPos.size() + ", numbatt = " + numbatt);
            if (p.equals(player) && !p.isBlocked()) {
                // Log each requested placement
                for (int idx = 0; idx < posBatAndNumBattXPos.size(); idx++) {
                    ArrayList<Integer> entry = posBatAndNumBattXPos.get(idx);
                    System.out.printf("[DEBUG] Entry %d -> x=%d, y=%d, qty=%d%n", idx, entry.get(0), entry.get(1), entry.get(2));
                }

                if (p.getMyShip().checkCorrectBatteriesDistribution(posBatAndNumBattXPos, numbatt)) {
                    System.out.println("[DEBUG] Battery distribution check passed.");
                    card.getGame().getEventBus().updateLostBatteriesSmug(p, posBatAndNumBattXPos, numbatt);
                    p.getMyShip().assertBatteryPos(posBatAndNumBattXPos);
                    // Restore the previous calc state
                    card.setState(prevState);
                    card.getState().activate(player.getMyShip().getFlightBoard());
                } else {
                    System.err.println("[ERROR] checkCorrectBatteriesDistribution returned false. Wrong input.");
                    card.getGame().getEventBus().wrongInput(player);
                }
            } else {
                System.err.println("[ERROR] Wrong player or player is blocked: " + p);
                card.getGame().getEventBus().wrongPlayer(player);
            }
        } else {
            System.err.println("[ERROR] posBatAndNumBattXPos is null.");
            card.getGame().getEventBus().wrongInput(player);
        }
    }

    /**
     * Retrieves the next state of the card associated with this instance.
     *
     * @return the next state of the card, represented as a Card_State object
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}
