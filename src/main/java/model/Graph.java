package model;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private static final int PENALTY = 10000;
    List<Node> nodes = new ArrayList<>();
    Map<String, List<Node>> lines = new HashMap<>();

    public void addNode(Stop stop) {
        Node node = new Node(stop);
        String line = stop.getLine();
        nodes.add(node);
        lines.putIfAbsent(line, new ArrayList<>());
        lines.get(line).add(node);
        for (Node n : nodes) {
            Stop s = n.getStop();
            if (s.getLine().equals(stop.getLine()))
                continue;

            double dist = s.distanceTo(stop);
            if (dist <= 500) {
                addEdge(s, stop, PENALTY+dist);
                addEdge(stop, s, PENALTY+dist);
            }
        }
    }

    private Node getNode(Stop stop) {
        List<Node> stops = lines.get(stop.getLine());

        if (stops == null)
            return null;


        for (Node node: stops) {
            double a = Math.pow(stop.getLat() - node.stop.getLat(), 2);
            double b = Math.pow(stop.getLon() - node.stop.getLon(), 2);
            double cmp = Math.sqrt(a+b);
            if (cmp <= 0.00000001)
                return node;
        }

        return null;
    }

    public boolean hasStop(Stop stop) {
        return getNode(stop) != null;
    }

    public void addEdge(Stop from, Stop to, double weight) {
        Node fromNode = getNode(from);
        Node toNode = getNode(to);
        if (fromNode == null || toNode == null) return;
        fromNode.addEdge(new Edge(weight, toNode));
    }

    private Node getClosestNode(double lat, double lon) {
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

    private List<Node> getClosest(double lat, double lon) {
        TreeSet<Node> toReturn = new TreeSet<>();
        Node aux = null;
        double auxDist = Double.MAX_VALUE;
        double maxDist = 500;
        for (Node node : nodes) {
            double d = node.getStop().distanceTo(lat, lon);
            if (d < maxDist) {
                node.weight = d;
                toReturn.add(node);
            }
            if (d < auxDist) {
                aux = node;
                auxDist = d;
            }
        }
        if (toReturn.size() == 0) toReturn.add(aux);
        return toReturn.stream().limit(10).collect(Collectors.toList()); // Return the 10 closest stops
    }

    public List<BusInPath> findPath(double fromLat, double fromLon, double toLat, double toLon) {
        List<BusInPath> toReturn = null;

        List<Node> from = getClosest(fromLat, fromLon);
        List<Node> to = getClosest(toLat, toLon);
        double toReturnDist = Double.MAX_VALUE;
        for (Node start : from) {
            for (Node end : to) {
                List<BusInPath> aux = dijkstra(start, end);
                double dist = end.weight;
                if (dist < toReturnDist) {
                    toReturn = aux; // Me guardo el de menor distancia;
                    toReturnDist = dist;
                }
            }
        }

        return toReturn;
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
            if (n == end) break; // Already found best path to end node
            for (Edge edge : n.edges) {
                Node to = edge.to;
                double d = n.weight + edge.weight;
                if (d < to.weight) {
                    to.weight = d;
                    to.from = n;
                    queue.add(to);
                }
            }
        }

        Node current = end;
        LinkedList<BusInPath> toReturn = new LinkedList<>();
        while (current != start) {
            Stop s1 = current.stop;
            Node prev = current.from;
            Stop s2 = prev.stop;
            if (!s1.getLine().equals(s2.getLine())) {
                current = prev;
                continue;
            }

            Stop aux = s2;
            while (s1.getLine().equals(s2.getLine()) && prev != start) {
                aux = s2;
                prev = prev.from;
                s2 = prev.stop;
            }

            toReturn.addFirst(new BusInPath(s1.getLine(), aux.getLat(), aux.getLon(), s1.getLat(), s1.getLon()));
            current = prev;
        }

        return toReturn;
    }

    private class Node implements Comparable<Node> {
        Stop stop;
        List<Edge> edges = new ArrayList<>();
        double weight;
        Node from;

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
            return Double.compare(weight, o.weight);
        }
    }

    private class Edge {
        double weight;
        Node to;

        public Edge(double weight, Node to) {
            this.weight = weight;
            this.to = to;
        }
    }
}
