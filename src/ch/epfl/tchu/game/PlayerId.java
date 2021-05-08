package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Type énuméré representant l'identité d'un joueur
 */
public enum PlayerId {
    PLAYER_1, PLAYER_2;


    public static final List<PlayerId> ALL = List.of(PlayerId.values());
    public static final int COUNT = ALL.size();

    /**
     * Méthode qui retourne 'identité du joueur qui suit celui auquel on l'applique
     */
    public PlayerId next(){
        if (this == PLAYER_1) {
            return PLAYER_2;
        } else {
            return PLAYER_1;
        }
    }
}
