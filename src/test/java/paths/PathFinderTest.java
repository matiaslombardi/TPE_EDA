package paths;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class PathFinderTest {

    @Test
    void testPathFinder() {
        List<Stop> stops = new ArrayList<>();
        List<Stop> A_stops = new ArrayList<>();
        List<Stop> B_stops = new ArrayList<>();

        A_stops.add(new Stop(new Point(-34.57, -58.47), "A", 0));
        A_stops.add(new Stop(new Point(-34.58, -58.44), "A", 0));
        A_stops.add(new Stop(new Point(-34.6, -58.41), "A", 0));
        B_stops.add(new Stop(new Point(-34.6, -58.41), "B", 0));
        B_stops.add(new Stop(new Point(-34.62, -58.5), "B", 0));

        Trip A = new Trip(A_stops);
        Trip B = new Trip(B_stops);

        stops.addAll(A_stops);
        stops.addAll(B_stops);

        Graph graph = new Graph(stops);
        List<Trip> trips = new ArrayList<>();
        trips.add(A);
        trips.add(B);

        graph.generateEdges(trips, false);

        //Sin trasbordos
        PathFinder pathFinder = new PathFinder(graph);
        List<BusInPath> l = pathFinder.findPath(-34.57, -58.47,-34.6,-58.41);
        BusInPath[] toCompare = {
                new BusInPath("A",  -34.57, -58.47,-34.6, -58.41, 0)
        };
        Assertions.assertArrayEquals(l.toArray(), toCompare);

        //Haciendo combinación
        l = pathFinder.findPath(-34.57, -58.47,-34.62,-58.5);
        toCompare = new BusInPath[]{
                new BusInPath("A", -34.57, -58.47, -34.6, -58.41, 0),
                new BusInPath("B", -34.6, -58.41, -34.62, -58.5, 0)
        };
        Assertions.assertArrayEquals(l.toArray(), toCompare);

        //Fuera de rango
        l = pathFinder.findPath(-50, -58.47,-34.62,-58.5);
        toCompare = new BusInPath[]{new BusInPath("Not in range", 0, 0 ,0, 0, 0)};
        Assertions.assertArrayEquals(l.toArray(), toCompare);

        //Caminando
        l = pathFinder.findPath(-34.57, -58.47,-34.57,-58.4702);
        toCompare = new BusInPath[]{new BusInPath("Walking", 0, 0 ,0, 0, 0)};
        Assertions.assertArrayEquals(l.toArray(), toCompare);

        //No hay paradas alrededor
        l = pathFinder.findPath(-34.521517, -58.962238,-34.57,-58.4702);
        toCompare = new BusInPath[]{new BusInPath("There are no stops in your surroundings"
                , 0, 0 ,0, 0, 0)};
        Assertions.assertArrayEquals(l.toArray(), toCompare);
    }
}
