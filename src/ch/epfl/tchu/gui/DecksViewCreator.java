package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.*;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.List;

import static ch.epfl.tchu.game.ChMap.routes;


/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Classe contenant les méthodes qui crée la represntation graphique des decks
 */

final class DecksViewCreator {

    final static int EXT_CARD_WIDTH = 60;
    final static int EXT_CARD_HEIGHT = 90;
    final static int INT_CARD_WIDTH = 40;
    final static int INT_CARD_HEIGHT = 70;

    private DecksViewCreator(){}
    /**
     * Méthode prenant en argument l'état du jeu observable et retourne la vue de la main,
     * @param observableGameState état du jeu observable
     * @return la vue de la main
     */
    public static Node createHandView(ObservableGameState observableGameState){

        ObservableList<Ticket> ticketList = observableGameState.ticketListReadOnly();

        HBox hBoxView = new HBox();
        ListView<Ticket> billets = new ListView<>(ticketList);
        billets.setCellFactory(e->new ListCell<>(){
            @Override
            protected void updateItem(Ticket item, boolean empty) {
                if (!empty){
                    if (ticketowned(item, observableGameState.ownedRoutesCurrentPlayerReadOnly())){
                        setStyle("-fx-control-inner-background: derive(palegreen, 50%);");
                    }else {
                        setStyle("-fx-control-inner-background: derive(white, 0%);");
                    }
                    setText(item.text());
                }
            }
        });

        billets.setId("tickets");

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
            createCard(stackPane);


            //Compteur
            ReadOnlyIntegerProperty count = observableGameState.nbTypeCarteReadOnly(card);
            count.addListener((o, oV, nV) -> stackPane.getStyleClass().add(nV.toString()));
            Text text = new Text();
            text.getStyleClass().add("count");
            text.textProperty().bind(Bindings.convert(count));
            stackPane.getChildren().add(text);

            stackPane.visibleProperty().bind(Bindings.greaterThan(count, 0));
            text.visibleProperty().bind(Bindings.greaterThan(count, 1));
        }
        return hBoxView;
    }


    /**
     * Méthode prenant en argument l'état du jeu observable et retourne la vue des cartes en jeu
     * @param observableGameState état du jeu observable
     * @return la vue des cartes en jeu
     */
    public static Node createCardsView(ObservableGameState observableGameState, ObjectProperty<ActionHandlers.DrawTicketsHandler> chooseTicketsH, ObjectProperty<ActionHandlers.DrawCardHandler> chooseCardsH){

        VBox vbox = new VBox();
        vbox.getStylesheets().addAll("decks.css", "colors.css");
        vbox.setId("card-pane");


        //Pioche Billet
        Button buttonBillet = new Button(StringsFr.TICKETS);
        buttonBillet.getStyleClass().add("gauged");
        vbox.getChildren().add(buttonBillet);
        buttonBillet.disableProperty().bind(chooseTicketsH.isNull());
        buttonBillet.setOnAction(e -> chooseTicketsH.get().onDrawTickets());
        //jaugeBiller
        createGaugedButton(buttonBillet, observableGameState.percentageTicketsReadOnly());

        //Cartes
        for(int index: Constants.FACE_UP_CARD_SLOTS){

            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll("", "card");
            vbox.getChildren().add(stackPane);
            ReadOnlyObjectProperty<Card> faceUpCard = observableGameState.faceUpCardsReadOnly(index);
            stackPane.disableProperty().bind(chooseCardsH.isNull());
            faceUpCard.addListener((o, oV, nV) -> {
                if (nV.equals(Card.LOCOMOTIVE)){
                    stackPane.getStyleClass().set(0, "NEUTRAL");
                } else{
                    stackPane.getStyleClass().set(0, nV.name());
                }
            });

            stackPane.setOnMouseClicked(e -> chooseCardsH.get().onDrawCard(index));
            createCard(stackPane);
        }

        //PiocheCarte
        Button buttonCarte = new Button(StringsFr.CARDS);
        buttonCarte.getStyleClass().add("gauged");
        vbox.getChildren().add(buttonCarte);
        buttonCarte.disableProperty().bind(chooseCardsH.isNull());
        buttonCarte.setOnAction(e -> chooseCardsH.get().onDrawCard(Constants.DECK_SLOT));
        //jauge
        createGaugedButton(buttonCarte, observableGameState.percentageCardsLeftReadOnly());

        return vbox;
    }


    private static void createCard(StackPane stackPane){
        Rectangle r_Ext = new Rectangle(EXT_CARD_WIDTH, EXT_CARD_HEIGHT);
        r_Ext.getStyleClass().add("outside");
        Rectangle r_Int = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
        r_Int.getStyleClass().addAll("filled", "inside");
        Rectangle r_Train_Image = new Rectangle(INT_CARD_WIDTH, INT_CARD_HEIGHT);
        r_Train_Image.getStyleClass().add("train-image");
        stackPane.getChildren().addAll(r_Ext, r_Int, r_Train_Image);
    }

    private static void createGaugedButton (Button button, ReadOnlyIntegerProperty pctProperty){

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
        rect_foreground.widthProperty().bind(pctProperty.multiply(50).divide(100));
    }

    //retourne vrai si il a réussi a compléter le ticket
    private static boolean ticketowned(Ticket t, List<Route> routeList){
        int maxValeur = 0;
        for (Route routesPossibles: routeList) {
            int maximumlocal = Math.max(routesPossibles.station1().id(), routesPossibles.station2().id());
            if ( maximumlocal> maxValeur){
                maxValeur = maximumlocal;
            }
        }
        StationPartition.Builder partitionBuild = new StationPartition.Builder(maxValeur + 1);
        for (Route routesPossibles: routeList) { partitionBuild.connect(routesPossibles.station1(), routesPossibles.station2()); }
        StationPartition partition = partitionBuild.build();
        return t.points(partition) > 0;

    }





}
