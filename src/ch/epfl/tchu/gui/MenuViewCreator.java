package ch.epfl.tchu.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import static javafx.application.Platform.runLater;

public final class MenuViewCreator{

    private static final StringProperty stringProperty = new SimpleStringProperty();
    private static Stage primaryStage1;
    private static final BlockingDeque<String> stringBlockingDeque = new LinkedBlockingDeque<>(1);
    private static final BooleanProperty inTheChooseNameMenu = new SimpleBooleanProperty(false);

    //TODO à deplacer dans ActionHandlers
    @FunctionalInterface
    interface ChooseNameHandler{
        void onChooseName(String name);
    }

    public static void chooseName(){

        ChooseNameHandler chooseNameHandler = name -> new Thread( () -> {
            try {
                stringBlockingDeque.put(name);
            }catch (InterruptedException e){
                throw new Error("Erreur dans chooseName");
            }
        }).start();
        runLater(()->enterString(chooseNameHandler));
    }

    public static String getName(){
        try {
            return stringBlockingDeque.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    public static StringProperty createMenuView(Stage primaryStage){ //TODO type de retour ? ? ? Et si on veut avoir deux types ?
        primaryStage1 = primaryStage;

        VBox root = new VBox();
        root.getStylesheets().add("menu.css");

        //StackPane
        AnchorPane anchorPane = new AnchorPane();
        root.getChildren().add(anchorPane);

        //Ajouter le fond au anchorPane
        Node fond = new ImageView();
        fond.getStyleClass().add("ImageView");
        anchorPane.getChildren().add(fond);

        //Ajouter le bouton au anchorPane
        Button playButton = new Button("PLAY");
        playButton.setId("round-red");

        //Action si on passe la souris sur le bouton
        playButton.hoverProperty().addListener((o, oV, nV)-> {
            if (nV){
                playButton.setId("round-red-dragOver");
            }else{
                playButton.setId("round-red");
            }
        });

        //positioner le bouton
        playButton.setMinSize(192, 48);
        AnchorPane.setLeftAnchor(playButton, 400.0);
        AnchorPane.setRightAnchor(playButton, 400.0);
        AnchorPane.setBottomAnchor(playButton, 120.0);

        //Actions du bouton
        playButton.setOnAction(e -> chooseName());
        playButton.disableProperty().bind(inTheChooseNameMenu);

        //ajouter le bouton au anchorPane
        anchorPane.getChildren().add(playButton);


        //Ajouter la scène
        Scene scene = new Scene(root);
        primaryStage.setTitle("Tchu");
        primaryStage.setScene(scene);

        primaryStage.show();

        return stringProperty;

    }


    private static void enterString(ChooseNameHandler chooseNameHandler){
        inTheChooseNameMenu.set(true);
        Stage stage = new Stage();


        //gridPane
        GridPane gridPane = new GridPane();
        gridPane.getStylesheets().add("menu.css");
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 0, 0, 20));

        //ajouter le titre
        Label titre = new Label("Entrez votre nom");
        titre.setId("chooseNameWindow");
        gridPane.addRow(0, titre);

        //ajouter l'entrée du nom
        TextInputControl textField = new TextField();
        gridPane.addRow(2, textField);
        textField.setPromptText("Nom");
        textField.setFocusTraversable(false);

        //ajouter le bouton
        Button buttonChoose = new Button("Choisir");
        buttonChoose.disableProperty().bind(textField.textProperty().isEmpty());
        buttonChoose.setOnAction(e -> {
            //primaryStage1.hide();
            chooseNameHandler.onChooseName(textField.getText());
            stringProperty.set(getName());
            stage.hide();
            inTheChooseNameMenu.set(false);

        });
        gridPane.addRow(4, buttonChoose);
        GridPane.setHalignment(buttonChoose, HPos.CENTER);


        //ajouter la scene
        Scene scene = new Scene(gridPane, 200, 150);
        stage.setScene(scene);

        //ajouter le stage
        stage.setTitle("Nom");
        stage.initOwner(primaryStage1);
        stage.setOnCloseRequest(Event::consume);
        stage.show();
    }

}
