package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;

public final class MenuViewCreator{
    private static final IntegerProperty nbJoueursProperty = new SimpleIntegerProperty();
    private static final BlockingDeque<String> stringBlockingDeque = new LinkedBlockingDeque<>(1);
    private static final BooleanProperty inTheChooseNameMenu = new SimpleBooleanProperty(false);
    private static final ObservableList<String> listStringNames = observableArrayList();

    @FunctionalInterface
    interface ChooseNameHandler{
        void onChooseName(String name);
    }

    public static void chooseName(Stage stage, PlayerId player){

        ChooseNameHandler chooseNameHandler = name -> new Thread( () -> {
            try {
                stringBlockingDeque.put(name);
            }catch (InterruptedException e){
                throw new Error("Erreur dans chooseName");
            }
        }).start();
        runLater(()->enterString(chooseNameHandler, stage, player));
    }

    public static String getName(){
        try {
            return stringBlockingDeque.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    public static ObservableList<String> createMenuView(Stage primaryStage, int nbPlayers){
        nbJoueursProperty.set(nbPlayers);
        Stage stage = new Stage();
        stage.initOwner(primaryStage);

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

        List<PlayerId> joueurs = PlayerId.ALL.subList(0, nbPlayers);


        //Actions du bouton
        playButton.setOnAction(e -> {
            for (PlayerId player : reverseList(joueurs)) {
                chooseName(stage, player);
            }
        });
        playButton.disableProperty().bind(inTheChooseNameMenu);

        //ajouter le bouton au anchorPane
        anchorPane.getChildren().add(playButton);


        //Ajouter la scène
        Scene scene = new Scene(root);

        stage.setTitle("Tchu");
        stage.setScene(scene);
        stage.show();
        return listStringNames;

    }

    private static void enterString(ChooseNameHandler chooseNameHandler, Stage stageM, PlayerId player){
        inTheChooseNameMenu.set(true);
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.getStylesheets().add("menu.css");
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 50, 0, 50));

        //ajouter le titre
        Label titre = new Label(playerTitre(player));
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
            stageM.hide();
            chooseNameHandler.onChooseName(textField.getText());
            listStringNames.add(getName());
            stage.hide();
            inTheChooseNameMenu.set(false);
        });
        gridPane.addRow(4, buttonChoose);
        GridPane.setHalignment(buttonChoose, HPos.CENTER);


        //ajouter la scene

        Scene scene = new Scene(gridPane, width(player), 150);
        stage.setScene(scene);



        //ajouter le stage
        stage.setTitle("Nom");
        stage.setOnCloseRequest(Event::consume);
        stage.show();
    }

    private static int width(PlayerId player){
        if (player == PlayerId.PLAYER_1){
            return 250;
        } else {
            return 400;
        }
    }

    private static List<PlayerId> reverseList(List<PlayerId> myList) { List<PlayerId> invertedList = new ArrayList<PlayerId>(); for (int i = myList.size() - 1; i >= 0; i--) { invertedList.add(myList.get(i)); } return invertedList; }

    private static String playerTitre(PlayerId player){
        String string;
        if(player == PlayerId.PLAYER_1){
            string = "Entrez votre nom";
        } else if (player == PlayerId.PLAYER_2){
            string = "Entrez le nom du deuxième joueur";
        } else if(player == PlayerId.PLAYER_3){
            string = "Entrez le nom du troisième joueur";
        } else if(player == PlayerId.PLAYER_4){
            string = "Entrez le nom du quatrième joueur";
        } else {
            string = "Entrez le nom du cinquième joueur";
        }
        return string;
    }

}
