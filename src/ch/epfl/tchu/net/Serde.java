package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author loicmisenta
 * @author lagutovaalexandra
 */
interface Serde<T> {

    String serialize(T object);

    T deserialize(String string);

    static <T> Serde<T> of(Function<T, String> serialization, Function<String, T> deserialization){
        return new Serde<>() {
            @Override
            public String serialize(T object) {
                return serialization.apply(object);
            }

            @Override
            public T deserialize(String string) {
                return deserialization.apply(string);
            }
        };
    }


    static <T> Serde<T> oneOf(List<T> listEnum){
        Preconditions.checkArgument(!listEnum.isEmpty());
        //Function<X, String> f = (X t) -> String.valueOf(listEnum.indexOf(t));
        //Function<String, X> f2 = (X t) -> listEnum.indexOf(t);
        return Serde.of(i -> Integer.toString(listEnum.indexOf(i)), Integer::parseInt);
    }





    static <X> Serde<List<X>> listOf(Serde<X> serde, String stringDelimit){
        return new Serde <> () {
            @Override
            public String serialize(List<X> serde) {
                String[] serdeString = new String[serde.size()];
                for (X el: serde) {
                    serdeString[serde.indexOf(el)] += el.toString();
                }
                List<String> l = Arrays.asList(serdeString);
                return String.join(stringDelimit, l);
            }

            @Override
            public List<X> deserialize(String string) {
                String[] stringOfDes = string.split(Pattern.quote(stringDelimit), -1);
                List<X> liste = new ArrayList<>();
                for (String s: stringOfDes) {
                    liste.add(serde.deserialize(s));
                }
                return liste;
            }
        };
    }

    static <X extends Comparable<X>> Serde<SortedBag<X>> bagOf(Serde<X> serde, String stringDelimit){
        return new Serde <> () {
            @Override
            public String serialize(SortedBag<X> serde) {
                String[] serdeString = new String[serde.size()];
                for (int i = 0 ; i < serde.size(); ++i) {
                    serdeString[i] += serde.get(i).toString();
                }
                List<String> l = Arrays.asList(serdeString);
                return String.join(stringDelimit, l);
            }

            @Override
            public SortedBag<X> deserialize(String string) {
                String[] stringOfDes = string.split(Pattern.quote(stringDelimit), -1);
                List<X> liste = new ArrayList<>();
                for (String s: stringOfDes) {
                    liste.add(serde.deserialize(s));
                }
                return SortedBag.of(liste);
            }
        };

    }

}
