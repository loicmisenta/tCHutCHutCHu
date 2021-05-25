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
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import static javafx.application.Platform.isFxApplicationThread;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import java.util.List;
import java.util.Map;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Classe représente l'interface graphique d'un joueur de tCHu.
 */
public final class GraphicalPlayer {

    final private ObservableGameState observableGameState;
    final private ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerProperty;
    final private ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerProperty;
    final private ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerProperty;
    final private ObservableList<Text> observableList;
    final private Stage mainPane;
    final private int NB_MESSAGES = 5;

    /**
     * Constructeur de la classe GraphiquePlayer qui aura en parametre:
     * @param playerId l'identité du player
     * @param nomsJoueurs map du nom des joueur
     */
    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> nomsJoueurs) {
        assert isFxApplicationThread();
        this.observableGameState = new ObservableGameState(playerId, nomsJoueurs.size());
        this.mainPane = new Stage(StageStyle.UTILITY);

        drawTicketsHandlerProperty = new SimpleObjectProperty<>();
        drawCardHandlerProperty = new SimpleObjectProperty<>();
        claimRouteHandlerProperty = new SimpleObjectProperty<>();
        observableList = observableArrayList();

        Node handView = DecksViewCreator.createHandView(observableGameState);
        Node cardsView = DecksViewCreator.createCardsView(observableGameState, drawTicketsHandlerProperty, drawCardHandlerProperty);
        Node mapView = MapViewCreator.createMapView(observableGameState, claimRouteHandlerProperty, this::chooseClaimCards);
        Node infoView = InfoViewCreator.createInfoView(playerId, nomsJoueurs, observableGameState, observableList);
        BorderPane mainPaneBorder = new BorderPane(mapView, null, cardsView, handView, infoView);

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
        if (observableList.size() == NB_MESSAGES){
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
            drawCardHandlerProperty.set(card -> {
                drawTicketsHandlerProperty.set(null);
                claimRouteHandlerProperty.set(null);
                drawCardHandler.onDrawCard(card);
                drawCardHandlerProperty.set(null);
            });
        } else {
            drawCardHandlerProperty.set(null);
        }

        if(observableGameState.canDrawTickets()){
            drawTicketsHandlerProperty.set(() -> {
                drawCardHandlerProperty.set(null);
                claimRouteHandlerProperty.set(null);
                drawTicketsHandler.onDrawTickets();
                drawTicketsHandlerProperty.set(null);

            });
        }else{
            drawTicketsHandlerProperty.set(null);
        }

        claimRouteHandlerProperty.set((route, cartes) -> {
            drawTicketsHandlerProperty.set(null);
            drawCardHandlerProperty.set(null);
            claimRouteHandler.onClaimRoute(route, cartes);
            claimRouteHandlerProperty.set(null);
        });
    }

    /**
     * Utilisée quand le joueur va tirer sa deuxième carte
     * @param drawCardHandler gestionnaire de tirage de cartes
     */
    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler){
        assert isFxApplicationThread();
        drawCardHandlerProperty.set(card -> {
            drawTicketsHandlerProperty.set(null);
            claimRouteHandlerProperty.set(null);
            drawCardHandler.onDrawCard(card);
            drawCardHandlerProperty.set(null);
        });
    }



    /**
     * Utilisée quand le joueur va choisir un billet
     * @param ticketsOption bilet a choisir
     * @param chooseTicketsHandler gestionnaire de choix de billet
     */
    public void chooseTickets(SortedBag<Ticket>ticketsOption, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler){
        assert isFxApplicationThread();

        String message = String.format(StringsFr.CHOOSE_TICKETS, Constants.IN_GAME_TICKETS_COUNT, StringsFr.plural(Constants.IN_GAME_TICKETS_COUNT));
        ListView<Ticket> listView = new ListView<>(FXCollections.observableList(ticketsOption.toList()));
        MultipleSelectionModel<Ticket> listViewGetSelectModel = listView.getSelectionModel();

        listViewGetSelectModel.setSelectionMode(SelectionMode.MULTIPLE);

        TextFlow textFlow = new TextFlow();
        Button button = new Button(StringsFr.CHOOSE);
        Stage stage = fenetreDeSelect(StringsFr.TICKETS_CHOICE, message, textFlow, listView, button);

        button.disableProperty().bind(Bindings.size(listViewGetSelectModel.getSelectedItems()).lessThan(ticketsOption.size()-2));
        stage.setOnCloseRequest(Event::consume);
        button.setOnAction(e ->{
            stage.hide();
            chooseTicketsHandler.onChooseTickets(SortedBag.of(listViewGetSelectModel.getSelectedItems()));
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
        MultipleSelectionModel<SortedBag<Card>> listViewGetSelectModel = listView.getSelectionModel();
        Button button = new Button(StringsFr.CHOOSE);
        button.disableProperty().bind(listViewGetSelectModel.selectedItemProperty().isNull());
        cardChoice( chooseCardsHandler, StringsFr.CHOOSE_CARDS, button, listViewGetSelectModel, listView);

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
        MultipleSelectionModel<SortedBag<Card>> listViewGetSelectModel = listView.getSelectionModel();
        Button button = new Button(StringsFr.CHOOSE);
        cardChoice(chooseCardsHandler, StringsFr.CHOOSE_ADDITIONAL_CARDS, button, listViewGetSelectModel, listView);
    }


    private void cardChoice( ActionHandlers.ChooseCardsHandler chooseCardsHandler, String choice, Button button, MultipleSelectionModel<SortedBag<Card>> listViewGetSelectModel, ListView<SortedBag<Card>> listView){
        listView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        TextFlow textFlow = new TextFlow();
        Stage stage = fenetreDeSelect(StringsFr.CARDS_CHOICE, choice, textFlow, listView, button);

        stage.setOnCloseRequest(Event::consume);
        button.setOnAction(e ->{
            stage.hide();
            SortedBag<Card> selectedModel = listViewGetSelectModel.getSelectedItem();
            chooseCardsHandler.onChooseCards(selectedModel == null ? SortedBag.of() : selectedModel);
        });
        stage.show();

    }


    private <T> Stage fenetreDeSelect(String titre, String textAction, TextFlow textFlow, ListView<T> listView, Button button ){
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle(titre);
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(mainPane);
        stage.initModality(Modality.WINDOW_MODAL);
        borderPane.setLeft(vbox);
        Text text = new Text(textAction);
        textFlow.getChildren().add(text);
        vbox.getChildren().addAll(textFlow, listView, button);
        return stage;
    }

    private static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {
        @Override
        public String toString(SortedBag<Card> cards) {
            return Info.cardToString(cards);
        }
        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }


}
