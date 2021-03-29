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
        gameState = GameState.initial(tickets, rng);


        Map<PlayerId, SortedBag<Ticket>> mapTicketsChoisis= new EnumMap<>(PlayerId.class);
        //TODO PAS UTILISER LA METHODE receiveInfo ?!
        players.forEach(((playerId, player) -> {
            players.get(playerId).initPlayers(playerId, playerNames);
            infoMap.put(playerId, new Info(playerNames.get(playerId)));
            players.get(playerId).receiveInfo(infoMap.get(playerId).willPlayFirst()); //info qui va jouer
            players.get(playerId).setInitialTicketChoice(gameState.playerState(playerId).tickets());
            players.get(playerId).receiveInfo(infoMap.get(playerId).drewTickets(Constants.INITIAL_TICKETS_COUNT)); //info tickets init
            updateState(players, gameState);
            mapTicketsChoisis.put(playerId, players.get(playerId).chooseInitialTickets());
            gameState = gameState.withInitiallyChosenTickets(playerId, mapTicketsChoisis.get(playerId));

        }));
        //info tickets choisis:
        players.forEach(((playerId, player) -> {
            players.get(playerId).receiveInfo(infoMap.get(playerId).keptTickets(mapTicketsChoisis.get(playerId).size()));
                }));


        //La déroulement de la partie
        while (!gameState.lastTurnBegins()) {
            PlayerId currentId = gameState.currentPlayerId();
            Player joueurCourant = players.get(currentId);

            joueurCourant.receiveInfo(infoMap.get(currentId).canPlay()); //info tour commence
            updateState(players, gameState);

            switch (joueurCourant.nextTurn()) {

                case DRAW_TICKETS:
                    receiveInfo(players, infoMap.get(currentId).drewTickets(Constants.IN_GAME_TICKETS_COUNT));//info tire des billets
                    SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    SortedBag<Ticket> ticketsChoisis = joueurCourant.chooseTickets(drawnTickets);
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets, ticketsChoisis);
                    receiveInfo(players, infoMap.get(currentId).keptTickets(ticketsChoisis.size()));//info tickets choisis
                    break;

                case DRAW_CARDS:
                    for (int i = 0; i < 2; i++) {
                        gameState = deckisEmpty(rng);
                        int cartePioche = joueurCourant.drawSlot();
                        if (cartePioche == 1) {
                            receiveInfo(players, infoMap.get(currentId).drewBlindCard());//info prends carte pioche
                            gameState = gameState.withBlindlyDrawnCard();
                        } else {
                            receiveInfo(players, infoMap.get(currentId).drewVisibleCard(gameState.cardState().faceUpCard(cartePioche)));//info prends carte pioche
                            gameState = gameState.withDrawnFaceUpCard(cartePioche);
                        }
                    }
                    updateState(players, gameState);
                    break;
                case CLAIM_ROUTE:
                    Route claimRoute = joueurCourant.claimedRoute();
                    SortedBag<Card> claimCards = joueurCourant.initialClaimCards();


                    //si 0 comme overground
                    //autre possible add dans playerstate
                    //si vide c'est bon
                    //doit choisir si rien ensemble vide
                    //actualise le jeu + discarded cards à jour




                    if ((joueurCourant.claimedRoute().level() == Route.Level.UNDERGROUND)) {
                        receiveInfo(players, infoMap.get(currentId).attemptsTunnelClaim(claimRoute,claimCards));
                        SortedBag.Builder listecartebuilder = null;
                        SortedBag<Card> listCartePioche = SortedBag.of();
                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                            gameState = deckisEmpty(rng); //redefnir si vide
                            int cartePioche = joueurCourant.drawSlot(); // à chaque fois faire une action sur le Player
                            listecartebuilder.add(gameState.topCard());
                        }

                        //if (claimRoute.additionalClaimCardsCount(claimCards, ))){ //TODO avoir les 3 cartes piochés
                        //     joueurCourant.chooseAdditionalCards( ); //en paramètre le 3 cartes piochés
                        if(gameState.cardState()) { //si peut claim le tunnel TODO quelle méthode ?
                        joueurCourant.receiveInfo(infoMap.get(currentId).claimedRoute(claimRoute, claimCards));
                        gameState = gameState.forNextTurn();
                        }

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


    private static void updateState(Map<PlayerId, Player> playersMap, GameState gameState){
        playersMap.forEach(((playerId, player) -> {
            playersMap.get(playerId).updateState(gameState, gameState.currentPlayerState());
        }));
    }


    private static void receiveInfo(Map<PlayerId, Player> playersMap, String string){
        playersMap.forEach(((playerId, player) -> {
            playersMap.get(playerId).receiveInfo(string);
        }));
    }

    private static GameState deckisEmpty(Random rng){
        if(gameState.cardState().isDeckEmpty()) {
            return gameState.withCardsDeckRecreatedIfNeeded(rng);
        } else return gameState;
    }//TODO ON COMPRENDS PAS :(
}
