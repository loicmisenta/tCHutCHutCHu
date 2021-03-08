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
public class CardStateTest {

    @Test
    void moinsDeCinqCartesPioche(){
        assertThrows(IllegalArgumentException.class, () -> {
            CardState étatDesCartes = CardState.of(Deck.of(SortedBag.of(List.of(Card.YELLOW, Card.LOCOMOTIVE)), new Random()));
        });
    }

    @Test
    void carteVisibleRemplacéParCarteBleue(){
        var expectedValue = CardState.of(new Deck(List.of(Card.YELLOW, Card.ORANGE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW, Card.WHITE)));
        CardState étatDesCartes = CardState.of(new Deck(List.of(Card.YELLOW, Card.BLUE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW, Card.ORANGE, Card.WHITE)));
        assertEquals(expectedValue, étatDesCartes.withDrawnFaceUpCard(2));
    }



    //@Test
    //void deckComposéDeDiscards(){
    //    var expectedValue = CardState.of(new Deck(List.of(Card.YELLOW, Card.ORANGE, Card.LOCOMOTIVE,  Card.WHITE,
    //            Card.YELLOW, Card.WHITE)));
    //    CardState étatDesCartes = CardState.of(new Deck(List.of(Card.YELLOW, Card.BLUE, Card.LOCOMOTIVE,  Card.WHITE,
    //            Card.YELLOW, Card.ORANGE, Card.WHITE)));
    //    assertEquals(expectedValue, étatDesCartes.withDeckRecreatedFromDiscards(new Random()));

    //}

    //@Test
    //void piocheVideTopDeck(){
    //    CardState étatDesCartes = new CardState(List.of(),0 ,3 , new Deck<Card>(List.of(Card.YELLOW)));
    //    assertThrows(IllegalArgumentException.class, () -> {
    //           étatDesCartes.topDeckCard();
    //        });

    //}

    //@Test
    //void piocheVideWithoutTop(){
    //    CardState étatDesCartes = new CardState(List.of(),0 ,3 , new Deck<Card>(List.of(Card.YELLOW)));
    //    assertThrows(IllegalArgumentException.class, () -> {
    //           étatDesCartes.withoutTopDeckCard();
    //        });

    //}

}
