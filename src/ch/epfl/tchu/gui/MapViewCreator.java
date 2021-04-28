package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.util.List;


class MapViewCreator {
    ObservableGameState observGameState;

    //TODO type de retour ??????
    public MapViewCreator createMapView(ObservableGameState observGameState, ObjectProperty<ActionHandlers.ClaimRouteHandler> gestionnaireActions, CardChooser cardChooser){
        observGameState = observGameState;
        Image image = new Image("map.png");
        Node fond = new ImageView(); //TODO imageView du fond  comment mettre la reference ?
        Pane pane = new Pane(); // TODO ?
        Group groupRoutes = new Group(); //Mettre les classes de style associées ? Classes de

        pane.getChildren().add(fond);
        pane.getChildren().add(groupRoutes);
        pane.getStylesheets().add("map.css");
        pane.getStylesheets().add("colors.css");

        for (Route route: ChMap.routes()) {
            groupRoutes.setId(route.id());
        }

    }


    // DERNIERE LIGNE APPEL
    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler handler);
    }
}
