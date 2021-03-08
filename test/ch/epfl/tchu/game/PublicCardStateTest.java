package ch.epfl.tchu.game;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class PublicCardStateTest {

    @Test
    void ConstructorException(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicCardState(List.of(Card.BLUE, Card.BLACK), 2, 2);
        });
    }

    @Test
    void TailleTotal(){
        PublicCardState carte = new PublicCardState(List.of(Card.BLUE, Card.BLACK, Card.YELLOW, Card.GREEN, Card.VIOLET), 2, 1);
        var ExpectedValue = 8;
        assertEquals(ExpectedValue, carte.totalSize());
    }
    @Test
    void retourneLa2emeCarteFaceVisible(){
        PublicCardState carte = new PublicCardState(List.of(Card.BLUE, Card.BLACK, Card.YELLOW, Card.GREEN, Card.VIOLET), 2, 1);
        var ExpectedValue = Card.BLACK;
        assertEquals(ExpectedValue, carte.faceUpCard(1));
    }
    @Test
    void retourneLa6emeCarteFaceVisibleImpossible(){
        PublicCardState carte = new PublicCardState(List.of(Card.BLUE, Card.BLACK, Card.YELLOW, Card.GREEN, Card.VIOLET), 2, 1);
        assertThrows(IndexOutOfBoundsException.class, () -> {
           carte.faceUpCard(6);
                });
    }
    @Test
    void PiocheVide(){
        PublicCardState carte = new PublicCardState(List.of(Card.BLUE, Card.BLACK, Card.YELLOW, Card.GREEN, Card.VIOLET), 0, 1);
        var ExpectedValue = true;
        assertEquals(ExpectedValue, carte.isDeckEmpty());
    }

}
