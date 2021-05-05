package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.List;
import java.util.Map;

public class GraphicalPlayer {

    PlayerId playerId;
    Map<PlayerId, String> nomsJoueurs;
    ObservableGameState observableGameState;
    ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerProperty;
    ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerProperty;
    ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerProperty;
    final Window window;
    
    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> nomsJoueurs){
        this.playerId = playerId;
        this.nomsJoueurs = nomsJoueurs;
        this.observableGameState = new ObservableGameState(playerId);
        this.window = new Stage(StageStyle.UTILITY);
        BorderPane borderPane = new BorderPane();


        //TODO graphe de scene ?????
        //BorderPane mainPane = new BorderPane(mapView, null, cardsView, handView, null);
        //window.setScene(new Scene(borderPane));
        //window.show();

        //setState(gameState);

    }

    public void setState(PublicGameState publicGameState, PlayerState playerState){
        observableGameState.setState(publicGameState, playerState);
    }
    
    public void receiveInfo(String message){  //5 derniers messages
        //TODO IDK
    }


    public void startTurn(ActionHandlers.DrawTicketsHandler drawTicketsHandler, ActionHandlers.DrawCardHandler drawCardHandler, ActionHandlers.ClaimRouteHandler claimRouteHandler){
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

    public void chooseTickets(SortedBag<Ticket> ticketsOption, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler){

    }

    /**
     * Utilisée quand le joueur va tirer sa deuxième carte
     * @param drawCardHandler gestionnaire de tirage de cartes
     */

    // .show()
    // TODO   Le gestionnaire qu'elle stocke vide toutes les propriétés contenant des gestionnaires
    // TODO   dès que le joueur aura choisi la carte à tirer
    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler){
        if(observableGameState.canDrawCards()){
            drawCardHandlerProperty.set(drawCardHandler);
        } else {
            drawCardHandlerProperty.set(null);
        }
        drawTicketsHandlerProperty.set(null); //TODO doit les set à null?
        claimRouteHandlerProperty.set(null);
    }

    public void chooseClaimCards(List<SortedBag<Card>> initialCards, ActionHandlers.ChooseCardsHandler chooseCardsHandler){
        //observableGameState.canDrawCards()
        Stage stage = fenetreDeSelect("");

    }

    public void chooseAdditionalCards(SortedBag<Card> cartesAddit, ActionHandlers.ChooseCardsHandler chooseCardsHandler){
        //Appeler le gestionnaire de choix avec le choix du joueur en argument.
    }

    private  Stage fenetreDeSelect(String titre, String textIntro, ListView<SortedBag<T>> listView){
        Stage stage = new Stage(StageStyle.UTILITY);
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().add("chooser.css");
        VBox vbox = new VBox();
        stage.initOwner(window); //TODO fenêtre princip de l'interface
        stage.initModality(Modality.WINDOW_MODAL);


        TextFlow textFlow = new TextFlow();
        vbox.getChildren().add(textFlow);
        Text text = new Text(text);  // TODO remplir avec ? ? ?

        vbox.getChildren().addAll(text);

        ListView<> list; // TODO init with what ?

        /*
        A mon avis vous avez avantage a mettre le code commun dans une méthode privée,
        et appeler cette méthode-là depuis celles qui ouvrent les différentes fenêtres.
        Cette méthode prend en argument tout ce qui varie, p.ex. le titre, le texte d’introduction, la ListView, etc.

         */


        return stage;
    }
}
