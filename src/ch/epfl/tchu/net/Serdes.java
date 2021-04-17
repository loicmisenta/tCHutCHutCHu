package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class Serdes {

    public static final Serde<Integer> intSerde = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt);
    public static final Serde<String> stringSerde = Serde.of(
            i -> Base64.getEncoder().encodeToString(String.getBytes(StandardCharsets.UTF_8)),
            Integer::parseInt);
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

    public static final Serde<PublicCardState> publicCardStateSerde = Serde.of(String.join(";", listCardSerde.serialize(), intSerde.serialize(), intSerde.serialize()), stringToPublicCardState());



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
