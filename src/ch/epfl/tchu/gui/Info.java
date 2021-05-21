package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Classe contenant les méthodes qui générent les textes
 * décrivant le déroulement d'une partie
 */
public final class Info {
    private final String playerName;

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
            case BLACK: return cardNameReturn(StringsFr.BLACK_CARD, count);
            case YELLOW: return cardNameReturn(StringsFr.YELLOW_CARD, count);
            case RED: return cardNameReturn(StringsFr.RED_CARD, count);
            case GREEN: return cardNameReturn(StringsFr.GREEN_CARD, count);
            case WHITE: return cardNameReturn(StringsFr.WHITE_CARD, count);
            case ORANGE: return cardNameReturn(StringsFr.ORANGE_CARD, count);
            case VIOLET: return cardNameReturn(StringsFr.VIOLET_CARD, count);
            case BLUE: return cardNameReturn(StringsFr.BLUE_CARD, count);
            case LOCOMOTIVE: return cardNameReturn(StringsFr.LOCOMOTIVE_CARD, count);
            default: return "";
        }
    }

    /**
     *
     * @param playerNames noms des joueurs
     * @param points le nb de point
     * @return le message qui dit que les joueurs ont terminé la partie exæqo
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
     * @return le message déclarant que le joueur a tiré une carte du sommet de la pioche
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
     * @param drawnCards cartes additionnelles tirés
     * @param additionalCost un coût additionel des cartes données
     * @return le message déclarant que le joueur a tiré les trois cartes additionnelles données, et qu'elles impliquent un coût additionel du nombre de cartes donné
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost){
        if (additionalCost==0){
            return String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardToString(drawnCards)) + StringsFr.NO_ADDITIONAL_COST;

        }else
            return String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardToString(drawnCards)) +
                String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost));
    }

    /**
     * @param route tunnel donné
     * @return e message déclarant que le joueur n'a pas pu (ou voulu) s'emparer d'un tunnel
     */
    public String didNotClaimRoute(Route route){
        String nomRoute = route.station1().toString() + StringsFr.EN_DASH_SEPARATOR + route.station2().toString();
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, nomRoute);
    }

    /**
     * @param carCount nb de wagon
     * @return un message  déclarant que le dernier tour commence et que le joueur
     * a un nb de wagons donnés
     */
    public String lastTurnBegins(int carCount){
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     * @param longestTrail le chemin le plus long
     * @return un message  déclarant que le joueur obtient le bonus de fin de partie
     * avec le chemin le plus long
     */
    public String getsLongestTrailBonus(Trail longestTrail){
        String longChemin = longestTrail.station1().toString() + StringsFr.EN_DASH_SEPARATOR + longestTrail.station2().toString();
        return String.format(StringsFr.GETS_BONUS, playerName, longChemin);
    }

    /**
     * @param points nb de points données
     * @param loserPoints points de l'adversaire
     * @return un message déclarant le nom du gagnant
     */
    public String won(int points, int loserPoints){
        return String.format(StringsFr.WINS, playerName, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }

    /**
     * Methode utilisée pour construire les strings
     * @param cards les cartes
     * @return un string composé des cartes données
     */

    public static String cardToString(SortedBag<Card> cards){
        String cardsString = "";
        List<String> listString = new ArrayList<>();

        //Cas spécial dans lequel cards est composé d'un seul élement
        if (cards.size() == 1){
            int nombreCarte = cards.countOf(cards.get(0));
            listString.add( nombreCarte + " " + cardName(cards.get(0), nombreCarte));
        } else {
        //Boucle principale, crée les cartes
            for (int i = 0; i < cards.size() ; i++) {
                int nombreCartes = cards.countOf(cards.get(i));
                listString.add( nombreCartes + " " + cardName(cards.get(i), nombreCartes));
                i += nombreCartes - 1;
            }
        }
        //Affichage des cartes
        if(listString.size() == 1){
            cardsString += listString.get(0);
        } else {
            cardsString += String.join(", ", listString.subList(0, listString.size() - 1));
            cardsString += StringsFr.AND_SEPARATOR + listString.get(listString.size() - 1);
        }
        return cardsString;
    }

    /**
     * utilisée pour optimser la méthode de cardName
     * @param string prends le StringFr de la couleur
     * @param count le nombre de carte
     * @return le string correspondant
     */
    private static String cardNameReturn(String string, int count){
        return string + StringsFr.plural(count);
    }

}
