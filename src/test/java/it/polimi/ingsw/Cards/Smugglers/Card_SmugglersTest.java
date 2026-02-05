package it.polimi.ingsw.Cards.Smugglers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Cards.Smugglers.*;
import it.polimi.ingsw.Model.GameControl.BuildingPhaseStateLev2;
import it.polimi.ingsw.Model.GameControl.Flight_Phase_State;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.GameControl.GameController;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Utils.Dice.FixedDiceRoller;
import it.polimi.ingsw.View.TUI.TUIHandler;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Card_SmugglersTest {
    Game mainGame;
    Deck EventDeck;
    GameController gameController;

    @BeforeEach
    void setUp() {
        mainGame = new Game(1, new FixedDiceRoller(0), 1, 10);
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it.polimi.ingsw/smugglers.json")) {
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
    void cardTest_shouldWinGoodsandLooseGoodsAndBatteries() throws Exception {
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

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 2); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 1); // Secondo input
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


        //alieni viola +2 potenza di fuoco, alieni arancione +2 potenza motrice solo se pot senza alieno è >0
        //fai si che l'ordine sia 2 che perde e poi 1 che vince.
        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(3, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

        //flightboard order is giuseppe, loreto, giacomo, lorenzo
        //first who have to answer is giuseppe.
        //loreto responds
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //should be wrong player
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);

        //now should be the right player, who loses
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe), null, null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        //wrong player
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2))
                )));
        //right player, right answer
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2)),
                        new ArrayList<>(List.of(1, 5, 2))
                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//        //right player wrong answer
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(loreto), null, null);
        //perde. non ha merci, quindi perde batterie
////        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        TUIHandler tui = new TUIHandler();
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto), //wrong input
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 5, 2)),
//                        new ArrayList<>(List.of(1, 3, 2))
//                )));
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);

        //new card should have started

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 1, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //il giocatore sceglie di accendere i cannoni e vince
        mainGame.getGameState().chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);


            mainGame.getGameState().chooseCannonBatteryPos(mainGame.getGameFlightBoard(),
                    mainGame.getPlayer(giuseppe), null, null);


        // Crea un ArrayList di ArrayList<Goods>
        ArrayList<ArrayList<Goods>> goodsList = new ArrayList<>();
        // Crea un ArrayList di Goods
        ArrayList<Goods> goodsSubList1 = new ArrayList<>();
        goodsSubList1.add(new Goods(3, false)); // Aggiungi un oggetto Goods con valore e radioattività
        goodsSubList1.add(new Goods(1, false));
        // Aggiungi il sotto ArrayList alla lista principale
        goodsList.add(goodsSubList1);
        // Crea un altro ArrayList di Goods
        ArrayList<Goods> goodsSubList2 = new ArrayList<>();
        goodsSubList2.add(new Goods(2, false));
        // Aggiungi il secondo sotto ArrayList alla lista principale
        goodsList.add(goodsSubList2);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
//accetta il premio e aggiunge i good, ma è il guocatore sbagliato.
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);

        //giocatore giusto, input errato
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(3, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
        //giocatore giusto, input ok.
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );

//        TUIHandler tui = new TUIHandler();
//        tui.printCard(mainGame.getGameState().getCardinuse());

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersEndState);
        mainGame.getGameState().getCardinuse().getCrewLost();
        mainGame.getGameState().getCardinuse().getMoneyGained();
        assertEquals(2, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

        assertEquals(mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getEffectivePresentGoods(), goodsSubList1); //controllo con i vettorini usati prima
        assertEquals(mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getEffectivePresentGoods(), goodsSubList2);

        assertEquals(1, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getNumCharges());
        assertEquals(2, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getNumCharges());
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());


        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                null);
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )));

        //finito la prima carta
        Thread.sleep(1005);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);

        //gioca giuseppe, giocatore giusto e non inserisce nulla
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe), null, null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);

//gioca giuseppe, giocatore giusto e non inserisce nulla, quindi perde batteria
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2))
                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        mainGame.getPlayer(loreto).getMyShip().removeTile(0, 4, false);
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), null, null);

        ArrayList<ArrayList<Goods>> goodsListP = new ArrayList<>();
        ArrayList<Goods> goodsSubList11 = new ArrayList<>();
        goodsSubList11.add(new Goods(1, false));
        goodsListP.add(goodsSubList11);
        ArrayList<Goods> goodsSubList22 = new ArrayList<>();
        goodsSubList22.add(new Goods(2, false));
        goodsListP.add(goodsSubList22);
