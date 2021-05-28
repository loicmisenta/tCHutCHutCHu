package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 */
public final class Game {
    private static Trail longestTrailBonus;
    private static boolean isGameOver = false;
    /**
     * constructeur privé non-instanciable
     */
    private Game(){}

    /**
     * Fait jouer une partie
     * @param players     joueurs
     * @param playerNames les noms des joueurs
     * @param tickets     les billets dissponibles
     * @param rng         générateur aléatoire de nombres
     */

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {
        //Preconditions.checkArgument((players.size() == PlayerId.COUNT) && (playerNames.size() == PlayerId.COUNT));
        //Le début de la partie
        Map<PlayerId, Info> infoMap = new EnumMap<>(PlayerId.class);
        GameState gameState = GameState.initial(tickets, rng, players.size());
        Map<PlayerId, SortedBag<Ticket>> mapTicketsChoisis = new EnumMap<>(PlayerId.class);

        gameState = beginGame(players, playerNames, infoMap, mapTicketsChoisis, gameState);

        gameState = middleGame(players, infoMap, rng, gameState);

        endGame(players, infoMap, gameState);

    }

    private static GameState beginGame(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames,
                                       Map<PlayerId, Info> infoMap, Map<PlayerId, SortedBag<Ticket>> mapTicketsChoisis, GameState gameState) {

        for (PlayerId playerId : PlayerId.ALL.subList(0, players.size())) {
            players.get(playerId).initPlayers(playerId, playerNames);
            infoMap.put(playerId, new Info(playerNames.get(playerId))); }
        receiveInfo(players, infoMap.get(gameState.currentPlayerId()).willPlayFirst());
        for (PlayerId playerId : PlayerId.ALL.subList(0, players.size())) {
            players.get(playerId).setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }
        updateState(players, gameState);

        for (PlayerId playerId : PlayerId.ALL.subList(0, players.size())) {
            SortedBag<Ticket> initialticket = players.get(playerId).chooseInitialTickets();
            mapTicketsChoisis.put(playerId, initialticket);
            gameState = gameState.withInitiallyChosenTickets(playerId, mapTicketsChoisis.get(playerId));
        }

        //info tickets choisis:
        players.forEach(((playerId, player) -> receiveInfo(players, infoMap.get(playerId).keptTickets(mapTicketsChoisis.get(playerId).size()))));
        return gameState;
    }

