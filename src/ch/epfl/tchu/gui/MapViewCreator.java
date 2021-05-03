package ch.epfl.tchu.gui;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

        //carte
        Pane pane = new Pane();
        pane.getStylesheets().addAll("map.css", "colors.css");

        //fond
        Node fond = new ImageView();
        fond.getStyleClass().add("ImageView");
        pane.getChildren().add(fond);


        //Route
        for (Route route: ChMap.routes()) {
            Group group = new Group();
            group.setId(route.id());
            group.getStyleClass().addAll("route", route.level().name(), route.color() == null? "NEUTRAL" : route.color().toString());
            pane.getChildren().add(group);




            //Placer les noeuds
            ReadOnlyBooleanProperty claimRouteHP = observGameState.claimableRoute(route);
            group.disableProperty().bind(gestionnaireActions.isNull().or(claimRouteHP.not())); //desactivée quand pas d'actions ou non claimable


            List<SortedBag<Card>> possibleClaimCards = route.possibleClaimCards();
            ActionHandlers.ClaimRouteHandler routeHandler = gestionnaireActions.get();
            ReadOnlyObjectProperty<PlayerId> RouteOwned = observGameState.ownedRoutesReadOnly(route);
            RouteOwned.addListener((o, oV, nV) -> group.getStyleClass().add(nV.toString()));
            group.setOnMouseClicked(e -> {
                ActionHandlers.ChooseCardsHandler chooseCardsH = chosenCards -> routeHandler.onCliamRouteHandler(route, chosenCards);
                cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
            });


            //Case
            for (int i = 0; i < route.length(); i++) {
                Group groupCase = new Group();
                groupCase.setId(route.id() + "_" + (i+1));
                group.getChildren().add(groupCase);

                //Voie
                Rectangle r = new Rectangle(RECT_LARGEUR, RECT_LONG);
                r.getStyleClass().addAll("track", "filled");
                groupCase.getChildren().add(r);

                //Wagon
                Group groupWagons = new Group();
                groupWagons.getStyleClass().add("car");
                groupCase.getChildren().add(groupWagons);
                Rectangle rect = new Rectangle(RECT_LARGEUR, RECT_LONG);
                rect.getStyleClass().add("filled");
                Circle cercle1 = new Circle(12, DIST_CERCLE, RAYON_CERCLE);
                Circle cercle2 = new Circle(24, DIST_CERCLE, RAYON_CERCLE);
                groupWagons.getChildren().addAll(rect, cercle1, cercle2);
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
