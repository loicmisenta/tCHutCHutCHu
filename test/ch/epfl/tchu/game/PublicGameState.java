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
public class PublicGameState {
    PublicCardState statePubliqueCartes = new PublicCardState(List.of(Card.GREEN, Card.LOCOMOTIVE), 20, 20);
    Map<PlayerId, PlayerState> map = Map.of(
            PlayerId.PLAYER_1 , new PlayerState(SortedBag.of(
                    1, new Ticket(new Station(1, "Bâle"), new Station(3, "Berne"), 5),
                    1, new Ticket(new Station(1, "Bâle"), new Station(4, "Brigue"), 10)),
                    SortedBag.of(3, Card.WHITE, 4, Card.LOCOMOTIVE),
                    List.of(new Route("BAL_DEL_1", new Station(1, "Bâle"), new Station(8, "Delémont"), 2, Route.Level.UNDERGROUND, Color.YELLOW))
                    ));
    PublicGameState stateDuJeu = new PublicGameState();


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
