package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public final class Info {
    private String playerName;

    public Info(String playerName){
        this.playerName = playerName;
    }

    public static String cardName(Card card, int count){
        switch (card){
            case BLACK: return StringsFr.BLACK_CARD + StringsFr.plural(count);
            case YELLOW: return StringsFr.YELLOW_CARD + StringsFr.plural(count);
            case RED: return StringsFr.RED_CARD + StringsFr.plural(count);
            case GREEN: return StringsFr.GREEN_CARD + StringsFr.plural(count);
            case WHITE: return StringsFr.WHITE_CARD + StringsFr.plural(count);
            case ORANGE: return StringsFr.ORANGE_CARD + StringsFr.plural(count);
            case VIOLET: return StringsFr.VIOLET_CARD + StringsFr.plural(count);
            case BLUE: return StringsFr.BLUE_CARD + StringsFr.plural(count);
            case LOCOMOTIVE: return StringsFr.LOCOMOTIVE_CARD + StringsFr.plural(count);
            default: return "";
        }
    }

    public static String draw(List<String> playerNames, int points){
        return String.format(StringsFr.DRAW, (playerNames.get(0) + StringsFr.AND_SEPARATOR + playerNames.get(1)), points);
    }
    public String willPlayFirst(){
        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }
    public String keptTickets(int count){
        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count, StringsFr.plural(count));
    }
    public String canPlay(){
        return String.format(StringsFr.CAN_PLAY, playerName);
    }
    public String drewTickets(int count){
        return String.format(StringsFr.DREW_TICKETS, playerName, count, StringsFr.plural(count));
    }
    public String drewBlindCard(){
        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }
    public String drewVisibleCard(Card card){
        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }
    public String claimedRoute(Route route, SortedBag<Card> cards){
        String nomRoute = route.station1().toString() + StringsFr.EN_DASH_SEPARATOR + route.station2().toString();
        return String.format(StringsFr.CLAIMED_ROUTE, playerName, nomRoute, cards.toString());
    }
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards){
        String nomRoute = route.station1().toString() + StringsFr.EN_DASH_SEPARATOR + route.station2().toString();
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, nomRoute, initialCards.toString());
    }
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost){
        String additionalString = "";
        if (additionalCost==0){
            return StringsFr.NO_ADDITIONAL_COST;

        }
        else{
            List<String> listString = new ArrayList<>();

            for (int i = 0; i < drawnCards.size()-1; i++) {
                int n = drawnCards.countOf(drawnCards.get(i));
                listString.add(cardName(drawnCards.get(i), n));
            }
            additionalString += String.join(", ", listString);
            additionalString += StringsFr.AND_SEPARATOR + cardName(drawnCards.get(drawnCards.size()-1), drawnCards.countOf(drawnCards.get(drawnCards.size()-1)));

        }
        return String.format(StringsFr.ADDITIONAL_CARDS_ARE, additionalString) + String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost));
    }
    public String didNotClaimRoute(Route route){
        String nomRoute = route.station1().toString() + StringsFr.EN_DASH_SEPARATOR + route.station2().toString();
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, nomRoute);
    }
    public String lastTurnBegins(int carCount){
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }
    String getsLongestTrailBonus(Trail longestTrail){
        String longChemin = longestTrail.station1().toString() + StringsFr.EN_DASH_SEPARATOR + longestTrail.station2().toString();
        return String.format(StringsFr.GETS_BONUS, playerName, longChemin);
    }
    String won(int points, int loserPoints){
        return String.format(StringsFr.WINS, playerName, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }

}
