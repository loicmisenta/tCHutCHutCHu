package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class GameStateTest {

    CardState etatDesCartes = CardState.of(Deck.of(SortedBag.of(3, Card.BLUE, 2, Card.BLACK), new Random()));
    //ATTENTION CARDSTATE EST MIS EN PUBLIC POUR TEST
    CardState cardState = new CardState(List.of(Card.BLUE, Card.BLUE, Card.BLUE, Card.BLACK, Card.BLACK), 8, 3, Deck.of(SortedBag.of(6, Card.GREEN, 2, Card.WHITE), new Random()), SortedBag.of(3, Card.ORANGE));

    PlayerState etatJoueur = new PlayerState(SortedBag.of(
            1, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
            1, new Ticket(new Station(1, "Bâle"), new Station(4, "Brigue"), 10)),
            SortedBag.of(3, Card.WHITE, 4, Card.LOCOMOTIVE),
            List.of(new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW)));

    Map<PlayerId, PlayerState> map = Map.of(
            PlayerId.PLAYER_1 ,etatJoueur, PlayerId.PLAYER_2, etatJoueur );
    GameState gameState = new GameState(3, cardState, PlayerId.PLAYER_1, map, PlayerId.PLAYER_2, Deck.of(
            SortedBag.of(2, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
            1, new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9)), new Random()));

    @Test
    void initialTestBuild(){
        var expectedValue = true;
        var expectedinitialPlayer = PlayerId.PLAYER_1;
        var expectedTickets = "";


        assertNull(GameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()).lastPlayer());
        //assertEquals(expectedinitialPlayer , GameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()).currentPlayerId()); //va fail une fois sur deux
        assertEquals(ChMap.tickets().get(0), GameState.initial(SortedBag.of(ChMap.tickets().get(0)), new Random()).topTickets(1).get(0));

    }

    @Test
    void returnTopCard(){
        var expectedValue1 = true;
        var expectedValue2 = new Ticket(new Station(1, "Bâle"), new Station(4, "Brigue"), 10);

        assertEquals(expectedValue1, List.of(Card.GREEN, Card.WHITE).contains(gameState.topCard()));
        assertEquals(1 , gameState.withoutTopTickets(2).ticketsCount());
        assertEquals(expectedValue2.text(), gameState.playerState.get(PlayerId.PLAYER_1).tickets().get(1).text());
    }


    //
    // FAUX JAI PAS COMPRIIIIIIIIIIIIISSSS :(
    // C'est L IN - COM - PRE - HENSIOOOONN
    // PEUT ETRE LA FACON DE TRIER
    @Test
    void withoutTopTicketsReturnTopTickets(){
        var expectedValue = new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9);
        assertEquals(expectedValue.text(), gameState.withoutTopTickets(2).tickets.topCard().text());
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
    //
    //
    // FAUX LE TRUC AVEC LIST MAIS JE PEUX PAS TESTER...


    @Test
    void withChosenAdditionalTickets(){
        var expectedValue = List.of(2, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
                1, new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9), 1, ChMap.tickets().get(0));
        assertEquals(expectedValue, List.of(gameState.withChosenAdditionalTickets(SortedBag.of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(2), ChMap.tickets().get(5))), SortedBag.of(ChMap.tickets().get(0))).tickets));
    }
    @Test
    void withDrawnFaceUpCard(){
        var expectedValue = 1;
        assertEquals(expectedValue, gameState.withDrawnFaceUpCard(1).currentPlayerState().cards().countOf(Card.BLUE));
    }
    @Test
    void withBlindlyDrawnCard(){
        var expectedValue = 1;
        assertEquals(expectedValue, gameState.withBlindlyDrawnCard().currentPlayerState().cards().countOf(Card.BLUE));
    }
}
