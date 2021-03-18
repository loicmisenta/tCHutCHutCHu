package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTestTancredi {

    Station cannes = new Station(0, "Cannes");
    Station lausanne = new Station(1, "Lausanne");
    Station geneve = new Station(2, "Geneve");
    Station paris = new Station(3, "Paris");
    Station grasse = new Station(4, "Grasse");

    Station horsBorne = new Station(5, "HorsBorne");
    @Test
    void testConnected() {
        StationPartition stationBuilderTest = new StationPartition.Builder(5).
                connect(cannes, lausanne).
                connect(cannes,geneve).
                connect(grasse, lausanne).
                connect(geneve, paris).build();
        assertEquals(true, stationBuilderTest.connected(cannes, grasse));
        assertEquals(true,  stationBuilderTest.connected(lausanne, paris));
        assertEquals(true, stationBuilderTest.connected(geneve, geneve));


        StationPartition stationBuilderTest2 = new StationPartition.Builder(5).
                connect(cannes, lausanne).
                connect(paris, grasse).
                connect(geneve, geneve).build();
        assertEquals(false, stationBuilderTest2.connected(cannes, geneve));
        assertEquals(false, stationBuilderTest2.connected(cannes, grasse));
        assertEquals(true, stationBuilderTest2.connected(paris, grasse));
        assertEquals(true, stationBuilderTest2.connected(geneve, geneve));
        assertEquals(true, stationBuilderTest2.connected(cannes , lausanne));

        StationPartition stationBuilderTest3 = new StationPartition.Builder(5).connect(cannes, lausanne).
                connect(grasse, paris).
                connect(paris, lausanne).build();
        assertEquals(false, stationBuilderTest3.connected(cannes, geneve));
        assertEquals(false, stationBuilderTest3.connected(geneve, paris));
        assertEquals(false, stationBuilderTest3.connected(grasse, geneve));
        assertEquals(false, stationBuilderTest3.connected(lausanne, geneve));
        assertEquals(true, stationBuilderTest3.connected(lausanne, grasse));
        assertEquals(true, stationBuilderTest3.connected(grasse, lausanne));
        assertEquals(true, stationBuilderTest3.connected(paris, cannes));
        assertEquals(true, stationBuilderTest3.connected(cannes, paris));

        StationPartition stationBuilderTest4 = new StationPartition.Builder(10).
                connect(grasse, geneve).
                connect(grasse, grasse).connect(geneve, geneve).
                connect(grasse, cannes).build();

        assertEquals(true, stationBuilderTest4.connected(cannes, geneve));
        assertEquals(true, stationBuilderTest4.connected(grasse, cannes));
        assertEquals(false, stationBuilderTest4.connected(paris, lausanne));
        assertEquals(false, stationBuilderTest4.connected(grasse, lausanne));
        assertEquals(false, stationBuilderTest4.connected(paris, cannes));


        assertEquals(true, stationBuilderTest.connected(horsBorne, horsBorne));
        assertEquals(false, stationBuilderTest.connected(horsBorne, cannes));

    }


    @Test
    void testBuilderConstructor(){
        StationPartition stationBuilderTest = new StationPartition.Builder(10).build();
        int[] tab = new int[10];

        for(int i=0; i<tab.length; i++){
            tab[i] = i;
        }

        //assertArrayEquals(tab, stationBuilderTest.stationEnsembleID);

        assertThrows(IllegalArgumentException.class, () -> {
            StationPartition stationBuilderTest2 = new StationPartition.Builder(-1).build();
        });
    }


}