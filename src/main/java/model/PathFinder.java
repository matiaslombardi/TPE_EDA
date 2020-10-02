package model;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathFinder {

    private final Graph graph;

    public PathFinder(Graph graph) {
       this.graph = graph;
    }

    public List<BusInPath> findPath(double fromLat, double fromLng, double toLat, double toLng) {
        return graph.findPath(fromLat, fromLng, toLat, toLng);
    }

}

