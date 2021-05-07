package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

import java.util.Map;

import static com.sun.javafx.application.PlatformImpl.isFxApplicationThread;

public class GraphicalPlayer {

    final PlayerId playerId;
    final Map<PlayerId, String> nomsJoueurs;
    final ObservableGameState observableGameState;
    ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerProperty; //TODO final ?
    ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerProperty;
    ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerProperty;
    final Stage mainPane;
    
    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> nomsJoueurs){
        if (! isFxApplicationThread()) throw new AssertionError();
        this.playerId = playerId;
        this.nomsJoueurs = nomsJoueurs;
        this.observableGameState = new ObservableGameState(playerId);
        this.mainPane = new Stage(StageStyle.UTILITY);
        BorderPane borderPane = new BorderPane();

        Node mapView = MapViewCreator.createMapView(observableGameState, drawTicketsHandlerProperty, drawCardHandlerProperty);
        //BorderPane mainPane = new BorderPane(mapView, null, cardsView, handView, null);
        //window.setScene(new Scene(borderPane));
        //window.show();
        //setState(gameState);

    }

    public void setState(PublicGameState publicGameState, PlayerState playerState){
        if (! isFxApplicationThread()) throw new AssertionError();
        observableGameState.setState(publicGameState, playerState);
    }
    
    public void receiveInfo(String message){  //5 derniers messages
        if (! isFxApplicationThread()) throw new AssertionError();
        //créer une propriété receive info
        //faire une sublist ?
        // TODO IDK
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
        claimRouteHandlerProperty.set(claimRouteHandler); //TODO doit tjrs être remplie ????
    }


    //TODO difference entre 2.2.1 et 2.2.2 CHOIX INITIAL / TIRAGE DES BILLETS
    public void chooseTickets(ObservableList<SortedBag<Ticket>> ticketsOption, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler){
        if (! isFxApplicationThread()) throw new AssertionError();
        String message = String.format(StringsFr.CHOOSE_TICKETS, Constants.IN_GAME_TICKETS_COUNT, StringsFr.plural(Constants.IN_GAME_TICKETS_COUNT));
        ListView<SortedBag<Ticket>> listView = new ListView<>(ticketsOption);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        BooleanProperty booleanProperty = new SimpleBooleanProperty(listView.getSelectionModel().getSelectedItems().size() >= ticketsOption.size()-2);
        Stage stage = fenetreDeSelect(StringsFr.TICKETS_CHOICE, message, listView, booleanProperty);
        stage.show();
    }

    /**
     * Utilisée quand le joueur va tirer sa deuxième carte
     * @param drawCardHandler gestionnaire de tirage de cartes
     */

    //
    // TODO   Le gestionnaire qu'elle stocke vide toutes les propriétés contenant des gestionnaires
    // TODO   dès que le joueur aura choisi la carte à tirer
    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler){
        if (! isFxApplicationThread()) throw new AssertionError();
        if(observableGameState.canDrawCards()){
            drawCardHandlerProperty.set(drawCardHandler);
        } else {
            drawCardHandlerProperty.set(null);
        }
        drawTicketsHandlerProperty.set(null); //TODO doit les set à null?
        claimRouteHandlerProperty.set(null);
    }

    public void chooseClaimCards(ObservableList<SortedBag<Card>> initialCards, ActionHandlers.ChooseCardsHandler chooseCardsHandler){
        if (! isFxApplicationThread()) throw new AssertionError();
        //TODO possible de mettre un observable list des  cartes init?
        //observableGameState.canDrawCards()
        ListView<SortedBag<Card>> listView = new ListView<>(initialCards);
        BooleanProperty booleanProperty = new SimpleBooleanProperty(listView.getSelectionModel().getSelectedItems().size() >= 1);
        //TODO item.size() ?
        Stage stage = fenetreDeSelect(StringsFr.CARDS_CHOICE, StringsFr.CHOOSE_CARDS, listView, booleanProperty);
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
        stage.initOwner(mainPane); //TODO fenêtre princip de l'interface
        stage.initModality(Modality.WINDOW_MODAL);

        //TODO mettre tout cela dans chaque méthode !
        TextFlow textFlow = new TextFlow();
        Button button = new Button(StringsFr.CHOOSE);
        Text text = new Text(textIntro);
        vbox.getChildren().addAll(listView, textFlow, button);
        textFlow.getChildren().add(text);
        //TODO CellFactory pourquoi erreur ?

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
        public String toString(SortedBag<Card> cardSortedBag) {
            return null; //TODO Info.cardToString fait exactement ça !
        }

        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }


}
