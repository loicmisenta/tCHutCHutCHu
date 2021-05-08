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


//PAS PUBLIC JE CROIS (POUR TEST)
public class InfoViewCreator {
    //TODO instances de text
    static VBox vbox;

    private static final int CERCLE_RAYON = 5;

    //TODO pourquoi on utilise pas playerId
    public static Node createInfoView(PlayerId playerId, Map<PlayerId, String> playerIdStringMap, ObservableGameState obsGS, List<Text> text){
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

        return vbox;
    }

    private static void createPlayerInt(PlayerId id, VBox vboxPlayerStats, Map<PlayerId, String> playerIdStringMap, ObservableGameState obsGS){
        TextFlow statPlayer = new TextFlow();
        statPlayer.setId(id.name());
        vboxPlayerStats.getChildren().add(statPlayer);

        Circle circle = new Circle(CERCLE_RAYON);
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
