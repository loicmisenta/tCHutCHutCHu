package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class StationPartitionTest {

    @Test
    void stationsConnectésEtNon(){
        var station1 = new Station(2, "deuxième station");
        var station2 = new Station(1, "première station");
        var station3 = new Station(0, "station nulle");
        var station4 = new Station(3, "toisième station");
        var stationNotConnected = new Station(4, "station pas connecté");
        var expectedString = 3;
        StationPartition station = new StationPartition.Builder(4).connect(station1, station2).connect(station2, station3).connect(station3, station4).build();

        assertEquals(expectedString, station.liens[0]);
        assertEquals(expectedString, station.liens[1]);
        assertEquals(expectedString, station.liens[2]);
        assertEquals(expectedString, station.liens[3]);
        assertFalse(station.connected(station4, stationNotConnected));
    }

    @Test
    void buildDeStationPartition(){
        var stations = new Station[6];
        StationPartition.Builder station = new StationPartition.Builder(6);
        for (int i = 0; i < stations.length -1; i++) {
            stations[i] = new Station(i, "Station " + i);
            stations[i+ 1] = new Station(i + 1, "Station " + i + 1);
            station.connect(stations[i], stations[i + 1]);
        }
        StationPartition builtPartition = station.build();
        String tousLesId = "";
        for (int identifiant: builtPartition.liens) {
            tousLesId += identifiant + " ";
        }


        var expectedValue = "5 5 5 5 5 5 ";
        assertEquals(expectedValue, tousLesId);


    }

    @Test
    void stationPartitionWithTwoDifferentRepresentatives(){
        var lesAutresStations = new Station[9];
        StationPartition.Builder autreStation = new StationPartition.Builder(9);
        for (int i = 0; i < 5; i++) {
            lesAutresStations[i] = new Station(i, "Station " + i);
            lesAutresStations[i+ 1] = new Station(i + 1, "Station " + i + 1);
            autreStation.connect(lesAutresStations[i], lesAutresStations[i + 1]);
        }
        for (int i = 6; i < lesAutresStations.length -1; i++) {
            lesAutresStations[i] = new Station(i, "Station " + i);
            lesAutresStations[i+ 1] = new Station(i + 1, "Station " + i + 1);
            autreStation.connect(lesAutresStations[i], lesAutresStations[i + 1]);
        }
        StationPartition builtAutrePartition = autreStation.build();
        String tousLesAutresId = "";
        for (int identifiant: builtAutrePartition.liens) {
            tousLesAutresId += identifiant + " ";
        }

        var expectedOtherValue = "5 5 5 5 5 5 8 8 8 ";
        assertEquals(expectedOtherValue, tousLesAutresId);
    }

    @Test
    void stationConstructorFailsForNegativeStationCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StationPartition.Builder(-1);
        });
    }


}
