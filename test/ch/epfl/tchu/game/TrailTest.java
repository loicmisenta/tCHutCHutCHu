package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrailTest {


    @Test
    void TestlongestWithTwoRoutes(){
        var listRouteTest = List.of(ChMap.routes().get(2), ChMap.routes().get(5));
        var expectedValue = 4;
        assertEquals(expectedValue, Trail.longest(listRouteTest).length());
    }

}
