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

    }

}
