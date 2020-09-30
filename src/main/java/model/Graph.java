package model;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    static String WALK = "Caminar";
    List<Node> nodes = new ArrayList<>();
    Map<String, List<Node>> lines = new HashMap<>();

    public void addNode(BusStop stop) {
        Node node = new Node(stop);
        String line = stop.getLine();
        nodes.add(node);
        lines.putIfAbsent(line, new ArrayList<>());
        lines.get(line).add(node);
        for (Node n : nodes) {
            BusStop s = n.getStop();
            if (s.getLine().equals(stop.getLine()))
                continue;

            double dist = s.distanceTo(stop);
            if (dist <= 500) {
                addEdge(s, stop, dist, WALK);
                addEdge(stop, s, dist, WALK);
            }
        }
    }

    private Node getNode(BusStop stop) {
        List<Node> stops = lines.get(stop.getLine());

        if (stops == null)
            return null;

        Node aux = null;
        for (Node node: stops) {
            double cmp = Math.abs(stop.getLat() - node.stop.getLat());
            if (cmp <= 0.0000001)
                aux = node;
        }

        return aux;
    }

    private List<BusInPath> dijkstra(Node start, Node end) {
        for (Node node : nodes) {
            node.weight = Double.MAX_VALUE;
            node.from = null;
        }

        start.weight = 0;

        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(start);

        while (queue.size() != 0) {
            Node n = queue.remove();
            for (Edge edge : n.edges) {
                double d = n.weight + edge.weight;
                if (d < edge.to.weight) {
                    edge.to.weight = d;
                    edge.to.from = n;
                    queue.add(edge.to);
                }
            }
        }

        Node aux = end;
        List<BusInPath> toReturn = new ArrayList<>();
        while (aux != start) {
            Node aux2 = aux.from;
            BusStop s1 = aux.stop;
            BusStop s2 = aux2.stop;
            String mode = s1.getLine();
            if (!s1.getLine().equals(s2.getLine())) {
                for (Edge edge : aux2.edges) {
                    if (edge.to == aux)
                        mode = edge.mode;
                }
            }
            toReturn.add(new BusInPath(mode, s1.getLat(), s1.getLon(), s2.getLat(), s2.getLon()));
            aux = aux.from;
        }
        return toReturn;
    }


    public void addEdge(BusStop from, BusStop to, double weight, String mode) {
        Node fromNode = getNode(from);
        Node toNode = getNode(to);
        if (fromNode == null || toNode == null) return;
        fromNode.addEdge(new Edge(weight, toNode, mode));
    }

    private Node getClosest(double lat, double lon) {
        Node aux = null;
        double auxDist = Double.MAX_VALUE;
        for (Node node : nodes) {
            double d = node.getStop().distanceTo(lat, lon);
            if (d < auxDist) {
                aux = node;
                auxDist = d;
            }
        }
        return aux;
    }

    public List<BusInPath> findPath(double fromLat, double fromLon, double toLat, double toLon) {
        Node from = getClosest(fromLat, fromLon);
        Node to = getClosest(toLat, toLon);
        return dijkstra(from, to);
    }

    private class Node implements Comparable<Node> {
        BusStop stop;
        List<Edge> edges = new ArrayList<>();
        double weight;
        Node from;

        public Node(BusStop stop) {
            this.stop = stop;
        }

        public void addEdge(Edge edge) {
            edges.add(edge);
        }

        public BusStop getStop() {
            return stop;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(weight, o.weight);
        }
    }

    private class Edge {
        double weight;
        String mode;
        Node to;

        public Edge(double weight, Node to, String mode) {
            this.weight = weight;
            this.to = to;
            this.mode = mode;
        }
    }
}
