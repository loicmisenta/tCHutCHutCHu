package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {
    @Test
    void retourneStationOpposeeCorrect(){
        var routeTest = ChMap.routes().get(0);
        var expectedValues = ChMap.routes().get(0).station1();
        assertEquals(expectedValues, routeTest.stationOpposite(routeTest.station2()));
    }
    @Test
    void retourneStationException(){
        var routeTest = ChMap.routes().get(0);
        var expectedValues = ChMap.routes().get(0).station1();

        assertEquals(expectedValues, routeTest.stationOpposite(routeTest.station2()));
    }


    
    @Test 
    void possibleClaimDeuxWagonsVioletsBon(){
        var routeTest = ChMap.routes().get(0);
        assertThrows(IllegalArgumentException.class, () -> {
            routeTest.stationOpposite(ChMap.routes().get(5).station1());
        });
    }

    @Test
    void tunnelGrisLongTrois(){
        var routeTest = ChMap.routes().get(31);
        var expectedValues = List.of(SortedBag.of(3, Card.BLACK), SortedBag.of(3, Card.VIOLET),
                SortedBag.of(3, Card.BLUE), SortedBag.of(3, Card.GREEN), SortedBag.of(3, Card.YELLOW), SortedBag.of(3, Card.ORANGE),
                SortedBag.of(3, Card.RED), SortedBag.of(3, Card.WHITE),
                SortedBag.of(2, Card.BLACK, 1, Card.LOCOMOTIVE), SortedBag.of(2, Card.VIOLET, 1, Card.LOCOMOTIVE),
                SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE), SortedBag.of(2, Card.GREEN , 1, Card.LOCOMOTIVE), SortedBag.of(2, Card.YELLOW, 1, Card.LOCOMOTIVE),
                SortedBag.of(2, Card.ORANGE, 1, Card.LOCOMOTIVE), SortedBag.of(2, Card.RED, 1, Card.LOCOMOTIVE), SortedBag.of(2, Card.WHITE , 1, Card.LOCOMOTIVE),
                SortedBag.of(1, Card.BLACK, 2, Card.LOCOMOTIVE), SortedBag.of(1, Card.VIOLET, 2, Card.LOCOMOTIVE),
                SortedBag.of(1, Card.BLUE, 2, Card.LOCOMOTIVE), SortedBag.of(1, Card.GREEN , 2, Card.LOCOMOTIVE), SortedBag.of(1, Card.YELLOW, 2, Card.LOCOMOTIVE),
                SortedBag.of(1, Card.ORANGE, 2, Card.LOCOMOTIVE), SortedBag.of(1, Card.RED, 2, Card.LOCOMOTIVE), SortedBag.of(1, Card.WHITE , 2, Card.LOCOMOTIVE),
                SortedBag.of(3, Card.LOCOMOTIVE)
        );
        assertEquals(expectedValues, routeTest.possibleClaimCards());

    }

    @Test
    void possibleClaimOrdreAlphabetique(){
        var routeTest = ChMap.routes().get(16);
        var expectedValues = List.of(SortedBag.of(4, Card.BLACK), SortedBag.of(4, Card.VIOLET),
                SortedBag.of(4, Card.BLUE),SortedBag.of(4, Card.GREEN),SortedBag.of(4, Card.YELLOW),SortedBag.of(4, Card.ORANGE),
                SortedBag.of(4, Card.RED), SortedBag.of(4, Card.WHITE));
        assertEquals(expectedValues, routeTest.possibleClaimCards());
    }


    @Test
    void TunnelJaunePrisWagons(){
        var routeTest = ChMap.routes().get(6);
        var expectedValues = List.of(SortedBag.of(2, Card.YELLOW), SortedBag.of(1, Card.YELLOW, 1, Card.LOCOMOTIVE),
                SortedBag.of(2, Card.LOCOMOTIVE));
        assertEquals(expectedValues, routeTest.possibleClaimCards());

    }

    @Test
    void carteNestPasUnTunnel(){
        var routeTest = ChMap.routes().get(4);
        assertThrows(IllegalArgumentException.class, () -> {
            routeTest.additionalClaimCardsCount(SortedBag.of(2, Card.YELLOW), SortedBag.of(3, Card.WHITE));
        });
    }

    @Test
    void stationIsNull(){
        assertThrows(NullPointerException.class, () -> {
            new Route("AT1_STG_1", null, new Station(0, "Baden"), 4, Route.Level.UNDERGROUND, null);
        });
    }
    @Test
    void idIsNull(){
        assertThrows(NullPointerException.class, () -> {
            new Route(null, new Station(4, "Brigue"), new Station(0, "Baden"), 4, Route.Level.UNDERGROUND, null);
        });
    }
    @Test
    void claimPointsCorrect(){
        var routeTest = ChMap.routes().get(0);
        var expectedValue = 7;
        assertEquals(expectedValue, routeTest.claimPoints());
    }
    @Test
    void additionnalClaimCardGeneralTest(){
        var routeTest = ChMap.routes().get(0);
        var claimCards = SortedBag.of(4, Card.BLUE);
        var drawnCards = SortedBag.of(1, Card.BLUE, 2, Card.WHITE);
        var expectedValue = 1;
        assertEquals(expectedValue, routeTest.additionalClaimCardsCount(claimCards, drawnCards));
    }
    @Test
    void additionnalClaimCardExceptionNotTunnel(){
        var routeTest = ChMap.routes().get(3);
        var claimCards = SortedBag.of(4, Card.BLUE);
        var drawnCards = SortedBag.of(1, Card.BLUE, 2, Card.WHITE);
        var expectedValue = 1;
        assertThrows(IllegalArgumentException.class, () -> {
            routeTest.additionalClaimCardsCount(claimCards, drawnCards);
        });
    }
    @Test
    void additionnalClaimCardExceptionDrawnCardsNot3(){
        var routeTest = ChMap.routes().get(0);
        var claimCards = SortedBag.of(4, Card.BLUE);
        var drawnCards = SortedBag.of(3, Card.BLUE, 2, Card.WHITE);
        var expectedValue = 1;
        assertThrows(IllegalArgumentException.class, () -> {
            routeTest.additionalClaimCardsCount(claimCards, drawnCards);
        });
    }
    @Test
    void constructeur2EqualStations(){
        var Station = new Station(0, "Gare1");
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("GARE", Station, Station, 2, Route.Level.UNDERGROUND, Color.BLACK);
        });
    }
    @Test
    void constructeurTooLong(){
        var Station = new Station(0, "Gare1");
        var Station2 = new Station(1, "Gare2");
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("GARE", Station, Station2, 200, Route.Level.UNDERGROUND, Color.BLACK);
        });
    }
    @Test
    void constructeurLengthTooShort(){
        var Station = new Station(0, "Gare1");
        var Station2 = new Station(1, "Gare2");
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("GARE", Station, Station2, 0, Route.Level.UNDERGROUND, Color.BLACK);
        });
    }
}
