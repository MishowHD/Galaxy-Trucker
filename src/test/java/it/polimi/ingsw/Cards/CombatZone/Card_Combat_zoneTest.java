package it.polimi.ingsw.Cards.CombatZone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Cards.CombatZone.*;
import it.polimi.ingsw.Model.GameControl.BuildingPhaseStateLev2;
import it.polimi.ingsw.Model.GameControl.Flight_Phase_State;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.GameControl.GameController;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import it.polimi.ingsw.Utils.Dice.FixedDiceRoller;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.View.TUI.TUIHandler;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Card_Combat_zoneTest {
    Game mainGame;
    Deck EventDeck;
    GameController gameController;


    void setUp() {
        mainGame = new Game(1, new FixedDiceRoller(0), 10, 120);
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it.polimi.ingsw/combat.json")) {
            if (input == null) {
                System.out.println("File JSON non trovato!");
                return;
            }
            List<Card> gameCards = mapper.readValue(input, new TypeReference<List<Card>>() {
            });
            EventDeck = new Deck(gameCards);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del mazzo");
        }
        mainGame.setEventDeck(EventDeck);
    }

    void setUp2card() {
        mainGame = new Game(1, new FixedDiceRoller(1), 10, 120);
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it.polimi.ingsw/combat1.json")) {
            if (input == null) {
                System.out.println("File JSON non trovato!");
                return;
            }
            List<Card> gameCards = mapper.readValue(input, new TypeReference<List<Card>>() {
            });
            EventDeck = new Deck(gameCards);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del mazzo");
        }
        mainGame.setEventDeck(EventDeck);
    }

    @Test
    void cardTest() throws Exception {
        setUp();

        EventBus b = mainGame.getEventBus();
        gameController = new GameController(mainGame, b);
        gameController.JoinGame("Loreto", 4);
        gameController.JoinGame("Giuseppe", 0);
        gameController.JoinGame("Giacomo", 0);
        gameController.JoinGame("Lorenzo", 0);

        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        int giacomo = mainGame.getPlayerIDFromName("Giacomo");
        int lorenzo = mainGame.getPlayerIDFromName("Lorenzo");
        gameController.activateTimer(mainGame.getPlayer(loreto));
        //PRIMO GIOCATORE
        mainGame.getGameState().pickTile(149, loreto);
        mainGame.getGameState().insertTile(2, 2, loreto, 180);

        mainGame.getGameState().pickTile(16, loreto);
        mainGame.getGameState().insertTile(2, 1, loreto, 270);

        mainGame.getGameState().pickTile(103, loreto);
        mainGame.getGameState().insertTile(2, 0, loreto, 270);

        mainGame.getGameState().pickTile(120, loreto);
        mainGame.getGameState().insertTile(1, 1, loreto, 270);

        mainGame.getGameState().pickTile(6, loreto);
        mainGame.getGameState().insertTile(1, 2, loreto, 270);

        mainGame.getGameState().pickTile(126, loreto);
        mainGame.getGameState().insertTile(0, 2, loreto, 0);

        mainGame.getGameState().pickTile(15, loreto);
        mainGame.getGameState().insertTile(1, 3, loreto, 180);

        mainGame.getGameState().pickTile(98, loreto);
        mainGame.getGameState().insertTile(3, 3, loreto, 0);

        mainGame.getGameState().pickTile(62, loreto);
        mainGame.getGameState().insertTile(3, 2, loreto, 0);

        mainGame.getGameState().pickTile(32, loreto);
        mainGame.getGameState().insertTile(3, 1, loreto, 270);

        mainGame.getGameState().pickTile(26, loreto);
        mainGame.getGameState().insertTile(3, 0, loreto, 180);

        mainGame.getGameState().pickTile(75, loreto);
        mainGame.getGameState().insertTile(4, 0, loreto, 0);

        mainGame.getGameState().pickTile(11, loreto);
        mainGame.getGameState().insertTile(4, 1, loreto, 0);

        mainGame.getGameState().pickTile(74, loreto);
        mainGame.getGameState().insertTile(4, 2, loreto, 0);

        mainGame.getGameState().pickTile(154, loreto);
        mainGame.getGameState().insertTile(2, 4, loreto, 0);

        mainGame.getGameState().pickTile(41, loreto);
        mainGame.getGameState().insertTile(3, 4, loreto, 270);

        mainGame.getGameState().pickTile(145, loreto);
        mainGame.getGameState().insertTile(4, 4, loreto, 90);

        mainGame.getGameState().pickTile(31, loreto);
        mainGame.getGameState().insertTile(1, 4, loreto, 180);

        mainGame.getGameState().pickTile(104, loreto);
        mainGame.getGameState().insertTile(0, 4, loreto, 0);

        mainGame.getGameState().pickTile(135, loreto);
        mainGame.getGameState().insertTile(1, 5, loreto, 0);

        mainGame.getGameState().pickTile(44, loreto);
        mainGame.getGameState().insertTile(2, 5, loreto, 270);

        mainGame.getGameState().pickTile(1, loreto);
        mainGame.getGameState().insertTile(3, 5, loreto, 180);

        mainGame.getGameState().pickTile(142, loreto);
        mainGame.getGameState().insertTile(4, 5, loreto, 90);

        mainGame.getGameState().pickTile(35, loreto);
        mainGame.getGameState().insertTile(4, 6, loreto, 90);

        mainGame.getGameState().pickTile(28, loreto);
        mainGame.getGameState().insertTile(3, 6, loreto, 0);

        mainGame.getGameState().pickTile(68, loreto);
        mainGame.getGameState().insertTile(2, 6, loreto, 270);

//SECONDO GIOCATORE
        mainGame.getGameState().pickTile(153, giuseppe);
        mainGame.getGameState().insertTile(2, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(60, giuseppe);
        mainGame.getGameState().insertTile(3, 2, giuseppe, 0);

        mainGame.getGameState().pickTile(37, giuseppe);
        mainGame.getGameState().insertTile(4, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(138, giuseppe);
        mainGame.getGameState().insertTile(4, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(146, giuseppe);
        mainGame.getGameState().insertTile(4, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(110, giuseppe);
        mainGame.getGameState().insertTile(3, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(45, giuseppe);
        mainGame.getGameState().insertTile(2, 0, giuseppe, 0);

        mainGame.getGameState().pickTile(59, giuseppe);
        mainGame.getGameState().insertTile(2, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(19, giuseppe);
        mainGame.getGameState().insertTile(3, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(50, giuseppe);
        mainGame.getGameState().insertTile(1, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(7, giuseppe);
        mainGame.getGameState().insertTile(1, 2, giuseppe, 180);

        mainGame.getGameState().pickTile(21, giuseppe);
        mainGame.getGameState().insertTile(0, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(2, giuseppe);
        mainGame.getGameState().insertTile(1, 3, giuseppe, 180);

        mainGame.getGameState().pickTile(85, giuseppe);
        mainGame.getGameState().insertTile(3, 3, giuseppe, 0);

        mainGame.getGameState().pickTile(136, giuseppe);
        mainGame.getGameState().insertTile(2, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(25, giuseppe);
        mainGame.getGameState().insertTile(3, 4, giuseppe, 270);

        mainGame.getGameState().pickTile(79, giuseppe);
        mainGame.getGameState().insertTile(4, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(67, giuseppe);
        mainGame.getGameState().insertTile(1, 4, giuseppe, 90);

        mainGame.getGameState().pickTile(113, giuseppe);
        mainGame.getGameState().insertTile(0, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(4, giuseppe);
        mainGame.getGameState().insertTile(1, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(65, giuseppe);
        mainGame.getGameState().insertTile(2, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(57, giuseppe);
        mainGame.getGameState().insertTile(3, 5, giuseppe, 270);

        mainGame.getGameState().pickTile(83, giuseppe);
        mainGame.getGameState().insertTile(4, 5, giuseppe, 0);

        mainGame.getGameState().pickTile(87, giuseppe);
        mainGame.getGameState().insertTile(2, 6, giuseppe, 270);

        mainGame.getGameState().pickTile(70, giuseppe);
        mainGame.getGameState().insertTile(3, 6, giuseppe, 90);

        mainGame.getGameState().pickTile(99, giuseppe);
        mainGame.getGameState().insertTile(4, 6, giuseppe, 0);

//MANCA TILE IN ALTO A SINISTRA!! RICORDA DI GESTIRE ECCEZIONE

//PLAYER 3 SHIPBOARD
        mainGame.getGameState().pickTile(18, giacomo);
        mainGame.getGameState().insertTile(2, 2, giacomo, 270);

        mainGame.getGameState().pickTile(63, giacomo);
        mainGame.getGameState().insertTile(2, 1, giacomo, 0);

        mainGame.getGameState().pickTile(155, giacomo);
        mainGame.getGameState().insertTile(2, 0, giacomo, 180);

        mainGame.getGameState().pickTile(111, giacomo);
        mainGame.getGameState().insertTile(3, 3, giacomo, 180);

        mainGame.getGameState().pickTile(47, giacomo);
        mainGame.getGameState().insertTile(3, 2, giacomo, 0);

        mainGame.getGameState().pickTile(23, giacomo);
        mainGame.getGameState().insertTile(3, 1, giacomo, 0);

        mainGame.getGameState().pickTile(46, giacomo);
        mainGame.getGameState().insertTile(3, 0, giacomo, 0);

        mainGame.getGameState().pickTile(38, giacomo);
        mainGame.getGameState().insertTile(4, 0, giacomo, 90);

        mainGame.getGameState().pickTile(141, giacomo);
        mainGame.getGameState().insertTile(4, 1, giacomo, 0);

        mainGame.getGameState().pickTile(132, giacomo);
        mainGame.getGameState().insertTile(4, 2, giacomo, 180);

        mainGame.getGameState().pickTile(69, giacomo);
        mainGame.getGameState().insertTile(1, 1, giacomo, 180);

        mainGame.getGameState().pickTile(56, giacomo);
        mainGame.getGameState().insertTile(1, 2, giacomo, 270);

        mainGame.getGameState().pickTile(109, giacomo);
        mainGame.getGameState().insertTile(0, 2, giacomo, 0);

        mainGame.getGameState().pickTile(40, giacomo);
        mainGame.getGameState().insertTile(1, 3, giacomo, 90);

        mainGame.getGameState().pickTile(143, giacomo);
        mainGame.getGameState().insertTile(1, 4, giacomo, 0);

        mainGame.getGameState().pickTile(78, giacomo);
        mainGame.getGameState().insertTile(0, 4, giacomo, 270);

        mainGame.getGameState().pickTile(82, giacomo);
        mainGame.getGameState().insertTile(1, 5, giacomo, 270);

        mainGame.getGameState().pickTile(156, giacomo);
        mainGame.getGameState().insertTile(2, 4, giacomo, 0);

        mainGame.getGameState().pickTile(42, giacomo);
        mainGame.getGameState().insertTile(3, 4, giacomo, 90);

        mainGame.getGameState().pickTile(97, giacomo);
        mainGame.getGameState().insertTile(4, 4, giacomo, 0);

        mainGame.getGameState().pickTile(58, giacomo);
        mainGame.getGameState().insertTile(2, 5, giacomo, 90);

        mainGame.getGameState().pickTile(5, giacomo);
        mainGame.getGameState().insertTile(3, 5, giacomo, 0);

        mainGame.getGameState().pickTile(89, giacomo);
        mainGame.getGameState().insertTile(4, 5, giacomo, 0);

        mainGame.getGameState().pickTile(51, giacomo);
        mainGame.getGameState().insertTile(2, 6, giacomo, 270);

        mainGame.getGameState().pickTile(12, giacomo);
        mainGame.getGameState().insertTile(3, 6, giacomo, 0);

        mainGame.getGameState().pickTile(29, giacomo);
        mainGame.getGameState().insertTile(4, 6, giacomo, 90);

//PLayer 4
        mainGame.getGameState().pickTile(3, lorenzo);
        mainGame.getGameState().insertTile(2, 2, lorenzo, 270);

        mainGame.getGameState().pickTile(49, lorenzo);
        mainGame.getGameState().insertTile(2, 1, lorenzo, 270);

        mainGame.getGameState().pickTile(137, lorenzo);
        mainGame.getGameState().insertTile(2, 0, lorenzo, 0);

        mainGame.getGameState().pickTile(134, lorenzo);
        mainGame.getGameState().insertTile(3, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(30, lorenzo);
        mainGame.getGameState().insertTile(3, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(64, lorenzo);
        mainGame.getGameState().insertTile(3, 1, lorenzo, 180);

        mainGame.getGameState().pickTile(10, lorenzo);
        mainGame.getGameState().insertTile(3, 0, lorenzo, 90);

        mainGame.getGameState().pickTile(151, lorenzo);
        mainGame.getGameState().insertTile(4, 0, lorenzo, 180);

        mainGame.getGameState().pickTile(96, lorenzo);
        mainGame.getGameState().insertTile(4, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(90, lorenzo);
        mainGame.getGameState().insertTile(4, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(144, lorenzo);
        mainGame.getGameState().insertTile(1, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(86, lorenzo);
        mainGame.getGameState().insertTile(1, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(131, lorenzo);
        mainGame.getGameState().insertTile(0, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(36, lorenzo);
        mainGame.getGameState().insertTile(1, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(13, lorenzo);
        mainGame.getGameState().insertTile(1, 4, lorenzo, 270);

        mainGame.getGameState().pickTile(116, lorenzo);
        mainGame.getGameState().insertTile(0, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(43, lorenzo);
        mainGame.getGameState().insertTile(1, 5, lorenzo, 270);

        mainGame.getGameState().pickTile(152, lorenzo);
        mainGame.getGameState().insertTile(2, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(54, lorenzo);
        mainGame.getGameState().insertTile(2, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(73, lorenzo);
        mainGame.getGameState().insertTile(2, 6, lorenzo, 270);

        mainGame.getGameState().pickTile(55, lorenzo);
        mainGame.getGameState().insertTile(3, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(95, lorenzo);
        mainGame.getGameState().insertTile(4, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(119, lorenzo);
        mainGame.getGameState().insertTile(4, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(66, lorenzo);
        mainGame.getGameState().insertTile(3, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(22, lorenzo);
        mainGame.getGameState().insertTile(3, 6, lorenzo, 0);

        mainGame.getGameState().pickTile(140, lorenzo);
        mainGame.getGameState().insertTile(4, 6, lorenzo, 90);

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 1); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 2); // Secondo input
        gameController.endbuilding(mainGame.getPlayer(giacomo).getPlayerId(), 3); // Terzo input
        gameController.endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4); // Quarto input
        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());
        gameController.setNumTile(giuseppe, 0);
        gameController.setNumTile(giuseppe, 0);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.setNumTile(giuseppe, 1);
        gameController.setNumTile(lorenzo, 1);
        gameController.chooseOneSubShip(0,lorenzo);
        gameController.setNumTile(lorenzo, 0);

        //        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 1);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);

        TUIHandler tui = new TUIHandler();

        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());

        for (Player p : mainGame.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (p.getMyShip().getShipMatrix()[i][j].getType() == SSTTypes.Tile_Cabin && p.getMyShip().getShipMatrix()[i][j].getNumPassengers() == 0) {
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.BROWN, i, j);
                        } catch (Exception _) {
                        }
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.VIOLET, i, j);
                        } catch (Exception _) {
                        }
                    }
                }
            }
        }

        // PRIMA NAVE
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(3));

        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][1].getType());
        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][3].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][5].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][6].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][0].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(3));

        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][6].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(2));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(3));
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].getType());
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(3));

        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(3));

        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Double_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][3].getType());

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(3));

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(3));


        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getLostPieces());
        assertEquals(0, mainGame.getPlayer(loreto).getMyShip().getLostPieces());
        assertEquals(2, mainGame.getPlayer(giacomo).getMyShip().getLostPieces());
        assertEquals(4, mainGame.getPlayer(lorenzo).getMyShip().getLostPieces());

        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());


        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());


        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());

        //conf di base con  le batterie
        assertEquals(5, mainGame.getPlayer(lorenzo).getMyShip().getBatteriesNumber());
        assertEquals(5, mainGame.getPlayer(giacomo).getMyShip().getBatteriesNumber());
        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getBatteriesNumber());
        assertEquals(12, mainGame.getPlayer(loreto).getMyShip().getBatteriesNumber());

        FlightBoard board = mainGame.getGameFlightBoard();

        //stato, la prima causa è crew
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);
//        TUIHandler tui = new TUIHandler();
//        tui.printCard(mainGame.getGameState().getCardinuse());
        //dovrebbe in automatico aver già fatto arretrate il cl4 di 3
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());//all pos 21
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());//all pos 1
        assertEquals(3, mainGame.getPlayer(giuseppe).getPositionOnBoard());//all pos 3
        assertEquals(2, mainGame.getPlayer(loreto).getPositionOnBoard());//all pos 6
        //secondo effett deve parlare il primo giocatore, motors

        //attiva la firepower in 2,0
        ArrayList<Integer> xy = new ArrayList<>();
        xy.add(0);
        xy.add(2);
        ArrayList<ArrayList<Integer>> posDC = new ArrayList<>();
        posDC.add(xy);
        //pos motore
        //pos batteria
        ArrayList<Integer> Bxy = new ArrayList<>();
        Bxy.add(1);
        Bxy.add(2);
        Bxy.add(1);
        ArrayList<ArrayList<Integer>> Bpos = new ArrayList<>();
        Bpos.add(Bxy);
        gameController.chooseToStartMotor(mainGame.getPlayer(giuseppe), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(loreto), board, posDC, Bpos);
        //dovrebbe dirmi che non va bene perchè ho attivato un cannone
        //gli do che non attivo niente
        gameController.chooseToStartMotor(mainGame.getPlayer(loreto), board, null, Bpos);
        //stessa cosa tutti gli altri
        gameController.chooseToStartMotor(mainGame.getPlayer(giacomo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        //cl1 ha 2 cannoni, tutti gli altri 1 quindi il 2 è quello che paga
        //faccio scegliere
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneChoosingPass);
        //dovrebber dire che non sono la persona che deve parlare
        gameController.choosePassengersToLose(mainGame.getPlayer(loreto), false, null, board);
        ArrayList<Integer> passxy = new ArrayList<>();
        passxy.add(3);// la x è 0
        passxy.add(0);//la y è
        passxy.add(2);//le persone sono 2
        ArrayList<ArrayList<Integer>> posizionipasstoloose = new ArrayList<>();
        posizionipasstoloose.add(passxy);
        gameController.choosePassengersToLose(mainGame.getPlayer(giacomo), false, posizionipasstoloose, board);
        //ho finito e torno allo stato vecchio
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);
        //terza condizione, fire power, deve perderla il 2 che ha zero FP
        //faccio comunque attivare la fp a cl1 così vedo se perde le batterie (è lo stesso di prima)
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), null, Bpos);
        assertEquals(12, mainGame.getPlayer(loreto).getMyShip().getBatteriesNumber()); //prima ne ha 12
        gameController.chooseToStartFirePower(mainGame.getPlayer(loreto), posDC, Bpos);
        assertEquals(11, mainGame.getPlayer(loreto).getMyShip().getBatteriesNumber()); //poi ne ha 11
        //tutti gli altri dicono di no
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), null, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giacomo), null, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(lorenzo), null, Bpos);
        //lo stato ora è
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof MeteorCardEffectStateFROMCOMBAT);
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
        //non deve rispondere l'1 ma il 2
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        //ora deve parlare quello giusto
        ArrayList<Integer> posP2 = new ArrayList<>();
        posP2.add(2);
        posP2.add(2);
        posP2.add(1);
        posP2.add(2);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giuseppe), posP2, board);
        //vedo se ha perso la batt
        assertEquals(1, mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[1][2].getNumCharges());
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof MeteorCardEffectStateFROMCOMBAT);
        //non attivo niente
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giuseppe), null, board);
        //assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneFinal);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneFinal);
        //    assertTrue(mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[1][1].getType() == SSTTypes.Tile_NonAccesiblePlace);
        //assertTrue(mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[4][0].getType()==SSTTypes.Tile_NonAccesiblePlace);

    }

    @Test
    void cardTestWithChoosingBATT() throws Exception {
        setUp2card();

        EventBus b = mainGame.getEventBus();
        gameController = new GameController(mainGame, b);
        gameController.JoinGame("Loreto", 4);
        gameController.JoinGame("Giuseppe", 0);
        gameController.JoinGame("Giacomo", 0);
        gameController.JoinGame("Lorenzo", 0);

        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        int giacomo = mainGame.getPlayerIDFromName("Giacomo");
        int lorenzo = mainGame.getPlayerIDFromName("Lorenzo");
        gameController.activateTimer(mainGame.getPlayer(loreto));
        //PRIMO GIOCATORE
        mainGame.getGameState().pickTile(149, loreto);
        mainGame.getGameState().insertTile(2, 2, loreto, 180);

        mainGame.getGameState().pickTile(16, loreto);
        mainGame.getGameState().insertTile(2, 1, loreto, 270);

        mainGame.getGameState().pickTile(103, loreto);
        mainGame.getGameState().insertTile(2, 0, loreto, 270);

        mainGame.getGameState().pickTile(120, loreto);
        mainGame.getGameState().insertTile(1, 1, loreto, 270);

        mainGame.getGameState().pickTile(6, loreto);
        mainGame.getGameState().insertTile(1, 2, loreto, 270);

        mainGame.getGameState().pickTile(126, loreto);
        mainGame.getGameState().insertTile(0, 2, loreto, 0);

        mainGame.getGameState().pickTile(15, loreto);
        mainGame.getGameState().insertTile(1, 3, loreto, 180);

        mainGame.getGameState().pickTile(98, loreto);
        mainGame.getGameState().insertTile(3, 3, loreto, 0);

        mainGame.getGameState().pickTile(62, loreto);
        mainGame.getGameState().insertTile(3, 2, loreto, 0);

        mainGame.getGameState().pickTile(32, loreto);
        mainGame.getGameState().insertTile(3, 1, loreto, 270);

        mainGame.getGameState().pickTile(26, loreto);
        mainGame.getGameState().insertTile(3, 0, loreto, 180);

        mainGame.getGameState().pickTile(75, loreto);
        mainGame.getGameState().insertTile(4, 0, loreto, 0);

        mainGame.getGameState().pickTile(11, loreto);
        mainGame.getGameState().insertTile(4, 1, loreto, 0);

        mainGame.getGameState().pickTile(74, loreto);
        mainGame.getGameState().insertTile(4, 2, loreto, 0);

        mainGame.getGameState().pickTile(154, loreto);
        mainGame.getGameState().insertTile(2, 4, loreto, 0);

        mainGame.getGameState().pickTile(41, loreto);
        mainGame.getGameState().insertTile(3, 4, loreto, 270);

        mainGame.getGameState().pickTile(145, loreto);
        mainGame.getGameState().insertTile(4, 4, loreto, 90);

        mainGame.getGameState().pickTile(31, loreto);
        mainGame.getGameState().insertTile(1, 4, loreto, 180);

        mainGame.getGameState().pickTile(104, loreto);
        mainGame.getGameState().insertTile(0, 4, loreto, 0);

//        mainGame.getGameState().pickTile(135, loreto);
//        mainGame.getGameState().insertTile(1, 5, loreto, 0);

        mainGame.getGameState().pickTile(44, loreto);
        mainGame.getGameState().insertTile(2, 5, loreto, 270);

        mainGame.getGameState().pickTile(1, loreto);
        mainGame.getGameState().insertTile(3, 5, loreto, 180);

        mainGame.getGameState().pickTile(142, loreto);
        mainGame.getGameState().insertTile(4, 5, loreto, 90);

        mainGame.getGameState().pickTile(35, loreto);
        mainGame.getGameState().insertTile(4, 6, loreto, 90);


        mainGame.getGameState().pickTile(28, loreto);
        mainGame.getGameState().insertTile(3, 6, loreto, 0);

        mainGame.getGameState().pickTile(20, loreto);
        mainGame.getGameState().insertTile(2, 6, loreto, 90);

        mainGame.getGameState().pickTile(68, loreto);
        mainGame.getGameState().insertTile(1, 6, loreto, 270);


//SECONDO GIOCATORE
        mainGame.getGameState().pickTile(153, giuseppe);
        mainGame.getGameState().insertTile(2, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(60, giuseppe);
        mainGame.getGameState().insertTile(3, 2, giuseppe, 0);

        mainGame.getGameState().pickTile(37, giuseppe);
        mainGame.getGameState().insertTile(4, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(138, giuseppe);
        mainGame.getGameState().insertTile(4, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(146, giuseppe);
        mainGame.getGameState().insertTile(4, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(110, giuseppe);
        mainGame.getGameState().insertTile(3, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(45, giuseppe);
        mainGame.getGameState().insertTile(2, 0, giuseppe, 0);

        mainGame.getGameState().pickTile(59, giuseppe);
        mainGame.getGameState().insertTile(2, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(19, giuseppe);
        mainGame.getGameState().insertTile(3, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(50, giuseppe);
        mainGame.getGameState().insertTile(1, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(7, giuseppe);
        mainGame.getGameState().insertTile(1, 2, giuseppe, 180);

        mainGame.getGameState().pickTile(21, giuseppe);
        mainGame.getGameState().insertTile(0, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(2, giuseppe);
        mainGame.getGameState().insertTile(1, 3, giuseppe, 180);

        mainGame.getGameState().pickTile(85, giuseppe);
        mainGame.getGameState().insertTile(3, 3, giuseppe, 0);

        mainGame.getGameState().pickTile(136, giuseppe);
        mainGame.getGameState().insertTile(2, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(25, giuseppe);
        mainGame.getGameState().insertTile(3, 4, giuseppe, 270);

        mainGame.getGameState().pickTile(79, giuseppe);
        mainGame.getGameState().insertTile(4, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(67, giuseppe);
        mainGame.getGameState().insertTile(1, 4, giuseppe, 90);

        mainGame.getGameState().pickTile(113, giuseppe);
        mainGame.getGameState().insertTile(0, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(4, giuseppe);
        mainGame.getGameState().insertTile(1, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(65, giuseppe);
        mainGame.getGameState().insertTile(2, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(57, giuseppe);
        mainGame.getGameState().insertTile(3, 5, giuseppe, 270);

        mainGame.getGameState().pickTile(83, giuseppe);
        mainGame.getGameState().insertTile(4, 5, giuseppe, 0);

        mainGame.getGameState().pickTile(87, giuseppe);
        mainGame.getGameState().insertTile(2, 6, giuseppe, 270);

        mainGame.getGameState().pickTile(70, giuseppe);
        mainGame.getGameState().insertTile(3, 6, giuseppe, 90);

        mainGame.getGameState().pickTile(99, giuseppe);
        mainGame.getGameState().insertTile(4, 6, giuseppe, 0);

//MANCA TILE IN ALTO A SINISTRA!! RICORDA DI GESTIRE ECCEZIONE

//PLAYER 3 SHIPBOARD
        mainGame.getGameState().pickTile(18, giacomo);
        mainGame.getGameState().insertTile(2, 2, giacomo, 270);

        mainGame.getGameState().pickTile(63, giacomo);
        mainGame.getGameState().insertTile(2, 1, giacomo, 0);

        mainGame.getGameState().pickTile(155, giacomo);
        mainGame.getGameState().insertTile(2, 0, giacomo, 180);

        mainGame.getGameState().pickTile(111, giacomo);
        mainGame.getGameState().insertTile(3, 3, giacomo, 180);

        mainGame.getGameState().pickTile(47, giacomo);
        mainGame.getGameState().insertTile(3, 2, giacomo, 0);

        mainGame.getGameState().pickTile(23, giacomo);
        mainGame.getGameState().insertTile(3, 1, giacomo, 0);

        mainGame.getGameState().pickTile(46, giacomo);
        mainGame.getGameState().insertTile(3, 0, giacomo, 0);

        mainGame.getGameState().pickTile(38, giacomo);
        mainGame.getGameState().insertTile(4, 0, giacomo, 90);

        mainGame.getGameState().pickTile(141, giacomo);
        mainGame.getGameState().insertTile(4, 1, giacomo, 0);

        mainGame.getGameState().pickTile(132, giacomo);
        mainGame.getGameState().insertTile(4, 2, giacomo, 180);

        mainGame.getGameState().pickTile(69, giacomo);
        mainGame.getGameState().insertTile(1, 1, giacomo, 180);

        mainGame.getGameState().pickTile(56, giacomo);
        mainGame.getGameState().insertTile(1, 2, giacomo, 270);

        mainGame.getGameState().pickTile(109, giacomo);
        mainGame.getGameState().insertTile(0, 2, giacomo, 0);

        mainGame.getGameState().pickTile(40, giacomo);
        mainGame.getGameState().insertTile(1, 3, giacomo, 90);

        mainGame.getGameState().pickTile(143, giacomo);
        mainGame.getGameState().insertTile(1, 4, giacomo, 0);

        mainGame.getGameState().pickTile(78, giacomo);
        mainGame.getGameState().insertTile(0, 4, giacomo, 270);

        mainGame.getGameState().pickTile(82, giacomo);
        mainGame.getGameState().insertTile(1, 5, giacomo, 270);

        mainGame.getGameState().pickTile(156, giacomo);
        mainGame.getGameState().insertTile(2, 4, giacomo, 0);

        mainGame.getGameState().pickTile(42, giacomo);
        mainGame.getGameState().insertTile(3, 4, giacomo, 90);

        mainGame.getGameState().pickTile(97, giacomo);
        mainGame.getGameState().insertTile(4, 4, giacomo, 0);

        mainGame.getGameState().pickTile(58, giacomo);
        mainGame.getGameState().insertTile(2, 5, giacomo, 90);

        mainGame.getGameState().pickTile(5, giacomo);
        mainGame.getGameState().insertTile(3, 5, giacomo, 0);

        mainGame.getGameState().pickTile(89, giacomo);
        mainGame.getGameState().insertTile(4, 5, giacomo, 0);

        mainGame.getGameState().pickTile(51, giacomo);
        mainGame.getGameState().insertTile(2, 6, giacomo, 270);

        mainGame.getGameState().pickTile(12, giacomo);
        mainGame.getGameState().insertTile(3, 6, giacomo, 0);

        mainGame.getGameState().pickTile(29, giacomo);
        mainGame.getGameState().insertTile(4, 6, giacomo, 90);

//PLayer 4
        mainGame.getGameState().pickTile(3, lorenzo);
        mainGame.getGameState().insertTile(2, 2, lorenzo, 270);

        mainGame.getGameState().pickTile(49, lorenzo);
        mainGame.getGameState().insertTile(2, 1, lorenzo, 270);

        mainGame.getGameState().pickTile(137, lorenzo);
        mainGame.getGameState().insertTile(2, 0, lorenzo, 0);

        mainGame.getGameState().pickTile(134, lorenzo);
        mainGame.getGameState().insertTile(3, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(30, lorenzo);
        mainGame.getGameState().insertTile(3, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(64, lorenzo);
        mainGame.getGameState().insertTile(3, 1, lorenzo, 180);

        mainGame.getGameState().pickTile(10, lorenzo);
        mainGame.getGameState().insertTile(3, 0, lorenzo, 90);

        mainGame.getGameState().pickTile(151, lorenzo);
        mainGame.getGameState().insertTile(4, 0, lorenzo, 180);

        mainGame.getGameState().pickTile(96, lorenzo);
        mainGame.getGameState().insertTile(4, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(90, lorenzo);
        mainGame.getGameState().insertTile(4, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(144, lorenzo);
        mainGame.getGameState().insertTile(1, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(86, lorenzo);
        mainGame.getGameState().insertTile(1, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(131, lorenzo);
        mainGame.getGameState().insertTile(0, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(36, lorenzo);
        mainGame.getGameState().insertTile(1, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(13, lorenzo);
        mainGame.getGameState().insertTile(1, 4, lorenzo, 270);

        mainGame.getGameState().pickTile(116, lorenzo);
        mainGame.getGameState().insertTile(0, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(43, lorenzo);
        mainGame.getGameState().insertTile(1, 5, lorenzo, 270);

        mainGame.getGameState().pickTile(152, lorenzo);
        mainGame.getGameState().insertTile(2, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(54, lorenzo);
        mainGame.getGameState().insertTile(2, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(73, lorenzo);
        mainGame.getGameState().insertTile(2, 6, lorenzo, 270);

        mainGame.getGameState().pickTile(55, lorenzo);
        mainGame.getGameState().insertTile(3, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(95, lorenzo);
        mainGame.getGameState().insertTile(4, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(119, lorenzo);
        mainGame.getGameState().insertTile(4, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(66, lorenzo);
        mainGame.getGameState().insertTile(3, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(22, lorenzo);
        mainGame.getGameState().insertTile(3, 6, lorenzo, 0);

        mainGame.getGameState().pickTile(140, lorenzo);
        mainGame.getGameState().insertTile(4, 6, lorenzo, 90);

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 1); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 2); // Secondo input
        gameController.endbuilding(mainGame.getPlayer(giacomo).getPlayerId(), 3); // Terzo input
        gameController.endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4); // Quarto input
        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());
        gameController.setNumTile(giuseppe, 0);
        gameController.setNumTile(giuseppe, 0);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.setNumTile(giuseppe, 1);
        gameController.setNumTile(lorenzo, 1);
        gameController.chooseOneSubShip(0,lorenzo);
        gameController.setNumTile(lorenzo, 0);

        //        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 1);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);

        TUIHandler tui = new TUIHandler();

        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());

        for (Player p : mainGame.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (p.getMyShip().getShipMatrix()[i][j].getType() == SSTTypes.Tile_Cabin && p.getMyShip().getShipMatrix()[i][j].getNumPassengers() == 0) {
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.BROWN, i, j);
                        } catch (Exception _) {
                        }
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.VIOLET, i, j);
                        } catch (Exception _) {
                        }
                    }
                }
            }
        }

        // PRIMA NAVE
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(3));

        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][1].getType());
        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][3].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][5].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][6].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][0].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(3));
//
//        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(1));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(2));
//        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(3));
//        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][6].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(2));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(3));
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].getType());
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(3));
//
//        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(1));
//        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(2));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(3));

        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Double_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][3].getType());

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(3));

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(3));


        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getLostPieces());
        assertEquals(0, mainGame.getPlayer(loreto).getMyShip().getLostPieces());
        assertEquals(2, mainGame.getPlayer(giacomo).getMyShip().getLostPieces());
        assertEquals(4, mainGame.getPlayer(lorenzo).getMyShip().getLostPieces());

        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());


        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());


        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());

        //conf di base con  le batterie
        assertEquals(5, mainGame.getPlayer(lorenzo).getMyShip().getBatteriesNumber());
        assertEquals(5, mainGame.getPlayer(giacomo).getMyShip().getBatteriesNumber());
        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getBatteriesNumber());
        assertEquals(12, mainGame.getPlayer(loreto).getMyShip().getBatteriesNumber());

        FlightBoard board = mainGame.getGameFlightBoard();

        //stato, la prima causa è crew
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);

        //attiva la firepower in 2,0
        ArrayList<Integer> xy = new ArrayList<>();
        xy.add(0);
        xy.add(2);
        ArrayList<ArrayList<Integer>> posDC = new ArrayList<>();
        posDC.add(xy);
        //pos batteria
        ArrayList<Integer> Bxy = new ArrayList<>();
        Bxy.add(1);
        Bxy.add(2);
        Bxy.add(1);
        ArrayList<ArrayList<Integer>> Bpos = new ArrayList<>();
        Bpos.add(Bxy);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), posDC, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(loreto), posDC, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), null, null);
        //dovrebbe dirmi che non va bene perchè ho attivato un cannone
        //gli do che non attivo niente
        gameController.chooseToStartFirePower(mainGame.getPlayer(giacomo), null, null);
        //stessa cosa tutti gli altri
        gameController.chooseToStartFirePower(mainGame.getPlayer(lorenzo), null, null);

        assertEquals(6, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(21, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        //cl1 ha 2 cannoni, tutti gli altri 1 quindi il 2 è quello che paga
        //faccio scegliere
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);
        //lets see ships
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());

        gameController.chooseToStartMotor(mainGame.getPlayer(loreto), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(giacomo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(giuseppe), board, null, Bpos);

        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giacomo), null);
        ArrayList<Integer> posbatt = new ArrayList<>();
        posbatt.add(3);
        posbatt.add(6);
        posbatt.add(2);
        ArrayList<ArrayList<Integer>> posbattfin = new ArrayList<>();
        posbattfin.add(posbatt);
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giacomo), posbattfin);
        //loreto lost
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof MeteorCardEffectStateFROMCOMBAT);
        //lorenzo talks while he should not
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giacomo), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        //prendo colpo in 7 da dx dove ci stanno due ship
        System.out.println(mainGame.getGameState().getCardinuse().getState());
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof combat_fixing_state);
        gameController.chooseOneSubShip(0, giacomo);
        gameController.chooseOneSubShip(5, loreto);
        gameController.chooseOneSubShip(0, loreto);


    }

    @Test
    void cardTestWithChoosingGoods() throws Exception {
        setUp2card();

        EventBus b = mainGame.getEventBus();
        gameController = new GameController(mainGame, b);
        gameController.JoinGame("Loreto", 4);
        gameController.JoinGame("Giuseppe", 0);
        gameController.JoinGame("Giacomo", 0);
        gameController.JoinGame("Lorenzo", 0);

        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        int giacomo = mainGame.getPlayerIDFromName("Giacomo");
        int lorenzo = mainGame.getPlayerIDFromName("Lorenzo");
        gameController.activateTimer(mainGame.getPlayer(loreto));
        //PRIMO GIOCATORE
        mainGame.getGameState().pickTile(149, loreto);
        mainGame.getGameState().insertTile(2, 2, loreto, 180);

        mainGame.getGameState().pickTile(16, loreto);
        mainGame.getGameState().insertTile(2, 1, loreto, 270);

        mainGame.getGameState().pickTile(103, loreto);
        mainGame.getGameState().insertTile(2, 0, loreto, 270);

        mainGame.getGameState().pickTile(120, loreto);
        mainGame.getGameState().insertTile(1, 1, loreto, 270);

        mainGame.getGameState().pickTile(6, loreto);
        mainGame.getGameState().insertTile(1, 2, loreto, 270);

        mainGame.getGameState().pickTile(126, loreto);
        mainGame.getGameState().insertTile(0, 2, loreto, 0);

        mainGame.getGameState().pickTile(15, loreto);
        mainGame.getGameState().insertTile(1, 3, loreto, 180);

        mainGame.getGameState().pickTile(98, loreto);
        mainGame.getGameState().insertTile(3, 3, loreto, 0);

        mainGame.getGameState().pickTile(62, loreto);
        mainGame.getGameState().insertTile(3, 2, loreto, 0);

        mainGame.getGameState().pickTile(32, loreto);
        mainGame.getGameState().insertTile(3, 1, loreto, 270);

        mainGame.getGameState().pickTile(26, loreto);
        mainGame.getGameState().insertTile(3, 0, loreto, 180);

        mainGame.getGameState().pickTile(75, loreto);
        mainGame.getGameState().insertTile(4, 0, loreto, 0);

        mainGame.getGameState().pickTile(11, loreto);
        mainGame.getGameState().insertTile(4, 1, loreto, 0);

        mainGame.getGameState().pickTile(74, loreto);
        mainGame.getGameState().insertTile(4, 2, loreto, 0);

        mainGame.getGameState().pickTile(154, loreto);
        mainGame.getGameState().insertTile(2, 4, loreto, 0);

        mainGame.getGameState().pickTile(41, loreto);
        mainGame.getGameState().insertTile(3, 4, loreto, 270);

        mainGame.getGameState().pickTile(145, loreto);
        mainGame.getGameState().insertTile(4, 4, loreto, 90);

        mainGame.getGameState().pickTile(31, loreto);
        mainGame.getGameState().insertTile(1, 4, loreto, 180);

        mainGame.getGameState().pickTile(104, loreto);
        mainGame.getGameState().insertTile(0, 4, loreto, 0);

//        mainGame.getGameState().pickTile(135, loreto);
//        mainGame.getGameState().insertTile(1, 5, loreto, 0);

        mainGame.getGameState().pickTile(44, loreto);
        mainGame.getGameState().insertTile(2, 5, loreto, 270);

        mainGame.getGameState().pickTile(1, loreto);
        mainGame.getGameState().insertTile(3, 5, loreto, 180);

        mainGame.getGameState().pickTile(142, loreto);
        mainGame.getGameState().insertTile(4, 5, loreto, 90);

        mainGame.getGameState().pickTile(35, loreto);
        mainGame.getGameState().insertTile(4, 6, loreto, 90);


        mainGame.getGameState().pickTile(28, loreto);
        mainGame.getGameState().insertTile(3, 6, loreto, 0);

        mainGame.getGameState().pickTile(20, loreto);
        mainGame.getGameState().insertTile(2, 6, loreto, 90);

        mainGame.getGameState().pickTile(68, loreto);
        mainGame.getGameState().insertTile(1, 6, loreto, 270);


//SECONDO GIOCATORE
        mainGame.getGameState().pickTile(153, giuseppe);
        mainGame.getGameState().insertTile(2, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(60, giuseppe);
        mainGame.getGameState().insertTile(3, 2, giuseppe, 0);

        mainGame.getGameState().pickTile(37, giuseppe);
        mainGame.getGameState().insertTile(4, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(138, giuseppe);
        mainGame.getGameState().insertTile(4, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(146, giuseppe);
        mainGame.getGameState().insertTile(4, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(110, giuseppe);
        mainGame.getGameState().insertTile(3, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(45, giuseppe);
        mainGame.getGameState().insertTile(2, 0, giuseppe, 0);

        mainGame.getGameState().pickTile(59, giuseppe);
        mainGame.getGameState().insertTile(2, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(19, giuseppe);
        mainGame.getGameState().insertTile(3, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(50, giuseppe);
        mainGame.getGameState().insertTile(1, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(7, giuseppe);
        mainGame.getGameState().insertTile(1, 2, giuseppe, 180);

        mainGame.getGameState().pickTile(21, giuseppe);
        mainGame.getGameState().insertTile(0, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(2, giuseppe);
        mainGame.getGameState().insertTile(1, 3, giuseppe, 180);

        mainGame.getGameState().pickTile(85, giuseppe);
        mainGame.getGameState().insertTile(3, 3, giuseppe, 0);

        mainGame.getGameState().pickTile(136, giuseppe);
        mainGame.getGameState().insertTile(2, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(25, giuseppe);
        mainGame.getGameState().insertTile(3, 4, giuseppe, 270);

        mainGame.getGameState().pickTile(79, giuseppe);
        mainGame.getGameState().insertTile(4, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(67, giuseppe);
        mainGame.getGameState().insertTile(1, 4, giuseppe, 90);

        mainGame.getGameState().pickTile(113, giuseppe);
        mainGame.getGameState().insertTile(0, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(4, giuseppe);
        mainGame.getGameState().insertTile(1, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(65, giuseppe);
        mainGame.getGameState().insertTile(2, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(57, giuseppe);
        mainGame.getGameState().insertTile(3, 5, giuseppe, 270);

        mainGame.getGameState().pickTile(83, giuseppe);
        mainGame.getGameState().insertTile(4, 5, giuseppe, 0);

        mainGame.getGameState().pickTile(87, giuseppe);
        mainGame.getGameState().insertTile(2, 6, giuseppe, 270);

        mainGame.getGameState().pickTile(70, giuseppe);
        mainGame.getGameState().insertTile(3, 6, giuseppe, 90);

        mainGame.getGameState().pickTile(99, giuseppe);
        mainGame.getGameState().insertTile(4, 6, giuseppe, 0);

//MANCA TILE IN ALTO A SINISTRA!! RICORDA DI GESTIRE ECCEZIONE

//PLAYER 3 SHIPBOARD
        mainGame.getGameState().pickTile(18, giacomo);
        mainGame.getGameState().insertTile(2, 2, giacomo, 270);

        mainGame.getGameState().pickTile(63, giacomo);
        mainGame.getGameState().insertTile(2, 1, giacomo, 0);

        mainGame.getGameState().pickTile(155, giacomo);
        mainGame.getGameState().insertTile(2, 0, giacomo, 180);

        mainGame.getGameState().pickTile(111, giacomo);
        mainGame.getGameState().insertTile(3, 3, giacomo, 180);

        mainGame.getGameState().pickTile(47, giacomo);
        mainGame.getGameState().insertTile(3, 2, giacomo, 0);

        mainGame.getGameState().pickTile(23, giacomo);
        mainGame.getGameState().insertTile(3, 1, giacomo, 0);

        mainGame.getGameState().pickTile(46, giacomo);
        mainGame.getGameState().insertTile(3, 0, giacomo, 0);

        mainGame.getGameState().pickTile(38, giacomo);
        mainGame.getGameState().insertTile(4, 0, giacomo, 90);

        mainGame.getGameState().pickTile(141, giacomo);
        mainGame.getGameState().insertTile(4, 1, giacomo, 0);

        mainGame.getGameState().pickTile(132, giacomo);
        mainGame.getGameState().insertTile(4, 2, giacomo, 180);

        mainGame.getGameState().pickTile(69, giacomo);
        mainGame.getGameState().insertTile(1, 1, giacomo, 180);

        mainGame.getGameState().pickTile(56, giacomo);
        mainGame.getGameState().insertTile(1, 2, giacomo, 270);

        mainGame.getGameState().pickTile(109, giacomo);
        mainGame.getGameState().insertTile(0, 2, giacomo, 0);

        mainGame.getGameState().pickTile(40, giacomo);
        mainGame.getGameState().insertTile(1, 3, giacomo, 90);

        mainGame.getGameState().pickTile(143, giacomo);
        mainGame.getGameState().insertTile(1, 4, giacomo, 0);

        mainGame.getGameState().pickTile(78, giacomo);
        mainGame.getGameState().insertTile(0, 4, giacomo, 270);

        mainGame.getGameState().pickTile(82, giacomo);
        mainGame.getGameState().insertTile(1, 5, giacomo, 270);

        mainGame.getGameState().pickTile(156, giacomo);
        mainGame.getGameState().insertTile(2, 4, giacomo, 0);

        mainGame.getGameState().pickTile(42, giacomo);
        mainGame.getGameState().insertTile(3, 4, giacomo, 90);

        mainGame.getGameState().pickTile(97, giacomo);
        mainGame.getGameState().insertTile(4, 4, giacomo, 0);

        mainGame.getGameState().pickTile(58, giacomo);
        mainGame.getGameState().insertTile(2, 5, giacomo, 90);

        mainGame.getGameState().pickTile(5, giacomo);
        mainGame.getGameState().insertTile(3, 5, giacomo, 0);

        mainGame.getGameState().pickTile(89, giacomo);
        mainGame.getGameState().insertTile(4, 5, giacomo, 0);

        mainGame.getGameState().pickTile(51, giacomo);
        mainGame.getGameState().insertTile(2, 6, giacomo, 270);

        mainGame.getGameState().pickTile(12, giacomo);
        mainGame.getGameState().insertTile(3, 6, giacomo, 0);

        mainGame.getGameState().pickTile(29, giacomo);
        mainGame.getGameState().insertTile(4, 6, giacomo, 90);

//PLayer 4
        mainGame.getGameState().pickTile(3, lorenzo);
        mainGame.getGameState().insertTile(2, 2, lorenzo, 270);

        mainGame.getGameState().pickTile(49, lorenzo);
        mainGame.getGameState().insertTile(2, 1, lorenzo, 270);

        mainGame.getGameState().pickTile(137, lorenzo);
        mainGame.getGameState().insertTile(2, 0, lorenzo, 0);

        mainGame.getGameState().pickTile(134, lorenzo);
        mainGame.getGameState().insertTile(3, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(30, lorenzo);
        mainGame.getGameState().insertTile(3, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(64, lorenzo);
        mainGame.getGameState().insertTile(3, 1, lorenzo, 180);

        mainGame.getGameState().pickTile(10, lorenzo);
        mainGame.getGameState().insertTile(3, 0, lorenzo, 90);

        mainGame.getGameState().pickTile(151, lorenzo);
        mainGame.getGameState().insertTile(4, 0, lorenzo, 180);

        mainGame.getGameState().pickTile(96, lorenzo);
        mainGame.getGameState().insertTile(4, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(90, lorenzo);
        mainGame.getGameState().insertTile(4, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(144, lorenzo);
        mainGame.getGameState().insertTile(1, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(86, lorenzo);
        mainGame.getGameState().insertTile(1, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(131, lorenzo);
        mainGame.getGameState().insertTile(0, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(36, lorenzo);
        mainGame.getGameState().insertTile(1, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(13, lorenzo);
        mainGame.getGameState().insertTile(1, 4, lorenzo, 270);

        mainGame.getGameState().pickTile(116, lorenzo);
        mainGame.getGameState().insertTile(0, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(43, lorenzo);
        mainGame.getGameState().insertTile(1, 5, lorenzo, 270);

        mainGame.getGameState().pickTile(152, lorenzo);
        mainGame.getGameState().insertTile(2, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(54, lorenzo);
        mainGame.getGameState().insertTile(2, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(73, lorenzo);
        mainGame.getGameState().insertTile(2, 6, lorenzo, 270);

        mainGame.getGameState().pickTile(55, lorenzo);
        mainGame.getGameState().insertTile(3, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(95, lorenzo);
        mainGame.getGameState().insertTile(4, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(119, lorenzo);
        mainGame.getGameState().insertTile(4, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(66, lorenzo);
        mainGame.getGameState().insertTile(3, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(22, lorenzo);
        mainGame.getGameState().insertTile(3, 6, lorenzo, 0);

        mainGame.getGameState().pickTile(140, lorenzo);
        mainGame.getGameState().insertTile(4, 6, lorenzo, 90);

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 1); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 2); // Secondo input
        gameController.endbuilding(mainGame.getPlayer(giacomo).getPlayerId(), 3); // Terzo input
        gameController.endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4); // Quarto input
        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());
        gameController.setNumTile(giuseppe, 0);
        gameController.setNumTile(giuseppe, 0);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.setNumTile(giuseppe, 1);
        gameController.setNumTile(lorenzo, 1);
        gameController.chooseOneSubShip(0,lorenzo);
        gameController.setNumTile(lorenzo, 0);

        //        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 1);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);

        TUIHandler tui = new TUIHandler();

        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());

        for (Player p : mainGame.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (p.getMyShip().getShipMatrix()[i][j].getType() == SSTTypes.Tile_Cabin && p.getMyShip().getShipMatrix()[i][j].getNumPassengers() == 0) {
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.BROWN, i, j);
                        } catch (Exception _) {
                        }
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.VIOLET, i, j);
                        } catch (Exception _) {
                        }
                    }
                }
            }
        }

        // PRIMA NAVE
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(3));

        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][1].getType());
        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][3].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][5].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][6].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][0].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(3));
//
//        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(1));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(2));
//        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(3));
//        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][6].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(2));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(3));
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].getType());
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(3));
//
//        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(1));
//        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(2));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(3));

        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Double_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][3].getType());

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(3));

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(3));


        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getLostPieces());
        assertEquals(0, mainGame.getPlayer(loreto).getMyShip().getLostPieces());
        assertEquals(2, mainGame.getPlayer(giacomo).getMyShip().getLostPieces());
        assertEquals(4, mainGame.getPlayer(lorenzo).getMyShip().getLostPieces());

        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());


        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());


        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());

        //conf di base con  le batterie
        assertEquals(5, mainGame.getPlayer(lorenzo).getMyShip().getBatteriesNumber());
        assertEquals(5, mainGame.getPlayer(giacomo).getMyShip().getBatteriesNumber());
        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getBatteriesNumber());
        assertEquals(12, mainGame.getPlayer(loreto).getMyShip().getBatteriesNumber());

        FlightBoard board = mainGame.getGameFlightBoard();

        //stato, la prima causa è crew
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);

        //attiva la firepower in 2,0
        ArrayList<Integer> xy = new ArrayList<>();
        xy.add(0);
        xy.add(2);
        ArrayList<ArrayList<Integer>> posDC = new ArrayList<>();
        posDC.add(xy);
        //pos batteria
        ArrayList<Integer> Bxy = new ArrayList<>();
        Bxy.add(1);
        Bxy.add(2);
        Bxy.add(1);
        ArrayList<ArrayList<Integer>> Bpos = new ArrayList<>();
        Bpos.add(Bxy);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), posDC, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(loreto), posDC, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), null, null);
        //dovrebbe dirmi che non va bene perchè ho attivato un cannone
        //gli do che non attivo niente
        gameController.chooseToStartFirePower(mainGame.getPlayer(giacomo), null, null);
        //stessa cosa tutti gli altri
        gameController.chooseToStartFirePower(mainGame.getPlayer(lorenzo), null, null);

        ArrayList<Goods> g = new ArrayList<>();
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().activate(board);
        });

        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(null, null, board);
        });


        assertEquals(6, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(21, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        //cl1 ha 2 cannoni, tutti gli altri 1 quindi il 2 è quello che paga
        //faccio scegliere
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);
        //lets see ships
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(null, null, board);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseCannonBatteryPos(null, null, null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseHowToFaceMeteors(null, null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseToPlaceBatteries(null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseToClaimReward(null, true, null, null, null);
        });
        gameController.getCardinuse().getCrewLost();
        gameController.getCardinuse().getMoneyGained();


        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//da dei goods
        mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[4][6].addGoods(new Goods(1, false));
        mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[4][6].addGoods(new Goods(1, false));
        mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[4][6].addGoods(new Goods(1, false));
        mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[3][1].addGoods(new Goods(1, false));
        gameController.chooseToStartMotor(mainGame.getPlayer(loreto), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(giacomo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(giuseppe), board, null, Bpos);
        ArrayList<Integer> posgood = new ArrayList<>();
        posgood.add(4);
        posgood.add(6);
        ArrayList<ArrayList<Integer>> posbattfin = new ArrayList<>();
        posbattfin.add(posgood);
        ArrayList<Goods> goodarray = new ArrayList<>();
        Goods guu = new Goods(1, false);
        goodarray.add(guu);
        ArrayList<ArrayList<Goods>> listgood = new ArrayList<>();
        listgood.add(goodarray);
        gameController.chooseWhereToPutGoods(mainGame.getPlayer(giacomo), posbattfin, listgood);

        //loreto lost
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof MeteorCardEffectStateFROMCOMBAT);
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(board);
        });
        //lorenzo talks while he should not
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        //prendo colpo in 7 da dx dove ci stanno due ship
        //assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof fixing_state);


    }

    @Test
    void cardTestWithChoosinglostallGoods() throws Exception {
        setUp2card();

        EventBus b = mainGame.getEventBus();
        gameController = new GameController(mainGame, b);
        gameController.JoinGame("Loreto", 4);
        gameController.JoinGame("Giuseppe", 0);
        gameController.JoinGame("Giacomo", 0);
        gameController.JoinGame("Lorenzo", 0);

        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        int giacomo = mainGame.getPlayerIDFromName("Giacomo");
        int lorenzo = mainGame.getPlayerIDFromName("Lorenzo");
        gameController.activateTimer(mainGame.getPlayer(loreto));
        //PRIMO GIOCATORE
        mainGame.getGameState().pickTile(149, loreto);
        mainGame.getGameState().insertTile(2, 2, loreto, 180);

        mainGame.getGameState().pickTile(16, loreto);
        mainGame.getGameState().insertTile(2, 1, loreto, 270);

        mainGame.getGameState().pickTile(103, loreto);
        mainGame.getGameState().insertTile(2, 0, loreto, 270);

        mainGame.getGameState().pickTile(120, loreto);
        mainGame.getGameState().insertTile(1, 1, loreto, 270);

        mainGame.getGameState().pickTile(6, loreto);
        mainGame.getGameState().insertTile(1, 2, loreto, 270);

        mainGame.getGameState().pickTile(126, loreto);
        mainGame.getGameState().insertTile(0, 2, loreto, 0);

        mainGame.getGameState().pickTile(15, loreto);
        mainGame.getGameState().insertTile(1, 3, loreto, 180);

        mainGame.getGameState().pickTile(98, loreto);
        mainGame.getGameState().insertTile(3, 3, loreto, 0);

        mainGame.getGameState().pickTile(62, loreto);
        mainGame.getGameState().insertTile(3, 2, loreto, 0);

        mainGame.getGameState().pickTile(32, loreto);
        mainGame.getGameState().insertTile(3, 1, loreto, 270);

        mainGame.getGameState().pickTile(26, loreto);
        mainGame.getGameState().insertTile(3, 0, loreto, 180);

        mainGame.getGameState().pickTile(75, loreto);
        mainGame.getGameState().insertTile(4, 0, loreto, 0);

        mainGame.getGameState().pickTile(11, loreto);
        mainGame.getGameState().insertTile(4, 1, loreto, 0);

        mainGame.getGameState().pickTile(74, loreto);
        mainGame.getGameState().insertTile(4, 2, loreto, 0);

        mainGame.getGameState().pickTile(154, loreto);
        mainGame.getGameState().insertTile(2, 4, loreto, 0);

        mainGame.getGameState().pickTile(41, loreto);
        mainGame.getGameState().insertTile(3, 4, loreto, 270);

        mainGame.getGameState().pickTile(145, loreto);
        mainGame.getGameState().insertTile(4, 4, loreto, 90);

        mainGame.getGameState().pickTile(31, loreto);
        mainGame.getGameState().insertTile(1, 4, loreto, 180);

        mainGame.getGameState().pickTile(104, loreto);
        mainGame.getGameState().insertTile(0, 4, loreto, 0);

//        mainGame.getGameState().pickTile(135, loreto);
//        mainGame.getGameState().insertTile(1, 5, loreto, 0);

        mainGame.getGameState().pickTile(44, loreto);
        mainGame.getGameState().insertTile(2, 5, loreto, 270);

        mainGame.getGameState().pickTile(1, loreto);
        mainGame.getGameState().insertTile(3, 5, loreto, 180);

        mainGame.getGameState().pickTile(142, loreto);
        mainGame.getGameState().insertTile(4, 5, loreto, 90);

        mainGame.getGameState().pickTile(35, loreto);
        mainGame.getGameState().insertTile(4, 6, loreto, 90);


        mainGame.getGameState().pickTile(28, loreto);
        mainGame.getGameState().insertTile(3, 6, loreto, 0);

        mainGame.getGameState().pickTile(20, loreto);
        mainGame.getGameState().insertTile(2, 6, loreto, 90);

        mainGame.getGameState().pickTile(68, loreto);
        mainGame.getGameState().insertTile(1, 6, loreto, 270);


//SECONDO GIOCATORE
        mainGame.getGameState().pickTile(153, giuseppe);
        mainGame.getGameState().insertTile(2, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(60, giuseppe);
        mainGame.getGameState().insertTile(3, 2, giuseppe, 0);

        mainGame.getGameState().pickTile(37, giuseppe);
        mainGame.getGameState().insertTile(4, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(138, giuseppe);
        mainGame.getGameState().insertTile(4, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(146, giuseppe);
        mainGame.getGameState().insertTile(4, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(110, giuseppe);
        mainGame.getGameState().insertTile(3, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(45, giuseppe);
        mainGame.getGameState().insertTile(2, 0, giuseppe, 0);

        mainGame.getGameState().pickTile(59, giuseppe);
        mainGame.getGameState().insertTile(2, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(19, giuseppe);
        mainGame.getGameState().insertTile(3, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(50, giuseppe);
        mainGame.getGameState().insertTile(1, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(7, giuseppe);
        mainGame.getGameState().insertTile(1, 2, giuseppe, 180);

        mainGame.getGameState().pickTile(21, giuseppe);
        mainGame.getGameState().insertTile(0, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(2, giuseppe);
        mainGame.getGameState().insertTile(1, 3, giuseppe, 180);

        mainGame.getGameState().pickTile(85, giuseppe);
        mainGame.getGameState().insertTile(3, 3, giuseppe, 0);

        mainGame.getGameState().pickTile(136, giuseppe);
        mainGame.getGameState().insertTile(2, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(25, giuseppe);
        mainGame.getGameState().insertTile(3, 4, giuseppe, 270);

        mainGame.getGameState().pickTile(79, giuseppe);
        mainGame.getGameState().insertTile(4, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(67, giuseppe);
        mainGame.getGameState().insertTile(1, 4, giuseppe, 90);

        mainGame.getGameState().pickTile(113, giuseppe);
        mainGame.getGameState().insertTile(0, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(4, giuseppe);
        mainGame.getGameState().insertTile(1, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(65, giuseppe);
        mainGame.getGameState().insertTile(2, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(57, giuseppe);
        mainGame.getGameState().insertTile(3, 5, giuseppe, 270);

        mainGame.getGameState().pickTile(83, giuseppe);
        mainGame.getGameState().insertTile(4, 5, giuseppe, 0);

        mainGame.getGameState().pickTile(87, giuseppe);
        mainGame.getGameState().insertTile(2, 6, giuseppe, 270);

        mainGame.getGameState().pickTile(70, giuseppe);
        mainGame.getGameState().insertTile(3, 6, giuseppe, 90);

        mainGame.getGameState().pickTile(99, giuseppe);
        mainGame.getGameState().insertTile(4, 6, giuseppe, 0);

//MANCA TILE IN ALTO A SINISTRA!! RICORDA DI GESTIRE ECCEZIONE

//PLAYER 3 SHIPBOARD
        mainGame.getGameState().pickTile(18, giacomo);
        mainGame.getGameState().insertTile(2, 2, giacomo, 270);

        mainGame.getGameState().pickTile(63, giacomo);
        mainGame.getGameState().insertTile(2, 1, giacomo, 0);

        mainGame.getGameState().pickTile(155, giacomo);
        mainGame.getGameState().insertTile(2, 0, giacomo, 180);

        mainGame.getGameState().pickTile(111, giacomo);
        mainGame.getGameState().insertTile(3, 3, giacomo, 180);

        mainGame.getGameState().pickTile(47, giacomo);
        mainGame.getGameState().insertTile(3, 2, giacomo, 0);

        mainGame.getGameState().pickTile(23, giacomo);
        mainGame.getGameState().insertTile(3, 1, giacomo, 0);

        mainGame.getGameState().pickTile(46, giacomo);
        mainGame.getGameState().insertTile(3, 0, giacomo, 0);

        mainGame.getGameState().pickTile(38, giacomo);
        mainGame.getGameState().insertTile(4, 0, giacomo, 90);

        mainGame.getGameState().pickTile(141, giacomo);
        mainGame.getGameState().insertTile(4, 1, giacomo, 0);

        mainGame.getGameState().pickTile(132, giacomo);
        mainGame.getGameState().insertTile(4, 2, giacomo, 180);

        mainGame.getGameState().pickTile(69, giacomo);
        mainGame.getGameState().insertTile(1, 1, giacomo, 180);

        mainGame.getGameState().pickTile(56, giacomo);
        mainGame.getGameState().insertTile(1, 2, giacomo, 270);

        mainGame.getGameState().pickTile(109, giacomo);
        mainGame.getGameState().insertTile(0, 2, giacomo, 0);

        mainGame.getGameState().pickTile(40, giacomo);
        mainGame.getGameState().insertTile(1, 3, giacomo, 90);

        mainGame.getGameState().pickTile(143, giacomo);
        mainGame.getGameState().insertTile(1, 4, giacomo, 0);

        mainGame.getGameState().pickTile(78, giacomo);
        mainGame.getGameState().insertTile(0, 4, giacomo, 270);

        mainGame.getGameState().pickTile(82, giacomo);
        mainGame.getGameState().insertTile(1, 5, giacomo, 270);

        mainGame.getGameState().pickTile(156, giacomo);
        mainGame.getGameState().insertTile(2, 4, giacomo, 0);

        mainGame.getGameState().pickTile(42, giacomo);
        mainGame.getGameState().insertTile(3, 4, giacomo, 90);

        mainGame.getGameState().pickTile(97, giacomo);
        mainGame.getGameState().insertTile(4, 4, giacomo, 0);

        mainGame.getGameState().pickTile(58, giacomo);
        mainGame.getGameState().insertTile(2, 5, giacomo, 90);

        mainGame.getGameState().pickTile(5, giacomo);
        mainGame.getGameState().insertTile(3, 5, giacomo, 0);

        mainGame.getGameState().pickTile(89, giacomo);
        mainGame.getGameState().insertTile(4, 5, giacomo, 0);

        mainGame.getGameState().pickTile(51, giacomo);
        mainGame.getGameState().insertTile(2, 6, giacomo, 270);

        mainGame.getGameState().pickTile(12, giacomo);
        mainGame.getGameState().insertTile(3, 6, giacomo, 0);

        mainGame.getGameState().pickTile(29, giacomo);
        mainGame.getGameState().insertTile(4, 6, giacomo, 90);

//PLayer 4
        mainGame.getGameState().pickTile(3, lorenzo);
        mainGame.getGameState().insertTile(2, 2, lorenzo, 270);

        mainGame.getGameState().pickTile(49, lorenzo);
        mainGame.getGameState().insertTile(2, 1, lorenzo, 270);

        mainGame.getGameState().pickTile(137, lorenzo);
        mainGame.getGameState().insertTile(2, 0, lorenzo, 0);

        mainGame.getGameState().pickTile(134, lorenzo);
        mainGame.getGameState().insertTile(3, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(30, lorenzo);
        mainGame.getGameState().insertTile(3, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(64, lorenzo);
        mainGame.getGameState().insertTile(3, 1, lorenzo, 180);

        mainGame.getGameState().pickTile(10, lorenzo);
        mainGame.getGameState().insertTile(3, 0, lorenzo, 90);

        mainGame.getGameState().pickTile(151, lorenzo);
        mainGame.getGameState().insertTile(4, 0, lorenzo, 180);

        mainGame.getGameState().pickTile(96, lorenzo);
        mainGame.getGameState().insertTile(4, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(90, lorenzo);
        mainGame.getGameState().insertTile(4, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(144, lorenzo);
        mainGame.getGameState().insertTile(1, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(86, lorenzo);
        mainGame.getGameState().insertTile(1, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(131, lorenzo);
        mainGame.getGameState().insertTile(0, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(36, lorenzo);
        mainGame.getGameState().insertTile(1, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(13, lorenzo);
        mainGame.getGameState().insertTile(1, 4, lorenzo, 270);

        mainGame.getGameState().pickTile(116, lorenzo);
        mainGame.getGameState().insertTile(0, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(43, lorenzo);
        mainGame.getGameState().insertTile(1, 5, lorenzo, 270);

        mainGame.getGameState().pickTile(152, lorenzo);
        mainGame.getGameState().insertTile(2, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(54, lorenzo);
        mainGame.getGameState().insertTile(2, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(73, lorenzo);
        mainGame.getGameState().insertTile(2, 6, lorenzo, 270);

        mainGame.getGameState().pickTile(55, lorenzo);
        mainGame.getGameState().insertTile(3, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(95, lorenzo);
        mainGame.getGameState().insertTile(4, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(119, lorenzo);
        mainGame.getGameState().insertTile(4, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(66, lorenzo);
        mainGame.getGameState().insertTile(3, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(22, lorenzo);
        mainGame.getGameState().insertTile(3, 6, lorenzo, 0);

        mainGame.getGameState().pickTile(140, lorenzo);
        mainGame.getGameState().insertTile(4, 6, lorenzo, 90);

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 1); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 2); // Secondo input
        gameController.endbuilding(mainGame.getPlayer(giacomo).getPlayerId(), 3); // Terzo input
        gameController.endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4); // Quarto input
        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());
        gameController.setNumTile(giuseppe, 0);
        gameController.setNumTile(giuseppe, 0);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.setNumTile(giuseppe, 1);
        gameController.setNumTile(lorenzo, 1);
        gameController.chooseOneSubShip(0,lorenzo);
        gameController.setNumTile(lorenzo, 0);

        //        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 1);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);

        TUIHandler tui = new TUIHandler();

        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());

        for (Player p : mainGame.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (p.getMyShip().getShipMatrix()[i][j].getType() == SSTTypes.Tile_Cabin && p.getMyShip().getShipMatrix()[i][j].getNumPassengers() == 0) {
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.BROWN, i, j);
                        } catch (Exception _) {
                        }
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.VIOLET, i, j);
                        } catch (Exception _) {
                        }
                    }
                }
            }
        }

        // PRIMA NAVE
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(3));

        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][1].getType());
        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][3].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][5].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][6].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][0].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(3));
//
//        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(1));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(2));
//        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(3));
//        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][6].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(2));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(3));
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].getType());
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(3));
//
//        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(1));
//        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(2));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(3));

        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Double_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][3].getType());

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(3));

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(3));


        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getLostPieces());
        assertEquals(0, mainGame.getPlayer(loreto).getMyShip().getLostPieces());
        assertEquals(2, mainGame.getPlayer(giacomo).getMyShip().getLostPieces());
        assertEquals(4, mainGame.getPlayer(lorenzo).getMyShip().getLostPieces());

        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());


        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());


        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());

        //conf di base con  le batterie
        assertEquals(5, mainGame.getPlayer(lorenzo).getMyShip().getBatteriesNumber());
        assertEquals(5, mainGame.getPlayer(giacomo).getMyShip().getBatteriesNumber());
        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getBatteriesNumber());
        assertEquals(12, mainGame.getPlayer(loreto).getMyShip().getBatteriesNumber());

        FlightBoard board = mainGame.getGameFlightBoard();

        //stato, la prima causa è crew
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);

        //attiva la firepower in 2,0
        ArrayList<Integer> xy = new ArrayList<>();
        xy.add(0);
        xy.add(2);
        ArrayList<ArrayList<Integer>> posDC = new ArrayList<>();
        posDC.add(xy);
        //pos batteria
        ArrayList<Integer> Bxy = new ArrayList<>();
        Bxy.add(1);
        Bxy.add(2);
        Bxy.add(1);
        ArrayList<ArrayList<Integer>> Bpos = new ArrayList<>();
        Bpos.add(Bxy);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), posDC, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(loreto), posDC, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), null, null);
        //dovrebbe dirmi che non va bene perchè ho attivato un cannone
        //gli do che non attivo niente
        gameController.chooseToStartFirePower(mainGame.getPlayer(giacomo), null, null);
        //stessa cosa tutti gli altri
        gameController.chooseToStartFirePower(mainGame.getPlayer(lorenzo), null, null);

        ArrayList<Goods> g = new ArrayList<>();
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().activate(board);
        });

        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(null, null, board);
        });


        assertEquals(6, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(21, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        //cl1 ha 2 cannoni, tutti gli altri 1 quindi il 2 è quello che paga
        //faccio scegliere
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);
        //lets see ships
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(null, null, board);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseCannonBatteryPos(null, null, null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseHowToFaceMeteors(null, null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseToPlaceBatteries(null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseToClaimReward(null, true, null, null, null);
        });
        gameController.getCardinuse().getCrewLost();
        gameController.getCardinuse().getMoneyGained();


        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//da dei goods
        mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[4][6].addGoods(new Goods(1, false));
        mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[4][6].addGoods(new Goods(1, false));
        mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[4][6].addGoods(new Goods(1, false));

        gameController.chooseToStartMotor(mainGame.getPlayer(loreto), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(giacomo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(giuseppe), board, null, Bpos);
        ArrayList<Integer> posgood = new ArrayList<>();
        posgood.add(4);
        posgood.add(6);
        ArrayList<ArrayList<Integer>> posbattfin = new ArrayList<>();
        posbattfin.add(posgood);
        ArrayList<Goods> goodarray = new ArrayList<>();
        Goods guu = new Goods(1, false);
        goodarray.add(guu);
        ArrayList<ArrayList<Goods>> listgood = new ArrayList<>();
        listgood.add(goodarray);
        //gameController.chooseWhereToPutGoods(mainGame.getPlayer(giacomo),posbattfin,listgood);

        //loreto lost
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof MeteorCardEffectStateFROMCOMBAT);
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(board);
        });
        //lorenzo talks while he should not
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        //prendo colpo in 7 da dx dove ci stanno due ship
        //assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof fixing_state);


    }

    @Test
    void cardTestWithChoosinglostallbatt() throws Exception {
        setUp2card();

        EventBus b = mainGame.getEventBus();
        gameController = new GameController(mainGame, b);
        gameController.JoinGame("Loreto", 4);
        gameController.JoinGame("Giuseppe", 0);
        gameController.JoinGame("Giacomo", 0);
        gameController.JoinGame("Lorenzo", 0);

        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        int giacomo = mainGame.getPlayerIDFromName("Giacomo");
        int lorenzo = mainGame.getPlayerIDFromName("Lorenzo");
        gameController.activateTimer(mainGame.getPlayer(loreto));
        //PRIMO GIOCATORE
        mainGame.getGameState().pickTile(149, loreto);
        mainGame.getGameState().insertTile(2, 2, loreto, 180);

        mainGame.getGameState().pickTile(16, loreto);
        mainGame.getGameState().insertTile(2, 1, loreto, 270);

        mainGame.getGameState().pickTile(103, loreto);
        mainGame.getGameState().insertTile(2, 0, loreto, 270);

        mainGame.getGameState().pickTile(120, loreto);
        mainGame.getGameState().insertTile(1, 1, loreto, 270);

        mainGame.getGameState().pickTile(6, loreto);
        mainGame.getGameState().insertTile(1, 2, loreto, 270);

        mainGame.getGameState().pickTile(126, loreto);
        mainGame.getGameState().insertTile(0, 2, loreto, 0);

        mainGame.getGameState().pickTile(15, loreto);
        mainGame.getGameState().insertTile(1, 3, loreto, 180);

        mainGame.getGameState().pickTile(98, loreto);
        mainGame.getGameState().insertTile(3, 3, loreto, 0);

        mainGame.getGameState().pickTile(62, loreto);
        mainGame.getGameState().insertTile(3, 2, loreto, 0);

        mainGame.getGameState().pickTile(32, loreto);
        mainGame.getGameState().insertTile(3, 1, loreto, 270);

        mainGame.getGameState().pickTile(26, loreto);
        mainGame.getGameState().insertTile(3, 0, loreto, 180);

        mainGame.getGameState().pickTile(75, loreto);
        mainGame.getGameState().insertTile(4, 0, loreto, 0);

        mainGame.getGameState().pickTile(11, loreto);
        mainGame.getGameState().insertTile(4, 1, loreto, 0);

        mainGame.getGameState().pickTile(74, loreto);
        mainGame.getGameState().insertTile(4, 2, loreto, 0);

        mainGame.getGameState().pickTile(154, loreto);
        mainGame.getGameState().insertTile(2, 4, loreto, 0);

        mainGame.getGameState().pickTile(41, loreto);
        mainGame.getGameState().insertTile(3, 4, loreto, 270);

        mainGame.getGameState().pickTile(145, loreto);
        mainGame.getGameState().insertTile(4, 4, loreto, 90);

        mainGame.getGameState().pickTile(31, loreto);
        mainGame.getGameState().insertTile(1, 4, loreto, 180);

        mainGame.getGameState().pickTile(104, loreto);
        mainGame.getGameState().insertTile(0, 4, loreto, 0);

//        mainGame.getGameState().pickTile(135, loreto);
//        mainGame.getGameState().insertTile(1, 5, loreto, 0);

        mainGame.getGameState().pickTile(44, loreto);
        mainGame.getGameState().insertTile(2, 5, loreto, 270);

        mainGame.getGameState().pickTile(1, loreto);
        mainGame.getGameState().insertTile(3, 5, loreto, 180);

        mainGame.getGameState().pickTile(142, loreto);
        mainGame.getGameState().insertTile(4, 5, loreto, 90);

        mainGame.getGameState().pickTile(35, loreto);
        mainGame.getGameState().insertTile(4, 6, loreto, 90);


        mainGame.getGameState().pickTile(28, loreto);
        mainGame.getGameState().insertTile(3, 6, loreto, 0);

        mainGame.getGameState().pickTile(20, loreto);
        mainGame.getGameState().insertTile(2, 6, loreto, 90);

        mainGame.getGameState().pickTile(68, loreto);
        mainGame.getGameState().insertTile(1, 6, loreto, 270);


//SECONDO GIOCATORE
        mainGame.getGameState().pickTile(153, giuseppe);
        mainGame.getGameState().insertTile(2, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(60, giuseppe);
        mainGame.getGameState().insertTile(3, 2, giuseppe, 0);

        mainGame.getGameState().pickTile(37, giuseppe);
        mainGame.getGameState().insertTile(4, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(138, giuseppe);
        mainGame.getGameState().insertTile(4, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(146, giuseppe);
        mainGame.getGameState().insertTile(4, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(110, giuseppe);
        mainGame.getGameState().insertTile(3, 0, giuseppe, 180);

        mainGame.getGameState().pickTile(45, giuseppe);
        mainGame.getGameState().insertTile(2, 0, giuseppe, 0);

        mainGame.getGameState().pickTile(59, giuseppe);
        mainGame.getGameState().insertTile(2, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(19, giuseppe);
        mainGame.getGameState().insertTile(3, 1, giuseppe, 0);

        mainGame.getGameState().pickTile(50, giuseppe);
        mainGame.getGameState().insertTile(1, 1, giuseppe, 270);

        mainGame.getGameState().pickTile(7, giuseppe);
        mainGame.getGameState().insertTile(1, 2, giuseppe, 180);

        mainGame.getGameState().pickTile(21, giuseppe);
        mainGame.getGameState().insertTile(0, 2, giuseppe, 270);

        mainGame.getGameState().pickTile(2, giuseppe);
        mainGame.getGameState().insertTile(1, 3, giuseppe, 180);

        mainGame.getGameState().pickTile(85, giuseppe);
        mainGame.getGameState().insertTile(3, 3, giuseppe, 0);

        mainGame.getGameState().pickTile(136, giuseppe);
        mainGame.getGameState().insertTile(2, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(25, giuseppe);
        mainGame.getGameState().insertTile(3, 4, giuseppe, 270);

        mainGame.getGameState().pickTile(79, giuseppe);
        mainGame.getGameState().insertTile(4, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(67, giuseppe);
        mainGame.getGameState().insertTile(1, 4, giuseppe, 90);

        mainGame.getGameState().pickTile(113, giuseppe);
        mainGame.getGameState().insertTile(0, 4, giuseppe, 0);

        mainGame.getGameState().pickTile(4, giuseppe);
        mainGame.getGameState().insertTile(1, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(65, giuseppe);
        mainGame.getGameState().insertTile(2, 5, giuseppe, 90);

        mainGame.getGameState().pickTile(57, giuseppe);
        mainGame.getGameState().insertTile(3, 5, giuseppe, 270);

        mainGame.getGameState().pickTile(83, giuseppe);
        mainGame.getGameState().insertTile(4, 5, giuseppe, 0);

        mainGame.getGameState().pickTile(87, giuseppe);
        mainGame.getGameState().insertTile(2, 6, giuseppe, 270);

        mainGame.getGameState().pickTile(70, giuseppe);
        mainGame.getGameState().insertTile(3, 6, giuseppe, 90);

        mainGame.getGameState().pickTile(99, giuseppe);
        mainGame.getGameState().insertTile(4, 6, giuseppe, 0);

//MANCA TILE IN ALTO A SINISTRA!! RICORDA DI GESTIRE ECCEZIONE

//PLAYER 3 SHIPBOARD
        mainGame.getGameState().pickTile(18, giacomo);
        mainGame.getGameState().insertTile(2, 2, giacomo, 270);

        mainGame.getGameState().pickTile(63, giacomo);
        mainGame.getGameState().insertTile(2, 1, giacomo, 0);

        mainGame.getGameState().pickTile(155, giacomo);
        mainGame.getGameState().insertTile(2, 0, giacomo, 180);

        mainGame.getGameState().pickTile(111, giacomo);
        mainGame.getGameState().insertTile(3, 3, giacomo, 180);

        mainGame.getGameState().pickTile(47, giacomo);
        mainGame.getGameState().insertTile(3, 2, giacomo, 0);

        mainGame.getGameState().pickTile(23, giacomo);
        mainGame.getGameState().insertTile(3, 1, giacomo, 0);

        mainGame.getGameState().pickTile(46, giacomo);
        mainGame.getGameState().insertTile(3, 0, giacomo, 0);

        mainGame.getGameState().pickTile(38, giacomo);
        mainGame.getGameState().insertTile(4, 0, giacomo, 90);

        mainGame.getGameState().pickTile(141, giacomo);
        mainGame.getGameState().insertTile(4, 1, giacomo, 0);

        mainGame.getGameState().pickTile(132, giacomo);
        mainGame.getGameState().insertTile(4, 2, giacomo, 180);

        mainGame.getGameState().pickTile(69, giacomo);
        mainGame.getGameState().insertTile(1, 1, giacomo, 180);

        mainGame.getGameState().pickTile(56, giacomo);
        mainGame.getGameState().insertTile(1, 2, giacomo, 270);

        mainGame.getGameState().pickTile(109, giacomo);
        mainGame.getGameState().insertTile(0, 2, giacomo, 0);

        mainGame.getGameState().pickTile(40, giacomo);
        mainGame.getGameState().insertTile(1, 3, giacomo, 90);

        mainGame.getGameState().pickTile(143, giacomo);
        mainGame.getGameState().insertTile(1, 4, giacomo, 0);

        mainGame.getGameState().pickTile(78, giacomo);
        mainGame.getGameState().insertTile(0, 4, giacomo, 270);

        mainGame.getGameState().pickTile(82, giacomo);
        mainGame.getGameState().insertTile(1, 5, giacomo, 270);

        mainGame.getGameState().pickTile(156, giacomo);
        mainGame.getGameState().insertTile(2, 4, giacomo, 0);

        mainGame.getGameState().pickTile(42, giacomo);
        mainGame.getGameState().insertTile(3, 4, giacomo, 90);

        mainGame.getGameState().pickTile(97, giacomo);
        mainGame.getGameState().insertTile(4, 4, giacomo, 0);

        mainGame.getGameState().pickTile(58, giacomo);
        mainGame.getGameState().insertTile(2, 5, giacomo, 90);

        mainGame.getGameState().pickTile(5, giacomo);
        mainGame.getGameState().insertTile(3, 5, giacomo, 0);

        mainGame.getGameState().pickTile(89, giacomo);
        mainGame.getGameState().insertTile(4, 5, giacomo, 0);

        mainGame.getGameState().pickTile(51, giacomo);
        mainGame.getGameState().insertTile(2, 6, giacomo, 270);

        mainGame.getGameState().pickTile(12, giacomo);
        mainGame.getGameState().insertTile(3, 6, giacomo, 0);

        mainGame.getGameState().pickTile(29, giacomo);
        mainGame.getGameState().insertTile(4, 6, giacomo, 90);

//PLayer 4
        mainGame.getGameState().pickTile(3, lorenzo);
        mainGame.getGameState().insertTile(2, 2, lorenzo, 270);

        mainGame.getGameState().pickTile(49, lorenzo);
        mainGame.getGameState().insertTile(2, 1, lorenzo, 270);

        mainGame.getGameState().pickTile(137, lorenzo);
        mainGame.getGameState().insertTile(2, 0, lorenzo, 0);

        mainGame.getGameState().pickTile(134, lorenzo);
        mainGame.getGameState().insertTile(3, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(30, lorenzo);
        mainGame.getGameState().insertTile(3, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(64, lorenzo);
        mainGame.getGameState().insertTile(3, 1, lorenzo, 180);

        mainGame.getGameState().pickTile(10, lorenzo);
        mainGame.getGameState().insertTile(3, 0, lorenzo, 90);

        mainGame.getGameState().pickTile(151, lorenzo);
        mainGame.getGameState().insertTile(4, 0, lorenzo, 180);

        mainGame.getGameState().pickTile(96, lorenzo);
        mainGame.getGameState().insertTile(4, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(90, lorenzo);
        mainGame.getGameState().insertTile(4, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(144, lorenzo);
        mainGame.getGameState().insertTile(1, 2, lorenzo, 90);

        mainGame.getGameState().pickTile(86, lorenzo);
        mainGame.getGameState().insertTile(1, 1, lorenzo, 0);

        mainGame.getGameState().pickTile(131, lorenzo);
        mainGame.getGameState().insertTile(0, 2, lorenzo, 0);

        mainGame.getGameState().pickTile(36, lorenzo);
        mainGame.getGameState().insertTile(1, 3, lorenzo, 180);

        mainGame.getGameState().pickTile(13, lorenzo);
        mainGame.getGameState().insertTile(1, 4, lorenzo, 270);

        mainGame.getGameState().pickTile(116, lorenzo);
        mainGame.getGameState().insertTile(0, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(43, lorenzo);
        mainGame.getGameState().insertTile(1, 5, lorenzo, 270);

        mainGame.getGameState().pickTile(152, lorenzo);
        mainGame.getGameState().insertTile(2, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(54, lorenzo);
        mainGame.getGameState().insertTile(2, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(73, lorenzo);
        mainGame.getGameState().insertTile(2, 6, lorenzo, 270);

        mainGame.getGameState().pickTile(55, lorenzo);
        mainGame.getGameState().insertTile(3, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(95, lorenzo);
        mainGame.getGameState().insertTile(4, 4, lorenzo, 0);

        mainGame.getGameState().pickTile(119, lorenzo);
        mainGame.getGameState().insertTile(4, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(66, lorenzo);
        mainGame.getGameState().insertTile(3, 5, lorenzo, 90);

        mainGame.getGameState().pickTile(22, lorenzo);
        mainGame.getGameState().insertTile(3, 6, lorenzo, 0);

        mainGame.getGameState().pickTile(140, lorenzo);
        mainGame.getGameState().insertTile(4, 6, lorenzo, 90);

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 1); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 2); // Secondo input
        gameController.endbuilding(mainGame.getPlayer(giacomo).getPlayerId(), 3); // Terzo input
        gameController.endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4); // Quarto input
        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());
        gameController.setNumTile(giuseppe, 0);
        gameController.setNumTile(giuseppe, 0);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.setNumTile(giuseppe, 1);
        gameController.setNumTile(lorenzo, 1);
        gameController.chooseOneSubShip(0,lorenzo);
        gameController.setNumTile(lorenzo, 0);

        //        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(giuseppe, 1);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);
//        mainGame.getGameState().setNumTile(lorenzo, 0);
//        mainGame.getGameState().endbuilding(mainGame.getPlayer(lorenzo).getPlayerId(), 4);

        TUIHandler tui = new TUIHandler();

        assertEquals(BuildingPhaseStateLev2.class, mainGame.getGameState().getClass());

        for (Player p : mainGame.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (p.getMyShip().getShipMatrix()[i][j].getType() == SSTTypes.Tile_Cabin && p.getMyShip().getShipMatrix()[i][j].getNumPassengers() == 0) {
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.BROWN, i, j);
                        } catch (Exception _) {
                        }
                        try {
                            gameController.CommandFillTile(mainGame.getPlayerIDFromName(p.getUsername()), true, AlienColor.VIOLET, i, j);
                        } catch (Exception _) {
                        }
                    }
                }
            }
        }

        // PRIMA NAVE
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(3));

        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][1].getType());
        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][3].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][4].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][5].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][6].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][0].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][1].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getConnector(3));