//should loose good
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostGoodsState);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4))
                )),
                goodsListP);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 4))
                )),
                goodsListP);
        ArrayList<ArrayList<Goods>> goodsListPP = new ArrayList<>();
        goodsListPP.add(goodsSubList11);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 4))
                )),
                goodsListPP);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                null,
                goodsListPP);
        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                null,
                null);
        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4))
                )),
                goodsListPP);


        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giacomo), null, null);

        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(lorenzo), null, null);

        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giacomo), null, null); //hanno perso tutti e 4

        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
    }


    @Test
    void cardTest_shouldWinGoodsandUseSpecialStorage() throws Exception {
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

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 2); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 1); // Secondo input
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


        //alieni viola +2 potenza di fuoco, alieni arancione +2 potenza motrice solo se pot senza alieno è >0
        //fai si che l'ordine sia 2 che perde e poi 1 che vince.
        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(3, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

        //flightboard order is giuseppe, loreto, giacomo, lorenzo
        //first who have to answer is giuseppe.
        //loreto responds
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //should be wrong player
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);

        //now should be the right player, who loses
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe), null, null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        //wrong player
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2))
                )));
        //right player, right answer
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2)),
                        new ArrayList<>(List.of(1, 5, 2))
                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//        //right player wrong answer
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(loreto), null, null);
        //perde. non ha merci, quindi perde batterie
////        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        TUIHandler tui = new TUIHandler();
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto), //wrong input
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 5, 2)),
//                        new ArrayList<>(List.of(1, 3, 2))
//                )));
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);

        //new card should have started

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 1, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //il giocatore sceglie di accendere i cannoni e vince
        mainGame.getGameState().chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);


            mainGame.getGameState().chooseCannonBatteryPos(mainGame.getGameFlightBoard(),
                    mainGame.getPlayer(giuseppe), null, null);

        // Crea un ArrayList di ArrayList<Goods>
        ArrayList<ArrayList<Goods>> goodsList = new ArrayList<>();
        // Crea un ArrayList di Goods
        ArrayList<Goods> goodsSubList1 = new ArrayList<>();
        goodsSubList1.add(new Goods(3, false)); // Aggiungi un oggetto Goods con valore e radioattività
        goodsSubList1.add(new Goods(1, false));
        // Aggiungi il sotto ArrayList alla lista principale
        goodsList.add(goodsSubList1);
        // Crea un altro ArrayList di Goods
        ArrayList<Goods> goodsSubList2 = new ArrayList<>();
        goodsSubList2.add(new Goods(2, false));
        // Aggiungi il secondo sotto ArrayList alla lista principale
        goodsList.add(goodsSubList2);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
//accetta il premio e aggiunge i good, ma è il guocatore sbagliato.
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);

        //giocatore giusto, input errato
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(3, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
        //giocatore giusto, input ok.
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 6)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );

//        TUIHandler tui = new TUIHandler();
//        tui.printCard(mainGame.getGameState().getCardinuse());

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersEndState);
        mainGame.getGameState().getCardinuse().getCrewLost();
        mainGame.getGameState().getCardinuse().getMoneyGained();
        assertEquals(2, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

//        assertEquals(mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getEffectivePresentGoods(), goodsSubList1); //controllo con i vettorini usati prima
//        assertEquals(mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getEffectivePresentGoods(), goodsSubList2);

        assertEquals(1, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getNumCharges());
        assertEquals(2, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getNumCharges());
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());


        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                null);
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )));

        //finito la prima carta
        Thread.sleep(1005);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);

        //gioca giuseppe, giocatore giusto e non inserisce nulla
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe), null, null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);

//gioca giuseppe, giocatore giusto e non inserisce nulla, quindi perde batteria
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2))
                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        mainGame.getPlayer(loreto).getMyShip().removeTile(0, 4, false);
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), null, null);

        ArrayList<ArrayList<Goods>> goodsListP = new ArrayList<>();
        ArrayList<Goods> goodsSubList11 = new ArrayList<>();
        goodsSubList11.add(new Goods(1, false));
        goodsListP.add(goodsSubList11);
        ArrayList<Goods> goodsSubList22 = new ArrayList<>();
        goodsSubList22.add(new Goods(2, false));
        goodsListP.add(goodsSubList22);
