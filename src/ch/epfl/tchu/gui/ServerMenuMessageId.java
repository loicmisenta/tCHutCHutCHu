package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ServerMenuMessageId extends Application {
    /**
     * Méthode main qui  se contente d'appeler la méthode launch, qui démarre (entre autres) le fil d'application JavaFX,
     * puis appelle la méthode start sur ce fil.
     * @param args les arguments de la méthode
     */
    public static void main(String[] args) { launch(args);}
    /**
     * methode qui va lancer la partie
     * @throws Exception exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> arguments = this.getParameters().getRaw();
        try (ServerSocket serverSocket = new ServerSocket(5108)) {
            Map<PlayerId, Player> mapPlayer = new EnumMap<>(PlayerId.class);
            Map<PlayerId, String> map = new EnumMap<>(PlayerId.class);
            int i = 0;
            mapPlayer.put(PlayerId.PLAYER_1, new GraphicalPlayerAdapter());
            //map.put(PlayerId.PLAYER_1, joueur.getValue());
            for (PlayerId id: PlayerId.ALL.subList(0, 3)) {
                map.put(id, arguments.get(i++));
                if(id == PlayerId.PLAYER_1) continue;
                Socket socket = serverSocket.accept();
                mapPlayer.put(id, new RemotePlayerProxy(socket));
            }
            new Thread(() -> Game.play(mapPlayer, map, SortedBag.of(ChMap.tickets(mapPlayer.size())), new Random())).start();

        }}
}
