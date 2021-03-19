package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;


/**
*@author loicmisenta
*@author lagutovaalexandra
 * classe qui represente une partition (aplatie) des gares
*/
public class StationPartition implements StationConnectivity  {

    private final int[] liens; //TODO  privé

    /**
     * Constructeur de la partition
     * @param liens tableau d'entiers contenant les liens
     */
    private StationPartition(int[] liens){
        this.liens = liens;
    }



    /**
     * Classe imbriquée statiquement, representant un bâtisseur
     * de partition de gare
     */
    public static final class Builder{
        private int[] buildLiens;

        /**
         * Constructeur public du builder
         * @param stationCount
         * @throw IllegalArgumentException (grâce à Preconditions)
         * si stationCount est négatif
         */
        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount> 0);
            buildLiens = new int[stationCount];
            for (int i = 0; i < stationCount; i++) {
                buildLiens[i] = i;
            }
        }


        /**
         * Va connecter les sous-ensembles (connecte les représentatifs)
         * @param s1 station 1
         * @param s2 station 2
         * @return le bâtisseur
         */
        public Builder connect(Station s1, Station s2){
            buildLiens[representative(s1.id())] = representative(s2.id());
            return this;
        }

        /**
         * Méthode qui construit une partition à partir du Builder
         * @return la partition construite
         */

        public StationPartition build(){
            for (int i = 0; i < buildLiens.length ; i++) {
                    buildLiens[i] = representative(i);
            }
            return new StationPartition(buildLiens);
        }


        /**
         * Méthode qui prend en paramètre
         * @param id l'identifiant
         * @return la valeur de son representant
         */
        private int representative(int id) {
            if (buildLiens[id] == id) {
                return id;
            } else {
                return representative(buildLiens[id]);
            }
        }

    }

    /**
     * Verifie si les deux gares:
     * @param s1 gare 1
     * @param s2 gare 2
     * sont connectée / les mêmes representant
     * @return True si c'est le cas.
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        if (s1.id()< liens.length && s2.id() < liens.length){
            if (liens[s1.id()] == liens[s2.id()]){
                return true;
            }
        } else if(s1.id() == s2.id()){
            return true;
        }
        return false;
    }


}
