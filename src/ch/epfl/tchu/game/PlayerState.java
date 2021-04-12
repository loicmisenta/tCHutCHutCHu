package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 * Une classe representant l'état complet d'un joueur.
 */

public final class PlayerState extends PublicPlayerState{

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    /**
     * Constructeur de la classe PlayerState qui aura comme paramètres:
     *
     * @param tickets ses tickets.
     * @param cards ses cartes.
     * @param routes ses routes.
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = SortedBag.of(tickets);
        this.cards = SortedBag.of(cards);
    }

    /**
     *
     * @param initialCards cartes initiales.
     * @return l'état initial d'un joueur auquel les cartes initiales données ont été distribuées.
     */
    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size() == Constants.INITIAL_CARDS_COUNT);
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }

    /**
     *
     * @return les tickets
     */
    public SortedBag<Ticket> tickets(){
        return tickets;

    }

    /**
     *
     * @return ses cartes
     */
    public SortedBag<Card> cards() {
        return cards;
    }

    /**
     *
     * @param newTickets tickets à ajouter.
     * @return un état identique au récepteur avec les tickets ajoutés.
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        return new PlayerState(tickets().union(newTickets), cards(), routes());
    }

    /**
     *
     * @param additionalCards cartes à ajouter.
     * @return un état identique au récepteur avec les cartes ajoutés.
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        return new PlayerState(tickets(), cards().union(SortedBag.of(additionalCards)), routes());
    }

    /**
     *
     * @param card cartes à ajouter.
     * @return un état identique au récepteur avec la carte ajouté.
     */
    public PlayerState withAddedCard(Card card){
        return this.withAddedCards(SortedBag.of(card));
    }

    /**
     *
     * @param route que le joueur veux savoir s'il peut s'en emparer.
     * @return True si la route peut être prise, false sinon.
     */
    public boolean canClaimRoute(Route route){
        return route.length() <= carCount() && !possibleClaimCards(route).isEmpty();
    }

    /**
     * @return la liste de tous les ensembles de cartes que le joueur pourrait utiliser
     * pour prendre possession de @param route.
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route){
        Preconditions.checkArgument(route.length() <= carCount()); //
        List<SortedBag<Card>> listeDesRoutesEmparables = new ArrayList<>();
        for (SortedBag<Card> routePossible: route.possibleClaimCards()) {
            if (cards.contains(routePossible)){
                listeDesRoutesEmparables.add(routePossible);
            }
        }
        return listeDesRoutesEmparables;
    }

    /**
     *
     * @param additionalCardsCount nb de cartes additionelles
     * @param initialCards cartes initialements posées
     * @param drawnCards les 3 cartes tirées du sommet de la pioche.
     * @return retourne la liste de tous les ensembles de cartes qu'il pourrait utiliser pour s'emparer d'un tunnel,
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){
        Preconditions.checkArgument(((additionalCardsCount > 0) && (additionalCardsCount <= Constants.ADDITIONAL_TUNNEL_CARDS)) && ((!initialCards.isEmpty()) && (initialCards.toSet().size() <=2)
        && (drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS)));

        //La couleur de la carte supp
        Card carteDeCouleurJoue = null;
        for (int i = 0; i < initialCards.size(); i++) {
            if(initialCards.get(i).color() != null){
                carteDeCouleurJoue = initialCards.get(i);
            }  //ajouter dans une liste si locomotive ou une couleur
        }

        SortedBag<Card> sansCartesJoues = cards().difference(initialCards);

        SortedBag.Builder<Card> cartesJouables = new SortedBag.Builder<>();
        for (Card cartes : sansCartesJoues) {
            if((cartes == carteDeCouleurJoue) || (cartes == Card.LOCOMOTIVE)){
                cartesJouables.add(cartes);
            }
        }
        SortedBag<Card> cartesJoues = cartesJouables.build();

        //ajout des subsets de taille donnée dans une liste

        if(cartesJoues.size() >= additionalCardsCount) {
            //condition si addCC est plus grand
            List<SortedBag<Card>> possibilitesDesCartes = new ArrayList<>(cartesJoues.subsetsOfSize(additionalCardsCount));
            //tri des cartes
            possibilitesDesCartes.sort(
                    Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));

            return possibilitesDesCartes;
        } else return List.of();
    }

    /**
     *
     * @return un état identique au récepteur, si ce n'est qu'il s'est de plus emparé de @param route donnée
     * au moyen des @param claimCards données,
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){
        SortedBag<Card> sansCartesJoues = cards().difference(claimCards);
        List<Route> avecRouteEmpare = new ArrayList<>(routes());
        avecRouteEmpare.add(route);
        return new PlayerState(tickets, SortedBag.of(sansCartesJoues), avecRouteEmpare);
    }

    /**
     *
     * @return  le nombre de points obtenus grâce à ses billets.
     */
    public int ticketPoints(){
        int maxValeur = 0;
        for (Route routesPossibles: routes()) {
            int maximumlocal = Math.max(routesPossibles.station1().id(), routesPossibles.station2().id());
            if ( maximumlocal> maxValeur){
                maxValeur = maximumlocal;
            }
        }
        StationPartition.Builder partitionBuild = new StationPartition.Builder(maxValeur + 1);
        for (Route routesPossibles: routes()) { partitionBuild.connect(routesPossibles.station1(), routesPossibles.station2()); }
        StationPartition partition = partitionBuild.build();
        int points = 0;

        for (Ticket t: tickets()) {
            points += t.points(partition);
        }

        return points;
    }

    /**
     * @return la totalité des points obtenus par le joueur à la fin de la partie.
     */
    public int finalPoints(){
        return ticketPoints() + claimPoints();
    }


    @Override //TODO Supprimer
    public String toString() {
        return "PlayerState = {" +
                "tickets:" + tickets.toString() +
                ", cards:" + cards.toString() +
                '}';
    }
}
