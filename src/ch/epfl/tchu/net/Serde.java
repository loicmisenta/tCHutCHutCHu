package ch.epfl.tchu.net;

import java.util.function.Function;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
interface Serde<T> {

    String serialize(T object);

    T deserialize(String string);

    static <X> Serde<X> of(Function<X, String> serialization, Function<String, X> deserialization){
        return new Serde<>() {
            @Override
            public String serialize(X object) {
                return serialization.apply(object);
            }

            @Override
            public X deserialize(String string) {
                return deserialization.apply(string);
            }
        };
    }


    static void oneOf(){

    }

}
