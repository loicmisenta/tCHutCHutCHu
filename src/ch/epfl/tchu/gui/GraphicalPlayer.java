package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sun.javafx.application.PlatformImpl.isFxApplicationThread;
import static javafx.collections.FXCollections.observableArrayList;

public class GraphicalPlayer {

    final PlayerId playerId;
    final Map<PlayerId, String> nomsJoueurs;
    final ObservableGameState observableGameState;
    ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerProperty;
    ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerProperty;
    ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerProperty;
    ObservableList<Text> observableList;
    final Stage mainPane;
    
    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> nomsJoueurs) {
        if (!isFxApplicationThread()) throw new AssertionError();
        this.playerId = playerId;
        this.nomsJoueurs = nomsJoueurs;
        this.observableGameState = new ObservableGameState(playerId);
        this.mainPane = new Stage(StageStyle.UTILITY);


        BorderPane borderPane = new BorderPane();
        Node handView = DecksViewCreator.createHandView(observableGameState);
        Node cardsView = DecksViewCreator.createCardsView(observableGameState, drawTicketsHandlerProperty, drawCardHandlerProperty);
        Node mapView = MapViewCreator.createMapView(observableGameState, claimRouteHandlerProperty, this::chooseClaimCards);
        BorderPane mainPaneBorder = new BorderPane(mapView, null, cardsView, handView, null);
        this.observableList = observableArrayList();
        Node infoView = InfoViewCreator.createInfoView(playerId, nomsJoueurs, observableGameState, observableList); //TODO listText ?

        mainPane.setScene(new Scene(borderPane)); //TODO appeler setScene dessus ?
        //window.show();
        //setState(gameState, playerstate); //TODO où avoir ce playerState ?

    }

    public void setState(PublicGameState publicGameState, PlayerState playerState){
        if (! isFxApplicationThread()) throw new AssertionError();
        observableGameState.setState(publicGameState, playerState);
    }
    
    public void receiveInfo(String message){  //TODO  5 derniers messages
        if (! isFxApplicationThread()) throw new AssertionError();
        if (observableList.size() == 5){
            observableList.remove( 0 , 1 );
        }
        observableList.add(new Text(message));

    }


    public void startTurn(ActionHandlers.DrawTicketsHandler drawTicketsHandler, ActionHandlers.DrawCardHandler drawCardHandler, ActionHandlers.ClaimRouteHandler claimRouteHandler){
        if (! isFxApplicationThread()) throw new AssertionError();
        if(observableGameState.canDrawCards()){
            drawCardHandlerProperty.set(drawCardHandler);
        } else {
            drawCardHandlerProperty.set(null);
        }
        if(observableGameState.canDrawTickets()){
            drawTicketsHandlerProperty.set(drawTicketsHandler);
        }else{
            drawTicketsHandlerProperty.set(null);
        }
        claimRouteHandlerProperty.set(claimRouteHandler);
    }


    public void chooseTickets(List<SortedBag<Ticket>> ticketsOption, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler){
        if (! isFxApplicationThread()) throw new AssertionError();
        String message = String.format(StringsFr.CHOOSE_TICKETS, Constants.IN_GAME_TICKETS_COUNT, StringsFr.plural(Constants.IN_GAME_TICKETS_COUNT));
        ListView<SortedBag<Ticket>> listView = new ListView<>(FXCollections.observableList(ticketsOption));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        BooleanProperty booleanProperty = new SimpleBooleanProperty(listView.getSelectionModel().getSelectedItems().size() >= ticketsOption.size()-2);
        // TODO Bindings.size()  + Obsv List

        Stage stage = new Stage(StageStyle.UTILITY);
        Text textTitre = new Text(StringsFr.TICKETS_CHOICE);

        BorderPane borderPane = new BorderPane(textTitre); //TODO titre?
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(mainPane);
        stage.initModality(Modality.WINDOW_MODAL);


        //TODO mettre tout cela dans chaque méthode !
        TextFlow textFlow = new TextFlow();
        Button button = new Button(StringsFr.CHOOSE);
        Text text = new Text(message);
        vbox.getChildren().addAll(listView, textFlow, button);
        textFlow.getChildren().add(text);


        //listView.setCellFactory(v -> new TextFieldListCell<SortedBag<T>>(new CardBagStringConverter()));
        button.disableProperty().bind(booleanProperty.not());
        stage.setOnCloseRequest(Event::consume);
        button.setOnAction(e ->{
            stage.hide();
            //TODO appeler le Handler specifique ?
            // chooseTickets(listView.getSelectionModel().getSelectedItems(), ActionHandlers.ChooseTicketsHandler);
        });
        //TODO faut-il le lier avec handview
        stage.show();
    }

    /**
     * Utilisée quand le joueur va tirer sa deuxième carte
     * @param drawCardHandler gestionnaire de tirage de cartes
     */


    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler){
        if (! isFxApplicationThread()) throw new AssertionError();
        if(observableGameState.canDrawCards()){
            drawCardHandlerProperty.set(drawCardHandler);
        } else {
            drawCardHandlerProperty.set(null);
        }
        drawTicketsHandlerProperty.set(null);
        claimRouteHandlerProperty.set(null);
    }

    public void chooseClaimCards(List<SortedBag<Card>> initialCards, ActionHandlers.ChooseCardsHandler chooseCardsHandler){
        if (! isFxApplicationThread()) throw new AssertionError();
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableList(initialCards));
        BooleanProperty booleanProperty = new SimpleBooleanProperty(listView.getSelectionModel().getSelectedItems().size() >= 1);

        Stage stage = new Stage(StageStyle.UTILITY);
        Text textTitre = new Text(StringsFr.CARDS_CHOICE);
        BorderPane borderPane = new BorderPane(textTitre); //TODO titre?
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(mainPane);
        stage.initModality(Modality.WINDOW_MODAL);


        //TODO mettre tout cela dans chaque méthode !
        TextFlow textFlow = new TextFlow();
        Button button = new Button(StringsFr.CHOOSE);
        Text text = new Text(StringsFr.CHOOSE_CARDS);
        vbox.getChildren().addAll(listView, textFlow, button);
        textFlow.getChildren().add(text);


        //listView.setCellFactory(v -> new TextFieldListCell<SortedBag<T>>(new CardBagStringConverter()));
        button.disableProperty().bind(booleanProperty.not());

        stage.setOnCloseRequest(Event::consume);
        button.setOnAction(e ->{
            stage.hide();
            chooseCardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItems().get(0)); //TODO pas sûre de ça !
        });



        stage.show();


    }

    public void chooseAdditionalCards(ObservableList<SortedBag<Card>> cartesAddit, ActionHandlers.ChooseCardsHandler chooseCardsHandler){
        if (! isFxApplicationThread()) throw new AssertionError();
        //Appeler le gestionnaire de choix avec le choix du joueur en argument.
        ListView<SortedBag<Card>> listView = new ListView<>(cartesAddit);
        BooleanProperty booleanProperty = new SimpleBooleanProperty(listView.getSelectionModel().getSelectedItems().size() >= 1);
        Stage stage = fenetreDeSelect(StringsFr.CARDS_CHOICE, StringsFr.CHOOSE_ADDITIONAL_CARDS,listView , booleanProperty);
        stage.show();
    }


    private <T extends Comparable<T>> Stage fenetreDeSelect(String titre, String textIntro, ListView<SortedBag<T>> listView, BooleanProperty booleanProperty){

        Stage stage = new Stage(StageStyle.UTILITY);
        Text textTitre = new Text(titre);
        BorderPane borderPane = new BorderPane(textTitre); //TODO titre?
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(mainPane);
        stage.initModality(Modality.WINDOW_MODAL);


        //TODO mettre tout cela dans chaque méthode !
        TextFlow textFlow = new TextFlow();
        Button button = new Button(StringsFr.CHOOSE);
        Text text = new Text(textIntro);
        vbox.getChildren().addAll(listView, textFlow, button);
        textFlow.getChildren().add(text);


        //listView.setCellFactory(v -> new TextFieldListCell<SortedBag<T>>(new CardBagStringConverter()));
        button.disableProperty().bind(booleanProperty.not());

        stage.setOnCloseRequest(Event::consume);
        button.setOnAction(e ->{
            stage.hide();
            //TODO appeler le Handler specifique ?
            // chooseTickets(listView.getSelectionModel().getSelectedItems(), ActionHandlers.ChooseTicketsHandler);
        });
        return stage;
    }

    private static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {
        @Override
        public String toString(SortedBag<Card> cards) { //TODO je ne sais pas si c'est le plus simple ?
            String cardsString = "";
            List<String> listString = new ArrayList<>();

            for (Card card: cards) {
                listString.add(cards.countOf(card) + " " + card.name());
            }
            //Affichage des cartes
            if(listString.size() == 1){
                cardsString += listString.get(0);
            } else {
                cardsString += String.join(StringsFr.AND_SEPARATOR, listString.get(0), listString.get(1));
            }
            return cardsString;
        }

        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }


}
