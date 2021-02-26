package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 Cette classe représente les stations du jeu
 avec leur identifications unique et leur nom
 */
public final class Station {
    private final int id;
    private final String name;
    private final int TOTALSTATIONS = 51;

    public Station(int id, String name) {
        Preconditions.checkArgument(id>=0);
        this.id = id;
        this.name = name;
    }
    public int id(){
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
