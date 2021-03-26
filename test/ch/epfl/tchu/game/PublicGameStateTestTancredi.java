package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PublicGameStateTestTancredi {

    List<Card> cards = List.of(Card.GREEN, Card.GREEN, Card.LOCOMOTIVE, Card.BLUE, Card.BLUE);
    Map<PlayerId, PublicPlayerState> m = Map.of(PlayerId.PLAYER_1, new PublicPlayerState(4, 7, List.of(ChMap.routes().get(1))),
            PlayerId.PLAYER_2, new PublicPlayerState(5, 7, List.of(ChMap.routes().get(0))));

    PublicCardState pCS = new PublicCardState(cards, 1, 1);
    PublicGameState pGM = new PublicGameState(1, pCS,
            PlayerId.PLAYER_1, m, null);
    @Test
    void constructorFailsWithIllegalParam(){
        Random rdm = new Random();
        Deck deck = Deck.of(SortedBag.of(10, Card.GREEN), rdm);
        Map<PlayerId, PublicPlayerState> m = Map.of(PlayerId.PLAYER_1, new PublicPlayerState(4, 7, List.of()),
                PlayerId.PLAYER_2, new PublicPlayerState(4, 7, List.of()));

        assertThrows(IllegalArgumentException.class, () -> {
            new PublicGameState(-1, pCS,
                    PlayerId.PLAYER_1, m, null);
        });

        assertThrows(NullPointerException.class, () -> {
            new PublicGameState(0, pCS,
                    null, m, null);
        });

        assertThrows(NullPointerException.class, () -> {
            new PublicGameState(1, null,
                    PlayerId.PLAYER_1, m, null);
        });

        //Map<PlayerId, PublicPlayerState> wrongMap1 = Map.of(PlayerId.PLAYER_1, new PublicPlayerState(4, 7, List.of()),
              //  PlayerId.PLAYER_2, new PublicPlayerState(4, 7, List.of()), PlayerId.PLAYER_3, new PublicPlayerState(4, 7, List.of()));

        /*

        assertThrows(IllegalArgumentException.class, () -> {
            new PublicGameState(1, pCS,
                    PlayerId.PLAYER_1, wrongMap1, null);
        });
        */
        Map<PlayerId, PublicPlayerState> wrongMap2 = Map.of(PlayerId.PLAYER_1, new PublicPlayerState(4, 7, List.of()));
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicGameState(1, pCS,
                    PlayerId.PLAYER_1, wrongMap2, null);
        });

    }

    @Test
    void canDrawTickets(){
        PublicGameState pGM1 = new PublicGameState(0, pCS,
                PlayerId.PLAYER_1, m, null);
        assertTrue(!pGM1.canDrawTickets());

        PublicGameState pGM2 = new PublicGameState(1, pCS,
                PlayerId.PLAYER_1, m, null);
        assertTrue(pGM2.canDrawTickets());

    }

    @Test
    void cardState(){
        assertTrue(comparePublicCardState(pCS, pGM.cardState()));
    }

    @Test
    void canDrawCards(){
        PublicGameState pGM1 = new PublicGameState(0, new PublicCardState(cards, 5, 0),
                PlayerId.PLAYER_1, m, null);
        assertTrue(pGM1.canDrawCards());

        PublicGameState pGM2 = new PublicGameState(0, new PublicCardState(cards, 0, 5),
                PlayerId.PLAYER_1, m, null);
        assertTrue(pGM2.canDrawCards());

        PublicGameState pGM3 = new PublicGameState(0, new PublicCardState(cards, 2, 3),
                PlayerId.PLAYER_1, m, null);
        assertTrue(pGM3.canDrawCards());

        PublicGameState pGM4 = new PublicGameState(1, new PublicCardState(cards, 3, 3) ,
                PlayerId.PLAYER_1, m, null);
        assertTrue(pGM4.canDrawTickets());

        PublicGameState pGM5 = new PublicGameState(1, new PublicCardState(cards, 2, 2) ,
                PlayerId.PLAYER_1, m, null);
        assertTrue(pGM5.canDrawTickets());

    }

    @Test
    void currentPlayerId(){
        PublicGameState pGM1 = new PublicGameState(0, new PublicCardState(cards, 5, 0),
                PlayerId.PLAYER_1, m, null);
        assertEquals(PlayerId.PLAYER_1, pGM1.currentPlayerId());
    }

    @Test
    void currentPlayerState(){
        PublicPlayerState pPS = new PublicPlayerState(4, 7, List.of(ChMap.routes().get(1)));

        assertTrue(comparePublicPlayerState(pGM.currentPlayerState(), pPS));
    }

    public static boolean comparePublicPlayerState(PublicPlayerState pPS1, PublicPlayerState pPS2){
        if (pPS1.ticketCount() == pPS2.ticketCount()
            && pPS1.carCount() == pPS2.carCount()
            && pPS1.cardCount() == pPS2.cardCount()
            && pPS1.claimPoints() == pPS2.claimPoints()
            && pPS1.routes().equals(pPS2.routes())){
            return true;
        } else {
            return false;
        }
    }

    public static boolean comparePublicCardState(PublicCardState pCS1, PublicCardState pCS2){
        if (pCS1.faceUpCards().equals(pCS2.faceUpCards())
            && pCS1.deckSize() == pCS2.deckSize()
            && pCS1.discardsSize() == pCS2.discardsSize()){
            return true;
        } else {
            return false;
        }
    }

    public static boolean comparePublicGameState(PublicGameState pGS1, PublicGameState pGS2){
        if (comparePublicCardState(pGS1.cardState(), pGS2.cardState())
        && comparePublicPlayerState(pGS1.currentPlayerState(), pGS2.currentPlayerState())
        && pGS1.ticketsCount() == pGS2.ticketsCount()
        && pGS1.currentPlayerId().equals(pGS2.currentPlayerId())
        && pGS1.lastPlayer().equals(pGS2.lastPlayer())
        && pGS1.playerState.get(PlayerId.PLAYER_1).equals(pGS2.playerState.get(PlayerId.PLAYER_1))
        && pGS1.playerState.get(PlayerId.PLAYER_2).equals(pGS2.playerState.get(PlayerId.PLAYER_2))) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    void playerState(){
        PublicPlayerState pPS1 = new PublicPlayerState(4, 7, List.of(ChMap.routes().get(1)));
        PublicPlayerState pPS2 =  new PublicPlayerState(5, 7, List.of(ChMap.routes().get(0)));

        assertTrue(comparePublicPlayerState(pPS1, pGM.playerState(PlayerId.PLAYER_1)));
        assertTrue(comparePublicPlayerState(pPS2, pGM.playerState(PlayerId.PLAYER_2)));
    }

    @Test
    void claimedRoutes(){
        assertEquals(List.of(ChMap.routes().get(1), ChMap.routes().get(0)).get(1).id(), pGM.claimedRoutes().get(0).id());
        assertEquals(List.of(ChMap.routes().get(1), ChMap.routes().get(0)).get(0).id(), pGM.claimedRoutes().get(1).id());

    }

    @Test
    void lastPlayer(){
        Map<PlayerId, PublicPlayerState> m1 = Map.of(PlayerId.PLAYER_1, new PublicPlayerState(4, 7, List.of(ChMap.routes().get(1))),
                PlayerId.PLAYER_2, new PublicPlayerState(5, 7, List.of(
                        ChMap.routes().get(0), ChMap.routes().get(1), ChMap.routes().get(2), ChMap.routes().get(3),
                        ChMap.routes().get(4), ChMap.routes().get(5), ChMap.routes().get(6), ChMap.routes().get(7),
                        ChMap.routes().get(8), ChMap.routes().get(9), ChMap.routes().get(10), ChMap.routes().get(11),
                        ChMap.routes().get(12), ChMap.routes().get(13), ChMap.routes().get(14), ChMap.routes().get(15),
                        ChMap.routes().get(16), ChMap.routes().get(18))));


        PublicCardState pCS = new PublicCardState(cards, 1, 1);
        PublicGameState pGM2 = new PublicGameState(1, pCS,
                PlayerId.PLAYER_1, m1, null);
        assertEquals(null, pGM.lastPlayer());
        // assertEquals(PlayerId.PLAYER_1, pGM2.lastPlayer());
    }


}
