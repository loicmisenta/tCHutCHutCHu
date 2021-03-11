package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InfoTest {

    @Test
    void cardNameReturnsCorrectString(){
        assertEquals("2 noires", Info.cardName(Card.BLACK, 2));
        assertEquals("1 locomotive", Info.cardName(Card.LOCOMOTIVE, 1));
        assertEquals("0 locomotives", Info.cardName(Card.LOCOMOTIVE, 0));
    }
    /**
    @Test
    void drawReturnsCorrectString(){
        List<String> names = new ArrayList<>();
        names.add(PlayerId.PLAYER_1.name());
        names.add(PlayerId.PLAYER_2.name());
        assertEquals("\nPLAYER_1 et PLAYER_2 sont ex æqo avec 50 points !\n", Info.draw(names, 50));
    }
     */

    @Test
    void willPlayFirst(){
        Info player = new Info("Jean");
        assertEquals("Jean jouera en premier.\n\n", player.willPlayFirst());
    }

    @Test
    void keptTickets(){
        Info player = new Info("Jean");
        assertEquals("Jean a gardé 1 billet.\n", player.keptTickets(1));
        assertEquals("Jean a gardé 2 billets.\n", player.keptTickets(2));
        assertEquals("Jean a gardé 0 billets.\n", player.keptTickets(0));
    }

    @Test
    void canPlay(){
        Info player = new Info("Jean");
        assertEquals("\nC'est à Jean de jouer.\n", player.canPlay());
    }

    @Test
    void drewTickets(){
        Info player = new Info("Jean");
        assertEquals("Jean a tiré 0 billets...\n", player.drewTickets(0));
        assertEquals("Jean a tiré -1 billet...\n", player.drewTickets(-1));
        assertEquals("Jean a tiré 2 billets...\n", player.drewTickets(2));
        assertEquals("Jean a tiré 1 billet...\n", player.drewTickets(1));
    }

    @Test
    void drewBlindCard(){
        Info player = new Info("Jean");
        assertEquals("Jean a tiré une carte de la pioche.\n", player.drewBlindCard());
    }

    @Test
    void drewVisibleCard(){
        Info player = new Info("Jean");
        assertEquals("Jean a tiré une carte rouge visible.\n", player.drewVisibleCard(Card.RED));
    }

    @Test
    void claimedRoute(){
        Info player = new Info("Jean");
        assertEquals("Jean a pris possession de la route Genève – Lausanne au moyen de 4 bleues.\n",
                player.claimedRoute(ChMap.routes().get(46), SortedBag.of(4, Card.BLUE)));
        assertEquals("Jean a pris possession de la route Genève – Yverdon au moyen de 4 bleues et 2 locomotives.\n",
                player.claimedRoute(ChMap.routes().get(48), SortedBag.of(4, Card.BLUE, 2, Card.LOCOMOTIVE)));
    }

    @Test
    void attemptsTunnelClaim(){
        Info player = new Info("Jean");
        assertEquals("Jean tente de s'emparer du tunnel Genève – Lausanne au moyen de 4 bleues !\n",
                player.attemptsTunnelClaim(ChMap.routes().get(46), SortedBag.of(4, Card.BLUE)));
        assertEquals("Jean tente de s'emparer du tunnel Autriche – Saint-Gall au moyen de 3 noires et 1 locomotive !\n",
                player.attemptsTunnelClaim(ChMap.routes().get(0), SortedBag.of(1, Card.LOCOMOTIVE, 3, Card.BLACK)));
        assertEquals("Jean tente de s'emparer du tunnel Autriche – Vaduz au moyen de 1 rouge !\n",
                player.attemptsTunnelClaim(ChMap.routes().get(1), SortedBag.of(1, Card.RED)));
    }

    @Test
    void drewAdditionalCards(){
        Info player = new Info("Jean");
        assertEquals("Les cartes supplémentaires sont 2 bleues et 1 locomotive. " + "Elles impliquent un coût additionnel de 3 cartes.\n",
                player.drewAdditionalCards(SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE), 3));

        assertEquals("Les cartes supplémentaires sont 2 bleues et 1 locomotive. " + "Elles impliquent un coût additionnel de 1 carte.\n",
                player.drewAdditionalCards(SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE), 1));
        assertEquals("Les cartes supplémentaires sont 2 bleues et 1 locomotive. " + "Elles n'impliquent aucun coût additionnel.\n",
                player.drewAdditionalCards(SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE), 0));
    }

    @Test
    void didNotClaimRoute(){
        Info player = new Info("Jean");
        assertEquals("Jean n'a pas pu (ou voulu) s'emparer de la route Genève – Lausanne.\n", player.didNotClaimRoute(ChMap.routes().get(46)));
    }

    @Test
    void lastTurnBegins(){
        Info player = new Info("Jean");
        assertEquals("\nJean n'a plus que 2 wagons, le dernier tour commence !\n", player.lastTurnBegins(2));
        assertEquals("\nJean n'a plus que 0 wagons, le dernier tour commence !\n", player.lastTurnBegins(0));
        assertEquals("\nJean n'a plus que 1 wagon, le dernier tour commence !\n", player.lastTurnBegins(1));
    }
    /**
    @Test
    void getsLongestTrailBonus(){
        Info player = new Info("Jean");
        List<Route> routes = new ArrayList<>();
        routes.add(ChMap.routes().get(47));
        routes.add(ChMap.routes().get(44));
        routes.add(ChMap.routes().get(13));
        assertEquals("\nJean reçoit un bonus de 10 points pour le plus long trajet (Genève – Lausanne).\n",
                player.getsLongestTrailBonus(new Trail(ChMap.stations().get(10), ChMap.stations().get(13), routes)));
    }*/

    @Test
    void won(){
        Info player1 = new Info("Jean");
        Info player2 = new Info("Isabelle");

        assertEquals("\nJean remporte la victoire avec 1 point, contre 0 points !\n" , player1.won(1,0));
    }
    /**
    @Test
    void setOfCards(){
        Info player = new Info("Jean");
        SortedBag<Card> s1 = SortedBag.of(1, Card.GREEN, 2, Card.ORANGE);
        SortedBag<Card> s2 = SortedBag.of(3, Card.LOCOMOTIVE, 1, Card.BLUE);
        SortedBag<Card> s = s1.union(s2);
        assertEquals("1 bleue, 1 verte, 2 oranges et 3 locomotives", player.listToStringWithSeparator(player.setOfCards(s)));
    }
    */


}
