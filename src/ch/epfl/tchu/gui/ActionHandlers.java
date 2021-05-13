package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * L'interface contenant les cinq interfaces fonctionelles imbriquées
 * Qui eux répresnetent un "gestionnaire d'actions"
 */
public interface ActionHandlers {


    @FunctionalInterface
    interface DrawTicketsHandler{

        //TODO doit conteneir un gestionnaire valide ? TYPE DE RETOUR ?
        /**
         * Appellée quand le joueur decide de tirer des tickets
         */
        void onDrawTickets();
    }

    @FunctionalInterface
    interface DrawCardHandler{
        //TODO type de retour

        /**
         * ppelée lorsque le joueur désire tirer une carte à l'emplacement donné
         * @param emplacement l'emplacement
         */
        void onDrawCard(int emplacement);
    }

    @FunctionalInterface
    interface ClaimRouteHandler{
        /**
         * Appelée lorsque le joueur désire s'emparer de la route donnée
         * @param route route donnée
         * @param cartes  cartes données
         */
        void onClaimRoute(Route route, SortedBag<Card> cartes);
    }

    @FunctionalInterface
    interface ChooseTicketsHandler{
        /**
         * Appelée lorsque le joueur a choisi de garder les billets donnés suite à un tirage de billets
         * @param tickets billets
         */
        void onChooseTickets(SortedBag<Ticket> tickets);
    }

    @FunctionalInterface
    interface ChooseCardsHandler{
        /**
         * Appelée lorsque le joueur a choisi d'utiliser les cartes données comme cartes initiales
         * ou additionnelles lors de la prise de possession d'une route
         * @param cartes données
         */
        void onChooseCards(SortedBag<Card> cartes);
    }
}
