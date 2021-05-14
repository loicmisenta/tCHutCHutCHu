package ch.epfl.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
//import ch.epfl.tchu.game.GameTest;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import ch.epfl.test.TestRandomizer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.*;

public final class TestServer {
    public static void main(String[] args) throws IOException {

        System.out.println("Starting server!");
        try (ServerSocket serverSocket = new ServerSocket(5108);
             Socket socket = serverSocket.accept()) {
            Player playerProxy = new RemotePlayerProxy(socket);
            var rng = TestRandomizer.newRandom();
            var playerNames = Map.of(PLAYER_1, "Ada",
                    PLAYER_2, "Charles");
            var routes = GameTest.ChMap.ALL_ROUTES;
            var p1 =  new PlayerState(SortedBag.of(ChMap.ALL_TICKETS.subList(0, 2)), SortedBag.of(1, Card.BLUE, 3, Card.WHITE), ChMap.routes().subList(1, 3));
            var p2 = new PlayerState(SortedBag.of(ChMap.tickets().subList(5, 8)), SortedBag.of(3, Card.VIOLET, 2, Card.BLACK), ChMap.routes().subList(6, 8));
            var players = Map.of(
                    PlayerId.PLAYER_1, (PublicPlayerState) p1,
                    PlayerId.PLAYER_2, (PublicPlayerState) p2);
            var deck = Deck.of(
                    SortedBag.of(Constants.FACE_UP_CARDS_COUNT, Card.RED),
                    TestRandomizer.newRandom());
            PublicCardState cardState = new PublicCardState(List.of(Card.GREEN, Card.BLACK, Card.LOCOMOTIVE, Card.BLUE, Card.LOCOMOTIVE), 15, 15);
            PublicGameState gameState = new PublicGameState(13, cardState, PLAYER_1, players, null );

            playerProxy.initPlayers(PLAYER_1, playerNames);
            playerProxy.initPlayers(PLAYER_2, playerNames);
            playerProxy.receiveInfo("LALA");
            playerProxy.updateState(gameState, p1);
            System.out.println(" -------");
            playerProxy.nextTurn();
            playerProxy.drawSlot();

            System.out.println("   .....");
            playerProxy.setInitialTicketChoice(SortedBag.of(ChMap.ALL_TICKETS.subList(0, 2)));
            playerProxy.nextTurn();
            playerProxy.initialClaimCards();
            playerProxy.nextTurn();
            SortedBag<Ticket> tickets = playerProxy.chooseTickets(SortedBag.of(ChMap.tickets().subList(0, 9)));
            playerProxy.chooseInitialTickets();
            playerProxy.claimedRoute();
            System.out.println("FINI !");
            playerProxy.initialClaimCards();
            playerProxy.nextTurn();

            playerProxy.chooseAdditionalCards(List.of(SortedBag.of(1, Card.BLUE, 2, Card.BLACK)));
            playerProxy.nextTurn();
            playerProxy.nextTurn();
            playerProxy.nextTurn();


        }
        System.out.println("Server done!");
    }
}