//should loose good
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostGoodsState);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4))
                )),
                goodsListP);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 4))
                )),
                goodsListP);
        ArrayList<ArrayList<Goods>> goodsListPP = new ArrayList<>();
        goodsListPP.add(goodsSubList11);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 4))
                )),
                goodsListPP);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                null,
                goodsListPP);
        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                null,
                null);
        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4))
                )),
                goodsListPP);


        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giacomo), null, null);

        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(lorenzo), null, null);

        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giacomo), null, null); //hanno perso tutti e 4

        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
    }
    @Test
    void cardTest_shouldTie() throws Exception {
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

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 2); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 1); // Secondo input
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


        //alieni viola +2 potenza di fuoco, alieni arancione +2 potenza motrice solo se pot senza alieno è >0
        //fai si che l'ordine sia 2 che perde e poi 1 che vince.
        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(3, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

        //flightboard order is giuseppe, loreto, giacomo, lorenzo
        //first who have to answer is giuseppe.
        //loreto responds
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //should be wrong player
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);

        //now should be the right player, who loses
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe), null, null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        //wrong player
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2))
                )));
        //right player, right answer
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2)),
                        new ArrayList<>(List.of(1, 5, 2))
                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//        //right player wrong answer
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(loreto), null, null);
        //perde. non ha merci, quindi perde batterie
////        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        TUIHandler tui = new TUIHandler();
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto), //wrong input
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 5, 2)),
//                        new ArrayList<>(List.of(1, 3, 2))
//                )));
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);

        //new card should have started

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 1, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //il giocatore sceglie di non accendere nulla e pareggia
        mainGame.getGameState().chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
               null,
                null
        );
        System.out.println(mainGame.getGameState().getCardinuse().getState());
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);



//        // Crea un ArrayList di ArrayList<Goods>
//        ArrayList<ArrayList<Goods>> goodsList = new ArrayList<>();
//        // Crea un ArrayList di Goods
//        ArrayList<Goods> goodsSubList1 = new ArrayList<>();
//        goodsSubList1.add(new Goods(3, false)); // Aggiungi un oggetto Goods con valore e radioattività
//        goodsSubList1.add(new Goods(1, false));
//        // Aggiungi il sotto ArrayList alla lista principale
//        goodsList.add(goodsSubList1);
//        // Crea un altro ArrayList di Goods
//        ArrayList<Goods> goodsSubList2 = new ArrayList<>();
//        goodsSubList2.add(new Goods(2, false));
//        // Aggiungi il secondo sotto ArrayList alla lista principale
//        goodsList.add(goodsSubList2);
        assertFalse(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
//accetta il premio e aggiunge i good, ma è il guocatore sbagliato.
//        gameController.chooseToClaimReward(
//                mainGame.getGameFlightBoard(),
//                true,
//                mainGame.getPlayer(giuseppe),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 4)),
//                        new ArrayList<>(List.of(3, 2))
//                )),
//                goodsList
//        );
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
//
//        //giocatore giusto, input errato
//        gameController.chooseToClaimReward(
//                mainGame.getGameFlightBoard(),
//                true,
//                mainGame.getPlayer(loreto),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(3, 4)),
//                        new ArrayList<>(List.of(3, 2))
//                )),
//                goodsList
//        );
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
//        //giocatore giusto, input ok.
//        gameController.chooseToClaimReward(
//                mainGame.getGameFlightBoard(),
//                true,
//                mainGame.getPlayer(loreto),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 4)),
//                        new ArrayList<>(List.of(3, 2))
//                )),
//                goodsList
//        );
//
////        TUIHandler tui = new TUIHandler();
////        tui.printCard(mainGame.getGameState().getCardinuse());
//
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersEndState);
//        mainGame.getGameState().getCardinuse().getCrewLost();
//        mainGame.getGameState().getCardinuse().getMoneyGained();
//        assertEquals(2, mainGame.getPlayer(loreto).getPositionOnBoard());
//        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
//        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
//        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
//        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
//        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
//        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
//        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());
//
//        assertEquals(mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getEffectivePresentGoods(), goodsSubList1); //controllo con i vettorini usati prima
//        assertEquals(mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getEffectivePresentGoods(), goodsSubList2);
//
//        assertEquals(1, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getNumCharges());
//        assertEquals(2, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getNumCharges());
//        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
//        tui.printFlightBoard(mainGame.getGameFlightBoard());


