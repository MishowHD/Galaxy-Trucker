package it.polimi.ingsw.Cards.MeteorSwarm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Cards.MeteorSwarm.Card_MeteorSwarm;
import it.polimi.ingsw.Model.Cards.MeteorSwarm.MeteorCardEffectState;
import it.polimi.ingsw.Model.Cards.MeteorSwarm.MeteorCardEndState;
import it.polimi.ingsw.Model.Cards.MeteorSwarm.MeteorCardState;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
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
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class Card_MeteorSwarmTest {
    Game mainGame;
    Deck EventDeck;
    GameController gameController;

    @BeforeEach
    void setUp() throws Exception {
        mainGame = new Game(1, new FixedDiceRoller(0), 1, 10);
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it.polimi.ingsw/meteor-swarm.json")) {
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
        gameController.setNumTile(lorenzo, 1);
        gameController.setNumTile(lorenzo, 1);
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
        assertEquals(5, mainGame.getPlayer(lorenzo).getMyShip().getLostPieces());

        assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());
    }

    @Test
    void cardTest() throws Exception {
        TUIHandler tui = new TUIHandler();
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
//                for (Player p : mainGame.getPlayers()) {
//                    System.out.println("\n\n\n\n\n"+ p);
//                    SpaceShipTile[][] mat= p.getMyShip().getShipMatrix();
//                    for (int i = 0; i < 5; i++) { // Scorre le righe
//                        for (int j = 0; j < 7; j++) { // Scorre le colonne
//                            System.out.print(mat[i][j].getType() + " ");
//                        }
//                        System.out.println();
//                    }
//                }
        //Assert che ho il waste iniziale
        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        int giacomo = mainGame.getPlayerIDFromName("Giacomo");
        int lorenzo = mainGame.getPlayerIDFromName("Lorenzo");
        int w1 = mainGame.getPlayer(loreto).getMyShip().getLostPieces();
        int w2 = mainGame.getPlayer(giuseppe).getMyShip().getLostPieces();
        int w3 = mainGame.getPlayer(giacomo).getMyShip().getLostPieces();
        int w4 = mainGame.getPlayer(lorenzo).getMyShip().getLostPieces();
        System.out.println(w1 + " " + w2 + " " + w3 + " " + w4);

        assertEquals(6, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(3, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());
        assertInstanceOf(Card_State.class, mainGame.getGameState().getCardinuse().getState().getNextState());
        assertInstanceOf(MeteorCardEffectState.class, mainGame.getGameState().getCardinuse().getState());
        // 9, 5, 7, 2, 3, 4, 1, 5, 7, 8, 4, 2, 6
        assertEquals(0, mainGame.getPlayer(loreto).getMyShip().getLostPieces());

                mainGame.getGameState().chooseCannonBatteryPos(mainGame.getGameFlightBoard(), mainGame.getPlayer(loreto), null, null);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giuseppe), null, mainGame.getGameFlightBoard());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, mainGame.getGameFlightBoard());
        assertInstanceOf(MeteorCardEffectState.class, mainGame.getGameState().getCardinuse().getState());
        assertInstanceOf(Card_State.class, mainGame.getGameState().getCardinuse().getState().getNextState());
        // gameController.chooseOneSubShip(0, mainGame.getPlayer(loreto).getPlayerId());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giuseppe), null, mainGame.getGameFlightBoard());
        //     gameController.chooseOneSubShip(0, mainGame.getPlayer(giuseppe).getPlayerId());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giacomo), null, mainGame.getGameFlightBoard());
        assertThrows(Exception.class, () ->
                mainGame.getGameState().chooseHowToFaceMeteors(mainGame.getPlayer(giacomo), null, mainGame.getGameFlightBoard())
        );
