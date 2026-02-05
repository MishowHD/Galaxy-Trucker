package it.polimi.ingsw.Model.Cards.Planets;

import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PLANET_FIN_STAT;

public class PlanetFinStat extends PlanetState {
    /**
     * Represents the PlanetCard associated with the current planetary state.
     * This variable holds an immutable reference to a {@link Card_PlanetCard} instance,
     * which contains the attributes and behaviors pertinent to the card's effect on the planet.
     *
     * The {@code card} is utilized in managing the planet's state and its interactions
     * with the players and the game's environment. It provides contextual information
     * such as penalties, planet vector, and the card's state transitions.
     *
     * It is initialized through the constructor of the containing class and remains
     * constant throughout the lifecycle of the state.
     */
    private final Card_PlanetCard card;

    /**
     * Constructs a new instance of PlanetFinStat, a specific state for a planet card in the game.
     * This constructor initializes the state of the provided planet card and updates its state to PLANET_FIN_STAT.
     *
     * @param card the Card_PlanetCard instance representing the planet card whose state is being set
     * @throws Exception if an error occurs during state initialization or state update
     */
    public PlanetFinStat(Card_PlanetCard card) throws Exception {
        super();
        this.card = card;
        this.card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),PLANET_FIN_STAT);

    }

    /**
     * Activates the "Planet Fin Stat" card effect for the given players and flight board.
     * This method modifies the game state by applying a penalty to each player's position on the flight board,
     * triggers the planet fin stat activation event, and starts the surrender timer in the game.
     *
     * @param playersss The list of players for which the card effect is to be applied.
     * @param flightBoard The flight board associated with the players and game state.
     * @throws Exception If an error occurs during the process, such as invalid game state or player actions.
     */
    public void Activate2(ArrayList<Player> playersss, FlightBoard flightBoard) throws Exception {
        ArrayList<Player> h = new ArrayList<>(playersss);
        h.reversed();
        for (Player p : h) {
            p.getMyShip().getFlightBoard().movePlayer(p, -1 * card.getDaysPenalty());
        }
        card.getGame().getEventBus().planetFinStatActivate(playersss, flightBoard);//already ends card effect
        card.getGame().getGameState().startSurrenderTimer();
    }
}
