package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable<Node> {
    Stop stop;
    List<Edge> edges = new ArrayList<>();
    double distance;
    LinkedList<BusInPath> from;
    boolean visited;
    public Node(Stop stop) {
        this.stop = stop;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public Stop getStop() {
        return stop;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(distance, o.distance);
    }
}
