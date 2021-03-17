package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTestTheo {

    static SortedBag<Ticket> tickets;
    public final static SortedBag<Card> cards = SortedBag.of(Card.ALL);
    public final static SortedBag<Card> cards1 = SortedBag.of();
    public final static SortedBag<Card> cards2 = SortedBag.of(2,Card.GREEN,5,Card.VIOLET);
    public final static SortedBag<Card> cards3 = SortedBag.of(2,Card.GREEN,2,Card.VIOLET);
    public final static Station cannes = new Station(0, "Cannes");
    public final static Station lausanne = new Station(1, "Lausanne");
    public final static Station geneve = new Station(2, "Geneve");
    public final static Station paris =  new Station(3, "Paris");
    public final static Station grasse = new Station(4, "Grasse");
    public final static Station bordeau =  new Station(5, "Bordeau");
    public final static Station epfl = new Station(6, "Epfl");

    public final static Route r1 = new Route("a", cannes, lausanne, 1, Route.Level.UNDERGROUND, Color.RED);
    public final static Route r2 = new Route("a", geneve, paris, 2, Route.Level.UNDERGROUND, Color.RED);
    public final static Route r3 = new Route("a", cannes, epfl, 3, Route.Level.UNDERGROUND, Color.RED);
    public final static Route r4 = new Route("a", cannes, geneve, 2, Route.Level.UNDERGROUND, Color.RED);
    public final static Route r5 = new Route("a", lausanne, geneve, 1, Route.Level.UNDERGROUND, Color.RED);
    public final static ArrayList<Route> routes = new ArrayList<Route>(List.of(r1,r2,r3,r4,r5));
    public final static ArrayList<Route> routes2 = new ArrayList<Route>(List.of(r1,r2,r3));
    public final static ArrayList<Route> routes3 = new ArrayList<Route>(List.of(r1,r4,r5));
    public final static ArrayList<Route> routes4 = new ArrayList<Route>(List.of(r1));

    public final static Trip trip1 = new Trip(cannes,lausanne,5);
    public final static Trip trip2 = new Trip(cannes,epfl,4);
    public final static Trip trip3 = new Trip(cannes,lausanne,2);
    public final static Trip trip4 = new Trip(cannes,epfl,6);
    public final static Trip trip5 = new Trip(cannes,grasse,1);
    public final static ArrayList<Trip> trips1 = new ArrayList<Trip>(List.of(trip1,trip2,trip3,trip4,trip5));
    public final static ArrayList<Trip> trips2 = new ArrayList<Trip>(List.of(trip1,trip2,trip4));
    public final static ArrayList<Trip> trips3 = new ArrayList<Trip>(List.of(trip5));

    public final static Ticket ticket1 = new Ticket(cannes,lausanne,5);
    public final static Ticket ticket2 = new Ticket(geneve,grasse,5);
    public final static Ticket ticket3 = new Ticket(trips1);
    public final static Ticket ticket4 = new Ticket(trips2);
    public final static Ticket ticket5 = new Ticket(trips3);
    public final static SortedBag<Ticket> tickets1 = SortedBag.of(ticket1);
    public final static SortedBag<Ticket> tickets2 = SortedBag.of(List.of(ticket2,ticket3));

    @Test
    void initial() {
        assertThrows(IllegalArgumentException.class, ()-> {
            PlayerState.initial(cards);
        });
        PlayerState playerState = PlayerState.initial(cards3);
        assertEquals(SortedBag.of(),playerState.tickets());
        assertEquals(cards3,playerState.cards());
        assertEquals(List.of(),playerState.routes());
    }

    @Test
    void tickets() {
        assertEquals(SortedBag.of(ticket1),new PlayerState(SortedBag.of(ticket1),cards,routes).tickets());
        assertEquals(SortedBag.of(ticket2),new PlayerState(SortedBag.of(ticket2),cards,routes).tickets());
        assertEquals(SortedBag.of(1,ticket3,1,ticket4),new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards,routes).tickets());
        assertEquals(SortedBag.of(ticket5),new PlayerState(SortedBag.of(ticket5),cards,routes).tickets());
    }

    @Test
    void withAddedTickets() {
        assertEquals(SortedBag.of(List.of(ticket1,ticket2,ticket3)),new PlayerState(SortedBag.of(ticket1),cards,routes).withAddedTickets(tickets2).tickets());
        assertEquals(SortedBag.of(1,ticket1,1,ticket2),new PlayerState(SortedBag.of(ticket2),cards,routes).withAddedTickets(tickets1).tickets());
        assertEquals(SortedBag.of(ticket3),new PlayerState(SortedBag.of(ticket3),cards,routes).tickets());
    }

    @Test
    void cards() {
        assertEquals(cards,new PlayerState(SortedBag.of(ticket1),cards,routes).cards());
        assertEquals(cards1,new PlayerState(SortedBag.of(ticket2),cards1,routes).cards());
        assertEquals(cards2,new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards2,routes).cards());
    }

    @Test
    void withAddedCard() {
        assertEquals(SortedBag.of(cards2.union(SortedBag.of(Card.BLUE))),new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards2,routes).withAddedCard(Card.BLUE).cards());
        assertNotEquals(SortedBag.of(cards.union(SortedBag.of(Card.BLUE))),new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards,routes).withAddedCard(Card.GREEN).cards());
        assertEquals(SortedBag.of(1,Card.VIOLET),new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards1,routes).withAddedCard(Card.VIOLET).cards());
        assertEquals(SortedBag.of(3,Card.GREEN,5,Card.VIOLET),new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards2,routes).withAddedCard(Card.GREEN).cards());
    }

    @Test
    void withAddedCards() {
        assertEquals(SortedBag.of(cards2.union(SortedBag.of(Card.BLUE))),new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards2,routes).withAddedCards(SortedBag.of(Card.BLUE)).cards());
        assertEquals(SortedBag.of(cards.union(SortedBag.of(2,Card.BLUE,3,Card.LOCOMOTIVE))),new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards,routes).withAddedCards(SortedBag.of(2,Card.BLUE,3,Card.LOCOMOTIVE)).cards());
        assertEquals(SortedBag.of(cards2.union(SortedBag.of(1,Card.LOCOMOTIVE,1,Card.GREEN))),new PlayerState(SortedBag.of(1,ticket3,1,ticket4),cards2,routes).withAddedCards(SortedBag.of(1,Card.LOCOMOTIVE,1,Card.GREEN)).cards());
    }

    @Test
    void canClaimRoute() {

    }

    @Test
    void possibleClaimCards() {
        //route coloré
        Route test1 = new Route("e", cannes, lausanne, 5, Route.Level.OVERGROUND, Color.RED);
        PlayerState actual1 = new PlayerState(SortedBag.of(), SortedBag.of(10, Card.RED, 2, Card.BLACK), List.of());
        List<SortedBag<Card>> expected1 =  new ArrayList<>();
        expected1.add(SortedBag.of(5, Card.RED));
        assertEquals(expected1, actual1.possibleClaimCards(test1));

        //tunnel coloré
        Route test2 = new Route("e", cannes, lausanne, 3, Route.Level.UNDERGROUND, Color.RED);
        SortedBag<Card> sb1 = SortedBag.of(3, Card.ORANGE, 2, Card.GREEN);
        PlayerState actual2 = new PlayerState(SortedBag.of(), SortedBag.of(3, Card.RED, 3, Card.LOCOMOTIVE).union(sb1), List.of());
        List<SortedBag<Card>> expected2 =  new ArrayList<>();
        expected2.add(SortedBag.of(3, Card.RED));
        expected2.add(SortedBag.of(2, Card.RED, 1, Card.LOCOMOTIVE));
        expected2.add(SortedBag.of(1, Card.RED, 2, Card.LOCOMOTIVE));
        expected2.add(SortedBag.of(3, Card.LOCOMOTIVE));
        assertEquals(expected2, actual2.possibleClaimCards(test2));

        //assert(false);
        //route neutre
        Route test3 = new Route("e", cannes, lausanne, 2, Route.Level.OVERGROUND, null);
        PlayerState actual3 = new PlayerState(SortedBag.of(), SortedBag.of(3, Card.RED, 3, Card.LOCOMOTIVE), List.of());
        List<SortedBag<Card>> expected3 =  new ArrayList<>();
        expected3.add(SortedBag.of(2, Card.RED));
        assertEquals(expected3, actual3.possibleClaimCards(test3));

        //assert(false);
        //tunnel neutre
        Route test4 = new Route("e", cannes, lausanne, 2, Route.Level.UNDERGROUND, null);
        SortedBag<Card> sb2 = SortedBag.of(2, Card.RED, 2, Card.GREEN);
        PlayerState actual4 = new PlayerState(SortedBag.of(), SortedBag.of(1, Card.LOCOMOTIVE).union(sb2), List.of());
        List<SortedBag<Card>> expected4 =  new ArrayList<>();
        expected4.add(SortedBag.of(2, Card.GREEN));
        expected4.add(SortedBag.of(2, Card.RED));
        expected4.add(SortedBag.of(1, Card.GREEN, 1, Card.LOCOMOTIVE));
        expected4.add(SortedBag.of(1, Card.RED, 1 , Card.LOCOMOTIVE));
        assertEquals(expected4, actual4.possibleClaimCards(test4));

        //assert(false);
        //cas ou il a pas assez de cartes
        assertThrows(IllegalArgumentException.class, ()-> {
            PlayerState actual5 = new PlayerState(SortedBag.of(), SortedBag.of(), List.of());
            actual5.possibleClaimCards(test1);
        });

    }

    @Test
    void possibleAdditionalCards() {
        List<SortedBag<Card>> expected = new ArrayList<>();
        expected.add(SortedBag.of(2, Card.GREEN));
        expected.add(SortedBag.of(1, Card.GREEN, 1, Card.LOCOMOTIVE));
        expected.add(SortedBag.of(2, Card.LOCOMOTIVE));

        SortedBag<Card> sortedBag1 = SortedBag.of(3, Card.GREEN, 2, Card.BLUE);
        SortedBag<Card> sortedBag2 = SortedBag.of(2, Card.LOCOMOTIVE);
        PlayerState actual = new PlayerState(SortedBag.of(), sortedBag1.union(sortedBag2), List.of());

        assertEquals(expected, actual.possibleAdditionalCards(2, SortedBag.of(Card.GREEN), SortedBag.of(3, Card.WHITE)));


        assertThrows(IllegalArgumentException.class, ()->{
            actual.possibleAdditionalCards(0, SortedBag.of(Card.GREEN), SortedBag.of(3, Card.WHITE));
        });
        assertThrows(IllegalArgumentException.class, ()->{
            actual.possibleAdditionalCards(4, SortedBag.of(Card.GREEN), SortedBag.of(3, Card.WHITE));
        });

        assertThrows(IllegalArgumentException.class, ()->{
            actual.possibleAdditionalCards(2, SortedBag.of(), SortedBag.of(3, Card.WHITE));
        });
        assertThrows(IllegalArgumentException.class, ()->{
            actual.possibleAdditionalCards(2, sortedBag1.union(sortedBag2), SortedBag.of(3, Card.WHITE));
        });

        assertThrows(IllegalArgumentException.class, ()->{
            actual.possibleAdditionalCards(2, SortedBag.of(1, Card.GREEN), SortedBag.of(2, Card.WHITE));
        });

        List<SortedBag<Card>> expected2 = new ArrayList<>();
        expected2.add(SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE));
        expected2.add(SortedBag.of(2, Card.LOCOMOTIVE, 1, Card.BLUE));

        SortedBag<Card> sortedBag3 = SortedBag.of(3, Card.RED, 5, Card.BLUE);
        SortedBag<Card> sortedBag5 = SortedBag.of(2, Card.LOCOMOTIVE, 2, Card.BLACK);
        PlayerState actual2 = new PlayerState(SortedBag.of(), sortedBag3.union(sortedBag5), List.of());

        assertEquals(expected2, actual2.possibleAdditionalCards(3, SortedBag.of(3, Card.BLUE), SortedBag.of(3, Card.WHITE)));


    }

    @Test
    void withClaimedRoute() {
    }

    @Test
    void ticketPoints() {
        assertEquals(5,new PlayerState(SortedBag.of(ticket1),cards,routes).ticketPoints());
        assertEquals(-5,new PlayerState(SortedBag.of(ticket2),cards,routes).ticketPoints());
        assertEquals(6,new PlayerState(SortedBag.of(ticket3),cards,routes).ticketPoints());
        assertEquals(6,new PlayerState(SortedBag.of(ticket4),cards,routes2).ticketPoints());
        assertEquals(-1,new PlayerState(SortedBag.of(ticket5),cards,routes).ticketPoints());
    }

    @Test
    void finalPoints() {
        PlayerState p1 = new PlayerState(SortedBag.of(ticket1),cards,routes);
        assertEquals(15,p1.finalPoints());  //5
        assertEquals(2,new PlayerState(SortedBag.of(ticket2),cards,routes2).finalPoints()); //-5 + 7
        assertEquals(9,new PlayerState(SortedBag.of(ticket3),cards,routes3).finalPoints()); //5 + 4
        assertEquals(16,new PlayerState(SortedBag.of(ticket4),cards,routes).finalPoints());
        assertEquals(0,new PlayerState(SortedBag.of(ticket5),cards,routes4).finalPoints()); //-1 +1
    }





}