package ch.epfl.tchu.game;

import java.util.List;

/**
 Représente les huit couleurs utilisées dans le jeu
 pour colorer les cartes wagon et les routes.

 */
public enum Color{
    BLACK("noir"), VIOLET("violet"), BLUE("bleu"), GREEN("vert"), YELLOW("jaune"),
    ORANGE("orange") , RED("rouge") , WHITE("blanc");
    private String value;
    private Color (String value){
        this.value = value;
    }
    public static final List<Color> ALL = List.of(Color.values());
    public static final int COUNT = ALL.size();

}

