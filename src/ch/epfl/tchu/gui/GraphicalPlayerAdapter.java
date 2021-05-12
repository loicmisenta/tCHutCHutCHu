package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public class GraphicalPlayerAdapter implements Player {

    private final ArrayBlockingQueue<Integer> blockingIntegerDrawSlotQueue; //TODO PAS SUR
    private final ArrayBlockingQueue<SortedBag<Ticket>> blockingTicketsQueue;
    private final ArrayBlockingQueue<SortedBag<Card>> blockingCardsQueue;
    private final ArrayBlockingQueue<Route> blockingRouteQueue;
    private final ArrayBlockingQueue<TurnKind> blockingTurnKindQueue;

    GraphicalPlayer graphicalPlayer;


    public GraphicalPlayerAdapter(){
        blockingIntegerDrawSlotQueue = new ArrayBlockingQueue<>(1);
        blockingTicketsQueue =  new ArrayBlockingQueue<>(1);
        blockingCardsQueue = new ArrayBlockingQueue<>(1);
        blockingRouteQueue = new ArrayBlockingQueue<>(1);
        blockingTurnKindQueue = new ArrayBlockingQueue<>(1);

    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        this.graphicalPlayer = new GraphicalPlayer(ownId, playerNames);
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        ChooseTicketsHandler chooseTicketsHandler = tickets1 -> new Thread( ()-> {
            try {
                blockingTicketsQueue.put(tickets1);
            } catch (InterruptedException e) {
                throw new Error();
            }
        }).start();
        runLater(() -> graphicalPlayer.chooseTickets(List.of(tickets), chooseTicketsHandler));
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
                blockingTurnKindQueue.put(TurnKind.DRAW_TICKETS);
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
        runLater(() -> graphicalPlayer.chooseTickets(List.of(options), chooseTicketsHandler));
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
