package it.polimi.ingsw;

import it.polimi.ingsw.Model.GameControl.GameController;
import it.polimi.ingsw.Utils.Dice.FixedDiceRoller;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.Model.GameControl.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotMistakesAccumulationLTTest {
    private Game mainGame;
    private EventBus eventBus;
    private GameController controller;

    @BeforeEach
    void setUp() {

        mainGame = new Game(0, new FixedDiceRoller(0), 10, 120);
        eventBus = mainGame.getEventBus();
        controller = new GameController(mainGame, eventBus);

    }

    @Test
    public void testShipBoardConstruction() throws Exception {
        controller.JoinGame("Loreto", 2);
        controller.JoinGame("Giuseppe", 0);

        //FIRST PLAYER
        mainGame.getGameState().pickTile(149, mainGame.getPlayerIDFromName("Loreto"));
        //System.out.println("Prova 1:" + (mainGame.getPlayer(1).getTileInHand().getConnector(2)));
        mainGame.getGameState().insertTile(2, 2, mainGame.getPlayerIDFromName("Loreto"), 180);
        //System.out.println("Prova 2:" + (mainGame.getPlayer(1).getMyShip().getShipMatrix()[2][2]).getConnector(2));

        //SECONDO PLAYER
        mainGame.getGameState().pickTile(153, mainGame.getPlayerIDFromName("Giuseppe"));
        mainGame.getGameState().insertTile(2, 2, mainGame.getPlayerIDFromName("Giuseppe"), 90);


        controller.endbuilding(mainGame.getPlayerFromNickname("Loreto").getPlayerId(), 1); // Primo input
        controller.endbuilding(mainGame.getPlayerFromNickname("Giuseppe").getPlayerId(), 2); // Secondo input
        controller.setNumTile(mainGame.getPlayerFromNickname("Giuseppe").getPlayerId(), 1);
        //assertEquals(Flight_Phase_State.class, mainGame.getGameState().getClass());
        assertEquals(SSTTypes.Tile_NonAccesiblePlace, mainGame.getPlayerFromNickname("Giuseppe").getMyShip().getShipMatrix()[2][2].getType());
        assertEquals(0, mainGame.getPlayerFromNickname("Loreto").getMyShip().getLostPieces());
        assertEquals(0, mainGame.getPlayerFromNickname("Giuseppe").getMyShip().getLostPieces());

    }
}
