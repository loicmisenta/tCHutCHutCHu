package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Trail {
    public int length;
    public Station station1;
    public Station station2;

    //faire un constructeur qui n'est pas forcément public
    public Trail (List<Route> routes){
        this.station1 = routes.get(0).station1();
        this.station2 = routes.get(routes.size()-1).station2();
        for (Route r: routes) {
            this.length += r.length();
        }
    }

    /**
     *
     * @return la longeur du Trail
     */
    public int length() {
        return length;
    }

    public Station station1() {
        return station1;
    }

    public Station station2() {
        return station2;
    }


    /**
     *
     * @param routes
     */
    static Trail longest(List<Route> routes){


        List<Route> cs = new ArrayList<>(routes);
        List<Route> lastcs = new ArrayList<>();



        while(!cs.isEmpty()){
            List<Route> csPrime = new ArrayList<>();
            for (Route c: cs) {
                List<Route> rs = new ArrayList<>(routes);


                rs.removeAll(Collections.singleton(c));//PEUT ETRE IF ON SAIS PAS TROP KOI



                for (Route r: rs) {
                    if(!r.station1().equals(c.station2())){
                        rs.remove(r);
                    } else{
                        Trail t = new Trail(List.of(r, c));
                        csPrime.addAll(List.of(c, r));
                    }
                }
                if(csPrime.isEmpty()){
                    lastcs = cs;
                }
                cs = csPrime;

            }
        }

        return new Trail(lastcs);
        //liste de tous les chemins constitués d'une seule route


    }

}
