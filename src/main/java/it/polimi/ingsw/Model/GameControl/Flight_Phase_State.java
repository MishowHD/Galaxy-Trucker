package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.Goods;

import java.util.ArrayList;

public class Flight_Phase_State extends GameState {
    /**
     * Represents an integer variable used within the context of the Flight_Phase_State class.
     * The exact purpose or role of this variable is determined by its usage in the class methods
     * and their respective logic.
     */
    private int i;
    /**
     * Represents the card currently in use within the Flight_Phase_State.
     * This variable holds a reference to the active Card being played or processed.
     */
    private Card cardinuse;
    /**
     * Represents the active status of the surrender timer in the current game state.
     * This variable determines whether the timer associated with the surrender functionality
     * is running or not.
     *
     * It is declared as volatile to ensure visibility of changes across threads, as
     * state transitions or game actions involving surrender might span multiple threads.
     */
    private volatile boolean surrenderTimerActive = false;

    //i counts the number of cards which have been already activated

    /**
     * Constructs a new instance of Flight_Phase_State, initializing it with the specified game instance.
     * This state represents a specific phase in the flight portion of the game, and initializes internal variables as required.
     *
     * @param game the current game instance used to set up and manage the state context
     * @throws RuntimeException if the state initialization fails
     */
    public Flight_Phase_State(Game game) throws RuntimeException {
        super(game);
        i = 0;
    }

    /**
     * Calculates and retrieves the next state of the game based on the current level of the game.
     *
     * @return The next {@code GameState} object representing the subsequent phase of the game
     *         based on the current level. Returns an instance of {@code ScoringPhaseStateTest}
     *         if the level is 0, {@code ScoringPhaseStateLev2} if the level is 1, or {@code null}
     *         if no corresponding state is defined for the current level.
     *
     * @throws Exception if an error occurs during the transition to the next state.
     */
    @Override
    public GameState getNextState() throws Exception {
        return switch (game.getLevel()) {
            case 0 -> new ScoringPhaseStateTest(game);
            case 1 -> new ScoringPhaseStateLev2(game);
            default -> null;
        };
    }

    /**
     * Handles the surrender action for a player during the flight phase of the game.
     * If the surrender timer is not active, the player is not present on the flight board, or the player is blocked,
     * the surrender action is not allowed, and an event is triggered to indicate no surrender.
     * Otherwise, the player successfully surrenders by updating their status and removing them from the flight board.
     *
     * @param playerID The unique identifier of the player attempting to surrender.
     * @throws Exception If an error occurs during the surrender process.
     */
    @Override
    public void Surrend(int playerID) throws Exception {
        //if player is not in flightboard (or is blocked)  or is not time for surrender, he cant surrender
    if (!surrenderTimerActive||!game.getGameFlightBoard().getBoard().contains(playerID)||game.getPlayer(playerID).isBlocked()) {
            //throw new RuntimeException("Can't surrend");
            game.getEventBus().noSurrender(game.getPlayer(playerID));
        }
        else
            game.getPlayer(playerID).Surrender();
    }


    /**
     * Activates the effect of the current card in the event deck during the flight phase.
     *
     * This method:
     * 1. Marks the current card as activated within the game context.
     * 2. Retrieves the next card in the event deck and sets it as the currently used card.
     * 3. Updates relevant listeners through the event bus about the card's usage.
     * 4. Prints a notification regarding the activated card to the standard output.
     * 5. Activates the card, applies its effect to the flight board, and then deactivates the card.
     *
     * @throws Exception if an error occurs during card effect application or event bus update.
     */
    @Override
    public void EffectActivation() throws Exception {
        game.setCurrentCardActivated(true);

        i++;
        cardinuse = game.getEventDeck().getCardList().get(i - 1);
        game.getEventBus().updateCardUse(cardinuse);
        System.out.println("Effect activated with card" + cardinuse);

        game.getEventDeck().getCardList().get(i - 1).setActivation(true);
        game.getEventDeck().getCardList().get(i - 1).effect(game.getGameFlightBoard());
        game.getEventDeck().getCardList().get(i - 1).setActivation(false);

    }


