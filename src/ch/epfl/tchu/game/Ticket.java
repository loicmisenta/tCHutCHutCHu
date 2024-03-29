package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import java.util.List;
import java.util.TreeSet;


/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 *
 * Une classe represantant un ticket
 */
public final class Ticket implements Comparable<Ticket>{
    private final List<Trip> trips;
    private final String billetText;

    /**
     * Constructeur Principal du Ticket à partir  du'une liste de trajets:
     * @param trips les trajets
     */
    public Ticket(List<Trip> trips) {
        Preconditions.checkArgument(!trips.isEmpty());

        for (int i = 1; i < trips.size(); i++) {
            Preconditions.checkArgument((trips.get(0).from().name().equals(trips.get(i).from().name())));
        }

        this.trips = List.copyOf(trips);
        billetText = computeText(trips);
    }

    /**
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
     * @return la representation grpahique du billet
     */
    public String text(){
        return billetText;
    }

    /**
     * compile billet (avec sa liste de trajet)
     * @param trajets la liste de trajet
     * @return sous forme de texte
     */
    private static String computeText(List<Trip> trajets){
        String departText = trajets.get(0).from().toString();
        TreeSet<String> listeArriveeText = new TreeSet<>();
        for (Trip s: trajets) {
            listeArriveeText.add(String.format("%s (%s)", s.to().toString(), s.points()));
        }
        String arrivee = String.join(", ", listeArriveeText);
        if (listeArriveeText.size() > 1){
            return departText + " - {" + arrivee + "}";
        } else{
            return departText + " - " + arrivee;
        }
    }

    /**
     * @param connectivity represente le fait que deux gares sont reliés ou non par
     *                     le réseau
     * @return le nombre de points que vaut le billet
     */


    public int points(StationConnectivity connectivity){
        int max = Integer.MIN_VALUE;
        for (Trip trip : trips) {
            if (trip.points(connectivity) > max) {
                max = trip.points(connectivity);
            }
        }
        return max;
    }

    /**
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

    /**
     * @return la même valeur que la méthode toText
     */
    @Override
    public String toString() {
        return billetText;
    }

}
