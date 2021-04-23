package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import static java.nio.charset.StandardCharsets.*;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;
import static ch.epfl.tchu.net.Serdes.*;

public class RemotePlayerProxy implements Player {
    private final BufferedReader r;
    private final BufferedWriter w;

    public RemotePlayerProxy(Socket socket) throws IOException {
        r = new BufferedReader( new InputStreamReader(socket.getInputStream(), US_ASCII));
        w = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
    }

    private String readMessage() { //TODO sert à qqch?
        try { return r.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //TODO why MessageId en param ??
    private void sendMessage(MessageId messageId , String string){
        try {
            w.write(String.join(" ", messageId.name(), string));
            w.write('\n');
            w.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }


    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String ownIdSerialize = playerIdSerde.serialize(ownId);
        String playerNamesSerialize = listStringSerde.serialize(List.of(playerNames.get(PlayerId.PLAYER_1), playerNames.get(PlayerId.PLAYER_2)));
        String initPlayerStringSer = String.join(" ", ownIdSerialize, playerNamesSerialize);
        sendMessage(MessageId.INIT_PLAYERS, initPlayerStringSer);
    }

    @Override
    public void receiveInfo(String info)  {
        sendMessage(MessageId.RECEIVE_INFO, stringSerde.serialize(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        sendMessage(MessageId.UPDATE_STATE, String.join(" ", publicGameStateSerde.serialize(newState), playerStateSerde.serialize(ownState)));
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        sendMessage(MessageId.SET_INITIAL_TICKETS, sortedBagOfTicketSerde.serialize(tickets));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return sortedBagOfTicketSerde.deserialize(readMessage()); //ToDo exceptions
    }

    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN, " ");
        return turnKindSerde.deserialize(readMessage());
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS, sortedBagOfTicketSerde.serialize(options));
        return sortedBagOfTicketSerde.deserialize(readMessage());
    }

    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT, " ");
        return intSerde.deserialize(readMessage());
    }

    @Override
    public Route claimedRoute() {
        return routeSerde.deserialize(readMessage());
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return sortedBagOfCardSerde.deserialize(readMessage());
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS, listSortedBagOfCard.serialize(options));
        return sortedBagOfCardSerde.deserialize(readMessage());
    }
}
