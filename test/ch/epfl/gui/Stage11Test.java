package ch.epfl.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.GraphicalPlayerAdapter;
import ch.epfl.tchu.gui.MenuViewCreator;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

import static ch.epfl.tchu.game.PlayerId.*;

public class Stage11Test extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        StringProperty joueur = new SimpleStringProperty();
        StringProperty s = MenuViewCreator.createMenuView(primaryStage);
        s.addListener((o, oV, nV)-> joueur.set(nV));



        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets(2));
        Map<PlayerId, String> names =
                Map.of(PLAYER_1, joueur.toString(), PLAYER_2, "Charles");
        Map<PlayerId, Player> players =
                Map.of(PLAYER_1, new GraphicalPlayerAdapter(),
                        PLAYER_2, new GraphicalPlayerAdapter());
        Random rng = new Random();

        new Thread(() -> Game.play(players, names, tickets, rng))
                .start();


    }
}

