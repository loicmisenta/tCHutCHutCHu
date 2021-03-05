package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


/**
 * @author loicmisenta
 * @author lagutovaalexandra
 *
 * Une classe represantant un ticket
 */
public final class Ticket implements Comparable<Ticket>{
    private final List<Trip> trips;
    private final String billetText;

    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * Constructeur Principal du Ticket à partir  du'une liste de trajets:
     * @param trips
     */
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

    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * Constructeur Secondaire  du ticket pour un seul trajet, qui va prendre les memes parametre qu'à la création
     * d'un trajet:
     * @param from destination de départ
     * @param to destination d'arrivée
     * @param points la distance entre les deux destinations
     * Et va crée une liste d'un seul trajet pour ensuite appeler le constructeur principal
     */
    public Ticket(Station from, Station to, int points){
        this(List.of(new Trip(from, to, points)));
    }

    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * @return la representation grpahique du billet
     */
    public String text(){
        return billetText;
    }

    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * compile billet (avec sa liste de trajet)
     * @param trajets la liste de trajet
     * @return sous forme de texte
     */
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

    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * @param connectivity represente le fait que deux gares sont reliés ou non par
     *                     le réseau
     * @return le nombre de points que vaut le billet
     */


    public int points(StationConnectivity connectivity){
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).points(connectivity) > max){
                max = trips.get(i).points(connectivity);
            }
        }
        return max;
    }

    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * compare deux tickets entre eux
     * @param that le ticket avec on veut le comparer
     * @return retourne un entier négatif  si la première vient avant la seconde dans l'ordre alphabétique,
     * zéro si les deux sont égales, sinon un entier positif.
     */
    @Override
    public int compareTo(Ticket that) {
        String thisText = this.text();
        String thatText = that.text();
        return thisText.compareTo(thatText);
    }
}
