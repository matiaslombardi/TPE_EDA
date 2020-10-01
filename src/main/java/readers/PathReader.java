package readers;

import com.opencsv.CSVReader;
import model.BusStop;
import model.Graph;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathReader {

    public Graph readPaths() {
        Graph graph = new Graph();
        System.out.println("Reading stops");
        String fileName = "src/main/resources/paradas-de-colectivo.csv";
        try (FileReader fr = new FileReader(fileName); CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                BusStop toAdd = new BusStop(Double.parseDouble(nextLine[3]),
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
                List<BusStop> stops = parseStops(graph, nextLine[14], line);
                for (int i = 1; i < stops.size(); i++) {
                    BusStop s1 = stops.get(i-1);
                    BusStop s2 = stops.get(i);
                    graph.addEdge(s1, s2, s1.distanceTo(s2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    private List<BusStop> parseStops(Graph graph, String toParse, String line) {
        List<BusStop> toReturn = new ArrayList<>();
        String aux = toParse.substring(12, toParse.length() - 1);
        String[] arr = aux.split(", ");

        for (String str: arr) {
            String[] pair = str.split(" ");
            BusStop stop = new BusStop(Double.parseDouble(pair[1]),
                    Double.parseDouble(pair[0]), line);

            if (graph.hasStop(stop))
                toReturn.add(stop);
        }

        return toReturn;
    }

}
