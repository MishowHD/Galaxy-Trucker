package it.polimi.ingsw.Tiles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.View.TUI.TUIHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TilesInitTest {
    private ArrayList<SpaceShipTile> GameTiles;

    @BeforeEach
    void setUp() {

        ObjectMapper mappertiles = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it/polimi/ingsw/json/tiles.json")) {
            if (input == null) {
                System.out.println("File JSON non trovato!");
                return;
            }
            GameTiles = mappertiles.readValue(input, new TypeReference<ArrayList<SpaceShipTile>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del mazzo");
            return;
        }
    }

    @Test
    public void testTilesInstantiationAndFunctionality() {
        assertNotNull(GameTiles, "La lista di tiles non è stata inizializzata");
        assertTrue(GameTiles.size() > 0, "La lista di tiles è vuota");
        for (SpaceShipTile tile : GameTiles) {
            assertTrue(tile.getID() >= 0, "Il tile con id " + tile.getID() + " ha un id non valido");
            try {
                assertNotNull(tile.getConnector(0), "Il tile con id " + tile.getID() + " ha una lista di connettori nulla o vuota");
            } catch (IndexOutOfBoundsException e) {
                fail("Il tile con id " + tile.getID() + " ha una lista di connettori vuota");
            }
            assertNotNull(tile.getType(), "Il tile con id " + tile.getID() + " non ha un tipo definito");
            assertEquals(0, tile.getRotation(), "La tile con id " + tile.getID() + " non ha la rotazione iniziale a 0");
            assertFalse(tile.isFlipped(), "La tile con id " + tile.getID() + " non è impostata come non flipped di default");
            assertNull(tile.getAttachedShip(), "La tile con id " + tile.getID() + " dovrebbe avere attachedShip nullo di default");
        }

        System.out.println("Test Tiles: " + GameTiles.size() + " tiles caricate e istanziate correttamente.");
        TUIHandler tuiHandler = new TUIHandler();
        tuiHandler.printTilesArray(GameTiles);
    }
}
