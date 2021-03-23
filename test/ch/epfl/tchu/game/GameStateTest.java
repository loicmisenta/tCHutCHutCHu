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
            SortedBag.of(2, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
                         1, new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9)), new Random()));

    @Test
    void initialTest(){
        var expectedValue = "";
        var initialPlayer = " ";
        assertEquals(expectedValue, gameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()).lastPlayer());
        assertEquals(initialPlayer , gameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()).currentPlayerState());
    }

    @Test
    void returnTopCard(){
        var expectedValue = Card.BLUE;
        assertEquals(expectedValue , gameState.withoutTopTickets(2).topCard());
    }

    @Test
    void withoutTopTicketsReturnTopTickets(){
        var expectedValue = "";
        assertEquals(expectedValue, gameState.withoutTopTickets(2).topTickets(1).toString());
    }


    @Test

    void withMoreDiscardedCards(){
        var expectedValue = 3;
        assertEquals(expectedValue, gameState.withMoreDiscardedCards(SortedBag.of(1, Card.BLUE,2, Card.BLACK)).cardState().discardsSize());
    }


}
