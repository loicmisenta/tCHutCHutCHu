package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        var expectedString = "première station";
        StationPartition station = new StationPartition.Builder(4).connect(station1, station2).connect(station2, station3).connect(station3, station4).build();
       assertEquals(expectedString, station.toString());  // produit une boucle reccursive
    }
    @Test
    void representativaCalled(){
        var stations = new Station[10];
        for (int i = 0; i < stations.length; i++)
            stations[i] = new Station(i, "Station " + i);

       // assertEquals();
    }

}
