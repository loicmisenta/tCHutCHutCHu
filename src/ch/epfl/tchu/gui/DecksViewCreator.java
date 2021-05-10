package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
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

public class DecksViewCreator { //TODO package private ?

    public static Node createHandView(ObservableGameState observableGameState ){
        int EXT_CARD_WIDTH = 60;
        int EXT_CARD_HEIGHT = 90;
        int INT_CARD_WIDTH = 40;
        int INT_CARD_HEIGHT = 70;

        HBox hBoxView = new HBox();
        Node billets = new ListView<>(observableGameState.ticketListReadOnly());
        billets.setId("tickets");
        //Ajouter un listner et

        hBoxView.getChildren().add(billets);
        hBoxView.getStylesheets().addAll("decks.css", "colors.css");

        HBox hboxHandPane = new HBox();
        hboxHandPane.setId("hand-pane");
        hBoxView.getChildren().add(hboxHandPane);

        //carte + compteur

        for (Card card: Card.ALL) {
            StackPane stackPane = new StackPane();
            if (card.equals(Card.LOCOMOTIVE)) {
                stackPane.getStyleClass().addAll("NEUTRAL", "card");
            } else {
                stackPane.getStyleClass().addAll(card.color().toString(), "card");
            }
            hboxHandPane.getChildren().add(stackPane);


            //Carte
            Rectangle r_Ext = new Rectangle(EXT_CARD_WIDTH, EXT_CARD_HEIGHT);
            r_Ext.getStyleClass().add("outside");
            Rectangle r_Int = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
            r_Int.getStyleClass().addAll("filled", "inside");
            Rectangle r_Train_Image = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
            r_Train_Image.getStyleClass().add("train-image");
            //Compteur
            ReadOnlyIntegerProperty count = observableGameState.nbTypeCarteReadOnly(card);
            count.addListener((o, oV, nV) -> stackPane.getStyleClass().add(nV.toString()));
            Text text = new Text();
            text.getStyleClass().add("count");
            text.textProperty().bind(Bindings.convert(count));
            stackPane.getChildren().addAll(r_Ext, r_Int, r_Train_Image, text);

            stackPane.visibleProperty().bind(Bindings.greaterThan(count, 0));
            text.visibleProperty().bind(Bindings.greaterThan(count, 1));



        }
        return hBoxView;
    }

    public static Node createCardsView(ObservableGameState observableGameState, ObjectProperty<ActionHandlers.DrawTicketsHandler> chooseTicketsH, ObjectProperty<ActionHandlers.DrawCardHandler> chooseCardsH){
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
        Button buttonBillet = new Button(StringsFr.TICKETS);
        buttonBillet.getStyleClass().add("gauged");
        vbox.getChildren().add(buttonBillet);
        buttonBillet.disableProperty().bind(chooseTicketsH.isNull());
        //SET ON ACtion
        buttonBillet.setOnAction(e -> chooseTicketsH.get().onDrawTickets());

        //jaugeBiller
        createGaugedButton(buttonBillet, observableGameState, observableGameState.percentageTicketsReadOnly());

        //Cartes
        for(int index: Constants.FACE_UP_CARD_SLOTS){

            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll(observableGameState.faceUpCardsReadOnly(index).toString(), "card");
            vbox.getChildren().add(stackPane);
            ReadOnlyObjectProperty<Card> faceUpCard = observableGameState.faceUpCardsReadOnly(index);
            stackPane.disableProperty().bind(chooseCardsH.isNull());
            faceUpCard.addListener((o, oV, nV) -> stackPane.getStyleClass().add(nV.name())); //Ajout d'un listener à la faceUpCard


            stackPane.setOnMouseClicked(e -> { chooseCardsH.get().onDrawCard(index); }
            );


            Rectangle r_Ext = new Rectangle(EXT_CARD_WIDTH, EXT_CARD_HEIGHT);
            r_Ext.getStyleClass().add("outside");
            Rectangle r_Int = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
            r_Int.getStyleClass().addAll("filled", "inside");
            Rectangle r_Train_Image = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
            r_Train_Image.getStyleClass().add("train-image");
            stackPane.getChildren().addAll(r_Ext, r_Int, r_Train_Image);
        }


        //PiocheCarte
        Button buttonCarte = new Button(StringsFr.CARDS);
        buttonCarte.getStyleClass().add("gauged");
        vbox.getChildren().add(buttonCarte);
        buttonCarte.disableProperty().bind(chooseCardsH.isNull());
        buttonCarte.setOnAction(e -> chooseCardsH.get().onDrawCard(-1));
        //jauge
        createGaugedButton(buttonCarte, observableGameState, observableGameState.percentageCardsLeftReadOnly());

        return vbox;
    }

    private static void createGaugedButton (Button button, ObservableGameState observableGameState, ReadOnlyIntegerProperty pctProperty){

        int RECT_INI_WIDTH = 50;
        int RECT_INIT_HEIGHT = 5;

        Group group = new Group();
        Rectangle rect_background = new Rectangle(RECT_INI_WIDTH, RECT_INIT_HEIGHT);
        Rectangle rect_foreground = new Rectangle(RECT_INI_WIDTH, RECT_INIT_HEIGHT);
        rect_background.getStyleClass().add("background");
        rect_foreground.getStyleClass().add("foreground");
        group.getChildren().addAll(rect_background, rect_foreground);
        button.setGraphic(group);

        pctProperty.addListener((o, oV, nV) ->  rect_foreground.getStyleClass().add(String.valueOf(nV)));
        System.out.println("  prop " + pctProperty);
        rect_foreground.widthProperty().bind(pctProperty.multiply(50).divide(100));
    }


}
