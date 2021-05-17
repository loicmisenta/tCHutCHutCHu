package ch.epfl.tchu.net;

import ch.epfl.tchu.game.*;
import java.io.*;
import java.net.Socket;
import java.util.EnumMap;
import java.util.Map;

import static ch.epfl.tchu.net.Serdes.*;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * classe qui represente représente un client de joueur distant
 */
public class RemotePlayerClient {
    private final Player player;
    private final String nom;
    private final int port;

    /**
     * constructeur
     * @param player le joueur
     * @param nom    le nom
     * @param port   le port pour se connecter au mandataire
     */
    public RemotePlayerClient(Player player, String nom, int port) {
        System.out.println("client lancé");
        this.player = player;
        this.nom = nom;
        this.port = port;
    }

    /**
     * cette méthode attend un message en provenance du mandataire,
     * le découpe,
     * détermine le type du message,
     * en fonction de ce type de message, désérialise les arguments,
     * appelle la méthode correspondante du joueur,
     * si cette méthode retourne un résultat, le sérialise pour le renvoyer au mandataire en réponse.
     */
    public void run(){
        try (Socket socket = new Socket(nom,port);
             BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())))
        {
            String s;
            while ((s = r.readLine()) != null){
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
                        w.write('\n');
                        w.flush();
                        break;
                    case NEXT_TURN:
                        w.write(turnKindSerde.serialize(player.nextTurn()));
                        w.write('\n');
                        w.flush();
                        break;
                    case CHOOSE_TICKETS:
                        w.write(sortedBagOfTicketSerde.serialize(player.chooseTickets(sortedBagOfTicketSerde.deserialize(ls[1]))));
                        w.write('\n');
                        w.flush();
                        break;
                    case DRAW_SLOT:
                        w.write(intSerde.serialize(player.drawSlot()));
                        w.write('\n');
                        w.flush();
                        break;
                    case ROUTE:
                        Route route = player.claimedRoute();
                        w.write(routeSerde.serialize(route));
                        w.write('\n');
                        w.flush();
                        break;
                    case CARDS:
                        w.write(sortedBagOfCardSerde.serialize(player.initialClaimCards()));
                        w.write('\n');
                        w.flush();
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        w.write(sortedBagOfCardSerde.serialize(player.chooseAdditionalCards(listSortedBagOfCard.deserialize(ls[1]))));
                        w.write('\n');
                        w.flush();
                        break;
                }
        }} catch(IOException e){
                throw new UncheckedIOException(e);
            }

    }

}
