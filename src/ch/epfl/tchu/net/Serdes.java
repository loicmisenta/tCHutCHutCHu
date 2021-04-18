package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public final class Serdes {
    public static final Serde<Integer> intSerde = Serde.of(i -> Integer.toString(i), Integer::parseInt);
    public static final Serde<String> stringSerde = Serde.of(i -> Base64.getEncoder().encodeToString(String.getBytes(StandardCharsets.UTF_8)), Integer::parseInt);
    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);
    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);
    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);
    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());
    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());

    public static final Serde<List<String>> listStringSerde = Serde.listOf(stringSerde, ",");
    public static final Serde<List<Card>> listCardSerde = Serde.listOf(cardSerde, ",");
    public static final Serde<List<Route>> listRouteSerde = Serde.listOf(routeSerde, ",");
    public static final Serde<SortedBag<Card>> sortedBagOfCardSerde = Serde.bagOf(cardSerde, ",");
    public static final Serde<SortedBag<Ticket>> sortedBagOfTicketSerde = Serde.bagOf(ticketSerde, ",");
    public static final Serde<List<SortedBag<Card>>> listSortedBagOfCard = Serde.listOf(sortedBagOfCardSerde, ";");
    //TODO QUE METTRE DANS LES PARANTHESES ?!?!?!
    public static final Serde<PublicCardState> publicCardStateSerde = Serde.of(String.join(";", listCardSerde.serialize(), intSerde.serialize(), intSerde.serialize()), stringToPublicCardState());
    public static final Serde<PublicPlayerState> publicPlayerStateSerde = Serde.of(String.join(";", intSerde.serialize(), intSerde.serialize(), listRouteSerde.serialize()), stringToPublicPlayerState());
    public static final Serde<PlayerState> playerStateSerde = Serde.of(String.join(";", sortedBagOfTicketSerde.serialize(), sortedBagOfCardSerde.serialize(), listRouteSerde.serialize()), stringToPlayerState());
    public static final Serde<PublicGameState> publicGameStateSerde = Serde.of(String.join(":", intSerde.serialize(), publicCardStateSerde.serialize(), playerIdSerde.serialize(), playerStateSerde.serialize(), playerStateSerde.serialize() , playerIdSerde.serialize()), stringToPublicGameState());

    private static PublicGameState stringToPublicGameState(String string){
        List<String> listeString = stringSplit(string,":");
        int ticketsCount = intSerde.deserialize(listeString.get(0));
        PublicCardState cardState = stringToPublicCardState(listeString.get(1));
        PlayerId currentPlayerId = playerIdSerde.deserialize(listeString.get(2));
        Map<PlayerId, PublicPlayerState> mapPlayerState = new EnumMap<PlayerId, PublicPlayerState>(PlayerId.class);
        PublicPlayerState playerState1 = stringToPlayerState(listeString.get(3));
        PublicPlayerState playerState2 = stringToPlayerState(listeString.get(4));
        mapPlayerState.put(PlayerId.PLAYER_1, playerState1);
        mapPlayerState.put(PlayerId.PLAYER_2, playerState2);
        PlayerId lastPlayer = playerIdSerde.deserialize(listeString.get(5));
        return new PublicGameState(ticketsCount, cardState, currentPlayerId, mapPlayerState, lastPlayer);
    }


    private static PlayerState stringToPlayerState(String string){
        List<String> listeString = stringSplit(string,";");
        List<String> listeTicketsString = stringSplit(listeString.get(0), ",");
        List<Ticket> tickets = new ArrayList<>();
        //TODO comment simplifier ??
        for (String t: listeTicketsString) {
            tickets.add(ticketSerde.deserialize(t));
        }
        List<String> listeCardString = stringSplit(listeString.get(1), ",");
        List<Card> cards = new ArrayList<>();
        for (String c: listeCardString) {
            cards.add(cardSerde.deserialize(c));
        }
        List<String> listeRouteString = stringSplit(listeString.get(2), ",");
        List<Route> routes = new ArrayList<>();
        for (String s: listeRouteString) {
            routes.add(routeSerde.deserialize(s));
        }
        return new PlayerState(SortedBag.of(tickets), SortedBag.of(cards), routes);

    }

    private static PublicPlayerState stringToPublicPlayerState(String string){
        List<String> listeString = stringSplit(string,";");
        List<String> listeRouteString = stringSplit(listeString.get(2), ",");
        List<Route> routes = new ArrayList<>();
        for (String s: listeRouteString) {
            routes.add(routeSerde.deserialize(s));
        }
        int ticketCount = intSerde.deserialize(listeString.get(0));
        int cardCount = intSerde.deserialize(listeString.get(1));
        return new PublicPlayerState(ticketCount, cardCount, routes);

    }


    private static PublicCardState stringToPublicCardState(String string){
        List<String> listeString = stringSplit(string,";");
        List<String> listeCardString = stringSplit(listeString.get(0), ",");
        List<Card> faceUpCards = new ArrayList<>();
        for (String s: listeCardString) {
            faceUpCards.add(cardSerde.deserialize(s));
        }
        int decksize = intSerde.deserialize(listeString.get(1));
        int discardsize = intSerde.deserialize(listeString.get(2));
        return new PublicCardState(faceUpCards, decksize, discardsize);

    }
    private static List<String> stringSplit(String string, String stringDeLimit){
        String[] stringOfDes = string.split(Pattern.quote(stringDeLimit), -1);
        List<String> liste = new ArrayList<>();
        Collections.addAll(liste, stringOfDes);
        return liste;
    }

    
}
