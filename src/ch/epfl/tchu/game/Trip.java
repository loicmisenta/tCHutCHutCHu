package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Une classe representant les trajets
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     * Une classe qui va contruire un trajet entre deux desinations données
     * @param from destination de départ
     * @param to destination d'arrivée
     * @param points la distance entre les deux destinations
     */
    public Trip(Station from, Station to, int points) {
        if(from == null || to == null){
            throw new NullPointerException("Une des deux gares est nulle");
        }
        Preconditions.checkArgument(points>0);


        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.points = points;
    }

    /**
     *Une classe qui va contruire un trajet entre deux desinations données
     * @param from destination de départ
     * @param to destination d'arrivée
     * @param points la distance entre les deux destinations
     * @return
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points){
        if ((from.isEmpty() || to.isEmpty()) || points<=0){
            throw new IllegalArgumentException("Liste vide ou points négatifs");
        }
        List<Trip> trajet = new ArrayList<>();
        for (Station f: from) {
            for (Station t: to) {
                trajet.add(new Trip(f, t, points));
            }
        }
        return trajet;
    }

    /**
     * @return la destination de départ
     */
    public Station from() {
        return from;
    }

    /**
     * @return la destination d'arrivée
     */
    public Station to() {
        return to;
    }

    /**
     *
     * @return les points reliant les deux destinations
     */
    public int points() {
        return points;
    }

    /**
     * Cette methode @return nb de points en fonction de
     * @param connectivity
     * qu'on lui passe en paramétre
     */
    public int points(StationConnectivity connectivity){
        if(connectivity.connected(from(), to())){
            return points();
        }
        else{
            return -1*points();
        }

    }
}
