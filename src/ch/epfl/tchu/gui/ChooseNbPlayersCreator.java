package ch.epfl.tchu.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


public class ChooseNbPlayersCreator {
    private ChooseNbPlayersCreator(){}

    static ChoiceBox<String> choice = new ChoiceBox<>();
    static String[] nbPLayers = {"2 joueurs", "3 joueurs", "4 joueurs", "5 joueurs"};
    private static final SimpleIntegerProperty simpleIntegerProperty = new SimpleIntegerProperty();

    public static IntegerProperty ChooseNbPlayers(Stage primaryStage){
        Stage stageNbMenu = new Stage();
        Button button = new Button(StringsFr.CHOOSE);
        GridPane pane = new GridPane();
        TextFlow text = new TextFlow();


        stageNbMenu.setScene(new Scene(pane, 250, 150));
        stageNbMenu.setTitle("Choisir le nombre de joueurs");
        text.getChildren().add(new Text("Commencer la partie avec"));
        pane.setPadding(new Insets(20, 0, 0, 20));
        pane.setVgap(5);

        pane.addRow(0, text);
        pane.addRow(1, choice);
        pane.addRow(2, button);
        choice.getItems().addAll(nbPLayers);
        AnchorPane.setRightAnchor(button, 50.0);
        button.disableProperty().bind(choice.valueProperty().isNull());
        button.setOnAction(e -> {
            simpleIntegerProperty.set(Integer.parseInt(String.valueOf(choice.getValue().charAt(0))));
            stageNbMenu.hide();
        });
        stageNbMenu.show();
        stageNbMenu.setOnCloseRequest(Event::consume);
        return simpleIntegerProperty;
    }

}
