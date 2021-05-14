package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerMain extends Application {

    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO il faut prendre le m Socket
        try (ServerSocket serverSocket = new ServerSocket(5108);
             Socket socket = serverSocket.accept()) {
             Player playerProxy = new RemotePlayerProxy(socket);


        }

    }
}
