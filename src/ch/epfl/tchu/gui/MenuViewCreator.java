package ch.epfl.tchu.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

//TODO should the class be static ?
public final class MenuViewCreator { //TODO better to create for each player to after show ot in ServerMain ?
                                    // String in this case ?

    private Stage primaryStage; //TODO mettre en final ?

    public String createMenuView(Stage primaryStage){ //TODO type de retour ? ? ?
       this.primaryStage = primaryStage;
       Pane pane = new Pane();
       primaryStage.setScene(new Scene(pane));
       pane.getStylesheets().add("menu.css");

       primaryStage.setTitle("Tchu");
       Button play = new Button("PLAY");
       String nom = enterString(play);

       primaryStage.show();
       return nom;
    }


    private String enterString(Button play){

        Stage stage = new Stage();
        VBox vbox = new VBox();
        stage.initOwner(primaryStage);

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
