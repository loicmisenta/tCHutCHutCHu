package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.List;


public class MapViewCreator {

    public static Pane createMapView(ObservableGameState observGameState, ObjectProperty<ActionHandlers.ClaimRouteHandler> gestionnaireActions, CardChooser cardChooser){
        int RECT_LARGEUR = 36;
        int RECT_LONG = 12;
        int RAYON_CERCLE = 3;
        int DIST_CERCLE = 6;

        Pane pane = new Pane();
        Node fond = new ImageView();
        fond.getStyleClass().add("ImageView");
        Group groupRoutes = new Group(); //Mettre les classes de style associées ? Classes de


        pane.getChildren().add(fond);
        pane.getChildren().add(groupRoutes);
        pane.getStylesheets().add("map.css");
        pane.getStylesheets().add("colors.css");

        //TODO Creer des Routes et les remlir tout de suite avec des cases ou non

        //Route
        for (Route route: ChMap.routes()) {
            Group group = new Group();
            group.setId(route.id());
            String color;
            if (route.color() == null) {
                color = "NEUTRAL";
            }else {
                color = route.color().toString();
            }
            group.getStyleClass().add(color);
            group.getStyleClass().add("route");
            group.getStyleClass().add(route.level().name());
            pane.getChildren().add(group);


            /*
            //TODO ACTIONS ?
            ReadOnlyBooleanProperty claimRouteHP = observGameState.claimableRoute(route);
            group.disableProperty().bind(gestionnaireActions.isNull().or(gameState.claimable(route).not()));
            List<SortedBag<Card>> possibleClaimCards =route.possibleClaimCards();
            ActionHandlers.ClaimRouteHandler claimRouteH = ;
            ActionHandlers.ChooseCardsHandler chooseCardsH = chosenCards -> claimRouteH.onClaimRoute(route, chosenCards);
            cardChooser.chooseCards(possibleClaimCards, chooseCardsH);

            group.setOnMouseClicked(e-> {
                if(possibleClaimCards.size() != 0){
                    // faire set la route à Claimed ?
                }
            });


             */


            //Case
            for (int i = 0; i < route.length(); i++) {
                Group groupCase = new Group();
                groupCase.setId(route.id());

                //Voie
                Rectangle r = new Rectangle(RECT_LARGEUR, RECT_LONG);
                r.getStyleClass().addAll("track", "filled");
                group.getChildren().add(r);

                //Wagon
                Group groupWagons = new Group();
                groupWagons.getStyleClass().add("car");
                group.getChildren().add(groupWagons);
                Rectangle rect = new Rectangle(RECT_LARGEUR, RECT_LONG);
                rect.getStyleClass().add("filled");
                Circle cercle1 = new Circle(12, DIST_CERCLE, RAYON_CERCLE);
                Circle cercle2 = new Circle(24, DIST_CERCLE, RAYON_CERCLE);
                groupWagons.getChildren().add(rect);
                groupWagons.getChildren().add(cercle1);
                groupWagons.getChildren().add(cercle2);

            }

        }



        return pane;
    }


    // DERNIERE LIGNE APPEL
    @FunctionalInterface
    public interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler handler);
    }
}