//
//        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(1));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(2));
//        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getConnector(3));
//        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][6].getType());

        assertEquals(SSTTypes.Tile_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][0].getConnector(2));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getConnector(3));
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].getType());
        assertEquals(SSTTypes.Tile_ShieldGenerator, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][4].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getConnector(3));
//
//        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(1));
//        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(2));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][0].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][1].getConnector(3));

        assertEquals(SSTTypes.Tile_SpecialCargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getConnector(3));

        assertEquals(SSTTypes.Tile_Double_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(2));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][3].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(0));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(1));
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getType());
        assertEquals(Type_side_connector.DOUBLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(2));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][5].getConnector(3));

        assertEquals(SSTTypes.Tile_CargoHold, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(1));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][0].getConnector(3));

        assertEquals(SSTTypes.Tile_BatteryComponent, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][1].getConnector(3));

        assertEquals(SSTTypes.Tile_Engine, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][2].getConnector(3));
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][3].getType());

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][4].getConnector(3));

        assertEquals(SSTTypes.Tile_AlienLifeSupport, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getType());
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(0));
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][5].getConnector(3));

        assertEquals(SSTTypes.Tile_Cabin, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getType());
        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(2));
        assertEquals(Type_side_connector.UNIV_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[4][6].getConnector(3));


        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getLostPieces());
        assertEquals(0, mainGame.getPlayer(loreto).getMyShip().getLostPieces());
        assertEquals(2, mainGame.getPlayer(giacomo).getMyShip().getLostPieces());
        assertEquals(4, mainGame.getPlayer(lorenzo).getMyShip().getLostPieces());

        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());


        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());


        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());

        //conf di base con  le batterie
        assertEquals(5, mainGame.getPlayer(lorenzo).getMyShip().getBatteriesNumber());
        assertEquals(5, mainGame.getPlayer(giacomo).getMyShip().getBatteriesNumber());
        assertEquals(6, mainGame.getPlayer(giuseppe).getMyShip().getBatteriesNumber());
        assertEquals(12, mainGame.getPlayer(loreto).getMyShip().getBatteriesNumber());

        FlightBoard board = mainGame.getGameFlightBoard();

        //stato, la prima causa è crew
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);

        //attiva la firepower in 2,0
        ArrayList<Integer> xy = new ArrayList<>();
        xy.add(0);
        xy.add(2);
        ArrayList<ArrayList<Integer>> posDC = new ArrayList<>();
        posDC.add(xy);
        //pos batteria
        ArrayList<Integer> Bxy = new ArrayList<>();
        Bxy.add(1);
        Bxy.add(2);
        Bxy.add(1);
        ArrayList<ArrayList<Integer>> Bpos = new ArrayList<>();
        Bpos.add(Bxy);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), posDC, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(loreto), posDC, Bpos);
        gameController.chooseToStartFirePower(mainGame.getPlayer(giuseppe), null, null);
        //dovrebbe dirmi che non va bene perchè ho attivato un cannone
        //gli do che non attivo niente
        gameController.chooseToStartFirePower(mainGame.getPlayer(giacomo), null, null);
        //stessa cosa tutti gli altri
        gameController.chooseToStartFirePower(mainGame.getPlayer(lorenzo), null, null);

        ArrayList<Goods> g = new ArrayList<>();
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().activate(board);
        });

        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(null, null, board);
        });


        assertEquals(6, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(21, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        //cl1 ha 2 cannoni, tutti gli altri 1 quindi il 2 è quello che paga
        //faccio scegliere
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof CombatZoneBegin);
        //lets see ships
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(null, null, board);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseCannonBatteryPos(null, null, null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseHowToFaceMeteors(null, null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseToPlaceBatteries(null, null);
        });
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().chooseToClaimReward(null, true, null, null, null);
        });
        gameController.getCardinuse().getCrewLost();
        gameController.getCardinuse().getMoneyGained();


        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//no goods

        mainGame.getPlayer(giacomo).getMyShip().looseAllBatteris();
        mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[3][6].addCharge();
        gameController.chooseToStartMotor(mainGame.getPlayer(loreto), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(giacomo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(lorenzo), board, null, Bpos);
        gameController.chooseToStartMotor(mainGame.getPlayer(giuseppe), board, null, Bpos);
        ArrayList<Integer> posgood = new ArrayList<>();
        posgood.add(4);
        posgood.add(6);
        ArrayList<ArrayList<Integer>> posbattfin = new ArrayList<>();
        posbattfin.add(posgood);
        ArrayList<Goods> goodarray = new ArrayList<>();
        Goods guu = new Goods(1, false);
        goodarray.add(guu);
        ArrayList<ArrayList<Goods>> listgood = new ArrayList<>();
        listgood.add(goodarray);
        //gameController.chooseWhereToPutGoods(mainGame.getPlayer(giacomo),posbattfin,listgood);

        //loreto lost
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof MeteorCardEffectStateFROMCOMBAT);
        assertThrows(Exception.class, () -> {
            gameController.getCardinuse().getState().Activate(board);
        });
        //lorenzo talks while he should not
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, board);
        //prendo colpo in 7 da dx dove ci stanno due ship
        //assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof fixing_state);


    }

}