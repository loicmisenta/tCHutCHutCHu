package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;

import javax.swing.text.html.ImageView;
import java.util.List;


class MapViewCreator {
    ObservableGameState observGameState;

    //TODO type de retour ??????
    public MapViewCreator createMapView(ObservableGameState observGameState, ObjectProperty<ActionHandlers.ClaimRouteHandler> gestionnaireActions, CardChooser cardChooser){
        observGameState = observGameState;
        ImageView fond = new ImageView(); //TODO imageView du fond  comment mettre la reference ?
        Pane pane = new Pane(); // TODO ?

    }


    // DERNIERE LIGNE APPEL
    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler handler);
    }
}