//        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
//                null);
//        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 5, 2)),
//                        new ArrayList<>(List.of(1, 3, 2))
//                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giacomo), null, null);

        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(lorenzo), null, null);

        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giacomo), null, null); //hanno perso tutti e 4

        //finito la prima carta
        Thread.sleep(1005);
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//
//        //gioca giuseppe, giocatore giusto e non inserisce nulla
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(giuseppe), null, null);
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
//
////gioca giuseppe, giocatore giusto e non inserisce nulla, quindi perde batteria
//        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 3, 2))
//                )));
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//        mainGame.getPlayer(loreto).getMyShip().removeTile(0, 4, false);
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(loreto), null, null);
//
//        ArrayList<ArrayList<Goods>> goodsListP = new ArrayList<>();
//        ArrayList<Goods> goodsSubList11 = new ArrayList<>();
//        goodsSubList11.add(new Goods(1, false));
//        goodsListP.add(goodsSubList11);
//        ArrayList<Goods> goodsSubList22 = new ArrayList<>();
//        goodsSubList22.add(new Goods(2, false));
//        goodsListP.add(goodsSubList22);
////should loose good
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostGoodsState);
//
//        gameController.chooseWhereToPutGoods(mainGame.getPlayer(giuseppe),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 4))
//                )),
//                goodsListP);
//
//        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(2, 4))
//                )),
//                goodsListP);
//        ArrayList<ArrayList<Goods>> goodsListPP = new ArrayList<>();
//        goodsListPP.add(goodsSubList11);
//
//        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(2, 4))
//                )),
//                goodsListPP);
//
//        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
//                null,
//                goodsListPP);
//        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
//                null,
//                null);
//        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 4))
//                )),
//                goodsListPP);
//
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(giacomo), null, null);
//
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(lorenzo), null, null);
//
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(giacomo), null, null); //hanno perso tutti e 4
//
//        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
//        tui.printFlightBoard(mainGame.getGameFlightBoard());
    }


    @Test
    void cardTest_shouldWinGoodsandLooseAllGoods() throws Exception {
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

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 2); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 1); // Secondo input
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


        //alieni viola +2 potenza di fuoco, alieni arancione +2 potenza motrice solo se pot senza alieno è >0
        //fai si che l'ordine sia 2 che perde e poi 1 che vince.
        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(3, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

        //flightboard order is giuseppe, loreto, giacomo, lorenzo
        //first who have to answer is giuseppe.
        //loreto responds
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        mainGame.getGameState().getCardinuse().getState().getNextState();
        //should be wrong player
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        mainGame.getGameState().getCardinuse().getState().getNextState();

        //now should be the right player, who loses
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe), null, null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        mainGame.getGameState().getCardinuse().getState().getNextState();
        //wrong player
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2))
                )));
        //right player, right answer
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2)),
                        new ArrayList<>(List.of(1, 5, 2))
                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        mainGame.getGameState().getCardinuse().getState().getNextState();
//        //right player wrong answer
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(loreto), null, null);
        //perde. non ha merci, quindi perde batterie
////        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        TUIHandler tui = new TUIHandler();
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto), //wrong input
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 5, 2)),
//                        new ArrayList<>(List.of(1, 3, 2))
//                )));
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);

        //new card should have started

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 1, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //il giocatore sceglie di accendere i cannoni e vince
        mainGame.getGameState().chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
        mainGame.getGameState().getCardinuse().getState().getNextState();


            mainGame.getGameState().chooseCannonBatteryPos(mainGame.getGameFlightBoard(),
                    mainGame.getPlayer(giuseppe), null, null);

        // Crea un ArrayList di ArrayList<Goods>
        ArrayList<ArrayList<Goods>> goodsList = new ArrayList<>();
        // Crea un ArrayList di Goods
        ArrayList<Goods> goodsSubList1 = new ArrayList<>();
        goodsSubList1.add(new Goods(3, false)); // Aggiungi un oggetto Goods con valore e radioattività
        goodsSubList1.add(new Goods(1, false));
        // Aggiungi il sotto ArrayList alla lista principale
        goodsList.add(goodsSubList1);
        // Crea un altro ArrayList di Goods
        ArrayList<Goods> goodsSubList2 = new ArrayList<>();
        goodsSubList2.add(new Goods(2, false));
        // Aggiungi il secondo sotto ArrayList alla lista principale
        goodsList.add(goodsSubList2);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
