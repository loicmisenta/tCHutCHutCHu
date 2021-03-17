package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
        return listeDesRoutesEmparables;
    }

    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){
        Preconditions.checkArgument(((additionalCardsCount > 0) && (additionalCardsCount < 4)) && (!initialCards.isEmpty()) && (initialCards.toSet().size() <=2)
        && (drawnCards.size() == 3));

        //La couleur de la carte supp
        Card carteDeCouleurJoué = null;
        for (int i = 0; i < initialCards.size(); i++) {
            if(initialCards.get(i).color() != null){
                carteDeCouleurJoué = initialCards.get(i);
            }  //ajouter dans une liste si locomotive ou une couleur
        }

        SortedBag<Card> sansCartesJoués = cards().difference(initialCards);

        SortedBag.Builder<Card> cartesJouables = null;
        for (Card cartes : sansCartesJoués) {
            if((cartes == carteDeCouleurJoué) || (cartes == Card.LOCOMOTIVE)){
                cartesJouables.add(cartes);
            }
        }
        SortedBag<Card> cartesJoués = cartesJouables.build();

        //ajout des subsets de taille donnée dans une liste
        List<SortedBag<Card>> possibilitésDesCartes = new ArrayList<>();

        if(cartesJoués.size() >= additionalCardsCount) {
            //condition si addCC est plus grand
            possibilitésDesCartes.addAll(cartesJoués.subsetsOfSize(additionalCardsCount));
            //tri des cartes
            possibilitésDesCartes.sort(
                    Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));

            return possibilitésDesCartes;
        } else return List.of();
    }

    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){
        SortedBag<Card> sansCartesJoués = cards().difference(claimCards);

        List<Route> avecRouteEmparé = routes();
        avecRouteEmparé.add(route);
        return new PlayerState(tickets, SortedBag.of(sansCartesJoués), avecRouteEmparé);
    }

    public int ticketPoints(){
        int max = 0;
        for (Route routesPossibles: routes()) {
            int maximumlocal = Math.max(routesPossibles.station1().id(), routesPossibles.station2().id());
            if ( maximumlocal> max){
                max = maximumlocal;
            }
        }
        max += 1;
        StationPartition.Builder partitionBuild = new StationPartition.Builder(max);
        for (Route routesPossibles: routes()) {
            partitionBuild.connect(routesPossibles.station1(), routesPossibles.station2());
        }
        StationPartition partition = partitionBuild.build();
        int point = 0;
        for (Ticket t: tickets()) {
            point += t.points(partition);
        }

        //RETOURNE LES POINT NEGATIF A LA PLACE DE POSITIF ???!!!!????
        return point;
    }

    public int finalPoints(){
        return ticketPoints() + claimPoints();
    }

    @Override
    public String toString() {
        return "PlayerState{" +
                "tickets=" + tickets.toString() +
                ", cards=" + cards.toString() +
                '}';
    }
}
