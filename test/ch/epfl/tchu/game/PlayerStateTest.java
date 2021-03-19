package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {
    Ticket ticket = new Ticket(
            new Station(9, "Fribourg"),
            new Station(16, "Lucerne"), 5);
    Ticket autreTicket = new Ticket(
            new Station(9, "Fribourg"),
            new Station(16, "Lucerne"), 9);

    PlayerState playerState = new PlayerState(
            SortedBag.of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(1))),
            SortedBag.of(5, Card.BLUE, 3, Card.LOCOMOTIVE),
            List.of(ChMap.routes().get(0), ChMap.routes().get(1)));
    Route route1 = new Route("BER_LUC_1",
            new Station(3, "Berne"),
            new Station(16, "Lucerne"), 3, Route.Level.UNDERGROUND, Color.BLUE);
    Route route2 = new Route("BER_LUC_1",
            new Station(3, "Berne"),
            new Station(16, "Lucerne"), 3, Route.Level.OVERGROUND, Color.BLUE);

    @Test
    void initialWith1Card() {
        assertThrows(IllegalArgumentException.class, () -> {
            PlayerState.initial(SortedBag.of(1, Card.BLUE));
        });
    }

    @Test
    void tickets() {
        var ExpectedValue = SortedBag.of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(1)));
        assertEquals(ExpectedValue, playerState.tickets());
    }

    @Test
    void cards() {
        var ExpectedValue = SortedBag.of(5, Card.BLUE, 3, Card.LOCOMOTIVE);
        assertEquals(ExpectedValue, playerState.cards());
    }

    @Test
    void withAdded1Tickets() {
        var ExpectedValue = new PlayerState(SortedBag.of(List.of(ChMap.tickets().get(0),
                ChMap.tickets().get(1), ChMap.tickets().get(2))),SortedBag.of(5, Card.BLUE, 3, Card.LOCOMOTIVE),
                List.of(ChMap.routes().get(0), ChMap.routes().get(1)));

        assertEquals(ExpectedValue.tickets().toString(), playerState.withAddedTickets(SortedBag.of(ChMap.tickets().get(2))).tickets().toString());
    }
    @Test
    void ticketPointsTest(){
        PlayerState playerState1 = new PlayerState(SortedBag.of(List.of(
                new Ticket(
                        new Station(16, "Lucerne"),
                        new Station(33, "Zürich"), 2))),
                SortedBag.of( 2, Card.GREEN, 2, Card.YELLOW),
                List.of(new Route("ZOU_ZUR_1",
                        new Station(32, "Zoug"),
                        new Station(33, "Zürich"), 1, Route.Level.OVERGROUND, Color.GREEN),
                        new Route("LUC_ZOU_2",
                        new Station(16, "Lucerne"),
                        new Station(32, "Zoug"), 1, Route.Level.OVERGROUND, Color.YELLOW)));

        PlayerState playerState2 = new PlayerState(SortedBag.of(List.of(
                ChMap.tickets().get(12))),
                SortedBag.of(5, Card.BLUE, 3, Card.LOCOMOTIVE),
                List.of(new Route("ZOU_ZUR_1",
                        new Station(32, "Zoug"),
                        new Station(33, "Zürich"),
                        1, Route.Level.OVERGROUND, Color.GREEN),
                        new Route("LUC_ZOU_2",
                        new Station(16, "Lucerne"),
                        new Station(32, "Zoug"), 1, Route.Level.OVERGROUND, Color.YELLOW)));
        PlayerState playerState3 = new PlayerState(SortedBag.of(List.of(
                ChMap.tickets().get(12),
                new Ticket(
                        new Station(16, "Lucerne"),
                        new Station(33, "Zürich"), 2))),
                SortedBag.of(2, Card.GREEN, 2, Card.YELLOW),
                List.of(new Route("ZOU_ZUR_1",
                                new Station(32, "Zoug"),
                                new Station(33, "Zürich"),
                                1, Route.Level.OVERGROUND, Color.GREEN),
                        new Route("LUC_ZOU_2",
                                new Station(16, "Lucerne"),
                                new Station(32, "Zoug"), 1, Route.Level.OVERGROUND, Color.YELLOW)));

        var ExpectedValue = 2;
        var ExpectedValue2 = -7;
        var ExpectedValue3 = -5;
        assertEquals(ExpectedValue, playerState1.ticketPoints());
        assertEquals(ExpectedValue2, playerState2.ticketPoints());
        assertEquals(ExpectedValue3, playerState3.ticketPoints());
    }

    @Test
    void possibleClaimCardsFails(){
        PlayerState oneMorePlayerState = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(2, Card.LOCOMOTIVE), List.of( ChMap.routes().get(0), ChMap.routes().get(1), ChMap.routes().get(0), ChMap.routes().get(0), ChMap.routes().get(0), ChMap.routes().get(0), ChMap.routes().get(0), ChMap.routes().get(0), ChMap.routes().get(0), ChMap.routes().get(0), ChMap.routes().get(0)));
        assertThrows(IllegalArgumentException.class, () -> {
            oneMorePlayerState.possibleClaimCards(ChMap.routes().get(2));
        });
    }



    @Test
    void possibleAdditionalCardsFails(){
        assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(0, SortedBag.of(2, Card.YELLOW), SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(2, SortedBag.of(), SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE));
        });

    }

    @Test
    void withAddedCards() {
        var expectedValue = "{5×BLUE, 2×YELLOW, 3×LOCOMOTIVE}";
        assertEquals(expectedValue, playerState.withAddedCards(SortedBag.of(2, Card.YELLOW)).cards().toString());

    }

    @Test
    void canClaimRouteAndPossibleClaim() {
        assertTrue(playerState.canClaimRoute(route1));

        var expectedValue = "[{3×BLUE}, {2×BLUE, LOCOMOTIVE}, {BLUE, 2×LOCOMOTIVE}, {3×LOCOMOTIVE}]";
        assertEquals(expectedValue, playerState.possibleClaimCards(route1).toString());
        var expectedValue2 = "[{3×BLUE}]";
        assertEquals(expectedValue2, playerState.possibleClaimCards(route2).toString());
    }


    @Test
    void possibleAdditionalCards() {
        var expectedValue = "[{2×BLUE}, {BLUE, LOCOMOTIVE}, {2×LOCOMOTIVE}]";
        var expectedValue2 = "[{3×BLUE}, {2×BLUE, LOCOMOTIVE}, {BLUE, 2×LOCOMOTIVE}]";
        assertEquals(expectedValue, playerState.possibleAdditionalCards(2, SortedBag.of(2 , Card.BLUE), SortedBag.of(2, Card.BLUE, 1, Card.GREEN)).toString());
        assertEquals(expectedValue2, playerState.possibleAdditionalCards(3, SortedBag.of(1, Card.LOCOMOTIVE, 1, Card.BLUE), SortedBag.of(3, Card.BLUE)).toString());
    }

    @Test
    void possibleAdditionalPasDeCartesPossiblesAjoutables(){
        PlayerState newPlayer = new PlayerState(SortedBag.of(autreTicket), SortedBag.of(1, Card.GREEN), List.of(route1));
        var expectedValueForNewPlayer = "[]";
        assertEquals(expectedValueForNewPlayer, newPlayer.possibleAdditionalCards(1, SortedBag.of(1, Card.LOCOMOTIVE), SortedBag.of(3, Card.YELLOW)).toString());

    }

    @Test
    void possibleAdditionalAvecCarteQueLeJoueurNAPas(){
        //doit retourner une liste vide
        var expectedValue = "[{LOCOMOTIVE}]";
        assertEquals(expectedValue, playerState.possibleAdditionalCards(1, SortedBag.of(1, Card.GREEN), SortedBag.of(3, Card.LOCOMOTIVE)).toString());

    }

    @Test
    void withClaimedRoute() {
        PlayerState otherPlayerState = playerState.withClaimedRoute(ChMap.routes().get(2), SortedBag.of(1, Card.LOCOMOTIVE));
        var expectedValue = List.of( ChMap.routes().get(0), ChMap.routes().get(1), ChMap.routes().get(2));
        assertEquals(expectedValue, otherPlayerState.routes());

    }

    @Test
    void finalPoints(){
        assertEquals(-4, (new PlayerState(SortedBag.of(ticket), SortedBag.of(1, Card.LOCOMOTIVE), List.of(ChMap.routes().get(1)))).finalPoints());
        assertEquals(-5 , (new PlayerState(SortedBag.of(autreTicket), SortedBag.of(3, Card.LOCOMOTIVE), List.of(ChMap.routes().get(2)))).finalPoints());
    }


    @Test
    void AdditionalCardTunnelCasLimit(){
        PlayerState playerStateCopy = new PlayerState(
                SortedBag.of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(1))),
                SortedBag.of(3, Card.BLUE),
                List.of(ChMap.routes().get(0), ChMap.routes().get(1)));
        assertEquals(List.of(), playerStateCopy.possibleAdditionalCards(2, SortedBag.of(3, Card.BLUE), SortedBag.of(3, Card.RED)));
    }
}