package ch.epfl.tchu;


/**
 * @author loicmisenta
 * @author lagutovaalexandra
 *
 * Cette méthode contrôle que l'appel de la méthode ne se fait
 * qu'avec des arguments valides.
 * @throws IllegalArgumentException
 */

public final class Preconditions {
    private Preconditions() {}
    public static void checkArgument(boolean b){
            if (!b){
                throw new IllegalArgumentException();
        }
    }
}
