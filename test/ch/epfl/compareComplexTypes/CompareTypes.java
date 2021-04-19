package ch.epfl.compareComplexTypes;

import ch.epfl.tchu.game.*;


public class CompareTypes {
    /*
    public static boolean compareCardState(CardState cS1, CardState cS2){
        if (cS1.deck.equals(cS2.deck) && cS1.discards.equals(cS2.discards) && PublicGameStateTest.comparePublicCardState(cS1, cS2)){
            return true;
        } else {
            return false;
        }
    }*/

    public static boolean comparePlayerState(PlayerState pS1, PlayerState pS2){
        if (pS1.tickets().equals(pS2.tickets()) && pS1.cards().equals(pS2.cards()) && comparePublicPlayerState(pS1, pS2)){
            return true;
        } else {
            return false;
        }
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
                && pGS1.lastPlayer() == pGS2.lastPlayer()) {
            //&& pGS1.playerState.get(PlayerId.PLAYER_1).equals(pGS2.playerState.get(PlayerId.PLAYER_1))
            //&& pGS1.playerState.get(PlayerId.PLAYER_2).equals(pGS2.playerState.get(PlayerId.PLAYER_2))) {
            return true;
        } else {
            return false;
        }
    }

}