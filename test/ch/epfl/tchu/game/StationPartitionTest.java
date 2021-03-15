package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class StationPartitionTest {

    @Test
    void construitCorrectement(){
        var stations = new Station[10];
        for (int i = 0; i < stations.length; i++)
            stations[i] = new Station(i, "Station " + i);
        //StationPartition.Builder station = new StationPartition.Builder(4).connect(ChMap.SCH, ChMAp.).connect(a, b). ... .build();

         }
}
