package it.polimi.ingsw.Cards;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.View.TUI.TUIHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it/polimi/ingsw/json/mazzo.json")) {
            if (input == null) {
                System.out.println("File JSON non trovato!");
                return;
            }
            List<Card> GameCards = mapper.readValue(input, new TypeReference<List<Card>>() {
            });
            deck = new Deck(GameCards); // Assign the created deck to the field
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del mazzo");
            return;
        }
        TUIHandler tui = new TUIHandler();
        for (Card card : deck.getCardList()) {
            tui.printCard(card, null);
            System.out.println();
        }

    }

    @Test
    void test_should_shuffle() {
        List<Card> beforeShuffle = new ArrayList<>(deck.getCardList());
        deck.shuffle();
        List<Card> afterShuffle = deck.getCardList();
        assertNotEquals(beforeShuffle, afterShuffle);
    }

    @Test
    void test_draw_card() {
        List<Card> beforeDraw = new ArrayList<>(deck.getCardList());
        Card card = deck.drawCard();
        List<Card> afterDraw = new ArrayList<>(deck.getCardList());
        assertNotNull(card);
        assertEquals(beforeDraw.getFirst(), card);
        assertEquals(beforeDraw.size() - 1, afterDraw.size());
    }

    @Test
    void test_remove_card() {
        assertFalse(deck.getCardList().isEmpty());
        List<Card> beforeRemove = new ArrayList<>(deck.getCardList());
        deck.removeCard(deck.getCardList().getFirst());
        assertFalse(deck.getCardList().contains(beforeRemove.getFirst()));
        assertEquals(beforeRemove.size() - 1, deck.getCardList().size());
    }

    @Test
    void test_merge_decks() {
        // per implementare serve FlightBoard con metodi
    }

    @Test
    public void testDeckInstantiationAndFunctionality() {
        assertNotNull(deck, "Il mazzo non è stato creato.");
        int deckSize = deck.getCardList().size();
        assertTrue(deckSize > 0, "Il mazzo è vuoto.");
        for (int i = 0; i < deckSize; i++) {
            Card card = deck.getCardList().get(i);
            assertNotNull(card, "La carta all'indice " + i + " è null.");
            assertNotNull(card.getId(), "La carta all'indice " + i + " non ha un id.");
            assertNotNull(card.getCardName(), "La carta all'indice " + i + " non ha un nome.");
            assertNotNull(card.getCardLevel(), "La carta all'indice " + i + " non ha un livello.");
            assertNotNull(card.isTest(), "La carta all'indice " + i + " non vede se è di test.");
        }

        System.out.println("Test riuscito: il mazzo contiene " + deckSize + " carte istanziate correttamente.");
    }

    @Test
    public void testDeckShuffleAndDraw() {
        int deckSize = deck.getCardList().size();
        for (int i = 0; i <= deckSize; i++) {
            deck.drawCard();
        }
    }
}