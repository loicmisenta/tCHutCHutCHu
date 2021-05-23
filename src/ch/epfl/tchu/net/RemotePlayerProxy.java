package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import static java.nio.charset.StandardCharsets.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static ch.epfl.tchu.net.Serdes.*;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * classe qui represente représente un proxy de joueur distant
 */
public final class RemotePlayerProxy implements Player {
    private final BufferedReader r;
    private final BufferedWriter w;
    /**
     * Constructeur du proxy
     * @param socket la "prise"
     * @throws IOException
     */
    public RemotePlayerProxy(Socket socket) throws IOException {
        r = new BufferedReader( new InputStreamReader(socket.getInputStream(), US_ASCII));
        w = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
    }

    /**
     * Methode privée pour lire un message.
     * @return String du message lu.
     */
    private String readMessage() { 
        try { return r.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    /**
     * Methode privée pour envoyer un message.
     */
    private void sendMessage(MessageId messageId , String string){
        try {
            w.write(String.join(" ", messageId.name(), string));
            w.write('\n');
            w.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Mise en oeuvre contrète de la méthode initPlayer (serialiser, puis envoyer)
     * @param ownId l'identité du joueur
     * @param playerNames noms des joueurs
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String ownIdSerialize = playerIdSerde.serialize(ownId);
        List<String> listPlayerNames = new ArrayList<>();
        for (PlayerId playerId: PlayerId.ALL) {
            listPlayerNames.add(playerNames.get(playerId));
        }
        String playerNamesSerialize = listStringSerde.serialize(listPlayerNames);
        String initPlayerStringSer = String.join(" ", ownIdSerialize, playerNamesSerialize);
        sendMessage(MessageId.INIT_PLAYERS, initPlayerStringSer);
    }

    /**
     * Mise en oeuvre contrète de la méthode receiveInfo (serialiser, puis envoyer)
     * @param info l'information
     */
    @Override
    public void receiveInfo(String info)  {
        sendMessage(MessageId.RECEIVE_INFO, stringSerde.serialize(info));
    }

    /**
     * Mise en oeuvre contrète de la méthode updateState (serialiser, puis envoyer)
     * @param newState nouvel état de la partie
     * @param ownState l'état propre du joueur
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        sendMessage(MessageId.UPDATE_STATE, String.join(" ", publicGameStateSerde.serialize(newState), playerStateSerde.serialize(ownState)));
    }

    /**
     * Mise en oeuvre contrète de la méthode setInitialTicketChoice (serialiser, puis envoyer)
     * @param tickets  les cinq billets qui lui ont été distribués
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        sendMessage(MessageId.SET_INITIAL_TICKETS, sortedBagOfTicketSerde.serialize(tickets));
    }

    /**
     * Mise en oeuvre contrète de la méthode chooseInitialTickets (serialiser, puis envoyer)
     * @return les tickets choisis (lu et deserialisé)
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS, " ");
        return sortedBagOfTicketSerde.deserialize(readMessage());
    }

    /**
     * Mise en oeuvre contrète de la méthode nextTurn (serialiser, puis envoyer)
     * @return le type d'action qu'il désire effectuer (lu et deserialisé)
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN, " ");
        return turnKindSerde.deserialize(readMessage());
    }

    /**
     * Mise en oeuvre contrète de la méthode chooseTickets (serialiser, puis envoyer)
     * @param options les billets tirés
     * @return les tickets choisi (lu et deserialisé)
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS, sortedBagOfTicketSerde.serialize(options));
        return sortedBagOfTicketSerde.deserialize(readMessage());
    }

    /**
     * Mise en oeuvre contrète de la méthode drawSlot (serialiser, puis envoyer)
     * @return l'emplacement de la carte visible/ ou de la pioche (lu et deserialisé)
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT, " ");
        return intSerde.deserialize(readMessage());
    }

    /**
     * Mise en oeuvre contrète de la méthode claimedRoute (serialiser, puis envoyer)
     * @return la route dont il veut s'emparer (lu et deserialisé)
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE, " ");
        return routeSerde.deserialize(readMessage());
    }

    /**
     * Mise en oeuvre contrète de la méthode initialClaimCards (serialiser, puis envoyer)
     * @return les cartes qu'il utilise pour s'emaprer de la route (lu et deserialisé)
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS, " ");
        return sortedBagOfCardSerde.deserialize(readMessage());
    }

    /**
     * Mise en oeuvre contrète de la méthode chooseAdditionalCards (serialiser, puis envoyer)
     * @param options les possibilités des cartes additionnelles
     * @return les cartes choisies additionelles (lu et deserialisé)
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS, listSortedBagOfCard.serialize(options));
        return sortedBagOfCardSerde.deserialize(readMessage());
    }
}
