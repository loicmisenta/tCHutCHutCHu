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
    PublicCardState statePubliqueCartes = new PublicCardState(List.of(Card.GREEN, Card.LOCOMOTIVE), 20, 20);
    Map<PlayerId, PublicPlayerState> map = Map.of(
            PlayerId.PLAYER_1 , new PublicPlayerState(45, 40,
                    List.of(new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW))));
    PublicGameState stateDuJeu = new PublicGameState(45, statePubliqueCartes, PlayerId.PLAYER_1, map, PlayerId.PLAYER_2);



    @Test
    void constructionCorrecteDePublicGameState(){
        var expectedValue = 40 ;
        assertEquals(expectedValue, stateDuJeu.canDrawCards());
    }

    @Test
    void erreursDansLeConstructeurLances(){
        assertThrows(IllegalArgumentException.class, () -> {
           // new PublicGameState(-1, statePubliqueCartes, PlayerId.PLAYER_1,  ));
        });
    }
}
