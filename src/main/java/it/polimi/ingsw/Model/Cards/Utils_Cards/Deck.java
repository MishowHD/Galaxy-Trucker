package it.polimi.ingsw.Model.Cards.Utils_Cards;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.Model.Cards.Card;

public class Deck implements Serializable {
    /**
     * Represents a list of cards contained within a deck. Each card is an instance
     * of the {@link Card} class or one of its subclasses.
     *
     * This variable is the core component of the {@code Deck} class, storing
     * all the cards currently in the deck. The list is mutable and allows operations
     * such as shuffling, drawing, and removing cards.
     *
     * The elements within this list represent various card types, as defined by
     * the {@code Card} abstract class and its concrete implementations. These cards
     * can interact with the game state or players as defined in their specific logic.
     *
     * This list can be directly accessed or modified via getters, but it should
     * generally be manipulated through methods provided by the {@code Deck} class,
     * such as {@code shuffle()}, {@code drawCard()}, or {@code removeCard(Card)},
     * to maintain consistency in the game state.
     */
    private List<Card> CardList;
    /**
     * Represents the current state of the Deck, indicating whether it is occupied or not.
     *
     * This variable is used to track the availability of the Deck for certain actions.
     * It can be set to {@code true} or {@code false} using the respective setter methods.
     * - {@code true}: The Deck is currently occupied and may not be available for specific operations.
     * - {@code false}: The Deck is free and available for usage.
     */
    private boolean isOccupied = false;

    /**
     * Constructs a new Deck object with the specified list of cards.
     *
     * @param cards the list of cards to initialize the deck with
     */
    public Deck(List<Card> cards) {
        CardList = cards;
    }

    /**
     * Marks the current Deck instance as occupied by setting the internal state to true.
     * The isOccupied flag is used to track whether the Deck is currently in use or blocked.
     */
    public void setOccupied() {
        isOccupied = true;
    }

    /**
     * Marks the deck as free by setting the `isOccupied` flag to false.
     * This indicates that the deck is no longer occupied or in use.
     */
    public void setFree() {
        isOccupied = false;
    }

    /**
     * Checks if the deck is currently occupied.
     *
     * @return true if the deck is occupied, otherwise false.
     */
    public boolean isOccupied() {
        return isOccupied;
    }

    /**
     * Randomly shuffles the order of the cards in the deck.
     * The method rearranges the elements of the internal card list (CardList)
     * in a random order using {@code Collections.shuffle}.
     */
    public void shuffle() {
        Collections.shuffle(this.CardList);
    }

    /**
     * Retrieves the list of cards currently held in the deck.
     *
     * @return a list of Card objects representing the cards in the deck.
     */
    public List<Card> getCardList() {
        return CardList;
    }

    /**
     * Draws and removes the first card from the deck if the deck is not empty.
     *
     * @return the first card from the deck, or {@code null} if the deck is empty.
     */
    public Card drawCard() {
        if (CardList.isEmpty()) {
            return null;
        }
        return CardList.removeFirst();
    }

    /**
     * Removes the specified card from the card list if the list is not empty.
     *
     * @param c The card to be removed from the card list.
     */
    public void removeCard(Card c) {
        if (CardList.isEmpty()) {
            return;
        }
        CardList.remove(c);
    }

}
