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

public class DecksViewCreator {

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
            if (card.equals(Card.LOCOMOTIVE)){
                stackPane.getStyleClass().addAll("NEUTRAL", "card");
            }
            else{
                stackPane.getStyleClass().addAll(card.color().toString(), "card");
            }
            hboxHandPane.getChildren().add(stackPane);

            //Ajouter un listener
            //Ajouter un SetOnMouseClicked à StackPane TODO not sure?

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
        Button buttonBillet = new Button(StringsFr.TICKETS);
        buttonBillet.getStyleClass().add("gauged");
        vbox.getChildren().add(buttonBillet);
        buttonBillet.disableProperty().bind(chooseTicketsHandler.isNull());

        ObservableList<Ticket> tickets = observableGameState.ticketListReadOnly();
        //Ajouter un listner
        //TODO ne peut pas ajouter de lambda ?  pour le listner ? ?  add nV de String MAIS PAS POSSIBLE CAR LISTE
        tickets.addListener((o, oV, nV) ->  buttonBillet.getStyleClass().add(nV)); //TODO not sure


        //jaugeBiller
        Group group = new Group();
        Rectangle rect_backgroundB = new Rectangle(RECT_INI_WIDTH, RECT_INIT_HEIGHT);
        Rectangle rect_foregroundB = new Rectangle(RECT_INI_WIDTH, RECT_INIT_HEIGHT);
        group.getChildren().addAll(rect_backgroundB, rect_foregroundB);
        buttonBillet.setGraphic(group);


        //TODO n'ajoute pas le pourcentage ?
        ReadOnlyIntegerProperty pctPropertyTickets = observableGameState.percentageTicketsReadOnly();
        pctPropertyTickets.addListener((o, oV, nV) ->  rect_foregroundB.getStyleClass().add(String.valueOf(nV)));
        rect_foregroundB.widthProperty().bind(pctPropertyTickets.multiply(50).divide(100));


        //Cartes
        for (int i = 0; i < Constants.FACE_UP_CARD_SLOTS.size(); i++) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll(observableGameState.faceUpCardsReadOnly(i).toString(), "card");
            vbox.getChildren().add(stackPane);
            ReadOnlyObjectProperty<Card> faceUpCard = observableGameState.faceUpCardsReadOnly(i);
            stackPane.disableProperty().bind(chooseCardsHandler.isNull());
            faceUpCard.addListener((o, oV, nV) -> stackPane.getStyleClass().add(nV.name())); //Ajout d'un listener à la faceUpCard

            //TODO choisir l'index
            ActionHandlers.ChooseCardsHandler chooseFaceUpCard = chosenCard -> chooseCardsHandler.get().onDrawCard(chosenCard.);
            stackPane.setOnMouseClicked();
            //ajouter un setOnMouseClicked ! TODO ? not sure

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
        buttonCarte.disableProperty().bind(chooseCardsHandler.isNull());





        //jauge
        Group group2 = new Group();
        Rectangle rect_backgroundC = new Rectangle(RECT_INI_WIDTH, RECT_INIT_HEIGHT);
        Rectangle rect_foregroundC = new Rectangle(RECT_INI_WIDTH, RECT_INIT_HEIGHT);
        group2.getChildren().addAll(rect_backgroundC, rect_foregroundC);
        buttonCarte.setGraphic(group2);

        //TODO ne marche pas ?
        ReadOnlyIntegerProperty pctPropertyCards = observableGameState.percentageCardsLeftReadOnly();
        pctPropertyCards.addListener((o, oV, nV) ->  rect_foregroundC.getStyleClass().add(String.valueOf(nV)));
        rect_foregroundC.widthProperty().bind(pctPropertyCards.multiply(50).divide(100));



        return vbox;
    }


}
