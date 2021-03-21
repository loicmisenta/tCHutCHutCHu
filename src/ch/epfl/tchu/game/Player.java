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
    public abstract void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    public abstract void receiveInfo(String info);

    public abstract void updateState(PublicGameState newState, PlayerState ownState);

    public abstract void setInitialTicketChoice(SortedBag<Ticket> tickets);

    public abstract SortedBag<Ticket> chooseInitialTickets();

    public abstract TurnKind nextTurn();

    public abstract SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    public abstract int drawSlot();

    public abstract Route claimedRoute();

    public abstract SortedBag<Card> initialClaimCards();

    public abstract SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
