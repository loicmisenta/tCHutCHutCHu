package ch.epfl.tchu.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static com.sun.javafx.application.PlatformImpl.runLater;

public class ChooseNbPlayersCreator {
    private ChooseNbPlayersCreator(){}
    static ChoiceBox<String> choice = new ChoiceBox<>();
    static String[] nbPLayers = {"2 joueurs", "3 joueurs", "4 joueurs", "5 joueurs"};
    private static final BlockingDeque<String> stringBlockingDeque = new LinkedBlockingDeque<>();

    public static int ChooseNbPlayers(){
        Stage stage = new Stage();
        Button button = new Button(StringsFr.CHOOSE);

        stage.setScene(new Scene(choice));
        choice.getItems().addAll(nbPLayers);
        button.disableProperty().bind(choice.valueProperty().isNull());
        button.setOnAction(e -> {
            chooseNbPlayers();
            stage.hide();
        });
        stage.show();
        stage.setOnCloseRequest(Event::consume);
        return Integer.parseInt(String.valueOf(getNumber().charAt(0)));
    }

    public static void chooseNbPlayers(){
        ActionHandlers.ChooseNbPlayersHandler chooseNbPlayersHandler = number -> {
            try {
                stringBlockingDeque.put(number);
            } catch (InterruptedException e) {
                throw new Error();
            }
        };
        runLater(() -> chooseNbPlayersHandler.onChooseNbPlayers(choice.getValue()));
    }

    public static String getNumber(){
        try {
            return stringBlockingDeque.take();
        } catch (InterruptedException e) {
            throw new Error();
        }

    }
}
