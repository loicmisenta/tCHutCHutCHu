package ch.epfl.tchu.net;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 */

/**
 * Type énuméré composé des messages que le serveur peut envoyer aux clients
 */
public enum MessageId {
    INIT_PLAYERS, RECEIVE_INFO, UPDATE_STATE, SET_INITIAL_TICKETS,
    CHOOSE_INITIAL_TICKETS, NEXT_TURN, CHOOSE_TICKETS,
    DRAW_SLOT, ROUTE, CARDS, CHOOSE_ADDITIONAL_CARDS
}
