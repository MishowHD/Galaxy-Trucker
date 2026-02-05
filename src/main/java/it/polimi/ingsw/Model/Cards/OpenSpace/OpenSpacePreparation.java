package it.polimi.ingsw.Model.Cards.OpenSpace;

import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Boards.Lev2FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.View.Utils_View.CommandType;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.OPEN_SPACE_PREPARATION;

public class OpenSpacePreparation extends OpenSpaceState {
    /**
     * Represents the ranking of players during the open space preparation phase of the game.
     * The ranking is used to determine the order of players' actions in subsequent game processes.
     * This list is populated based on the game's flight board ranking.
     */
    private ArrayList<Player> playerRank;
    /**
     * A list of players who are required to provide an answer or make a decision
     * during the current phase of the game.
     *
     * This variable tracks the players who need to respond before the game can
     * progress to the next step. The players in this list are typically expected
     * to perform actions such as selecting options, making decisions, or submitting
     * inputs as part of the game mechanics. As players complete their required actions,
     * they are removed from this list.
     */
    private ArrayList<Player> playersWhoHaveToAnswer;
    /**
     * Stores the motion powers of players during the OpenSpacePreparation phase.
     * Each integer in this list corresponds to the total motion power of a player
     * in the order defined by the player ranking (`playerRank`).
     *
     * The motion power represents the combined thrust of a player's engines and
     * batteries, and is used to determine their movement on the flight board.
     *
     * This list is updated incrementally as players provide their engine and battery
     * configurations, and it will contain null values for players who have yet to provide
     * their motion power. Once all players have recorded their motion power, the list
     * is utilized to move players on the flight board accordingly.
     */
    private ArrayList<Integer> playersMotionPowers;

    /**
     * Constructs an instance of {@code OpenSpacePreparation}, initializing it with the specified {@code Card_OpenSpace}.
     * This state corresponds to the preparation phase of an open space scenario.
     *
     * @param openSpace the {@code Card_OpenSpace} instance associated with this state
     * @throws Exception if there is an issue during the initialization of the state
     */
    public OpenSpacePreparation(Card_OpenSpace openSpace) throws Exception {
        super(openSpace);

    }

    /**
     * Activates the `OpenSpacePreparation` state by initializing the necessary game state
     * components and player rankings. This method prepares the game for the "Open Space
     * Preparation" phase where players must interact and make decisions accordingly.
     *
     * @param flightBoard the flight board object which contains the current ranking and
     *                    information about the players' positions and game state.
     * @throws Exception if there is an issue initializing the state or interacting with
     *                   the game components.
     */
    public void activate(FlightBoard flightBoard) throws Exception {
        card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),OPEN_SPACE_PREPARATION);
        playerRank = flightBoard.getRanking();
        this.playersWhoHaveToAnswer = new ArrayList<>(playerRank);
        this.playersMotionPowers = new ArrayList<>();
        ArrayList<CommandType> commands = new ArrayList<>();
        commands.add(CommandType.CHOOSE_TO_START_MOTOR);
        card.getGame().getEventBus().effectStarted();
    }

    /**
     * Allows the specified player to choose whether to start their ship's motor during the game.
     * This method performs checks to ensure the engines and batteries specified by the player are valid,
     * updates the game state accordingly, and processes the player's motion power.
     *
     * @param player The player attempting to start their motor.
     * @param flightBoard The flight board representing the current state of space the players navigate.
     * @param enginesPos A list of positions representing the player's chosen engines.
     * @param batteriesPos A list of positions representing the player's chosen batteries.
     * @throws Exception If an error occurs during the execution of this method or if the player's input is invalid.
     */
    public void chooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        if (playersWhoHaveToAnswer.contains(player)) {
            if (player.getMyShip().checkCorrectEngineBatteries(enginesPos, batteriesPos, player)) {
                card.getGame().getEventBus().openSpaceChooseToStartMotor(player, flightBoard, enginesPos, batteriesPos);
                int totMotPow = player.getMyShip().getTotalMotionPower(enginesPos, batteriesPos, player);
                int index = playerRank.indexOf(player);
                if (totMotPow == 0 && flightBoard instanceof Lev2FlightBoard) { //se sono nel livello 2 devo essere tirato via
                    //player.Surrender();
                    player.getMyShip().removeAllPassengers();
                }
                playersWhoHaveToAnswer.remove(player);
                while (playersMotionPowers.size() <= index) {
                    playersMotionPowers.add(null);
                }
                // Inserisce solo se la posizione è vuota
                if (playersMotionPowers.get(index) == null) {

                    playersMotionPowers.set(index, totMotPow);
                }
            } else {
                card.getGame().getEventBus().wrongInput(player);
            }
        } else {
            card.getGame().getEventBus().notYourTurn(player);
        }
        if (playersWhoHaveToAnswer.isEmpty()) {//ho le risposte di tutti i giocatori e quello che ha appena risposto era l'ultimo
            for (Player p : playerRank) { //sposto i giocatori in ordine di rotta
                card.getGame().getEventBus().movePlayerInFlightBoard(p.getUsername(), playersMotionPowers.get(playerRank.indexOf(p)));
                player.getMyShip().getFlightBoard().movePlayer(p, playersMotionPowers.get(playerRank.indexOf(p)));
            }
            card.setState(new OpenSpaceFinal(card));
        }
    }

}
