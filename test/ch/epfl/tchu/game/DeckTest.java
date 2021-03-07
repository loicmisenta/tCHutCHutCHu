package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class DeckTest {

    @Test
    void carteDuHautSupprimé(){
        Deck cardDeck = Deck.of(SortedBag.of(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN)), new Random());
        var expectedvalue = new Deck(List.of( Card.LOCOMOTIVE, Card.GREEN));
        assertEquals(expectedvalue.toString(), cardDeck.withoutTopCard().toString());
    }

    //

    @Test
    void listeDontCarteDuHautSupprimé(){
        Deck cardDeck = Deck.of(SortedBag.of(List.of(Card.YELLOW, Card.LOCOMOTIVE, Card.GREEN)), new Random());
        var expectedvalue = new Deck(List.of( Card.LOCOMOTIVE, Card.GREEN));
        assertEquals(expectedvalue.toString(), cardDeck.withoutTopCard().toString());
    }
}
