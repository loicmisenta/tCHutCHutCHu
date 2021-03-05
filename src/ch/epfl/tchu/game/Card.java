package ch.epfl.tchu.game;

import java.awt.*;
import java.net.SocketOption;
import java.util.ArrayList;
import java.util.List;

/**
 Cette classe représente les différents types de cartes du jeu,
 donc les huit types de cartes wagon (chacun avec une couleur associé),
 et le type de carte locomotive.

 */
public enum Card {
    BLACK(Color.BLACK), VIOLET( Color.VIOLET), BLUE( Color.BLUE),
    GREEN(Color.GREEN), YELLOW( Color.YELLOW), ORANGE( Color.ORANGE),
    RED( Color.RED), WHITE( Color.WHITE),
    LOCOMOTIVE( null);

    private final Color color;


    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * Constructeur de Card qui prend en compte:
     * @param color la coiuleur attribuée à un wagon
     */
    private Card( Color color) {
        this.color = color;
    }


    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();
    public static final List<Card> CARS = List.of(BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);


    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * Methode qui prend en compte:
     * @param color la couleur de la carte
     * @return le wagon qui lui est associé
     */
     public static Card of(Color color) {
         return valueOf(color.name());
     }



    /**
     * @author loicmisenta
     * @author lagutovaalexandra
     *
     * Methode qui
     * @return la couleur
     */
    public Color color() { return color;}

}






