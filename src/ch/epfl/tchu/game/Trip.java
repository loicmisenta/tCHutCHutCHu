package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     *
     * @param from
     * @param to
     * @param points
     */
    public Trip(Station from, Station to, int points) {
        if(from == null || to == null){
            throw new NullPointerException("Une des deux gares est nulle");
        }
        if (points <= 0){
            throw new IllegalArgumentException("Points négatifs");
        }
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.points = points;
    }
    public static List<Trip> all(List<Station> from, List<Station> to, int points){
        if ((from.isEmpty() || to.isEmpty()) || points<=0){
            throw new IllegalArgumentException("Liste vide ou points négatifs");
        }

        List<Trip> trajet = new ArrayList<>();
        for (Station f:
             from) {
            for (Station t:
                 to) {
                trajet.add(new Trip(f, t, points));
            }
        }
        return trajet;
    }

    /**
     *
     * @return
     */
    public Station from() {
        return from;
    }

    public Station to() {
        return to;
    }

    public int points() {
        return points;
    }
    public int points(StationConnectivity connectivity){
        if(connectivity.connected(from(), to())){
            return points();
        }
        else{
            return -1*points();
        }

    }
}
