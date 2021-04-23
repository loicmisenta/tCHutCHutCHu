package ch.epfl.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerClient;

import java.util.List;
import java.util.Map;

public final class TestClient {
    public static void main(String[] args) {
        System.out.println("Starting client!");
        RemotePlayerClient playerClient =
                new RemotePlayerClient(new TestPlayer(),"localhost",5108);
        playerClient.run();
        System.out.println("Client done!");
    }

    private final static class TestPlayer implements Player {
        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> names) {
            System.out.printf("ownId: %s\n", ownId);
            System.out.printf("playerNames: %s\n", names);
        }

        @Override
        public void receiveInfo(String info) {
            System.out.println(info);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            System.out.println("update state");
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            System.out.println("Set initial ticket choice : " + tickets.toString());
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            List<Ticket> listTicket = List.of(ChMap.tickets().get(0), ChMap.tickets().get(1), ChMap.tickets().get(2));
            System.out.println("Choose Initial Tickets" + listTicket.toString());
            return SortedBag.of(listTicket);
        }

        @Override
        public TurnKind nextTurn() {
            System.out.println("DRAWN_CARDS");
            return TurnKind.DRAW_CARDS;
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
}