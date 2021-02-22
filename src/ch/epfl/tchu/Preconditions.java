package ch.epfl.tchu;

public final class Preconditions {
    private Preconditions() {}
    private static void checkArgument(boolean b){
            if (!b){
                throw new IllegalArgumentException();
        }
    }
}
