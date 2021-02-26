package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 Cette classe représente les différents types de cartes du jeu,
 donc les huit types de cartes wagon (chacun avec une couleur associé),
 et le type de carte locomotive.

 */
public enum Card {
    BLACK("wagon noir", Color.BLACK), VIOLET("wagon violet", Color.VIOLET), BLUE("wagon bleu", Color.BLUE),
    GREEN("wagon vert", Color.GREEN), YELLOW("wagon jaune", Color.YELLOW), ORANGE("wagon orange", Color.ORANGE),
    RED("wagon rouge", Color.RED), WHITE("wagon blanc", Color.RED),
    LOCOMOTIVE("locomotive", null);

    private String value;
    private Color color;

    private Card(String value, Color color) {
        this.value = value;
        this.color = color;
    }

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();
    public static final List<Card> CARS = List.of(BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);

    public static Card of(Color color) { return valueOf(color.name());}
}






