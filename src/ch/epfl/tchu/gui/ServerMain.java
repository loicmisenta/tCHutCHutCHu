package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ServerMain extends Application {

    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {

        List<String> list = this.getParameters().getRaw();
        try (ServerSocket serverSocket = new ServerSocket(5108)) {
            Socket socket = serverSocket.accept();
            Map<PlayerId, String> map = new EnumMap<>(PlayerId.class);
            map.put(PlayerId.PLAYER_1, list.get(0));
            map.put(PlayerId.PLAYER_2, list.get(1));

            GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
            Player remotePlayerProxy = new RemotePlayerProxy(socket);

            Map<PlayerId, Player> mapPlayer = new EnumMap<>(PlayerId.class);
            mapPlayer.put(PlayerId.PLAYER_1, graphicalPlayerAdapter);
            mapPlayer.put(PlayerId.PLAYER_2, remotePlayerProxy);

            new Thread(() -> Game.play(mapPlayer, map, SortedBag.of(ChMap.tickets()), new Random())).start();

        }

    }
}
