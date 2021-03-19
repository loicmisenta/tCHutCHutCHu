package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Arrays;

/**
*@author loicmisenta
*@author lagutovaalexandra
 * classe qui represente une partition (aplatie) des gares
*/
public class StationPartition implements StationConnectivity  {

    public int[] liens;

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
         * Constructeur public
         * @param stationCount
         * @throw IllegalArgumentException (grâce à Preconditions)
         * si stationCount est négatif
         */
        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount> 0);
            buildLiens = new int[stationCount];
        }

        public Builder connect(Station s1, Station s2){
            //// connecter les repreentatives
                //pour tout ayant s1 en represntative passe pas
                //efface lien
            buildLiens[representative(s2.id())] = representative(s1.id());
                //buildLiens[s1.id()] = (s2.id());
                //buildLiens[s2.id()] = (s2.id());
            return this;
        }

        /**
         * Méthode qui construit une partition à partir du Builder
         * @return la partition construite
         */

        public StationPartition build(){
            for (int i = 0; i < buildLiens.length ; i++) {
                if(buildLiens[i] != representative(i)){
                    buildLiens[i] = representative(i);
                }
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

        public int[] getBuildLiens(){
            return buildLiens;
        }


    }

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
