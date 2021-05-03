package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Map;

class InfoViewCreator {
    //TODO instances de text

    private final int CERCLE_RAYON = 5;
    /**
     * Appelée avant le début de la partie
     * Va être liée au graphe de scène
     * @param playerId
     * @param playerIdStringMap
     * @param obsGS
     * @param text
     * @return
     */
    public Node createInfoview(PlayerId playerId, Map<PlayerId, String> playerIdStringMap, ObservableGameState obsGS, List<String> text){
        VBox vbox = new VBox();
        vbox.getStylesheets().addAll("info.css", "colors.css");

        Separator separator = new Separator();//TODO autre chose?
        vbox.getChildren().add(separator);

        //message
        TextFlow textFlow = new TextFlow();
        textFlow.setId("game-info");
        vbox.getChildren().add(textFlow);


        //TODO afficher 4 messages maxà chaque fois???????
        for (String string: text) {
            Text textMessage = new Text(string);
            textFlow.getChildren().addAll(textMessage);
        }
        //TODO ne possede aucun fils?


        VBox vboxPlayerStats = new VBox();
        vboxPlayerStats.setId("player-stats");
        vbox.getChildren().add(vboxPlayerStats);

        //statistique joueur
        for (PlayerId id: PlayerId.values()) {
            TextFlow statistiquesJoueur = new TextFlow();
            statistiquesJoueur.setId(String.valueOf(id));
            vboxPlayerStats.getChildren().add(statistiquesJoueur);

            Circle circle = new Circle(CERCLE_RAYON);
            circle.getStyleClass().add("filled");
            Text text1 = new Text();
            text1.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS, obsGS.ownedTicketsReadOnly(id), obsGS.ownedCardReadOnly(id), obsGS.ownedCarsReadOnly(id), obsGS.ownedConstructPointsReadOnly(id))); //TODO doit le bind à une Valeur observée !!!
            statistiquesJoueur.getChildren().addAll(circle, text1);
        }

        return vbox;
    }
}
