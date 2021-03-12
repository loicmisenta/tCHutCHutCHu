package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
*@author loicmisenta
*@author lagutovaalexandra
*/
public class StationPartition implements StationConnectivity  {
    private int[] liens;

    private StationPartition(int[] liens){
        this.liens = liens;
    }

    public static final class Builder{
        private int[] buildLiens;

        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount< 0);
            buildLiens = new int[stationCount];
        }

        public Builder connect(Station s1, Station s2){
            buildLiens[s1.id()] = s2.id();
            return this;
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
