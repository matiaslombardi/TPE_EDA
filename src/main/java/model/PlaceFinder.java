package model;
import com.opencsv.CSVReader;
import utils.QGrams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class PlaceFinder {
    private final List<PlaceLocation> places = new ArrayList<>();
    private final QGrams qGrams = new QGrams(3);

    public PlaceFinder() throws IOException {
        String fileName = "src/main/resources/espacios-culturales.csv";
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                PlaceLocation toAdd = new PlaceLocation(nextLine[3], Double.parseDouble(nextLine[13]),
                        Double.parseDouble(nextLine[14]));
                places.add(toAdd);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<PlaceLocation> findPlaces(String query){
        TreeSet<BetterMatch> toReturn = new TreeSet<>();
        for (PlaceLocation place : places) {
            double sim = qGrams.similarity(place.getName(), query);
            BetterMatch toAdd = new BetterMatch(place, sim);
            if(toReturn.size() < 10) {
                toReturn.add(toAdd);
            }
            else if(toReturn.last().getSimilarity() < sim){
                toReturn.remove(toReturn.last());
                toReturn.add(new BetterMatch(place, sim));
            }
        }
        return toReturn.stream().map(BetterMatch::getPlace).collect(Collectors.toList());
    }

    private static class BetterMatch implements Comparable<BetterMatch>{
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
    }
}
