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
 */
public final class CardState extends PublicCardState{
    //Creer une nouvelle pioche
    Deck<Card> deck;

    private CardState(List<Card> faceUpCards, int deckSize, int discardsSize) {
        super(faceUpCards, deckSize, discardsSize);

    }

    public CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deckSize >= 5);
        this.deck = deck;
        return new CardState(deck.topCards(5).toList(), deckSize()-5, 0);

    }

    public CardState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(!deck.isEmpty());
        if((slot < 0) || (slot >= 5)) throw new IndexOutOfBoundsException();
        List<Card> piocheModifié = Collections.singletonList(List.copyOf(faceUpCards).remove(slot));
        piocheModifié.add(slot, topDeckCard());
        return new CardState(piocheModifié, deckSize() -1, discardsSize + 1);
    }

    public Card topDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return deck.topCard();
    }

    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        List<Card> piocheModifié = Collections.singletonList(List.copyOf(faceUpCards).remove(0));
        return new CardState(piocheModifié, deckSize() -1, discardsSize);
    }

    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(deckSize() == 0);
        List<Card> pioche = new ArrayList<>(deck.getCards());
        Collections.shuffle(pioche, rng);
        return new CardState(pioche, pioche.size(), 0);
    }
    //ON EST PERDU -> PAS SUR
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        return new CardState(faceUpCards(), deckSize(), discardsSize() + additionalDiscards.size());
    }
}
