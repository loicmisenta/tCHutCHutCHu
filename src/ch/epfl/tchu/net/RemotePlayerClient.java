package ch.epfl.tchu.net;

import ch.epfl.tchu.game.*;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.*;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.net.Serdes.*;

public class RemotePlayerClient {
    private final Player player;
    private final String nom;
    private final int port;


    public RemotePlayerClient(Player player, String nom, int port) {
        this.player = player;
        this.nom = nom;
        this.port = port;
    }


    public void run(){
        try (Socket socket = new Socket(nom,port);
             BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())))
        {
            String s;
            while ((s = r.readLine()) != null){
                System.out.println(s);
                String[] ls = s.split(" ");
                switch (MessageId.valueOf(ls[0])){
                    case INIT_PLAYERS:
                        Map<PlayerId, String> mapJoueurs = new EnumMap<>(PlayerId.class);
                        String[] noms = ls[2].split(",");
                        mapJoueurs.put(PlayerId.PLAYER_1, stringSerde.deserialize(noms[0]));
                        mapJoueurs.put(PlayerId.PLAYER_2, stringSerde.deserialize(noms[1]));
                        player.initPlayers(playerIdSerde.deserialize(ls[1]), mapJoueurs);
                        break;
                    case RECEIVE_INFO:
                        player.receiveInfo(stringSerde.deserialize(ls[1]));
                        break;
                    case UPDATE_STATE:
                        player.updateState(publicGameStateSerde.deserialize(ls[1]), playerStateSerde.deserialize(ls[2]));
                        break;
                    case SET_INITIAL_TICKETS:
                        player.setInitialTicketChoice(sortedBagOfTicketSerde.deserialize(ls[1]));
                        break;
                    case CHOOSE_INITIAL_TICKETS:
                        w.write(sortedBagOfTicketSerde.serialize(player.chooseInitialTickets()));
                        break;
                    case NEXT_TURN:
                        System.out.println("JE RENTRE MAIS RIEN NE SE PASSSE WHYY??? I DON'T KNOOOWWWW !!");
                        w.write(turnKindSerde.serialize(player.nextTurn()));
                        break;
                    case CHOOSE_TICKETS:
                        w.write(sortedBagOfTicketSerde.serialize(player.chooseTickets(sortedBagOfTicketSerde.deserialize(ls[1]))));
                        break;
                    case DRAW_SLOT:
                        w.write(intSerde.serialize(player.drawSlot()));
                        break;
                    case ROUTE:
                        w.write(routeSerde.serialize(player.claimedRoute()));
                        break;
                    case CARDS:
                        w.write(sortedBagOfCardSerde.serialize(player.initialClaimCards()));
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        w.write(sortedBagOfCardSerde.serialize(player.chooseAdditionalCards(listSortedBagOfCard.deserialize(ls[1]))));
                        break;
                }
        }} catch(IOException e){
                throw new UncheckedIOException(e);
            }

    }

}
