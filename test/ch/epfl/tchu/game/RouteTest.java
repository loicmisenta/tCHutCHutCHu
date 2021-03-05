package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ch.epfl.tchu.game.Constants.ROUTE_CLAIM_POINTS;
//import static ch.epfl.test.TestRandomizer.getRandomInt;
import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {
    @Test
    void constructorFailsWithNullStations() {
        assertThrows(NullPointerException.class, () -> {
            new Route(ChMap.routes().get(0).id(),null, new Station(1, "Lausanne"), 1, Route.Level.OVERGROUND, Color.YELLOW);
        });
        assertThrows(NullPointerException.class, () -> {
            new Route(ChMap.routes().get(5).id(), new Station(1, "Lausanne"),null, 1, Route.Level.OVERGROUND, Color.YELLOW);
        });
    }

    @Test
    void constructorFailsWithInvalidLength() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        assertThrows(IllegalArgumentException.class, () -> {
            new Route(ChMap.routes().get(4).id(), s1, s2, -1, Route.Level.UNDERGROUND, Color.ORANGE);
        });
    }

    @Test
    void constructorFailsWithNullLevel(){
        assertThrows(NullPointerException.class, () -> {
            new Route(ChMap.routes().get(0).id(),null, new Station(1, "Lausanne"), 2, null, Color.RED);
        });
    }

    @Test
    void constructorFailsWithSameStations(){
        var s1 = new Station(0, "Lausanne");
        assertThrows(IllegalArgumentException.class, () -> {
            new Route(null, s1, s1, 3, Route.Level.UNDERGROUND, Color.ORANGE);
        });
    }

    @Test
    void constructorFailsWithInvalidId(){
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        assertThrows(NullPointerException.class, () -> {
            new Route(null, s1, s2, 3, Route.Level.UNDERGROUND, Color.ORANGE);
        });
    }


    /**
    @Test
    void constructorFailsWithSameId(){
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        Route r1 = new Route("Geneve", s2, s1, 5, Route.Level.OVERGROUND, Color.BLUE);

        assertThrows(IllegalArgumentException.class, () -> {
            new Route("Geneve", s1, s2, 3, Route.Level.UNDERGROUND, Color.ORANGE);
        });
    }
     */

    @Test
    void stationsReturnedInGoodOrder(){
        Route r1 = new Route("Geneve", new Station(0, "Lausanne"), new Station(1, "EPFL"), 5, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(r1.station1(), r1.stations().get(0));
        assertEquals(r1.station2(), r1.stations().get(1));

    }

    @Test
    void stationOppositeReturnsGoodStation() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        Route r1 = new Route("Geneve", s1, s2, 5, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(r1.station1(), r1.stationOpposite(s2));
        assertEquals(r1.station2(), r1.stationOpposite(s1));
    }

    @Test
    void stationOppositeFailsWithIllegalArgument(){
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        Route r1 = new Route("Geneve", s1, s2, 3, Route.Level.UNDERGROUND, Color.ORANGE);
        assertThrows(IllegalArgumentException.class, () -> {
            r1.stationOpposite(new Station(1, "Geneve"));
        });
    }
    /**
    @Test
    void claimPointsReturnsCorrectPoints(){
        int rdm = getRandomInt(7);
        Route r1 = new Route("Geneve", new Station(0, "Lausanne"), new Station(1, "EPFL"), rdm, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(ROUTE_CLAIM_POINTS.get(rdm),r1.claimPoints());
    }
    **/

    @Test
    void possibleClaimCardsFixedColorUnderGroundRouteTest() {
        List<SortedBag<Card>> cards = new ArrayList<>();
        SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();

        // Creation of list of sorted bag

        // 3xYellow
        cards.add(cardBuilder.add(3, Card.YELLOW).build());
        cardBuilder = new SortedBag.Builder<>();

        // 2xYellow, 1xLoc
        cards.add(cardBuilder.add(2, Card.YELLOW).add(Card.LOCOMOTIVE).build());
        cardBuilder = new SortedBag.Builder<>();

        // 1xYellow, 2xLoc
        cards.add(cardBuilder.add(1, Card.YELLOW).add(2, Card.LOCOMOTIVE).build());
        cardBuilder = new SortedBag.Builder<>();

        // 3xLoc
        cards.add(cardBuilder.add(3, Card.LOCOMOTIVE).build());
        cardBuilder = new SortedBag.Builder<>();

        // Test
        Route route = ChMap.routes().get(58);
        assertEquals(cards, route.possibleClaimCards());

    }

    @Test
    void possibleClaimCardsNullOverGroundRouteTest(){
        List<SortedBag<Card>> cards = new ArrayList<>();
        SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();

        // Creation of list of sorted bag

        // 2xBlack
        cards.add(cardBuilder.add(2, Card.BLACK).build());
        cardBuilder = new SortedBag.Builder<>();

        // 2xViolet
        cards.add(cardBuilder.add(2, Card.VIOLET).build());
        cardBuilder = new SortedBag.Builder<>();

        // 2xBlue
        cards.add(cardBuilder.add(2, Card.BLUE).build());
        cardBuilder = new SortedBag.Builder<>();

        // 2xGreen
        cards.add(cardBuilder.add(2, Card.GREEN).build());
        cardBuilder = new SortedBag.Builder<>();

        // 2xYellow
        cards.add(cardBuilder.add(2, Card.YELLOW).build());
        cardBuilder = new SortedBag.Builder<>();

        //2xOrange
        cards.add(cardBuilder.add(2, Card.ORANGE).build());
        cardBuilder = new SortedBag.Builder<>();

        //2xRed
        cards.add(cardBuilder.add(2, Card.RED).build());
        cardBuilder = new SortedBag.Builder<>();

        //2xWhite
        cards.add(cardBuilder.add(2, Card.WHITE).build());
        cardBuilder = new SortedBag.Builder<>();

        // Test
        Route route = ChMap.routes().get(37);
        assertEquals(cards, route.possibleClaimCards());

    }

    @Test
    void possibleClaimCardsNullUnderGroundRouteTest(){
        List<SortedBag<Card>> cards = new ArrayList<>();
        SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();

        // Creation of list of sorted bag

        // 2x all Color
        for(Color c : Color.ALL) {
            cards.add(cardBuilder.add(2, Card.of(c)).build());
            cardBuilder = new SortedBag.Builder<>();
        }

        // 1x all Color, 1x Loc
        for(Color c : Color.ALL) {
            cards.add(cardBuilder.add(Card.of(c)).add(Card.LOCOMOTIVE).build());
            cardBuilder = new SortedBag.Builder<>();
        }

        cards.add(cardBuilder.add(2, Card.LOCOMOTIVE).build());
        cardBuilder = new SortedBag.Builder<>();

        // Test
        Route route = ChMap.routes().get(41);
        assertEquals(cards, route.possibleClaimCards());
    }

    @Test
    void possibleClaimCardsLongNullUnderGroundRouteTest(){
        List<SortedBag<Card>> cards = new ArrayList<>();
        SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();

        // Creation of list of sorted bag

        // 3x all Color
        for(Color c : Color.ALL) {
            cards.add(cardBuilder.add(3, Card.of(c)).build());
            cardBuilder = new SortedBag.Builder<>();
        }

        // 2x all Color, 1x Loc
        for(Color c : Color.ALL) {
            cards.add(cardBuilder.add(2, Card.of(c)).add(Card.LOCOMOTIVE).build());
            cardBuilder = new SortedBag.Builder<>();
        }

        // 1x all Color, 2x Loc
        for(Color c : Color.ALL) {
            cards.add(cardBuilder.add(Card.of(c)).add(2, Card.LOCOMOTIVE).build());
            cardBuilder = new SortedBag.Builder<>();
        }

        cards.add(cardBuilder.add(3, Card.LOCOMOTIVE).build());
        cardBuilder = new SortedBag.Builder<>();

        // Test
        Route route = ChMap.routes().get(32);
        assertEquals(cards, route.possibleClaimCards());
    }

    @Test
    void additionalClaimCardsCountNoLocomotiveClaimedOrDrawnDifferentColors(){
        SortedBag.Builder<Card> claimedCardsBuilder = new SortedBag.Builder<>();
        claimedCardsBuilder.add(2, Card.BLACK);

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.RED).add(Card.GREEN).add(Card.BLACK);

        assertEquals(1, ChMap.routes().get(41).additionalClaimCardsCount(claimedCardsBuilder.build(), drawnCardsBuilder.build()));
    }

   @Test
   void additionalClaimCardsCountOverGroundRoute(){
       SortedBag.Builder<Card> claimedCardsBuilder = new SortedBag.Builder<>();
       claimedCardsBuilder.add(2, Card.BLACK);

       SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
       drawnCardsBuilder.add(Card.RED).add(Card.GREEN).add(Card.BLACK);
       assertThrows(IllegalArgumentException.class, () -> {
            ChMap.routes().get(3).additionalClaimCardsCount(claimedCardsBuilder.build(), drawnCardsBuilder.build());
       });
   }

   @Test
   void additionalClaimCardsCountIllegalNumberOfDrawnCards(){
       SortedBag.Builder<Card> claimedCardsBuilder = new SortedBag.Builder<>();
       claimedCardsBuilder.add(2, Card.BLACK);

       SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
       drawnCardsBuilder.add(Card.RED).add(Card.GREEN);
       assertThrows(IllegalArgumentException.class, () -> {
           ChMap.routes().get(41).additionalClaimCardsCount(claimedCardsBuilder.build(), drawnCardsBuilder.build());
       });
   }

   @Test
   void additionalClaimCardsCountThreeColorCardsToReturn(){
       SortedBag.Builder<Card> claimedCardsBuilder = new SortedBag.Builder<>();
       claimedCardsBuilder.add(Card.BLACK).add(Card.LOCOMOTIVE);

       SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
       drawnCardsBuilder.add(Card.BLACK).add(Card.BLACK).add(Card.BLACK);

       assertEquals(3, ChMap.routes().get(41).additionalClaimCardsCount(claimedCardsBuilder.build(), drawnCardsBuilder.build()));
   }

   @Test
    void additionalClaimCardsCountThreeLocomotivesToReturn(){
       SortedBag.Builder<Card> claimedCardsBuilder = new SortedBag.Builder<>();
       claimedCardsBuilder.add(Card.BLACK).add(Card.LOCOMOTIVE);

       SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
       drawnCardsBuilder.add(Card.LOCOMOTIVE).add(Card.LOCOMOTIVE).add(Card.LOCOMOTIVE);

       assertEquals(3, ChMap.routes().get(41).additionalClaimCardsCount(claimedCardsBuilder.build(), drawnCardsBuilder.build()));
   }

   @Test
   void additionalClaimCardsCountTwoLocomotivesAnd1ColorCardToReturn(){
       SortedBag.Builder<Card> claimedCardsBuilder = new SortedBag.Builder<>();
       claimedCardsBuilder.add(2, Card.BLACK).add(Card.LOCOMOTIVE);

       SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
       drawnCardsBuilder.add(Card.LOCOMOTIVE).add(Card.LOCOMOTIVE).add(Card.BLACK);

       assertEquals(3, ChMap.routes().get(31).additionalClaimCardsCount(claimedCardsBuilder.build(), drawnCardsBuilder.build()));
   }

   @Test
   void additionalClaimCardsCount0ToReturn(){
       SortedBag.Builder<Card> claimedCardsBuilder = new SortedBag.Builder<>();
       claimedCardsBuilder.add(2, Card.BLACK).add(Card.LOCOMOTIVE);

       SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
       drawnCardsBuilder.add(Card.GREEN).add(Card.YELLOW).add(Card.GREEN);

       assertEquals(0, ChMap.routes().get(31).additionalClaimCardsCount(claimedCardsBuilder.build(), drawnCardsBuilder.build()));
   }
}
