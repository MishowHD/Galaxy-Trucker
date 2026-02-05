package it.polimi.ingsw.Model.Cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.Model.Cards.AbandonedShip.Card_AbandonedShip;
import it.polimi.ingsw.Model.Cards.AbandonedStation.Card_AbandonedStation;
import it.polimi.ingsw.Model.Cards.CombatZone.Card_Combat_zone;
import it.polimi.ingsw.Model.Cards.CombatZone.Causes;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Epidemic.Card_Epidemic;
import it.polimi.ingsw.Model.Cards.MeteorSwarm.Card_MeteorSwarm;
import it.polimi.ingsw.Model.Cards.OpenSpace.Card_OpenSpace;
import it.polimi.ingsw.Model.Cards.Planets.Card_PlanetCard;
import it.polimi.ingsw.Model.Cards.Pirates.Card_Pirates;
import it.polimi.ingsw.Model.Cards.Planets.Planet;
import it.polimi.ingsw.Model.Cards.Slavers.Card_Slavers;
import it.polimi.ingsw.Model.Cards.Smugglers.Card_Smugglers;
import it.polimi.ingsw.Model.Cards.Stardust.Card_Stardust;
import it.polimi.ingsw.Model.Cards.Utils_Cards.*;
import it.polimi.ingsw.Model.GameControl.Flight_Phase_State;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.io.Serializable;
import java.util.ArrayList;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Card_AbandonedShip.class, name = "Card_AbandonedShip"),
        @JsonSubTypes.Type(value = Card_AbandonedStation.class, name = "Card_AbandonedStation"),
        @JsonSubTypes.Type(value = Card_Combat_zone.class, name = "Card_Combat_zone"),
        @JsonSubTypes.Type(value = Card_Epidemic.class, name = "Card_Epidemic"),
        @JsonSubTypes.Type(value = Card_MeteorSwarm.class, name = "Card_MeteorSwarm"),
        @JsonSubTypes.Type(value = Card_OpenSpace.class, name = "Card_OpenSpace"),
        @JsonSubTypes.Type(value = Card_Pirates.class, name = "Card_Pirates"),
        @JsonSubTypes.Type(value = Card_PlanetCard.class, name = "Card_PlanetCard"),
        @JsonSubTypes.Type(value = Card_Slavers.class, name = "Card_Slavers"),
        @JsonSubTypes.Type(value = Card_Smugglers.class, name = "Card_Smugglers"),
        @JsonSubTypes.Type(value = Card_Stardust.class, name = "Card_Stardust"),
})
public abstract class Card implements Serializable {
    /**
     * The unique identifier for the card.
     * This field stores the ID of the card which is used to uniquely distinguish
     * between different card instances.
     */
    protected int id;
    /**
     * The name of the card represented as a string.
     * This field typically holds a unique or descriptive name for the card.
     */
    protected String cardName;
    /**
     * Represents the level or rank of the card within the game.
     * CardLevel is used to denote the card's hierarchy or progression level,
     * which may influence its abilities, effects, or interactions
     * with other game elements.
     */
    protected int CardLevel;
    /**
     * Indicates whether the card is used for testing purposes.
     * This variable is a flag to distinguish between test and non-test instances of the card.
     */
    protected boolean isTest;
    /**
     * Represents the current state of the card within the game lifecycle.
     *
     * The `state` variable denotes a `Card_State` object which encapsulates
     * the current operational or contextual status of the card. This variable
     * plays a crucial role in managing the behavior, transitions, and effects
     * associated with the card during gameplay.
     *
     * This field is accessible to subclasses and other classes within the same
     * package, and it is modifiable through appropriate setter methods. The
     * state is an integral part of the card's logic, determining its interactions
     * and the flow of associated game mechanics.
     */
    protected Card_State state;
    /**
     * Indicates whether the card is currently activated.
     * This variable determines the active status of a card
     * within the game. When set to true, the card is considered
     * activated, enabling its functionality or effects.
     */
    protected boolean activated;
    /**
     * Represents the enumerated state associated with the card object.
     * This field tracks the current state of the card using the c_State type,
     * which can represent various phases or conditions of the card
     * relevant to its functionality within the game.
     */
    protected c_State State_enum;

