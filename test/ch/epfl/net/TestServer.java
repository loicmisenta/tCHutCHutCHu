package ch.epfl.net;

import ch.epfl.tchu.game.Game;
//import ch.epfl.tchu.game.GameTest;
import ch.epfl.tchu.game.GameTest;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerProxy;

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

            var playerNames = Map.of(PLAYER_1, "Ada",
                    PLAYER_2, "Charles");

            playerProxy.initPlayers(PLAYER_1, playerNames);
            playerProxy.initPlayers(PLAYER_2, playerNames);
            playerProxy.receiveInfo("LALA");
        }
        System.out.println("Server done!");
    }
}
