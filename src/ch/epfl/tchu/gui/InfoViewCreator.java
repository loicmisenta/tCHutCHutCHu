package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Map;


/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Cette classe (non instanciable) représente l'interface graphique de la vue des informations
 */
//PAS PUBLIC JE CROIS (POUR TEST)
public final class InfoViewCreator {
    static VBox vbox;
    private static final int CERCLE_RAYON = 5;

    private InfoViewCreator(){}

    /**
     * permettant de créer la vue des informations.
     * @param playerId l'identité du joueur auquel l'interface correspond
     * @param playerIdStringMap la table associative des noms des joueurs
     * @param obsGS l'état de jeu observable,
     * @param text une liste (observable) contenant les informations sur le déroulement de la partie, sous la forme d'instances de Text.
     * @return un Node de la vue des informations
     */
    public static Node createInfoView(PlayerId playerId, Map<PlayerId, String> playerIdStringMap, ObservableGameState obsGS, ObservableList<Text> text){
        vbox = new VBox();
        vbox.getStylesheets().addAll("info.css", "colors.css");

        VBox vboxPlayerStats = new VBox();
        vboxPlayerStats.setId("player-stats");
        vbox.getChildren().add(vboxPlayerStats);

        //statistique joueur
        createPlayerInt(playerId, vboxPlayerStats, playerIdStringMap, obsGS);
        for (PlayerId id: PlayerId.values()) {
            if (id == playerId) continue;
            createPlayerInt(id, vboxPlayerStats, playerIdStringMap, obsGS);
        }
        Separator separator = new Separator(Orientation.HORIZONTAL);
        vbox.getChildren().add(separator);


        //message
        TextFlow textFlow = new TextFlow();
        textFlow.setId("game-info");
        vbox.getChildren().add(textFlow);
        List <Text> listText = text.size() > 5? text.subList(text.size()-6, text.size()-2) : text;


        for (Text string: listText) {
            Text textMessage = new Text(string.getText());
            textFlow.getChildren().addAll(textMessage);
        }
        Bindings.bindContent(textFlow.getChildren(), text);

        return vbox;
    }

    private static void createPlayerInt(PlayerId id, VBox vboxPlayerStats, Map<PlayerId, String> playerIdStringMap, ObservableGameState obsGS){
        TextFlow statPlayer = new TextFlow();
        vboxPlayerStats.getChildren().add(statPlayer);

        Circle circle = new Circle(CERCLE_RAYON);
        statPlayer.getStyleClass().add(id.name());
        Text text1 = new Text();
        circle.getStyleClass().add("filled"); //TODO affiche pas la couleur
        statPlayer.getChildren().addAll(circle, text1);
        text1.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS,
                playerIdStringMap.get(id),
                obsGS.ownedTicketsReadOnly(id),
                obsGS.ownedCardReadOnly(id),
                obsGS.ownedCarsReadOnly(id),
                obsGS.ownedConstructPointsReadOnly(id)));

    }
}
