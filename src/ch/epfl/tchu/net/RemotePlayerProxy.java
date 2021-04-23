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
        String initPlayerStringSer = String.join(" ", MessageId.INIT_PLAYERS.name() , ownIdSerialize, playerNamesSerialize);
    }

    @Override
    public void receiveInfo(String info)  {
        sendMessage(MessageId.RECEIVE_INFO, stringSerde.serialize(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return null;
    }

    @Override
    public int drawSlot() {
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
