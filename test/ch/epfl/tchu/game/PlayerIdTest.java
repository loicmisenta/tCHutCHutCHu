package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.epfl.tchu.game.PlayerId.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class PlayerIdTest {
    @Test
    void playerIdValuesAreDefinedInTheRightOrder() {
        var expectedValues = new PlayerId[]{
                PLAYER_1, PLAYER_2
        };
        assertArrayEquals(expectedValues, PlayerId.values());
        }

        @Test
        void playerIdAllIsDefinedCorrectly() {
            assertEquals(List.of(PlayerId.values()), ALL);
        }

        @Test
        void playerIdCountIsDefinedCorrectly() {
            assertEquals(2, COUNT);
        }
    }
