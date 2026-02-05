package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class CombatZoneBegin extends CombatZoneState implements Serializable {
    /**
     * Represents a combat zone card that is associated with the initial state of the combat zone.
     * This card is critical during the setup and activation of the combat zone mechanics.
     * It holds information about its associated causes and consequences, influencing
     * the gameplay dynamics for the involved players.
     *
     * The `card` field is immutable and ensures the ability to reference
     * the specific card throughout various states and operations within the combat zone.
     */
    private final Card_Combat_zone card;
    /**
     * List of consequences associated with the current state of the combat zone.
     * These consequences represent potential outcomes or penalties that might
     * affect players or gameplay, such as loss of crew, days, goods, or other
     * player-related effects like shots being fired.
     *
     * The contents of this list are dynamically managed and determined by specific
     * game events and player actions.
     */
    private final ArrayList<Consequences> consequences = new ArrayList<>();
    /**
     * Represents a list of causes associated with the combat zone.
     * Each cause encapsulates specific actions or events that contribute
     * to the combat scenario.
     *
     * <ul>
     *   <li>Causes are expressed as instances of the {@link Causes} abstract class,
     *   which may include various types such as {@code Causes_Crew}, {@code Causes_Fire_power},
     *   or {@code Causes_Motion_power}.</li>
     *   <li>This list is immutable as it is declared with {@code final}, ensuring
     *   that the reference to the list cannot be changed once initialized.</li>
     *   <li>The specific causes provide the context for evaluating various combat effects or transitions
     *   in the combat zone.</li>
     * </ul>
     */
    private final ArrayList<Causes> causes;
    /**
     * A private instance variable representing the flight board in the combat zone.
     * This object is utilized to manage and reference the current state of the
     * flight elements involved in the combat zone mechanics.
     */
    private FlightBoard flightBoard;
    /**
     * A list of players involved in motor-related actions or processes within the combat zone.
     * This list is used to track players who are interacting with or starting motors during the game.
     */
    private ArrayList<Player> playersForMotors;
    /**
     * Represents a collection of players prepared to engage in fire power activities
     * within the combat zone during the game's progression.
     *
     * Each player in this list is selected and involved in fire power-related operations
     * such as firing strategies, targeting, or contributing towards the game's fire mechanics.
     */
    private ArrayList<Player> playersForFire;
    /**
     * Represents a data structure to store motion power rankings of players in the combat zone.
     * The list contains integer values corresponding to the rank or power level of each player
     * in relation to their motion capabilities. The ranks are maintained in a dynamic list
     * supporting changes and updates during gameplay.
     */
    private final ArrayList<Integer> MotionPower4PlayerRank = new ArrayList<>();
    /**
     * Represents the firepower values associated with player ranks in the combat zone.
     * This list stores floating-point values corresponding to the firepower for each
     * rank position held by players.
     *
     * The values are used to evaluate player performance and determine actions like
     * consequences or advantages in the combat zone.
     *
     * This list is final, meaning its reference cannot be changed, though its contents
     * may be modified.
     */
    private final ArrayList<Float> FirePower4PlayerRank = new ArrayList<>();

    /**
     * Updates the state of the combat zone card based on the first cause present in the causes list.
     *
     * <ul>
     *     <li>If the first cause is an instance of {@code Causes_Crew}, the state is set to {@code COMBAT_CREW}.</li>
     *     <li>If the first cause is an instance of {@code Causes_Motion_power}, the state is set to {@code COMBAT_MOTION_POWER}.</li>
     *     <li>If the first cause is an instance of {@code Causes_Fire_power}, the state is set to {@code COMBAT_FIRE_POWER}.</li>
     * </ul>
     *
     * This method interacts with the {@code card} object to set the state by invoking {@code setStateENUM},
     * passing the list of players ranked by their positions and a specific combat state.
     *
     * @throws Exception if there is an issue during the state transition or while setting the state in the underlying game logic.
     */
    public void updateCombatcause() throws Exception {
        if (causes.getFirst() instanceof Causes_Crew) {
            card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),c_State.COMBAT_CREW);
        } else if (causes.getFirst() instanceof Causes_Motion_power) {
            card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),c_State.COMBAT_MOTION_POWER);
        } else if (causes.getFirst() instanceof Causes_Fire_power) {
            card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),c_State.COMBAT_FIRE_POWER);
        }
    }

    /**
     * Initializes a new instance of the CombatZoneBegin class, representing the initial state
     * of a combat zone in the card game. This constructor initializes internal lists of
     * causes and consequences by copying them from the provided {@link Card_Combat_zone} object.
     *
     * @param cardC The {@link Card_Combat_zone} object representing the combat zone card
     *              associated with this state. This object contains a predefined list
     *              of causes and consequences to initialize the state.
     */
    public CombatZoneBegin(Card_Combat_zone cardC) {
        super(cardC);
        this.card = cardC;
        causes = new ArrayList<>();
        causes.addAll(card.getCauses());
        consequences.addAll(card.getConsequences());

    }

    /**
     * Activates the initial state of the combat zone by setting up the necessary fields,
     * preparing players' ranking lists, handling causes, and transitioning to the appropriate state or final state.
     *
     * @param board the {@code FlightBoard} instance containing the context and player rankings for the combat zone
     * @throws Exception if an error occurs during the activation process or during state transitions
     */
    public void Activate(FlightBoard board) throws Exception {
        flightBoard = board;
        playersForMotors = new ArrayList<>();
        playersForFire = new ArrayList<>();
        playersForMotors.addAll(flightBoard.getPlayerRankList());
        playersForFire.addAll(flightBoard.getPlayerRankList());

        if (!causes.isEmpty()) {
            if (causes.getFirst() instanceof Causes_Crew) {
                int minIndex = getMinIndex(board);
                causes.removeFirst();
                goToConsequencesState(board.getPlayerRankList().get(minIndex));
                //Activate(flightBoard);
                //looseCrew();//provo a vedere se la prossima cause è una loosecreww
            } else {
                updateCombatcause();

            }
        } else {
            card.setState(new CombatZoneFinal(card, flightBoard));
        }

    }

    /**
     * Determines the index of the player with the minimum number of passengers on their ship.
     *
     * @param board the flight board containing the list of players and their respective ship data
     * @return the index of the player with the minimum number of passengers
     */
    private static int getMinIndex(FlightBoard board) {
        ArrayList<Integer> crw = new ArrayList<>();
        for (Player p : board.getPlayerRankList()) {
            crw.add(p.getMyShip().getPassengerNumber());
        }
        int minIndex = 0;
        int minValue = crw.getFirst();
        //causes.removeFirst();//rimuovo la prima causa che non mi serve più
        //updateCombatcause();
        //card.getGame().getEventBus().updateCardUse(card);
        for (int i = 1; i < crw.size(); i++) {
            if (crw.get(i) < minValue) {
                minValue = crw.get(i);
                minIndex = i;
            }
        }
        return minIndex;
    }


    /**
     * Handles the process of a player deciding and applying motor power during the combat phase.
     * This method ensures that the player provides correct input for activating engines
     * and processes the consequences for all participants during this phase.
     *
     * @param p The player who is taking their turn to decide the motor power.
     * @param b The flight board representing the game state and players' positions.
     * @param DoubleMotPow A matrix containing the motor power values proposed by the player.
     *        Each inner list represents a motor group, and the elements indicate their activation state.
     * @param BatteriesToAct A matrix indicating which batteries are activated for engine use.
     *        Each inner list represents a specific battery group for the associated motor group.
     * @throws Exception If there is an error in game state management or during processing of incorrect inputs.
     */
    public void chooseToStartMotor(Player p, FlightBoard b, ArrayList<ArrayList<Integer>> DoubleMotPow, ArrayList<ArrayList<Integer>> BatteriesToAct) throws Exception { //i player scelgono uno all volta o posso assumere di avere tutte le risposte?
        if (!(causes.getFirst() instanceof Causes_Motion_power) || playersForMotors.isEmpty()) {
            card.getGame().getEventBus().wrongInput(p);
        } else {
            if ((p.equals(playersForMotors.getFirst()))) {
                if (p.getMyShip().checkCorrectEngineBatteries(DoubleMotPow, BatteriesToAct, p)) {
                    card.getGame().getEventBus().nextPlayerTurn();
                    if(DoubleMotPow!=null)card.getGame().getEventBus().lostBatteries(p,BatteriesToAct);
                    int PlayerMotPower = p.getMyShip().getTotalMotionPower(DoubleMotPow, BatteriesToAct, p);
                    playersForMotors.remove(p);
                    MotionPower4PlayerRank.add(PlayerMotPower);

                } else {//se la proposta di cannoni non è giusta il player deve parlare di nuovo quindi mi stoppo

                    card.getGame().getEventBus().wrongInput(p);
                }
            } else {

                card.getGame().getEventBus().notYourTurn(p);
            }
            //qui attivo le conseguenze quando non ci sono player
            if (playersForMotors.isEmpty()) {//ho finito  e faccio i controlli su chi è il min

                //updateCombatcause();
                int minIndex = 0;
                float minValue = MotionPower4PlayerRank.getFirst();

                for (int i = 0; i < MotionPower4PlayerRank.size(); i++) {
                    if (MotionPower4PlayerRank.get(i) < minValue) {
                        minValue = MotionPower4PlayerRank.get(i);
                        minIndex = i;
                    }
                }
                causes.removeFirst();
                //vado nello stato conseguenze
                System.out.println("player how lost is " + flightBoard.getPlayerRankList().get(minIndex).getUsername());
                goToConsequencesState(flightBoard.getPlayerRankList().get(minIndex));
                //Activate(flightBoard);
                //looseCrew();//provo a vedere se la prossima cause è una loosecreww

            }
        }
    }

    /**
     * Initiates the firepower action for a player during their turn in the combat zone.
     * Verifies if the provided input is valid for firing and updates the game state accordingly.
     * Handles player turns, calculates fire power, and transitions to consequence states when all players finish their turns.
     *
     * @param p the player attempting to start the firepower action
     * @param DoubFireTriplets a list of lists representing the selected triplets of cannons for double firepower
     * @param BatteriesToAct a list of lists representing the selected batteries to fire
     * @throws Exception if any game rules or state issues arise during execution
     */
    @Override
    public void chooseToStartFirePower(Player p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct) throws Exception {
        if (!(causes.getFirst() instanceof Causes_Fire_power) || playersForFire.isEmpty()) {

            card.getGame().getEventBus().wrongInput(p);
        } else {
            if ((p.equals(playersForFire.getFirst()))) {
                if (p.getMyShip().checkCorrectCannonBatteries(DoubFireTriplets, BatteriesToAct, p)) {
                    if(DoubFireTriplets!=null)card.getGame().getEventBus().lostBatteries(p,BatteriesToAct);
                    card.getGame().getEventBus().nextPlayerTurn();
                    float PlayerFirePower = 0;
                    PlayerFirePower += p.getMyShip().getTotalFirePower(DoubFireTriplets, BatteriesToAct, p);
                    playersForFire.remove(p);
                    FirePower4PlayerRank.add(PlayerFirePower);

                } else {

                    card.getGame().getEventBus().wrongInput(p);
                }
            } else {

                card.getGame().getEventBus().notYourTurn(p);
            }
            //qui attivo le conseguenze quando non ci sono player
            if (playersForFire.isEmpty()) {//ho finito  e faccio i controlli su chi è il min

//                updateCombatcause();
                int minIndex = 0;
                float minValue = FirePower4PlayerRank.getFirst();

                for (int i = 0; i < FirePower4PlayerRank.size() - 1; i++) {
                    if (FirePower4PlayerRank.get(i) < minValue) {
                        minValue = FirePower4PlayerRank.get(i);
                        minIndex = i;
                    }
                }
                //vado nello stato conseguenze
                causes.removeFirst();
                goToConsequencesState(flightBoard.getPlayerRankList().get(minIndex));
                //looseCrew();//provo a vedere se la prossima cause è una loosecreww

            }
        }
    }

    /**
     * Transitions the game state based on the type of consequence associated with the player.
     * This method processes the first consequence in the consequence list
     * and applies the relevant game logic, updating the game state accordingly.
     *
     * @param p the player for whom the consequences are being processed
     * @throws Exception if an unexpected error occurs during state transitions
     */
    private void goToConsequencesState(Player p) throws Exception {
        //card.getGame().getEventBus().youPayConsequences(p);
        //card.getGame().getEventBus().updateCardUse(card);//vediamo se funge
        if (consequences.getFirst() instanceof Consequences_Lost_crew) {

            card.setState(new CombatZoneChoosingPass(card, consequences, this, p));
            //consequences.removeFirst();
        } else if (consequences.getFirst() instanceof Consequences_Lost_days) {

            int numDays = ((Consequences_Lost_days) consequences.getFirst()).getNumDays();

            flightBoard.movePlayer(p, -1 * numDays);
            //ho finito gli effetti, finisce la carta
            card.getGame().getEventBus().updateConsequenceLostDays(p, flightBoard, numDays, consequences.size() == 1);

            consequences.removeFirst();
            Activate(flightBoard);
        } else if (consequences.getFirst() instanceof Consequences_Lost_goods) {
            int checkprel = p.getMyShip().getMySortedGoods().size() - consequences.getFirst().getNumGoods();
            if (checkprel > 0) {
                //vado a scegliere solo dove mettere i goods
                ArrayList<Goods> goodFInali = p.getMyShip().looseOrAddGoods(null, consequences.getFirst().getNumGoods());
                //per i goods faccio vedere
                //update dei goods che posso posizionare per farli vedere
                card.getGame().getEventBus().updateGoodsRemaining(p, goodFInali);
                card.setState(new CombatZoneChooseGoods(card, consequences, this, p));
                ArrayList<Goods> goodsDiPrima = new ArrayList<>();
                //ci metto i goods che avevo e quelli del pianeta
                goodsDiPrima.addAll(p.getMyShip().getMySortedGoods());
                goodsDiPrima = p.getMyShip().SortGoods(goodsDiPrima);
                card.getState().Activate(goodsDiPrima);
            } else if (checkprel == 0) {
                //tolgo tutti i goods e basta
                p.getMyShip().looseAllGoods();
                card.getGame().getEventBus().updateLooseAllGoods(p, consequences.size() == 1, false, false);
                consequences.removeFirst();
                Activate(flightBoard);
            } else {
                p.getMyShip().looseAllGoods();
                int battnum = p.getMyShip().getBatteriesNumber();
                if (-checkprel < battnum) {
                    card.getGame().getEventBus().updateLooseAllGoods(p, consequences.size() == 1, true, false);
                    //tolgo tutti i goods e devo far scegliere dove mettere le batterie
                    int batteriedaposizionare = battnum + checkprel;
                    p.getMyShip().looseAllBatteris();
                    card.setState(new CombatZoneChooseBatteries(card, consequences, this, p, batteriedaposizionare));
                } else {
                    card.getGame().getEventBus().updateLooseAllGoods(p, consequences.size() == 1, true, true);
                    //tolgo tutt le batte e tutti i goods
                    p.getMyShip().looseAllBatteris();
                    consequences.removeFirst();
                    Activate(flightBoard);
                }
            }

        } else if (consequences.getFirst() instanceof Consequences_Shots) {
            ArrayList<Shot> shotss = consequences.getFirst().getShots();
            card.setState(new MeteorCardCalcStateFROMCOMBAT(card, consequences, this, p, shotss));
            card.getState().activate(flightBoard);
            //consequences.removeFirst();
        }

    }

}
