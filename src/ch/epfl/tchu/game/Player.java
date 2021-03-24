package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 * Interface contenent les méthodes qui doivent communiquer des informations concernant
 * le déroulement de partie
 */
public interface Player {
    /**
     * Appelée au début de la partie pour communiquer au joueur
     * @param ownId l'identité du joueur
     * @param playerNames noms des joueurs
     */
    public abstract void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * Méthode appelée chaque fois qu'une information doit être communiquée au joueur au cours de la partie
     * @param info l'information
     */
    public abstract void receiveInfo(String info);

    /**
     * Appelée chaque fois que l'état du jeu change et prévient le joueur du
     * @param newState nouvel état de la partie
     * @param ownState l'état propre du joueur
     */
    public abstract void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * Appelée au début de la partie pour communiquer au joueur
     * @param tickets  les cinq billets qui lui ont été distribués
     */
    public abstract void setInitialTicketChoice(SortedBag<Ticket> tickets);


    public abstract SortedBag<Ticket> chooseInitialTickets();

    public abstract TurnKind nextTurn();

    public abstract SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    public abstract int drawSlot();

    public abstract Route claimedRoute();

    public abstract SortedBag<Card> initialClaimCards();

    public abstract SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
