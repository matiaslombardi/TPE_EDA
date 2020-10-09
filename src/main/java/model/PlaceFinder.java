package model;
import utils.QGrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PlaceFinder {
    private final List<PlaceLocation> places;
    private final QGrams qGrams = new QGrams(3);

    public PlaceFinder(List<PlaceLocation> places) {
        this.places = places;
    }

    public List<PlaceLocation> findPlaces(String query){
        TreeSet<PlaceLocation> toReturn = new TreeSet<>();
        for (PlaceLocation place : places) { // p lugares
            double sim = qGrams.similarity(place.getName(), query); // n * m
            place.setSim(sim);
            if(toReturn.size() < 10) {
                toReturn.add(place); // log a, a < 10 = cte
            } else if(toReturn.last().getSim() < sim) {
                toReturn.remove(toReturn.last()); // log 10 = cte
                toReturn.add(place); // log 9
            }
        } // p * n * m
        return new ArrayList<>(toReturn); // Orden 10
    }

    /*private static class BetterMatch implements Comparable<BetterMatch>{
        private final PlaceLocation place;
        private final double similarity;

        public BetterMatch(PlaceLocation place, double similarity) {
            this.place = place;
            this.similarity = similarity;
        }

        public PlaceLocation getPlace() {
            return place;
        }

        public double getSimilarity() {
            return similarity;
        }

        @Override
        public int compareTo(BetterMatch o) {
            int cmp = Double.compare(o.similarity, similarity);
            if(cmp == 0){
                cmp = place.getName().compareTo(o.place.getName());
            }
            return cmp;
        }
    }*/
}
