package ch.epfl.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class GraphicalPlayerTest extends Application{

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        Map<PlayerId, String> playerNames =
                Map.of(PLAYER_1, "Ada", PLAYER_2, "Charles");
        GraphicalPlayer p = new GraphicalPlayer(PLAYER_1, playerNames);
        setState(p);

        ActionHandlers.DrawTicketsHandler drawTicketsH =
                () -> {
            p.receiveInfo("Je tire des billets !");
            /*
            p.chooseTickets(SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)), SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)), new ActionHandlers.ChooseTicketsHandler() {
                @Override
                public void onChooseTickets(SortedBag<Ticket> tickets) {
                    System.out.println("choisi ticket");
                }
            });

             */
                };
        ActionHandlers.DrawCardHandler drawCardH =
                s -> {
            p.receiveInfo(String.format("Je tire une carte de %s !", s));
            p.drawCard(new ActionHandlers.DrawCardHandler() {
                @Override
                public void onDrawCard(int emplacement) {
                    System.out.println("choisi draw card a " + emplacement);
                }
            });

                };
        ActionHandlers.ClaimRouteHandler claimRouteH =
                (r, cs) -> {
                    String rn = r.station1() + " - " + r.station2();
                    p.receiveInfo(String.format("Je m'empare de %s avec %s", rn, cs));
                    /*
                    p.chooseClaimCards(List.of(SortedBag.of( 3, Card.RED)), new ActionHandlers.ChooseCardsHandler() {
                        @Override
                        public void onChooseCards(SortedBag<Card> cartes) {
                            System.out.println("choisi carte");
                        }
                    });

                     */


                };

        p.startTurn(drawTicketsH, drawCardH, claimRouteH);
    }

    private void setState(GraphicalPlayer graphicalPlayer) {
        PlayerState p1State =
                new PlayerState(SortedBag.of(ChMap.tickets(2).subList(0, 3)),
                        SortedBag.of(1, Card.WHITE, 3, Card.RED),
                        ChMap.routes().subList(0, 3));

        PublicPlayerState p2State =
                new PublicPlayerState(0, 0, ChMap.routes().subList(3, 6));

        Map<PlayerId, PublicPlayerState> pubPlayerStates =
                Map.of(PLAYER_1, p1State, PLAYER_2, p2State);
        PublicCardState cardState =
                new PublicCardState(Card.ALL.subList(0, 5), 110 - 2 * 4 - 5, 0);
        PublicGameState publicGameState =
                new PublicGameState(36, cardState, PLAYER_1, pubPlayerStates, null);
        graphicalPlayer.setState(publicGameState, p1State);
    }

    private static void claimRoute(Route route, SortedBag<Card> cards) {
        System.out.printf("Prise de possession d'une route : %s - %s %s%n",
                route.station1(), route.station2(), cards);
    }

    private static void chooseCards(List<SortedBag<Card>> options,
                                    ActionHandlers.ChooseCardsHandler chooser) {
        chooser.onChooseCards(options.get(0));
    }

    private static void drawTickets() {
        System.out.println("Tirage de billets !");
    }

    private static void drawCard(int slot) {
        System.out.printf("Tirage de cartes (emplacement %s)!\n", slot);
    }


}
