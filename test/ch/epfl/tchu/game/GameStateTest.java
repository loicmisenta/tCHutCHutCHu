package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class GameStateTest {

    @Test
    void initialTest(){
        var expectedValue = "";
        assertEquals(expectedValue, GameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()));
    }
}