    private static GameState middleGame(Map<PlayerId, Player> players, Map<PlayerId, Info> infoMap, Random rng, GameState gameState) {
        while(true) {
            PlayerId currentId = gameState.currentPlayerId();
            Player joueurCourant = players.get(currentId);
            receiveInfo(players, infoMap.get(currentId).canPlay()); //info tour commence
            updateState(players, gameState);

            switch (joueurCourant.nextTurn()) {

                case DRAW_TICKETS:
                    receiveInfo(players, infoMap.get(currentId).drewTickets(Constants.IN_GAME_TICKETS_COUNT)); //info tire des billets
                    SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    SortedBag<Ticket> ticketsChoisis = joueurCourant.chooseTickets(drawnTickets);
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets, ticketsChoisis);
                    receiveInfo(players, infoMap.get(currentId).keptTickets(ticketsChoisis.size())); //info tickets choisis
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
                        SortedBag.Builder<Card> listCardBuild = new SortedBag.Builder<>();
                        SortedBag<Card> listCartePioche;
                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng); //redefnir si vide
                            listCardBuild.add(gameState.topCard());
                            gameState = gameState.withoutTopCard();
                        }
                        listCartePioche = listCardBuild.build();
                        int nbCarteAdd = claimRoute.additionalClaimCardsCount(claimCards, listCartePioche);
                        receiveInfo(players, infoMap.get(currentId).drewAdditionalCards(listCartePioche, nbCarteAdd));
                        if (nbCarteAdd != 0) {
                            List<SortedBag<Card>> possibleAddCartes = gameState.currentPlayerState().possibleAdditionalCards(nbCarteAdd, claimCards, listCartePioche);//en paramètre le 3 cartes piochés
                            if (possibleAddCartes.size() == 0) {
                                receiveInfo(players, infoMap.get(currentId).didNotClaimRoute(claimRoute));
                            } else {
                                SortedBag<Card> carteAddChoisi = joueurCourant.chooseAdditionalCards(possibleAddCartes);
                                if (carteAddChoisi.isEmpty()) {
                                    receiveInfo(players, infoMap.get(currentId).didNotClaimRoute(claimRoute));
                                } else {
                                    SortedBag<Card> cardsUnion = claimCards.union(carteAddChoisi);
                                    gameState = gameState.withClaimedRoute(claimRoute, cardsUnion);
                                    receiveInfo(players, infoMap.get(currentId).claimedRoute(claimRoute, cardsUnion));
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

            if (gameState.lastTurnBegins()) {
                receiveInfo(players, infoMap.get(currentId).lastTurnBegins(gameState.currentPlayerState().carCount()));
            }

            if (currentId == gameState.lastPlayer()) {
                break;
            }

            gameState = gameState.forNextTurn();
        }
        return gameState;
    }

    private static void endGame(Map<PlayerId, Player> players, Map<PlayerId, Info> infoMap, GameState gameState) {

        int maxLength = 0;
        int maxPoints = Integer.MIN_VALUE;
        List<PlayerId> listLongestTrail = new ArrayList<>();
        List<PlayerId> playerIdWon = new ArrayList<>();
        Map<String, Integer> playerNamesWon = new HashMap<>();
        Map<String, Integer> playerNamesLost = new HashMap<>();
        Map<PlayerId, Integer> mapPlayerPoints = new EnumMap<>(PlayerId.class);
        for (PlayerId playerId : PlayerId.ALL.subList(0, players.size())) {
            Trail longest = Trail.longest(gameState.playerState(playerId).routes());
            if (longest.length() == maxLength) {
                listLongestTrail.add(playerId);
            } else if (longest.length() > maxLength) {
                maxLength = longest.length();
                listLongestTrail.clear();
                listLongestTrail.add(playerId);
            }


            int pointsFinaux = gameState.playerState(playerId).finalPoints();
            if (listLongestTrail.contains(playerId)) {
                pointsFinaux += Constants.LONGEST_TRAIL_BONUS_POINTS;
            }
            mapPlayerPoints.put(playerId, pointsFinaux);
            if (pointsFinaux == maxPoints) {
                playerIdWon.add(playerId);
                playerNamesWon.put(playerId.name(), pointsFinaux);
            } else if (pointsFinaux > maxPoints) {
                maxPoints = pointsFinaux;
                playerNamesLost.putAll(playerNamesWon);
                playerIdWon.clear();
                playerIdWon.add(playerId);
            } else{
                playerNamesLost.put(playerId.name(), pointsFinaux);
            }
        }

        Trail longestTrail = Trail.longest(gameState.playerState(listLongestTrail.get(0)).routes());
        for (PlayerId playerId: listLongestTrail) {
            receiveInfo(players, infoMap.get(playerId).getsLongestTrailBonus(longestTrail));
        }
        updateState(players, gameState);

        PlayerId joueurGagnant = playerIdWon.get(0);
        players.forEach(((playerId, player) -> {

            if (playerIdWon.size() >= 2) {
                List<String> playerNamesString = new ArrayList<>();
                for (PlayerId joueur : playerIdWon) { playerNamesString.add(joueur.name()); }
                players.get(playerId).receiveInfo(Info.draw(playerNamesString, mapPlayerPoints.get(playerId)));
            } else {
                //TODO adapter le message si un a gagné deux ont perdu
                //TODO players.get(playerId).receiveInfo(infoMap.get(joueurGagnant).won(mapPlayerPoints.get(joueurGagnant), mapPlayerPoints.get(joueurGagnant.next())));
                players.get(playerId).receiveInfo(infoMap.get(joueurGagnant).wonMulti(playerNamesWon, playerNamesLost));
            }
        }));
        isGameOver = true;
        setLongestTrail(longestTrail);

    }


    private static void updateState(Map<PlayerId, Player> playersMap, GameState gameState) {
        playersMap.forEach(((playerId, player) -> playersMap.get(playerId).updateState(gameState, gameState.playerState(playerId))));
    }


    private static void receiveInfo(Map<PlayerId, Player> playersMap, String string) {
       playersMap.forEach((playerId, player) -> player.receiveInfo(string));
    }
    private static void setLongestTrail(Trail trail){
        longestTrailBonus = trail;
    }
    public static Trail getLongestTrail(){
        return longestTrailBonus;
    }
    public static boolean gameIsOver(){
        return isGameOver;
    }
}