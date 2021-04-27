package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;

import javax.swing.text.html.ImageView;
import java.util.List;

class MapViewCreator {

    //TODO type de retour ??????
    public MapViewCreator createMapView(ObservableGameState etatJeuObs, ObjectProperty<ActionHandlers.ClaimRouteHandler> gestionnaireActions, CardChooser cardChooser){
        ImageView fond = new ImageView(); //TODO imageView du fond
        Pane pane = new Pane(); // TODO

    }


    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler handler);
    }
}
