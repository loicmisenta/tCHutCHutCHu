package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerStateTestTancredii {

    private PlayerState getPS() {
        // TICKETS
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)); // BAL -> BER / BAL -> BRI

        // ROUTES
        List<Route> routes = new ArrayList<>();
        routes.add(ChMap.routes().get(46));
        routes.add(ChMap.routes().get(6));
        routes.add(ChMap.routes().get(40));
        routes.add(ChMap.routes().get(19));
        routes.add(ChMap.routes().get(67));
        routes.add(ChMap.routes().get(7));

        return new PlayerState(tickets, SortedBag.of(), routes);
    }

    @Test
    void possibleAdditionalCardsTeachersExample(){
        SortedBag.Builder<Card> initialCards = new SortedBag.Builder<>();
        initialCards.add(3, Card.GREEN);
        initialCards.add(2, Card.LOCOMOTIVE);
        initialCards.add(2, Card.BLUE);
        SortedBag<Card> iC = initialCards.build();
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder<>();
        drawnCards.add(3, Card.BLACK);

        SortedBag<Card> dC = drawnCards.build();
        PlayerState pS = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), iC, List.of(ChMap.routes().get(0)));
        List<SortedBag<Card>> expectedList = new ArrayList<>();
        expectedList.add(SortedBag.of(2, Card.GREEN));
        expectedList.add(SortedBag.of(1, Card.LOCOMOTIVE, 1, Card.GREEN));
        expectedList.add(SortedBag.of(2, Card.LOCOMOTIVE));
        assertEquals(expectedList, pS.possibleAdditionalCards(2, SortedBag.of(1, Card.GREEN), dC));
    }

    @Test
    void simpleTicketPointsTest() {
        PlayerState ps = getPS();

        final int actualPoints = ps.ticketPoints();
        assertEquals(-5, actualPoints);
    }

    @Test
    void initialFailsWithIllegalParam(){
        assertThrows(IllegalArgumentException.class, () -> {
            PlayerState.initial(SortedBag.of(3, Card.LOCOMOTIVE));
            PlayerState.initial(SortedBag.of(5, Card.LOCOMOTIVE));
        });
    }

    @Test
    void initialCreatesEmptyTicketsAndRoute(){
        PlayerState pS = PlayerState.initial(SortedBag.of(4, Card.LOCOMOTIVE));
        assertEquals(SortedBag.of(), pS.tickets());
        assertEquals(List.of(), pS.routes());
    }

    @Test
    void gettersWorkCorrectly(){
        PlayerState pS = new PlayerState(getPS().tickets(), SortedBag.of(3, Card.LOCOMOTIVE), List.of(ChMap.routes().get(0), ChMap.routes().get(1)));
        assertEquals(SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)), pS.tickets());
        assertEquals(SortedBag.of(3, Card.LOCOMOTIVE), pS.cards());
    }

    @Test
    void withMethods(){
        PlayerState pS = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(3, Card.LOCOMOTIVE), List.of(ChMap.routes().get(0), ChMap.routes().get(1)));
        // withAddedTickets
        assertEquals(SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(2)), pS.withAddedTickets(SortedBag.of(ChMap.tickets().get(2))).tickets());
        // withAddedCard
        assertEquals(SortedBag.of(3, Card.LOCOMOTIVE, 1, Card.GREEN), pS.withAddedCard(Card.GREEN).cards());
        assertEquals(SortedBag.of(4, Card.LOCOMOTIVE), pS.withAddedCard(Card.LOCOMOTIVE).cards());

        // withAddedCards
        assertEquals(SortedBag.of(4, Card.LOCOMOTIVE, 2, Card.BLUE), pS.withAddedCards(SortedBag.of(1, Card.LOCOMOTIVE, 2, Card.BLUE)).cards());
        assertEquals(SortedBag.of(3, Card.LOCOMOTIVE, 2, Card.BLUE), pS.withAddedCards(SortedBag.of(2, Card.BLUE)).cards());
        assertEquals(SortedBag.of(3, Card.LOCOMOTIVE, 1, Card.BLUE), pS.withAddedCards(SortedBag.of(1, Card.BLUE)).cards());

        // withClaimedRoute
        PlayerState newPS = pS.withClaimedRoute(ChMap.routes().get(2), SortedBag.of(1, Card.LOCOMOTIVE));
        assertEquals(List.of(ChMap.routes().get(0), ChMap.routes().get(1), ChMap.routes().get(2)), newPS.routes());
        assertEquals(SortedBag.of(2, Card.LOCOMOTIVE), newPS.cards());
    }

    @Test
    void canClaimRoute(){
        PlayerState pS = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(4, Card.LOCOMOTIVE), List.of( ChMap.routes().get(0), ChMap.routes().get(1)));
        // should work
        assertTrue(pS.canClaimRoute(ChMap.routes().get(1)));
        // wrong color
        assertTrue(!pS.canClaimRoute(ChMap.routes().get(3)));
        // not enough cars left
        for (int i = 0; i < 8; ++i){
            pS = pS.withClaimedRoute(ChMap.routes().get(0), SortedBag.of());
        }
    }

    @Test
    void possibleClaimCardsFailsWithIllegalParam(){
        PlayerState pS = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(4, Card.LOCOMOTIVE), List.of( ChMap.routes().get(0), ChMap.routes().get(1)));

        for (int i = 0; i < 8; ++i){
            pS = pS.withClaimedRoute(ChMap.routes().get(0), SortedBag.of());
        }

        final PlayerState finalPS = pS;
        List<SortedBag<Card>> possible1 = finalPS.possibleClaimCards(ChMap.routes().get(2));
        assertThrows(IllegalArgumentException.class, () -> {
            List<SortedBag<Card>> possible2 = finalPS.possibleClaimCards(ChMap.routes().get(0));
        });
    }

    @Test
    void possibleAdditionalCardsFailsWithIllegalParam(){
        PlayerState pS = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(4, Card.LOCOMOTIVE), List.of( ChMap.routes().get(0), ChMap.routes().get(1)));

        // additionalCardCount
        assertThrows(IllegalArgumentException.class, () -> {
            pS.possibleAdditionalCards(0, SortedBag.of(1, Card.LOCOMOTIVE), SortedBag.of(2, Card.BLUE));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            pS.possibleAdditionalCards(4, SortedBag.of(1, Card.LOCOMOTIVE), SortedBag.of(3, Card.BLUE));
        });

        // empty initialCards
        assertThrows(IllegalArgumentException.class, () -> {
            pS.possibleAdditionalCards(3, SortedBag.of(), SortedBag.of(3, Card.BLUE));
        });

        // more than 2 different types of cards
        SortedBag.Builder<Card> initialCards = new SortedBag.Builder<>();
        initialCards.add(3, Card.GREEN);
        initialCards.add(2, Card.LOCOMOTIVE);
        initialCards.add(2, Card.BLUE);
        SortedBag<Card> iC = initialCards.build();
        assertThrows(IllegalArgumentException.class, () -> {
            pS.possibleAdditionalCards(3, SortedBag.of(1, Card.BLUE), SortedBag.of(2, Card.BLUE));
        });

        // drawn cards doesn't exactly contain 3 cards
        assertThrows(IllegalArgumentException.class, () -> {
            pS.possibleAdditionalCards(3, SortedBag.of(1, Card.BLUE), SortedBag.of(2, Card.BLUE));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            pS.possibleAdditionalCards(3, SortedBag.of(1, Card.BLUE), SortedBag.of(4, Card.BLUE));
        });
    }

    @Test
    void possibleAdditionalCards(){
        // colored underground route
        SortedBag.Builder<Card> initialCards1 = new SortedBag.Builder<>();
        initialCards1.add(2, Card.GREEN);
        initialCards1.add(4, Card.LOCOMOTIVE);
        initialCards1.add(7, Card.RED);
        SortedBag<Card> iC1 = initialCards1.build();
        SortedBag.Builder<Card> drawnCards1 = new SortedBag.Builder<>();
        drawnCards1.add(1, Card.GREEN);
        drawnCards1.add(1, Card.LOCOMOTIVE);
        drawnCards1.add(1, Card.BLUE);
        SortedBag<Card> dC1 = drawnCards1.build();
        PlayerState pS = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), iC1, List.of(ChMap.routes().get(0)));

        List<SortedBag<Card>> expectedList11 = new ArrayList<>();
        expectedList11.add(SortedBag.of(1, Card.LOCOMOTIVE));
        assertEquals(expectedList11 , pS.possibleAdditionalCards(1, SortedBag.of(2, Card.GREEN), dC1));

        List<SortedBag<Card>> expectedList12 = new ArrayList<>();
        expectedList12.add(SortedBag.of(1, Card.LOCOMOTIVE, 1, Card.GREEN));
        expectedList12.add(SortedBag.of(2, Card.LOCOMOTIVE));
        assertEquals(expectedList12 , pS.possibleAdditionalCards(2, SortedBag.of(1, Card.GREEN, 1, Card.LOCOMOTIVE), dC1));

        List<SortedBag<Card>> expectedList13 = new ArrayList<>();
        expectedList13.add(SortedBag.of(3, Card.RED));
        expectedList13.add(SortedBag.of(1, Card.LOCOMOTIVE, 2, Card.RED));
        expectedList13.add(SortedBag.of(2, Card.LOCOMOTIVE, 1, Card.RED));
        expectedList13.add(SortedBag.of(3, Card.LOCOMOTIVE));
        assertEquals(expectedList13 , pS.possibleAdditionalCards(3, SortedBag.of(1, Card.RED, 1, Card.LOCOMOTIVE), dC1));

        /**
        // colored route
        List<SortedBag<Card>> expectedList21 = new ArrayList<>();
        expectedList21.add(SortedBag.of(1, Card.GREEN));
        assertEquals(expectedList21 , pS.possibleAdditionalCards(1, SortedBag.of(1, Card.GREEN), dC1));

        List<SortedBag<Card>> expectedList22 = new ArrayList<>();
        expectedList22.add(SortedBag.of());
        assertEquals(expectedList22 , pS.possibleAdditionalCards(2, SortedBag.of(2, Card.GREEN), dC1));

        List<SortedBag<Card>> expectedList23 = new ArrayList<>();
        expectedList23.add(SortedBag.of(3, Card.RED));
        assertEquals(expectedList23 , pS.possibleAdditionalCards(3, SortedBag.of(3, Card.RED), dC1));

        // neutral route
        */
        // neutral underground route
        List<SortedBag<Card>> expectedList41 = new ArrayList<>();
        expectedList41.add(SortedBag.of(1, Card.LOCOMOTIVE));
        assertEquals(expectedList41 , pS.possibleAdditionalCards(1, SortedBag.of(2, Card.LOCOMOTIVE), dC1));

        List<SortedBag<Card>> expectedList42 = new ArrayList<>();
        expectedList42.add(SortedBag.of(2, Card.LOCOMOTIVE));
        assertEquals(expectedList42 , pS.possibleAdditionalCards(2, SortedBag.of(1, Card.VIOLET, 1, Card.LOCOMOTIVE), dC1));

        List<SortedBag<Card>> expectedList43 = new ArrayList<>();
        expectedList43.add(SortedBag.of(3, Card.RED));
        expectedList43.add(SortedBag.of(1, Card.LOCOMOTIVE, 2, Card.RED));
        expectedList43.add(SortedBag.of(2, Card.LOCOMOTIVE, 1, Card.RED));
        assertEquals(expectedList43 , pS.possibleAdditionalCards(3, SortedBag.of(1, Card.RED, 2, Card.LOCOMOTIVE), dC1));

    }
    @Test
    void finalPointsSpecialCases(){
        // claimed none of the routes for the tickets
        PlayerState pS1 = new PlayerState(getPS().tickets(), SortedBag.of(1, Card.GREEN), List.of());
        assertEquals(-15, pS1.finalPoints());

        // claimed none of the routes for the tickets (with a international ticket)
        PlayerState pS2 = new PlayerState(SortedBag.of(ChMap.frToNeighbors), SortedBag.of(1, Card.GREEN), List.of());
        assertEquals(-5, pS2.finalPoints());

        // international ticket (country to country with 1 claimed)
        PlayerState pS3 = new PlayerState(SortedBag.of(ChMap.frToNeighbors), SortedBag.of(1, Card.GREEN),
                List.of(ChMap.routes().get(5),ChMap.routes().get(6), ChMap.routes().get(38)));
        assertEquals(10, pS3.finalPoints());

        // international ticket (country to country with 2 claimed)
        PlayerState pS4 = new PlayerState(SortedBag.of(ChMap.atToNeighbors), SortedBag.of(1, Card.GREEN),
                List.of(ChMap.routes().get(0),ChMap.routes().get(37), ChMap.routes().get(31), ChMap.routes().get(32)));
        assertEquals(23, pS4.finalPoints());

        // city to country
        PlayerState pS5 = new PlayerState(SortedBag.of(ChMap.tickets().get(35)), SortedBag.of(1, Card.GREEN),
                List.of(ChMap.routes().get(28), ChMap.routes().get(31), ChMap.routes().get(32)));
        assertEquals(10 + 5, pS5.finalPoints());

        // city to city added during the game
        PlayerState pS6 = new PlayerState(SortedBag.of(ChMap.tickets().get(1)), SortedBag.of(4, Card.VIOLET, 2, Card.WHITE),
                List.of(ChMap.routes().get(7), ChMap.routes().get(60)));
        PlayerState pS7 = pS6.withClaimedRoute(ChMap.routes().get(49), SortedBag.of(4, Card.VIOLET));
        PlayerState pS8 = pS7.withClaimedRoute(ChMap.routes().get(20), SortedBag.of(2, Card.WHITE));
        assertEquals(10 + 15, pS8.finalPoints());
    }


}
