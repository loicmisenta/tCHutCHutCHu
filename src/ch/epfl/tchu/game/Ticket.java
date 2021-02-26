package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class Ticket implements Comparable<Ticket>{
    private final List<Trip> trips;
    private final String billetText;

    public Ticket(List<Trip> trips) {
        boolean allSameFrom = true;
        for (int i = 1; i < trips.size()-1; i++) {
            if (trips.get(0).from() != trips.get(i).from()){
                allSameFrom = false;
            }
        }
        if (trips.isEmpty() || allSameFrom){
            throw new IllegalArgumentException("trajet vide ou pas les même gare de départ");
        }
        this.trips = trips;
        billetText = computeText(trips);
    }

    public Ticket(Station from, Station to, int points){
        this(List.of(new Trip(from, to, points)));
    }

    public String text(){
        return billetText;
    }

    private static String computeText(List<Trip> trajets){
        String departText = trajets.get(0).from().toString();
        TreeSet<String> listeArriveeText = new TreeSet<>();
        for (Trip s:
             trajets) {
            listeArriveeText.add(String.format("%s (%s)", s.to().toString(), s.points()));
        }
        String arrivee = String.join(", ", listeArriveeText);

        return departText + " - " + arrivee;
    }
    public int points(StationConnectivity connectivity){
        int max = 0;
        for (int i = 0; i < trips.size()-1; i++) {
            if (trips.get(i).points(connectivity) > max){
                max = trips.get(i).points(connectivity);
            }
        }
        return max;
    }

    @Override
    public int compareTo(Ticket that) {
        String thisText = text();
        String thatText = computeText(that.trips);
        return thisText.compareTo(thatText);
    }
}
