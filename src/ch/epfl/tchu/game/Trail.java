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
    public List<Route> routes;


    //faire un constructeur ??

    private Trail (List<Route> routes){
        this.station1 = routes.get(0).station1();
        this.station2 = routes.get(routes.size()-1).station2();
        for (Route r: routes) {
            this.length += r.length();
        }
        this.routes = routes;
    }

    private Trail(Station station1, Station station2, int length, List<Route> routes){
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.routes = routes;
    }
    //private reverse
    private static Route reverseRoute(Route route){
        return new Route(route.id(), route.station2(), route.station1(), route.length(), route.level(), route.color());
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


    public static Trail longest(List<Route> routes){


        List<Trail> cs = new ArrayList<Trail>();
        for (Route r: routes) {
            cs.add(new Trail(List.of(r)));
            cs.add(new Trail( List.of(reverseRoute(r))));
        }

        Trail longest = new Trail(null, null, 0 , List.of());

        //le cas si la route passée en paramètre est vide
        if (routes == null ) {
            return longest;
        }

        while(!cs.isEmpty()){
            List<Trail> csPrime = new ArrayList<>();

            for (Trail c: cs) {
                List<Route> rs = new ArrayList<>(routes);

                rs.removeAll(c.routes);

                for (Route r: rs) {
                    Trail trailLong;
                        if ((r.station1().equals(c.station2()))) {
                            List<Route> routesAjouter = new ArrayList<>(c.routes);
                            routesAjouter.add(r);
                            csPrime.add(new Trail(routesAjouter));

                            //crée un nouveau trail et le conserve si sa
                            //longeur est la plus grande
                            trailLong = new Trail(routesAjouter);

                        } else if ((r.station2().equals(c.station2()))) {
                            List<Route> routesAjouter = new ArrayList<>(c.routes);
                            routesAjouter.add(r);
                            csPrime.add(new Trail(routesAjouter));

                            //crée un nouveau trail et le conserve si sa
                            //longeur est la plus grande
                            trailLong = new Trail(routesAjouter);
                        } else {
                            trailLong = new Trail(null, null, 0, List.of());
                        }
                        if (c.length() > longest.length()) {
                            longest = c;
                        }
                }
            }
            cs = csPrime;
        }
        return longest;

    }

    /**
     * La redefinition de la méthode toString
     * @return une représentation textuelle du chemin
     */
    @Override
    public String toString() {
        //return "Trail {( " + " length = " + length + ") , station1 = " + station1 +
        //       ", station2 = " + station2 + " }";

        String list = station1.toString() + " - ";
        Station station = station1;
        for (Route r: routes) {
            list += r.stationOpposite(station).toString() + " - ";
            station = r.stationOpposite(station);
        }
        return list + " (" + length() + ") ";
    }


}
