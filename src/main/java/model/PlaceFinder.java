package model;
import com.opencsv.CSVReader;
import utils.Levenshtein;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class PlaceFinder {
    private final List<PlaceLocation> places = new ArrayList<>();

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
        Set<BetterMatch> aux = new TreeSet<>();
        for (PlaceLocation place : places) {
            aux.add(new BetterMatch(place, Levenshtein.normalizedSimilarity(place.getName(), query)));
        }
        List<PlaceLocation> toReturn = new ArrayList<>();
        int amount = 0;
        for (BetterMatch betterMatch : aux) {
            toReturn.add(betterMatch.getPlace());
            amount++;
            if(amount == 9)
                break;
        }
        return toReturn;
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
