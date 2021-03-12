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
    private Deck<Card> deck;
    private SortedBag<Card> discards;


    public CardState(List<Card> faceUpCards, int deckSize, int discardsSize, Deck<Card> deck, SortedBag<Card> discards) {
        super(faceUpCards, deckSize, discardsSize);
        this.deck = deck;
        this.discards = discards;
    }


    static public CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= 5);
        return new CardState(deck.topCards(5).toList(), deck.size()-5, 0, deck, SortedBag.of());

    }

    public CardState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(!deck.isEmpty());
        if((slot < 0) || (slot >= 5)) throw new IndexOutOfBoundsException();
        System.out.println(List.copyOf(faceUpCards()));
        List<Card> piocheModifié = new ArrayList<>(List.copyOf(faceUpCards()));
        piocheModifié.remove(slot);

        piocheModifié.add(slot, topDeckCard());
        return new CardState(piocheModifié, deckSize() -1, discardsSize() + 1, deck, discards.union(SortedBag.of(faceUpCards().get(slot))));
    }

    public Card topDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return deck.topCard();
    }

    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        List<Card> piocheModifié = Collections.singletonList(List.copyOf(faceUpCards()).remove(0));
        return new CardState(piocheModifié, deckSize() -1, discardsSize(), deck, discards);
    }

    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(deckSize() == 0);
        Deck<Card> pioche = Deck.of(discards, rng);
        return new CardState((pioche.topCards(5)).toList(), pioche.size(), 0, pioche, SortedBag.of());
    }

    //ON EST PERDU -> PAS SUR
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        return new CardState(faceUpCards(), deckSize(), discardsSize() + additionalDiscards.size(), deck, discards.union(additionalDiscards));
    }

}
