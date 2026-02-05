package it.polimi.ingsw.Model.Cards.Planets;

import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PLANET_STATE_INIT;

public class PlanetStateInit extends PlanetState {
    /**
     * Represents the primary card associated with a planet in the context of the game's state.
     * This variable is an immutable reference to an instance of {@link Card_PlanetCard},
     * which encapsulates the attributes and behaviors of a planet card, such as its state,
     * associated planets, flight day penalties, and other mechanics.
     *
     * The `card` is initialized through the constructor of the containing class {@code PlanetStateInit}
     * and is used to manage and manipulate the game state related to the planet card.
     */
    private final Card_PlanetCard card;
    /**
     * A list of Player objects representing the players currently involved in the game state.
     * This collection is immutable and intended to manage and access player-specific information
     * during the initialization and progression of the game.
     */
    private final ArrayList<Player> players = new ArrayList<>();
    /**
     * A collection of {@link Planet} objects representing all the available planets within the game state.
     * Each planet in this list may have its own attributes and characteristics, such as associated goods
     * or the player currently located on it.
     *
     * This list is initialized as a final collection and is used to maintain the game logic and interactions
     * involving accessible planets. The contents of the list are determined at the time of game initialization
     * and are immutable in terms of reference, ensuring consistency throughout the lifecycle of the game state.
     */
    private final ArrayList<Planet> availablePlanets;
    /**
     * Represents the flight board associated with the planet state initialization.
     * This field provides the necessary data or interface to manage flight operations
     * and interactions during the planet state setup.
     * The variable is immutable and defined at the initialization of the PlanetStateInit class.
     */
    private final FlightBoard fbb;
    /**
     * Represents the total number of planets within the initialized state of the game.
     *
     * This variable is used within the {@code PlanetStateInit} class to manage
     * or track the count of planets available or involved in the game's state
     * initialization process. It serves as a core component of the planetary
     * game logic, typically interacting with other properties and methods
     * in the class to ensure consistent management of planets.
     */
    private int nunTotplanets;
    /**
     * A list that holds the planets selected during the initialization of the game state.
     * Each element in the list represents a {@link Planet} object chosen for interaction or gameplay.
     *
     * This list is immutable after its creation and is managed internally within the class.
     */
    private final ArrayList<Planet> ChoosedPlanets = new ArrayList<>();

    /**
     * Constructs a new instance of the PlanetStateInit class, which initializes the state of a planet card
     * within the game. This involves setting up the card, flight board, and the available planets for interaction.
     *
     * @param cardP the Card_PlanetCard object representing the planet card associated with this state
     * @param fb the FlightBoard object providing contextual information about the current game board
     * @param PlanetVector an ArrayList of Planet objects representing the planets available in the game
     */
    public PlanetStateInit(Card_PlanetCard cardP, FlightBoard fb, ArrayList<Planet> PlanetVector) {
        super();
        card = cardP;
        fbb = fb;
        players.addAll(fb.getPlayerRankList());
        availablePlanets = new ArrayList<>(PlanetVector);
        nunTotplanets=availablePlanets.size();
        //card.setStateENUM(PLANET_STATE_INIT);
    }

