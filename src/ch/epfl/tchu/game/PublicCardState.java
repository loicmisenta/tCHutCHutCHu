package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class PublicCardState {
     private List<Card> faceUpCards;
     private int deckSize;
     private int discardsSize;

    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize){
        Preconditions.checkArgument((faceUpCards.size() == 5) && (deckSize >= 0 ) && (discardsSize >= 0));
        this.faceUpCards = faceUpCards;
        this.deckSize = Objects.requireNonNull(deckSize);
        this.discardsSize = discardsSize;
    }

    public int totalSize(){
        return 5 + deckSize() + discardsSize();
    }

    public List<Card> faceUpCards(){
        return faceUpCards;
    }

    public Card faceUpCard(int slot){
        if ((slot < 0) || (slot >= 5)) throw new IndexOutOfBoundsException();
        return faceUpCards.get(slot);
    }

    public int deckSize(){
        return deckSize;
    }

    public int discardsSize(){
        return discardsSize;
    }

    public boolean isDeckEmpty(){
        return deckSize() == 0;
    }

}
