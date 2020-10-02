package model;
import com.opencsv.CSVReader;
import utils.QGrams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class PlaceFinder {
    private final List<PlaceLocation> places;
    private final QGrams qGrams = new QGrams(3);

    public PlaceFinder(List<PlaceLocation> places) {
        this.places = places;
    }

    public List<PlaceLocation> findPlaces(String query){
        TreeSet<PlaceLocation> toReturn = new TreeSet<>();
        for (PlaceLocation place : places) {
            double sim = qGrams.similarity(place.getName(), query);
            place.setSim(sim);
            if(toReturn.size() < 10) {
                toReturn.add(place);
            } else if(toReturn.last().getSim() < sim) {
                toReturn.remove(toReturn.last());
                toReturn.add(place);
            }
        }
        return new ArrayList<>(toReturn);
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
