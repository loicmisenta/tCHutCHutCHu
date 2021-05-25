package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 *
 * La classe représentant la partie publique de l'état d'un joueur
 * le nombre de billets, de cartes et de wagons qu'il possède,
 * les routes dont il s'est emparé, et le nombre de points de construction.
 *
 */
public class PublicPlayerState {
    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int carCount;
    private final int claimPoints;

    /**
     * Constructeur de PublicPlayerState qui prends en parametre
     * @param ticketCount nombre de billets
     * @param cardCount nombre de cartes
     * @param routes nombre de routes
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes) {
        Preconditions.checkArgument(ticketCount>=0 && cardCount>=0);
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);

        //calcul nb de wagon
        int lengthRoute = 0;
        for (Route r: routes()) {
            lengthRoute += r.length();
        }
        carCount = Constants.INITIAL_CAR_COUNT - lengthRoute;

        //calcul nb de points
        int pointsRoute = 0;
        for (Route r: routes()) {
            pointsRoute+=r.claimPoints();
        }
        claimPoints = pointsRoute;
    }

    /**
     * @return le nombre de billets
     */
    public int ticketCount() {
        return ticketCount;
    }

    /**
     * @return le nombre de cartes
     */
    public int cardCount() {
        return cardCount;
    }

    /**
     * @return le nombre de routes
     */
    public List<Route> routes() {
        return routes;
    }

    /**
     * @return le nombre de wagon qu'il possede
     */
    public int carCount() {
        return carCount;
    }

    /**
     *
     * @return le nombre de points de construction obtenus
     */
    public int claimPoints() {
        return claimPoints;
    }

}