    /**
     * Retrieves the associated Game object for the current instance of the class.
     *
     * @return the Game object associated with the current instance
     */
    public Game getGame() {
        return game;
    }

    /**
     * Represents a game instance associated with the current card.
     * This field holds the reference to the {@link Game} object,
     * which contains the state and behavior of the game.
     *
     * It is marked as {@code transient} to exclude it from the default
     * Java serialization process, as it may be managed or re-initialized
     * separately upon deserialization.
     *
     * This variable provides access to game-specific properties and
     * operations, enabling interaction between the card and the game state.
     */
    protected transient Game game;

    /**
     * Constructs a new instance of the Card class.
     *
     * This is the default no-argument constructor for the Card class.
     * It initializes a generic Card object without setting specific field values or states.
     */
    public Card() {
    }

    /**
     * Constructor for the Card class that initializes the core attributes of a card.
     *
     * @param id         An integer representing the unique identifier for the card.
     * @param cardName   A string specifying the name of the card.
     * @param cardLevel  An integer indicating the level of the card.
     * @param isTest     A boolean flag indicating whether the card is used in a test or mock scenario.
     */
    @JsonCreator
    public Card(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int cardLevel,
            @JsonProperty("isTest") boolean isTest
    ) {
        this.id = id;
        this.cardName = cardName;
        this.CardLevel = cardLevel;
        this.isTest = isTest;
        activated = false;
    }

    /**
     * Retrieves the unique identifier of the card.
     *
     * @return the unique identifier of the card as an integer
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the name of the card.
     *
     * @return the card name as a String
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Returns the number of crew members lost as a result of the card's effect.
     *
     * @return the number of crew members lost
     */
    // Getter methods for Slavers class
    public abstract int getCrewLost();

    /**
     * Retrieves the amount of money gained from the current card.
     *
     * @return the amount of money gained as an integer
     */
    public abstract int getMoneyGained();

    /**
     * Applies the effect of the card on the provided flight board.
     *
     * @param board the flight board where the card's effect will be applied
     * @throws Exception if an error occurs during the execution of the effect
     */
    public void effect(FlightBoard board) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the firepower associated with this card.
     *
     * @return the firepower value as an integer
     * @throws RuntimeException if the method is not supported or implemented
     */
    public int getFirePower() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Sets the activation state of the card.
     *
     * @param activated the new activation state to set. True indicates the card is activated,
     *                  while false indicates it is deactivated.
     */
    public void setActivation(boolean activated) {
        this.activated = activated;
    }

    /**
     * Retrieves the level of the card.
     *
     * @return the level of the card as an integer
     */
    public int getCardLevel() {
        return CardLevel;
    }

    /**
     * Determines if the card is marked as a test card.
     *
     * @return true if the card is a test card; false otherwise.
     */
    public boolean isTest() {
        return isTest;
    }

