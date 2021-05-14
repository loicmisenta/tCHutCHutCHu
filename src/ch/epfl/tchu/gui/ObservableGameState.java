package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Cette classe représente l'état observable d'une partie de tCHu
 */

public class ObservableGameState {
    private final PlayerId playerId;
    private PublicGameState publicGameState;
    private PlayerState playerState;

    //groupe1
    final private IntegerProperty percentageTicketsLeft;
    private final IntegerProperty percentageCardsLeft;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route, ObjectProperty<PlayerId>> ownedRoutes;

    //groupe2
    private final Map<PlayerId, IntegerProperty> ownedTickets;
    private final Map<PlayerId, IntegerProperty> ownedCards;
    private final Map<PlayerId, IntegerProperty> ownedCars;
    private final Map<PlayerId, IntegerProperty> ownedConstructPoints;

    //groupe3
    private final ObservableList<Ticket> ticketList;
    private final Map<Card, IntegerProperty> nbTypeCarte;
    private final Map<Route, BooleanProperty> claimableRoutes;

    /**
     * Constructeur de la classe ObservableGameState qui aura comme paramètres:
     * @param playerId l'id du joueur
     */
    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;

        percentageTicketsLeft = new SimpleIntegerProperty();
        percentageCardsLeft = new SimpleIntegerProperty();
        faceUpCards = createFaceUpCards();
        ownedRoutes = createOwnedRoutes();
        ownedTickets = createOwnedTickets();
        ownedCards = createOwnedCards();
        ownedCars = createOwnedCars();
        ownedConstructPoints = createOwnedConstructPoints();
        ticketList = observableArrayList();
        nbTypeCarte = createNbTypeCarte();
        claimableRoutes = createClaimableRoutes();

    }

    /**
     *  Cette méthode met à jour la totalité des propriétés décrites ci-dessus en fonction de ces deux états.
     * @param publicGameState la partie publique de l'état d'une partie
     * @param playerState l'état complet d'un joueur.
     */
    public void setState(PublicGameState publicGameState, PlayerState playerState){
        this.publicGameState = publicGameState;
        this.playerState = playerState;
        percentageTicketsLeft.set((int) (( (double) publicGameState.ticketsCount() / (double) ChMap.ALL_TICKETS.size()) * 100));
        percentageCardsLeft.set( (int)(( (double) publicGameState.cardState().deckSize() / (double) Constants.TOTAL_CARDS_COUNT) * 100));

        //faceupcard
        for (int slot : Constants.FACE_UP_CARD_SLOTS) {
            Card newCard = publicGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
        //ownedroute
        for (Route r : ChMap.routes()) {
            if (publicGameState.playerState(PlayerId.PLAYER_1).routes().contains(r)) {
                ownedRoutes.get(r).set(PlayerId.PLAYER_1);
            } else if (publicGameState.playerState(PlayerId.PLAYER_2).routes().contains(r)) {
                ownedRoutes.get(r).set(PlayerId.PLAYER_2);
            } else {
                ownedRoutes.get(r).set(null);
            }
        }
        //ownedtickets
        ownedTickets.get(PlayerId.PLAYER_1).set(publicGameState.playerState(PlayerId.PLAYER_1).ticketCount());
        ownedTickets.get(PlayerId.PLAYER_2).set(publicGameState.playerState(PlayerId.PLAYER_2).ticketCount());
        //ownedcards
        ownedCards.get(PlayerId.PLAYER_1).set(publicGameState.playerState(PlayerId.PLAYER_1).cardCount());
        ownedCards.get(PlayerId.PLAYER_2).set(publicGameState.playerState(PlayerId.PLAYER_2).cardCount());
        //ownedcars
        ownedCars.get(PlayerId.PLAYER_1).set(publicGameState.playerState(PlayerId.PLAYER_1).carCount());
        ownedCars.get(PlayerId.PLAYER_2).set(publicGameState.playerState(PlayerId.PLAYER_2).carCount());
        //ownedconstructpoints
        ownedConstructPoints.get(PlayerId.PLAYER_1).set(publicGameState.playerState(PlayerId.PLAYER_1).claimPoints());
        ownedConstructPoints.get(PlayerId.PLAYER_2).set(publicGameState.playerState(PlayerId.PLAYER_2).claimPoints());
        //ticketlist
        ticketList.setAll(playerState.tickets().toList());
        //nbtypecard
        for (Card c : Card.ALL) {
            nbTypeCarte.get(c).set(playerState.cards().countOf(c));
        }
        //claimableroute

        List<List<Station>> stationList = new ArrayList<>();
        for (Route r: publicGameState.claimedRoutes()) {
            stationList.add(r.stations());
        }

        claimableRoutes.forEach((r, b) -> b.set((playerId == publicGameState.currentPlayerId()) && !(stationList.contains(r.stations())) && playerState.canClaimRoute(r)));// && (ownedRoutes.get(r) == null) }

    }

    /**
     * @return la propriété contenant le pourcentage de ticket restant
     */
    public ReadOnlyIntegerProperty percentageTicketsReadOnly(){ return percentageTicketsLeft;}

    /**
     * @return la propriété contenant le pourcentage de carte restant
     */
    public ReadOnlyIntegerProperty percentageCardsLeftReadOnly(){ return percentageCardsLeft; }

    /**
     * @return la propriété contenant  la carte retournée a l'index donné
     * @param slot l'index contenant  la carte
     */
    public ReadOnlyObjectProperty<Card> faceUpCardsReadOnly(int slot){ return faceUpCards.get(slot); }

    /**
     * @return la propriété contenant le playerId du joueur possédant la carte donnée
     * @param route route donnée
     */
    public ReadOnlyObjectProperty<PlayerId> ownedRoutesReadOnly(Route route){ return ownedRoutes.get(route);}

    /**
     * @return la propriété contenant le nombre de ticket possedé par le joueur.
     * @param playerId id du joueur du quel on veut savoir son nombre de ticket
     */
    public ReadOnlyIntegerProperty ownedTicketsReadOnly(PlayerId playerId){ return ownedTickets.get(playerId); }

    /**
     * @return la propriété contenant le nombre de cartes possedé par le joueur.
     * @param playerId id du joueur du quel on veut savoir son nombre de carte
     */
    public ReadOnlyIntegerProperty ownedCardReadOnly(PlayerId playerId){ return ownedCards.get(playerId); }

    /**
     * @return la propriété contenant le nombre de wagon possedé par le joueur.
     * @param playerId id du joueur du quel on veut savoir son nombre de wagon
     */
    public ReadOnlyIntegerProperty ownedCarsReadOnly(PlayerId playerId){ return ownedCars.get(playerId); }
    /**
     * @return la propriété contenant le nombre de point de constructions possedé par le joueur.
     * @param playerId id du joueur du quel on veut savoir son nombre de points de constructions
     */
    public ReadOnlyIntegerProperty ownedConstructPointsReadOnly(PlayerId playerId){ return ownedConstructPoints.get(playerId); }

    /**
     * @return la propriété contenant le nombre de ticket possedé par le joueur auquel l'instance de ObservableGameState correspond.
     */
    public ObservableList<Ticket> ticketListReadOnly(){ return FXCollections.unmodifiableObservableList(ticketList);}

    /**
     * @return la propriété contenant le nombre de type de carte possedé par le joueur auquel l'instance de ObservableGameState correspond.
     */
    public ReadOnlyIntegerProperty nbTypeCarteReadOnly(Card c){ return nbTypeCarte.get(c); }

    /**
     * @return la propriété contenant vrai si la route est claimable.
     * @param route route passée en paramètre
     */
    public ReadOnlyBooleanProperty claimableRoute(Route route){ return claimableRoutes.get(route); }


    private List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>();
        for (int i : Constants.FACE_UP_CARD_SLOTS) {
            faceUpCards.add(new SimpleObjectProperty<>());
        }
        return faceUpCards;
    }

    private Map<Route, ObjectProperty<PlayerId>> createOwnedRoutes() {
        Map<Route, ObjectProperty<PlayerId>> ownedRoutes = new HashMap<>(ChMap.routes().size());
        for (Route r : ChMap.routes()) {
            ownedRoutes.put(r, new SimpleObjectProperty<>());
        }
        return ownedRoutes;
    }

    private Map<Card, IntegerProperty> createNbTypeCarte() {
        Map<Card, IntegerProperty> nbTypeCarte = new HashMap<>();
        for (Card c : Card.ALL) {
            nbTypeCarte.put(c, new SimpleIntegerProperty());
        }
        return nbTypeCarte;
    }

    private Map<PlayerId, IntegerProperty> createOwnedTickets() {
        Map<PlayerId, IntegerProperty> ownedTickets = new EnumMap<>(PlayerId.class);
        ownedTickets.put(PlayerId.PLAYER_1, new SimpleIntegerProperty());
        ownedTickets.put(PlayerId.PLAYER_2, new SimpleIntegerProperty());
        return ownedTickets;
    }


    private Map<PlayerId, IntegerProperty> createOwnedCards() {
        Map<PlayerId, IntegerProperty> ownedCards = new EnumMap<>(PlayerId.class);
        ownedCards.put(PlayerId.PLAYER_1, new SimpleIntegerProperty());
        ownedCards.put(PlayerId.PLAYER_2, new SimpleIntegerProperty());
        return ownedCards;
    }

    private Map<PlayerId, IntegerProperty> createOwnedCars() {
        Map<PlayerId, IntegerProperty> ownedCars = new EnumMap<>(PlayerId.class);
        ownedCars.put(PlayerId.PLAYER_1, new SimpleIntegerProperty());
        ownedCars.put(PlayerId.PLAYER_2, new SimpleIntegerProperty());
        return ownedCars;
    }

    private Map<PlayerId, IntegerProperty> createOwnedConstructPoints() {
        Map<PlayerId, IntegerProperty> ownedConstructPoints = new EnumMap<>(PlayerId.class);
        ownedConstructPoints.put(PlayerId.PLAYER_1, new SimpleIntegerProperty());
        ownedConstructPoints.put(PlayerId.PLAYER_2, new SimpleIntegerProperty());
        return ownedConstructPoints;
    }

    private Map<Route, BooleanProperty> createClaimableRoutes() {
        Map<Route, BooleanProperty> claimableRoutes = new HashMap<>(ChMap.routes().size());
        for (Route r : ChMap.routes()) {
            claimableRoutes.put(r, new SimpleBooleanProperty());
        }
        return claimableRoutes;
    }


    /**
     * @return vrai si il est encore possible de tirer des billets
     */
    public boolean canDrawTickets(){
        return publicGameState.canDrawTickets();
    }

    /**
     * @return  vrai si il est encore possible de tirer des cartes
     */
    public boolean canDrawCards(){
        return publicGameState.canDrawCards();
    }

    /**
     * @return la liste de tous les ensembles de cartes que le joueur pourrait utiliser
     * pour prendre possession de @param route.
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route){
        return playerState.possibleClaimCards(route);
    }
}
