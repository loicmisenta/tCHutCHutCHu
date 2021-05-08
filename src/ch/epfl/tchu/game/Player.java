package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Interface contenent les méthodes qui doivent communiquer des informations concernant
 * le déroulement de partie
 */
public interface Player {


    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     * Type énuméré  qui décrit les actions d'un joueur
     */
    enum TurnKind {
        DRAW_TICKETS, DRAW_CARDS, CLAIM_ROUTE;
        public static List<TurnKind> ALL = List.of(TurnKind.values());
    }

    /**
     * Appelée au début de la partie pour communiquer au joueur
     * @param ownId l'identité du joueur
     * @param playerNames noms des joueurs
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * Méthode appelée chaque fois qu'une information doit être communiquée au joueur au cours de la partie
     * @param info l'information
     */
    void receiveInfo(String info);

    /**
     * Appelée chaque fois que l'état du jeu change et prévient le joueur du
     * @param newState nouvel état de la partie
     * @param ownState l'état propre du joueur
     */
    void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * Appelée au début de la partie pour communiquer au joueur
     * @param tickets  les cinq billets qui lui ont été distribués
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * Appelée au début de la partie et demande au joueur lesquels des billets qu'on lui a
     * distribué initialement il veut garder
     * @return un SortedBag de Tickets qu'il garde
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * Appelée au début du tour d'un joueur, pour savoir quel type d'action il désire effectuer durant ce tour
     * @return l'action
     */
    TurnKind nextTurn();

    /**
     * Appelée lorsque le joueur a décidé de tirer des billets supplémentaires en cours de partie
     * @param options les billets tirés
     * @return les tickets qu'il garde
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * Appelée lorsque le joueur a décidé de tirer des cartes wagon/locomotive,
     * afin de savoir d'où il désire les tirer
     * @return l'emplacement de la carte visible/ ou de la pioche
     * ( dans ce cas la valeur retournée vaut Constants.DECK_SLOT (==1))
     */
    int drawSlot();

    /**
     * Appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route
     * @return la route dont il veut s'emparer
     */
    Route claimedRoute();

    /**
     * Appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route
     * @return les cartes qu'il utilise afin de le faire
     */
    SortedBag<Card> initialClaimCards();

    /**
     * Appelée lorsque le joueur a décidé de tenter de s'emparer d'un tunnel
     * et que des cartes additionnelles sont nécessaires
     * @param options les possibilités des cartes additionnelles
     * @return les cartes choisies additionelles
     * ou un ensemble vide (si le joueur ne veut ou ne peut pas choisir une des cartes)
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
