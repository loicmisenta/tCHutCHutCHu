package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.*;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;

public class ObservableGameState {
    private final PlayerId playerId;
    private PublicGameState publicGameState;
    private PlayerState playerState;


    //groupe1
    private final IntegerProperty percentageTicketsLeft = createPercentageTicketsLeft();
    private final IntegerProperty percentageCardsLeft = createPercentageCardsLeft();
    private final List<ObjectProperty<Card>> faceUpCards = createFaceUpCards();
    private final Map<Route, ObjectProperty<PlayerId>> ownedRoutes = createOwnedRoutes();

    //groupe2
    private final Map<PlayerId, IntegerProperty> ownedTickets = createOwnedTickets();
    private final Map<PlayerId, IntegerProperty> ownedCard = createOwnedCard();
    private final Map<PlayerId, IntegerProperty> ownedCars = createOwnedCars();
    private final Map<PlayerId, IntegerProperty> ownedConstructPoints = createOwnedConstructPoints();

    //groupe3
    private List<ObjectProperty<Ticket>> ticketList = createListTickets(); //TODO bien une liste ?
    private List<IntegerProperty> nbTypeCarte = createNbTypeCarte();
    private Map<Route, BooleanProperty> claimableRoutes = createClaimableRoutes();



    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;
    }

    public void setState(PublicGameState publicGameState, PlayerState playerState){
        this.publicGameState = publicGameState;
        this.playerState = playerState;
    }

    private IntegerProperty createPercentageCardsLeft() {
        return new SimpleIntegerProperty((int) (((double) publicGameState.cardState().deckSize() / (double) Constants.TOTAL_CARDS_COUNT) * 100));
    }

    private IntegerProperty createPercentageTicketsLeft() {
        return new SimpleIntegerProperty((int) (((double) publicGameState.ticketsCount() / (double) ChMap.ALL_TICKETS.size()) * 100));
    }

    private List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>();
        for (Card c : publicGameState.cardState().faceUpCards()) {
            faceUpCards.add(new SimpleObjectProperty<>(c));
        }
        return faceUpCards;
    }

    private Map<Route, ObjectProperty<PlayerId>> createOwnedRoutes() {
        Map<Route, ObjectProperty<PlayerId>> ownedRoutes = new HashMap<>(ChMap.routes().size());
        for (Route r : ChMap.routes()) {
            if (publicGameState.playerState(PlayerId.PLAYER_1).routes().contains(r)) {
                ownedRoutes.put(r, new SimpleObjectProperty<>(PlayerId.PLAYER_1));
            } else if (publicGameState.playerState(PlayerId.PLAYER_2).routes().contains(r)) {
                ownedRoutes.put(r, new SimpleObjectProperty<>(PlayerId.PLAYER_2));
            } else {
                ownedRoutes.put(r, null);
            }
        }
        return ownedRoutes;
    }


    private ObservableList<Ticket> createListTickets() {
        ObservableList<Ticket> listTickets = observableArrayList();
        for (Ticket t : playerState.tickets()) {
            listTickets.add(new SimpleObjectProperty<>(t));
        }
        return listTickets;
    }

    private List<IntegerProperty> createNbTypeCarte () {
        List<IntegerProperty> nbTypeCarte = new ArrayList<>();
        for (Card c : Card.ALL) {
            nbTypeCarte.add(new SimpleIntegerProperty(playerState.cards().countOf(c)));
        }
        return nbTypeCarte;
    }

    private Map<PlayerId, IntegerProperty> createOwnedTickets() {
        Map<PlayerId, IntegerProperty> ownedTickets = new EnumMap<>(PlayerId.class);
        ownedTickets.put(PlayerId.PLAYER_1, new SimpleIntegerProperty(publicGameState.playerState(PlayerId.PLAYER_1).ticketCount()));
        ownedTickets.put(PlayerId.PLAYER_2, new SimpleIntegerProperty(publicGameState.playerState(PlayerId.PLAYER_2).ticketCount()));
        return ownedTickets;
    }


    private Map<PlayerId, IntegerProperty> createOwnedCard() {
        Map<PlayerId, IntegerProperty> ownedCard = new EnumMap<>(PlayerId.class);
        ownedCard.put(PlayerId.PLAYER_1, new SimpleIntegerProperty(publicGameState.playerState(PlayerId.PLAYER_1).cardCount()));
        ownedCard.put(PlayerId.PLAYER_2, new SimpleIntegerProperty(publicGameState.playerState(PlayerId.PLAYER_2).cardCount()));
        return ownedCard;
    }

    private Map<PlayerId, IntegerProperty> createOwnedCars() {
        Map<PlayerId, IntegerProperty> ownedCars = new EnumMap<>(PlayerId.class);
        ownedCars.put(PlayerId.PLAYER_1, new SimpleIntegerProperty(publicGameState.playerState(PlayerId.PLAYER_1).carCount()));
        ownedCars.put(PlayerId.PLAYER_2, new SimpleIntegerProperty(publicGameState.playerState(PlayerId.PLAYER_2).carCount()));
        return ownedCars;
    }

    private Map<PlayerId, IntegerProperty> createOwnedConstructPoints() {
        Map<PlayerId, IntegerProperty> ownedConstructPoints = new EnumMap<>(PlayerId.class);
        ownedConstructPoints.put(PlayerId.PLAYER_1, new SimpleIntegerProperty(publicGameState.playerState(PlayerId.PLAYER_1).claimPoints()));
        ownedConstructPoints.put(PlayerId.PLAYER_2, new SimpleIntegerProperty(publicGameState.playerState(PlayerId.PLAYER_2).claimPoints()));
        return ownedConstructPoints;
    }

    private Map<Route, BooleanProperty> createClaimableRoutes() {
        Map<Route, BooleanProperty> claimableRoutes = new HashMap<>(ChMap.routes().size());
        for (Route r : ChMap.routes()) {
            //TODO verifier conditions
            if ((playerId == publicGameState.currentPlayerId()) && (ownedRoutes.get(r) == null) && (playerState.canClaimRoute(r))) {
                claimableRoutes.put(r, new SimpleBooleanProperty(true));
            }
            else{
                claimableRoutes.put(r, new SimpleBooleanProperty(false));
            }
        }
        return claimableRoutes;
    }

}
