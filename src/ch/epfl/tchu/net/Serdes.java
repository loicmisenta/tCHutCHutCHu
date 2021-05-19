package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public final class Serdes {

    /**
     * Caractère point-virgule
     */
    public static final String DELIMITER_POINT_VIRGULE = ";";
    /**
     * Caractère virgule
     */
    public static final String DELIMITER_VIRGULE = ",";
    /**
     * Caractère deux-points
     */
    public static final String DELIMITER_DEUX_POINTS = ":";

    public static final Serde<Integer> intSerde = Serde.of(i -> Integer.toString(i), Integer::parseInt);
    public static final Serde<String> stringSerde = Serde.of(i -> Base64.getEncoder().encodeToString(i.getBytes(StandardCharsets.UTF_8)), i -> new String(Base64.getDecoder().decode(i), StandardCharsets.UTF_8));
    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);
    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);
    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);
    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());
    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());

    public static final Serde<List<String>> listStringSerde = Serde.listOf( stringSerde, DELIMITER_VIRGULE);
    public static final Serde<List<Card>> listCardSerde = Serde.listOf(cardSerde, DELIMITER_VIRGULE);
    public static final Serde<List<Route>> listRouteSerde = Serde.listOf(routeSerde, DELIMITER_VIRGULE);
    public static final Serde<SortedBag<Card>> sortedBagOfCardSerde = Serde.bagOf(cardSerde, DELIMITER_VIRGULE);
    public static final Serde<SortedBag<Ticket>> sortedBagOfTicketSerde = Serde.bagOf(ticketSerde, DELIMITER_VIRGULE);
    public static final Serde<List<SortedBag<Card>>> listSortedBagOfCard = Serde.listOf(sortedBagOfCardSerde, DELIMITER_POINT_VIRGULE);

    public static final Serde<PublicCardState> publicCardStateSerde = Serde.of(i -> String.join(DELIMITER_POINT_VIRGULE, listCardSerde.serialize(i.faceUpCards()), intSerde.serialize(i.deckSize()), intSerde.serialize(i.discardsSize())), Serdes::stringToPublicCardState);
    public static final Serde<PublicPlayerState> publicPlayerStateSerde = Serde.of(i -> String.join(DELIMITER_POINT_VIRGULE, intSerde.serialize(i.ticketCount()), intSerde.serialize(i.cardCount()), listRouteSerde.serialize(i.routes())), Serdes::stringToPublicPlayerState);
    public static final Serde<PlayerState> playerStateSerde = Serde.of(i -> String.join(DELIMITER_POINT_VIRGULE, sortedBagOfTicketSerde.serialize(i.tickets()), sortedBagOfCardSerde.serialize(i.cards()), listRouteSerde.serialize(i.routes())), Serdes::stringToPlayerState);
    public static final Serde<PublicGameState> publicGameStateSerde = Serde.of(i -> {
        String stringSerializeMapPlayer = "";
        for (PlayerId playerId : PlayerId.ALL) {
            stringSerializeMapPlayer = String.join(DELIMITER_DEUX_POINTS, publicPlayerStateSerde.serialize(i.playerState(playerId)), stringSerializeMapPlayer);
        }
        return String.join(DELIMITER_DEUX_POINTS, intSerde.serialize(i.ticketsCount()), publicCardStateSerde.serialize(i.cardState()),
                playerIdSerde.serialize(i.currentPlayerId()), stringSerializeMapPlayer, playerIdSerde.serialize(i.lastPlayer()));
        }, Serdes::stringToPublicGameState);

    public Serdes(){}

    private static PublicGameState stringToPublicGameState(String string){
        String[] listeString = string.split(Pattern.quote( DELIMITER_DEUX_POINTS), -1); //TODO
        int ticketsCount = intSerde.deserialize(listeString[0]);
        PublicCardState cardState = stringToPublicCardState(listeString[1]);
        PlayerId currentPlayerId = playerIdSerde.deserialize(listeString[2]);
        Map<PlayerId, PublicPlayerState> mapPlayerState = new EnumMap<>(PlayerId.class);

        int i = 3;
        for (PlayerId playerId: PlayerId.ALL) {
            mapPlayerState.put(playerId, stringToPublicPlayerState(listeString[i++]));
        }
        PlayerId lastPlayer;
        if(listeString[listeString.length-1].length() == 0){
            lastPlayer = null;
        } else {
            lastPlayer = playerIdSerde.deserialize(listeString[listeString.length-1]);}
        return new PublicGameState(ticketsCount, cardState, currentPlayerId, mapPlayerState, lastPlayer);
    }


    private static PlayerState stringToPlayerState(String string){
        String[] listeString = string.split(Pattern.quote(DELIMITER_POINT_VIRGULE), -1);
        SortedBag<Ticket> tickets = sortedBagOfTicketSerde.deserialize(listeString[0]);
        SortedBag<Card> cards = sortedBagOfCardSerde.deserialize(listeString[1]);
        List<Route> routes = listRouteSerde.deserialize(listeString[2]);
        return new PlayerState(tickets, cards, routes);

    }

    private static PublicPlayerState stringToPublicPlayerState(String string){
        String[] listeString = string.split(Pattern.quote(DELIMITER_POINT_VIRGULE), -1);
        int ticketCount = intSerde.deserialize(listeString[0]);
        int cardCount = intSerde.deserialize(listeString[1]);
        List<Route> routes = listRouteSerde.deserialize(listeString[2]);
        return new PublicPlayerState(ticketCount, cardCount, routes);

    }


    private static PublicCardState stringToPublicCardState(String string){
        String[] listeString = string.split(Pattern.quote(DELIMITER_POINT_VIRGULE), -1);
        List<Card> faceUpCards = listCardSerde.deserialize(listeString[0]);
        int decksize = intSerde.deserialize(listeString[1]);
        int discardsize = intSerde.deserialize(listeString[2]);
        return new PublicCardState(faceUpCards, decksize, discardsize);
    }

    
}
