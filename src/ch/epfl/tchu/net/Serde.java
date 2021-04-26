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
public interface Serde<T> {

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

    //TODO possiblement faux
    static <T> Serde<T> oneOf(List<T> listEnum){
        Preconditions.checkArgument(!listEnum.isEmpty());
        return Serde.of(i ->{
            if(i == null){
                return "";
            } return Integer.toString(listEnum.indexOf(i));
        }, i ->   //TODO cas pour desirialize ??  pour i ""
             listEnum.get(Integer.parseInt(i)));

    }





    static <X> Serde<List<X>> listOf(Serde<X> serde, String stringDelimit){
        return new Serde <> () {
            @Override
            public String serialize(List<X> liste) { //TODO StringJoiner

                String[] serdeString = new String[liste.size()];
                for (int i = 0; i < liste.size(); ++i) {
                    serdeString[i] = serde.serialize(liste.get(i));
                }
                List<String> l = Arrays.asList(serdeString);
                return String.join(stringDelimit, l);
            }

            @Override
            public List<X> deserialize(String string) {
                if (string.length() == 0){
                    return List.of();
                }
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
            public String serialize(SortedBag<X> serdeSortedBag) {
                return listOf(serde, stringDelimit).serialize(serdeSortedBag.toList());
            }

            @Override
            public SortedBag<X> deserialize(String string) {
                return SortedBag.of(Serde.listOf(serde, stringDelimit).deserialize(string));
            }
        };

    }

}
