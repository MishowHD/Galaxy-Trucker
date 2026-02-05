package it.polimi.ingsw.Cards.Epidemic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.GameControl.*;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import it.polimi.ingsw.Utils.Dice.FixedDiceRoller;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Card_EpidemicTest_demo {
    Game mainGame;
    Deck EventDeck;
    GameController gameController;

    @BeforeEach
    void setUp() {
        mainGame = new Game(1,new FixedDiceRoller(0), 1 ,10);
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it.polimi.ingsw/epidemic.json")) {
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
    void cardTest1() throws Exception {
        EventBus b = mainGame.getEventBus();
        gameController = new GameController(mainGame, b);
        gameController.JoinGame("Loreto", 3);
        gameController.JoinGame("Giuseppe", 0);
        gameController.JoinGame("Giacomo", 0);

        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        gameController.activateTimer(mainGame.getPlayer(loreto));
        gameController.demoGame();
    }

    @Test
    void cardTest2() throws Exception {
        EventBus b = mainGame.getEventBus();
        gameController = new GameController(mainGame, b);
        gameController.JoinGame("Loreto", 4);
        gameController.JoinGame("Giuseppe", 0);
        gameController.JoinGame("Giacomo", 0);

        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        gameController.activateTimer(mainGame.getPlayer(loreto));
        gameController.demoGame();
    }

    @Test
    void cardTest() throws Exception {
        EventBus b = mainGame.getEventBus();
        gameController = new GameController(mainGame, b);
        gameController.JoinGame("Loreto", 2);
        gameController.JoinGame("Giuseppe", 0);

        int loreto = mainGame.getPlayerIDFromName("Loreto");
        int giuseppe = mainGame.getPlayerIDFromName("Giuseppe");
        gameController.activateTimer(mainGame.getPlayer(loreto));
        gameController.demoGame();
        // PRIMA NAVE
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getType());
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(0));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(1));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][0].getConnector(3));

        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][1].getType());
//        assertEquals(SSTTypes.Tile_Double_Cannon, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getType());
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(0));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(1));
//        assertEquals(Type_side_connector.SINGLE_connector, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(2));
//        assertEquals(Type_side_connector.SMOOTH_SIDE, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][2].getConnector(3));
//        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayer(loreto).getMyShip().getShipMatrix()[0][3].getType());

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
    }
}