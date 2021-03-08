package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;
import static java.util.List.copyOf;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 * Représentant un tas de cartes
 */
public final class Deck<C extends Comparable<C>>  {

    private List<C> cards;

    public Deck( List<C> cards){
        this.cards = cards;
    }


    /**
     * Une méthode qui mélange:
     * @param cards le tas de cartes
     * @param rng générateur de nombres aléatoires
     * @param <C> le type des cartes du tas
     * @return
     */

    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        List<C> cardArray = new ArrayList<>(cards.toList());
        Collections.shuffle(cardArray, rng);
        return new Deck<C>( cardArray);
    }

    //PAS SUR MAIS VOILA KOI
    public List<C> getCards(){
        return cards;
    }

    public int size(){
        return getCards().size();
    }

    public boolean isEmpty(){
        return getCards().isEmpty();
    }

    public C topCard(){
        Preconditions.checkArgument(!isEmpty());
        return getCards().get(0);
    }
    //PEUT ETRE IMMUABLE LISTE

    public Deck<C> withoutTopCard(){
        Preconditions.checkArgument(!isEmpty());
        return new Deck<C>( (cards).subList(1, size()));
    }

    public SortedBag<C> topCards(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= size()));
        return SortedBag.of((cards).subList(0, count));
    }

    public Deck<C> withoutTopCards(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= size()));
        return new Deck<C>((cards).subList(count + 1, size()));
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                '}';
    }
}
