package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
        boolean lastTurn = false;
        //boolean lastPlayer = false;
        Map<PlayerId, Info> infoMap = new EnumMap<>(PlayerId.class);
        gameState = GameState.initial(tickets, rng);
        Map<PlayerId, SortedBag<Ticket>> mapTicketsChoisis= new EnumMap<>(PlayerId.class);

        players.forEach(((playerId, player) -> {
            players.get(playerId).initPlayers(playerId, playerNames);
            infoMap.put(playerId, new Info(playerNames.get(playerId)));
            players.get(playerId).setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
            updateState(players, gameState);
            SortedBag<Ticket> initialticket = players.get(playerId).chooseInitialTickets();
            mapTicketsChoisis.put(playerId, initialticket);
            gameState = gameState.withInitiallyChosenTickets(playerId, mapTicketsChoisis.get(playerId));
        }));
        receiveInfo(players, infoMap.get(PlayerId.PLAYER_1).willPlayFirst());

        players.forEach(((playerId, player) -> { //TODO à refaire pas efficace !!
        receiveInfo(players, infoMap.get(playerId).drewTickets(Constants.INITIAL_TICKETS_COUNT));
        }));

        //info tickets choisis:
        players.forEach(((playerId, player) -> {
            players.get(playerId).receiveInfo(infoMap.get(playerId).keptTickets(mapTicketsChoisis.get(playerId).size()), playerId);
                }));



        //La déroulement de la partie
        while (!gameState.lastTurnBegins() && !gameState.currentPlayerId().equals(gameState.lastPlayer())) {   // || lastPlayer

            PlayerId currentId = gameState.currentPlayerId();
            Player joueurCourant = players.get(currentId);

            if (lastTurn ){ // && !lastPlayer
                System.out.println("-----------------------------------LE DERNIER TOUR COMMENCE-------------------------------------");
                receiveInfo(players, infoMap.get(gameState.lastPlayer()).lastTurnBegins(gameState.playerState(gameState.lastPlayer()).carCount()));

            //    lastPlayer = true;
            } //else if ( lastPlayer) { lastPlayer =false;}

            receiveInfo(players, infoMap.get(currentId).canPlay()); //info tour commence
            updateState(players, gameState);


            switch (joueurCourant.nextTurn()) { //TODO NE PASSE PAS DEDANS !!

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
                        if (cartePioche == Constants.DECK_SLOT) {
                            receiveInfo(players, infoMap.get(currentId).drewBlindCard());//info prends carte pioche
                            gameState = gameState.withBlindlyDrawnCard();
                        } else {
                            receiveInfo(players, infoMap.get(currentId).drewVisibleCard(gameState.cardState().faceUpCard(cartePioche)));//info prends carte pioche
                            gameState = gameState.withDrawnFaceUpCard(cartePioche);
                        }
                        if(i != 1) {updateState(players, gameState);}// TODO juste avant d'appeler drawSlot pour la seconde fois lorsqu'un joueur tire des cartes

                    }

                    break;
                case CLAIM_ROUTE:
                    System.out.println("route"); //TODO enlever
                    Route claimRoute = joueurCourant.claimedRoute();
                    SortedBag<Card> claimCards = joueurCourant.initialClaimCards();

                    if ((joueurCourant.claimedRoute().level() == Route.Level.UNDERGROUND)) {
                        System.out.println("------------------------------------------" + claimRoute);
                        System.out.println("-------------------------------------------------------" + claimCards);
                        receiveInfo(players, infoMap.get(currentId).attemptsTunnelClaim(claimRoute,claimCards));
                        SortedBag.Builder<Card> listecartebuilder = new SortedBag.Builder<>();
                        SortedBag<Card> listCartePioche;
                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                            gameState = deckisEmpty(rng); //redefnir si vide
                            listecartebuilder.add(gameState.topCard());
                            gameState = gameState.withoutTopCard();
                        }
                        listCartePioche = listecartebuilder.build();
                        gameState = gameState.withMoreDiscardedCards(listCartePioche);

                        int nbCarteAdd = claimRoute.additionalClaimCardsCount(claimCards, listCartePioche);
                        receiveInfo(players, infoMap.get(currentId).drewAdditionalCards(listCartePioche, nbCarteAdd));
                        if (nbCarteAdd != 0){
                            List<SortedBag<Card>> possibleAddCartes = gameState.currentPlayerState().possibleAdditionalCards(nbCarteAdd, claimCards, listCartePioche);//en paramètre le 3 cartes piochés
                            if (possibleAddCartes.size() == 0){
                                receiveInfo(players, infoMap.get(currentId).didNotClaimRoute(claimRoute));
                            }
                            else {

                                SortedBag<Card> carteAddChoisi = joueurCourant.chooseAdditionalCards(gameState.currentPlayerState().possibleAdditionalCards(nbCarteAdd, claimCards, listCartePioche));
                                receiveInfo(players, infoMap.get(currentId).claimedRoute(claimRoute, claimCards.union(carteAddChoisi)));
                                gameState = gameState.withClaimedRoute(claimRoute, claimCards.union(carteAddChoisi));
                            }
                        }
                        else{
                            receiveInfo(players, infoMap.get(currentId).claimedRoute(claimRoute, claimCards));
                            gameState = gameState.withClaimedRoute(claimRoute, claimCards);
                        }

                    }
                    else {
                        receiveInfo(players, infoMap.get(currentId).claimedRoute(claimRoute, claimCards));
                        gameState = gameState.withClaimedRoute(claimRoute, claimCards);
                    }
                    break;
            }
            updateState(players, gameState);

            if(gameState.lastTurnBegins()){
                System.out.println("------------l'appel au dernier tour --------");
                lastTurn = true;
            }
            gameState = gameState.forNextTurn();
        }

        
        int maxLength = 0;
        int maxPoints = 0;
        List<PlayerId> listLongestTrail= new ArrayList<>();
        List<String> playerNamesWon = new ArrayList<>();
        for (PlayerId joueur: PlayerId.ALL) {

            Trail longest = Trail.longest(gameState.playerState(joueur).routes());
            if (longest.length() == maxLength) {
                listLongestTrail.add(joueur);
            } else if (longest.length() > maxLength) {
                maxLength = longest.length();
                listLongestTrail.clear();
                listLongestTrail.add(joueur);
            }

            if(gameState.playerState(joueur).finalPoints() == maxPoints){
                playerNamesWon.add(joueur.name());
            } else if (gameState.playerState(joueur).finalPoints() > maxPoints){
                maxPoints = gameState.playerState(joueur).finalPoints();
                playerNamesWon.clear();
                playerNamesWon.add(joueur.name());
            }

        }

        int finalMaxPoints = maxPoints;

            PlayerId plrLongestTr = listLongestTrail.get(0);
            //if dans le cas où il y a deux routes de même longueur
            if (listLongestTrail.size() > 1){receiveInfo(players, infoMap.get(plrLongestTr.next()).getsLongestTrailBonus(Trail.longest(gameState.playerState(plrLongestTr.next()).routes()))); }
            receiveInfo(players, infoMap.get(plrLongestTr).getsLongestTrailBonus(Trail.longest(gameState.playerState(plrLongestTr).routes())));

        players.forEach(((playerId, player) -> {
            int finalPoints = gameState.playerState(playerId).finalPoints();
            int otherPoints = gameState.playerState(playerId.next()).finalPoints();
            if(finalPoints > otherPoints){
                players.get(playerId).receiveInfo(infoMap.get(playerId).won(finalPoints, otherPoints), playerId);
            } else if (finalPoints < otherPoints ){
                players.get(playerId.next()).receiveInfo(infoMap.get(playerId.next()).won(otherPoints, finalPoints), playerId);
            } else {
                players.get(playerId).receiveInfo(Info.draw(playerNamesWon, finalMaxPoints), playerId);
            }
        }));

        //TODO TEST
        System.out.println("PLAYER 1" );
        System.out.println();
        System.out.println( "Claim Points");
        System.out.println(gameState.playerState(PlayerId.PLAYER_1).claimPoints());
        System.out.println();
        System.out.println( "Routes");
        System.out.println( gameState.playerState(PlayerId.PLAYER_1).routes());
        System.out.println();
        System.out.println("Tickets ");
        System.out.println(gameState.playerState(PlayerId.PLAYER_1).tickets());
        System.out.println();


        System.out.println("PLAYER 2" );
        System.out.println();
        System.out.println( "Claim Points");
        System.out.println(gameState.playerState(PlayerId.PLAYER_2).claimPoints());
        System.out.println();
        System.out.println( "Routes");
        System.out.println( gameState.playerState(PlayerId.PLAYER_2).routes());
        System.out.println();
        System.out.println("Tickets ");
        System.out.println(gameState.playerState(PlayerId.PLAYER_2).tickets());
        System.out.println();
    }


    private static void updateState(Map<PlayerId, Player> playersMap, GameState gameState){
        System.out.println();
        System.out.println();
        System.out.println("updateState appelée");
        System.out.println();
        System.out.println();
        playersMap.forEach(((playerId, player) -> {
            playersMap.get(playerId).updateState(gameState, gameState.currentPlayerState());
        }));
    }


    private static void receiveInfo(Map<PlayerId, Player> playersMap, String string){
        playersMap.forEach(((playerId, player) -> {
            System.out.println();
            player.receiveInfo(string, playerId); //TODO supprimer player
        }));
    }

    private static GameState deckisEmpty(Random rng){
        if(gameState.cardState().isDeckEmpty()) {
            return gameState.withCardsDeckRecreatedIfNeeded(rng);
        } else return gameState;
    }
}
