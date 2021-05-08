package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Classe représentant l'état des cartes wagon et/ou locomotive qui ne sont pas en main des joueurs.
 *
 */
public final class CardState extends PublicCardState{
    private final Deck<Card> deck;
    private final SortedBag<Card> discards;

    /**
     * Le constructeur qui prend en paramètre
     * @param faceUpCards les cartes faces visibles
     * @param deck le deck
     * @param discards la défausse
     */
    private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discards) {
        super(faceUpCards, deck.size(), discards.size());
        this.deck = deck;
        this.discards = SortedBag.of(discards);
    }


    /**
     *
     * @param deck un deck de cartes
     * @return CardState avec les 5 premières cartes du deck face visible, la pioche sera les cartes restante.
     */
    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT);
        return new CardState(deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList(), deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT), SortedBag.of());
    }


    /**
     *
     * @param slot l'indice
     * @return CardState avec les carte faces visible identiques sauf celle à slot est remplacée par
     * la première carte de la pioche
     */
    public CardState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(!isDeckEmpty());
        if((slot < 0) || (slot >= Constants.FACE_UP_CARDS_COUNT)) throw new IndexOutOfBoundsException();
        List<Card> piocheModifie = new ArrayList<>(List.copyOf(faceUpCards()));
        piocheModifie.set(slot, topDeckCard());

        return new CardState(piocheModifie,  deck.withoutTopCard(), discards);
    }

    /**
     *
     * @return la carte se trouvant au sommet de la pioche.
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!isDeckEmpty());
        return deck.topCard();
    }

    /**
     *
     * @return CardState identique mais sans la prmière carte du tas.
     */
    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!isDeckEmpty());
        return new CardState(faceUpCards(),  deck.withoutTopCard(), discards);
    }

    /**
     *
     * @param rng générateur aléatoire
     * @return melange la pioche pour recreer un nouveau deck.
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(isDeckEmpty());
        Deck<Card> pioche = Deck.of(discards, rng);
        return new CardState(faceUpCards(), pioche, SortedBag.of());
    }

    /**
     *
     * @param additionalDiscards cartes a ajouté
     * @return un ensemble de cartes identique au récepteur mais avec les cartes données ajoutées à la défausse.
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        return new CardState(faceUpCards(),  deck, discards.union(additionalDiscards));
    }

}
