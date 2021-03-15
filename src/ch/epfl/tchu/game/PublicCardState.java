package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 * Classe représenant une partie de l'état des cartes wagon ou/et locomotive qui
 * donc les 5 cartes visibles à côté du plateau, ceux dans la pioche,
 * ceux dans la défausse
 *
 */
public class PublicCardState {
     private final List<Card> faceUpCards;
     private final int deckSize;
     private final int discardsSize;

    /**
     * Le constructeur qui prend en paramètre
     * @param faceUpCards les cartes face visible
     * @param deckSize la taille du deck
     * @param discardsSize la taille des discards
     * @throws IllegalArgumentException (grâce à Preconditions) si le nb des cartes
     * n'est pas d'exactement de 5 et la taille de la pioche ou des discards est négative
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize){
        Preconditions.checkArgument((faceUpCards.size() == 5) && (deckSize >= 0 ) && (discardsSize >= 0));
        this.faceUpCards = List.copyOf(faceUpCards);   //faire ça avec chaque constructeur!
        this.deckSize = deckSize;
        this.discardsSize = discardsSize;
    }

    /**
     * @return le nombre total de cartes qui ne sont pas en main des joueur
     */
    public int totalSize(){
        return 5 + deckSize() + discardsSize();
    }

    /**
     * @return les 5 cartes face visible, sous la forme d'une liste comportant
     * exactement 5 éléments
     */
    public List<Card> faceUpCards(){
        return faceUpCards;
    }

    /**
     * @param slot l'index
     * @return la carte face visible à l'index donné
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 0 (inclus) et 5 (exclus)
     */
    public Card faceUpCard(int slot){
        if ((slot < 0) || (slot >= 5)) throw new IndexOutOfBoundsException();
        return faceUpCards.get(slot);
    }

    /**
     * @return la taille de la pioche
     */
    public int deckSize(){
        return deckSize;
    }

    /**
     * @return la taille de la défausse
     */
    public int discardsSize(){
        return discardsSize;
    }

    /**
     * @return vrai si la pioche est vide
     */
    public boolean isDeckEmpty(){
        return deckSize() == 0;
    }

}
