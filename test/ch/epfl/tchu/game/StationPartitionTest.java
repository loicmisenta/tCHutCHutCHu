package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class StationPartitionTest {

    @Test
    void construitCorrectement(){
        var station1 = new Station(0, "première station");
        var station2 = new Station(1, "deuxième station");
        var station3 = new Station(2, "troisième station");
        var station4 = new Station(3, "quatrième station");
        var stationNotConnected = new Station(4, "station pas connecté");
        var expectedString = 3;
        StationPartition station = new StationPartition.Builder(4).connect(station1, station2).connect(station2, station3).connect(station3, station4).build();

        assertEquals(expectedString, station);  // produit une boucle reccursive
        assertFalse(station.connected(station4, stationNotConnected));
    }


    @Test
    void representativaCalled(){
        var stations = new Station[10];
        for (int i = 0; i < stations.length; i++)
            stations[i] = new Station(i, "Station " + i);

       // assertEquals();
    }

}
