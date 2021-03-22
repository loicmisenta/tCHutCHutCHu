package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class PublicGameState {
    PublicCardState statePubliqueCartes = new PublicCardState(List.of(Card.GREEN, Card.LOCOMOTIVE), 20, 20);
    //Map map = new Map();
    //for (PlayerId p: PlayerId.ALL) {
    //   map.put(p, PlayerState);
    //}
    @Test
    void constructionCorrecteDePublicGameState(){

    }

    @Test
    void erreursDansLeConstructeurLances(){
        assertThrows(IllegalArgumentException.class, () -> {
           // new PublicGameState(-1, statePubliqueCartes, PlayerId.PLAYER_1,  ));
        });
    }
}
