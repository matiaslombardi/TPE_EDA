package model;

import java.util.*;

public class Graph {
    private static final double LEFT = -58.962238;
    private static final double TOP = -34.521517;
    private static final double RIGHT = -57.5;
    private static final double BOTTOM = -35.25;
    private static final int PENALTY = 10;
    private static final double WALKABLE_DISTANCE = 0.0055;
    private static final int ROWS = (int) (Math.abs(TOP - BOTTOM) / WALKABLE_DISTANCE);
    private static final int COLS = (int) (Math.abs(LEFT - RIGHT) / WALKABLE_DISTANCE);
    private static final double THRESHOLD = 0.0005;
    private static final String FINISHED_PATH = "FINISHED";

    private final Grid[][] blocks;
    private final Map<Stop, Node> stopsMap = new HashMap<>();

    public Graph(List<Stop> stops) {
        blocks = new Grid[ROWS + 1][COLS + 1];
        for (Stop stop : stops) {
            if(inRange(stop.getPoint())) {
                Node node = new Node(stop);
                int row = getRow(stop.getPoint());
                int col = getCol(stop.getPoint());
                if (blocks[row][col] == null)
                    blocks[row][col] = new Grid();
                blocks[row][col].add(node);
                stopsMap.put(stop, node);
                List<Node> closest = getClosest(stop.getPoint());
                for (Node n : closest) {
                    Stop s = n.getStop();
                    double dist = s.distanceTo(stop);
                    if (dist < WALKABLE_DISTANCE)
                        addEdge(stop, s, dist, false);
                }
            }
        }
    }

    private Node getNode(Stop stop) {
        Point point = stop.getPoint();
        if(!inRange(point))
            return null;
        Node aux = stopsMap.get(stop);
        if (aux != null) return aux;
        List<Node> nodes = getClosest(stop.getPoint());
        for (Node node : nodes) {
            if (node.getStop().sameLine(stop)) {
                if (aux == null || node.distance < aux.distance) {
                    aux = node;
                }
            }
        }
        if (aux != null && aux.distance < THRESHOLD)
            return aux;
        return null;
    }

    public boolean hasStop(Stop stop) {
        return getNode(stop) != null;
    }

