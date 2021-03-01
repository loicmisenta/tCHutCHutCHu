package ch.epfl.tchu.game;


import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Objects;

public final class Route {
    String id;
    Station station1;
    Station station2;
    int length;
    Level level;
    Color color;

    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        Preconditions.checkArgument(station1.equals(station2) || length <= Constants.MAX_ROUTE_LENGTH ||
                length >= Constants.MIN_ROUTE_LENGTH);
        //????
        if (id == null || station1 == null || station2 == null) {
            throw new NullPointerException("l'id, la station 1 ou la station 2 est nulle");
        }
        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.length = length;
        this.level = level;
        this.color = color;

    }

    public enum Level {
        OVERGROUND("route en surface"), UNDERGROUND("route en tunnel");
        private final String nomFrancais;

        private Level(String nomFrancais) {
            this.nomFrancais = nomFrancais;
        }
    }

    public String id() {
        return id;
    }

    public Station station1() {
        return station1;
    }

    public Station station2() {
        return station2;
    }

    public int length() {
        return length;
    }

    public Level level() {
        return level;
    }

    public Color color() {
        return color;
    }

    public List<Station> stations() {
        return List.of(station1(), station2());
    }

    public Station stationOpposite(Station station) {
        Preconditions.checkArgument(station.equals(station1) ||
                station.equals(station2));
        if (station.equals(station1)) {
            return station2();
        } else {
            return station1();
        }

    }

    public List<SortedBag<Card>> possibleClaimCards() {
        SortedBag.Builder<Card> possibleClaimCards = new SortedBag.Builder<>();

        //le cas de Overground
        if (level == Level.OVERGROUND){
            //ajouter le nombre de wagons en fnct de la long
            for (int i = 1; i <= length; i++){
                //couleur grise
                if (color == null ){
                    for (Card c : Card.CARS){ possibleClaimCards.add(c); }
                } else {
                    possibleClaimCards.add(Card.of(color)); }
            }
        //le cas de underground
        } else {
            for (int i = 0; i <= length; i++){ // nb de locomotives
                for(int j = length ; j >= 0; j-- ){ // nb de wagons
                    if (color == null) {
                        for (Card c : Card.CARS){ possibleClaimCards.add(SortedBag.of(j, c , i, Card.LOCOMOTIVE)); }
                    } else {
                        possibleClaimCards.add(SortedBag.of(j, Card.of(color), i, Card.LOCOMOTIVE));
                    }
                }
            }

        }
        return List.of(possibleClaimCards.build());
    }


}
