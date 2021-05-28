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
public final class ServerMainTwoMenus extends Application {
    private final StringProperty joueur = new SimpleStringProperty();
    private final IntegerProperty nbJoueurs = new SimpleIntegerProperty();

    /**
     * Méthode main qui  se contente d'appeler la méthode launch, qui démarre (entre autres) le fil d'application JavaFX,
     * puis appelle la méthode start sur ce fil.
     * @param args les arguments de la méthode
     */
    public static void main(String[] args) { launch(args);}
    //TODO un par un marche + Mais seul non ?

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        IntegerProperty s = ChooseNbPlayersCreator.ChooseNbPlayers(primaryStage);
        s.addListener((o, oV, nV)-> {
            System.out.println(nV.toString());
            nbJoueurs.set(nV.intValue());
            if (!(nV.intValue() == 0)){
                try {
                    //startGame();
                    startMenu(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startMenu(Stage primaryStage) {
        Platform.setImplicitExit(false);
        StringProperty s = MenuViewCreator.createMenuView(primaryStage);
        s.addListener((o, oV, nV)-> new Thread( () ->{
            joueur.set(nV);
            if (!nV.isEmpty()){
                try {
                    startGame();
                } catch (Exception e) {
                    throw new Error();
                }

        }}));
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
            int i = 1;
            mapPlayer.put(PlayerId.PLAYER_1, new GraphicalPlayerAdapter());
            map.put(PlayerId.PLAYER_1, joueur.getValue());
            //map.put(PlayerId.PLAYER_1, arguments.get(0));
            System.out.println(nbJoueurs.getValue());
            for (PlayerId id: PlayerId.ALL.subList(0, nbJoueurs.getValue())) {
                if(id == PlayerId.PLAYER_1) continue;
                map.put(id, arguments.get(i++));
                Socket socket = serverSocket.accept();
                mapPlayer.put(id, new RemotePlayerProxy(socket));
            }
            Platform.setImplicitExit(true);
            new Thread(() -> Game.play(mapPlayer, map, SortedBag.of(ChMap.tickets()), new Random())).start();

        }
    }
}
