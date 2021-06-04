package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * La classe Serdes contient la totalité des serdes utiles au projet.
 */
public final class Serdes {
    /**
     * Constructeur public de Serdes qui rend cette classe non-instanciable
     */
    private Serdes(){}

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

    /**
     * serde qui sérialise et déserialise un int
     */
    public static final Serde<Integer> intSerde = Serde.of(i -> Integer.toString(i), Integer::parseInt);

    /**
     * Serdes qui serialise et deserialise un String
     */
    public static final Serde<String> stringSerde = Serde.of(i -> Base64.getEncoder().encodeToString(i.getBytes(StandardCharsets.UTF_8)), i -> new String(Base64.getDecoder().decode(i), StandardCharsets.UTF_8));

    /**
     * Serdes qui serialise et deserialise un player
     */
    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);

    /**
     * Serdes qui serialise et deserialise un turnKind
     */
    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);

    /**
     * Serdes qui serialise et deserialise une carte
     */
    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);

    /**
     * Serdes qui serialise et deserialise une route
     */
    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());

    /**
     * Serdes qui serialise et deserialise un ticket
     */
    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets(5));

    /**
     * Serdes qui serialise et deserialise une liste de String
     */
    public static final Serde<List<String>> listStringSerde = Serde.listOf( stringSerde, DELIMITER_VIRGULE);

    /**
     * Serdes qui serialise et deserialise une liste de carte
     */
    public static final Serde<List<Card>> listCardSerde = Serde.listOf(cardSerde, DELIMITER_VIRGULE);

    /**
     * Serdes qui serialise et deserialise une liste de route
     */
    public static final Serde<List<Route>> listRouteSerde = Serde.listOf(routeSerde, DELIMITER_VIRGULE);

    /**
     * Serdes qui serialise et deserialise un SortedBag de Cartes
     */
    public static final Serde<SortedBag<Card>> sortedBagOfCardSerde = Serde.bagOf(cardSerde, DELIMITER_VIRGULE);

    /**
     * Serdes qui serialise et deserialise un PublicGameState
     */
    public static final Serde<SortedBag<Ticket>> sortedBagOfTicketSerde = Serde.bagOf(ticketSerde, DELIMITER_VIRGULE);

    /**
     * Serdes qui serialise et deserialise und Liste de SortedBag de cartes
     */
    public static final Serde<List<SortedBag<Card>>> listSortedBagOfCard = Serde.listOf(sortedBagOfCardSerde, DELIMITER_POINT_VIRGULE);

    /**
     * Serdes qui serialise et deserialise un PublicCardState
     */
    public static final Serde<PublicCardState> publicCardStateSerde = Serde.of(i -> String.join(DELIMITER_POINT_VIRGULE, listCardSerde.serialize(i.faceUpCards()), intSerde.serialize(i.deckSize()), intSerde.serialize(i.discardsSize())), Serdes::stringToPublicCardState);

    /**
     * Serdes qui serialise et deserialise un PublicPlayerState
     */
    public static final Serde<PublicPlayerState> publicPlayerStateSerde = Serde.of(i -> String.join(DELIMITER_POINT_VIRGULE, intSerde.serialize(i.ticketCount()), intSerde.serialize(i.cardCount()), listRouteSerde.serialize(i.routes())), Serdes::stringToPublicPlayerState);

    /**
     * Serdes qui serialise et deserialise un playerState
     */
    public static final Serde<PlayerState> playerStateSerde = Serde.of(i -> String.join(DELIMITER_POINT_VIRGULE, sortedBagOfTicketSerde.serialize(i.tickets()), sortedBagOfCardSerde.serialize(i.cards()), listRouteSerde.serialize(i.routes())), Serdes::stringToPlayerState);

    /**
     * Serdes qui serialise et deserialise un PublicGameState
     */
    public static final Serde<PublicGameState> publicGameStateSerde = Serde.of(i -> {
        String stringSerializeMapPlayer = "";

        for (PlayerId playerId : PlayerId.ALL.subList(0, i.playerCount())) {

            if(stringSerializeMapPlayer.equals("")){
                stringSerializeMapPlayer = publicPlayerStateSerde.serialize(i.playerState(playerId));
            } else {
                stringSerializeMapPlayer = String.join(":", stringSerializeMapPlayer, publicPlayerStateSerde.serialize(i.playerState(playerId)));
            }
        };
        return String.join(DELIMITER_DEUX_POINTS, intSerde.serialize(i.ticketsCount()), publicCardStateSerde.serialize(i.cardState()),
                playerIdSerde.serialize(i.currentPlayerId()), stringSerializeMapPlayer, playerIdSerde.serialize(i.lastPlayer()));
    }, Serdes::stringToPublicGameState);



    private static PublicGameState stringToPublicGameState(String string){
        String[] listeString = string.split(Pattern.quote( DELIMITER_DEUX_POINTS), -1);
        int ticketsCount = intSerde.deserialize(listeString[0]);
        PublicCardState cardState = stringToPublicCardState(listeString[1]);
        PlayerId currentPlayerId = playerIdSerde.deserialize(listeString[2]);
        Map<PlayerId, PublicPlayerState> mapPlayerState = new EnumMap<>(PlayerId.class);

        int i = 3;
        for (PlayerId playerId: PlayerId.ALL.subList(0, listeString.length - 4)) {
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

    private static Trail stringToTrail(String string){

        return new Trail(listRouteSerde.deserialize(string));
    }
    public static final Serde<Trail> trailSerde = Serde.of(i-> listRouteSerde.serialize(i.getRoutes()), Serdes::stringToTrail);

    private static List<Trail> stringToTrailList(String string){
        String[] listStringTrail = string.split(Pattern.quote(DELIMITER_DEUX_POINTS), -1);
        List<Trail> listTrail  = new ArrayList<>();
        for (String s : listStringTrail) {
            listTrail.add(trailSerde.deserialize(s));
        }
        return listTrail;
    }

    public static final Serde<List<Trail>> listTrailSerde = Serde.listOf(trailSerde, DELIMITER_DEUX_POINTS);

}