package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class Ticket implements Comparable<Ticket>{
    private final List<Trip> trips;
    private final String billetText;

    public Ticket(List<Trip> trips) {
        boolean allSameFrom = true;
        for (int i = 1; i < trips.size(); i++) {
            if (!(trips.get(0).from().name().equals(trips.get(i).from().name()))){
                System.out.println(trips.get(0).from().toString() + "     " + trips.get(i).from().toString());;
                allSameFrom = false;
            }

        }
        //if (trips.isEmpty() || !allSameFrom){
        //    throw new IllegalArgumentException("trajet vide ou pas les même gare de départ");
        //}
        Preconditions.checkArgument(!(trips.isEmpty())|| !allSameFrom);

        this.trips = List.copyOf(trips);
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
        if (listeArriveeText.size()>1){
            return departText + " - {" + arrivee + "}";
        }
        else{
            return departText + " - " + arrivee;
        }

    }
    public int points(StationConnectivity connectivity){
        int min = -1000000;
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).points(connectivity) > min){
                min = trips.get(i).points(connectivity);
            }
        }
        return min;
    }

    @Override
    public int compareTo(Ticket that) {
        String thisText = this.text();
        String thatText = that.text();
        return thisText.compareTo(thatText);
    }
}