    /**
     * Retrieves the number of days to be penalized by this card. The penalty is applied
     * based on the game's current state and logic.
     *
     * @return the number of days to be penalized as an integer.
     * @throws RuntimeException if the method is not supported or an error occurs during execution.
     */
    public int getDaysPenalty() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the penalty value associated with goods in the current context.
     * The method determines the penalty that will be applied depending on the game's state or card's behavior.
     *
     * @return the penalty value for goods as an integer
     * @throws RuntimeException if the method is not implemented or an error occurs during the retrieval
     */
    public int getGoodsPenalty() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the list of goods associated with this card.
     * This method is not supported and will always throw a RuntimeException.
     *
     * @return an ArrayList of Goods associated with this card
     * @throws RuntimeException when the method is called, as it is not supported
     */
    public ArrayList<Goods> getGoods() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the crew associated with the card.
     *
     * @return the number of crew for this card
     * @throws RuntimeException if the method is not supported
     */
    public int getCrew() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }


    /**
     * Provides the monetary earnings associated with the card.
     *
     * @return the amount of money earned by the card
     * @throws RuntimeException if the operation is not supported
     */
    public int viewMoneyEarning() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Calculates or retrieves the penalty associated with a flight.
     *
     * @return the penalty value tied to the flight as an integer
     * @throws RuntimeException if this method is not supported or implemented
     */
    public int viewFlightPenalty() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the penalty related to the crew associated with the card.
     * Throws a {@link RuntimeException} if the method is not supported or
     * implemented in the context of the card.
     *
     * @return the integer value representing the crew penalty
     * @throws RuntimeException if this operation is not supported
     */
    public int viewCrewPenalty() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves a list of {@link Planet} objects relevant to the card's current state
     * or functionality. The list may represent planets associated with the card
     * under specific game mechanics or conditions.
     *
     * @return an {@link ArrayList} of {@link Planet} objects; the specific contents
     *         and conditions dictating the list will depend on the card's game logic.
     * @throws RuntimeException if the method is not supported in the current implementation.
     */
    public ArrayList<Planet> getPlanetVector() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the list of consequences associated with the current card.
     * This may include various types of consequences, such as penalties or effects
     * related to crew, goods, or other game elements.
     *
     * @return an ArrayList containing {@code Consequences} objects representing the consequences of the card
     * @throws RuntimeException if the method is not supported
     */
    public ArrayList<Consequences> getConsequences() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves a list of causes represented by the Causes objects.
     *
     * @return an ArrayList of Causes objects that represent the causes.
     */
    public ArrayList<Causes> getCauses() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the current state of the card.
     *
     * @return the current state of the card as a Card_State object
     */
    public Card_State getState() {
        return state;
    }
/**
 * Updates the card's state to the specified enumeration value and communicates the changes to clients.
 * Triggers actions involving the specified players if applicable.
 *
 * @param ps the list of players involved in the state change
 * @param state the new state to be assigned to the card
 * @throws Exception if an error occurs during the state update or communication process
 */
//changes model's card's state and communicates to clients the changes and players involved to call an action
    public synchronized void setStateENUM(ArrayList<Player> ps,c_State state) throws Exception {
        State_enum = state;
        if (game != null && game.getGameState() instanceof Flight_Phase_State)
            game.getEventBus().updateCardUseSTATE(ps, state);
    }
    /**
     * Updates the current card's state in the model without triggering any additional actions
     * or notifications to clients or other entities.
     *
     * @param state the new state to set for the card.
     */
    //only changes little model's current card's state
    public void setStatENUMonly(c_State state){
        State_enum = state;
    }

    /**
     * Updates the state of the card to the specified state.
     *
     * @param state the new state to set for the card, represented by a Card_State object
     * @throws Exception if an error occurs during state update
     */
    public void setState(Card_State state) throws Exception {
        this.state = state;
    }

    /**
     * Proceeds the card to its next state based on the provided flight board.
     * This involves updating the card's current state and potentially triggering
     * any related actions or consequences specific to the new state.
     *
     * @param flightBoard The {@code FlightBoard} object used as context to determine
     *                    the next state and handle state transition logic.
     * @throws Exception If the state transition cannot be performed or encounters issues.
     */
    public void goNextState(FlightBoard flightBoard) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the list of shots associated with the card.
     *
     * The returned shots represent specific actions or events defined
     * within the context of the game. Each shot provides details
     * such as its size, rotation, and type (e.g., Meteor or Cannon).
     * This method allows external components or states to access
     * and interact with these shots, enabling game mechanics
     * that involve handling or processing these actions.
     *
     * @return an ArrayList of Shot objects representing the shots associated with the card.
     */
    public ArrayList<Shot> getShots() {
        return null;
    }

    /**
     * Retrieves the current state of the card as a state enumeration.
     *
     * @return the current state of the card represented as a c_State enumeration.
     */
    public c_State getStateEnum() {
        return State_enum;
    }

    /**
     * Sets the current game instance.
     *
     * @param game the Game object to be assigned
     */
    public void setGame(Game game) {
        this.game = game;
    }

}
