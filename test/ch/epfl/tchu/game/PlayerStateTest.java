package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {

    PlayerState playerState = new PlayerState(SortedBag.of(List.of(ChMap.tickets().get(0),
            ChMap.tickets().get(1))),SortedBag.of(5, Card.BLUE, 3, Card.LOCOMOTIVE),
            List.of(ChMap.routes().get(0), ChMap.routes().get(1)));
    Route route = ChMap.routes().get(0);

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

        assertEquals(ExpectedValue.toString(), playerState.withAddedTickets(SortedBag.of(ChMap.tickets().get(2))).toString());
    }

    @Test
    void withAddedCards() {

    }

    @Test
    void withAddedCard() {
    }

    @Test
    void canClaimRoute() {
        //assertTrue(playerState.canClaimRoute(route));
        //var expectedValue = 2 bleu, 1 bleu et 1 wagon, 2 wagons
        //assertEquals(expectedValue, playerState.possibleClaimCards(route));
    }

    @Test
    void possibleClaimCards() {
    }

    @Test
    void possibleAdditionalCards() {
    }

    @Test
    void withClaimedRoute() {
    }
}