    private void addEdge(Stop from, Stop to, double weight, boolean isDirected) {
        Node fromNode = getNode(from);
        Node toNode = getNode(to);
        if (fromNode == null || toNode == null) return;
        fromNode.addEdge(new Edge(weight, toNode));
        if (!isDirected)
            toNode.addEdge(new Edge(weight, fromNode));
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

    public void generateAllEdges(List<Trip> trips) {
        for (Trip trip : trips) {
            List<Stop> stops = trip.getStops();
            for (int i = 0; i < stops.size() - 1; i++) {
                Stop s1 = stops.get(i);
                for (int j = i+1; j < stops.size(); j++) {
                    Stop s2 = stops.get(j);
                    addEdge(s1, s2, s1.distanceTo(s2), false);
                }
            }
        }
    }

    private List<Node> getClosest(Point point) {
        int row = getRow(point);
        int col = getCol(point);
        Grid aux = blocks[row][col];
        List<Node> toReturn = new ArrayList<>();
        if(aux != null)
            toReturn.addAll(aux.getNodes());
        if(row >= 1){
            if(blocks[row - 1][col] != null)
                toReturn.addAll(blocks[row - 1][col].getNodes());
            if(col >= 1)
                if(blocks[row - 1][col - 1] != null)
                    toReturn.addAll(blocks[row - 1][col - 1].getNodes());
        }
        if(col >= 1)
            if(blocks[row][col - 1] != null)
                toReturn.addAll(blocks[row][col - 1].getNodes());
        if(row <= ROWS){
            if(blocks[row + 1][col] != null)
                toReturn.addAll(blocks[row + 1][col].getNodes());
            if(col <= COLS)
                if(blocks[row + 1][col + 1] != null)
                    toReturn.addAll(blocks[row + 1][col + 1].getNodes());
        }
        if(row >= 1 && col <= COLS){
            if(blocks[row - 1][col + 1] != null)
                toReturn.addAll(blocks[row - 1][col + 1].getNodes());
        }
        if(row <= ROWS && col >= 1){
            if(blocks[row + 1][col - 1] != null)
                toReturn.addAll(blocks[row + 1][col - 1].getNodes());
        }
        if(col <= COLS)
            if(blocks[row][col + 1] != null)
                toReturn.addAll(blocks[row][col + 1].getNodes());

        for (Node node : toReturn) {
            node.distance = point.distanceTo(node.stop.getPoint());
        }
        return toReturn;
    }

    public List<BusInPath> findPath(double fromLat, double fromLon, double toLat, double toLon) {
        Point start = new Point(fromLat, fromLon);
        Point end = new Point(toLat, toLon);
        if (!inRange(start) || !inRange(end)) {
            BusInPath notInRange = new BusInPath("Not in range"
                    , 0, 0 ,0, 0, 0);
            return Collections.singletonList(notInRange);
        }

        if (start.distanceTo(end) < WALKABLE_DISTANCE) {
            BusInPath walking = new BusInPath("Walking"
                    , 0, 0 ,0, 0, 0);
            return Collections.singletonList(walking);
        }

        List<Node> from = getClosest(start);

        if(from.isEmpty()){
            BusInPath noStops = new BusInPath("There are no stops in your surroundings"
                    , 0, 0 ,0, 0, 0);
            return Collections.singletonList(noStops);
        }

        Map<Node, Double> initDist = new HashMap<>();
        for (Node node : from)
            initDist.put(node, node.distance + PENALTY * node.distance);

        return dijkstra(from, end, initDist);
    }

    private LinkedList<BusInPath> dijkstra(List<Node> start, Point end, Map<Node, Double> initDist) {
        for (Node node : stopsMap.values()) {
            node.distance = Double.MAX_VALUE;
            node.from = new LinkedList<>();
            node.visited = false;
        }

        for (Node node : start)
            node.distance = initDist.get(node);

        PriorityQueue<Node> queue = new PriorityQueue<>(start);

        while (!queue.isEmpty()) {
            Node n = queue.remove();
            if (n.visited) continue;
            n.visited = true;

            if(n.getStop().getLine().equals(FINISHED_PATH)) {
                System.out.println("Entre");
                System.out.println(n.from);
                return n.from;
            }

            if (end.distanceTo(n.getStop().getPoint()) < WALKABLE_DISTANCE) {
                Node closePath = new Node(new Stop(end, FINISHED_PATH));
                closePath.distance = n.distance + PENALTY * end.distanceTo(n.stop.getPoint());
                closePath.from = new LinkedList<>(n.from);
                queue.add(closePath);
            }

            for (Edge edge : n.edges) {
                Node to = edge.to;
                double newCost = n.distance + edge.weight;
                if(!to.stop.sameLine(n.stop))
                    newCost += PENALTY + PENALTY * edge.weight;

                if (newCost < to.distance) {
                    to.distance = newCost;
                    to.from = new LinkedList<>(n.from);
                    if(!to.from.isEmpty() && to.from.getLast().samePath(to.stop)){
                        BusInPath path = new BusInPath(to.from.getLast());
                        path.toLat = to.stop.getLat();
                        path.toLng = to.stop.getLon();
                        to.from.removeLast();
                        to.from.addLast(path);
                    } else if (n.stop.sameLine(to.stop)){
                        to.from.addLast(new BusInPath(n.stop.getLine(),
                                n.stop.getLat(), n.stop.getLon(),
                                to.stop.getLat(), to.stop.getLon(),
                                to.stop.getId()));
                    }
                    queue.add(to);
                }
            }
        }
        return new LinkedList<>();
    }

    private int getRow(Point point){
        double lngDist = (point.getLat() - TOP) / WALKABLE_DISTANCE;
        return (int) Math.abs(lngDist);
    }

    private int getCol(Point point){
        double latDist = (point.getLng() - LEFT) / WALKABLE_DISTANCE;
        return (int) Math.abs(latDist);
    }

    private boolean inRange(Point point){
        double lat = point.getLat();
        double lon = point.getLng();
        return !(lat > TOP) && !(lat < BOTTOM) && !(lon < LEFT) && !(lon > RIGHT);
    }

}