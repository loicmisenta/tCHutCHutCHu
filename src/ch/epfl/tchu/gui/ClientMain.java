package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.stage.Stage;

public class ClientMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
        RemotePlayerClient playerClient = new RemotePlayerClient(graphicalPlayerAdapter, "localhost", 5108);
        playerClient.run();

        //TODO quoi faire avec getParameters et getRaw
    }
}
