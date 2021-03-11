package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PublicCardStateTest {
    @Test
    void constructorFailsWithWrongNumberOfFaceUpCards(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 2, Card.GREEN);

        assertThrows(IllegalArgumentException.class, () -> {
            new PublicCardState(originalSortedBag.toList(),5,5);
        });
    }

    @Test
    void constructorFailsWithNegativeDiscardsSize(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);

        assertThrows(IllegalArgumentException.class, () -> {
            new PublicCardState(originalSortedBag.toList(),-1,5);
        });
    }

    @Test
    void constructorFailsWithNegativeDeckSize(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);

        assertThrows(IllegalArgumentException.class, () -> {
            new PublicCardState(originalSortedBag.toList(),0,-1);
        });
    }

    @Test
    void totalSizeReturnsRightNumber(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        PublicCardState pCS = new PublicCardState(originalSortedBag.toList(),4,3);
        assertEquals(12,pCS.totalSize());
    }

    @Test
    void faceUpCardsReturnsCorrectList(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        PublicCardState pCS = new PublicCardState(originalSortedBag.toList(),4,3);
        assertEquals(originalSortedBag.toList(), pCS.faceUpCards());
        assertEquals("[BLACK, BLACK, GREEN, GREEN, GREEN]", pCS.faceUpCards().toString());
    }

    @Test
    void faceUpCardsFailsWithOutOfBoundIndex(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        PublicCardState pCS = new PublicCardState(originalSortedBag.toList(),4,3);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            pCS.faceUpCard(-1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            pCS.faceUpCard(5);
        });
    }

    @Test
    void faceUpCardsReturnsCorrectCard(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        PublicCardState pCS = new PublicCardState(originalSortedBag.toList(),4,3);
        assertEquals(Card.BLACK, pCS.faceUpCard(0));
        assertEquals(Card.GREEN, pCS.faceUpCard(4));
    }

    @Test
    void isEmptyReturnsFalseWithEmptyDeck(){
        SortedBag<Card> originalSortedBag = SortedBag.of(2, Card.BLACK, 3, Card.GREEN);
        PublicCardState pCS = new PublicCardState(originalSortedBag.toList(),1,3);
        assertFalse(pCS.isDeckEmpty());
    }

}
