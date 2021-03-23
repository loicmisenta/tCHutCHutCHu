package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class GameStateTest {
    CardState etatDesCartes = CardState.of(Deck.of(SortedBag.of(3, Card.BLUE, 2, Card.BLACK), new Random()));
    Map<PlayerId, PlayerState> map = Map.of(
            PlayerId.PLAYER_1 , new PlayerState(SortedBag.of(
                    1, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
                    1, new Ticket(new Station(1, "Bâle"), new Station(4, "Brigue"), 10)),
                    SortedBag.of(3, Card.WHITE, 4, Card.LOCOMOTIVE),
                    List.of(new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW))
            ));
    GameState gameState = new GameState(40, etatDesCartes, PlayerId.PLAYER_1, map, PlayerId.PLAYER_2, Deck.of(
            SortedBag.of(1, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5)), new Random()));

    @Test
    void initialTest(){
        var expectedValue = "";
        assertEquals(expectedValue, GameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()));
    }
}
