package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Représente la partie publique de l'état d'une partie
 */
public class PublicGameState {
    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;
    private final int CAN_DRAW_TICKETS = 0;

    /**
     * Constructeur public de la partie publique de l'état de partie
     * @param ticketsCount la taille de la pioche de billets
     * @param cardState l'état public des wagons/locomotoves
     * @param currentPlayerId le joueur courant
     * @param playerState l'état public des joueurs
     * @param lastPlayer  l'identité du dernier joueur
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {
        Preconditions.checkArgument((ticketsCount >= CAN_DRAW_TICKETS ) && (playerState.size() == PlayerId.COUNT));
        if((cardState == null)|| (currentPlayerId == null)) throw new NullPointerException();
        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = Map.copyOf(Objects.requireNonNull(playerState));
        this.lastPlayer = lastPlayer;
    }

    /**
     * @return la taille de la pioche de tickets
     */
    public int ticketsCount(){
        return ticketsCount;
    }

    /**
     * @return vrai si il est encore possible de tirer des billets
     */
    public boolean canDrawTickets(){
        return ticketsCount != CAN_DRAW_TICKETS;
    }

    /**
     * @return  la partie publique de l'état des cartes wagon/locomotive
     */
    public PublicCardState cardState(){
        return cardState;
    }

    /**
     * @return  vrai si il est encore possible de tirer des cartes
     */
    public boolean canDrawCards(){
        return cardState().deckSize() + cardState().discardsSize() >= 5;
    }

    /**
     * @return l'identité du joueur actuel
     */
    public PlayerId currentPlayerId(){
        return currentPlayerId;
    }

    /**
     * @param playerId le joueur donné
     * @return  la partie publique de l'état de ce joueur
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }


    /**
     * @return  la partie publique de l'état du joueur courant
     */
    public PublicPlayerState currentPlayerState(){ return playerState(currentPlayerId);}

    /**
     * @return la totalité des routes dont l'un ou l'autre des joueurs s'est emparé
     */
    public List<Route> claimedRoutes(){
        List<Route> routes = new ArrayList<>();
        for (PublicPlayerState values:playerState.values()) {
            routes.addAll(values.routes());
        }
        return routes;
    }

    /**
     * @return l'identité du dernier joueur
     */
    public PlayerId lastPlayer(){
        return lastPlayer;
    }



}
