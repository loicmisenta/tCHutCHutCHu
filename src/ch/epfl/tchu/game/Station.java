package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 Cette classe représente les stations du jeu
 avec leur identifications unique et leur nom
 et comportant le nombre total de stations
 */
public final class Station {
    private final int id;
    private final String name;

    /**
     * @param id retourne le numéro d'identification de la gare
     * @param name le nom de la gare
     */
    public Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);
        this.id = id;
        this.name = name;
    }

    /**
     * Methode qui
     * @return le numéro d'identification de la gare
     */
    public int id(){
        return id;
    }

    /**
     * Methode qui
     * @return le nom de la gare
     */
    public String name() {
        return name;
    }

    /**
     * @return le nom de la gare
     */
    @Override
    public String toString() {
        return name;
    }
}
