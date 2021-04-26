package ch.epfl.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap.*;
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
        private int i = 0;

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
            System.out.println("Choose Initial Tickets" + listTicket);
            return SortedBag.of(listTicket);
        }

        @Override
        public TurnKind nextTurn() {
            /*
            if(i == 5){
                System.out.println("CLAIMED_ROUTE");
                return TurnKind.CLAIM_ROUTE;
            }

             */
            System.out.println("DRAWN_CARDS");
            i++;

            return TurnKind.DRAW_CARDS;
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            System.out.println("CHOOSETICKET : " + options.get(0).toString());
            return SortedBag.of(options.get(0));
        }

        @Override
        public int drawSlot() {
            System.out.println("DRAWSLOT : " + 0);
            return 0;
        }

        @Override
        public Route claimedRoute() {
            Route zeRoute = new Route("DE5_STG_1", new Station(38, "Allemagne"), new Station(27, "Saint-Gall"), 2, Route.Level.OVERGROUND, null);
            System.out.println("CLAIMED_ROUTE : " + zeRoute);
            return zeRoute;

        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            System.out.println("INITIALE_CLAIM_CARDS : GREEN CARD");
            return SortedBag.of(Card.GREEN);
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            System.out.println("CHOOSEADDITIONALCARDS : " + options.get(0));
            return options.get(0);
        }

    }
}