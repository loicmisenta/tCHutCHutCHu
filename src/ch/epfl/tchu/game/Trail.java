package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 *
 * La classe représentant un chemin dans le réseau
 * fait à partir des routes qui lui sont donnés et contennant:
 * deux stations qui sont reliés par une longueur
 */
public final class Trail {
    public int length;
    public Station station1;
    public Station station2;


    //faire un constructeur ??

    private Trail (List<Route> routes){
        this.station1 = routes.get(0).station1();
        this.station2 = routes.get(routes.size()-1).station2();
        for (Route r: routes) {
            this.length += r.length();
        }
    }
    private Trail(int length, Station station1, Station station2){
        this.length = length;
        this.station1 = station1;
        this.station2 = station2;
    }


    /**
     * @return la longeur du Trail
     */
    public int length() {
        return length;
    }

    /**
     * @return la première station
     */
    public Station station1() {
        return station1;
    }

    /**
     * @return la deuxième station
     */
    public Station station2() {
        return station2;
    }


    /**
     * Méthode calculant à partir des
     * @param routes dont le joueur s'est émparé
     * @return le chemin le plus long
     */

    //ERREUR RUNTIME EXCEPTION
    public static Trail longest(List<Route> routes){


        //Trail cs = new Trail(routes);
        List<Route> cs = new ArrayList<>(routes);
        Trail longest = new Trail(0, null, null);

        //le cas si la route passée en paramètre est vide
        if (routes == null ) {
            return longest;
        }

        while(!cs.isEmpty()){
            List<Route> csPrime = new ArrayList<>();

            for (Route c: cs) {
                List<Route> rs = new ArrayList<>(routes);

                rs.removeAll(Collections.singleton(c));//PEUT ETRE IF ON SAIS PAS TROP KOI
                for (Route r: rs) {
                    if(!(r.station1().equals(c.station2())) || (r.station2().equals(c.station1()))){
                        rs.remove(r);

                    } else{
                        if (r.station1().equals(c.station2())){
                            csPrime.addAll(List.of(c, r));


                            //crée un nouveau trail et le conserve si sa
                            //longeur est la plus grande
                            Trail trail1 = new Trail(List.of(c, r));
                            if (trail1.length() > longest.length()){
                                longest = trail1;

                        } else {
                            csPrime.addAll(List.of(r, c));
                            //crée un nouveau trail et le conserve si sa
                            //longeur est la plus grande
                            Trail trail2 = new Trail(List.of(r, c));
                                if (trail2.length() > longest.length()){
                                    longest = trail2;
                        }}

                        }


                    }
                }
                for (Route r:
                     cs) {
                    System.out.println(r.toString());
                }
                System.out.println(longest.toString());
                cs = csPrime;
            }
        }
        return longest;

    }

    /**
     * La redefinition de la méthode toString
     * @return une représentation textuelle du chemin
     */
    @Override
    public String toString() {
        return "Trail {( " + " length = " + length + " ) , station1 = " + station1 +
                " , station2 = " + station2 + " }";
    }
}
