package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Classe qui va créer le serveur pour lancer le jeu
 */
public final class ServerMainTwoMenus extends Application {
    private final List<String> joueurs = new ArrayList<>();
    private final IntegerProperty nbJoueurs = new SimpleIntegerProperty();


    /**
     * Méthode main qui  se contente d'appeler la méthode launch, qui démarre (entre autres) le fil d'application JavaFX,
     * puis appelle la méthode start sur ce fil.
     * @param args les arguments de la méthode
     */
    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        IntegerProperty s = ChooseNbPlayersCreator.ChooseNbPlayers(primaryStage);
        s.addListener((o, oV, nV)-> {
            System.out.println(nV.toString());
            nbJoueurs.set(nV.intValue());
            if (!(nV.intValue() == 0)){
                try {
                    startMenu(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startMenu(Stage primaryStage) {
        Platform.setImplicitExit(false);
        BooleanProperty notEmpty = new SimpleBooleanProperty(false);
        int i = 0;
        int joueur = nbJoueurs.getValue();
        ObservableList<String> s = MenuViewCreator.createMenuView(primaryStage, joueur);
        System.out.println(s);
        List<String> stringList = new ArrayList<>();
        s.addListener((ListChangeListener<String>) c -> {
            System.out.println(stringList);
            if(c.getList().size() == nbJoueurs.getValue()){
                stringList.addAll(c.getList());
                for (String el: stringList.subList(0, joueur )) {
                    joueurs.add(el);
                    notEmpty.set(!el.isEmpty());
                    System.out.println(el);
                }
            }
        });
        notEmpty.addListener((o, oV, nV) -> {
            if (nV){ new Thread(() ->{//TODO comment lancer que après que toute la List<StringProperty> soit pleine ?
                try {
                    startGame();
                } catch (Exception e) {
                    throw new Error();
                }
            }).start();
            }
        });

    }

    /**
     * methode qui va lancer la partie
     * @throws Exception exception
     */
    public void startGame() throws Exception {
        List<String> arguments = this.getParameters().getRaw();
        try (ServerSocket serverSocket = new ServerSocket(5108)) {
            Map<PlayerId, Player> mapPlayer = new EnumMap<>(PlayerId.class);
            Map<PlayerId, String> map = new EnumMap<>(PlayerId.class);
            int i = 0;
            mapPlayer.put(PlayerId.PLAYER_1, new GraphicalPlayerAdapter());
            System.out.println(joueurs);
            for (PlayerId id: PlayerId.ALL.subList(0, nbJoueurs.getValue())) {
                map.put(id, joueurs.get(i++));
                if(id == PlayerId.PLAYER_1) continue;
                Socket socket = serverSocket.accept();
                RemotePlayerProxy player = new RemotePlayerProxy(socket);
                mapPlayer.put(id, player);
            }
            new Thread(() -> Game.play(mapPlayer, map, SortedBag.of(ChMap.tickets(mapPlayer.size())), new Random())).start();

        }
    }
}
