package model;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private static final int PENALTY = 10000;
    private static final int WALKABLE_DISTANCE = 500;
    private static final double THRESHOLD = Math.pow(10, -7);
    //List<Node> nodes = new ArrayList<>();
    Map<Stop, Node> stopsMap = new HashMap<>();
    Map<String, List<Node>> lines = new HashMap<>();

    public Graph(List<Stop> stops) {
        for (Stop stop : stops) {
            Node node = new Node(stop);
            stopsMap.put(stop, node);
            lines.putIfAbsent(stop.getLine(), new ArrayList<>());
            lines.get(stop.getLine()).add(node);
            for (Stop s : stopsMap.keySet()) {
                if (s.getLine().equals(stop.getLine())) continue;

                double dist = s.distanceTo(stop);
                if (dist < WALKABLE_DISTANCE)
                    addEdge(stop, s, PENALTY+dist,false);
            }
        }
    }

    /*public void addNode(Stop stop) {
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
    }*/

    private Node getNode(Stop stop) {
        Node aux = stopsMap.get(stop);
        if (aux != null) return aux;

        final Collection<Node> stops = lines.get(stop.getLine());

        if (stops == null)
            return null;

        for (Node node: stops) {
            //if (!node.getStop().getLine().equals(stop.getLine())) continue;
            double a = Math.pow(stop.getLat() - node.stop.getLat(), 2);
            double b = Math.pow(stop.getLon() - node.stop.getLon(), 2);
            double cmp = Math.sqrt(a+b);
            if (cmp <= THRESHOLD)
                return node;
        }

        return null;
    }

    public boolean hasStop(Stop stop) {
        return getNode(stop) != null;
    }

    public void generateEdges(List<Trip> trips, boolean isDirected) {
        for (Trip trip : trips) {
            List<Stop> stops = trip.getStops();
            for (int i = 1; i < stops.size(); i++) {
                Stop s1 = stops.get(i-1);
                Stop s2 = stops.get(i);
                addEdge(s1, s2, s1.distanceTo(s2)/100, isDirected);
            }
        }
    }

    public void addEdge(Stop from, Stop to, double weight, boolean isDirected) {
        Node fromNode = getNode(from);
        Node toNode = getNode(to);
        if (fromNode == null || toNode == null) return;
        fromNode.addEdge(new Edge(weight, toNode));
        if (!isDirected)
            toNode.addEdge(new Edge(weight, fromNode));
    }

    private List<Node> getClosest(double lat, double lon) {
        TreeSet<Node> toReturn = new TreeSet<>();
        Node aux = null;
        double auxDist = Double.MAX_VALUE;
        for (Node node : stopsMap.values()) {
            double d = node.getStop().distanceTo(new Point(lat, lon));
            node.distance = d;
            if (d < WALKABLE_DISTANCE) {
                toReturn.add(node);
            }
            if (d < auxDist) {
                aux = node;
                auxDist = d;
            }
        }
        if (toReturn.size() == 0) toReturn.add(aux);
        return toReturn.stream().limit(20).collect(Collectors.toList()); // Return the 20 closest stops
    }

    public List<BusInPath> findPath(double fromLat, double fromLon, double toLat, double toLon) {
        List<BusInPath> toReturn = null;

        List<Node> from = getClosest(fromLat, fromLon);
        Map<Node, Double> map = new HashMap<>();
        for (Node node : from)
            map.put(node, node.distance);

        //List<Node> to = getClosest(toLat, toLon);
        double toReturnDist = Double.MAX_VALUE;
        for (Node start : from) {
            System.out.println("start: "+start.getStop().getLine());
            //for (Node end : to) {
                //System.out.println("end: "+end.getStop().getLine());
                LinkedList<BusInPath> aux = dijkstra(start, new Point(toLat, toLon), map.get(start));
                for (BusInPath busInPath : aux)
                    System.out.println(busInPath);

                BusInPath last = aux.getLast();
                Node end = getNode(new Stop(new Point(last.toLat, last.toLng), last.name, 0));
                if (end == null) continue;

                double dist = end.distance;
                System.out.println(dist);
                if (dist < toReturnDist) {
                    toReturn = aux; // Me guardo el de menor distancia;
                    toReturnDist = dist;
                }
            //}
        }

        return toReturn;
    }

    private LinkedList<BusInPath> dijkstra(Node start, Point end, double startingDistance) {
        for (Node node : stopsMap.values()) {
            node.distance = Double.MAX_VALUE;
            node.from = null;
            node.visited = false;
        }

        start.distance = startingDistance;

        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(start);

        Node endingNode = null;
        while (queue.size() != 0) {
            Node n = queue.remove();
            if (end.distanceTo(new Point(n.getStop().getLat(), n.getStop().getLon())) < WALKABLE_DISTANCE) {
                endingNode = n;
                break; // Already found best path to end node
            }
            if (n.visited) continue;
            n.visited = true;

            for (Edge edge : n.edges) {
                Node to = edge.to;
                double d = n.distance + edge.weight;
                if (d < to.distance) {
                    to.distance = d;
                    to.from = n;
                    queue.add(to);
                }
            }
        }

        Node current = endingNode;
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

            toReturn.addFirst(new BusInPath(aux.getLine(), aux.getLat(), aux.getLon(), s1.getLat(), s1.getLon()));
            current = prev;
        }

        return toReturn;
    }

    private class Node implements Comparable<Node> {
        Stop stop;
        List<Edge> edges = new ArrayList<>();
        double distance;
        Node from;
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

    private class Edge {
        double weight;
        Node to;

        public Edge(double weight, Node to) {
            this.weight = weight;
            this.to = to;
        }
    }
}
