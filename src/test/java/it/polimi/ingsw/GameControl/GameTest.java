package it.polimi.ingsw.GameControl;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Utils.Hourglass;
import it.polimi.ingsw.Utils.Dice.FixedDiceRoller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() throws Exception {
        game = new Game(0, new FixedDiceRoller(0), 10, 120);
        game.getGameState().JoinGame("Loreto", 2);
        game.getGameState().JoinGame("Giacomo", 0);

    }

    @Test
    void testGetLevel() {
        assertEquals(0, game.getLevel());
    }


    @Test
    void testAddPlayerMaxLimit() {
        assertThrows(RuntimeException.class, () -> game.getGameState().JoinGame("ciao", 0));
    }

    @Test
    void testGetPlayer() {
        // Utilizziamo il metodo getPlayerIDFromName per ottenere gli ID
        int id1 = game.getPlayerIDFromName("Loreto");
        int id2 = game.getPlayerIDFromName("Giacomo");
        // Verifichiamo che il recupero dei giocatori tramite getPlayer con gli ID corrisponda ai giocatori attesi
        assertEquals(0, id1);
        assertEquals(1, id2);
    }

    @Test
    void testGetPlayerID() {
        // Verifica che venga restituito l'ID corretto per player1
        assertEquals(0, game.getPlayerID(game.getPlayerFromNickname("Loreto")));
    }

    @Test
    void testSetAndGetGameBank() {
        Bank bank = new Bank(0, 0, 0, 0, 0);
        game.setGameBank(bank);
        assertEquals(bank, game.getGameBank());
    }

    @Test
    void testSetAndGetGameFlightBoard() {
        FlightBoard flightBoard = new FlightBoard(4, 20, new Bank(0, 0, 0, 0, 0), null);
        game.setGameFlightBoard(flightBoard);
        assertEquals(flightBoard, game.getGameFlightBoard());
    }

    @Test
    void testSetAndGetGameTiles() {
        ArrayList<SpaceShipTile> tiles = new ArrayList<>();
        ArrayList<Type_side_connector> emptyConnectors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            emptyConnectors.add(Type_side_connector.SMOOTH_SIDE);
        }
        tiles.add(new SpaceShipTile(1, emptyConnectors, SSTTypes.Tile_NonAccesiblePlace));
        game.setGameTiles(tiles);
        assertEquals(tiles, game.getGameTiles());
    }

    @Test
    void testGetGameSSTIH() {
        // Inizialmente, la lista delle tile prese dai giocatori deve essere vuota
        ArrayList<SpaceShipTile> tiles = new ArrayList<>();
        ArrayList<Type_side_connector> connectors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            connectors.add(Type_side_connector.SMOOTH_SIDE);
        }
        // Impostiamo le game tiles, ma non ancora prese dai giocatori
        tiles.add(new SpaceShipTile(1, connectors, SSTTypes.Tile_NonAccesiblePlace));
        game.setGameTiles(tiles);
        assertTrue(game.getGameSSTIH().isEmpty());
    }

    @Test
    void testSetAndGetTimer() {
        Hourglass timer = new Hourglass(game.getHourglassTime(), 2, game.getGameState());
        game.setTimer(timer);
        assertEquals(timer, game.getHourglass());
    }

    @Test
    void testGetEventDeck() {
        Deck eventDeck = new Deck(new ArrayList<Card>());
        game.setEventDeck(eventDeck);
        assertEquals(eventDeck, game.getEventDeck());
    }

    @Test
    void testGetOriginalDeck() {
        Deck originalDeck = game.getOriginalDeck();
        assertNotNull(originalDeck);
    }

    @Test
    void testGetOriginalTiles() {
        ArrayList<SpaceShipTile> originalTiles = game.getOriginalTiles();
        assertNotNull(originalTiles);
    }

    @Test
    void shouldFinishBankMaterial(){
        game.getGameBank().useBattery(99);
        game.getGameBank().useBattery(2);
        Goods blueGood = new Goods(1,false);
        Goods greenGood = new Goods(2,false);
        Goods yellowGood = new Goods(3,false);
        Goods redGood = new Goods(4,true);
        ArrayList<Goods> goodsList = new ArrayList<>();
        goodsList.add(blueGood);
        goodsList.add(greenGood);
        goodsList.add(yellowGood);
        goodsList.add(redGood);
        game.getGameBank().addGood(redGood);
        game.getGameBank().addGoodsFromList(goodsList);
        for (int i = 0; i < 105; i++) {
            game.getGameBank().useGood(blueGood);
            game.getGameBank().useGood(greenGood);
            game.getGameBank().useGood(yellowGood);
            game.getGameBank().useGood(redGood);
        }
    }



//    @Test
//    void NextPhaseTest() {
//        // Acquisizione dello stato iniziale
//        GameState initialState = game.getGameState();
//        // Calcolo dello stato finale atteso: dopo 4 transizioni di fase
//        GameState expectedState = initialState;
//        for (int i = 0; i < 4; i++) {
//            expectedState = expectedState.getNextState();
//        }
//        for (int i = 0; i < 4; i++) {
//            game.NextPhase();
//        }
//        assertEquals(expectedState, game.getGameState());
//    }
}