    /**
     * Starts the surrender timer in the flight phase of the game.
     *
     * This method manages the flow of the game when a surrender timer is activated, particularly
     * during a flight phase. Depending on the game conditions such as the level of the game,
     * the current player's actions, and the state of the event deck, the functionality
     * decides whether to activate card effects, proceed to the next phase, or initiate and
     * manage a timer for surrender.
     *
     * Key functionalities:
     * - Disables the current card effect activation at the start.
     * - If the game is not at level 1 and there are cards remaining in the event deck, it triggers
     *   card effect activation.
     * - If at level 1, handles various scenarios such as checking if players surrender, proceeding
     *   to scoring if no players remain, or activating the surrender timer if applicable.
     * - Manages the surrender timer through a separate thread. Once the timer completes, it activates
     *   the next card effects or moves to the next phase of the game.
     *
     * Thread behavior:
     * - A new thread is created to handle the countdown for the surrender timer.
     * - The timer countdown lasts as long as the time defined in the game's surrender timer configuration.
     * - On countdown completion, transitions the game state to the next appropriate state or activates
     *   the next card.
     *
     * Exceptions:
     * - Throws a generic {@code Exception} in case any game-state transitions or accesses encounter
     *   errors.
     * - Exceptions occurring within the surrender timer thread could lead to runtime errors, as these
     *   are wrapped and re-thrown as {@code RuntimeException}.
     *
     * Note:
     * - Game state transitions and player interactions are directly tied to this method, making
     *   its behavior critical for handling various in-game scenarios during the flight phase.
     */
    public void startSurrenderTimer() throws Exception {
        game.setCurrentCardActivated(false);
        if (game.getLevel() != 1) {
            if (i < game.getEventDeck().getCardList().size()) //&& (game.getGameState() instanceof Flight_Phase_State))
                EffectActivation();
            else game.NextPhase();
        } else {
            ArrayList<Player> flightPlayer = new ArrayList<>(game.getGameFlightBoard().getPlayerRankList());
            //if someone must leave the flight him does
            // if there are no more player then we go to scoring
            for (Player p : flightPlayer) {
                p.getMyShip().checkSurrender();
            }
            if(game.getGameFlightBoard().getDoubledplayers()!=null&&!game.getGameFlightBoard().getDoubledplayers().isEmpty())
            {
                for (Player p : game.getGameFlightBoard().getDoubledplayers()) {
                    p.Surrender();
                }
                game.getGameFlightBoard().resetDoubledPlayers();
            }
            //if there are no more players i go to scoring
            if(game.getGameFlightBoard().getPlayerRankList().isEmpty()) game.NextPhase();
            //if there is already someone
            //if there are more cards I can check if someone wants to left the flight
            else if (i < game.getEventDeck().getCardList().size() - 1){// && (game.getGameState() instanceof Flight_Phase_State)) {
                surrenderTimerActive = true;
                new Thread(() -> {
                    try {
                        for (int j = 0; j < game.getSurrenderTimer(); j++) {
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        //if players left the flight I've no more player
                        surrenderTimerActive = false;
                        try {
                            nextCardActivation();
                        } catch (Exception e) {

                            throw new RuntimeException(e);
                        }

                    }
                }).start();
            } else {
                //I go to scoring
                game.NextPhase();
            }
        }


    }


    /**
     * Executes the next card activation process during the flight phase of the game.
     * If there are no players in the player rank list, the game phase transitions to the next phase.
     * Otherwise, the card effect activation process is triggered.
     *
     * @throws Exception if there is an issue transitioning to the next phase or activating card effects.
     */
    public void nextCardActivation() throws Exception {
        if (game.getGameFlightBoard().getPlayerRankList().isEmpty()) {
            game.NextPhase();
        } else {
            EffectActivation();
        }

    }

    /**
     * Executes the main logic for the flight phase state.
     *
     * This method performs the following operations:
     * 1. Iterates over all players in the game and sets their `blocked` status to false.
     * 2. Activates effects associated with the current game state by invoking the `EffectActivation` method.
     *
     * @throws Exception if any unexpected error occurs during execution.
     */
    @Override
    public void StateMain() throws Exception {
        for (Player p : game.getPlayers()) {
            p.setBlocked(false);
        }
        EffectActivation();
    }

    /**
     * Allows a player to select a specific sub-ship during the flight phase of the game.
     * Delegates the action to the current state of the card in use.
     *
     * @param index the index of the sub-ship to be selected
     * @param playerID the unique identifier of the player attempting the selection
     * @throws Exception if the action is invalid or any error occurs in the process
     */
    public void chooseOneSubShip(int index, int playerID) throws Exception {
        game.getGameState().getCardinuse().getState().chooseOneSubShip(index, playerID);
    }

    /**
     * Retrieves the card currently in use within the flight phase state.
     *
     * @return the card currently in use
     * @throws RuntimeException if an error occurs while retrieving the card
     */
    public Card getCardinuse() throws RuntimeException {
        return cardinuse;
    }

    /**
     * Allows a player to accept the landing on a specified planet during the flight phase of the game.
     *
     * @param p        The player who is accepting the landing on the planet.
     * @param yOn      A boolean indicating whether the action is being performed (true) or skipped (false).
     * @param NumPlanet The identifier of the planet on which the player intends to land.
     *
     * @throws Exception If there are any issues during the acceptance or if the action cannot be performed.
     */
    public void acceptToLandOnAPlanet(Player p, boolean yOn, int NumPlanet) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().acceptToLandOnAPlanet(p, yOn, NumPlanet);
    }

