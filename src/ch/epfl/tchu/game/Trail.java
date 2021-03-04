package ch.epfl.tchu.game;

import java.util.List;

public final class Trail {
    public int length;
    public Station station1;
    public Station station2;

    //faire un constructeur qui n'est pas forcément public
    private Trail (List<Route> routes){

    }
    /**
     *
     * @param routes
     */
    static void longest(List<Route> routes){

        //FAUX ::::
        List<Route> cs = List.copyOf(routes);  //il faudra faire une copie
        List<Route> rs = List.copyOf(routes);  // à chaque fois
        //liste de tous les chemins constitués d'une seule route

    }

}
