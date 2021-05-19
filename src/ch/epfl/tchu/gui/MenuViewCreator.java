package ch.epfl.tchu.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public final class MenuViewCreator { //TODO better to create for each player to after show ot in ServerMain ?
                                    // String in this case ?

    public static String createMenuView(Stage primaryStage){ //TODO type de retour ? ? ?
       String nom = enterString();
       Pane pane = new Pane();
       primaryStage.setScene(new Scene(pane));
       pane.getStylesheets().add("menu.css");

       primaryStage.setTitle("Tchu");
       Button play = new Button("PLAY");


       primaryStage.show();
       return nom;
    }


    private static String enterString(){
        AtomicReference<String> nom = null;
        TextInputControl entreeDuNom = new TextField();
        Button buttonNom = new Button("Choisir");
        buttonNom.disableProperty().bind(entreeDuNom.textProperty().isEmpty()); //TODO pas sûre que reste Observable ?
        // car montre que set can be null
        buttonNom.setOnAction(e -> {
            nom.set(entreeDuNom.getText()); //TODO atomic variables ?
        });
        return nom.get();
    }

}
