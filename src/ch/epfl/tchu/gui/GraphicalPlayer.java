package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class GraphicalPlayer {

    PlayerId playerId;
    Map<PlayerId, String> nomsJoueurs;
    ObservableGameState observableGameState;
    ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerProperty;
    ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerProperty;
    ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerProperty;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> nomsJoueurs){
        this.playerId = playerId;
        this.nomsJoueurs = nomsJoueurs;
        this.observableGameState = new ObservableGameState(playerId);
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);

    }

    public void setState(PublicGameState publicGameState, PlayerState playerState){
        observableGameState.setState(publicGameState, playerState);
    }
    
    public void receiveInfo(String message){
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
     *
     *
     */

    // TODO   Le gestionnaire qu'elle stocke vide toutes les propriétés contenant des gestionnaires
    // TODO   dès que le joueur aura choisi la carte à tirer
    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler){

        if(observableGameState.canDrawCards()){
            drawCardHandlerProperty.set(drawCardHandler);
        } else {
            drawCardHandlerProperty.set(null);
        }
    }

    public void chooseClaimCards(List<SortedBag<Card>> initialCards, ActionHandlers.ChooseCardsHandler chooseCardsHandler){


    }

    public void chooseAdditionalCards(SortedBag<Card> cartesAddit, ActionHandlers.ChooseCardsHandler chooseCardsHandler){


        //Appeler le gestionnaire de choix avec le choix du joueur en argument.
    }
}
