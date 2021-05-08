package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 Représente les huit couleurs utilisées dans le jeu
 pour colorer les cartes wagon et les routes.

 */

public enum Color{
    BLACK, VIOLET, BLUE, GREEN, YELLOW,
    ORANGE, RED, WHITE;

    public static final List<Color> ALL = List.of(Color.values());
    public static final int COUNT = ALL.size();

}

