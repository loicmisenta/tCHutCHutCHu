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
        StationPartition station = new StationPartition.Builder(4).connect(station1, station3).connect(station2, station3).connect(station3, station4).build();
        /*
        //assertEquals(expectedString, station.liens[0]);
        assertEquals(expectedString, station.liens[1]);
        assertEquals(expectedString, station.liens[2]);
        assertEquals(expectedString, station.liens[3]);
        assertFalse(station.connected(station4, stationNotConnected));
        assertTrue(station.connected(station1, station4));
        assertTrue(station.connected(station1, station2));
        assertTrue(station.connected(station3, station2));

         */
    }

    @Test
    void buildDeStationPartition(){
        /*
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


         */

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

    // TODO
    @Test
    void BuilderConstructorTestFailsNegCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            StationPartition.Builder stb = new StationPartition.Builder(-1);
        });

        //StationPartition.Builder stb = new StationPartition.Builder(0);
    }

    // TODO
    @Test
    void connectedTest() {
        StationPartition.Builder stb = new StationPartition.Builder(33);
        stb.connect(ChMap.stations().get(11), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(3), ChMap.stations().get(11));
        stb.connect(ChMap.stations().get(16), ChMap.stations().get(3));
        stb.connect(ChMap.stations().get(32), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(24), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(32), ChMap.stations().get(24));
        StationPartition sp = stb.build();

        assertTrue(sp.connected(ChMap.stations().get(3), ChMap.stations().get(11)));
        assertFalse(sp.connected(ChMap.stations().get(32), ChMap.stations().get(17)));
    }
    @Test
    void testConnected() {

        Station cannes = new Station(0, "Cannes");
        Station lausanne = new Station(1, "Lausanne");
        Station geneve = new Station(2, "Geneve");
        Station paris = new Station(3, "Paris");
        Station grasse = new Station(4, "Grasse");

        StationPartition stationBuilderTest = new StationPartition.Builder(5).
                connect(cannes, lausanne).
                connect(cannes, geneve).
                connect(grasse, lausanne).
                connect(geneve, paris).build();
        assertEquals(true, stationBuilderTest.connected(cannes, grasse));
        assertEquals(true, stationBuilderTest.connected(lausanne, paris));
        assertEquals(true, stationBuilderTest.connected(geneve, geneve));


        StationPartition stationBuilderTest2 = new StationPartition.Builder(5).
                connect(cannes, lausanne).
                connect(paris, grasse).
                connect(geneve, geneve).build();
        assertEquals(false, stationBuilderTest2.connected(cannes, geneve));
        assertEquals(false, stationBuilderTest2.connected(cannes, grasse));
        assertEquals(true, stationBuilderTest2.connected(paris, grasse));
        assertEquals(true, stationBuilderTest2.connected(geneve, geneve));
        assertEquals(true, stationBuilderTest2.connected(cannes, lausanne));
    }


}
