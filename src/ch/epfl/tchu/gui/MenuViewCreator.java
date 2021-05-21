package ch.epfl.tchu.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


//TODO creer des actions et l'interface en séparée ?
//TODO should the class be static ?
public final class MenuViewCreator { //TODO better to create for each player to after show ot in ServerMain ?
                                        // String in this case ?

    private static final StringProperty stringProperty = new SimpleStringProperty(); //TODO utiliser une prop
    private static Stage primaryStage1; //TODO mettre en final ?


    public static String createMenuView(Stage primaryStage){ //TODO type de retour ? ? ? Et si on veut avoir deux types ?
        // Passer deux val en paramètre et les changer?

       primaryStage1 = primaryStage;
       Pane pane = new Pane();
       Button play = new Button("PLAY");
       Node fond = new ImageView();

       primaryStage.setScene(new Scene(pane));
       primaryStage.setTitle("Tchu");
       pane.getStylesheets().add("menu.css");
       play.getStyleClass().add("round-red");
       fond.getStyleClass().add("ImageView");

       pane.getChildren().addAll(fond, play);
       play.setOnAction(e -> enterString());
       //stringProperty.addListener((o, oV, nV)-> );
       primaryStage.show();
       return stringProperty.toString(); //TODO faire retourner que après le changement
                                            // d'une valeur
    }


    private String enterString(Button play){

        Stage stage = new Stage();
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid);
        stage.setTitle("Choisir nom");
        stage.initOwner(primaryStage1);
        stage.setScene(scene);
        TextInputControl entreeDuNom = new TextField();


        Label titre = new Label("Entrez votre nom");
        Label nameL = new Label("Nom : ");
        Button buttonNom = new Button("Choisir");

        buttonNom.disableProperty().bind(entreeDuNom.textProperty().isEmpty());
        buttonNom.setOnAction(e -> {
            stage.hide();
            stringProperty.set(entreeDuNom.getText());
        });

        grid.addRow(0, titre);
        grid.addRow(1, nameL, entreeDuNom);
        grid.addRow(2, buttonNom);
        GridPane.setHalignment(titre, HPos.CENTER);
        GridPane.setHalignment(buttonNom, HPos.CENTER);
        stage.show();
    }

}