    /**
     * Allows the player to choose an abandoned station to interact with during the flight phase.
     *
     * The method validates whether the action can be executed based on the current card
     * in use. If the card is restricted from activation, an exception is thrown. Otherwise,
     * it delegates the logic to the state of the card currently in use.
     *
     * @param player the player performing the action
     * @param flightBoard the flight board that holds the state of the game related to navigation and positions
     * @param yOn a boolean indicating a condition that influences how the action is performed
     * @param storageTiles a 2D list representing the storage tiles available for the action
     * @param newGoods a 2D list of goods that are potentially added as part of the station selection process
     * @throws Exception if an error occurs during station selection or if the card is restricted from activation
     */
    public void chooseAbandonedStation(Player player, FlightBoard flightBoard, boolean yOn, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseAbandonedStation(player, flightBoard, yOn, storageTiles, newGoods);
    }

    /**
     * Allows the player to choose the positions for cannons and batteries on the flight board.
     * Validates if the card in use is eligible for activation and delegates the positioning logic to the card's state.
     *
     * @param fb the current flight board representing the state of the game.
     * @param player the player making the selection for cannon and battery positions.
     * @param cannonPos a list of positions where cannons are to be placed, represented as list of coordinates.
     * @param batteriesPos a list of positions where batteries are to be placed, represented as list of coordinates.
     * @throws Exception if the card cannot be activated or if an error occurs in selecting the positions.
     */
    public void chooseCannonBatteryPos(FlightBoard fb, Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseCannonBatteryPos(fb, player, cannonPos, batteriesPos);
    }

