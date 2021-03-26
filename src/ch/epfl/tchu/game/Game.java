package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Map;
import java.util.Random;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
public class Game {

    /**
     * Fait jouer une partie
     * @param players joueurs
     * @param playerNames les noms des joueurs
     * @param tickets les billets dissponibles
     * @param rng générateur aléatoire de nombres
     */
    public void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng){
        Preconditions.checkArgument((players.size() == 2 )|| (playerNames.size() == 2));

    }
}
