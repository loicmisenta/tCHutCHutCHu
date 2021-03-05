package ch.epfl.tchu.game;

/**
 * Interface representant la connectivité du réseau d'un joueur
 */
public interface StationConnectivity {
    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * Methode abstraite la
     * @param s1 gare 1
     * @param s2 gare 2
     * @return boolean si les deux gares sont connectés
     */
    public abstract boolean connected(Station s1, Station s2);
}