    /**
     * This method determines how to face meteor threats during the flight phase. It interacts with the current card in use and delegates
     * the operation to the card's state to allow specific behavior for addressing the meteor-related challenge.
     *
     * @param player The player who is deciding how to face the meteors.
     * @param howToDefenceFromShots A list of integers representing the strategy or configurations for defending against meteors.
     * @param flightBoard The current state of the flight board where the game events occur.
     * @throws Exception If there is an error during execution or if the card in use has an invalid activation state.
     */
    public void chooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, FlightBoard flightBoard) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseHowToFaceMeteors(player, howToDefenceFromShots, flightBoard);
    }

    /**
     * Allows the player to choose which passengers to lose, considering the current game state and environment.
     *
     * @param p the player making the selection.
     * @param yOn a boolean indicating a specific game context or option.
     * @param pass an ArrayList of ArrayLists containing information about the passengers potentially being lost.
     * @param flightBoard the current flight board in the game, relevant to the action.
     * @throws Exception if there are any issues while processing the action, or if the card in use cannot be activated.
     */
    public void choosePassengersToLose(Player p, boolean yOn, ArrayList<ArrayList<Integer>> pass, FlightBoard flightBoard) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().choosePassengersToLose(p, yOn, pass);
    }

    /**
     * Allows the player to choose whether to claim a reward based on the current game state and input parameters.
     * If the card in use has a specific invalid ID, an exception will be thrown to signal that the option cannot be activated.
     *
     * @param yOn a boolean parameter indicating the player's decision related to claiming the reward.
     * @param player the player attempting to claim the reward.
     * @throws Exception if an error occurs during the reward claiming process or if activation cannot be performed.
     */
    public void chooseToClaimReward(boolean yOn, Player player) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseToClaimReward(yOn, player);
    }

    /**
     * Handles the player's choice to claim a reward during the flight phase.
     * Delegates the action to the current card's state, ensuring the appropriate logic is executed for the reward claim process.
     *
     * @param fb the flight board related to the current game state
     * @param yOn a boolean that represents the player's decision to engage with the reward (true to accept, false otherwise)
     * @param player the player attempting to claim the reward
     * @param storageTiles a nested list of integers representing the player's storage tiles
     * @param newGoods a nested list of goods representing new goods available for the player
     * @throws Exception if the reward cannot be claimed due to an invalid state or other conditions
     */
    public void chooseToClaimReward(FlightBoard fb, boolean yOn, Player player, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseToClaimReward(fb, yOn, player, storageTiles, newGoods);
    }

    /**
     * Allows the player to choose positions to place batteries and specifies the number of batteries to place at each position.
     * The action is performed if the current card in use allows this operation.
     * Throws an exception if the card in use does not permit the placement of batteries.
     *
     * @param p the player making the choice of where to place the batteries
     * @param posBatAndNumBattXPos a nested list representing the positions and the corresponding number of batteries to place at each position
     * @throws Exception if the operation is invalid or cannot be performed
     */
    public void chooseToPlaceBatteries(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseToPlaceBatteries(p, posBatAndNumBattXPos);
    }

    /**
     * Determines whether a player chooses to activate firepower during the flight phase
     * based on the game state and the player's decisions. It interacts with the currently
     * active card and delegates the behavior to the card's state.
     *
     * @param p                    The player who is making the decision to activate firepower.
     * @param DoubFireTriplets     A list containing triplets of integers that represent the details
     *                             of double firepower activations.
     * @param BatteriesToAct       A list containing details of batteries that the player wishes to
     *                             activate as part of the firepower action.
     * @throws Exception           If there is an issue during the process or the action is not allowed.
     */
    public void chooseToStartFirePower(Player p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseToStartFirePower(p, DoubFireTriplets, BatteriesToAct);
    }

    /**
     * Activates the card's functionality to choose whether to start the motor.
     * The method interacts with the game's flight phase state, including the
     * player, flight board, engine positions, and battery positions. It may throw
     * an exception if the card cannot be activated or due to other runtime conditions.
     *
     * @param player the player invoking the action to start the motor.
     * @param flightBoard the current state of the flight board in the game.
     * @param enginesPos a list of lists containing the positions of engines that may be activated.
     * @param batteriesPos a list of lists containing the positions of batteries that may power the engines.
     * @throws Exception if the card cannot be activated or if an unexpected error occurs during execution.
     */
    public void chooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseToStartMotor(player, flightBoard, enginesPos, batteriesPos);
    }

    /**
     * Determines the positions where goods will be placed and delegates the decision
     * to the state of the currently active card. Throws an exception if the card
     * is not activatable.
     *
     * @param player the player who is making the decision on where to place the goods
     * @param posGoods a list of positions, each represented as a list of integer coordinates,
     *                 where goods could potentially be placed
     * @param goodsSets a list of sets of goods, each represented as a list of Goods objects,
     *                  corresponding to the possible positions in posGoods
     * @throws Exception if an error occurs during the process, or if the active card is not activatable
     */
    public void chooseWhereToPutGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        if (cardinuse.getId() == -5) {
            throw new RuntimeException("Can't activate");
        }
        cardinuse.getState().chooseWhereToPutGoods(player, posGoods, goodsSets);
    }
}
