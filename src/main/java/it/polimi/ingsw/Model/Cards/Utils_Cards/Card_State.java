package it.polimi.ingsw.Model.Cards.Utils_Cards;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Card_State implements Serializable {
    /**
     * Represents a specific card that serves as the current state within the game.
     * This variable holds a reference to a card object and plays a central role in managing the game state.
     */
    protected Card card;

    /**
     * Retrieves the next state of the current card.
     *
     * @return the next state of the card, which in this implementation,
     *         returns the current state itself.
     * @throws Exception if an error occurs during the state transition.
     */
    public Card_State getNextState() throws Exception {
        return this;
    }

    /**
     * Activates a specific behavior or state based on the current game context.
     *
     * @param players   the list of players participating in the game
     * @param indices   the list of indices relevant to the activation logic
     * @param flightBoard the current state of the flight board in the game
     */
    public void activate(ArrayList<Player> players, ArrayList<Integer> indices, FlightBoard flightBoard) {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Activates a specific card state within the context of the flight board and players.
     *
     * @param p the list of players involved in the activation process
     * @param c the list of integers representing specific contextual data for activation
     * @param flightBoard the flight board on which the activation is applied
     */
    public void Activate(ArrayList<Player> p, ArrayList<Integer> c, FlightBoard flightBoard) {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Activates a process involving the provided list of goods.
     *
     * @param goods the list of goods to be processed during activation
     * @throws Exception if an error occurs during activation
     */
    public void Activate(ArrayList<Goods> goods) throws Exception {
    }

    /**
     * Activates the specified flight board.
     *
     * @param fb the flight board to be activated
     * @throws Exception if the activation process is not supported or fails
     */
    public void Activate(FlightBoard fb) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Activates the current state of the card using the provided flight board.
     *
     * @param flightBoard the FlightBoard object used during the activation process
     * @throws Exception if the activation is not supported or cannot be performed
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows a player to choose positions for cannon and battery placements on their flight board.
     * This method ensures the action is performed only during the player's turn.
     *
     * @param flightBoard the flight board associated with the current game session
     * @param player the player who is performing the action
     * @param cannonPos a list of positions selected for placing cannons
     * @param batteriesPos a list of positions selected for placing batteries
     * @throws Exception if the action is attempted outside of the player's turn
     */
    public void chooseCannonBatteryPos(FlightBoard flightBoard, Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        player.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(player);
    }

    /**
     * Advances the current state of the card in the state management process.
     * The specific implementation of this transition is not yet supported and will throw a runtime exception when invoked.
     * This method is intended to be overridden by subclasses or implemented as part of the card state transition logic.
     *
     * @throws RuntimeException always, as the current implementation does not support transitioning to the next state.
     */
    public void goNextState() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Transitions the current state of the card to the next state for the given player.
     *
     * @param player the player for whom the state transition is being performed
     * @throws Exception if an error occurs during the state transition
     */
    public void goNextState(Player player) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Allows the player to determine how to defend against approaching meteors using available defenses.
     *
     * @param player The player making the decision on how to face the meteors.
     * @param howToDefenceFromShots A list representing options or strategies for defending against meteor shots.
     * @param flightBoard The current state of the flight board, which may influence the decision-making process.
     * @throws Exception If an error occurs during the execution of this method.
     */
    public void chooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, FlightBoard flightBoard) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Handles the action of a player deciding whether to land on a specific planet.
     *
     * @param p        The player attempting to land on the planet.
     * @param yOn      A boolean indicating the player's agreement or decision to land.
     * @param NumPlanet The identifier of the planet the player is attempting to land on.
     * @throws Exception If the operation cannot be completed, such as when it is not the player's turn.
     */
    public void acceptToLandOnAPlanet(Player p, boolean yOn, int NumPlanet) throws Exception {
        p.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(p);
    }

    /**
     * Allows the player to decide whether to claim a reward during the game.
     * This method interacts with the player's current game state and the game board
     * to manage the decision-making process.
     *
     * @param flightBoard The current flight board of the game, representing the central game state.
     * @param yOn A boolean indicating the player's choice or condition for claiming the reward.
     * @param player The player making the decision to claim the reward.
     * @param storageTiles A 2D list representing storage tile positions or states that may influence the process.
     * @param newGoods A 2D list representing the new goods being introduced or transferred during this action.
     * @throws Exception If the action cannot be completed or causes an unexpected game state error.
     */
    public void chooseToClaimReward(FlightBoard flightBoard, boolean yOn, Player player, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods) throws Exception {
        player.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(player);
    }

    /**
     * Handles the process where a player chooses to claim a reward.
     * Verifies if it is the player's turn before allowing the action.
     *
     * @param yOn a boolean indicating whether the reward claim option is enabled or not for the player.
     * @param player the player attempting to claim the reward.
     * @throws Exception if it is not the player's turn or any other issue occurs during the operation.
     */
    public void chooseToClaimReward(boolean yOn, Player player) throws Exception {
        player.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(player);
    }

    /**
     * Handles the logic for allowing a player to choose where to place goods on the game board.
     *
     * @param player The player making the decision about where to place the goods.
     * @param posGoods A list of positions where goods can be placed, represented as indices.
     * @param goodsSets A list of sets of goods available for placement.
     * @throws Exception If an invalid action is attempted or the player is not allowed to take a turn.
     */
    public void chooseWhereToPutGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        player.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(player);
    }

    /**
     * Allows the player to choose an abandoned station. This method involves updating the player's
     * game state and interacting with the flight board and other provided game data.
     *
     * @param player        The player making the choice.
     * @param flightBoard   The flight board for the current game where the action takes place.
     * @param yOn           A boolean flag that may indicate a specific game condition or choice.
     * @param storageTiles  A nested list representing storage tile positions or configurations.
     * @param newGoods      A nested list of goods that may be associated with the abandoned station choice.
     * @throws Exception    If the action is invalid or cannot be completed due to game rules.
     */
    public void chooseAbandonedStation(Player player, FlightBoard flightBoard, boolean yOn, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods) throws Exception {
        player.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(player);
    }

    /**
     * Allows the player to initiate firepower actions during the game. This includes
     * handling the provided cannon batteries and activating certain components
     * for firing.
     *
     * @param p The Player object representing the player who is performing
     *          the action.
     * @param DoubFireTriplets An ArrayList containing lists of integers which
     *                         represent triplets indicating double firepower
     *                         configurations.
     * @param BatteriesToAct An ArrayList containing lists of integers which
     *                        represent positions of batteries that need to
     *                        be activated for firing.
     * @throws Exception If the action cannot be performed due to game state constraints
     *                   or invalid input.
     */
    public void chooseToStartFirePower(Player p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct) throws Exception {
        p.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(p);
    }

    /**
     * Allows a player to choose actions for starting their motor based on the given positions of engines and batteries.
     *
     * @param player The player attempting to start the motor.
     * @param flightBoard The flight board associated with the player's ship.
     * @param enginesPos A list of positions for the engines that can be used by the player.
     * @param batteriesPos A list of positions for the batteries that can be used by the player.
     * @throws Exception If the action cannot be performed, such as when it is not the player's turn.
     */
    public void chooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        player.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(player);
    }

    /**
     * Allows a player to choose the positions to place batteries on the flight board.
     *
     * @param p The player making the selection.
     * @param posBatAndNumBattXPos A 2D list where each inner list contains battery positions and the number of batteries per position.
     * @throws Exception If the action is not allowed or an error occurs related to game state.
     */
    public void chooseToPlaceBatteries(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception {
        p.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(p);
    }

    /**
     * Handles the process in which a player chooses passengers to lose.
     *
     * @param p The player making the selection.
     * @param yOn A boolean indicating if a specific condition is active during the selection process.
     * @param pass A list of lists where each inner list represents groups of passengers on the player's ship.
     * @throws Exception If the action is attempted when it is not the player's turn or if any other error occurs during the process.
     */
    public void choosePassengersToLose(Player p, boolean yOn, ArrayList<ArrayList<Integer>> pass) throws Exception {

        p.getMyShip().getFlightBoard().getMygame().getEventBus().notYourTurn(p);
    }

    /**
     * This method continues the current state or operation
     * within the provided flight board context. Specific behavior
     * and implementation are yet to be defined.
     *
     * @param fb the FlightBoard passed as the context for this action
     * @throws Exception if the operation cannot be performed
     */
    public void Continue(FlightBoard fb) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * This method allows a player to select a specific sub-ship based on an index.
     * If the action is attempted out of turn, an exception is thrown using the event bus.
     *
     * @param index the index of the sub-ship to be selected
     * @param playerID the unique identifier of the player attempting the selection
     * @throws Exception if the player attempts the action out of turn or any other error occurs
     */
    public void chooseOneSubShip(int index, int playerID) throws Exception {
        card.getGame().getEventBus().notYourTurn(card.getGame().getPlayer(playerID));
    }
}
