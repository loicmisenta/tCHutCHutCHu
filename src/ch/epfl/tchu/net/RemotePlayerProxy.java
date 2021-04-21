package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.net.Serdes.*;

public class RemotePlayerProxy implements Player {
    final Socket socket;

    public RemotePlayerProxy(Socket socket){
        this.socket = socket;
    }

    private String readMessage() throws IOException {
        try(socket) {
            return null;


        }catch (IOException e){
            throw new UncheckedIOException(e);

        }
    }

    private void sendMessage(MessageId messageId , String string){
        try(socket) {


        }catch (IOException e){
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
    public void receiveInfo(String info) {
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
