package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static ch.epfl.tchu.SortedBag.of;

public final class TestConstant {

    public static final Random rng= new Random();
    static SortedBag<Ticket> tickets;
    public final static SortedBag<Card> cards = SortedBag.of(Card.ALL);
    public final static SortedBag<Card> cards1 = SortedBag.of();
    public final static SortedBag<Card> cards2 = SortedBag.of(2,Card.GREEN,5,Card.VIOLET);
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
    public final static Route rLength6 = new Route("a", lausanne, geneve, 6, Route.Level.UNDERGROUND, Color.RED);
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
    public final static SortedBag<Ticket> tickets2 = SortedBag.of(List.of(ticket2,ticket3, ticket4, ticket5));


    public static final List<Card> faceUpCards = Card.ALL.subList(0, 5);
    public static final List<Card> faceUpCards1 = Card.ALL.subList(2, 7);
    public static final List<Card> faceUpCards2 = Card.ALL.subList(3, 8);
    public static final PublicCardState publicCardState = new PublicCardState(faceUpCards, 2, 3);
    public static final PublicCardState publicCardState1 = new PublicCardState(faceUpCards1, 0, 3);
    public static final PublicCardState publicCardState2 = new PublicCardState(faceUpCards2, 2, 0);
    public static final PublicCardState publicCardState3 = new PublicCardState(faceUpCards2, 0, 0);
    public static final PublicCardState publicCardState4 = new PublicCardState(faceUpCards2, 6, 4);


    public static final CardState CardState = new CardState(faceUpCards, new Deck(Constants.ALL_CARDS.toList()), SortedBag.of());
    public static final CardState CardState1EmptyDiscard = new CardState(faceUpCards1, new Deck(Constants.ALL_CARDS.toList().subList(3,6)), SortedBag.of());
    public static final CardState CardState2 = new CardState(faceUpCards2, new Deck(Constants.ALL_CARDS.toList().subList(23,32)), SortedBag.of());
    public static final CardState CardState3 = new CardState(faceUpCards2, new Deck(Constants.ALL_CARDS.toList().subList(40,41)), SortedBag.of(Constants.ALL_CARDS.toList().subList(10,18)));
    public static final CardState CardState4 = new CardState(faceUpCards2, new Deck(Constants.ALL_CARDS.toList().subList(0,28)), SortedBag.of());
    public static final CardState CardStateEmptyDeck = new CardState(faceUpCards, new Deck(List.of()), Constants.ALL_CARDS);




    public static final Deck<Card> testcards = Deck.of(cards, new Random());

    //liste vide
    public static final Deck<Card> testcards2 = Deck.of(cards2, new Random());

    //liste partiellement remplie
    public static final SortedBag<Card> cards3 = of(1,Card.BLUE,2,Card.GREEN);
    public static final Deck<Card> testcards3 = Deck.of(cards3, new Random());

    //liste 1 elem
    public static final SortedBag<Card> cards4 = of(1,Card.LOCOMOTIVE);
    public static final Deck<Card> testcards4 = Deck.of(cards4, new Random());

    //liste 2 carte
    public static final SortedBag<Card> cards5 = of(1,Card.BLUE,1,Card.GREEN);
    public static final Deck<Card> testcards5 = Deck.of(cards5, new Random());


    public static final PlayerId player1 = PlayerId.PLAYER_1;
    public static final PlayerId player2 = PlayerId.PLAYER_2;

    public static final PublicPlayerState publicPlayerState= new PublicPlayerState(10, 13, List.of(TestConstant.r1,TestConstant.r2));
    public static final PublicPlayerState publicPlayerState1= new PublicPlayerState(10, 5,TestConstant.routes);
    public static final PublicPlayerState publicPlayerState2= new PublicPlayerState(10, 5, routes2);
    public static final PublicPlayerState publicPlayerState3 = new PublicPlayerState(10, 5, List.of());

    public static final PlayerState PlayerState = new PlayerState(tickets1, cards, List.of(TestConstant.r1,TestConstant.r2));
    public static final PlayerState PlayerState1 = new PlayerState(tickets1, cards2,TestConstant.routes);
    public static final PlayerState PlayerState2 = new PlayerState(tickets2, cards, routes2);
    public static final PlayerState PlayerState3 = new PlayerState(tickets2, cards2,List.of());
    public static final PlayerState PlayerStateWithoutTicket = new PlayerState(SortedBag.of(), cards, List.of(TestConstant.r1,TestConstant.r2));
    public static final PlayerState PlayerState1carRemaining = new PlayerState(tickets2, cards, List.of(rLength6,rLength6,rLength6,rLength6,rLength6,rLength6,r3));

    public static HashMap<PlayerId, PublicPlayerState> mapsPlayerId_PublicPlayerState(){
        HashMap<PlayerId, PublicPlayerState> mapsPlayerId_PublicPlayerState = new HashMap<>();
        mapsPlayerId_PublicPlayerState.put(player1, publicPlayerState1);
        mapsPlayerId_PublicPlayerState.put(player2, publicPlayerState2);
        return mapsPlayerId_PublicPlayerState;
    }

    public static HashMap<PlayerId, PublicPlayerState> mapsPlayerId_PublicPlayerStateNoRoute(){
        HashMap<PlayerId, PublicPlayerState> mapsPlayerId_PublicPlayerState = new HashMap<>();
        mapsPlayerId_PublicPlayerState.put(player1, publicPlayerState3);
        mapsPlayerId_PublicPlayerState.put(player2, publicPlayerState3);
        return mapsPlayerId_PublicPlayerState;
    }

    public static HashMap<PlayerId, PlayerState> mapsPlayerId_PlayerState(){
        HashMap<PlayerId, PlayerState> mapsPlayerId_PlayerState = new HashMap<>();
        mapsPlayerId_PlayerState.put(player1, PlayerState1);
        mapsPlayerId_PlayerState.put(player2, PlayerState2);
        return mapsPlayerId_PlayerState;
    }

    public static HashMap<PlayerId, PlayerState> mapsPlayerId_PlayerStateNoRoute(){
        HashMap<PlayerId, PlayerState> mapsPlayerId_PlayerState = new HashMap<>();
        mapsPlayerId_PlayerState.put(player1, PlayerState3);
        mapsPlayerId_PlayerState.put(player2, PlayerState3);
        return mapsPlayerId_PlayerState;
    }
    public static HashMap<PlayerId, PlayerState> mapsPlayerId_PlayerStateWithoutTicket(){
        HashMap<PlayerId, PlayerState> mapsPlayerId_PlayerState = new HashMap<>();
        mapsPlayerId_PlayerState.put(player1, PlayerStateWithoutTicket);
        mapsPlayerId_PlayerState.put(player2, PlayerStateWithoutTicket);
        return mapsPlayerId_PlayerState;
    }

    public static HashMap<PlayerId, PlayerState> mapsPlayerId_PlayerStateOneCarEach(){
        HashMap<PlayerId, PlayerState> mapsPlayerId_PlayerState = new HashMap<>();
        mapsPlayerId_PlayerState.put(player1, PlayerState1carRemaining);
        mapsPlayerId_PlayerState.put(player2, PlayerState1carRemaining);
        return mapsPlayerId_PlayerState;
    }

}
