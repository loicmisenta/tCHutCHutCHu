package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sun.javafx.application.PlatformImpl.isFxApplicationThread;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Classe représente l'interface graphique d'un joueur de tCHu.
 */
public class GraphicalPlayer {

    final PlayerId playerId;
    final Map<PlayerId, String> nomsJoueurs;
    final ObservableGameState observableGameState;
    final ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerProperty;
    final ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerProperty;
    final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerProperty;
    final ObservableList<Text> observableList;
    final Stage mainPane;

    /**
     * Constructeur de la classe GraphiquePlayer qui aura en parametre:
     * @param playerId l'identité du player
     * @param nomsJoueurs map du nom des joueur
     */
    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> nomsJoueurs) {
        assert isFxApplicationThread();
        this.playerId = playerId;
        this.nomsJoueurs = nomsJoueurs;
        this.observableGameState = new ObservableGameState(playerId);
        this.mainPane = new Stage(StageStyle.UTILITY);

        drawTicketsHandlerProperty = new SimpleObjectProperty<>();
        drawCardHandlerProperty = new SimpleObjectProperty<>();
        claimRouteHandlerProperty = new SimpleObjectProperty<>();
        observableList = observableArrayList();

        Node handView = DecksViewCreator.createHandView(observableGameState);
        Node cardsView = DecksViewCreator.createCardsView(observableGameState, drawTicketsHandlerProperty, drawCardHandlerProperty);
        Node mapView = MapViewCreator.createMapView(observableGameState, claimRouteHandlerProperty, this::chooseClaimCards);
        Node infoView = InfoViewCreator.createInfoView(playerId, nomsJoueurs, observableGameState, observableList); //TODO listText ?
        BorderPane mainPaneBorder = new BorderPane(mapView, null, cardsView, handView, infoView);

        //TODO infoView jamais liée + Player

        mainPane.setScene(new Scene(mainPaneBorder));
        mainPane.show();

    }

    /**
     * Appeler setState sur l'état observable du joueur,
     * @param publicGameState l'état public de la partie
     * @param playerState l'état du joueur complet
     */
    public void setState(PublicGameState publicGameState, PlayerState playerState){
        assert isFxApplicationThread();
        observableGameState.setState(publicGameState, playerState);
    }

    /**
     * Prenant un
     * @param message de type String
     * et l'ajoutant au bas des informations sur le déroulement de la partie,
     */
    public void receiveInfo(String message){
        assert isFxApplicationThread();
        if (observableList.size() == 5){
            observableList.remove( 0 , 1 );
        }
        observableList.add(new Text(message));

    }


    /**
     * Qui prend en arguments trois gestionnaires d'action, et qui permet au joueur d'en effectuer une
     * @param drawTicketsHandler gestionnaires d'action du tirage de ticket
     * @param drawCardHandler gestionnaires d'action du tirage de carte
     * @param claimRouteHandler gestionnaires d'action lors de la prise d'une route
     */
    public void startTurn(ActionHandlers.DrawTicketsHandler drawTicketsHandler, ActionHandlers.DrawCardHandler drawCardHandler, ActionHandlers.ClaimRouteHandler claimRouteHandler){
        assert isFxApplicationThread();
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

    /**
     * Utilisée quand le joueur va tirer sa deuxième carte
     * @param drawCardHandler gestionnaire de tirage de cartes
     */
    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler){ //TODO doit être appelée où?
        assert isFxApplicationThread();
        if(observableGameState.canDrawCards()){
            drawCardHandlerProperty.set(drawCardHandler);
        } else {
            drawCardHandlerProperty.set(null);
        }
        drawTicketsHandlerProperty.set(null);
        claimRouteHandlerProperty.set(null);
    }


    //TODO peut être simplement un SortedBag ? ? ? et non pas une liste ?

    /**
     * Utilisée quand le joueur va choisir un billet
     * @param ticketsOption bilet a choisir
     * @param chooseTicketsHandler gestionnaire de choix de billet
     */
    public void chooseTickets(List<SortedBag<Ticket>> ticketsOption, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler){
        assert isFxApplicationThread();
        String message = String.format(StringsFr.CHOOSE_TICKETS, Constants.IN_GAME_TICKETS_COUNT, StringsFr.plural(Constants.IN_GAME_TICKETS_COUNT));
        ListView<SortedBag<Ticket>> listView = new ListView<>(FXCollections.observableList(ticketsOption));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        BooleanProperty booleanProperty = new SimpleBooleanProperty(listView.getSelectionModel().getSelectedItems().size() >= ticketsOption.size()-2);
        // TODO Bindings.size()  + Obsv List

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle(StringsFr.TICKETS_CHOICE);
        BorderPane borderPane = new BorderPane(); //TODO titre?
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(mainPane);
        stage.initModality(Modality.WINDOW_MODAL);
        borderPane.setLeft(vbox);

        TextFlow textFlow = new TextFlow();
        Button button = new Button(StringsFr.CHOOSE);
        Text text = new Text(message);
        text.setStyle("-fx-font-weight: bold");
        vbox.getChildren().addAll( textFlow, listView, button);
        textFlow.getChildren().add(text);
        //Bindings.bindContent(text);

        button.disableProperty().bind(booleanProperty.not());
        stage.setOnCloseRequest(Event::consume);
        button.setOnAction(e ->{
            stage.hide();
            chooseTicketsHandler.onChooseTickets(listView.getSelectionModel().getSelectedItem());
        });
        stage.show();
    }

    /**
     * Utilisée quand le joueur va choisir un carte a claim
     * @param initialCards cartes initiales
     * @param chooseCardsHandler gestionnaire de choix de cartes
     */
    public void chooseClaimCards(List<SortedBag<Card>> initialCards, ActionHandlers.ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableList(initialCards));
        System.out.println(initialCards); //TODO POURQUOI [{BLUE, RED}, {BLACK, GREEN}] ???????
        BooleanProperty booleanProperty = new SimpleBooleanProperty(listView.getSelectionModel().getSelectedItems().size() >= 1);

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle(StringsFr.CARDS_CHOICE);
        BorderPane borderPane = new BorderPane(); //TODO titre?
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(mainPane);
        stage.initModality(Modality.WINDOW_MODAL);
        borderPane.setLeft(vbox);


        //TODO mettre tout cela dans chaque méthode !
        TextFlow textFlow = new TextFlow();
        Button button = new Button(StringsFr.CHOOSE);
        Text text = new Text(StringsFr.CHOOSE_CARDS);
        text.setStyle("-fx-font-weight: bold");
        vbox.getChildren().addAll(textFlow, listView, button);
        textFlow.getChildren().add(text);


        listView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        button.disableProperty().bind(booleanProperty.not());

        stage.setOnCloseRequest(Event::consume);
        button.setOnAction(e ->{
            stage.hide();
            chooseCardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItems().get(0)); //TODO pas sûre de ça !!!
        });

        stage.show();

    }

    /**
     * Utilisée quand le joueur va choisir un carte additionnelles
     * @param cartesAddit cartes additionelles
     * @param chooseCardsHandler gestionnaire de choix de cartes
     */
    public void chooseAdditionalCards(List<SortedBag<Card>> cartesAddit, ActionHandlers.ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();
        //Appeler le gestionnaire de choix avec le choix du joueur en argument.
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableList(cartesAddit));

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle(StringsFr.CARDS_CHOICE);
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(mainPane);
        stage.initModality(Modality.WINDOW_MODAL);
        borderPane.setLeft(vbox);



        TextFlow textFlow = new TextFlow();
        Button button = new Button(StringsFr.CHOOSE);
        Text text = new Text(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        text.setStyle("-fx-font-weight: bold");
        vbox.getChildren().addAll(textFlow, listView, button);
        textFlow.getChildren().add(text);


        stage.setOnCloseRequest(Event::consume);
        button.setOnAction(e ->{
            stage.hide();
            chooseCardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItems().get(0));//TODO PAS SUR NON PLUS !
        });
        stage.show();
    }


    private <T extends Comparable<T>> Stage fenetreDeSelect(String titre, String textIntro, ListView<SortedBag<T>> listView, BooleanProperty booleanProperty){

        Stage stage = new Stage(StageStyle.UTILITY);
        Text textTitre = new Text(titre);
        BorderPane borderPane = new BorderPane(textTitre);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(mainPane);
        stage.initModality(Modality.WINDOW_MODAL);
        borderPane.setLeft(vbox);
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
