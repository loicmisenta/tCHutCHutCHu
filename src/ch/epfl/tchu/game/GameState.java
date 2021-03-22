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
    Deck<Ticket> tickets;
    CardState cardState;
    Map<PlayerId, PlayerState> playerState;
    /**
     * Constructeur privé de la partie publique de l'état de partie
     *
     * @param ticketsCount    la taille de la pioche de billets
     * @param cardState       l'état public des wagons/locomotoves
     * @param currentPlayerId le joueur courant
     * @param playerState     l'état public des joueurs
     * @param lastPlayer      l'identité du dernier joueur
     */
    private GameState(int ticketsCount, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer,
                      Deck<Ticket> tickets) {
        //completer CardState?
        super(ticketsCount, cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
        this.tickets = tickets;
    }

    public static GameState initial(SortedBag<Ticket> tickets, Random rng){
        Deck piocheInitiale = Deck.of(Constants.ALL_CARDS, rng);

        Map<PlayerId, PlayerState> map = new EnumMap<>(PlayerId.class);

        PlayerId firstPlayer = PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT));
        System.out.println(PlayerId.class.toString());

        for (PlayerId p: PlayerId.ALL) {
            map.put(p, PlayerState.initial(piocheInitiale.topCards(Constants.INITIAL_CARDS_COUNT)));
            piocheInitiale = piocheInitiale.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        }
        Deck billets = Deck.of(tickets, rng);

        return new GameState(billets.size(), CardState.of(piocheInitiale), firstPlayer, map, null, billets);
    }


    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

   @Override
    public PlayerState currentPlayerState(){
        return playerState(currentPlayerId());
    }

    public SortedBag<Ticket> topTickets(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= ticketsCount()));
        return tickets.topCards(count);
    }

    public GameState withoutTopTickets(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= ticketsCount()));
        return new GameState(ticketsCount()-count, cardState, currentPlayerId(), playerState, lastPlayer(),  tickets.withoutTopCards(count));
    }

    public Card topCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(ticketsCount(), cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer(), tickets);
    }

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState(ticketsCount(), cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), playerState, lastPlayer(), tickets);
    }

    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        //pas fini
        if (cardState.isDeckEmpty()){
            return new GameState(ticketsCount(), cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), playerState, lastPlayer(), tickets);
        } else return this;
    }

    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){
        playerState.get(playerId).withAddedTickets(chosenTickets);
        return new GameState(ticketsCount(), cardState, currentPlayerId(), playerState, lastPlayer(), tickets );
    }



}