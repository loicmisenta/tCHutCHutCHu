package ch.epfl.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.gui.GraphicalPlayerAdapter;
import ch.epfl.tchu.gui.MenuViewCreator;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Stage12Server extends Application{

        /**
         * Méthode main qui  se contente d'appeler la méthode launch, qui démarre (entre autres) le fil d'application JavaFX,
         * puis appelle la méthode start sur ce fil.
         * @param args les arguments de la méthode
         */
        public static void main(String[] args) { launch(args);}

        /**
         * Méthode qui va commencer la partie
         * @param primaryStage le stage principal
         * @throws Exception
         */
        @Override
        public void start(Stage primaryStage) throws Exception {

            try (ServerSocket serverSocket = new ServerSocket(5108)) {
                Map<PlayerId, String> map = new EnumMap<>(PlayerId.class);
                Map<PlayerId, Player> mapPlayer = new EnumMap<>(PlayerId.class);
                List<String> arguments = this.getParameters().getRaw();
                //TODO nb de joueurs
                mapPlayer.put(PlayerId.PLAYER_1, new GraphicalPlayerAdapter());
                map.put(PlayerId.PLAYER_1, arguments.get(0));
                map.put(PlayerId.PLAYER_2, arguments.get(1));


                for (PlayerId id: PlayerId.ALL) {
                    //map.put(id, MenuViewCreator.createMenuView(primaryStage));  //TODO faux pas dans une boucle main comment ? Blocking qeue ?
                    if(id == PlayerId.PLAYER_1) continue;
                    Socket socket = serverSocket.accept();
                    mapPlayer.put(id, new RemotePlayerProxy(socket)); //TODO nom par defaut  + Remote
                }

                //TODO comment lancer quand map contient une valeur pour le String  ?
                new Thread(() -> Game.play(mapPlayer, map, SortedBag.of(ChMap.tickets()), new Random())).start();

            }
        }
}
