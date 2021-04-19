package ch.epfl.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static ch.epfl.compareComplexTypes.CompareTypes.*;
import static ch.epfl.tchu.game.Card.*;
import static ch.epfl.tchu.game.PlayerId.*;
import static ch.epfl.tchu.net.Serdes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class SerdeTest {

    @Test
    void listOfCartes(){
        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        var expectedValue = "6,7,2,0,6";
        assertEquals(expectedValue, listCardSerde.serialize(fu));
        assertEquals(fu, listCardSerde.deserialize(expectedValue));
        assertEquals(fu, listCardSerde.deserialize(listCardSerde.serialize(fu)));
    }



    @Test
    void testPublicCardState(){
        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        var expectedValue = "6,7,2,0,6;30;31";
        assertEquals(expectedValue, publicCardStateSerde.serialize(cs));
        assertTrue(comparePublicCardState(cs, publicCardStateSerde.deserialize(publicCardStateSerde.serialize(cs))));
        assertTrue(comparePublicCardState(cs, publicCardStateSerde.deserialize(expectedValue)));
    }

    @Test
    void testGameStateProf(){
        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        Map<PlayerId, PublicPlayerState> ps = Map.of(
                PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PLAYER_2, new PublicPlayerState(20, 21, List.of()));
        PublicGameState gs =
                new PublicGameState(40, cs, PLAYER_2, ps, null);
        var expectedValue = "40:6,7,2,0,6;30;31:1:10;11;0,1:20;21;:";
        assertEquals(expectedValue, publicGameStateSerde.serialize(gs));
        assertTrue(comparePublicGameState(gs, publicGameStateSerde.deserialize(publicGameStateSerde.serialize(gs))));
    }


    @Test
    void testPlayerId(){
        PlayerId playerId = PLAYER_1;
        var expectedValue = "0";
        assertEquals(expectedValue, playerIdSerde.serialize(playerId));
        assertEquals(playerId, playerIdSerde.deserialize(playerIdSerde.serialize(playerId)));

    }

    @Test
    void testPlayerState(){
        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets().subList(0, 2));
        SortedBag<Card> cards = SortedBag.of(2, RED, 2, WHITE);
        PlayerState playerState = new PlayerState(tickets, cards, rs1);
        var expectedValue = "0,1;6,6,7,7;0,1";
        assertEquals(expectedValue, playerStateSerde.serialize(playerState));
        assertTrue(comparePlayerState(playerStateSerde.deserialize(expectedValue), playerState));
    }


    @Test
    void serailizeDeserializeTest(){
       String x = " s ";
       assertEquals(x, stringSerde.deserialize(stringSerde.serialize(x)));
    }


    @Test
    void serializeInt(){
        var expectedValue = "20";
        assertEquals(expectedValue, intSerde.serialize(20));
        assertEquals(20, intSerde.deserialize("20"));
    }

    @Test
    void serializeString(){
        String s = "Charles";
        var expectedValue = "Q2hhcmxlcw==";
        assertEquals(expectedValue, stringSerde.serialize(s));
        assertEquals(s, stringSerde.deserialize(expectedValue));
        assertEquals(s, stringSerde.deserialize(stringSerde.serialize(s)));

    }

    @Test
    void serializeStringList(){
        List<String> s = List.of("Charles", "Charles", "Charles") ;
        var expectedValue = "Q2hhcmxlcw==,Q2hhcmxlcw==,Q2hhcmxlcw==";
        assertEquals(expectedValue, listStringSerde.serialize(s));
        assertEquals(s, listStringSerde.deserialize(expectedValue));
    }

    @Test
    void serializeSortedBagCard(){
        SortedBag<Card> c = SortedBag.of(2, RED, 3, BLUE);
        var expectedValue = "2,2,2,6,6";
        assertEquals(expectedValue, sortedBagOfCardSerde.serialize(c));
        assertEquals(c, sortedBagOfCardSerde.deserialize(expectedValue));
    }

    @Test
    void serializePublicPlayerState(){
        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        PublicPlayerState ps = new PublicPlayerState(10, 11, rs1);
        var expectedValue = "10;11;0,1";
        assertEquals(expectedValue, publicPlayerStateSerde.serialize(ps));
        assertTrue(comparePublicPlayerState(ps, publicPlayerStateSerde.deserialize(expectedValue)));
        assertTrue(comparePublicPlayerState(ps, publicPlayerStateSerde.deserialize(publicPlayerStateSerde.serialize(ps))));
    }

}
