package ch.epfl.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.GraphicalPlayerAdapter;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Map;
import java.util.Random;
import static ch.epfl.tchu.game.PlayerId.*;

public class Stage11 extends Application {

    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage primaryStage) {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets(2));
        Map<PlayerId, String> names =
                Map.of(PLAYER_1, "Ada", PLAYER_2, "Charles");
        Map<PlayerId, Player> players =
                Map.of(PLAYER_1, new GraphicalPlayerAdapter(),
                        PLAYER_2, new GraphicalPlayerAdapter());
        Random rng = new Random();
        new Thread(() -> Game.play(players, names, tickets, rng))
                .start();
    }
}