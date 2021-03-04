package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

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
    void possibleClaimDeuxWagonsVioletsBon(){
        var routeTest = ChMap.routes().get(3);
        var expectedValues = List.of(SortedBag.of(2, Card.VIOLET));
        assertEquals(expectedValues, routeTest.possibleClaimCards());

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
}}
