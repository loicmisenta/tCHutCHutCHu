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
        var expectedValue = new CardState(List.of(Card.BLACK, Card.YELLOW, Card.BLACK, Card.BLACK, Card.BLACK), 8, 0 , new Deck(List.of(Card.ORANGE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW, Card.BLUE, Card.WHITE, Card.GREEN)), SortedBag.of(Card.BLACK));


        CardState étatDesCartes = new CardState(List.of(Card.BLACK, Card.BLACK, Card.BLACK, Card.BLACK, Card.BLACK), 8, 0 , new Deck(List.of(Card.YELLOW, Card.ORANGE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW, Card.BLUE, Card.WHITE, Card.GREEN)), SortedBag.of());
        assertEquals(expectedValue.toString(), (étatDesCartes.withDrawnFaceUpCard(2)).toString());
    }


    @Test
    void deckComposéDeDiscards(){
        var expectedValue = new CardState(List.of(Card.YELLOW, Card.ORANGE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW), 9, 0, new Deck<>(List.of(Card.YELLOW, Card.BLUE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW, Card.ORANGE, Card.WHITE,Card.YELLOW, Card.ORANGE)), SortedBag.of());
        List<Card> cardsVide = List.of();

        CardState étatDesCartes =  new CardState(List.of(Card.YELLOW, Card.ORANGE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW), 0, 9, new Deck<>(cardsVide), SortedBag.of(List.of(Card.YELLOW, Card.BLUE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW, Card.ORANGE, Card.WHITE,Card.YELLOW, Card.ORANGE)));

        assertEquals(expectedValue.deckSize(), étatDesCartes.withDeckRecreatedFromDiscards(new Random()).deckSize());

    }


    @Test
    void piocheVideTopDeck(){
        List<Card> cardsVide = List.of();
        CardState étatDesCartes =  new CardState(List.of(Card.YELLOW, Card.ORANGE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW), 0, 9, new Deck<>(cardsVide), SortedBag.of(List.of(Card.YELLOW, Card.BLUE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW, Card.ORANGE, Card.WHITE,Card.YELLOW, Card.ORANGE)));

        assertThrows(IllegalArgumentException.class, () -> {
            étatDesCartes.topDeckCard();
            });

    }
    @Test
    void piocheVideWithoutTop(){
        List<Card> cardsVide = List.of();
        CardState étatDesCartes =  new CardState(List.of(Card.YELLOW, Card.ORANGE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW), 0, 9, new Deck<>(cardsVide), SortedBag.of(List.of(Card.YELLOW, Card.BLUE, Card.LOCOMOTIVE,  Card.WHITE,
                Card.YELLOW, Card.ORANGE, Card.WHITE,Card.YELLOW, Card.ORANGE)));

        assertThrows(IllegalArgumentException.class, () -> {
            étatDesCartes.withoutTopDeckCard();
        });

    }

}
