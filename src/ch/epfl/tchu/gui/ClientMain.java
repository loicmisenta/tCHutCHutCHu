package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.stage.Stage;

import java.util.List;

public class ClientMain extends Application {

    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
        List<String> list = this.getParameters().getRaw();
        RemotePlayerClient playerClient = new RemotePlayerClient(graphicalPlayerAdapter, list.get(0), Integer.parseInt(list.get(1)));
        new Thread(playerClient::run).start();

    }
}
