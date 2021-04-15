package ch.epfl.tchu.game;

import java.util.ArrayList;
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
    private final int length;
    private final Station station1;
    private final Station station2;
    private final List<Route> routes;

    private Trail (List<Route> routes){
        this.station1 = routes.get(0).station1();
        this.station2 = routes.get(routes.size()-1).station2();
        int temp = 0;
        for (Route r: routes) {
            temp += r.length();
        }
        this.length = temp;
        this.routes = routes;
    }

    private Trail(Station station1, Station station2, int length, List<Route> routes){
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.routes = List.copyOf(routes);
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

        Trail longest = new Trail(null, null, 0 , List.of());

        //le cas si la route passée en paramètre est vide
        if (routes == null ) {
            return longest;
        }

        List<Trail> trails = new ArrayList<>();
        for (Route r: routes) {
            trails.add(new Trail(List.of(r)));
            trails.add(new Trail(r.station2(), r.station1(), r.length(), List.of(r)));
        }

        while(!trails.isEmpty()){
            List<Trail> trailsPrime = new ArrayList<>();

            for (Trail t: trails) {
                List<Route> rs = new ArrayList<>(routes);
                rs.removeAll(t.routes);

                for (Route r: rs) {

                    for (Station station: r.stations()) {
                        Station opposite = r.stationOpposite(station);
                        if(station.equals(t.station2())){

                            List<Route> routesAjouter = new ArrayList<>(t.routes);
                            routesAjouter.add(r);
                            trailsPrime.add(new Trail(t.station1(), opposite, t.length() + r.length(), routesAjouter));
                        }
                    }
                }
                if (t.length() > longest.length()) {
                    longest = t;
                }
            }
            trails = trailsPrime;
        }
        return longest;

    }

    /**
     * La redefinition de la méthode toString
     * @return une représentation textuelle du chemin
     */
    @Override
    public String toString() {

        if (routes==null){
            return "route vide";
        }
        List<String> l = new ArrayList<>();
        String list = station1.toString() + " - ";
        Station station = station1;


        for (Route r: routes) {
            l.add(r.stationOpposite(station).toString());
            station = r.stationOpposite(station);
        }
        list += String.join(" - ", l);
        return list + " (" + length() + ")";
    }


}
