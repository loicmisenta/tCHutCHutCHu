package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InfoTest {
    Info joueur = new Info("le boss");
    @Test
    void cardName(){
        var ExpectedValue = "bleues";
        assertEquals(ExpectedValue, joueur.cardName(Card.BLUE, 2));
    }
    @Test
    void draw(){
        var ExpectedValue = "\n" +
                "Patrick et Henry sont ex æqo avec 2 points !\n";
        assertEquals(ExpectedValue, joueur.draw(List.of("Patrick", "Henry"), 2));
    }
    @Test
    void willPlayFirst(){
        var ExpectedValue = "le boss jouera en premier.\n\n";
        assertEquals(ExpectedValue, joueur.willPlayFirst());
    }
    @Test
    void keptTickets(){
        var ExpectedValue = "le boss a gardé 3 billets.\n";
        assertEquals(ExpectedValue, joueur.keptTickets(3));
    }
    @Test
    void canPlay(){
        var ExpectedValue = "\n" +
                "C'est à le boss de jouer.\n";
        assertEquals(ExpectedValue, joueur.canPlay());
    }
    @Test
    void drewTickets(){
        var ExpectedValue = "le boss a tiré 2 billets...\n";
        assertEquals(ExpectedValue, joueur.drewTickets(2));
    }
    @Test
    void drewBlindCard(){
        var ExpectedValue = "le boss a tiré une carte de la pioche.\n";
        assertEquals(ExpectedValue, joueur.drewBlindCard());
    }
    @Test
    void drewVisibleCard(){
        var ExpectedValue = "le boss a tiré une carte bleue visible.\n";
        assertEquals(ExpectedValue, joueur.drewVisibleCard(Card.BLUE));
    }
    @Test
    void claimedRoute(){
        SortedBag<Card> drawnCards = SortedBag.of(List.of(Card.BLUE, Card.YELLOW));
        var ExpectedValue = "le boss a pris possession de la route AT1 – STG au moyen de 1 bleue et 1 jaune.\n";
        assertEquals(ExpectedValue, joueur.claimedRoute(new Route("AT1_STG_1", new Station(1, "AT1"),
                new Station(1, "STG"), 4, Route.Level.UNDERGROUND, null), drawnCards));
    }
    @Test
    void attemptsTunnelClaim(){
        SortedBag<Card> drawnCards = SortedBag.of(List.of(Card.BLUE, Card.YELLOW));
        var ExpectedValue = "le boss tente de s'emparer du tunnel AT1 – STG au moyen de 1 bleue et 1 jaune !\n";
        assertEquals(ExpectedValue, joueur.attemptsTunnelClaim(new Route("AT1_STG_1", new Station(1, "AT1"),
                new Station(1, "STG"), 4, Route.Level.UNDERGROUND, null), drawnCards));
    }
    @Test
    void drewAdditionalCards(){
        SortedBag<Card> drawnCards = SortedBag.of(List.of(Card.BLUE, Card.BLUE, Card.YELLOW, Card.BLUE));
        var ExpectedValue = "Les cartes supplémentaires sont 3 bleues et 1 jaune. " +
                "Elles impliquent un coût additionnel de 3 cartes.\n";
        assertEquals(ExpectedValue, joueur.drewAdditionalCards(drawnCards, 3));
    }
    @Test
    void didNotClaimRoute(){
        SortedBag<Card> drawnCards = SortedBag.of(List.of(Card.BLUE, Card.BLUE, Card.YELLOW, Card.BLUE));
        var ExpectedValue = "le boss n'a pas pu (ou voulu) s'emparer de la route AT1 – STG.\n";
        assertEquals(ExpectedValue, joueur.didNotClaimRoute(new Route("AT1_STG_1", new Station(1, "AT1"),
                new Station(1, "STG"), 4, Route.Level.UNDERGROUND, null)));
    }
    @Test
    void lastTurnBegins(){
        SortedBag<Card> drawnCards = SortedBag.of(List.of(Card.BLUE, Card.BLUE, Card.YELLOW, Card.BLUE));
        var ExpectedValue = "\n" + "le boss n'a plus que 2 wagons, le dernier tour commence !\n";
        assertEquals(ExpectedValue, joueur.lastTurnBegins(2));
    }
    @Test
    void getsLongestTrailBonus(){
        SortedBag<Card> drawnCards = SortedBag.of(List.of(Card.BLUE, Card.BLUE, Card.YELLOW, Card.BLUE));
        var ExpectedValue = "\n" + "le boss reçoit un bonus de 10 points pour le plus long trajet (Baden – Berne).\n";
        assertEquals(ExpectedValue, joueur.getsLongestTrailBonus(new Trail(ChMap.stations().get(0), ChMap.stations().get(3),
                7, List.of(ChMap.routes().get(0), ChMap.routes().get(9)))));
    }
    @Test
    void won(){
        var ExpectedValue = "\n" + "le boss remporte la victoire avec 2 points, contre 5 points !\n";
        assertEquals(ExpectedValue, joueur.won(2, 5));
    }



}
