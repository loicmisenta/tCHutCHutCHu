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
 * Classe contenant les méthodes qui générent les textes
 * décrivant le déroulement d'une partie
 */
public final class Info {
    private String playerName;

    /**
     * Constructeur qui prend en paramètre le
     * @param playerName nom du joueur
     */
    public Info(String playerName){
        this.playerName = playerName;
    }

    /**
     * @param card la carte
     * @param count la quantité de cartes
     * @return le nom de la carte donnée en français
     */
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

    /**
     *
     * @param playerNames noms des joueurs
     * @param points le nb de point
     * @return le message qui dit que les joueurs ont terminé la partie ex æqo
     * en ayant chacun remporté le nb de points donné
     */
    public static String draw(List<String> playerNames, int points){
        return String.format(StringsFr.DRAW, (playerNames.get(0) + StringsFr.AND_SEPARATOR + playerNames.get(1)), points);
    }

    /**
     * @return  le message déclarant que le joueur jouera en premier
     */
    public String willPlayFirst(){
        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }

    /**
     *
     * @param count le nombre donné de billets
     * @return un message déclarant que le joueur a tiré des billets
     */
    public String keptTickets(int count){
        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     *
     * @return  le message déclarant que le joueur peut jouer
     */
    public String canPlay(){
        return String.format(StringsFr.CAN_PLAY, playerName);
    }

    /**
     * @param count le nombre donné de billets
     * @return  le message déclarant que le joueur a tiré des billets
     */
    public String drewTickets(int count){
        return String.format(StringsFr.DREW_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * @return e message déclarant que le joueur a tiré une carte du sommet de la pioche
     */
    public String drewBlindCard(){
        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }

    /**
     * @param card la carte disposée face visible
     * @return le message déclarant que le joueur a tiré une carte
     */
    public String drewVisibleCard(Card card){
        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }

    /**
     * @param route la route donnée
     * @param cards les cartes données
     * @return le message déclarant que le joueur s'est emparé d'une route
     */
    public String claimedRoute(Route route, SortedBag<Card> cards){

        String nomRoute = route.station1().toString() + StringsFr.EN_DASH_SEPARATOR + route.station2().toString();
        return String.format(StringsFr.CLAIMED_ROUTE, playerName, nomRoute, cardToString(cards));
    }

    /**
     * @param route la route donnée
     * @param initialCards cartes untilisées pour s'emparer de la route
     * @return le message déclarant que le joueur désire s'emparer de la route en tunnel
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards){
        String nomRoute = route.station1().toString() + StringsFr.EN_DASH_SEPARATOR + route.station2().toString();
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, nomRoute, cardToString(initialCards));
    }

    /**
     * @param drawnCards
     * @param additionalCost
     * @return
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost){
        if (additionalCost==0){
            return String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardToString(drawnCards)) + StringsFr.NO_ADDITIONAL_COST;

        }else
            return String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardToString(drawnCards)) +
                String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost));
    }
    public String didNotClaimRoute(Route route){
        String nomRoute = route.station1().toString() + StringsFr.EN_DASH_SEPARATOR + route.station2().toString();
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, nomRoute);
    }
    public String lastTurnBegins(int carCount){
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }
    public String getsLongestTrailBonus(Trail longestTrail){
        String longChemin = longestTrail.station1().toString() + StringsFr.EN_DASH_SEPARATOR + longestTrail.station2().toString();
        return String.format(StringsFr.GETS_BONUS, playerName, longChemin);
    }
    public String won(int points, int loserPoints){
        return String.format(StringsFr.WINS, playerName, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }

    /**
     *A MODIFIER ERREUR
     *
     * @param cards
     * @return
     */
    private String cardToString(SortedBag<Card> cards){

        String cardsString = "";
        List<String> listString = new ArrayList<>();

        for (int i = 0; i < cards.size()-1; i++) {
            int n = cards.countOf(cards.get(i));
            listString.add( n + " " + cardName(cards.get(i), n));
            i += n-1;
        }

        cardsString += String.join(", ", listString);

        //ERREUR !!!!
        cardsString += StringsFr.AND_SEPARATOR + cards.countOf(cards.get(cards.size()-1)) + " "
                + cardName(cards.get(cards.size()-1), cards.countOf(cards.get(cards.size()-1)));
        return cardsString;
    }

}
