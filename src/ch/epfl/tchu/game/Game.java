package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public final class Game {
    private static GameState gameState; //TODO correct de l'avoir ?
    /**
     * Fait jouer une partie
     *
     * @param players     joueurs
     * @param playerNames les noms des joueurs
     * @param tickets     les billets dissponibles
     * @param rng         générateur aléatoire de nombres
     */

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {
        Preconditions.checkArgument((players.size() == 2) && (playerNames.size() == 2));
        //Le début de la partie
        Map<PlayerId, Info> infoMap = new EnumMap<>(PlayerId.class);
        gameState = GameState.initial(tickets, rng);
        Map<PlayerId, SortedBag<Ticket>> mapTicketsChoisis= new EnumMap<>(PlayerId.class);

        beginGame(players, playerNames, infoMap, mapTicketsChoisis);

        do { middleGame(players, infoMap, rng);
        } while (!gameState.lastTurnBegins() && (gameState.lastPlayer() == null));

        receiveInfo(players, infoMap.get(gameState.lastPlayer()).lastTurnBegins(gameState.playerState(gameState.lastPlayer()).carCount()));
        for (int i = 0; i < PlayerId.COUNT ; i++) {
            middleGame(players, infoMap, rng);
        }

        endGame(players, infoMap);

    }

    private static void beginGame(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, Map<PlayerId, Info> infoMap, Map<PlayerId, SortedBag<Ticket>> mapTicketsChoisis){
        players.forEach(((playerId, player) -> {
            players.get(playerId).initPlayers(playerId, playerNames);
            infoMap.put(playerId, new Info(playerNames.get(playerId)));
            players.get(playerId).setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
            updateState(players, gameState); //TODO boucles séparées  pour choix ticket
            SortedBag<Ticket> initialticket = players.get(playerId).chooseInitialTickets();
            mapTicketsChoisis.put(playerId, initialticket);
            gameState = gameState.withInitiallyChosenTickets(playerId, mapTicketsChoisis.get(playerId));
        }));
        receiveInfo(players, infoMap.get(gameState.currentPlayerId()).willPlayFirst());
        //info tickets choisis:
        players.forEach(((playerId, player) -> receiveInfo(players, infoMap.get(playerId).keptTickets(mapTicketsChoisis.get(playerId).size()))));
    }

    private static void middleGame(Map<PlayerId, Player> players, Map<PlayerId, Info> infoMap, Random rng){
        PlayerId currentId = gameState.currentPlayerId();
        Player joueurCourant = players.get(currentId);

        receiveInfo(players, infoMap.get(currentId).canPlay()); //info tour commence
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
                    int cartePioche = joueurCourant.drawSlot();
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    if (cartePioche == Constants.DECK_SLOT) {
                        receiveInfo(players, infoMap.get(currentId).drewBlindCard());//info prends carte pioche
                        gameState = gameState.withBlindlyDrawnCard();
                    } else {
                        receiveInfo(players, infoMap.get(currentId).drewVisibleCard(gameState.cardState().faceUpCard(cartePioche)));//info prends carte pioche
                        gameState = gameState.withDrawnFaceUpCard(cartePioche);
                    }
                    if (i == 0) {
                        updateState(players, gameState);
                    }

                }
                break;
            case CLAIM_ROUTE:
                Route claimRoute = joueurCourant.claimedRoute();
                SortedBag<Card> claimCards = joueurCourant.initialClaimCards();
                if ((claimRoute.level() == Route.Level.UNDERGROUND)) {
                    receiveInfo(players, infoMap.get(currentId).attemptsTunnelClaim(claimRoute, claimCards));
                    SortedBag.Builder<Card> listecartebuilder = new SortedBag.Builder<>();
                    SortedBag<Card> listCartePioche;
                    for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                        gameState = gameState.withCardsDeckRecreatedIfNeeded(rng); //redefnir si vide
                        listecartebuilder.add(gameState.topCard());
                        gameState = gameState.withoutTopCard();
                    }
                    listCartePioche = listecartebuilder.build();
                    int nbCarteAdd = claimRoute.additionalClaimCardsCount(claimCards, listCartePioche);
                    receiveInfo(players, infoMap.get(currentId).drewAdditionalCards(listCartePioche, nbCarteAdd));
                    if (nbCarteAdd != 0) {
                        List<SortedBag<Card>> possibleAddCartes = gameState.currentPlayerState().possibleAdditionalCards(nbCarteAdd, claimCards, listCartePioche);//en paramètre le 3 cartes piochés
                        if (possibleAddCartes.size() == 0) {
                            receiveInfo(players, infoMap.get(currentId).didNotClaimRoute(claimRoute));
                        } else {
                            SortedBag<Card> carteAddChoisi = joueurCourant.chooseAdditionalCards(possibleAddCartes);
                            if(carteAddChoisi.isEmpty()){
                                receiveInfo(players, infoMap.get(currentId).didNotClaimRoute(claimRoute));
                            } else {
                                gameState = gameState.withClaimedRoute(claimRoute, claimCards.union(carteAddChoisi));
                                receiveInfo(players, infoMap.get(currentId).claimedRoute(claimRoute, claimCards.union(carteAddChoisi)));
                            }
                        }
                    } else {
                        gameState = gameState.withClaimedRoute(claimRoute, claimCards);
                        receiveInfo(players, infoMap.get(currentId).claimedRoute(claimRoute, claimCards));
                    }
                    gameState = gameState.withMoreDiscardedCards(listCartePioche);

                } else {
                    gameState = gameState.withClaimedRoute(claimRoute, claimCards);
                    receiveInfo(players, infoMap.get(currentId).claimedRoute(claimRoute, claimCards));
                }
                break;
        }
        gameState = gameState.forNextTurn();
        updateState(players, gameState);
    }

    private static void endGame(Map<PlayerId, Player> players, Map<PlayerId, Info> infoMap){

        int maxLength = 0;
        int maxPoints = Integer.MIN_VALUE;
        List<PlayerId> listLongestTrail= new ArrayList<>();
        List<PlayerId> playerNamesWon = new ArrayList<>();
        for (PlayerId joueur: PlayerId.ALL) {

            Trail longest = Trail.longest(gameState.playerState(joueur).routes());
            if (longest.length() == maxLength) {
                listLongestTrail.add(joueur);
            } else if (longest.length() > maxLength) {
                maxLength = longest.length();
                listLongestTrail.clear();
                listLongestTrail.add(joueur);
            }


            int pointsFinaux = gameState.playerState(joueur).finalPoints();
            if(listLongestTrail.contains(joueur)){ pointsFinaux += Constants.LONGEST_TRAIL_BONUS_POINTS;}

            if(pointsFinaux == maxPoints){
                playerNamesWon.add(joueur);
            } else if (gameState.playerState(joueur).finalPoints() > maxPoints){
                maxPoints = gameState.playerState(joueur).finalPoints();
                playerNamesWon.clear();
                playerNamesWon.add(joueur);
            }
        }


        int finalMaxPoints = maxPoints;
        PlayerId plrLongestTr = listLongestTrail.get(0);
        //if dans le cas où il y a deux routes de même longueur

        if (listLongestTrail.size() > 1){ receiveInfo(players, infoMap.get(plrLongestTr.next()).getsLongestTrailBonus(Trail.longest(gameState.playerState(plrLongestTr.next()).routes()))); }
        receiveInfo(players, infoMap.get(plrLongestTr).getsLongestTrailBonus(Trail.longest(gameState.playerState(plrLongestTr).routes())));

        updateState(players, gameState);
        players.forEach(((playerId, player) -> {

            if (playerNamesWon.size() >= 2) {
                List<String> playerNamesString = new ArrayList<>();
                for (PlayerId joueur: playerNamesWon) {
                    playerNamesString.add(joueur.name());
                }
                players.get(playerId).receiveInfo(Info.draw(playerNamesString, finalMaxPoints));
            } else {
                PlayerId joueurGagnant = playerNamesWon.get(0);
                int finalPoints = gameState.playerState(joueurGagnant).finalPoints();
                int otherPoints = gameState.playerState(joueurGagnant.next()).finalPoints();
                if (playerNamesWon.contains(joueurGagnant)){        finalPoints += Constants.LONGEST_TRAIL_BONUS_POINTS; }
                if (playerNamesWon.contains(joueurGagnant.next())){ otherPoints += Constants.LONGEST_TRAIL_BONUS_POINTS; }
                players.get(playerId).receiveInfo(infoMap.get(joueurGagnant).won( finalPoints, otherPoints));
            }
            //TODO simplifier ???

        }));

    }


    private static void updateState(Map<PlayerId, Player> playersMap, GameState gameState){
        playersMap.forEach(((playerId, player) -> playersMap.get(playerId).updateState(gameState, gameState.playerState(playerId))));
    }


    private static void receiveInfo(Map<PlayerId, Player> playersMap, String string){
        playersMap.forEach(((playerId, player) -> player.receiveInfo(string)));
    }
}