    /**
     * Activates the current planetary state in the context of the active game.
     * This method checks the list of players associated with the current state and performs
     * one of the following actions based on the players' presence:
     *
     * - If no players are present:
     *   - Transitions the state of the associated planet card to {@code PlanetFinStat}, representing the
     *     final phase of the planet's state.
     *   - Prepares a list of players from the selected planets and activates the next phase
     *     using the planet's effect.
     * - If players are present:
     *   - Updates the state of the planet card to the initial planetary state.
     *
     * The method ensures correct transitions based on the state and conditions of the game,
     * enabling seamless progression between planetary states.
     *
     * @throws Exception if an error occurs during the activation process, such as invalid game state
     * or issues with state transitions.
     */
    public void Activate() throws Exception {
        //System.out.println("la dim è "+players.size());
        if (players.isEmpty()) {
            //System.out.println("Ho finito vado in fine");
            card.setState(new PlanetFinStat(card));
            ArrayList<Player> PlayerLost = new ArrayList<>();
            for (Planet planet : ChoosedPlanets) {
                PlayerLost.add(planet.getWhoIsThere());
            }
            card.getState().Activate2(PlayerLost, fbb);
        } else {
            card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),PLANET_STATE_INIT);
        }
    }

    /**
     * Allows a player to attempt to land on a specified planet and perform certain actions depending on the game state and conditions.
     *
     * @param p The player attempting to land on a planet.
     * @param yOn A boolean indicating if the player agrees to land on the planet.
     * @param NumPlanet The index of the planet the player wants to land on, within the available planets list.
     * @throws Exception If there is an error during the process or an invalid operation is attempted.
     */
    public void acceptToLandOnAPlanet(Player p, boolean yOn, int NumPlanet) throws Exception {//da cambiare il nome
        if(NumPlanet >=nunTotplanets){
            card.getGame().getEventBus().wrongInput(p);
        }else if (p.equals(players.getFirst()) && !p.isBlocked()) {
            if (yOn) {
                if (!ChoosedPlanets.contains(availablePlanets.get(NumPlanet))) {
                    players.remove(p);
                    ChoosedPlanets.add(availablePlanets.get(NumPlanet));
                    availablePlanets.get(NumPlanet).setPlayerThere(p);
                    //ci metto i goods che avevo e quelli del pianeta
                    ArrayList<Goods> goodsDiPrimaEguadagnati = new ArrayList<>(p.getMyShip().getMySortedGoods());

                    List<Goods> goodList = availablePlanets.get(NumPlanet).getGoods();
                    Bank bank = card.getGame().getBank();
                    Map<Integer, Long> requiredByColor = goodList.stream()
                            .collect(Collectors.groupingBy(Goods::getValue, Collectors.counting()));
                    boolean enoughGoods = requiredByColor.entrySet().stream().allMatch(entry -> {
                        int color = entry.getKey();
                        long needed = entry.getValue();
                        return switch (color) {
                            case 4 -> bank.getRedGood() >= needed;
                            case 3 -> bank.getYellowGood() >= needed;
                            case 2 -> bank.getGreenGood() >= needed;
                            case 1 -> bank.getBlueGood() >= needed;
                            default -> false;
                        };
                    });
                    if (enoughGoods) {
                        goodsDiPrimaEguadagnati.addAll(goodList);
                    }else{
                        card.getGame().getEventBus().notEnoughGoods(p);
                    }

                    goodsDiPrimaEguadagnati = p.getMyShip().SortGoods(goodsDiPrimaEguadagnati);
                    //update dei goods che posso posizionare per farli vedere
                    card.getGame().getEventBus().updateGoodsRemaining(p, goodsDiPrimaEguadagnati);
                    //card.setStateENUM(PLANET_STATE_ADD_GOODS);
                    card.setState(new PlanetStateAddGoods(this, card, p));
                    card.getState().Activate(goodsDiPrimaEguadagnati);
                } else {

                    card.getGame().getEventBus().wrongInput(p);
                }
            } else {


                players.remove(p);
                if (players.isEmpty()) {
                    //System.out.println("Ho finito vado in fine");
                    card.setState(new PlanetFinStat(card));
                    ArrayList<Player> PlayerLost = new ArrayList<>();
                    for (Planet planet : ChoosedPlanets) {
                        PlayerLost.add(planet.getWhoIsThere());
                    }
                    card.getState().Activate2(PlayerLost, p.getMyShip().getFlightBoard());
                } else {
                    card.getGame().getEventBus().nextPlayerTurn();
                }

                //System.out.println("la dim è "+players.size());
            }
        } else {

            card.getGame().getEventBus().notYourTurn(p);
        }

    }

    /**
     * Updates the current state of the planet to the specified PlanetState instance.
     *
     * This method allows transitioning the planet's current behavior or status to a new
     * state, as defined by the provided PlanetState object. The new state represents
     * updated logic or behavior in the context of the game.
     *
     * @param planetState the new state to be applied to the planet. This defines the
     *                    characteristics and behaviors the planet will adopt after the
     *                    state transition.
     * @throws RuntimeException if the operation is not supported.
     */
    public void setState(PlanetState planetState) {
        throw new RuntimeException("Not supported yet");
    }

}
