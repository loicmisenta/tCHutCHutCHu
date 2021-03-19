package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Map;
import java.util.Objects;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class PublicGameState {
    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;

    /**
     * Constructeur public de la partie publique de l'état de partie
     * @param ticketsCount la taille de la pioche de billets
     * @param cardState l'état public des wagons/locomotoves
     * @param currentPlayerId le joueur courant
     * @param playerState l'état public des joueurs
     * @param lastPlayer  l'identité du dernier joueur
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {
        Preconditions.checkArgument((ticketsCount >= 0 ) && (playerState.size() == 2));
        if((playerState == null) || (cardState == null)|| (currentPlayerId == null)){
        throw new NullPointerException();}
        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = Objects.requireNonNull(playerState);
        this.lastPlayer = lastPlayer;
    }

    public int ticketsCount(){
        return ticketsCount;
    }

    public boolean canDrawTickets(){
        return ticketsCount != 0;
    }

    public PublicCardState cardState(){
        return cardState;
    }

    public boolean canDrawCards(){
        return cardState().deckSize() + cardState().discardsSize() >= 5;
    }



}
