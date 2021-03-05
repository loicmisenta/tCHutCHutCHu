package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 * représentant un tas de cartes
 */
public final class Deck<C extends Comparable<C>>  {
    private final C c;
    private Deck(C c){

    }

    /**
     * Une méthode qui mélange:
     * @param cards le tas de cartes
     * @param rng générateur de nombres aléatoires
     * @param <C> le type des cartes du tas
     * @return
     */
    <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        return new Deck<C>(Collections.shuffle((cards), rng));
    }
}