//        assertThrows(Exception.class, () ->
//                mainGame.getGameState().chooseCannonBatteryPos(mainGame.getGameFlightBoard(), mainGame.getPlayer(giacomo), null, null)
//        );
        gameController.chooseOneSubShip(37, mainGame.getPlayer(giacomo).getPlayerId());
        gameController.chooseOneSubShip(27, mainGame.getPlayer(loreto).getPlayerId());
        gameController.chooseOneSubShip(0, mainGame.getPlayer(giacomo).getPlayerId());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(lorenzo), null, mainGame.getGameFlightBoard());
        //     gameController.chooseOneSubShip(0, mainGame.getPlayer(lorenzo).getPlayerId());

        //assert che è andato via il componente di client1 in pos 1,5
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[1][5].getType());
        //client 2 1,5
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[1][5].getType());
        //client 3 2,5 e tutta la colonna 6 (righe 2,3 e 4  come effetto collaterale)
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[2][5].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[2][6].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[3][6].getType());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[4][6].getType());
        //client 4 perde 1,5
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(lorenzo).getMyShip().getShipMatrix()[1][5].getType());
        //controllo il giusto waste per tutti
        assertEquals(w1 + 1, mainGame.getPlayer(loreto).getMyShip().getLostPieces());
        assertEquals(w2 + 1, mainGame.getPlayer(giuseppe).getMyShip().getLostPieces());
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());
        assertEquals(w3 + 4, mainGame.getPlayer(giacomo).getMyShip().getLostPieces());
        assertEquals(w4 + 1, mainGame.getPlayer(lorenzo).getMyShip().getLostPieces());
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(lorenzo).getMyShip().getShipMatrix()[0][2].getType());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, mainGame.getGameFlightBoard());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giuseppe), null, mainGame.getGameFlightBoard());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giacomo), null, mainGame.getGameFlightBoard());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(lorenzo), new ArrayList<>(List.of(4, 0, 2, 2)), mainGame.getGameFlightBoard());

        //controlla che in posizione 0,2 ci sia ancora qualcosa per tutti
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getType());
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[0][2].getType());
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giacomo).getMyShip().getShipMatrix()[0][2].getType());
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(lorenzo).getMyShip().getShipMatrix()[0][2].getType());
        //1 in posiz 2,6 ha qualocsa
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][6].getType());
        //2 in posizo 2,5 ha qualcosa
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[2][5].getType());

        mainGame.getGameState().getCardinuse().getCrewLost();
        mainGame.getGameState().getCardinuse().getMoneyGained();
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(loreto), null, mainGame.getGameFlightBoard());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giuseppe), null, mainGame.getGameFlightBoard());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giacomo), new ArrayList<>(List.of(2, 4, 3, 5)), mainGame.getGameFlightBoard());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(lorenzo), new ArrayList<>(List.of(2, 4, 2, 2)), mainGame.getGameFlightBoard());
        assertEquals(0, mainGame.getGameState().getCardinuse().getCrewLost());
        assertEquals(0, mainGame.getGameState().getCardinuse().getMoneyGained());
        assertEquals(0, mainGame.getPlayerIDFromName("bvdfgredv"));
        //1 in posiz 2,6 ha qualocsa
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[2][5].getType());
        //2 in posizo 2,5 non ha nulla
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[2][5].getType());
        assertThrows(RuntimeException.class, () ->
                mainGame.getGameState().chooseHowToFaceMeteors(mainGame.getPlayer(giacomo), null, mainGame.getGameFlightBoard())
        );

        mainGame.getGameState().chooseCannonBatteryPos(mainGame.getGameFlightBoard(), mainGame.getPlayer(loreto), null, null);

         mainGame.getGameState().chooseToPlaceBatteries(mainGame.getPlayer(loreto), null);

        assertNull(mainGame.getGameState().getCardinuse().getState().getNextState());
        assertInstanceOf(MeteorCardEndState.class, mainGame.getGameState().getCardinuse().getState());
        mainGame.getGameState().getCardinuse().getState().getNextState();

    }

    @Test
    void testUnsupportedMethodsThrowException() throws Exception {
        Card_MeteorSwarm meteorCard = (Card_MeteorSwarm) mainGame.getGameState().getCardinuse();
        FlightBoard flightBoard = mainGame.getGameFlightBoard();
        List<Player> playerRank = mainGame.getPlayers();
        Player anyPlayer = mainGame.getPlayers().get(0);
        int anyPlayerId = anyPlayer.getPlayerId();


    }

    private static class TestState extends MeteorCardState {
        public TestState(Card_MeteorSwarm meteorSwarm) {
            super(meteorSwarm);
        }

        @Override
        public Card_State getNextState() {
            return this;
        }

        @Override
        public void activate(FlightBoard flightBoard) throws Exception {
            throw new RuntimeException();
        }

        public Card_MeteorSwarm getMeteorSwarm() {
            return meteorSwarm;
        }

        @Override
        public void chooseCannonBatteryPos(FlightBoard flightBoard, Player player,
                                           ArrayList<ArrayList<Integer>> list1,
                                           ArrayList<ArrayList<Integer>> list2) {
            throw new RuntimeException();
        }


        @Override
        public void chooseToClaimReward(boolean decision, Player player) {
            throw new RuntimeException();
        }

        @Override
        public void chooseToStartMotor(Player player, FlightBoard flightBoard,
                                       ArrayList<ArrayList<Integer>> lists1,
                                       ArrayList<ArrayList<Integer>> lists2) {
            throw new RuntimeException();
        }

        @Override
        public void chooseAbandonedStation(Player player, FlightBoard flightBoard,
                                           boolean decision,
                                           ArrayList<ArrayList<Integer>> lists,
                                           ArrayList<ArrayList<Goods>> goodsLists) {
            throw new RuntimeException();
        }

        @Override
        public void chooseToPlaceBatteries(Player player,
                                           ArrayList<ArrayList<Integer>> lists) {
            throw new RuntimeException();
        }

        @Override
        public void chooseWhereToPutGoods(Player player,
                                          ArrayList<ArrayList<Integer>> lists,
                                          ArrayList<ArrayList<Goods>> goodsLists) {
            throw new RuntimeException();
        }
    }

    @Test
    void testBaseConstructorAndGetNextState() {
        Card_MeteorSwarm card = (Card_MeteorSwarm) mainGame.getGameState().getCardinuse();
        TestState state = new TestState(card);

        assertSame(card, state.getMeteorSwarm(), "meteorSwarm field not set correctly in constructor");
        assertSame(state, state.getNextState(), "getNextState should return the instance for stub");
    }

    @Test
    void testActivateThrowsByDefault() {
        Card_MeteorSwarm card = (Card_MeteorSwarm) mainGame.getGameState().getCardinuse();
        TestState state = new TestState(card);
        FlightBoard flightBoard = mainGame.getGameFlightBoard();
        assertThrows(RuntimeException.class,
                () -> state.activate(flightBoard),
                "Default activate should throw RuntimeException");
    }


    @Test
    void testAbstractCardDefaultBehaviors() throws Exception {
        FlightBoard board = mainGame.getGameFlightBoard();
        Player player = mainGame.getPlayerFromNickname("loreto");
        HashMap<ArrayList<Integer>, Integer> map = new HashMap<>();
        Card card = mainGame.getGameState().getCardinuse();
        assertThrows(RuntimeException.class, card::getFirePower);
        assertThrows(RuntimeException.class, card::getDaysPenalty);
        assertThrows(RuntimeException.class, card::getGoodsPenalty);
        assertThrows(RuntimeException.class, card::getGoods);
        assertThrows(RuntimeException.class, card::getCrew);

        assertThrows(RuntimeException.class, card::viewMoneyEarning);
        assertThrows(RuntimeException.class, card::viewFlightPenalty);
        assertThrows(RuntimeException.class, card::viewCrewPenalty);

        assertThrows(RuntimeException.class, card::getPlanetVector);
        assertThrows(RuntimeException.class, card::getConsequences);
        assertThrows(RuntimeException.class, card::getCauses);
    }
}