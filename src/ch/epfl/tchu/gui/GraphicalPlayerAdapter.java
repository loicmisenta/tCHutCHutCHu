package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public final class GraphicalPlayerAdapter implements Player {

    private final BlockingQueue<Integer> blockingIntegerDrawSlotQueue;
    private final BlockingQueue<SortedBag<Ticket>> blockingTicketsQueue;
    private final BlockingQueue<SortedBag<Card>> blockingCardsQueue;
    private final BlockingQueue<Route> blockingRouteQueue;
    private final BlockingQueue<TurnKind> blockingTurnKindQueue;
    GraphicalPlayer graphicalPlayer;

    /**
     * Constructeur de GraphicalPlayerAdapter qui se charge d'initialiser les files bloquantes
     */
    public GraphicalPlayerAdapter(){
        blockingIntegerDrawSlotQueue = new ArrayBlockingQueue<>(1);
        blockingTicketsQueue =  new ArrayBlockingQueue<>(1);
        blockingCardsQueue = new ArrayBlockingQueue<>(1);
        blockingRouteQueue = new ArrayBlockingQueue<>(1);
        blockingTurnKindQueue = new ArrayBlockingQueue<>(1);
    }

    /**
     * Initialise le GraphicalPlayer
     * @param ownId l'identité du joueur
     * @param playerNames noms des joueurs
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> this.graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    /**
     * Appelle la méthode receiveInfo sur le GraphicalPlayer
     * @param info l'information
     */
    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    /**
     * Appelle la méthode updateState sur le GraphicalPlayer
     * @param newState nouvel état de la partie
     * @param ownState l'état propre du joueur
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    /**
     * Appelle la méthode chooseTickets sur le GraphicalPlayer sur la file bloquante
     * @param tickets  les cinq billets qui lui ont été distribués
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        ChooseTicketsHandler chooseTicketsHandler = tickets1 -> new Thread( ()-> {
            try {
                blockingTicketsQueue.put(tickets1);
            } catch (InterruptedException e) {
                throw new Error();
            }
        }).start();
        runLater(() -> graphicalPlayer.chooseTickets(tickets, chooseTicketsHandler));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        try {
            return blockingTicketsQueue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public TurnKind nextTurn() {  //TODO comment gérer les cas 1 ou 2 ou 3 ? ? ?

        DrawTicketsHandler drawTicketsHandler = () -> {
            try {
                blockingTurnKindQueue.put(TurnKind.DRAW_TICKETS); //TODO quel ticket mettre dedans ?
            } catch (InterruptedException e) {
                throw new Error();
            }
        };

        DrawCardHandler drawCardHandler = emplacement -> {
            try {
                blockingTurnKindQueue.put(TurnKind.DRAW_CARDS);
                blockingIntegerDrawSlotQueue.put(emplacement);
            } catch (InterruptedException e) {
                throw new Error();
            }
        };

        ClaimRouteHandler claimRouteHandler = (route, cartes) -> {
            try {
                blockingTurnKindQueue.put(TurnKind.CLAIM_ROUTE);
                blockingRouteQueue.put(route);
                blockingCardsQueue.put(cartes);
            } catch (InterruptedException e) {
                throw new Error();

            }
        };

        runLater(() -> graphicalPlayer.startTurn(drawTicketsHandler, drawCardHandler, claimRouteHandler));
        try {
            return blockingTurnKindQueue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }


    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        ChooseTicketsHandler chooseTicketsHandler = tickets -> {
            try {
                blockingTicketsQueue.put(tickets);
            } catch (InterruptedException e) {
                throw new Error();
            }
        };
        runLater(() -> graphicalPlayer.chooseTickets(options, chooseTicketsHandler));
        try {
            return blockingTicketsQueue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }


    @Override
    public int drawSlot() {
        if (blockingIntegerDrawSlotQueue.isEmpty()){
            DrawCardHandler drawCardHandler = empl -> {
                try {
                    blockingIntegerDrawSlotQueue.put(empl);
                } catch (InterruptedException e) {
                    throw new Error();
                }
            };
            runLater(() -> graphicalPlayer.drawCard(drawCardHandler));
        }

        try {
            return blockingIntegerDrawSlotQueue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    };

    @Override
    public Route claimedRoute() {
        try {
            return blockingRouteQueue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        try {
            return blockingCardsQueue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        ChooseCardsHandler chooseCardsHandler = cartes -> {
            try {
                blockingCardsQueue.put(cartes);
            } catch (InterruptedException e) {
                throw new Error();
            }
        };
        runLater(() -> graphicalPlayer.chooseAdditionalCards(options, chooseCardsHandler));
        try {
            return blockingCardsQueue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }
}
