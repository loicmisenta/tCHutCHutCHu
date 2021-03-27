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
    /**
     * Fait jouer une partie
     * @param players joueurs
     * @param playerNames les noms des joueurs
     * @param tickets les billets dissponibles
     * @param rng générateur aléatoire de nombres
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng){
        Preconditions.checkArgument((players.size() == 2 )|| (playerNames.size() == 2));

        //Le début de la partie
        Map<PlayerId, Info> infoMap = new EnumMap<>(PlayerId.class);
        GameState gameState = GameState.initial(tickets, rng);
        players.forEach(((playerId, player) -> {
            players.get(playerId).initPlayers(playerId, playerNames);
            infoMap.put(playerId, new Info( playerNames.get(playerId)));
            players.get(playerId).receiveInfo(infoMap.get(playerId).willPlayFirst());
            players.get(playerId).setInitialTicketChoice(gameState.playerState(playerId).tickets());
            players.get(playerId).chooseInitialTickets();
            players.get(playerId).receiveInfo(infoMap.get(playerId).keptTickets(gameState.playerState(playerId).ticketCount()));
        }));

        //La déroulement de la partie
        while (!gameState.lastTurnBegins()){
            PlayerId currentId = gameState.currentPlayerId();
            Player joueurCourant = players.get(currentId);
            switch(joueurCourant.nextTurn()){
                joueurCourant.receiveInfo(infoMap.get(currentId).canPlay());
                case DRAW_TICKETS:
                    joueurCourant.chooseTickets(gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT));
                    joueurCourant.receiveInfo(infoMap.get(currentId).drewTickets(Constants.IN_GAME_TICKETS_COUNT));
                    break;
                case DRAW_CARDS:
                    joueurCourant.drawSlot(); // TODO Faut-il stocker la valeur?
                    joueurCourant.drawSlot(); // TODO Et l'utiliser ?
                    gameState.topCard();
                    gameState.topCard();  //TODO joueurCourant et gameState pas liés
                    break;
                case CLAIM_ROUTE:
                    Route claimRoute = joueurCourant.claimedRoute();
                    SortedBag<Card> claimCards = joueurCourant.initialClaimCards();

                    if((joueurCourant.claimedRoute().level() == Route.Level.UNDERGROUND)) {
                    for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                        if(gameState.cardState().isDeckEmpty()){
                            gameState.withCardsDeckRecreatedIfNeeded(rng);
                        }
                        joueurCourant.drawSlot(); // à chaque fois faire une action sur le Player
                        gameState.topCard(); //tirer une carte du haut ?

                   //if (claimRoute.additionalClaimCardsCount(claimCards, ))){ //TODO avoir les 3 cartes piochés
                   //     joueurCourant.chooseAdditionalCards( ); //en paramètre le 3 cartes piochés
            }
            gameState.forNextTurn();
        }


        private GameState deckisEmpty(){
                if(gameState.cardState().isDeckEmpty()){
                    gameState.withCardsDeckRecreatedIfNeeded(rng);
                }
            }

        //nextTurn faire des elseif?
        //comment appeler régulièrement une méthode ? receiveInfo/updateState
    }
    }
}
