package it.polimi.ingsw;

import it.polimi.ingsw.Utils.Dice.FixedDiceRoller;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.GameControl.GameController;
import org.junit.jupiter.api.Test;

public class TestExeption {
    private Player Client1, Client2, Client3, Client4;
    private Game mainGame;
    private GameController gameController;

    @Test
    public void testExeption() {

        Client1 = new Player(1, "Loreto", null, null);
        Client2 = new Player(2, "Giuseppe", null, null);
        Client3 = new Player(3, "Giacomo", null, null);
        Client4 = new Player(4, "Lorenzo", null, null);
        mainGame = new Game(0, new FixedDiceRoller(0), 10, 120);
        gameController = new GameController(mainGame, mainGame.getEventBus());
        gameController.JoinGame("Loreto", 4);
        gameController.JoinGame("Giuseppe", 0);
        gameController.JoinGame("Giacomo", 0);
        gameController.JoinGame("Lorenzo", 0);
        mainGame.getPlayer(mainGame.getPlayerIDFromName("Loreto")).setMyShip(null);
    }
}
