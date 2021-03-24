package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class GameStateTest {

    CardState etatDesCartes = CardState.of(Deck.of(SortedBag.of(3, Card.BLUE, 2, Card.BLACK), new Random()));

    PlayerState etatJoueur = new PlayerState(SortedBag.of(
            1, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
            1, new Ticket(new Station(1, "Bâle"), new Station(4, "Brigue"), 10)),
            SortedBag.of(3, Card.WHITE, 4, Card.LOCOMOTIVE),
            List.of(new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW)));

    Map<PlayerId, PlayerState> map = Map.of(
            PlayerId.PLAYER_1 ,etatJoueur, PlayerId.PLAYER_2, etatJoueur );
    GameState gameState = new GameState(40, etatDesCartes, PlayerId.PLAYER_1, map, PlayerId.PLAYER_2, Deck.of(
            SortedBag.of(2, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
                         1, new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9)), new Random()));

    @Test
    void initialTestBuild(){
        var expectedValue = "null";
        var expectedinitialPlayer = PlayerId.PLAYER_1;
        var expectedTickets = "";
        assertEquals(expectedValue, gameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()).lastPlayer().toString());
        assertEquals(expectedinitialPlayer , gameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()).currentPlayerId()); //va fail une fois sur deux
        assertEquals(ChMap.tickets().get(0), gameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()).topTickets(1).get(0));

    }

    @Test
    void returnTopCard(){
        var expectedValue1 = Card.BLUE;
        var expectedValue2 = Card.BLACK;
        assertEquals(expectedValue1, gameState.topCard());
        assertEquals(expectedValue2 , gameState.withoutTopTickets(3).topCard());
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


    @Test
    void lastTurnBeguins(){
        PlayerState etatJoueur1 = new PlayerState(SortedBag.of(
                1, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
                1, new Ticket(new Station(1, "Bâle"), new Station(4, "Brigue"), 10)),
                SortedBag.of(3, Card.WHITE, 4, Card.LOCOMOTIVE),
                List.of(new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW),
                        new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW),
                        new Route("GEN_YVE_1", new Station(10, "Genève"), new Station(31, "Yverdon"), 6, Route.Level.OVERGROUND, null),
                        new Route("GEN_YVE_1", new Station(10, "Genève"), new Station(31, "Yverdon"), 6, Route.Level.OVERGROUND, null),
                        new Route("GEN_YVE_1", new Station(10, "Genève"), new Station(31, "Yverdon"), 6, Route.Level.OVERGROUND, null),
                        new Route("GEN_YVE_1", new Station(10, "Genève"), new Station(31, "Yverdon"), 6, Route.Level.OVERGROUND, null),
                        new Route("GEN_YVE_1", new Station(10, "Genève"), new Station(31, "Yverdon"), 6, Route.Level.OVERGROUND, null),
                        new Route("GEN_YVE_1", new Station(10, "Genève"), new Station(31, "Yverdon"), 6, Route.Level.OVERGROUND, null)));

        Map<PlayerId, PlayerState> maplast = Map.of(
                PlayerId.PLAYER_1 , etatJoueur1, PlayerId.PLAYER_2, etatJoueur );
        GameState lastTurnGameState = new GameState(10, etatDesCartes, PlayerId.PLAYER_1, maplast, null, Deck.of(SortedBag.of(new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5)), new Random()));
        Assertions.assertTrue(lastTurnGameState.lastTurnBegins());
        assertFalse(gameState.lastTurnBegins());
    }

}
