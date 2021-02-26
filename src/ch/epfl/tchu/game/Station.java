package ch.epfl.tchu.game;

/**
 Cette classe représente les stations du jeu
 avec leur identifications unique et leur nom
 */
public final class Station {
    private final int id;
    private final String name;
    private final int TOTALSTATIONS = 51;

    public Station(int id, String name) {
        if (id < 0){
            throw new IllegalArgumentException("le numéro d'identification est strictement négatif");
        }
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
