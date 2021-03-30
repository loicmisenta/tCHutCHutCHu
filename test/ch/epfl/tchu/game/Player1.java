package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class Player1 implements Player{
    PlayerId ownId;
    Map<PlayerId, String> playerNames;
    {
        this.playerNames = new HashMap<PlayerId, String>();
        playerNames.put(PlayerId.PLAYER_1, "joueur 1");
        playerNames.put(PlayerId.PLAYER_2, "joueur 2");
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        this.ownId = ownId;
        this.playerNames = playerNames;
        System.out.println("l'initialisation du " + ownId + "/n" + "Les joueurs sont" + playerNames.get(ownId) + " " + playerNames.get(ownId.next()));
    }

    @Override
    public void receiveInfo(String info) {
        System.out.println("info reçue:" + " /n" + info);
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        System.out.println("l'update de l'etat " + newState);
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return null;
    }

    @Override
    public int drawSlot() {
        return 0;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
    }
}
