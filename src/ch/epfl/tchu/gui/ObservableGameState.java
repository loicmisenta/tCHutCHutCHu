package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.*;
import javafx.beans.property.*;

import java.util.*;

public class ObservableGameState {
    private final PlayerId playerId;
    private PublicGameState publicGameState;
    private PlayerState playerState;


    //TODO comment mettre null ou 0 par défaut ?
    //groupe1
    private final ReadOnlyIntegerProperty percentageTicketsLeft = new SimpleIntegerProperty((int)(((double)publicGameState.ticketsCount()/ (double) ChMap.ALL_TICKETS.size()) * 100));
    private final ReadOnlyIntegerProperty percentageCardsLeft = new SimpleIntegerProperty((int)(((double)publicGameState.cardState().deckSize()/(double)Constants.TOTAL_CARDS_COUNT) * 100));
    private final List<ObjectProperty<Card>> faceUpCards = createFaceUpCards();
    private final Map<ObjectProperty<Route>, ObjectProperty<PlayerId>> ownedRoutes = createOwnedRoutes();

    //groupe2

    //groupe3
    private List<ObjectProperty<Ticket>> ticketList = createListTickets();//TODO Object ou ReadOnly
    private List<IntegerProperty> nbTypeCarte = createNbTypeCarte();

    private List<ObjectProperty<Ticket>> createListTickets(){
        List<ObjectProperty<Ticket>> listTickets = new ArrayList<>();
        for (Ticket t: playerState.tickets()) {
            listTickets.add(new SimpleObjectProperty<>(t));
        }
        return listTickets;
    }

    private List<IntegerProperty> createNbTypeCarte(){
        List<IntegerProperty> nbTypeCarte = new ArrayList<>();
        for (Card c: Card.ALL) {
            nbTypeCarte.add(new SimpleIntegerProperty(playerState.cards().countOf(c)));
        }
        return nbTypeCarte;
    }


    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;
    }

    public void setState(PublicGameState publicGameState, PlayerState playerState){
        this.publicGameState = publicGameState;
        this.playerState = playerState;
    }

    private List<ObjectProperty<Card>> createFaceUpCards(){
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>();
        for (Card c: publicGameState.cardState().faceUpCards()) {
            faceUpCards.add(new SimpleObjectProperty<>(c));
        }
        return faceUpCards;
    }

    private Map<ObjectProperty<Route>, ObjectProperty<PlayerId>> createOwnedRoutes(){
        Map<ObjectProperty<Route>, ObjectProperty<PlayerId>> ownedRoutes = new HashMap<>(ChMap.routes().size());
        for (Route r: ChMap.routes()) {
            if(publicGameState.playerState(PlayerId.PLAYER_1).routes().contains(r)){
                ownedRoutes.put(new SimpleObjectProperty<>(r), new SimpleObjectProperty<>(PlayerId.PLAYER_1));
            } else if (publicGameState.playerState(PlayerId.PLAYER_2).routes().contains(r)){
                ownedRoutes.put(new SimpleObjectProperty<>(r), new SimpleObjectProperty<>(PlayerId.PLAYER_2));
            } else{
                ownedRoutes.put(new SimpleObjectProperty<>(r), null);
            }
        }
        return ownedRoutes;
    }




}
