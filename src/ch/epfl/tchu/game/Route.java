package ch.epfl.tchu.game;


import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * Une classe representant une route qui va lier deux gares entre elles et ses caractéristiques
 */
public final class Route {
    private String id;
    private Station station1;
    private Station station2;
    private int length;
    private Level level;
    private Color color;


    /**
     * Constructeur de la classe route qui aura comme paramètres:
     *
     * @param id l'identité de la route
     * @param station1 la première gare de la route
     * @param station2 la deuxième gare de la route
     * @param length la longueur de la route
     * @param level le niveau auquel se trouve la route
     * @param color la couleur de la route
     *
     * Et va lancer:
     * @throws IllegalArgumentException dans Preconditions
     * si les deux stations sont égales ou que la longeur de la route n'est pas comprise
     * entre l'intervalle donnée
     *
     * @throws NullPointerException si une des stations ou/et l'identifiant sont nulls
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        Preconditions.checkArgument(station1.equals(station2) || length <= Constants.MAX_ROUTE_LENGTH ||
                length >= Constants.MIN_ROUTE_LENGTH);
        if (id == null || station1 == null || station2 == null) {
            throw new NullPointerException("l'id, la station 1 ou la station 2 est nulle");
        }
        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.length = Objects.requireNonNull(length);
        this.level = Objects.requireNonNull(level);
        this.color = color;

    }

    /**
     * Type énuméré qui représente les deux niveaux auquel une route
     * peut se trouver
     */
    public enum Level {
        OVERGROUND("route en surface"), UNDERGROUND("route en tunnel");
        private final String nomFrancais;

        private Level(String nomFrancais) {
            this.nomFrancais = nomFrancais;
        }
    }

    /**
     *
     * @return l'identité de la route
     */
    public String id() {
        return id;
    }

    /**
     *
     * @return la première gare de la route
     */
    public Station station1() {
        return station1;
    }

    /**
     *
     * @return la deuxième gare de la route
     */
    public Station station2() {
        return station2;
    }

    /**
     *
     * @return la longueur de la route
     */
    public int length() {
        return length;
    }

    /**
     *
     * @return le niveau auquel se trouve la route
     */
    public Level level() {
        return level;
    }

    /**
     *
     * @return la couleur de la route
     */
    public Color color() {
        return color;
    }

    /**
     *
     * @return la liste des deux gares de la route
     */
    public List<Station> stations() {
        return List.of(station1(), station2());
    }

    /**
     * Mézhode qui prend en parametre:
     * @param station la station et
     * @return son opposé
     *
     * @throws IllegalArgumentException (grâce à Preconditions.checkArgument)
     * quand la gare donnée n'est ni la première ni la seconde gare de la route
     */
    public Station stationOpposite(Station station) {
        Preconditions.checkArgument(station.equals(station1) ||
                station.equals(station2));
        if (station.equals(station1)) {
            return station2();
        } else {
            return station1();
        }

    }

    /**
     * Méthode qui retourne:
     * @return la liste de tous les ensembles de cartes qui pourraient être joués
     * pour s'emparer d'une route
     */

    //ERREUR AVEC LA METHODE
    //TEST NE PASSE PAS
    //RAJOUTER DANS DES SORTED BAGS DIFF
    public List<SortedBag<Card>> possibleClaimCards() {

        SortedBag.Builder<Card> possibleClaimCards = new SortedBag.Builder<>();
        List<SortedBag<Card>> sortedBagList = new ArrayList<>();
        int j = length();  // j = nb de wagons

        //le cas de Overground

        if (level() == Level.OVERGROUND){
            //couleur grise
            if (color() == null){
                for (Card c : Card.CARS){
                    sortedBagList.add(SortedBag.of(j, c));
                }
            }
            else {
                sortedBagList.add(SortedBag.of(j, Card.of(color())));
            }

        //le cas de Underground
        } else {
            for (int i = 0; i <= length(); i++){ // nb de locomotives

                //couleur grise
                if (color() == null) {
                    if(i == length() && j == 0){
                        sortedBagList.add(SortedBag.of(i, Card.LOCOMOTIVE));
                    }
                    else{
                        for (Card c : Card.CARS){
                            sortedBagList.add(SortedBag.of(j, c , i, Card.LOCOMOTIVE));
                        }
                    }

                } else {
                    sortedBagList.add(SortedBag.of(j, Card.of(color()), i, Card.LOCOMOTIVE));
                }

                j--;
            }
        }
        return sortedBagList;
    }

    /**
     * Méthode qui prend en paramètre:
     * @param claimCards cartes posés par le joueur
     * @param drawnCards cartes tirés de la pioche
     * @return nombre de cartes additionnelles à jouer pour s'emparer d'un tunnel
     *
     * @throws IllegalArgumentException (grâce à Preconditions.checkArgument)
     * si la route à laquelle on applique la méthode n'est pas un tunnel ou si
     * drawnCards ne contient pas exactement trois cartes
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards){

        Preconditions.checkArgument(level().equals(Level.UNDERGROUND) && drawnCards.size()==3);
        int count = 0;
        for (int i = 0; i < claimCards.size(); i++) {
            if (drawnCards.get(i).equals(claimCards.get(0)) || drawnCards.get(i).equals(Card.LOCOMOTIVE)){
                count += 1;
            }
        }
        return count;

    }

    /**
     * Méthode qui retourne:
     * @return le nombre de points de construction qu'un joueur obtient
     * en s'emparant d'une route
     */
    public int claimPoints(){
        return Constants.ROUTE_CLAIM_POINTS.get(length());
    }


}