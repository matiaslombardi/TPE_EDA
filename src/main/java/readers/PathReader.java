package readers;

import com.opencsv.CSVReader;
import model.Point;
import model.Trip;
import model.Stop;
import model.Graph;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathReader {
    
    public List<Stop> readPaths() {
        System.out.println("Reading stops");
        String fileName = "src/main/resources/paradas-de-colectivo.csv";
        List<Stop> stops = new ArrayList<>();
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                Stop toAdd = new Stop(new Point(Double.parseDouble(nextLine[3]), Double.parseDouble(nextLine[4])),
                        nextLine[8], Integer.parseInt(nextLine[5]));
                stops.add(toAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stops;
    }

    public Map<String, List<Stop>> readSubways() {
        System.out.println("Reading subways");
        Map<String, List<Stop>> map = new HashMap<>();

        String fileName = "src/main/resources/subte.csv";
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                String line = nextLine[4];
                Stop stop = new Stop(new Point(doubleFromString(nextLine[1]), doubleFromString(nextLine[0])), line);
                map.putIfAbsent(line, new ArrayList<>());
                map.get(line).add(stop);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private double doubleFromString(String str) {
        return Double.parseDouble(str);
    }

    public List<Trip> readSubwayRoutes(Map<String, List<Stop>> map) {
        List<Trip> toReturn = new ArrayList<>();
        for (List<Stop> value : map.values())
            toReturn.add(new Trip(value));

        return toReturn;
    }

    public List<Trip> readRoutes(Graph graph) {
        System.out.println("Reading paths");
        List<Trip> toReturn = new ArrayList<>();
        String fileName = "src/main/resources/recorridos.csv";
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                String line = nextLine[4];
                List<Stop> stops = parseStops(graph, nextLine[14], line, Integer.parseInt(nextLine[1]));
                Trip trip = new Trip(stops);
                toReturn.add(trip);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private List<Stop> parseStops(Graph graph, String toParse, String line, int id) {
        List<Stop> toReturn = new ArrayList<>();
        String aux = toParse.substring(12, toParse.length() - 1);
        String[] arr = aux.split(", ");

        for (String str: arr) {
            String[] pair = str.split(" ");
            Stop stop = new Stop(new Point(doubleFromString(pair[1]),
                    doubleFromString(pair[0])), line, id);

           if (graph.hasStop(stop))
                toReturn.add(stop);
        }

        return toReturn;
    }

}
