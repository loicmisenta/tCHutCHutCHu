package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.*;

public class DecksViewCreator {

    public static Node createHandView(ObservableGameState observableGameState ){
        int EXT_CARD_WIDTH = 60;
        int EXT_CARD_HEIGHT = 90;
        int INT_CARD_WIDTH = 40;
        int INT_CARD_HEIGHT = 70;

        HBox hBoxView = new HBox();
        Node billets = new ListView<>(observableGameState.ticketListReadOnly());
        billets.setId("tickets");

        hBoxView.getChildren().add(billets);
        hBoxView.getStylesheets().addAll("decks.css", "colors.css");

        HBox hboxHandPane = new HBox();
        hboxHandPane.setId("hand-pane");
        hBoxView.getChildren().add(hboxHandPane);

        //carte + compteur
        //TODO juste la boucle ?
        for (Card card: Card.ALL) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll(card.color().toString(), "card");
            hboxHandPane.getChildren().add(stackPane);

            //Carte
            Rectangle r_Ext = new Rectangle(EXT_CARD_WIDTH, EXT_CARD_HEIGHT);
            r_Ext.getStyleClass().add("outside");
            Rectangle r_Int = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
            r_Int.getStyleClass().addAll("filled", "inside");
            Rectangle r_Train_Image = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
            r_Train_Image.getStyleClass().add("train-image");
            stackPane.getChildren().addAll(r_Ext, r_Int, r_Train_Image);
            //Compteur
            Text text = new Text();
            text.getStyleClass().add("count");



            ReadOnlyIntegerProperty count = observableGameState.nbTypeCarteReadOnly(card);
            text.visibleProperty().bind(Bindings.greaterThan(count, 1));
            //BIND JSP QUOI
        }
        return hBoxView;
    }

    public static Node createCardsView(ObservableGameState observableGameState, ObjectProperty<ActionHandlers.DrawTicketsHandler> chooseTicketsHandler, ObjectProperty<ActionHandlers.DrawCardHandler> chooseCardsHandler){
        int EXT_CARD_WIDTH = 60;
        int EXT_CARD_HEIGHT = 90;
        int INT_CARD_WIDTH = 40;
        int INT_CARD_HEIGHT = 70;
        int RECT_INI_WIDTH = 50;
        int RECT_INIT_HEIGHT = 5;

        VBox vbox = new VBox();
        vbox.getStylesheets().addAll("decks.css", "colors.css");
        vbox.setId("card-pane");

        //Pioche Billet
        Button buttonBillet = new Button();
        buttonBillet.getStyleClass().add("gauged");
        vbox.getChildren().add(buttonBillet);

        //PiocheCarte
        Button buttonCarte = new Button();
        buttonCarte.getStyleClass().add("gauged");
        vbox.getChildren().add(buttonCarte);

        //Cartes
        for (int i = 0; i < Constants.FACE_UP_CARD_SLOTS.size(); i++) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll(observableGameState.faceUpCardsReadOnly(i).toString(), "card");
            vbox.getChildren().add(stackPane);

            Rectangle r_Ext = new Rectangle(EXT_CARD_WIDTH, EXT_CARD_HEIGHT);
            r_Ext.getStyleClass().add("outside");
            Rectangle r_Int = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
            r_Int.getStyleClass().addAll("filled", "inside");
            Rectangle r_Train_Image = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
            r_Train_Image.getStyleClass().add("train-image");
            stackPane.getChildren().addAll(r_Ext, r_Int, r_Train_Image);
        }
        //jauge
        Group group = new Group();
        Rectangle rect_background = new Rectangle(RECT_INI_WIDTH, RECT_INIT_HEIGHT);
        Rectangle rect_foreground = new Rectangle(RECT_INI_WIDTH, RECT_INIT_HEIGHT);
        group.getChildren().addAll(rect_background, rect_foreground);
        //TODO besoin de getChildren ou set graphic suffit ?
        buttonBillet.setGraphic(group);
        buttonCarte.setGraphic(group);

        return vbox;
    }


}
