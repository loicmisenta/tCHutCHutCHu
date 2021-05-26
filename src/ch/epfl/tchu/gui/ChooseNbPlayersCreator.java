package ch.epfl.tchu.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static com.sun.javafx.application.PlatformImpl.runLater;

public class ChooseNbPlayersCreator {
    private ChooseNbPlayersCreator(){}

    static ChoiceBox<String> choice = new ChoiceBox<>();
    static String[] nbPLayers = {"2 joueurs", "3 joueurs", "4 joueurs", "5 joueurs"};
    private static final BlockingDeque<String> stringBlockingDeque = new LinkedBlockingDeque<>();

    @FunctionalInterface
    interface ChooseNbPlayersHandler{
        void onChooseNbPlayers(String name);
    }

    public static IntegerProperty ChooseNbPlayers(Stage primaryStage){
        Stage stage = new Stage();
        Button button = new Button(StringsFr.CHOOSE);
        GridPane pane = new GridPane();
        TextFlow text = new TextFlow();


        stage.setScene(new Scene(pane));
        stage.setTitle("Choisir le nombre de joueurs");
        text.getChildren().add(new Text("Commencer la partie avec"));
        pane.setPadding(new Insets(20, 0, 0, 20));
        pane.setVgap(5);

        pane.addRow(0, text);
        pane.addRow(1, choice);
        pane.addRow(2, button);  //pane.getChildren().addAll(text, choice, button);
        choice.getItems().addAll(nbPLayers);
        button.disableProperty().bind(choice.valueProperty().isNull());
        button.setOnAction(e -> {
            chooseNbPlayers();
            stage.hide();
        });
        stage.initOwner(primaryStage);
        stage.show();
        stage.setOnCloseRequest(Event::consume);
        return new SimpleIntegerProperty(Integer.parseInt(String.valueOf(getNumber().charAt(0))));
    }

    public static void chooseNbPlayers(){
        ChooseNbPlayersHandler chooseNbPlayersHandler = number -> new Thread(() -> {
            try {
                stringBlockingDeque.put(number);
            } catch (InterruptedException e) {
                throw new Error();
            }
        }).start();
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
