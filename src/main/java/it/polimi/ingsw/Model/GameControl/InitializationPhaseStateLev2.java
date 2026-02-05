package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Boards.Lev2FlightBoard;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InitializationPhaseStateLev2 extends Initialization_Phase_State {

    /**
     * Constructs a new InitializationPhaseStateLev2 instance.
     *
     * @param game the Game instance associated with this state
     */
    public InitializationPhaseStateLev2(Game game) {
        super(game);
    }

    /**
     * Extracts and processes the event deck during the initialization phase of the game.
     * This method retrieves the event deck from the game, shuffles it, and processes
     * its cards based on their levels. Cards with a level greater than 2 are removed
     * from the deck. The updated deck is then shuffled again and set back as the
     * event deck in the game.
     *
     * Key steps:
     * 1. Retrieves the event deck using {@code game.getEventDeck()}.
     * 2. Shuffles the deck to randomize the order of its cards.
     * 3. Filters out cards with a {@code cardLevel} greater than 2.
     * 4. Constructs a new {@code Deck} object with the filtered list of cards.
     * 5. Shuffles the newly constructed deck to ensure randomness.
     * 6. Sets the updated deck as the event deck in the game using {@code game.setEventDeck()}.
     */
    @Override
    public void extractDeck() {
        Deck deck = game.getEventDeck();
        deck.shuffle();
        List<Card> cards = deck.getCardList();
        cards.removeIf(card -> card.getCardLevel() > 2);
        deck = new Deck(cards);
        deck.shuffle();
        game.setEventDeck(deck);
    }

    /**
     * Builds the flight board for the game by organizing event cards into visible and hidden decks,
     * setting up the decks, and associating them with the game instance.
     * It also initializes the flight board with the proper configuration based on game settings.
     *
     * The method performs the following steps:
     * 1. Extracts cards of specific levels from the event deck to create visible and hidden decks.
     * 2. Combines all decks into a final shuffled deck and re-assigns the shuffled deck back to the game event deck.
     * 3. Ensures each card in the decks is associated with the game instance.
     * 4. Creates decks that are showable (visible levels) and a hidden deck for the game.
     * 5. Initializes the flight board using the created decks, along with other game configurations such as the number of players and the bank state.
     * 6. Updates the game state by setting the initialized flight board.
     *
     * This method operates within the initialization phase and ensures the game's flight board is prepared with the correct setup prior to gameplay.
     */
    @Override
    public void buildFlightBoard() {
        //estraggo i decks
        ArrayList<Card> litdeck1 = new ArrayList<>();
        ArrayList<Card> litdeck2 = new ArrayList<>();
        ArrayList<Card> litdeck3 = new ArrayList<>();
        ArrayList<Card> hidedeck = new ArrayList<>();
        ArrayList<Card> finaldecklist = new ArrayList<>();

        litdeck1.add(extractCardByLevel(game.getEventDeck(), 2));
        litdeck1.add(extractCardByLevel(game.getEventDeck(), 2));
        litdeck1.add(extractCardByLevel(game.getEventDeck(), 1));

        litdeck2.add(extractCardByLevel(game.getEventDeck(), 2));
        litdeck2.add(extractCardByLevel(game.getEventDeck(), 2));
        litdeck2.add(extractCardByLevel(game.getEventDeck(), 1));

        litdeck3.add(extractCardByLevel(game.getEventDeck(), 2));
        litdeck3.add(extractCardByLevel(game.getEventDeck(), 2));
        litdeck3.add(extractCardByLevel(game.getEventDeck(), 1));

        hidedeck.add(extractCardByLevel(game.getEventDeck(), 2));
        hidedeck.add(extractCardByLevel(game.getEventDeck(), 2));
        hidedeck.add(extractCardByLevel(game.getEventDeck(), 1));

        finaldecklist.addAll(litdeck1);
        finaldecklist.addAll(litdeck2);
        finaldecklist.addAll(litdeck3);
        finaldecklist.addAll(hidedeck);
        Collections.shuffle(finaldecklist);
        Deck actualDeck = new Deck(finaldecklist);
        for (Card c : actualDeck.getCardList()) {
            c.setGame(game);
        }
        game.setEventDeck(actualDeck);
        for (Card c : litdeck1) {
            c.setGame(game);
        }
        for (Card c : litdeck2) {
            c.setGame(game);
        }
        for (Card c : litdeck3) {
            c.setGame(game);
        }
        ArrayList<Deck> showableDecks = new ArrayList<>();
        Deck l1 = new Deck(litdeck1);
        Deck l2 = new Deck(litdeck2);
        Deck l3 = new Deck(litdeck3);
        showableDecks.add(l1);
        showableDecks.add(l2);
        showableDecks.add(l3);
        for (Card c : hidedeck) {
            c.setGame(game);
        }
        Deck hd = new Deck(hidedeck);

        FlightBoard fb = new Lev2FlightBoard(game.getNumOfPlayers(), hd, showableDecks, 24, game.getBank(), game);

        game.setGameFlightBoard(fb);
        //update
    }

    /**
     * Extracts and removes a card from the given deck that matches the specified level.
     * If no card with the specified level is found, an exception is thrown.
     *
     * @param deck the deck from which a card is to be extracted
     * @param level the level of the card to be extracted
     * @return the extracted card of the specified level
     * @throws RuntimeException if no card of the specified level is found in the deck
     */
    private Card extractCardByLevel(Deck deck, int level) {
        List<Card> cards = new ArrayList<>(deck.getCardList());
        int rand = new Random().nextInt(cards.size());
        Card card= cards.get(rand);
        while(card.getCardLevel() != level&&!cards.isEmpty()){
            cards.remove(rand);
            if(!cards.isEmpty()){
                rand = new Random().nextInt(cards.size());
                card= cards.get(rand);
            }
        }
        if(!cards.isEmpty()){
            deck.getCardList().remove(card);
            return card;
        }
        else{
            throw new RuntimeException("Nessuna carta di livello " + level + " trovata nel deck.");
        }
    }

    /**
     * Shuffles and sets up the game tiles for the initialization phase of level 2.
     *
     * This method processes the tiles used in the game by performing the following steps:
     * 1. Retrieves the current game tiles from the game instance.
     * 2. Removes any tiles that are of type `SSTTypes.Tile_NonAccesiblePlace`.
     * 3. Randomizes the order of the remaining tiles using `Collections.shuffle`.
     * 4. Updates the game instance with the shuffled and filtered list of tiles.
     */
    @Override
    public void buildTiles() {
        ArrayList<SpaceShipTile> tilesHeap = game.getGameTiles();
        tilesHeap.removeIf(tile -> tile.getType() == SSTTypes.Tile_NonAccesiblePlace);
        Collections.shuffle(tilesHeap);
        game.setGameTiles(tilesHeap);
    }

    /**
     * Executes the primary logic for the StateMain method within the current game state.
     * This method is responsible for initializing key components and transitioning the game to the next state.
     *
     * Operations performed in this method include:
     * - Populating the bank with initial resources using the fillBank method.
     * - Constructing the flight board utilizing the buildFlightBoard method.
     * - Building game tiles with the buildTiles method.
     * - Extracting the card deck using the extractDeck method.
     * - Marking the construction phase as completed by setting the finishedconstruction flag to true.
     * - Proceeding to the next state by invoking the goNextState method.
     *
     * This method overrides the implementation from its superclass, including custom game state-specific logic.
     *
     * @throws Exception if an error occurs during the initialization or progression to the next state.
     */
    @Override
    public void StateMain() throws Exception {
        fillBank();
        buildFlightBoard();
        buildTiles();
        extractDeck();
        finishedconstruction = true;
        goNextState();
        //game.setGameState(new BuildingPhaseStateLev2(game));
        //game.getGameState().StateMain();
    }

    /**
     * Transition the game to the next state in the game phase sequence.
     *
     * @return a new instance of BuildingPhaseStateLev2, representing the next state in the game.
     */
    @Override
    public GameState getNextState() {
        return new BuildingPhaseStateLev2(game);
    }


}
