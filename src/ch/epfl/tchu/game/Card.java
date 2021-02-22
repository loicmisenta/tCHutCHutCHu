package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

public enum Card {
    BLACK("wagon noir"), VIOLET("wagon violet"), BLUE("wagon bleu"), GREEN("wagon vert"),
    YELLOW("wagon jaune"), ORANGE("wagon orange"), RED("wagon rouge"), WHITE("wagon blanc"),
    LOCOMOTIVE("locomotive");
    private String value;
    private Card (String value){
        this.value = value;
    }
    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();
    //public static final List<Card> CARS = new ArrayList<Card>(BLACK , VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);

}
