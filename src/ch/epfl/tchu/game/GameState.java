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
    private final Deck<Ticket> tickets;
    private final CardState cardState;
    private final Map<PlayerId, PlayerState> playerState;
    /**
     *
     * Constructeur privé de la partie de l'état de partie
     * @param cardState       l'état public des wagons/locomotoves
     * @param currentPlayerId le joueur courant
     * @param playerState     l'état public des joueurs
     * @param lastPlayer      l'identité du dernier joueur
     * @param tickets         le deck de ticket
     */
    private GameState(CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer,
                      Deck<Ticket> tickets) {
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
        this.tickets = tickets;
        this.cardState = cardState;
        this.playerState = playerState;
    }

    /**
     * @param tickets billets donnés au joueur
     * @param rng random qui mélange les cartes
     * @return un GameState initial
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng){
        Deck<Card> piocheInitiale = Deck.of(Constants.ALL_CARDS, rng);

        Map<PlayerId, PlayerState> map = new EnumMap<>(PlayerId.class);

        PlayerId firstPlayer = PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT));

        for (PlayerId p: PlayerId.ALL) {
            map.put(p, PlayerState.initial(piocheInitiale.topCards(Constants.INITIAL_CARDS_COUNT)));
            piocheInitiale = piocheInitiale.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        }
        Deck<Ticket> billets = Deck.of(tickets, rng);

        return new GameState( CardState.of(piocheInitiale), firstPlayer, map, null, billets);
    }

    /**
     * @return les
     * @param count
     * premier ticket
     */
    public SortedBag<Ticket> topTickets(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= ticketsCount()));
        return tickets.topCards(count);
    }

    /**
     *
     * @return un état identique au récepteur, mais sans les
     * @param count
     * billets du sommet de la pioche
     */
    public GameState withoutTopTickets(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= ticketsCount()));
        return new GameState( cardState, currentPlayerId(), playerState, lastPlayer(),  tickets.withoutTopCards(count));
    }

    /**
     *
     * @return a carte au sommet de la pioche
     */
    public Card topCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    /**
     *
     * @return un état identique au récepteur mais sans la carte au sommet de la pioche
     */
    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer(), tickets);
    }

    /**
     *
     * @return un état identique au récepteur mais avec les
     * @param discardedCards
     * données ajoutées à la défausse
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState( cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), playerState, lastPlayer(), tickets);
    }

    /**
     *
     * @param rng générateur aléatoire
     * @return un état identique au récepteur sauf si la pioche de cartes est vide(si c'est le cas elle sera recréée à partir de la défausse mélangée au moyen de @param rng)
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        if (cardState.isDeckEmpty()){
            return new GameState( cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), playerState, lastPlayer(), tickets);
        } else return this;
    }

    /**
     *
     * @return un état identique au récepteur mais dans lequel les
     * @param chosenTickets
     * donnés ont été ajoutés à la main du
     * @param playerId
     * donné
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(playerState.get(playerId).tickets().isEmpty());
        return new GameState( cardState, currentPlayerId(), mapChange(playerId, playerState.get(playerId).withAddedTickets(chosenTickets)), lastPlayer(), tickets );
    }

    /**
     *
     * @return un état identique au récepteur, mais dans lequel le joueur courant a tiré
     * @param drawnTickets
     * du sommet de la pioche, et choisi de garder ceux contenus dans
     * param chosenTickets
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        return new GameState( cardState, currentPlayerId(), mapChange(currentPlayerId(), currentPlayerState().withAddedTickets(chosenTickets)), lastPlayer(), tickets.withoutTopCards(drawnTickets.size()));
    }

    /**
     * @return un état identique au récepteur si ce n'est que la carte face retournée à
     * @param slot
     * a été placée dans la main du joueur courant, et remplacée par celle au sommet de la pioche
     */
    public GameState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(canDrawCards());
        return new GameState(cardState.withDrawnFaceUpCard(slot), currentPlayerId(), mapChange(currentPlayerId(), currentPlayerState().withAddedCard(cardState.faceUpCard(slot))), lastPlayer(), tickets);
    }

    /**
     * @return un état identique au récepteur si ce n'est que la carte du sommet de la pioche a été placée dans la main du joueur courant
     */
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

    /**
     *
     * @return une nouvelle map changée qui a la valeur associée à
     * @param playerId
     * modifié et égale à
     * @param playerstate
     */
    private Map<PlayerId, PlayerState> mapChange(PlayerId playerId, PlayerState playerstate){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(PlayerId.class);
        newPlayerState.put(playerId, playerstate);
        newPlayerState.put(playerId.next(), playerState.get(playerId.next()));
        return newPlayerState;
    }
}
