package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {
    @Test
    void retourneStationOpposeeCorrect(){
        //var expectedValues = new Route("id", );
    }
    
    
    @Test 
    void possibleClaimDeuxWagonsVioletsBon(){
        var routeTest = ChMap.routes().get(3);
        var expectedValues = List.of(SortedBag.of(2, Card.VIOLET));
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
