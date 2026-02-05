package it.polimi.ingsw.Cards.Pirates;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Pirates.PiratesCalcState;
import it.polimi.ingsw.Model.Cards.Pirates.PiratesWinningState;
import it.polimi.ingsw.Model.Cards.Pirates.pirates_fixing_state;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.GameControl.BuildingPhaseStateLev2;
import it.polimi.ingsw.Model.GameControl.Flight_Phase_State;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.GameControl.GameController;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import it.polimi.ingsw.Utils.Dice.FixedDiceRoller;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import it.polimi.ingsw.View.TUI.TUIHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Card_PiratesTest_Subships {
    Game mainGame;
    Deck EventDeck;
    GameController gameController;

    @BeforeEach
    void setUp() {
        ArrayList<Integer> dice = new ArrayList<>();
        dice.add(8);
        dice.add(8);
        dice.add(8);
        mainGame = new Game(1, new FixedDiceRoller(dice), 10, 120);
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it.polimi.ingsw/pirates_sub.json")) {
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


        TUIHandler tui = new TUIHandler();
        for (Player p : mainGame.getPlayers()) tui.printShipBoard(p.getMyShip());
        tui.printFlightBoard(mainGame.getGameFlightBoard());

        assertEquals(6, mainGame.getPlayer(giuseppe).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giuseppe).getNumFullRounds());
        assertEquals(3, mainGame.getPlayer(loreto).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(loreto).getNumFullRounds());
        assertEquals(1, mainGame.getPlayer(giacomo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(giacomo).getNumFullRounds());
        assertEquals(0, mainGame.getPlayer(lorenzo).getPositionOnBoard());
        assertEquals(0, mainGame.getPlayer(lorenzo).getNumFullRounds());

        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof PiratesCalcState);
        mainGame.getGameState().getCardinuse().getState().getNextState();
        //gioca prima player 2

        //faccio rispondere quello sbagliato
       gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(loreto),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 4))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 1))
                ))
        );
        //quello giusto con input errato
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 4))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 1))
                ))
        );
        //input giusto
        gameController.chooseCannonBatteryPos(
                mainGame.getGameFlightBoard(),
                mainGame.getPlayer(giuseppe),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(2, 4))
                )),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1, 5, 1))
                ))
        );
        System.out.println("in teoria firepower di 2");
        //perde.

        //il giocatore sceglie di accendere i cannoni
        gameController.chooseCannonBatteryPos(
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
        System.out.println("in teoria firepower di 8 ");

        //PLAYER Sbaglito
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof PiratesWinningState);
        mainGame.getGameState().getCardinuse().getState().getNextState();
        gameController.chooseToClaimReward(true, mainGame.getPlayer(giuseppe));
        gameController.chooseToClaimReward(true, mainGame.getPlayer(loreto));
        mainGame.getGameState().getCardinuse().getCrewLost();
        mainGame.getGameState().getCardinuse().getMoneyGained();
        //dice is x for Giuseppe to face shots
        assertEquals(2, mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[1][2].getNumCharges());
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[1][5].getType());
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giuseppe), new ArrayList<>(List.of(2, 2, 1, 2)), mainGame.getGameFlightBoard());
        assertNotEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(giuseppe).getMyShip().getShipMatrix()[1][5].getType());
        assertTrue(mainGame.getGameState().getCardinuse().getState() instanceof pirates_fixing_state);
        gameController.chooseOneSubShip(6,loreto);
        gameController.chooseOneSubShip(6,giuseppe);
        gameController.chooseOneSubShip(0,giuseppe);
        gameController.chooseHowToFaceMeteors(mainGame.getPlayer(giuseppe), null, mainGame.getGameFlightBoard());

    }
}


