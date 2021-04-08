package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
class GameTest {


    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private final Random rng;

        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        private int turnCounter;
        private PlayerState ownState;
        private GameState gameState;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;
        private String nomPlayer1;
        private String nomPlayer2;

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
        }


        Map<PlayerId, String> playerNames;
        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            this.playerNames = playerNames;
            System.out.println(playerNames.get(ownId) + " a été initilalisé");

            System.out.println();
            System.out.println();
            System.out.println(playerNames.get(PlayerId.PLAYER_1) + " seras le joueur 1");
            System.out.println(playerNames.get(PlayerId.PLAYER_2) + " seras le joueur 2");
            System.out.println();
            System.out.println();
            nomPlayer1 = playerNames.get(PlayerId.PLAYER_1);
            nomPlayer2 = playerNames.get(PlayerId.PLAYER_2);
            System.out.println();



        }

        @Override
        public void receiveInfo(String info) {
            System.out.println();
            System.out.println();
            System.out.println(info);
            System.out.println();
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = (GameState) newState;
            this.ownState = ownState;
            System.out.println("Etat de " + nomPlayer1 + " : " + newState.playerState(PlayerId.PLAYER_1).toString() +
                    "\n" + "Etat de " + nomPlayer2 + " : " + newState.playerState(PlayerId.PLAYER_2)
                    +"\n" + "Etat joueur courant"  + ownState.toString());
        }

        SortedBag<Ticket> tickets;

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            System.out.println("Appelée au début de la partie pour communiquer au joueur les cinq billets qui lui ont été distribués");
            System.out.println("Billets disitribués" + tickets.toString());
            this.tickets = tickets;

        }


        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            System.out.println("Le joueur choisit 3 tickets");
            Random randomticket1 = new Random();
            Ticket ticket1 = tickets.get(randomticket1.nextInt(5));
            Random randomticket2 = new Random();
            Ticket ticket2 = tickets.get(randomticket2.nextInt(5));
            Random randomticket3 = new Random();
            Ticket ticket3 = tickets.get(randomticket3.nextInt(5));
            SortedBag.Builder<Ticket> construction = new SortedBag.Builder<>();
            construction.add(ticket1).add(ticket2).add(ticket3);
            return construction.build();
        }

        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer

            List<Route> preClaimableRoutes = new ArrayList<>(allRoutes);
            List<Route> claimableRoutes = new ArrayList<>();
            for (Route r : allRoutes) {
                for (int j = 0; j < gameState.claimedRoutes().size(); j++) {
                    if (r == gameState.claimedRoutes().get(j)) {
                        preClaimableRoutes.remove(r);
                    }
                }
            }

            for (Route r : preClaimableRoutes) {
                if (ownState.canClaimRoute(r)) claimableRoutes.add(r);
            }


            System.out.println(" --- > nb de carte de la pioche : " + gameState.cardState().deckSize() + " < --- ");
            System.out.println(" --- > cartes face visible : " + gameState.cardState().faceUpCards() + " < --- ");
            System.out.println(" --- > nb de carte de la défausse : " + gameState.cardState().discardsSize() + " < --- ");
            System.out.println(" --- > nb de carte total pas en main du joueur : " + gameState.cardState().totalSize() + " < --- ");

            System.out.println(" --- > nb de route prise au total : " + (88 - preClaimableRoutes.size()) + " < --- ");
            System.out.println(" --- > size claimableRoutes & allRoutes -->   " + claimableRoutes.size() + "    " + allRoutes.size() + " < --- ");
            System.out.println();
            System.out.println("JOUEUR 1 nb de routes prises : ");
            System.out.println(gameState.playerState(PlayerId.PLAYER_1).routes().size());
            System.out.println();
            System.out.println("JOUEUR 2 nb de routes prises : ");
            System.out.println(gameState.playerState(PlayerId.PLAYER_2).routes().size());
            System.out.println();
            System.out.println();
            System.out.println("JOUEUR 1 nb de wagons : restants");
            System.out.println(gameState.playerState(PlayerId.PLAYER_1).carCount());
            System.out.println();
            System.out.println("JOUEUR 2 nb de wagons : restants");
            System.out.println(gameState.playerState(PlayerId.PLAYER_2).carCount());
            System.out.println();

            //nombre de carte en jeu doit etre egal a 110:
            if (gameState.cardState().deckSize() + gameState.cardState().discardsSize()
                    + gameState.cardState().faceUpCards().size() + gameState.playerState(PlayerId.PLAYER_1).cardCount()
                    + gameState.playerState(PlayerId.PLAYER_2).cardCount() != 110){
                throw new IllegalArgumentException("nombre de carte en jeu PAS EGAL 110");
            }

            if (claimableRoutes.isEmpty()) {
                System.out.println("ClaimableRoutes est vide, donc pioche cartes");
                return TurnKind.DRAW_CARDS;
            } else {

                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                System.out.println("Le joueur va claim une route");
                return TurnKind.CLAIM_ROUTE;
            }
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            System.out.println("Le joueur choisit les billets");
            return SortedBag.of(ChMap.tickets());
        }



        @Override
        public int drawSlot() {
            Random rn = new Random();
            int random = rn.nextInt(3);
            System.out.println("le joueur pioche la " + random );
            return random;
        }

        @Override
        public Route claimedRoute() {
            System.out.println("Le joueur va claim une route");
            return routeToClaim;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            System.out.println("Le joueur a choisi les cartes initiales pour prenre la route");
            return initialClaimCards;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            SortedBag<Card> option = options.get(0);
            System.out.println("Les cartes additionnelles choisies sont à l'incdex " + 0  + " " + option);
            return option;
        }
    }


    @Test
    void testPlay(){
        Map<PlayerId, Player> players = new EnumMap<>(PlayerId.class);
        Map<PlayerId, String> playerName = new EnumMap<>(PlayerId.class);
        Player firstPlayer = new TestPlayer((long)3.5, ChMap.routes());
        Player secondPlayer = new TestPlayer((long)3.5, ChMap.routes());
        players.put(PlayerId.PLAYER_1, firstPlayer);
        players.put(PlayerId.PLAYER_2, secondPlayer);
        playerName.put(PlayerId.PLAYER_1, "Loïc");
        playerName.put(PlayerId.PLAYER_2, "Alexandra");
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        System.out.println("Billets totaux avant lancement de play : " + tickets.toString());
        Game.play(players, playerName, tickets, new Random());
    }


}