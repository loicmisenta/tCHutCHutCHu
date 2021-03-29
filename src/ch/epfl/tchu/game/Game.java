package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public final class Game {
    private static GameState gameState;
    private static Random randomRng;

    /**
     * Fait jouer une partie
     *
     * @param players     joueurs
     * @param playerNames les noms des joueurs
     * @param tickets     les billets dissponibles
     * @param rng         générateur aléatoire de nombres
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {
        Preconditions.checkArgument((players.size() == 2) || (playerNames.size() == 2));

        //Le début de la partie
        Map<PlayerId, Info> infoMap = new EnumMap<>(PlayerId.class);
        randomRng = rng;
        gameState = GameState.initial(tickets, rng);
        players.forEach(((playerId, player) -> {
            players.get(playerId).initPlayers(playerId, playerNames);
            infoMap.put(playerId, new Info(playerNames.get(playerId)));
            players.get(playerId).receiveInfo(infoMap.get(playerId).willPlayFirst()); //info qui va jouer
            players.get(playerId).setInitialTicketChoice(gameState.playerState(playerId).tickets());
            players.get(playerId).receiveInfo(infoMap.get(playerId).drewTickets(Constants.INITIAL_TICKETS_COUNT)); //info tickets init
            //TODO on change pas le gamestate????????
            //modifier dedans !!!
            updateState(players.get(playerId), gameState);

            players.get(playerId).chooseInitialTickets();
            players.get(playerId).receiveInfo(infoMap.get(playerId).keptTickets(gameState.playerState(playerId).ticketCount())); //info tickets choisis
        }));

        //La déroulement de la partie
        while (!gameState.lastTurnBegins()) {
            PlayerId currentId = gameState.currentPlayerId();
            Player joueurCourant = players.get(currentId);

            joueurCourant.receiveInfo(infoMap.get(currentId).canPlay()); //info tour commence
            updateState(joueurCourant, gameState);

            switch (joueurCourant.nextTurn()) {

                case DRAW_TICKETS:
                    joueurCourant.receiveInfo(infoMap.get(currentId).drewTickets(Constants.IN_GAME_TICKETS_COUNT)); //info tire des billets
                    SortedBag<Ticket> ticketsChoisis = joueurCourant.chooseTickets(gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT));
                    joueurCourant.receiveInfo(infoMap.get(currentId).keptTickets(ticketsChoisis.size())); //info tickets choisis
                    break;

                case DRAW_CARDS:
                    for (int i = 0; i < 2; i++) {   //TODO ES BIEN UNE for i ?!?!
                        int cartePioche = joueurCourant.drawSlot(); // TODO Faut-il stocker la valeur?
                        if (cartePioche == 1) {

                            joueurCourant.receiveInfo(infoMap.get(currentId).drewBlindCard()); //info prends carte pioche

                            gameState = gameState.withBlindlyDrawnCard(); //TODO POUR LE REDEFINIR ?!

                            //joueurCourant.updateState(newGameState, gameState.currentPlayerState());//TODO devrait etre avant?!
                        } else {
                            joueurCourant.receiveInfo(infoMap.get(currentId).drewVisibleCard(gameState.cardState().faceUpCard(cartePioche)));//info prends carte pioche
                            gameState = gameState.withDrawnFaceUpCard(cartePioche); //TODO PEUT ETRE DANS LAUTRE SENS

                        }
                    }
                    updateState(joueurCourant, gameState);
                    break;
                case CLAIM_ROUTE:
                    Route claimRoute = joueurCourant.claimedRoute();
                    SortedBag<Card> claimCards = joueurCourant.initialClaimCards();

                    if ((joueurCourant.claimedRoute().level() == Route.Level.UNDERGROUND)) {
                        joueurCourant.receiveInfo(infoMap.get(currentId).attemptsTunnelClaim(claimRoute,claimCards));
                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                            gameState = deckisEmpty(); //redefnir si vide TODO comment faire autrement?
                            joueurCourant.drawSlot(); // à chaque fois faire une action sur le Player
                            gameState = gameState.withBlindlyDrawnCard();

                            //if (claimRoute.additionalClaimCardsCount(claimCards, ))){ //TODO avoir les 3 cartes piochés
                            //     joueurCourant.chooseAdditionalCards( ); //en paramètre le 3 cartes piochés

                            //TODO créer !!
                            if(gameState.cardState() ){ //si peut claim le tunnel TODO quelle méthode ?
                            joueurCourant.receiveInfo(infoMap.get(currentId).claimedRoute(claimRoute, claimCards));}
                        }
                        gameState = gameState.forNextTurn();

                    } else {
                        joueurCourant.receiveInfo(infoMap.get(currentId).claimedRoute(claimRoute, claimCards));
                    }
                    break;
            }
        }

        // TODO pas une foreach ?
        players.forEach(((playerId, player) -> {
            players.get(playerId).receiveInfo(infoMap.get(playerId).lastTurnBegins(gameState.playerState(playerId).carCount()));
            //s'inclinche avec le dernier joueur?

        }));
    }


    //TODO MAP FOREACH

    private static void updateState(Player joueurCourant, GameState gameState){
        joueurCourant.updateState(gameState, gameState.currentPlayerState());
    }

    //TODO A VOIR COMMENT FAIRE (PEUT ETRE DES VARIABLES AU DEBUT DE LA CLASSE)
    private static void receiveInfo(Player joueurCourant, String string){

        //joueurCourant.receiveInfo(infoMap.get(currentId).keptTickets(ticketsChoisis.size()));
    }

    private static GameState deckisEmpty(){
        if(gameState.cardState().isDeckEmpty()) {
            return gameState.withCardsDeckRecreatedIfNeeded(randomRng);
        } else return gameState;
    }//TODO ON COMPRENDS PAS :(
}
