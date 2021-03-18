package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTestTheoo {

    public String reprToString(int[] representativeArray) {
        int[] refArr = new int[representativeArray.length];
        for(int i = 0; i < representativeArray.length; ++i)
            refArr[i] = i;

        return  "StationPartition{" +
                "representativeArray=" + Arrays.toString(refArr) +
                "}\n" +
                "StationPartition{" +
                "representativeArray=" + Arrays.toString(representativeArray) +
                '}';
    }

    @Test
    void BuilderConstructorTestFailsNegCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            StationPartition.Builder stb = new StationPartition.Builder(-1);
        });

        StationPartition.Builder stb = new StationPartition.Builder(0);
    }

    @Test
    void BuilderConstructorTestWorksNormally() {
        StationPartition.Builder stb = new StationPartition.Builder(10);
        assertEquals(reprToString(new int[]{0,1,2,3,4,5,6,7,8,9}), stb.toString());
    }

    @Test
    void BuilderConnect() {
        StationPartition.Builder stb = new StationPartition.Builder(33);
        stb.connect(ChMap.stations().get(11), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(3), ChMap.stations().get(11));
        stb.connect(ChMap.stations().get(16), ChMap.stations().get(3));
        stb.connect(ChMap.stations().get(32), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(24), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(32), ChMap.stations().get(24));
        // System.out.println(stb);
    }

    @Test
    void BuilderBuild() {
        int[] exp = new int[]{0,1,2,24,4,5,6,7,8,9,10,24,12,13,14,15,24,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,24};

        StationPartition.Builder stb = new StationPartition.Builder(33);
        stb.connect(ChMap.stations().get(11), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(3), ChMap.stations().get(11));
        stb.connect(ChMap.stations().get(16), ChMap.stations().get(3));
        stb.connect(ChMap.stations().get(32), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(24), ChMap.stations().get(16));
        stb.connect(ChMap.stations().get(32), ChMap.stations().get(24));
        StationPartition sp = stb.build();

        assertEquals(reprToString(exp), stb.toString());
    }

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
}