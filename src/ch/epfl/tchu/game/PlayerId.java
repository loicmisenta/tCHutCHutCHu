package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Type énuméré representant l'identité d'un joueur
 */
public enum PlayerId {
    PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4, PLAYER_5;


    public static final List<PlayerId> ALL = List.of(PlayerId.values());
    public static final int COUNT = ALL.size();

    /**
     * Méthode qui retourne 'identité du joueur qui suit celui auquel on l'applique
     */
    public PlayerId next(int nbJoueur){
        if (nbJoueur == 2){
            if (this == PLAYER_1) {
                return PLAYER_2;
            } else {
                return PLAYER_1;
            }
        } else if (nbJoueur == 3){
            if (this == PLAYER_1) {
                return PLAYER_2;
            } else if (this == PLAYER_2){
                return PLAYER_3;
            } else {
                return PLAYER_1;
            }
        } else if (nbJoueur == 4){
            if (this == PLAYER_1) {
                return PLAYER_2;
            } else if (this == PLAYER_2){
                return PLAYER_3;
            } else if (this == PLAYER_3) {
                return PLAYER_4;
            } else {
                return PLAYER_1;
            }
        } else {
            if (this == PLAYER_1) {
                return PLAYER_2;
            } else if (this == PLAYER_2){
                return PLAYER_3;
            } else if (this == PLAYER_3) {
                return PLAYER_4;
            } else if(this == PLAYER_4){
                return PLAYER_5;
            } else {
                return PLAYER_1;
            }
        }
    }
}
