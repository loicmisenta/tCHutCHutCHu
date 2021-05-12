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

    private final ArrayBlockingQueue<Object> blockingQueue; //TODO PAS SUR
    GraphicalPlayer graphicalPlayer;


    public GraphicalPlayerAdapter(){
        blockingQueue = new ArrayBlockingQueue<>(1);

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

        //TODO put et take peuvent lever l'exception InterruptedException, qui est de type checked.
        // Il faut donc entourer les appels par des blocs try/catch

        BlockingQueue<SortedBag<Ticket>> q = new ArrayBlockingQueue<>(1);
        runLater(() -> graphicalPlayer.chooseTickets(tickets, new ChooseTicketsHandler() {
            @Override
            public void onChooseTickets(SortedBag<Ticket> tickets) {
                //TODO JSP QUOI
            }
        });

        try {
            new Thread( ()-> q.put()).start(); //mettre les actions ?
        } catch (Exception e){
            System.out.println("InterruptedException ne devrait jamais être levée");
        }

        runLater(() -> graphicalPlayer.chooseTickets(List.of(tickets), q);
        //TODO sur le fil JavaFX, la méthode chooseTickets du joueur graphique, pour lui demander de choisir ses billets initiaux,
        // en lui passant un gestionnaire de choix qui stocke le choix du joueur dans une file bloquante
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        //TODO retourne la val de la file bloquante de la méthode d'avant ? ???

        return null;
    }

    @Override
    public TurnKind nextTurn() {
        runLater(() -> graphicalPlayer.startTurn();
        return null;
    }


    // chooseTickets est la concatenation de SetInitialTicketChoice et ChooseInitialTickets
    // conseille de le dupliquer. En particulier, essayer d’appeler ces deux méthodes depuis chooseTickets me
    // semble être une très mauvaise idée, car le code est alors très troublant à lire (pourquoi est-ce que chooseTickets,
    // destinée à être utilisée en cours de partie, appellerait-elle setInitialTicketChoice, destinée à être utilisée au début de la partie,

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {


        runLater(() -> graphicalPlayer.chooseTickets(options, ???));
        return ???;
    }


    //sans bloquer !
    @Override
    public int drawSlot() {
        ArrayBlockingQueue<Integer> fileBloqEmplacement = new ArrayBlockingQueue<Integer>(1);

        //méthode qui est appelée seulement la deuxième fois, pour le deuxième appel
        runLater(() -> graphicalPlayer.drawCard();
        return 0;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
    }
}
