package it.polimi.ingsw.Tiles;

import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class SpaceShipTileTest {

    private List<Type_side_connector> connectors;

    @BeforeEach
    public void setUp() {
        connectors = Arrays.asList(
                Type_side_connector.SINGLE_connector,
                Type_side_connector.DOUBLE_connector,
                Type_side_connector.UNIV_connector,
                Type_side_connector.SMOOTH_SIDE
        );
    }

    @Test
    public void test_should_initial_state() {
        SpaceShipTile tile = new SpaceShipTile(1, new ArrayList<>(connectors), SSTTypes.Tile_Cabin);
        assertEquals(0, tile.getRotation());
        assertFalse(tile.isFlipped());
        assertNull(tile.getAttachedShip());
        assertEquals(SSTTypes.Tile_Cabin, tile.getType());
        assertEquals(Type_side_connector.SINGLE_connector, tile.getConnector(0));
        assertEquals(Type_side_connector.DOUBLE_connector, tile.getConnector(1));
        assertEquals(Type_side_connector.UNIV_connector, tile.getConnector(2));
        assertEquals(Type_side_connector.SMOOTH_SIDE, tile.getConnector(3));
    }

    @Test
    public void test_should_flip_and_setflip() {
        SpaceShipTile tile = new SpaceShipTile(2, new ArrayList<>(connectors), SSTTypes.Tile_Cabin);
        assertFalse(tile.isFlipped());
        tile.Flip();
        assertTrue(tile.isFlipped());
        tile.Flip();
        assertFalse(tile.isFlipped());
        tile.SetFlip(true);
        assertTrue(tile.isFlipped());
        tile.SetFlip(false);
        assertFalse(tile.isFlipped());
    }

    @Test
    public void test_should_rotate_90_right() {
        SpaceShipTile tile = new SpaceShipTile(3, new ArrayList<>(connectors), SSTTypes.Tile_Cabin);
        int initialRotation = tile.getRotation();
        tile.Rotate90right();
        assertEquals(initialRotation, tile.getRotation());
        List<Type_side_connector> expected = Arrays.asList(
                Type_side_connector.SMOOTH_SIDE,
                Type_side_connector.SINGLE_connector,
                Type_side_connector.DOUBLE_connector,
                Type_side_connector.UNIV_connector
        );
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), tile.getConnector(i));
        }
    }

    @Test
    public void test_should_checkcorrectpositioning() {
        List<Type_side_connector> connectorsCorrect = Arrays.asList(
                Type_side_connector.SINGLE_connector,
                Type_side_connector.SINGLE_connector,
                Type_side_connector.SINGLE_connector,
                Type_side_connector.SINGLE_connector
        );
        List<Type_side_connector> connectorsWrong = Arrays.asList(
                Type_side_connector.DOUBLE_connector,
                Type_side_connector.DOUBLE_connector,
                Type_side_connector.DOUBLE_connector,
                Type_side_connector.DOUBLE_connector
        );
        //DA FINIRE

    }

}
