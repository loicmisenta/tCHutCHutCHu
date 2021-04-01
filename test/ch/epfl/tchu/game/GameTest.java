package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
        }


        Map<PlayerId, String> playerNames;
        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            this.playerNames = playerNames;
            System.out.println("joueurs initilalisés");
        }

        @Override
        public void receiveInfo(String info) {

        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = (GameState) newState;
            this.ownState = ownState;
            System.out.println("nouveau etat du joueur 1 " + newState.playerState(PlayerId.PLAYER_1).toString() +
                    "\n" + "nouveau etat du joueur 2 " + newState.playerState(PlayerId.PLAYER_2)
                    +"\n" + "etat " + ownState.toString());
        }

        SortedBag<Ticket> tickets;

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            System.out.println("Appelée au début de la partie pour communiquer au joueur les cinq billets qui lui ont été distribués");
            System.out.println(tickets.toString());
            this.tickets = tickets;

        }


        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            System.out.println("le joueur prend les deux premiers tickets");
            return SortedBag.of(1, tickets.get(0), 1, tickets.get(1));
        }

        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> claimableRoutes = List.of();
            if (claimableRoutes.isEmpty()) {
                System.out.println(" claimableRoutes est vide, donc pioche cartes");
                return TurnKind.DRAW_CARDS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                System.out.println("le joueur va claim une route");
                return TurnKind.CLAIM_ROUTE;
            }
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            System.out.println("le joueur choisit les tickets");
            return SortedBag.of(ChMap.tickets());
        }

        Random rn = new Random();
        int random = rn.nextInt(3);

        @Override
        public int drawSlot() {
            System.out.println("le joueur pioche la" + random );
            return random;
        }

        @Override
        public Route claimedRoute() {
            System.out.println("le joueur a claim une route" + " tjrs la 1ere ");
            return ChMap.routes().get(0);
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            System.out.println("le joueur a choisi les cartes initiales pour prenre la rote");
            return null;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            System.out.println("les cartes additionnelles choisies sont à l'incdex " + random  + " " + options.get(random).toString());
            return options.get(random);
        }
    }


    @Test
    void testPlay(){
        Map<PlayerId, Player> players = new EnumMap<>(PlayerId.class);
        Map<PlayerId, String> playerName = new EnumMap<>(PlayerId.class);
        Player firstPlayer = new TestPlayer((long)3.5, List.of());
        Player secondPlayer = new TestPlayer((long)3.5, List.of());
        players.put(PlayerId.PLAYER_1, firstPlayer);
        players.put(PlayerId.PLAYER_2, secondPlayer);
        playerName.put(PlayerId.PLAYER_1, "Loïc");
        playerName.put(PlayerId.PLAYER_2, "Alexandra");
        SortedBag<Ticket> tickets = SortedBag.of(10, ChMap.tickets().get(1), 10, ChMap.tickets().get(0));
        System.out.println(tickets.toString());
        Game.play(players, playerName, tickets, new Random());
    }


}