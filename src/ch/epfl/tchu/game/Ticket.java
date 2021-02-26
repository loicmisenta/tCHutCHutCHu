package ch.epfl.tchu.game;

import java.util.List;

public final class Ticket implements Comparable<Ticket>{
    private final List<Trip> trips;
    private final Station from;             //????
    private final Station to;
    private final int points;
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
        from = null;//??
        to = null;//??
        points = 0;//??

        billetText = computeText();
    }

    public Ticket(Station from, Station to, int points){
    //??????????????? ou declarer les attribut
        this.from = from;
        this.to = to;
        this.points = points;

        billetText = computeText();
    }

    public String text(){
        return billetText;
    }
    private static String computeText(){

    }

}
