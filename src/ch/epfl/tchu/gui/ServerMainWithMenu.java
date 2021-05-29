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
import ch.epfl.tchu.gui.MenuViewCreator;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Classe qui va créer le serveur pour lancer le jeu
 */
public final class ServerMainWithMenu extends Application {
    private final StringProperty joueur = new SimpleStringProperty();

    /**
     * Méthode main qui  se contente d'appeler la méthode launch, qui démarre (entre autres) le fil d'application JavaFX,
     * puis appelle la méthode start sur ce fil.
     * @param args les arguments de la méthode
     */
    public static void main(String[] args) { launch(args);}
    /**
     * Méthode qui va commencer le menu
     * @param primaryStage le stage principal
     * @throws Exception exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        StringProperty s = MenuViewCreator.createMenuView(primaryStage);
        s.addListener((o, oV, nV)-> {
            joueur.set(nV);
            if (!nV.isEmpty()){
                try {
                    startGame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //
    /**
     * methode qui va lancer la partie
     * @throws Exception exception
     */
    public void startGame() throws Exception {
        List<String> arguments = this.getParameters().getRaw();
        try (ServerSocket serverSocket = new ServerSocket(5108)) {
            Socket socket = serverSocket.accept();
            Map<PlayerId, String> map = new EnumMap<>(PlayerId.class);
            map.put(PlayerId.PLAYER_1, joueur.getValue());
            map.put(PlayerId.PLAYER_2, arguments.get(1));
            Map<PlayerId, Player> mapPlayer = new EnumMap<>(PlayerId.class);
            mapPlayer.put(PlayerId.PLAYER_1, new GraphicalPlayerAdapter());
            mapPlayer.put(PlayerId.PLAYER_2, new RemotePlayerProxy(socket));

            new Thread(() -> Game.play(mapPlayer, map, SortedBag.of(ChMap.tickets()), new Random())).start();

        }
    }
}