//accetta il premio e aggiunge i good, ma è il guocatore sbagliato.
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);

        //giocatore giusto, input errato
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(3, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
        //giocatore giusto, input ok.
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );

//        TUIHandler tui = new TUIHandler();
//        tui.printCard(mainGame.getGameState().getCardinuse());

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersEndState);
        mainGame.getGameState().getCardinuse().getState().getNextState();
        mainGame.getGameState().getCardinuse().getCrewLost();
        mainGame.getGameState().getCardinuse().getMoneyGained();
        assertEquals(2, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

        assertEquals(mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][4].getEffectivePresentGoods(), goodsSubList1); //controllo con i vettorini usati prima
        assertEquals(mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][2].getEffectivePresentGoods(), goodsSubList2);

        assertEquals(1, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].getNumCharges());
        assertEquals(2, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][3].getNumCharges());
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());


        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                null);
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )));

        //finito la prima carta
        Thread.sleep(1005);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);

        //gioca giuseppe, giocatore giusto e non inserisce nulla
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe), null, null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);

//gioca giuseppe, giocatore giusto e non inserisce nulla, quindi perde batteria
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2))
                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        mainGame.getPlayer(loreto).getMyShip().removeTile(0, 4, false);
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), null, null);

        ArrayList<ArrayList<Goods>> goodsListP = new ArrayList<>();
        ArrayList<Goods> goodsSubList11 = new ArrayList<>();
        goodsSubList11.add(new Goods(1, false));
        goodsListP.add(goodsSubList11);
        ArrayList<Goods> goodsSubList22 = new ArrayList<>();
        goodsSubList22.add(new Goods(2, false));
        goodsListP.add(goodsSubList22);
//should loose good
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostGoodsState);
        mainGame.getGameState().getCardinuse().getState().getNextState();
        gameController.chooseWhereToPutGoods(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4))
                )),
                goodsListP);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 4))
                )),
                goodsListP);
        ArrayList<ArrayList<Goods>> goodsListPP = new ArrayList<>();
        goodsListPP.add(goodsSubList11);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 4))
                )),
                goodsListPP);

        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                null,
                goodsListPP);
        gameController.chooseWhereToPutGoods(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4))
                )),
                goodsListPP);


        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giacomo), null, null);

        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(lorenzo), null, null);

        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giacomo), null, null); //hanno perso tutti e 4

        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
    }

    @Test
    void cardTest_shouldWinbutRefuses() throws Exception {
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

        gameController.endbuilding(mainGame.getPlayer(loreto).getPlayerId(), 2); // Primo input
        gameController.endbuilding(mainGame.getPlayer(giuseppe).getPlayerId(), 1); // Secondo input
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


        //alieni viola +2 potenza di fuoco, alieni arancione +2 potenza motrice solo se pot senza alieno è >0
        //fai si che l'ordine sia 2 che perde e poi 1 che vince.
        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(3, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

        //flightboard order is giuseppe, loreto, giacomo, lorenzo
        //first who have to answer is giuseppe.
        //loreto responds
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //should be wrong player
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);

        //now should be the right player, who loses
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe), null, null);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        //wrong player
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2))
                )));
        //right player, right answer
        gameController.chooseToPlaceBatteries(mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 3, 2)),
                        new ArrayList<>(List.of(1, 5, 2))
                )));
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//        //right player wrong answer
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto), new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 2)),
                        new ArrayList<>(List.of(1, 3, 2))
                )), null);
//        gameController.chooseCannonBatteryPos(
//                mainGame.getGameFlightBoard(),
//                mainGame.getPlayer(loreto), null, null);
        //perde. non ha merci, quindi perde batterie
