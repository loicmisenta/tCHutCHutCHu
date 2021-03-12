package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ch.epfl.tchu.SortedBag.of;
import static org.junit.jupiter.api.Assertions.*;

public class CardStateTest {
    SortedBag<Card> cards = SortedBag.of(Card.ALL);
    Deck<Card> deck = Deck.of(cards, new Random());
    CardState cardStateTest = CardState.of(deck);

    SortedBag<Card> cards2 = SortedBag.of(1,Card.BLUE,1,Card.GREEN);
    Deck<Card> deck2Card = Deck.of(cards2, new Random());

    SortedBag<Card> cards3 = SortedBag.of();
    Deck<Card> deckVide = Deck.of(cards3, new Random());

    SortedBag<Card> cards4 = SortedBag.of(4,Card.BLUE,1,Card.GREEN);
    Deck<Card> deck4CardNoDeck = Deck.of(cards4, new Random());

    SortedBag<Card> cards5 = SortedBag.of(4,Card.BLUE,2,Card.GREEN);
    Deck<Card> deck5Card = Deck.of(cards5, new Random());

    @Test
    void ofTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            CardState.of(deckVide);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            CardState.of(deck2Card);
        });

        assertEquals(deck.topCards(5).toList(), cardStateTest.faceUpCards());

        assertEquals(deck.withoutTopCards(5).cards, cardStateTest.deck.cards); /*TODO*/
    }

    @Test
    void withDrawnFaceUpCardTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            CardState.of(deck4CardNoDeck).withDrawnFaceUpCard(3);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            cardStateTest.withDrawnFaceUpCard(5);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            cardStateTest.withDrawnFaceUpCard(-1);
        });

        CardState cardStateTest21 = CardState.of(deck);
        CardState cardStateTest22 = cardStateTest21.withDrawnFaceUpCard(1);
        assertEquals(cardStateTest21.deckSize()-1,cardStateTest22.deckSize());
        assertNotEquals(cardStateTest21.faceUpCard(1),cardStateTest22.faceUpCard(1));
        assertEquals(cardStateTest21.topDeckCard(),cardStateTest22.faceUpCard(1));

    }

    @Test
    void topDeckCardTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            CardState.of(deck4CardNoDeck).topDeckCard();
        });
        CardState a = CardState.of(deck5Card);
        assertEquals(a.deck.cards.get(0), a.topDeckCard()); /*TODO*/
    }

    @Test
    void withoutTopDeckCardTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            CardState.of(deck4CardNoDeck).topDeckCard();
        });
        CardState c1 = CardState.of(deck5Card);
        assertEquals(1,c1.deckSize());
        assertEquals(0,c1.withoutTopDeckCard().deckSize());
        CardState c2 =cardStateTest.withoutTopDeckCard();
        assertEquals(cardStateTest.deckSize()-1,c2.deckSize());
        ArrayList<Card> arrayList = new ArrayList<Card>(cardStateTest.deck.cards); /*TODO*/
        arrayList.remove(0);
        assertEquals(arrayList,c2.deck.cards); /*TODO*/
    }

    @Test
    void withDeckRecreatedFromDiscardsTest(){
        //assertThrows(IllegalArgumentException.class, () -> {
        //    cardStateTest.withDeckRecreatedFromDiscards(new Random());
        //});
        CardState a = CardState.of(deck4CardNoDeck);
        //assertTrue(a.isDeckEmpty());
        //assertEquals(0,a.discardsSize());
        CardState discardsFull = CardState.of(deck4CardNoDeck).withMoreDiscardedCards(of(3,Card.GREEN,1,Card.BLUE));
        //assertEquals(4,discardsFull.discardsSize());
        //assertTrue(discardsFull.isDeckEmpty());
        CardState c = discardsFull.withDeckRecreatedFromDiscards(new Random());
        //assertFalse(c.isDeckEmpty());
        //assertTrue(a.isDeckEmpty());
        //assertTrue(discardsFull.isDeckEmpty());
        List<Card> newElem = List.of(Card.BLUE,Card.GREEN,Card.GREEN,Card.GREEN);
        //List<Card> outputElem = c.deck.cards; /*TODO*/
        //assertTrue(newElem.size() == outputElem.size() && newElem.containsAll(outputElem) && outputElem.containsAll(newElem));
    }

    @Test
    void withMoreDiscardedCardsTest(){
        assertEquals(List.of(Card.BLUE),CardState.of(deck).withMoreDiscardedCards(of(Card.BLUE)).discards.toList());/*TODO*/
        CardState card = CardState.of(deck);
        CardState card2 = card.withMoreDiscardedCards(of(1,Card.GREEN,1,Card.BLUE));
        assertEquals(List.of(Card.BLUE,Card.GREEN),card2.discards.toList()); /*TODO*/
        assertNotEquals(List.of(Card.BLUE,Card.GREEN),card.discards.toList());/*TODO*/
        assertEquals(List.of(Card.BLUE,Card.GREEN,Card.GREEN,Card.GREEN),CardState.of(deck).withMoreDiscardedCards(of(3,Card.GREEN,1,Card.BLUE)).discards.toList());/*TODO*/
    }


}
