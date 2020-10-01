package readers;

import com.opencsv.CSVReader;
import model.Stop;
import model.Graph;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathReader {

    public Graph readPaths() {
        Graph graph = new Graph();
        System.out.println("Reading stops");
        String fileName = "src/main/resources/paradas-de-colectivo.csv";
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                Stop toAdd = new Stop(Double.parseDouble(nextLine[3]),
                        Double.parseDouble(nextLine[4]), nextLine[8]);
                graph.addNode(toAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading paths");
        fileName = "src/main/resources/recorridos.csv";
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                String line = nextLine[4];
                List<Stop> stops = parseStops(graph, nextLine[14], line);
                for (int i = 1; i < stops.size(); i++) {
                    Stop s1 = stops.get(i-1);
                    Stop s2 = stops.get(i);
                    graph.addEdge(s1, s2, s1.distanceTo(s2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading subways");
        fileName = "src/main/resources/subte.csv";
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            Map<String, List<Stop>> map = new HashMap<>();
            while ((nextLine = reader.readNext()) != null) {
                String line = nextLine[4];
                Stop stop = new Stop(Double.parseDouble(nextLine[1]), Double.parseDouble(nextLine[0]), line);
                graph.addNode(stop);
                map.putIfAbsent(line, new ArrayList<>());
                map.get(line).add(stop);
            }

            for (String l : map.keySet()) {
                List<Stop> aux = map.get(l);
                for (int i = 1; i < aux.size(); i++) {
                    Stop s1 = aux.get(i-1);
                    Stop s2 = aux.get(i);
                    double dist = s1.distanceTo(s2);
                    graph.addEdge(s1, s2, dist);
                    graph.addEdge(s2, s1, dist);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    private List<Stop> parseStops(Graph graph, String toParse, String line) {
        List<Stop> toReturn = new ArrayList<>();
        String aux = toParse.substring(12, toParse.length() - 1);
        String[] arr = aux.split(", ");

        for (String str: arr) {
            String[] pair = str.split(" ");
            Stop stop = new Stop(Double.parseDouble(pair[1]),
                    Double.parseDouble(pair[0]), line);

            if (graph.hasStop(stop))
                toReturn.add(stop);
        }

        return toReturn;
    }

}
