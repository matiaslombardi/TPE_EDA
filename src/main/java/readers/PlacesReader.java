package readers;

import com.opencsv.CSVReader;
import model.PlaceLocation;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlacesReader {

    private static final String fileName = "src/main/resources/espacios-culturales.csv";

    public List<PlaceLocation> readPlaces() {
        List<PlaceLocation> places = new ArrayList<>();
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                PlaceLocation toAdd = new PlaceLocation(nextLine[3], Double.parseDouble(nextLine[13]),
                        Double.parseDouble(nextLine[14]));
                places.add(toAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }
}
