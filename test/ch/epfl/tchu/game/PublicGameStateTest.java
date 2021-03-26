package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author loicmisenta,
 * @author lagutovaalexandra
 */
public class PublicGameStateTest {
    //PublicGameState gameState = new PublicGameState();
    /**
    PublicCardState statePubliqueCartes = new PublicCardState(List.of(Card.GREEN, Card.LOCOMOTIVE, Card.BLUE, Card.BLUE, Card.VIOLET), 23, 2);
    PublicCardState statePubliqueCartesFALSE = new PublicCardState(List.of(Card.GREEN, Card.LOCOMOTIVE, Card.BLUE, Card.BLUE, Card.VIOLET), 1, 2);

    Map<PlayerId, PublicPlayerState> map = Map.of(PlayerId.PLAYER_1 ,
            new PublicPlayerState(5, 10, List.of(
                            new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW))),
            PlayerId.PLAYER_2 , new PublicPlayerState(2, 5,List.of(ChMap.routes().get(3), ChMap.routes().get(4), ChMap.routes().get(5))));


    PublicGameState stateDuJeu = new PublicGameState(20, statePubliqueCartes, PlayerId.PLAYER_1, map, PlayerId.PLAYER_2);



    @Test
    void constructionCorrecteDePublicGameState(){
        var expectedValue = true ;
        assertEquals(expectedValue, stateDuJeu.canDrawCards());
    }

    @Test
    void erreursDansLeConstructeurLances(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicGameState(-1, statePubliqueCartes, PlayerId.PLAYER_1, map, PlayerId.PLAYER_2 );
        });
    }

    @Test
    void ticketsCount(){
        var expectedValue = 20 ;
        assertEquals(expectedValue, stateDuJeu.ticketsCount());
    }

    @Test
    void cardStateFaceUpCard(){
        var expectedValue = List.of(Card.GREEN, Card.LOCOMOTIVE, Card.BLUE, Card.BLUE, Card.VIOLET) ;
        assertEquals(expectedValue, stateDuJeu.cardState().faceUpCards());
    }

    @Test
    void CurrentPlayerID(){
        var expectedValue = PlayerId.PLAYER_1 ;
        assertEquals(expectedValue, stateDuJeu.currentPlayerId());
    }

    @Test
    void playerState(){
        var expectedValue = map.get(PlayerId.PLAYER_2) ;
        assertEquals(expectedValue, stateDuJeu.playerState(PlayerId.PLAYER_2));
    }

    @Test
    void currentPlayerState(){
        var expectedValue = map.get(PlayerId.PLAYER_1) ;
        assertEquals(expectedValue, stateDuJeu.currentPlayerState());
    }

    //VERIFIER PEUT ETRE QUE CEST BIEN LES BONNES MAIS CA DOIT ETRE BON
    @Test
    void claimedRoutesSize(){
        var expectedValue = 4;
        assertEquals(expectedValue, stateDuJeu.claimedRoutes().size());
    }

    @Test
    void lastPlayer(){
        var expectedValue = PlayerId.PLAYER_2;
        assertEquals(expectedValue, stateDuJeu.lastPlayer());
    }

    @Test
    void canNotDrawCards(){
        var expectedValue = false;
        assertEquals(expectedValue, (new PublicGameState(3, statePubliqueCartesFALSE, PlayerId.PLAYER_1, map, PlayerId.PLAYER_2)).canDrawCards());
    }*/

}
