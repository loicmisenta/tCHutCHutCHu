package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.List.copyOf;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 * représentant un tas de cartes
 */
public final class Deck<C extends Comparable<C>>  {

    private final C c;
    private SortedBag<C> cards;

    private Deck(C c, SortedBag<C> cards){
        this.c  = c;
        this.cards = cards;
    }


    /**
     * Une méthode qui mélange:
     * @param cards le tas de cartes
     * @param rng générateur de nombres aléatoires
     * @param <C> le type des cartes du tas
     * @return
     */
    <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        return new Deck<C>(Collections.shuffle(cards.toList(), rng));
    }

    private SortedBag<C> getCards(){
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

    public Deck<C> withoutTopCard(){
        Preconditions.checkArgument(!isEmpty());
        return new Deck<C>(c , SortedBag.of((cards.toList()).subList(1, size())));
    }

    public SortedBag<C> topCards(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= size()));
        return SortedBag.of((cards.toList()).subList(1, count + 1));
    }

    public Deck<C> withoutTopCards(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= size()));
        return new Deck<C>(c ,SortedBag.of((cards.toList()).subList(count + 1, size())));
    }

}
