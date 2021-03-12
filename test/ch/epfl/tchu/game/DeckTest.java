package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    /**
    @Test
    void ofDoesNotChangeOriginalSortedBag(){
        SortedBag<Card> originalSortedBag1 = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        SortedBag<Card> originalSortedBag2 = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Random rng = new Random();
        Collections.shuffle(originalSortedBag1.toList(), rng);
        assertEquals(originalSortedBag2, originalSortedBag1);
    }

    @Test
    void differenceWorksAsWeWant(){
        SortedBag<Card> originalSortedBag1 = SortedBag.of(2, Card.BLACK, 1, Card.GREEN);
        SortedBag<Card> originalSortedBag2 = SortedBag.of(1, Card.GREEN);
        SortedBag<Card> difference = originalSortedBag1.difference(originalSortedBag2);
        SortedBag<Card> expectedSortedBag = SortedBag.of(2, Card.BLACK);

        assertEquals(expectedSortedBag, difference);
    }

    @Test
    void ofShuffles(){
        SortedBag<Card> originalSortedBag1 = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Random rng = new Random();
        Deck<Card> notShuffled = new Deck(originalSortedBag1.toList());
        Deck<Card> shuffled = Deck.of(originalSortedBag1, rng);
        assertNotEquals(notShuffled.cards.toString(), shuffled.cards.toString());
    }


    @Test
    void topCardFailsWithEmptyDeck(){
        SortedBag<Card> emptyBag = SortedBag.of();
        Deck<Card> emptyDeck = new Deck(emptyBag.toList());
        assertThrows(IllegalArgumentException.class, () -> {
            emptyDeck.topCard();
        });
    }

    @Test
    void isEmpty(){
        SortedBag<Card> emptyBag = SortedBag.of();
        Deck<Card> emptyDeck = new Deck(emptyBag.toList());
        assertTrue(emptyDeck.isEmpty());
    }

    @Test
    void topCardReturnsRightCard(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());
        assertEquals(Card.GREEN, originalDeck.topCard());
    }

    @Test
    void withoutTopCardFailsWithEmptyDeck(){
        SortedBag<Card> emptyBag = SortedBag.of();
        Deck<Card> emptyDeck = new Deck(emptyBag.toList());
        assertThrows(IllegalArgumentException.class, () -> {
            emptyDeck.withoutTopCard();
        });
    }



    @Test
    void withoutTopCardReturnsRightDeck(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());
        Deck<Card> expectedDeck = new Deck(SortedBag.of(2, Card.BLACK, 2, Card.GREEN).toList());

        assertEquals(expectedDeck.cards.toString(), originalDeck.withoutTopCard().cards.toString());
    }


    @Test
    void withoutTopCardReturnsNewDeck(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());
        assertNotEquals(originalDeck, originalDeck.withoutTopCard());
    }

    @Test
    void topCardsFailsWithNegativeParam(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());

        assertThrows(IllegalArgumentException.class, () -> {
            originalDeck.topCards(-1);
        });
    }

    @Test
    void topCardsFailsWithSizeOfDeckPlus1Param(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());

        assertThrows(IllegalArgumentException.class, () -> {
            originalDeck.topCards(originalDeck.size() + 1);
        });
    }

    @Test
    void topCardsReturnsRightCards(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());
        assertEquals(SortedBag.of(2, Card.GREEN), originalDeck.topCards(2));
    }

    @Test
    void topCardsWorksWithNullParam(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());
        assertEquals(SortedBag.of(), originalDeck.topCards(0));
    }

    @Test
    void withoutTopCardsFailsWithEmptyDeck(){
        SortedBag<Card> emptyBag = SortedBag.of();
        Deck<Card> emptyDeck = new Deck(emptyBag.toList());
        assertThrows(IllegalArgumentException.class, () -> {
            emptyDeck.withoutTopCards(3);
        });
    }

    @Test
    void withoutTopCardsFailsWithNegativeParam(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());

        assertThrows(IllegalArgumentException.class, () -> {
            originalDeck.withoutTopCards(-1);
        });
    }

    @Test
    void withoutTopCardsFailsWithSizeOfDeckPlus1Param(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());

        assertThrows(IllegalArgumentException.class, () -> {
            originalDeck.withoutTopCards(originalDeck.size() + 1);
        });
    }


    @Test
    void withoutTopCardsReturnsRightCards(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        Deck<Card> originalDeck = new Deck(originalSortedBag.toList());
        Deck<Card> expectedDeck = new Deck(SortedBag.of(2, Card.BLACK, 1, Card.GREEN).toList());
        assertEquals(expectedDeck.cards.toString(), originalDeck.withoutTopCards(2).cards.toString());
    }
    **/
}

