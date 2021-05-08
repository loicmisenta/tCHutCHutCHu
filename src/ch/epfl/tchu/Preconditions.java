package ch.epfl.tchu;


/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 *
 * Cette méthode contrôle que l'appel de la méthode ne se fait
 * qu'avec des arguments valides.
 */

public final class Preconditions {
    private Preconditions() {}

    /**
     * La méthode va qui check
     * si la valeur est fausse
     * @param b boolean
     */
    public static void checkArgument(boolean b){
            if (!b){
                throw new IllegalArgumentException();
        }
    }
}
