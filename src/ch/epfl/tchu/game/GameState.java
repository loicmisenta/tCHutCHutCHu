package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
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
    Deck<Card> cartes;
    Deck<Ticket> tickets;
    CardState cardState;

    Map<PlayerId, PublicPlayerState> playerState; //TODO on peut faire ça?
    /**
     * Constructeur privé de la partie publique de l'état de partie
     *
     * @param ticketsCount    la taille de la pioche de billets
     * @param cardState       l'état public des wagons/locomotoves
     * @param currentPlayerId le joueur courant
     * @param playerState     l'état public des joueurs
     * @param lastPlayer      l'identité du dernier joueur
     */
    private GameState(int ticketsCount, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer,
                      Deck<Ticket> tickets, Deck<Card> cartes) {
        //completer CardState?
        super(ticketsCount, cardState, currentPlayerId, playerState, lastPlayer);
        this.tickets = tickets;
        this.cartes = cartes;
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

        return new GameState(billets.size(), CardState.of(piocheInitiale), firstPlayer, mapPublique, null, billets, piocheInitiale);
    }

    // TODO ??
    @Override
    public PlayerState playerState(PlayerId playerId){
        //est-ce qu'elle appelle playerState de PublicGameState?
        return new PlayerState(currentPlayerState().tickets(), currentPlayerState().cards(), currentPlayerState().routes());
    }
    // TODO ??
   @Override
    public PlayerState currentPlayerState(){
        //doit contenir les tickets du joueur ?
        return new PlayerState(tickets, cardState(), playerState(playerId).routes());
    }

    public SortedBag<Ticket> topTickets(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= ticketsCount()));
        return tickets.topCards(count);
    }

    public GameState withoutTopTickets(int count){
        //TODO creer la map ?
        return new GameState(ticketsCount()-count, cardState, currentPlayerId(), playerState, lastPlayer(),  tickets.withoutTopCards(count), cartes);
    }

    public Card topCard(){
        Preconditions.checkArgument(!cartes.isEmpty());
        return cartes.topCard();
    }

    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cartes.isEmpty());
        return new GameState(ticketsCount(), cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer(), tickets, cartes.withoutTopCard());
    }

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        //TODO quoi enlever aux cartes ?
        return new GameState(ticketsCount(), cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), playerState, lastPlayer(), tickets, cartes.);
    }

    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        //pas fini
        if (cardState.isDeckEmpty()){
            //TODO ....
            return this;
        } else return this;
    }





}
