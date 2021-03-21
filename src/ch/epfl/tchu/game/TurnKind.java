package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public enum TurnKind {
    DRAW_TICKETS, DRAW_CARDS, CLAIM_ROUTE;
    public static List<TurnKind> ALL = List.of(TurnKind.values());
}
