package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ch.epfl.tchu.game.PublicGameStateTestTancredi.comparePublicGameState;
//import static ch.epfl.tchu.game.PublicGameStateTest.comparePublicGameState;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTestTancredi {
    /**
    public static final Random NON_RANDOM = new Random(){
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };

    public static Random rdm = new Random();
    private static GameState gS = GameState.initial(SortedBag.of(ChMap.tickets()), rdm);

    public static final SortedBag<Ticket> getSB() {
        SortedBag.Builder<Ticket> sB1 = new SortedBag.Builder<>();
        sB1.add(gS.tickets.cards.get(gS.ticketsCount() - 4));
        sB1.add(gS.tickets.cards.get(gS.ticketsCount() - 3));
        sB1.add(gS.tickets.cards.get(gS.ticketsCount() - 2));
        sB1.add(gS.tickets.cards.get(gS.ticketsCount() - 1));
        return sB1.build();
    }


    @Test
    void initialTest() {
        GameState gs = GameState.initial(SortedBag.of(ChMap.tickets()), NON_RANDOM);
        assertTrue(true);
    }

    @Test
    void topTicketsFailsWithIllegalParam(){
        assertThrows(IllegalArgumentException.class, () -> {
           gS.topTickets(-1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gS.topTickets(gS.ticketsCount() + 1);
        });
    }

    @Test
    void topTickets(){
        assertEquals(SortedBag.of(ChMap.tickets()), gS.topTickets(gS.ticketsCount()));
        SortedBag.Builder<Ticket> sB1 = new SortedBag.Builder<>();
        sB1.add(gS.tickets.cards.get(gS.ticketsCount() - 4));
        sB1.add(gS.tickets.cards.get(gS.ticketsCount() - 3));
        sB1.add(gS.tickets.cards.get(gS.ticketsCount() - 2));
        sB1.add(gS.tickets.cards.get(gS.ticketsCount() - 1));
        SortedBag<Ticket> s1 = sB1.build();
        System.out.println(s1);
        System.out.println(gS.topTickets(4));
        //assertTrue(gS.topTickets(4).contains(s1));
        //assertTrue(s1.contains(gS.topTickets(4)));
    }

    @Test
    void withoutTopTicketsFailsWithIllegalParam(){
        assertThrows(IllegalArgumentException.class, () -> {
            gS.withoutTopTickets(-1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gS.withoutTopTickets(gS.ticketsCount() + 1);
        });
    }

    @Test
    void withInitiallyChosenTicketsFailsWithParam(){
        PlayerState ps = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(), new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> {
            Deck deck = new Deck(List.of());
        GameState gs = new GameState(deck.size(),new CardState(
                List.of(Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE), deck.size(),5 , deck,
                SortedBag.of(5, Card.LOCOMOTIVE)), PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, ps, PlayerId.PLAYER_2, ps),null, new Deck(List.of()));
        gs.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of());
        });
    }

    public static boolean compareGameState(GameState gS1, GameState gS2){
        if (gS1.tickets.equals(gS2.tickets)
        && compareCardState(gS1.cardState, gS2.cardState)
        && comparePlayerState(gS1.currentPlayerState(),gS2.currentPlayerState())
        && comparePublicGameState(gS1, gS2)) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    void withChosenAdditionalTicketsFailsWithIllegalParam(){
        assertThrows(IllegalArgumentException.class, () -> {
            gS.withChosenAdditionalTickets(SortedBag.of(), SortedBag.of(ChMap.tickets().get(0)));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gS.withChosenAdditionalTickets(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(ChMap.tickets().get(1)));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gS.withChosenAdditionalTickets(SortedBag.of(1, ChMap.tickets().get(1)), SortedBag.of(1, ChMap.tickets().get(1), 1, ChMap.tickets().get(0)));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gS.withChosenAdditionalTickets(SortedBag.of(1, ChMap.tickets().get(1)), SortedBag.of(2, ChMap.tickets().get(1)));
        });
    }

    @Test
    void withInitiallyChosenTicketsDoesNotChangeTickets(){
        Deck<Ticket> expectedTickets = gS.tickets;
        gS.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(ChMap.tickets().get(0)));
        assertEquals(expectedTickets.cards, gS.tickets.cards);
    }

    @Test
    void withChosenAdditionalTicketsChangesTickets(){
        List<Ticket> expectedTickets = gS.tickets.cards.subList(0, gS.ticketsCount()-1);
        //System.out.println(gS.tickets.topCard());
        System.out.println(gS.tickets.cards);
        gS = gS.withChosenAdditionalTickets(SortedBag.of(gS.tickets.topCard()), SortedBag.of(gS.tickets.topCard()));
        assertEquals(expectedTickets, gS.tickets.cards);
    }

    @Test
    void withChosenAdditionalTickets(){
        Deck<Ticket> deck = gS.tickets;
        SortedBag<Ticket> sbt = gS.topTickets(1);
        GameState newGS = gS.withChosenAdditionalTickets(sbt, sbt);
        assertEquals(sbt , newGS.currentPlayerState().tickets());
        assertEquals(deck.withoutTopCard().cards, newGS.tickets.cards);
    }

    @Test
    void withDrawnFaceUpCardFailsWhenCanDrawCardsReturnsFalse(){
        List<Card> cards = new ArrayList();
        for(int i = 0; i < 10; ++i)
            cards.add(Card.LOCOMOTIVE);

        Random rdm = new Random();
        PlayerState ps = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(), new ArrayList<>());
        Deck deck1 = new Deck(List.of());
        Deck deckCardState = new Deck(List.of( Card.LOCOMOTIVE, Card.LOCOMOTIVE));
        GameState gS1 = new GameState(deck1.size(), new CardState(List.of(Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE), deckCardState.size(), 1, deckCardState,
                SortedBag.of(Card.GREEN)), PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, ps, PlayerId.PLAYER_2, ps), null, deck1);

        assertThrows(IllegalArgumentException.class, () -> {
            gS1.withDrawnFaceUpCard(1);
        });
    }

    @Test
    void withDrawnFaceUpCardChangesCardState(){
        Card faceUpCardExpected = gS.cardState.faceUpCard(3);
        Card topCardExpected = gS.topCard();
        SortedBag<Card> playersCard = gS.currentPlayerState().cards();
        gS = gS.withDrawnFaceUpCard(3);
        assertEquals(topCardExpected, gS.cardState.faceUpCard(3));
        assertEquals(playersCard.union(SortedBag.of(faceUpCardExpected)), gS.currentPlayerState().cards());
    }

    @Test
    void withBlindlyDrawnCardFailsWhenCanDrawCardsReturnsFalse() {
        PlayerState ps = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(), new ArrayList<>());
        Deck deck1 = new Deck(List.of());
        Deck deckcardState = new Deck(List.of( Card.LOCOMOTIVE, Card.LOCOMOTIVE));
        GameState gS1 = new GameState(deck1.size(), new CardState(List.of(Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE), deckcardState.size(),
                1, deckcardState, SortedBag.of(Card.GREEN)), PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, ps, PlayerId.PLAYER_2, ps), null, deck1);
        assertThrows(IllegalArgumentException.class, () -> {
            gS1.withBlindlyDrawnCard();
        });
    }

    @Test
    void withBlindlyDrawnCardChangesCardState(){
        Card topCardExpected = gS.topCard();
        SortedBag<Card> playersCard = gS.currentPlayerState().cards();
        gS = gS.withBlindlyDrawnCard();
        assertEquals(playersCard.union(SortedBag.of(topCardExpected)), gS.currentPlayerState().cards());
    }

    @Test
    void withClaimedRouteChanges(){
        Random rdm = new Random();
        GameState gS1 = GameState.initial(SortedBag.of(ChMap.tickets().get(0)), rdm);
        SortedBag<Card> discardsExpected = gS1.cardState.discards;
        SortedBag<Card> playersCardsExpected = gS1.currentPlayerState().cards();
        SortedBag<Card> cards = SortedBag.of(2, Card.LOCOMOTIVE, 1, Card.GREEN);
        gS1 = gS1.withClaimedRoute(ChMap.routes().get(0), cards);
        assertEquals(discardsExpected.union(cards), gS1.cardState.discards);
        assertEquals(List.of(ChMap.routes().get(0)), gS1.currentPlayerState().routes());
        assertEquals(playersCardsExpected.difference(cards), gS1.currentPlayerState().cards());
    }

    /**
    @Test
    void addPlayerTicketsTest() {
        PlayerState ps = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(), new ArrayList<>());
        SortedBag.Builder sbb = new SortedBag.Builder();
        sbb.add(ChMap.tickets().get(0));
        sbb.add(SortedBag.of(1,ChMap.tickets().get(12), 1, ChMap.tickets().get(14)));
        PlayerState ps2 = new PlayerState(sbb.build(), SortedBag.of(), new ArrayList<>());


        Deck cardStateDeck = new Deck(List.of());
        GameState gs = new GameState(cardStateDeck.size(), new CardState(List.of(Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE), cardStateDeck.size(), 5,
                cardStateDeck, SortedBag.of(5, Card.LOCOMOTIVE)), PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, ps, PlayerId.PLAYER_2, ps), null, cardStateDeck);

        gs = gs.addPlayerTickets(PlayerId.PLAYER_1, SortedBag.of(1,ChMap.tickets().get(12), 1, ChMap.tickets().get(14)));
        assertTrue(comparePlayerState(gs.playerState.get(PlayerId.PLAYER_1),ps2));
    }

    @Test
    void addPlayerCardTest() {
        PlayerState ps = new PlayerState(SortedBag.of(), SortedBag.of(Card.BLUE), new ArrayList<>());
        PlayerState ps2 = new PlayerState(SortedBag.of(), SortedBag.of(1, Card.BLUE, 1, Card.GREEN), new ArrayList<>());

        GameState gs = new GameState(new Deck(List.of()), new CardState(new Deck(List.of()),
                List.of(Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE),
                SortedBag.of(5, Card.LOCOMOTIVE)), PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, ps, PlayerId.PLAYER_2, ps), null);

        gs = gs.addPlayerCard(PlayerId.PLAYER_1, Card.GREEN);
        assertTrue(comparePlayerState(gs.playerState.get(PlayerId.PLAYER_1),ps2));
    }

    @Test
    void forNextTurnTest() {
        List<Route> routes = new LinkedList<>();
        for(int i = 1; i < 19; ++i)
            routes.add(ChMap.routes().get(i));

        PlayerState ps = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(), new ArrayList<>());
        PlayerState ps3 = new PlayerState(SortedBag.of(ChMap.tickets().get(0)), SortedBag.of(), routes);
        Deck deck1 = new Deck(List.of());
        Deck deckcardState = new Deck(List.of( Card.LOCOMOTIVE, Card.LOCOMOTIVE));
        GameState gS1 = new GameState(deck1.size(), new CardState(List.of(Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE), deckcardState.size(),
                1, deckcardState, SortedBag.of(Card.GREEN)), PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, ps, PlayerId.PLAYER_2, ps), null, deck1);

        Deck cardStateDeck = new Deck(List.of( Card.LOCOMOTIVE, Card.LOCOMOTIVE));
        Deck deck2 = new Deck(List.of());
        GameState gS3 = new GameState(deck2.size(), new CardState(List.of(Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.LOCOMOTIVE), cardStateDeck.size(), 1,
                cardStateDeck, SortedBag.of(Card.GREEN)), PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, ps3, PlayerId.PLAYER_2, ps), null, deck2);

        GameState gS2 = gS1.forNextTurn();
        assertEquals(PlayerId.PLAYER_2, gS2.currentPlayerId());
        GameState gS4 = gS3.forNextTurn();
        assertEquals(PlayerId.PLAYER_1, gS4.lastPlayer());
    }


    public static boolean compareCardState(CardState cS1, CardState cS2){
        if (cS1.deck.equals(cS2.deck) && cS1.discards.equals(cS2.discards) && PublicGameStateTestTancredi.comparePublicCardState(cS1, cS2)){
            return true;
        } else {
            return false;
        }
    }

    public static boolean comparePlayerState(PlayerState pS1, PlayerState pS2){
        if (pS1.tickets().equals(pS2.tickets()) && pS1.cards().equals(pS2.cards()) && PublicGameStateTestTancredi.comparePublicPlayerState(pS1, pS2)){
            return true;
        } else {
            return false;
        }
    }**/
}