////        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);
        TUIHandler tui = new TUIHandler();
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//        gameController.chooseToPlaceBatteries(mainGame.getPlayer(loreto), //wrong input
//                new ArrayList<>(List.of(
//                        new ArrayList<>(List.of(1, 5, 2)),
//                        new ArrayList<>(List.of(1, 3, 2))
//                )));
//        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersLostBatteriesState);

        //new card should have started

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
//wrong input
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),//non è un cannone
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 1, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersCalcState);
        //il giocatore sceglie di accendere i cannoni e vince
        mainGame.getGameState().chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(0, 2)),
                        new ArrayList<>(List.of(1, 5))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 2, 1)),
                        new ArrayList<>(List.of(1, 3, 1))
                ))
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);


            mainGame.getGameState().chooseCannonBatteryPos(mainGame.getGameFlightBoard(),
                    mainGame.getPlayer(giuseppe), null, null);

        // Crea un ArrayList di ArrayList<Goods>
        ArrayList<ArrayList<Goods>> goodsList = new ArrayList<>();
        // Crea un ArrayList di Goods
        ArrayList<Goods> goodsSubList1 = new ArrayList<>();
        goodsSubList1.add(new Goods(3, false)); // Aggiungi un oggetto Goods con valore e radioattività
        goodsSubList1.add(new Goods(1, false));
        // Aggiungi il sotto ArrayList alla lista principale
        goodsList.add(goodsSubList1);
        // Crea un altro ArrayList di Goods
        ArrayList<Goods> goodsSubList2 = new ArrayList<>();
        goodsSubList2.add(new Goods(2, false));
        // Aggiungi il secondo sotto ArrayList alla lista principale
        goodsList.add(goodsSubList2);
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
//accetta il premio e aggiunge i good, ma è il guocatore sbagliato.
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);

        //giocatore giusto, input errato
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                true,
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(3, 4)),
                        new ArrayList<>(List.of(3, 2))
                )),
                goodsList
        );
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersWinningState);
        //giocatore giusto, input ok. refuses
        gameController.chooseToClaimReward(
                mainGame.getGameFlightBoard(),
                false,
                mainGame.getPlayer(loreto),
               null,
                null
        );

//        TUIHandler tui = new TUIHandler();
//        tui.printCard(mainGame.getGameState().getCardinuse());

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof SmugglersEndState);
        mainGame.getGameState().getCardinuse().getCrewLost();
        mainGame.getGameState().getCardinuse().getMoneyGained();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].addGoods(goodsSubList2.getFirst());
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].addGoods(goodsSubList2.getFirst());
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].addGoods(goodsSubList2.getFirst());
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].addGoods(goodsSubList2.getFirst());

        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addGoods(new Goods(4, true));
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addGoods(goodsSubList2.getFirst());
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addGoods(goodsSubList2.getFirst());
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addGoods(goodsSubList2.getFirst());
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addGoods(goodsSubList2.getFirst());
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addGoods(goodsSubList2.getFirst());
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addGoods(goodsSubList2.getFirst());


        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].removePassenger(1);
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addPassenger();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addCharge();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].addAlien(true, AlienColor.BROWN, 3, 6);
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].removeAlien();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].removeCharge(2);
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].RechargeAll();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getNumPassengers();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][6].getNumCharges();
        });



        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].addCharge();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].addCharge();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].addCharge();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].addCharge();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].removeCharge(10);
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].RechargeAll();



        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].addPassenger();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].addAlien(true , AlienColor.BROWN,2 ,1 );
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].removePassenger(2);
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].removeAlien();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getNumPassengers();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getEffectivePresentGoods();
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].setAlienColor(AlienColor.BROWN);
        });
        assertThrows(Exception.class, () -> {
            mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][1].getAlienColor();
        });




        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].addCharge();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].addCharge();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].addCharge();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].removeCharge(10);
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][2].RechargeAll();


        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].addPassenger();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].addPassenger();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].removePassenger(1);
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].removePassenger(1);
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].removePassenger(1);


        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].addPassenger();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].addPassenger();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].addAlien(true, AlienColor.BROWN, 2,3 );
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].addAlien(true, AlienColor.BROWN, 2,3 );
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][3].addAlien(true, AlienColor.VIOLET,2 ,3 );

        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].removePassenger(10);
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].addAlien(true, AlienColor.BROWN,3 ,4 );
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].addAlien(true, AlienColor.BROWN,3 , 4);
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].addAlien(true, AlienColor.VIOLET,3 ,4 );
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].removeAlien();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].addPassenger();
        mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[3][4].addPassenger();

    }
}

