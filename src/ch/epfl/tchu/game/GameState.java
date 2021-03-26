package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 * Représente l'état d'une partie
 */
public final class GameState extends PublicGameState{
    public Deck<Ticket> tickets;
    public CardState cardState;
    public Map<PlayerId, PlayerState> playerState;
    /**
     * TODO privé !
     * Constructeur privé de la partie de l'état de partie
     * @param cardState       l'état public des wagons/locomotoves
     * @param currentPlayerId le joueur courant
     * @param playerState     l'état public des joueurs
     * @param lastPlayer      l'identité du dernier joueur
     */
    public GameState(CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer,
                      Deck<Ticket> tickets) {
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
        this.tickets = tickets;  //ticketCount = tickets.size();
        this.cardState = cardState;
        this.playerState = playerState;
    }

    /**
     * @param tickets billets donnés au joueur
     * @param rng random qui mélange les cartes
     * @return un GameState initial
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng){
        Deck piocheInitiale = Deck.of(Constants.ALL_CARDS, rng);

        Map<PlayerId, PlayerState> map = new EnumMap<>(PlayerId.class);

        PlayerId firstPlayer = PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT));

        for (PlayerId p: PlayerId.ALL) {
            map.put(p, PlayerState.initial(piocheInitiale.topCards(Constants.INITIAL_CARDS_COUNT)));
            piocheInitiale = piocheInitiale.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        }
        Deck billets = Deck.of(tickets, rng);

        return new GameState( CardState.of(piocheInitiale), firstPlayer, map, null, billets);
    }

    public SortedBag<Ticket> topTickets(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= ticketsCount()));
        return tickets.topCards(count);
    }

    public GameState withoutTopTickets(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= ticketsCount()));
        return new GameState( cardState, currentPlayerId(), playerState, lastPlayer(),  tickets.withoutTopCards(count));
    }

    public Card topCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer(), tickets);
    }

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState( cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), playerState, lastPlayer(), tickets);
    }

    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        if (cardState.isDeckEmpty()){
            return new GameState( cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), playerState, lastPlayer(), tickets);
        } else return this;
    }

    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(playerState.get(playerId).tickets().isEmpty());
        return new GameState( cardState, currentPlayerId(), mapChange(playerId, playerState.get(playerId).withAddedTickets(chosenTickets)), lastPlayer(), tickets );
    }

    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        return new GameState( cardState, currentPlayerId(), mapChange(currentPlayerId(), currentPlayerState().withAddedTickets(chosenTickets)), lastPlayer(), tickets.withoutTopCards(drawnTickets.size()));
    }

    public GameState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(canDrawCards());
        return new GameState(cardState.withDrawnFaceUpCard(slot), currentPlayerId(), mapChange(currentPlayerId(), currentPlayerState().withAddedCard(cardState.faceUpCard(slot))), lastPlayer(), tickets);
    }
    public GameState withBlindlyDrawnCard(){
        Preconditions.checkArgument(canDrawCards());
        //playerState.put(currentPlayerId(), currentPlayerState().withAddedCard(cardState.topDeckCard()));

        return new GameState( cardState.withoutTopDeckCard(), currentPlayerId(), mapChange(currentPlayerId(), currentPlayerState().withAddedCard(cardState.topDeckCard())), lastPlayer(), tickets);
    }

    /**
     * @param route route donnée
     * @param cards cartes données
     * @return un état identique avec le joueur s'emparant de cette route avec ces cartes
     */

    //TEST POUR CONTRER L'UNSUPPORTED EXCEPTION
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        return new GameState( cardState.withMoreDiscardedCards(cards), currentPlayerId(), mapChange(currentPlayerId(), currentPlayerState().withClaimedRoute(route, cards)), lastPlayer(), tickets);
    }

    /**
     * @return vrai ssi le dernier tour commence
     */
    public boolean lastTurnBegins(){
        return ((lastPlayer() == null) && (currentPlayerState().carCount() <= 2));
    }


    /**
     * @return un état dans lequel le joueur courant finit son tour
     */
    public GameState forNextTurn(){
        if (lastTurnBegins()){
            return new GameState( cardState, currentPlayerId().next(), playerState, currentPlayerId(), tickets);
        }
        else return new GameState( cardState, currentPlayerId().next(), playerState, lastPlayer(), tickets);
    }

    /**
     * Redéfinition de la méthode de sa classe mère
     * @param playerId le joueur donné
     * @return l'état de ce joueur
     */
    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * Redéfinition de la méthode de sa classe mère
     * @return le joueur courant
     */
    @Override
    public PlayerState currentPlayerState(){
        return playerState(currentPlayerId());
    }

    private Map mapChange(PlayerId playerId, PlayerState playerstate){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(PlayerId.class);
        newPlayerState.put(playerId, playerstate);
        newPlayerState.put(playerId.next(), playerState.get(playerId.next()));
        return newPlayerState;
    }
}
