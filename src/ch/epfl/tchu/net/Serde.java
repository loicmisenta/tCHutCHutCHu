package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author loicmisenta (330593)
 * @author lagutovaalexandra (324449)
 * Interface qui représente un serde (de serializer-deserializer)
 * un objet capable de sérialiser et désérialiser des valeurs
 */
public interface Serde<T> {

    /**
     * Méthode statique sérialisant un élément donné
     * @param object l'objet à sérializer
     * @return String sérializé
     */
    String serialize(T object);

    /**
     * Méthode qui va desérializer un String donné
     * @param string l'objet à desérializer
     * @return l'objet
     */
    T deserialize(String string);

    /**
     *
    * @param serialization fonction de sérialisation
    * @param deserialization fonction de desérialisation
    * @param <T> type de serde
    * @return le serde
     */
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

    /**
     *
    * @param listEnum liste des valeuts
    * @param <T> type de serde
    * @return le serde
     */
    static <T> Serde<T> oneOf(List<T> listEnum){
        Preconditions.checkArgument(!listEnum.isEmpty());
        return Serde.of(i ->{
            if(i == null){
                return "";
            } return Integer.toString(listEnum.indexOf(i));
        }, i -> listEnum.get(Integer.parseInt(i)));

    }


    /**
    *
    * @param serde serde
    * @param stringDelimit un caractère de séparatoin
    * @param <X> type de la serde
    * @return serde serialisant et desérisalisant les listes
     */

    static <X> Serde<List<X>> listOf(Serde<X> serde, String stringDelimit){
        return new Serde <> () {
            @Override
            public String serialize(List<X> liste) {

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

    /**
     *
    * @param serde serde
    * @param  stringDelimit un caractère de séparatoin
    * @param <X> type de la serde
    * @return serde serialisant et desérisalisant les sortedBag
     */
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
