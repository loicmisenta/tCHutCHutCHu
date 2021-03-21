package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public final class GameState extends PublicGameState{

    int NB_CARTES_INIT = 4;
    /**
     * Constructeur privé de la partie publique de l'état de partie
     *
     * @param ticketsCount    la taille de la pioche de billets
     * @param cardState       l'état public des wagons/locomotoves
     * @param currentPlayerId le joueur courant
     * @param playerState     l'état public des joueurs
     * @param lastPlayer      l'identité du dernier joueur
     */
    private GameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {
        super(ticketsCount, cardState, currentPlayerId, playerState, lastPlayer);
    }

    public static GameState initial(SortedBag<Ticket> tickets, Random rng){
        Deck piocheInitiale = Deck.of(Constants.ALL_CARDS, rng);

        Map<PlayerId, PlayerState> map = new EnumMap<>(PlayerId.class);
        PlayerId firstPlayer = PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT));
        System.out.println(PlayerId.class.toString());


        //comment faire autrement ?  TODO
        for (Map.Entry<PlayerId, PlayerState> element: map.entrySet()) {
            map.put(element.getKey(), PlayerState.initial(piocheInitiale.topCards(4)));
            piocheInitiale = piocheInitiale.withoutTopCards(4);
        }

        Map<PlayerId, PublicPlayerState> mapPublique = Map.copyOf(map);
        Deck billets = Deck.of(tickets, rng);

        return new GameState(billets.size(), CardState.of(piocheInitiale), firstPlayer, mapPublique, null);
    }

    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState(playerId);
    }



}
