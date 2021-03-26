package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


    public SortedBag<Ticket> TicketsGamestate() {
        SortedBag.Builder<Ticket> ticketGamestate = new SortedBag.Builder<>();
        ticketGamestate.add(new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5));
        ticketGamestate.add(new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5));
        ticketGamestate.add(new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9));
        return ticketGamestate.build();
    }

    public SortedBag<Card> CardGameState(){
        SortedBag.Builder<Card> cardGameState = new SortedBag.Builder<>();
        cardGameState.add(1, Card.ORANGE);;
        cardGameState.add(2, Card.LOCOMOTIVE);
        return cardGameState.build();
    }

    Map<PlayerId, PlayerState> map = Map.of(
            PlayerId.PLAYER_1 ,etatJoueur, PlayerId.PLAYER_2, etatJoueur );
    GameState gameState = new GameState(3, cardState, PlayerId.PLAYER_1, map, PlayerId.PLAYER_2, new Deck<Ticket>(List.of(new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
            new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5), new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9))));

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
    void withInitialyChosenTickets(){
        //var initialTickets =
    }

    @Test
    void returnTopCardAndWithoutIt(){
        var expectedValue2 = new Ticket(new Station(1, "Bâle"), new Station(4, "Brigue"), 10);
        assertTrue(List.of(Card.GREEN, Card.WHITE).contains(gameState.topCard()));
        assertEquals(1 , gameState.withoutTopTickets(2).ticketsCount());
        assertEquals(expectedValue2.text(), gameState.playerState.get(PlayerId.PLAYER_1).tickets().get(1).text());

        /*var expectedValue = new GameState(3,
                new CardState(List.of(Card.BLUE, Card.BLUE, Card.BLUE, Card.BLACK, Card.BLACK), 8, 3, Deck.of(SortedBag.of(5, Card.GREEN, 2, Card.WHITE), new Random()), SortedBag.of(3, Card.ORANGE)),
                PlayerId.PLAYER_1, map, PlayerId.PLAYER_2, new Deck<Ticket>(List.of(new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
                new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5), new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9))));
        */ //not a very good test
        assertEquals(7, gameState.withoutTopCard().cardState().deckSize());
        //assertTrue(gameState.withoutTopCard().cardState().equals(expectedValue));
    }


    @Test
    void withoutTopTicketsReturnTopTickets(){
        var expectedValue = new Ticket(new Station(33, "Zürich"), new Station(17, "Lugano"), 9);
        var expectedValue2 = new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5);
        assertEquals(expectedValue.text(), gameState.withoutTopTickets(2).tickets.topCard().text());
        assertEquals(expectedValue2.text(), gameState.withoutTopTickets(0).tickets.topCard().text());
    }

    @Test
    void topTicketsFails(){
        assertThrows(IllegalArgumentException.class,()->
                gameState.topTickets(-2));
    }

    @Test
    void withMoreDiscardedCards(){
        var expectedValue = 6;
        assertEquals(expectedValue, gameState.withMoreDiscardedCards(SortedBag.of(1, Card.BLUE,2, Card.BLACK)).cardState().discardsSize());
        assertEquals(8, gameState.withMoreDiscardedCards(SortedBag.of(1, Card.BLUE,2, Card.BLACK)).cardState().deckSize());
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

    @Test
    void stateWithClaimedRoute(){
        var routesClaimed = List.of(new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW),
                new Route("BAL_OLT_1", new Station(1, "Bâle"), new Station(20, "Olten"), 3, Route.Level.UNDERGROUND, Color.ORANGE), new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW));
        assertEquals(routesClaimed.toString(), gameState.withClaimedRoute(new Route("BAL_OLT_1", new Station(1, "Bâle"), new Station(20, "Olten"), 3, Route.Level.UNDERGROUND, Color.ORANGE), CardGameState()).claimedRoutes().toString());
    }

    @Test
    void withChosenAdditionalTickets(){
        //TICKET RETOURNE UN DECK WATZEFUCK LES ZAMIS????????????????????????????

        var expectedValue = SortedBag.of(2, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
                1, new Ticket(new Station(1, "Bâle"), new Station(4, "Brigue"), 10));
        assertEquals(expectedValue, gameState.withChosenAdditionalTickets(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(ChMap.tickets().get(0))).currentPlayerState().tickets());
    }
    @Test
    void withDrawnFaceUpCard(){
        var expectedValue = 1;
        assertEquals(expectedValue, gameState.withDrawnFaceUpCard(1).currentPlayerState().cards().countOf(Card.BLUE));
    }
    @Test
    void withBlindlyDrawnCard(){
        var expectedValue = gameState.cardState.topDeckCard();

        assertTrue(gameState.withBlindlyDrawnCard().currentPlayerState().cards().contains(gameState.cardState.topDeckCard()));
    }
    @Test
    void withClaimedRoute(){
        var expectedValue = ChMap.routes().get(4);
        assertTrue(gameState.withClaimedRoute(ChMap.routes().get(4), SortedBag.of(Card.YELLOW)).currentPlayerState().routes().contains(expectedValue));
    }
    @Test
    void lastTurnNotBegins(){
        assertFalse(gameState.lastTurnBegins());
    }

    @Test
    void lastTurnBegins(){
        //GameState newGameState = gameState.;
        assertTrue(gameState.lastTurnBegins());
    }
}
