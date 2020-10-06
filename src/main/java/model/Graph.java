package model;

import java.util.*;


public class Graph {
    private static final double LEFT = -58.962238;
    private static final double TOP = -34.521517;
    private static final double RIGHT = -57.5;   //-58.339342;
    private static final double BOTTOM = -35.25;     //  -34.788718;
    private static final int PENALTY = 5;
    private static final double WALKABLE_DISTANCE = 0.005;
    private static final int ROWS = (int) (Math.abs(TOP - BOTTOM) / WALKABLE_DISTANCE);
    private static final int COLS = (int) (Math.abs(LEFT - RIGHT) / WALKABLE_DISTANCE);
    private static final double THRESHOLD = 0.0001;

    private final List<Node>[][] blocks;
    //List<Node> nodes = new ArrayList<>();
    Map<Stop, Node> stopsMap = new HashMap<>();
    Map<String, List<Node>> lines = new HashMap<>();


    private int getRow(Point point){
        double lngDist = (point.getLat() - TOP) / WALKABLE_DISTANCE;
        return (int) Math.abs(lngDist);
    }

    private int getCol(Point point){
        double latDist = (point.getLng() - LEFT) / WALKABLE_DISTANCE;
        return (int) Math.abs(latDist);
    }

    @SuppressWarnings("unchecked")
    public Graph(List<Stop> stops) {
        System.out.println(ROWS);
        System.out.println(COLS);
        blocks = (List<Node>[][]) new List[ROWS + 1][COLS + 1];
        for (Stop stop : stops) {
            Node node = new Node(stop);
            int row = getRow(stop.getPoint());
            int col = getCol(stop.getPoint());
            if(blocks[row][col] == null)
                blocks[row][col] = new ArrayList<>();
            blocks[row][col].add(node);
            stopsMap.put(stop, node);
            lines.putIfAbsent(stop.getLine(), new ArrayList<>());
            lines.get(stop.getLine()).add(node);
            for (Stop s : stopsMap.keySet()) {
                //if (s.getLine().equals(stop.getLine())) continue;
                double dist = s.distanceTo(stop);
                if (dist < WALKABLE_DISTANCE)
                    addEdge(stop, s, dist,false);
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
        Point point = stop.getPoint();
        if(point.getLat() < BOTTOM || point.getLat() > TOP
                || point.getLng() < LEFT || point.getLng() > RIGHT)
            return null;
        Node aux = stopsMap.get(stop);
        if (aux != null) return aux;

        int row = getRow(stop.getPoint());
        int col = getCol(stop.getPoint());
        if(blocks[row][col] == null)
            return null;
        for (Node node : blocks[row][col]) {
            if(node.stop.getLine().equals(stop.getLine()) &&
                    node.stop.getPoint().distanceTo(stop.getPoint()) < THRESHOLD) {
                return node;
            }
        }
        return null;
        /*
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
         */
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
                addEdge(s1, s2, s1.distanceTo(s2), isDirected);
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
        Point point = new Point(lat,lon);
        int row = getRow(point);
        int col = getCol(point);
        List<Node> aux = blocks[row][col];
        List<Node> toReturn = new ArrayList<>();
        if(aux != null)
            toReturn.addAll(aux);
        if(row >= 1){
            if(blocks[row - 1][col] != null)
                toReturn.addAll(blocks[row - 1][col]);
            if(col >= 1)
                if(blocks[row - 1][col - 1] != null)
                    toReturn.addAll(blocks[row - 1][col - 1]);
        }
        if(col >= 1)
            if(blocks[row][col - 1] != null)
                toReturn.addAll(blocks[row][col - 1]);
        if(row <= ROWS){
            if(blocks[row + 1][col] != null)
                toReturn.addAll(blocks[row + 1][col]);
            if(col <= COLS)
                if(blocks[row + 1][col + 1] != null)
                    toReturn.addAll(blocks[row + 1][col + 1]);
        }
        if(row >= 1 && col <= COLS){
            if(blocks[row - 1][col + 1] != null)
                toReturn.addAll(blocks[row - 1][col + 1]);
        }
        if(row <= ROWS && col >= 1){
            if(blocks[row + 1][col - 1] != null)
                toReturn.addAll(blocks[row + 1][col - 1]);
        }
        if(col <= COLS)
            if(blocks[row][col + 1] != null)
                toReturn.addAll(blocks[row][col + 1]);
        for (Node node : toReturn) {
            node.distance = point.distanceTo(node.stop.getPoint());
        }
        return toReturn;
        /*
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
         */
    }

    public List<BusInPath> findPath(double fromLat, double fromLon, double toLat, double toLon) {
        List<Node> from = getClosest(fromLat, fromLon);
        Map<Node, Double> initDist = new HashMap<>();
        for (Node node : from) {
            initDist.put(node, node.distance);
        }
        return dijkstra(from, new Point(toLat, toLon), initDist);
    }

    private LinkedList<BusInPath> dijkstra(List<Node> start, Point end, Map<Node, Double> intiDist) {
        for (Node node : stopsMap.values()) {
            node.distance = Double.MAX_VALUE;
            node.from = new LinkedList<>();
            node.visited = false;
        }
        for (Node node : start) {
            node.distance = intiDist.get(node);
        }

        PriorityQueue<Node> queue = new PriorityQueue<>(start);

        while (!queue.isEmpty()) {
            Node n = queue.remove();
            if (n.visited) continue;
            n.visited = true;
            System.out.println(n.stop);
            if(n.getStop().getLine().equals("Llegamos")) {
                System.out.println("ENTREEEE");
                System.out.println(n.from);
                return n.from;
            }

            if (end.distanceTo(n.getStop().getPoint()) < WALKABLE_DISTANCE) {
                Node closePath = new Node(new Stop(end, "Llegamos", 0));
                closePath.distance = n.distance + end.distanceTo(n.stop.getPoint());
                closePath.from = n.from;
                queue.add(closePath);
            }

            for (Edge edge : n.edges) {
                Node to = edge.to;
                double newCost = n.distance + edge.weight;
                if(!to.stop.getLine().equals(n.stop.getLine()))
                    newCost += PENALTY + PENALTY*edge.weight;
                if (newCost < to.distance) {
                    to.distance = newCost;
                    to.from = new LinkedList<>(n.from);
                    if(!to.from.isEmpty() && to.from.getLast().name.equals(to.stop.getLine())){
                        BusInPath path = to.from.getLast();
                        path.toLat = to.stop.getLat();
                        path.toLng = to.stop.getLon();
                    }
                    else if (n.stop.getLine().equals(to.stop.getLine())){
                        to.from.addLast(new BusInPath(n.stop.getLine(),
                                n.stop.getLat(), n.stop.getLon(),
                                to.stop.getLat(), to.stop.getLon()));
                    }
                    queue.add(to);
                }
            }
        }
        return new LinkedList<>();
    }

    private class Node implements Comparable<Node> {
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

    private class Edge {
        double weight;
        Node to;

        public Edge(double weight, Node to) {
            this.weight = weight;
            this.to = to;
        }
    }
}
