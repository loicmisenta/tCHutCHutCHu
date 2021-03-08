package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class DeckTest {

    @Test
    void carteDuHautSupprimé(){
        //Attention DECK OF melange les carte

        //Deck cardDeck = Deck.of(SortedBag.of(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN)), new Random());
        //Deck cardDeck = Deck.of(SortedBag.of(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN)), new Random());
        Deck cardDeck = new Deck(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN));
        var expectedvalue = new Deck(List.of( Card.LOCOMOTIVE, Card.GREEN));
        assertEquals(expectedvalue.toString(), cardDeck.withoutTopCard().toString());

    }

    @Test
    void listeDontCarteDuHautSupprimé(){

        Deck cardDeck = new Deck(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN));
        var expectedvalue = new Deck(List.of( Card.YELLOW,Card.LOCOMOTIVE, Card.GREEN));
        Deck deckModifié = cardDeck.withoutTopCard();
        assertEquals(expectedvalue.toString(), cardDeck.toString());
    }

    @Test
    void topCardWithEmptyList(){
        Deck cardDeck = new Deck(List.of());
        assertThrows(IllegalArgumentException.class, () -> {
            cardDeck.topCard();
        });
    }


    @Test
    void withoutTopCardWithEmptyList(){
        Deck cardDeck = new Deck(List.of());
        assertThrows(IllegalArgumentException.class, () -> {
            cardDeck.withoutTopCard();
        });
    }

    @Test
    void TopCardRetourneLes3PremieresCard(){
        Deck cardDeck = new Deck(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN, Card.WHITE, Card.ORANGE));
        var expectedvalue = SortedBag.of(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN));
        assertEquals(expectedvalue.toString(), cardDeck.topCards(3).toString());
    }


    @Test
    void retourneTopCard(){
        Deck cardDeck = new Deck(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN, Card.WHITE, Card.ORANGE));
        var expectedvalue = Card.YELLOW;
        assertEquals(expectedvalue.toString(), cardDeck.topCard().toString());
    }

    @Test
    void withoutFourFirstCard(){
        Deck cardDeck = new Deck(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN, Card.WHITE, Card.ORANGE, Card.BLACK));
        var expectedvalue = new Deck(List.of(Card.ORANGE, Card.BLACK));
        assertEquals(expectedvalue.toString(), cardDeck.withoutTopCards(4).toString());
    }

    @Test
    void withoutTopCardsCountImpossibleCount(){
        Deck cardDeck = new Deck(List.of(Card.YELLOW, Card.LOCOMOTIVE));
        assertThrows(IllegalArgumentException.class, () -> {
            cardDeck.withoutTopCards(3).toString();
        });
    }

    @Test
    void withoutThreeFirstCardsListUnchanged(){
        Deck cardDeck = new Deck(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN, Card.WHITE, Card.ORANGE));
        Deck deckModifie = cardDeck.withoutTopCards(2);
        var expectedvalue = new Deck(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN, Card.WHITE, Card.ORANGE));
        assertEquals(expectedvalue.toString(), cardDeck.toString());
    }
}
