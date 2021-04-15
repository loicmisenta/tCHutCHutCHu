package ch.epfl.tchu.net;

import java.util.List;
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


    static <X> Serde<X> oneOf(List<X> listEnum){
        Function<X, String> f = (X t) -> String.valueOf(listEnum.indexOf(t));
        Function<String, X> f2 = (X t) -> listEnum.indexOf(t);
        return Serde.of(f)
    }

}
