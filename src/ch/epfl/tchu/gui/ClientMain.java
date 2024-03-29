package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.List;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Classe qui va créer le client pour lancer le jeu
 */
public final class ClientMain extends Application {
    /**
     * Méthode main qui  se contente d'appeler la méthode launch, qui démarre (entre autres) le fil d'application JavaFX,
     * puis appelle la méthode start sur ce fil.
     * @param args les arguments
     */
    public static void main(String[] args) { launch(args);}


    /**
     * Méthode qui va commencer la partie
     * @param primaryStage le stage principal
     */


    @Override
    public void start(Stage primaryStage) throws Exception{
        String adress;
        int socket;
        GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
        List<String> arguments = this.getParameters().getRaw();
        if (arguments.isEmpty()){
            adress = "localhost";
            socket = 5108;
        } else {
            adress = arguments.get(0);
            socket = Integer.parseInt(arguments.get(1));
        }
        RemotePlayerClient playerClient = new RemotePlayerClient(graphicalPlayerAdapter, adress, socket);
        new Thread(playerClient::run).start();

    }
}
