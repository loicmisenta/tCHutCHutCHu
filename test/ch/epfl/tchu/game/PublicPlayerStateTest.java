package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicPlayerStateTest {

    PublicPlayerState playerState = new PublicPlayerState(2, 2, List.of(ChMap.routes().get(0), ChMap.routes().get(1)));
    @Test
    void IllegalArgumentExceptionConstructorNegativeTicket(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(-1, 1, List.of(ChMap.routes().get(0)));
        });
    }
    @Test
    void IllegalArgumentExceptionConstructorNegativeCard(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(1, -1, List.of(ChMap.routes().get(0)));
        });
    }

    @Test
    void ticketCount() {
        var ExpectedValue = 2;
        assertEquals(ExpectedValue, playerState.ticketCount());
    }

    @Test
    void cardCount() {
        var ExpectedValue = 2;
        assertEquals(ExpectedValue, playerState.cardCount());
    }

    @Test
    void routes() {
        var ExpectedValue = List.of(ChMap.routes().get(0), ChMap.routes().get(1));
        assertEquals(ExpectedValue, playerState.routes());
    }

    @Test
    void carCount() {
        var ExpectedValue = 40-5;
        assertEquals(ExpectedValue, playerState.carCount());
    }

    @Test
    void claimPoints() {
        var ExpectedValue = 7+1;
        assertEquals(ExpectedValue, playerState.claimPoints());
    }
}