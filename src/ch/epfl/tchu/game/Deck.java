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

    private final List<C> cards; //TODO private

    public Deck( List<C> cards){
        this.cards = List.copyOf(cards);
    }


    /**
     * Une méthode qui mélange:
     * @param cards le tas de cartes
     * @param rng générateur de nombres aléatoires
     * @param <C> le type des cartes du tas
     * @return un tas de cartes mélangés
     */

    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        List<C> cardArray = new ArrayList<>(cards.toList());
        Collections.shuffle(cardArray, rng);
        return new Deck<C>( cardArray);
    }


    private List<C> getCards(){
        return cards;
    }

    /**
     * @return la taille du tas
     */
    public int size(){
        return getCards().size();
    }
    /**
     * @return vrai si le tas est vide
     */
    public boolean isEmpty(){
        return getCards().isEmpty();
    }

    /**
     * @return la carte du sommet du tas
     * @throws IllegalArgumentException (grâce à Preconditions) si vide
     */
    public C topCard(){
        Preconditions.checkArgument(!isEmpty());
        return getCards().get(0);
    }


    /**
     * @return un tas identique au récepteur
     * sans la carte du sommet du tas
     * @throws IllegalArgumentException (grâce à Preconditions) si vide
     */
    public Deck<C> withoutTopCard(){
        Preconditions.checkArgument(!isEmpty());
        return new Deck<C>( (cards).subList(1, size()));
    }

    /**
     * @param count le nb de cartes se trouvant au sommet du tas
     * @return un multisensemble contenant ces cartes
     * @throws IllegalArgumentException (grâce à Preconditions) si pas compris entre
     * 0 et la taille du tas
     */
    public SortedBag<C> topCards(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= size()));
        return SortedBag.of((cards).subList(0, count));
    }

    /**
     * @param count le nb de cartes se trouvant au sommet du tas
     * @return un tas identique au récepteur (this) mais sans ces cartes du sommet
     * @throws IllegalArgumentException (grâce à Preconditions) si pas compris entre
     * 0 et la taille du tas
     */
    public Deck<C> withoutTopCards(int count){
        Preconditions.checkArgument((count >= 0 ) && (count <= size()));
        return new Deck<C>((cards).subList(count , size()));
    }


}
