package ch.epfl.tchu.game;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Interface representant la connectivité du réseau d'un joueur
 */
public interface StationConnectivity {
    /**
     * Methode abstraite la
     * @param s1 gare 1
     * @param s2 gare 2
     * @return boolean si les deux gares sont connectés
     */
    boolean connected(Station s1, Station s2);
}
