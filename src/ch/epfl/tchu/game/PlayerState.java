package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;

public final class PlayerState extends PublicPlayerState{
    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = SortedBag.of(tickets);
        this.cards = SortedBag.of(cards);
    }
    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size()==4);
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }
    public SortedBag<Ticket> tickets(){
        return tickets;

    }

    public SortedBag<Card> cards() {
        return cards;
    }

    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        return new PlayerState(tickets().union(newTickets), cards(), routes());
    }

    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        return new PlayerState(tickets(), cards().union(SortedBag.of(additionalCards)), routes());
    }

    public PlayerState withAddedCard(Card card){
        return this.withAddedCards(SortedBag.of(card));
    }


    public boolean canClaimRoute(Route route){
        return !possibleClaimCards(route).isEmpty() && route.length() <= carCount();
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route){
        List<SortedBag<Card>> listeDesRoutesEmparables = new ArrayList<>();
        for (SortedBag<Card> routePossible: route.possibleClaimCards()) {
            if (cards.contains(routePossible)){
                listeDesRoutesEmparables.add(routePossible);
            }
        }
        return null;
    }

    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){

        //si locomotive
    }

}
