package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.tchu.game.Trail.longest;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrailTest {


    @Test
    void testlongestWithTwoRoutes(){
        var listRouteTest = List.of(ChMap.routes().get(2), ChMap.routes().get(5));
        var expectedValue = "Trail {( length = 4), station1 = Baden , station2 = Allemagne }";
        assertEquals(expectedValue, longest(listRouteTest).toString());
    }


    @Test
    void cheminLucerneFribourgAvecRoutesInutiles(){
        var listRouteTest = List.of(ChMap.routes().get(16), ChMap.routes().get(18),
                ChMap.routes().get(65), ChMap.routes().get(19) , ChMap.routes().get(13),
                ChMap.routes().get(41), ChMap.routes().get(42), ChMap.routes().get(46));
        var expectedValue = "Trail {( length = 13 ), station1 = Lucerne , station2 = Fribourg }";
        assertEquals(expectedValue, longest(listRouteTest));
    }

    @Test
    void deuxRoutesMemeLongueur(){
        var listRouteTest =  List.of(ChMap.routes().get(2), ChMap.routes().get(5), ChMap.routes().get(0),
                ChMap.routes().get(81), ChMap.routes().get(87));
        var expectedValue = "";
        assertEquals(expectedValue, longest(listRouteTest));
    }


    @Test
    void longestListeVide(){
        var listeRouteVide = new ArrayList<Route>();
        var expectedValue = 0;
        assertEquals(expectedValue, longest(listeRouteVide).length());
        assertEquals(null , longest(listeRouteVide).station1());
        assertEquals(null , longest(listeRouteVide).station2());
    }
}
