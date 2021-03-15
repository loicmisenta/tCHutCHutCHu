package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 * Classe représentant l'état des cartes wagon et/ou locomotive qui ne sont pas en main des joueurs.
 *
 */
public final class CardState extends PublicCardState{
    //Creer une nouvelle pioche
    private final Deck<Card> deck;
    private final SortedBag<Card> discards;

    /**
     * le constructeur prend en parametre
     * @param faceUpCards les cartes faces visibles
     * @param deckSize taille du deck
     * @param discardsSize taille de la défausse
     * @param deck le deck
     * @param discards la défausse
     */
    private CardState(List<Card> faceUpCards, int deckSize, int discardsSize, Deck<Card> deck, SortedBag<Card> discards) {
        super(faceUpCards, deckSize, discardsSize);
        this.deck = deck;
        this.discards = SortedBag.of(discards);
    }

    /**
     *
     * @param deck
     * @return CardState avec les 5 premières cartes du deck face visible, la pioche sera les cartes restante.
     */
    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= 5);
        return new CardState(deck.topCards(5).toList(), deck.size()-5, 0, deck.withoutTopCards(5), SortedBag.of());

    }

    /**
     *
     * @param slot
     * @return CardState avec les carte faces visible identiques sauf celle a l'indice slot est remplacée par
     * la première carte de la pioche
     */
    public CardState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(!deck.isEmpty());
        if((slot < 0) || (slot >= 5)) throw new IndexOutOfBoundsException();
        List<Card> piocheModifié = new ArrayList<>(List.copyOf(faceUpCards()));
        piocheModifié.remove(slot);
        piocheModifié.add(slot, topDeckCard());

        return new CardState(piocheModifié, deckSize() -1, discardsSize() + 1, deck.withoutTopCard(), discards);
    }

    /**
     *
     * @return la carte se trouvant au sommet de la pioche.
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return deck.topCard();
    }

    /**
     *
     * @return CardState identique mais sans la prmière carte du tas.
     */
    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return new CardState(faceUpCards(), deckSize() -1, discardsSize(), deck.withoutTopCard(), discards);
    }

    /**
     *
     * @param rng générateur aléatoire
     * @return melange la pioche pour recreer un nouveau deck.
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(deckSize() == 0);

        //creer une condition pour taille 0 de discards ?

        Deck<Card> pioche = Deck.of(discards, rng);
        return new CardState(faceUpCards(), pioche.size(), 0, pioche, SortedBag.of());
    }

    /**
     *
     * @param additionalDiscards cartes a ajouté
     * @return un ensemble de cartes identique au récepteur mais avec les cartes données ajoutées à la défausse.
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        return new CardState(faceUpCards(), deckSize(), discardsSize() + additionalDiscards.size(), deck, discards.union(additionalDiscards));
    }

}
